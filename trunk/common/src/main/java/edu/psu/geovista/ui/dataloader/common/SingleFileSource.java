/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Obtain the source (path + name) of a file
 * @author: jin Chen 
 * @date: Oct 10, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.ui.dataloader.common;

import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.psu.geovista.common.image.ImageUtils;

public class SingleFileSource extends JPanel {
    public static final String FileChooser_Root="fcroot";
	final static Logger logger = Logger.getLogger(SingleFileSource.class.getName());
   //images
    final public static String RESOURCES="resources/";
    final public static String IMAGES=RESOURCES+"images/";
    public static final String IMAGE_Open="OpenFile.gif";
    public static final String IMAGE_OK="ok.gif";
    public static final String IMAGE_NotOK="notOK.gif";
    protected  Hashtable images=new Hashtable();
    protected File root;//root dir for FileChooser
    protected File selected;//selected by filechooser , not checkbox
    protected String name="data";
    protected String fileName; //file's full path value
    protected boolean valid;//true if "fileName" is a file existing
    /** Creates new form JPanel */
    public SingleFileSource() {
        loadImage();
        initComponents();
        initValue();
    }

    public SingleFileSource(String name) {
       this.name = name;
       loadImage();
       initComponents();
        initValue();
    }
    public void selectNone(){
        this.fileList.setSelectedItem("");
    }
    protected void initValue() {
        this.fileList.setEditable(true);

        categoryL.setText(this.name );

        ImageIcon openImg=(ImageIcon) this.images.get(IMAGE_Open);
        openBtn.setText("");
        openBtn.setIcon(openImg);

        ImageIcon avaImg=(ImageIcon) this.images.get(IMAGE_NotOK);
        availableL.setText("");
        availableL.setIcon(avaImg);
    }
    private void loadImage(){
        String packagePath=this.getPackagePath() ;
        String newPackagePath=packagePath.replace('.','/');
        String imagePath=newPackagePath+"/"+IMAGES;
        ImageIcon open=ImageUtils.getImageIcon(imagePath,IMAGE_Open);
        images.put(IMAGE_Open,open);
        ImageIcon ok=ImageUtils.getImageIcon(imagePath,IMAGE_OK);
        images.put(IMAGE_OK,ok);
        ImageIcon notOk=ImageUtils.getImageIcon(imagePath,IMAGE_NotOK);
        images.put(IMAGE_NotOK,notOk);
    }
    protected String getPackagePath(){

          return SingleFileSource.class.getPackage().getName();
    }
    /**
     * Set the file name(include full path)
     * if add duplicate item, may behavoir incorrectly
     * @param fileName
     */
    public void setFileName(String fileName) {
        //Following 4 lines is must as the value can be set by outside upon loading configure
        if(!this.containItem(fileName) ){
                        this.fileList.addItem(fileName);
                }
        this.fileList.setSelectedItem(fileName);

        this.fileName =fileName;

    }
    private boolean containItem(String fn) {
        DefaultComboBoxModel model=(DefaultComboBoxModel) this.fileList.getModel() ;
        for (int i=0;i<model.getSize() ;i++){
            String item=(String) model.getElementAt(i);

            if(fn.equals(item ) )
                return true;
        }
        return false;
    }
    public String getFileName(){
        return fileName;
    }

    public void addSetSelectionListener(ActionListener l) {
        this.fileList.addActionListener(l);
    }
    public void addChangeSelectionListener(ItemListener l) {
        this.fileList.addItemListener( l);
    }
    public void addInputMethodListener(InputMethodListener l) {
        this.fileList.addInputMethodListener(l );

    }
    public void addKeyListener(KeyListener l) {
        this.fileList.addKeyListener( l) ;

    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
         private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        labelP = new javax.swing.JPanel();
        availableL = new javax.swing.JLabel();
        categoryL = new javax.swing.JLabel();
        inputP = new javax.swing.JPanel();
        fileList = new javax.swing.JComboBox();
        openBtn = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        labelP.setLayout(new java.awt.GridBagLayout());

        labelP.setPreferredSize(new java.awt.Dimension(130, 26));   //set label wide
        availableL.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        availableL.setText("av");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        labelP.add(availableL, gridBagConstraints);

        categoryL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        categoryL.setText("ob  data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;     //jin
        labelP.add(categoryL, gridBagConstraints);

        add(labelP, new java.awt.GridBagConstraints());

        inputP.setLayout(new java.awt.GridBagLayout());

        fileList.setEditable(true);
        fileList.setPreferredSize(new java.awt.Dimension(124, 20));
        fileList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileListActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 9.0;
        inputP.add(fileList, gridBagConstraints);

        openBtn.setText("open");
        openBtn.setMaximumSize(new java.awt.Dimension(40, 26));
        openBtn.setPreferredSize(new java.awt.Dimension(40, 20));
        openBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bttnActionPerformed(evt);
            }
        });

        inputP.add(openBtn, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(inputP, gridBagConstraints);

    }
    /**
     * Get event both on selected item or input item in CheckBox
     * @param evt
     */
    private void fileListActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        /*JComboBox combox=(JComboBox) evt.getSource() ;
        String fileName=(String) combox.getSelectedItem() ;*/

        /*ImageIcon ok=(ImageIcon) this.images.get(IMAGE_OK);
        this.availableL.setIcon(ok);*/
        updateAvailability();

    }
    /**
     *  invoked upon enter value in JComboBox
     */
    protected void updateAvailability(){
        String fn=(String) this.fileList.getSelectedItem() ;
        if (logger.isLoggable(Level.FINEST)){
                   logger.finest("input file:"+fn);
               }//dp}
        File file=new File(fn);
        this.fileName =fn;
        if(!this.containItem(fn) ){
              this.fileList.addItem(fn);
        }
        if(file.exists() &&file.isFile() ){
             ImageIcon ok=(ImageIcon) this.images.get(IMAGE_OK);
                this.availableL.setIcon(ok);
                if (logger.isLoggable(Level.FINEST)){
                   logger.finest("select file:"+fn);
               }//dp}
            this.setValid(true);
        }
        else{
            ImageIcon notok=(ImageIcon) this.images.get(IMAGE_NotOK);
            this.availableL.setIcon(notok);
            this.setValid(false);

        }
    }
    protected  void bttnActionPerformed(java.awt.event.ActionEvent evt) {
        // Add your handling code here:
        JFileChooser fc=new JFileChooser();
        if(root!=null&&root.isDirectory() ){
            fc.setCurrentDirectory(root);
        }
        else {
            fc.setCurrentDirectory(new File("c:/"));
        }
        fc.setDialogTitle("Please choose a file");
        //fc.setDialogType(JFileChooser.OPEN_DIALOG );
        int state=fc.showOpenDialog(this);
        if(state==JFileChooser.APPROVE_OPTION ){
            this.selected =fc.getSelectedFile() ;
            if(selected.exists() &&selected.isFile() ){
                /*ImageIcon ok=(ImageIcon) this.images.get(IMAGE_OK);
                this.availableL.setIcon(ok);*/
                String fileName=null ;
                try {
                    fileName = selected.getCanonicalPath();
                } catch (IOException e) {
                    e.printStackTrace();
                    //???exception
                }
                this.setFileName(fileName);


            }
            this.root =fc.getCurrentDirectory() ;
            this.firePropertyChange(FileChooser_Root,null,fc.getCurrentDirectory());

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

       this.name = name;
    }

    public boolean isValid() {
        return valid;
    }

    protected void setValid(boolean valid) {
        this.valid = valid;
    }

    public File getRoot() {
        return root;
    }

    public void setRoot(File root) {
        this.root = root;

    }
    public String getInput() {
        String s= (String) fileList.getEditor().getItem();
        return s;
    }

    // Variables declaration - do not modify
    protected javax.swing.JLabel availableL;
    protected javax.swing.JLabel categoryL;
    protected javax.swing.JButton openBtn;
    protected javax.swing.JPanel labelP;
    protected javax.swing.JComboBox fileList;
    protected javax.swing.JPanel inputP;
    // End of variables declaration

    public static void main(String[] args) {
        JFrame mf = new JFrame();
        Container container = mf.getRootPane().getContentPane();
        final SingleFileSource sf = new SingleFileSource();
        /*sf.addInputMethodListener(new InputMethodListener() {
            public void caretPositionChanged(InputMethodEvent event) {
                //To change body of implemented methods use File | Settings | File Templates.
                logger.finest("caret");
            }
            public void inputMethodTextChanged(InputMethodEvent event) {
                //To change body of implemented methods use File | Settings | File Templates.
                AttributedCharacterIterator  txt=event.getText();
                String s=txt.toString();
                logger.finest("input:"+s);
               



        }
          });

        sf.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
                logger.finest("press");
            }

            public void keyReleased(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void keyTyped(KeyEvent e) {
               String s=sf.getInput();
               logger.finest("input:"+s);
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });*/
        container.add(sf);

        mf.setSize(800, 600);
        mf.setVisible(true);


    }

}
