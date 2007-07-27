/* -------------------------------------------------------------------
 Java source file for the class ChatPanelTester
 Original Authors: Linna Li and Frank hardisty
 $Author: hardisty $
 $Id: ChatPanelTester.java,v 1.1 2006/02/16 16:54:51 hardisty Exp $
 $Date: 2006/02/16 16:54:51 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package edu.psu.geovista.collaboration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class ChatPanelTester extends JPanel implements ActionListener {

	JButton sendMessageButton;
	JEditorPane conversationPane;
	JTextArea inputArea;
	// XXX hack
	String name = "Linna";

	public ChatPanelTester() {
		this.setBorder(new LineBorder(Color.black));
		JPanel convPanel = new JPanel(new BorderLayout());
		JLabel conv = new JLabel("Conversation:");
		convPanel.add(conv, BorderLayout.NORTH);
		conversationPane = new JEditorPane();
		conversationPane.setEditable(false);
		conversationPane.setPreferredSize(new Dimension(400, 200));
		convPanel.add(conversationPane, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(convPanel, BorderLayout.CENTER);

		JPanel sendPanel = new JPanel();
		inputArea = new JTextArea();
		inputArea.setPreferredSize(new Dimension(200, 30));
		sendMessageButton = new JButton("Send \n Message");
		sendPanel.add(inputArea);
		sendPanel.add(sendMessageButton);
		this.add(sendPanel, BorderLayout.SOUTH);
		sendMessageButton.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.sendMessageButton) {
			String message = this.name + ": " + this.inputArea.getText();
			appendMessage(message);
		}
	}

	public void addMessage(String message) {
		appendMessage(message);
	}

	private void appendMessage(String message) {
		// XXX hack
		String currConv = conversationPane.getText();
		String newConv = currConv + "\n" + message;
		conversationPane.setText(newConv);
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		ChatPanelTester rc = new ChatPanelTester();
		rc.setPreferredSize(new Dimension(450, 400));
		rc.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(rc);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setVisible(true);
	}
}
