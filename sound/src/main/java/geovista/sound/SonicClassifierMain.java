/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.sound;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import geovista.common.classification.ClassifierPicker;
import geovista.readers.example.GeoData48States;

public class SonicClassifierMain {
	protected SonicClassifier vc;

	public SonicClassifierMain(String name) {
		// super(name);
		setUp();
	}

	protected void setUp() {
		vc = new SonicClassifier();
	}

	/*
	 * public void testSymbolize() { Color[] colors =
	 * vc.symbolize(vc.getNumClasses());
	 * 
	 * assertTrue(colors[0].getRed() == 255); assertTrue(colors[2].getRed() ==
	 * 128); assertTrue(colors[4].getRed() == 0);
	 * 
	 * }
	 */

	/**
	 * Main method for testing.
	 */
	public static void main(String[] args) {

		JFrame app = new JFrame();
		app.getContentPane().setLayout(
				new BoxLayout(app.getContentPane(), BoxLayout.Y_AXIS));

		SonicClassifier vc = new SonicClassifier();
		app.getContentPane().add(vc);
		// app.getContentPane().add(vc2);
		// app.getContentPane().add(vc3);

		GeoData48States data = new GeoData48States();
		vc.setDataSet(data.getDataForApps());

		vc
				.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);

		vc.getSymbolizationPanel().playKey(0);
		vc.getSymbolizationPanel().playKey(1);
		vc.getSymbolizationPanel().playKey(2);
		vc.getSymbolizationPanel().playKey(0);
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

		/*
		 * try { XMLEncoder enc = new XMLEncoder( new BufferedOutputStream( new
		 * FileOutputStream("test_write_vc.xml"))); enc.writeObject(vc);
		 * enc.close(); } catch (Exception ex) { ex.printStackTrace(); }
		 */

		// vc.makeColors();
	}

}