/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.sound;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class SonicRampPickerMain {
	protected SonicRampPicker vc;

	/*
	 * public SonicRampPickerMain(String name) { super(name); setUp(); } public
	 * static void main2 (String[] args) { junit.textui.TestRunner.run
	 * (suite()); } protected void setUp() { vc = new SonicRampPicker(); }
	 * public static Test suite() { //return new
	 * TestSuite(SonicRampPickerTest.class); return null; }
	 */

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
		app.getContentPane().setLayout(new BorderLayout());

		SonicRampPicker pick = new SonicRampPicker();

		// pick.panSet[3].setSwatchColor(Color.black);
		// pick.panSet[3].setAnchored(true);
		// pick.ramp.rampColors();
		// pick.rampSwatches();
		app.getContentPane().add(pick);

		// app.getContentPane().add(this,BorderLayout.SOUTH);
		app.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// app.getContentPane().add(setColorsPan);

		app.pack();
		app.setVisible(true);

	}

}
