package geovista.common.data;
   
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;
public class SpatialWeights {

	ArrayList<ArrayList<Integer>> neighbors;
	
	
	
	public SpatialWeights(int listLength) {
		super();
		this.neighbors = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < listLength; i++){
			this.neighbors.add(new ArrayList<Integer>());
		}
	}

	public List<Integer> getNeighbor(int obs){
		return neighbors.get(obs);
	}
	
	public void setNeighbors(int obs, List<Integer> neighbors){
		this.neighbors.set(obs, (ArrayList<Integer>) neighbors);
	}
	public void addNeighbor(int id1, int id2){
		if (id1 == id2){
			return;
		}
		List<Integer> id1Neighbors = this.getNeighbor(id1);
		List<Integer> id2Neighbors = this.getNeighbor(id2);
		if (id1Neighbors == null){
			id1Neighbors = new ArrayList<Integer>();
		}
		if (id2Neighbors == null){
			id2Neighbors = new ArrayList<Integer>();
		}
		id1Neighbors.add(id2);
		id2Neighbors.add(id1);
		
	}
	
	public void findNeighbors(List<Geometry> geoms){
		for (int i = 0; i < geoms.size() - 1; i++) {
			Geometry geom = geoms.get(i);

			for (int j = i + 1; j < geoms.size(); j++) {
				Geometry geom2 = geoms.get(j);
				if (geom.touches(geom2)) {
					this.addNeighbor(i, j);
				}
			}//next j
		}// next i
	}

}
