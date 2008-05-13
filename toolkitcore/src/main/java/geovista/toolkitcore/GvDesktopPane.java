/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 */

package geovista.toolkitcore;

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.KeyStroke;

/**
 * Assumptions: 1. One dataset at a time. 2. Maximum coordination as a default.
 * 
 * @author Frank Hardisty
 */

public class GvDesktopPane extends JDesktopPane implements
		PropertyChangeListener {
	final static Logger logger = Logger
			.getLogger(GvDesktopPane.class.getName());

	public GvDesktopPane() {
		super();
		addBindings();

	}

	protected void addBindings() {
		InputMap inputMap = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

		MyAction act = new MyAction();
		this.getActionMap().put("do something", act);
		act.addPropertyChangeListener(this);

		// Ctrl-b to go backward one character
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK);
		inputMap.put(key, "do something");

		// Ctrl-f to go forward one character
		key = KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK);
		inputMap.put(key, act);

		// Ctrl-p to go up one line
		key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
		inputMap.put(key, act);

		// Ctrl-n to go down one line
		key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
		inputMap.put(key, act);
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		logger.info("got key event");
		logger.info("char = " + e.getKeyChar());
		// ToolkitIO.saveImageToFile(this);

	}

	private class MyAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {

			firePropertyChange("name", "old", "new");

		}

	}

	public void propertyChange(PropertyChangeEvent e) {
		logger.info("a wonderful thing happened... property change!");
		logger.info("e.getNewValue() = " + e.getNewValue());
		logger.info("e.getPropertyName() = " + e.getPropertyName());

	}

}
