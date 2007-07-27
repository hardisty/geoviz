/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordMultiPoint  (1)
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
* $Id: ShapeFileRecordMultiPoint.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 27 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package edu.psu.geovista.io.geog;

import java.io.IOException;

/**
* Holds a multipoint's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordMultiPoint.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordMultiPoint {
	// indices in box for the bounds of the bounding box
	private static final int XMIN = 0;
	private static final int YMIN = 1;


	// multipoint attributes
	private double[] box = {0, 0, 0, 0}; // bounding box
	private int numPoints = 0; // total number of points
	private double[][] points = {{0}}; // all points in multiPoint

	private int dataSize; // in shorts

	public ShapeFileRecordMultiPoint() {
	}

	public ShapeFileRecordMultiPoint(ShapeFileDataInputStream dis, boolean littleEndian) throws IOException {
		dataSize = 0;

		if(littleEndian) // littleEndian shoulds always be true
			dataSize = parseData(dis, dataSize);
	}

	public ShapeFileRecordMultiPoint(ShapeFileDataInputStream dis, int theDataSize) throws IOException {
		int dataRead = 0; // in shorts

		int shapeType = dis.readIntLE();
		dataRead += 2;

		// ignore null shapes
		if(shapeType == ShapeFile.SHAPE_TYPE_NULL)
			return;

		if(shapeType != ShapeFile.SHAPE_TYPE_MULTIPOINT)
			throw new IOException("ShapeFileRecordMultiPoint(), Unexpected shapeType = " + shapeType);

		dataRead = parseData(dis, dataRead);

		if(dataRead != theDataSize)	{
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

		numPoints = dis.readIntLE();
		dataRead += 2;
		points = new double[numPoints][2];

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
		dataSize += 2; // numPoints
		dataSize += 8 * numPoints; // points[]
	}

	public void write(ShapeFileDataOutputStream dos, boolean writeShapeType) throws IOException {
		if(writeShapeType) {
			dos.writeIntLE(ShapeFile.SHAPE_TYPE_MULTIPOINT);
		}

		for(int i = 0; i < 4; i++) {
			dos.writeDoubleLE(box[i]);
		}

		dos.writeIntLE(numPoints);

		for(int i = 0; i < numPoints; i++) {
			dos.writeDoubleLE(points[i][0]);
			dos.writeDoubleLE(points[i][1]);
		}
	}

	public static final int getShapeType() {
		return ShapeFile.SHAPE_TYPE_MULTIPOINT;
	}

	public void setBox(double[] b) {
		box = b;
	}

	public double[] getBox() {
		return box;
	}

	public void setNumPoints(int n) {
		numPoints = n;
	}

	public int getNumPoints() {
		return numPoints;
	}

	public void setPoints(double[][] p) {
		points = p;
	}

	public double[][] getPoints() {
		return points;
	}

	public void setDataSize(int d) {
		dataSize = d;
	}

	public int getDataSize() {
		return dataSize;
	}

	// Interface: GvVisualizableObject
	public double doubleValue() {
		return numPoints;
	}

	public String toString() {
		String res = new String("NumPoints=" + numPoints + " Origin X,Y=" + box[XMIN] + ", " + box[YMIN] );
		return res;
	}
}
