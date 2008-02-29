/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotRenderer
 Copyright (c), 2003, Frank Hardisty
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotRenderer.java,v 1.4 2006/02/17 17:21:23 hardisty Exp $
 $Date: 2006/02/17 17:21:23 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */
package geovista.symbolization.glyph;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Paint a glyph;
 * 
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.4 $
 */
public class GlyphPicker extends JDialog implements ActionListener {
	private JPanel myPanel = null;
	private JButton yesButton = null;
	private JButton noButton = null;
	private boolean answer = false;
	private Glyph glyph;

	public boolean getAnswer() {
		return answer;
	}

	public GlyphPicker(JFrame frame, boolean modal, String myMessage) {
		super(frame, modal);
		myPanel = new JPanel();
		getContentPane().add(myPanel);
		myPanel.add(new JLabel(myMessage));
		yesButton = new JButton("Yes");
		yesButton.addActionListener(this);
		myPanel.add(yesButton);
		noButton = new JButton("No");
		noButton.addActionListener(this);
		myPanel.add(noButton);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	public GlyphPicker() {
		// TODO Auto-generated constructor stub
	}

	public Glyph showGvDialog(Component component, String title,
			Color initialColor) {
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		if (yesButton == e.getSource()) {
			System.err.println("User chose yes.");
			answer = true;
			setVisible(false);
		} else if (noButton == e.getSource()) {
			System.err.println("User chose no.");
			answer = false;
			setVisible(false);
		}
	}

	public static void main(String[] args) {
		GlyphPicker picker = new GlyphPicker(null, false, "title");
		picker.setVisible(true);
	}

	public Glyph getGlyph() {
		return glyph;
	}

	public void setGlyph(Glyph glyph) {
		this.glyph = glyph;
	}

}
