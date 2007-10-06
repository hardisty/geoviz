package geovista.category;

/**
 * <p>Title: Studio applications</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GeoVSITA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;

import javax.swing.JPanel;

import edu.psu.geovista.common.data.ArraySort2D;
import geovista.matrix.scatterplot.DataArray;

public class DistributionGraph extends JPanel implements ComponentListener
{

	private static double AXISSPACEPORTION = 1.0/6.0;
	private static double DELTA = 1;
	private int pdf_method =0;
	transient private double[] data;
	transient private double[] continuousData;
	transient private double[][] dataWithIndex;
	transient private String variableName;
	transient private DataArray dataArray;
	transient private DataArray pdfDataArray;
	transient private double[] pdfArray;
	private double delta=DELTA;
	private boolean axisOn = true;
	private int plotOriginX;
	private int plotOriginY;
	private int plotEndX;
	private int plotEndY;
	private double[] xAxisExtents;
	private double[] yAxisExtents;
	private int[] exsInt;
	private int[] whyInt;

	private Color background;
	private Color foreground;
	private int nonsamplenum=100;

	private double[] gaussianPDF;
	private boolean gauss = false;
	private int[] gx;
	private int[] gy;
	protected final static Logger logger = Logger.getLogger(DistributionGraph.class.getName());
    public DistributionGraph()
    {
		this.setPreferredSize(new Dimension(300,300));
		this.setMinimumSize(new Dimension(100, 100));
    }

	public void setData (double[] data){
		this.data = data;
		this.dataArray = new DataArray(data);
		this.xAxisExtents = (double[])dataArray.getExtent().clone();
		this.delta = (this.xAxisExtents[1] - this.xAxisExtents[0])/20;
		this.addComponentListener(this);
		this.setBackground(Color.white);
		if (pdf_method == 0){
			calculatePDF();
		} else{
			this.nonsamplepdf();
		}
		setupDataforDisplay();
		this.validate();
	}

	public double[] getData (){
		return this.data;
	}

	public void setGaussianPDF(double[] gauss){
		this.gauss = true;
		this.gaussianPDF = gauss;
	}

	public void setVariableName (String name){
		this.variableName = name;
	}

	public String getVariableName (){
		return this.variableName;
	}


	public void setAxisOn (boolean axisOn){
		this.axisOn = axisOn;
	}

	public boolean getAxisOn (){
		return this.axisOn;
	}

	/**
	 * Minimum and maximum values for xAxis. xAxisExtents[0] = min, xAxisExtents[1] = max.
	 * @param double[] xAxisExtents
	 */
	public void setXAxisExtents (double[] xAxisExtents) {
		logger.finest("set up axis ..." + xAxisExtents[0]);
		this.xAxisExtents = (double[])xAxisExtents.clone();
		logger.finest("set up axis ..." + xAxisExtents[0]);
		//this.histogramCalculation();
		//this.setupDataforDisplay();
		repaint();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public double[] getXAxisExtents () {
		return  this.xAxisExtents;
	}

	/**
	 * put your documentation comment here
	 * @param c
	 */
	public void setBackground (Color c) {
		if (c == null)
			return;
		this.background = c;
		int colorTotal = c.getRed() + c.getGreen() + c.getBlue();
		int greyColor = 128 * 3;
		if (colorTotal < greyColor)
			this.foreground = Color.white;
		else
			this.foreground = Color.black;
		this.repaint();
	}

	private void calculatePDF(){
		int len = data.length;
		this.pdfArray = new double[len];
		//add index
		if (dataWithIndex == null || dataWithIndex.length != data.length || dataWithIndex[0].length != 2) {
			dataWithIndex = new double[data.length][2];
		}
		for (int i = 0; i < len; i++) {
		  dataWithIndex[i][0] = data[i];
		  dataWithIndex[i][1] = i;
		}
		//now sort
	      ArraySort2D sorter = new ArraySort2D();
	      sorter.sortDouble(dataWithIndex,0);
		//this.pdfArray[0] = dataWithIndex[0][0];
		//this.pdfArray[len-1] = dataWithIndex[len-1][0];
		for (int i = 0; i < len; i ++){
			int numberInDelta = 1;
			int j = 1;
			double mean = dataWithIndex[i][0];
			while ((i != 0)&&(dataWithIndex[i-j][0] > dataWithIndex[i][0] - this.delta)){
				mean += dataWithIndex[i-j][0];
				j ++;
				if (j > i){
					break;
				}
			}
			numberInDelta += j-1;
			j = 1;
			while ((i != len-1)&&(dataWithIndex[i+j][0] < dataWithIndex[i][0] + this.delta)){
				mean += dataWithIndex[i+j][0];
				j ++;
				if ((i+j) == len){
					break;
				}
			}
			numberInDelta += j-1;
			if (numberInDelta ==0){
				this.pdfArray[i] = 0;
			}else{
				mean /= numberInDelta;
			this.pdfArray[i] = numberInDelta/this.delta/2/len;
			}
		}
		this.pdfDataArray = new DataArray(pdfArray);
		this.yAxisExtents = (double[])this.pdfDataArray.getExtent().clone();
	}

	private void setupDataforDisplay(){

		if (axisOn){
			plotOriginX = (int)(this.getWidth()*AXISSPACEPORTION);
			plotOriginY = (int)(this.getHeight()*(1 - AXISSPACEPORTION));
			plotEndX = (int)(this.getWidth()) - (int)(this.getWidth()*AXISSPACEPORTION/2);
			plotEndY = (int)(this.getHeight()*AXISSPACEPORTION/2);
		}else {
			plotOriginX = 0;
			plotOriginY = (int)(this.getSize().getHeight() - 2);
			plotEndX = (int)(this.getSize().getWidth()) - 3;
			plotEndY = 3;
		}
		double[] dataX;
		if (pdf_method==0) { // sample
			int len = this.data.length;
			exsInt = new int[len];
			whyInt = new int[len];
			dataX = new double[len];
			for(int i = 0; i < len; i ++) {
				dataX[i] = this.dataWithIndex[i][0];
			}
		}
		else { //nonsample
			exsInt = new int[nonsamplenum];
			whyInt = new int[nonsamplenum];
			dataX = new double[nonsamplenum];
			for (int i=0;i<nonsamplenum;i++) {
				dataX[i] = this.continuousData[i];
			}
		}

		//get positions on screen
		double scaleX, scaleY;
		scaleX = getScale(plotOriginX, plotEndX, xAxisExtents[0], xAxisExtents[1]);
		exsInt = getValueScreen(dataX, scaleX, plotOriginX, xAxisExtents[0]);
		scaleY = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
		whyInt = getValueScreen(this.pdfArray, scaleY, plotOriginY, yAxisExtents[0]);

		if (gauss == true){
			int len = this.data.length;
			gx = new int[len];
			gy = new int[len];
			gx = getValueScreen(data, scaleX, plotOriginX, xAxisExtents[0]);
			gy = getValueScreen(this.gaussianPDF, scaleY, plotOriginY, yAxisExtents[0]);
		}
	}

	public void paintComponent (Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(foreground);
		if (this.axisOn == true){
			drawAxis(g);
		}
		drawPlot(g);
	}

	private void drawPlot (Graphics g) {
		int len = this.data.length;
		if (pdf_method == 0){
			for (int i = 0; i < len; i ++){
				g.drawOval(exsInt[i], whyInt[i], 1, 1);
			}
		}else{
			for (int i = 0; i < nonsamplenum; i ++){
				g.drawOval(exsInt[i], whyInt[i], 1, 1);
			}
		}

		g.setColor(Color.red);
		if(gauss == true){
			for (int i = 0; i < len; i ++){
				g.drawOval(gx[i], gy[i], 1, 1);
			}
		}
		g.setColor(Color.black);
	}


	private void drawAxis (Graphics g) {
		int plotWidth, plotHeight;
		plotWidth = (int)this.getSize().getWidth();
		plotHeight = (int)this.getSize().getHeight();
		g.setColor(foreground);
		g.drawLine(plotOriginX, plotEndY, plotOriginX, plotOriginY);
		g.drawLine(plotOriginX, plotOriginY, plotEndX, plotOriginY);
		// draw tick bars for scales on Y coordinate
		int fontSize;
		if (plotWidth < plotHeight){
			if (plotWidth < 300){
				fontSize = 9;
			} else {
				fontSize = (int)(plotWidth/32);
			}
		}else {
			if (plotHeight < 300){
				fontSize = 9;
			} else {
				fontSize = (int)(plotHeight/32);
			}
		}
		Font font = new Font("", Font.PLAIN, fontSize);
		g.setFont(font);
		//draw the labels on y axis (frequency).
		String scaleStringY;
		double barNumber = this.pdfDataArray.getTickNumber();
		double yBarDistance = ((plotOriginY - plotEndY)/barNumber);
			logger.finest("drawaxis: "+plotOriginY+" "+plotEndY+" "+yBarDistance+" "+barNumber);
		for (int i = 0; i <= barNumber; i++) {
			g.drawLine(plotOriginX - 3, plotEndY + (int)(i*yBarDistance), plotOriginX,
						plotEndY + (int)(i*yBarDistance));
			if (Math.abs(this.pdfDataArray.getMajorTick()) <= 1) {
					scaleStringY = Float.toString((float)(yAxisExtents[1] - i*this.pdfDataArray.getMajorTick()));
			}
			else {
					scaleStringY = Integer.toString((int)(yAxisExtents[1] - i*this.pdfDataArray.getMajorTick()));
			}
			g.drawString(scaleStringY, plotOriginX - (int)(plotWidth*AXISSPACEPORTION/2),
						plotEndY + (int)(i*yBarDistance + yBarDistance*1/6));
		}
		//draw the labels on x axis.
		//First tick.
		String scaleStringX;
		g.drawLine(plotOriginX, plotOriginY, plotOriginX, plotOriginY + 3);
		if (Math.abs(xAxisExtents[0]) <= 1) {
			scaleStringX = Float.toString((float)xAxisExtents[0]);
		} else {
			scaleStringX = Integer.toString((int)xAxisExtents[0]);
		}
		g.drawString(scaleStringX, plotOriginX - 3, plotOriginY + (int)(plotHeight*AXISSPACEPORTION/4));
		//Last tick.
		g.drawLine(plotEndX, plotOriginY, plotEndX, plotOriginY + 3);
		if (Math.abs(xAxisExtents[1]) <= 1) {
			scaleStringX = Float.toString((float)xAxisExtents[1]);
		} else {
			scaleStringX = Integer.toString((int)xAxisExtents[1]);
		}
		g.drawString(scaleStringX, plotEndX - 8, plotOriginY + (int)(plotHeight*AXISSPACEPORTION/4));
		font = new Font("", Font.PLAIN, fontSize + 3);
		g.setFont(font);
		//draw X axis attribute string
		//g.drawString(this.variableName, plotOriginX + (plotEndX - plotOriginX)/2 - plotWidth/12,
		//			plotOriginY + plotHeight/6 - 5);
		//draw Y axis attribute string. Need rotation for drawing the string vertically.
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(-Math.PI/2, plotOriginX - plotWidth/9, plotOriginY - (plotOriginY
					- plotEndY)/3);
		g2d.drawString("Density", plotOriginX - plotWidth/9, plotOriginY - (plotOriginY
					- plotEndY)/3);
		g2d.rotate(+Math.PI/2, plotOriginX - plotWidth/9, plotOriginY - (plotOriginY
					- plotEndY)/3);
	}

/**
 * Calculate scale between real data and integer data for showing up on screen.
 * @param min
 * @param max
 * @param dataMin
 * @param dataMax
 * @return scale
 */
private double getScale (int min, int max, double dataMin, double dataMax) {
	double scale;
	scale = (max - min)/(dataMax - dataMin);
	return  scale;
}
/**
 * Convert the numeric values of observations to integer value worked on screen.
 * @param dataArray
 * @param scale
 * @param min
 * @param dataMin
 * @return valueScreen
 */
private int[] getValueScreen (double[] dataArray, double scale, int min, double dataMin) {
	int[] valueScreen = new int[dataArray.length];
	for (int i = 0; i < dataArray.length; i++) {
		if (Double.isNaN(dataArray[i])) {
			valueScreen[i] = Integer.MIN_VALUE;
		}
		else {
			valueScreen[i] = (int)((dataArray[i] - dataMin)*scale + min);
		}
	}
	return  valueScreen;
}


	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {
		logger.finest("in component resized");
		this.setupDataforDisplay();
		//this.setAccumulativeFrequency();
		this.repaint();
	}

	public void componentShown(ComponentEvent e) {
	}

	private void nonsamplepdf() {
		int len = data.length;
		//add index
		if (dataWithIndex == null || dataWithIndex.length != data.length || dataWithIndex[0].length != 2) {
			dataWithIndex = new double[data.length][2];
		}
		for (int i = 0; i < len; i++) {
		  dataWithIndex[i][0] = data[i];
		  dataWithIndex[i][1] = i;
		}
		//now sort
	      ArraySort2D sorter = new ArraySort2D();
	      sorter.sortDouble(dataWithIndex,0);
		//this.pdfArray[0] = dataWithIndex[0][0];
		//this.pdfArray[len-1] = dataWithIndex[len-1][0];
		int minindex, maxindex;
		minindex=maxindex=0;
		double interval=(this.xAxisExtents[1]-this.xAxisExtents[0])/nonsamplenum;
		this.pdfArray=new double[nonsamplenum]; // y
		this.continuousData = new double[nonsamplenum]; // x
		for (int i = 0; i < nonsamplenum; i ++){
			this.continuousData[i]=(i+0.5)*interval+this.xAxisExtents[0];
			// find minimum
			while ((this.dataWithIndex[minindex][0]<this.continuousData[i]-this.delta)&&minindex<len-1) {
				minindex+=1;
			}
			// find maximum
			while ((this.dataWithIndex[maxindex][0]<this.continuousData[i]+this.delta)&&maxindex<len-1) {
				maxindex+=1;
			}
			pdfArray[i]=(maxindex-minindex)/(len*this.delta*2.0);
		}
		this.pdfDataArray = new DataArray(pdfArray);
		this.yAxisExtents = (double[])this.pdfDataArray.getExtent().clone();
	}

}