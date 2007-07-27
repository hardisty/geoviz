/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordPolyLineZ   (1)
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
* $Id: ShapeFileRecordPolyLineZ.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 28 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package edu.psu.geovista.io.geog;

import java.io.IOException;

/**
* Holds a polylineZ's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordPolyLineZ.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordPolyLineZ {
	// polylineZ attributes
	private ShapeFileRecordPolyLine polyLine = new ShapeFileRecordPolyLine();
	private double zMin = 0;
	private double zMax = 0;
	private double[] zArray = {};
	private double mMin = 0;
	private double mMax = 0;
	private double[] mArray = {};

	private int dataSize; // in shorts

	public ShapeFileRecordPolyLineZ() {
	}

	public ShapeFileRecordPolyLineZ(ShapeFileDataInputStream dis, int theDataSize) throws IOException {
		int dataRead = 0; // in shorts

		int shapeType = dis.readIntLE();
		dataRead += 2;

		// ignore null shapes
		if(shapeType == ShapeFile.SHAPE_TYPE_NULL)
			return;

		if(shapeType != ShapeFile.SHAPE_TYPE_POLYLINEZ)
			throw new IOException("ShapeFileRecordPolyLineZ(), Unexpected shapeType = " + shapeType);

		polyLine = new ShapeFileRecordPolyLine(dis, true);
		dataSize += polyLine.getDataSize();

		zMin = dis.readDoubleLE();
		zMax = dis.readDoubleLE();
		dataRead += 8;

		zArray = new double[polyLine.getNumPoints()];
		for(int i = 0; i < polyLine.getNumPoints(); i++) {
			zArray[i] = dis.readDoubleLE();
			dataRead += 4;
		}

		// if exists because the "M" info is optional
		if(dataRead != theDataSize) {
			mMin = dis.readDoubleLE();
			mMax = dis.readDoubleLE();
			dataRead += 8;

			mArray = new double[polyLine.getNumPoints()];
			for(int i = 0; i < polyLine.getNumPoints(); i++) {
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
		polyLine.updateDataSize();
		dataSize += polyLine.getDataSize() - 2; // polyLine - (2 from polyLine's shapeType)
		dataSize += 8; // zMin and zMax
		dataSize += 4 * polyLine.getNumPoints(); // zArray[]

		if(mArray.length > 0) {
			dataSize += 8; // mMin and mMax
			dataSize += 4 * polyLine.getNumPoints(); // mArray[]
		}
	}

	public void write(ShapeFileDataOutputStream dos, boolean writeShapeType) throws IOException {
		if(writeShapeType) {
			dos.writeIntLE(ShapeFile.SHAPE_TYPE_POLYLINEZ);
		}

		polyLine.write(dos, false);
		dos.writeDoubleLE(zMin);
		dos.writeDoubleLE(zMax);

		for(int i = 0; i < polyLine.getNumPoints(); i++) {
			dos.writeDoubleLE(zArray[i]);
		}

		if(mArray.length > 0) {
			dos.writeDoubleLE(mMin);
			dos.writeDoubleLE(mMax);

			for(int i = 0; i < polyLine.getNumPoints(); i++) {
				dos.writeDoubleLE(mArray[i]);
			}
		}
	}

	public static final int getShapeType() {
		return ShapeFile.SHAPE_TYPE_POLYLINEZ;
	}

	public void setBox(double[] box) {
		polyLine.setBox(box);
	}

	public double[] getBox() {
		return polyLine.getBox();
	}

	public void setNumPoints(int numPoints) {
		polyLine.setNumPoints(numPoints);
	}

	public int getNumPoints() {
		return polyLine.getNumPoints();
	}

	public void setNumParts(int numParts) {
		polyLine.setNumParts(numParts);
	}

	public int getNumParts() {
		return polyLine.getNumParts();
	}

	public void setParts(int[] parts) {
		polyLine.setParts(parts);
	}

	public int[] getParts() {
		return polyLine.getParts();
	}

	public void setPoints(double[][] points) {
		polyLine.setPoints(points);
	}

	public double[][] getPoints() {
		return polyLine.getPoints();
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
		double[] box = polyLine.getBox();
		String res = new String("NumPolyLineZs=" + polyLine.getNumParts() + " Origin X,Y,Z,M=" + box[0] + ", " + box[1] + ", " + zMin + ", " + mMin);
		return res;
	}
}
