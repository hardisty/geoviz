/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class DBaseFieldDescriptor
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Michael T. Wheeler
 Modified for more generic use, accessors added,
 some comments added by: Frank Hardisty

 $Author: hardisty $
 $Id: DBaseFieldDescriptor.java 1998 2005-04-04 17:53:06Z hardisty $
 $Date: 2005-04-04 13:53:06 -0400 (Mon, 04 Apr 2005) $
 Reference:		Document no:
 ___				___
 To Do:
 ___
 -------------------------------------------------------------------  *
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */
/* --------------------------- Package ---------------------------- */


package  geovista.geoviz.dbase;


/* ------------------ Import classes (packages) ------------------- */
import  java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/*====================================================================
 Implementation of class DBaseFieldDescriptor
 ====================================================================*/
/**
 * DBaseFieldDescriptor holds the information about each
 * "column" or "variable" in a dBase file.
 *
 * @version $Revision: 1998 $
 * @author Michael T. Wheeler (mtwheels@psu.edu)
 * @see: http://www.apptools.com/dbase/faq/qformt.htm
 */
public class DBaseFieldDescriptor {
	protected final static Logger logger = Logger.getLogger(DBaseFieldDescriptor.class.getName());
    // Constants
	private static final int DATA_SIZE_BYTES = 32;
	public static final byte FIELD_TYPE_STRING = 'C';
	public static final byte FIELD_TYPE_DATE = 'D';
	public static final byte FIELD_TYPE_NUMERIC = 'N';
	public static final byte FIELD_TYPE_BOOLEAN = 'L';             // Logical
	public static final byte FIELD_TYPE_MEMO = 'M';
    // Fields
	private String fieldName;
	private byte fieldType;
	private byte fieldLength;
	private byte fieldDecimalPlaces;
	private byte setFieldsFlag;
        private Object dataArray;

    public DBaseFieldDescriptor (Object[] dataSet,
                                                  byte[] byteArray,
                                                  int numRecords, int currArrayNum
                                                  ) throws IOException {
	//public DBaseFieldDescriptor (byte[] byteArray) throws IOException {
		fieldName = new String(byteArray, 0, 10);
		fieldName = fieldName.trim();
		fieldDecimalPlaces = byteArray[17];
		if (logger.isLoggable(Level.FINEST)){
                logger.finest("");
                logger.finest("DBaseFieldDescriptor, fieldName = " + fieldName);
                logger.finest("DBaseFieldDescriptor, fieldType = " + byteArray[11]);
                if (fieldName.equals("UNITS50_UP")) {
                  for (int i = 0; i < byteArray.length; i++) {
                      logger.finest(i + " " + byteArray[i]);
                  }
                }
		}
		fieldType = processType(dataSet, fieldName, (fieldDecimalPlaces > 0),
				byteArray[11], numRecords, currArrayNum);
		fieldLength = byteArray[16];
		setFieldsFlag = byteArray[23];
		if (setFieldsFlag != 0){
                  logger.finest(this.getClass().getName() + "What is a SET FIELDS flag?");
                  logger.finest("Ignoring SET FIELDS flag");
                }
	}

    /**
     * Doesn't really parse right now, we just use the dBase standard values
     * as the constant.	We should probably convert to a Java-specific type
     * or a GeoVISTA-specific type at some point in here.
     */
      private byte processType (Object[] dataSet, String fieldName,
                                boolean floatingPoint, byte dBaseFieldType,
                                int numRecords, int currArrayNum) throws IOException {
		switch (dBaseFieldType) {
			case FIELD_TYPE_STRING:
				dataArray = new String[numRecords];
				break;
			case FIELD_TYPE_NUMERIC:
				if (floatingPoint) {
				  dataArray = new double[numRecords];
				}
				else {
				  dataArray = new int[numRecords];
				}
				break;

			case FIELD_TYPE_BOOLEAN:
				dataArray = new boolean[numRecords];
				break;
			case FIELD_TYPE_DATE:case FIELD_TYPE_MEMO:
				throw  new IOException("Currently unsupported fieldType, dBaseFieldType = "
						+ dBaseFieldType);
			default:
                          logger.warning("Unexpected dBaseFieldType = " + dBaseFieldType); //just continue and hope for the best
                               dataArray = new Double[numRecords];
		}
                //add to big array
                dataSet[currArrayNum] = dataArray;
		return  dBaseFieldType;
	}

    //begin accessors
    public void setFieldName (String fieldName) {
      this.fieldName = fieldName;
    }
    public String getFieldName () {
      return this.fieldName;
    }

    public void setFieldType (byte fieldType) {
      this.fieldType = fieldType;
    }
    public byte getFieldType () {
      return this.fieldType;
    }

    public void setFieldLength (byte fieldLength) {
      this.fieldLength = fieldLength;
    }
    public byte getFieldLength () {
      return this.fieldLength;
    }

    public void setFieldDecimalPlaces (byte fieldDecimalPlaces) {
      this.fieldDecimalPlaces = fieldDecimalPlaces;
    }
    public byte getFieldDecimalPlaces () {
      return this.fieldDecimalPlaces;
    }

    public void setSetFieldsFlag (byte setFieldsFlag) {
      this.setFieldsFlag = setFieldsFlag;
    }
    public byte getSetFieldsFlag () {
      return this.setFieldsFlag;
    }

    public void setDataArray (Object dataArray) {
      this.dataArray = dataArray;
    }
    public Object getDataArray () {
      return this.dataArray;
    }
    //end accessors

	final static int getDataSizeBytes () {
		return  DATA_SIZE_BYTES;
	}

	/**
	 * Gives info about the object.
	 */
	public String toString () {
		String res = new String("{" + fieldName + ", type = " + fieldType + ", length = "
				+ fieldLength + ", decimalPlaces = " + fieldDecimalPlaces + "}");
		return  res;
	}
}



