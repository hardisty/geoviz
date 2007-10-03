/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileHeader            (1)
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
* $Id: ShapeFileHeader.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 19 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package edu.psu.geovista.readers.geog;

import java.io.IOException;

/**
* Holds a header's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileHeader.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileHeader {
	// constants for values in header if header is valid
	private static final int FILE_CODE = 9994;
	private static final int VERSION   = 1000;
	private static final int HEADER_SIZE_SHORTS	= 50;

	// header attributes
	private int fileCode   = -1; // should be == FILE_CODE
	private int fileLength = -1; // length in 16-bit shorts minus 50 for the header
	private int version    = -1; // should be == VERSION
	private int shapeType  = -1;
	private double boundingBox [] = {0, 0, 0, 0, 0, 0, 0, 0};
	// boundingBox: {xMin, yMin, xMax, yMax, zMin, zMax, mMin, mMax}

	// constructor
	public ShapeFileHeader() {
		fileCode = FILE_CODE;
		version = VERSION;
	}

	public ShapeFileHeader(ShapeFileDataInputStream dis) throws IOException {
		fileCode = dis.readInt();
		if(fileCode != FILE_CODE)
			throw new IOException("File ID in header not that of a ShapeFileHeader	(Found " + fileCode + " : Expected " + FILE_CODE +")");

		dis.skipBytes(20); // unused part of header
		fileLength = dis.readInt();
		fileLength -= HEADER_SIZE_SHORTS; // length of rest of file
		version = dis.readIntLE();
		shapeType = dis.readIntLE();
		for(int i = 0; i < 8; i++)
			boundingBox[i] = dis.readDoubleLE();
	}

	// copy constructor
	public ShapeFileHeader(ShapeFileHeader sfh) {
		fileCode = sfh.fileCode;
		fileLength = 0;
		version = sfh.version;
		shapeType = sfh.shapeType;
		for(int i = 0; i < 8; i++)
			boundingBox[i] = sfh.boundingBox[i];
	}

	public void write(ShapeFileDataOutputStream dos) throws IOException {
		// write all the shp file info
		dos.writeInt(fileCode);

		// write 20 bytes
		for(int i = 0; i < 5; i++)
			dos.writeInt(0);

		dos.writeInt(fileLength + HEADER_SIZE_SHORTS);
		dos.writeIntLE(version);
		dos.writeIntLE(shapeType);

		for(int i = 0; i < 8; i++)
			dos.writeDoubleLE(boundingBox[i]);
	}

	// accessor methods
	public void setFileCode(int fileCode) {
		this.fileCode = fileCode;
	}

	public int getFileCode() {
		return fileCode;
	}

	public void setFileLength(int fileLength) {
		this.fileLength = fileLength;
	}

	public int getFileLength() {
		return fileLength;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getVersion() {
		return version;
	}

	public void setShapeType(int shapeType) {
		this.shapeType = shapeType;
	}

	public int getShapeType() {
		return shapeType;
	}

	public void setBoundingBox(double[] boundingBox) {
		this.boundingBox = boundingBox;
	}

	public double[] getBoundingBox() {
		return boundingBox;
	}

	public String toString() {
		String res = new String("type = " + fileCode + ", size = " + fileLength + ", version = " + version + ", Shape Type = " + shapeType +
			"\n" + "X = {" + boundingBox[0] + ", " + boundingBox[2] + "}, Y = {" + boundingBox[1] + ", " + boundingBox[3] + "}, Z = {" + boundingBox[4] + ", " + boundingBox[5] +
			"}, M = " + boundingBox[6] + ", " + boundingBox[7] + "}");
		return res;
	}
}
