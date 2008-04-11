/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.common.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.vividsolutions.jts.geom.Geometry;

public class SpatialWeights {

	ArrayList<ArrayList<Integer>> neighbors;
	protected final static Logger logger = Logger
			.getLogger(SpatialWeights.class.getName());

	public SpatialWeights(int listLength) {
		super();
		neighbors = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < listLength; i++) {
			neighbors.add(new ArrayList<Integer>());
		}

	}

	public List<Integer> getNeighbor(int obs) {
		return neighbors.get(obs);
	}

	public void setNeighbors(int obs, List<Integer> neighbors) {
		this.neighbors.set(obs, (ArrayList<Integer>) neighbors);
	}

	public void addNeighbor(int id1, int id2) {
		if (id1 == id2) {
			return;
		}
		List<Integer> id1Neighbors = getNeighbor(id1);
		List<Integer> id2Neighbors = getNeighbor(id2);
		if (id1Neighbors == null) {
			id1Neighbors = new ArrayList<Integer>();
		}
		if (id2Neighbors == null) {
			id2Neighbors = new ArrayList<Integer>();
		}
		id1Neighbors.add(id2);
		id2Neighbors.add(id1);

	}

	public void findNeighbors(List<Geometry> geoms) {
		long nTouches = 0;
		for (int i = 0; i < geoms.size() - 1; i++) {
			Geometry geom = geoms.get(i);

			for (int j = i + 1; j < geoms.size(); j++) {
				Geometry geom2 = geoms.get(j);
				if (geom.touches(geom2)) {
					addNeighbor(i, j);
					nTouches++;
				}
			}// next j
		}// next i
		logger.info("Number of touches = " + nTouches);
	}
}
