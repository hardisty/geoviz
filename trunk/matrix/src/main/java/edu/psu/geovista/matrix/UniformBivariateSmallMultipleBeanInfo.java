package edu.psu.geovista.matrix;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class UniformBivariateSmallMultipleBeanInfo extends SimpleBeanInfo {
	Class beanClass = UniformBivariateSmallMultiple.class;
	String iconColor16x16Filename = "resources/uniformSamllMultiple16.gif";
	String iconColor32x32Filename = "resources/uniformSamllMultiple32.gif";
	String iconMono16x16Filename;
	String iconMono32x32Filename;

	public UniformBivariateSmallMultipleBeanInfo() {
    }

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor _background = new PropertyDescriptor("background", beanClass, "getBackground", "setBackground");
			PropertyDescriptor _elementClassName = new PropertyDescriptor("elementClassName", beanClass, "getElementClassName", "setElementClassName");
			PropertyDescriptor _maxNumArrays = new PropertyDescriptor("maxNumArrays", beanClass, "getMaxNumArrays", "setMaxNumArrays");
			PropertyDescriptor _plottedBegin = new PropertyDescriptor("plottedBegin", beanClass, "getPlottedBegin", "setPlottedBegin");
			PropertyDescriptor _selectionColor = new PropertyDescriptor("selectionColor", beanClass, "getSelectionColor", "setSelectionColor");
			PropertyDescriptor _selOriginalColorMode = new PropertyDescriptor("selOriginalColorMode", beanClass, "getSelOriginalColorMode", "setSelOriginalColorMode");
			PropertyDescriptor[] pds = new PropertyDescriptor[] {
				_background,
				_elementClassName,
				_maxNumArrays,
				_plottedBegin,
				_selOriginalColorMode,
				_selectionColor};
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