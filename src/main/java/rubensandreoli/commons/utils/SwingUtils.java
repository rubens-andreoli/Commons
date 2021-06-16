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

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import rubensandreoli.commons.others.Level;
import rubensandreoli.commons.others.Logger;
import rubensandreoli.commons.others.PickyConsumer;

/**
 * References:
 * https://stackoverflow.com/questions/53419438/create-a-java-application-that-keeps-your-computer-from-going-idle-without-movin<br>
 * https://stackoverflow.com/questions/7814089/how-to-schedule-a-periodic-task-in-java
 * 
 * @author Rubens A. Andreoli Jr.
 */
public class SwingUtils {
    
    public static final int FILES_ONLY = FileUtils.FILES_ONLY;
    public static final int DIRECTORIES_ONLY = FileUtils.DIRECTORIES_ONLY;
    public static final int FILES_AND_DIRECTORIES = FileUtils.FILES_AND_DIRECTORIES;
    private static final int TIMER_INTERVAL = 1; //minutes
    
    private static JFileChooser chooser;
    private static ScheduledExecutorService timer;
    
    private SwingUtils(){}
    
    public static File selectFile(Component parent, int mode){
        getChooser(mode);
        if(chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        return null;
    }
    
    public static synchronized JFileChooser getChooser(int mode){
        if(chooser == null) chooser = new JFileChooser();
        chooser.setFileSelectionMode(mode);
        return chooser;
    }
    
    @SuppressWarnings("unchecked")
    public static void setDropTarget(Component c, PickyConsumer<File> consumer){
        c.setDropTarget(new DropTarget() {
            @Override
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    final List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    for (File file : droppedFiles) {
                        if(consumer.accept(file)) break;
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    Logger.log.print(Level.ERROR, "drag and drop failed", ex);
                }
            }
        });
    }
    
    public static void removeDropTarget(Component c){
        c.setDropTarget(null);
    }
    
    public static void addClickableLink(Component c, String url){
        c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                final String os = System.getProperty("os.name").toLowerCase();
                final Runtime runtime = Runtime.getRuntime();
                IOException exception = null;

                if(os.contains("win")){
                    try { runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
                    } catch (IOException ex) {exception = ex;}
                }else if(os.contains("mac")){
                    try { runtime.exec("open " + url);
                    } catch (IOException ex) {exception = ex;}
                }else if(os.contains("nix") || os.contains("nux")){
                    try { runtime.exec("xdg-open " + url);
                    } catch (IOException ex) {exception = ex;}
                }

                if(exception != null){
                    Logger.log.print(Level.ERROR, "failed opening link on "+os, exception);
                }
            }
        });
    }
    
    public static void showMessageDialog(Component parent, String msg, String title, Level lvl, boolean beep){
        int type;
        switch(lvl){
            case WARNING:
                type = JOptionPane.WARNING_MESSAGE;
                break;
            case ERROR:
            case CRITICAL:
            case SEVERE:
                type = JOptionPane.ERROR_MESSAGE;
                break;
            default:
                type = JOptionPane.INFORMATION_MESSAGE;
        }
        if(beep) Toolkit.getDefaultToolkit().beep();
        JOptionPane.showMessageDialog(parent, msg, title, type);
    }
    
    public static void showMessageDialog(Component parent, Exception ex, Level lvl, boolean beep){
        showMessageDialog(parent, ex.getMessage(), "An exception has occurred!", lvl, beep);
    }
    
    public static void keepAwake(boolean b) throws AWTException{
        if(b){
            timer = Executors.newScheduledThreadPool(1);
            final Robot robot = new Robot();
            timer.scheduleAtFixedRate(() -> {
                final Point point = MouseInfo.getPointerInfo().getLocation();
                robot.mouseMove(point.x, point.y);
            }, TIMER_INTERVAL, TIMER_INTERVAL, TimeUnit.MINUTES);
        }else if(timer != null){
            timer.shutdownNow();
            timer = null;
        }
    }
    
}
