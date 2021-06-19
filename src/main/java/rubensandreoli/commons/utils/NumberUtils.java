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

public final class NumberUtils {
    
    private NumberUtils(){}
    
    /**
     * Parses the {@code String} argument as a signed decimal integer.<br>
     * Convenience method for {@code Integer::parseInt} that doesn't
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
     * Parses the {@code String} argument as a signed {@code double}.<br>
     * Convenience method for {@code Double::parseDouble} that doesn't throw
     * {@code NumberFormatException}, and supports multiple number formatting.
     * 
     * @param value a {@code String} containing the {@code double} to be parsed
     * @return the {@code double} value or {@literal 0} if an exception is thrown
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

    /**
     * Generates a random integer between two given values.
     * 
     * @param min lower bound
     * @param max upper bound; must be bigger than the lower bound
     * @return a random {@code int} between the bounds set
     * @throws IllegalArgumentException if the higher bound is smaller or equal to the lower bound
     */
    public static int getRandomBetween(int min, int max){
        if(max <= min) throw new IllegalArgumentException("parameter max value must be bigger than min");
        return (int) (Math.random() * (max - min)) + min;
    }

    /**
     * Tests if two {@code double} values are equal within a given tolerance.
     * 
     * @param d1 first {@code double} to compare
     * @param d2 second {@code double} to compare
     * @param epsilon tolerance
     * @return {@code true} if and only if the {@code double} values are equal considering the tolerance given;<br>
     *         {@code false} otherwise
     */
    public static boolean equalsDouble(double d1, double d2, double epsilon){
        return Math.abs(d1 - d2) <= epsilon;
    }

}
