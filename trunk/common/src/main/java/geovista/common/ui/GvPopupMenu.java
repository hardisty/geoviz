/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Common operations to be supported by context menus supported here.
 */
public class GvPopupMenu extends JPopupMenu {
	ArrayList<JMenuItem> items;
	ActionListener itemListener;

	/**
	 * null ctr
	 */
	public GvPopupMenu(ActionListener itemListener) {
		super();
		this.itemListener = itemListener;
		makeItems();

	}

	private void makeItems() {
		JMenuItem bgColorItem = new JMenuItem("Choose background color");
		this.add(bgColorItem);
		bgColorItem.addActionListener(itemListener);
		JMenuItem indColorItem = new JMenuItem("Choose selection color");
		this.add(indColorItem);
		indColorItem.addActionListener(itemListener);
	}

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		GvMenuClient client = new GvMenuClient();
		app.add(client);
		app.setVisible(true);

	}

}