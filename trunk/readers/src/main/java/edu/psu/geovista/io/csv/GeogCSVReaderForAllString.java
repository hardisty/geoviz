/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * For reading csv file contain only String type. e.g. Meta file in csv format
 * @author: jin Chen 
 * @date: Oct 21, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.io.csv;

import java.io.InputStream;
import java.util.Arrays;

public class GeogCSVReaderForAllString extends GeogCSVReader{

  public GeogCSVReaderForAllString(){
  }

  public Object[] readFile(InputStream is){

        CSVParser shredder = new CSVParser(is);
        shredder.setCommentStart("#;!");
        shredder.setEscapes("nrtf", "\n\r\t\f");
        String[] headers = null;
        String[] types = null;
        int[] dataTypes = null;
        String[][] fileContent = null;
		int dataBegin;
		Object[] data;
        try {
          fileContent = shredder.getAllValues();

        } catch (Exception ex) {
          ex.printStackTrace();
        }
        //Add by jin
        headers = fileContent[0];//jin from 1=>0
		types = new String[fileContent.length ];//first line tells us types
        //Specify type for all columns are String ->
        for (int i=0;i<types.length ;i++){//this is
            types[i]="string";
        }
        //<-
		dataTypes = new int[types.length];
		int len;
		if (types[0].equalsIgnoreCase("int") || types[0].equalsIgnoreCase("double") || types[0].equalsIgnoreCase("string")){
			dataBegin = 1; //Jin chen , from 2=>1
			//headers = fileContent[1];//remved by jin
			data = new Object[headers.length + 1];//plus one for the headers themselves
			len = fileContent.length -dataBegin;
			for (int i = 0; i < headers.length; i++){
				if (types[i].equalsIgnoreCase("int")){
					data[i+1] = new int[len];
					dataTypes[i] = GeogCSVReader.DATA_TYPE_INT;
				} else if (types[i].equalsIgnoreCase("double")){
					data[i+1] = new double[len];
					dataTypes[i] = GeogCSVReader.DATA_TYPE_DOUBLE;
				} else if (types[i].equalsIgnoreCase("string")){
					data[i+1] = new String[len];
					dataTypes[i] = GeogCSVReader.DATA_TYPE_STRING;
				} else {
					throw new IllegalArgumentException("GeogCSVReader.readFile, unknown type = " + types[i]);
				}
			}
		} else{
			dataBegin = 1;
			headers = fileContent[0];
			data = new Object[headers.length + 1];//plus one for the headers themselves
			len = fileContent.length -dataBegin;
			for (int i = 0; i < headers.length; i++){
				if ((fileContent[1][i].equals(fileContent[1][i].toLowerCase())) && (fileContent[1][i].equals(fileContent[1][i].toUpperCase()))){
					if (fileContent[1][i].lastIndexOf(".") >=0){
						data[i+1] = new double[len];
						dataTypes[i] = GeogCSVReader.DATA_TYPE_DOUBLE;
					}else{
						data[i+1] = new int[len];
						dataTypes[i] = GeogCSVReader.DATA_TYPE_INT;
					}
				}else{
					data[i+1] = new String[len];
					dataTypes[i] = GeogCSVReader.DATA_TYPE_STRING;
				}
			}
		}
		data[0] = headers;

        String[] line = null;


          for (int row = dataBegin; row < len + dataBegin; row++){

              line = fileContent[row];


            int[] ints = null;
            double[] doubles = null;
            String[] strings = null;

            for (int column = 0; column < line.length; column++){
              String item = line[column];
              if (dataTypes[column] == GeogCSVReader.DATA_TYPE_INT){

                if (Arrays.binarySearch(GeogCSVReader.NULL_STRINGS,item) >= 0){
                                  ints = (int[])data[column+1];
                 ints[row -dataBegin] = GeogCSVReader.NULL_INT;
                } else {
                  ints = (int[])data[column+1];
                  ints[row-dataBegin] = Integer.parseInt(item);
                }
              } else if (dataTypes[column] == GeogCSVReader.DATA_TYPE_DOUBLE){
                if (Arrays.binarySearch(GeogCSVReader.NULL_STRINGS,item) >= 0){
                  doubles = (double[])data[column+1];
                  doubles[row -dataBegin] = GeogCSVReader.NULL_DOUBLE;
                } else {
                doubles = (double[])data[column+1];
                doubles[row-dataBegin] = Double.parseDouble(item);
                }
              } else if (dataTypes[column] == GeogCSVReader.DATA_TYPE_STRING){
                strings = (String[])data[column+1];
                strings[row-dataBegin] = item;
              } else {
                throw new IllegalArgumentException("GeogCSVReader.readFile, unknown type = " + types[row]);
              }//end if

            }//next column
          } //next row
          return data;

  }


}

