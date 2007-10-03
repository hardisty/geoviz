/*
 * RadVizBeanInfo.java
 *
 * Created on February 14, 2005, 1:30 PM
 */
/**
 *
 * @author  yliu
 */
package edu.psu.geovista.geoviz.radviz;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class RadVizBeanInfo extends SimpleBeanInfo {
    static Class beanClass = RadViz.class;
    static String iconColor16x16Filename = "resources/radviz16.gif";
    static String iconColor32x32Filename = "resources/radviz32.gif";
    static String iconMono16x16Filename;
    static String iconMono32x32Filename;
    
    public RadVizBeanInfo() {
    }
    
    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = new PropertyDescriptor[] {};
        return pds;
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
    
    public BeanInfo[] getAdditionalBeanInfo() {
        Class superclass = beanClass.getSuperclass();
        try {
            BeanInfo superBeanInfo = Introspector.getBeanInfo(superclass);
            return new BeanInfo[] {superBeanInfo};
        }
        catch (IntrospectionException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
