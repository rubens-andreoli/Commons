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
 * References:<br>
 * https://stackoverflow.com/questions/5931261/java-use-stringbuilder-to-insert-at-the-beginning<br>
 * https://stackoverflow.com/questions/12524826/why-should-i-use-deque-over-stack<br>
 * https://stackoverflow.com/questions/196830/what-is-the-easiest-best-most-correct-way-to-iterate-through-the-characters-of-a<br>
 * https://stackoverflow.com/questions/7569335/reverse-a-string-in-java<br>
 * https://stackoverflow.com/questions/14189262/fitting-text-to-jtextfield-using<br>
 * https://stackoverflow.com/questions/30987866/java-enforce-textfield-format-ux-00000000<br>
 * https://docs.oracle.com/javase/tutorial/uiswing/components/formattedtextfield.html<br>
 * https://stackoverflow.com/questions/8075373/path-separator-vs-filesystem-getseparator-vs-system-getpropertyfile-separato<br>
 * https://stackoverflow.com/questions/58631724/paths-get-vs-path-of<br>
 * https://stackoverflow.com/questions/811248/how-can-i-use-drag-and-drop-in-swing-to-get-path-path<br>
 * http://zetcode.com/tutorials/javaswingtutorial/draganddrop/
 * 
 * @author Rubens A. Andreoli Jr.
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
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public PathField(int mode, int length){
        this.mode = mode;
        this.length = length;
        setEditable(false);
        setDragEnabled(true);
    }

    public PathField(int mode){
        this(mode, 0);
    }
    
    public PathField(){
        this(FILES_AND_DIRECTORIES, 0);
    }

    @Override
    public void setDragEnabled(boolean b) {
        if(b) SwingUtils.setDropTarget(this, f -> setText(f));
        else setDropTarget(null);
    }

    @Override
    public void setText(String path){
        if(path == null) {
            clear();
            return;
        }
        if(!setText(new File(path))){
            throw new IllegalArgumentException("file "+path+" doesn't match set mode "+mode);
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
            if ((mode!=FILES_AND_DIRECTORIES) && (mode==FILES_ONLY && !file.isFile()) || (mode==DIRECTORIES_ONLY && !file.isDirectory())){
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
        if(length < MIN_LENGTH) throw new IllegalArgumentException("parameter length "+length+" < "+MIN_LENGTH);
        this.length = length;
        if(file != null) setText(file.getPath());
    }

}
