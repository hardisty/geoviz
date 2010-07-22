package ncg.statistics;
/*
Class to Read data from an input csv file into an array
Peter Foley, 19.07.2010
*/
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.Array2DRowRealMatrix;

public class ReadData {
	
	// url to input file
	private String url = null;
	
	// data 
	private RealMatrix data = null;

	// fieldnames for data array
	private String[] fieldNames = null;

	// array to store row indices (speed)
	private int[] rowIndices = null;

	// default empty constructor
	public ReadData() {
	}
	
	//constructor specifying url to input file
	public ReadData(String url) {
		this.url = url;
	}

	// read the data from input file
	public void readFile() throws IOException {
		
		LineNumberReader inputStream = null;

		try {
			// open the input stream
			inputStream = new LineNumberReader(
								new FileReader(url));
			
			// temporary list of string arrays to hold input data
			List<String[]> s_data = new ArrayList<String[]>();

			// read the data from file
			int num_cols = 0;
			String line = null;
			while ((line = inputStream.readLine() ) != null ) {
				String[] fields = line.split(",");
				s_data.add(fields);
				num_cols = Math.max(num_cols,fields.length);
			}
			
			// compute the number of rows in input file
			int num_rows = s_data.size();
			
			// allocate enough memory to store data in file
			double[][] data_arr =  new double[num_rows-1][num_cols];

			// allocate memory for rowIndices 
			rowIndices = new int[num_rows-1];

			//convert s_data to data / fieldnames
			for ( int item = 0; item < num_rows; item++) {
				
				// get fields for current line
				String[] fields = s_data.get(item);

				if (item > 0) {
					
					// store data
					for (int field = 0; field < fields.length; field++ ) {
						data_arr[item-1][field] = 
									Double.parseDouble(fields[field]);
					}

					rowIndices[item-1] = item-1;

				} else {
					
					// store field names
					fieldNames = fields;
				}
			}

			// convert to a real matrix
			data = new Array2DRowRealMatrix(data_arr);

		} finally {
			
			// close the input stream
			if (inputStream != null) {
				inputStream.close();
			}
		}

		if (data.getColumnDimension() != fieldNames.length ) {
			System.out.println("WARNING : Data Column Dimension [" + 
								data.getColumnDimension() + 
								"] does not match number of fields[" + 
								fieldNames.length);
		}

		System.out.println("Read [" + data.getRowDimension() + 
									"] lines from input file");
		System.out.println("Input Data contains [" + 
								data.getColumnDimension() +
								"] fields");
	}

	// return the data as an array of doubles
	public double[][] getData() {
		return data.getData();
	}
	
	// set the data array
	public void setData(double[][] data) {
		this.data = new Array2DRowRealMatrix(data);
	}
	
	// get the fieldNames array
	public String[] getFieldNames() {
		return fieldNames;
	}
	
	// set the fieldNames array
	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}
	
	// get the number of fields
	public int getNumFields() {
		return data.getColumnDimension();
	}
	
	// get the number of rows in the data
	public int getSizeData() {
		return data.getRowDimension();
	}
	
	// get the string for the field at index 'index'
	public String getField(int index ) {
		
		String field = null;
		try {
			field = fieldNames[index];
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println(e.getMessage());
		}

		return field;
	}
	
	// get data item at (i,j)
	public double getDataItem(int i, int j) {
		return data.getEntry(i,j);
	}

	// get the data in column j as an array of doubles
	public double[] getColumn(int j) {
		return data.getColumn(j);
	}

	// get the data in row i as an array of doubles
	public double[] getRow(int i) {
		return data.getRow(i);
	}

	// get a submatrix for all rows and columns specified by indices 
	// in cols array
	public double[][] getSubMatrix(int [] cols) {
		return data.getSubMatrix(rowIndices,cols).getData();
	}



}
