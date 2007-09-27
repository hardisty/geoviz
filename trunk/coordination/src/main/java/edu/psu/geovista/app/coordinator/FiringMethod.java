/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class FiringMethod
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: FiringMethod.java,v 1.2 2003/04/25 17:58:13 hardisty Exp $
 $Date: 2003/04/25 17:58:13 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.app.coordinator;

import java.lang.reflect.Method;
import java.util.Vector;


/**
 * This class represents a particular method which is used by firing.
 * 
 * A particular FiringBean will have one to many FiringMethod instances.
 * 
 * @see FiringBean
 * @author Frank Hardisty
 */
public class FiringMethod implements Comparable {
  private int position = -1; //position in parent array of FiringMethods held by FiringBean
  private int parentBeanPosition = -1;
  private String methodName;
  private Method originalAddMethod;
  private Method originalRemoveMethod;
  private Object parentBean;
  transient private FiringBean fBean;
  private ListeningBean[] listeners;
  private Class listeningInterface;

  /**
  */
  public FiringMethod() {
    this.listeners = new ListeningBean[0];
  }

  public int compareTo(Object obj) {
    FiringMethod otherMeth = (FiringMethod) obj;
    String myInterfaceName = this.listeningInterface.getName();
    String otherInterfaceName = otherMeth.listeningInterface.getName();

    return myInterfaceName.compareTo(otherInterfaceName);
  }

  public void disableAllBeans() {
    for (int i = 0; i < listeners.length; i++) {
      //find and set its listening status
      Class listenerInterface = this.checkForListening(listeners[i]);

      if (listenerInterface != null) {
        this.deregisterListener(listeners[i], listenerInterface,
                                this.parentBean);
        this.listeners[i].setListeningStatus(ListeningBean.STATUS_WONT_LISTEN);
      }
    }
  }

  public void registerListener(ListeningBean lBean, Class interf,
                               Object firingBean) {
    //listenerInterface.for
    //Object[] args = new Object[1];
    //listenerInterface[] args = new listenerInterface.class[1];
    //System.out.println("I'm gonna register you.... right now!!!");
    //System.out.println(interf.getName());
    //args[0] = lBean;
    try {
      Object[] args = new Object[1];
      args[0] = lBean.getOriginalBean();
      originalAddMethod.invoke(firingBean, args);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void deregisterListener(ListeningBean lBean, Class interf,
                                 Object firingBean) {
    //listenerInterface.for
    //Object[] args = new Object[1];
    //listenerInterface[] args = new listenerInterface.class[1];
    //args[0] = lBean;
    try {
      Object[] args = new Object[1];
      args[0] = lBean.getOriginalBean();
      originalRemoveMethod.invoke(firingBean, args);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void addListeningBean(ListeningBean lBean) {
    ListeningBean newBean = new ListeningBean();
    newBean.setOriginalBean(lBean.getOriginalBean());

    //first, if this bean is the same as the owning bean, don't add it
    if (newBean.getPosition() == this.getParentBeanPosition()) {
      System.out.println("FiringMethod.addListeningBean, found self");

      return;
    }

    //find and set its listening status
    Class listenerInterface = this.checkForListening(newBean);

    if (listenerInterface != null) {
      newBean.setListeningStatus(ListeningBean.STATUS_LISTENING);
      this.registerListener(newBean, listenerInterface, this.parentBean);
    } else {
      newBean.setListeningStatus(ListeningBean.STATUS_CANT_LISTEN);
    }

    this.increaseArraySize();
    this.listeners[listeners.length - 1] = newBean;
  }

  public void removeListeningBean(int oldBeanPositionInListenersArray) {
    if (this.parentBeanPosition > oldBeanPositionInListenersArray) {
      this.parentBeanPosition = parentBeanPosition - 1;
    }

    //find and set its listening status
    Class listenerInterface = this.checkForListening(
                                  listeners[oldBeanPositionInListenersArray]);

    if (listenerInterface != null) {
      this.deregisterListener(listeners[oldBeanPositionInListenersArray],
                              listenerInterface, this.parentBean);
    }

    this.decreaseArraySize(oldBeanPositionInListenersArray);
  }

  private void increaseArraySize() {
    ListeningBean[] tempBeans = new ListeningBean[listeners.length + 1];

    for (int i = 0; i < listeners.length; i++) {
      tempBeans[i] = listeners[i];
    }

    listeners = tempBeans;
  }

  private void decreaseArraySize(int arrayPosition) {
    ListeningBean[] tempBeans = new ListeningBean[listeners.length - 1];

    for (int i = 0; i < arrayPosition; i++) {
      tempBeans[i] = listeners[i];
    }

    for (int i = arrayPosition; i < tempBeans.length; i++) {
      tempBeans[i] = listeners[i + 1];
    }

    this.listeners = tempBeans;
  }

  private Class checkForListening(ListeningBean listener) {
    Vector inters = new Vector();
    inters = this.findInterfaces(listener.getOriginalBean().getClass(), inters);

    for (int i = 0; i < inters.size(); i++) {
      Class c = (Class) inters.get(i);

      if (c.equals(this.listeningInterface)) {
        //it passes so...
        return c;
      }
    }

    return null;
  }

  private Vector findInterfaces(Class clazz, Vector v) {
    if (clazz == Object.class) {
      return v;
    }

    Class[] interfs = clazz.getInterfaces();

    for (int i = 0; i < interfs.length; i++) {
      v.add(interfs[i]);
    }

    Class base = clazz.getSuperclass();

    if (base != null) {
      findInterfaces(base, v);
    }

    return v;
  }

  public boolean listeningBeanOccurs(ListeningBean lBean) {
    boolean occurs = false;

    for (int i = 0; i < this.listeners.length; i++) {
      if (lBean.getOriginalBean() == this.listeners[i].getOriginalBean()) {
        if (this.listeners[i].getListeningStatus() != ListeningBean.STATUS_CANT_LISTEN) {
          occurs = true;
        }
      }
    }

    return occurs;
  }

  //begin accessors
  public String getMethodName() {
    return methodName;
  }

  public int getPosition() {
    return position;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public ListeningBean[] getListeners() {
    return listeners;
  }

  public void setListeners(ListeningBean[] listeners) {
    this.listeners = listeners;
  }

  public int getParentBeanPosition() {
    return parentBeanPosition;
  }

  public void setParentBeanPosition(int parentBeanPosition) {
    this.parentBeanPosition = parentBeanPosition;
  }

  public Object getParentBean() {
    return parentBean;
  }

  public void setParentBean(Object parentBean) {
    this.parentBean = parentBean;
  }

  public void setFiringBean(FiringBean fBean) {
    this.fBean = fBean;
  }

  public FiringBean getFiringBean() {
    return this.fBean;
  }

  public Method getOriginalRemoveMethod() {
    return originalRemoveMethod;
  }

  public void setOriginalRemoveMethod(Method originalRemoveMethod) {
    this.originalRemoveMethod = originalRemoveMethod;
  }

  public Method getOriginalAddMethod() {
    return originalAddMethod;
  }

  public void setOriginalAddMethod(Method originalAddMethod) {
    this.originalAddMethod = originalAddMethod;
  }

  public Class getListeningInterface() {
    return listeningInterface;
  }

  public void setListeningInterface(Class listeningInterface) {
    this.listeningInterface = listeningInterface;
  }

  //end accessors
}