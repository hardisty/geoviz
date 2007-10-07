/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileRecordHeader      (1)
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
* $Id: ShapeFileRecordHeader.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
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
* Holds a record header's data
*
* @see ShapeFile.java
* @version $Id: ShapeFileRecordHeader.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileRecordHeader {
	// constant for length (size) of record header
	private final static int RECORD_HEADER_LENGTH = 4; // in shorts

	// record header attributes
	private int recordNumber;
	private int contentLength; // in shorts

	// constructor
	public ShapeFileRecordHeader() {
	}

	public ShapeFileRecordHeader(ShapeFileDataInputStream dis) throws IOException {
		recordNumber = dis.readInt();
		contentLength = dis.readInt();
	}

	public void write(ShapeFileDataOutputStream dos) throws IOException {
		dos.writeInt(recordNumber);
		dos.writeInt(contentLength);
	}
        public void writeShx(ShapeFileDataOutputStream dos) throws IOException {
                dos.writeInt(contentLength);
        }

	// accessor methods
	public void setRecordNumber(int recordNumber) {
		this.recordNumber = recordNumber;
	}

	public int getRecordNumber() {
		return recordNumber;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public int getContentLength() {
		return contentLength;
	}

	public int getTotalLength() {
		return contentLength + RECORD_HEADER_LENGTH;
	}

	public static int getHeaderLength() {
		return RECORD_HEADER_LENGTH;
	}

	public String toString() {
		String res = new String("recordNumber = " + recordNumber + ", contentLength = " + contentLength);
		return res;
	}
}
