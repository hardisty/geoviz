package  geovista.matrix.scatterplot;

/**
 * Title: ScatterPlot
 * Description: construct a scatterplot
 * Copyright:    Copyright (c) 2001
 * Company: GeoVISTA Center
 * @author Xiping Dai
 * @author Jin Chen
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Graphics;

import geovista.symbolization.BivariateColorSymbolClassification;

/**
 * put your documentation comment here
 */
public class ScatterPlot extends ScatterPlotBasic

{
    public static final Color COLOR_NOSELECTED=new Color(204,204,204);
  transient protected ScatterPlot detailSP;
  transient boolean externalColor = false;


        /**
        * put your documentation comment here
        */
	public ScatterPlot () {
		super();
	}

    /**
     * put your documentation comment here
     * @param 	String attributeX
     * @param 	String attributeY
     * @param 	double[] dataX
     * @param 	double[] dataY
     * @param 	boolean axisOn
     * @param   boolean plotLine
     * @param   double slope
     * @param   double intercept
     */
	public ScatterPlot (Object[] dataObject, int[] dataIndices, boolean axisOn, Color c) {
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

		this.axisOn = axisOn;

                this.background = c;
		if (c == Color.black)
			this.foreground = Color.white;
		else
			this.foreground = Color.black;
		initialize();
	}

	public void setColorArrayForObs(Color[] colorArray){
		//this.colorArrayForObs = colorArray;
              if (colorArray != null){
                  this.pointColors = colorArray;
                  this.externalColor = true;
              }
                if (this.pointColors == null){
                  logger.finest("In SP... pointColors null");
                }
        this.repaint();
	}


	public Color[] getColors(){
		return this.pointColors;
	}

	public String getShortDiscription(){
	    return "XYP";
	}
    /**
     * Set up data and axis for drawing the scatter plot.
     */
	protected void initialize () {
          super.initialize();
          //added for colors
          if (this.externalColor == false){
            this.makeColors();
          }

	}

//    /**
//     * Draw pot (points) on the screen.
//     * @param g
//     */
//	protected void drawPlot (Graphics g) {
//          super.drawPlot(g);
//		if (this.dataIndices[0] != this.dataIndices[1]) {
//                  if (this.multipleSelectionColors != null){
//                    for (int i = 0; i < this.dataX.length; i++) {
//                      if (this.multipleSelectionColors[i] != null){
//                        g.setColor(multipleSelectionColors[i]);
//                        g.drawOval(exsint[i] - 1, whyint[i] - 1, pointSize, pointSize);
//                        g.fillOval(exsint[i] - 1, whyint[i] -1 , pointSize, pointSize);
//                      }
//                    }
//                  }
//
//		}
//	}

	protected void drawSlections(Graphics g, Color[] colorNonSelected, int len){
          if (colorNonSelected!=null&& colorNonSelected.length != len){
            return;
          }
		if (this.pointSelected == false){ //only draw original points.
			for (int i = 0; i < len; i++) {
				if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
					&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){
                                      if (colorNonSelected != null)
					g.setColor(colorNonSelected[i]);
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

						g.setColor(colorNonSelected[i]);
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
                                            /*if (colorNonSelected != null) {
                                                final Color c = colorNonSelected[i];

                                                g.setColor(c);
                                            }*/
                        g.setColor(new Color(61,3,87));//distinguish selected and non-selected
						g.drawOval(exsint[i] - 2, whyint[i] - 2, pointSize-2, pointSize-2);
					}
				}
				for (int i = 0; i < len; i++) {
					if ((exsint[i] <= this.plotEndX) && (exsint[i] >= plotOriginX) && (whyint[i] <= plotOriginY)
							&& (whyint[i] >= plotEndY) && (conditionArray[i] > -1)){
                                              if (colorNonSelected != null)
                                                   g.setColor(colorNonSelected[i]);
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

	/**
	 * Sets colors for the current data.
	 */
     public void setBivarColorClasser (BivariateColorSymbolClassification bivarColorClasser, boolean reverseColor) {
       this.bivarColorClasser = bivarColorClasser;
       this.makeColors();
	  repaint();
    }

    public BivariateColorSymbolClassification getBivarColorClasser(){
      return this.bivarColorClasser;
    }

    public void makeColors(){
      if (this.dataX != null && this.dataY != null) {
        this.pointColors = this.bivarColorClasser.symbolize(dataX,dataY);
      }
    }


}



