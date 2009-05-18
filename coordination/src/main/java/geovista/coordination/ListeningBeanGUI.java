/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.coordination;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

/**
 * This class holds the GUI references to connect or disconnect all of the
 * incoming or outgoing coordinated connections for this bean. The original
 * design calls for a CoordinationManagerGUI to hold a reference to one of these
 * per coordinated bean, and for this class to have one ListeningInterfaceGUI
 * per interface in the listening bean that is matched with an event type in a
 * firing bean.
 * 
 * @see CoordinationManager
 * @author Frank Hardisty
 */
public class ListeningBeanGUI extends JPanel implements ActionListener {
	private transient final ListeningInterfaceGUI[] listenGUI;
	private transient final JPanel listenersGUIPanel;

	/**
   *
  */
	public ListeningBeanGUI(ListeningBean lBean, CoordinationManager cm) {
		setBorder(BorderFactory.createLineBorder(Color.blue));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel preTitle = new JLabel("Listening Bean:");
		preTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(preTitle);

		String beanName = lBean.getBeanName();
		Icon icon = new ImageIcon(lBean.getIcon());
		JLabel title = new JLabel(beanName, icon, JLabel.CENTER);
		title.setAlignmentX(Component.LEFT_ALIGNMENT);
		this.add(title);

		FiringMethod[] firingMethods = cm.getFiringMethods(lBean);

		ArrayList[] lists = ListeningBeanGUI
				.seperateFiringMethods(firingMethods);

		// each list should now contain a different interface
		listenersGUIPanel = new JPanel();
		listenersGUIPanel.setLayout(new BoxLayout(listenersGUIPanel,
				BoxLayout.Y_AXIS));
		listenGUI = new ListeningInterfaceGUI[firingMethods.length];

		for (int i = 0; i < lists.length; i++) {
			FiringMethod[] meths = ListeningBeanGUI.listToFMethod(lists[i]);
			ListeningInterfaceGUI liGUI = new ListeningInterfaceGUI(lBean,
					meths, cm);
			listenGUI[i] = liGUI;
			listenersGUIPanel.add(liGUI);
		}
		// if there are no listening interfaces, skip creating gui
		if (lists.length == 0) {
			removeAll();
		}

		// this.listenGUI[0] = new ListeningInterfaceGUI("Incoming On");
		// this.listenGUI[0].addActionListener(this);
		// Font f = new Font("", Font.PLAIN, 10); //decreasing the size to 10
		// point
		// this.listenGUI[0].setFont(f);
		// listenersGUIPanel.add(listenGUI[0]);
		this.add(listenersGUIPanel);
	}

	public static ArrayList[] seperateFiringMethods(FiringMethod[] meths) {
		Arrays.sort(meths);

		// first we want to know the number of unique interfaces
		int numUnique = 0;

		if (meths.length > 0) {
			numUnique = 1;
		}

		for (int i = 1; i < meths.length; i++) {
			if (meths[i].getListeningInterface() != meths[i - 1]
					.getListeningInterface()) {
				numUnique++;
			}
		}

		ArrayList[] returnLists = new ArrayList[numUnique];
		int whichList = 0;

		if (meths.length > 0) {
			returnLists[0] = new ArrayList();
			returnLists[0].add(meths[0]);
		}

		for (int i = 1; i < meths.length; i++) {

			if (meths[i].getListeningInterface() != meths[i - 1]
					.getListeningInterface()) {
				whichList++;
				returnLists[whichList] = new ArrayList();
			}
			returnLists[whichList].add(meths[i]);
		}

		return returnLists;
	}

	public static FiringMethod[] listToFMethod(ArrayList aList) {
		FiringMethod[] meths = new FiringMethod[aList.size()];

		for (int i = 0; i < meths.length; i++) {
			meths[i] = (FiringMethod) aList.get(i);
		}

		return meths;
	}

	/** Listens to the boxen */
	public void actionPerformed(ActionEvent e) {

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