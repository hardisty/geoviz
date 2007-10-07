/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordPolygonM    (1)
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
* $Id: ShapeFileRecordPolygonM.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
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
* Holds a polygonM's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordPolygonM.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordPolygonM {
	// polygon attributes
	private ShapeFileRecordPolygon polygon = new ShapeFileRecordPolygon();
	private double mMin = 0;
	private double mMax = 0;
	private double[] mArray = {};

	private int dataSize; // in shorts

	public ShapeFileRecordPolygonM() {
	}

	public ShapeFileRecordPolygonM(ShapeFileDataInputStream dis, int theDataSize) throws IOException {
		int dataRead = 0; // in shorts

		int shapeType = dis.readIntLE();
		dataRead += 2;

		// ignore null shapes
		if(shapeType == ShapeFile.SHAPE_TYPE_NULL)
			return;

		if(shapeType != ShapeFile.SHAPE_TYPE_POLYGONM)
			throw new IOException("ShapeFileRecordPolygonM(), Unexpected shapeType = " + shapeType);

		polygon = new ShapeFileRecordPolygon(dis, true);
		dataRead += polygon.getDataSize();

		// if exists because the "M" info is optional
		if(dataRead != theDataSize) {
			mMin = dis.readDoubleLE();
			mMax = dis.readDoubleLE();
			dataRead += 8;

			mArray = new double[polygon.getNumPoints()];
			for(int i = 0; i < polygon.getNumPoints(); i++) {
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
		polygon.updateDataSize();
		dataSize += polygon.getDataSize() - 2; // polygon - (2 from polygon's shapeType)

		if(mArray.length > 0) {
			dataSize += 8; // mMin and mMax
			dataSize += 4 * polygon.getNumPoints(); // mArray[]
		}
	}

	public void write(ShapeFileDataOutputStream dos, boolean writeShapeType) throws IOException {
		if(writeShapeType) {
			dos.writeIntLE(ShapeFile.SHAPE_TYPE_POLYGONM);
		}

		polygon.write(dos, false);

		if(mArray.length > 0) {
			dos.writeDoubleLE(mMin);
			dos.writeDoubleLE(mMax);

			for(int i = 0; i < polygon.getNumPoints(); i++) {
				dos.writeDoubleLE(mArray[i]);
			}
		}
	}

	public static final int getShapeType() {
		return ShapeFile.SHAPE_TYPE_POLYGONM;
	}

	public void setBox(double[] box) {
		polygon.setBox(box);
	}

	public double[] getBox() {
		return polygon.getBox();
	}

	public void setNumPoints(int numPoints) {
		polygon.setNumPoints(numPoints);
	}

	public int getNumPoints() {
		return polygon.getNumPoints();
	}

	public void setNumParts(int numParts) {
		polygon.setNumParts(numParts);
	}

	public int getNumParts() {
		return polygon.getNumParts();
	}

	public void setParts(int[] parts) {
		polygon.setParts(parts);
	}

	public int[] getParts() {
		return polygon.getParts();
	}

	public void setPoints(double[][] points) {
		polygon.setPoints(points);
	}

	public double[][] getPoints() {
		return polygon.getPoints();
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
		double[] box = polygon.getBox();
		String res = new String("NumPolygonMs=" + polygon.getNumParts() + " Origin X,Y,M=" + box[0] + ", " + box[1] + ", " + mMin);
		return res;
	}
}
