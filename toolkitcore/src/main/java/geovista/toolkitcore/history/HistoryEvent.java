/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.toolkitcore.history;

import java.util.EventObject;

import javax.swing.JFrame;

public class HistoryEvent {

	String source;
	String eventName;
	EventObject event;

	public HistoryEvent(String source, String eventName, EventObject event) {

		setSource(source);
		setEventName(eventName);
		setEvent(event);
	}

	static public void main(String args[]) {
		JFrame app = new JFrame();

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.pack();
		app.setVisible(true);
	}

	public EventObject getEvent() {
		return event;
	}

	public String getEventName() {
		return eventName;
	}

	public String getSource() {
		return source;
	}

	public void setEvent(EventObject event) {
		this.event = event;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
