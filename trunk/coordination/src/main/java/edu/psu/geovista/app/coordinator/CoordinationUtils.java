/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CoordinationUtils
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: CoordinationUtils.java,v 1.4 2005/02/12 17:42:36 hardisty Exp $
 $Date: 2005/02/12 17:42:36 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.app.coordinator;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;

/**
 *  Utility functions for the coordinator package. 
 *  
 */
public final class CoordinationUtils {
  /*
     This version does not require an instantiated object.
   */
  public static Image findIcon(Class clazz) {
    BeanInfo info = null;
    try {
      info = Introspector.getBeanInfo(clazz);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    Image img = info.getIcon(BeanInfo.ICON_COLOR_32x32);

    if (img == null) {
      CoordinationUtils dc = new CoordinationUtils();
      Class cl = dc.getClass();
      URL urlGif = cl.getResource("resources/GenericBean32.gif");
      ImageIcon icon = new ImageIcon(urlGif, "generic bean image");
      img = icon.getImage();
    }

    return img;

  }

  /*
   This method extracts the class from the object, then calls the Class version.
   */

  public static Image findIcon(Object originalBean) {

    Class clazz = originalBean.getClass();
    return CoordinationUtils.findIcon(clazz);
  }
  /*
     This version does not require an instantiated object.
   */
  public static Image findSmallIcon(Class clazz) {

    BeanInfo info = null;
    try {
      info = Introspector.getBeanInfo(clazz);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    Image img = info.getIcon(BeanInfo.ICON_COLOR_16x16);

    if (img == null) { //we don't have a small icon
      Image bigImg = CoordinationUtils.findIcon(clazz);

      if (bigImg != null) { //but we do have a big icon
        img = bigImg.getScaledInstance(16, 16, Image.SCALE_DEFAULT);
      }
      else { // we don't have any icon
        CoordinationUtils dc = new CoordinationUtils();
        Class cl = dc.getClass();
        URL urlGif = cl.getResource("resources/GenericBean16.gif");
        ImageIcon icon = new ImageIcon(urlGif, "generic bean image");
        img = icon.getImage();
      }
    }

    return img;

  }

  public static Image findSmallIcon(Object originalBean) {
    Class clazz = originalBean.getClass();
    return CoordinationUtils.findSmallIcon(clazz);
  }

  static Vector findMethods(Class clazz, Vector v) {
    if (clazz == Object.class) {
      return v;
    }

    Method[] meths = clazz.getMethods();

    for (int i = 0; i < meths.length; i++) {
      v.add(meths[i]);
    }

    Class base = clazz.getSuperclass();

    if (base != null) {
      findMethods(base, v);
    }

    return v;
  }

  static String findInterfaceName(Class clazz) {
    String name = clazz.getName();
    int periodPlace = name.lastIndexOf(".");
    name = name.substring(periodPlace + 1);

    if (name.endsWith("Listener")) {
      name = name.substring(0, name.lastIndexOf("Listener"));
    }

    return name;
  }
}
