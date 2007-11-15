package geovista.touchgraph.mst;

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
	private final File file;

	private final int[][] edges;

	private int numOfVertices = 0;

	private Vector mst = null;

	private final int[] cluster;

	private int totalWeight;

	/**
	 * Constructor
	 * 
	 * @param The
	 *            name of the file containing the graph.
	 */
	public MinimumSpanningTree(String fileName) {
		file = new File(fileName);
		edges = readFromFile(file);
		for (int[] element : edges) {
			if (element[0] > numOfVertices) {
				numOfVertices = element[0];
			}
			if (element[1] > numOfVertices) {
				numOfVertices = element[1];
			}
		}
		mst = new Vector(10, 5);

		cluster = new int[numOfVertices];
		for (int j = 0; j < numOfVertices; j++) {
			cluster[j] = j + 1;
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
		edges = readFromFile(file);
		for (int[] element : edges) {
			if (element[0] > numOfVertices) {
				numOfVertices = element[0];
			}
			if (element[1] > numOfVertices) {
				numOfVertices = element[1];
			}
		}
		mst = new Vector(10, 5);

		cluster = new int[numOfVertices];
		for (int j = 0; j < numOfVertices; j++) {
			cluster[j] = j + 1;
		}
	}

	/**
	 * Calculate the MST using Kruskal's algorithm.
	 * 
	 * @return The MST as a two-dimensional array.
	 */
	public static ArrayList<MSTEdge> kruskal(int[] fromEdge, int[] toEdge,
			double[] weights, int numVertices) {
		int[] cluster = new int[numVertices];
		double totalWeight = 0;
		ArrayList<MSTEdge> mst = null;
		PriorityQue que = new PriorityQue(numVertices);
		for (int i = 0; i < fromEdge.length; i++) {
			que.insertItem(new MSTEdge(fromEdge[i], toEdge[i], weights[i]));
		}

		while (mst.size() < numVertices - 1) {
			MSTEdge current = que.removeMin();
			int pos1 = cluster[current.getEnd() - 1];
			int pos2 = cluster[current.getStart() - 1];

			if (pos1 != pos2) {
				mst.add(current);
				totalWeight += current.getWeight();
				for (int k = 0; k < cluster.length; k++) {
					if (cluster[k] == pos1) {
						cluster[k] = pos2;
					}
				}
			}
		}
		return mst;
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
				+ file.getAbsolutePath() + res.getString("Possible_Tree"));
		for (int i = 0; i < mst.size(); i++) {
			MSTEdge temp = (MSTEdge) mst.get(i);
			returnString = returnString + (temp.toString(res));
		}
		returnString = (returnString
				+ "\n-------------------------------------------------"
				+ res.getString("Total_Weight") + totalWeight + "\n\n");
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
		return mst;
	}
}