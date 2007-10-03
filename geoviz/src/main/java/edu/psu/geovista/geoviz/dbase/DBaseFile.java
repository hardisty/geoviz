/* -------------------------------------------------------------------
         GeoVISTA Center (Penn State, Dept. of Geography)

         Java source file for the class DBaseFile

         Copyright (c), 2002, GeoVISTA Center
         All Rights Reserved.

         Original Author: Michael T. Wheeler
          Modified for more generic use, accessors added,
          some comments added by: Frank Hardisty

         $Author: hardisty $
         $Id: DBaseFile.java 2369 2005-09-15 19:38:56Z hardisty $
         $Date: 2005-09-15 15:38:56 -0400 (Thu, 15 Sep 2005) $

         $Author: hardisty $
         $Id: DBaseFile.java 2369 2005-09-15 19:38:56Z hardisty $
         $Date: 2005-09-15 15:38:56 -0400 (Thu, 15 Sep 2005) $

         Reference:                Document no:
         ___                                ___

         To Do:
         ___

------------------------------------------------------------------- */
/* --------------------------- Package ---------------------------- */
package edu.psu.geovista.geoviz.dbase;

/* ------------------ Import classes (packages) ------------------- */
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Vector;
import java.util.logging.Logger;


/*====================================================================
                                                Implementation of class DBaseFile
====================================================================*/

/**
 * DBaseFile reads a dBase (*.dbf) file into an Object[] array where
 * the first Object is an array of Strings that holds the names of the arrays,
 * and the sucessive Objects are arrays of primitives of doubles, ints, strings,
 * or booleans.
 *
 * DBaseFile can also be used to write dBase files, but this functionality
 * is only partly supported.
 *
 * @version $Revision: 2369 $
 * @author Michael T. Wheeler (mtwheels@psu.edu)
 * @see DBaseFieldDescriptor
 */
public class DBaseFile {
	protected final static Logger logger = Logger.getLogger(DBaseFile.class.getName());
    private static byte HEADER_FIELD_TERMINATOR = 0x0D;
    private static byte RECORD_STATUS_DELETED = 0x2A; // => '*'
    private static byte FIELD_STATUS_NULL = 0x2A; // => '*'
    private transient int numRecords;
    private transient int numFields;
    private transient short sizeRecordBytes;
    private transient Vector recordDescriptor;
    private Object[] dataSet;

    public DBaseFieldDescriptor getRecordDiscriptor(int idx) {
      return (DBaseFieldDescriptor) (this.recordDescriptor.elementAt(idx));
    }

    public DBaseFile(InputStream inStream) {
        this.readRecords(inStream);
    }

    public DBaseFile(String fileName) throws IOException {
        //dataSet = dataIn;
        FileInputStream fisDbase = new FileInputStream(new File(fileName));
        this.readRecords(fisDbase);
    }

    // Really a copy with an addition.	Hack for Craig
    public void initOutput(InputStream in, String outputFileName)
              throws IOException {

        GvDataInputStream gdis = new GvDataInputStream(in);

        FileOutputStream fosDbase = new FileOutputStream(
                                            new File(outputFileName));
        GvDataOutputStream gdos = new GvDataOutputStream(fosDbase);

        try {
            copyFileHeader(gdis, gdos);

            byte[] recordBuffer = new byte[sizeRecordBytes];

            // The recordNumber value in the Shape file starts at 1
            boolean numeric = false;

            for (int i = 1; i <= numRecords; i++) {
                gdis.read(recordBuffer, 0, sizeRecordBytes);
                gdos.write(recordBuffer, 0, sizeRecordBytes);

                if (numeric) {
                    byte ch1 = (byte) ((i >> 24) & 0xFF); // big
                    byte ch2 = (byte) ((i >> 16) & 0xFF);
                    byte ch3 = (byte) ((i >> 8) & 0xFF);
                    byte ch4 = (byte) ((i >> 0) & 0xFF); // little
                    int littleEndian = (ch4 << 24) | (ch3 << 16) |
                                       (ch2 << 8) | (ch1 << 0);
                    gdos.writeInt(littleEndian);
                } else {
                    String string = Integer.toString(i);
                    byte[] arrayByte = string.getBytes();

                    for (int j = 0; j < 6; j++) {
                        if (j < arrayByte.length) {
                            gdos.writeByte(arrayByte[j]);
                        } else {
                            gdos.writeByte(' ');
                        }
                    }
                }
            }
        } // end try
        catch (EOFException e) {
            //XXX bad bad...
            // Ignore EOF
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DBaseFile(String fileName, String outputFileName) throws IOException {
      FileInputStream fisDbase = new FileInputStream(new File(fileName));
      initOutput(fisDbase, outputFileName);
    }
    public DBaseFile(InputStream in, String outputFileName) throws IOException {
      initOutput(in,outputFileName);
  }
    private void readRecords(InputStream inStream) {
        GvDataInputStream gdis = new GvDataInputStream(inStream);

        try {
            @SuppressWarnings("unused")
			int numRecs = readFileHeader(gdis);

            byte[] recordBuffer = new byte[sizeRecordBytes];

            // Side effect - sets numRecords
            @SuppressWarnings("unused")
			int dimension = recordDescriptor.size();

            // The recordNumber value in the Shape file starts at 1
            for (int i = 1; i <= numRecords; i++) {
                gdis.read(recordBuffer, 0, sizeRecordBytes);
                loadRecord(i, dataSet, recordBuffer);
            }
        } // end try
        catch (EOFException e) {
            //XXX bad bad...
            // Ignore EOF
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int readFileHeader(GvDataInputStream gdis)
                        throws IOException {
        long byteCount = 0;
        @SuppressWarnings("unused")
		byte fileType = gdis.readByte();
        byteCount++;

        byte[] dateBytes = new byte[3];

        for (int i = 0; i < 3; i++)
            dateBytes[i] = gdis.readByte();

        byteCount++;

        // dBase file of course does not handle 2000 dates correctly.	Also, Calendar
        // seems to use a 0-based month.
        @SuppressWarnings("unused")
		GregorianCalendar lastModified = new GregorianCalendar(((dateBytes[0] < 50)
                                                                ? (2000 + dateBytes[0])
                                                                : (1900 + dateBytes[0])),
                                                               dateBytes[1] - 1,
                                                               dateBytes[2]);
        logger.finest(lastModified.toString());
        numRecords = gdis.readIntLE(); //num records is the length of the individual arrays
        byteCount = byteCount + 4;

        short sizeHeaderBytes = gdis.readShortLE();
        sizeRecordBytes = gdis.readShortLE();
        byteCount = byteCount + 4;

        gdis.skipBytes(20); // Unused and /or reserved
        byteCount = byteCount + 20;

        // We've already read 32 bytes + 1 for the final field terminator
        int fieldDescriptorSize = DBaseFieldDescriptor.getDataSizeBytes();
        sizeHeaderBytes -= 33;

        if ((sizeHeaderBytes % fieldDescriptorSize) != 0) {
            throw new IOException(
                    "Invalid number of header bytes.	Available = " +
                    sizeHeaderBytes + ", size = " + fieldDescriptorSize);
        }

        //fieldDescriptorCount is the number of arrays
        int fieldDescriptorCount = sizeHeaderBytes / fieldDescriptorSize;
        this.numFields = fieldDescriptorCount;
        this.dataSet = new Object[fieldDescriptorCount + 1]; //plus one for the variable names array

        String[] variableNames = new String[fieldDescriptorCount];
        dataSet[0] = variableNames;

        byte[] fieldDescriptorByteArray = new byte[fieldDescriptorSize];
        recordDescriptor = new Vector();

        for (int i = 0; i < fieldDescriptorCount; i++) {
            gdis.read(fieldDescriptorByteArray, 0, fieldDescriptorSize);
            byteCount = byteCount + fieldDescriptorSize;

            logger.finest("fieldDescriptorSize = " + fieldDescriptorSize);
            logger.finest("byteCount = " + byteCount);
            DBaseFieldDescriptor fieldDescriptor = new DBaseFieldDescriptor(
                                                           dataSet,
                                                           fieldDescriptorByteArray,
                                                           this.numRecords,
                                                           i + 1);

            recordDescriptor.addElement(fieldDescriptor);
            variableNames[i] = fieldDescriptor.getFieldName(); //add the names
        }

        // Read the last byte of the header, it should be a 0
        byte fieldTerminator = gdis.readByte();

        if (fieldTerminator != HEADER_FIELD_TERMINATOR) {
            throw new IOException("Invalid field terminator = " +
                                  fieldTerminator);
        }

        return numRecords;
    }

    // See above, hack for Craig
    private void copyFileHeader(GvDataInputStream gdis, GvDataOutputStream gdos)
                         throws IOException {
        // Input
        byte fileType = gdis.readByte();
        byte[] dateBytes = new byte[3];

        for (int i = 0; i < 3; i++)
            dateBytes[i] = gdis.readByte();


        // Output
        gdos.writeByte(fileType);

        for (int i = 0; i < 3; i++)
            gdos.writeByte(dateBytes[i]);


        // Input
        numRecords = gdis.readIntLE();

        short sizeHeaderBytes = gdis.readShortLE();
        sizeRecordBytes = gdis.readShortLE();


        // Ouput
        gdos.writeIntLE(numRecords);

        int fieldDescriptorSize = DBaseFieldDescriptor.getDataSizeBytes();
        gdos.writeShortLE(sizeHeaderBytes + fieldDescriptorSize);
        gdos.writeShortLE(sizeRecordBytes + 6);


        // Input
        gdis.skipBytes(20); // Unused and /or reserved

        // Output (skip 20)
        for (int i = 0; i < 20; i++)
            gdos.writeByte(0);


        // We've already read 32 bytes + 1 for the final field terminator
        sizeHeaderBytes -= 33;

        if ((sizeHeaderBytes % fieldDescriptorSize) != 0) {
            throw new IOException(
                    "Invalid number of header bytes.	Available = " +
                    sizeHeaderBytes + ", size = " + fieldDescriptorSize);
        }

        int fieldDescriptorCount = sizeHeaderBytes / fieldDescriptorSize;
        byte[] fieldDescriptorByteArray = new byte[fieldDescriptorSize];

        for (int i = 0; i < fieldDescriptorCount; i++) {
            // input
            gdis.read(fieldDescriptorByteArray, 0, fieldDescriptorSize);


            // output
            gdos.write(fieldDescriptorByteArray, 0, fieldDescriptorSize);
        }

        byte[] descriptorNew = createNewDescriptor(fieldDescriptorSize);
        gdos.write(descriptorNew, 0, fieldDescriptorSize);

        // Read the last byte of the header, it should be a 0
        byte fieldTerminator = gdis.readByte();

        if (fieldTerminator != HEADER_FIELD_TERMINATOR) {
            throw new IOException("Invalid field terminator = " +
                                  fieldTerminator);
        }


        // Output
        gdos.writeByte(fieldTerminator);
    }

    // This was a hack to create an outgoing data file for Craig.	It needs a lot of
    // work to be a more-general solution
    private byte[] createNewDescriptor(int fieldDescriptorSize) {
        byte[] arrayByte = new byte[fieldDescriptorSize];
        arrayByte[0] = 'I';
        arrayByte[1] = 'D';
        arrayByte[2] = '_';
        arrayByte[3] = 'C';
        arrayByte[17] = 0; // decimal places


        //		arrayByte[11] = DBaseFieldDescriptor.FIELD_TYPE_NUMERIC;
        arrayByte[11] = DBaseFieldDescriptor.FIELD_TYPE_STRING;
        arrayByte[16] = 6; // field length
        arrayByte[23] = 0;

        return arrayByte;
    }

    private void loadRecord(int rowNumber, Object[] dataSet,
                            byte[] recordBuffer) {
        try {
            // Read the status of the record from the first byte
            byte dataRecordStatus = recordBuffer[0];

            if (dataRecordStatus == RECORD_STATUS_DELETED) {
                logger.finest("Ignoring deleted record");

                return;
            }

            // Iterate through all of our fields
            // Note, the bufferOffset = 1 compensates for an unused first space (see Arc doc)
            int bufferOffset = 1;

            // Iterate through all of our fields
            // Note, the bufferOffset = 1 compensates for an unused first space (see Arc doc)
            int indexField = 0;

            for (Enumeration enumeration = recordDescriptor.elements();
                 enumeration.hasMoreElements();
                 indexField++) {
                DBaseFieldDescriptor fieldDescriptor =
                        (DBaseFieldDescriptor) enumeration.nextElement();

                // Read expected length and type from the schema
                @SuppressWarnings("unused")
				String fieldName = fieldDescriptor.getFieldName();

                // Since Java doesn't have an unsigned type, we need to use a short here so we don't screw up the sign bit
                short fieldLength = fieldDescriptor.getFieldLength();
                byte fieldType = fieldDescriptor.getFieldType();
                byte fieldStatus = recordBuffer[bufferOffset];
                Object dataArray = fieldDescriptor.getDataArray();

                if (fieldStatus == FIELD_STATUS_NULL) {
                    // No data for this field
                } else {
                    String dataString = new String(recordBuffer, bufferOffset,
                                                   fieldLength);


                    logger.finest(dataString);
                    dataString = dataString.trim();

                    // Convert the data
                    switch (fieldType) {
                    case DBaseFieldDescriptor.FIELD_TYPE_STRING:

                        if (dataString.length() > 0) {
                            String gvString = new String(dataString);
                            String[] stArray = null;
                            stArray = (String[]) dataArray;
                            stArray[rowNumber - 1] = gvString;
                        }

                        break;

                    case DBaseFieldDescriptor.FIELD_TYPE_NUMERIC:

                        byte decimalPlaces = fieldDescriptor.getFieldDecimalPlaces();

                        if (decimalPlaces > 0) { //it's a double

                            double[] dArray = null;
                            dArray = (double[]) dataArray;

                            if (dataString.length() > 0) { //not null?

                                double doubleValue = Double.parseDouble(
                                                             dataString);
                                dArray[rowNumber - 1] = doubleValue;
                            } else { //it's null
                                dArray[rowNumber - 1] = Double.NaN;
                            } //not null
                        } else { //an int

                            int[] iArray = null;
                            iArray = (int[]) dataArray;

                            if (dataString.length() > 0) { //not null?

                                int intValue = Integer.parseInt(dataString);
                                iArray[rowNumber - 1] = intValue;
                            } else { //null
                                iArray[rowNumber - 1] = Integer.MIN_VALUE;
                            } //end if null
                        } //end if decimal

                        break;

                    default:
                        break;
                    }
                }


                // Step over the field we just read
                bufferOffset += fieldLength;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getNumRecords() {
      return numRecords;
    }

    public int getNumFields(){
      return numFields;
    }

    public Object[] getDataSet() {
        return this.dataSet;
    }

    public void setDataSet(Object[] dataSet) {
        this.dataSet = dataSet;
    }

    public static void main(String[] args) {
        try {
            String stringFileName = "/D:/geovista_data/Temp/test_fields.dbf";
            String outputFileName = "/D:/geovista_data/Temp/test_fields2.dbf";
            @SuppressWarnings("unused")
			Object[] dataArray = null;
            DBaseFile dbaseFile2 = new DBaseFile(stringFileName); //, dataArray);
            dataArray = dbaseFile2.getDataSet();

            @SuppressWarnings("unused")
			DBaseFile dbaseFile = new DBaseFile(stringFileName, outputFileName);
            logger.finest(dbaseFile.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
