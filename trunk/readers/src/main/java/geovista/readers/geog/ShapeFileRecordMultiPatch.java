/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordMultiPatch  (1)
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
* $Id: ShapeFileRecordMultiPatch.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 28 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package geovista.readers.geog;

import java.io.IOException;

/**
* Holds a multipatch's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordMultiPatch.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordMultiPatch {
	// indices in box for the bounds of the bounding box
	private static final int XMIN = 0;
	private static final int YMIN = 1;


	// multipatch attributes
	private double[] box = new double[4]; // bounding box
	private int numParts; // number of rings
	private int numPoints; // total number of points
	private int[] parts; // for each ring, index of first point in points array
	private int[] partTypes; // for each part, holds part type
	private double[][] points; // all points in polygon
	private double zMin;
	private double zMax;
	private double[] zArray;
	private double mMin;
	private double mMax;
	private double[] mArray;

	private int dataSize; // in shorts

	public ShapeFileRecordMultiPatch() {
	}

	public ShapeFileRecordMultiPatch(ShapeFileDataInputStream dis, int theDataSize) throws IOException {
		int dataRead = 0; // in shorts

		int shapeType = dis.readIntLE();
		dataRead += 2;

		// ignore null shapes
		if(shapeType == ShapeFile.SHAPE_TYPE_NULL)
			return;

		if(shapeType != ShapeFile.SHAPE_TYPE_MULTIPATCH)
			throw new IOException("ShapeFileRecordMultiPatch(), Unexpected shapeType = " + shapeType);

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
				throw new IOException("ShapeFileRecordMultiPatch(), Invalid ringIndex = " + nextIndex);
			parts[i] = nextIndex;
		}

		for(int i = 0; i < numParts; i++) {
			int nextType = dis.readIntLE();
			dataRead += 2;
			if((nextType < 0) || (nextType > 5))
				throw new IOException("ShapeFileRecordMultiPatch(), Invalid partType = " + nextType);
			partTypes[i] = nextType;
		}

		for(int i = 0; i < numPoints; i++) {
			points[i][0] = dis.readDoubleLE();
			points[i][1] = dis.readDoubleLE();
			dataRead += 8;
		}

		// make sure any and all rings are closed
		for(int i = 0; i < numParts; i++) {
			if(partTypes[i] >= 2) { // the part is a ring
				int lastIndex;

				// find the last point in the ring
				if(i == (numParts - 1))
					lastIndex = numPoints - 1;
				else
					lastIndex = parts[i+1] - 1;

				// if ring is not closed, throw exception
				if(points[parts[i]][0] != points[lastIndex][0] || points[parts[i]][1] != points[lastIndex][1])
					throw new IOException("ShapeFileRecordMultiPatch(), Invalid ring: first=" + points[parts[i]].toString() + " != last=" + points[lastIndex].toString());
			}
		}

		zMin = dis.readDoubleLE();
		zMax = dis.readDoubleLE();
		dataRead += 8;

		zArray = new double[numPoints];
		for(int i = 0; i < numPoints; i++) {
			zArray[i] = dis.readDoubleLE();
			dataRead += 4;
		}

		// if exists because the "M" info is optional
		if(dataRead != theDataSize) {
			mMin = dis.readDoubleLE();
			mMax = dis.readDoubleLE();
			dataRead += 8;

			mArray = new double[numPoints];
			for(int i = 0; i < numPoints; i++) {
				mArray[i] = dis.readDoubleLE();
				dataRead += 4;
			}
		}

		if(dataRead != theDataSize) {
			throw new IOException("Record data unaccounted for, read: " + dataRead + ", expected: " + theDataSize);
		}

		// See comment above
		dataSize = theDataSize;
	}

	public void updateDataSize() {
		dataSize = 2; // shapeType
		dataSize += 16; // box[]
		dataSize += 4; // numParts and numPoints
		dataSize += 2 * numParts; // parts[]
		dataSize += 2 * numParts; // partTypes[]
		dataSize += 8 * numPoints; // points[][]
		dataSize += 8; // zMin and zMax
		dataSize += 4 * numPoints; // zArray[]

		if(mArray.length > 0) {
			dataSize += 8; // mMin and mMax
			dataSize += 4 * numPoints; // mArray[]
		}
	}

	public void write(ShapeFileDataOutputStream dos, boolean writeShapeType) throws IOException {
		if(writeShapeType) {
			dos.writeIntLE(ShapeFile.SHAPE_TYPE_MULTIPATCH);
		}

		for(int i = 0; i < 4; i++) {
			dos.writeDoubleLE(box[i]);
		}

		dos.writeIntLE(numParts);
		dos.writeIntLE(numPoints);

		for(int i = 0; i < numParts; i++) {
			dos.writeIntLE(parts[i]);
		}

		for(int i = 0; i < numParts; i++) {
			dos.writeIntLE(partTypes[i]);
		}

		for(int i = 0; i < numPoints; i++) {
			dos.writeDoubleLE(points[i][0]);
			dos.writeDoubleLE(points[i][1]);
		}

		dos.writeDoubleLE(zMin);
		dos.writeDoubleLE(zMax);

		for(int i = 0; i < numPoints; i++) {
			dos.writeDoubleLE(zArray[i]);
		}

		if(mArray.length > 0) {
			dos.writeDoubleLE(mMin);
			dos.writeDoubleLE(mMax);

			for(int i = 0; i < numPoints; i++) {
				dos.writeDoubleLE(mArray[i]);
			}
		}
	}

	public static final int getShapeType() {
		return ShapeFile.SHAPE_TYPE_MULTIPATCH;
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

	public void setPartTypes(int[] partTypes) {
		this.partTypes = partTypes;
	}

	public int[] getPartTypes() {
		return partTypes;
	}

	public void setPoints(double[][] points) {
		this.points = points;
	}

	public double[][] getPoints() {
		return points;
	}

	public void setZMin(double zMin) {
		this.zMin = zMin;
	}

	public double getZMin() {
		return zMin;
	}

	public void setZMax(double zMax) {
		this.zMax = zMax;
	}

	public double getZMax() {
		return zMax;
	}

	public void setZArray(double[] zArray) {
		this.zArray = zArray;
	}

	public double[] getZArray() {
		return zArray;
	}

	public void setMMin(double mMin) {
		this.mMin = mMin;
	}

	public double getMMin() {
		return mMin;
	}

	public void setMMax(double mMax) {
		this.mMax = mMax;
	}

	public double getMMax() {
		return mMax;
	}

	public void setMArray(double[] mArray) {
		this.mArray = mArray;
	}

	public double[] getMArray() {
		return mArray;
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
