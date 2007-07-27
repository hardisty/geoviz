/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen 
 * @date: Oct 10, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.common.image;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;

public class ImageUtils {
	final static Logger logger = Logger.getLogger(ImageUtils.class.getName());

    /**
     *
     * loading image
     * @param imagePath  the folder contains images.The path start with class's package name as classloader will search classpath
     *                   e.g.: edu/psu/geovista/app/pcp/resources/images/
     * @param name       image name. e.g.: DataShowAll.gif
     * @return           ImageIcon
     */
    public static ImageIcon getImageIcon(String imagePath,String name) {
        String fullName=imagePath+name;
        //ClassLoader cl=ActionManager.class.getClassLoader() ;
        ClassLoader cl=ImageUtils.class.getClassLoader() ;
        URL url=cl.getResource(fullName);
        if (url == null) {
            //dp{
            if (logger.isLoggable(Level.FINEST)){
               logger.finest("image "+fullName+" not found");
            }//dp}

            return null;
        }
        return new ImageIcon(url);
    }
}
