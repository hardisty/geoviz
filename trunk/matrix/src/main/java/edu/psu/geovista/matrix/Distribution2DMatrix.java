package edu.psu.geovista.matrix;

/**
 * <p>Title: Studio applications</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import geovista.category.MultiClassDistributions2D;

public class Distribution2DMatrix extends AbstractMatrix implements  ListSelectionListener
{


	private String[] variableNames;
	protected EventListenerList listenerList = new EventListenerList();
	private Vector[] dataVector;
	private MultiClassDistributions2D distributions[];
	private Color[] classColors;

    public Distribution2DMatrix()
    {
		super();
		this.setSize(panelWidthPixels, panelHeightPixels);
    }

	public void setDataVector(Vector[] dataVector){
		this.dataVector = dataVector;
		init();
	}

	public void setClassColors(Color[] colors){
		this.classColors = colors;
	}

	public void setVariableNames(String[] variableNames){
		this.variableNames = variableNames;
		int numLen;
		numLen = this.variableNames.length;
		//check if there are enough attributes to display in matrix from specified beginning.
		if (plottedBegin >= numLen) {
			System.err.println("There aren't enough attributes to display! Please reset the begin display attribute or reload a more attribute data file.");
			return;
		}
		plotNumber = (numLen <= maxNumArrays) ? numLen : maxNumArrays;
		if ((plottedBegin + plotNumber) > numLen) {
			plotNumber = numLen - plottedBegin;
		}
		this.plottedAttributes = new int[plotNumber];
		for (int i = plottedBegin; i < plottedBegin + plotNumber; i++) {
			plottedAttributes[i - plottedBegin] = i;
		}
	}

	protected synchronized void init () {
		logger.finest("get in init()...");
		if (!this.recreate) {
			return;      // maybe display error message.
		}
		this.removeAll();

		attList = new JList(this.variableNames);

		if (this.dataVector != null) {
			this.distributions = new MultiClassDistributions2D[plotNumber*plotNumber];
			matrixLayout = new GridBagLayout();
			c = new SPGridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			this.createMatrix();
			Container parent = getParent();
			if (parent != null) {
				parent.validate();
			}
			else {
				validate();
			}
		}
	}

	protected void createMatrix () {

		this.setLayout(matrixLayout);
		try {
			for (int i = 0; i < plotNumber + 1; i++) {
				for (int j = 0; j < plotNumber + 1; j++) {
					if ((i == 0) && (j == 0)) {
						//The M button on left up corner. Click on that will pop up a variable selection dialog for displaying in matrix.
						c.weightx = 0.0;
						c.weighty = 0.0;
						c.gridwidth = 1;
						c.gridheight = 1;
						JButton configButton = new JButton("M");
						configButton.addActionListener(new java.awt.event.ActionListener() {

							/**
							 * Set up the attributes for plotted in SPM.
							 * @param e
							 */
							public void actionPerformed (ActionEvent e) {
								try {

									configButton_actionPerformed(e);

								} catch (Exception exception) {exception.printStackTrace();}
							}
						});
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(configButton, c);
						add(configButton);
					}
					else if ((i == 0) && (j != 0)) {
						//The first row in matrix. They are buttons which can be dragged to change the column position.
						c.weightx = 1.0;
						c.weighty = 0.0;
						c.gridwidth = 4;
						//c.gridheight = 1;
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER;              //end row
						SPTagButton columnButton = new SPTagButton(leftRightArrow);
						columnButton.addMouseListener(columnButton);
						columnButton.addMouseMotionListener(columnButton);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(columnButton, c);
						add(columnButton);
					}
					else if ((i != 0) && (j == 0)) {
						//The first column in matrix. They are buttons which can be dragged to change the row position.
						c.weightx = 0.0;
						c.weighty = 1.0;
						c.gridwidth = 1;
						c.gridheight = 4;
						SPTagButton rowButton = new SPTagButton(topDownArrow);
						rowButton.addMouseListener(rowButton);
						rowButton.addMouseMotionListener(rowButton);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(rowButton, c);
						add(rowButton);
					}
					else {
						//Actually set up the graph components in matrix.
						c.weightx = 1.0;
						c.weighty = 1.0;
						c.gridwidth = 4;
						c.gridheight = 4;
						int indexCurrent = (i - 1)*(plotNumber) + (j - 1);
						int[] dataIndices = new int[2];
						dataIndices[0] = plottedAttributes[j - 1]; //+1 is because in the data structure, we reserve the first element for variable names.
						dataIndices[1] = plottedAttributes[i - 1];
						//construct of each element
						this.distributions[indexCurrent] = new MultiClassDistributions2D();
						this.distributions[indexCurrent].setAxisOn(false);
						this.distributions[indexCurrent].setClassColors(this.classColors);
						this.distributions[indexCurrent].setDisplayXVariableIndex(dataIndices[0]);
						this.distributions[indexCurrent].setDisplayYVariableIndex(dataIndices[1]);
						this.distributions[indexCurrent].setBackground(background);
						this.distributions[indexCurrent].setGaussOn(true);
						this.distributions[indexCurrent].setVariableNames(this.variableNames);
						this.distributions[indexCurrent].setDataVector(this.dataVector);
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER;              //end row
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints((Component)this.distributions[indexCurrent], c);
						add((Component)this.distributions[indexCurrent]);
						//this.distributions[indexCurrent].addActionListener(new ActionListener() {

							/**
							 * Get the event from a unit plot and send it to all units.
							 * @param e
							 */
							//public void actionPerformed (ActionEvent e) {
								// This gets the source or originator of the event
								//try {
									//unit_actionPerformed(e);
								//} catch (Exception exception) {exception.printStackTrace()}
							//}
						//});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Shift columns or rows in the matrix.
	 * @param lastPos
	 * @param newPos
	 */
	protected void moveRowAndColumn (int lastPos, int newPos) {
		logger.finest("move row or column...");
		int indicesRow;
		int indicesCol;
		int indicesLast1;
		int indicesNew1;
		int indicesLast2;
		int indicesNew2;

		for (int i = 0; i < plotNumber; i++) {
			indicesLast1 = (distributions[i*plotNumber + lastPos - 1].getDisplayXVariableIndex());
			indicesLast2 = (distributions[(lastPos - 1)*plotNumber + i].getDisplayYVariableIndex());
			
			indicesRow = indicesLast1;
			indicesCol = indicesLast2;
			indicesNew1 = distributions[i*plotNumber + newPos - 1].getDisplayXVariableIndex();
			indicesNew2 = distributions[(newPos - 1)*plotNumber + i].getDisplayYVariableIndex();
			
			indicesLast1 = indicesNew1;
			indicesLast2 = indicesNew2;
			distributions[i*plotNumber + lastPos - 1].setDisplayXVariableIndex(indicesLast1);
			distributions[(lastPos - 1)*plotNumber + i].setDisplayYVariableIndex(indicesLast2);
			indicesNew1 = indicesRow;
			indicesNew2 = indicesCol;
			distributions[i*plotNumber + newPos - 1].setDisplayXVariableIndex(indicesNew1);
			distributions[(newPos - 1)*plotNumber + i].setDisplayYVariableIndex(indicesNew2);
		}
	}

	/**
	 * Configure attributes for plotted in matrix.
	 * @param e
	 */
	private void configButton_actionPerformed (ActionEvent e) {
		attSelectDialog(400, 400);
	}

	JFrame dummyFrame = new JFrame();
	JDialog dialog = null;
	JScrollPane dialogPane = null;

	/**
	 * put your documentation comment here
	 * @param x
	 * @param y
	 */
	private void attSelectDialog (int x, int y) {
		attList.setSelectedIndices(this.plottedAttributes);
		if (this.dialog == null) {
			this.dialog = new JDialog(dummyFrame, "Attributes for Plot", true);
			JButton selectButton;
			JButton closeButton;
			dialog.setSize(150, 250);
			dialog.getContentPane().setLayout(new BorderLayout());
			attList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			//attList.addListSelectionListener(this);
			//JScrollPane scrollPane = new JScrollPane(attList);
			this.dialogPane = new JScrollPane(attList);

			selectButton = new JButton("Apply");
			selectButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void actionPerformed (ActionEvent e) {
					selectButton_actionPerformed(e);
				}
			});
			closeButton = new JButton("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void actionPerformed (ActionEvent e) {
					closeButton_actionPerformed(e);
				}
			});

			JPanel buttonPanel = new JPanel(new BorderLayout());
			buttonPanel.add(selectButton, BorderLayout.WEST);
			buttonPanel.add(closeButton, BorderLayout.EAST);
			JPanel namePanel = new JPanel(new GridLayout(1,1));
			namePanel.add(new JLabel("Column Vars:"));
			JPanel attPanel = new JPanel(new GridLayout(1,2));
			attPanel.add(this.dialogPane);
			dialog.getContentPane().add(namePanel, BorderLayout.NORTH);
			dialog.getContentPane().add(attPanel, BorderLayout.CENTER);
			dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		}
		else {
			this.dialogPane.setViewportView(attList);
		}
		this.attList.addListSelectionListener(this);
		this.plottedAttributes = attList.getSelectedIndices();
		this.dialog.setLocation(x, y);
		this.dialog.setVisible(true);
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	private void selectButton_actionPerformed (ActionEvent e) {
		plotNumber = plottedAttributes.length;
		init();
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	private void closeButton_actionPerformed (ActionEvent e) {
		dialog.setVisible(false);
	}

	/**
	 * put your documentation comment here
	 * @param e
	 */
	public void valueChanged (ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList theList = (JList)e.getSource();
		if (theList.isSelectionEmpty()) {
			return;
		}
		else {
			plottedAttributes = theList.getSelectedIndices();
		}
	}

}