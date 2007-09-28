package edu.psu.geovista.category;

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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CategoryConceptPane extends JPanel{

	private BorderLayout allPaneLayout;
	private JPanel idPane;
	private GridLayout idPaneLayout;
	private JPanel conceptDspPane;
	private GridLayout dspPaneLayout;
	private JPanel classParametersPane;
	private GridLayout classParaPaneLayout;
	private JTextField idField;
	private JTextField shortNameField;
	private JTextField classLabelField;
	private JTextArea dspArea;
	private JTextField contextField;
	private JTextField methodField;
	private JTextField exampleField;
	private final JButton colorButton = new JButton();

	private Color currentColor;

	private String categoryID;
	private String shortName;
	private String classLabel;

	private String descriptions;

	private String context;
	private String method;
	private int[] examples;

	public CategoryConceptPane() {
		super();
		this.setPreferredSize(new Dimension(150, 200));
		this.setMinimumSize(new Dimension (150,200));
		paneInit();
    }

	private void paneInit(){
		this.allPaneLayout = new BorderLayout();
		this.setLayout(this.allPaneLayout);

		//construct id panel
		idPaneLayout = new GridLayout(3,2);
		this.idPane = new JPanel(this.idPaneLayout);
		JLabel idLabel = new JLabel("Category ID:");
		idField = new JTextField (16);
		JLabel shortNameLabel = new JLabel("Name:");
		shortNameField = new JTextField(16);
		JLabel classLabel = new JLabel("Category Label:");
		classLabelField = new JTextField (16);
		this.idPane.add(idLabel);
		this.idPane.add(idField);
		this.idPane.add(shortNameLabel);
		this.idPane.add(shortNameField);
		this.idPane.add(classLabel);
		this.idPane.add(classLabelField);

		//Construct description panel
		dspPaneLayout = new GridLayout(3,1);
		this.conceptDspPane = new JPanel(this.dspPaneLayout);
		JLabel dspLabel = new JLabel("Description:");
		dspArea = new JTextArea();
		JPanel indicatedColor = new JPanel(new GridLayout(1,2));
		JLabel colorLabel = new JLabel("Prefered Color:");

		colorButton.setBackground(Color.lightGray);
		colorButton.setBorderPainted(true);
		colorButton.setMargin(new Insets(2,2,2,2));
		//Now create an editor to encapsulate the button, and
		//set it up as the editor for all Color cells.
		//Set up the dialog that the button brings up.
		final JColorChooser colorChooser = new JColorChooser();
		currentColor = Color.white;
		ActionListener okListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentColor = colorChooser.getColor();
				colorButton.setBackground(currentColor);
			}
		};
		final JDialog dialog = JColorChooser.createDialog(colorButton,
										"Pick a Color",
										true,
										colorChooser,
										okListener,
										null); //XXXDoublecheck this is OK

		//Here's the code that brings up the dialog.
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//colorButton.setBackground(currentColor);
				//colorChooser.setColor(currentColor);
				//Without the following line, the dialog comes up
				//in the middle of the screen.
				dialog.setLocationRelativeTo(colorButton);
				dialog.setVisible(true);
			}
		});
		indicatedColor.add(colorLabel);
		indicatedColor.add(colorButton);
		this.conceptDspPane.add(dspLabel);
		this.conceptDspPane.add(dspArea);
		this.conceptDspPane.add(indicatedColor);

		//Construct classification preference panel
		classParaPaneLayout = new GridLayout(6,1);
		this.classParametersPane = new JPanel(this.classParaPaneLayout);
		JLabel contextLabel = new JLabel("Application Context:");
		contextField = new JTextField(30);
		JLabel methodLabel = new JLabel("Prefered Classification method:");
		methodField = new JTextField(30);
		JLabel exampleLabel = new JLabel("Examples for This Category:");
		exampleField = new JTextField(30);
		this.classParametersPane.add(contextLabel);
		this.classParametersPane.add(contextField);
		this.classParametersPane.add(methodLabel);
		this.classParametersPane.add(methodField);
		this.classParametersPane.add(exampleLabel);
		this.classParametersPane.add(exampleField);

		//add these sub panels to class panel
		this.add(this.idPane, BorderLayout.NORTH);
		this.add(this.conceptDspPane, BorderLayout.CENTER);
		this.add(this.classParametersPane, BorderLayout.SOUTH);
		this.validate();
	}

	public String getCategoryID() {
      categoryID = this.idField.getText();
        return categoryID;
    }
    public String getClassLabel() {
    classLabel = this.classLabelField.getText();
        return classLabel;
    }
    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
        this.idField.setText(this.categoryID);
    }
    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
        this.classLabelField.setText(this.classLabel);
    }
    public String getContext() {
		context = this.contextField.getText();
        return context;
    }
    public void setContext(String context) {
        this.context = context;
        this.contextField.setText(this.context);
    }
    public Color getCurrentColor() {
        return currentColor;
    }
    public String getDescriptions() {
      this.descriptions = this.dspArea.getText();
        return descriptions;
    }
    public void setCurrentColor(Color currentColor) {
        this.currentColor = currentColor;
        this.colorButton.setBackground(this.currentColor);
    }
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
        this.dspArea.setText(this.descriptions);
    }
    public int[] getExamples() {
        return examples;
    }
    public String getMethod() {
      this.method = this.methodField.getText();
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
        this.methodField.setText(this.method);
    }
    public String getShortName() {
		this.shortName = this.shortNameField.getText();
        return shortName;
    }
    public void setShortName(String shortName) {
        this.shortName = shortName;
		this.shortNameField.setText(this.shortName);
    }

}
