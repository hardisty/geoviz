/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CoordinationManager
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: CoordinationManager.java,v 1.5 2005/03/24 20:34:05 hardisty Exp $
 $Date: 2005/03/24 20:34:05 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package geovista.coordination;

import java.util.ArrayList;


/**
 *  This is the normal entry point into the coordination package.
 * 
 *  This class accepts an arbitrary number of objects, and performs a
 *  default coordination based on their method signatures.
 *
 *  Objects with methods starting with addFooListener(FooListener l)
 *  and removeFooListener(FooListener l) are considered
 *  to be trying to broadcast events of type Foo.
 *  Objects that implement interfaces of type FooListener will be added to
 *  the broadcasting classes ListenerList, except for events in the "disallowedPackages"
 *  listed in the FiringBean class (such as java.awt.event and javax.swing.event).
 */
public class CoordinationManager {
  private transient FiringBean[] firingBeans;
  private transient ListeningBean[] listeningBeans;
  //these two arrays should maintain the
  //same length and "originalBean"

  /**
  * Null constructor.
  */
  public CoordinationManager() {
    firingBeans = new FiringBean[0];
    this.listeningBeans = new ListeningBean[0];
  }

//  /**
//  * Prevents this bean from firing any events.
//  */
//  private void disableAllFiringMethods(FiringBean bean) {
//    bean.disableAllFiringMethods();
//  }
//
//  /**
//  * Prevents this bean from recieving any events.
//  */
//  private void disableAllListening(ListeningBean bean) {
//    bean.setListeningStatus(ListeningBean.STATUS_WONT_LISTEN);
//  }

  /**
  * Utility method.
  */
  private void increaseArraySize() {
    FiringBean[] tempBeans = new FiringBean[firingBeans.length + 1];
    ListeningBean[] tempLBeans = new ListeningBean[firingBeans.length + 1];

    for (int i = 0; i < firingBeans.length; i++) {
      tempBeans[i] = firingBeans[i];
      tempLBeans[i] = listeningBeans[i];
    }

    firingBeans = tempBeans;
    listeningBeans = tempLBeans;
  }
  /**
  * Utility method.
  */
  private void decreaseArraySize(int position) {
    FiringBean[] tempBeans = new FiringBean[firingBeans.length - 1];
    ListeningBean[] tempLBeans = new ListeningBean[firingBeans.length - 1];

    for (int i = 0; i < position; i++) {
      tempBeans[i] = firingBeans[i];
      tempLBeans[i] = listeningBeans[i];
    }

    for (int i = position; i < tempBeans.length; i++) {
      tempBeans[i] = firingBeans[i + 1];
      tempLBeans[i] = listeningBeans[i + 1];
    }

    this.firingBeans = tempBeans;
    this.listeningBeans = tempLBeans;
  }
  /**
  * Adds bean to the lists of firing beans and to the list of listening beans.
  * Also registers this bean with previously added beans, and visa versa,
  * based on the criteria above.
  * Returns the instance of "FiringBean" that is created based on the added
  * bean, or null if the bean was previously added.
  * Also returns null if null is handed in.
  */
  public FiringBean addBean(Object beanIn) {
    int searchResult = this.findBeanIndex(beanIn);

    if (beanIn == null || searchResult >= 0) {
      return null;
    }

    FiringBean newBean = new FiringBean();
    newBean.setPosition(this.firingBeans.length);
    newBean.setOriginalBean(beanIn);
    increaseArraySize();

    addNewBean(newBean);
    findUniqueName(newBean);
    return newBean;
  }

  private void findUniqueName(FiringBean newBean) {
    Object bean = newBean.getOriginalBean();

    //how many instances of this class already?
    int numFound = 0;

    for (int i = 0; i < (firingBeans.length - 1); i++) { //-1 to skip current bean

      Object obj = firingBeans[i].getOriginalBean();

      if (obj.getClass() == bean.getClass()) {
        numFound++;
      } //end if
    } //next bean

    if (numFound > 0) {
      String beanName = newBean.getBeanName();
      numFound++; //let's count like humans
      beanName = beanName + " (" + numFound + ")";
      newBean.setBeanName(beanName);
    }
  }

  public int removeBean(Object oldBean) {
    int searchResult = this.findBeanIndex(oldBean);

    if (searchResult < 0) {
      return searchResult;
    }

    removeOldBean(searchResult);
    return searchResult;
  }

  private void removeOldBean(int position) {

    //remove this bean as a listener from all existing firing beans
    //this should also correct the "position" of each member firing bean and method
    for (int i = 0; i < firingBeans.length; i++) {
      firingBeans[i].removeListeningBean(position);
    }

    decreaseArraySize(position);
    this.decrimentAllListeners(position);
  }

  private void addNewBean(FiringBean newBean) {
    firingBeans[firingBeans.length - 1] = newBean;

    ListeningBean newListener = new ListeningBean();
    newListener.setOriginalBean(newBean.getOriginalBean());
    newListener.setPosition(firingBeans.length - 1);
    this.listeningBeans[firingBeans.length - 1] = newListener;

    //add this bean as a listener to all existing firing beans
    for (int i = 0; i < (firingBeans.length - 1); i++) {
      firingBeans[i].addListeningBean(newListener);
    }

    //next add all existing beans as listeners of the new one
    for (int i = 0; i < (firingBeans.length - 1); i++) {
      ListeningBean aListener = new ListeningBean();
      aListener.setOriginalBean(firingBeans[i].getOriginalBean());
      aListener.setPosition(i);
      newBean.addListeningBean(aListener);
    }
  }

  private void decrimentAllListeners(int beanToRemove) {
    for (int i = 0; i < this.listeningBeans.length; i++) {
      int currPos = this.listeningBeans[i].getPosition();

      if (currPos > beanToRemove) {
        currPos--;
        this.listeningBeans[i].setPosition(currPos);
      }
    }
  }

  /**
  * Compares the object handed in with the "originalBean" fields of each
  * firing bean and returns the index in that array, or -1 if the object
  * is not found.
  */
  private int findBeanIndex(Object beanIn) {
    for (int i = 0; i < firingBeans.length; i++) {
      if (beanIn == firingBeans[i].getOriginalBean()) {
        return i;
      }
    }

    return -1;
  }

  /**
  * This method returns a list of all FiringMethods which
  * reference the ListeningBean passed in.
  */
  public FiringMethod[] getFiringMethods(ListeningBean lBean) {
    FiringBean[] fBeans = this.getFiringBeans();
    ArrayList li = new ArrayList();

    //int index = this.findBeanIndex(lBean.getOriginalBean());
    for (int i = 0; i < fBeans.length; i++) {
      FiringBean fireBean = fBeans[i];
      FiringMethod[] meths = fireBean.getMethods();

      for (int j = 0; j < meths.length; j++) {
        FiringMethod meth = meths[j];
        boolean occurs = meth.listeningBeanOccurs(lBean);

        if (occurs) {
          li.add(meth);
        } //if occurs
      } //next firingMethod
    } //next firingBean

    FiringMethod[] allMeths = new FiringMethod[li.size()];

    for (int i = 0; i < li.size(); i++) {
      allMeths[i] = (FiringMethod) li.get(i);
    }

    return allMeths;
  }

  public FiringBean[] getFiringBeans() {
    return firingBeans;
  }

  public void disconnectBeans(FiringMethod meth, ListeningBean lBean) {
    meth.deregisterListener(lBean, meth.getListeningInterface(),
                            meth.getParentBean());
  }

  public void reconnectBeans(FiringMethod meth, ListeningBean lBean) {
    meth.registerListener(lBean, meth.getListeningInterface(),
                          meth.getParentBean());
  }


}
