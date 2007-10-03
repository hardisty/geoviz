package edu.psu.geovista.matrix.spacefill;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SpaceFillMatrixElementBeanInfo extends SimpleBeanInfo {
  static Class beanClass = SpaceFillMatrixElement.class;
  static String iconColor16x16Filename = "resources/spacefillElement16.gif";
  static String iconColor32x32Filename = "resources/spacefillElement32.gif";
  static String iconMono16x16Filename;
  static String iconMono32x32Filename;

  public SpaceFillMatrixElementBeanInfo() {
  }
  public PropertyDescriptor[] getPropertyDescriptors() {
    try {
      PropertyDescriptor _axisOn = new PropertyDescriptor("axisOn", beanClass, null, "setAxisOn");
      PropertyDescriptor _conditionArray = new PropertyDescriptor("conditionArray", beanClass, null, "setConditionArray");
      PropertyDescriptor _dataObject = new PropertyDescriptor("dataObject", beanClass, null, "setDataObject");
      PropertyDescriptor _elementPosition = new PropertyDescriptor("elementPosition", beanClass, "getElementPosition", "setElementPosition");
      PropertyDescriptor _xAxisExtents = new PropertyDescriptor("xAxisExtents", beanClass, null, null);
      PropertyDescriptor _yAxisExtents = new PropertyDescriptor("yAxisExtents", beanClass, null, null);
      PropertyDescriptor[] pds = new PropertyDescriptor[] {
	      _axisOn,
	      _conditionArray,
	      _dataObject,
	      _elementPosition,
	      _xAxisExtents,
	      _yAxisExtents};
      return pds;






}
    catch(IntrospectionException ex) {
      ex.printStackTrace();
      return null;
    }
  }
  public java.awt.Image getIcon(int iconKind) {
    switch (iconKind) {
    case BeanInfo.ICON_COLOR_16x16:
        return iconColor16x16Filename != null ? loadImage(iconColor16x16Filename) : null;
    case BeanInfo.ICON_COLOR_32x32:
        return iconColor32x32Filename != null ? loadImage(iconColor32x32Filename) : null;
    case BeanInfo.ICON_MONO_16x16:
        return iconMono16x16Filename != null ? loadImage(iconMono16x16Filename) : null;
    case BeanInfo.ICON_MONO_32x32:
        return iconMono32x32Filename != null ? loadImage(iconMono32x32Filename) : null;
        }
    return null;
  }
}