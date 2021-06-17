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
package rubensandreoli.commons.swing.filters;

public abstract class AbstractFieldFilter implements FieldFilter {
    
    private String defaultText, replacementText;
    
    @Override
    public boolean shouldAdd(String text, String add, int offset, int length){
        if(add.isEmpty()) return true; //clearing the field setText(""), no need to proceed; move to field?
        final StringBuilder sb = new StringBuilder(text);
        if(length > 0){
            sb.replace(offset, offset+length, add);
        }else{
            sb.insert(offset, add);
        }
        return isValid(sb.toString());
    }
    
    @Override
    public boolean shouldRemove(String text, int offset, int length){
        if(text.length() == length) return true; //clearing the field, no need to proceed; move to field?
        final StringBuilder sb = new StringBuilder(text);
        sb.delete(offset, offset+length);
        return isValid(sb.toString());
    }
    
    protected abstract boolean isValid(String text);

    @Override
    public String formatFocusLost(String text) {
        if(defaultText != null && text.isBlank()){
            return defaultText;
        }
        return null;
    }

    @Override
    public String retrieveReplacementText(){
        final String text = replacementText;
        replacementText = null;
        return text;
    }

    protected void setReplacementText(String replacementText) {
        this.replacementText = replacementText;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public AbstractFieldFilter setDefaultText(String defaultText) {
        if(!isValid(defaultText)) throw new IllegalArgumentException("default text was not accepted by the filter");
        this.defaultText = defaultText;
        return this;
    }
  
}
