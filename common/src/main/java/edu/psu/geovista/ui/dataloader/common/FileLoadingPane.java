/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen 
 * @date: Oct 20, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.ui.dataloader.common;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.ui.dataloader.event.LoadDataEvent;
import edu.psu.geovista.ui.dataloader.event.LoadDataListener;

public class FileLoadingPane extends JPanel
                            {
	final static Logger logger = Logger.getLogger(FileLoadingPane.class.getName());
    protected EventListenerList listenerList = new EventListenerList();

    //protected JCheckBox projected;//true, the data is projected one
    protected MultipleFileSource fileSrcs;
    protected ConfigurePropSavor propertySavor=new ConfigurePropSavor("/.pcp/config/","dataFiles.cfg");
    protected String[] fileSrcNames;
    protected JPanel controlPane;
    protected JButton loadBtn;
    public FileLoadingPane(String[] loaderNames) {
        this.initComponents(loaderNames) ;
        addControlPane();
        this.load() ; //remove this will turn off load from   propertySavor

    }
    protected  void initComponents(String[] fileSrcNames){
        //GUI
        this.fileSrcNames=fileSrcNames;
        fileSrcs=new MultipleFileSource(fileSrcNames);
        fileSrcs.setBorder(new TitledBorder("Paths"));


        //String[] loadNames=new String[]{"Observation","Ob Meta","Shape"};
        //loaders=new FileLoaderPane(loadNames);

        this.setLayout(new BorderLayout());
        this.add(fileSrcs);



    }
    /**
     * overrided by subclass
     */
    protected void addControlPane(){
        controlPane=new JPanel();
        controlPane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        /*projected=new JCheckBox("projected shape");
        projected.setSelected(true);
        control.add(projected);*/

        //load button
       // this.addLoadInfoButton();

        this.add(controlPane,BorderLayout.SOUTH );
    }
    //set the title of panel that contain the paths. By default it is "paths"
    public void setFileSrcBorder(AbstractBorder border){
       this.fileSrcs.setBorder(border);
    }
    // 
    public  void addLoadInfoButton(){
        loadBtn=new JButton("load info");
        Action loadAct=new AbstractAction("load info"){
            public void actionPerformed(ActionEvent e){
                 fireLoadDataEvent();
            }

        };
        loadBtn.setAction(loadAct);

        controlPane.add(loadBtn);
    }
    /**
     * add any JComponent to the controlPane. The JPanel only contain the component's GUI
     * User are responsible to implement its logic
     * @param c
     */
    public void addComponentToControlPane(JComponent c) {
        this.controlPane.add(c);
    }

    public JPanel getControlPane() {
        return controlPane;
    }

    public  boolean isAllSourceAvailable(){
        for (int i=0;i<this.fileSrcNames.length ;i++){
            String n=  fileSrcNames[i];
            String path=this.getFileLoaderValue(n);
            if(path==null) return false;
        }
        return true;
    }
    /**
     *
     * @param loaderName   name of loader. It is displayed in the label on the left
     * @return             the name (including full path) of the source.
     */
    public String getFileLoaderValue(String loaderName){
        return this.fileSrcs.getFileLoaderValue(loaderName);
    }
    public void setFileLoaderValue(String loaderName,String value){
        this.fileSrcs.setFileLoaderValue(loaderName,value);
    }
   public Properties getFileLoaderValues() {

        return this.fileSrcs.getSources() ;

    }
    public void setFileLoaderValues(Properties nameValue){
        this.fileSrcs.setSources(nameValue);
    }
    public void  setAllFileLoaderValues(String value){
        this.fileSrcs.setAllSourceValue(value)      ;

    }
    /*******************************************************************************************************
     *                load and save configure
     *******************************************************************************************************/
    public  void load(){
        propertySavor.load() ;
        String filePath="";
        for (int i=0;i<fileSrcNames.length ;i++){

            filePath=propertySavor.getProperty(fileSrcNames[i]);
            if(filePath!=null)
                fileSrcs.setFileLoaderValue(fileSrcNames[i],filePath);
        }


    }

    public  void save(){
        //savor.clear() ;  //No need clear it, since saving properties that already exist just override it.
        //With PcpLoader and TsLoader, if clear(), TsLoader will clean PcpLoader's properties
        for (int i=0;i<fileSrcNames.length ;i++){
            String key=fileSrcNames[i];
            String value=fileSrcs.getFileLoaderValue(key);
            if(value==null)value="";

            propertySavor.setProperty(key,value);
            //dp{
            if (logger.isLoggable(Level.FINEST)){
                logger.finest("Save property: "+key+":"+value);
            }//dp}
        }
        propertySavor.save() ;
    }

    /*******************************************************************************************************
     *                properties
     *******************************************************************************************************/
    public ConfigurePropSavor getPropertySavor() {
        return propertySavor;
    }

    public void setPropertySavor(ConfigurePropSavor propertySavor) {
        this.propertySavor = propertySavor;
       // this.load() ; //use this propertySavor to load properties. NO. should load property before setting
    }
    /*public String[] getLoaderNames() {
        return loaderNames;
    }*/
    /*public void addLoadingActionListener(ActionListener al) {
        this.listenerList.add()
    }
    public void addLoadingActionListener(ActionListener al) {

    }*/
    /**
     *
     * @param file    this is the initial directory which displayed by filechooser when the Open button is click
     */
    public void setRoot(File file) {
        this.fileSrcs.setAllSourceRoot(file);
    }
    /*******************************************************************************************************
     *                event
     *******************************************************************************************************/
    protected void fireLoadDataEvent(){
        this.save() ;
       Object[] listeners=this.listenerList.getListenerList() ;
       LoadDataEvent e=null;
       for (int i=listeners.length -2;i>=0;i-=2){
           if (listeners[i]==LoadDataListener.class){
               e=new LoadDataEvent(this);
           }
           ((LoadDataListener)listeners[i+1]).loadData(e);

       }
    }
    public void addLoadDataListner(LoadDataListener ll) {
        this.listenerList.add(LoadDataListener.class, ll);
    }
    public void removeLoadDataListner(LoadDataListener ll) {
        this.listenerList.remove(LoadDataListener.class, ll);
    }
    public static void main(String[] args) {
        JFrame mf = new JFrame();
        Container container = mf.getRootPane().getContentPane();
        String[] names={"ob data","meta data","shape data"};
        final FileLoadingPane fp=new FileLoadingPane(names);
        container.add(fp);
        mf.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                fp.save() ;
            }
        })  ;
        mf.setSize(800, 600);
        mf.setVisible(true);
        mf.setVisible(true);
    }

}
