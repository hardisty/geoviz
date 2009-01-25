/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix.treemap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import geovista.common.data.DataSetForApps;
import geovista.common.data.DescriptiveStatistics;
import geovista.common.event.ClassificationEvent;
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
import geovista.treemap.TMDataDraw;
import geovista.treemap.TMDataNode;
import geovista.treemap.TMDataSize;
import geovista.treemap.tm.TMView;

/**
 * Paints an array of TreeMapCanvas. Responds to and broadcasts DataSetChanged,
 * IndicationChanged etc. events.
 * 
 * @author Frank Hardisty
 * 
 */
public class TreeMapCanvas extends JPanel implements DataSetListener,
		ColumnAppendedListener, IndicationListener, SubspaceListener,
		ColorArrayListener, MouseListener, MouseMotionListener {
	protected final static Logger logger = Logger.getLogger(TreeMapCanvas.class
			.getName());
	private TMDataNode root = null; // the root of the tree
	TMDataSize fSize;
	TMDataDraw fDraw;
	int indication;
	TMView tmview;
	DataSetForApps dataSet;
	TMDataNode[] leaves;
	int[] classification;
	Color[] colors;
	int groupingVarID;
	int sizingVarID;
	int colorVarID;

	public TreeMapCanvas() {
		setLayout(new BorderLayout());
		root = new TMDataNode(0d, "root");
		fSize = new TMDataSize();
		fDraw = new TMDataDraw();
		geovista.treemap.tm.TreeMap treeMap = new geovista.treemap.tm.TreeMap(
				root);
		tmview = treeMap.getView(fSize, fDraw);
		this.add(tmview, BorderLayout.CENTER);
		addMouseListener(this);
		addMouseMotionListener(this);
		tmview.addMouseListener(this);
		tmview.addMouseMotionListener(this);
	}

	private String findRootName() {
		if (dataSet == null) {
			return "no data set";
		}
		String rootName = "";
		rootName = rootName + "Colored by: "
				+ dataSet.getNumericArrayName(colorVarID);
		rootName = rootName + ", Grouped by: "
				+ dataSet.getNumericArrayName(groupingVarID);
		rootName = rootName + ", Sized by: "
				+ dataSet.getNumericArrayName(sizingVarID);

		return rootName;
	}

	private void initTMView() {
		String rootName = findRootName();
		root = new TMDataNode(0, rootName);
		if (dataSet == null || classification == null) {
			return;
		}

		int nObs = dataSet.getNumObservations();
		leaves = new TMDataNode[nObs];

		String varName = dataSet.getNumericArrayName(groupingVarID);
		int nBranches = DescriptiveStatistics.max(classification) + 1;
		TMDataNode[] branches = new TMDataNode[nBranches];
		double[] branchMin = new double[nBranches];
		double[] branchMax = new double[nBranches];

		for (int i = 0; i < branches.length; i++) {
			TMDataNode branch = new TMDataNode(0, varName + " branch " + i);
			branches[i] = branch;
			branchMin[i] = Double.MAX_VALUE;
			branchMax[i] = Double.MAX_VALUE * -1;
		}

		for (int i = 0; i < nObs; i++) {
			double val = dataSet.getNumericValueAsDouble(sizingVarID, i);
			double branchVal = dataSet
					.getNumericValueAsDouble(groupingVarID, i);
			String name = dataSet.getObservationName(i);
			TMDataNode aData = new TMDataNode(val, name);
			int whichBranch = classification[i];
			if (whichBranch < 0) {
				whichBranch = 0;
			}
			branches[whichBranch].addChild(aData);
			if (branchVal < branchMin[whichBranch]) {
				branchMin[whichBranch] = branchVal;
			}
			if (branchVal > branchMax[whichBranch]) {
				branchMax[whichBranch] = branchVal;
			}

			leaves[i] = aData;
			if (colors != null) {
				leaves[i].setColor(colors[i]);
			}
		}
		for (int i = 0; i < branches.length; i++) {
			root.addChild(branches[i]);
			branches[i].setName(branchMin[i] + " < " + varName + " < "
					+ branchMax[i]);
		}

		geovista.treemap.tm.TreeMap treeMap = new geovista.treemap.tm.TreeMap(
				root); // xxx
		// fix
		// me?
		// do
		// we
		// need
		// a
		// new
		// tree
		// every
		// time?
		tmview = treeMap.getView(fSize, fDraw);
		tmview.addMouseListener(this);
		tmview.addMouseMotionListener(this);
		removeAll();
		this.add(tmview, BorderLayout.CENTER);

	}

	public void dataSetChanged(DataSetEvent e) {
		dataSet = e.getDataSetForApps();
		initTMView();
	}

	public void dataSetModified(ColumnAppendedEvent e) {

	}

	public void subspaceChanged(SubspaceEvent e) {

	}

	public void classificationChanged(ClassificationEvent e) {
		int[] newClasses = e.getClassification();
		setClassification(newClasses);
	}

	public void setClassification(int[] newClasses) {
		classification = newClasses;
		initTMView();
	}

	public void setLayoutMethod(String layoutMethod) {

		tmview.setAlgorithm(layoutMethod);
	}

	public void setGroupingVarID(int groupingVarID) {
		this.groupingVarID = groupingVarID;
	}

	public void setSizingVarID(int sizingVarID) {
		if (this.sizingVarID == sizingVarID || dataSet == null) {
			return;
		}
		this.sizingVarID = sizingVarID;
		double[] data = dataSet.getNumericDataAsDouble(sizingVarID);
		if (leaves == null) {
			return;
		}
		for (int i = 0; i < data.length; i++) {
			leaves[i].setValue(data[i]);

		}
		// xxx fah we should do the line following, instead of initTMView
		// xxx but the method doesn't exist yet...
		// tmview.changeTMComputeSize(new TMComputeSize());
		initTMView();

	}

	public void setColorVarID(int colorVarID) {
		this.colorVarID = colorVarID;
		root.setName(findRootName());

	}

	public void colorArrayChanged(ColorArrayEvent e) {
		Color[] dataColors = e.getColors();
		colors = dataColors; // remember these for when leaves get made, if
		// not now
		if (leaves == null || dataSet == null) {
			return;
		}
		for (int i = 0; i < dataColors.length; i++) {
			leaves[i].setColor(dataColors[i]);

		}
		if (root != null) {
			tmview.changeTMComputeDraw(new TMDataDraw());
		}
	}

	public void indicationChanged(IndicationEvent e) {
		int newIndication = e.getIndication();
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("this.indicaiton = " + indication);
			logger.finest("new  = " + newIndication);
		}
		// need to fix old one?
		if (newIndication < 0 && indication >= 0) { // new indication is out
			// of range, old was was
			// in range
			leaves[indication].setIsIndicated(false);

			indication = newIndication;
			tmview.repaint();
			tmview.changeTMComputeDraw(new TMDataDraw());
		}
		if (newIndication != indication && leaves != null && newIndication >= 0
				&& newIndication < leaves.length) {
			if (indication >= 0) {
				leaves[indication].setIsIndicated(false);
			}
			leaves[newIndication].setIsIndicated(true);
			indication = newIndication;

			tmview.changeTMComputeDraw(new TMDataDraw());

		}
	}

	// start mouse events
	/***************************************************************************
	 * Interface methods for mouse events *
	 **************************************************************************/
	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {
		fireIndicationChanged(-1); // clear indication
		indicationChanged(new IndicationEvent(this, -1));
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {

		if (tmview == null || dataSet == null) {
			return;
		}
		int whichPlot = -1;
		Object node = tmview.getNodeUnderTheMouse(e);
		if (node == null) {
			return;// can't find anything, so forget about it.
		}
		for (int i = 0; i < leaves.length; i++) {
			if (node == leaves[i]) {
				whichPlot = i;
			}
		}

		fireIndicationChanged(whichPlot);

	}

	// end mouse events
	/**
	 * adds an IndicationListener to the component
	 */
	public void addIndicationListener(IndicationListener l) {
		listenerList.add(IndicationListener.class, l);
	}

	/**
	 * removes an IndicationListener from the component
	 */
	public void removeIndicationListener(IndicationListener l) {
		listenerList.remove(IndicationListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	public void fireIndicationChanged(int indication) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		IndicationEvent e = null;

		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == IndicationListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new IndicationEvent(this, indication);
				}

				((IndicationListener) listeners[i + 1]).indicationChanged(e);
			}
		}
	}

	public int getGroupingVarID() {
		return groupingVarID;
	}

} // end class
