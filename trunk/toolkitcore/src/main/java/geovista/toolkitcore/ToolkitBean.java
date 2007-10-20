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

import geovista.coordination.CoordinationUtils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.event.EventListenerList;

/*
 * This is a set of objects that each bean coordinated in a GeoVizToolkit is
 * expected to have
 * 
 * 
 * 
 */

public class ToolkitBean implements ComponentListener {

	private Object originalBean;
	private JMenuItem removeMenuItem;
	JInternalFrame internalFrame;
	EventListenerList listenerList;
	String uniqueName;

	public ToolkitBean(){
		
	}
	
	public ToolkitBean(Object originalBean, String uniqueName) {
		this.listenerList = new EventListenerList();
		this.setOriginalBean(originalBean, uniqueName);

		this.uniqueName = uniqueName;

	}

	public JInternalFrame getInternalFrame() {
		return internalFrame;
	}

	public Object getOriginalBean() {
		return originalBean;
	}

	public JMenuItem getRemoveMenuItem() {
		return removeMenuItem;
	}

	private JInternalFrame makeInternalFrame(Object newInstance,
			String uniqueName, Icon ic) {
		JInternalFrame newFrame = new JInternalFrame(uniqueName, true, true,
				true, true);
		newFrame.addComponentListener(this);

		newFrame.setVisible(true);

		newFrame.setFrameIcon(ic);
		Component newComp = null;
		if (newInstance instanceof Component) {
			newComp = (Component) newInstance;
		} else {
			newComp = new JButton(uniqueName);
		}
		Dimension d = newComp.getPreferredSize();
		Dimension newD = new Dimension(d.width + 5, d.height + 10);
		newFrame.setSize(newD);
		newFrame.getContentPane().add(newComp);
		newFrame.revalidate();
		return newFrame;
	}

	public void setOriginalBean(Object originalBean, String uniqueName) {
		Image im = CoordinationUtils.findSmallIcon(originalBean);
		Icon ic = new ImageIcon(im);
		this.originalBean = originalBean;
		this.internalFrame = this.makeInternalFrame(originalBean, uniqueName,
				ic);
		this.removeMenuItem = new JMenuItem(uniqueName, ic);
		this.uniqueName = uniqueName;
	}

	public String getUniqueName() {
		return this.uniqueName;
	}

	// start component event handling
	// note: this class only listens to its JInternalFrame
	// we pass these right on up to any interested listener
	public void componentHidden(ComponentEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ComponentListener.class) {

				((ComponentListener) listeners[i + 1]).componentHidden(e);
			}
		}

	}

	public void componentMoved(ComponentEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ComponentListener.class) {

				((ComponentListener) listeners[i + 1]).componentMoved(e);
			}
		}

	}

	public void componentShown(ComponentEvent e) {
		// Supposed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ComponentListener.class) {

				((ComponentListener) listeners[i + 1]).componentShown(e);
			}
		}

	}

	public void componentResized(ComponentEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ComponentListener.class) {

				((ComponentListener) listeners[i + 1]).componentResized(e);
			}
		}
	}

	public void addComponentListener(ComponentListener l) {
		listenerList.add(ComponentListener.class, l);
	}

	public void removeComponentListener(ComponentListener l) {
		listenerList.remove(ComponentListener.class, l);
	}

	public void setOriginalBean(Object originalBean) {
		this.originalBean = originalBean;
	}

	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}

}
