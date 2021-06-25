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

import java.awt.Desktop;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import rubensandreoli.commons.exceptions.UnsupportedException;
import rubensandreoli.commons.others.CachedFile;

/** 
 * References:<br>
 * https://stackoverflow.com/questions/265769/maximum-name-length-in-ntfs-windows-xp-and-windows-vista#:~:text=14%20Answers&text=Individual%20components%20of%20a%20filename,files%2C%20248%20for%20folders).<br>
 * https://stackoverflow.com/questions/57807466/what-is-the-maximum-filename-length-in-windows-10-java-would-try-catch-would<br>
 * https://docs.oracle.com/javase/6/docs/technotes/tools/solaris/javadoc.html#@inheritDoc<br>
 * https://examples.javacodegeeks.com/desktop-java/imageio/determine-format-of-an-image/<br>
 * https://www.sparkhound.com/blog/detect-image-file-types-through-byte-arrays<br>
 * https://stackoverflow.com/questions/27476845/what-is-the-difference-between-a-null-array-and-an-empty-array
 */
public final class FileUtils {
    
    // <editor-fold defaultstate="collapsed" desc=" STATIC FIELDS ">
    public static final String IMAGES_REGEX = ".*\\.jpg|jpeg|bmp|png|gif|webp";
    public static final String IMAGES_GLOB = "*.{jpg,jpeg,bmp,png,gif,webp}";
    public static final HashSet<String> IMAGES_EXT = new HashSet<>();
    static {
	IMAGES_EXT.add(".jpg");
        IMAGES_EXT.add(".jpeg");
	IMAGES_EXT.add(".bmp");
	IMAGES_EXT.add(".png");
        IMAGES_EXT.add(".gif");
        IMAGES_EXT.add(".webp");
    }
    
    public static final String separator = File.separator;
    private static final Pattern FOLDER_PATTERN = Pattern.compile("([^"+Matcher.quoteReplacement(separator)+"]*["+Matcher.quoteReplacement(separator)+"]+)");
    private static final String FOLDER_INVALID_CHARS_REGEX = "[*?\"<>|]";
    private static final String FILENAME_INVALID_CHARS_REGEX = "[\\/\\\\:\\*?\\\"<\\>|]";
    private static final String EXTENSION_REGEX = "^.[a-z]{3,}$";
    private static final String EXTENSION_INVALID_CHARS_REGEX = "[^a-z-A-Z\\.]";
    public static final int MASKED_FILENAME_MIN_LENGTH = 5;
    public static final int FILEPATH_MAX_LENGTH = 255;
    
    public static final int DEFAULT_CONNECTION_TIMEOUT = 2000; //ms
    public static final int DEFAULT_READ_TIMEOUT = 4000; //ms
    public static final int DEFAULT_BUFFER_SIZE = 1024 * 4; //bytes
    
    public static final int FILES_ONLY = 0;
    public static final int DIRECTORIES_ONLY = 1;
    public static final int FILES_AND_DIRECTORIES = 2;
    // </editor-fold>

    private FileUtils(){}
    
    // <editor-fold defaultstate="collapsed" desc=" PARSE PATHNAME ">
    public static String getParent(String pathname){
        return new File(pathname).getParent();
    }

    public static String getParentName(String pathname){
        return new File(pathname).getParentFile().getName();
    }
    
    public static String getRoot(String pathname){
        pathname = normalize(pathname);
        final Matcher matcher = FOLDER_PATTERN.matcher(pathname);
        final StringBuilder sb = new StringBuilder();
        while(matcher.find()){
            final String node = matcher.group(1);
            sb.append(node);
            if(!node.startsWith("http") && !node.startsWith(separator)) break;
        }
        if(sb.length() == 0) return null;
        return sb.toString();    
        
        // <editor-fold defaultstate="collapsed" desc=" ALTERNATIVE ">
//        final String[] tokens = pathname.split("[/\\\\]");
//        if(tokens.length == 1) {
//            if(!pathname.contains(".")) return tokens[0]+SEPARATOR;
//            return null;
//        }
//        final StringBuilder sb = new StringBuilder();
//        boolean found = false;
//        int i = 0;
//        for (String token : tokens) {
//            if(token.isEmpty()){
//                if(!found) sb.append(SEPARATOR);
//            }else{
//                if(!found){
//                    sb.append(token);
//                    if(!token.startsWith("http")) found = true;
//                    else sb.append(SEPARATOR);
//                }else{
//                    break;
//                }
//            }
//            i++;
//        }
//        if(found){ 
//            if(i <= tokens.length)sb.append(SEPARATOR);
//            return sb.toString();
//        }else{
//            return null;
//        }
        // </editor-fold>
    }

    public static String normalize(String pathname){
        return new File(pathname).getPath();
    }

    public static String getName(String pathname){
        return new File(pathname).getName();
    }
    
    /**
     * Returns the name of the file or directory denoted by this abstract pathname,
 without any characters considered invalid by Windows OS.
     * 
     * @see Utils#parseFilename(String, boolean) 
     * @param pathname abstract pathname from which the name of the file/directory will be parsed
     * @return last item of the pathname without extension and without invalid
     *          characters; or an empty {@code String} if this pathname's name sequence is empty
     */
    public static String getFilename(String pathname){
        return getFilename(pathname, true);
    }
    
    /**
     * Returns the name of the file until the first {@literal '.'} (dot) 
     * (without extension) or directory denoted by this abstract pathname. 
     * This is just the last name in the pathname's  name sequence. 
     * It can also remove any characters considered invalid  by Windows OS.
     * 
     * @see Utils#parseFile(String) 
     * @see Utils#FILENAME_INVALID_CHARS_REGEX
     * @param pathname abstract pathname from which the name of the file will be parsed
     * @param normalize {@code true} to remove invalid characters; {@code false} otherwise
     * @return last item of the pathname without extension {@code .ext} or an empty 
     *          {@code String} if this pathname's name sequence is empty
     */
    public static String getFilename(String pathname, boolean normalize){
        String name = getName(pathname);
        final int extIndex = name.lastIndexOf(".");
        if(extIndex != -1) name = name.substring(0, extIndex);
        if(normalize) name = name.replaceAll(FILENAME_INVALID_CHARS_REGEX, "");
        return name;
    }
 
    /**
     * Extracts the file from the abstract pathname and then returns a 
     * {@code String} containing anything after the first {@literal '.'} (dot) 
     * removing anything from the end of the {@Code String} until it matches
     * a common extension regex.
     * 
     * @see Utils#parseFile(String) 
     * @see Utils#DEFAULT_EXTENSION
     * @see Utils#EXTENSION_REGEX
     * @param pathname abstract pathname from which the extension of the file will be parsed
     * @param defaultValue default extension in case none is found
     * @return {@code String} containing the extension of the file without invalid characters; 
     *          or a given default value if this pathname doesn't contain one
     */
    public static String getExtension(String pathname, String defaultValue){
        final String name = getName(pathname);
        String ext = defaultValue;
        final int extIndex = name.lastIndexOf(".");
        if(extIndex != -1){
            String tmpExt = name.substring(extIndex);
            boolean empty = false;
            while(!tmpExt.matches(EXTENSION_REGEX)){
                if(tmpExt.isEmpty()){
                    empty = true;
                    break;
                }
                tmpExt = tmpExt.substring(0, tmpExt.length()-1);
            }
            if(!empty) ext = tmpExt;
        }
        return ext;
    }
    
    public static String getExtension(String pathname){
        return getExtension(pathname, "");
    }
    
    public static String buildPathname(File root, String...nodes){
        for (String node : nodes) {
            root = new File(root, node);
        }
        return root.getPath();
    }

    public static String buildPathname(String root, String...nodes){
        return buildPathname(new File(root), nodes);
    }

    public static String maskPathname(String pathname, int maxLenght){
        if(pathname.isEmpty()) return "";
        pathname = normalize(pathname);
        if((maxLenght < MASKED_FILENAME_MIN_LENGTH) || (pathname.length() <= maxLenght)) return pathname;
        
        String formated = pathname.substring(pathname.length()-maxLenght, pathname.length());
        formated = formated.replaceFirst("([^\\"+separator
                +"]{3}(?=\\"+separator
                +"))|(.{3})(?=[^\\"+separator
                +"]*$)", "..."); //or only (.{3,}?(?=\/))|(.{3})

        String root = getRoot(pathname);
        if(root != null){
            formated = formated.replaceFirst(".{"+root.length()
                    +",}(\\.{3})|(^.{"+(root.length()+3)
                    +",}?(?=\\"+separator+"))", Matcher.quoteReplacement(root)+"..."); //if separator is "\" Matcher.quoteReplacement(root)
            int index = formated.indexOf("..."+separator);
            if(index < root.length()) formated = formated.substring(Math.max(0, index));
        }

        return formated;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" CREATE VALID FILE "> 
    /**
     * Returns a valid {@code File} conforming the filename to Windows OS
     * standards. It removes all invalid characters; 
     * reduces the maximum filename length to fit the given pathname;
     * and resolve duplicated files.
     * 
     * @param folder directory {@code File} where the file will be saved, 
     *              must not be {@code null}
     * @param filename name of the file without extension, must not be {@code null}
     * @param extension extension of the file with {@literal '.'} (Dot) at the start, 
     *                  must not be {@code null}
     * @return valid {@code File} ready to be saved
     */
    public static File createValidFile(File folder, String filename, String extension){
        //FIX INVALID CHARACTERS
        filename = filename.replaceAll(FILENAME_INVALID_CHARS_REGEX, "");
        extension = extension.replaceAll(EXTENSION_INVALID_CHARS_REGEX, "");
        File file = new File(folder, new StringBuilder(filename).append(extension).toString());

        //FIX FILEPATH LENGTH
        if(file.getAbsolutePath().length() > FILEPATH_MAX_LENGTH){
            int toRemove = file.getAbsolutePath().length() - FILEPATH_MAX_LENGTH;
            if(filename.length() > toRemove){
                filename = filename.substring(0, filename.length()-toRemove);
                file = new File(folder.getPath(), new StringBuilder(filename).append(extension).toString());
            }else{
                return null;
            }
        }

        //FIX DUPLICATED NAME
        for(int n=1; file.exists(); n++){
            file = new File(folder.getPath(), new StringBuilder(filename)
                            .append(" (").append(String.valueOf(n)).append(")")
                            .append(extension).toString());
        }
        return file;
    }

    /**
     * Returns a valid {@code File} conforming the filename by Windows OS
     * standards. It removes all invalid characters; 
     * reduces the maximum filename length to fit the given pathname;
     * and resolve duplicated files.
     * 
     * @see Utils#createValidFile(File, String, String)
     * @param folder directory {@code File} where the file will be saved, 
     *              must not be {@code null}
     * @param file name with extension, must not be {@code null}
     * @return valid {@code File} ready to be saved
     */
    public static File createValidFile(File folder, String file){
        return createValidFile(folder, getFilename(file), getExtension(file));
    }

    /**
     * Returns a valid {@code File} conforming the filename by Windows OS
     * standards. It removes all invalid characters; 
     * reduces the maximum filename length to fit the given pathname;
     * and resolve duplicated files.
     * 
     * @see Utils#createValidFile(File, String, String)
     * @param folder abstract directory pathname where the file will be saved, 
     *              must not be {@code null}
     * @param filename name of the file without extension, must not be {@code null}
     * @param extension extension of the file with {@literal '.'} (Dot) at the start, 
     *                  must not be {@code null}
     * @return valid {@code File} ready to be saved
     */
    public static File createValidFile(String folder, String filename, String extension){
        folder = folder.replaceAll(FOLDER_INVALID_CHARS_REGEX, "");
        return createValidFile(new File(folder), filename, extension);
    }
    
    /**
     * Returns a valid {@code File} conforming the filename by Windows OS
     * standards. It removes all invalid characters; 
     * reduces the maximum filename length to fit the given pathname;
     * and resolve duplicated files.
     * 
     * @see Utils#createValidFile(String, String, String)
     * @param folder abstract directory pathname where the file will be saved, 
     *              must not be {@code null}
     * @param file name with extension, must not be {@code null}
     * @return valid {@code File} ready to be saved
     */
    public static File createValidFile(String folder, String file){
        return createValidFile(folder, getFilename(file), getExtension(file));
    }
    // </editor-fold>
 
    // <editor-fold defaultstate="collapsed" desc=" MODIFY "> 
    /**
     * Creates child directory if doesn't exists and try to move the source file to it.
     * If there is already a file with the same pathname, a new filename will be created.
     * This method doesn't throw {@code SecurityException} of the {@code java.io.File} 
     * methods called.
     * 
     * @see Utils#createValidFile(String, String, String)
     * @param file file to be moved
     * @param subfolder name of the sub-folder
     * @return {@code true} if and only if the file was moved; 
     *         {@code false} otherwise
     */
    public static boolean moveFileToChild(File file, String subfolder){
        final File dest = new File(file.getParent(), subfolder);
        try{
            dest.mkdir();
        }catch(SecurityException ex){
            return false;
        }
        return moveFileTo(file, dest.getPath());
    }

    public static boolean moveFileTo(File file, String folder){
        boolean moved = false;
        File tempDest = new File(folder, file.getName());
        try{
            moved = file.renameTo(tempDest);
            if(!moved){ //costly method only if failed above
                tempDest = createValidFile(folder, file.getPath());
                moved = file.renameTo(tempDest);
            }
        }catch(Exception ex){}
        
        return moved;
    }
    
    /**
     * Deletes the file or directory. If it is a directory, it must be empty.
     * Convenience method for {@code java.io.File#delete()} that doesn't
     * throw {@code SecurityException}.
     * 
     * @param file file or directory to be deleted
     * @return {@code true} if and only if the file or directory is successfully deleted; 
     *         {@code false} otherwise
     */
    public static boolean deleteFile(File file){
        boolean removed = false;
        try{
            removed = file.delete();
        }catch(SecurityException ex){}
        return removed;
    }
    
    public static boolean removeFile(File file) throws UnsupportedException {
        if(Desktop.isDesktopSupported()){
            final Desktop d = Desktop.getDesktop();
            if(d.isSupported(Desktop.Action.MOVE_TO_TRASH)){
                try{
                    return d.moveToTrash(file);
                }catch(IllegalArgumentException|SecurityException ex){ //file not found or no access
                    return false;
                }
            }else{
                throw new UnsupportedException("move to trash action not supported");
            }
        }else{
            throw new UnsupportedException("current platform doesn't support desktop class");
        }
    }
    
    public static File createFolder(File root, String...nodes){
        for (String node : nodes) {
            root = new File(root, node);
        }
        if(!root.isDirectory()){
            boolean created = false;
            try{
                created = root.mkdirs();
            }catch(SecurityException ex){}
            if(!created){
                return null; //couldn't create
            }
        }
        return root; //already there or created
    }
    
    public static File createFolder(String root, String...nodes){
        return createFolder(new File(root), nodes);
    }
    // </editor-fold>
      
    // <editor-fold defaultstate="collapsed" desc=" READ "> 
    public static long getFileSize(File file){
        try{
            return file.length();
        }catch(SecurityException ex){
            return 0L;
        }
    }
    
    public static String getFormattedFileSize(File file){
        return formatFilesize(getFileSize(file));
    }
    
    public static String formatFilesize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" }; //TODO: remove from here
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    
    public static byte[] readAllBytes(File file){
        try(var bi = new BufferedInputStream(new FileInputStream(file))){
            return bi.readAllBytes();
        }catch(IOException ex){
            return null;
        }
    }

    public static byte[] readFirstBytes(File file, int amount){
        try(var bi = new BufferedInputStream(new FileInputStream(file), amount)){
            return bi.readNBytes(amount);
        } catch (IOException ex) {
            return null;
        }
    }

    public static Byte readFirstByte(File file){
        try(var r = new FileReader(file)){
            return (byte) r.read(); //does NOT support extend chars (2 bytes)
        } catch (Exception ex) {
            return null;
        }
    }
        
    public static ImageIcon loadIcon(String url){
        try{
            return new ImageIcon(FileUtils.class.getClassLoader().getResource(url));
        }catch(NullPointerException ex){
            return new ImageIcon();
        }
    }
    
    public static ImageIcon loadIcon(String url, int size){
        if(size < 1) throw new IllegalArgumentException();
        try{
            final Image i = new ImageIcon(FileUtils.class.getClassLoader().getResource(url)).getImage().getScaledInstance(size, size, Image.SCALE_DEFAULT);
            return new ImageIcon(i);
        }catch(NullPointerException ex){
            return null;
        }
    }
    
    public static BufferedImage loadImage(String url){
        try {
            return ImageIO.read(FileUtils.class.getResource(url));
        } catch (IOException ex) {
            return null;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" SCAN FILESYSTEM "> 
    public static List<File> scanChildren(File root){
        return scanChildren(root, FILES_AND_DIRECTORIES, true);
    }
    
    public static List<File> scanChildren(File root, int mode){
        return scanChildren(root, mode, true);
    }
    
    public static List<File> scanChildren(File root, int mode, boolean showHidden){
        if(!root.isDirectory()) return null;
        final List<File> files = new ArrayList<>();
        
        switch(mode){
            case FILES_ONLY: {
                final Stack<File> folders = new Stack<>();
                folders.add(root);
                while(!folders.empty()){
                    final File[] folderFiles = folders.pop().listFiles(f -> showHidden || !f.isHidden());
                    if(folderFiles == null) continue;
                    for(File file : folderFiles){
                        if(file.isDirectory()) folders.push(file);
                        else files.add(file);
                    }
                }
                break;}
            
            case DIRECTORIES_ONLY:{
                files.add(root);

                for(int i=0; i<files.size(); i++){
                    final File[] folders = files.get(i).listFiles(f -> f.isDirectory() && (showHidden || !f.isHidden()));
                    if(folders != null) files.addAll(Arrays.asList(folders));
                }
                break;}
            
            case FILES_AND_DIRECTORIES:{
                final Stack<File> folders = new Stack<>();
                folders.add(root);
                while(!folders.empty()){
                    final File folder = folders.pop();
                    files.add(folder);
                    final File[] folderFiles = folder.listFiles(f -> showHidden || !f.isHidden());
                    if(folderFiles == null) continue;
                    for(File file : folderFiles){
                        if(file.isDirectory()) folders.push(file);
                        else files.add(file);
                    }
                }
                break;}
        }       
        
        return files;
    }

    public static List<File> listChildren(File root){
        return listChildren(root, FILES_AND_DIRECTORIES, true);
    }
    
    public static List<File> listChildren(File root, int mode){
        return listChildren(root, mode, true);
    }
    
    public static List<File> listChildren(File root, int mode, boolean showHidden){
        final List<File> files = new ArrayList<>();
        visitChildren(root, mode, showHidden, f -> files.add(f));
        return files;
    }
    
    public static void visitChildren(File root, int mode, boolean showHidden, Consumer<File> consumer){
        if(!root.isDirectory()) return;
        final File[] listOfFiles = root.listFiles();
        switch(mode){
            case FILES_ONLY:
                for (File file : listOfFiles) {
                    if (file.isFile() && (showHidden || !file.isHidden())) {
                        consumer.accept(file);
                    }
                }
                break;
            case DIRECTORIES_ONLY:
                for (File file : listOfFiles) {
                    if (file.isDirectory() && (showHidden || !file.isHidden())) {
                        consumer.accept(file);
                    }
                }
                break;
            case FILES_AND_DIRECTORIES:
                for (File file : listOfFiles) {
                    if (showHidden || !file.isHidden()) {
                        consumer.accept(file);
                    }
                }
                break;
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" DOWNLOAD ">
    public static void downloadToFile(String url, CachedFile file) throws IOException{
        downloadToFile(url, file, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_READ_TIMEOUT, DEFAULT_BUFFER_SIZE);
    }

    public static void downloadToFile(String url, CachedFile file, int connectionTimeout, int readTimeout) throws IOException{
        downloadToFile(url, file, connectionTimeout, readTimeout, DEFAULT_BUFFER_SIZE);
    }
    
    public static void downloadToFile(String url, CachedFile file, int connectionTimeout, int readTimeout, int bufferSize) throws IOException{
        long bytesWritten = 0;
        try (final InputStream in = openInputStream(new URL(url), connectionTimeout, readTimeout);
                OutputStream out = openOutputStream(file)) {
            int bytesRead;
            final byte[] buffer = new byte[bufferSize];
            byte[] signature = null;
            while ((bytesRead = in.read(buffer)) != -1) {
                if(signature == null) signature = Arrays.copyOf(buffer, CachedFile.SIGNATURE_BYTES);
                out.write(buffer, 0, bytesRead);
                bytesWritten += bytesRead;
            }
            file.setSize(bytesWritten);
            file.setSignature(signature);
        }
    }
    
    private static InputStream openInputStream(URL path, int connectionTimeout, int readTimeout) throws IOException{
        try {
            final var conn = path.openConnection();
            conn.setConnectTimeout(connectionTimeout);
            conn.setReadTimeout(readTimeout);
            return conn.getInputStream();
        } catch (IOException ex) {
             throw new IOException("URL '"+path+"' cannot be reached");
        }
    }
    
    private static FileOutputStream openOutputStream(File file) throws IOException {
        if (file.isDirectory()) {
            throw new IOException("File '"+file+"' is a directory");
        }else if (file.isFile() && !file.canWrite()) {
            throw new IOException("File '"+file+"' cannot be overridden");
        } else {
            final File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) { //if not given, not created, and not validated
                throw new IOException("Directory '"+parent+"' could not be created");
            }
        }
        return new FileOutputStream(file, false);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" VALIDATION "> 
    public static boolean isImage(File file){
        try {
            return ImageIO.read(file) != null;
        } catch (IOException e) {
            return false;
        }
    }
    
    public static boolean hasImageExtension(File file){
        return IMAGES_EXT.contains(getExtension(file.getPath()));
    }
    // </editor-fold>
    
}
