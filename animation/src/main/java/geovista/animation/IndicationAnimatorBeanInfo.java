/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class IndicationAnimatorBeanInfo
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: IndicationAnimatorBeanInfo.java,v 1.1 2003/05/12 18:21:11 hardisty Exp $
 $Date: 2003/05/12 18:21:11 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */

package geovista.animation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class IndicationAnimatorBeanInfo extends SimpleBeanInfo {
  static Class beanClass = IndicationAnimator.class;
  static String iconColor16x16Filename = "resources/IndicationAnimatorIcon16.gif";
  static String iconColor32x32Filename = "resources/IndicationAnimatorIcon32.gif";
  static String iconMono16x16Filename;
  static String iconMono32x32Filename;

  public IndicationAnimatorBeanInfo() {
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