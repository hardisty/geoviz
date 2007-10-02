/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SpaceFillMatrixElement
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: SpaceFillMatrixElement.java,v 1.5 2005/01/04 19:17:20 xpdai Exp $
 $Date: 2005/01/04 19:17:20 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package  edu.psu.geovista.app.spacefill;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JToolBar;

import edu.psu.geovista.app.matrix.MatrixElement;
import edu.psu.geovista.common.data.DataSetForApps;

public class SpaceFillMatrixElement extends SpaceFillCanvas
                                                  implements MatrixElement {

        //the following are required for returning to matrix
        private int[] elementPosition;
        private double[] xAxisExtents;
        private double[] yAxisExtents;
        private DataSetForApps dataSet;
		private Color selectionColor;

        public SpaceFillMatrixElement() {
          super();
          super.setUseDrawingShapes(false);
          this.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
        }

        /**
         * @param data
         * 
         * This method is deprecated becuase it wants to create its very own pet
         * DataSetForApps. This is no longer allowed, to allow for a mutable, 
         * common data set. Use of this method may lead to unexpected
         * program behavoir. 
         * Please use setDataSet instead.
         */
        @Deprecated
        public void setDataObject(Object[] data) {
      	 this.setDataSet(new DataSetForApps(data));
          
        }
        public void setDataSet(DataSetForApps data){
          this.dataSet = data;
          super.setDataSet(data);
        }

	public void setElementPosition(int[] dataIndices){
            this.elementPosition = (int[])dataIndices.clone();
            super.setCurrOrderColumn(this.elementPosition[0]);//order = x
            super.setCurrColorColumn(this.elementPosition[1]);//color = y


        }

        public int[] getElementPosition(){
          return this.elementPosition;
        }

	//For axes of scatter plot.
        //a noop for this class
	public void setAxisOn (boolean axisOn){
         }




	//Set min and max for axes. xAxisExtents[0] = min, [1] = max.
	public void setXAxisExtents (double[] xAxisExtents){

        }

	public void setYAxisExtents (double[] yAxisExtents){  }

	public double[] getXAxisExtents () {
          return this.xAxisExtents;
        }

	public double[] getYAxisExtents () {
          return this.yAxisExtents;
        }

	public String getShortDiscription () {
	    return "SFP";
	}
    //public void setBivarColorClasser (BivariateColorSymbolClassification bivarColorClasser) {
      //this.bivarColorClasser = bivarColorClasser;
      //this.sendColorsToLayers(this.dataColorX.length);
    //}

	public void setSelectionColor (Color c){
	    this.selectionColor = c;
            super.setColorSelection(c);
	}

	public Color getSelectionColor (){
	    return this.selectionColor;
	}

	public void setMultipleSelectionColors (Color[] c){
	}

	public void setColorArrayForObs (Color[] c){
	}

        public JToolBar getTools (){
          return null;
        }
    /**
     * This method only paints the current contents of the drawingBuff.
     * @param g
     */
	public void paintComponent (Graphics g) {
            super.paintComponent(g);

            if (this.elementPosition == null) {
              return;
            }

            if (this.elementPosition[0] == this.elementPosition[1]) {
              Graphics2D g2 = (Graphics2D)g;
              Color half = new Color(255,255,255,150);
              g2.setColor(half);
              Font font = new Font("Serif",Font.PLAIN,24);
              g2.setFont(font);
              FontRenderContext frc = g2.getFontRenderContext();

              String[] varNames = this.dataSet.getAttributeNamesNumeric();//XXX right method call?
              String varName = varNames[this.elementPosition[0]-1];//-1 to skip string array

              int midX = 10;
              int midY = this.getHeight()/2;

              Rectangle2D textBounds = g2.getFont().getStringBounds(varName,frc);
              Rectangle rect = textBounds.getBounds();
              rect.setSize(rect.width,(int)(rect.height * 1.5));
              rect.setLocation(midX,midY - (int)(rect.getHeight()/1.5));
              //Rectangle rect = new Rectangle();
              rect.setLocation(0,0);
              rect.setSize(this.getWidth(),this.getHeight());
              g2.fill(rect);
              //g2.fillRect(0,0,this.getWidth(),this.getHeight());
              g2.setColor(Color.black);



              //g2.drawString(varName,midX,midY);
            }
        }

}
