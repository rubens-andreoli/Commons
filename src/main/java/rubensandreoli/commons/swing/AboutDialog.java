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

import java.awt.Frame;
import rubensandreoli.commons.utils.SwingUtils;
import rubensandreoli.commons.utils.FileUtils;

/** 
 * Creates a dialog that displays the program and its author information.<br>
 * <br>
 * References:<br>
 * https://stackoverflow.com/questions/51660856/open-a-browser-with-java<br>
 * https://stackoverflow.com/questions/222214/managing-constructors-with-many-parameters-in-java<br>
 * https://stackoverflow.com/questions/1842223/java-linebreaks-in-jlabels
 * 
 * @author Rubens A. Andreoli Jr.
 */
public class AboutDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;
    
    public static final String GNU_PUBLIC = "<html><body style=\"text-align:justify\">"
	    + "This program is free software: you can redistribute it and/or modify "
	    + "it under the terms of the GNU General Public License as published by "
	    + "the Free Software Foundation, either version 3 of the License, or "
	    + "any later version."
	    + "<p>This program is distributed in the hope that it will be useful, "
	    + "but WITHOUT ANY WARRANTY; without even the implied warranty of "
	    + "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the "
	    + "GNU General Public License for more details.</p>"
	    + "<p>You should have received a copy of the GNU General Public License "
	    + "along with this program.  If not, see http://www.gnu.org/licenses.</p></body></html>";
    
    // <editor-fold defaultstate="collapsed" desc=" PROGRAM INFO "> 
    public static class ProgramInfo {
       public final String name;
       public final String description;
       public final String version;
       public final String year;

        public ProgramInfo(String name, String description, String version, String year) {
            this.name = name;
            this.description = description;
            this.version = version;
            this.year = year;
        }
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" AUTHOR INFO "> 
    public static class AuthorInfo {
        public final String name;
        public final String logo_path;
        public final String site;
        public final String donate_site;

        public AuthorInfo(String name, String logo_path, String site, String donate_site) {
            this.name = name;
            this.logo_path = logo_path;
            this.site = site;
            this.donate_site = donate_site;
        }
    }
    // </editor-fold>
    
    private StringBuilder atributions;
    
    public AboutDialog(Frame parent, ProgramInfo programInfo, AuthorInfo authorInfo) {
	super(parent);
	initComponents();
        
        if(parent != null){
            setLocationRelativeTo(parent);
            setIconImage(parent.getIconImage());
        }

        lblLogo.setIcon(FileUtils.loadIcon(authorInfo.logo_path));
        lblProgram.setText(programInfo.name);
        lblVersion.setText("Version: "+ programInfo.version);
        if(programInfo.description!=null){
            lblDescription.setText("<html><p style=\"width:200px\">"+programInfo.description+"</p></html>");
            pack();
        }
        lblCopyright.setText("Copyright (C) "+programInfo.year+" "+authorInfo.name);
        txpLicense.setText("<html><body style=\"text-align:justify\">"+GNU_PUBLIC+"</body></html>");
        txpLicense.setCaretPosition(0);
        
        SwingUtils.addClickableLink(lblLogo, authorInfo.site);
        SwingUtils.addClickableLink(lblHere, authorInfo.donate_site);

    }
    
    public AboutDialog(Frame parent, ProgramInfo programInfo){
        this(parent, programInfo, new AuthorInfo(
                "Rubens A. Andreoli Junior", 
                "images/logo.png", 
                "https://github.com/rubens-andreoli", 
                "https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=Q5NUAPVCTC5U4&currency_code=USD&source=url"
        ));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblProgram = new javax.swing.JLabel();
        lblVersion = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblCopyright = new javax.swing.JLabel();
        sclLicense = new javax.swing.JScrollPane();
        txpLicense = new javax.swing.JTextPane();
        sclAtributions = new javax.swing.JScrollPane();
        txpAtributions = new javax.swing.JTextPane();
        pnlColor = new javax.swing.JPanel();
        lblDonating = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        lblHere = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setModalityType(java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
        setResizable(false);

        lblLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblProgram.setFont(lblProgram.getFont().deriveFont(lblProgram.getFont().getStyle() | java.awt.Font.BOLD, lblProgram.getFont().getSize()+4));

        lblVersion.setFont(lblVersion.getFont());

        lblDescription.setFont(lblDescription.getFont().deriveFont(lblDescription.getFont().getStyle() & ~java.awt.Font.BOLD));

        lblCopyright.setFont(lblCopyright.getFont().deriveFont(lblCopyright.getFont().getSize()-2f));

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblVersion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCopyright, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblProgram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlTopLayout.createSequentialGroup()
                        .addComponent(lblProgram, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblVersion, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCopyright, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        txpLicense.setEditable(false);
        txpLicense.setContentType("text/html"); // NOI18N
        sclLicense.setViewportView(txpLicense);

        sclAtributions.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(0, -2, 0, -2), javax.swing.BorderFactory.createTitledBorder(null, "Atributions", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14)))); // NOI18N

        txpAtributions.setBackground(getBackground());
        txpAtributions.setContentType("text/html"); // NOI18N
        txpAtributions.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txpAtributions.setMargin(new java.awt.Insets(0, 6, 2, 6));
        sclAtributions.setViewportView(txpAtributions);

        pnlColor.setBackground(javax.swing.UIManager.getDefaults().getColor("TextField.shadow"));

        lblDonating.setForeground(new java.awt.Color(0, 0, 0));
        lblDonating.setText("<html>Please considere <b>donating</b> by clicking</html>");

        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        lblHere.setText("<html> <a href=\"https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=Q5NUAPVCTC5U4&currency_code=USD&source=url\">here...</a></html>");

        javax.swing.GroupLayout pnlColorLayout = new javax.swing.GroupLayout(pnlColor);
        pnlColor.setLayout(pnlColorLayout);
        pnlColorLayout.setHorizontalGroup(
            pnlColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlColorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDonating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(lblHere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                .addComponent(btnClose)
                .addContainerGap())
        );
        pnlColorLayout.setVerticalGroup(
            pnlColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlColorLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(pnlColorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClose)
                    .addComponent(lblDonating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlColor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sclAtributions, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sclLicense)
                    .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sclLicense, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sclAtributions, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JLabel lblCopyright;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblDonating;
    private javax.swing.JLabel lblHere;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblProgram;
    private javax.swing.JLabel lblVersion;
    private javax.swing.JPanel pnlColor;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JScrollPane sclAtributions;
    private javax.swing.JScrollPane sclLicense;
    private javax.swing.JTextPane txpAtributions;
    private javax.swing.JTextPane txpLicense;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public void setVisible(boolean b) {
	if(b){
            if(atributions!=null){
                txpAtributions.setText("<html><body>"+atributions.toString()+"</body></html>");
            }else{
                remove(sclAtributions);
                setSize(getWidth(), getHeight() - sclAtributions.getHeight()-10);
                validate();
            }
            btnClose.requestFocus();
        }
        super.setVisible(b);
    }
    
    public AboutDialog addAtribution(String item, String creator, String site){
        if(atributions == null) atributions = new StringBuilder();
        else atributions.append("<br/>");
        atributions.append("<span style=\"font-size:9px;\">&#8226; ")
                .append(item).append(" made by <b>").append(creator)
                .append("</b> at:<br/><i>").append(site).append("</i></span>");
        return this;
    }
    
}
