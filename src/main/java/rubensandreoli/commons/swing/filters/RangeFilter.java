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

import rubensandreoli.commons.others.Range;

public abstract class RangeFilter<T extends Number & Comparable> extends AbstractFieldFilter {
    
    protected Range<T> range;

    @Override
    protected boolean isValid(String text) {
        if("-".equals(text)) return true;
        if(isValidNumber(text)){
            final T val = getValue();
            if(val == null) 
                throw new IllegalStateException("filtered result was allowed but parsed value was set as null");
            return resolveRange(val);
        }
        return false;
    }
    
    protected abstract boolean isValidNumber(String text);
    protected abstract T getValue();
 
    protected boolean resolveRange(T val){
        if(range == null) return true;
        final int position = range.locate(val);
        if(position < -1){
            setReplacementText(String.valueOf(range.getMin()));
            return false;
        }
        if(position > 1){
            setReplacementText(String.valueOf(range.getMax()));
            return false;
        }
        return true;
    }

    @Override
    public String formatFocusLost(String text) {
        if(text.isBlank() || "-".equals(text)){
            if(getDefaultText() != null){
                return getDefaultText();
            }
            else if(range != null){
                return String.valueOf(range.getMin());
            }
        }
        return String.valueOf(getValue());
    }
 
    public void setInterval(T minValue, T maxValue) {
         range = new Range<>(minValue, maxValue);
    }
    
    public void setRange(Range range){
        this.range = range;
    }
    
    public Range getRange(){
        return range;
    }

}
