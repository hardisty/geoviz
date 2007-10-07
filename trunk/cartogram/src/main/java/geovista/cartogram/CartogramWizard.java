/* -------------------------------------------------------------------
 Java source file for the class CartogramWizard
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramWizard.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.geoviz.map.GeoMapUni;
import edu.psu.geovista.geoviz.shapefile.ShapeFileDataReader;
import edu.psu.geovista.geoviz.shapefile.ShapeFileProjection;
import edu.psu.geovista.geoviz.shapefile.ShapeFileToShape;

//wizard

//step 0: instructions
//
//step 1: choose input spatial data
//
//step 2: choose variable used to resize areas
//
//step 3: choose output file location
//
//step 4: create cartogram...wait
//
// step 5:view results

//step 6: create aux layers

/*
 * This class is the GUI and traffic cop for creating cartograms.
 */

public class CartogramWizard extends JPanel implements ActionListener {

    GeoMapUni mapInput; //this is shared between multiple panels
    GeoMapUni mapOutput; //likewise

    CartogramInputData stepOneInputData;
    CartogramChooseVariable stepTwoChooseVariable;
    CartogramOutputFile stepThreeChooseOutputFile;
    CartogramCreate stepFourCreate;
    CartogramViewResults stepFiveViewResults;
    CartogramCreateAux stepSixCreateAux;

    CartogramPreferences preferencesFrame;

    JButton buttonPrevious;
    JButton buttonNext;
    JButton buttonRun;
    JButton buttonQuit;
    JButton buttonPreferences;
    JButton buttonAbout;

    JPanel bottomPanel;
    JPanel centerPanel;

    static final int STEP_ONE_INPUT_DATA = 0;
    static final int STEP_TWO_CHOOSE_VARIABLE = 1;
    static final int STEP_THREE_CHOOSE_OUTPUT_FILE = 2;
    static final int STEP_FOUR_CREATE = 3;
    static final int STEP_FIVE_VIEW_RESULTS = 4;
    static final int STEP_SIX_CREATE_AUX = 5;

    static final boolean DEBUG = true;

    private int currStep = 0;

    private boolean useProj = false;
    private String inputFileName = "";
    private String outputFileName = "";
    private ShapeFileDataReader shpRead;
    private ShapeFileToShape shpToShape;
    private ShapeFileProjection shpProj;
    private DataSetForApps dataSet;
    private TransformsMain transforms;
    JProgressBar progressBar;

    boolean isAux;

    Console messagePane;
	@SuppressWarnings("unused")
	private String auxInputFileName;
	final static Logger logger = Logger.getLogger(CartogramWizard.class.getName());

    //GuiUtils.VCSelectionExchanger selectionExchanger;
    /**
     * CartogramWizard
     */
    public CartogramWizard() {
        isAux = false;
        makeBottomPanel();
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.centerPanel = new JPanel();
        //this.centerPanel.setPreferredSize(new Dimension(500, 300));
        this.centerPanel.setBorder(BorderFactory.createBevelBorder(0));
        this.add(centerPanel, BorderLayout.CENTER);
        this.mapInput = new GeoMapUni();

        mapInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        mapInput.setBorder(BorderFactory.createTitledBorder("Map of Input Data"));
        this.mapOutput = new GeoMapUni();

        this.stepOneInputData = new CartogramInputData(mapInput);
        this.stepOneInputData.addActionListener(this);

        this.centerPanel.add(this.stepOneInputData);
        shpRead = new ShapeFileDataReader();
        shpToShape = new ShapeFileToShape();
        shpProj = new ShapeFileProjection();
        this.transforms = new TransformsMain(false);
        this.preferencesFrame = new CartogramPreferences(
                "Cartogram Preferences", this, this.transforms);

        try {

            Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
            this.setInputFile(gvPrefs);
            this.setOutputFileName(gvPrefs);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (CartogramWizard.logger.isLoggable(Level.FINEST)) {
            try {
                new Console();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.stepOneInputData) {
            String fileName = e.getActionCommand();
            if (fileName != null) {
                readShapeFile(fileName);
            }
        } else if (e.getSource() == this.stepThreeChooseOutputFile) {
            outputFileName = e.getActionCommand();

        } else if (e.getSource().equals(this.buttonQuit)) {
            showQuitDialog();
        } else if (e.getSource().equals(this.buttonRun)) {

            boolean doIt = this.transforms.retreiveX() == null;
            if (!doIt) {
                Object[] options = {"Yes, replace", "No"};
                JOptionPane pane = new JOptionPane(
                        "Replace current cartogram field?",
                        JOptionPane.WARNING_MESSAGE);
                pane.setOptions(options);
                Dialog dia = pane.createDialog(this, "Replace?");
                dia.setVisible(true);
                if (pane.getValue() != null && pane.getValue().equals(options[0])) {
                    doIt = true;
                }

            }
            if (doIt) {
                this.currStep = CartogramWizard.STEP_FOUR_CREATE;
                this.addCurrentPanel();
                new CartogramThread(this, this.isAux).start();
            }
        } else if (e.getSource().equals(this.buttonNext)) {
            incrementStep();

        } else if (e.getSource().equals(this.buttonPrevious)) {
            decrementStep();
        } else if (e.getSource() == this.preferencesFrame.getProjCheckBox()) {
            this.useProj = this.preferencesFrame.getProjCheckBox().isSelected();
            this.readShapeFile(this.inputFileName);
        } else if (e.getSource() == this.preferencesFrame.getAuxCheckBox()) {
            this.isAux = this.preferencesFrame.getAuxCheckBox().isSelected();
        	if (logger.isLoggable(Level.FINEST)){
        		logger.finest("isAux = " + this.isAux);
        	}

        } else if (e.getSource() == this.buttonPreferences) {

            this.preferencesFrame.setVisible(true);
        } else if (e.getSource() == this.stepSixCreateAux) {
            this.auxInputFileName = e.getActionCommand();
            //this.isAux = true;
        } else if(e.getSource() == this.buttonAbout){
            this.showAboutMessage();
        }else {
        	if (logger.isLoggable(Level.WARNING)){
        		logger.warning( "CartogramWizard.actionPerformed, unexpected source encountered: " +
                        e.getSource().getClass());;
        	}

        }
        this.repaint();
    }

    private void showQuitDialog() throws HeadlessException {
      Object[] options = {"Yes, quit", "No"};
      JOptionPane pane = new JOptionPane("Really Quit?",
                                         JOptionPane.WARNING_MESSAGE);
      pane.setOptions(options);
      Dialog dia = pane.createDialog(this, "Quit?");
      dia.setVisible(true);
      if (pane.getValue() != null && pane.getValue().equals(options[0])) {
          System.exit(0);
      }
    }

    private void incrementStep() {
        this.currStep++;
        this.addCurrentPanel();
        this.buttonPrevious.setEnabled(true);
        if (this.currStep == CartogramWizard.STEP_SIX_CREATE_AUX) {
            this.buttonNext.setEnabled(false);
        } else if (this.currStep == CartogramWizard.STEP_FOUR_CREATE){
          this.buttonNext.setEnabled(false);
        } else {
          this.buttonNext.setEnabled(true);
        }
    }

    private void decrementStep() {
        this.currStep--;
        this.addCurrentPanel();
        this.buttonNext.setEnabled(true);
        if (this.currStep == CartogramWizard.STEP_ONE_INPUT_DATA) {
            this.buttonPrevious.setEnabled(false);
            this.stepOneInputData.setMap(this.mapInput);

            this.stepOneInputData.setFileName(this.inputFileName);

        }
    }

    private void addCurrentPanel() {
        this.centerPanel.removeAll();

        switch (this.currStep) {
        case CartogramWizard.STEP_ONE_INPUT_DATA:
            this.centerPanel.add(this.stepOneInputData);
            break;

        case CartogramWizard.STEP_TWO_CHOOSE_VARIABLE:
            if (this.stepTwoChooseVariable == null) {
                this.stepTwoChooseVariable = new CartogramChooseVariable();

            }
            this.stepTwoChooseVariable.setMap(this.mapInput);
            if (this.dataSet != stepTwoChooseVariable.getDataSet()) {
                this.stepTwoChooseVariable.setDataSet(this.dataSet);
            }
            this.centerPanel.add(this.stepTwoChooseVariable);
            break;

        case CartogramWizard.STEP_THREE_CHOOSE_OUTPUT_FILE:
            if (this.stepThreeChooseOutputFile == null) {
                //this.setOutputFileName(Preferences.get);
                this.stepThreeChooseOutputFile = new CartogramOutputFile(this,
                        this.outputFileName);
            }
            this.centerPanel.add(this.stepThreeChooseOutputFile);
            break;

        case CartogramWizard.STEP_FOUR_CREATE:
            if (this.stepFourCreate == null) {
                this.stepFourCreate = new CartogramCreate(this);
                this.progressBar = this.stepFourCreate.getProgressBar();
            }
            this.centerPanel.add(this.stepFourCreate);
            break;

        case CartogramWizard.STEP_FIVE_VIEW_RESULTS:
            if (this.stepFiveViewResults == null) {
                this.stepFiveViewResults = new CartogramViewResults(this.
                        mapInput, this.mapOutput);
            }
            this.stepFiveViewResults.setInputMap(this.mapInput);
            this.centerPanel.add(this.stepFiveViewResults);

            break;
        case CartogramWizard.STEP_SIX_CREATE_AUX:
            if (this.stepSixCreateAux == null) {
                this.stepSixCreateAux = new CartogramCreateAux(this);
            }

            this.stepSixCreateAux.haveCartogram(this.transforms != null);

            this.centerPanel.add(this.stepSixCreateAux);

        }
        this.revalidate();
        this.repaint();

    }

    private void makeBottomPanel() {
        this.buttonPrevious = new JButton("<< Previous");
        this.buttonPrevious.setEnabled(false);
        this.buttonPrevious.addActionListener(this);
        this.buttonNext = new JButton("Next >>");
        this.buttonNext.addActionListener(this);
        this.buttonRun = new JButton("Run!");
        this.buttonRun.addActionListener(this);
        this.buttonQuit = new JButton("Quit");
        this.buttonQuit.addActionListener(this);
        this.buttonPreferences = new JButton("Preferences");
        this.buttonPreferences.addActionListener(this);
        this.buttonAbout = new JButton("About");
        this.buttonAbout.addActionListener(this);
        this.bottomPanel = new JPanel();

        bottomPanel.add(buttonPrevious);
        bottomPanel.add(buttonNext);
        bottomPanel.add(buttonRun);
        bottomPanel.add(buttonQuit);
        bottomPanel.add(buttonPreferences);
        bottomPanel.add(buttonAbout);
    }

    private void setOutputFileName(Preferences gvPrefs) {
        String lastOutputDir = gvPrefs.get("LastGoodOutputDirectory", "");
        if (lastOutputDir == "") {
            String homeDir = System.getProperty("user.home");
            this.outputFileName = homeDir + "/test.shp";

        } else {
            this.outputFileName = lastOutputDir;
        }

    }




    private void setInputFile(Preferences gvPrefs) {
        String lastInputDir = gvPrefs.get("LastGoodInputDirectory", "");
        if (lastInputDir.equals("")) {

            String fileName = GuiUtils.writeDefaultShapefile();
            lastInputDir = fileName;
            this.useProj = true;
        }

        try {
            this.stepOneInputData.setFileName(lastInputDir);
            this.inputFileName = lastInputDir;
            this.readShapeFile(inputFileName);
            //this.useProj = this.projCheckBox.isSelected();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                                          "Sorry, could not correctly read input file: " +
                                          inputFileName +
                                          " Please restart FX.Cartogram.");
            //if something bad happened here, better reset the prefs file
            //otherwise the user could get stuck with a "bad file" every time
            //the program starts
            gvPrefs.put("LastGoodInputDirectory", "");
            ex.printStackTrace();
            System.exit(0);
        }

    }

    private void readShapeFile(String fileName) {
        GuiUtils.disconnectMaps(this.mapInput, this.mapOutput);
        if (fileName == null || fileName == "") {
            return;
        }
        try {
            shpRead.setFileName(fileName);
        } catch (Exception ex) {
            System.err.println("Error reading file " + fileName);
            //this.setInputFile();
        }
        if (this.useProj) {
            this.shpProj.setInputDataSet(shpRead.getDataSet());
            this.dataSet = shpProj.getOutputDataSetForApps();
        } else {
            this.shpToShape.setInputDataSet(shpRead.getDataSet());
            this.dataSet = shpRead.getDataForApps();
        }

        this.mapInput.setDataSet(dataSet);
        this.inputFileName = fileName;
        if (dataSet.getSpatialType() == DataSetForApps.SPATIAL_TYPE_LINE ||
            dataSet.getSpatialType() == DataSetForApps.SPATIAL_TYPE_POINT) {

            this.isAux = true;
        } else {

            this.isAux = false;
        }
    }
    private void showAboutMessage() throws HeadlessException {
        JOptionPane.showMessageDialog(this,
                                      "Cartogram creation using the Gastner-Newman method. \n" +
                                      "\n" +
                                      "Graphical user interface by Frank Hardisty. \n" +
                                      "\n" +
                                      "If you use output created by this program please acknowledge the use of this \n" +
                                      "code and its first publication in: \n" +
                                      "'Generating population density-equalizing maps', Michael T. Gastner and \n" +
                                      "M. E. J. Newman, Proceedings of the National Academy of Sciences of the \n" +
                                      " United States of America, vol. 101, pp. 7499-7504, 2004. \n"
                );
    }
    /*
     * This method actually creates the temporary files and creates
       a TransformsMain to do the work.
     */
    public void createCartogram() {
        Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
        int currentVar = this.mapInput.getCurrentVariable();
        this.transforms = new TransformsMain(false);
        this.preferencesFrame.setTransformParams(this.transforms);
        DataSetForApps newData = MapGenFile.createCartogram(this.progressBar,
                this.dataSet, gvPrefs, outputFileName, inputFileName,
                currentVar,
                this.transforms);

        this.mapOutput.setDataSet(newData);

        GuiUtils.connectMaps(this.mapInput, this.mapOutput);

    }

    public void createAuxCartogram() {
        Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());

        int currentVar = this.mapInput.getCurrentVariable();

        DataSetForApps newData = MapGenFile.createAuxCartogram(this.progressBar,
                this.dataSet, gvPrefs, outputFileName, inputFileName,
                currentVar,
                this.transforms);

        this.mapOutput.setDataSet(newData);
        GuiUtils.connectMaps(this.mapInput, this.mapOutput);

    }

    private class CartogramThread extends Thread {
        CartogramWizard wizard;
        boolean isAux;

        CartogramThread(CartogramWizard wizard, boolean isAux) {
            this.isAux = isAux;
            this.wizard = wizard;
        }

        public void run() {
            wizard.buttonRun.setEnabled(false);
            wizard.buttonNext.setEnabled(false);
            wizard.buttonPrevious.setEnabled(false);
            wizard.buttonPreferences.setEnabled(false);
            wizard.stepFourCreate.startWaiting();
            if (isAux) {
                wizard.createAuxCartogram();
            } else {
                wizard.createCartogram();
            }
            wizard.stepFourCreate.stopWaiting();
            wizard.buttonRun.setEnabled(true);
            wizard.buttonNext.setEnabled(true);
            wizard.buttonPrevious.setEnabled(true);
            wizard.buttonPreferences.setEnabled(true);
            //wizard.stepSixCreateAux.haveCartogram(true);
            wizard.incrementStep();

            wizard.stepFiveViewResults.zoomFullExtentOnDelay();
        }


    }


    public static void main(String[] args) {

        CartogramWizard gui = new CartogramWizard();
        JFrame frame = new JFrame(
                "FX.Cartogram");
        frame.getContentPane().add(gui);

        frame.pack();
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        gui.mapInput.zoomFullExtent();
    }
}
