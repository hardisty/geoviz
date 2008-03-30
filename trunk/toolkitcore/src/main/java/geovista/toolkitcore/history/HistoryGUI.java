/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.toolkitcore.history;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;

public class HistoryGUI extends JPanel implements ActionListener,
		ChangeListener, ListSelectionListener, SpatialExtentListener,
		SelectionListener, SubspaceListener {
	protected final static Logger logger = Logger.getLogger(HistoryGUI.class
			.getName());
	Preferences prefs;
	ArrayList<HistoryEvent> events;
	DefaultListModel listModel;
	JList eventHistoryList;
	JScrollPane eventScrollPane;

	public HistoryGUI() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new FlowLayout());
		listModel = new DefaultListModel();
		eventHistoryList = new JList(listModel);
		JPanel eventPanel = new JPanel();
		eventPanel.setLayout(new BorderLayout());
		events = new ArrayList<HistoryEvent>();
		JLabel eventLabel = new JLabel("Event Type");
		eventPanel.add(eventLabel, BorderLayout.NORTH);
		eventScrollPane = new JScrollPane(eventHistoryList);
		eventPanel.add(eventScrollPane, BorderLayout.CENTER);
		this.add(eventPanel);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
	 */
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
	}

	public void addEventToStack(HistoryEvent e) {
		logger.finest("Adding event, event source = " + e.getSource());
		listModel.addElement(e.getEventName());
		eventHistoryList.ensureIndexIsVisible(listModel.size() - 1);
	}

	SpatialExtentEvent savedEvent;

	public SpatialExtentEvent getSpatialExtentEvent() {
		return savedEvent;
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		savedEvent = e;
	}

	public void selectionChanged(SelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void subspaceChanged(SubspaceEvent arg0) {
		// TODO Auto-generated method stub

	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		HistoryGUI rc = new HistoryGUI();
		app.getContentPane().add(rc);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setVisible(true);
		Rectangle2D.Float rect = new Rectangle2D.Float();
		SpatialExtentEvent e = new SpatialExtentEvent(app, rect);
		HistoryEvent e1 = new HistoryEvent("HyunJin", "spatial event", e);
		HistoryEvent e2 = new HistoryEvent("HyunJin", "selection event", e);
		rc.addEventToStack(e1);
		for (int i = 0; i < 30; i++) {
			rc.addEventToStack(e2);
		}
		rc.addEventToStack(e1);

	}

	public SelectionEvent getSelectionEvent() {
		// TODO Auto-generated method stub
		return null;
	}

}
