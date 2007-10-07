/*------------------------------------------------------------------------------
*
* Java source file for the class ShapeFileWriter            (1)
*
* Copyright (c), 2002, GeoVISTA Center                      (2)
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
*
* Original Author: Benyah Shaparenko, benyah5@hotmail.com   (3)
* $Author: hardisty $
*
* $Date: 2005/09/15 14:54:06 $
*
* $Id: ShapeFileWriter.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* May 21 2002     Benyah     Created file
*
------------------------------------------------------------------------------*/

package geovista.readers.geog;

import java.util.Vector;

/**
* Writes one or more shape files
* An example of how to use these classes to write a shape file
*
* @see ShapeFile.java
* @version $Id: ShapeFileWriter.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileWriter {

	public ShapeFileWriter() {
	}

	public static void main(String[] args) {
		try {
/*
			// EXAMPLE FOR POINT SHAPETYPE...  TO SEE, UNCOMMENT CODE AND COMMENT POLYGON CODE BELOW
			String baseName = new String("C:\\my documents\\points");
			String fileName = new String(baseName + ".shp");

			// create a ShapeFile
			ShapeFile shapeFile = new ShapeFile();

			// create a ShapeFileHeader
			ShapeFileHeader header = new ShapeFileHeader();

			// set the shape type
			header.setShapeType(ShapeFile.SHAPE_TYPE_POINT);

			// set the bounding box
			// boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}
			double[] theBox = {-1, -1, 1, 1, -1, 1, -1, 1};
			header.setBoundingBox(theBox);

			// in ShapeFile, set the ShapeFileHeader
			shapeFile.setFileHeader(header);

			// create a Vector with actual shapes (in order they should appear in file)
			// in shapes, all values must be set except for the dataSize value
			Vector theShapes = new Vector();
			ShapeFileRecordPoint point;

			for(int i = 0; i < 9; i++) {
				point = new ShapeFileRecordPoint();
				point.setX(i/10.0);
				point.setY((i-4.5)/5);
				theShapes.addElement(point);
			}

			// in ShapeFile, set the data
			shapeFile.setData(theShapes);

			// call the write function
			shapeFile.write(fileName, baseName);
			// END OF POINT SHAPETYPE EXAMPLE
*/

			// EXAMPLE FOR POLYLINR SHAPETYPE...
			String baseName = new String("C:\\polyline");
			String fileName = new String(baseName + ".shp");

			// create a ShapeFile
			ShapeFile shapeFile = new ShapeFile();

			// create a ShapeFileHeader
			ShapeFileHeader header = new ShapeFileHeader();

			// set the shape type
			header.setShapeType(ShapeFile.SHAPE_TYPE_POLYLINE);

			// set the bounding box
			// boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}
			double[] theBox = {-1, -1, 1, 1, -1, 1, -1, 1};
			header.setBoundingBox(theBox);

			// in ShapeFile, set the ShapeFileHeader
			shapeFile.setFileHeader(header);

			// create a Vector with actual shapes (in order they should appear in file)
			// in shapes, all values must be set except for the dataSize value
			Vector theShapes = new Vector();
			ShapeFileRecordPolyLine polyLine;

			for(int i = 0; i < 9; i++) {
				polyLine = new ShapeFileRecordPolyLine();
				double[] box = {-1, -1, 1, 1}; // {xmin, ymin, xmax, ymax}
				polyLine.setBox(box);
				polyLine.setNumPoints(1+i*3);
				polyLine.setNumParts(i+1);
				int[] parts = new int[i+1];
				for(int j = 0; j < i+1; j++) {
					parts[j] = 3*j;
				}
				polyLine.setParts(parts);
				double[][] thePoints = new double[1+i*3][2];
				for(int j = 0; j < 1+i*3; j++) {
					thePoints[j][0] = ((i+1)+(j+1))/((i+1)*(j+1));
					thePoints[j][1] = (j-i)/((i+1)*(j+1));
				}
				polyLine.setPoints(thePoints);
				theShapes.addElement(polyLine);
			}

			// in ShapeFile, set the data
			shapeFile.setData(theShapes);

			// call the write function
			shapeFile.write(fileName, baseName);
                        shapeFile.write(fileName, baseName);
			// END OF POLYLINE SHAPETYPE EXAMPLE

			System.out.println("shapeFile = " + fileName + ", file data = " + shapeFile.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
