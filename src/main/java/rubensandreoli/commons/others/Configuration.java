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
package rubensandreoli.commons.others;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import rubensandreoli.commons.exceptions.CastException;
import rubensandreoli.commons.utils.BooleanUtils;

/**
 * Used to register configuration key-value pairs into a {@code xml} file. <br>
 * This file is generated, if it doesn't exist, on the same folder as the application.<br>
 * When trying to access an entry, if it can't be found, or the value set doesn't comply to the limits set,<br>
 * or the value appear to be set incorrectly, the default value provided will be saved and used instead.
 * 
 * @author Rubens A. Andreoli Jr.
 */
public class Configuration {
    
    public static final String FILENAME = "config.xml";
    
    public static final Configuration values = new Configuration(); //eager initialization;

    private final Properties p;
    private File file;
    private boolean changed;
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    private Configuration(){
        p = new Properties();
        load();
    }
    
    public boolean load(){
        file = new File(FILENAME);
        if(file.isFile()){
            try(var bis = new BufferedInputStream(new FileInputStream(file))){
                p.loadFromXML(bis);
                return true;
            } catch (IOException ex) {
                Logger.log.print(Level.ERROR, "failed loading config file", ex);
            }
        }
        return false;
    }

    public String get(String key, String defaultValue){
        String v = p.getProperty(key);
        if(v == null){
            put(key, defaultValue);
            v = defaultValue;
        }
        return v;
    }
    
    public Integer get(String key, int defaultValue){
        final String v = get(key, String.valueOf(defaultValue));
        try{
            return Integer.parseInt(v);
        }catch(NumberFormatException ex){
            return defaultValue;
        }
    }
    
    public Integer get(String key, int defaultValue, int lowerBound){
        int v = get(key, defaultValue);
        if(v<lowerBound) v = defaultValue;
        return v;
    }
    
    public Integer get(String key, int defaultValue, int lowerBound, int upperBound){
        int v = get(key, defaultValue);
        if(v<lowerBound || v>upperBound) v = defaultValue;
        return v;
    }
    
    public Double get(String key, double defaultValue){
        final String v = get(key, String.valueOf(defaultValue));
        try{
            return Double.parseDouble(v);
        }catch(NumberFormatException ex){
            return defaultValue;
        }
    }
    
    public Double get(String key, double defaultValue, double lowerBound){
        double v = get(key, defaultValue);
        if(v<lowerBound) v = defaultValue;
        return v;
    }
    
    public Double get(String key, double defaultValue, double lowerBound, double upperBound){
        double v = get(key, defaultValue);
        if(v<lowerBound || v>upperBound) v = defaultValue;
        return v;
    }
    
    public Boolean get(String key, boolean defaultValue) {
        final String v = get(key, String.valueOf(defaultValue));
        try{
            return BooleanUtils.parseBoolean(v);
        }catch(CastException ex){
            return defaultValue;
        }
    }
    
    public void put(String key, String value) {
        p.put(key, value);
        changed = true;
    }

    /**
     * Save configuration key-values pairs to a {@code xml} file, if an entry was added or changed.
     * 
     * @see Configuration#save(String)
     * @return {@code true} if and only if the values were saved;<br>
     *         {@code false} if an exception occurred or there was no changes to be saved
     */
    public boolean save(){
        return save(null);
    }
    
    /**
     * Save configuration key-values pairs to a {@code xml} file, if an entry was added or changed.
     * 
     * @param title a title for the entries; or {@code null} if no title is desired
     * @return {@code true} if and only if the values were saved;<br>
     *         {@code false} if an exception occurred or there was no changes to be saved
     */
    public boolean save(String title){
        if(changed){
            try(var bos = new BufferedOutputStream(new FileOutputStream(new File(FILENAME)))){
                p.storeToXML(bos, title);
                return true;
            } catch (IOException ex) {
                Logger.log.print(Level.ERROR, "failed saving config file", ex);
            }
        }
        return false;
    }
    
    public boolean hasChanged(){
        return changed;
    }

    /**
     * Tests if the configuration file exists.
     * 
     * @return {@code true} if and only if the configuration file exists;<br>
     *         {@code false} otherwise
     */
    public boolean exists() {
        return file.isFile();
    }

}
