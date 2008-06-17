/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMap
 Copyright (c), 2002, GeoVISTA Center
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMap.java,v 1.20 2005/08/19 19:17:32 hardisty Exp $
 $Date: 2005/08/19 19:17:32 $
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

package geovista.geoviz.map;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.Vector;

import geovista.common.data.DataSetForApps;
import geovista.readers.shapefile.example.GeoData48States;

/**
 * Calculates spatial weights using only sun's awt classes
 */
public class OldSpatialWeights {
	Shape[] shapes;
	Vector<Integer>[] weightVectors;
	float tolerance;
    int nTouches;
	public static final float DEFAULT_TOLERANCE = 0.1f;
	public static boolean touches(Shape sOne, Shape sTwo, float tolerance) {
		BasicStroke stroke = new BasicStroke(tolerance);
		Shape strokedOne = stroke.createStrokedShape(sOne);
		Shape strokedTwo = stroke.createStrokedShape(sTwo);

		Area a1 = new Area(strokedOne);
		a1.intersect(new Area(strokedTwo));
		return !a1.isEmpty();
	}

	public static boolean touchesSimple(Shape sOne, Shape sTwo) {

		Area a1 = new Area(sOne);
		a1.intersect(new Area(sTwo));
		return !a1.isEmpty();
	}
	
	public static Shape[] createStrokedShapes(Shape[] inShapes, float tolerance){
		Shape[] outShapes = new Shape[inShapes.length];
		BasicStroke stroke = new BasicStroke(tolerance);
		
		for (int i = 0; i < inShapes.length; i++){
			outShapes[i] = stroke.createStrokedShape(inShapes[i]);
		}
		return outShapes;
	}
	public OldSpatialWeights(Shape[] shapes){
		this.tolerance = OldSpatialWeights.DEFAULT_TOLERANCE;
		
		this.shapes = shapes;
		//we need to be able to get out the neighbors for any value
		//let's store them in an array, shall we
		//where each array item is a vector
		this.weightVectors = new Vector[shapes.length];
		for (int row = 0; row < shapes.length; row++){
			this.weightVectors[row] = new Vector<Integer>();
			for (int col = 0; col < shapes.length; col++){
				if (row < col && OldSpatialWeights.touches(shapes[row], shapes[col], tolerance)){
					this.weightVectors[row].add(col);
					nTouches++;
				} else if (col < row && this.weightVectors[col].contains(row)){
					this.weightVectors[row].add(col);
					nTouches++;
				}
			}
		}
		
	}

	public int[] getNeighbors(int obs){
		Integer[] nBors = new Integer[this.weightVectors[obs].size()];
		nBors = (Integer[])this.weightVectors[obs].toArray(nBors);
		int[] nBorsInts = new int[nBors.length];
		for (int i = 0; i < nBors.length; i++){
			nBorsInts[i] = nBors[i];
		}
		return nBorsInts;
		
	}
	public static void main(String[] args) {
		GeoData48States stateData = new GeoData48States();
		DataSetForApps dataSet = stateData.getDataForApps();
		Shape[] shapeData = dataSet.getShapeData();

		
		
//		String fileName = "C:\\arcgis\\arcexe81\\Bin\\TemplateData\\USA\\counties.shp";
//		fileName = "C:\\temp\\shapefiles\\intrstat.shp";
//		fileName = "C:\\data\\geovista_data\\shapefiles\\larger_cities.shp";
//		fileName = "C:\\data\\geovista_data\\shapefiles\\jin\\CompanyProdLL2000Def.shp";
//		fileName = "C:\\data\\geovista_data\\Historical-Demographic\\census\\census80_90_00.shp";
//
//		ShapeFileDataReader shpRead = new ShapeFileDataReader();
//		shpRead.setFileName(fileName);
//	
//		dataSet = shpRead.getDataForApps();
//		shapeData = dataSet.getShapeData();
		int nTouches = 0;
		long startTime = System.currentTimeMillis();
		
//		for (int i = 0; i < shapeData.length; i++){

//			System.out.print("touches = ");
//			for (int j = 0; j < shapeData.length; j++){
//				if (i != j && OldSpatialWeights.touches(shapeData[i], shapeData[j], tolerance)){
//					System.out.print(dataSet.getObservationName(j) + " ");
//					nTouches++;
//				}
//				
//			}
//		}
		OldSpatialWeights w = new OldSpatialWeights(shapeData);
		long endTime = System.currentTimeMillis();
		System.out.println("that took = " + (endTime -startTime));
		System.out.println("n touches = " + w.nTouches);
		
//		int nTouches = 0;
//		long startTime = System.currentTimeMillis();
//		Shape[] strokedShapes = OldSpatialWeights.createStrokedShapes(shapeData, tolerance);
//		long interTime = System.currentTimeMillis();
//		for (int i = 0; i < shapeData.length; i++){

//			System.out.print("touches = ");
//			for (int j = 0; j < shapeData.length; j++){
//				if (i != j && OldSpatialWeights.touchesSimple(strokedShapes[i], strokedShapes[j])){
//					//System.out.print(dataSet.getObservationName(j) + " ");
//					nTouches++;
//				}
//				
//			}
//		}
//		long endTime = System.currentTimeMillis();

System.out.println("that took = " + (endTime -startTime));
System.out.println("n touches = " + nTouches);		
		
		
		
		
	}

}