package geovista.touchgraph.mst;

import java.util.ResourceBundle;

/**
 * Title: Minimum Spanning Tree Edge class Description: Used for the minimum
 * spanning tree assignment Copyright: Copyright (c) 2002 Company:
 * 
 * @author Markus Svensson
 * 
 */

public class MSTEdge {
	private int start;

	private int end;

	private double weight;

	/**
	 * Default constructor, init weight to +infinity
	 */
	public MSTEdge() {
		this.weight = Integer.MAX_VALUE;
	}

	/**
	 * Constructor
	 * 
	 * @param The
	 *            start vertex
	 * @param The
	 *            end vertex
	 * @param The
	 *            weight
	 */
	public MSTEdge(int start, int end, double weight) {
		this.start = start;
		this.end = end;
		this.weight = weight;
	}

	/**
	 * Get the weight of the edge
	 * 
	 * @return The weight
	 */
	public double getWeight() {
		return this.weight;
	}

	/**
	 * Get the start vertex
	 * 
	 * @return The start vertex
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * Get the end vertex
	 * 
	 * @return The end vretex
	 */
	public int getEnd() {
		return this.end;
	}

	/**
	 * Print the edge
	 * 
	 * @param The
	 *            language to use for output
	 * @return String representation of the edge
	 */
	public String toString(ResourceBundle res) {
		String returnString = (res.getString("Start") + this.start
				+ res.getString("End") + this.end + res.getString("Weight") + this.weight);
		return returnString;

	}
}