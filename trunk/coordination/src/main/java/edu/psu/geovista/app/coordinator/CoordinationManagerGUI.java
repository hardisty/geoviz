/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class CoordinationManagerGUI
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: CoordinationManagerGUI.java,v 1.3 2005/02/12 17:42:36 hardisty Exp $
 $Date: 2005/02/12 17:42:36 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *
 */
package edu.psu.geovista.app.coordinator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


//this class maintains the GUI
//it examines beans on the way in and out to make changes to itself
//CoordinationManager maintains the state of the beans and their connections
//it gets messages from the GUI if the user requests changes to the connections
public class CoordinationManagerGUI extends JScrollPane implements ActionListener{
  private transient CoordinationManager cm;
  transient private JPanel boxHolder;
  transient private JPanel shortcutsHolder;
  transient private JPanel allHolder;
  protected final static Logger logger = Logger.getLogger(CoordinationManagerGUI.class.getName());
  //private
  public CoordinationManagerGUI() {
    super();

    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void createShortCuts() {
    //this.shortcutsHolder = new JPanel();

    FiringBean[] fBeans = cm.getFiringBeans();
    FiringBean newBean = fBeans[fBeans.length - 1];
    Icon ic = newBean.getIcon();
    BeanShortcutsGUI bGUI = new BeanShortcutsGUI(newBean.getBeanName(), ic);
    this.shortcutsHolder.add(bGUI);
    bGUI.addActionListener(this);
    this.revalidate();

  }

  public void createBoxes() {
    this.boxHolder.removeAll();
    FiringBean[] fBeans = cm.getFiringBeans();
    for (int i = 0; i < fBeans.length; i++){
      Object whichBean = fBeans[i].getOriginalBean();
      ListeningBean lBean = new ListeningBean();
      lBean.setOriginalBean(whichBean);
      ListeningBeanGUI lGUI = new ListeningBeanGUI(lBean,cm);
      this.boxHolder.add(lGUI);
    }

  }

  public void validateBoxes() {
  }

  public void addBean(Object beanIn) {
    this.cm.addBean(beanIn);

    //XXXthese are being commented out because gui creation is too expensive
    //TODO: replace the functionality represented by the three lines of
    //code below with something more efficient.

    //createShortCuts();
    //createBoxes();
    //validateBoxes();
  }

  public void removeBean(Object oldBean) {
    this.cm.removeBean(oldBean);
  }

  private void jbInit() throws Exception {
    this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    this.setViewportBorder(BorderFactory.createLineBorder(Color.black));
    this.allHolder = new JPanel();
    this.allHolder.setLayout(new BoxLayout(this.allHolder,BoxLayout.Y_AXIS));




    this.cm = new CoordinationManager();
    this.boxHolder = new JPanel();
    boxHolder.setLayout(new BoxLayout(this.boxHolder, BoxLayout.Y_AXIS));
    this.allHolder.add(boxHolder);
    this.getViewport().add(allHolder);
    this.setPreferredSize(new Dimension(120, 200));
    this.setMinimumSize(new Dimension(120, 200));

    this.shortcutsHolder = new JPanel();
    this.shortcutsHolder.setLayout(new BoxLayout(this.shortcutsHolder,BoxLayout.Y_AXIS));
    this.shortcutsHolder.add(new JLabel("Shortcuts Holder"));
    this.allHolder.add(shortcutsHolder);
  }

  public void actionPerformed(ActionEvent e){
    String cmd = e.getActionCommand();
    if (cmd == BeanShortcutsGUI.EVENT_INCOMING_ON){
      logger.finest(BeanShortcutsGUI.EVENT_INCOMING_ON);
    } else if (cmd == BeanShortcutsGUI.EVENT_INCOMING_OFF){
      logger.finest(BeanShortcutsGUI.EVENT_INCOMING_OFF);
    } else if(cmd == BeanShortcutsGUI.EVENT_OUTGOING_ON){
      logger.finest(BeanShortcutsGUI.EVENT_OUTGOING_ON);
    } else if(cmd == BeanShortcutsGUI.EVENT_OUTGOING_OFF){
      logger.finest(BeanShortcutsGUI.EVENT_OUTGOING_OFF);
    }
  }

  public CoordinationManager getCm() {
    return cm;
  }
}
