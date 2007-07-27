/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *  Data( numeric&non-numeric) is stored as a list of rows, each of row represent an observation
 *  Can add/remove column data
 *
 * @Original Author: jin Chen
 * @date: Aug 21, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.data.model;

import java.awt.Shape;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.common.utils.collection.CollectionUtils;
import edu.psu.geovista.data.geog.DataSetForApps;

public class DefaultAppDataModel implements AppDataModel {
	final static Logger logger = Logger.getLogger(DefaultAppDataModel.class.getName());

    public static final String  ShpColName="GeoVistaShape";
    public static final String  ShpAttrName="GeoVistaAttr";
    protected Vector <Object> Ids;// deprecated, could remove this field in future, use rowNames instead
    protected Map Id2Index;// key:id, value: row index. Thus easy obtain row index by ID.
    protected Vector  rowNames = new Vector();
                                             
    protected Vector values = new Vector(); //numeric values only

    protected ColumnList columns=new ColumnList();   //numeric column only
    protected String[] rowNameColumns;//the columnName of the column which serve as human-understandable observation name

    private String stringForNaN="";//String representation for missing data: NaN


    /**
        * Non-numerical data
        * Unless mention specifically, a data should be numerical one.
        * Usually we focus on numeric data and don't care too much on non-numeric data. Operations on non-numeric data
        * is just easy utilities to access the original data considering  it is hard to access them from Object[] data structure
        * But if dataModel involves add/remove record, it affects numeric data, not non-numeric data
        */
    protected Vector nvalues=new Vector(); //non-numeric values ?
    protected ColumnList ncolumns=new ColumnList(); //non-numerica column
    protected MetaData meta;

    /** List to store our event subscribers. */
    protected EventListenerList listenerList = new EventListenerList();
    /*
     *For regenerate rawData.
     */
     protected Object[] raw;
     protected Shape[] shpData;

    /*******************************************************************************************************
     *                Construction
     *******************************************************************************************************/

    public DefaultAppDataModel() {

    }

    DefaultAppDataModel(DataSetForApps dataSet){
        Object[] rawData=dataSet.getDataObjectOriginal();
        this.feedData(rawData);

    }
    /**
     *  Copyied from constructor of STFDataSet.java to make the model compatible with GeoVista
     * @param rawData
     */
    DefaultAppDataModel(Object[] rawData) {//
        this.feedData(rawData);

    }
    /**
     *
     * @param rawData STF data: A column based data. First element is an array of column header
     *                          Other elements are array of data. Only int[] and double[] are treated as numeric data
     */
    public void feedData(Object[] rawData) {
        raw=new Object[rawData.length ];
        System.arraycopy(rawData,0,raw,0,raw.length );

        Object[] dataIn = rawData;
        Object[] dataToShow = null; //Each element is an array
        int[] orignalToNew = new int[dataIn.length];
        String[] varNames = (String[])dataIn[0];
        boolean[] isNumeric = new boolean[dataIn.length];
        int count = 0;
        for (int i = 0; i < dataIn.length; i++) {
            Object obj = dataIn[i];
            //Determine if a data is numeric or not. Only double[] and int[] is treated as numeric
            if (obj instanceof double[] || obj instanceof int[]) {
                isNumeric[i] = true;
                count++;
            } else {
                isNumeric[i] = false;
            }
        }
        if (count == 0) return;

        String[] numericVarNames = new String[count];
        dataToShow = new Object[count + 3]; //one for variable names,one for observation names,one for spatial data;

        count = 0;
        for (int i = 1; i < isNumeric.length; i++) { //isNumeric.length= dataIn.length
            if (isNumeric[i]){
                dataToShow[count+1] = dataIn[i];
                numericVarNames[count] = varNames[i-1]; // -1 because of the varNames themselves
                orignalToNew[i] = count;//so we can get back if need be
                count++;
            }
            else{  //non numeric data excluding any shape data since shape data will be treated specially later
                Object[] nonNumeric=(Object[]) dataIn[i];
                if(! (nonNumeric instanceof Shape[])&&
                        i<=varNames.length ){
                    //varNames not including name of shape column thus, if want to add shape data here => ArrayOutofIndexException
                    //varNames = (String[])dataIn[0];(column names). However, some column don't have name.e.g.: Shape column but it is array of GeneralPath[].
                    //Any way, only keep data has name
                    String name=varNames[i-1]; // -1 because of the varNames themselves
                    this.addNonNumericData(name, nonNumeric);
                }


            }
        }
        dataToShow[0] = numericVarNames;
        for (int i = 0; i < varNames.length; i++) {
            String lower = varNames[i].toLowerCase();
            /** Observation names:
             *   By default, any String column whose name contain "name" will be treated as observation name column
             *   Now dataToShow[numericVarNames.length +1]  contain ob names. Ob name is set together with adding record
             *   see the bottom of the method
             *
            */
            if (lower.endsWith("name") && (dataIn[i + 1] instanceof String[])){
                dataToShow[numericVarNames.length +1] = (String[])dataIn[i+1];
                //this.spat.setObservationNames((String[])dataIn[i+1]);//+1 to skip varNames
            }
        }

        this.initColumns(numericVarNames.length);
        this.setColumnNames(numericVarNames);
        Object obj = dataToShow[1];
        double[] someDoubles = null;
        int[] someInts = null;
        int numObservations = -1;
        if (obj instanceof double[]) {
            someDoubles = (double[])obj;
            numObservations = someDoubles.length;
        } else if (obj instanceof int[]) {
            someInts = (int[])obj;
            numObservations = someInts.length;
        }


        String name = null;
        for (int row = 0; row < numObservations; row++) {
            float[] dataVals = new float[numericVarNames.length];
            for (int column = 1; column < dataVals.length + 1; column++) {
                obj = dataToShow[column];
                if (obj instanceof double[]) {
                    someDoubles = (double[])obj;
                    if(Double.isNaN(someDoubles[row]) )
                        dataVals[column-1]=Float.NaN ; //
                    else
                        dataVals[column-1] = (float)someDoubles[row];

                } else if (obj instanceof int[]) {
                    someInts = (int[])obj;
                    /*if(someInts[row]<AppDataModel.NULL_INT +10) {
                    logger.finest("get:"+someInts[row]);
                    logger.finest("AppDataModel.NULL_INT :"+AppDataModel.NULL_INT );
                    }*/
                    if(someInts[row]<=AppDataModel.NULL_INT+1 ) {
                        /* Jin: Hand null value
                        A bug in ShapeFileReader as well as in CSVFileReader: sometime it treat null as
                        -Integer.MAX_VALUE  and sometime Integer.MIN_VALUE.
                        To read data from these readers, treat int value equal to either extreme value as null
                        */
                        dataVals[column-1]=Float.NaN ;
                    }
                    else
                        dataVals[column-1] = (float)someInts[row];
                }//end if
            }//next column

            /**
             *  Observatoin name(record name).
             * If there is meta file to specify ob names, it will replace this one.
             */
            if (dataToShow[numericVarNames.length + 1] != null) {
                String[] names = (String[])dataToShow[numericVarNames.length +1];//
                name = names[row]; //name is observation name
            }
            this.addRow(dataVals, name);
        }//next row

        //shape data
        /*for (int i = 0; i < rawData.length; i++) {
            if (rawData[i] instanceof Shape[]){
                Shape[] shp=(Shape[]) rawData[i];
                this.setShpData(shp);
                break;
            }
            else if(rawData[i] instanceof Point2D[]) {
                assert false:"Only accept Shape type, not Point2D type!";
                break;

            }
        }


        this.originalNumericColNames= this.columns.getColumnNameList() ;*/


    }

    /*******************************************************************************************************
     *                Properties
     *******************************************************************************************************/
    /**
     *
     * Return data in the original format. We can't return just original data(so-called:rawData) since
     * rawData's format is used for data communication between Map and Scatterplot and other old Beans.
     * rawData must contain shape data, otherwise map won't work.  However, since with NCI project, shp data
     * and observation data are loaded seperated, which means rawData do NOT have shape data. We need add shape
     * data to rawData before send it to Map or other beans. Thus don't just return rawData.
     *
     *
     * @return       Object[]
     */

    public Object[] getGeoVistaRawData() throws Exception {

        //1. get numeric column names
        String[] numericColNames = this.getColumnNames();
        ColumnList numericCols = this.getColumns();
        if(numericColNames.length !=numericCols.size() ){
            throw new Exception("Internal exception: length of numeric column name doesn't match to length of the numeric values" );
        }
        //2. get non-numeric column names
        String[] nonNumColNames = this.getNonNumericColumnNames();
        ColumnList nonNumericCols = this.getNonNumericColumns();
        if(nonNumColNames.length !=nonNumericCols.size() ){
            throw new Exception("Internal exception: length of non-numeric column name doesn't match to length of the non-numeric values" );
        }


        int numericCount = numericCols.size();
        int nonnumericCount = nonNumericCols.size();

        List datalist=new ArrayList(numericCount);//for storing final data
        List headerlist=new ArrayList(numericCount); // column name(header) list
        //String[] header=(String[]) raw[0]; //header


        makeNameHeader(datalist, headerlist);
        //MUST add numeric before add non - numeric
        for (int i=0;i<numericCount ;i++){//numeric data
            double[] colvalue = this.getColumnValueAsDouble(i);// MUST use double[], not float. DataSetForAs NOT treat float[] as numeric data
            datalist.add(colvalue);

            String colName = numericColNames[i];
            headerlist.add(colName);
        }
          //add non-numeric data
        for (int i=0;i<nonnumericCount;i++){
            Object obj=this.getNonNumericColumnData(i);//   this.nvalues.get(i);
            datalist.add(obj);

            String colName = nonNumColNames[i];
            headerlist.add(colName);
        }




        String[] colNames = convertListToString(headerlist);
        datalist.add(0,colNames); //1st column is colnames

        //shape data
        if(shpData!=null)
        datalist.add(this.shpData );

        Object[] data = datalist.toArray();
        return data;
    }
    /**
     *
     * @param datalist
     * @param headerList
     *
     */
    protected  void makeNameHeader( List datalist, List headerList) {
        String[] rowNamesCols=this.getRowNameColumns() ;
        if(rowNamesCols!=null&&rowNamesCols.length >1){
            String[] combinednames = this.getRowNames(rowNamesCols, "-");//an additional column u make to act as observation name column
            headerList.add("Observationname");


            datalist.add(combinednames);//
        }
        else if(rowNamesCols!=null && rowNamesCols.length ==1){
            String rowNamesCol=rowNamesCols[0];
            if(headerList.size() ==0){
                if(!rowNamesCol.toLowerCase().endsWith("name") ){
                    rowNamesCol=rowNamesCol+"name";
                }
                headerList.add(rowNamesCol);
                String[] rowNames = CollectionUtils.convertToStringArray(this.getRowNames());
                datalist.add(rowNames);
            }
            else{
                for (int i=0;i<headerList.size() ;i++){
                    String  colName = (String) headerList.get(i);
                    if(colName.equalsIgnoreCase(rowNamesCol) ) {
                        if(!colName.toLowerCase().endsWith("name") ){//
                            colName=colName+"name"; // To make Map, ScatterPlot... obtain observation name, we will explicitly add "name" to the column serving as RowName column if it does not end with "name"
                            headerList.set(i,colName);
                        }
                    }
                }
            }

        }
        else{
            logger.finest(this.getClass() + " does not find rowNamesColumns. Observation name may not be available. ");
        }

    }
     protected  void initColumns(int num) {
        //this.numDimensions =num;
         //initMyNumDimensions(num,this.values , this.columns);
         columns =new ColumnList(num);
        for (int i=0; i<num; i++){
            columns.add(new Column());
            //valueLabels.add(null);
        }

     }

    public Object[] getRaw() {
        if(raw==null) try {
            return this.getGeoVistaRawData() ;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return raw;
    }

    public void setRaw(Object[] raw) {
        this.raw = raw;
    }
     /**/
    /*******************************************************************************************************
     *                Row(Record) operations
     *******************************************************************************************************/

    /**
     * Adds a record. The record must have the same number of dimensions as the currently
     * stored records.
     *
     *
     * @param values The float values of the record.
     * @param id A id for the record.
     */
    public void addRow(float values[], Object id){
        Vector arow = convertFloatToVector(values);


        if (getColumnCount() == 0){
            initColumns(values.length);
        }
        else if (arow.size()  != getColumnCount()){
            throw new IllegalArgumentException("Recordsets must have same number of dimensions (" + getColumnCount() + ")");
        }
        this.values.add(arow);
        Vector<Object> ids = this.getIds();
        ids.add(id);//
        if(rowNames==null){
            rowNames=new Vector();
        }
        rowNames.add(id);//by default, name =id


        this.fireRowsInserted(this.getRowCount(),this.getRowCount());

    }

     /**
     * Adds a record. The record must have the same number of dimensions as the currently
     * stored records.
     *
     * @param values The float values of the record.
     */
    public  void addRow(float values[]){
        addRow(values, null);
    }
    /**
     * Returns all values of a specific record.
     *
     * @param recordnum The number of the record to be returned.
     *
     * @return All values of the specified record..
     */
    public float[] getRowValueAsFloat(int recordnum) {

        return convertVectorToFloat((Vector)values.elementAt(recordnum));//buggy: need thrown if recordId<0
    }
    /**
     * Returns all values of a specific record.
     *
     * @param recordnum The number of the record to be returned.
     *
     * @return All values of the specified record..
     */
    public double[] getRowValueAsDouble(int recordnum) {
        //return (float[])values.elementAt(recordnum);
        return convertVectorToDouble((Vector)values.elementAt(recordnum));
    }
    /**
     * Returns a human-readable label for a specific record.
     *
     * @param row  row index.
     *
     * @return    A human-readable label for the record.
     */
    public String getRowName(int row) {
        Vector rowNames = this.getRowNames();

        if(row <0||row >=rowNames.size() ){
            return "";
            //throw new IndexOutOfBoundsException(row +"th row is not available, total rows size:"+this.rowNames.size());
        }
        if(rowNames.size() >0) {
            Object o = rowNames.elementAt(row);
            if(o!=null)
                return o.toString() ;
        }

        return "row "+ row;
    }


    public int[] getAllRowIndexs(){
        int[] ids=new int[this.getRowCount()];
        for (int i=0;i<ids.length ;i++){
            ids[i]=i;
        }
        return ids;
    }
    /**
     * get a row (num/non-numeric)as string[], each element is the attribute value for the row
     * @param row
     * @return
     */
    public String[] getRecordAsString(int row){
    	
        float[] numrow = this.getRowValueAsFloat(row);
        Object[] nnrow = this.getNonNumericRecord(row);//non-numeric data

        int d1=0;
        int d2=0;
        if (numrow!=null) d1=numrow.length ;
        if (nnrow!=null) d2=nnrow.length ;
        int len=d1+d2;
        String arow[]=new String[len];
        if(d1>0){
            for (int i=0;i<d1;i++){
                if(Float.isNaN(numrow[i])){
                    arow[i]=this.getStringForNaN() ;
                }
                else{
                    arow[i]=numrow[i]+"";
                }
            }
        }
        if(d2>0){
            for (int i=0;i<d2;i++){
                int col=d1+i;
                arow[col]=nnrow[i].toString() ;
            }
        }
        return arow;
    }
       /**
     * Returns the number of records.
     *
     * @return The number of records currently stored in the model.
     */
    public int getRowCount() {
           int num1 = values.size();
           int num2 = this.nvalues.size();
           return Math.max(num1,num2) ;
    }

    /**
     *
     * @param rowNames  a collection of record(or observation) name
     */
    /*public void setRowNames(String[] rowNameCol,Vector rowNames) {
        this.rowNameColumns =rowNameCol;
        this.rowNames = rowNames;
    }*/

    public void setRowNameColumns(String[] rowNameColumns) {
        this.setRowNames(null);// set current the rowNames=null
        this.rowNameColumns = rowNameColumns;
    }

    public void setRowNames(Vector rowNames) {
        this.rowNames = rowNames;
    }

    /**
     *
     * @return rownames
     * @deprecated
     * @
     */
    public Vector getRowNames() {

        if(rowNames!=null &&rowNames.size() >0)
            return rowNames;
        else{
            String[] rowNameColumns = this.getRowNameColumns();
            if(rowNameColumns!=null&&rowNameColumns.length >0){
                String[] rowNames = this.getRowNames(rowNameColumns, "-");
                Vector rns = CollectionUtils.convertToVector(rowNames);
                this.setRowNames(rns);//
                return rns;

            }
            else{
                return new Vector(0);
            }
        }
    }
    public String[] getRownames(){
      return CollectionUtils.convertToStringArray(this.getRowNames());
    }

    /**
     *
     * @param columns
     * @param delimit
     * @return
     */
    public String[] getRowNames(String[] columns,String delimit){
        int rowCount = this.getRowCount();
        String[] rownames=new String[rowCount];
        int[] colindexs=new int[columns.length ];
        for (int i=0;i<colindexs.length ;i++){
            String colname=columns[i];
            colindexs[i]= this.getNonNumericColumnIndexByName(colname);
            if(colindexs[i]<0){
                System.err.println(this.getClass() + "getRowNames unable to find column: "+colname);
            }
        }

        for (int row =0;row <rownames.length ;row++){
            StringBuffer bf=new StringBuffer(columns.length *2);
           for (int col =0;col <colindexs.length ;col++){
               int colindex=colindexs[col];
               if(colindex>=0){
                    Object[] acolumn = this.getNonNumericColumnData(colindex);
                    String value=(String) acolumn[row];
                    bf.append(value);
               }
               if(col<colindexs.length -1){
                   bf.append(delimit);
               }
           }
            rownames[row] = bf.toString();

        }
        return rownames;
    }

    public String[] getRowNameColumns() {
        return rowNameColumns;
    }


     public void setIds(Vector<Object> ids) {
        Ids = ids;
    }
    public Vector<Object> getIds() {
        if(Ids==null){
            Ids=new Vector<Object>(this.getRowCount() );
        }
        return Ids;
    }
    public Object getId(int index){
        if(Ids==null||Ids.get(index)==null ){
            String rowName = this.getRowName(index);
            if(rowName!=null)
                return rowName;
            else
                return new Integer(index);
        }
        return Ids.get(index);
    }
    /**
     * set a column as an ID column if such a column exist
     * @param columnName
     * @return true if sucessful
     */
   public boolean setIdColumn ( String columnName){
        int col = this.getNonNumericColumnIndexByName(columnName);
         Object[] idcol=null;
        if(col>=0){

            idcol =this.getNonNumericColumnData(col);

        }
        else{
           col = this.getColumnIndexByName(columnName);

          if(col >=0) {//FIPS code in numeric values
               float[] idvalues = this.getColumnValueAsFloat(col);
               idcol=new Object[idvalues.length];
               for (int i=0;i<idvalues.length;i++){
                   idcol[i]=new Integer((int) idvalues[i]) ;
               }
           }
           else {
               return false;
           }
        }


        if(idcol!=null){
            Map id2Index = getId2Index();
            id2Index.clear();
            Vector<Object> ids = this.getIds();
            ids.clear();
            for (int row =0;row <idcol.length ;row++){
                Object id = idcol[row];
                id2Index.put(id,row);
                ids.add(id);
            }
            return true;
        }
        else{
            System.err.println("Not found Id column:"+columnName+". The related function won't work. ");
            return false;
        }
    }

    public int getIndexById(Object id){
        Integer row = (Integer) Id2Index.get(id);
        return row.intValue() ;
    }

    public Map getId2Index() {
        if(this.Id2Index ==null){
                 Id2Index =new HashMap(this.getRowCount() );
             }
        return Id2Index;
    }

    /**
     *
     * @param id2Index
     */
    public void setId2Index(Map id2Index) {
        Id2Index = id2Index;
    }
    /*******************************************************************************************************
     *                Index operations
     *******************************************************************************************************/
     /**
     * remove numerica data
     */
    public void clearNumericValues() {
        this.values.clear() ;
        this.columns.clear() ;
    }
    /**
     *  Adds a column to the model.  The new column will have the
     *  identifier <code>columnName</code>.  <code>columnData</code> is the
     *  optional array of data for the column.  If it is <code>null</code>
     *  the column is filled with <code>null</code> values.  Otherwise,
     *  the new data will be added to model starting with the first
     *  element going to row 0, etc.  This method will send a
     *  <code>tableChanged</code> notification message to all the listeners.
     *
     * @see
     */
    public void addColumn(String columnName, float[] columnData) {
        addColumn(columnName, convertFloatToVector(columnData));
    }
    public void addColumn(String columnName, double[] columnData) {
        addColumn(columnName, convertDoubleToVector(columnData));
    }

    /**
     *  Adds a column to the model.  The new column will have the
     *  identifier <code>columnName</code>, which may be null.
     *  <code>columnData</code> is the
     *  optional vector of data for the column.  If it is <code>null</code>
     *  the column is filled with <code>null</code> values.  Otherwise,
     *  the new data will be added to model starting with the first
     *  element going to row 0, etc.  This method will send a
     *  <code>tableChanged</code> notification message to all the listeners.
     *
     * @param   columnName the identifier of the column being added
     * @param   columnData       optional data of the column being added
     */
    public void addColumn(String columnName, java.util.List columnData) {
        this.addColumn(columnName,columnData,true);
    }

    private synchronized void addColumn(String columnName, java.util.List columnData,boolean fireEvent) {

        addAColumn(columnData, columnName, fireEvent,this.columns ,this.values );
    }

    private void addAColumn(List columnData, String columnName, boolean fireEvent, ColumnList columns, Vector values) {
        int  numDimension=columns.size() ;
        if (columnData != null&&columnData.size() >0) {
            Column column=this.createColumn(columnName,columnData);
            columns.add(column);
            int numRecords = columnData.size(); //size of columnData. That is, the numofRecord (row) in the column
            if (numRecords > this.getRowCount() ) {
                values.setSize(numRecords); //values is row based, so need set row = numofRows
            }

            justifyRows(0, numRecords,values, numDimension+1);    //getNumRecords()
            int newColumn =numDimension;// columns.size()-1;//getNumDimensions()-1 ;
            for(int i = 0; i < numRecords; i++) {
                List row = (List)values.elementAt(i);
                //Object data= columnData.get(i);
                //Class type=data.getClass() ;
                row.set(newColumn,columnData.get(i));
            }
        }
        else {
            justifyRows(0, getRowCount(),values,numDimension);
        }
        if(fireEvent)
            fireTableStructureChanged();
    }

    private Column createColumn(String colName,List colData){

       Class dType=getColumnDataType(colData);

       boolean isNum=isNumericaData(dType);
       int fType=Column.FTYPE_NONE ;//AppDataModel.FTYPE_NONE ;
       if(isNum){
           fType=Column.FTYPE_NUMDATA ;//AppDataModel.FTYPE_NUMDATA ;
       }
       else{
          fType=Column.FTYPE_DATA ;//AppDataModel.FTYPE_DATA ;
       }
       /*int fType=AppDataModel.FTYPE_NONE ;
       if(isNum){
           fType=AppDataModel.FTYPE_NUMDATA ;
       }
       else{
          fType=AppDataModel.FTYPE_DATA ;
       }*/
       return new Column(colName,dType,fType) ;

    }
    private static Class getColumnDataType(List columnData){
        Class cls=null;
        for (Iterator iterator = columnData.iterator(); iterator.hasNext();) {
            Object o = (Object) iterator.next();
            if(o!=null){
                 cls=o.getClass() ;
                 return cls;
            }
        }
        return cls;
    }
    public static boolean isNumericaData(Class cls){
        if(cls==null){

            return false;
        }
        if(cls.equals(Double.class) ||cls.equals(Float.class)||
           cls.equals(Integer.class) ||cls.equals(Boolean.class ) ){
            return true;
        }
        return false;
    }
    public void removeColumn(String name) {
        int col=this.getColumnIndexByName(name);
        removeColumn(col);
    }
    public synchronized void removeColumn(int col) {
        removeMyColumn(col,values,columns);
    }

    private void removeMyColumn(int col,Vector values,ColumnList columns) {
        if(col<0||col>=this.getColumnCount() ){
            throw new IllegalArgumentException("out of range");
        }
        columns.remove(col);
        Enumeration e=values.elements() ;
        while(e.hasMoreElements() ){
            List row=(List) e.nextElement() ;
            row.remove(col);

        }
        fireTableStructureChanged();
    }

    /**
     * Returns a specific value of the dataset.
     *
     * @param row
     * @param column
     *
     * @return The value specified by row, column.
     */
    public float getValue(int row, int column) {
        //return ((float[])values.elementAt(record))[dimension];
        Object o=((Vector)values.get(row)).get(column);
        if (o instanceof Double){
            logger.finest("Double");
        }
        Float value=(Float) ((Vector)values.get(row)).get(column);
        return value.floatValue() ;
    }
    /**
     *
     * @param column  index of the column
     * @return
     */
    public double[] getColumnValueAsDouble(int column){
        long start=0;long stop=0; //put is at start of method
        if(logger.isLoggable(Level.FINEST)){//put is at start of method
            start=Calendar.getInstance().getTime().getTime() ;
        }

        double[] r=new double[values.size() ];

        Enumeration e=values.elements();
        int i=0 ;
        while(e.hasMoreElements() ){
            /*if(i==3110){
            logger.finest("3110");
            }*/
            Vector row=(Vector) e.nextElement() ;
            r[i]=((Float)row.get(column)).floatValue() ;
            i++;
        }
        if(logger.isLoggable(Level.FINEST)){  //put is at end of method
            stop=Calendar.getInstance().getTime().getTime() ;//for 3111 row take 0.0f to finish
            logger.finest("getColumnValueAsDouble() take "+(stop-start)*0.001f+" to finish" );
        }
        return r;
    }
    public float[] getColumnValueAsFloat(int column){
        long start=0; long stop=0; //put is at start of method
        if(logger.isLoggable(Level.FINEST)){//put is at start of method
            start=Calendar.getInstance().getTime().getTime() ;
        }

        float[] r = getColumData(column,this.values );

        if(logger.isLoggable(Level.FINEST)){  //put is at end of method
            stop=Calendar.getInstance().getTime().getTime() ;//for 3111 row take 0.0f to finish
            logger.finest("getColumnValueAsDouble() take "+(stop-start)*0.001f+" to finish" );
        }
        return r;
    }
    /**
     *
     * @param column
     * @param values
     * @return
     */
    private float[] getColumData(int column,Vector values) {
        float[] r=new float[values.size() ];

        Enumeration e=values.elements();
        int i=0 ;
        while(e.hasMoreElements() ){

            Vector row=(Vector) e.nextElement() ;
            r[i]=((Float)row.get(column)).floatValue() ;
            i++;
        }
        return r;
    }

    /**
     * Returns the maximum value for the given dimension.
     * see JMath.getMax() also
     * @return Maximum value of all records for the given dimension.
     */
    public float getColumnMaxValue(int column) {

        float maxval = this.getValue(0,column);
        //float maxval = ((float[])values.firstElement())[dimension];
        if (Float.isNaN(maxval)){
            maxval = -1 * Float.MAX_VALUE;
        }
        if(values.size() ==1){
            return Math.max(0,maxval);
        }
        for (int i=0; i<values.size(); i++){
            //if (((float[])values.elementAt(i))[dimension] > maxval) maxval = ((float[])values.elementAt(i))[dimension];
            float f=this.getValue(i,column);
            if (f > maxval)
                maxval = f;
        }

        return maxval;

    }

    /**
     * Returns the minimum value for the given dimension.
     *
     * @return Minimum value of all records for the given dimension.
     */
    public float getColumnMinValue(int column) {

        //float minval = ((float[])values.firstElement())[dimension];
        float minval =this.getValue(0,column);//initial
        if (Float.isNaN(minval)){
            minval = Float.MAX_VALUE;
        }
        if(values.size() ==1){
            return Math.min(0,minval);
        }
        for (int i=0; i<values.size(); i++){
            //if (((float[])values.elementAt(i))[dimension] < minval) minval = ((float[])values.elementAt(i))[dimension];
            float f=this.getValue(i,column);
            if (f < minval&&(int)f!=AppDataModel.NULL_INT )
                minval = f;
        }

        return minval;

    }

    /**
     *
     * @param model
     * @return    1st is mins[], 2nd is maxs[].
     */
    public static Object[] getColumnMinMaxValues(AppDataModel model){

        int columnCount = model.getColumnCount();
        float[] mins=new float[columnCount];

            float[] maxs=new float[columnCount] ;
        for (int col =0;col <columnCount;col++){
            float[] acolumn = model.getColumnValueAsFloat(col);
            float[] minMax = getMinMax(acolumn);
            mins[col]=minMax[0];
            maxs[col]=minMax[1];

        }
        Object[] result=new Object[]{mins,maxs};
        return result;
    }
      public static double[] getMinMax(double[] values){
           double[] v=new double[values.length ];
            System.arraycopy(values,0,v,0,values.length );
            Arrays.sort(v);
            return new double[]{ v[0],v[v.length -1]};

     }
     public static float[] getMinMax(float[] values){
          float[] v=new float[values.length ];
            System.arraycopy(values,0,v,0,values.length );
            Arrays.sort(v);
            return new float[]{ v[0],v[v.length -1]};

    }

    /**
     * Returns a String label for a specific dimension.
     *
     * @param column The dimension.
     *
     * @return A Human-readable label for the dimension.
     */
    public String getColumnName(int column) {
        return ((Column) this.columns.get(column)).getId() ; //axisLabels[dimension];
    }
    /**
     *
     * @param colName
     * @return    -1 if can't find the name
     */
    public int getColumnIndexByName(String colName){
        Enumeration e=this.columns.elements() ;
        int index=0;
        while(e.hasMoreElements() ){
            String id=((Column)e.nextElement()).getId() ;
            //String id=(String)e.nextElement() ;
            if(id.equals(colName) )
                return index;
            index++;
        }
        return -1;
    }

    public List getColumnNameList() {
        return columns.getColumnNameList() ;
    }
    public String[] getColumnNames() {
        return convertListToString(columns.getColumnNameList() );
    }
    /**
     * numeric +non-numeric
     * @return
     */
    public String [] getAllColumnNames(){
        String[] numColname = this.getColumnNames();
        String[] nnColname = this.getNonNumericColumnNames();
         int d1=0;
        int d2=0;
        if (numColname!=null) d1=numColname.length ;
        if (nnColname!=null) d2=nnColname.length ;
        int len=d1+d2;
        String arow[]=new String[len];
        if(d1>0){
            for (int i=0;i<d1;i++){
                arow[i]=numColname[i]+"";
            }
        }
        if(d2>0){
            for (int i=0;i<d2;i++){
                int col=d1+i;
                arow[col]=nnColname[i].toString() ;
            }
        }
        return arow;
    }
    /**
     * Sets the labels for all columns.
     *
     * @param colnames An Array of Strings to be used as human-readable labels for the axes.
     */
    public  void setColumnNames(String colnames[]){
        setMyColumnNames(colnames,this.columns );
    }

    private void setMyColumnNames(String[] colnames,ColumnList columns) {
        columns.setSize(colnames.length );
        for (int i=0; i<colnames.length; i++){
            this.setColumnName(i,colnames[i],columns);
        }
    }

    /**
     * Sets the label of a single axis.
     *
     * @param dimension The dimension this label is for.
     *
     */
    private  void setColumnName(int dimension, String label,ColumnList columns){
        //axisLabels[dimension] = label;
        Column col=((Column)columns.get(dimension));
        if(col==null){
            columns.setElementAt( new Column(label),dimension);
        }
        else{
            col.setId(label);
        }
    }

       /**
     *
     * @return   copy of the columnList
     */
    public ColumnList getColumns() {
        return (ColumnList) columns.clone() ;
    }

    public void setColumns(ColumnList columns) {
        this.columns = columns;
    }


    /**
     * add dataModel's numeric data, non-numeric data and shape data to this dataModel
     * @param dataModel
     */
    public void addDataModel(AppDataModel dataModel){
        int numCols = dataModel.getColumnCount();
        for (int i=0;i<numCols;i++){
            String colname = dataModel.getColumnName(i);
            double[] colvalue = dataModel.getColumnValueAsDouble(i);
            this.addColumn(colname,colvalue);
        }
    
            if(this.rowNameColumns ==null ||rowNameColumns.length ==0){
                this.rowNameColumns =dataModel.getRowNameColumns();
            }
            //DefaultAppDataModel dm = ((DefaultAppDataModel)dataModel);
            edu.psu.geovista.data.model.AppDataModel dm = dataModel;
            //Vector nonv = dm.getNonNumericValues();
            //non-numeric data names. Assume in the order of elements in ncolumns match the order of corresponding elements in nvalues
            ColumnList nonNnames = dm.getNonNumericColumns();
            /*ncolumns.addAll(nonNnames);
            nvalues.addAll(nonv);*/
            for (int i=0;i<nonNnames.size() ;i++){
                Column  col = (Column) nonNnames.get(i);
                String colname = col.getId();
                Object[] data = dm.getNonNumericColumnData(colname );
                this.addNonNumericData(colname,data);
            }
            Shape[] shp=dm.getShpData();
            if(this.getShpData()==null&&shp!=null)
               this.setShpData(shp);

            //need also add meta data
            MetaData meta = dm.getMeta();
            MetaData myMeta = this.getMeta();
            if(myMeta==null){
                myMeta=meta;
            }
            else{
                myMeta.add(meta);
            }

        
    }

    /*******************************************************************************************************
     *                support
     *******************************************************************************************************/
     //change # of row from <from> to <to>
     private void justifyRows(int from, int to,Vector values,int numDimension) {
        // Sometimes the DefaultTableModel is subclassed
        // instead of the AbstractTableModel by mistake.
        // Set the number of rows for the case when getNumRecords
        // is overridden.
        values.setSize(to);           //getNumRecords()

        for (int i = from; i < to; i++) {
            if (values.elementAt(i) == null) {
                values.setElementAt(new Vector(), i);
            }
            ((Vector)values.elementAt(i)).setSize(numDimension);             //getNumDimensions()
        }
    }
    /*******************************************************************************************************
     *                utilities
     *******************************************************************************************************/

    public  static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(anArray[i]);
        }
        return v;
    }
    /**
     * Returns a vector that contains the same objects as the array.
     * @param anArray  the array to be converted
     * @return  the new vector; if <code>anArray</code> is <code>null</code>,
     *				returns <code>null</code>
     */
    public  static Vector convertFloatToVector(float[] anArray) {
        if (anArray == null||anArray.length <=0) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(new Float(anArray[i]));
        }
        return v;
    }
    public  static Vector convertDoubleToVector(double[] anArray) {
        if (anArray == null||anArray.length <=0) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i=0; i < anArray.length; i++) {
            v.add(new Float(anArray[i]));
        }
        return v;
    }
    /**
     * assume v contain Integer or Double or Float or Long
     * @param v
     * @return
     */
    public static float[] convertVectorToFloat(Vector v){
        Object[] os=v.toArray();
        float[] result=new float[os.length ];
        for (int i=0;i<os.length ;i++){
            Object o=os[i];
            result[i]=floatValue(o);

        }
        return result;
    }
    /**
     * assume v contain Integer or Double or Float or Long
     * @param v
     * @return
     */
    public static double[] convertVectorToDouble(Vector v){
        Object[] os=v.toArray();
        double[] result=new double[os.length ];
        for (int i=0;i<os.length ;i++){
            Object o=os[i];
            result[i]=floatValue(o);

        }
        return result;
    }

    public static float floatValue(Object o) {
        float f=0;
        if(o instanceof Integer){
            f=((Integer)o).floatValue() ;
        }
        else if(o instanceof Double){
            f=((Double)o).floatValue() ;
        }
        else if(o instanceof Float){
            f=((Float)o).floatValue() ;
        }
        else{
            f=((Long)o).floatValue() ;
        }
        return f;
    }
    public static String[] convertListToString(List v){
        Object[] os=v.toArray();
        String[] result=new String[os.length ];
        for (int i=0;i<os.length ;i++){
            Object o=os[i];
            result[i]=(String)o;

        }
        return result;
    }
    public float[] convertTofloat(double[] da){
        float[] fa=new float[da.length ];
        for (int i=0;i<da.length ;i++){
            fa[i] = (float) da[i];
        }
        return fa;
    }

     /*******************************************************************************************************
     *                for non-numeric data
     *******************************************************************************************************/
     /**
      * assume the index of the array match the index of non-numeric columnlist
      * @param record
      */
     public void addNonNumericRow(Object[] record){
         this.nvalues.add(record);
         //this.add
     }
     /**
      *
      * add non-numeric data column
      * @param name       column name
      * @param nonNumeric  column data
      */
    public void addNonNumericData(String name, Object[] nonNumeric) {
                Class type=nonNumeric[0].getClass(); //
                //dp{
                if (logger.isLoggable(Level.FINEST)){

                    logger.finest("Add non-numeric data:");
                    logger.finest("name:"+name);
                    logger.finest("type:"+type.getName() );


                }//dp}


         Vector data = CollectionUtils.convertToVector(nonNumeric);

         this.addAColumn(data,name,true,this.ncolumns,this.nvalues );
    }


    public void removeNonNumericColumn(int index){
        this.removeMyColumn(index,this.nvalues ,this.ncolumns);
    }

    /**
     *
     * @return a copy of non numeric columns
     */
    public ColumnList getNonNumericColumns() {

        return (ColumnList) ncolumns.clone() ;
    }
    public String [] getNonNumericColumnNames(){
        return convertListToString(this.ncolumns.getColumnNameList());
    }


    public void setNonNumericColumns(ColumnList ncolumns) {
        this.ncolumns = ncolumns;
    }
    public void setNonNumericColumns(String[] ncolumns) {
        /*ColumnList clist=new ColumnList();

        List list = CollectionUtils.convertArrayToList(ncolumns);
        clist.addAll(list);
        this.ncolumns = clist;*/
        this.setMyColumnNames(ncolumns,this.ncolumns);
    }
    public Object[] getNonNumericRecord(int row){
        if(nvalues==null||nvalues.size() ==0)return new Object[0];
        Object record = this.nvalues.get(row);
        if(record instanceof Object[]){
            Object[] o = (Object[])record;
            return  o;
        }
        else{//vector
            Vector v=(Vector) record;
            return v.toArray();

        }

    }


    /**
     * As mapping between data and column is based on index they are added, we assume
     * that no non-numeric data can be added/removed once initialized
     * @param colName
     * @return
     */
    public Object[] getNonNumericColumnData(String colName){
       int index= getNonNumericColumnIndexByName(colName);//;getColumnIndexByName,ncolumns);
        if(index>=0)
        return this.getNonNumericColumnData(index);
        else
        return new Object[0];

    }
    public Object[] getNonNumericColumnData(int column){
        Object[] r=new Object[values.size() ];

        Enumeration rows=nvalues.elements();
        int i=0 ;
        while(rows.hasMoreElements() ){
            Vector row=(Vector) rows.nextElement() ;
            r[i]=(row.get(column)) ;
            i++;
        }
        return r;

    }
     public int getNonNumericColumnIndexByName(String colName){
       int index= getColumnIndexByName(colName,ncolumns);
       return index;
    }

    private static int getColumnIndexByName(String colName,ColumnList columns){
        Enumeration e=columns.elements() ;
        int index=0;
        while(e.hasMoreElements() ){
            String id=((Column)e.nextElement()).getId() ;
            //String id=(String)e.nextElement() ;
            if(id.equalsIgnoreCase(colName) )
                return index;
            index++;
        }
        return -1;
    }



     /*******************************************************************************************************
     *                 Event
     *******************************************************************************************************/
    /**
     * Subscribes a ChangeListener with the model.
     *
     * @param l The ChangeListener to be notified when values change.
     */
   public void addChangeListener(PlotModelListener l) {
        //listenerList.add(ChangeListener.class, l);
        listenerList.add(PlotModelListener.class, l);
    }

    /**
     * Removes a previously subscribed changeListener.
     *
     * @param l The ChangeListener to be removed from the model.
     */
    public void removeChangeListener(PlotModelListener l) {
        //listenerList.remove(ChangeListener.class, l);
        listenerList.remove(PlotModelListener.class, l);
    }

    public void fireRowsInserted(int firstRow, int lastRow) {
        firePlotChanged(new PlotModelEvent(this, firstRow, lastRow,
                PlotModelEvent.ALL_COLUMNS, PlotModelEvent.INSERT));
    }

    public void firePlotChanged(PlotModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList() ;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2) {
            if (listeners[i]==PlotModelListener.class) {
                ((PlotModelListener)listeners[i+1]).plotChanged(e);
            }
        }
    }

    private void fireTableStructureChanged() {
        firePlotChanged(new PlotModelEvent(this, PlotModelEvent.HEADER_ROW));
    }


    /*******************************************************************************************************
     *                Properties
     *******************************************************************************************************/

    /**
     * Returns the number of dimnesions.
     *
     * @return The number of dimensions of the records in this model.
     */
    public int getColumnCount() {
        return this.columns.size() ;
    }

    public Vector getNonNumericValues() {
        return nvalues;
    }

   /*******************************************************************************************************
     *                shp data
     *******************************************************************************************************/
    public Shape[] getShpData() {
        return shpData;
    }

    public void setShpData(Shape[] shpData) {
        this.shpData = shpData;
    }
    /*******************************************************************************************************
     *                meta data
     *******************************************************************************************************/
    public MetaData getMeta() {
        /*if(meta==null)meta=new EstatMetaData();*/
        return meta;
    }

    public void setMeta(MetaData meta) {
        this.meta = meta;
    }

    public void saveAsCsv(String filename) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public AppDataModel getSubsetByRowIndexs(int[] rows){
        DefaultAppDataModel mymodel=new DefaultAppDataModel();
        String[] columnNames = this.getColumnNames();
        mymodel.setColumnNames(columnNames);
        MetaData meta = this.getMeta();
        mymodel.setMeta(meta);
        mymodel.setRowNameColumns(this.getRowNameColumns());

        for (int i=0;i<rows.length ;i++){
            int row = rows[i];
            String rowName = this.getRowName(row);
            float[] rowvalues = this.getRowValueAsFloat(row);
            mymodel.addRow(rowvalues,rowName);
        }
        return mymodel;
    }
    /*******************************************************************************************************
     *                test
     *******************************************************************************************************/
    public void simplePrint(){
        int numRecords = this.getRowCount();
        ColumnList columns = this.getColumns();
        printNumericColumnsHeader(columns);
        for (int i=0;i<numRecords;i++){
             printARow(i,columns);
        }

    }
    private void printARow(int row, ColumnList columns){
        int len=20;
        String tab="\t";

       StringBuffer sb=new StringBuffer();
        String rowName = this.getRowName(row);
           sb.append(this.getFixedLengthString(rowName,len ));
           sb.append(tab);
       for (Iterator iterator = columns.iterator(); iterator.hasNext();) {

           Column column = (Column) iterator.next();
           String name = column.getId();
           int columnIndex = this.getColumnIndexByName(name);
           float[] values = this.getColumnValueAsFloat(columnIndex);
           float value = values[row];
           String svalue = this.getFixedLengthString(value+"",len );
           sb.append(svalue);
           sb.append(tab);
       }
        logger.finest(sb.toString() );
    }

    private  void printNumericColumnsHeader(ColumnList columns){
        int len=20;
        String tab="\t";

        StringBuffer sb=new StringBuffer();

        sb.append(getFixedLengthString("RowName",len  ));
        sb.append(tab);
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            Column column = (Column) iterator.next();
            String name = column.getId();
            String colname = getFixedLengthString(name,len);
            sb.append(colname);
            sb.append(tab);

        }
        logger.finest(sb.toString() );

    }

    private String getFixedLengthString(String s, int len){
        //int len=20;//# of letter;
        int mylen = s.length();
        int dlen=len-mylen;//difference
        String mys="";
        if(dlen<0){ //len< mylen, need cut some charaters
             mys=s.substring(0,len);
        }
        else if (dlen>0){  //len> mylen,  need add some space characters
            StringBuffer sb=new StringBuffer();
            for (int i=0;i<dlen;i++){
                sb.append(" ");
            }
            mys=s+sb.toString() ;
        }
        else{
            mys=s;
        }
        return mys;
    }
    /**
     * For a NaN data, return its string representation
     * @return
     */
    public String getStringForNaN() {
        return stringForNaN;
    }

    public void setStringForNaN(String stringForNaN) {
        this.stringForNaN = stringForNaN;
    }



    public static AppDataModel createDefaultAppDataModel() {
        return new DefaultAppDataModel();
    }

    public static AppDataModel createDefaultAppDataModel(DataSetForApps dataSet) {
        return new DefaultAppDataModel(dataSet);
    }

    public static AppDataModel createDefaultAppDataModel(Object[] rawData) {
        return new DefaultAppDataModel(rawData);
    }
    /*******************************************************************************************************
     *                convenient methods that not necessary put in the AppDataModel
     *******************************************************************************************************/
      /**
     *
     * @param datamodel
     * @param colnames
     * @param row
     * @return
     */
    public static Object[] getNonNumericData(AppDataModel datamodel, String[] colnames, int row){
        Object[] result=new Object[colnames.length ];
        for (int i=0;i<colnames.length ;i++){
            Object[] acol = datamodel.getNonNumericColumnData(colnames[i]);
            if(acol!=null && acol.length-1>=row ){
                result[i] = acol[row];

            }
        }
        return result;
    }

    /**
     *
     * @param datamodel
     * @param colnames
     * @param delimit
     * @return
     */
    public static String[] getDefaultRowNames(AppDataModel datamodel, String[] colnames, String delimit) {
        String[] rownames=new String[datamodel.getRowCount() ];
         for (int i=0;i<rownames.length ;i++){
             Object[] rownameinfo = DefaultAppDataModel.getNonNumericData(datamodel, colnames, i);
             StringBuffer namebf =new StringBuffer();
             for (int j =0;j <rownameinfo.length ;j++){

                    namebf.append(rownameinfo[j].toString() );
                    if(j <rownameinfo.length-1)
                    namebf.append(delimit);
            }
             rownames[i]=namebf.toString() ;
        }
        return rownames;
    }

	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}



}