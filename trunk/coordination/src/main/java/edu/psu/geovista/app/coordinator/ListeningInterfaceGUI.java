/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ListeningInterfaceGUI
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ListeningInterfaceGUI.java,v 1.2 2003/04/25 17:58:13 hardisty Exp $
 $Date: 2003/04/25 17:58:13 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  */
package edu.psu.geovista.app.coordinator;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;


/**
 *  This class holds the BeanConnectionGUI that allow
 *  user  to connect or disconnect the
 *  incoming or outgoing coordinated connections for this bean. The original
 *  design calls for a ListeningBeanGUI to hold a reference to one of
 *  these per type of method the bean can listen for.
 *  
 * @see CoordinationManager
 * @author Frank Hardisty
 */
public class ListeningInterfaceGUI extends JPanel implements ActionListener {

  private transient BeanConnectionGUI[] connectionBoxen;
  private transient JPanel boxenPanel;
  /**
   *
  */
  public ListeningInterfaceGUI(ListeningBean lBean, FiringMethod[] meths,
                               CoordinationManager cm) {
    this.setBorder(BorderFactory.createLineBorder(Color.blue));
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel preTitle = new JLabel("Event Type:");
    preTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.add(preTitle);
    String interfaceName = CoordinationUtils.findInterfaceName(meths[0].getListeningInterface());
    JLabel title = new JLabel(interfaceName, JLabel.CENTER);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    this.add(title);
    boxenPanel = new JPanel();
    boxenPanel.setLayout(new BoxLayout(boxenPanel, BoxLayout.Y_AXIS));
    this.connectionBoxen = new BeanConnectionGUI[meths.length];
    for (int i = 0; i < meths.length; i++){
      this.connectionBoxen[i] = new BeanConnectionGUI(lBean, meths[i],cm);
      this.connectionBoxen[i].setAlignmentX(Component.LEFT_ALIGNMENT);
      this.connectionBoxen[i].setBorder(BorderFactory.createLineBorder(Color.white));
      this.boxenPanel.add(connectionBoxen[i]);
    }

    //this.connectionBoxen[0].addActionListener(this);


    //Font f = new Font("", Font.PLAIN, 10); //decreasing the size to 10 point

    this.add(boxenPanel);
  }

  /** Listens to the buttons*/
  public void actionPerformed(ActionEvent e) {


  }

  /**
   * adds an ActionListener, e.g. CoordinationManagerGUI
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * removes an ActionListener, e.g. CoordinationManagerGUI
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
  * Notify all listeners that have registered interest for
  * notification on this event type.
  * @see EventListenerList
  */
  protected void fireActionPerformed(String command) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ActionEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ActionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        }

        ((ActionListener) listeners[i + 1]).actionPerformed(e);
      }
    } //next listener
  } //end method
}