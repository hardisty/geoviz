/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai */

package geovista.matrix;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class FixedRowMatrixBeanInfo extends SimpleBeanInfo {
	Class beanClass = FixedRowMatrix.class;
	String iconColor16x16Filename = "resources/mixedGraph16.gif";
	String iconColor32x32Filename = "resources/mixedGraph32.gif";
	String iconMono16x16Filename;
	String iconMono32x32Filename;

	public FixedRowMatrixBeanInfo() {
	}

	@Override
	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor _background = new PropertyDescriptor(
					"background", beanClass, "getBackground", "setBackground");
			PropertyDescriptor _elementClassName1 = new PropertyDescriptor(
					"elementClassName1", beanClass, "getElementClassName1",
					"setElementClassName1");
			PropertyDescriptor _elementClassName2 = new PropertyDescriptor(
					"elementClassName2", beanClass, "getElementClassName2",
					"setElementClassName2");
			PropertyDescriptor _elementClassName3 = new PropertyDescriptor(
					"elementClassName3", beanClass, "getElementClassName3",
					"setElementClassName3");
			PropertyDescriptor _maxNumArrays = new PropertyDescriptor(
					"maxNumArrays", beanClass, "getMaxNumArrays",
					"setMaxNumArrays");
			PropertyDescriptor _plottedBegin = new PropertyDescriptor(
					"plottedBegin", beanClass, "getPlottedBegin",
					"setPlottedBegin");
			PropertyDescriptor _selectionColor = new PropertyDescriptor(
					"selectionColor", beanClass, "getSelectionColor",
					"setSelectionColor");
			PropertyDescriptor _selOriginalColorMode = new PropertyDescriptor(
					"selOriginalColorMode", beanClass,
					"getSelOriginalColorMode", "setSelOriginalColorMode");
			PropertyDescriptor[] pds = new PropertyDescriptor[] { _background,
					_elementClassName1, _elementClassName2, _elementClassName3,
					_maxNumArrays, _plottedBegin, _selOriginalColorMode,
					_selectionColor };
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

}