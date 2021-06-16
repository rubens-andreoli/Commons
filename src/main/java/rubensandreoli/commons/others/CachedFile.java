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

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import rubensandreoli.commons.utils.FileUtils;
import rubensandreoli.commons.utils.StringUtils;

/**
 * Extends {@code File} too provide an alternative that reduces disk access or 
 * repetitive processing by keeping previous sought information cached.<br>
 * This class may cause memory leaks if not properly handled, use it with care.
 * It may also keep information non longer relevant if the referenced file was changed.
 * 
 * @author Rubens A. Andreoli Jr.
 */
public class CachedFile extends File{ //TODO: keep this class or not?
    private static final long serialVersionUID = 1L;
 
    public static final int SIGNATURE_BYTES = 4;
    
    private Long size;
    private String parent, filename, name, extension;
    private byte[] signature;
    private byte[] content;
    
    public CachedFile(String pathname){
        super(pathname);
    }
    
    public CachedFile(File file){
        this(file.getPath());
    }
    
    public CachedFile(String root, String...nodes){
        super(FileUtils.buildPathname(root, nodes));
    }
    
    public CachedFile(File root, String...nodes) {
        super(FileUtils.buildPathname(root, nodes));
    }

    public CachedFile(URI uri) {
        super(uri);
    }

    /**
     * Deletes the file or directory denoted by this abstract pathname.<br>
     * If this pathname denotes a directory, then the directory must be empty in order to be deleted.<br>
     * Note that this override does not throws an {@code SecurityException}.
     * 
     * @return {@code true} if and only if the file or directory is successfully deleted<br>
     *         {@code false} otherwise
     */
    @Override
    public boolean delete(){
        //don't use FileUtils#deleteFile or it will generate a circular reference
        boolean removed = false;
        try{
            removed = super.delete();
        }catch(SecurityException ex){}
        return removed;
    }

    public boolean matchSignature(byte...bytes){
        if(signature == null) return bytes == null;
        if(bytes.length == 0 && signature.length == 0) return true;
        final int lenght = Math.min(bytes.length, signature.length);
        return Arrays.compare(bytes, 0, lenght, signature, 0, lenght) == 0;
    }
    
    public File toFile(){
        return new File(getPath());
    }
  
    // <editor-fold defaultstate="collapsed" desc=" GETTERS "> 
    @Override
    public long length() {
        if(size == null) size = FileUtils.getFileSize(this);
        return size;
    }

    @Override
    public String getParent() {
        if(parent == null) parent = super.getParent();
        return parent;
    }
    
    public CachedFile getParentCachedFile(){
        return new CachedFile(this.getParent());
    }
    
    @Override
    public String getName() {
        if(name == null) name = super.getName();
        return name;
    }
    
    public String getExtension() {
        if(extension == null) extension = FileUtils.getExtension(getPath());
        return extension;
    }
    
    public String getFilename() {
        if(filename == null) filename = FileUtils.getFilename(getPath());
        return filename;
    }
 
    public byte[] getRawSignature() {
        if(signature == null) signature = FileUtils.readFirstBytes(this, SIGNATURE_BYTES);
        return signature;
    }
    
    public String getSignature() {
        return new String(getRawSignature());
    }
    
    public byte[] getContent() {
        if(content == null){
            content = FileUtils.readAllBytes(this);
            size = (long) content.length;
        }
        return content;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" SETTERS "> 
    public void setSize(long size) {
        this.size = size;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
    
    public void setContent(byte[] content) {
        this.content = content;
    }
    
    public void freeContent(){
        content = null;
    }
    // </editor-fold>

}
