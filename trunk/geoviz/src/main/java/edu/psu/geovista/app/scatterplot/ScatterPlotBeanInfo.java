package edu.psu.geovista.app.scatterplot;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ScatterPlotBeanInfo extends SimpleBeanInfo {
    Class beanClass = ScatterPlot.class;
    String iconColor16x16Filename = "scatterplotm16.gif";
    String iconColor32x32Filename = "scatterplotm32.gif";
    String iconMono16x16Filename;
    String iconMono32x32Filename;

    public ScatterPlotBeanInfo() {
    }
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor _attributeArrays = new PropertyDescriptor("attributeArrays", beanClass, null, "setAttributeArrays");
            PropertyDescriptor _axisOn = new PropertyDescriptor("axisOn", beanClass, "isAxisOn", "setAxisOn");
            PropertyDescriptor _elementPosition = new PropertyDescriptor("elementPosition", beanClass, "getElementPosition", "setElementPosition");
            PropertyDescriptor _doubleDataArrays = new PropertyDescriptor("doubleDataArrays", beanClass, null, "setDoubleDataArrays");
            PropertyDescriptor _background = new PropertyDescriptor("background", beanClass, null, "setBackground");
            PropertyDescriptor _selectionColor = new PropertyDescriptor("selectionColor", beanClass, null, "setSelectionColor");
			PropertyDescriptor _selectedObservations = new PropertyDescriptor("selectedObservations", beanClass, "getSelectedObservations", "setSelectedObservations");
            PropertyDescriptor _this = new PropertyDescriptor("this", beanClass, "getThis", null);
            PropertyDescriptor _x = new PropertyDescriptor("x", beanClass, null, "setX");
            PropertyDescriptor _XAxisExtents = new PropertyDescriptor("XAxisExtents", beanClass, "getXAxisExtents", "setXAxisExtents");
            PropertyDescriptor _y = new PropertyDescriptor("y", beanClass, null, "setY");
            PropertyDescriptor _YAxisExtents = new PropertyDescriptor("YAxisExtents", beanClass, "getYAxisExtents", "setYAxisExtents");
            PropertyDescriptor[] pds = new PropertyDescriptor[] {
	            _attributeArrays,
	            _axisOn,
	            _elementPosition,
	            _doubleDataArrays,
	            _background,
				_selectionColor,
	            _selectedObservations,
	            _this,
	            _x,
	            _XAxisExtents,
	            _y,
	            _YAxisExtents};
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