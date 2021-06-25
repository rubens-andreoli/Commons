/*
 * Copyright (C) 2021 Rubens A. Andreoli Jr.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package rubensandreoli.commons.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * References:<br>
 * https://stackoverflow.com/questions/2683202/comparing-the-values-of-two-generic-numbers/12884075<br>
 * https://stackoverflow.com/questions/3622402/how-to-intercept-keyboard-strokes-going-to-java-swing-jtextfield<br>
 * https://stackoverflow.com/questions/4863850/disable-input-some-symbols-to-jtextfield<br>
 * https://stackoverflow.com/questions/18084104/accept-only-numbers-and-a-dot-in-java-textfield<br>
 * https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers<br>
 * https://stackoverflow.com/questions/24844559/jtextfield-using-document-filter-to-filter-integers-and-periods
 * 
 * @author Rubens A. Andreoli Jr
 * @param <T> 
 */
public abstract class IntervalField<T extends Number & Comparable> extends javax.swing.JTextField{

    private T minValue, maxValue;
    private T defaultValue, value;
    private PlainDocument document;
    private boolean bypass;
    
    public IntervalField(T min, T max) {
        this.minValue = min;
        this.maxValue = max;
        document = (PlainDocument) getDocument();
        document.setDocumentFilter(new DocumentFilter(){
            @Override //length > 0 if replacing value; length = 0 if inserting value; length > 0 and text.isEmpty if clearing
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String add, AttributeSet attrs) throws BadLocationException {
                final int textLength = getText().length();
                if(add.isEmpty() || //clearing the field
                        (offset == 0 && "-".equals(add) && minValue.compareTo(0) < 0 && (textLength == 0 || textLength == length))){ //negative sign
                    fb.replace(offset, length, add, attrs);
                }else{
                    if(validate(fb, parseValue(buildAdd(add, offset, length)), attrs)) 
                        fb.replace(offset, length, add, attrs);
                }
            }

            @Override //called by Document.insertString(...)
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String add, AttributeSet attr) throws BadLocationException {
                if(bypass) fb.replace(offset, getText().length(), add, attr);
                else this.replace(fb, offset, 0, add, attr);
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                if(length == getText().length() || '-' == getText().charAt(0)){ //clearing the field or only negative sign
                    fb.remove(offset, length);
                }else{
                    if(validate(fb, parseValue(buildRemove(offset, length)), null)) 
                        fb.remove(offset, length);
                }
            }
            
            private String buildAdd(String add, int offset, int length) {
                final StringBuilder sb = new StringBuilder(getText());
                if(length > 0){
                    sb.replace(offset, offset+length, add);
                }else{
                    sb.insert(offset, add);
                }
                return sb.toString();
            }
            
            private String buildRemove(int offset, int length){
                final StringBuilder sb = new StringBuilder(getText());
                sb.delete(offset, offset+length);
                return sb.toString();
            }
    
            private void replaceText(DocumentFilter.FilterBypass fb, T newValue, AttributeSet attrs) throws BadLocationException{
                value = newValue;
                fb.replace(0, getText().length(), String.valueOf(newValue), attrs);
            }
            
            private boolean validate(DocumentFilter.FilterBypass fb, T newValue, AttributeSet attrs) throws BadLocationException{
                if(newValue != null){
                    if(newValue.compareTo(minValue) < 0) replaceText(fb, minValue, attrs);
                    else if(newValue.compareTo(maxValue) > 0) replaceText(fb, maxValue, attrs);
                    else{
                        value = newValue;
                        return true;
                    }
                }
                return false;
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                final int textLength = getText().length();
                if(textLength == 0 || (textLength == 1 && getText().charAt(0) == '-')){
                    if(defaultValue != null){
                        bypass(String.valueOf(defaultValue));
                        value = defaultValue;
                    }else if(value != null){
                        bypass(String.valueOf(value));
                    }else{
                        setText("");
                    }
                }else{
                    bypass(formatValue(value));
                }
                super.focusLost(e);
            }  
            
            private void bypass(String number){
                bypass = true; //solution for not going throw filter again with setText()
                try {
                    document.insertString(0, number, null);
                } catch (BadLocationException ex) {} //never suppose to happen
                bypass = false;
            }
            
        });
    }
    
    protected abstract T parseValue(String number);
    protected abstract String formatValue(T value);

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        setText(String.valueOf(value)); //value set by the filter if valid
    }

    public void setInterval(T min, T max){
        if(min.compareTo(max) > 0) throw new IllegalArgumentException("min value must be <= max");
        this.minValue = min;
        this.maxValue = max;
    }

    public void setDefaultValue(T defaultValue) {
        if(defaultValue.compareTo(maxValue) > 0 || defaultValue.compareTo(minValue) < 0) 
            throw new IllegalArgumentException("default value must be >= min and <= max");
        this.defaultValue = defaultValue;
    }  

}
