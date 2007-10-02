package  edu.psu.geovista.app.scatterplot;

/**
 * Title: ScatterPlot
 * Description: construct a scatterplot
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA Center
 * @author Xiping Dai
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JDialog;
import javax.swing.JFrame;

import edu.psu.geovista.common.classification.BoundaryClassifier;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;

/**
 * put your documentation comment here
 */
public class ScatterPlotWithBackground extends ScatterPlotBasic
{
  transient protected BoundaryClassifier xClasser = null;
  transient protected BoundaryClassifier yClasser = null;
  transient protected double[] xBoundaries;
  transient protected double[] yBoundaries;
  transient protected int[] xBoundariesInt;
  transient protected int[] yBoundariesInt;
  transient protected Color[][] classColors;
  transient protected ScatterPlotWithBackground detailSP;
  transient protected JFrame dummyFrame;
  transient protected JDialog dlgSP;

    /**
     * put your documentation comment here
     */
	public ScatterPlotWithBackground () {
		super() ;
	}

    /**
     * put your documentation comment here
     * @param 	String attributeX
     * @param 	String attributeY
     * @param 	double[] dataX
     * @param 	double[] dataY
     * @param 	boolean axisOn
     */
	public ScatterPlotWithBackground (Object[] dataObject, int[] dataIndices, boolean axisOn, Color c) {
          super();
		this.dataObject = dataObject;
		this.attributeArrays = (String[])dataObject[0];
		int len = attributeArrays.length;
		if (dataObject[len + 1] == null) {
			this.observNames = null;
		}
		else {
			this.observNames = (String[])dataObject[len + 1];
		}
		this.dataIndices = (int[])dataIndices;
		//convert Object array to double arrays.
		axisDataSetup ();
		//initialize();
		this.axisOn = axisOn;
		this.background = c;
		if (c == Color.black)
			this.foreground = Color.white;
		else
			this.foreground = Color.black;
		initialize();
	}

	public String getShortDiscription(){
	    return "XYP";
	}
    /**
     * Set up data and axis for drawing the scatter plot.
     */
	protected void initialize () {
          super.initialize();
          //Set up class boundaries, which will be plotted at the background.
          this.makeBoundaries();
	}

     /**
     * Draw the scatter plot.
     * @param g
     */
	public void paintComponent (Graphics g) {

		if (this.dataIndices == null)
			return;
		g.setColor(background);
		g.fillRect(0, 0, getSize().width, getSize().height);
		g.setColor(foreground);
		this.paintBorder(g);

		if (axisOn) {
			drawAxis(g);
		}
		drawPlot(g);
		Graphics2D g2 = (Graphics2D)g;
		if (exLabels != null && this.axisOn == true) {
			this.setToolTipText("");
			exLabels.paint(g2, getBounds());
		}
                // add by Jin Chen for indication
                if(indiationId>=0){
                  this.drawIndication(g2,indiationId);
                }

	}

    /**
     * Draw pot (points) on the screen.
     * @param g
     */
	protected void drawPlot (Graphics g) {

                  this.drawClassBackground(g);
                  super.drawPlot(g);
			if (this.multipleSelectionColors != null){
				for (int i = 0; i < this.dataX.length; i++) {
					if (this.multipleSelectionColors[i] != null){
						//g.setColor(multipleSelectionColors[i]);
                                                g.setColor(this.foreground);
						g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
						//g.fillOval(exsint[i] - 1, whyint[i] -1 , pointSize, pointSize);
					}
				}
                        }

	}

    protected void drawClassBackground(Graphics g){
          //draw background in colors.
        if (this.xClasser != null){
          logger.finest("classer is not null");
          for(int i = 0; i < this.classColors.length; i ++){
            for(int j = 0; j < this.classColors[0].length; j ++){
              g.setColor(this.classColors[i][j]);
              g.fillRect(this.xBoundariesInt[i], this.yBoundariesInt[j+1],
                         this.xBoundariesInt[i+1]-this.xBoundariesInt[i], this.yBoundariesInt[j]-this.yBoundariesInt[j+1]);
            }
          }
        }

    }

	protected void drawSlections(Graphics g, Color[] colorNonSelected, int len){
		if (this.pointSelected == false){ //only draw original points.
			for (int i = 0; i < len; i++) {
				if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
					&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){
                                      g.setColor(this.foreground);
					//g.setColor(colorNonSelected[i]);
                                        //g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
					g.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
				}
			}
		}else{  //draw original points and selected points.
			//according to the color mode, draw selected points and non-selected points.
			if (this.selOriginalColorMode == false){
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
						&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){
                                              g.setColor(this.foreground);
						//g.setColor(colorNonSelected[i]);
							//g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
						g.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
					}
				}
				for (int i = 0; i < len; i++) {
					g.setColor(this.selectionColor);
					if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
						&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){

						if (this.selections[i] == 1){
							//g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
							g.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
						}
					}
				}
			}else{
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){
                                                      g.setColor(this.foreground);
						//g.setColor(colorNonSelected[i]);
						g.drawOval(exsint[i] - 2, whyint[i] - 2, pointSize-2, pointSize-2);
					}
				}
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){
						//g.setColor(colorNonSelected[i]);
						g.setColor(this.foreground);
                                                if (this.selections[i] == 1){
								//g2.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
								g.fillOval(exsint[i] - 2, whyint[i] - 2, pointSize, pointSize);
								//g2.fillRect(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
								//g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize+1, pointSize+1);
						}
					}
				}
			}
		}
	}

	protected void setupDataforDisplay(){

		logger.finest("In setup data for display ..." + xAxisExtents[0]);
		    this.setVisibleAxis(axisOn);
		    if (dataArrayX == null) return;
			int len = dataArrayX.length();
			if (len != dataArrayY.length())
				return;
			//exsint = new int[len];
			//whyint = new int[len];
            //get positions on screen
			double xScale;
			double yScale;
			xScale = getScale(plotOriginX, plotEndX, xAxisExtents[0], xAxisExtents[1]);
			exsint = getValueScreen(dataX, xScale, plotOriginX, xAxisExtents[0]);
			yScale = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
			whyint = getValueScreen(dataY, yScale, plotOriginY, yAxisExtents[0]);
			//get class boundaries' positions on screen
			if(this.xBoundaries != null && this.yBoundaries != null){
				logger.finest("x and y boundaries are not null.");
				this.xBoundariesInt = new int[this.xBoundaries.length];
				this.yBoundariesInt = new int[this.yBoundaries.length];
				this.xBoundariesInt = getValueScreen(this.xBoundaries, xScale, plotOriginX, xAxisExtents[0]);
				this.yBoundariesInt = getValueScreen(this.yBoundaries, yScale, plotOriginY, yAxisExtents[0]);
			}
	}


	/**
	 * Sets colors for the current data.
	 */
     public void setBivarColorClasser (BivariateColorSymbolClassification bivarColorClasser, boolean reverseColor) {
       this.bivarColorClasser = bivarColorClasser;
       this.makeColors();
	//this.classColors = this.bivarColorClasser.getClassColors();
	this.makeBoundaries();
	repaint();
    }

    public BivariateColorSymbolClassification getBivarColorClasser(){
      return this.bivarColorClasser;
    }

    protected void makeBoundaries (){
      int numClasses;
        try {
         if (this.dataX != null){
           xClasser = (BoundaryClassifier)this.bivarColorClasser.getClasserX();
           numClasses = this.bivarColorClasser.getXColorSymbolizer().getNumClasses();
           logger.finest("num classes" + numClasses);
           xBoundaries = xClasser.getBoundaries(this.dataX, numClasses);
           yClasser = (BoundaryClassifier)this.bivarColorClasser.getClasserY();
           numClasses = this.bivarColorClasser.getYColorSymbolizer().getNumClasses();
           yBoundaries = yClasser.getBoundaries(this.dataY, numClasses);
           this.setupDataforDisplay();
         }
       }
       catch (ClassCastException ex) {
       }
    }


    public void makeColors(){
      this.classColors = this.bivarColorClasser.getClassColors();
      if (this.dataX != null && this.dataY != null) {
        this.pointColors = this.bivarColorClasser.symbolize(dataX,dataY);
      }
    }

}



