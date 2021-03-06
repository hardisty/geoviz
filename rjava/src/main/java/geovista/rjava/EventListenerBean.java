/* -------------------------------------------------------------------
 Java source file for the class EventListenerBean
 Copyright (c), 2004, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: EventListenerBean.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
 $Date: 2005/02/13 03:26:27 $
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

package geovista.rjava;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;

/**
 * This class listens for events, especially coordinated events, and makes the
 * results available for query by entities that cannot be themselves
 * coordinated. An example is an instance of R polling an instance of this class
 * for the current selection etc.
 */
public class EventListenerBean implements SelectionListener {
	private transient int[] selection;
	private transient boolean selectionHasChanged;

	public EventListenerBean() {
		super();
		selectionHasChanged = false;
	}

	public void selectionChanged(SelectionEvent e) {
		selection = e.getSelection();
		selectionHasChanged = true;
	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, selection);
	}

	public boolean getSelectionHasChanged() {
		return selectionHasChanged;
	}

	public void setSelectionHasChanged(boolean selectionHasChanged) {
		this.selectionHasChanged = selectionHasChanged;
	}

	public int[] getSelection() {
		selectionHasChanged = false;
		return selection;

	}

	public void setSelection(int[] selection) {
		this.selection = selection;
	}

}
