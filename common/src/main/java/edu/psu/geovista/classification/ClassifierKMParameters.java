package edu.psu.geovista.classification;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ClassifierKMParameters extends JPanel implements ListSelectionListener {
	protected final static Logger logger = Logger.getLogger(ClassifierKMParameters.class.getName());
	private int classNum = 3;
	private String[] attributeNames;
	private int[] selectedAttIdx;
	private JList attList;
	private JPanel attSelPanel;
	private JPanel classifierParaPane;
	private JTextField[] iniField;
	private int[] iniObsIdx;
	private EventListenerList listenerListAction = new EventListenerList();

    public ClassifierKMParameters(String[] attributeNames, int classNum) {
		super();
		this.attributeNames = attributeNames;
		this.classNum = classNum;
		this.AttributeSelectionPanel();
		this.classifierParaPanel();
        this.setLayout(new GridLayout(1,2));
        this.add(this.attSelPanel);
        this.add(this.classifierParaPane);
        this.setPreferredSize(new Dimension(300,50));
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

	public int[] getIniObsIdx(){
	    return this.iniObsIdx;
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

		//Class Number Display
		JPanel classNumPane = new JPanel (new GridLayout(1, 2));
		JLabel classNumStringLabel = new JLabel("Class #:");
		JLabel classNumLabel = new JLabel(new Integer(this.classNum).toString());
	    classNumPane.add(classNumStringLabel);
		classNumPane.add(classNumLabel);

		//Initial points setup
		JPanel iniPane = new JPanel(new BorderLayout());
		JLabel iniStringLabel = new JLabel("Set up Initial Points:");

		JPanel iniSetPane = new JPanel (new GridLayout(this.classNum, 2));
		JLabel[] iniLabel = new JLabel[this.classNum];
		iniField = new JTextField[this.classNum];
		for (int i = 0; i < this.classNum; i ++){
			iniLabel[i] = new JLabel(new Integer(i).toString());
			iniField[i] = new JTextField();
			iniField[i].setText(new Integer(i).toString());
			iniField[i].setAlignmentY(Component.TOP_ALIGNMENT);
			iniSetPane.add(iniLabel[i]);
			iniSetPane.add(iniField[i]);
		}
		iniPane.add(iniStringLabel, BorderLayout.NORTH);
		iniPane.add(iniSetPane, BorderLayout.CENTER);

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

		this.classifierParaPane.add(classNumPane, BorderLayout.NORTH);
		this.classifierParaPane.add(iniPane, BorderLayout.CENTER);
		this.classifierParaPane.add(concludePane, BorderLayout.SOUTH);

	}

	private void applyButton_actionPerformed (ActionEvent e) {
	    this.iniObsIdx = new int[this.classNum];
		for (int i = 0; i < this.classNum; i++){
		    this.iniObsIdx[i] = Integer.parseInt(iniField[i].getText());
		}
		fireActionPerformed (e);
	}

	//Cancel, means using default parameter sets, all of attributes are considered and non initial points are specified.
	private void cancelButton_actionPerformed (ActionEvent e) {
		this.selectedAttIdx = new int[this.attributeNames.length];
		for (int i = 0; i < this.attributeNames.length; i++){
			this.selectedAttIdx[i] = i;
		}
		this.attList.setSelectedIndices(this.selectedAttIdx);
		iniObsIdx = null;
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