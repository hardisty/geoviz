/* -------------------------------------------------------------------
 Java source file for the class GeoVizToolkit
 $Author: hardisty $
 $Id: GeoVizToolkit.java,v 1.12 2006/02/16 16:55:19 hardisty Exp $
 $Date: 2006/02/16 16:55:19 $
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
 Foundation, Inc., 59 Temple Place, Suite 3bb      30, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.toolkitcore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import geovista.animation.IndicationAnimator;
import geovista.animation.SelectionAnimator;
import geovista.cartogram.GeoMapCartogram;
import geovista.common.data.DataSetBroadcaster;
import geovista.common.data.DataSetForApps;
import geovista.common.data.DataSetModifiedBroadcaster;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.ui.VariablePicker;
import geovista.coordination.CoordinationManager;
import geovista.coordination.CoordinationUtils;
import geovista.coordination.FiringBean;
import geovista.geoviz.condition.ConditionManager;
import geovista.geoviz.map.GeoMap;
import geovista.geoviz.map.GeoMapUni;
import geovista.geoviz.parvis.ParallelPlot;
import geovista.geoviz.radviz.RadViz;
import geovista.geoviz.sample.GeoDataGeneralizedStates;
import geovista.geoviz.scatterplot.SingleHistogram;
import geovista.geoviz.scatterplot.SingleScatterPlot;
import geovista.geoviz.shapefile.ShapeFileDataReader;
import geovista.geoviz.shapefile.ShapeFileProjection;
import geovista.geoviz.spreadsheet.SpreadSheetBean;
import geovista.geoviz.spreadsheet.VariableTransformer;
import geovista.geoviz.star.StarPlot;
import geovista.geoviz.star.StarPlotMap;
import geovista.matrix.MapAndScatterplotMatrix;
import geovista.matrix.MapMatrix;
import geovista.matrix.MapScatterplotTreemapMatrix;
import geovista.matrix.TreemapAndScatterplotMatrix;
import geovista.matrix.map.MoranMap;
import geovista.satscan.SaTScan;
import geovista.sound.SonicClassifier;
import geovista.toolkitcore.data.GeoDataCartogram;
import geovista.toolkitcore.data.GeoDataNCTC;
import geovista.toolkitcore.data.GeoDataNiger;
import geovista.toolkitcore.data.GeoDataPennaPCA;
import geovista.toolkitcore.data.GeoDataSCarolina;
import geovista.toolkitcore.data.GeoDataSCarolinaCities;
import geovista.touchgraph.LinkGraph;
import geovista.touchgraph.PCAViz;
import geovista.touchgraph.SubspaceLinkGraph;

/*
 * Assumptions: 1. One dataset at a time. 2. Maximum coordination as a default.
 * 
 * 
 */

public class GeoVizToolkit extends JFrame implements ActionListener,
		ComponentListener, InternalFrameListener {

	/**
	 * 
	 */
	final transient static Logger logger = Logger.getLogger(GeoVizToolkit.class
			.getName());
	// collection of classes to add
	ArrayList toolMenuList = new ArrayList();
	HashMap toolClassHash = new HashMap();

	// collection of active beans
	ToolkitBeanSet tBeanSet;

	// Create JDesktopPane to hold the internal frames
	JDesktopPane desktop = new JDesktopPane();

	// managing our layouts

	String filePath = "48States";
	JFileChooser fileChooser;
	ShapeFileDataReader shpRead = new ShapeFileDataReader();
	ShapeFileProjection shpProj = new ShapeFileProjection();
	DataSetBroadcaster dataCaster = new DataSetBroadcaster();
	CoordinationManager coord = new CoordinationManager();
	transient DataSetForApps dataSet;
	transient Vector backgroundDataSets; // every item in this should be a
	// DataSetForApps
	transient JMenuBar jMenuBar1 = new JMenuBar();
	transient JMenu menuFile = new JMenu();
	transient JMenuItem menuItemLoadShp = new JMenuItem();

	transient JMenu menuAddTool = new JMenu();
	transient JMenuItem menuItemLoadStates = new JMenuItem();
	transient JMenuItem menuItemLoadSC = new JMenuItem();
	transient JMenuItem menuItemLoadSCCities = new JMenuItem();
	transient JMenuItem menuItemLoadCartogram = new JMenuItem();
	transient JMenuItem menuItemLoadBackgroundShape = new JMenuItem();
	transient JMenuItem menuItemLoadSCBackgroundShape = new JMenuItem();
	transient JMenu menuRemoveTool = new JMenu();
	transient JMenuItem menuItemRemoveAllTools = new JMenuItem();
	transient JMenu menuAbout = new JMenu();
	transient JMenu menuHelp = new JMenu();
	transient JMenuItem menuItemAboutGeoviz = new JMenuItem();
	transient JMenuItem menuItemAboutGeoVista = new JMenuItem();
	transient JMenuItem menuItemHelp = new JMenuItem();
	transient JMenuItem menuItemOpenLayout = new JMenuItem();
	transient JMenuItem menuItemSaveLayout = new JMenuItem();
	transient JMenuItem menuItemExit = new JMenuItem();
	transient JMenu menuCollaborate = new JMenu();
	transient JMenuItem menuItemEnableCollaboration = new JMenuItem();
	transient JMenuItem menuItemDisableCollaboration = new JMenuItem();
	transient JMenuItem menuItemConnect = new JMenuItem();
	transient JMenuItem menuItemDisconnect = new JMenuItem();
	transient JMenu menuScreenShot = new JMenu();
	transient JMenuItem menuItemCopyApplicationToClipboard = new JMenuItem();
	transient JMenuItem menuItemCopySelectedWindowToClipboard = new JMenuItem();
	transient JMenuItem menuItemSaveWholeImageToFile = new JMenuItem();
	transient JMenuItem menuItemSaveSelectedWindowToFile = new JMenuItem();

	VizState vizState;
	// how about svg and postscript?

	USCHelp help;

	boolean useProj;

	static boolean DEBUG = false;

	/*
	 * need to call init after the null ctr as an alternative with
	 * initializaiton, call GeoVizToolkit("")
	 */
	public GeoVizToolkit() {
		super();
		vizState = new VizState();
	}

	public GeoVizToolkit(String fileNameIn) {
		new GeoVizToolkit(fileNameIn, false, true);
		// Thread.dumpStack();
	}

	public GeoVizToolkit(String fileNameIn, boolean useProj, boolean useAux) {

		super("GeoViz Toolkit");
		vizState = new VizState();
		init(fileNameIn, useProj);
	}

	public void init(String fileNameIn, boolean useProj) {
		desktop.setBackground(new Color(20, 20, 80));
		this.useProj = useProj;
		getContentPane().add(desktop, BorderLayout.CENTER);
		coord.addBean(dataCaster);

		coord.addBean(vizState);
		try {
			this.init();
			initMenuListeners();
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		if (fileNameIn == null || fileNameIn.equals("")) {
			fileNameIn = "C:\\data\\grants\\NCI 07\\Files_for_Frank\\Health_Care_2000_Smaller.shp";
			// fileNameIn = "C:\\data\\grants\\NCI
			// 07\\Files_for_Frank\\Health_Care_2000_Smaller.shp";
			fileNameIn = filePath;

		}

		loadData(fileNameIn);

		URL urlGif = null;

		ImageIcon im = null;
		try {
			Class cl = this.getClass();
			urlGif = cl.getResource("resources/geoviz_toolkit32.gif");
			im = new ImageIcon(urlGif);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		setIconImage(im.getImage());
		pack();
		setVisible(true);

		setExtendedState(Frame.MAXIMIZED_BOTH);

		// if we are coming up from xml format, this might have already been
		// set.
		if (tBeanSet == null) {
			tBeanSet = ToolkitIO.openStarPlotMapLayout();
		}
		addToolkitBeanSet(tBeanSet);

		this.repaint();
	}

	void openAllComponents() {
		// this.removeAllBeans();
		ToolkitBeanSet tempBeanSet = ToolkitIO.openAllComponentsLayout();
		addToolkitBeanSet(tempBeanSet);
		this.repaint();
	}

	void setProgramState(VizState newState) {
		removeAllBeans();
		coord.removeBean(vizState);
		vizState = newState;
		useProj = vizState.useProj;
		loadData(newState.getDataSource());
		addToolkitBeanSet(vizState.getBeanSet());

	}

	void saveProgramState() {

	}

	public void addExternalBean(Object bean) {
		coord.addBean(bean);
		// XXX total hack for demo
		DataSetModifiedBroadcaster localCaster = (DataSetModifiedBroadcaster) bean;
		DataSetForApps dataSetOut = shpProj.getOutputDataSetForApps();
		logger.finest("geovizdemo, addBean, dataSetIsNull ="
				+ (dataSetOut == null));
		localCaster.setDataSet(dataSetOut);
	}

	/*
	 * deleting all beans
	 * 
	 */

	public void removeAllBeans() {
		Iterator it = tBeanSet.getBeanSet().iterator();
		while (it.hasNext()) {
			ToolkitBean oldBean = (ToolkitBean) it.next();
			removeBeanFromGui(oldBean);
			oldBean = null;
		}
		// this.tBeanSet.clear();

	}

	/*
	 * deleting named bean
	 * 
	 */

	public void deleteBean(ToolkitBean oldBean) {
		removeBeanFromGui(oldBean);
		tBeanSet.remove(oldBean);
		oldBean = null;
	}

	/*
	 * cleanup 1. remove item from remove menu 2. remove from map of beans 3.
	 * remove from coordination 4. remove from GUI (should be no more references
	 * at this point, besides in tBeanSet)
	 */
	private void removeBeanFromGui(ToolkitBean oldBean) {
		JMenuItem item = oldBean.getRemoveMenuItem();
		item.removeActionListener(this);
		menuRemoveTool.remove(item);
		coord.removeBean(oldBean.getOriginalBean());

		// find area in layout occupied by internal frame
		JInternalFrame iFrame = oldBean.getInternalFrame();
		int iX = iFrame.getX();
		int iY = iFrame.getY();
		int iWidth = iFrame.getWidth();
		int iHeight = iFrame.getHeight();
		// remove it, then repaint area
		desktop.remove(oldBean.getInternalFrame());
		desktop.repaint(iX, iY, iWidth, iHeight);

	}

	public static Object makeObject(String className) {
		Object obj = null;
		try {

			Class beanClass = Class.forName(className);
			obj = beanClass.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	/*
	 * one reason this method is called when a bean is instantiated because the
	 * user has clicked on the appropriate menu item. If a bean is instantiated
	 * because we have loaded a layout, then the method in ToolkitIO is used.
	 */
	private ToolkitBean instantiateBean(String className) {

		ToolkitBean newToolkitBean = null;
		JInternalFrame bInterFrame = null;

		Object newBean = GeoVizToolkit.makeObject(className);
		FiringBean newFBean = coord.addBean(newBean);
		String uniqueName = newFBean.getBeanName();

		newToolkitBean = new ToolkitBean(newBean, uniqueName);

		bInterFrame = newToolkitBean.getInternalFrame();

		// ############################################################################//
		// TODO: replace with something a bit less "by hand"
		// maybe use the bean's peferredSize if they are a java.awt.Component
		bInterFrame.setLocation(25 * tBeanSet.size(), 25 * tBeanSet.size());

		int place = className.lastIndexOf(".");
		int length = className.length();
		String shortClassName = className.substring(place + 1, length);

		if (shortClassName.equalsIgnoreCase("LinkGraph")) {
			bInterFrame.setSize(450, 450);
		} else if (shortClassName.equalsIgnoreCase("StarPlot")) {
			bInterFrame.setSize(400, 400);
		} else if (shortClassName.equalsIgnoreCase("SubspaceLinkGraph")) {
			bInterFrame.setSize(500, 450);
		} else if (shortClassName.equalsIgnoreCase("TableBrowser")) {
			bInterFrame.setSize(670, 530);
		} else if (shortClassName.equalsIgnoreCase("SonicClassifier")) {
			// Ke: ######SONICCLASSIFIER COULD MAKE THE PROGRAM DEAD IN ABSENCE
			// OF SOUND DEVICE
			// Frank: sound devices depend on the presence of a "soundbank.gm"
			// file,
			// which is avialble as a resource from
			// geovista.sound.resources

			bInterFrame.setSize(350, 100);
		} else if (shortClassName.equalsIgnoreCase("SelectionAnimator")) {
			bInterFrame.setSize(885, 100);
		} else if (shortClassName.equalsIgnoreCase("IndicationAnimator")) {
			bInterFrame.setSize(780, 100);
			// ############################################################################//

		}
		// newToolkitBean.setInternalFrame(bInterFrame);
		return newToolkitBean;
	}

	/*
	 * adds bean to coordinator. Adds bean to the main gui area, also to the
	 * remove bean menu
	 * 
	 */
	public void addBeanToGui(ToolkitBean newToolkitBean) {

		desktop.add(newToolkitBean.getInternalFrame(), 0); // add on top
		newToolkitBean.addComponentListener(this);
		newToolkitBean.getInternalFrame().addInternalFrameListener(this);
		JMenuItem item = newToolkitBean.getRemoveMenuItem();
		item.addActionListener(this);

		menuRemoveTool.add(item, 0); // add at the top

		Object newBean = newToolkitBean.getOriginalBean();

		if (newBean instanceof DataSetListener) {
			DataSetListener dataListener = (DataSetListener) newBean;
			dataListener.dataSetChanged(new DataSetEvent(dataSet, this));

		}

	}

	private void addToolkitBeanSet(ToolkitBeanSet beanSet) {
		tBeanSet = beanSet;
		Iterator iter = tBeanSet.iterator();
		while (iter.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) iter.next();
			Object obj = tBean.getOriginalBean();
			coord.addBean(obj);
			addBeanToGui(tBean);

		}

	}

	// public void dataSetChanged(DataSetEvent e) {
	// dataSet = e.getDataSetForApps();
	// }

	private void showHelp() {
		// lazy initialize
		if (help == null) {
			help = new USCHelp();
			help.pack();
		}
		boolean haveHelp = false;
		Component[] desktopComponents = desktop.getComponents();
		for (Component element : desktopComponents) {
			if (element == help) {
				desktop.remove(element);
				desktop.add(element, 0);
				haveHelp = true;
			}
		}
		if (!haveHelp) {
			logger.finest("GVToolkit, showhelp, adding to desktop");
			desktop.add(help, 0); // add on top
		}
		this.repaint();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuItemAboutGeoVista) {

			JOptionPane.showMessageDialog(this,
					"Most components in this toolkit "
							+ "developed at the GeoVISTA Center.");

		} else if (e.getSource() == menuItemAboutGeoviz) {

			JOptionPane
					.showMessageDialog(
							this,
							"This application developed by "
									+ "Frank Hardisty with contributions by Diansheng Guo, Ke Liao, and Aaron Myers "
									+ "at the Univerisity of South Carolina, and many others at the GeoVISTA Center.");
		} else if (e.getSource() == menuItemHelp) {
			showHelp();
		} else if (e.getSource() == menuItemExit) {
			// is this too weak? do we need a system exit?
			setVisible(false);
		} else if (e.getSource() == menuItemLoadSC) {
			loadData("SC");
		} else if (e.getSource() == menuItemLoadSCCities) {
			loadData("SCCities");

		} else if (e.getSource() == menuItemLoadShp) {
			openShapefilePicker();
		} else if (e.getSource() == menuItemLoadStates) {
			loadData("48States");
		} else if (e.getSource() == menuItemLoadCartogram) {
			loadData("Cartogram");
		} else if (e.getSource() == menuItemLoadBackgroundShape) {
			openBackgroundShapeFilePicker();
		} else if (e.getSource() == menuItemLoadSCBackgroundShape) {
			loadBackgroundData("SC");
		}

		else if (e.getSource() == menuItemOpenLayout) {

			ToolkitBeanSet tempBeanSet = ToolkitIO.openLayout(this);
			if (tempBeanSet == null) {
				return;
			}
			removeAllBeans();
			addToolkitBeanSet(tempBeanSet);

		} else if (e.getSource() == menuItemSaveLayout) {

			ToolkitIO.writeLayout(getFileName(), tBeanSet, this);

		} else if (toolClassHash.containsKey(e.getSource())) { // one of
			// our added
			// classes
			String className = (String) toolClassHash.get(e.getSource());
			ToolkitBean tBean = null;
			tBean = instantiateBean(className);
			addBeanToGui(tBean);
			tBeanSet.add(tBean);

		}

		else if (e.getSource() == menuItemRemoveAllTools) {
			removeAllBeans();
		} else if (e.getSource() instanceof JMenuItem
				&& tBeanSet.contains((JMenuItem) e.getSource())) {
			JMenuItem item = (JMenuItem) e.getSource();

			ToolkitBean oldTool = tBeanSet.getToolkitBean(item);
			// assert (item != null);
			deleteBean(oldTool); // deleteBean calls removeBeanFromGUI
			oldTool = null; // or should we just let it go out of scope? or do
			// we need to do more?

		} else if (e.getSource() == menuItemCopyApplicationToClipboard) {

			ToolkitIO.copyComponentImageToClipboard(this);
		} else if (e.getSource() == menuItemCopySelectedWindowToClipboard) {
			JInternalFrame frame = desktop.getSelectedFrame();
			if (frame != null) {
				ToolkitIO.copyComponentImageToClipboard(frame);
			}

		} else if (e.getSource() == menuItemSaveWholeImageToFile) {

			ToolkitIO.saveImageToFile(this);
		} else if (e.getSource() == menuItemSaveSelectedWindowToFile) {
			JInternalFrame frame = desktop.getSelectedFrame();
			if (frame != null) {
				ToolkitIO.saveImageToFile(frame);
			}
		}

		// else if (e.getSource() == this.menuItemDisableCollaboration) {
		//
		// } else if (e.getSource() == this.menuItemConnect) {
		//
		// } else if (e.getSource() == this.menuItemDisconnect) {
		//
		// }
	}

	private void loadBackgroundData(String name) {
		Object[] newDataSet = createData(name);
		DataSetForApps auxData = new DataSetForApps(newDataSet);
		dataCaster.fireAuxiliaryDataSet(auxData);
	}

	public void loadData(String name) {
		filePath = name;
		logger
				.fine("geovizdemo, loadData, dataSetIsNull ="
						+ (dataSet == null));
		Object[] newDataSet = createData(name);
		logger
				.fine("geovizdemo, loadData, dataSetIsNull ="
						+ (dataSet == null));
		dataSet = new DataSetForApps(newDataSet);
		dataCaster.setAndFireDataSet(newDataSet);
		logger
				.fine("geovizdemo, loadData, dataSetIsNull ="
						+ (dataSet == null));

	}

	private Object[] createData(String name) {
		if (name == null) {
			return null;
		}
		Object[] newDataSet = null;
		if (name.equals("48States")) {
			GeoDataGeneralizedStates statesData = new GeoDataGeneralizedStates();
			newDataSet = statesData.getDataSet();

		} else if (name.equals("SC")) {
			GeoDataSCarolina carolinaData = new GeoDataSCarolina();
			newDataSet = carolinaData.getDataSet();

		} else if (name.equals("SCCities")) {
			GeoDataSCarolinaCities carolinaData = new GeoDataSCarolinaCities();
			newDataSet = carolinaData.getDataSet();

		} else if (name.equals("Cartogram")) {
			GeoDataCartogram cartogramData = new GeoDataCartogram();
			newDataSet = cartogramData.getDataSet();

		} else if (name.equals("nctc")) {
			GeoDataNCTC cartogramData = new GeoDataNCTC();
			newDataSet = cartogramData.getDataSet();

		}

		else if (name.equals("niger")) {
			GeoDataNiger cartogramData = new GeoDataNiger();
			newDataSet = cartogramData.getDataSet();

		}

		else if (name.equals("PennaPCA")) {
			// XXX hack for testing
			GeoDataPennaPCA pennaPCAData = new GeoDataPennaPCA();
			// GeoDataGeneralizedStates pennaPCAData = new
			// GeoDataGeneralizedStates();
			newDataSet = pennaPCAData.getDataSet();

		} else {
			shpRead.setFileName(name);
			if (useProj) {
				shpProj.setInputDataSet(shpRead.getDataSet());
				newDataSet = shpProj.getOutputDataSet();
			} else {
				newDataSet = shpRead.getDataSet();
			}
		}
		setTitle("GeoViz Toolkit -- " + name);
		return newDataSet;

	}

	private void openShapefilePicker() {

		String fileName = ToolkitIO.getFileName(this, ToolkitIO.ACTION_OPEN,
				ToolkitIO.FILE_TYPE_SHAPEFILE);

		loadData(fileName);
	}

	private void openBackgroundShapeFilePicker() {
		String fileName = ToolkitIO.getFileName(this, ToolkitIO.ACTION_OPEN,
				ToolkitIO.FILE_TYPE_SHAPEFILE);

		loadBackgroundData(fileName);
		logger.finest("Background name = " + fileName);

	}

	/*
	 * flow of tool added to menu: add JMenuItem with appropriate name and icon
	 * to menu add app as a listener to JMenuItem add entry in ListArray of
	 * added menu tools
	 */
	private void addToolToMenu(Class tool) {

		String className = tool.getName();
		int place = className.lastIndexOf(".") + 1;
		int len = className.length();
		String shortName = className.substring(place, len);
		Image im = CoordinationUtils.findSmallIcon(tool);
		Icon ic = new ImageIcon(im);
		JMenuItem item = new JMenuItem(shortName, ic);
		menuAddTool.add(item); // the menu
		item.addActionListener(this);
		toolClassHash.put(item, className); // the menuItem is the key,
		// the classname the value
		toolMenuList.add(item);

	}

	/*
	 * There is no corresponding setter, because it would duplicate
	 * loadData(String)
	 */
	public String getFileName() {
		return filePath;
	}

	// start component event handling
	public void componentHidden(ComponentEvent e) {
		if (e.getSource() instanceof JInternalFrame
				&& tBeanSet.contains((JInternalFrame) e.getSource())) {
			deleteBean(tBeanSet.getToolkitBean((JInternalFrame) e.getSource()));

		}

	}

	public void componentMoved(ComponentEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("moved");
		}
	}

	public void componentShown(ComponentEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("shown");
		}

	}

	public void componentResized(ComponentEvent e) {
		if (logger.isLoggable(Level.FINEST)) {
			if (e.getSource() instanceof JInternalFrame
					&& tBeanSet.contains((JInternalFrame) e.getSource())) {
				JInternalFrame iFrame = (JInternalFrame) e.getSource();
				int width = iFrame.getWidth();
				int height = iFrame.getHeight();
				logger.finest("iFrame.width = " + width + ",iFrame.height"
						+ height);

			}

		}

	}

	// end component event handling
	private void initMenuListeners() {

		menuItemAboutGeoVista.addActionListener(this);
		menuItemAboutGeoviz.addActionListener(this);
		menuItemExit.addActionListener(this);
		menuItemLoadSC.addActionListener(this);
		menuItemLoadSCCities.addActionListener(this);
		menuItemLoadShp.addActionListener(this);
		menuItemLoadCartogram.addActionListener(this);
		menuItemLoadBackgroundShape.addActionListener(this);
		menuItemLoadSCBackgroundShape.addActionListener(this);
		menuItemLoadStates.addActionListener(this);
		menuItemOpenLayout.addActionListener(this);
		menuItemSaveLayout.addActionListener(this);
		menuItemRemoveAllTools.addActionListener(this);
		menuItemHelp.addActionListener(this);
		menuItemEnableCollaboration.addActionListener(this);
		menuItemDisableCollaboration.addActionListener(this);
		menuItemConnect.addActionListener(this);
		menuItemDisconnect.addActionListener(this);
		menuItemDisableCollaboration.setEnabled(false);
		menuItemConnect.setEnabled(false);
		menuItemDisconnect.setEnabled(false);
		menuItemSaveWholeImageToFile.addActionListener(this);
		menuItemSaveSelectedWindowToFile.addActionListener(this);
		menuItemCopyApplicationToClipboard.addActionListener(this);
		menuItemCopySelectedWindowToClipboard.addActionListener(this);

		// components are organized by dimension of analysis
		// then by commonality of usage

		// data handling components
		addToolToMenu(VariablePicker.class);
		addToolToMenu(SpreadSheetBean.class);
		addToolToMenu(VariableTransformer.class);
		menuAddTool.addSeparator();

		// univariate data viz
		addToolToMenu(SingleHistogram.class);
		addToolToMenu(GeoMapUni.class);
		// addToolToMenu(SpaceFill.class);
		addToolToMenu(SonicClassifier.class);
		addToolToMenu(GeoMapCartogram.class);
		menuAddTool.addSeparator();

		// bivariate data viz
		addToolToMenu(SingleScatterPlot.class);
		addToolToMenu(GeoMap.class);
		menuAddTool.addSeparator();

		// multivaraite data viz
		addToolToMenu(LinkGraph.class);
		addToolToMenu(ParallelPlot.class);
		addToolToMenu(StarPlot.class);
		addToolToMenu(StarPlotMap.class);
		addToolToMenu(RadViz.class);
		// addToolToMenu(CartogramAndScatterplotMatrix.class);
		// addToolToMenu(CartogramMatrix.class);
		menuAddTool.addSeparator();

		// tools that operate on variables
		addToolToMenu(PCAViz.class);
		addToolToMenu(SubspaceLinkGraph.class);
		menuAddTool.addSeparator();

		// matrix tools
		// addToolToMenu(TreeMap.class);
		addToolToMenu(MapAndScatterplotMatrix.class);
		addToolToMenu(TreemapAndScatterplotMatrix.class);
		addToolToMenu(MapScatterplotTreemapMatrix.class);
		addToolToMenu(MapMatrix.class);
		menuAddTool.addSeparator();

		// dynamic tools
		addToolToMenu(SelectionAnimator.class);
		addToolToMenu(IndicationAnimator.class);
		addToolToMenu(ConditionManager.class);
		menuAddTool.addSeparator();

		// Spatial analysis tools
		addToolToMenu(MoranMap.class);
		addToolToMenu(SaTScan.class);
		menuAddTool.addSeparator();

		// other...
		// addToolToMenu(GeoJabber.class);

	}

	private void init() throws Exception {
		setJMenuBar(jMenuBar1);
		menuFile.setText("File");
		menuItemLoadShp.setText("Load Shapefile from disk");
		menuAddTool.setText("Add Tool");
		menuItemLoadStates.setText("Load U.S. State Data");
		menuItemLoadSC.setText("Load South Carolina County Data");
		menuItemLoadSCCities.setText("Load South Carolina City Data");
		menuItemLoadCartogram.setText("Load Population Cartogram");
		menuRemoveTool.setText("Remove Tool");
		menuAbout.setText("About");
		menuHelp.setText("Help");
		menuItemAboutGeoviz.setText("About GeoViz Toolkit");
		menuItemAboutGeoVista.setText("About GeoVISTA Studio");
		menuItemHelp.setText("Tutorial");
		menuItemOpenLayout.setText("Open Layout");
		menuItemSaveLayout.setText("Save Layout");
		menuItemLoadBackgroundShape.setText("Load Background Map from disk");
		menuItemLoadSCBackgroundShape.setText("Load South Carolina Background");
		menuCollaborate.setText("Remote Collaboration");
		menuCollaborate.setEnabled(false);// until it works
		menuItemEnableCollaboration.setText("Enable Collaboration");
		menuItemDisableCollaboration.setText("Disable Collaboration");
		menuItemConnect.setText("Connect");
		menuItemDisconnect.setText("Disconnect");
		menuItemRemoveAllTools.setText("Remove All Tools");
		menuScreenShot.setText("Save Images");
		menuItemCopyApplicationToClipboard
				.setText("Copy image of main window to clipboard");
		menuItemCopySelectedWindowToClipboard
				.setText("Copy image of currently selected window to clipboard");
		menuItemSaveWholeImageToFile
				.setText("Save image of main window to file");
		menuItemSaveSelectedWindowToFile
				.setText("Save image of currently selected window to file");

		jMenuBar1.add(menuFile);
		jMenuBar1.add(menuAddTool);
		jMenuBar1.add(menuRemoveTool);
		jMenuBar1.add(menuCollaborate);
		jMenuBar1.add(menuScreenShot);
		jMenuBar1.add(menuAbout);
		jMenuBar1.add(menuHelp);
		menuFile.add(menuItemLoadShp);
		menuFile.add(menuItemLoadStates);
		menuFile.add(menuItemLoadSC);
		menuFile.add(menuItemLoadSCCities);
		menuFile.add(menuItemLoadCartogram);
		menuFile.addSeparator();
		menuFile.add(menuItemLoadBackgroundShape);
		menuFile.add(menuItemLoadSCBackgroundShape);

		menuFile.addSeparator();

		menuFile.add(menuItemOpenLayout);
		menuFile.add(menuItemSaveLayout);
		menuFile.addSeparator();

		menuCollaborate.add(menuItemEnableCollaboration);
		menuCollaborate.add(menuItemDisableCollaboration);
		menuCollaborate.add(menuItemConnect);
		menuCollaborate.add(menuItemDisconnect);

		menuScreenShot.add(menuItemCopyApplicationToClipboard);
		menuScreenShot.add(menuItemCopySelectedWindowToClipboard);
		menuScreenShot.add(menuItemSaveWholeImageToFile);
		menuScreenShot.add(menuItemSaveSelectedWindowToFile);

		menuAbout.add(menuItemAboutGeoviz);
		menuAbout.add(menuItemAboutGeoVista);
		menuHelp.add(menuItemHelp);
		menuRemoveTool.addSeparator();
		menuRemoveTool.add(menuItemRemoveAllTools);
	}

	public static void main(String[] args) {

		Logger logger = Logger.getLogger("geovista");
		// Logger mapLogger = Logger.getLogger("geovista.geoviz.map.MapCanvas");
		// logger.setLevel(Level.FINEST);
		// mapLogger.setLevel(Level.FINEST);
		// LogManager mng = LogManager.getLogManager();
		// mng.addLogger(logger);
		// mng.addLogger(mapLogger);
		/*
		 * ConsoleHandler handler = new ConsoleHandler();
		 * handler.setLevel(Level.INFO); logger.addHandler(handler);
		 */
		try {
			// Create a file handler that write log record to a file called
			// my.log
			FileHandler fHandler = new FileHandler("gv_toolkit.log");
			fHandler.setFormatter(new SimpleFormatter());
			// logger.addHandler(fHandler);
		} catch (IOException e) {
			System.err.println("could not create log file");
			e.printStackTrace();
		}

		boolean useProj = true;
		boolean useAux = true;
		System.setProperty("swing.aatext", "true");

		try {
			// UIManager.setLookAndFeel(new SyntheticaStandardLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * try { Class beanClass =
		 * Class.forName("geovista.collaboration.GeoJabber"); Object
		 * beanInstance = beanClass.newInstance(); logger.finest("yahhh");
		 * logger.finest("found a " + beanClass.getClass().getCanonicalName());
		 * logger.finest("found a " +
		 * beanInstance.getClass().getCanonicalName()); } catch (Exception e) {
		 * logger.finest("waaaa"); e.printStackTrace(); }
		 */
		logger.finest("starting.....");
		logger.finest("java.version = " + System.getProperty("java.version"));

		String fileName = null;

		if (args.length == 0) {
			// fileName = "c:\\temp\\shapefiles\\ca_cities.shp";
			// fileName =
			// "C:\\data\\geovista_data\\Historical-Demographic\\census\\census80_90_00.shp";
		} else {
			fileName = args[0];
		}
		// fileName = "D:\\publications\\atpm\\Export_Output4.shp";
		// fileName = "D:\\courses\\geog
		// 741\\charlie\\richlex_zone63_diss2.shp";
		// fileName = "C:\\temp\\states48.shp";
		// fileName = "C:\\temp\\county\\county.shp";
		if (args.length >= 2) {
			String arg2 = args[1];

			if (arg2.compareToIgnoreCase("false") == 0) {
				useProj = false;
			}
		}

		GeoVizToolkit app = new GeoVizToolkit(fileName, useProj, useAux);

		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setPreferredSize(new Dimension(800, 600));
		app.setMinimumSize(new Dimension(800, 600));
		// app.openAllComponents();

		/*
		 * until we get jts in maven String fileName2 =
		 * "C:\\data\\grants\\nevac\\crimes\\cri.shp"; fileName2 =
		 * "C:\\data\\grants\\esda 07\\oe_data\\race1_00.shp"; ShapefileReader
		 * reader = new ShapefileReader(); DriverProperties dp = new
		 * DriverProperties(fileName2); FeatureCollection featColl = null;
		 * 
		 * try { featColl = reader.read(dp); } catch (IllegalParametersException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * List<Feature> featList = featColl.getFeatures();
		 * 
		 * for (Feature feat : featList) {
		 * 
		 * Geometry geom = (Geometry) feat.getAttribute(0);
		 * //System.out.println(geom.getClass().getName());
		 * 
		 * Java2DConverter converter = new Java2DConverter(new Viewport(app
		 * .getGlassPane())); try { Shape shp = converter.toShape(geom);
		 * Graphics g = app.desktop.getGraphics(); Graphics2D g2 = (Graphics2D)
		 * g; g2.setColor(Color.yellow); g2.fill(shp); } catch
		 * (NoninvertibleTransformException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); } }
		 */
	}

	public ToolkitBeanSet getTBeanSet() {
		return tBeanSet;
	}

	public void setTBeanSet(ToolkitBeanSet beanSet) {
		tBeanSet = beanSet;
	}

	public void internalFrameActivated(InternalFrameEvent arg0) {
		// no-op

	}

	public void internalFrameClosed(InternalFrameEvent arg0) {
		// no-op
	}

	public void internalFrameClosing(InternalFrameEvent arg0) {
		JInternalFrame iFrame = arg0.getInternalFrame();
		ToolkitBean tBean = tBeanSet.getToolkitBean(iFrame);

		deleteBean(tBean);

	}

	public void internalFrameDeactivated(InternalFrameEvent arg0) {
		// no-op

	}

	public void internalFrameDeiconified(InternalFrameEvent arg0) {
		// no-op

	}

	public void internalFrameIconified(InternalFrameEvent arg0) {
		// no-op

	}

	public void internalFrameOpened(InternalFrameEvent arg0) {
		// no-op

	}

	public boolean isUseProj() {
		return useProj;
	}

	public void setUseProj(boolean useProj) {
		this.useProj = useProj;
	}

}
