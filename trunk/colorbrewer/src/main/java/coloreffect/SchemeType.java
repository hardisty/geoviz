package coloreffect;

import java.awt.Dimension;
import java.awt.GridLayout;

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

public class SchemeType extends JPanel{

  //create a titled border
  TitledBorder title = BorderFactory.createTitledBorder("title");

  public SchemeType() {
    //customize the slider and the textfield
    this.setPreferredSize(new Dimension(150, 200));
    this.setLayout(new GridLayout(5, 1));

    //setting titled border
    this.setBorder(title);

  }

  //a customized function to set the name of the control
  public void setTitle(String title){
    this.title = BorderFactory.createTitledBorder(title);
    this.setBorder(this.title);
  }

}