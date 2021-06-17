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
package rubensandreoli.commons.others;

/**
 * References:<br>
 * 
 * https://stackoverflow.com/questions/2683202/comparing-the-values-of-two-generic-numbers/12884075
 * 
 * @author Rubens A. Andreoli Jr.
 * @param <T> the type o values this object may hold
 */
public class Range<T extends Number & Comparable>{

    private T min, max;
    
    public Range(T min, T max) {
        if(min.compareTo(max) >= 0) 
            throw new IllegalArgumentException("lower bound greater than upper: "+min+" >= "+max);
        this.min = min;
        this.max = max;
    }
    
    public boolean contains(T val){
        return val.compareTo(min) >= 0 && val.compareTo(max) <= 0;
    }
    
    //-2 -1 0 +1 +2
    public int locate(T val){
        return val.compareTo(min) + val.compareTo(max);
    }
    
    public boolean greaterThan(T val){
        return val.compareTo(min) < 0;
    }
    
    public boolean smallerThan(T val){
        return val.compareTo(max) > 0;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }
    
}
