/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GraduatedSymbolsMap
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GraduatedSymbolsMap.java,v 1.1 2005/11/04 19:23:11 hardisty Exp $
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
package geovista.geoviz.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

import geovista.common.data.DataSetForApps;
import geovista.common.event.ColorArrayEvent;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetModifiedEvent;
import geovista.common.event.IndicationEvent;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.geoviz.sample.GeoDataGeneralizedStates;
import geovista.geoviz.visclass.VisualClassifier;
import geovista.symbolization.glyph.Glyph;
import geovista.symbolization.glyph.GlyphComboBox;
import geovista.symbolization.glyph.GlyphEvent;
import geovista.symbolization.glyph.GlyphListener;
import geovista.symbolization.glyph.GlyphSizePicker;

/**
 * Paints an array of GraduatedSymbolsMap. Responds to and broadcasts
 * DataSetChanged, IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * 
 */
public class GraduatedSymbolsMap extends GeoMap implements GlyphListener,
		SubspaceListener {

	NGonGroup ngonLayer;

	VisualClassifier ngonColorer;

	int indication;

	int defaultGlyphSize = 50;

	DataSetForApps data;

	static boolean DEBUG = true;

	boolean settingUp = true;
	GlyphComboBox glyphCombo;
	GlyphSizePicker glyphPick;

	public GraduatedSymbolsMap() {
		super();
		ngonColorer = new VisualClassifier();
		ngonLayer = new NGonGroup();

		JPanel southPanel = new JPanel();
		southPanel.setBorder(BorderFactory.createLineBorder(Color.red));
		JPanel southGroupPanel = new JPanel();
		southGroupPanel.setBorder(BorderFactory.createLineBorder(Color.cyan));
		southGroupPanel.setLayout(new BoxLayout(southGroupPanel,
				BoxLayout.Y_AXIS));
		glyphPick = new GlyphSizePicker();
		southGroupPanel.add(glyphPick);
		southGroupPanel.add(ngonColorer);
		southPanel.add(southGroupPanel);

		glyphCombo = new GlyphComboBox();
		southPanel.add(glyphCombo);
		glyphCombo.glyphList.addActionListener(this);
		this.add(southPanel, BorderLayout.SOUTH);
		ngonColorer.addColorArrayListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (glyphCombo == null) {
			return;
		}
		if (e.getSource().equals(glyphCombo.glyphList)) {
			int nSides = glyphCombo.glyphList.getSelectedIndex()
					+ GlyphComboBox.PLUS_FACTOR;
			setNSides(nSides);
			glyphPick.setNSides(nSides);

		}

	}

	private void setNSides(int sides) {
		ngonLayer.setNSides(sides);
		mapCan.setGlyphs(ngonLayer.getNGons());
	}

	public void glyphChanged(GlyphEvent e) {
		// this.mapCan.glyphChanged(e);
	}

	@Override
	public void dataSetChanged(DataSetEvent e) {
		settingUp = true;
		super.dataSetChanged(e);
		data = e.getDataSetForApps();
		ngonLayer.dataSetChanged(e);
		ngonColorer.setDataSet(e.getDataSetForApps());
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

		Glyph[] sbs = createGlyphs();
		mapCan.setGlyphs(sbs);
		settingUp = false;
	}

	private Glyph[] createGlyphs() {

		Rectangle[] plotLocs = new Rectangle[data.getNumObservations()];

		for (int i = 0; i < plotLocs.length; i++) {
			plotLocs[i] = new Rectangle(defaultGlyphSize, defaultGlyphSize);
		}

		ngonLayer.setPlotLocations(plotLocs);

		Color[] starColors = ngonColorer.findDataColors();
		ngonLayer.setStarFillColors(starColors);
		return ngonLayer.findGlyphs();
	}

	public void dataSetModified(DataSetModifiedEvent e) {

	}

	public void subspaceChanged(SubspaceEvent e) {
		ngonLayer.subspaceChanged(e);

		mapCan.setGlyphs(createGlyphs());

		String[] varNames = ngonLayer.getVarNames();

		double[] values = ngonLayer.getValues(indication);

		setLegendIndication(indication);
	}

	@Override
	public void colorArrayChanged(ColorArrayEvent e) {
		if (GraduatedSymbolsMap.logger.isLoggable(Level.FINEST)) {
			logger
					.finest("in GraduatedSymbolsMap colorarraychanged, got colors");
		}
		if (e == null || e.getColors() == null) {
			return;
		}
		Color[] starColors = e.getColors();
		ngonLayer.setStarFillColors(starColors);
		mapCan.setGlyphs(ngonLayer.findGlyphs());// XXX should not
		// redo glyphs here
	}

	@Override
	public void indicationChanged(IndicationEvent e) {
		if (settingUp) {
			return;
		}

		int ind = e.getIndication();
		if (ind > ngonLayer.getDataSet().getNumObservations()) {
			logger.severe("got indication greater than data set size, ind = "
					+ ind);
			return;
		}
		if (e.getSource() != ngonLayer) {
			ngonLayer.indicationChanged(e);
		}
		setLegendIndication(ind);
		super.indicationChanged(e);

	}

	private void setLegendIndication(int ind) {
		if (ind >= 0) {
			// starLeg.setObsName(ngonLayer.getObservationName(ind));
			// String[] varNames = ngonLayer.getVarNames();
			// double[] values = ngonLayer.getValues(ind);
			// int[] spikeLengths = ngonLayer.getSpikeLengths(ind);
			// // we need this check, or we start blowing null pointer
			// exceptions
			// // if the we get an indication during iniditalization.
			// if (spikeLengths == null) {
			// return;
			// }
			// starLeg.setValues(values);
			// starLeg.setVariableNames(varNames);
			//
			// starLeg.setSpikeLengths(spikeLengths);
			// Color starColor = ngonLayer.getStarFillColor(ind);
			// starLeg.setStarFillColor(starColor);

		}

	}

	public static void main(String[] args) {
		GraduatedSymbolsMap map = new GraduatedSymbolsMap();
		JFrame frame = new JFrame("Graduated Symbols Map");
		frame.add(map);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeoDataGeneralizedStates geodata = new GeoDataGeneralizedStates();
		DataSetEvent e = new DataSetEvent(geodata.getDataForApps(), geodata);
		map.dataSetChanged(e);

	}
} // end class
