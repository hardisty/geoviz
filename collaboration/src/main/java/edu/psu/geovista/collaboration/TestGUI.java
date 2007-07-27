/*
 * Created on Jul 4, 2006
 *
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, using
 version 2.1 of the License.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package edu.psu.geovista.collaboration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class TestGUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private JScrollPane historyPane = null;
	private JTable historyTable = null;

	/**
	 * This method initializes historyPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getHistoryPane() {
		if (historyPane == null) {
			historyPane = new JScrollPane();
			historyPane.setViewportView(getHistoryTable());
		}
		return historyPane;
	}

	/**
	 * This method initializes historyTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getHistoryTable() {
		if (historyTable == null) {
			historyTable = new JTable();
		}
		return historyTable;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * This is the default constructor
	 */
	public TestGUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridx = 0;
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(getHistoryPane(), gridBagConstraints);
	}

}
