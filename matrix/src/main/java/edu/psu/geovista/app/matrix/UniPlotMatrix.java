/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package edu.psu.geovista.app.matrix;

/**
 * Title: Matrix
 * Description: Manipulable Matrix
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA Center
 * @author Xiping Dai
 * @version 1.0
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * put your documentation comment here
 */
public class UniPlotMatrix extends AbstractMatrix
		implements ListSelectionListener{

	private String elementClassName;
	private String[] attributesArray;
	protected EventListenerList listenerList = new EventListenerList();
	private ImageIcon matrixIcon = new ImageIcon(this.getClass().getResource("resources/matrixicon16.gif"));

    /**
     * put your documentation comment here
     */
	public UniPlotMatrix () {
		super();
	}

    /**
     * Set up the name for element which will be displayed in matrix. Can be defined in bean proporty.
     * @param classname
     */
	public void setElementClassName (String classname) {
		this.elementClassName = classname;
		try {
			setElementClass((this.elementClassName != null) ? Class.forName(this.elementClassName) :
					null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Get the name for element which will be displayed in matrix.
     * @return
     */
	public String getElementClassName () {
		return  this.elementClassName;
	}

    /**
     * Set the element which will be displayed in matrix by connecting to an object.
     * @param obj
     */
	public void setElementClass (Object obj) {
		setElementClassName((obj != null) ? obj.getClass().getName() : null);
	}

    /**
     * Set up the element displayed in matrix. Called by setElementClass(Oject obj).
     * @param clazz
     */
	public void setElementClass (Class clazz) {
		if (this.elementClass != clazz) {
			this.recreate = true;
		}
		this.elementClass = clazz;
	}

    /**
     * put your documentation comment here
     * @return
     */
	public String[] getAttributesArray () {
		return  attributesArray;
	}

    /**
     * The actual procedure to create the matrix, fill graphs in each matrix element.
     */
	protected void createMatrix () {

		this.setLayout(matrixLayout);
		columnButton = new SPTagButton[plotNumber];
		rowButton = new SPTagButton[this.plotNumber];
		Dimension tagDm = new Dimension(0, 0);
		configButton = new JButton();
		try {
			for (int i = 0; i < plotNumber + 1; i++) {
				for (int j = 0; j < plotNumber + 1; j++) {
					if ((i == 0) && (j == 0)) {
						//The M button on left up corner. Click on that will pop up a variable selection dialog for displaying in matrix.
						c.weightx = 0.0;
						c.weighty = 0.0;
						c.gridwidth = 1;
						c.gridheight = 1;
						configButton.setIcon(this.matrixIcon);
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
						c.gridwidth = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
                        //c.gridheight = 1;
						if (j == plotNumber){
							c.gridwidth = GridBagConstraints.REMAINDER;              //end row
						}
						varTags[j-1] = this.attributesDisplay[plottedAttributes[j - 1]];
						columnButton[j-1] = new SPTagButton(varTags[j-1]);
						columnButton[j-1].setMargin(AbstractMatrix.nullInsets);
						columnButton[j-1].addMouseListener(columnButton[j-1]);
						columnButton[j-1].addMouseMotionListener(columnButton[j-1]);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(columnButton[j-1], c);
						add(columnButton[j-1]);
					}
					else if ((i != 0) && (j == 0)) {
						//The first column in matrix. They are buttons which can be dragged to change the row position.
						c.weightx = 0.0;
						c.weighty = 1.0;
						c.gridwidth = 1;
						c.gridheight = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;

						rowButton[i-1] = new SPTagButton("s");
						rowButton[i-1].setMargin(AbstractMatrix.nullInsets);
						rowButton[i-1].setVerticalAlignment(SwingConstants.BOTTOM);
						
						AffineTransform trans = new AffineTransform();
						trans.rotate(-Math.PI/2, rowButton[i-1].getWidth(), rowButton[i-1].getHeight()/2);
						Font font = new Font("", Font.BOLD, 11);
						font = font.deriveFont(trans);
						
						rowButton[i-1].setFont(font);
						rowButton[i-1].setText(varTags[i-1]);
						rowButton[i-1].addMouseListener(rowButton[i-1]);
						rowButton[i-1].addMouseMotionListener(rowButton[i-1]);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(rowButton[i-1], c);
						add(rowButton[i-1]);
					}
					else {
						//Actually set up the graph components in matrix.
						c.weightx = 1.0;
						c.weighty = 1.0;
						c.gridwidth = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
						c.gridheight = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
						int indexCurrent = (i - 1)*(plotNumber) + (j - 1);
						int[] dataIndices = new int[2];
						dataIndices[0] = plottedAttributes[j - 1] + 1; //+1 is because in the data structure, we reserve the first element for variable names.
						dataIndices[1] = plottedAttributes[i - 1] + 1;
                        //construct of each element
						this.element[indexCurrent] = (MatrixElement)this.elementClass.newInstance();
						this.element[indexCurrent].setAxisOn(false);
						this.element[indexCurrent].setDataSet(this.dataSet);
						this.element[indexCurrent].setBackground(background);
						this.element[indexCurrent].setElementPosition(dataIndices);
						this.element[indexCurrent].setSelOriginalColorMode(this.selOriginalColorMode);
					    this.element[indexCurrent].setSelectionColor(this.selectionColor);
						if (this.bivarColorClasser != null){
							boolean reverseColor = false;
							if (i > j){
								reverseColor = true;
							}
							this.element[indexCurrent].setBivarColorClasser(this.bivarColorClasser, reverseColor);
						}
						if (this.colorArrayForObs != null){
							logger.finest("about to set color for each obs...");
							this.element[indexCurrent].setColorArrayForObs(this.colorArrayForObs);
						}
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER;              //end row
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints((Component)this.element[indexCurrent], c);
						add((Component)this.element[indexCurrent]);
						this.element[indexCurrent].addActionListener(new ActionListener() {

                            /**
                             * Get the event from a unit plot and send it to all units.
                             * @param e
                             */
							public void actionPerformed (ActionEvent e) {
                                // This gets the source or originator of the event
								try {
									unit_actionPerformed(e);
								} catch (Exception exception) {exception.printStackTrace();}
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int j = 0; j < plotNumber; j++) {
			if (columnButton[j].getPreferredSize().getWidth() > tagDm.getWidth())
				tagDm = columnButton[j].getPreferredSize();
			logger.finest("tagDm: " + tagDm.getWidth() + tagDm.getHeight());
		}
		for (int j = 0; j < plotNumber; j++) {
			columnButton[j].setPreferredSize(tagDm);
			columnButton[j].setMinimumSize(tagDm);
			columnButton[j].setMaximumSize(tagDm);
		}
		Dimension rowTag = new Dimension((int)tagDm.getHeight(), (int)tagDm.getHeight());
		configButton.setPreferredSize(rowTag);
		configButton.setMinimumSize(rowTag);
		configButton.setMaximumSize(rowTag);
	}

    /**
     * put your documentation comment here
     * @param e
     */
	private void unit_actionPerformed (ActionEvent e) {
 		MatrixElement source = (MatrixElement)e.getSource();
		String command = e.getActionCommand();
		if (command.compareTo(MatrixElement.COMMAND_POINT_SELECTED) == 0) {
			//For selection event.
			//selectedObvs = source.getSelectedObservations();
			selectedObvsInt = source.getSelections();
			for (int k = 0; k < plotNumber*plotNumber; k++) {
				MatrixElement otherElement = element[k];
                // Don't recall the scatterplot which generated the original event
				if (otherElement != source) {
					//otherElement.setSelectedObservations(selectedObvs);
					otherElement.setSelections(this.selectedObvsInt);
					otherElement.setMultipleSelectionColors(null);
				}
			}
			this.repaint();
			fireChangeEvent();
			this.fireSelectionChanged(this.getSelectedObvs());
		} else if (command.compareTo(MatrixElement.COMMAND_COLOR_CLASSFICIATION) == 0) {
			//For classification color event.
			this.bivarColorClasser = source.getBivarColorClasser();
			for (int k = 0; k < plotNumber*plotNumber; k++) {
				MatrixElement otherElement = element[k];
                // Don't recall the scatterplot which generated the original event
				if (otherElement != source) {
					otherElement.setBivarColorClasser(this.bivarColorClasser, false);
				}
			}
			this.repaint();
			fireChangeEvent();
		} else if  (command.compareTo(MatrixElement.COMMAND_DATARANGE_SET) == 0) {
				//For set up new axis extents in each scatterplots in matrix.
				double[] xAxisExtents = source.getXAxisExtents();
				double[] yAxisExtents = source.getYAxisExtents();
				int pos = 0;
				for (int k = 0; k < plotNumber*plotNumber; k++) {
					MatrixElement otherElement = element[k];
                    // Don't recall the scatterplot which generated the original event
					if (otherElement == source) {
						pos = k;
						logger.finest("Element: " + pos);
						break;
					}
				}
				//Find the elements which need to reset the axis extents.
				int r = pos/plotNumber;
				int c = pos%plotNumber;
				logger.finest("Position: " + r + " " + c);
				for (int i = 0; i < plotNumber; i++) {
					logger.finest("xAxis: " + (i*plotNumber + c) + " " + (c * plotNumber + i));
					element[i*plotNumber + c].setXAxisExtents(xAxisExtents);//The xAxisExtents for all of the elements in the same column have to be changed.
					element[c * plotNumber + i].setYAxisExtents(xAxisExtents);//The yAxisExtents for the corresponding elements on the other side
																		    //of diagonal need to be changed.
				    logger.finest("yAxis: " + (r*plotNumber + i) + " " + (r + i*plotNumber));
					element[r*plotNumber + i].setYAxisExtents(yAxisExtents); //The yAxisExtents for all of the elements in the same row have to be changed.
					element[r + i*plotNumber].setXAxisExtents(yAxisExtents);//The xAxisExtents for the corresponding elements on the other side
																		    //of diagonal need to be changed.
				}
			} else {
				System.err.println("Unknown command! = " + command);

              }//end if
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
			dialog.setSize(150, 300);
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
                                logger.finest("about to press button Select");
					selectButton_actionPerformed(e);
                                 logger.finest("after pressed button Select");
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
			JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
			buttonPanel.add(selectButton);
			buttonPanel.add(closeButton);

			JPanel attSelPanel = new JPanel(new BorderLayout());
			attSelPanel.setPreferredSize(new Dimension(150, 290));
			attSelPanel.add(new JLabel("Attribute Names:"), BorderLayout.NORTH);
			attSelPanel.add(this.dialogPane, BorderLayout.CENTER);
			attSelPanel.add(buttonPanel, BorderLayout.SOUTH);

			if (this.attributeDescriptions != null){
				JPanel descPane = new JPanel(new BorderLayout());
				descPane.add(new JLabel("Descriptions:"), BorderLayout.NORTH);

				JList descriptionList = new JList(this.attributeDescriptions);
				JScrollPane descListPane = new JScrollPane(descriptionList);
				descPane.add(descListPane, BorderLayout.CENTER);
				dialog.getContentPane().add(descPane, BorderLayout.CENTER);
			}
			dialog.getContentPane().add(attSelPanel, BorderLayout.WEST);

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
     * Selection button in variable selection dialog. Click on it, the variables selected in the JList will be forwarded to matrix.
     * @param e
     */
	private void selectButton_actionPerformed (ActionEvent e) {
		logger.finest("select button pressed, in action performed");
		plotNumber = plottedAttributes.length;
        logger.finest("select button pressed, plot number = " + plotNumber);
		init();
	}

    /**
     * Close the variable selection dialog.
     * @param e
     */
	private void closeButton_actionPerformed (ActionEvent e) {
		dialog.setVisible(false);
	}

    /**
     * Inform that value or selection in the JList (variable selection dialog) has been changed.
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



