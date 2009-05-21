/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.geoviz.star;

import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;

/**
 * Paints an array of SelectionStarPlot. Responds to and broadcasts
 * DataSetChanged, IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * 
 */
public class SelectionStarPlot extends StarPlot implements SelectionListener {

	int[] savedSelection;

	/**
	 * 
	 */

	public SelectionStarPlot() {
		super();

	}

	public void selectionChanged(SelectionEvent e) {
		int[] selection = e.getSelection();
		starCan.setObsList(selection);
		savedSelection = e.getSelection();
	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, savedSelection);
	}

}
