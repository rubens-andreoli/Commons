/*
 * Copyright (C) 2020 Rubens A. Andreoli Jr..
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 * A text field that accepts only one key at a time, and displays a {@code String} describing said key.
 * 
 * @author Rubens A. Andreoli Jr.
 */
public class KeyField extends javax.swing.JTextField{
    private static final long serialVersionUID = 1L;

    private int key;
    private boolean write, pressed;
    private Set<Integer> blacklist;
    
    public KeyField() {
        ((PlainDocument) getDocument()).setDocumentFilter(new DocumentFilter(){
            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if(write) super.replace(fb, offset, length, text, attrs);
            }
 
            public @Override void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {}           
            public @Override void remove(DocumentFilter.FilterBypass fb, int offset, int length) throws BadLocationException {}
        });
                
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {               
                if(!pressed){
                    pressed = true;
                    final int tmpKey = e.getExtendedKeyCode();
                    if(blacklist == null || !blacklist.contains(tmpKey)) setText(tmpKey);
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                pressed = false;
            }
        });
        
        setHorizontalAlignment(JTextField.CENTER);
    }

    /**
     * Clears the field text and resets the associated key code;
     * 
     * @see JTextField#setText(String)
     */
    public void clear(){
        key = 0;
        setText(null);
    }

    /**
     * Gets the extended key code for the keyboard key set.
     * 
     * @return {@code 0} if no key is set;<br>
     *         a positive integer otherwise
     */
    public int getKey() {
        return key;
    }

    /**
     * This method won't set the key code just the text to be displayed.<br>
     * A {@code null} parameter or an empty {@code String} will also not reset the key code, only the text.<br>
     * Use of this method should be avoided.
     * 
     * @see KeyField#setText(int)
     * @see KeyField#clear()
     * @param t {@code String} displayed by the field
     */
    @Override
    public void setText(String t) {
        write = true;
        super.setText(t);
        write = false;
    }
    
    /**
     * Sets the key code and text displayed by this field.<br>
     * Key codes with an Unknown string representation will be ignored.
     * 
     * @param keyCode integer value representing a keyboard key
     */
    public void setText(int keyCode){
        clear();
        final String t = KeyEvent.getKeyText(keyCode);
        if(!t.startsWith("Unk")){
            key = keyCode;
            setText(t);
        }
    }

    public void addBlacklisted(int...keyCode){
        if(blacklist == null) blacklist = new HashSet<>();
        for (int i : keyCode) blacklist.add(i);
    }
    
    public void removeBlacklisted(int keyCode){
        if(blacklist != null){
            blacklist.remove(keyCode);
            if(blacklist.isEmpty()) blacklist = null;
        }
    }
    
    public void clearBlacklist(){
        blacklist = null;
    }
    
    public Set<String> getBlacklistNames(){
        if(blacklist == null) return null;
        final Set<String> names = new HashSet<>();
        blacklist.forEach(keyCode -> names.add(KeyEvent.getKeyText(keyCode)));
        return names;
    }
    
    public Set<Integer> getBlacklist(){
        return blacklist;
    }
    
}
