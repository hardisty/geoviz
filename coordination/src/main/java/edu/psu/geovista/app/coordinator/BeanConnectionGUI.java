/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class FiringBeanGUI
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: BeanConnectionGUI.java,v 1.2 2003/04/25 17:58:13 hardisty Exp $
 $Date: 2003/04/25 17:58:13 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  */
package edu.psu.geovista.app.coordinator;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *  This subclass of JPanel is responsible for communicating between the
 *  user and an instance of CoordinationManager concerning the status of
 *  one FiringBean -> ListeningBean connection for one type of coordinated
 *  event. It has a JCheckBox.
 */
public class BeanConnectionGUI extends JPanel implements ItemListener {
  private transient ListeningBean lBean;
  private transient FiringMethod meth; //contains reference to parent FiringBean
  private transient CoordinationManager cm;
  private JCheckBox cBox;

  /**
   *
  */
  public BeanConnectionGUI(ListeningBean lBean, FiringMethod meth,
                           CoordinationManager cm) {
    FiringBean fBean = meth.getFiringBean();
    this.cBox = new JCheckBox();
    //cBox.setAlignmentX(Component.LEFT_ALIGNMENT);
    cBox.setSelected(true);

    JLabel lab = new JLabel(fBean.getBeanName(), fBean.getSmallIcon(),
                            JLabel.LEFT);

    this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
    this.add(cBox);
    this.add(lab);
    this.lBean = lBean;
    this.meth = meth;
    this.cm = cm;
    //this.setAlignmentX(Component.LEFT_ALIGNMENT);
    cBox.addItemListener(this);
  }

  /** We listen to our box*/
  public void itemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.DESELECTED) {
      cm.disconnectBeans(this.meth,this.lBean);
    } else if (e.getStateChange() == ItemEvent.SELECTED) {
      cm.reconnectBeans(this.meth,this.lBean);
    }
  }

public JCheckBox getCBox() {
	return cBox;
}

public void setCBox(JCheckBox box) {
	cBox = box;
}
}