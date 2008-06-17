/* -------------------------------------------------------------------
 Java source file for the class CartogramPicker
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramGeoMap.java,v 1.9 2005/12/05 20:35:36 hardistf Exp $
 $Date: 2005/12/05 20:35:36 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.cartogram;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import geovista.common.data.DataSetForApps;
import geovista.readers.shapefile.example.GeoDataGeneralizedStates;


/*
 * This class like a GeoMap but it makes cartograms
 */

public class CartogramPicker
    extends JPanel implements ActionListener {

  DataSetForApps dataSet;
  String inputFileName;
  CartogramPreferences preferencesFrame;
  boolean DEBUG = true;
  TransformsMain trans;
  JButton showPrefs;
  JProgressBar pBar;
  protected  JComboBox variableCombo;
  JButton sendShapes;
  //private ActionListener parent;

  public CartogramPicker() {
    showPrefs = new JButton("Settings");
    this.add(showPrefs);
    //showPrefs.addActionListener(this);//parent listens instead

    variableCombo = new JComboBox();
    this.add(variableCombo);
    //variableCombo.addActionListener(this);//parent listens instead

    pBar = new JProgressBar();
    this.add(pBar);

    sendShapes = new JButton("Send");
    this.add(sendShapes);


  }

  public int getSelectedIndex() {
    return this.variableCombo.getSelectedIndex();
  }

  public JProgressBar getProgressBar() {
    return this.pBar;
  }

  public void actionPerformed(ActionEvent e) {

  }

  
  
  public void setDataSet(DataSetForApps dataIn) {
    this.dataSet = dataIn;
    this.setVariableNames(dataSet.getAttributeNamesNumeric());
    if (dataSet.getNumberNumericAttributes() > 1) {
      this.variableCombo.setSelectedIndex(0);
    }
  }

  public void setVariableNames(String[] variableNames) {
//  this.variableNames = variableNames;
    this.variableCombo.removeAllItems();

    for (int i = 0; i < variableNames.length; i++) {
      this.variableCombo.addItem(variableNames[i]);
    }
  }

  public static void main(String[] args) {
    CartogramPicker gui = new CartogramPicker();
    JFrame frame = new JFrame(
        "CartogramPicker");
    frame.getContentPane().add(gui);
   GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();
    gui.setDataSet(stateData.getDataForApps());
    frame.pack();
    frame.setVisible(true);
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

  }

  public JComboBox getVariableCombo() {
    return variableCombo;
  }

  public JButton getSendShapes() {
    return sendShapes;
  }

  public JButton getPreferencesButton() {
    return this.showPrefs;
  }

}
