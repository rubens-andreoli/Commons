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

public class IntegerField extends IntervalField<Integer>{

    public IntegerField(){
        this(0,0);
    }
    
    public IntegerField(Integer min, Integer max) {
        super(min, max);
    }

    @Override
    protected Integer parseValue(String number) {
        try{
            return Integer.parseInt(number);
        }catch(NumberFormatException ex){
            return null;
        }
    }

    @Override
    protected String formatValue(Integer value) {
        return String.valueOf(value);
    }

}
