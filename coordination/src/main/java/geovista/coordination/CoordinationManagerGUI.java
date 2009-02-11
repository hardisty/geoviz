/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.coordination;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * this class maintains the GUI it examines beans on the way in and out to make
 * changes to itself CoordinationManager maintains the state of the beans and
 * their connections it gets messages from the GUI if the user requests changes
 * to the connections
 */

public class CoordinationManagerGUI extends JScrollPane implements
		ActionListener {

	final static Logger logger = Logger.getLogger(CoordinationManager.class
			.getName());
	private transient CoordinationManager cm;
	transient private JPanel boxHolder;
	transient private JPanel shortcutsHolder;
	transient private JPanel allHolder;

	// private
	public CoordinationManagerGUI() {
		super();

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createShortCuts() {
		// this.shortcutsHolder = new JPanel();

		FiringBean[] fBeans = null;

		fBeans = cm.getFiringBeans().toArray(fBeans);

		FiringBean newBean = fBeans[fBeans.length - 1];
		Icon ic = newBean.getIcon();
		BeanShortcutsGUI bGUI = new BeanShortcutsGUI(newBean.getBeanName(), ic);
		shortcutsHolder.add(bGUI);
		bGUI.addActionListener(this);
		revalidate();

	}

	public void createBoxes() {
		boxHolder.removeAll();

		FiringBean[] fBeans = null;

		fBeans = cm.getFiringBeans().toArray(fBeans);
		for (FiringBean bean : fBeans) {
			Object whichBean = bean.getOriginalBean();
			ListeningBean lBean = new ListeningBean();
			lBean.setOriginalBean(whichBean);
			ListeningBeanGUI lGUI = new ListeningBeanGUI(lBean, cm);
			boxHolder.add(lGUI);
		}

	}

	public void validateBoxes() {
	}

	public void addBean(Object beanIn) {
		cm.addBean(beanIn);

		// XXXthese are being commented out because gui creation is too
		// expensive
		// TODO: replace the functionality represented by the three lines of
		// code below with something more efficient.

		// createShortCuts();
		// createBoxes();
		// validateBoxes();
	}

	public void removeBean(Object oldBean) {
		cm.removeBean(oldBean);
	}

	private void jbInit() throws Exception {
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setViewportBorder(BorderFactory.createLineBorder(Color.black));
		allHolder = new JPanel();
		allHolder.setLayout(new BoxLayout(allHolder, BoxLayout.Y_AXIS));

		cm = new CoordinationManager();
		boxHolder = new JPanel();
		boxHolder.setLayout(new BoxLayout(boxHolder, BoxLayout.Y_AXIS));
		allHolder.add(boxHolder);
		getViewport().add(allHolder);
		setPreferredSize(new Dimension(120, 200));
		setMinimumSize(new Dimension(120, 200));

		shortcutsHolder = new JPanel();
		shortcutsHolder.setLayout(new BoxLayout(shortcutsHolder,
				BoxLayout.Y_AXIS));
		shortcutsHolder.add(new JLabel("Shortcuts Holder"));
		allHolder.add(shortcutsHolder);
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd == BeanShortcutsGUI.EVENT_INCOMING_ON) {
			logger.info(BeanShortcutsGUI.EVENT_INCOMING_ON);
		} else if (cmd == BeanShortcutsGUI.EVENT_INCOMING_OFF) {
			logger.info(BeanShortcutsGUI.EVENT_INCOMING_OFF);
		} else if (cmd == BeanShortcutsGUI.EVENT_OUTGOING_ON) {
			logger.info(BeanShortcutsGUI.EVENT_OUTGOING_ON);
		} else if (cmd == BeanShortcutsGUI.EVENT_OUTGOING_OFF) {
			logger.info(BeanShortcutsGUI.EVENT_OUTGOING_OFF);
		}
	}

	public CoordinationManager getCm() {
		return cm;
	}
}
