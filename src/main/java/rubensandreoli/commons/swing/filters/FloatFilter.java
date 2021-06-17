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

public class FloatFilter extends RangeFilter<Float>{

    private static final int MIN_DECIMAL_LIMIT = 1;
    
    private Float value;
    private int decimalLimit;

    public FloatFilter() {}

    public FloatFilter(float minValue, float maxValue) {
        setInterval(minValue, maxValue);
    }

    public FloatFilter(float minValue, float maxValue, int decimalLimit) {
        this(minValue, maxValue);
        if(decimalLimit < MIN_DECIMAL_LIMIT) throw new IllegalArgumentException("decimal places < "+MIN_DECIMAL_LIMIT);
        this.decimalLimit = decimalLimit;
    }

    @Override
    public boolean shouldAdd(String text, String add, int offset, int length) {
        if("f".equals(add) || "d".equals(add)) return false;
        return super.shouldAdd(text, add, offset, length);
    }  
    
    @Override
    protected boolean isValidNumber(String text) {
        try{
            value = Float.parseFloat(text);
            if(decimalLimit != 0){
                final int index = text.indexOf(".");
                if(index != -1 && (text.length() - index) > decimalLimit+1) return false;
            }
            return true;
        }catch(Exception e){
            value = null;
            return false;
        }
    }

    @Override
    protected Float getValue() {
        return value;
    }

    public FloatFilter setDecimalLimit(int maxPlaces) {
        this.decimalLimit = decimalLimit;
        return this;
    }
    
    
    
}
