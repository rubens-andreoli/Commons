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
import rubensandreoli.commons.swing.filters.FieldFilter;

/**
 * References:<br>
 * https://stackoverflow.com/questions/3622402/how-to-intercept-keyboard-strokes-going-to-java-swing-jtextfield<br>
 * https://stackoverflow.com/questions/4863850/disable-input-some-symbols-to-jtextfield<br>
 * https://stackoverflow.com/questions/18084104/accept-only-numbers-and-a-dot-in-java-textfield<br>
 * https://stackoverflow.com/questions/11093326/restricting-jtextfield-input-to-integers<br>
 * https://stackoverflow.com/questions/24844559/jtextfield-using-document-filter-to-filter-integers-and-periods
 * 
 * @author Rubens A. Andreoli Jr.
 */
public class FilteredField extends javax.swing.JTextField{

    private FieldFilter filter;
    private final PlainDocument document;
    private boolean bypass;
    
    public FilteredField() {
        document = (PlainDocument) getDocument();
        document.setDocumentFilter(new DocumentFilter(){
            
            //length > 0 if replacing value; length = 0 if inserting value;
            //length > 0 and text.isEmpty if clearing
            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (filter != null){
                    if(filter.shouldAdd(getText(), text, offset, length)){
                        fb.replace(offset, length, text, attrs);
                    }else{
                        final String replacement = filter.retrieveReplacementText();
                        if(replacement != null) fb.replace(0, getText().length(), replacement, attrs);
                    }
                }else{
                    fb.replace(offset, length, text, attrs);
                }
            }

            @Override
            public void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {
                if (filter != null){
                    if(filter.shouldRemove(getText(), offset, length)){
                        fb.remove(offset, length);
                    }else{
                        final String replacement = filter.retrieveReplacementText();
                        if(replacement != null) fb.replace(0, getText().length(), replacement, null);
                    }
                }else{
                    fb.remove(offset, length);
                }
            }

            @Override //called by Document.insertString(...)
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                if(bypass) fb.replace(offset, getText().length(), text, attr);
                else this.replace(fb, offset, 0, text, attr);
            }
        });
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if(filter != null){
                    final String replacement = filter.formatFocusLost(getText());
                    if(replacement != null){
                        if(replacement.isEmpty()) setText("");
                        else{
                            bypass = true; //solution for not going throw filter again with setText()
                            try {
                                document.insertString(0, replacement, null);
                            } catch (BadLocationException ex) {}
                            bypass = false;
                        }
                    }
                }
                super.focusLost(e);
            }
            
        });
        
    }

    public void setFilter(FieldFilter filter) {
        this.filter = filter;
    }

}
