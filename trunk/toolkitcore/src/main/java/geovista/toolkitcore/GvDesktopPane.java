/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 */

package geovista.toolkitcore;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;

import geovista.common.ui.VisualSettingsPopupAdapter;
import geovista.common.ui.VisualSettingsPopupListener;
import geovista.common.ui.VisualSettingsPopupMenu;

/**
 * Assumptions: 1. One dataset at a time. 2. Maximum coordination as a default.
 * 
 * @author Frank Hardisty
 */

public class GvDesktopPane extends JDesktopPane implements
		PropertyChangeListener, VisualSettingsPopupListener {
	final static Logger logger = Logger
			.getLogger(GvDesktopPane.class.getName());

	JInternalFrame lastFrame;
	FrameListener fListener;
	GeoVizToolkit parentKit;

	public GvDesktopPane() {
		super();
		VisualSettingsPopupMenu popMenu = new VisualSettingsPopupMenu(this);
		MouseAdapter listener = new VisualSettingsPopupAdapter(popMenu);
		popMenu.addMouseListener(listener);
		addMouseListener(listener);

		lastFrame = new JInternalFrame();
		addBindings();

	}

	@Override
	public void setSelectedFrame(JInternalFrame f) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.info("set internal frame");
		}
		if (fListener != null) {
			fListener.selectedFrameChanged(f);
		}
		super.setSelectedFrame(f);
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

	interface FrameListener {

		void selectedFrameChanged(JInternalFrame f);
	}

	public Color getIndicationColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public Color getSelectionColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSelectionBlur() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isSelectionFade() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setIndicationColor(Color indColor) {
		// TODO Auto-generated method stub

	}

	public void setSelectionColor(Color selColor) {
		// TODO Auto-generated method stub

	}

	public void useMultiIndication(boolean useMultiIndic) {
		// TODO Auto-generated method stub

	}

	public void useSelectionBlur(boolean selBlur) {
		logger.info("selection blur = " + selBlur);

		ToolkitBeanSet beanSet = parentKit.getTBeanSet();
		for (ToolkitBean bean : beanSet.getBeanSet()) {
			Object originalBean = bean.getOriginalBean();
			if (originalBean instanceof VisualSettingsPopupListener) {
				VisualSettingsPopupListener listener = (VisualSettingsPopupListener) originalBean;
				listener.useSelectionBlur(selBlur);
			}
		}

	}

	public void useSelectionFade(boolean selFade) {
		logger.info("selection fade = " + selFade);
		ToolkitBeanSet beanSet = parentKit.getTBeanSet();
		for (ToolkitBean bean : beanSet.getBeanSet()) {
			Object originalBean = bean.getOriginalBean();
			if (originalBean instanceof VisualSettingsPopupListener) {
				VisualSettingsPopupListener listener = (VisualSettingsPopupListener) originalBean;
				listener.useSelectionFade(selFade);
			}
		}

	}

}
