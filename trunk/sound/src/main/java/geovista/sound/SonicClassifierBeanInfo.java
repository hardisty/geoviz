/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SonicClassifierBeanInfo
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SonicClassifierBeanInfo.java,v 1.1 2005/02/12 18:41:50 hardisty Exp $
 $Date: 2005/02/12 18:41:50 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */

package geovista.sound;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class SonicClassifierBeanInfo extends SimpleBeanInfo {
  static Class beanClass = SonicClassifier.class;
  static String iconColor16x16Filename = "resources/SonicAnchor16.gif";
  static String iconColor32x32Filename = "resources/SonicAnchor32.gif";
  static String iconMono16x16Filename;
  static String iconMono32x32Filename;

  public SonicClassifierBeanInfo() {
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
    PropertyDescriptor[] pds = new PropertyDescriptor[] {  };

    return pds;
  }

  public java.awt.Image getIcon(int iconKind) {
    switch (iconKind) {
    case BeanInfo.ICON_COLOR_16x16:
      return (iconColor16x16Filename != null)
             ? loadImage(iconColor16x16Filename) : null;

    case BeanInfo.ICON_COLOR_32x32:
      return (iconColor32x32Filename != null)
             ? loadImage(iconColor32x32Filename) : null;

    case BeanInfo.ICON_MONO_16x16:
      return (iconMono16x16Filename != null)
             ? loadImage(iconMono16x16Filename) : null;

    case BeanInfo.ICON_MONO_32x32:
      return (iconMono32x32Filename != null)
             ? loadImage(iconMono32x32Filename) : null;
    }

    return null;
  }

  public BeanInfo[] getAdditionalBeanInfo() {
    Class superclass = beanClass.getSuperclass();

    try {
      BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);

      return new BeanInfo[] { superBeanInfo };
    } catch (IntrospectionException ex) {
      ex.printStackTrace();

      return null;
    }
  }
}
