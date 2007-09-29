package edu.psu.geovista.touchgraph.mst;

import java.util.Vector;

/**
 * Title: PriorityQue Description: A very simple priority que implementation,
 * for use with the minimum spanning tree assignment. Copyright: Copyright (c)
 * 2002 Company: MAH TS
 * 
 * @author Markus Svensson
 * @version 1.1
 */

public class PriorityQue {
	private int capacity;

	private Vector que;

	/**
	 * Construct the que
	 * 
	 * @param The
	 *            number of elements in the initial que
	 */
	public PriorityQue(int numOfElements) {
		this.capacity = numOfElements;
		this.que = new Vector(this.capacity);
	}

	/**
	 * Inserts an edge into the que.
	 * 
	 * @param The
	 *            edge to insert
	 */
	public void insertItem(MSTEdge item) {
		this.que.add(item);
	}

	/**
	 * Get the num of elements in the que
	 * 
	 * @return The num of elements.
	 */
	public int getNumOfElements() {
		return this.capacity;
	}

	/**
	 * Removes the element with the lowest priority from the que.
	 * 
	 * @return The edge with the lowest weight
	 */
	public MSTEdge removeMin() {
		
		MSTEdge temp = new MSTEdge();
		int index = 0;
		for (int i = 0; i < this.que.size(); i++) {
			MSTEdge tmp1 = (MSTEdge) this.que.elementAt(i);
			if (temp.getWeight() > tmp1.getWeight()) {
				temp = tmp1;
				index = i;
			}
		}
		this.que.removeElementAt(index);
		this.capacity = this.que.size();
		return temp;
	}

}