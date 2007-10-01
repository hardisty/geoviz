/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ListeningBean
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ListeningBean.java,v 1.2 2003/04/25 17:58:13 hardisty Exp $
 $Date: 2003/04/25 17:58:13 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.coordination;

import java.awt.Image;


/**
 * This class represents the event receiving role of an object being managed 
 * by a coordination manager.
 * 
 * A particular object will be represented as both a FiringBean and as a ListeningBean.
 * 
 * @see CoordinationManager
 * @author Frank Hardisty
 */
public class ListeningBean {
  public static final int STATUS_LISTENING = 0;
  public static final int STATUS_CANT_LISTEN = 1; //this is a non-listening state
  public static final int STATUS_WONT_LISTEN = 2; //this is a non-listening state
  private int position = -1; //position in array of FiringBeans held by DefaultCoordinator
  private String beanName;
  private Object originalBean;
  private int listeningStatus;
  private Image icon;

  /**
   *
  */
  public ListeningBean() {
  }

  public String getBeanName() {
    return beanName;
  }
  //for those boolean times
  public boolean isListening(){
    return listeningStatus > 0;
  }
  public int getListeningStatus() {
    int i = this.listeningStatus;
    return i;
  }

  public Object getOriginalBean() {
    return originalBean;
  }

  public int getPosition() {
    return position;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public void setListeningStatus(int listeningStatus) {
    this.listeningStatus = listeningStatus;
  }

  public void setOriginalBean(Object originalBean) {
    this.originalBean = originalBean;

    String name = originalBean.getClass().getName();

    //name = name.substring(clazz.getPackage().getName().length() + 1);//no finding package in applets!
    //look for nearest period
    int periodPlace = name.lastIndexOf(".");
    name = name.substring(periodPlace + 1);
    this.setBeanName(name);
    this.icon = CoordinationUtils.findIcon(originalBean);
  }

  public void setPosition(int position) {
    this.position = position;
  }

  public Image getIcon() {
    return icon;
  }

  public void setIcon(Image icon) {
    this.icon = icon;
  }
}