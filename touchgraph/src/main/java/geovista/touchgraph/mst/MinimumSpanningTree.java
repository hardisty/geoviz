package geovista.touchgraph.mst;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Title: Minimum Spanning Tree Description: Uses Kruskal's algorithm to
 * calculate a MST from a graph. Copyright: Copyright (c) 2002 Company:
 * 
 * @author Markus Svensson
 * 
 */

public class MinimumSpanningTree {

	/**
	 * Calculate the MST using Kruskal's algorithm.
	 * 
	 * @return The MST as a two-dimensional array.
	 */
	public static ArrayList<MSTEdge> kruskal(int[] fromEdge, int[] toEdge,
			double[] weights, int numVertices) {
		int[] cluster = new int[numVertices];
		for (int j = 0; j < numVertices; j++) {
			cluster[j] = j + 1;
		}
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

	public static int findNumNodes(ArrayList<MSTEdge> edges) {
		HashSet set = new HashSet();

		for (MSTEdge edge : edges) {
			set.add(edge.getStart());
			set.add(edge.getEnd());

		}

		System.out.println(set.size());
		return set.size();

	}

	/**
	 * Calculate the MST using Kruskal's algorithm.
	 * 
	 * @return The MST as a two-dimensional array.
	 */
	public static ArrayList<MSTEdge> kruskal(ArrayList<MSTEdge> inputEdges) {
		int numVertices = MinimumSpanningTree.findNumNodes(inputEdges);
		int[] cluster = new int[numVertices];
		for (int j = 0; j < numVertices; j++) {
			cluster[j] = j + 1;
		}
		double totalWeight = 0;
		ArrayList<MSTEdge> mst = new ArrayList<MSTEdge>();
		PriorityQue que = new PriorityQue(numVertices);
		for (int i = 0; i < inputEdges.size(); i++) {
			que.insertItem(new MSTEdge(inputEdges.get(i).getStart(), inputEdges
					.get(i).getEnd(), inputEdges.get(i).getWeight()));
		}

		while (mst.size() < numVertices - 1) {
			MSTEdge current = que.removeMin();
			int pos1 = cluster[current.getEnd()];
			int pos2 = cluster[current.getStart()];

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

	public static MSTEdge[] kruskalArray(MSTEdge[] inputEdges) {
		ArrayList<MSTEdge> list = new ArrayList<MSTEdge>(inputEdges.length);
		for (MSTEdge element : inputEdges) {
			list.add(element);
		}
		ArrayList<MSTEdge> mstList = MinimumSpanningTree.kruskal(list);
		MSTEdge[] outputEdges = new MSTEdge[mstList.size()];
		for (int i = 0; i < mstList.size(); i++) {
			outputEdges[i] = mstList.get(i);
		}

		return outputEdges;
	}
}