/* -------------------------------------------------------------------
 Java source file for the class LeaderFollowerGUI
 Original Author: Frank Hardisty 
 $Author: hardisty $
 $Id: LeaderFollowerGUI.java,v 1.9 2006/02/27 19:28:41 hardisty Exp $
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

package edu.psu.geovista.collaboration;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class LeaderFollowerGUI extends JPanel implements ActionListener {

	Preferences prefs;
	ButtonGroup gr = new ButtonGroup();
	JRadioButton radioLeader;
	JRadioButton radioFollower;
	JRadioButton radioNeither;
	int state;
	public static String STATE_PROPTERTY_NAME = "State";
	protected final static Logger logger = Logger.getLogger(LeaderFollowerGUI.class.getName());
	public LeaderFollowerGUI() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		this.setBorder(BorderFactory.createTitledBorder("Choose Role"));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		//this.setPreferredSize(new Dimension(200,300));
		gr = new ButtonGroup();
		radioLeader = new JRadioButton("Leader");
		radioFollower = new JRadioButton("Follower");
		radioNeither = new JRadioButton("Neither");
		gr.add(radioLeader);
		gr.add(radioFollower);
		gr.add(radioNeither);
		this.add(radioLeader);
		this.add(radioFollower);
		this.add(radioNeither);
		radioNeither.setSelected(true);
		radioLeader.addActionListener(this);
		radioFollower.addActionListener(this);
		radioNeither.addActionListener(this);

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.radioNeither) {
			if (this.state != JabberUtils.STATE_NEITHER) {
				int oldState = this.state;
				this.state = JabberUtils.STATE_NEITHER;
				this.firePropertyChange("State", oldState, this.state);
			}
		} else if (e.getSource() == this.radioFollower) {
			if (this.state != JabberUtils.STATE_FOLLOWER) {
				int oldState = this.state;
				this.state = JabberUtils.STATE_FOLLOWER;
				this.firePropertyChange("State", oldState, this.state);
			}
		} else if (e.getSource() == this.radioLeader) {
			if (this.state != JabberUtils.STATE_LEADER) {
				int oldState = this.state;
				this.state = JabberUtils.STATE_LEADER;
				this.firePropertyChange("State", oldState, this.state);
			}
		} else {
			System.out
					.println("LeaderFollowerGUI, recieved event from unknown action source");
		}
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		LeaderFollowerGUI rc = new LeaderFollowerGUI();
		app.getContentPane().add(rc);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setVisible(true);
		StateListener listener = rc.new StateListener();
		rc.addPropertyChangeListener(listener);

	}

	private class StateListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(
					LeaderFollowerGUI.STATE_PROPTERTY_NAME))
				logger.finest("got event, new val = " + evt.getNewValue());


		}

	}

}
