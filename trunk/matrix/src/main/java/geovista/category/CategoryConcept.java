package geovista.category;

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

public class CategoryConcept extends JPanel{

	private BorderLayout allPaneLayout;
	private JPanel idPane;
	private GridLayout idPaneLayout;
	private JPanel conceptDspPane;
	private GridLayout dspPaneLayout;
	private JPanel classParametersPane;
	private GridLayout classParaPaneLayout;
	private Color currentColor;

	private int[] preferedVarID;



    public CategoryConcept() {
		super();
		this.setPreferredSize(new Dimension(100, 200));
		this.setMinimumSize(new Dimension (100,200));
		paneInit();
    }

	public void setPreferedVars(int[] preferedVars){
		this.preferedVarID = preferedVars;
	}

	public int[] getPreferedVars(){
		return this.preferedVarID;
	}

	private void paneInit(){
		this.allPaneLayout = new BorderLayout();
		this.setLayout(this.allPaneLayout);
		//construct id panel
		idPaneLayout = new GridLayout(2,2);
		this.idPane = new JPanel(this.idPaneLayout);
		JLabel idLabel = new JLabel("Category ID:");
		JTextField idField = new JTextField (16);
		JLabel shortNameLabel = new JLabel("Name:");
		JTextField shortNameField = new JTextField(16);
		this.idPane.add(idLabel);
		this.idPane.add(idField);
		this.idPane.add(shortNameLabel);
		this.idPane.add(shortNameField);
		//Construct description panel
		dspPaneLayout = new GridLayout(3,1);
		this.conceptDspPane = new JPanel(this.dspPaneLayout);
		JLabel dspLabel = new JLabel("Description:");
		JTextArea dspArea = new JTextArea();
		JPanel indicatedColor = new JPanel(new GridLayout(1,2));
		JLabel colorLabel = new JLabel("Prefered Color:");
		final JButton colorButton = new JButton();
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
		JLabel methodLabel = new JLabel("Application Context:");
		JTextField methodField = new JTextField(30);
		JLabel variableLabel = new JLabel("Prefered Classification method:");
		JTextField variableField = new JTextField(30);
		JLabel exampleLabel = new JLabel("Examples for This Category:");
		JTextField exampleField = new JTextField(30);
		this.classParametersPane.add(methodLabel);
		this.classParametersPane.add(methodField);
		this.classParametersPane.add(variableLabel);
		this.classParametersPane.add(variableField);
		this.classParametersPane.add(exampleLabel);
		this.classParametersPane.add(exampleField);

		//add these sub panels to class panel
		this.add(this.idPane, BorderLayout.NORTH);
		this.add(this.conceptDspPane, BorderLayout.CENTER);
		this.add(this.classParametersPane, BorderLayout.SOUTH);
		this.validate();
	}


}