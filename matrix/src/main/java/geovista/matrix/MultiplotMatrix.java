/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import javax.swing.JFrame;

import geovista.common.event.DataSetEvent;
import geovista.matrix.map.MapMatrixElement;
import geovista.matrix.scatterplot.ScatterPlotMatrixElement;
import geovista.matrix.spacefill.SpaceFillMatrixElement;
import geovista.matrix.treemap.TreeMapMatrixElement;
import geovista.readers.example.GeoDataGeneralizedStates;

public class MultiplotMatrix extends FixedRowMatrix
// implements DataSetListener, DataSetModifiedListener,
// IndicationListener, SubspaceListener,
// ColorArrayListener
{

	public MultiplotMatrix() {
		super();
		setElementClass0(ScatterPlotMatrixElement.class);
		setElementClass1(MapMatrixElement.class);
		setElementClass2(SpaceFillMatrixElement.class);
		setElementClass3(TreeMapMatrixElement.class);

	}

	public static void main(String[] args) {
		MultiplotMatrix map = new MultiplotMatrix();
		JFrame frame = new JFrame("Mutliplot Matrix");
		frame.add(map);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GeoDataGeneralizedStates geodata = new GeoDataGeneralizedStates();
		DataSetEvent e = new DataSetEvent(geodata.getDataForApps(), geodata);
		map.dataSetChanged(e);

	}
} // end class
