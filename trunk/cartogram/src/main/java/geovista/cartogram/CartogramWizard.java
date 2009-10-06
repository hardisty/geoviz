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

import geovista.common.data.DataSetForApps;
import geovista.geoviz.map.GeoMapUni;
import geovista.readers.shapefile.ShapeFileDataReader;
import geovista.readers.shapefile.ShapeFileProjection;
import geovista.readers.shapefile.ShapeFileToShape;

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

	GeoMapUni mapInput; // this is shared between multiple panels
	GeoMapUni mapOutput; // likewise

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
	private final ShapeFileDataReader shpRead;
	private final ShapeFileToShape shpToShape;
	private final ShapeFileProjection shpProj;
	private DataSetForApps dataSet;
	private TransformsMain transforms;
	JProgressBar progressBar;

	boolean isAux;

	Console messagePane;
	@SuppressWarnings("unused")
	private String auxInputFileName;
	final static Logger logger = Logger.getLogger(CartogramWizard.class
			.getName());

	// GuiUtils.VCSelectionExchanger selectionExchanger;
	/**
	 * CartogramWizard
	 */
	public CartogramWizard() {
		isAux = false;
		makeBottomPanel();
		BorderLayout border = new BorderLayout();
		setLayout(border);
		this.add(bottomPanel, BorderLayout.SOUTH);
		centerPanel = new JPanel();
		// this.centerPanel.setPreferredSize(new Dimension(500, 300));
		centerPanel.setBorder(BorderFactory.createBevelBorder(0));
		this.add(centerPanel, BorderLayout.CENTER);
		mapInput = new GeoMapUni();

		mapInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mapInput.setBorder(BorderFactory
				.createTitledBorder("Map of Input Data"));
		mapOutput = new GeoMapUni();

		stepOneInputData = new CartogramInputData(mapInput);
		stepOneInputData.addActionListener(this);

		centerPanel.add(stepOneInputData);
		shpRead = new ShapeFileDataReader();
		shpToShape = new ShapeFileToShape();
		shpProj = new ShapeFileProjection();
		transforms = new TransformsMain(false);
		preferencesFrame = new CartogramPreferences("Cartogram Preferences",
				this, transforms);

		try {

			Preferences gvPrefs = Preferences.userNodeForPackage(this
					.getClass());
			setInputFile(gvPrefs);
			setOutputFileName(gvPrefs);

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
		if (e.getSource() == stepOneInputData) {
			String fileName = e.getActionCommand();
			if (fileName != null) {
				readShapeFile(fileName);
			}
		} else if (e.getSource() == stepThreeChooseOutputFile) {
			outputFileName = e.getActionCommand();

		} else if (e.getSource().equals(buttonQuit)) {
			showQuitDialog();
		} else if (e.getSource().equals(buttonRun)) {

			boolean doIt = transforms.retreiveX() == null;
			if (!doIt) {
				Object[] options = { "Yes, replace", "No" };
				JOptionPane pane = new JOptionPane(
						"Replace current cartogram field?",
						JOptionPane.WARNING_MESSAGE);
				pane.setOptions(options);
				Dialog dia = pane.createDialog(this, "Replace?");
				dia.setVisible(true);
				if (pane.getValue() != null
						&& pane.getValue().equals(options[0])) {
					doIt = true;
				}

			}
			if (doIt) {
				currStep = CartogramWizard.STEP_FOUR_CREATE;
				addCurrentPanel();
				new CartogramThread(this, isAux).start();
			}
		} else if (e.getSource().equals(buttonNext)) {
			incrementStep();

		} else if (e.getSource().equals(buttonPrevious)) {
			decrementStep();
		} else if (e.getSource() == preferencesFrame.getProjCheckBox()) {
			useProj = preferencesFrame.getProjCheckBox().isSelected();
			readShapeFile(inputFileName);
		} else if (e.getSource() == preferencesFrame.getAuxCheckBox()) {
			isAux = preferencesFrame.getAuxCheckBox().isSelected();
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("isAux = " + isAux);
			}

		} else if (e.getSource() == buttonPreferences) {

			preferencesFrame.setVisible(true);
		} else if (e.getSource() == stepSixCreateAux) {
			auxInputFileName = e.getActionCommand();
			// this.isAux = true;
		} else if (e.getSource() == buttonAbout) {
			showAboutMessage();
		} else {
			if (logger.isLoggable(Level.WARNING)) {
				logger
						.warning("CartogramWizard.actionPerformed, unexpected source encountered: "
								+ e.getSource().getClass());
			}

		}
		this.repaint();
	}

	private void showQuitDialog() throws HeadlessException {
		Object[] options = { "Yes, quit", "No" };
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
		currStep++;
		addCurrentPanel();
		buttonPrevious.setEnabled(true);
		if (currStep == CartogramWizard.STEP_SIX_CREATE_AUX) {
			buttonNext.setEnabled(false);
		} else if (currStep == CartogramWizard.STEP_FOUR_CREATE) {
			buttonNext.setEnabled(false);
		} else {
			buttonNext.setEnabled(true);
		}
	}

	private void decrementStep() {
		currStep--;
		addCurrentPanel();
		buttonNext.setEnabled(true);
		if (currStep == CartogramWizard.STEP_ONE_INPUT_DATA) {
			buttonPrevious.setEnabled(false);
			stepOneInputData.setMap(mapInput);

			stepOneInputData.setFileName(inputFileName);

		}
	}

	private void addCurrentPanel() {
		centerPanel.removeAll();

		switch (currStep) {
		case CartogramWizard.STEP_ONE_INPUT_DATA:
			centerPanel.add(stepOneInputData);
			break;

		case CartogramWizard.STEP_TWO_CHOOSE_VARIABLE:
			if (stepTwoChooseVariable == null) {
				stepTwoChooseVariable = new CartogramChooseVariable();

			}
			stepTwoChooseVariable.setMap(mapInput);
			if (dataSet != stepTwoChooseVariable.getDataSet()) {
				stepTwoChooseVariable.setDataSet(dataSet);
			}
			centerPanel.add(stepTwoChooseVariable);
			break;

		case CartogramWizard.STEP_THREE_CHOOSE_OUTPUT_FILE:
			if (stepThreeChooseOutputFile == null) {
				// this.setOutputFileName(Preferences.get);
				stepThreeChooseOutputFile = new CartogramOutputFile(this,
						outputFileName);
			}
			centerPanel.add(stepThreeChooseOutputFile);
			break;

		case CartogramWizard.STEP_FOUR_CREATE:
			if (stepFourCreate == null) {
				stepFourCreate = new CartogramCreate(this);
				progressBar = stepFourCreate.getProgressBar();
			}
			centerPanel.add(stepFourCreate);
			break;

		case CartogramWizard.STEP_FIVE_VIEW_RESULTS:
			if (stepFiveViewResults == null) {
				stepFiveViewResults = new CartogramViewResults(mapInput,
						mapOutput);
			}
			stepFiveViewResults.setInputMap(mapInput);
			centerPanel.add(stepFiveViewResults);

			break;
		case CartogramWizard.STEP_SIX_CREATE_AUX:
			if (stepSixCreateAux == null) {
				stepSixCreateAux = new CartogramCreateAux(this);
			}

			stepSixCreateAux.haveCartogram(transforms != null);

			centerPanel.add(stepSixCreateAux);

		}
		revalidate();
		this.repaint();

	}

	private void makeBottomPanel() {
		buttonPrevious = new JButton("<< Previous");
		buttonPrevious.setEnabled(false);
		buttonPrevious.addActionListener(this);
		buttonNext = new JButton("Next >>");
		buttonNext.addActionListener(this);
		buttonRun = new JButton("Run!");
		buttonRun.addActionListener(this);
		buttonQuit = new JButton("Quit");
		buttonQuit.addActionListener(this);
		buttonPreferences = new JButton("Preferences");
		buttonPreferences.addActionListener(this);
		buttonAbout = new JButton("About");
		buttonAbout.addActionListener(this);
		bottomPanel = new JPanel();

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
			outputFileName = homeDir + "/test.shp";

		} else {
			outputFileName = lastOutputDir;
		}

	}

	private void setInputFile(Preferences gvPrefs) {
		String lastInputDir = gvPrefs.get("LastGoodInputDirectory", "");
		if (lastInputDir.equals("")) {

			String fileName = GuiUtils.writeDefaultShapefile();
			lastInputDir = fileName;
			useProj = true;
		}

		try {
			stepOneInputData.setFileName(lastInputDir);
			inputFileName = lastInputDir;
			readShapeFile(inputFileName);
			// this.useProj = this.projCheckBox.isSelected();
		} catch (Exception ex) {
			// how about let's use a shapefile that sits in the class files and
			// is
			// sure to be available.
			// also, it would be nice if this worked even when there is no local
			// disk acess....

			JOptionPane.showMessageDialog(this,
					"Sorry, could not correctly read input file: "
							+ inputFileName + " Please restart FX.Cartogram.");
			// if something bad happened here, better reset the prefs file
			// otherwise the user could get stuck with a "bad file" every time
			// the program starts
			gvPrefs.put("LastGoodInputDirectory", "");
			ex.printStackTrace();
			System.exit(0);
		}

	}

	private void readShapeFile(String fileName) {
		GuiUtils.disconnectMaps(mapInput, mapOutput);
		if (fileName == null || fileName == "") {
			return;
		}
		try {
			shpRead.setFileName(fileName);
		} catch (Exception ex) {
			System.err.println("Error reading file " + fileName);
			// this.setInputFile();
		}
		if (useProj) {
			shpProj.setInputDataSet(shpRead.getDataSet());
			dataSet = shpProj.getOutputDataSetForApps();
		} else {
			shpToShape.setInputDataSet(shpRead.getDataSet());
			dataSet = shpRead.getDataForApps();
		}

		mapInput.setDataSet(dataSet);
		inputFileName = fileName;
		if (dataSet.getSpatialType() == DataSetForApps.SPATIAL_TYPE_LINE
				|| dataSet.getSpatialType() == DataSetForApps.SPATIAL_TYPE_POINT) {

			isAux = true;
		} else {

			isAux = false;
		}
	}

	private void showAboutMessage() throws HeadlessException {
		JOptionPane
				.showMessageDialog(
						this,
						"Cartogram creation using the Gastner-Newman method. \n"
								+ "\n"
								+ "Graphical user interface by Frank Hardisty. \n"
								+ "\n"
								+ "If you use output created by this program please acknowledge the use of this \n"
								+ "code and its first publication in: \n"
								+ "'Generating population density-equalizing maps', Michael T. Gastner and \n"
								+ "M. E. J. Newman, Proceedings of the National Academy of Sciences of the \n"
								+ " United States of America, vol. 101, pp. 7499-7504, 2004. \n");
	}

	/*
	 * This method actually creates the temporary files and creates a
	 * TransformsMain to do the work.
	 */
	public void createCartogram() {
		Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
		int currentVar = mapInput.getCurrentVariable();
		transforms = new TransformsMain(false);
		preferencesFrame.setTransformParams(transforms);
		DataSetForApps newData = MapGenFile.createCartogram(progressBar,
				dataSet, gvPrefs, outputFileName, inputFileName, currentVar,
				transforms);

		mapOutput.setDataSet(newData);

		GuiUtils.connectMaps(mapInput, mapOutput);

	}

	public void createAuxCartogram() {
		Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());

		int currentVar = mapInput.getCurrentVariable();

		DataSetForApps newData = MapGenFile.createAuxCartogram(progressBar,
				dataSet, gvPrefs, outputFileName, inputFileName, currentVar,
				transforms);

		mapOutput.setDataSet(newData);
		GuiUtils.connectMaps(mapInput, mapOutput);

	}

	private class CartogramThread extends Thread {
		CartogramWizard wizard;
		boolean isAux;

		CartogramThread(CartogramWizard wizard, boolean isAux) {
			this.isAux = isAux;
			this.wizard = wizard;
		}

		@Override
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
			// wizard.stepSixCreateAux.haveCartogram(true);
			wizard.incrementStep();

			wizard.stepFiveViewResults.zoomFullExtentOnDelay();
		}

	}

	public static void main(String[] args) {

		CartogramWizard gui = new CartogramWizard();
		JFrame frame = new JFrame("FX.Cartogram");
		frame.getContentPane().add(gui);

		frame.pack();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.pack();
		gui.mapInput.zoomFullExtent();
	}
}
