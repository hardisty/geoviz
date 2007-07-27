/* -------------------------------------------------------------------
 Java source file for the class ChatPanel
 Original Authors: Linna Li and Frank hardisty
 $Author: hardisty $
 $Id: ChatPanel.java,v 1.5 2006/03/03 18:08:20 hardisty Exp $
 $Date: 2006/03/03 18:08:20 $
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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatPanel extends JPanel implements KeyListener, ActionListener,
		MessageReceiver {

	JButton sendMessageButton;
	JTextPane conversationPane;
	JTextField inputArea;

	private String userName;
	// ChatPanel otherChat;
	MessageReceiver msgReciever;
	Color textColor;
	private static Color DEFAULT_TEXT_COLOR = Color.red;
	Color altTextColor;
	private static Color DEFAULT_ALT_TEXT_COLOR = Color.blue;
	StyledDocument doc;
	JScrollPane sPanel;

	
	protected final static Logger logger = Logger.getLogger(ChatPanel.class.getName());
	
	public ChatPanel() {
		this.altTextColor = ChatPanel.DEFAULT_ALT_TEXT_COLOR;
		this.textColor = ChatPanel.DEFAULT_TEXT_COLOR;
		this.setBorder(new LineBorder(Color.black));
		// JScrollPane convPanel = new JScrollPane();

		JPanel convPanel = new JPanel(new BorderLayout());
		JLabel conv = new JLabel("Conversation:");

		convPanel.add(conv, BorderLayout.NORTH);
		conversationPane = new JTextPane();
		conversationPane.setEditable(false);
		conversationPane.setPreferredSize(new Dimension(400, 200));
		sPanel = new JScrollPane();
		sPanel.getViewport().setView(conversationPane);
		// sPanel.add(conversationPane);
		convPanel.add(sPanel, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		this.add(convPanel, BorderLayout.CENTER);

		JPanel sendPanel = new JPanel();
		inputArea = new JTextField();
		inputArea.setFont(new Font("Ariel", Font.PLAIN, 20));
		inputArea.setPreferredSize(new Dimension(200, 30));
		inputArea.addKeyListener(this);
		sendMessageButton = new JButton("Send \n Message");
		sendPanel.add(inputArea);
		sendPanel.add(sendMessageButton);
		this.add(sendPanel, BorderLayout.SOUTH);
		sendMessageButton.addActionListener(this);
	}

	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == 10) { // this is the key code for enter
			String message = this.userName + ": " + this.inputArea.getText();
			appendMessage(message, this.userName);
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("ChatPanel, sending message to receiver: "
						+ message);
			}
			this.msgReciever.receiveMessage(this.userName, this.inputArea
					.getText());
			this.inputArea.setText("");
			this.inputArea.requestFocusInWindow();
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.sendMessageButton) {
			String message = this.userName + ": " + this.inputArea.getText();
			appendMessage(message, this.userName);
			if (RemoteCollaboration.logger.isLoggable(Level.FINEST)) {
				logger.finest("ChatPanel, sending message to receiver: "
						+ message);
			}
			this.msgReciever.receiveMessage(this.userName, this.inputArea
					.getText());

			this.inputArea.setText("");
			this.inputArea.requestFocusInWindow();
		}

	}

	public void receiveMessage(String sender, String message) {
		String newMessage = sender + ": " + message;
		appendMessage(newMessage, sender);
	}

	private void appendMessage(String message, String sender) {

		try {
			SimpleAttributeSet set = new SimpleAttributeSet();
			if (sender.equalsIgnoreCase(this.userName)) {
				StyleConstants.setForeground(set, this.textColor);
			} else {
				StyleConstants.setForeground(set, this.altTextColor);
			}
			StyleConstants.setFontSize(set, 22);
			conversationPane.setCharacterAttributes(set, true);

			doc = conversationPane.getStyledDocument();
			doc.insertString(doc.getLength(), message + "\n", set);
			// Dimension d = sPanel.getViewport().getSize();
			// sPanel.getViewport().setViewPosition(new Point(0,200-d.height));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		// conversationPane.setText(newConv);
	}

	public void setUserName(String name) {
		this.userName = name;
		logger.finest("ChatPanel set username = " + name);

	}

	public void setTextColor(Color textcolor) {
		this.textColor = textcolor;
	}

	public void setMsgReciever(MessageReceiver msgReciever) {
		this.msgReciever = msgReciever;
	}

	public Color getTextColor() {
		return textColor;
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());
		ChatPanel cp = new ChatPanel();
		cp.setPreferredSize(new Dimension(450, 400));
		cp.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(cp);
		ChatPanel cp2 = new ChatPanel();
		// cp2.setTextColor(Color.blue);
		cp2.setPreferredSize(new Dimension(450, 400));
		cp2.setBorder(new LineBorder(Color.black));
		app.getContentPane().add(cp2);
		cp.setMsgReciever(cp2);
		cp2.setMsgReciever(cp);
		cp.setUserName("HyangJa");
		cp2.setUserName("Frank");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.pack();
		app.setVisible(true);
	}

}
