package edu.psu.geovista.common.classification;

/**
 * <p>Title: Studio applications</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ClassifierMLParameters extends JPanel implements ListSelectionListener {

	private static int UNBIASCOV = 0; //divided by N-1
	private static int GAUSSIAN_DISTRIBUTION = 0;
	private int distribution;
	private int isUnBiasCov;
	private int classNum = 3;
	private String[] attributeNames;
	private int[] selectedAttIdx;
	private JList attList;
	private JPanel attSelPanel;
	private JPanel classifierParaPane;
	private JCheckBox visualDisplayButton;
	private EventListenerList listenerListAction = new EventListenerList();
	protected final static Logger logger = Logger.getLogger(ClassifierMLParameters.class.getName());
    public ClassifierMLParameters(String[] attributeNames, int classNum) {
		super();
		this.attributeNames = attributeNames;
		this.classNum = classNum;
		this.AttributeSelectionPanel();
		this.classifierParaPanel();
		this.setLayout(new GridLayout(1,2));
		this.add(this.attSelPanel);
		this.add(this.classifierParaPane);
		this.setPreferredSize(new Dimension(300,250));
        this.revalidate();
    }

	public void setAttributeNames (String[] attributesNames){
		this.attributeNames = attributesNames;
	}

	public String[] getAttributeNames (){
		return this.attributeNames;
	}

	public void setSelectedAttIdx (int[] selectedAttIdx){
		this.selectedAttIdx = selectedAttIdx;
	}

	public int[] getSelectedAttIdx (){
		return this.selectedAttIdx;
	}

	public void setIsUnBiasCov(int isUnBiasCov){
		this.isUnBiasCov = isUnBiasCov;
	}

	public int getIsUnBiasCov(){
		return this.isUnBiasCov;
	}

	public void setVisualDisplay(boolean visualDisplay){
		this.visualDisplayButton.setSelected(visualDisplay);
	}

	public boolean getVisualDisplay(){
		return this.visualDisplayButton.isSelected();
	}

	public void setDistributionType(int distributionType){
		this.distribution = distributionType;
	}

	public int getDistributionType(){
		return this.distribution;
	}

	public void setClassNum (int classNum){
		this.classNum = classNum;
	}

	public int getClassNum (){
		return this.classNum;
	}

	private void AttributeSelectionPanel () {
		JScrollPane attPane;
		JButton seNoneButton;
		JButton seAllButton;
		this.attSelPanel = new JPanel(new BorderLayout());
		this.attList = new JList(this.attributeNames);
		this.selectedAttIdx = new int[this.attributeNames.length];
		for (int i = 0; i < this.attributeNames.length; i++){
			this.selectedAttIdx[i] = i;
		}
		this.attList.setSelectedIndices(this.selectedAttIdx);

		this.attList.setName("Attributes for Classifiers");
		JLabel attListName = new JLabel ("Attribute List");
		attList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			//attList.addListSelectionListener(this);
			//JScrollPane scrollPane = new JScrollPane(attList);
		attPane = new JScrollPane(attList);
			seNoneButton = new JButton("None");
			seNoneButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void actionPerformed (ActionEvent e) {
								logger.finest("about to press button Select");
					seNoneButton_actionPerformed(e);
								 logger.finest("after pressed button Select");
				}
			});
			seAllButton = new JButton("All");
			seAllButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
				public void actionPerformed (ActionEvent e) {
					seAllButton_actionPerformed(e);
				}
			});
			JPanel buttonPanel = new JPanel(new GridLayout(1,2));
			buttonPanel.add(seNoneButton);
			buttonPanel.add(seAllButton);

			this.attSelPanel.add(attListName, BorderLayout.NORTH);
			this.attSelPanel.add(attPane, BorderLayout.CENTER);
			this.attSelPanel.add(buttonPanel, BorderLayout.SOUTH);

		this.attList.addListSelectionListener(this);
		this.selectedAttIdx = attList.getSelectedIndices();
	}

	private void seNoneButton_actionPerformed (ActionEvent e) {
		this.attList.clearSelection();
	}

	private void seAllButton_actionPerformed (ActionEvent e) {
		this.selectedAttIdx = new int[this.attributeNames.length];
		for (int i = 0; i < this.attributeNames.length; i++){
			this.selectedAttIdx[i] = i;
		}
		this.attList.setSelectedIndices(this.selectedAttIdx);
	}

	public void valueChanged (ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
			return;
		JList theList = (JList)e.getSource();
		if (theList.isSelectionEmpty()) {
			return;
		}
		else {
			this.selectedAttIdx = this.attList.getSelectedIndices();
		}
	}

	private void classifierParaPanel () {
		this.classifierParaPane = new JPanel(new BorderLayout());

		JPanel paraPane = new JPanel (new GridLayout(7, 1));
		//Class Number Display
		JLabel classNumStringLabel = new JLabel("Class #:");
		JLabel classNumLabel = new JLabel(new Integer(this.classNum).toString());
		paraPane.add(classNumStringLabel);
		paraPane.add(classNumLabel);
		//Select proper distribution for data
		JLabel distributionLabel = new JLabel("Select Distribution");
		JComboBox distributionCombo = new JComboBox();
		distributionCombo.addItem("Gaussian");
		distributionCombo.addItem("Student T");
		this.distribution = ClassifierMLParameters.GAUSSIAN_DISTRIBUTION;
		distributionCombo.setMinimumSize(new Dimension(80,20));
		distributionCombo.setMaximumSize(new Dimension(120,20));
		distributionCombo.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			if (cb.getItemCount() > 0) {
				distribution = (int)cb.getSelectedIndex();
			}//end if count > 0
		  }//end inner class
		});//end add listener
		paraPane.add(distributionLabel);
        paraPane.add(distributionCombo);
		//Select Bias or unbias calculation for covariance matrix
		JLabel covLabel = new JLabel("Covariance matrix is");
		JComboBox covCombo = new JComboBox();
		covCombo.addItem("Unbias (divided by N-1)");
		covCombo.addItem("Bias (divided by N)");
		this.isUnBiasCov = ClassifierMLParameters.UNBIASCOV;
		covCombo.setMinimumSize(new Dimension(80,20));
		covCombo.setMaximumSize(new Dimension(120,20));
		covCombo.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			if (cb.getItemCount() > 0) {
				isUnBiasCov = (int)cb.getSelectedIndex();
			}//end if count > 0
		  }//end inner class
		});//end add listener
		paraPane.add(covLabel);
        paraPane.add(covCombo);

		this.visualDisplayButton = new JCheckBox("Display Distribution");
		this.visualDisplayButton.setSelected(false);
		paraPane.add(this.visualDisplayButton);

		//Conclusion button panel
		JPanel concludePane = new JPanel (new GridLayout(1, 2));
		JButton applyButton= new JButton ("Apply");
		applyButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
			public void actionPerformed (ActionEvent e) {
				applyButton_actionPerformed(e);
			}
		});
		JButton cancelButton = new JButton ("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {

				/**
				 * put your documentation comment here
				 * @param e
				 */
			public void actionPerformed (ActionEvent e) {
			cancelButton_actionPerformed(e);
			}
		});
		concludePane.add(applyButton);
		concludePane.add(cancelButton);

		this.classifierParaPane.add(paraPane, BorderLayout.NORTH);
		this.classifierParaPane.add(concludePane, BorderLayout.SOUTH);

	}

	private void applyButton_actionPerformed (ActionEvent e) {

		fireActionPerformed (e);
	}

	//Cancel, means using default parameter sets, all of attributes are considered and non initial points are specified.
	private void cancelButton_actionPerformed (ActionEvent e) {
		this.selectedAttIdx = new int[this.attributeNames.length];
		for (int i = 0; i < this.attributeNames.length; i++){
			this.selectedAttIdx[i] = i;
		}
		this.attList.setSelectedIndices(this.selectedAttIdx);

		fireActionPerformed (e);
	}

	/**
	 * adds an ActionListener to the button
	 */
	public void addActionListener (ActionListener l) {
		listenerListAction.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener (ActionListener l) {
		listenerListAction.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type. The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	public void fireActionPerformed (ActionEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerListAction.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
			ActionEvent e2 = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"OK");
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				((ActionListener)listeners[i + 1]).actionPerformed(e2);
			}
		}
	}

}