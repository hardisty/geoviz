package geovista.matrix;

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
import javax.swing.JComboBox;
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



public class FixedRowMatrix extends AbstractMatrix implements
		ListSelectionListener {

	private static final int DEFAULT_NUM_GRAPHES = 4;
	protected EventListenerList listenerList = new EventListenerList();
	private SPTagButton[] columnButton;
	private JComboBox attCombo;
	private int yIdx = 0;
	private static Insets nullInsets;
	private Class[] elementClasses = new Class[DEFAULT_NUM_GRAPHES];
	private String[] elementClassNames = new String[DEFAULT_NUM_GRAPHES];
	private int graphTypeNumber;
	private String[] varTags;
	protected ImageIcon matrixIcon = new ImageIcon(FixedRowMatrix.class.getResource(
			"resources/matrixicon16.gif"));
	final static Logger logger = Logger.getLogger(FixedRowMatrix.class.getName());

	public FixedRowMatrix() {
		super();
		this.setSize(panelWidthPixels, panelHeightPixels);
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param classname
	 */
	public void setElementClassName0(String classname) {
		this.elementClassNames[0] = classname;

	}

	public void setElementClassName1(String classname) {
		this.elementClassNames[1] = classname;

	}

	public void setElementClassName2(String classname) {
		this.elementClassNames[2] = classname;

	}

	public void setElementClassName3(String classname) {
		this.elementClassNames[3] = classname;

	}

	/**
	 * put your documentation comment here
	 * 
	 * @return
	 */
	public String getElementClassName0() {
		return this.elementClassNames[0];
	}

	public String getElementClassName1() {
		return this.elementClassNames[1];
	}

	public String getElementClassName2() {
		return this.elementClassNames[2];
	}

	public String getElementClassName3() {
		return this.elementClassNames[3];
	}

	/**
	 * put your documentation comment here
	 * 
	 * @param obj
	 */
	public void setElementClass0(Object obj) {
		setElementClassName0((obj != null) ? obj.getClass().getName() : null);
	}

	public void setElementClass1(Object obj) {
		setElementClassName1((obj != null) ? obj.getClass().getName() : null);
	}

	public void setElementClass2(Object obj) {
		setElementClassName2((obj != null) ? obj.getClass().getName() : null);
	}

	public void setElementClass3(Object obj) {
		setElementClassName3((obj != null) ? obj.getClass().getName() : null);
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
			for (int k = 0; k < plotNumber * this.graphTypeNumber; k++) {
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
		for (int k = 0; k < plotNumber * this.graphTypeNumber; k++) {
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
		graphTypeNumber = 0;
		attList = new JList(this.attributesDisplay);
		attCombo = new JComboBox(this.attributesDisplay);
		for (int i = 0; i < DEFAULT_NUM_GRAPHES; i++) {
			if (this.elementClassNames[i] != null) {
				try {
					this.elementClasses[graphTypeNumber] = Class
							.forName(this.elementClassNames[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				graphTypeNumber++;
			}
		}
		varTags = new String[plotNumber];
		if (this.dataObject != null) {
			this.element = new MatrixElement[plotNumber * graphTypeNumber];
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
		SPTagButton[] rowButton = new SPTagButton[this.graphTypeNumber];
		Dimension tagDm = new Dimension(0, 0);
		configButton = new JButton();
		try {
			for (int i = 0; i < graphTypeNumber + 1; i++) {
				for (int j = 0; j < plotNumber + 1; j++) {
					if ((i == 0) && (j == 0)) {
						c.weightx = 0.0;
						c.weighty = 0.0;
						c.gridwidth = 1;
						c.gridheight = 1;
						configButton.setIcon(this.matrixIcon);
						configButton.setMargin(FixedRowMatrix.nullInsets);
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
						c.gridwidth = AbstractMatrix.DEFAULT_BUTTON_CONSTRAINTS;
						c.gridheight = 1;
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER; // end
						// row
						varTags[j - 1] = this.attributesDisplay[plottedAttributes[j - 1]];
						columnButton[j - 1] = new SPTagButton(varTags[j - 1]);
						columnButton[j - 1]
								.setMargin(FixedRowMatrix.nullInsets);
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
						String yVarTag = this.attributesDisplay[yIdx];
						rowButton[i - 1] = new SPTagButton("s");
						rowButton[i - 1].setMargin(FixedRowMatrix.nullInsets);
						rowButton[i - 1]
								.setVerticalAlignment(SwingConstants.BOTTOM);
						AffineTransform trans = new AffineTransform();
						trans.rotate(-Math.PI / 2);
						Font font = new Font("", Font.BOLD, 11);
						font = font.deriveFont(trans);

						rowButton[i - 1].setFont(font);
						rowButton[i - 1].setText(yVarTag);
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
						dataIndices[1] = yIdx + 1;
						// construct of each element
						this.element[indexCurrent] = (MatrixElement) this.elementClasses[i - 1]
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
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("tagDm: " + tagDm.getWidth()
						+ tagDm.getHeight());
			}
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
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("action performed...");
		}
		MatrixElement source = (MatrixElement) e.getSource();
		String command = e.getActionCommand();
		if (command.compareTo(MatrixElement.COMMAND_POINT_SELECTED) == 0) {
			if (logger.isLoggable(Level.FINEST)) {
				System.out
						.println("SPMC.plotUnitPanel.actionPerformed(), point selected");
			}
			this.selectedObvsInt = source.getSelections();
			for (int k = 0; k < plotNumber * graphTypeNumber; k++) {
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
			for (int k = 0; k < plotNumber * graphTypeNumber; k++) {
				MatrixElement otherElement = element[k];
				// Don't recall the scatterplot which generated the original
				// event
				if (otherElement != source) {
					otherElement.setBivarColorClasser(this.bivarColorClasser,
							false);
				}
			}
			this.repaint();
		} else if (command.compareTo(MatrixElement.COMMAND_DATARANGE_SET) == 0) {
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("in axis reset...");
			}
			double[] xAxisExtents = source.getXAxisExtents();
			double[] yAxisExtents = source.getYAxisExtents();
			int pos = 0;
			for (int k = 0; k < this.graphTypeNumber * plotNumber; k++) {
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
			for (int i = 0; i < this.graphTypeNumber; i++) {
				if (element[c + i * plotNumber].getClass().getName().equals("geovista.geoviz.scatterplot.ScatterPlot")) {
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
		for (int i = 0; i < this.graphTypeNumber; i++) {
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

	/**
	 * put your documentation comment here
	 * 
	 * @param x
	 * @param y
	 */
	private void attSelectDialog(int x, int y) {
		attList.setSelectedIndices(this.plottedAttributes);
		if (this.dialog == null) {
			this.dialog = new JDialog(dummyFrame, "Attributes for Plot", true);
			JButton selectButton;
			JButton closeButton;
			dialog.setSize(200, 300);
			dialog.getContentPane().setLayout(new BorderLayout());
			attList
					.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			// attList.addListSelectionListener(this);
			// JScrollPane scrollPane = new JScrollPane(attList);
			this.dialogPane = new JScrollPane(attList);

			attCombo.setSelectedIndex(yIdx);
			attCombo.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					JComboBox cb = (JComboBox) e.getSource();
					yIdx = cb.getSelectedIndex();
				}
			});
			JPanel comboPanel = new JPanel(new BorderLayout());
			comboPanel.add(attCombo, BorderLayout.NORTH);

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
			attPanel.add(comboPanel);
			dialog.getContentPane().add(namePanel, BorderLayout.NORTH);
			dialog.getContentPane().add(attPanel, BorderLayout.CENTER);
			dialog.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		} else {
			this.dialogPane.setViewportView(attList);
		}
		this.attList.addListSelectionListener(this);
		this.plottedAttributes = attList.getSelectedIndices();
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
					.getX(), e.getY(), FixedRowMatrix.this);
			posNew = matrixLayout.location(posNew.x, posNew.y);
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
			if ((posDrag.x != posNew.x) && (posDrag.y == 0)) {
				lastPos = posLast.x;
				newPos = posNew.x;
				moveColumn(lastPos, newPos);
				posDrag = posNew;
				repaint();
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
					.getX(), e.getY(), FixedRowMatrix.this);
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
			plottedAttributes = theList.getSelectedIndices();
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