/*
 * Copyright (C) 2020 Rubens A. Andreoli Jr.
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

import rubensandreoli.commons.exceptions.CastException;

public final class BooleanUtils {
    
    private BooleanUtils(){};
    
    /**
     * Parses the {@code String} argument as a {@code boolean}.<br>
     * The {@code boolean} returned represents the value {@code true} if the {@code String} argument
     * is equal, ignoring case, to the {@code String} "true", or {@code false} if the {@code String} is equal, 
     * ignoring case "false".
     * 
     * @param s the {@code String} containing the {@code boolean} representation to be parsed
     * @return the {@code boolean} value
     * @throws CastException if the {@code String} is neither "true" nor "false", ignoring case
     */
    public static boolean parseBoolean(String s) throws CastException {
        switch(s.toLowerCase()){
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new CastException();
        }
    }
    
    /**
     * Generates a random {@code boolean}.
     * 
     * @return the generated {@code boolean} value
     */
    public static boolean generateBoolean(){
        return Math.random() < 0.5;
    }

    /**
     * Generates a random {@code boolean} with the given odds.
     * 
     * @param ratio chances of returning {@code true}, between 0 and 1.0
     * @return the generated {@code boolean} value
     */
    public static boolean generateBoolean(double ratio){
        if(ratio <= 0.0) return false;
        if(ratio >= 1.0) return true;
        return Math.random() < ratio;
    }

}
