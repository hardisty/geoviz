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
package geovista.geoviz.mosaic;

import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import geovista.common.event.ColorArrayEvent;
import geovista.common.event.ColorArrayListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.ColumnAppendedEvent;
import geovista.common.event.ColumnAppendedListener;
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

} // end class
