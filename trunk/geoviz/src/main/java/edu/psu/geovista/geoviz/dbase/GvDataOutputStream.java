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
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/*====================================================================
                   Implementation of class GvDataOutputStream
====================================================================*/
/**
 * GvDataOutputStream writes to a DataOutputStream
 *
 * @version $Revision: 4 $
 * @author Frank Hardisty  (hardisty@geog.psu.edu)
 * @see DataOutputStream
 */
public class GvDataOutputStream extends DataOutputStream {

	/**
	 * Construct a newly created GvDataOutputStream
	 *
	 * @param in An InputStream.
	 */
	public GvDataOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * reads a short. Little Endian version.
	 */
	public void writeShortLE(int v) throws IOException{
		int ch1 = ((v >> 8) & 0xFF); // big
		int ch2 = ((v >> 0) & 0xFF); // little
		writeShort((ch2 << 8) + (ch1 << 0));
	}

	public void writeCharLE(int v) throws IOException{
		int ch1 = ((v >> 8) & 0xFF); // big
		int ch2 = ((v >> 0) & 0xFF); // little
		writeChar((ch2 << 8) + (ch1 << 0));
	}

	public void writeIntLE(int v) throws IOException{
		int ch1 = ((v >> 24) & 0xFF); // big
		int ch2 = ((v >> 16) & 0xFF);
		int ch3 = ((v >>  8) & 0xFF);
		int ch4 = ((v >>  0) & 0xFF); // little
		writeInt((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
	}
	public void writeLongLE(long v) throws IOException{
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

	public void writeFloatLE(float v) throws IOException{
		writeIntLE(Float.floatToIntBits(v));
	}

	public void writeDoubleLE(double v) throws IOException{
		writeLongLE(Double.doubleToLongBits(v));
	}
}

