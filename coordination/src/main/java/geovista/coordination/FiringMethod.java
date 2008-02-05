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
package geovista.coordination;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
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
 
  private String methodName;
  private Method originalAddMethod;
  private Method originalRemoveMethod;
  private Object parentBean;
  transient private FiringBean fBean;
  private HashSet <ListeningBean> listeners;
  private Class listeningInterface;

  /**
  */
  public FiringMethod() {
    this.listeners = new HashSet <ListeningBean>();
  }

  public int compareTo(Object obj) {
    FiringMethod otherMeth = (FiringMethod) obj;
    String myInterfaceName = this.listeningInterface.getName();
    String otherInterfaceName = otherMeth.listeningInterface.getName();

    return myInterfaceName.compareTo(otherInterfaceName);
  }

  public void disableAllBeans() {
		Iterator<ListeningBean> it = listeners.iterator();

		while (it.hasNext()) {
			ListeningBean lBean = it.next();
		      //find and set its listening status
		      Class listenerInterface = this.checkForListening(lBean);
		      if (listenerInterface != null) {
		          this.deregisterListener(lBean, listenerInterface,
		                                  this.parentBean);
		          lBean.setListeningStatus(ListeningBean.STATUS_WONT_LISTEN);
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
    if (newBean.getOriginalBean() == this.parentBean) {
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


    this.listeners.add(lBean);
  }
  
  
  
  
  public void removeListeningBean(Object oldBean) {
	  
	 ListeningBean lBean = this.findListeningBean(oldBean);
	 if(lBean == null){
		 return;
	 }
	  
    //find and set its listening status
    Class listenerInterface = this.checkForListening(lBean);

    if (listenerInterface != null) {
      this.deregisterListener(lBean,listenerInterface, this.parentBean);
    }
    this.listeners.remove(lBean);
    //this.decreaseArraySize(oldBeanPositionInListenersArray);
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
	Iterator<ListeningBean> it = listeners.iterator();

	while (it.hasNext()) {
		ListeningBean listBean = it.next();
		if (lBean.getOriginalBean() == listBean.getOriginalBean()){
			if (lBean.getListeningStatus() != ListeningBean.STATUS_CANT_LISTEN){
				occurs = true;
			}
		}
	}	
    return occurs;
  }
  
  
  public ListeningBean findListeningBean(Object bean) {
	  ListeningBean lBean = null;

		Iterator<ListeningBean> it = listeners.iterator();

		while (it.hasNext()) {
			ListeningBean listBean = it.next();
			if (bean == listBean.getOriginalBean()){
				return listBean;
			}
		}	
	    return lBean;
	  }
  

  //begin accessors
  public String getMethodName() {
    return methodName;
  }


  public void setMethodName(String methodName) {
    this.methodName = methodName;
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