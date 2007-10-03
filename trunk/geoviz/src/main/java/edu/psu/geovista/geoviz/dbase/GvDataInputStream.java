/* -------------------------------------------------------------------
   GeoVISTA Center (Penn State, Dept. of Geography)

   Java source file for the class GvFileIOException

   Copyright (c), 2000, GeoVISTA Center (Penn State, Dept. of Geography)
   All Rights Reserved.

   Original Author: Anonymous
   $Author: jmacgill $

   $Date: 2003-02-28 09:53:55 -0500 (Fri, 28 Feb 2003) $

   Reference:		Document no:
   ___				___

   To Do:
   ___

------------------------------------------------------------------- */

/* --------------------------- Package ---------------------------- */
package edu.psu.geovista.geoviz.dbase;

/* ------------------ Import classes (packages) ------------------- */
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;

/*====================================================================
                   Implementation of class GvDataInputStream
====================================================================*/
/**
 * GvDataInputStream reads from a DataInputStream
 *
 * @version $Revision: 4 $
 * @author Frank Hardisty  (hardisty@geog.psu.edu)
 * @see DataInputStream
 */

public class GvDataInputStream extends DataInputStream {
	private transient byte[] c = new byte[8];

	/**
	 * Construct a newly created GvDataInputStream
	 *
	 * @param in An InputStream.
	 */
	public GvDataInputStream(InputStream in) {
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

	public int readIntLE() throws IOException{
		readFully(c, 0, 4);
		return (((c[3])			<< 24) |
				((c[2] & 0xFF)	<< 16) |
				((c[1] & 0xFF)	<<  8) |
				((c[0] & 0xFF)));
	}

	public long readLongLE() throws IOException{
		return ((long)readIntLE() + ((long)readIntLE() << 32));
	}

	public float readFloatLE() throws IOException{
		return Float.intBitsToFloat(readIntLE());
	}

	public double readDoubleLE() throws IOException{
		return Double.longBitsToDouble(readLongLE());
	}
}
