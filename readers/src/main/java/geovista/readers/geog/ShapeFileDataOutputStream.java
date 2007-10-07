/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileDataOutputStream  (1)
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
* $Id: ShapeFileDataOutputStream.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 26 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package geovista.readers.geog;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
* Enables DataOutputSream to handle little endian.
*
* @see ShapeFile.java
* @see java.io.DataOutputStream
* @version $Id: ShapeFileDataOutputStream.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileDataOutputStream extends DataOutputStream {
	public ShapeFileDataOutputStream(OutputStream out) {
		super(out);
	}

	// LE stands for little endian
	public void writeShortLE(int v) throws IOException {
		int ch1 = ((v >> 8) & 0xFF); // big
		int ch2 = ((v >> 0) & 0xFF); // little
		writeShort((ch2 << 8) + (ch1 << 0));
	}

	public void writeCharLE(int v) throws IOException {
		int ch1 = ((v >> 8) & 0xFF); // big
		int ch2 = ((v >> 0) & 0xFF); // little
		writeChar((ch2 << 8) + (ch1 << 0));
	}

	public void writeIntLE(int v) throws IOException {
		int ch1 = ((v >> 24) & 0xFF); // big
		int ch2 = ((v >> 16) & 0xFF);
		int ch3 = ((v >>  8) & 0xFF);
		int ch4 = ((v >>  0) & 0xFF); // little
		writeInt((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
	}

	public void writeLongLE(long v) throws IOException {
		int ch1 = (int) ((v >> 56) & 0xFF); // big
		int ch2 = (int) ((v >> 48) & 0xFF);
		int ch3 = (int) ((v >> 40) & 0xFF);
		int ch4 = (int) ((v >> 32) & 0xFF);
		int ch5 = (int) ((v >> 24) & 0xFF);
		int ch6 = (int) ((v >> 16) & 0xFF);
		int ch7 = (int) ((v >>  8) & 0xFF);
		int ch8 = (int) ((v >>  0) & 0xFF); // little
		writeLong((long)(((long)ch8 << 56) +
						 ((long)ch7 << 48) +
						 ((long)ch6 << 40) +
						 ((long)ch5 << 32) +
						 ((long)ch4 << 24) +
						 ((long)ch3 << 16) +
						 ((long)ch2 <<  8) +
						 ((long)ch1 <<  0)));
	}

	public void writeFloatLE(float v) throws IOException {
		writeIntLE(Float.floatToIntBits(v));
	}

	public void writeDoubleLE(double v) throws IOException {
		writeLongLE(Double.doubleToLongBits(v));
	}
}
