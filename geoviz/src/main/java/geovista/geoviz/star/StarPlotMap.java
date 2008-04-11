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
package geovista.geoviz.star;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JPanel;

import geovista.common.data.DataSetForApps;
import geovista.common.event.ColorArrayEvent;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetModifiedEvent;
import geovista.common.event.IndicationEvent;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.geoviz.map.GeoMap;
import geovista.geoviz.sample.GeoDataGeneralizedStates;
import geovista.geoviz.visclass.VisualClassifier;
import geovista.symbolization.glyph.Glyph;
import geovista.symbolization.glyph.GlyphEvent;
import geovista.symbolization.glyph.GlyphListener;

/**
 * Paints an array of StarPlotMap. Responds to and broadcasts DataSetChanged,
 * IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * 
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

	boolean settingUp = true;

	public StarPlotMap() {
		super();
		starColorer = new VisualClassifier();
		starLayer = new StarPlotLayer();
		starLeg = new StarPlotLegend();

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(starLeg, BorderLayout.CENTER);
		southPanel.add(starColorer, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		starColorer.addColorArrayListener(this);

	}

	public void glyphChanged(GlyphEvent e) {
		// this.mapCan.glyphChanged(e);
	}

	@Override
	public void dataSetChanged(DataSetEvent e) {
		settingUp = true;
		super.dataSetChanged(e);
		data = e.getDataSetForApps();
		starLayer.dataSetChanged(e);
		starColorer.setDataSet(e.getDataSetForApps());
		setLegendIndication(0);
		int nNumericVars = e.getDataSetForApps().getNumberNumericAttributes();
		if (nNumericVars > 6) {
			nNumericVars = 6;
		}
		int[] selectedVars = new int[nNumericVars];
		for (int i = 0; i < nNumericVars; i++) {
			selectedVars[i] = i;
		}
		SubspaceEvent subE = new SubspaceEvent(this, selectedVars);
		subspaceChanged(subE);
		mapCan.setGlyphs(createGlyphs());
		settingUp = false;
	}

	private Glyph[] createGlyphs() {

		Rectangle[] plotLocs = new Rectangle[data.getNumObservations()];

		for (int i = 0; i < plotLocs.length; i++) {
			plotLocs[i] = new Rectangle(defaultGlyphSize, defaultGlyphSize);
		}

		starLayer.setPlotLocations(plotLocs);

		Color[] starColors = starColorer.findDataColors();
		starLayer.setStarFillColors(starColors);
		return starLayer.findGlyphs();
	}

	public void dataSetModified(DataSetModifiedEvent e) {

	}

	public void subspaceChanged(SubspaceEvent e) {
		starLayer.subspaceChanged(e);

		mapCan.setGlyphs(createGlyphs());

		String[] varNames = starLayer.getVarNames();
		starLeg.setVariableNames(varNames);
		double[] values = starLayer.getValues(indication);
		starLeg.setValues(values);

		setLegendIndication(indication);
	}

	@Override
	public void colorArrayChanged(ColorArrayEvent e) {
		if (StarPlotMap.logger.isLoggable(Level.FINEST)) {
			logger.finest("in starplotmap colorarraychanged, got colors");
		}
		if (e == null || e.getColors() == null) {
			return;
		}
		Color[] starColors = e.getColors();
		starLayer.setStarFillColors(starColors);
		mapCan.setGlyphs(starLayer.findGlyphs());// XXX should not
		// redo glyphs here
	}

	@Override
	public void indicationChanged(IndicationEvent e) {
		if (settingUp) {
			return;
		}

		int ind = e.getIndication();
		if (ind > starLayer.getDataSet().getNumObservations()) {
			logger.severe("got indication greater than data set size, ind = "
					+ ind);
			return;
		}
		if (e.getSource() != starLayer) {
			starLayer.indicationChanged(e);
		}
		setLegendIndication(ind);
		super.indicationChanged(e);

	}

	private void setLegendIndication(int ind) {
		if (ind >= 0) {
			starLeg.setObsName(starLayer.getObservationName(ind));
			String[] varNames = starLayer.getVarNames();
			double[] values = starLayer.getValues(ind);
			int[] spikeLengths = starLayer.getSpikeLengths(ind);
			// we need this check, or we start blowing null pointer exceptions
			// if the we get an indication during iniditalization.
			if (spikeLengths == null) {
				return;
			}
			starLeg.setValues(values);
			starLeg.setVariableNames(varNames);

			starLeg.setSpikeLengths(spikeLengths);
			Color starColor = starLayer.getStarFillColor(ind);
			starLeg.setStarFillColor(starColor);

		}

	}

	public static void main(String[] args) {
		StarPlotMap map = new StarPlotMap();
		JFrame frame = new JFrame("StarPlot Map");
		frame.add(map);
		frame.pack();
		frame.setVisible(true);
		GeoDataGeneralizedStates geodata = new GeoDataGeneralizedStates();
		DataSetEvent e = new DataSetEvent(geodata.getDataForApps(), geodata);
		map.dataSetChanged(e);

	}

} // end class
