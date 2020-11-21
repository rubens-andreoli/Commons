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
import javax.swing.JTextField;

public class KeyField extends javax.swing.JTextField{
    private static final long serialVersionUID = 1L;

    private int key;
    
    public KeyField() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                clear();
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                key = e.getKeyCode();
                e.consume();
                setText(key);
            }
        });
        
        setHorizontalAlignment(JTextField.CENTER);
    }
    
    public void clear(){
        setText("");
    }

    public int getKey() {
        return key;
    }

    @Override
    public void setText(String t) {
        if(t.startsWith("Unk")) t = "Unknown";
        super.setText(t);
    }
    
    public void setText(int keyCode){
        setText(KeyEvent.getKeyText(keyCode));
    }
    
}
