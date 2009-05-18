/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.coordination;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * 
 * This class provides shortcut buttons to connect or disconnect all of the
 * incoming or outgoing coordinated connections for this bean. The original
 * design calls for a CoordinationManagerGUI to hold a reference to one of these
 * per coordinated bean, and to listen for GUI events.
 * 
 * 
 * @see CoordinationManager
 * @author Frank Hardisty
 */
public class BeanShortcutsGUI extends JPanel implements ActionListener {
	static public String EVENT_INCOMING_ON = "incomingOn";
	static public String EVENT_INCOMING_OFF = "incomingOff";
	static public String EVENT_OUTGOING_ON = "outgoingOn";
	static public String EVENT_OUTGOING_OFF = "outgoingOff";
	private transient final JButton incomingOn;
	private transient final JButton incomingOff;
	private transient final JButton outgoingOn;
	private transient final JButton outgoingOff;
	private transient final JPanel buttonsPanel;

	/**
   *
  */
	public BeanShortcutsGUI(String beanName, Icon icon) {
		setBorder(BorderFactory.createLineBorder(Color.blue));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JLabel preTitle = new JLabel("Shortcuts for:");
		preTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(preTitle);
		JLabel title = new JLabel(beanName, icon, JLabel.CENTER);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(title);

		incomingOn = new JButton("Incoming On");
		incomingOff = new JButton("Incoming Off");
		outgoingOn = new JButton("Outgoing On");
		outgoingOff = new JButton("Outgoing Off");

		incomingOn.addActionListener(this);
		incomingOff.addActionListener(this);
		outgoingOn.addActionListener(this);
		outgoingOff.addActionListener(this);

		Font f = new Font("", Font.PLAIN, 10); // decreasing the size to 10
												// point

		incomingOn.setFont(f);
		incomingOff.setFont(f);
		outgoingOn.setFont(f);
		outgoingOff.setFont(f);

		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

		buttonsPanel.add(incomingOn);
		buttonsPanel.add(incomingOff);
		buttonsPanel.add(outgoingOn);
		buttonsPanel.add(outgoingOff);
		this.add(buttonsPanel);
	}

	/** Listens to the buttons */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof JButton) {
			JButton but = (JButton) e.getSource();
			String butName = but.getText();

			if (butName == incomingOn.getText()) {
				fireActionPerformed(BeanShortcutsGUI.EVENT_INCOMING_ON);
			}

			if (butName == incomingOff.getText()) {
				fireActionPerformed(BeanShortcutsGUI.EVENT_INCOMING_OFF);
			}

			if (butName == outgoingOn.getText()) {
				fireActionPerformed(BeanShortcutsGUI.EVENT_OUTGOING_ON);
			}

			if (butName == outgoingOff.getText()) {
				fireActionPerformed(BeanShortcutsGUI.EVENT_OUTGOING_OFF);
			}
		} // end if
	}

	/**
	 * adds an ActionListener, e.g. CoordinationManagerGUI
	 */
	public void addActionListener(ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener, e.g. CoordinationManagerGUI
	 */
	public void removeActionListener(ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type.
	 * 
	 * @see EventListenerList
	 */
	protected void fireActionPerformed(String command) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							command);
				}

				((ActionListener) listeners[i + 1]).actionPerformed(e);
			}
		} // next listener
	} // end method
}