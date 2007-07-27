/* -------------------------------------------------------------------
 Java source file for the class HistoryEvent
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: HistoryEvent.java,v 1.9 2006/02/27 19:28:41 hardisty Exp $
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

import java.util.EventObject;

import javax.swing.JFrame;

public class HistoryEvent {

	String source;
	String eventName;
	EventObject event;

	public HistoryEvent(String source, String eventName, EventObject event) {

		this.setSource(source);
		this.setEventName(eventName);
		this.setEvent(event);
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
