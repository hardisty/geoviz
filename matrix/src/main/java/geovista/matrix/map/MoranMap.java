/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoMapUni
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoMapUni.java,v 1.12 2005/08/12 17:25:21 hardisty Exp $
 $Date: 2005/08/12 17:25:21 $
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

package geovista.matrix.map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.data.DataSetBroadcaster;
import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.data.SpatialStatistics;
import geovista.common.data.SpatialWeights;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.PaletteEvent;
import geovista.common.event.PaletteListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SpatialExtentEvent;
import geovista.common.event.SpatialExtentListener;
import geovista.coordination.CoordinationManager;
import geovista.geoviz.map.GeoMap;
import geovista.geoviz.map.GeoMapUni;
import geovista.geoviz.scatterplot.SingleHistogram;
import geovista.geoviz.scatterplot.SingleScatterPlot;
import geovista.symbolization.event.ColorClassifierEvent;
import geovista.symbolization.event.ColorClassifierListener;
import geovista.toolkitcore.data.GeoDataCartogram;

/**
 * A Moran Map has a choropleth varSigMap and a scatterplot tied together
 */
public class MoranMap extends JPanel implements SelectionListener,
		IndicationListener, DataSetListener, ColorClassifierListener,
		SpatialExtentListener, PaletteListener, TableModelListener,
		ActionListener {
	protected final static Logger logger = Logger.getLogger(MoranMap.class
			.getName());

	GeoMapUni varMap;
	GeoMapUni moranMap;
	GeoMapUni sigMap;
	GeoMap varSigMap;
	SingleHistogram varHist;
	SingleHistogram moranHist;
	SingleHistogram sigHist;
	SingleScatterPlot varSigPlot;

	SingleScatterPlot sp;
	DataSetForApps dataSetOriginal;
	DataSetForApps dataSetZ;
	DataSetForApps dataSetMoran;
	SpatialWeights spatialWeights;
	JList varList;
	JButton sendButt;
	int monteCarloIterations = 100;
	DataSetBroadcaster dataCaster;

	CoordinationManager coord;

	public MoranMap() {
		super();

		varMap = new GeoMapUni();
		moranMap = new GeoMapUni();
		sigMap = new GeoMapUni();
		varSigMap = new GeoMap();
		varHist = new SingleHistogram();
		moranHist = new SingleHistogram();
		sigHist = new SingleHistogram();
		varSigPlot = new SingleScatterPlot();

		dataCaster = new DataSetBroadcaster();

		coord = new CoordinationManager();

		coord.addBean(dataCaster);
		coord.addBean(varMap);
		coord.addBean(moranMap);
		coord.addBean(sigMap);
		coord.addBean(varSigMap);
		coord.addBean(varHist);
		coord.addBean(moranHist);
		coord.addBean(sigHist);
		coord.addBean(varSigPlot);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(2, 4));
		mainPanel.add(varMap);
		mainPanel.add(moranMap);
		mainPanel.add(sigMap);
		mainPanel.add(varSigMap);
		mainPanel.add(varHist);
		mainPanel.add(moranHist);
		mainPanel.add(sigHist);
		mainPanel.add(varSigPlot);

		mainPanel.setPreferredSize(new Dimension(1000, 400));
		BoxLayout box = new BoxLayout(this, BoxLayout.X_AXIS);
		// varSigMap = new GeoMap();
		// sp = new SingleScatterPlot();
		// varSigMap.addSelectionListener(sp);
		// sp.addSelectionListener(varSigMap);
		// varSigMap.addIndicationListener(sp);
		// sp.addIndicationListener(varSigMap);
		// setLayout(box);
		Dimension prefSize = new Dimension(200, 200);
		varSigMap.setMaximumSize(prefSize);
		varHist.setMaximumSize(prefSize);
		// LineBorder border = (LineBorder) BorderFactory
		// .createLineBorder(Color.black);
		// varSigMap.setBorder(border);
		// sp.setBorder(border);

		this.add(mainPanel);
		// this.add(sp);

		JPanel varPanel = new JPanel();
		varList = new JList();
		sendButt = new JButton("Add Var");
		varList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		sendButt.addActionListener(this);
		varPanel.setLayout(new BorderLayout());
		varPanel.add(varList, BorderLayout.CENTER);
		varPanel.add(sendButt, BorderLayout.SOUTH);

		this.add(varPanel);

	}

	public void selectionChanged(SelectionEvent e) {
		varSigMap.selectionChanged(e);
		sp.selectionChanged(e);

	}

	public SelectionEvent getSelectionEvent() {
		return new SelectionEvent(this, varSigMap.getSelectedObservations());
	}

	public void indicationChanged(IndicationEvent e) {
		varSigMap.indicationChanged(e);
		sp.indicationChanged(e);

	}

	public void dataSetChanged(DataSetEvent e) {
		dataCaster.setAndFireDataSet(e.getDataSetForApps()
				.getDataObjectOriginal());
	}

	public void dataSetChanged_old(DataSetEvent e) {
		dataSetOriginal = e.getDataSetForApps();
		spatialWeights = dataSetOriginal.getSpatialWeights();
		;
		// first get the z scores....
		Object[] dataObjects = dataSetOriginal.getDataObjectOriginal();
		Object[] zDataObjects = new Object[dataObjects.length];
		String[] names = (String[]) dataObjects[0];
		String[] newNames = new String[names.length];
		System.arraycopy(names, 0, newNames, 0, names.length);
		for (int i = 1; i < dataObjects.length; i++) {
			Object thing = dataObjects[i];
			if (thing instanceof int[]) {
				int[] intThing = (int[]) thing;
				double[] doublething = new double[intThing.length];
				for (int obs = 0; obs < intThing.length; obs++) {
					doublething[obs] = intThing[obs];
				}
				newNames[i - 1] = names[i - 1] + "_Z";
				zDataObjects[i] = DescriptiveStatistics
						.calculateZScores(doublething);
			} else if (thing instanceof double[]) {
				double[] doublething = (double[]) thing;
				newNames[i - 1] = names[i - 1] + "_Z";
				zDataObjects[i] = DescriptiveStatistics
						.calculateZScores(doublething);
			} else {
				zDataObjects[i] = dataObjects[i];
			}
		}
		zDataObjects[0] = newNames;
		dataSetZ = new DataSetForApps(zDataObjects);

		// now do the moran's

		Object[] moranDataObjects = new Object[dataObjects.length];

		String[] moranNames = new String[names.length];
		System.arraycopy(names, 0, moranNames, 0, names.length);

		for (int i = 1; i < zDataObjects.length; i++) {
			Object thing = zDataObjects[i];
			if (thing instanceof double[]) {
				double[] doublething = (double[]) thing;
				moranNames[i - 1] = names[i - 1] + "_M";
				moranDataObjects[i] = SpatialStatistics.calculateMoranScores(
						doublething, spatialWeights);
			} else {
				moranDataObjects[i] = dataObjects[i];
			}
		}
		moranDataObjects[0] = moranNames;

		dataSetMoran = new DataSetForApps(moranDataObjects);

		// now do monte carlo
		Object[] monteCarloDataObjects = new Object[dataObjects.length];

		String[] monteCarloNames = new String[names.length];
		System.arraycopy(names, 0, monteCarloNames, 0, names.length);

		for (int i = 1; i < zDataObjects.length; i++) {
			Object thing = zDataObjects[i];
			if (thing instanceof double[]) {
				double[] zData = (double[]) thing;
				double[] moranData = (double[]) moranDataObjects[i];

				monteCarloDataObjects[i] = SpatialStatistics.findPValues(zData,
						moranData, monteCarloIterations, spatialWeights);
				monteCarloNames[i - 1] = names[i - 1] + "_Sig";
			} else {
				monteCarloDataObjects[i] = dataObjects[i];
			}
		}
		monteCarloDataObjects[0] = monteCarloNames;

		DataSetForApps dataSetMonteCarlo = new DataSetForApps(
				monteCarloDataObjects);
		DataSetForApps dataSetAppended = dataSetMonteCarlo
				.appendDataSet(dataSetZ);

		DataSetEvent e2 = new DataSetEvent(dataSetAppended, this);
		Vector vecData = new Vector();
		for (String element : monteCarloNames) {
			vecData.add(element);
		}
		varList.setListData(vecData);
		varSigMap.dataSetChanged(e2);
		sp.dataSetChanged(e2);
	}

	public void colorClassifierChanged(ColorClassifierEvent e) {
		// TODO Auto-generated method stub

	}

	SpatialExtentEvent savedEvent;

	public SpatialExtentEvent getSpatialExtentEvent() {
		return savedEvent;
	}

	public void spatialExtentChanged(SpatialExtentEvent e) {
		savedEvent = e;
		// TODO Auto-generated method stub

	}

	public void paletteChanged(PaletteEvent e) {
		// TODO Auto-generated method stub

	}

	public void tableChanged(TableModelEvent e) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendButt) {
			if (varList.getSelectedIndex() < 0) {
				return;
			}

			dataSetOriginal.addColumn((String) varList.getSelectedValue(),
					dataSetMoran.getNumericDataAsDouble(varList
							.getSelectedIndex()));
		}

	}

	public static void main(String[] args) {

		MoranMap varSigMap = new MoranMap();
		double[] vals = { 0, 1, 2, 3 };
		double along = DescriptiveStatistics.percentAbove(vals, 2.9);
		System.out.println(along);

		JFrame frame = new JFrame("Moran Map");
		frame.add(varSigMap);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeoDataCartogram geodata = new GeoDataCartogram();
		DataSetEvent e = new DataSetEvent(geodata.getDataForApps(), geodata);
		varSigMap.dataSetChanged(e);

	}

}
