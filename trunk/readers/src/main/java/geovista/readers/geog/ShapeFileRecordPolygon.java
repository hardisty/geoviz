/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordPolygon     (1)
*
* Copyright (c), 2001, GeoVISTA Center                      (2)
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
* $Id: ShapeFileRecordPolygon.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 20 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package geovista.readers.geog;

import java.io.IOException;

/**
* Holds a polygon's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordPolygon.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordPolygon {
	// indices in box for the bounds of the bounding box
	private static final int XMIN = 0;
	private static final int YMIN = 1;


	// polygon attributes
	private double[] box = {0, 0, 0, 0}; // bounding box
	private int numParts = 0; // number of rings
	private int numPoints = 0; // total number of points
	private int[] parts = {}; // for each ring, index of first point in points array
	private double[][] points = {{0}}; // all points in polygon

	protected int dataSize; // in shorts

	public ShapeFileRecordPolygon() {
	}

	public ShapeFileRecordPolygon(ShapeFileDataInputStream dis, boolean littleEndian) throws IOException {
		dataSize = 0;
		if(littleEndian) // littleEndian should always be true
			dataSize = parseData(dis, dataSize);
	}

	public ShapeFileRecordPolygon(ShapeFileDataInputStream dis, int theDataSize) throws IOException {
		int dataRead = 0; // in shorts

		int shapeType = dis.readIntLE();
		dataRead += 2;

		// ignore null shapes
		if(shapeType == ShapeFile.SHAPE_TYPE_NULL)
			return;

		if(shapeType != ShapeFile.SHAPE_TYPE_POLYGON)
			throw new IOException("ShapeFileRecordPolygon(), Unexpected shapeType = " + shapeType);

		dataRead = parseData(dis, dataRead);

		if(dataRead != theDataSize) {
			throw new IOException("Record data unaccounted for, read: " + dataRead + ", expected: " + theDataSize);
		}

		// See comment above
		dataSize = theDataSize;
	}

	private int parseData(ShapeFileDataInputStream dis, int dataRead) throws IOException {
		for(int i = 0; i < 4; i++) {
			box[i] = dis.readDoubleLE();
			dataRead += 4;
		}

		numParts = dis.readIntLE();
		numPoints = dis.readIntLE();
		dataRead += 4;
		parts = new int[numParts];
		points = new double[numPoints][2];

		for(int i = 0; i < numParts; i++) {
			int nextIndex = dis.readIntLE();
			dataRead += 2;
			if((nextIndex < 0) || (nextIndex >= numPoints))
				throw new IOException("ShapeFileRecordPolygon(), Invalid ringIndex = " + nextIndex);
			parts[i] = nextIndex;
		}

		for(int i = 0; i < numPoints; i++) {
			points[i][0] = dis.readDoubleLE();
			points[i][1] = dis.readDoubleLE();
			dataRead += 8;
		}

		return dataRead;
	}

	public void updateDataSize() {
		dataSize = 2; // shapeType
		dataSize += 16; // box[]
		dataSize += 4; // numParts and numPoints
		dataSize += 2 * numParts; // parts[]
		dataSize += 8 * numPoints; // points[]
	}

	public void write(ShapeFileDataOutputStream dos, boolean writeShapeType) throws IOException {
		if(writeShapeType) {
			dos.writeIntLE(ShapeFile.SHAPE_TYPE_POLYGON);
		}

		for(int i = 0; i < 4; i++) {
			dos.writeDoubleLE(box[i]);
		}

		dos.writeIntLE(numParts);
		dos.writeIntLE(numPoints);

		for(int i = 0; i < numParts; i++) {
			dos.writeIntLE(parts[i]);
		}

		for(int i = 0; i < numPoints; i++) {
			dos.writeDoubleLE(points[i][0]);
			dos.writeDoubleLE(points[i][1]);
		}
	}

	public static final int getShapeType() {
		return ShapeFile.SHAPE_TYPE_POLYGON;
	}

	public void setBox(double[] box) {
		this.box = box;
	}

	public double[] getBox() {
		return box;
	}

	public void setNumPoints(int numPoints) {
		this.numPoints = numPoints;
	}

	public int getNumPoints() {
		return numPoints;
	}

	public void setNumParts(int numParts) {
		this.numParts = numParts;
	}

	public int getNumParts() {
		return numParts;
	}

	public void setParts(int[] parts) {
		this.parts = parts;
	}

	public int[] getParts() {
		return parts;
	}

	public void setPoints(double[][] points) {
		this.points = points;
	}

	public double[][] getPoints() {
		return points;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getDataSize() {
		return dataSize;
	}

	public String toString() {
		String res = new String("NumPolygons=" + numParts + " Origin X,Y=" + box[XMIN] + ", " + box[YMIN] );
		return res;
	}
}
