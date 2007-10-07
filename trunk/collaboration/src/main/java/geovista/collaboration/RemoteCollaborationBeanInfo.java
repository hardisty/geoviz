/* -------------------------------------------------------------------
 Java source file for the class RemoteCollaborationBeanInfo
 Original Authors: Linna Li and Frank Hardisty
 $Author: hardisty $
 $Id: RemoteCollaborationBeanInfo.java,v 1.2 2006/02/27 16:25:06 hardisty Exp $
 $Date: 2006/02/27 16:25:06 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */


package geovista.collaboration;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class RemoteCollaborationBeanInfo
    extends SimpleBeanInfo {
  Class beanClass = RemoteCollaboration.class;
  static String iconColor16x16Filename = "resources/CollaborationIcon16.GIF";
  static String iconColor32x32Filename = "resources/CollaborationIcon32.GIF";
  String iconMono16x16Filename;
  String iconMono32x32Filename;

  public RemoteCollaborationBeanInfo() {
  }

  public PropertyDescriptor[] getPropertyDescriptors() {
    PropertyDescriptor[] pds = new PropertyDescriptor[] {};
    return pds;
  }

  public Image getIcon(int iconKind) {
    switch (iconKind) {
      case BeanInfo.ICON_COLOR_16x16:
        return ( (iconColor16x16Filename != null)
                ? loadImage(iconColor16x16Filename) : null);

      case BeanInfo.ICON_COLOR_32x32:
        return ( (iconColor32x32Filename != null)
                ? loadImage(iconColor32x32Filename) : null);

      case BeanInfo.ICON_MONO_16x16:
        return ( (iconMono16x16Filename != null)
                ? loadImage(iconMono16x16Filename) : null);

      case BeanInfo.ICON_MONO_32x32:
        return ( (iconMono32x32Filename != null)
                ? loadImage(iconMono32x32Filename) : null);
    }

    return null;
  }
}
