/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class FiringBean
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: FiringBean.java,v 1.2 2003/04/25 17:58:13 hardisty Exp $
 $Date: 2003/04/25 17:58:13 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package geovista.coordination;

import java.awt.Image;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;


/**
 * This class represents the event broadcasting role of an object being managed 
 * by a coordination manager.
 * 
 * A particular object will be represented as both a FiringBean and as a ListeningBean.
 * 
 * @see CoordinationManager
 * @author Frank Hardisty
 */
public class FiringBean {
  //these packages are primarily for within bean communication, not
  //between bean communication. Thus, we do not allow them as default listeners.
  //if some lucky maintainer of this class wishes to add a package to this
  //array, feel free!
  private static String[] disallowedPackages = {
    "java.awt.event", "javax.swing.event", "java.beans"
  };
  private int position = -1; //position in array of FiringBeans held by CoordinationManager
  private String beanName;
  private Object originalBean;
  private FiringMethod[] methods;
  /**
  *
  */
  public FiringBean() {
  }

  public void disableAllFiringMethods() {
    for (int i = 0; i < methods.length; i++) {
      this.methods[i].disableAllBeans();
    }
  }

  public void addListeningBean(ListeningBean newBean) {
    for (int i = 0; i < methods.length; i++) {
      this.methods[i].addListeningBean(newBean);
    }
  }

  public void removeListeningBean(int oldBeanPosition) {
    if (this.position == oldBeanPosition) {
      this.disableAllFiringMethods();

      return;
    }

    if (this.position > oldBeanPosition) {
      this.position = position - 1;
    }

    int listeningBeanArrayPosition = -1;

    for (int i = 0; i < methods.length; i++) {
      listeningBeanArrayPosition = oldBeanPosition - 1;

      if (listeningBeanArrayPosition < 0) {
        listeningBeanArrayPosition = 0;
      }

      this.methods[i].removeListeningBean(listeningBeanArrayPosition); //note that here

      //listeningBeanArrayPosition is the position in the array of listeners the firing
      //method holds, not the position of the listening bean in the overall array
      //of firing beans
    }
  }

  public String getBeanName() {
    return beanName;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public FiringMethod[] getMethods() {
    return methods;
  }

  public void setMethods(FiringMethod[] methods) {
    this.methods = methods;
  }

  public Object getOriginalBean() {
    return originalBean;
  }

  public void setOriginalBean(Object originalBean) {
    this.originalBean = originalBean;

    Class clazz = originalBean.getClass();
    String name = clazz.getName();

    //name = name.substring(clazz.getPackage().getName().length() + 1);//no finding package in applets!
    //look for nearest period to end
    int periodPlace = name.lastIndexOf(".");
    name = name.substring(periodPlace + 1);


    //System.out.println(name);
    this.setBeanName(name);

    Method[] meths = findFireMethods(originalBean.getClass());
    this.methods = new FiringMethod[meths.length];

    for (int i = 0; i < meths.length; i++) {
      FiringMethod newMeth = new FiringMethod();


      //newMeth.setOriginalFireMethod(meths[i]);
      newMeth.setPosition(i);
      newMeth.setParentBeanPosition(this.position);

      String aName = meths[i].getName();

      //subtract "add"
      String methName = aName.substring(3, aName.length());


      //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
      //System.out.println("Adding firing method: " + methName);
      //System.out.println("package = " + meths[i].getParameterTypes()[0].getPackage().getName());
      newMeth.setMethodName(methName);
      newMeth.setParentBean(originalBean);
      newMeth.setFiringBean(this);
      newMeth.setListeningInterface(meths[i].getParameterTypes()[0]);
      newMeth.setOriginalAddMethod(meths[i]);

      String removeMethName = "remove" + methName;
      Method removeMethod = this.findMethod(originalBean.getClass(),
                                            removeMethName);
      newMeth.setOriginalRemoveMethod(removeMethod);

      this.methods[i] = newMeth;
    }
  }

  private Method findMethod(Class clazz, String methodName) {
    Vector meths = new Vector();
    meths = CoordinationUtils.findMethods(clazz, meths);

    for (Enumeration e = meths.elements(); e.hasMoreElements();) {
      Method m = (Method) e.nextElement();
      String s = m.getName();

      if (s.equals(methodName)) {
        return m;
      } //end if
    } // nextelement

    return null;
  } //end method

  private Method[] findFireMethods(Class clazz) {
    Vector meths = new Vector();
    meths = CoordinationUtils.findMethods(clazz, meths);

    Vector fireMethods = new Vector();

    for (Enumeration e = meths.elements(); e.hasMoreElements();) {
      Method m = (Method) e.nextElement();
      String s = m.getName();
      int j = s.indexOf("add");

      if (j == 0) {
        Class[] params = m.getParameterTypes();

        if (params.length > 0) {
          Class paramOne = params[0];

          //if the arg is an interface and matches something in the name of the "add" method,
          if (paramOne.isInterface()) {
            String paramName = paramOne.getName();
            String interfaceName = s.substring(3, s.length());

            if (paramName.indexOf(interfaceName) > 0) {
              fireMethods.add(m);
            } //end if one in the other
          } //end if is interface
        } //end if #params > 0
      } // end if j
    } //next element

    //let's remove methods which accept arguments with are objects
    //implementing certain interfaces (awt and swing, mainly)
    Vector tempMethsPackage = new Vector();

    for (int i = 0; i < fireMethods.size(); i++) {
      Method meth = (Method) fireMethods.elementAt(i);
      String packName = meth.getParameterTypes()[0].getName();
      int periodPlace = packName.lastIndexOf(".");
      String pack = packName.substring(periodPlace + 1);
      pack = packName.substring(0, periodPlace);

      //Package pack = meth.getParameterTypes()[0].getPackage();
      boolean goodPack = true;

      for (int k = 0; k < FiringBean.disallowedPackages.length; k++) {
        if (pack.equals(disallowedPackages[k])) {
          goodPack = false;
        }
      }

      if (goodPack) {
        tempMethsPackage.add(meth);
      }
    }

    fireMethods = tempMethsPackage;

    //let's remove duplicates
    Vector tempMeths = new Vector();

    for (int i = 0; i < fireMethods.size(); i++) {
      Method meth = (Method) fireMethods.elementAt(i);
      String methName = meth.getName();
      Class[] methParams = meth.getParameterTypes();
      Class paramOne = null;

      if (methParams.length > 0) {
        paramOne = methParams[0];
      }

      boolean unique = true;

      for (int j = 0; j < i; j++) {
        Method meth2 = (Method) fireMethods.elementAt(j);
        String methName2 = meth2.getName();
        Class[] methParams2 = meth2.getParameterTypes();
        Class paramOne2 = null;

        if (methParams2.length > 0) {
          paramOne2 = methParams2[0];
        }

        if (methName.equals(methName2)) {
          if (paramOne.equals(paramOne2)) {
            unique = false;
            j = fireMethods.size(); //skip the rest
          }
        }
      }

      if (unique) {
        tempMeths.add(meth);
      }
    } // next i

    fireMethods = tempMeths;

    // let's check for remove methods
    for (int i = 0; i < fireMethods.size(); i++) {
      boolean foundRemove = false;
      Method m = (Method) fireMethods.elementAt(i);
      String methName = (String) m.getName();
      methName = methName.substring(3, methName.length());

      for (int j = 0; j < meths.size(); j++) {
        Method meth = (Method) meths.elementAt(j);
        String compMethName = meth.getName();

        if (compMethName.indexOf(methName) >= 0) {
          foundRemove = true;
        } //end if
      } //next j

      if (!foundRemove) {
        fireMethods.remove(i);
      } // end if
    } // next i

    Method[] fireMeths = new Method[fireMethods.size()];

    for (int i = 0; i < fireMeths.length; i++) {
      fireMeths[i] = (Method) fireMethods.elementAt(i);
    }

    return fireMeths;
  }

  public int getPosition() {
    return position;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public Icon getIcon() {
    Image im = CoordinationUtils.findIcon(this.getOriginalBean());

    return new ImageIcon(im);
  }

  public Icon getSmallIcon() {
    Image im = CoordinationUtils.findSmallIcon(this.getOriginalBean());

    return new ImageIcon(im);
  }
}