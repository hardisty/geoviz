/**
 * 
 */
package ncg.statistics;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;

//import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author pfoley
 *
 */
@SuppressWarnings("serial")
public class DiscriminantAnalysisGUI extends JPanel
	implements ActionListener {
	//implements ActionListener ,DataSetListener {
	 
	DiscriminantAnalysis da;
	JButton goButton;
	
	public DiscriminantAnalysisGUI() {
		super();
		da = new DiscriminantAnalysis();
		goButton = new JButton("go!");
		this.add(goButton);
		goButton.addActionListener(this);
	}

	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DiscriminantAnalysisGUI daGui = new DiscriminantAnalysisGUI();
		app.add(daGui);
		app.pack();
		app.setVisible(true);

	}




	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == this.goButton){
			System.out.println("Clicked!");
		}
		
	}


	public void dataSetChanged(DataSetEvent e) {
		DataSetForApps dsa = e.getDataSetForApps();
		int numNumericArrays = dsa.getNumberNumericAttributes();
		for (int i = 0;  i < numNumericArrays; i++){
			double[] data = dsa.getNumericDataAsDouble(i);
		}
		
		
	}

}
