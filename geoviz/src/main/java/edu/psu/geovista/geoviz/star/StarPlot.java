/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlot
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlot.java,v 1.4 2006/02/17 17:21:23 hardisty Exp $
 $Date: 2006/02/17 17:21:23 $
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
package edu.psu.geovista.geoviz.star;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import edu.psu.geovista.geoviz.visclass.VisualClassifier;
import geovista.common.event.ColorArrayEvent;
import geovista.common.event.ColorArrayListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.DataSetModifiedEvent;
import geovista.common.event.DataSetModifiedListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;

/**
 * Paints an array of StarPlot. Responds to and broadcasts DataSetChanged,
 * IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.4 $
 */
public class StarPlot extends JPanel implements DataSetListener,
		DataSetModifiedListener, IndicationListener, SubspaceListener,
		ColorArrayListener, TableModelListener {

	StarPlotCanvas starCan;
	StarPlotLegend starLeg;
	VisualClassifier vc;
	int indication;
	final static Logger logger = Logger.getLogger(StarPlot.class.getName());

	public StarPlot() {
		vc = new VisualClassifier();
		starCan = new StarPlotCanvas();
		starLeg = new StarPlotLegend();
		this.setLayout(new BorderLayout());
		this.add(starCan, BorderLayout.CENTER);
		this.add(starLeg, BorderLayout.SOUTH);
		this.add(vc, BorderLayout.NORTH);
		this.starCan.addIndicationListener(this);
		this.vc.addColorArrayListener(this);
	}

	public void dataSetChanged(DataSetEvent e) {
		e.getDataSetForApps().addTableModelListener(this);
		this.starCan.dataSetChanged(e);
		this.vc.setDataSet(e.getDataSetForApps());

		Color[] starColors = this.vc.getColorForObservations();
		this.starCan.setStarFillColors(starColors);
		int nNumericVars = starCan.getDataSet().getNumberNumericAttributes();
		if (nNumericVars > 6) {
			nNumericVars = 6;
		}
		int[] selectedVars = new int[nNumericVars];
		for (int i = 0; i < nNumericVars; i++) {
			selectedVars[i] = i;
		}
		SubspaceEvent subE = new SubspaceEvent(this, selectedVars);
		this.subspaceChanged(subE);
		this.setLegendIndication(0);
	}

	public void dataSetModified(DataSetModifiedEvent e) {

	}

	public void subspaceChanged(SubspaceEvent e) {
		this.starCan.subspaceChanged(e);
		this.setLegendIndication(indication);
	}

	public void colorArrayChanged(ColorArrayEvent e) {
		Color[] starColors = e.getColors();
		this.starCan.setStarFillColors(starColors);
	}

	public void indicationChanged(IndicationEvent e) {

		if (e.getSource() != this.starCan) {
			this.starCan.indicationChanged(e);
		}
		int ind = e.getIndication();
		this.setLegendIndication(ind);

	}

	private void setLegendIndication(int ind) {
		if (ind >= 0) {
			this.starLeg.setObsName(starCan.getObservationName(ind));
			String[] varNames = this.starCan.getVarNames();
			double[] values = this.starCan.getValues(ind);
			if (values == null) {
				return;
			}
			int[] spikeLengths = this.starCan.getSpikeLengths(ind);
			this.starLeg.setValues(values);
			this.starLeg.setVariableNames(varNames);
			if (spikeLengths == null){
				return;
			}
			this.starLeg.setSpikeLengths(spikeLengths);
			Color starColor = this.starCan.getStarFillColor(ind);
			if (starColor == null) {
				return;
			}
			this.starLeg.setStarFillColor(starColor);

		}

	}

	/**
	 * adds an IndicationListener to the component
	 */
	public void addIndicationListener(IndicationListener l) {
		this.starCan.addIndicationListener(l);
	}

	/**
	 * removes an IndicationListener from the component
	 */
	public void removeIndicationListener(IndicationListener l) {
		this.starCan.removeIndicationListener(l);
	}

	public void tableChanged(TableModelEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Starplot, got a data set, id = "
					+ this.starCan.getDataSet().hashCode());
		}
		this.vc.setDataSet(this.starCan.getDataSet());
		
	}



} // end class
