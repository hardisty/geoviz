package geovista.colorbrewer.coloreffect;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Datacontrol extends JPanel implements ChangeListener{
	protected final static Logger logger = Logger.getLogger(Datacontrol.class.getName());
  //create a pair of slider and spinner to allow the data control
  JSlider slider1 = new JSlider();

  JSpinner spinner1 = new JSpinner();

  //create a titled borderprotected final static Logger logger = Logger.getLogger(M.class.getName());
  TitledBorder title = BorderFactory.createTitledBorder("title");

  public Datacontrol() {

    //customize the slider and the textfield
    this.slider1.setPreferredSize(new Dimension(90, 20));

    this.spinner1.setPreferredSize(new Dimension(50, 20));
    //setting titled border
    this.setBorder(title);

    //customize the data input
    this.setLayout(new BorderLayout());
    this.add(slider1, BorderLayout.WEST);
    this.add(spinner1, BorderLayout.EAST);

    //add listeners
    this.spinner1.addChangeListener(this);
    this.slider1.addChangeListener(this);
  }

  //a customized function to set the name of the control
  public void setTitle(String title){
    this.title = BorderFactory.createTitledBorder(title);
    this.setBorder(this.title);
  }

  public void stateChanged(ChangeEvent e) {
    if(e.getSource() == this.spinner1){
      this.slider1.setValue(Integer.valueOf(String.valueOf(this.spinner1.getValue())).intValue());
      logger.finest("spinner feeds to slider");
    }

    if(e.getSource() == this.slider1){
      if(this.slider1.getValueIsAdjusting() == false){
        Integer integer1 = new Integer(this.slider1.getValue());
        this.spinner1.setValue(integer1);
        logger.finest("slider feeds to spinner");
      }
    }
  }

}