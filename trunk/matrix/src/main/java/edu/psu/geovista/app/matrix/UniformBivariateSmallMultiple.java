package edu.psu.geovista.app.matrix;

/**
 * Title: MixedGraphMatrix
 * Description: Manipulable Matrix
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA Center
 * @author Xiping Dai
 * @version 1.0
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UniformBivariateSmallMultiple extends AbstractMatrix implements
		ListSelectionListener {

	private static final int DEFAULT_NUM_ROW_VARIABLES = 3;
	protected EventListenerList listenerList = new EventListenerList();
	private SPTagButton[] columnButton;
	private JList rowAttList;
	private static Insets nullInsets;
	private String elementClassName;
	private Class elementClass;
	private int rowVarNumber = UniformBivariateSmallMultiple.DEFAULT_NUM_ROW_VARIABLES;
	private transient int[] plottedRowAttributes;
	private String[] varTags;
	private String[] rowTags;
	protected ImageIcon matrixIcon = new ImageIcon(this.getClass().getResource(
			"resources/matrixicon16.gif"));

	public UniformBivariateSmallMultiple() {
		super();
		this.setSize(panelWidthPixels, panelHeightPixels);
	}

	final static Logger logger = Logger.getLogger(UniformBivariateSmallMultiple.class.getName());

	/**
	 * put your documentation comment here
	 * 
	 * @param classname
	 */
	public void setElementClassName(String classname) {
		this.elementClassName = classname;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getElementClassName() {
		return this.elementClassName;
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param obj
	 */
	public void setElementClass(Object obj) {
		setElementClassName((obj != null) ? obj.getClass().getName() : null);
	}

	/**
	 * Overwrite the method in AbstractMatrix, because the size of the matrix is
	 * different.
	 * 
	 * @param condition
	 */
	public void setConditionArray(int[] condition) {
		if (condition == null) {
			return;
		} else {
			this.conditionArray = condition;
			for (int k = 0; k < plotNumber * this.rowVarNumber; k++) {
				MatrixElement otherElement = element[k];
				if (otherElement != null) {
					otherElement.setConditionArray(conditionArray);
				}
			}
			repaint();
		}
	}

	public void setSelectedObvs(int[] selected) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Set Selected Obs: ");
		}
		if (selected == null) {
			return;
		} else {
			for (int i = 0; i < this.selectedObvsInt.length; i++) {
				this.selectedObvsInt[i] = 0;
			}
			for (int i = 0; i < selected.length; i++) {
				this.selectedObvsInt[selected[i]] = 1;
			}
		}
		this.multipleSelectionColors = null;
		// Once selection from other components has been set, pass it to each
		// element inside of matrix.
		for (int k = 0; k < plotNumber * this.rowVarNumber; k++) {
			MatrixElement otherElement = element[k];
			otherElement.setSelections(this.selectedObvsInt);
			otherElement
					.setMultipleSelectionColors(this.multipleSelectionColors);
		}
		repaint();
	}

	protected synchronized void init() {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("get in init()...");
		}
		if (!this.recreate) {
			return; // maybe display error message.
		}
		this.removeAll();

		attList = new JList(this.attributesDisplay);
		this.rowAttList = new JList(this.attributesDisplay);

		if (this.elementClassName != null) {
			try {
				this.elementClass = Class.forName(this.elementClassName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		varTags = new String[this.plotNumber]; // column variables.
		rowTags = new String[this.rowVarNumber];

		if (this.plottedRowAttributes == null) {
			this.plottedRowAttributes = new int[this.rowVarNumber];
			for (int i = plottedBegin; i < plottedBegin + this.rowVarNumber; i++) {
				plottedRowAttributes[i - plottedBegin] = i;
			}
		}

		if (this.dataObject != null) {
			this.element = new MatrixElement[plotNumber * this.rowVarNumber];
			matrixLayout = new GridBagLayout();
			c = new SPGridBagConstraints();
			c.fill = GridBagConstraints.BOTH;
			this.createMatrix();
			Container parent = getParent();
			if (parent != null) {
				parent.validate();
			} else {
				validate();
			}
		}
		super.registerIndicationListeners();
	}

	// Set up elements in matrix.
	protected void createMatrix() {
		logger.finest("create SP:");
		this.setLayout(matrixLayout);

		columnButton = new SPTagButton[plotNumber];
		SPTagButton[] rowButton = new SPTagButton[this.rowVarNumber];
		Dimension tagDm = new Dimension(0, 0);
		configButton = new JButton();
		try {
			for (int i = 0; i < this.rowVarNumber + 1; i++) {
				for (int j = 0; j < plotNumber + 1; j++) {
					if ((i == 0) && (j == 0)) {
						c.weightx = 0.0;
						c.weighty = 0.0;
						c.gridwidth = 1;
						c.gridheight = 1;
						configButton.setIcon(this.matrixIcon);
						configButton
								.setMargin(UniformBivariateSmallMultiple.nullInsets);
						configButton
								.addActionListener(new java.awt.event.ActionListener() {

									/**
									 * Set up the attributes for plotted in SPM.
									 * 
									 * @param e
									 */
									public void actionPerformed(ActionEvent e) {
										try {
											configButton_actionPerformed(e);
										} catch (Exception exception) {
										}
									}
								});
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(configButton, c);
						add(configButton);
					} else if ((i == 0) && (j != 0)) {
						c.weightx = 1.0;
						c.weighty = 0.0;
						c.gridwidth = UniformBivariateSmallMultiple.DEFAULT_BUTTON_CONSTRAINTS;
						c.gridheight = 1;
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER; // end
						// row
						varTags[j - 1] = this.attributesDisplay[plottedAttributes[j - 1]];
						columnButton[j - 1] = new SPTagButton(varTags[j - 1]);
						columnButton[j - 1]
								.setMargin(UniformBivariateSmallMultiple.nullInsets);
						columnButton[j - 1]
								.addMouseListener(columnButton[j - 1]);
						columnButton[j - 1]
								.addMouseMotionListener(columnButton[j - 1]);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(columnButton[j - 1], c);
						add(columnButton[j - 1]);

					} else if ((i != 0) && (j == 0)) {
						c.weightx = 0.0;
						c.weighty = 1.0;
						c.gridwidth = 1;
						c.gridheight = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
						rowTags[i - 1] = this.attributesDisplay[this.plottedRowAttributes[i - 1]];
						rowButton[i - 1] = new SPTagButton("s");
						rowButton[i - 1]
								.setMargin(UniformBivariateSmallMultiple.nullInsets);
						rowButton[i - 1]
								.setVerticalAlignment(SwingConstants.BOTTOM);
						AffineTransform trans = new AffineTransform();
						trans.rotate(-Math.PI / 2);
						Font font = new Font("", Font.BOLD, 11);
						font = font.deriveFont(trans);
						rowButton[i - 1].setFont(font);
						rowButton[i - 1].setText(rowTags[i - 1]);
						rowButton[i - 1].addMouseListener(rowButton[i - 1]);
						rowButton[i - 1]
								.addMouseMotionListener(rowButton[i - 1]);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(rowButton[i - 1], c);
						add(rowButton[i - 1]);
					} else {
						c.weightx = 1.0;
						c.weighty = 1.0;
						c.gridwidth = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
						c.gridheight = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER; // end
						// row
						int indexCurrent = (i - 1) * (plotNumber) + (j - 1);
						int[] dataIndices = new int[2];
						dataIndices[0] = plottedAttributes[j - 1] + 1;
						dataIndices[1] = this.plottedRowAttributes[i - 1] + 1;
						// construct of each element
						this.element[indexCurrent] = (MatrixElement) this.elementClass
								.newInstance();
						this.element[indexCurrent].setAxisOn(false);
						this.element[indexCurrent]
								.setDataSet(this.dataSet);
						this.element[indexCurrent]
								.setSelectionColor(this.selectionColor);
						this.element[indexCurrent].setBackground(background);
						this.element[indexCurrent]
								.setElementPosition(dataIndices);
						if (this.bivarColorClasser != null) {
							this.element[indexCurrent].setBivarColorClasser(
									this.bivarColorClasser, false);
						}
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(
								(Component) this.element[indexCurrent], c);
						add((Component) this.element[indexCurrent]);
						if (rowButton[i - 1].getText().equals("s")) {
							rowButton[i - 1]
									.setText(this
											.stringToVertical(this.element[indexCurrent]
													.getShortDiscription()));
						}
						this.element[indexCurrent]
								.addActionListener(new ActionListener() {

									/**
									 * Get the event from a unit plot and send
									 * it to all units.
									 * 
									 * @param e
									 */
									public void actionPerformed(ActionEvent e) {
										// This gets the source or originator of
										// the event
										try {
											unit_actionPerformed(e);
										} catch (Exception exception) {
										}
									}
								});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int j = 0; j < plotNumber; j++) {
			if (columnButton[j].getPreferredSize().getWidth() > tagDm
					.getWidth())
				tagDm = columnButton[j].getPreferredSize();
			logger.finest("tagDm: " + tagDm.getWidth() + tagDm.getHeight());
		}
		for (int j = 0; j < plotNumber; j++) {
			columnButton[j].setPreferredSize(tagDm);
			columnButton[j].setMinimumSize(tagDm);
			columnButton[j].setMaximumSize(tagDm);
		}
		Dimension rowTag = new Dimension((int) tagDm.getHeight(), (int) tagDm
				.getHeight());
		configButton.setPreferredSize(rowTag);
		configButton.setMinimumSize(rowTag);
		configButton.setMaximumSize(rowTag);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	private void unit_actionPerformed(ActionEvent e) {
		logger.finest("action performed...");
		MatrixElement source = (MatrixElement) e.getSource();
		String command = e.getActionCommand();
		if (command.compareTo(MatrixElement.COMMAND_POINT_SELECTED) == 0) {
			this.selectedObvsInt = source.getSelections();
			for (int k = 0; k < plotNumber * this.rowVarNumber; k++) {
				MatrixElement otherElement = element[k];
				// Don't recall the scatterplot which generated the original
				// event
				if (otherElement != source) {
					otherElement.setSelections(this.selectedObvsInt);
				}
			}
			this.repaint();
			fireChangeEvent();
			this.fireSelectionChanged(this.getSelectedObvs());
		} else if (command
				.compareTo(MatrixElement.COMMAND_COLOR_CLASSFICIATION) == 0) {
			this.bivarColorClasser = source.getBivarColorClasser();
			for (int k = 0; k < plotNumber * this.rowVarNumber; k++) {
				MatrixElement otherElement = element[k];
				// Don't recall the scatterplot which generated the original
				// event
				if (otherElement != source) {
					otherElement.setBivarColorClasser(this.bivarColorClasser,
							false);
				}
			}
			this.repaint();
			// fireChangeEvent();
		} else if (command.compareTo(MatrixElement.COMMAND_DATARANGE_SET) == 0) {
			logger.finest("in axis reset...");
			double[] xAxisExtents = source.getXAxisExtents();
			double[] yAxisExtents = source.getYAxisExtents();
			int pos = 0;
			for (int k = 0; k < this.rowVarNumber * plotNumber; k++) {
				MatrixElement otherElement = element[k];
				// Don't recall the scatterplot which generated the original
				// event
				if (otherElement == source) {
					pos = k;
					break;
				}
			}
			int r = pos / plotNumber;
			int c = pos % plotNumber;

			for (int i = 0; i < this.plotNumber; i++) {
				element[r + i].setYAxisExtents(yAxisExtents);
				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("in y axis reset...");
				}
			}
			for (int i = 0; i < this.rowVarNumber; i++) {
				if (element[c + i * plotNumber].getClass().getName().equals("edu.psu.geovista.app.scatterplot.ScatterPlot")) {
					element[c + i * plotNumber].setXAxisExtents(xAxisExtents);
				}
			}
		} else {
			System.err.println("Unknown command! = " + command);
		}
	}

	/**
	 * Shift columns or rows in the matrix.
	 * 
	 * @param lastPos
	 * @param newPos
	 */
	private void moveColumn(int lastPos, int newPos) {
		int indicesRow;
		int[] indicesLast1;
		int[] indicesNew1;
		String varTagMoved = new String(varTags[lastPos - 1]);
		varTags[lastPos - 1] = varTags[newPos - 1];
		this.columnButton[lastPos - 1].setText(varTags[lastPos - 1]);
		varTags[newPos - 1] = varTagMoved;
		this.columnButton[newPos - 1].setText(varTags[newPos - 1]);
		if (logger.isLoggable(Level.FINEST)) {

			logger.finest("lastPos: " + lastPos + "newPos:" + newPos);
		}
		for (int i = 0; i < this.rowVarNumber; i++) {
			indicesLast1 = (element[i * plotNumber + lastPos - 1]
					.getElementPosition());
			indicesRow = indicesLast1[0];
			indicesNew1 = element[i * plotNumber + newPos - 1]
					.getElementPosition();
			indicesLast1[0] = indicesNew1[0];
			element[i * plotNumber + lastPos - 1]
					.setElementPosition(indicesLast1);
			indicesNew1[0] = indicesRow;
			element[i * plotNumber + newPos - 1]
					.setElementPosition(indicesNew1);
		}
	}

	/**
	 * Shift columns or rows in the matrix.
	 * 
	 * @param lastPos
	 * @param newPos
	 */
	private void moveRows(int lastPos, int newPos) {
		int indicesCol;
		int[] indicesLast2;
		int[] indicesNew2;
		String varTagMoved = new String(this.rowTags[lastPos - 1]);
		rowTags[lastPos - 1] = rowTags[newPos - 1];
		this.columnButton[lastPos - 1].setText(rowTags[lastPos - 1]);
		rowTags[newPos - 1] = varTagMoved;
		this.columnButton[newPos - 1].setText(rowTags[newPos - 1]);
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("lastPos: " + lastPos + "newPos:" + newPos);
		}
		for (int i = 0; i < this.rowVarNumber; i++) {
			indicesLast2 = (element[(lastPos - 1) * plotNumber + i]
					.getElementPosition());
			indicesCol = indicesLast2[1];
			indicesNew2 = element[(newPos - 1) * plotNumber + i]
					.getElementPosition();
			indicesLast2[1] = indicesNew2[1];
			element[(lastPos - 1) * plotNumber + i]
					.setElementPosition(indicesLast2);
			indicesNew2[1] = indicesCol;
			element[(newPos - 1) * plotNumber + i]
					.setElementPosition(indicesNew2);
		}
	}

	/**
	 * Configure attributes for plotted in matrix.
	 * 
	 * @param e
	 */
	private void configButton_actionPerformed(ActionEvent e) {
		attSelectDialog(400, 400);
	}

	JFrame dummyFrame = new JFrame();
	JDialog dialog = null;
	JScrollPane dialogPane = null;
	JScrollPane rowVarScrollPane = null;

	/**
	 * put your documentation comment here
	 * 
	 * @param x
	 * @param y
	 */
	private void attSelectDialog(int x, int y) {
		attList.setSelectedIndices(this.plottedAttributes);
		this.rowAttList.setSelectedIndices(this.plottedRowAttributes);
		if (this.dialog == null) {
			this.dialog = new JDialog(dummyFrame, "Attributes for Plot", true);
			JButton selectButton;
			JButton closeButton;
			dialog.setSize(200, 300);
			dialog.getContentPane().setLayout(new BorderLayout());

			attList
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.dialogPane = new JScrollPane(attList);

			rowAttList
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			this.rowVarScrollPane = new JScrollPane(rowAttList);

			selectButton = new JButton("Apply");
			selectButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
				public void actionPerformed(ActionEvent e) {
					selectButton_actionPerformed(e);
				}
			});
			closeButton = new JButton("Close");
			closeButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * 
				 * @param e
				 */
				public void actionPerformed(ActionEvent e) {
					closeButton_actionPerformed(e);
				}
			});

			JPanel buttonPanel = new JPanel(new BorderLayout());
			buttonPanel.add(selectButton, BorderLayout.WEST);
			buttonPanel.add(closeButton, BorderLayout.EAST);
			JPanel namePanel = new JPanel(new GridLayout(1, 2));
			namePanel.add(new JLabel("Column Vars:"));
			namePanel.add(new JLabel("Row Var:"));
			JPanel attPanel = new JPanel(new GridLayout(1, 2));
			attPanel.add(this.dialogPane);
			attPanel.add(rowVarScrollPane);
			dialog.getContentPane().add(namePanel, BorderLayout.NORTH);
			dialog.getContentPane().add(attPanel, BorderLayout.CENTER);
			dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		} else {
			this.dialogPane.setViewportView(attList);
			this.rowVarScrollPane.setViewportView(rowAttList);
		}
		this.attList.addListSelectionListener(this);
		rowAttList.addListSelectionListener(this);
		this.plottedAttributes = attList.getSelectedIndices();
		this.plottedRowAttributes = rowAttList.getSelectedIndices();
		this.dialog.setLocation(x, y);
		this.dialog.setVisible(true);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	private void selectButton_actionPerformed(ActionEvent e) {
		plotNumber = plottedAttributes.length;
		this.rowVarNumber = this.plottedRowAttributes.length;
		init();
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	private void closeButton_actionPerformed(ActionEvent e) {
		dialog.setVisible(false);
	}

	protected class SPTagButton extends JButton implements MouseListener,
			MouseMotionListener {

		/**
		 * put your documentation comment here
		 * 
		 * @param String
		 *            label
		 */
		SPTagButton(String label) {
			super(label);
			// buttonLabel = label;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param ImageIcon
		 *            icon
		 */
		SPTagButton(ImageIcon icon) {
			super(icon);
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mousePressed(MouseEvent e) {
			JButton button = (JButton) e.getSource();
			SPGridBagConstraints gbconst = (SPGridBagConstraints) matrixLayout
					.getConstraints(button);
			posLast = new Point(gbconst.column, gbconst.row);
			posDrag = (Point) posLast.clone();
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mouseReleased(MouseEvent e) {
			posNew = SwingUtilities.convertPoint((SPTagButton) e.getSource(), e
					.getX(), e.getY(), UniformBivariateSmallMultiple.this);
			posNew = matrixLayout.location(posNew.x, posNew.y);
			if (posNew.x > 4 * (plotNumber - 1))
				posNew.setLocation(posNew.x / 4 + 1, posNew.y / 4);
			else
				posNew.setLocation(posNew.x / 4, posNew.y / 4);
			logger.finest("PosNewX: " + posNew.x + "posNewY: " +posNew.y);
			int lastPos = 0;
			int newPos = 0;
			if (!validCellPos(posDrag) || !validCellPos(posNew))
				return;
			if ((posDrag.x != posNew.x) && (posDrag.y == 0)) {
				lastPos = posLast.x;
				newPos = posNew.x;
				moveColumn(lastPos, newPos);
				posDrag = posNew;
				repaint();
			} else if (posDrag.y != posNew.y) {
				lastPos = posLast.y;
				newPos = posNew.y;
				if (lastPos != newPos) {
					moveRows(lastPos, newPos);
					posDrag = posNew;
					repaint();
				}
			}
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mouseExited(MouseEvent e) {
			;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mouseDragged(MouseEvent e) {
			posNew = SwingUtilities.convertPoint((SPTagButton) e.getSource(), e
					.getX(), e.getY(), UniformBivariateSmallMultiple.this);
			posNew = matrixLayout.location(posNew.x, posNew.y);
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("PosNew0X: " + posNew.x + "posNew0Y: "
						+ posNew.y);
			}
			if (posNew.x > 4 * (plotNumber - 1))
				posNew.setLocation(posNew.x / 4 + 1, posNew.y / 4);
			else
				posNew.setLocation(posNew.x / 4, posNew.y / 4);
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("PosNewX: " + posNew.x + "posNewY: "
						+ posNew.y);
			}
			int lastPos = 0;
			int newPos = 0;
			if (!validCellPos(posDrag) || !validCellPos(posNew))
				return;
			if (posDrag.x != posNew.x) {
				lastPos = posDrag.x;
				newPos = posNew.x;
				moveColumn(lastPos, newPos);
				posDrag = posNew;
				repaint();
			} else if (posDrag.y != posNew.y) {
				lastPos = posDrag.y;
				newPos = posNew.y;
				if (lastPos != newPos) {
					moveRows(lastPos, newPos);
					posDrag = posNew;
					repaint();
				}
			}

		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mouseMoved(MouseEvent e) {
			;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mouseEntered(MouseEvent e) {
			;
		}

		/**
		 * put your documentation comment here
		 * 
		 * @param e
		 */
		public void mouseClicked(MouseEvent e) {
			;
		}
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param e
	 */
	public void valueChanged(ListSelectionEvent e) {

		if (e.getValueIsAdjusting())
			return;
		JList theList = (JList) e.getSource();
		if (theList.isSelectionEmpty()) {
			return;
		} else {
			if (e.getSource() == this.attList) {
				plottedAttributes = theList.getSelectedIndices();
			} else {
				plottedRowAttributes = theList.getSelectedIndices();
			}
		}
	}

	private String stringToVertical(String s) {
		String vSt;
		char[] sChar = s.toCharArray();
		int len = sChar.length;
		vSt = "<html> ";
		for (int i = 0; i < len; i++) {
			vSt = vSt + sChar[i] + "<br>";
		}
		vSt = vSt + "</html> ";
		return vSt;
	}

}