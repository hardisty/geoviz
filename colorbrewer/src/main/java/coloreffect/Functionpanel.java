package coloreffect;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Functionpanel extends JPanel{

  //create a titled border
  TitledBorder title = BorderFactory.createTitledBorder("title");

  public Functionpanel() {
    //customize the slider and the textfield
    this.setPreferredSize(new Dimension(330, 45));

    //setting titled border
    this.setBorder(title);

  }

  //a customized function to set the name of the control
  public void setTitle(String title){
    this.title = BorderFactory.createTitledBorder(title);
    this.setBorder(this.title);
  }

}