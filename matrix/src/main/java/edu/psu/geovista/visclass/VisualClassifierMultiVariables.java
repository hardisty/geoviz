package edu.psu.geovista.visclass;

/**
 * Title: VisualClassifierMultiVariables
 * Description: Viasual classifier for multi-variables, such as K-means.
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA
 * @author Xiping Dai
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.category.ErrorMatrixPane;
import edu.psu.geovista.common.classification.Classifier;
import edu.psu.geovista.common.classification.ClassifierKMParameters;
import edu.psu.geovista.common.classification.ClassifierKMeans;
import edu.psu.geovista.common.classification.ClassifierMLParameters;
import edu.psu.geovista.common.classification.ClusterMixtureModels;
import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.data.geog.TrainingData;
import edu.psu.geovista.io.util.MyFileFilter;
import edu.psu.geovista.symbolization.ColorRampPicker;
import edu.psu.geovista.symbolization.ColorSymbolizer;
import edu.psu.geovista.ui.event.ClassColorEvent;
import edu.psu.geovista.ui.event.ClassColorListener;
import edu.psu.geovista.ui.event.ClassNumberEvent;
import edu.psu.geovista.ui.event.ClassNumberListener;
import edu.psu.geovista.ui.event.ColorArrayEvent;
import edu.psu.geovista.ui.event.ColorArrayListener;
import edu.psu.geovista.ui.event.DataSetDoubleEvent;
import edu.psu.geovista.ui.event.DataSetDoubleListener;
import edu.psu.geovista.ui.event.DataSetEvent;
import edu.psu.geovista.ui.event.DataSetListener;
import edu.psu.geovista.ui.event.MergeCategoryEvent;
import edu.psu.geovista.ui.event.MergeCategoryListener;
import edu.psu.geovista.ui.event.SelectionEvent;
import edu.psu.geovista.ui.event.SelectionListener;

public class VisualClassifierMultiVariables extends JPanel implements ActionListener, ComponentListener,
    DataSetListener, ClassColorListener, MergeCategoryListener, ClassNumberListener, DataSetDoubleListener{
	protected final static Logger logger = Logger.getLogger(VisualClassifierMultiVariables.class.getName());
    private static final int DEFAULT_CLASS_NUMBER = 5;
	private ColorRampPicker colorPanel;
    //XXX in future, we want to support much beyond colors.
    private Color[] colors;
    public boolean[] anchored;

    private Object[] dataObject;//XXX get rid of this data structure
    private DataSetForApps dataSet;
    private int[] classificationIndex;
    private int[] classificationIndexTrain;
    private Color[] returnColors;

    private int nClasses;
    private boolean update;
    private boolean interpolate;
    private boolean setupFinished;

    private transient JCheckBox updateBox;
    private transient JCheckBox interpolateBox;
	private JPanel classPickerPanel;
	private JPanel colorPickerPanel;
	private JPanel classifyPanel;
	private JComboBox classifierBox;
	private JTextField classNumField;
	private JButton applyButton;
	private JButton detailButton;
	private JButton classifyTrainDataButton;
	private JButton classifyLoadDataButton;
	private JButton displayResultsButton;
	private JButton saveResultsButton;
	private String FileName;
	private String classifierName;
	private ClassifierKMeans classifierKMeans;
       private ClusterMixtureModels classifierMixureModels;
	private ClassifierMaximumLikelihood classifierML;
	private int[] iniObsIdx = null;
	private int[] selectedAttIdx;
	private boolean visualDisplay = false;
	private JDialog kmParameterSetupDialog;
	private JDialog mlParameterSetupDialog;
        private ErrorMatrixPane errorMatrixPane;
	private ClassifierKMParameters kMParameterSetup;

	private ClassifierMLParameters mlParameterSetup;

	private TrainingData setUpTrainingData;
	private Vector[] trainingData;
	private String[] trainingClassLabels;
        private double[][] classifyData;


    public static final String COMMAND_COLORS_CHANGED = "colors";
    public static final String COMMAND_BEAN_REGISTERED = "hi!";
    private String classLabel;
	private Color classColor;
//    private int currOrientation = this.X_AXIS;
//    public static final int X_AXIS = 0;
//    public static final int Y_AXIS = 1;

    public VisualClassifierMultiVariables() {
        super();
        this.addComponentListener(this);
        this.setupFinished = false;
        this.nClasses = VisualClassifierMultiVariables.DEFAULT_CLASS_NUMBER;
        this.update = true;
        this.interpolate = true;
        //this.colorerLinear = new ColorSymbolizerLinear();
		this.classifierKMeans = new ClassifierKMeans();
                this.classifierMixureModels = new ClusterMixtureModels();
		this.classifierML = new ClassifierMaximumLikelihood();

		//this.setLayout(new BorderLayout());
		this.makeClassifierPanel();
        this.makeColorPickerPanel();
		this.makeClassifyPanel();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(classPickerPanel);
        //this.add(classificationPanel);
        this.add(colorPickerPanel);
		this.add(this.classifyPanel);
        this.setupFinished = true;
//        this.makeColors();
        this.setPreferredSize(new Dimension(350,80));

        this.revalidate();

    }

	public void setDataObject (Object[] dataObject){
		//this.dataObject = dataObject;
                DataSetForApps dataObjTransfer = new DataSetForApps(dataObject);//XXX not really what we want. Can't get updates
                //this.dataObject = dataObjTransfer.getDataSetNumericAndSpatial();
                this.dataObject = dataObjTransfer.getDataSetNumericAndAtt();

		this.setUpTrainingData = new TrainingData();
                if ((this.trainingData != null)&&(this.nClasses == this.trainingData.length)){
                  this.setUpTrainingData.setNumberOfClasses(this.nClasses);
                  this.setUpTrainingData.setDataObject(this.dataObject);
                  this.trainingData = this.setUpTrainingData.getTrainingDataVector();
                  this.trainingClassLabels = this.setUpTrainingData.getTrainingClassLabels();
                }
	}

        public void setClassifyData (double[][] data){
          this.classifyData = data;
          this.mlcTuple();
        }

        public void setClassifyDataObject(Object[] dataObject){
          this.mlcDataObject(dataObject);
        }

	public Color[] getClassificationColors(){
		return this.returnColors;
	}

	public int[] getClassification(){
	    return this.classificationIndexTrain;
	}

        public void setClassNumber(int classNumber){
          if (classNumber != this.nClasses){
            this.nClasses = classNumber;
            this.classNumField.setText(Integer.toString(this.nClasses));
            this.makeColors();
          }
        }

	public void setClassColor(String classLabel, Color classColor){
		this.classColor = classColor;
		this.classLabel = classLabel; //To do, need to generalize it to any training labels.
		String[] classLabels = new String[this.nClasses];;
		if (this.setUpTrainingData == null){
			this.setUpTrainingData = new TrainingData();
			this.setUpTrainingData.setNumberOfClasses(this.nClasses);
			this.setUpTrainingData.setDataObject(this.dataObject);
		}
		classLabels = this.setUpTrainingData.getTrainingClassLabels();
//		int l = (new Integer(this.classLabel)).intValue();
//		if (l > this.nClasses){
//			return;
//		}
		boolean[] pickerAnchors = this.colorPanel.getAnchored();
		for (int i = 0; i < this.nClasses; i ++){
			if (classLabels[i].equals(this.classLabel)){
				this.colors[i] = this.classColor;
				pickerAnchors[i] = true;
			}
		}

		//this.colors[l-1] = this.classColor;
		colorPanel.setColors(this.colors);
		//boolean[] pickerAnchors = this.colorPanel.getAnchored();
		//pickerAnchors[l-1] = true;
		colorPanel.setAnchored(pickerAnchors);
		VisualClassifierMultiVariables.this.makeColors();
                VisualClassifierMultiVariables.this.fireActionPerformed(VisualClassifierMultiVariables.COMMAND_COLORS_CHANGED);
		//this.mergeClasses(classLabel);
	}

	public void mergeClasses(String mergedClassLabels){
		String mergedClassLabelString = new String();
		mergedClassLabelString = mergedClassLabels;
		if (this.setUpTrainingData == null){
			this.setUpTrainingData = new TrainingData();
			this.setUpTrainingData.setNumberOfClasses(this.nClasses);
			this.setUpTrainingData.setDataObject(this.dataObject);
		}
		this.setUpTrainingData.setMegedClassLabels(mergedClassLabelString);
		this.trainingClassLabels = this.setUpTrainingData.getClassLabelsAfterMerge();
		this.trainingData = this.setUpTrainingData.getMergedTrainingDataVector();
		this.nClasses = this.trainingClassLabels.length;
		this.classNumField.setText(Integer.toString(this.nClasses));
		VisualClassifierMultiVariables.this.setColorPanel();
	}

    private void makeColorPickerPanel() {
		//Color picker and apply button of this classificaiton
		this.colorPickerPanel = new JPanel();
		this.colorPickerPanel.setLayout(new BoxLayout(colorPickerPanel, BoxLayout.X_AXIS));
		colorPanel = new ColorRampPicker();
	    this.colorPanel.setNSwatches(this.nClasses);

        this.add(colorPanel);
        colorPanel.addActionListener(this);

		this.applyButton = new JButton ("Apply");
		this.applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyButton_actionPerformed(e);
            }
        });
		classPickerPanel.add(applyButton);
    }

	private void makeClassifierPanel(){
		this.classPickerPanel = new JPanel();
		this.classPickerPanel.setLayout(new BoxLayout(classPickerPanel, BoxLayout.X_AXIS));
	    this.classifierBox = new JComboBox ();
		this.classifierBox.addItem("Classifiers:");
		this.classifierBox.addItem("K Means Clustering");
                this.classifierBox.addItem("MixtureModels Clustering");
		this.classifierBox.addItem("MaximumLikelihood Classifier");
                this.classifierBox.addItem("Decision Tree");
		this.classifierName = "Classifiers:";
        this.classifierBox.setMinimumSize(new Dimension(80,20));
        this.classifierBox.setMaximumSize(new Dimension(120,20));
        classifierBox.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            if (cb.getItemCount() > 0) {
				classifierName = (String)cb.getSelectedItem();
            }//end if count > 0
          }//end inner class
        });//end add listener
        //JLabel twoSpacesclassifCombo = new JLabel("  ");
        //this.add(twoSpacesclassifCombo);
        //this.add(classifLabel);
        classPickerPanel.add(classifierBox);

		this.classNumField = new JTextField(2);
		this.classNumField.setMinimumSize(new Dimension(15,10));
		this.classNumField.setMaximumSize(new Dimension(20,20));
		this.classNumField.setText(Integer.toString(this.nClasses));
		this.classNumField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
			  JTextField classField = (JTextField)e.getSource();
			  if (Integer.parseInt(classField.getText()) != nClasses){
				  nClasses = Integer.parseInt(classField.getText());
				  makeColors();
			  }
		  }//end inner class
		});
		classPickerPanel.add(classNumField);

		this.detailButton = new JButton ("Detail");
		this.detailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                detailButton_actionPerformed(e);
            }
        });
		classPickerPanel.add(detailButton);
	}

	private void makeClassifyPanel(){
		this.classifyPanel = new JPanel();
		this.classifyPanel.setLayout(new BoxLayout(classifyPanel, BoxLayout.X_AXIS));

		this.classifyTrainDataButton = new JButton ("ClassifyTrain");
		this.classifyTrainDataButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classifyTrainButton_actionPerformed(e);
			}
        });

		this.classifyLoadDataButton = new JButton ("ClassifyLoad");
		this.classifyLoadDataButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				classifyLoadButton_actionPerformed(e);
			}
        });

		this.displayResultsButton = new JButton ("Display");
		this.displayResultsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayButton_actionPerformed(e);
			}
        });

		this.saveResultsButton = new JButton ("Save");
		this.saveResultsButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButton_actionPerformed(e);
			}
        });

		this.classifyPanel.add(this.classifyTrainDataButton);
		this.classifyPanel.add(this.classifyLoadDataButton);
		this.classifyPanel.add(this.displayResultsButton);
		this.classifyPanel.add(this.saveResultsButton);
	}

	private void setColorPanel(){
		this.colorPanel.setNSwatches(this.nClasses);
		Color[] pickerColors = this.colorPanel.getColors();
		boolean[] pickerAnchors = this.colorPanel.getAnchored();
		//now we interpolate...linearly
		int nPicks = pickerColors.length;
		double picksPerColor = (double)nPicks/(double)this.nClasses;
		if (this.colors == null){
			this.colors = new Color[nClasses];
		} else if (this.colors.length != nClasses) {
			this.colors = new Color[nClasses];
		}

		for (int i = 0; i < nClasses; i++) {
			double whichColor = (double)i * picksPerColor;
			int index = (int)Math.floor(whichColor);
			logger.finest("i = " + i + "index  = " + index);
			this.colors[i] = pickerColors[index];
		}
		if (this.interpolate) {
			//for each lock in the picker, find the class that is closest.
			double colorsPerPick = (double)(this.nClasses-1)/(double)(nPicks-1);
			//int[] newAnchors = new int[pickerAnchors.length];
			Vector newAnchors = new Vector();
			for (int i = 0; i < pickerAnchors.length; i++) {
				double whichClass = (double)i * colorsPerPick;
				int aClass = (int)Math.round(whichClass);
				if (pickerAnchors[i]){
				Integer Ind = new Integer(aClass);
				newAnchors.add(Ind);
			}
		}

		boolean[] colorAnchors = new boolean[nClasses];
		if (newAnchors.size() > 2) {
		  for (Enumeration e = newAnchors.elements() ; e.hasMoreElements() ;) {
		   Integer ind = (Integer)e.nextElement();
		   colorAnchors[ind.intValue()] = true;
		  }

		  colorAnchors[0] = true;
		  colorAnchors[colorAnchors.length -1] = true;
		} else if (newAnchors.size() == 2) {
		  colorAnchors[0] = true;
		  colorAnchors[colorAnchors.length -1] = true;
		} else if (newAnchors.size() == 1) {
		  colorAnchors[0] = true;
		}
		//now find those durn colors!
		this.colorPanel.getRamp().rampColors(this.colors,colorAnchors);
      }//end if interpolate
	}

    private void makeColors(){
		this.setColorPanel();
		this.setObsColors();
    }

	private void setObsColors(){
		if (classificationIndexTrain != null){
			if ((returnColors == null)||(classificationIndexTrain.length != returnColors.length)){
				this.returnColors = new Color[classificationIndexTrain.length];
			}
			for (int i = 0; i < classificationIndexTrain.length; i++) {

				if (classificationIndexTrain[i] == Classifier.NULL_CLASS) {
					returnColors[i] = ColorSymbolizer.DEFAULT_NULL_COLOR;
				} else {
					returnColors[i] = colors[classificationIndexTrain[i]];
				}
			}
			fireColorArrayChanged(getClassificationColors());
	  }
	}

    /** Listens to the check boxen. */
    class CheckBoxListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
          if (e.getSource().equals(VisualClassifierMultiVariables.this.updateBox)){
            if (e.getStateChange() == ItemEvent.SELECTED && VisualClassifierMultiVariables.this.setupFinished){
                VisualClassifierMultiVariables.this.update = true;
                VisualClassifierMultiVariables.this.makeColors();
                VisualClassifierMultiVariables.this.fireActionPerformed(VisualClassifierMultiVariables.COMMAND_COLORS_CHANGED);
                //VisualClassifierMultiVariables.this.fireColorClassifierPerformed();

            } else if (e.getStateChange() == ItemEvent.DESELECTED){

                VisualClassifierMultiVariables.this.update = false;
            }
          } else if (e.getSource().equals(VisualClassifierMultiVariables.this.interpolateBox)){
            if (e.getStateChange() == ItemEvent.SELECTED && VisualClassifierMultiVariables.this.setupFinished){
                VisualClassifierMultiVariables.this.interpolate = true;
                VisualClassifierMultiVariables.this.makeColors();
                VisualClassifierMultiVariables.this.fireActionPerformed(VisualClassifierMultiVariables.COMMAND_COLORS_CHANGED);
                //VisualClassifierMultiVariables.this.fireColorClassifierPerformed();

            } else if (e.getStateChange() == ItemEvent.DESELECTED){
                VisualClassifierMultiVariables.this.interpolate = false;
                VisualClassifierMultiVariables.this.makeColors();
                VisualClassifierMultiVariables.this.fireActionPerformed(VisualClassifierMultiVariables.COMMAND_COLORS_CHANGED);
                //VisualClassifierMultiVariables.this.fireColorClassifierPerformed();
            }
          }
        }
    }

	JFrame dummyFrame;

	private void detailButton_actionPerformed (ActionEvent e) {
		//Bring up a detailed parameter setting GUI.
		this.nClasses = Integer.parseInt(this.classNumField.getText());
		if (dataObject == null) return;
		if (this.classifierName.equals("Classifiers:")){
		    return;
		}
		if (this.classifierName.equals("K Means Clustering")&&(dataObject != null)){
			logger.finest("Inside K means Clustering...");
			kMParameterSetup = new ClassifierKMParameters((String[])this.dataObject[0], nClasses);
			//kMParameterSetup.setAttributeNames((String[])this.dataObject[0]);
			//kMParameterSetup.setClassNum(nClasses);
			if (kmParameterSetupDialog == null){
				if(dummyFrame == null){
					dummyFrame = new JFrame();
				}
				kmParameterSetupDialog = new JDialog(dummyFrame, "Parameter Setup", true);
				kmParameterSetupDialog.setLocation(300, 300);
				kmParameterSetupDialog.setSize(300, 300);
				kmParameterSetupDialog.getContentPane().setLayout(new BorderLayout());
				kmParameterSetupDialog.getContentPane().add(kMParameterSetup, BorderLayout.CENTER);
				kMParameterSetup.addActionListener(new ActionListener() {

					/**
					 * Get event from parameter setup GUI.
					 * @param e
					 */
					public void actionPerformed (ActionEvent e) {
						try {
							km_para_actionPerformed(e);
						} catch (Exception ex) {
							logger.throwing(this.getClass().getName(), "detail_button", ex);
						}
					}
			});
			}
			kmParameterSetupDialog.setVisible(true);
		}
		if (this.classifierName.equals("MaximumLikelihood Classifier")&&(dataObject != null)){
			logger.finest("Inside MKL ...");
			this.mlParameterSetup = new ClassifierMLParameters((String[])this.dataObject[0], nClasses);
			if (mlParameterSetupDialog == null){
				if(dummyFrame == null){
					dummyFrame = new JFrame();
				}
				mlParameterSetupDialog = new JDialog(dummyFrame, "Parameter Setup", true);
				mlParameterSetupDialog.setLocation(300, 300);
				mlParameterSetupDialog.setSize(300, 300);
				mlParameterSetupDialog.getContentPane().setLayout(new BorderLayout());
				mlParameterSetupDialog.getContentPane().add(mlParameterSetup, BorderLayout.CENTER);
				mlParameterSetup.addActionListener(new ActionListener() {

					/**
					 * Get event from parameter setup GUI.
					 * @param e
					 */
					public void actionPerformed (ActionEvent e) {
						try {
							ml_para_actionPerformed(e);
						} catch (Exception ex) {
							logger.throwing(this.getClass().getName(), "detail_button", ex);
						}
					}
			});
			}
			mlParameterSetupDialog.setVisible(true);

		}
	}

	private void applyButton_actionPerformed (ActionEvent e) {
		//Actually apply the parameters and do the classification, pass to other components.
		this.nClasses = Integer.parseInt(this.classNumField.getText());
		//this.makeColors();
		if (dataObject != null) logger.finest("data object is not null...");
		if (this.classifierName.equals("Classifiers:")){
			this.classifierBox.setSelectedIndex(1);
		    this.classifierKMeans.setClusterNumber(this.nClasses);
			this.classifierKMeans.setDataSet(this.dataSet);
			this.classificationIndexTrain = this.classifierKMeans.getKMeansClusters();
		}
		if (this.classifierName.equals("K Means Clustering")&&(dataObject != null)){
		    this.classifierKMeans.setClusterNumber(this.nClasses);
                    if (selectedAttIdx != null){
                      this.classifierKMeans.setSelectedAttIdx(selectedAttIdx);
                      logger.finest("selectedAttIdx: " + selectedAttIdx.length);
                    }
                        if (iniObsIdx != null){
                            this.classifierKMeans.setIniObsIdx(iniObsIdx);
                        }
			this.classifierKMeans.setDataSet(this.dataSet);

			this.classificationIndexTrain = this.classifierKMeans.getKMeansClusters();
		}
                if (this.classifierName.equals("MixtureModels Clustering")&&(dataObject != null)){
                 this.classifierMixureModels.setNumberOfCluster(this.nClasses);
                 //this.classifierKMeans.setSelectedAttIdx(selectedAttIdx);
                 this.classifierMixureModels.setDataSet(this.dataSet);
                 this.classificationIndexTrain = this.classifierMixureModels.getClusterResults();
               }

		if (this.classifierName.equals("MaximumLikelihood Classifier")&&(dataObject != null)){

			if ((this.trainingData == null)||this.nClasses != this.trainingData.length){
				this.setUpTrainingData.setNumberOfClasses(this.nClasses);
				this.setUpTrainingData.setDataObject(this.dataObject);
				this.trainingData = this.setUpTrainingData.getTrainingDataVector();
				this.trainingClassLabels = this.setUpTrainingData.getTrainingClassLabels();
			}
			this.classifierML.setClassColors(this.colors);
			//this.classifierML.setTrainingData(this.dataObject);
			this.classifierML.setTrainingAttributesLabels(this.setUpTrainingData.getAttributeNames());
			this.classifierML.setVisualDisplay(this.visualDisplay);
			this.classifierML.setTrainingData(this.trainingData);

			//this.classifierML.setDataObject(this.dataObject);
			//this.classificationIndex = this.classifierML.getClassificaiton();
			return;
		}

        this.returnColors = new Color[classificationIndexTrain.length];
	    for (int i = 0; i < classificationIndexTrain.length; i++) {

            if (classificationIndexTrain[i] == Classifier.NULL_CLASS) {
				returnColors[i] = ColorSymbolizer.DEFAULT_NULL_COLOR;
            } else {
				returnColors[i] = colors[classificationIndexTrain[i]];
            }
        }
		//fireSelectionChanged(getClassificationColors());
		this.fireColorArrayChanged(getClassificationColors());
	}

        JFrame errorMatrixFrame;
	private void classifyTrainButton_actionPerformed (ActionEvent e) {
		//Actually apply the parameters and do the classification, pass to other components.
		//this.nClasses = Integer.parseInt(this.classNumField.getText());
		this.setColorPanel();
		if (dataObject != null) logger.finest("data object is not null...");
		if (this.classifierName.equals("Classifiers:")){
			return;
		}
		if (this.classifierName.equals("K Means Clustering")&&(dataObject != null)){
			return;
		}
		if (this.classifierName.equals("MaximumLikelihood Classifier")&&(dataObject != null)){
			this.classifierML.setDataSet(this.dataSet);
			this.classificationIndexTrain = this.classifierML.getClassificaiton();

                        if (this.errorMatrixPane == null){
                          if(this.errorMatrixFrame == null){
                            this.errorMatrixFrame = new JFrame("Error Matrix");
                          }
                          this.errorMatrixPane = new ErrorMatrixPane();

                          this.errorMatrixFrame.setLocation(300, 300);
                          this.errorMatrixFrame.setSize(450, 200);
                          this.errorMatrixFrame.getContentPane().setLayout(new BorderLayout());
                          this.errorMatrixFrame.getContentPane().add(this.errorMatrixPane, BorderLayout.CENTER);

                        }
                        this.errorMatrixPane.setUpErrorMatrix((int[])(this.dataObject[this.dataObject.length-1]), this.classifierML.getClassificaiton(), this.setUpTrainingData.getTrainingClassNumber());
                        this.errorMatrixFrame.setVisible(true);

		}

		this.setObsColors();
//		this.returnColors = new Color[classificationIndexTrain.length];
//		for (int i = 0; i < classificationIndexTrain.length; i++) {
//
//			if (classificationIndexTrain[i] == Classifier.NULL_CLASS) {
//				returnColors[i] = ColorSymbolizer.DEFAULT_NULL_COLOR;
//			} else {
//				returnColors[i] = colors[classificationIndexTrain[i]];
//			}
//		}
//		this.fireColorArrayChanged(getClassificationColors());
	}


	private void classifyLoadButton_actionPerformed (ActionEvent e) {
		//apply the maximum classify rule to a new data set.
		if (dataObject != null) logger.finest("data object is not null...");
		if (this.classifierName.equals("Classifiers:")){
			return;
		}
		if (this.classifierName.equals("K Means Clustering")&&(dataObject != null)){
			return;
		}
		if (this.classifierName.equals("MaximumLikelihood Classifier")&&(dataObject != null)){
			this.loadFile();
		}

	}


	BufferedImage resultIMG;
	JPanel imagePane = new JPanel(new BorderLayout()) {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			logger.finest("paintComponenet");
			if (resultIMG != null) {
				logger.finest("paintImage");
//				Dimension d = getSize();
//				Graphics2D g2 = null;
//				if (resultIMG == null || resultIMG.getWidth() != d.width || resultIMG.getHeight() != d.height) {
//					resultIMG = (BufferedImage) createImage(d.width, d.height);
//				}
//				g2 = resultIMG.createGraphics();
//				g2.setBackground(getBackground());
//				g2.clearRect(0, 0, d.width, d.height);
//				g2.setRenderingHint(RenderingHints.KEY_RENDERING,
//                                RenderingHints.VALUE_RENDER_QUALITY);
				g.drawString("test", 100, 100);
				g.drawImage(resultIMG, 0, 0, this);
			}
		}
	};
	JFrame imageFrame = new JFrame();
	private void displayButton_actionPerformed (ActionEvent e) {

		//int len = this.classificationIndex.length;
		int w, h;
		w = h = 525;
		resultIMG = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		this.returnColors = new Color[classificationIndex.length];
		for (int i = 0; i < classificationIndex.length; i++) {

			if (classificationIndex[i] == Classifier.NULL_CLASS) {
				returnColors[i] = ColorSymbolizer.DEFAULT_NULL_COLOR;
			} else {
				returnColors[i] = colors[classificationIndex[i]];
			}
		}
		for(int i = 0; i < 525; i ++){
			for (int j = 0; j < 525; j ++){
				resultIMG.setRGB(j, i, returnColors[i*525 + j].getRGB());
			}
		}
		//edu.psu.geovista.category.GvImageOps image = new edu.psu.geovista.category.GvImageOps(resultIMG);

		imageFrame.getContentPane().add(this.imagePane);
		imageFrame.setSize(525, 525);
		imageFrame.setVisible(true);
		//this.fireColorArrayChanged(getClassificationColors());
	}

	//save classification results to a file.
	private void saveButton_actionPerformed (ActionEvent e) {
		saveOutput();
	}

	private void loadFile()
	{
	  FileName ="D:\\";
	  MyFileFilter filter = new MyFileFilter("IMG");
	  JFileChooser chooser = new JFileChooser();
	  chooser.setMultiSelectionEnabled(true);
	  chooser.setDialogTitle("Load Data");
	  chooser.setCurrentDirectory(new File(FileName));
	  chooser.setApproveButtonText("Load");
	  chooser.setFileFilter(filter);

	  File[] files;
	  int returnVal = chooser.showOpenDialog(null);
	  if(returnVal == JFileChooser.APPROVE_OPTION) {
		   files =  chooser.getSelectedFiles();
	  }
	  else return;

	  try{
		  int varnum = dataObject.length-2;
		  FileInputStream[] istreams = new FileInputStream[varnum];
		  edu.psu.geovista.io.Reader[] fileReader = new edu.psu.geovista.io.Reader[varnum];
		  String[] values = new String[varnum];
		  double[] tuple = new double[varnum];
		  this.classificationIndex = new int[525*525];

		  for (int i = 0; i < varnum; i ++){
			  istreams[i] = new FileInputStream(files[i]);
			  fileReader[i] = new edu.psu.geovista.io.Reader(istreams[i], ",\n");
		  }

		  for (int i=0;i<525*525;i++){

			  for (int j = 0; j < varnum; j ++){
				  values[j]= fileReader[j].readToken();
				  tuple[j] = new Double(values[j]).doubleValue();
			  }
			  this.classifierML.setSingleTuple(tuple);
			  this.classificationIndex[i] = classifierML.getClassTuple();
		  }

		for (int i = 0; i < varnum; i ++){
			istreams[i].close();
			fileReader[i].close();
		}
		}catch(Exception ee){   ee.printStackTrace();	}

  }

  private void mlcTuple(){
    int varnum = dataObject.length-2;
    double[] tuple = new double[varnum];
    this.classificationIndex = new int[this.classifyData.length];
    for (int i=0;i<this.classifyData.length;i++){
      tuple = this.classifyData[i];
      this.classifierML.setSingleTuple(tuple);
      this.classificationIndex[i] = classifierML.getClassTuple();
    }
  }

  private void mlcDataObject(Object[] data){
    this.classificationIndex = new int[this.classifyData.length];
    this.classifierML.setDataSet(dataSet);
  }


	private void saveOutput()
	{
	  MyFileFilter filter = new MyFileFilter("txt");
	  JFileChooser chooser = new JFileChooser();
	  chooser.setDialogTitle("Save Classifivation Results");
	  chooser.setCurrentDirectory(new File("D:\\"));
	  chooser.setApproveButtonText("Save");
	  chooser.setFileFilter(filter);

	  File file;
	  int returnVal = chooser.showOpenDialog(null);
	  if(returnVal == JFileChooser.APPROVE_OPTION) {
		   file =  chooser.getSelectedFile();
	  }
	  else return;

	  try{

		  FileWriter a = new FileWriter(file);
		  for (int i=0; i<this.classificationIndex.length; i++){
			  a.write(new Integer(this.classificationIndex[i]).toString());
			  a.write("\n");
		  }

		  a.close();

//		  FileOutputStream ostream = new FileOutputStream(file);
//
//		  for(int i = 0; i < this.classificationIndex.length; i ++){
//			  output.append(this.classificationIndex[i]);
//			  output.append("\n");
//		  }
//		byte[] s = output.toString().getBytes();
//		ostream.write(s, 0, s.length);
//		ostream.flush();
//		ostream.close();
		}catch(Exception ee){		ee.printStackTrace();	}
	}


	private void km_para_actionPerformed(ActionEvent e){
		 ClassifierKMParameters kmParameters = (ClassifierKMParameters)e.getSource();
		 this.iniObsIdx = kmParameters.getIniObsIdx();
		 this.selectedAttIdx = kmParameters.getSelectedAttIdx();
		 kmParameterSetupDialog.setVisible(false);
	}

	private void ml_para_actionPerformed(ActionEvent e){
		 ClassifierMLParameters mlParameters = (ClassifierMLParameters)e.getSource();
		 mlParameters.getIsUnBiasCov();
		 mlParameters.getDistributionType();
		 this.visualDisplay = mlParameters.getVisualDisplay();
		 this.selectedAttIdx = mlParameters.getSelectedAttIdx();
		 mlParameterSetupDialog.setVisible(false);
	}

     //start component event handling
     //note: this class only listens to itself
    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
        double pickPrefWidth = this.classPickerPanel.getPreferredSize().getWidth();
        int prefWidth = (int)(pickPrefWidth * 1.5);
        if (this.getWidth() >= prefWidth) {
            //this.changeOrientation(this.X_AXIS);
        } else {
            //this.changeOrientation(this.Y_AXIS);
        }
    }

    public void actionPerformed(ActionEvent e) {
      String command = e.getActionCommand();
      if (command == ColorRampPicker.COMMAND_SWATCH_COLOR_CHANGED){
        this.makeColors();
        this.fireActionPerformed(VisualClassifierMultiVariables.COMMAND_COLORS_CHANGED);
        //this.fireColorClassifierPerformed();
      } /*else if (command == this.COMMAND_CLASSES_CHANGED) {
        this.colorPickerPanel.setNSwatches(nClasses);
        this.nClasses = nClasses;
        this.makeColors();
        this.fireActionPerformed(this.COMMAND_COLORS_CHANGED);
        //this.fireColorClassifierPerformed();
      }*/
      //need to pass this along, if we are a FoldupPanel
      //super.actionPerformed(e);
    }

    /**
     * implements ActionListener
     */
	public void addActionListener (ActionListener l) {
		listenerList.add(ActionListener.class, l);
                this.fireActionPerformed(VisualClassifierMultiVariables.COMMAND_BEAN_REGISTERED);
	}

    /**
     * removes an ActionListener from the component
     */
	public void removeActionListener (ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}
    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
	private void fireActionPerformed (String command) {
          if (update) {
              // Guaranteed to return a non-null array
              Object[] listeners = listenerList.getListenerList();
              ActionEvent e = null;
              // Process the listeners last to first, notifying
              // those that are interested in this event
              for (int i = listeners.length - 2; i >= 0; i -= 2) {
				    if (listeners[i] == ActionListener.class) {
                       // Lazily create the event:
                       if (e == null) {
                            e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                        }
						((ActionListener)listeners[i + 1]).actionPerformed(e);
                    }
				}//next i
          }//end if
	}

        //Work with coordinator.
	public void dataSetChanged(DataSetEvent e){
        setDataObject(e.getDataSet());
    }

    public void selectionChanged(SelectionEvent e){
    }

    public void classColorChanged(ClassColorEvent e){
      setClassColor(e.getClassLabel(), e.getClassColor());
    }

    public void mergeCategoryChanged(MergeCategoryEvent e){
	mergeClasses(e.getClassLabel());
    }

    public void classNumberChanged(ClassNumberEvent e){
      setClassNumber(e.getClassNumber());
    }

    public void dataSetDoubleChanged(DataSetDoubleEvent e){
      setClassifyData(e.getDataSet());
    }


    /**
     * adds an SelectionListener
     */
    public void addSelectionListener (SelectionListener l) {
       listenerList.add(SelectionListener.class, l);
    }
    /**
     * removes an SelectionListener from the component
     */
	public void removeSelectionListener (SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);

	}

	/**
	 * adds an SelectionListener
	 */
	public void addColorArrayListener (ColorArrayListener l) {
		listenerList.add(ColorArrayListener.class, l);
	}
	/**
	 * removes an SelectionListener from the component
	 */
	public void removeColorArrayListener (ColorArrayListener l) {
		listenerList.remove(ColorArrayListener.class, l);

	}

	private void fireColorArrayChanged (Color[] newColorArray) {

		  // Guaranteed to return a non-null array
			  Object[] listeners = listenerList.getListenerList();
			  ColorArrayEvent e = null;
		  // Process the listeners last to first, notifying
		  // those that are interested in this event
			  for (int i = listeners.length - 2; i >= 0; i -= 2) {
				if (listeners[i] == ColorArrayListener.class) {
				  // Lazily create the event:
				  if (e == null) {
					e = new ColorArrayEvent(this, newColorArray);
				  }
					((ColorArrayListener)listeners[i + 1]).colorArrayChanged(e);
				}
			  }//next i

	}
}
