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

public class IntegerFilter extends RangeFilter<Integer>{

    private Integer value;
    
    public IntegerFilter() {}

    public IntegerFilter(int minValue, int maxValue){
        super.setInterval(minValue, maxValue);
    }

    @Override
    protected boolean isValidNumber(String text) {
        try{
            value = Integer.parseInt(text);
            return true;
        }catch(NumberFormatException e){
            value = null;
            return false;
        }    
    }

    @Override
    protected Integer getValue() {
        return value;
    }
   
}
