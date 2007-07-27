/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordPointM      (1)
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
* $Id: ShapeFileRecordPointM.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
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
* Holds a pointM's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordPointM.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordPointM {
	private double x = 0;
	private double y = 0;
	private double m = 0;

	private int dataSize; // in shorts

	// constructors
	public ShapeFileRecordPointM() {
	}

	public ShapeFileRecordPointM(ShapeFileDataInputStream dis, boolean littleEndian) throws IOException {
		if(littleEndian)
			m = dis.readDoubleLE();
		else
			m = dis.readDouble();
	}

	public ShapeFileRecordPointM(ShapeFileDataInputStream dis, int theDataSize) throws IOException {
		int dataRead = 0; // in shorts

		int shapeType = dis.readIntLE();
		dataRead += 2;

		// ignore null shapes
		if(shapeType == ShapeFile.SHAPE_TYPE_NULL)
			return;

		if(shapeType != ShapeFile.SHAPE_TYPE_POINTM)
			throw new IOException("ShapeFileRecordPointM(), Unexpected shapeType = " + shapeType);

		x = dis.readDoubleLE();
		y = dis.readDoubleLE();
		m = dis.readDoubleLE();
		dataRead += 12;

		if(dataRead != theDataSize) {
			throw new IOException("Record data unaccounted for, read: " + dataRead + ", expected: " + theDataSize);
		}

		// See comment above
		dataSize = theDataSize;
	}

	public void updateDataSize() {
		dataSize = 2; // shapeType
		dataSize += 12; // x, y, m
	}

	public void write(ShapeFileDataOutputStream dos, boolean writeShapeType) throws IOException {
		if(writeShapeType) {
			dos.writeIntLE(ShapeFile.SHAPE_TYPE_POINTM);
		}

		dos.writeDoubleLE(x);
		dos.writeDoubleLE(y);
		dos.writeDoubleLE(m);
	}

	public static final int getShapeType() {
		return ShapeFile.SHAPE_TYPE_POINTM;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setM(double m) {
		this.m = m;
	}

	public double getM() {
		return m;
	}

	public int getDataSize() {
		return dataSize;
	}

	public String toString() {
		String res = new String("{" + x + ", " + y + ", " + m + "}");
		return res;
	}
}
