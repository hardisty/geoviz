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
	protected final static Logger logger = Logger.getLogger(CartogramPreferences.class.getName());
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
    //private int maxNSquareLog = 18;
    //private double blurWidth = 0.1;
    //private double blurWidthFactor = 1.2;
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
        this.gvPrefs = Preferences.userNodeForPackage(parent.getClass());
        this.transforms = trans;
        initGui();
        setTransformParams(this.transforms);

    }

    public void setTransformParams(TransformsMain trans) {
        trans.setMaxNSquareLog(maxNSquareLog);
        trans.setBlurWidth(blurWidth);
        trans.setBlurWidthFactor(blurWidthFactor);
    }

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this.maxNSquareLogModel) {
            this.maxNSquareLog = this.maxNSquareLogModel.getNumber().intValue();
            gvPrefs.putInt("maxNSquareLog", this.maxNSquareLog);
            //this.transforms = new TransformsMain(false);

            setTransformParams(this.transforms);

        } else if (e.getSource() == this.blurWidthModel) {
            this.blurWidth = this.blurWidthModel.getNumber().doubleValue();
            gvPrefs.putDouble("blurWidth", this.blurWidth);
            this.transforms.setBlurWidth(blurWidth);

        } else if (e.getSource() == this.blurWidthFactorModel) {
            this.blurWidthFactor = this.blurWidthFactorModel.getNumber().
                                   doubleValue();
            gvPrefs.putDouble("blurWidthFactor", this.blurWidthFactor);
            this.transforms.setBlurWidthFactor(blurWidthFactor);

        } else if (e.getSource() == this.defaults && this.defaults.isFocusOwner()) {
            this.resetDefaults();

        } else if (e.getSource() == this.doneButton && this.doneButton.isFocusOwner()) {
            this.setVisible(false);
            this.auxCheckBox.requestFocus();
        }

    }

    private void resetDefaults() {
        this.maxNSquareLog = TransformsMain.DEFAULT_MAXNSQUARELOG;
        this.maxNSquareLogModel.setValue(new Integer(maxNSquareLog));
        this.transforms.setMaxNSquareLog(maxNSquareLog);

        this.blurWidth = TransformsMain.DEFAULT_BLURWIDTH;
        this.blurWidthModel.setValue(new Double(blurWidth));
        this.transforms.setBlurWidth(blurWidth);

        this.blurWidthFactor = TransformsMain.DEFAULT_BLURWIDTHFACTOR;
        this.blurWidthFactorModel.setValue(new Double(blurWidthFactor));
        this.transforms.setBlurWidthFactor(blurWidthFactor);

        if (CartogramPreferences.DEFAULT_AUX != this.auxCheckBox.isSelected()) {
            this.auxCheckBox.setSelected(CartogramPreferences.DEFAULT_AUX);
        }
        if (CartogramPreferences.DEFAULT_PROJECTION != this.projCheckBox.isSelected()) {
            this.projCheckBox.setSelected(CartogramPreferences.DEFAULT_PROJECTION);
        }
        if (CartogramPreferences.DEFAULT_CREATE_SHAPEFILE != this.shapeFileCheckBox.isSelected()) {
            this.shapeFileCheckBox.setSelected(CartogramPreferences.DEFAULT_CREATE_SHAPEFILE);
        }
        if (CartogramPreferences.DEFAULT_CREATE_POSTSCRIPT_FILE !=
            this.postScriptCheckBox.isSelected()) {
            this.postScriptCheckBox.setSelected(CartogramPreferences.
                                                DEFAULT_CREATE_POSTSCRIPT_FILE);
        }
    }

    private void initGui() {
        JPanel numericalPanel = makeNumericalPanel();
        JPanel bottomPanel = makeBottomPanel();
        JPanel cbPanel = makeCBPanel();
        JPanel centerPanel = new JPanel();
        centerPanel.add(cbPanel);
        centerPanel.add(numericalPanel);
        this.getContentPane().add(centerPanel);
        this.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        this.pack();
    }

    private JPanel makeBottomPanel() {
        JPanel bottomPanel = new JPanel();
        doneButton = new JButton("Done");
        doneButton.addChangeListener(this);
        bottomPanel.add(doneButton);
        this.defaults = new JButton("Revert to Defaults");
        this.defaults.addChangeListener(this);
        bottomPanel.add(this.defaults);
        return bottomPanel;
    }

    private JPanel makeCBPanel() {
        JPanel cbPanel = new JPanel();
        cbPanel.setLayout(new BoxLayout(cbPanel,
                                        BoxLayout.Y_AXIS));
        this.auxCheckBox = new JCheckBox("Transform Polygon as Auxilliary?");
        this.projCheckBox = new JCheckBox("Use Map Projection?");
        this.shapeFileCheckBox = new JCheckBox("Create Shapefile?");
        this.promptReplaceFile = new JCheckBox("Prompt on File Replace?");
        this.postScriptCheckBox = new JCheckBox("Create Postscript File?");
        //these variables deal with files, and are thus handled by the calling parent
        this.auxCheckBox.addActionListener(parent);
        this.projCheckBox.addActionListener(parent);
        this.shapeFileCheckBox.addActionListener(parent);
        this.postScriptCheckBox.addActionListener(parent);
        this.promptReplaceFile.addActionListener(parent);

//        auxCheckBox.setBorderPaintedFlat(true);
//        projCheckBox.setBorderPaintedFlat(true);
//        auxCheckBox.setBorder(BorderFactory.createLineBorder(Color.black));
//        projCheckBox.setBorder(BorderFactory.createLineBorder(Color.black));
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

        numericalPanel.setLayout(new BoxLayout(numericalPanel,
                                               BoxLayout.Y_AXIS));
        this.maxNSquareLog = gvPrefs.getInt("maxNSquareLog",
                                            TransformsMain.
                                            DEFAULT_MAXNSQUARELOG);
        Integer value = new Integer(maxNSquareLog);
        Integer min = new Integer(3);
        Integer max = new Integer(20);
        Integer step = new Integer(1);
        this.maxNSquareLogModel = new SpinnerNumberModel(value, min, max, step);
        String nSquareMsg = "This parameter controls the size of the grid \n" +
                            "used to transform the shapes. The number is the \n" +
                            "log of the number of number of grid squares. Numbers \n" +
                            "greater than twenty may cause the JVM to run out of \n" +
                            "memory.";
        JPanel maxNpan = this.makeSpinnerPanel(this.maxNSquareLogModel,
                                               "Log of Maximum Number of Squares",
                                               nSquareMsg);
        numericalPanel.add(maxNpan);

        this.blurWidth = gvPrefs.getDouble("blurWidth",
                                           TransformsMain.DEFAULT_BLURWIDTH);
        Double blurValue = new Double(blurWidth);
        Double blurMin = new Double(0.01d);
        Double blurMax = new Double(0.4d);
        Double blurStep = new Double(0.01d);
        this.blurWidthModel = new SpinnerNumberModel(blurValue, blurMin,
                blurMax, blurStep);
        String blurWidthMsg =
                "This number represents the starting point for width of the Gaussian \n" +
                "blur, from 0 to 1. The smaller the number, the more \n" +
                "accurate the transformation may potentially be. However, \n" +
                "smaller numbers may cause more passes that do not \n" +
                "converge, and take longer.";
        JPanel blurWidthPan = this.makeSpinnerPanel(this.blurWidthModel,
                "Width of Gaussian Blur", blurWidthMsg);
        numericalPanel.add(blurWidthPan);

        this.blurWidthFactor = gvPrefs.getDouble("blurWidthFactor",
                                                 TransformsMain.
                                                 DEFAULT_BLURWIDTHFACTOR);
        Double blurValueFactor = new Double(this.blurWidthFactor);
        Double blurMinFactor = new Double(1.01);
        Double blurMaxFactor = new Double(1.3);
        Double blurStepFactor = new Double(0.01);
        this.blurWidthFactorModel = new SpinnerNumberModel(blurValueFactor,
                blurMinFactor, blurMaxFactor, blurStepFactor);
        String factorMsg =
                "This is the factor by which the blur width will be \n" +
                "increased on each pass that does not converge. Lower \n" +
                "are potentially more accurate, higher numbers will converge \n" +
                "converge more quickly.";
        JPanel blurWidthFactorPan = this.makeSpinnerPanel(this.
                blurWidthFactorModel, "Factor for Increase of Gaussian Blur",
                factorMsg);
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
                JOptionPane.showMessageDialog(CartogramPreferences.this, about
                        );

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
        @SuppressWarnings("unused")
		Preferences gvPrefs = Preferences.userNodeForPackage(
                CartogramPreferences.class);
        logger.finest(gvPrefs.toString());
        TransformsMain trans = new TransformsMain(false);
        CartogramGUI pan = new CartogramGUI();
        CartogramPreferences app = new CartogramPreferences("Preferences",
                pan, trans);

        app.setVisible(true);
        app.addWindowListener(new WindowAdapter() {
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
