/*
 * Copyright (C) 2020 Morus.
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
package rubensandreoli.commons.utils;

public class NumberUtils {
    
    /**
     * Parses the string argument as a signed decimal integer.<br>
     * Convenience method for {@code Integer::parseInt} that doesn't<br>
     * throw {@code NumberFormatException}.
     * 
     * @param value a {@code String} containing the {@code int} to be parsed
     * @return the integer value or {@literal 0} if an exception is thrown
     */
    public static int parseInteger(String value){
        try{
            return Integer.parseInt(value);
        }catch(NumberFormatException ex){
            return 0;
        }
    }
    
    /**
     * Parses the string argument as a signed double.<br>
     * Convenience method for {@code Double::parseDouble} that doesn't throw<br>
     * {@code NumberFormatException}, and supports multiple number formatting.
     * 
     * @param value a {@code String} containing the {@code double} to be parsed
     * @return the double value or {@literal 0} if an exception is thrown
     */
    public static double parseDouble(String value){
        final int dotIndex = value.indexOf('.');
        final int commaIndex = value.indexOf(',');
        if(commaIndex >= 0){
            if(dotIndex >= 0){
                if(dotIndex < commaIndex){ //1.000.000,12
                    value = value.replaceAll("\\.", "").replaceAll(",", ".");
                }else{ //1,000,000.12
                    value = value.replaceAll(",", "");
                }
            }else{ //100,12
                value = value.replaceAll(",", ".");
            }
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public static int getRandomBetween(int min, int max){
        if(max < min) max = min;
        return (int) (Math.random() * (max - min)) + min;
    }
    
}
