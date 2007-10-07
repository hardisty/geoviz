/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotMatrixElement
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotMatrixElement.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
 $Date: 2005/02/13 03:26:27 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */
package geovista.matrix.star;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JToolBar;

import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.geoviz.star.StarPlotCanvas;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import geovista.common.data.DataSetForApps;
import geovista.matrix.MatrixElement;

/**
 * Suitable for inclusion into multiform matrixes
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class StarPlotMatrixElement extends StarPlotCanvas implements
		MatrixElement {
	DataSetForApps data;

	int[] elementPosition;

	
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
	  public void setData(Object[] data) {
		 this.setDataSet(new DataSetForApps(data));
	    
	  }	
		public void setDataSet(DataSetForApps data) {
			DataSetEvent e = new DataSetEvent(data,this);
			super.dataSetChanged(e);

			
		}


	public void setElementPosition(int[] dataIndices) {
		this.elementPosition = dataIndices;
		// xxx update
	}

	public int[] getElementPosition() {
		return this.elementPosition;
	}

	// For axes of scatter plot.
	// a noop for this class
	public void setAxisOn(boolean axisOn) {
	};

	public void setSelectionColor(Color c) {
		// xxx implement me
	}

	public void setSelOriginalColorMode(boolean selOriginalColorMode) {
		// xxx implement me?
	}

	public void setMultipleSelectionColors(Color[] c) {
		// xxx implement me
	}

	public void setColorArrayForObs(Color[] c) {
		// xxx implement me
	}

	public Vector getSelectedObservations() {
		// xxx implement me
		return null;
	}

	public void setIndication(int indication) {
		// xxx implement me
	}

	public void addIndicationListener(IndicationListener ind) {
		// xxx implement me
	}

	public void removeIndicationListener(IndicationListener ind) {
		// xxx implement me
	}

	public void setSelectedObservations(Vector selectedObservations) {
		// xxx implement me
	}

	public void setSelections(int[] selections) {
		// xxx implement me
	}

	public int[] getSelections() {
		// xxx implement me
		return null;
	}

	public void setConditionArray(int[] conditionArray) {
		// xxx implement me
	}

	public void addActionListener(ActionListener l) {
		// xxx implement me
	}

	// Set min and max for axes. xAxisExtents[0] = min, [1] = max.
	public void setXAxisExtents(double[] xAxisExtents) {
		// xxx implement me
	}

	public void setYAxisExtents(double[] yAxisExtents) {
		// xxx implement me
	}

	public double[] getXAxisExtents() {
		// noop
		return null;
	}

	public double[] getYAxisExtents() {
		// noop
		return null;
	}

	public JToolBar getTools() {
		return null;

	}

	public void setBivarColorClasser(
			BivariateColorSymbolClassification bivarColorClasser,
			boolean reverseColor) {
		// super isn't really a bivariate component, so let's handle this here
		double[] data = this.data
				.getNumericDataAsDouble(this.elementPosition[0]);
		Color[] obsColors = bivarColorClasser.symbolize(data, data);
		super.setStarFillColors(obsColors);

	}

	public BivariateColorSymbolClassification getBivarColorClasser() {
		// we don't really use this.
		return null;
	}

	public String getShortDiscription() {
		return "STARPLOT";
	}



} // end class
