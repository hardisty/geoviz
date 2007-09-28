/* -------------------------------------------------------------------
 Java source file for the class GuiUtils
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: GuiUtils.java,v 1.1 2005/12/05 20:17:06 hardistf Exp $
 $Date: 2005/12/05 20:17:06 $
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

package edu.psu.geovista.cartogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;

import edu.psu.geovista.app.map.GeoMapUni;
import edu.psu.geovista.data.sample.GeoDataGeneralizedStates;
import edu.psu.geovista.db.dbase.DBaseFile;
import edu.psu.geovista.io.util.MyFileFilter;
import edu.psu.geovista.visclass.VisualClassifier;


//import org.davekeen.gis.shapefile.GeoESRIWriter;



/*
 * This class holds refactored GUI methods
 */

public class GuiUtils
    extends JPanel {
    final static Logger logger = Logger.getLogger(GuiUtils.class.getName());
  public static String chooseOutputFilename(Component comp) {
    Preferences gvPrefs = Preferences.userNodeForPackage(comp.getClass());

    try {
      LookAndFeel laf = UIManager.getLookAndFeel();
      //this is systemUI (MS32 or whatever)
      UIManager.setLookAndFeel(UIManager.
                               getSystemLookAndFeelClassName());

      String defaultDir = gvPrefs.get("LastGoodOutputDirectory", "");

      JFileChooser fileChooser = new JFileChooser(defaultDir);

      MyFileFilter fileFilter = new MyFileFilter(new String[] {"shp"});
      fileChooser.setFileFilter(fileFilter);
      int returnVal = fileChooser.showOpenDialog(comp);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = null;
        file = fileChooser.getSelectedFile();
        String fileName = file.getAbsolutePath();

        gvPrefs.put("LastGoodOutputDirectory", fileName);
        UIManager.setLookAndFeel(laf); //UI back to whatever we were
        return fileName;
      }

      UIManager.setLookAndFeel(laf); //UI back to whatever we were

    }
    catch (Exception ex) {
      //something bad happened, reset
      gvPrefs.put("LastGoodOutputDirectory", "");
      ex.printStackTrace();
    }
    return null;
  }

  public static String chooseInputFilename(Component comp) {
    Preferences gvPrefs = Preferences.userNodeForPackage(comp.getClass());

    try {
      LookAndFeel laf = UIManager.getLookAndFeel();
      //this is systemUI (MS32 or whatever)
      UIManager.setLookAndFeel(UIManager.
                               getSystemLookAndFeelClassName());

      String defaultDir = gvPrefs.get("LastGoodInputDirectory", "");

      JFileChooser fileChooser = new JFileChooser(defaultDir);

      MyFileFilter fileFilter = new MyFileFilter(new String[] {"shp"});
      fileChooser.setFileFilter(fileFilter);
      int returnVal = fileChooser.showOpenDialog(comp);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = null;
        file = fileChooser.getSelectedFile();
        String fileName = file.getAbsolutePath();

        gvPrefs.put("LastGoodInputDirectory", fileName);
        UIManager.setLookAndFeel(laf); //UI back to whatever we were
        return fileName;

      }

      UIManager.setLookAndFeel(laf); //UI back to whatever we were

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /*
   * Connects the two maps for indication and selection.
   */
  public static void connectMaps(GeoMapUni mapInput, GeoMapUni mapOutput) {
    mapInput.addIndicationListener(mapOutput);
    mapInput.addSelectionListener(mapOutput);
    mapOutput.addIndicationListener(mapInput);
    mapOutput.addSelectionListener(mapInput);
    //mapInput.getVisClassOne().addActionListener(mapOutput.getVisClassOne());
    //mapOutput.getVisClassOne().addActionListener(mapInput.getVisClassOne());
  }

  public static JPanel createInputPanel(GeoMapUni mapInput,
                                        JLabel inputFileNameLabel,
                                        JButton chooseInputFileButton,
                                        JTextField inputFileTextField) {
    JPanel inputPanel = new JPanel();
    JPanel pickerPanel = new JPanel();
    //mapInput.setBackground(Color.white);

    //mapInput.setPreferredSize(mapSize);
    inputFileNameLabel = new JLabel();
    inputFileNameLabel.setText("Input File Name:");
    chooseInputFileButton.setText("Choose");
    Dimension mapSize = new Dimension(200, 150);
    mapInput.setMinimumSize(mapSize);
    Border border = BorderFactory.createLineBorder(Color.black);
    inputPanel.setBorder(border);
    pickerPanel.add(chooseInputFileButton);
    pickerPanel.add(inputFileNameLabel);
    pickerPanel.add(inputFileTextField);
    Dimension pickerPanelSize = new Dimension(200, 60);
    pickerPanel.setPreferredSize(pickerPanelSize);
    inputPanel.setLayout(new BorderLayout());
    inputPanel.add(mapInput, BorderLayout.CENTER);
    inputPanel.add(pickerPanel, BorderLayout.SOUTH);
    return inputPanel;

  }

  public static JPanel createOutputPanel(
      JLabel outputFileNameLabel,
      JButton chooseOutputFileButton,
      JTextField outputFileTextField, String borderTitle) {
    //JPanel outputPanel = new JPanel();
    JPanel pickerPanel = new JPanel();

    outputFileNameLabel = new JLabel();
    chooseOutputFileButton.setText("Choose");
    //chooseOutputFileButton.setPreferredSize(new Dimension(100,30));

    Border border = BorderFactory.createTitledBorder(borderTitle);
    outputFileTextField.setText("A file name");
    pickerPanel.setBorder(border);
    pickerPanel.add(chooseOutputFileButton);
    pickerPanel.add(outputFileNameLabel);
    pickerPanel.add(outputFileTextField);
    //Dimension pickerPanelSize = new Dimension(200, 60);
    //pickerPanel.setPreferredSize(pickerPanelSize);
    //outputPanel.setLayout(new BorderLayout());

    //outputPanel.add(pickerPanel, BorderLayout.SOUTH);
    return pickerPanel;

  }

  /*
   * Disconnects the two maps for indication and selection.
   */

  public static void disconnectMaps(GeoMapUni mapInput, GeoMapUni mapOutput) {
    mapInput.removeIndicationListener(mapOutput);
    mapInput.removeSelectionListener(mapOutput);
    mapOutput.removeIndicationListener(mapInput);
    mapOutput.removeSelectionListener(mapInput);
  }

  /*
   * If we don't have a valid file name, we create one.
   */
  public static String writeDefaultShapefile() {
    GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();
    String fileName = System.getProperty("user.home");
    String inputFileName = fileName + "/states48.shp";
    File newDir = new File(fileName);
    newDir.mkdir();
    MapGenFile.writeShapefile(stateData.getDataForApps().getGeneralPathData(),
                              inputFileName);
    Class cl = stateData.getClass();

    InputStream in = cl.getResourceAsStream("resources/states48.dbf");

    try {
      DBaseFile dBase = new DBaseFile(in, fileName + "/states48.dbf");

  	if (logger.isLoggable(Level.INFO)){
  		logger.info("dBase length = " + dBase.getNumRecords());
  	}

    }
    catch (IOException ex) {
      ex.printStackTrace();
    }

    return inputFileName;
  }

 class VCSelectionExchanger
      implements ActionListener {
    VisualClassifier vcOne;
    VisualClassifier vcTwo;
    public VCSelectionExchanger(VisualClassifier vcOne, VisualClassifier vcTwo){
      this.vcOne = vcOne;
      this.vcTwo = vcTwo;
    }
    public void actionPerformed(ActionEvent e) {
      if ( (e.getSource() == vcOne) && e.getActionCommand().equals("SelectedVariable")) {
        int index = vcOne.getCurrVariableIndex();
        vcTwo.setCurrVariableIndex(index);
      } else if ( (e.getSource() == vcTwo) && e.getActionCommand().equals("SelectedVariable")) {
        int index = vcTwo.getCurrVariableIndex();
        vcOne.setCurrVariableIndex(index);
      }

    }
  }

  }
