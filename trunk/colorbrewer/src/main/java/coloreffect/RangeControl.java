package coloreffect;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.psu.geovista.ui.slider.MultiSlider;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RangeControl extends JPanel implements ChangeListener{

  //create a MultiSlider and a pair of spinners to allow the data control
  MultiSlider multiSlider1 = new MultiSlider(0, 0, 100, 2);

  JSpinner leftSpinner = new JSpinner();

  JSpinner rightSpinner = new JSpinner();

  //create a titled border
  TitledBorder title = BorderFactory.createTitledBorder("title");

  public RangeControl() {

    //customize the slider and the textfield
    this.multiSlider1.setPreferredSize(new Dimension(200, 20));
    this.leftSpinner.setPreferredSize(new Dimension(45, 20));
    this.rightSpinner.setPreferredSize(new Dimension(45, 20));

    //setting titled border
    this.setBorder(title);

    //customize the data input
    this.setLayout(new BorderLayout());
    this.add(leftSpinner, BorderLayout.WEST);
    this.add(multiSlider1, BorderLayout.CENTER);
    this.add(rightSpinner, BorderLayout.EAST);

    //add listeners
    this.leftSpinner.addChangeListener(this);
    this.rightSpinner.addChangeListener(this);
    this.multiSlider1.addChangeListener(this);
  }

  //a customized function to set the name of the control
  public void setTitle(String title){
    this.title = BorderFactory.createTitledBorder(title);
    this.setBorder(this.title);
  }

  public void stateChanged(ChangeEvent e) {
    if(e.getSource() == this.leftSpinner){
      this.multiSlider1.setValueAt(0, Integer.valueOf(String.valueOf(this.leftSpinner.getValue())).intValue());
    }

    if(e.getSource() == this.rightSpinner){
      this.multiSlider1.setValueAt(1, Integer.valueOf(String.valueOf(this.rightSpinner.getValue())).intValue());
    }

    if(e.getSource() == this.multiSlider1){
      if(this.multiSlider1.getValueIsAdjusting() == false){
        Integer left = new Integer(this.multiSlider1.getValueAt(0));
        Integer right = new Integer(this.multiSlider1.getValueAt(1));
        //always set the greater value to the right and less value to the left
        if(left.doubleValue() < right.doubleValue()){
          this.leftSpinner.setValue(left);
          this.rightSpinner.setValue(right);
        }
        if(left.doubleValue() >= right.doubleValue()){
          this.leftSpinner.setValue(right);
          this.rightSpinner.setValue(left);
        }
      }
    }
  }

}