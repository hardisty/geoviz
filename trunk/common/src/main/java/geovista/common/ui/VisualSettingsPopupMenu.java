/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Common operations to be supported by context menus supported here.
 */
public class VisualSettingsPopupMenu extends JPopupMenu implements
		ActionListener {

	private final VisualSettingsPopupListener settingsListener;
	private JMenuItem itemBgColor;

	private JMenuItem itemSelColor;

	private JMenuItem itemIndColor;

	private JMenuItem itemLeaderColor;

	private JCheckBoxMenuItem itemBoxUseSelectionFade;

	private JCheckBoxMenuItem itemBoxUseSelectionBlur;

	final static Logger logger = Logger.getLogger(VisualSettingsPopupMenu.class
			.getName());

	/**
	 * null ctr
	 */
	public VisualSettingsPopupMenu(VisualSettingsPopupListener settingsListener) {
		super();
		this.settingsListener = settingsListener;
		makeItems();

	}

	public void addCheckBoxItem(String text, boolean selected) {
		JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(text);
		this.add(menuItem);
		menuItem.setSelected(selected);
		menuItem.addActionListener(this);

	}

	private void makeItems() {
		itemBgColor = new JMenuItem("Choose background color");
		// itemBgColor.setEnabled(false);
		this.add(itemBgColor);
		itemBgColor.addActionListener(this);
		itemSelColor = new JMenuItem("Choose selection color");
		// 32 itemSelColor.setEnabled(false);
		this.add(itemSelColor);
		itemSelColor.addActionListener(this);
		itemIndColor = new JMenuItem("Choose indication color");
		itemIndColor.setEnabled(false);
		this.add(itemIndColor);
		itemIndColor.addActionListener(this);

		itemLeaderColor = new JMenuItem("Choose leader line color");
		itemLeaderColor.setEnabled(false);
		this.add(itemLeaderColor);
		itemLeaderColor.addActionListener(this);

		addSeparator();
		itemBoxUseSelectionFade = new JCheckBoxMenuItem(
				"Use fade in selections?");

		itemBoxUseSelectionFade.setSelected(settingsListener.isSelectionFade());
		this.add(itemBoxUseSelectionFade);
		itemBoxUseSelectionFade.addActionListener(this);
		itemBoxUseSelectionBlur = new JCheckBoxMenuItem(
				"Use blur in selections?");

		itemBoxUseSelectionBlur.setSelected(settingsListener.isSelectionBlur());

		this.add(itemBoxUseSelectionBlur);
		itemBoxUseSelectionBlur.addActionListener(this);

	}

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		VisualSettingsMenuClientExample client = new VisualSettingsMenuClientExample();
		app.add(client);
		app.pack();
		app.setVisible(true);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == itemBgColor) {

			Color bgColor = JColorChooser
					.showDialog(this, "Pick a background color",
							settingsListener.getBackground());
			settingsListener.setBackground(bgColor);
		} else if (e.getSource() == itemSelColor) {
			Color bgColor = JColorChooser.showDialog(this,
					"Pick a selection color", settingsListener
							.getSelectionColor());
			settingsListener.setSelectionColor(bgColor);
		} else if (e.getSource() == itemIndColor) {
			Color bgColor = JColorChooser.showDialog(this,
					"Pick an indication color", settingsListener
							.getIndicationColor());
			settingsListener.setIndicationColor(bgColor);

		} else if (e.getSource() == itemLeaderColor) {
			Color bgColor = JColorChooser.showDialog(this,
					"Pick an leader line color", settingsListener
							.getIndicationColor());
			settingsListener.setIndicationColor(bgColor);

		} else if (e.getSource() == itemBoxUseSelectionBlur) {
			boolean value = itemBoxUseSelectionBlur.isSelected();
			settingsListener.useSelectionBlur(value);
		} else if (e.getSource() == itemBoxUseSelectionFade) {
			boolean value = itemBoxUseSelectionFade.isSelected();
			settingsListener.useSelectionFade(value);
		} else if (e.getSource() instanceof JCheckBoxMenuItem) {
			logger.info("hit custom checkbox");
			JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			boolean value = item.isSelected();
			settingsListener.processCustomCheckBox(value, item.getText());
		} else {
			logger.severe("unknown action performed");
		}

	}
}