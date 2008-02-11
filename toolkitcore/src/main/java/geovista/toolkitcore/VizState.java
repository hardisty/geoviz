/* -------------------------------------------------------------------
 Java source file for the class ToolkitBean
 Copyright (c), 2005 Frank Hardisty
 $Author: hardisty $
 $Id: ToolkitBean.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
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

package geovista.toolkitcore;

import javax.swing.event.EventListenerList;

import geovista.common.event.AuxiliaryDataSetEvent;
import geovista.common.event.AuxiliaryDataSetListener;
import geovista.common.event.ConditioningEvent;
import geovista.common.event.ConditioningListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.symbolization.event.ColorClassifierEvent;
import geovista.symbolization.event.ColorClassifierListener;

/*
 * A holder for the state of a GeoVizToolkit. Intended to help with marshalling
 * and unmarshalling.
 * 
 * 
 * 
 */

public class VizState implements SelectionListener, IndicationListener,
		AuxiliaryDataSetListener, ColorClassifierListener,
		SpatialExtentListener, ConditioningListener, SubspaceListener {
	ToolkitBeanSet beanSet;
	String dataSource;
	int[] selection;
	int indication;
	int[] subspace;

	AuxiliaryDataSetEvent auxDataEvent;
	ColorClassifierEvent colorClasserEvent;
	SpatialExtentEvent spatialExtentEvent;
	ConditioningEvent conditioningEvent;
	EventListenerList listenerList;
	boolean useProj;

	public VizState() {
		listenerList = new EventListenerList();
	}

	public void selectionChanged(SelectionEvent e) {
		selection = e.getSelection();

	}

	public void indicationChanged(IndicationEvent e) {
		indication = e.getIndication();

	}

	public void dataSetAdded(AuxiliaryDataSetEvent e) {
		auxDataEvent = e;

	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		colorClasserEvent = e;

	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		spatialExtentEvent = e;

	}

	public void conditioningChanged(ConditioningEvent e) {
		conditioningEvent = e;

	}

	/**
	 * adds an ConditioningListener.
	 * 
	 * @param l
	 *            the l
	 */
	public void addConditioningListener(ConditioningListener l) {
		listenerList.add(ConditioningListener.class, l);
	}

	/**
	 * removes an ConditioningListener from the component.
	 * 
	 * @param l
	 *            the l
	 */
	public void removeConditioningListener(ConditioningListener l) {
		listenerList.remove(ConditioningListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param newConditioning
	 *            the new conditioning
	 * 
	 * @see EventListenerList
	 */
	public void fireConditioningChanged() {
		if (conditioningEvent == null) {
			return;
		}
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ConditioningEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ConditioningListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new ConditioningEvent(this, conditioningEvent
							.getConditioning());
				}

				((ConditioningListener) listeners[i + 1])
						.conditioningChanged(e);
			}
		}

		// next i
	}

	/**
	 * adds an SelectionListener.
	 * 
	 * @param l
	 *            the l
	 */
	public void addSelectionListener(SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}

	/**
	 * removes an SelectionListener from the component.
	 * 
	 * @param l
	 *            the l
	 */
	public void removeSelectionListener(SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param newSelection
	 *            the new conditioning
	 * 
	 * @see EventListenerList
	 */
	public void fireSelectionChanged() {
		if (selection == null) {
			return;
		}
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, selection);
				}

				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		}

		// next i
	}

	/**
	 * adds an SubspaceListener.
	 * 
	 * @param l
	 *            the l
	 */
	public void addSubspaceListener(SubspaceListener l) {
		listenerList.add(SubspaceListener.class, l);
	}

	/**
	 * removes an SubspaceListener from the component.
	 * 
	 * @param l
	 *            the l
	 */
	public void removeSubspaceListener(SubspaceListener l) {
		listenerList.remove(SubspaceListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @param newSubspace
	 *            the new conditioning
	 * 
	 * @see EventListenerList
	 */
	public void fireSubspaceChanged() {
		if (subspace == null) {
			return;
		}
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

		// next i
	}

	public AuxiliaryDataSetEvent getAuxDataEvent() {
		return auxDataEvent;
	}

	public ColorClassifierEvent getColorClasserEvent() {
		return colorClasserEvent;
	}

	public SpatialExtentEvent getSpatialExtentEvent() {
		return spatialExtentEvent;
	}

	public ConditioningEvent getConditioningEvent() {
		return conditioningEvent;
	}

	public ToolkitBeanSet getBeanSet() {
		return beanSet;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setBeanSet(ToolkitBeanSet beanSet) {
		this.beanSet = beanSet;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void subspaceChanged(SubspaceEvent e) {
		subspace = e.getSubspace();

	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, selection);
	}

}
