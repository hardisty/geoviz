/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Contain multiple SingleFileSource
 * @author: jin Chen 
 * @date: Oct 13, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.ui.dataloader.common;

import java.awt.Container;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MultipleFileSource extends JPanel
                                implements PropertyChangeListener{
    protected Hashtable fileSrcs=new Hashtable();
    protected String[] fileSrcNames;//The name of each SingleFileSource. the sequence of elements determine the their layout in a GridLayout(loaderNames.length,1)

    //protected

    public MultipleFileSource(String[] loaderNames) {

         this.setFileSrcNames(loaderNames);
    }

    public void setFileSrcNames(String[] fileSrcNames) {
        if(fileSrcNames==null){
            new NullPointerException(this.getClass().getName() +" is passed a null argument");
        }
        if(fileSrcNames.length <=0)return;

        this.fileSrcNames=fileSrcNames;
        this.removeAll() ;
        this.setLayout(new GridLayout(fileSrcNames.length,1));
        for (int i=0;i<fileSrcNames.length ;i++){
            String name=fileSrcNames[i];
            SingleFileSource fl = (SingleFileSource) fileSrcs.get(name);
            if(fl==null ){
                fl = new SingleFileSource(name);
                fl.addPropertyChangeListener(this);
                fileSrcs.put(name,fl);
                this.add(fl);
            }
        }
    }
    private  SingleFileSource getFileloader(String name){
        return (SingleFileSource) this.fileSrcs.get(name);
    }
    /**
     *
     * @param loaderName     label name
     * @param value          path value
     */
    public void setFileLoaderValue(String loaderName,String value) {
         SingleFileSource ld=this.getFileloader(loaderName);
         if (ld!=null){
             ld.setFileName(value);
         }

    }
    /**
     *
     * @return       a pair of loader name(on the label) and value (path)
     */
    public Properties getSources(){
        Properties srcNameValues=new Properties();
        Enumeration enm=this.fileSrcs.elements() ;
        while (enm.hasMoreElements()) {
            SingleFileSource source = (SingleFileSource) enm.nextElement();

            srcNameValues.setProperty(source.getName() ,source.getFileName() );
        }
        return srcNameValues;
    }
    /**
     *
     * @param nvs     a pair of fileSource name(on the label) and  (path)value
     */
     public void setSources(Properties nvs){
           Enumeration keys=nvs.keys() ;
           while (keys.hasMoreElements()) {
               String name=(String) keys.nextElement() ;   //fileSource name
               String value=nvs.getProperty(name);
               SingleFileSource source = this.getFileloader(name);
               if(source!=null)
                   source.setFileName(value);



           }
    }
    /**
     * set the source value for all Source
     * @param value
     */
    public void setAllSourceValue(String value) {
        Enumeration fsrcs= this.fileSrcs.elements() ;
        while (fsrcs.hasMoreElements()) {
            SingleFileSource singleFileSource = (SingleFileSource) fsrcs.nextElement();
            singleFileSource.setFileName(value);
        }
    }
    /**
     *
     * @param loaderName   name of loader. It is displayed in the label on the left
     * @return             the name (including full path) of the source.
     */
    public String getFileLoaderValue(String loaderName){
         SingleFileSource ld=this.getFileloader(loaderName);
         if (ld!=null){
             return ld.getFileName() ;
         }
         else{
             return null;
         }
    }
    public void propertyChange(PropertyChangeEvent evt) {
        String pn=evt.getPropertyName() ;
        if(pn.equals(SingleFileSource.FileChooser_Root ) ){
            File file=(File) evt.getNewValue() ;
            setAllSourceRoot(file);
        }

    }
    /**
     *
     * @param file   this is the initial directory which displayed by filechooser when the Open button is click
     */
    public  void setAllSourceRoot(File file){
        Enumeration e=this.fileSrcs.elements() ;
        while (e.hasMoreElements()) {
            SingleFileSource fileSource = (SingleFileSource) e.nextElement();
            fileSource.setRoot(file);
        }
    }

    public static void main(String[] args) {
        JFrame mf = new JFrame();
        Container container = mf.getRootPane().getContentPane();
        String[] names={"ob data","meta data","shape data"};
        MultipleFileSource flp=new MultipleFileSource(names);
        container.add(flp);
        mf.setSize(800, 600);
        mf.setVisible(true);
        mf.setVisible(true);


    }


}
