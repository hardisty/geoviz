package edu.psu.geovista.geoviz.spreadsheet;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.SimpleBeanInfo;
import java.net.URL;

import javax.swing.ImageIcon;

import edu.psu.geovista.geoviz.spreadsheet.tools.ToolManager;
import edu.psu.geovista.geoviz.spreadsheet.util.Debug;

/*
 * Description:
 * Date: Apr 16, 2003
 * Time: 10:29:53 AM
 * @author Jin Chen
 */

public class SpreadSheetBeanBeanInfo extends SimpleBeanInfo{
    private final static Class beanClass=SpreadSheetBean.class;
    public BeanDescriptor getBeanDescriptor() {
        return new BeanDescriptor(beanClass);
    }

    public Image getIcon(int iconKind){
        if (iconKind == BeanInfo.ICON_MONO_16x16 ||
            iconKind == BeanInfo.ICON_COLOR_16x16 )
        {

          java.awt.Image img = getImage(ToolManager.IMAGES+"spreadsheet16.gif");  //root is the folder contain this class
          return img;
        }
        if (iconKind == BeanInfo.ICON_MONO_32x32 ||
            iconKind == BeanInfo.ICON_COLOR_32x32 )
        {
          //if(Debug.isDebug()) {
              String im = ToolManager.IMAGES + "spreadsheet32.gif";
              Debug.println(im);
          //}
          java.awt.Image img = getImage(ToolManager.IMAGES +"spreadsheet32.gif");
          return img;
        }
        return null;

    }  //getIcon
    // loading images
    public static Image getImage(String fullName) {
        //String fullName=IMAGES+name;
        ClassLoader cl=ToolManager.class.getClassLoader() ;
        URL url=cl.getResource(fullName);
        if (url == null) {
           
            return null;
        }
        return new ImageIcon(url).getImage() ;
    }

}
