/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/

package geovista.common.color;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * 
 * @author Frank Hardisty
 */
public class Palette1DPicker extends JPanel {

	public static void main(String[] vals) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Palette1DPicker vc = new Palette1DPicker();

		app.add(vc);
		app.pack();
		app.setVisible(true);

	}
}
