package edu.psu.geovista.touchgraph.mst;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Title: Minimum Spanning Tree Description: Uses Kruskal's algorithm to
 * calculate a MST from a graph. Copyright: Copyright (c) 2002 Company:
 * 
 * @author Markus Svensson
 * @version 1.2
 */

public class MinimumSpanningTree {
	private File file;

	private int[][] edges;

	private int numOfVertices = 0;

	private Vector mst = null;

	private int[] cluster;

	private int totalWeight;

	private PriorityQue que;

	/**
	 * Constructor
	 * 
	 * @param The
	 *            name of the file containing the graph.
	 */
	public MinimumSpanningTree(String fileName) {
		this.file = new File(fileName);
		this.edges = this.readFromFile(this.file);
		for (int i = 0; i < this.edges.length; i++) {
			if (this.edges[i][0] > this.numOfVertices) {
				this.numOfVertices = this.edges[i][0];
			}
			if (this.edges[i][1] > this.numOfVertices) {
				this.numOfVertices = this.edges[i][1];
			}
		}
		this.mst = new Vector(10, 5);
		this.que = new PriorityQue(this.edges.length);
		this.cluster = new int[this.numOfVertices];
		for (int j = 0; j < this.numOfVertices; j++) {
			this.cluster[j] = j + 1;
		}
	}

	/**
	 * Constructor
	 * 
	 * @param The
	 *            file containing the graph
	 */
	public MinimumSpanningTree(File file) {
		this.file = file;
		this.edges = this.readFromFile(file);
		for (int i = 0; i < this.edges.length; i++) {
			if (this.edges[i][0] > this.numOfVertices) {
				this.numOfVertices = this.edges[i][0];
			}
			if (this.edges[i][1] > this.numOfVertices) {
				this.numOfVertices = this.edges[i][1];
			}
		}
		this.mst = new Vector(10, 5);
		this.que = new PriorityQue(this.edges.length);
		this.cluster = new int[this.numOfVertices];
		for (int j = 0; j < this.numOfVertices; j++) {
			this.cluster[j] = j + 1;
		}
	}

	/**
	 * Calculate the MST using Kruskal's algorithm.
	 * 
	 * @return The MST as a two-dimensional array.
	 */
	public void kruskal() {
		for (int i = 0; i < this.edges.length; i++) {
			this.que.insertItem(new Edge(this.edges[i][0], this.edges[i][1], this.edges[i][2]));
		}

		while (this.mst.size() < this.numOfVertices - 1) {
			Edge current = this.que.removeMin();
			int pos1 = this.cluster[current.getEnd() - 1];
			int pos2 = this.cluster[current.getStart() - 1];

			if (pos1 != pos2) {
				this.mst.add(current);
				this.totalWeight += current.getWeight();
				for (int k = 0; k < this.cluster.length; k++) {
					if (this.cluster[k] == pos1) {
						this.cluster[k] = pos2;
					}
				}
			}
		}

	}

	/**
	 * Print the MST
	 * 
	 * @param The
	 *            language to use for output
	 * @return A string representation of the mst
	 */
	public String printMST(ResourceBundle res) {
		String returnString;

		returnString = (res.getString("Processed_File")
				+ this.file.getAbsolutePath() + res.getString("Possible_Tree"));
		for (int i = 0; i < this.mst.size(); i++) {
			Edge temp = (Edge) this.mst.get(i);
			returnString = returnString + (temp.toString(res));
		}
		returnString = (returnString
				+ "\n-------------------------------------------------"
				+ res.getString("Total_Weight") + this.totalWeight + "\n\n");
		return returnString;
	}

	/**
	 * Read the graph from a file.
	 * 
	 * @parma The file containing the graph.
	 * @return A 2-dimensional array of the edges of the graph.
	 */
	private int[][] readFromFile(File f) {
		ArrayList numbers = new ArrayList();
		try {
			StreamTokenizer tok = new StreamTokenizer(new FileReader(f));
			while (tok.nextToken() != StreamTokenizer.TT_EOF) {
				numbers.add(new Integer((int) tok.nval));
			}
		} catch (IOException e) {
			System.err.println("Fatal Error: File not found!");
			System.exit(0);
		}

		int numberOfEdges = numbers.size() / 3;
		int[][] result = new int[numberOfEdges][3];
		Iterator iter = numbers.iterator();
		for (int i = 0; i < numberOfEdges; i++) {
			result[i][0] = ((Integer) iter.next()).intValue();
			result[i][1] = ((Integer) iter.next()).intValue();
			result[i][2] = ((Integer) iter.next()).intValue();
		}
		return result;
	}

	/**
	 * Get the MST vector
	 * 
	 * @return The vector containing the MST
	 */
	public Vector getVector() {
		return this.mst;
	}
}