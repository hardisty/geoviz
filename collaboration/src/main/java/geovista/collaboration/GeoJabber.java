/* -------------------------------------------------------------------
 Java source file for the class GeoJabber
 Original Authors: Frank Hardisty
 $Author: hardisty $
 $Id: GeoJabber.java,v 1.9 2006/02/27 19:28:41 hardisty Exp $
 $Date: 2006/02/27 19:28:41 $
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

package geovista.collaboration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.EventListenerList;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;

public class GeoJabber extends JPanel implements SelectionListener,
		SpatialExtentListener, SubspaceListener, ActionListener,
		MessageReceiver, ConnectionListener, PacketListener {

	JPanel connectPanel;
	ChatPanel chatPanel;
	JPanel sendPanel;
	JTextField nameField;
	JPasswordField passwordField;
	JButton connectButton;

	JPanel buttonPanel;
	JButton sendSelection;
	JButton sendVariables;
	JButton sendSpatialExtent;
	JButton fileTransferButton;
	JButton sendComponentButton;
	EventListenerList listenerList;

	Object remoteListener;

	String userName;
	String password;

	XMPPConnection conn;
	Chat chat;
	RosterEntry friend;

	int[] selection;
	Rectangle2D spatialExtent;
	int[] subspace;

	private final String serverName = "localhost"; // XXX total hack

	static boolean DEBUG = true;
	JButton configButton;

	private int followerState = JabberUtils.STATE_FOLLOWER;

	PacketCollector myCollector;

	final static Logger logger = Logger.getLogger(GeoJabber.class.getName());

	public GeoJabber() {
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			;
			XMPPConnection.DEBUG_ENABLED = true;
		}
		setLayout(new BorderLayout());
		connectPanel = makeConnectPanel();
		this.add(connectPanel, BorderLayout.NORTH);
		buttonPanel = makeButtonPanel();
		this.add(buttonPanel, BorderLayout.SOUTH);
		chatPanel = new ChatPanel();

		chatPanel.setMsgReciever(this);
		this.add(chatPanel, BorderLayout.CENTER);
		listenerList = new EventListenerList();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectButton) {
			turnButtonsOn();
			connectAndLogin();
		} else if (e.getSource() == sendSelection) {
			sendRemoteSelection();
		} else if (e.getSource() == sendSpatialExtent) {
			sendRemoteSpatialExtent();
		} else if (e.getSource() == sendVariables) {
			sendRemoteSubspace();
		} else if (e.getSource() == configButton) {
			GeoJabberPreferences prefsFrame = new GeoJabberPreferences(conn);
			prefsFrame.setVisible(true);
		} else if (e.getSource() == fileTransferButton) {
			File sendFile = new File("C:\\temp\\shapefiles\\east_asia.shp");
			// how to get the fully qualified user name?
			// org.jivesoftware.smack.Roster.getPresences(String user)
			// -> org.jivesoftware.smack.packet.Presence.getFrom()
			String reciever = friend.getUser() + "@satchmo/Smack";
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("Sending file to " + reciever);
			}
			GeoJabber.sendFile(conn, reciever, sendFile);
		} else if (e.getSource() == sendComponentButton) {

		}

		else {
			logger.finest(this.getClass().getName()
					+ " recieved unknown action");
		}

	}

	/**
	 * 
	 */
	private void connectAndLogin() {
		userName = nameField.getText();
		chatPanel.setUserName(userName);
		char[] pass = passwordField.getPassword();
		password = new String(pass);
		pass = null;
		Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
		gvPrefs.put("LastGoodName", userName);
		gvPrefs.put("LastGoodPassword", password);
		conn = JabberUtils.openConnection(serverName);

		boolean loginOK = JabberUtils.login(conn, userName, password);
		// XXX following line can be reactivate when Smack lib gets update to
		// 3.x
		// conn.getRoster().setSubscriptionMode(Roster.SubscriptionMode.accept_all);
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {

			logger.finest(userName + " login OK = " + loginOK);
		}

		conn.addConnectionListener(this);
		if (friend == null) {
			this.findFriend();
		}

		// listen for packets
		PacketTypeFilter filt = new PacketTypeFilter(Message.class);
		myCollector = conn.createPacketCollector(filt);
		conn.addPacketListener(this, filt);
		// listen for files
		File incomingFile = new File("C:\\temp\\new.shp");
		recieveFile(conn, incomingFile);

	}

	private JPanel makeConnectPanel() {
		connectPanel = new JPanel(new FlowLayout());
		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		JLabel nameLabel = new JLabel("Name:");
		connectPanel.add(nameLabel);

		nameField = new JTextField();
		nameField.setPreferredSize(new Dimension(80, 25));
		connectPanel.add(nameField);

		JLabel passwordLabel = new JLabel("Password:");
		connectPanel.add(passwordLabel);
		passwordField = new JPasswordField();
		passwordField.setPreferredSize(new Dimension(40, 25));
		connectPanel.add(passwordField);

		connectPanel.add(connectButton);

		configButton = new JButton("Preferences");
		connectPanel.add(configButton);
		configButton.addActionListener(this);

		connectPanel.setBorder(new LineBorder(Color.black));

		Preferences gvPrefs = Preferences.userNodeForPackage(this.getClass());
		String name = gvPrefs.get("LastGoodName", "");
		nameField.setText(name);

		String password = gvPrefs.get("LastGoodPassword", "");
		passwordField.setText(password);

		return connectPanel;

	}

	private JPanel makeButtonPanel() {
		buttonPanel = new JPanel();
		// this.buttonPanel.setLayout(new
		// BoxLayout(this.buttonPanel,BoxLayout.Y_AXIS));
		sendSelection = new JButton("Send Selection");
		sendVariables = new JButton("Send Variables");
		sendSpatialExtent = new JButton("Send Spatial Extent");
		fileTransferButton = new JButton("Transfer File");
		buttonPanel.add(sendSelection);
		buttonPanel.add(sendVariables);
		// buttonPanel.add(sendSpatialExtent);
		buttonPanel.add(fileTransferButton);

		sendSelection.addActionListener(this);
		sendVariables.addActionListener(this);
		sendSpatialExtent.addActionListener(this);
		fileTransferButton.addActionListener(this);
		buttonPanel.setBorder(new LineBorder(Color.black));
		sendSelection.setEnabled(false);
		sendVariables.setEnabled(false);
		sendSpatialExtent.setEnabled(false);
		return buttonPanel;

	}

	private void sendRemoteSpatialExtent() {
		if (spatialExtent == null) {
			return;
		}

		DefaultPacketExtension extentExt = JabberUtils
				.makeSpatialExtentExtension(spatialExtent);
		sendExtension(extentExt);

	}

	private void sendRemoteSelection() {
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			logger.finest(userName + " sending Remote Selection");
		}
		// int[] someInts = { 1, 2, 4 };
		// this.selection = someInts;
		if (selection == null) {
			return;
		}

		sendExtension(JabberUtils.makeSelectionExtension(selection));

	}

	private void sendRemoteSubspace() {
		if (subspace == null) {
			return;
		}

		DefaultPacketExtension subExt = JabberUtils
				.makeSubspaceExtension(subspace);
		sendExtension(subExt);

	}

	@SuppressWarnings("unused")
	private String findFriend(Packet pack) {
		String from = pack.getFrom();
		int atPlace = from.indexOf("@");
		String name = from.substring(0, atPlace);
		return name;
		// Roster rost = conn.getRoster();
		// friend = rost.getEntry(pack.getFrom());
	}

	private void findFriend() {
		Roster rost = conn.getRoster();

		// Object[] entries = (Object[]) rost.getEntries().toArray()
		Iterator it = rost.getEntries().iterator();

		for (int i = 0; i < rost.getEntryCount(); i++) {
			RosterEntry entry = (RosterEntry) it.next();
			String friendName = entry.getUser();
			logger.finest(" friend = " + friendName);
			friend = entry;
		}

	}

	private void makeChat() {
		/*
		 * until smack lib gets updated ChatManager chatmanager =
		 * conn.getChatManager(); chat =
		 * chatmanager.createChat(friend.getUser()+"@satchmo", new
		 * MessageListener(){
		 * 
		 * public void processMessage(Chat arg0, Message arg1) { if
		 * (logger.isLoggable(Level.FINEST)){ logger.finest("got a chat message, " +
		 * arg1.getBody()); }
		 * 
		 * }}); if (logger.isLoggable(Level.FINEST)){ logger.finest("I'm " +
		 * this.userName + ", starting new chat with " + chat.getParticipant()); }
		 */
	}

	private void sendExtension(PacketExtension ext) {
		// connect to our pal
		if (friend == null) {
			this.findFriend();
		}

		Message msg = new Message(friend.getUser());
		msg.setBody("test message");
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			logger.finest("msg before = " + msg.toXML());
		}
		msg.addExtension(ext);
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			logger.finest("msg after = " + msg.toXML());
		}

		// conn.sendPacket(msg);

		try {
			if (chat == null) {
				makeChat();
			}
			chat.sendMessage(msg);
		} catch (XMPPException e) {

			e.printStackTrace();
		}
	}

	public void receiveMessage(String Name, String msg) {
		// got this from our local chat window
		// pass it along to remote listeners
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			System.out
					.println("GeoJabber, message entered in local chat panel: "
							+ msg);
		}
		if (msg == null) {
			return;
		}
		if (conn == null) {
			connectAndLogin();
		}
		if (friend == null) {
			this.findFriend();
		}
		if (chat == null) {
			makeChat();
		}
		try {
			Message newMessage = new Message(friend.getName());
			newMessage.setBody(msg);
			// message.setProperty("favoriteColor", "red");
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("About to send a message from " + userName
						+ " to " + newMessage.getTo());
				logger.finest(newMessage.toXML());
			}
			// conn.sendPacket(newMessage);
			chat.sendMessage(newMessage);
			// chat.sendMessage(msg);

		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// efaultPacketExtension ext = JabberUtils.makeMessageExtension(msg);
		// this.sendExtension(ext);

	}

	SpatialExtentEvent savedEvent;

	public SpatialExtentEvent getSpatialExtentEvent() {
		return savedEvent;
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {

		spatialExtent = e.getSpatialExtent();
		savedEvent = e;
	}

	public void subspaceChanged(SubspaceEvent e) {

		subspace = e.getSubspace();

	}

	public void selectionChanged(SelectionEvent e) {

		selection = e.getSelection();
		// we got this event from some source on the local jvm
		// so pass it along to remote listeners via our XMPP connection
		// this.fireSelectionChanged(this.selection);

	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, selection);
	}

	public void remoteMessageReceived(String name, String message) {
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			logger
					.finest("GeoJabber.remoteMessageReceived, got remote message");
		}
		// send it to our local chat panel
		chatPanel.receiveMessage(name, message);
	}

	public void remoteSelectionChanged(String source, int[] selection) {
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			logger.finest("got remote message from " + source);
		}
		// we got this selection from somewhere (probably another JVM)
		// send it along to registered listeners in this JVM
		fireSelectionChanged(selection);
	}

	public void remoteSpatialExtentChanged(String source, Rectangle2D extent) {

		fireSpatialExtentChanged(extent);

	}

	public void remoteSubspaceChanged(String source, int[] subspace) {

	}

	private void turnButtonsOn() {
		sendSelection.setEnabled(true);
		sendVariables.setEnabled(true);
		sendSpatialExtent.setEnabled(true);

	}

	/**
	 * adds an SpatialExtentListener
	 */
	public void addSpatialExtentListener(SpatialExtentListener l) {
		listenerList.add(SpatialExtentListener.class, l);
	}

	/**
	 * removes an SpatialExtentListener from the component
	 */
	public void removeSpatialExtentListener(SpatialExtentListener l) {
		listenerList.remove(SpatialExtentListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	private void fireSpatialExtentChanged(Rectangle2D newSpatialExtent) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SpatialExtentEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SpatialExtentListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SpatialExtentEvent(this, newSpatialExtent);
				}

				((SpatialExtentListener) listeners[i + 1])
						.spatialExtentChanged(e);
			}
		} // next i
	}

	public void addSelectionListener(SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}

	public void removeSelectionListener(SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireSelectionChanged(int[] newSelection) {

		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, newSelection);
				}
				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		} // next i

	}

	// Add Subspace Changed Listener
	public void addSubspaceListener(SubspaceListener l) {
		listenerList.add(SubspaceListener.class, l);
	}

	// Removes that Listener
	public void removeSubspaceListener(SubspaceListener l) {
		listenerList.remove(SubspaceListener.class, l);
	}

	// Fires Subspace Changed
	public void fireSubspaceChanged(int[] selection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SubspaceEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SubspaceListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SubspaceEvent(this, selection);
				}

				((SubspaceListener) listeners[i + 1]).subspaceChanged(e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
	 */
	public void connectionClosed() {
		// TODO Auto-generated method stub
		logger.finest("Connection Closed");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java.lang.Exception)
	 */
	public void connectionClosedOnError(Exception arg0) {
		// TODO Auto-generated method stub
		logger.finest("Connection Closed on Error");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 * 
	 * External event, send along to listeners in the local JVM
	 */
	public void processPacket(Packet pack) {
		Message msg = null;
		if (GeoJabber.logger.isLoggable(Level.FINEST)) {
			logger.finest("I'm " + nameField.getText());
			logger.finest("got packet, xml = " + pack.toXML());
			logger.finest("packet from = " + pack.getFrom());
			logger.finest("packet to = " + pack.getTo());

			msg = (Message) pack;
			logger.finest("Body = " + msg.getBody());
		}
		if (followerState == JabberUtils.STATE_LEADER
				|| followerState == JabberUtils.STATE_NEITHER) {
			if (GeoJabber.logger.isLoggable(Level.FINEST)) {
				logger.finest("Ignoing packet because I'm not a follower");
			}
			return;
		}

		chatPanel.receiveMessage(friend.getName(), msg.getBody());
		Iterator extensions = (Iterator) pack.getExtensions();
		logger.finest("extensions.hasNext() = " + extensions.hasNext());
		// XXX hack - what if someone else send us a message?
		// String sender = this.findFriend(pack);
		// if (sender == null) {
		// sender = "";
		// }

		int counter = 0;

		while (extensions.hasNext()) {
			Object obj = extensions.next();
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("extension " + counter + " = " + obj);
			}
			if (obj instanceof DefaultPacketExtension) {
				DefaultPacketExtension ext = (DefaultPacketExtension) obj;
				String elementName = ext.getElementName();
				if (elementName.equals(JabberUtils.SELECTION_ELEMENT_NAME)) {
					selection = JabberUtils.getSelection(ext);
					fireSelectionChanged(selection);// send along to
					// local listeners
				} else if (elementName
						.equals(JabberUtils.SPATIAL_EXTENT_ELEMENT_NAME)) {
					spatialExtent = JabberUtils.getSpatialExtent(ext);
					fireSpatialExtentChanged(spatialExtent);
				} else if (elementName
						.equals(JabberUtils.SUBSPACE_ELEMENT_NAME)) {
					subspace = JabberUtils.getSubspace(ext);
					fireSubspaceChanged(subspace);
				} else if (elementName.equals(JabberUtils.MESSAGE_ELEMENT_NAME)) {
					String message = JabberUtils.getMessage(ext);
					chatPanel.setUserName(userName);// xxx why do we
					// need to
					// clobber this?
					chatPanel.receiveMessage(friend.getName(), message);
				}

				String xml = ext.toXML();
				logger.finest("extension xml = " + xml);

				logger.finest("selection = " + selection);
			}

		}

	}

	public void setFollowerState(int followerState) {
		this.followerState = followerState;

	}

	public int getFollowerState() {
		return followerState;
	}

	static public void main(String args[]) {

		// try {
		// conn.connect();
		// //conn.loginAnonymously();
		// conn.login("Frank", "password");
		// String[] groups = {"friends"};
		// conn.getRoster().createEntry("HyangJa", "HyangJa", groups);
		// conn.disconnect();
		// conn.connect();
		//
		// conn.login("HyangJa", "password");
		// conn.getRoster().createEntry("Frank", "Frank", groups);
		// conn.disconnect();
		//	
		//			
		// //conn.getAccountManager().createAccount("Frank", "password");
		// //conn.getAccountManager().createAccount("HyangJa", "password");
		// } catch (XMPPException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		JFrame app = new JFrame();
		app.getContentPane().setLayout(new FlowLayout());

		GeoJabber rc2 = new GeoJabber();
		rc2.setPreferredSize(new Dimension(450, 400));
		rc2.setBorder(new LineBorder(Color.black));
		rc2.nameField.setText("HyangJa");
		app.getContentPane().add(rc2);

		GeoJabber rc = new GeoJabber();
		rc.setPreferredSize(new Dimension(450, 400));
		rc.setBorder(new LineBorder(Color.black));
		rc.nameField.setText("Frank");
		app.getContentPane().add(rc);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.pack();
		app.setVisible(true);

	}

	public void reconnectingIn(int arg0) {
		// TODO Auto-generated method stub

	}

	public void reconnectionFailed(Exception arg0) {
		// TODO Auto-generated method stub

	}

	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub

	}

	public static boolean sendFile(XMPPConnection conn, String reciever,
			File file) {
		// Create the file transfer manager
		FileTransferManager manager = new FileTransferManager(conn);

		// Create the outgoing file transfer
		OutgoingFileTransfer transfer = manager
				.createOutgoingFileTransfer(reciever);

		// Send the file
		try {
			transfer.sendFile(file, "You won't believe this!");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean recieveFile(XMPPConnection conn, File file) {
		// Create the file transfer manager
		final FileTransferManager manager = new FileTransferManager(conn);

		// Create the listener
		FileAcceptor fiAcc = new FileAcceptor();
		fiAcc.fi = file;
		manager.addFileTransferListener(fiAcc);
		return true;
	}

	private class FileAcceptor implements FileTransferListener {
		File fi;

		public void fileTransferRequest(FileTransferRequest request) {
			// Accept it
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest("getting a file.... ");
			}
			IncomingFileTransfer transfer = request.accept();
			try {
				transfer.recieveFile(fi);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}

		}

	}

}
