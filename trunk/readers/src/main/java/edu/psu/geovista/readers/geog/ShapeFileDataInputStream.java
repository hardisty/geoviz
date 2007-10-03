/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileDataInputStream   (1)
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
* $Id: ShapeFileDataInputStream.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 25 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package edu.psu.geovista.readers.geog;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
* Enables DataInputStream to handle little endian.
*
* @see ShapeFile.java
* @see java.io.DataInputStream
* @version $Id: ShapeFileDataInputStream.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileDataInputStream extends DataInputStream {
	transient private byte[] c = new byte[8];

	public ShapeFileDataInputStream(InputStream in) {
		super(in);
	}

	/**
	 * reads a short. Little Endian version.
	 *
	 * it uses "readFully" meathod instead of reading each byte.
	 * <pre>
	 * readFully(c, 0, 2) is probably faster and use less memory than
	 * int ch1 = read();
	 * int ch2 = read();
	 * return (short)((ch2 << 8) + (ch1 << 0));
	 * </pre>
	 */
	public final short readShortLE() throws IOException {
		readFully(c, 0, 2);
		return (short)(((c[1] & 0xFF) << 8) | (c[0] & 0xFF));
	}

	public final char readCharLE() throws IOException {
		return (char)readShortLE();
	}

	public int readIntLE() throws IOException {
		readFully(c, 0, 4);
		return (((c[3])			<< 24) |
				((c[2] & 0xFF)	<< 16) |
				((c[1] & 0xFF)	<<  8) |
				((c[0] & 0xFF)));
	}

	public long readLongLE() throws IOException {
		return ((long)readIntLE() + ((long)readIntLE() << 32));
	}

	public float readFloatLE() throws IOException {
		return Float.intBitsToFloat(readIntLE());
	}

	public double readDoubleLE() throws IOException {
		return Double.longBitsToDouble(readLongLE());
	}
}
