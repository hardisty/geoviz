package edu.psu.geovista.app.matrix;

/**
 * <p>Title: Studio applications</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.psu.geovista.category.MultiClassDistributions;


public class DistributionMatrix extends AbstractMatrix
		implements  ListSelectionListener
{


	private String[] variableNames;
	protected transient EventListenerList listenerList = new EventListenerList();
	private SPTagButton[] columnButton;
	private String[] varTags;
	private Vector[] dataVector;
	private MultiClassDistributions distributions[];

    public DistributionMatrix()
    {
		super();
		this.setSize(panelWidthPixels, panelHeightPixels);
    }

	public void setDataVector(Vector[] dataVector){
		this.dataVector = dataVector;
		init();
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
		varTags = new String[plotNumber];
		if (this.dataVector != null) {
			this.distributions = new MultiClassDistributions[plotNumber];
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

	//Set up elements in matrix.
	protected void createMatrix () {
		logger.finest("create SP:");
		this.setLayout(matrixLayout);
		JButton configButton = new JButton("m");
		SPTagButton rowButton;
		columnButton = new SPTagButton[plotNumber];
		Dimension tagDm = new Dimension(0, 0);
		try {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < plotNumber + 1; j++) {
					if ((i == 0) && (j == 0)) {
						c.weightx = 0.0;
						c.weighty = 0.0;
						c.gridwidth = 1;
						c.gridheight = 1;
						//configButton = new JButton("M");
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
						c.weightx = 1.0;
						c.weighty = 0.0;
						c.gridwidth = 4;
						c.gridheight = 1;
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER;            //end row
						varTags[j-1] = this.variableNames[plottedAttributes[j - 1]];
						columnButton[j-1] = new SPTagButton(varTags[j-1]);
						columnButton[j-1].addMouseListener(columnButton[j-1]);
						columnButton[j-1].addMouseMotionListener(columnButton[j-1]);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(columnButton[j-1], c);
						add(columnButton[j-1]);

					}
					else if ((i != 0) && (j == 0)) {
						c.weightx = 0.0;
						c.weighty = 1.0;
						c.gridwidth = 1;
						c.gridheight = 4;
						rowButton = new SPTagButton("s");
						//JLabel rowLabel = new JLabel();
						AffineTransform trans = new AffineTransform();
						trans.rotate(-Math.PI/2);
						Font font = new Font("", Font.BOLD, 12);
						font = font.deriveFont(trans);
						//rowLabel.setFont(font);
						//BufferedImage labelImage = new BufferedImage(rowLabel.getWidth()+1,rowLabel.getHeight()+1,BufferedImage.TYPE_INT_ARGB);
						//Graphics2D g2 = labelImage.createGraphics();
						//rowButton[i-1].paint(g2);
						rowButton.setFont(font);
						rowButton.setText("Distributions");
						rowButton.addMouseListener(rowButton);
						rowButton.addMouseMotionListener(rowButton);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints(rowButton, c);
						add(rowButton);
					}
					else {
						c.weightx = 1.0;
						c.weighty = 1.0;
						c.gridwidth = 4;
						c.gridheight = 4;
						if (j == plotNumber)
							c.gridwidth = GridBagConstraints.REMAINDER;              //end row
						int indexCurrent = (j - 1);
						int dataIndex;
						dataIndex = plottedAttributes[j - 1];
						//construct of each element
						this.distributions[indexCurrent] = new MultiClassDistributions();
						this.distributions[indexCurrent].setAxisOn(false);
						this.distributions[indexCurrent].setDisplayVariableIndex(dataIndex);
						this.distributions[indexCurrent].setBackground(background);
						this.distributions[indexCurrent].setGaussOn(true);
						this.distributions[indexCurrent].setDataVector(this.dataVector);
						c.column = j;
						c.row = i;
						matrixLayout.setConstraints((Component)this.distributions[indexCurrent], c);
						add((Component)this.distributions[indexCurrent]);

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
	 * Shift columns or rows in the matrix.
	 * @param lastPos
	 * @param newPos
	 */
	private void moveColumn (int lastPos, int newPos) {
		int indicesRow;
		int indicesLast1;
		int indicesNew1;

		String varTagMoved = new String(varTags[lastPos - 1]);
		varTags[lastPos - 1] = varTags[newPos - 1];
		this.columnButton[lastPos - 1].setText(varTags[lastPos - 1]);
		varTags[newPos - 1] = varTagMoved;
		this.columnButton[newPos - 1].setText(varTags[newPos - 1]);
		logger.finest("lastPos: " + lastPos + "newPos:" + newPos);
		int graphTypeNumber = 1;
		for (int i = 0; i < graphTypeNumber; i++) {
			indicesLast1 = (distributions[i*plotNumber + lastPos - 1].getDisplayVariableIndex());
			//indicesLast2 = (element[(lastPos - 1)*plotNumber + i].getElementPosition());
			indicesRow = indicesLast1;
			//indicesCol = indicesLast2[1];
			indicesNew1 = distributions[i*plotNumber + newPos - 1].getDisplayVariableIndex();
			//indicesNew2 = element[(newPos - 1)*plotNumber + i].getElementPosition();
			indicesLast1 = indicesNew1;
			//indicesLast2[1] = indicesNew2[1];
			distributions[i*plotNumber + lastPos - 1].setDisplayVariableIndex(indicesLast1);
			//element[(lastPos - 1)*plotNumber + i].setElementPosition(indicesLast2);
			indicesNew1 = indicesRow;
			//indicesNew2[1] = indicesCol;
			distributions[i*plotNumber + newPos - 1].setDisplayVariableIndex(indicesNew1);
			//element[(newPos - 1)*plotNumber + i].setElementPosition(indicesNew2);
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

	protected class SPTagButton extends JButton
			implements MouseListener, MouseMotionListener {

		/**
		 * put your documentation comment here
		 * @param 		String label
		 */
		SPTagButton (String label) {
			super(label);
			//buttonLabel = label;
		}

		/**
		 * put your documentation comment here
		 * @param 		ImageIcon icon
		 */
		SPTagButton (ImageIcon icon) {
			super(icon);
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mousePressed (MouseEvent e) {
			JButton button = (JButton)e.getSource();
			SPGridBagConstraints gbconst = (SPGridBagConstraints)matrixLayout.getConstraints(button);
			posLast = new Point(gbconst.column, gbconst.row);
			posDrag = (Point)posLast.clone();
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mouseReleased (MouseEvent e) {
			posNew = SwingUtilities.convertPoint((SPTagButton)e.getSource(), e.getX(),
					e.getY(), DistributionMatrix.this);
			posNew = matrixLayout.location(posNew.x, posNew.y);
			if (posNew.x > 4*(plotNumber - 1))
				posNew.setLocation(posNew.x/4 + 1, posNew.y/4);
			else
				posNew.setLocation(posNew.x/4, posNew.y/4);
			logger.finest("PosNewX: " + posNew.x + "posNewY: " + posNew.y);
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
			/*else if (posDrag.y != posNew.y) {
				lastPos = posLast.y;
				newPos = posNew.y;
			}
			if (lastPos != newPos) {
				moveRowAndColumn(lastPos, newPos);
				posDrag = posNew;
				repaint();
			}*/
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mouseExited (MouseEvent e) {
			;
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mouseDragged (MouseEvent e) {
			posNew = SwingUtilities.convertPoint((SPTagButton)e.getSource(), e.getX(),
					e.getY(), DistributionMatrix.this);
			posNew = matrixLayout.location(posNew.x, posNew.y);
			logger.finest("PosNew0X: " + posNew.x + "posNew0Y: " + posNew.y);
			if (posNew.x > 4*(plotNumber - 1))
				posNew.setLocation(posNew.x/4 + 1, posNew.y/4);
			else
				posNew.setLocation(posNew.x/4, posNew.y/4);
			logger.finest("PosNewX: " + posNew.x + "posNewY: " + posNew.y);
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
			/*else if (posDrag.y != posNew.y) {
				lastPos = posDrag.y;
				newPos = posNew.y;
			}
			if (lastPos != newPos) {
				moveRowAndColumn(lastPos, newPos);
				posDrag = posNew;
				repaint();
			}*/
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mouseMoved (MouseEvent e) {
			;
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mouseEntered (MouseEvent e) {
			;
		}

		/**
		 * put your documentation comment here
		 * @param e
		 */
		public void mouseClicked (MouseEvent e) {
			;
		}
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