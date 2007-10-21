package geovista.matrix;

/**
 * <p>Title: BiPlotMatrixBeanInfo</p>
 * <p>Description: Bean Info for BiPlotMatrix</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GeoVISTA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class BiPlotMatrixBeanInfo extends SimpleBeanInfo {
	Class beanClass = BiPlotMatrix.class;
	String iconColor16x16Filename = "resources/mixedmatrix16.gif";
	String iconColor32x32Filename = "resources/mixedmatrix32.gif";
	String iconMono16x16Filename;
	String iconMono32x32Filename;

	public BiPlotMatrixBeanInfo() {
    }

	public PropertyDescriptor[] getPropertyDescriptors() {
		try {
			PropertyDescriptor _background = new PropertyDescriptor("background", beanClass, "getBackground", "setBackground");
			PropertyDescriptor _selectionColor = new PropertyDescriptor("selectionColor", beanClass, "getSelectionColor", "setSelectionColor");
			PropertyDescriptor _selOriginalColorMode = new PropertyDescriptor("selOriginalColorMode", beanClass, "getSelOriginalColorMode", "setSelOriginalColorMode");
			PropertyDescriptor _bivarColorClasser = new PropertyDescriptor("bivarColorClasser", beanClass, "getBivarColorClasser", "setBivarColorClasser");
			PropertyDescriptor _elementClassName1 = new PropertyDescriptor("elementClassName1", beanClass, "getElementClassName1", "setElementClassName1");
			PropertyDescriptor _elementClassName2 = new PropertyDescriptor("elementClassName2", beanClass, "getElementClassName2", "setElementClassName2");
			PropertyDescriptor _maxNumArrays = new PropertyDescriptor("maxNumArrays", beanClass, "getMaxNumArrays", "setMaxNumArrays");
			PropertyDescriptor _plottedBegin = new PropertyDescriptor("plottedBegin", beanClass, "getPlottedBegin", "setPlottedBegin");
			PropertyDescriptor[] pds = new PropertyDescriptor[] {
				_background,
				_selectionColor,
				_selOriginalColorMode,
				_bivarColorClasser,
				_elementClassName1,
				_elementClassName2,
				_maxNumArrays,
				_plottedBegin};
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