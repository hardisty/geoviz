package geovista.category;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * 
 */

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SelectionRecords extends JPanel {

	private static String DEFAULT_USERINFO = "new";
	private static int DEFAULT_VERSION = 0;
	private static String DEFAULT_THEORY = "not clear";
	private static String DEFAULT_EXAMPLE_TYPE = "ideal";

	private String userInfo = DEFAULT_USERINFO;
	private int version = DEFAULT_VERSION;
	private String theory = DEFAULT_THEORY;
	private String exampleType = DEFAULT_EXAMPLE_TYPE;

	private String userInfoStr = "User Info:";
	private String versionStr = "Version (time) Info:";
	private String theoryStr = "Theory Used:";
	private String exampleTypeStr = "Example Type:";

	private JLabel userInfoLabel;
	private JLabel versionLabel;
	private JLabel theoryLabel;
	private JLabel exampleTypeLabel;

	private JTextField userInfoField;
	private JTextField versionField;
	private JTextField theoryField;
	private JTextField exampleTypeField;

	private String entityInputStr = "Input records entities:";
	private JLabel entityLabel;
	private int[] selection; //one selection record.
	private int[] classification;
	private Object[] categoryRecord; //one record for a selection or classification event.
	private Vector categoryRecords;

    public SelectionRecords() {
        super();
		this.setLayout(new BorderLayout());
		this.setSize(300, 300);
		init();
    }

	public void setSelection(int[] selection){
		this.selection = selection;
	}

	public int[] getSelection(){
	    return this.selection;
	}

	public void setClassification(int[] classes){
		this.classification = classes;
	}

	public int[] getClassification(){
	    return this.classification;
	}

	public void setCategoryRecords (Vector categoryRecords){
	  this.categoryRecords = categoryRecords;
	}

	public Vector getCategoryRecords (){
	  return this.categoryRecords;
	}

	void init(){
		this.categoryRecord = new Object[6];

		JButton okButton;
		JButton cancelButton;
		okButton = new JButton("SAVE");
		okButton.addActionListener(new java.awt.event.ActionListener() {
                /**
                 * put your documentation comment here
                 * @param e
                 */
		public void actionPerformed (ActionEvent e) {
				okButton_actionPerformed(e);
			}
		});

		cancelButton = new JButton("CANCEL");;
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
                cancelButton_actionPerformed(e);
            }
        });

		entityLabel = new JLabel(entityInputStr);

		userInfoLabel = new JLabel(userInfoStr);
		versionLabel = new JLabel(versionStr);
		theoryLabel = new JLabel(theoryStr);
		exampleTypeLabel = new JLabel(exampleTypeStr);

		userInfoField = new JTextField(16);
		versionField = new JTextField(8);
		theoryField = new JTextField(8);
		exampleTypeField = new JTextField(8);

		userInfoField.setText(userInfo);
		versionField.setText((new Integer(version)).toString());
		theoryField.setText(theory);
		exampleTypeField.setText(exampleType);

		JPanel userInfoPanel = new JPanel(new GridLayout(4,2));
		userInfoPanel.add(userInfoLabel);
		userInfoPanel.add(userInfoField);
		userInfoPanel.add(versionLabel);
		userInfoPanel.add(versionField);
		userInfoPanel.add(theoryLabel);
		userInfoPanel.add(theoryField);
		userInfoPanel.add(exampleTypeLabel);
		userInfoPanel.add(exampleTypeField);

		JPanel buttonPanel = new JPanel (new BorderLayout());
		buttonPanel.add(okButton, BorderLayout.WEST);
		buttonPanel.add(cancelButton, BorderLayout.EAST);

		this.add(entityLabel, BorderLayout.NORTH);
		this.add(userInfoPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);

		this.validate();
	}

	private void okButton_actionPerformed (ActionEvent e) {
		this.userInfo = this.userInfoField.getText();
		this.version = Integer.parseInt(this.versionField.getText());
		this.theory = this.theoryField.getText();
		this.exampleType = this.exampleTypeField.getText();
		this.categoryRecord[0] = this.userInfo;
		this.categoryRecord[1] = new Integer(this.version);
		this.categoryRecord[2] = this.theory;
		this.categoryRecord[3] = this.exampleType;
		this.categoryRecord[4] = this.selection;
		this.categoryRecord[5] = this.classification;
	}

	void cancelButton_actionPerformed(ActionEvent e){
        //System.exit(0);
    }

}