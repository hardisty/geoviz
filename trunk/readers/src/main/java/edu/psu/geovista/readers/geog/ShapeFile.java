/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFile                  (1)
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
* $Date: 2005/09/15 15:03:03 $
*
* $Id: ShapeFile.java,v 1.3 2005/09/15 15:03:03 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 26 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package edu.psu.geovista.readers.geog;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

/**
* Holds a shape file's data.
* Consists of one fileHeader, N recordHeaders >= 0, and N data records (shapes).
*
* @see ShapeFileHeader.java
* @see ShapeFileReader.java
* @see ShapeFileRecord*.java
* @version $Id: ShapeFile.java,v 1.3 2005/09/15 15:03:03 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFile {
	// shapefile data
	ShapeFileHeader fileHeader = new ShapeFileHeader();
	transient Vector rHeaders = new Vector();
	transient Vector data = new Vector(); // holds the shapes in this shape file

	// constants for shape file type
	public static final int SHAPE_TYPE_NULL        = 0;
	public static final int SHAPE_TYPE_POINT       = 1;
	public static final int SHAPE_TYPE_POLYLINE    = 3;
	public static final int SHAPE_TYPE_POLYGON     = 5;
	public static final int SHAPE_TYPE_MULTIPOINT  = 8;
	public static final int SHAPE_TYPE_POINTZ      = 11;
	public static final int SHAPE_TYPE_POLYLINEZ   = 13;
	public static final int SHAPE_TYPE_POLYGONZ    = 15;
	public static final int SHAPE_TYPE_MULTIPOINTZ = 18;
	public static final int SHAPE_TYPE_POINTM      = 21;
	public static final int SHAPE_TYPE_POLYLINEM   = 23;
	public static final int SHAPE_TYPE_POLYGONM    = 25;
	public static final int SHAPE_TYPE_MULTIPOINTM = 28;
	public static final int SHAPE_TYPE_MULTIPATCH  = 31;

	// accessors
	public void setFileHeader(ShapeFileHeader fileHeader) {
		this.fileHeader = fileHeader;
	}

	public ShapeFileHeader getFileHeader() {
		return fileHeader;
	}

	public void setRecordHeaders(Vector recordHeaders) {
		this.rHeaders = recordHeaders;
	}

	public Vector getRecordHeaders() {
		return rHeaders;
	}

	public void setData(Vector data) {
		this.data = data;
	}

	public Vector getData() {
		return data;
	}

	// constructors
	public ShapeFile() {
	}

	public ShapeFile(String fileName, String baseName) throws IOException {
		initialize(fileName, baseName);
	}

	public ShapeFile(String fileName) throws IOException {
		String tmpFile	= fileName.toLowerCase();
		int suffixIndex = tmpFile.lastIndexOf('.');
		if(suffixIndex == -1)
			throw new IOException("No suffix supplied in: " + fileName);

		if(suffixIndex == 0)
			throw new IOException("Poorly formed file name: " + fileName);

		String baseName = tmpFile.substring(0, suffixIndex);
		initialize(fileName, baseName);
	}
        public ShapeFile(InputStream inStream){
          try {
            parseShapeInputStream(inStream);
          } catch (Exception ex){
            ex.printStackTrace();
          }
        }

	private void initialize(String fileName, String baseName) throws IOException {
		// Do the actual parse of the shape file
		parseShapeFile(fileName);

		// Create dbase file name and parse
	///	String dBaseFileName = new String(baseName + strDBasePrefix);
	///	GvArcDBaseFile dBaseFile = new GvArcDBaseFile(dBaseFileName, dataArrayGroup);
	}
        private void parseShapeFile(String fileName) throws IOException {
		// Create our stream(s)
		FileInputStream fisShape = new FileInputStream(fileName);
                parseShapeInputStream(fisShape);
        }
	// parsing function
	private void parseShapeInputStream(InputStream fisShape) throws IOException {

		ShapeFileDataInputStream dis = new ShapeFileDataInputStream(fisShape);
		fileHeader = new ShapeFileHeader(dis);

		try {
			int shapeType = fileHeader.getShapeType();
			int lastRecordNumber = 0;
			int recordNumber = 0;
			int fileLength;
			for(fileLength = fileHeader.getFileLength(); fileLength > 0; ) {
				ShapeFileRecordHeader recordHeader = new ShapeFileRecordHeader(dis);
				rHeaders.addElement(recordHeader);
				recordNumber = recordHeader.getRecordNumber();

				// Sanity check
				if((lastRecordNumber > 0) && (recordNumber != (lastRecordNumber + 1)))
					throw new IOException("Bad record number sequence: " + lastRecordNumber + " -> " + recordNumber + "!");
				else
					lastRecordNumber = recordNumber;

				switch(shapeType) {
					case SHAPE_TYPE_NULL: break; // file of nulls?
					case SHAPE_TYPE_POINT:
						data.addElement(new ShapeFileRecordPoint(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POLYLINE:
						data.addElement(new ShapeFileRecordPolyLine(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POLYGON:
						data.addElement(new ShapeFileRecordPolygon(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_MULTIPOINT:
						data.addElement(new ShapeFileRecordMultiPoint(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POINTZ:
						data.addElement(new ShapeFileRecordPointZ(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POLYLINEZ:
						data.addElement(new ShapeFileRecordPolyLineZ(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POLYGONZ:
						data.addElement(new ShapeFileRecordPolygonZ(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_MULTIPOINTZ:
						data.addElement(new ShapeFileRecordMultiPointZ(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POINTM:
						data.addElement(new ShapeFileRecordPointM(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POLYLINEM:
						data.addElement(new ShapeFileRecordPolyLineM(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_POLYGONM:
						data.addElement(new ShapeFileRecordPolygonM(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_MULTIPOINTM:
						data.addElement(new ShapeFileRecordMultiPointM(dis, recordHeader.getContentLength()));
						break;
					case SHAPE_TYPE_MULTIPATCH:
						data.addElement(new ShapeFileRecordMultiPatch(dis, recordHeader.getContentLength()));
						break;
					default:
						throw new IOException("Unexpected Arc Shape type: " + shapeType + ", input data file error");
				} // end switch

				fileLength -= recordHeader.getTotalLength();
			} // end for

			if(fileLength != 0)
				throw new IOException("Residual file on read, fileLengthShorts = " + fileLength);

			// release extra memory
			rHeaders.trimToSize();
			data.trimToSize();
		}
		catch(EOFException e) {
			// Ignore EOF
		}
	}

	public void write(String fileName, String baseName) throws IOException {

		// create streams
		FileOutputStream fosShape = new FileOutputStream(fileName);

		ShapeFileDataOutputStream dos = new ShapeFileDataOutputStream(fosShape);
                FileOutputStream shxStream = new FileOutputStream(baseName + ".shx");
                ShapeFileDataOutputStream shx = new ShapeFileDataOutputStream(shxStream);

                //flush headers vector, in case this is the second or later time this method is called
                rHeaders.removeAllElements();

		// update size of records and create record headers
		int shapeType = fileHeader.getShapeType();
		ShapeFileRecordHeader header;
		for(int i = 0; i < data.size(); i++) {
			header = new ShapeFileRecordHeader();
			header.setRecordNumber(i+1);

			switch(shapeType) {
				case SHAPE_TYPE_NULL: break;
				case SHAPE_TYPE_POINT:
					((ShapeFileRecordPoint)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPoint)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POLYLINE:
					((ShapeFileRecordPolyLine)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPolyLine)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POLYGON:
					((ShapeFileRecordPolygon)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPolygon)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_MULTIPOINT:
					((ShapeFileRecordMultiPoint)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordMultiPoint)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POINTZ:
					((ShapeFileRecordPointZ)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPointZ)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POLYLINEZ:
					((ShapeFileRecordPolyLineZ)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPolyLineZ)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POLYGONZ:
					((ShapeFileRecordPolygonZ)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPolygonZ)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_MULTIPOINTZ:
					((ShapeFileRecordMultiPointZ)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordMultiPointZ)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POINTM:
					((ShapeFileRecordPointM)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPointM)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POLYLINEM:
					((ShapeFileRecordPolyLineM)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPolyLineM)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_POLYGONM:
					((ShapeFileRecordPolygonM)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordPolygonM)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_MULTIPOINTM:
					((ShapeFileRecordMultiPointM)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordMultiPointM)data.elementAt(i)).getDataSize());
					break;
				case SHAPE_TYPE_MULTIPATCH:
					((ShapeFileRecordMultiPatch)data.elementAt(i)).updateDataSize();
					header.setContentLength(((ShapeFileRecordMultiPatch)data.elementAt(i)).getDataSize());
					break;
				default: throw new IOException("Unexpected Arc Shape type: " + shapeType + ", output data file error");
			} // end switch

			rHeaders.addElement(header);
		}

		// update size of file
		int fileLength = 0;
		for(int i = 0; i < rHeaders.size(); i++) {
			fileLength += (4 + ((ShapeFileRecordHeader)rHeaders.elementAt(i)).getContentLength());
		}

		fileHeader.setFileLength(fileLength);

		// do the actual writing
                //first write the shp header
                fileHeader.write(dos);

                //write the shx header
                fileHeader.setFileLength(rHeaders.size() * 4);//as per ESRI white paper, p. 24
                fileHeader.write(shx);
                fileHeader.setFileLength(fileLength);//reset out of paranoia
                int shxOffset = 50;//starting offset as per ESRI white paper, p. 24
		for(int i = 0; i < data.size(); i++) {
                        header = ((ShapeFileRecordHeader)rHeaders.elementAt(i));
                        header.write(dos);

                        shx.writeInt(shxOffset);
                        header.writeShx(shx);
                        shxOffset = shxOffset + header.getTotalLength();

                        switch(shapeType) {
				case SHAPE_TYPE_NULL: break;
				case SHAPE_TYPE_POINT: ((ShapeFileRecordPoint)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POLYLINE: ((ShapeFileRecordPolyLine)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POLYGON: ((ShapeFileRecordPolygon)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_MULTIPOINT: ((ShapeFileRecordMultiPoint)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POINTZ: ((ShapeFileRecordPointZ)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POLYLINEZ: ((ShapeFileRecordPolyLineZ)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POLYGONZ: ((ShapeFileRecordPolygonZ)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_MULTIPOINTZ: ((ShapeFileRecordMultiPointZ)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POINTM: ((ShapeFileRecordPointM)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POLYLINEM: ((ShapeFileRecordPolyLineM)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_POLYGONM: ((ShapeFileRecordPolygonM)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_MULTIPOINTM: ((ShapeFileRecordMultiPointM)data.elementAt(i)).write(dos, true); break;
				case SHAPE_TYPE_MULTIPATCH: ((ShapeFileRecordMultiPatch)data.elementAt(i)).write(dos, true); break;
				default: throw new IOException("Unexpected Arc Shape type: " + shapeType + ", output data file error");
			} // end switch
		}
                shx.close();
                dos.close();
	}


        public void saveXYtoFile(String folder, String filename)
        {
          try{
            FileOutputStream fostream = new FileOutputStream(folder + filename);

            DataOutputStream p = new DataOutputStream(fostream);

            int shapeType = fileHeader.getShapeType();
            if (shapeType != SHAPE_TYPE_POINT) return;

            String dataStr = "";
            for(int i = 0; i < data.size(); i++)
            {
              dataStr = ((ShapeFileRecordPoint)(data.elementAt(i))).getX() +
                      "," +
                      ((ShapeFileRecordPoint)(data.elementAt(i))).getY() +
                      "\n";
              p.writeBytes(dataStr);
            }
            p.flush();

            fostream.close();
            p.close();
          }catch(Exception ee)
          {   ee.printStackTrace();	}
        }

	public String toString() {
		String res = "header: " + fileHeader.toString() + ", data = ";
		return res;
	}
}
