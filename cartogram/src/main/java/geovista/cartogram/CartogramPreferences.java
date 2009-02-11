/* -------------------------------------------------------------------
 Java source file for the class CartogramPreferences
  Original Author: Frank Hardisty
  $Author: hardistf $
  $Id: ComparableShapes.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

//

/*
 * This is a GUI to configure cartogram creation options
 */

public class CartogramPreferences extends JFrame implements ActionListener,
		ChangeListener {
	protected final static Logger logger = Logger
			.getLogger(CartogramPreferences.class.getName());
	JLabel stepLabel;
	JTextField inputFileNameTextField;
	JButton inputFileNameButton;

	static String DEFAULT_INPUT_SHAPEFILE_LOCATION = "";
	String inputFileLocation;
	static String DEFAULT_OUTPUT_SHAPEFILE_LOCATION = "";
	String outputShapefileLocation;
	static String DEFAULT_OUTPUT_POSTSCRIPT_LOCATION = "";
	String outputPostscriptLocation;
	static boolean DEFAULT_CREATE_POSTSCRIPT_FILE = false;
	boolean createPostscriptFile;
	static boolean DEFAULT_CREATE_SHAPEFILE = true;
	boolean createShapefile;

	static boolean DEFAULT_PROMPT_FOR_REPLACEMENT_FILE;
	boolean promptForReplacementFile;

	static boolean DEFAULT_DEBUG_INFO = false;
	boolean debugInfo = false;
	TransformsMain transforms;
	Preferences gvPrefs;
	// private int maxNSquareLog = 18;
	// private double blurWidth = 0.1;
	// private double blurWidthFactor = 1.2;
	private int maxNSquareLog;
	private double blurWidth;
	private double blurWidthFactor;
	JSpinner maxNSquareLogSpin;
	JSpinner blurWidthSpin;
	JSpinner blurWidthFactorSpin;
	SpinnerNumberModel maxNSquareLogModel;
	SpinnerNumberModel blurWidthModel;
	SpinnerNumberModel blurWidthFactorModel;
	JButton doneButton;
	JButton defaults;
	ActionListener parent;
	JCheckBox projCheckBox;
	JCheckBox auxCheckBox;
	JCheckBox shapeFileCheckBox;
	JCheckBox postScriptCheckBox;
	JCheckBox promptReplaceFile;

	static boolean DEFAULT_PROJECTION = false;
	static boolean DEFAULT_AUX = false;

	public CartogramPreferences(String title, ActionListener parent,
			TransformsMain trans) {

		super(title);
		this.parent = parent;
		gvPrefs = Preferences.userNodeForPackage(parent.getClass());
		transforms = trans;
		initGui();
		setTransformParams(transforms);

	}

	public void setTransformParams(TransformsMain trans) {
		trans.setMaxNSquareLog(maxNSquareLog);
		trans.setBlurWidth(blurWidth);
		trans.setBlurWidthFactor(blurWidthFactor);
	}

	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == maxNSquareLogModel) {
			maxNSquareLog = maxNSquareLogModel.getNumber().intValue();
			gvPrefs.putInt("maxNSquareLog", maxNSquareLog);
			// this.transforms = new TransformsMain(false);

			setTransformParams(transforms);

		} else if (e.getSource() == blurWidthModel) {
			blurWidth = blurWidthModel.getNumber().doubleValue();
			gvPrefs.putDouble("blurWidth", blurWidth);
			transforms.setBlurWidth(blurWidth);

		} else if (e.getSource() == blurWidthFactorModel) {
			blurWidthFactor = blurWidthFactorModel.getNumber().doubleValue();
			gvPrefs.putDouble("blurWidthFactor", blurWidthFactor);
			transforms.setBlurWidthFactor(blurWidthFactor);

		} else if (e.getSource() == defaults && defaults.isFocusOwner()) {
			resetDefaults();

		} else if (e.getSource() == doneButton && doneButton.isFocusOwner()) {
			setVisible(false);
			auxCheckBox.requestFocus();
		}

	}

	private void resetDefaults() {
		maxNSquareLog = TransformsMain.DEFAULT_MAXNSQUARELOG;
		maxNSquareLogModel.setValue(new Integer(maxNSquareLog));
		transforms.setMaxNSquareLog(maxNSquareLog);

		blurWidth = TransformsMain.DEFAULT_BLURWIDTH;
		blurWidthModel.setValue(new Double(blurWidth));
		transforms.setBlurWidth(blurWidth);

		blurWidthFactor = TransformsMain.DEFAULT_BLURWIDTHFACTOR;
		blurWidthFactorModel.setValue(new Double(blurWidthFactor));
		transforms.setBlurWidthFactor(blurWidthFactor);

		if (CartogramPreferences.DEFAULT_AUX != auxCheckBox.isSelected()) {
			auxCheckBox.setSelected(CartogramPreferences.DEFAULT_AUX);
		}
		if (CartogramPreferences.DEFAULT_PROJECTION != projCheckBox
				.isSelected()) {
			projCheckBox.setSelected(CartogramPreferences.DEFAULT_PROJECTION);
		}
		if (CartogramPreferences.DEFAULT_CREATE_SHAPEFILE != shapeFileCheckBox
				.isSelected()) {
			shapeFileCheckBox
					.setSelected(CartogramPreferences.DEFAULT_CREATE_SHAPEFILE);
		}
		if (CartogramPreferences.DEFAULT_CREATE_POSTSCRIPT_FILE != postScriptCheckBox
				.isSelected()) {
			postScriptCheckBox
					.setSelected(CartogramPreferences.DEFAULT_CREATE_POSTSCRIPT_FILE);
		}
	}

	private void initGui() {
		JPanel numericalPanel = makeNumericalPanel();
		JPanel bottomPanel = makeBottomPanel();
		JPanel cbPanel = makeCBPanel();
		JPanel centerPanel = new JPanel();
		centerPanel.add(cbPanel);
		centerPanel.add(numericalPanel);
		getContentPane().add(centerPanel);
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		pack();
	}

	private JPanel makeBottomPanel() {
		JPanel bottomPanel = new JPanel();
		doneButton = new JButton("Done");
		doneButton.addChangeListener(this);
		bottomPanel.add(doneButton);
		defaults = new JButton("Revert to Defaults");
		defaults.addChangeListener(this);
		bottomPanel.add(defaults);
		return bottomPanel;
	}

	private JPanel makeCBPanel() {
		JPanel cbPanel = new JPanel();
		cbPanel.setLayout(new BoxLayout(cbPanel, BoxLayout.Y_AXIS));
		auxCheckBox = new JCheckBox("Transform Polygon as Auxilliary?");
		projCheckBox = new JCheckBox("Use Map OldProjection?");
		shapeFileCheckBox = new JCheckBox("Create Shapefile?");
		promptReplaceFile = new JCheckBox("Prompt on File Replace?");
		postScriptCheckBox = new JCheckBox("Create Postscript File?");
		// these variables deal with files, and are thus handled by the calling
		// parent
		auxCheckBox.addActionListener(parent);
		projCheckBox.addActionListener(parent);
		shapeFileCheckBox.addActionListener(parent);
		postScriptCheckBox.addActionListener(parent);
		promptReplaceFile.addActionListener(parent);

		// auxCheckBox.setBorderPaintedFlat(true);
		// projCheckBox.setBorderPaintedFlat(true);
		// auxCheckBox.setBorder(BorderFactory.createLineBorder(Color.black));
		// projCheckBox.setBorder(BorderFactory.createLineBorder(Color.black));
		cbPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		cbPanel.add(auxCheckBox);
		cbPanel.add(projCheckBox);
		cbPanel.add(shapeFileCheckBox);
		cbPanel.add(postScriptCheckBox);
		cbPanel.add(promptReplaceFile);
		return cbPanel;
	}

	private JPanel makeNumericalPanel() {
		JPanel numericalPanel = new JPanel();

		numericalPanel
				.setLayout(new BoxLayout(numericalPanel, BoxLayout.Y_AXIS));
		maxNSquareLog = gvPrefs.getInt("maxNSquareLog",
				TransformsMain.DEFAULT_MAXNSQUARELOG);
		Integer value = new Integer(maxNSquareLog);
		Integer min = new Integer(3);
		Integer max = new Integer(20);
		Integer step = new Integer(1);
		maxNSquareLogModel = new SpinnerNumberModel(value, min, max, step);
		String nSquareMsg = "This parameter controls the size of the grid \n"
				+ "used to transform the shapes. The number is the \n"
				+ "log of the number of number of grid squares. Numbers \n"
				+ "greater than twenty may cause the JVM to run out of \n"
				+ "memory.";
		JPanel maxNpan = makeSpinnerPanel(maxNSquareLogModel,
				"Log of Maximum Number of Squares", nSquareMsg);
		numericalPanel.add(maxNpan);

		blurWidth = gvPrefs.getDouble("blurWidth",
				TransformsMain.DEFAULT_BLURWIDTH);
		Double blurValue = new Double(blurWidth);
		Double blurMin = new Double(0.01d);
		Double blurMax = new Double(0.4d);
		Double blurStep = new Double(0.01d);
		blurWidthModel = new SpinnerNumberModel(blurValue, blurMin, blurMax,
				blurStep);
		String blurWidthMsg = "This number represents the starting point for width of the Gaussian \n"
				+ "blur, from 0 to 1. The smaller the number, the more \n"
				+ "accurate the transformation may potentially be. However, \n"
				+ "smaller numbers may cause more passes that do not \n"
				+ "converge, and take longer.";
		JPanel blurWidthPan = makeSpinnerPanel(blurWidthModel,
				"Width of Gaussian Blur", blurWidthMsg);
		numericalPanel.add(blurWidthPan);

		blurWidthFactor = gvPrefs.getDouble("blurWidthFactor",
				TransformsMain.DEFAULT_BLURWIDTHFACTOR);
		Double blurValueFactor = new Double(blurWidthFactor);
		Double blurMinFactor = new Double(1.01);
		Double blurMaxFactor = new Double(1.3);
		Double blurStepFactor = new Double(0.01);
		blurWidthFactorModel = new SpinnerNumberModel(blurValueFactor,
				blurMinFactor, blurMaxFactor, blurStepFactor);
		String factorMsg = "This is the factor by which the blur width will be \n"
				+ "increased on each pass that does not converge. Lower \n"
				+ "are potentially more accurate, higher numbers will converge \n"
				+ "converge more quickly.";
		JPanel blurWidthFactorPan = makeSpinnerPanel(blurWidthFactorModel,
				"Factor for Increase of Gaussian Blur", factorMsg);
		numericalPanel.add(blurWidthFactorPan);

		return numericalPanel;
	}

	public JPanel makeSpinnerPanel(SpinnerNumberModel model, String title,
			final String about) {
		model.addChangeListener(this);
		JPanel panel = new JPanel();
		JLabel label = new JLabel(title);
		JSpinner spin = new JSpinner(model);

		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		JButton button = new JButton("Help");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(CartogramPreferences.this, about);

			}
		});
		panel.add(button);
		panel.add(spin);
		panel.add(label);
		return panel;
	}

	public void actionPerformed(ActionEvent e) {

	}

	public static void main(String args[]) {
		Preferences gvPrefs = Preferences
				.userNodeForPackage(CartogramPreferences.class);
		logger.finest(gvPrefs.toString());
		TransformsMain trans = new TransformsMain(false);
		CartogramGUI pan = new CartogramGUI();
		CartogramPreferences app = new CartogramPreferences("Preferences", pan,
				trans);

		app.setVisible(true);
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

	}

	public JCheckBox getProjCheckBox() {
		return projCheckBox;
	}

	public JCheckBox getAuxCheckBox() {
		return auxCheckBox;
	}

	public JCheckBox getPostScriptCheckBox() {
		return postScriptCheckBox;
	}

	public JCheckBox getShapeFileCheckBox() {
		return shapeFileCheckBox;
	}

	public JCheckBox getPromptReplaceFile() {
		return promptReplaceFile;
	}
}
