/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen
 * @date: Jan 8, 2004$
 * @version: 1.0
 */
package edu.psu.geovista.app.scatterplot;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;
import java.net.URL;

import javax.swing.ImageIcon;

public class SingleScatterPlotBeanInfo extends SimpleBeanInfo {
	
       final public static String RESOURCES="resources/";
       final public static String MODEL_ROOT="edu/psu/geovista/app/scatterplot/";
       final public static String IMAGES=MODEL_ROOT+RESOURCES+"images/";


        private final static Class beanClass=SingleScatterPlot.class;

    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(beanClass);
    }


        public Image getIcon(int iconKind){
            if (iconKind == BeanInfo.ICON_MONO_16x16 ||
                iconKind == BeanInfo.ICON_COLOR_16x16 )
            {

              java.awt.Image img = getImage(IMAGES+"SingleScatterPlot16.gif");  //root is the folder contain this class

              return img;
            }
            if (iconKind == BeanInfo.ICON_MONO_32x32 ||
                iconKind == BeanInfo.ICON_COLOR_32x32 )
            {
              //if(Debug.isDebug()) {
                  String im = IMAGES + "SingleScatterPlot32.gif";

              //java.awt.Image img = getImage(IMAGES +"PcpTs32.gif");
              java.awt.Image img = getImage(im);

              return img;
            }
            return null;

        }  //getIcon
        // loading images
        public static Image getImage(String fullName) {
            //String fullName=IMAGES+name;
            //todo change the name of class to current BeanInfo class
            ClassLoader cl=SingleScatterPlotBeanInfo.class.getClassLoader() ;
            URL url=cl.getResource(fullName);
            if (url == null) {
               
                return null;
            }
            return new ImageIcon(url).getImage() ;
        }
}
