/* -------------------------------------------------------------------
 Java source file for the class HistoryGUI
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: HistoryGUI.java,v 1.9 2006/02/27 19:28:41 hardisty Exp $
 $Date: 2006/02/27 19:28:41 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation under
 version 2.1 of the License.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.collaboration;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

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

public class HistoryGUI
    extends JPanel implements ActionListener,
    ChangeListener, ListSelectionListener, SpatialExtentListener,
    SelectionListener, SubspaceListener {
	protected final static Logger logger = Logger.getLogger(HistoryGUI.class.getName());
  Preferences prefs;

  Vector sourceHistoryStack;
  JList sourceHistoryList;
  JScrollPane sourceScrollPane;

  Vector eventHistoryStack;
  JList eventHistoryList;
  JScrollPane eventScrollPane;

  public HistoryGUI() {
    init();
  }

  /**
   *
   */
  private void init() {
    this.setLayout(new FlowLayout());

    this.sourceHistoryList = new JList();
    JPanel sourcePanel = new JPanel();
    sourcePanel.setLayout(new BorderLayout());
    this.sourceHistoryStack = new Vector();
    JLabel sourceLabel = new JLabel("Event Source");
    sourcePanel.add(sourceLabel, BorderLayout.NORTH);
    sourceScrollPane = new JScrollPane(sourceHistoryList);
    sourcePanel.add(sourceScrollPane, BorderLayout.CENTER);
    this.add(sourcePanel);

    this.eventHistoryList = new JList();
    JPanel eventPanel = new JPanel();
    eventPanel.setLayout(new BorderLayout());
    this.eventHistoryStack = new Vector();
    JLabel eventLabel = new JLabel("Event Type");
    eventPanel.add(eventLabel, BorderLayout.NORTH);
    eventScrollPane = new JScrollPane(eventHistoryList);
    eventPanel.add(eventScrollPane, BorderLayout.CENTER);
    this.add(eventPanel);

  }

  /* (non-Javadoc)
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
   */
  public void stateChanged(ChangeEvent e) {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
   */
  public void valueChanged(ListSelectionEvent e) {
    // TODO Auto-generated method stub
  }

  public void addEventToStack(HistoryEvent e) {
    logger.finest("Adding event, event source = " + e.getSource());
    this.sourceHistoryStack.addElement(e.getSource());
    this.sourceHistoryList.setListData(sourceHistoryStack);

    this.eventHistoryStack.addElement(e.getEventName());
    this.sourceHistoryList.setListData(sourceHistoryStack);
  }

  public void spatialExtentChanged(SpatialExtentEvent e) {

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
    HistoryEvent e2 = new HistoryEvent("HyunJin", "spatial event", e);
    rc.addEventToStack(e1);
    rc.addEventToStack(e2);
  }

}
