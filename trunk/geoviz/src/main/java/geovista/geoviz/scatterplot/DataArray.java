/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/

package geovista.geoviz.scatterplot;

/**
 * 
 * @author Xiping Dai
 * 
 */
public class DataArray {
	private double[] dataArray;
	private double[] extent = new double[2];
	private final double[] dataExtent = new double[2];
	private final double[] MMExtent = new double[2];
	private double alterBase;

	public DataArray() {
	}

	/**
	 * calculates extents upon construction
	 * 
	 * @param double[]
	 *            dataArray
	 */
	public DataArray(double[] dataArray) {
		this.dataArray = dataArray;
		calculateExtents(this.dataArray);
	}

	/**
	 * calculates extents
	 * 
	 * @param dataArray
	 */
	public void setDataArray(double[] dataArray) {
		this.dataArray = dataArray;
		calculateExtents(this.dataArray);
	}

	private void calculateExtents(double[] dataArray) {
		// Calculate the minimum and maximum value in an array.
		// extent[0] is minimum and extent[1] is maximum.
		if (dataArray == null) {
			return;
		}
		dataExtent[0] = Double.POSITIVE_INFINITY;
		dataExtent[1] = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < dataArray.length; i++) {
			if (!Double.isNaN(dataArray[i])) {
				dataExtent[0] = Math.min(dataExtent[0], dataArray[i]);
				dataExtent[1] = Math.max(dataExtent[1], dataArray[i]);
			}
		}
		extent[0] = dataExtent[0];
		extent[1] = dataExtent[1];
		calculateCoorExtents();
	}

	private void calculateCoorExtents() {
		// Find out the possible minimum and maximum values for axis based on
		// the array.
		// MMExtent[] are the extremes on axis and alterBase is the tick
		// spacing.
		double unitrange = (extent[1] - extent[0]) / 5;
		alterBase = Math
				.pow(10, Math.floor(Math.log(unitrange) / Math.log(10)));
		double ratio = unitrange / alterBase;

		if (ratio < 3) {
			alterBase = 2 * alterBase;
		} else if (ratio < 7.5) {
			alterBase = 5 * alterBase;
		} else {
			alterBase = 10 * alterBase;
		}
		MMExtent[1] = alterBase * Math.ceil(extent[1] / alterBase);
		MMExtent[0] = alterBase * Math.floor(extent[0] / alterBase);
	}

	public void setDataExtent() {
		extent = dataExtent;
		calculateCoorExtents();
	}

	public void setExtent(double[] extents) {
		extent = extents;
		calculateCoorExtents();
	}

	/**
	 * Return the data ranges which will be displayed.
	 * 
	 * @return double[] extent
	 */
	public double[] getExtent() {
		return extent;
	}

	public double getValueAtIndex(int index) {

		return dataArray[index];
	}

	/**
	 * Return the length of data array.
	 * 
	 * @return int length
	 */
	public int length() {
		return dataArray.length;
	}

	/**
	 * Return the extents on coordinate axes.
	 * 
	 * @return double[] MMExtent.
	 */
	public double[] getMaxMinCoorValue() {
		return MMExtent;
	}

	/**
	 * Return the base of labels.
	 * 
	 * @return double alterBase
	 */
	public double getMajorTick() {
		return alterBase;
	}

	/**
	 * Return the number of ticks on axis.
	 * 
	 * @return int tickNumber
	 */
	public int getTickNumber() {
		int tickNumber = (int) ((MMExtent[1] - MMExtent[0]) / alterBase);
		return tickNumber;
	}
}
