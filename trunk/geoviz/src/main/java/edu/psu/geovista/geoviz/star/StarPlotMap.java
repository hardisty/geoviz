/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class StarPlotMap
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: StarPlotMap.java,v 1.1 2005/11/04 19:23:11 hardisty Exp $
 $Date: 2005/11/04 19:23:11 $
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
import java.awt.Rectangle;
import java.util.logging.Level;

import javax.swing.JPanel;

import edu.psu.geovista.app.map.GeoMap;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.ColorArrayEvent;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetModifiedEvent;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.SubspaceEvent;
import edu.psu.geovista.common.event.SubspaceListener;
import edu.psu.geovista.symbolization.glyph.Glyph;
import edu.psu.geovista.symbolization.glyph.GlyphEvent;
import edu.psu.geovista.symbolization.glyph.GlyphListener;
import edu.psu.geovista.visclass.VisualClassifier;

/**
 * Paints an array of StarPlotMap. Responds to and broadcasts DataSetChanged,
 * IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * @version $Revision: 1.1 $
 */
public class StarPlotMap extends GeoMap implements GlyphListener,
		SubspaceListener {

	StarPlotLegend starLeg;

	StarPlotLayer starLayer;

	VisualClassifier starColorer;

	int indication;

	int defaultGlyphSize = 5;

	DataSetForApps data;

	static boolean DEBUG = true;

	public StarPlotMap() {
		super();
		starColorer = new VisualClassifier();
		starLayer = new StarPlotLayer();
		starLeg = new StarPlotLegend();

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(starLeg, BorderLayout.CENTER);
		southPanel.add(starColorer, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		this.starColorer.addColorArrayListener(this);

	}

	public void glyphChanged(GlyphEvent e) {
		// this.mapCan.glyphChanged(e);
	}

	public void dataSetChanged(DataSetEvent e) {
		super.dataSetChanged(e);
		this.data = e.getDataSetForApps();
		this.starLayer.dataSetChanged(e);
		this.starColorer.setDataSet(e.getDataSetForApps());
		this.setLegendIndication(0);
		int nNumericVars = e.getDataSetForApps().getNumberNumericAttributes();
		if (nNumericVars > 6) {
			nNumericVars = 6;
		}
		int[] selectedVars = new int[nNumericVars];
		for (int i = 0; i < nNumericVars; i++) {
			selectedVars[i] = i;
		}
		SubspaceEvent subE = new SubspaceEvent(this, selectedVars);
		this.subspaceChanged(subE);
		this.mapCan.setGlyphs(this.createGlyphs());

	}

	private Glyph[] createGlyphs() {

		Rectangle[] plotLocs = new Rectangle[data.getNumObservations()];

		for (int i = 0; i < plotLocs.length; i++) {
			plotLocs[i] = new Rectangle(this.defaultGlyphSize,
					this.defaultGlyphSize);
		}

		starLayer.setPlotLocations(plotLocs);

		Color[] starColors = this.starColorer.findDataColors();
		starLayer.setStarFillColors(starColors);
		return starLayer.findGlyphs();
	}

	public void dataSetModified(DataSetModifiedEvent e) {

	}

	public void subspaceChanged(SubspaceEvent e) {
		this.starLayer.subspaceChanged(e);

		this.mapCan.setGlyphs(this.createGlyphs());

		String[] varNames = this.starLayer.getVarNames();
		this.starLeg.setVariableNames(varNames);
		double[] values = this.starLayer.getValues(indication);
		this.starLeg.setValues(values);

		this.setLegendIndication(indication);
	}

	public void colorArrayChanged(ColorArrayEvent e) {
		if (StarPlotMap.logger.isLoggable(Level.FINEST)) {
			logger.finest("in starplotmap colorarraychanged, got colors");
		}
		if (e == null || e.getColors() == null){
			return;
		}
		Color[] starColors = e.getColors();
		this.starLayer.setStarFillColors(starColors);
		this.mapCan.setGlyphs(this.starLayer.findGlyphs());// XXX should not
															// redo glyphs here
	}

	public void indicationChanged(IndicationEvent e) {

		if (e.getSource() != this.starLayer) {
			this.starLayer.indicationChanged(e);
		}
		int ind = e.getIndication();
		this.setLegendIndication(ind);
		super.indicationChanged(e);

	}

	private void setLegendIndication(int ind) {
		if (ind >= 0) {
			this.starLeg.setObsName(starLayer.getObservationName(ind));
			String[] varNames = this.starLayer.getVarNames();
			double[] values = this.starLayer.getValues(ind);
			int[] spikeLengths = this.starLayer.getSpikeLengths(ind);
			//we need this check, or we start blowing null pointer exceptions
			//if the we get an indication during iniditalization.
			if (spikeLengths == null){
				return;
			}
			this.starLeg.setValues(values);
			this.starLeg.setVariableNames(varNames);

			this.starLeg.setSpikeLengths(spikeLengths);
			Color starColor = this.starLayer.getStarFillColor(ind);
			this.starLeg.setStarFillColor(starColor);

		}

	}



} // end class
