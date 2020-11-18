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
package rubensandreoli.commons.swing;

import java.awt.Component;
import java.io.File;
import rubensandreoli.commons.utils.FileUtils;
import rubensandreoli.commons.utils.SwingUtils;

/** 
 * References:
 * https://stackoverflow.com/questions/5931261/java-use-stringbuilder-to-insert-at-the-beginning
 * https://stackoverflow.com/questions/12524826/why-should-i-use-deque-over-stack
 * https://stackoverflow.com/questions/196830/what-is-the-easiest-best-most-correct-way-to-iterate-through-the-characters-of-a
 * https://stackoverflow.com/questions/7569335/reverse-a-string-in-java
 * https://stackoverflow.com/questions/14189262/fitting-text-to-jtextfield-using
 * https://stackoverflow.com/questions/30987866/java-enforce-textfield-format-ux-00000000
 * https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html
 * https://stackoverflow.com/questions/8075373/path-separator-vs-filesystem-getseparator-vs-system-getpropertyfile-separato
 * https://stackoverflow.com/questions/58631724/paths-get-vs-path-of
 * https://stackoverflow.com/questions/811248/how-can-i-use-drag-and-drop-in-swing-to-get-path-path
 * http://zetcode.com/tutorials/javaswingtutorial/draganddrop/
 */
public class PathField extends javax.swing.JTextField{
    private static final long serialVersionUID = 1L;
    
    public static final int FILES_ONLY = FileUtils.FILES_ONLY;
    public static final int DIRECTORIES_ONLY = FileUtils.DIRECTORIES_ONLY;
    public static final int FILES_AND_DIRECTORIES = FileUtils.FILES_AND_DIRECTORIES;
    public static final int MIN_LENGTH = FileUtils.MASKED_FILENAME_MIN_LENGTH;
    
    private final int mode;
    private File file;
    private int length;
    
    public PathField(int mode, int length){
        this.mode = mode;
        this.length = length;
        setEditable(false);
        addDropTarget();
    }

    public PathField(int mode){
        this(mode, 0);
    }
    
    public PathField(){
        this(FILES_AND_DIRECTORIES, 0);
    }
    
    private void addDropTarget(){
        SwingUtils.addDroppable(this, file -> setText(file));
    }

    @Override
    public void setDragEnabled(boolean b) {
        if(b) SwingUtils.addDroppable(this, file -> setText(file));
        else setDropTarget(null);
    }

    @Override
    public void setText(String path){
        if(path == null) {
            clear();
            return;
        }
        path = FileUtils.normalize(path);
        if(!setText(new File(path))){
            throw new IllegalArgumentException(path+" is not a valid mode "+mode+" file");
        }
    }
    
    public boolean setText(File file){
        return setText(file, false);
    }
    
    private boolean setText(File file, boolean validated){
        if(file == null){
            clear();
            return false;
        }
        
        if(!validated){
            if ((mode!=FILES_AND_DIRECTORIES) && 
                        (mode==FILES_ONLY && !file.isFile()) ||
                        (mode==DIRECTORIES_ONLY && !file.isDirectory())){
                return false;
            }
        }
        
        SwingUtils.getChooser(mode).setSelectedFile(new File(file, File.separator));
        super.setText(FileUtils.maskPathname(file.getPath(), length));
        this.file = file;
        fireActionPerformed();
        return true;
    }
    
    public boolean select(Component parent){
        return setText(SwingUtils.selectFile(parent, mode), true);
    }
    
    public void clear(){
        super.setText("");
        file = null;
    }
    
    @Override
    public String getText() {
        return file==null? "" : file.getPath();
    }

    public void setLenght(int length) {
        if(length < MIN_LENGTH) throw new IllegalArgumentException("length "+length+" < "+MIN_LENGTH);
        this.length = length;
        if(file != null) setText(file.getPath());
    }

}
