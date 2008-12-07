/* -------------------------------------------------------------------
 Java source file for the class ToolkitBeanSet
 Copyright (c), 2005 Frank Hardisty
 $Author: hardisty $
 $Id: ToolkitBeanSet.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;

/*
 * This class manages a set (no duplicate elements) of ToolkitBean objects. It
 * is intended to ease access to them. This implmentation is backed by a
 * HashSet.
 * 
 * 
 * 
 */

public class ToolkitBeanSet {
	final transient static Logger logger = Logger
			.getLogger(ToolkitBeanSet.class.getName());

	private HashSet<ToolkitBean> beanSet;

	public ToolkitBeanSet() {
		beanSet = new HashSet();
	}

	/*
	 * Adds the specified element to this set if it is not already present.
	 */
	public boolean add(ToolkitBean b) {
		return beanSet.add(b); // the HashSet should complain for us if a
		// duplicate is added
	}

	/*
	 * Removes all of the elements from this set.
	 */
	public void clear() {
		beanSet.clear();
	}

	/*
	 * Returns a shallow copy of this HashSet instance: the elements themselves
	 * are not cloned.
	 */
	@Override
	public Object clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			logger.throwing(ToolkitBeanSet.class.getName(), "clone()", e);
		}
		ToolkitBeanSet beanSet = new ToolkitBeanSet();
		Iterator it = this.beanSet.iterator();
		while (it.hasNext()) {
			beanSet.add((ToolkitBean) it.next());
		}

		return new ToolkitBeanSet();
	}

	/*
	 *           Returns true if and only if this set contains the specified
	 * element. 
	 */
	public boolean contains(ToolkitBean b) {
		return beanSet.contains(b);
	}

	/*
	 *           Returns true if and only if this set contains the specified
	 * element. 
	 */
	public boolean contains(JMenuItem item) {
		Iterator it = beanSet.iterator();
		while (it.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) it.next();
			JMenuItem beanItem = tBean.getRemoveMenuItem();
			if (item.equals(beanItem)) {
				return true;
			}

		}

		return false;
	}

	/*
	 *           Returns true if and only if this set contains the specified
	 * element. 
	 */
	public boolean contains(JInternalFrame iFrame) {
		Iterator it = beanSet.iterator();
		while (it.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) it.next();
			JInternalFrame internalFrame = tBean.getInternalFrame();
			if (iFrame.equals(internalFrame)) {
				return true;
			}

		}

		return false;
	}

	/*
	 *           Returns the ToolkitBean associated with this JMenuItem, else
	 * returns null; 
	 */
	public ToolkitBean getToolkitBean(JMenuItem item) {
		Iterator it = beanSet.iterator();
		while (it.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) it.next();
			JMenuItem beanItem = tBean.getRemoveMenuItem();
			if (item.equals(beanItem)) {
				return tBean;
			}
		}
		return null;
	}

	/*
	 *           Returns the ToolkitBean associated with this JInternalFrame,
	 * else returns null; 
	 */

	public ToolkitBean getToolkitBean(JInternalFrame iFrame) {
		if (iFrame == null) {
			return null;
		}
		Iterator it = beanSet.iterator();
		while (it.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) it.next();
			JInternalFrame internalFrame = tBean.getInternalFrame();
			if (iFrame.equals(internalFrame)) {
				return tBean;
			}
		}

		return null;
	}

	/*
	 *           Returns the ToolkitBean associated with the given unique name,
	 * else returns null; 
	 */

	public ToolkitBean getToolkitBean(String uniqueName) {

		Iterator it = beanSet.iterator();
		while (it.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) it.next();
			String uName = tBean.getUniqueName();

			if (uniqueName.equals(uName)) {
				return tBean;
			}
		}

		return null;
	}

	/*
	 *           Returns true if this set contains no elements. 
	 */
	public boolean isEmpty() {
		return beanSet.isEmpty();
	}

	/*
	 *           Returns an iterator over the elements in this set. 
	 */
	public Iterator<ToolkitBean> iterator() {
		return beanSet.iterator();
	}

	/*
	 *           Removes the specified element from this set if it is present. 
	 */
	/*
	 */
	public boolean remove(ToolkitBean b) {
		return beanSet.remove(b);
	}

	/*
	 *  Returns the number of elements in this set (its cardinality). 
	 */
	public int size() {
		return beanSet.size();
	}

	public HashSet<ToolkitBean> getBeanSet() {
		return beanSet;
	}

	public void setBeanSet(HashSet beanSet) {
		this.beanSet = beanSet;
	}
}
