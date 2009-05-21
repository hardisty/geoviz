/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */
package geovista.geoviz.mosaic;

import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.event.ColorArrayEvent;
import geovista.common.event.ColorArrayListener;
import geovista.common.event.ColumnAppendedEvent;
import geovista.common.event.ColumnAppendedListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.geoviz.star.StarPlotCanvas;
import geovista.geoviz.star.StarPlotLegend;
import geovista.geoviz.visclass.VisualClassifier;

/**
 * Paints an array of StarPlot. Responds to and broadcasts DataSetChanged,
 * IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * 
 */
public class MosaicPlot extends JPanel implements DataSetListener,
		ColumnAppendedListener, IndicationListener, SubspaceListener,
		ColorArrayListener, TableModelListener {

	StarPlotCanvas starCan;
	StarPlotLegend starLeg;
	VisualClassifier vc;
	int indication;
	final static Logger logger = Logger.getLogger(MosaicPlot.class.getName());

	public void dataSetChanged(DataSetEvent e) {
		// TODO Auto-generated method stub

	}

	public void dataSetModified(ColumnAppendedEvent e) {
		// TODO Auto-generated method stub

	}

	public void indicationChanged(IndicationEvent e) {
		// TODO Auto-generated method stub

	}

	public void subspaceChanged(SubspaceEvent e) {
		// TODO Auto-generated method stub

	}

	public void colorArrayChanged(ColorArrayEvent e) {
		// TODO Auto-generated method stub

	}

	public void tableChanged(TableModelEvent arg0) {
		// TODO Auto-generated method stub

	}

}
