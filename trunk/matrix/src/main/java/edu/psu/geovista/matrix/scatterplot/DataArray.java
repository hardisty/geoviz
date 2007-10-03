/*
 * put your module comment here
 * formatted with JxBeauty (c) johann.langhofer@nextra.at
 */


package  edu.psu.geovista.matrix.scatterplot;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
public class DataArray {
	private double[] dataArray;
	private double[] extent = new double[2];
	private double[] dataExtent = new double[2];
	private double[] MMExtent = new double[2];
	private double alterBase;

	/**
	 * put your documentation comment here
	 */
	public DataArray () {
	}

	/**
	 * put your documentation comment here
	 * @param 	double[] dataArray
	 */
	public DataArray (double[] dataArray) {
		this.dataArray = dataArray;
		this.calculateExtents(this.dataArray);
	}

	/**
	 * put your documentation comment here
	 * @param dataArray
	 */
	public void setDataArray (double[] dataArray) {
		this.dataArray = dataArray;
		this.calculateExtents(this.dataArray);
	}

	/**
	 * put your documentation comment here
	 * @param dataArray[]
	 */
	private void calculateExtents (double[] dataArray) {
        //Calculate the minimum and maximum value in an array.
        //extent[0] is minimum and extent[1] is maximum.
		if (dataArray == null){
			return;
		}
		this.dataExtent[0] = Double.POSITIVE_INFINITY;
		this.dataExtent[1] = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < dataArray.length; i++) {
			if (!Double.isNaN(dataArray[i])) {
				this.dataExtent[0] = Math.min(this.dataExtent[0], dataArray[i]);
				this.dataExtent[1] = Math.max(this.dataExtent[1], dataArray[i]);
			}
		}
		this.extent[0] = this.dataExtent[0];
		this.extent[1] = this.dataExtent[1];
		this.calculateCoorExtents();
	}

	private void calculateCoorExtents (){
		//Find out the possible minimum and maximum values for axis based on the array.
		//MMExtent[] are the extremes on axis and alterBase is the tick spacing.
		double unitrange = (extent[1]-extent[0])/5;
		alterBase = Math.pow(10, Math.floor(Math.log(unitrange)/Math.log(10)));
		double ratio = unitrange/alterBase;

		if (ratio<3) alterBase = 2*alterBase;
		  else if (ratio<7.5) alterBase = 5*alterBase;
		else alterBase = 10*alterBase;
		MMExtent[1] = alterBase*Math.ceil(extent[1]/alterBase);
        MMExtent[0] = alterBase*Math.floor(extent[0]/alterBase);
	}

	/**
	 * Return the data ranges which will be displayed.
	 * @return double[] extent
	 */
	public void setDataExtent () {
		this.extent = dataExtent;
		this.calculateCoorExtents();
	}

	/**
	 * Return the data ranges which will be displayed.
	 * @return double[] extent
	 */
	public void setExtent (double[] extents) {
		this.extent = extents;
		this.calculateCoorExtents();
	}

	/**
	 * Return the data ranges which will be displayed.
	 * @return double[] extent
	 */
	public double[] getExtent () {
		return  extent;
	}

	/**
	 * put your documentation comment here
	 * @param int index
	 * @return double dataArray[index]
	 */
	public double getValueAtIndex (int index) {
        //   if ((index < 0) || (index >= values.length))
        //     throw new IllegalArgumentException("index = " + index + " is out of range");
		return  dataArray[index];
	}

	/**
	 * Return the length of data array.
	 * @return int length
	 */
	public int length () {
		return  dataArray.length;
	}

	/**
	 * Return the extents on coordinate axies.
	 * @return double[] MMExtent.
	 */
	public double[] getMaxMinCoorValue () {
		return  MMExtent;
	}

	/**
	 * Return the base of labels.
	 * @return double alterBase
	 */
	public double getMajorTick () {
		return  alterBase;
	}

	/**
	 * Return the number of ticks on axis.
	 * @return int tickNumber
	 */
	public int getTickNumber () {
		int tickNumber = (int)((MMExtent[1] - MMExtent[0])/alterBase);
		return  tickNumber;
	}
}



