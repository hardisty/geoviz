/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.matrix;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class UniPlotMatrixBeanInfo extends SimpleBeanInfo {
	Class beanClass = UniPlotMatrix.class;
	String iconColor16x16Filename = "matrix16.gif";
	String iconColor32x32Filename = "matrix32.gif";
	String iconMono16x16Filename;
	String iconMono32x32Filename;

	public UniPlotMatrixBeanInfo() {
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor _background = new PropertyDescriptor(
					"background", beanClass, "getBackground", "setBackground");
			PropertyDescriptor _selectionColor = new PropertyDescriptor(
					"selectionColor", beanClass, "getSelectionColor",
					"setSelectionColor");
			PropertyDescriptor _selOriginalColorMode = new PropertyDescriptor(
					"selOriginalColorMode", beanClass,
					"getSelOriginalColorMode", "setSelOriginalColorMode");
			PropertyDescriptor _maxNumArrays = new PropertyDescriptor(
					"maxNumArrays", beanClass, "getMaxNumArrays",
					"setMaxNumArrays");
			PropertyDescriptor _plottedBegin = new PropertyDescriptor(
					"plottedBegin", beanClass, "getPlottedBegin",
					"setPlottedBegin");
			PropertyDescriptor _elementClassName = new PropertyDescriptor(
					"elementClassName", beanClass);
			PropertyDescriptor[] pds = new PropertyDescriptor[] { _background,
					_selectionColor, _selOriginalColorMode, _maxNumArrays,
					_plottedBegin, _elementClassName };
			return pds;

		} catch (IntrospectionException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public java.awt.Image getIcon(int iconKind) {
		switch (iconKind) {
		case BeanInfo.ICON_COLOR_16x16:
			return iconColor16x16Filename != null
					? loadImage(iconColor16x16Filename) : null;
		case BeanInfo.ICON_COLOR_32x32:
			return iconColor32x32Filename != null
					? loadImage(iconColor32x32Filename) : null;
		case BeanInfo.ICON_MONO_16x16:
			return iconMono16x16Filename != null
					? loadImage(iconMono16x16Filename) : null;
		case BeanInfo.ICON_MONO_32x32:
			return iconMono32x32Filename != null
					? loadImage(iconMono32x32Filename) : null;
		}
		return null;
	}

	@Override
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