/* 
 Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 */

package geovista.toolkitcore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.EventListenerList;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import geovista.animation.IndicationAnimator;
import geovista.animation.SelectionAnimator;
import geovista.cartogram.GeoMapCartogram;
import geovista.collaboration.GeoJabber;
import geovista.common.data.DataSetBroadcaster;
import geovista.common.data.DataSetForApps;
import geovista.common.data.DataSetModifiedBroadcaster;
import geovista.common.data.GeoDataSource;
import geovista.common.event.AnnotationEvent;
import geovista.common.event.AnnotationListener;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.IndicationEvent;
import geovista.common.event.IndicationListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;
import geovista.common.event.SubspaceEvent;
import geovista.common.event.SubspaceListener;
import geovista.common.ui.NotePad;
import geovista.common.ui.VariablePicker;
import geovista.coordination.CoordinationManager;
import geovista.coordination.CoordinationUtils;
import geovista.coordination.FiringBean;
import geovista.geoviz.condition.ConditionManager;
import geovista.geoviz.map.GeoMap;
import geovista.geoviz.map.GeoMapUni;
import geovista.geoviz.map.GraduatedSymbolsMap;
import geovista.geoviz.parvis.ParallelPlot;
import geovista.geoviz.radviz.RadViz;
import geovista.geoviz.sample.GeoDataGeneralizedStates;
import geovista.geoviz.scatterplot.SingleHistogram;
import geovista.geoviz.scatterplot.SingleScatterPlot;
import geovista.geoviz.shapefile.ShapeFileDataReader;
import geovista.geoviz.shapefile.ShapeFileProjection;
import geovista.geoviz.spacefill.SpaceFill;
import geovista.geoviz.spreadsheet.TableViewer;
import geovista.geoviz.spreadsheet.VariableTransformer;
import geovista.geoviz.star.StarPlot;
import geovista.geoviz.star.StarPlotMap;
import geovista.matrix.MapAndScatterplotMatrix;
import geovista.matrix.MapMatrix;
import geovista.matrix.MapScatterplotTreemapMatrix;
import geovista.matrix.MultiplotMatrix;
import geovista.matrix.TreemapAndScatterplotMatrix;
import geovista.matrix.map.MoranMap;
import geovista.satscan.Proclude;
import geovista.satscan.SaTScan;
import geovista.sound.SonicClassifier;
import geovista.toolkitcore.data.GeoDataCartogram;
import geovista.toolkitcore.data.GeoDataPennaPCA;
import geovista.toolkitcore.data.GeoDataSCarolina;
import geovista.toolkitcore.data.GeoDataSCarolinaCities;
import geovista.toolkitcore.marshal.Marshaller;
import geovista.touchgraph.LinkGraph;
import geovista.touchgraph.PCAViz;
import geovista.touchgraph.SubspaceLinkGraph;

/**
 * Assumptions: 1. One dataset at a time. 2. Maximum coordination as a default.
 * 
 * @author Frank Hardisty
 */

public class GeoVizToolkit extends JFrame implements ActionListener,
		ComponentListener, InternalFrameListener, AnnotationListener {

	final static Logger logger = Logger
			.getLogger(GeoVizToolkit.class.getName());
	private static String VERSION_NUM = "0.8.5";
	// collection of classes to add
	ArrayList toolMenuList;
	HashMap toolClassHash;

	// collection of active beans
	ToolkitBeanSet tBeanSet;

	// Create JDesktopPane to hold the internal frames
	GvDesktopPane desktop = new GvDesktopPane();

	// managing our layouts

	String filePath = "48States";
	JFileChooser fileChooser;
	ShapeFileDataReader shpRead;
	ShapeFileProjection shpProj;
	DataSetBroadcaster dataCaster;
	CoordinationManager coord;
	DataSetForApps dataSet;
	Vector<DataSetForApps> backgroundDataSets;
	JMenuBar jMenuBar1;
	JMenu menuFile;
	JMenuItem menuItemLoadShp;

	JMenu menuAddTool;
	JMenuItem menuItemLoadStates;
	JMenuItem menuItemLoadSC;
	JMenuItem menuItemLoadSCCities;
	JMenuItem menuItemLoadCartogram;
	JMenuItem menuItemLoadGTD;
	JMenuItem menuItemLoadBackgroundShape;
	JMenuItem menuItemLoadSCBackgroundShape;
	JMenu menuRemoveTool;
	JMenuItem menuItemRemoveAllTools;
	JMenu menuAbout;
	JMenu menuHelp;
	JMenuItem menuItemAboutGeoviz;
	JMenuItem menuItemAboutGeoVista;
	JMenuItem menuItemHelp;
	JMenuItem menuItemOpenLayout;
	JMenuItem menuItemSaveLayout;
	JMenuItem menuItemExit;
	JMenu menuCollaborate;
	JMenuItem menuItemEnableCollaboration;
	JMenuItem menuItemDisableCollaboration;
	JMenuItem menuItemConnect;
	JMenuItem menuItemDisconnect;
	JMenu menuScreenShot;
	JMenuItem menuItemCopyApplicationToClipboard;
	JMenuItem menuItemCopySelectedWindowToClipboard;
	JMenuItem menuItemSaveWholeImageToFile;
	JMenuItem menuItemSaveSelectedWindowToFile;

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
		super("GeoViz Toolkit");
		vizState = new VizState();
		init(fileNameIn, useProj);
	}

	public GeoVizToolkit(String fileNameIn, boolean useProj, boolean useAux) {

		super("GeoViz Toolkit");
		vizState = new VizState();
		init(fileNameIn, useProj);
	}

	private void initMembers() {

		// collection of classes to add
		toolMenuList = new ArrayList();
		toolClassHash = new HashMap();

		// Create JDesktopPane to hold the internal frames
		desktop = new GvDesktopPane();

		// addBindings();
		// managing our layouts
		filePath = "48States";
		shpRead = new ShapeFileDataReader();
		shpProj = new ShapeFileProjection();
		dataCaster = new DataSetBroadcaster();
		coord = new CoordinationManager();

		jMenuBar1 = new JMenuBar();
		menuFile = new JMenu();
		menuItemLoadShp = new JMenuItem();

		menuAddTool = new JMenu();
		menuItemLoadStates = new JMenuItem();
		menuItemLoadSC = new JMenuItem();
		menuItemLoadSCCities = new JMenuItem();
		menuItemLoadCartogram = new JMenuItem();
		menuItemLoadGTD = new JMenuItem();
		menuItemLoadBackgroundShape = new JMenuItem();
		menuItemLoadSCBackgroundShape = new JMenuItem();
		menuRemoveTool = new JMenu();
		menuItemRemoveAllTools = new JMenuItem();
		menuAbout = new JMenu();
		menuHelp = new JMenu();
		menuItemAboutGeoviz = new JMenuItem();
		menuItemAboutGeoVista = new JMenuItem();
		menuItemHelp = new JMenuItem();
		menuItemOpenLayout = new JMenuItem();
		menuItemSaveLayout = new JMenuItem();
		menuItemExit = new JMenuItem();
		menuCollaborate = new JMenu();
		menuItemEnableCollaboration = new JMenuItem();
		menuItemDisableCollaboration = new JMenuItem();
		menuItemConnect = new JMenuItem();
		menuItemDisconnect = new JMenuItem();
		menuScreenShot = new JMenu();
		menuItemCopyApplicationToClipboard = new JMenuItem();
		menuItemCopySelectedWindowToClipboard = new JMenuItem();
		menuItemSaveWholeImageToFile = new JMenuItem();
		menuItemSaveSelectedWindowToFile = new JMenuItem();
	}

	public void init(String fileNameIn, boolean useProj) {
		initMembers();
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

		// loadData(fileNameIn);

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
		if (vizState.beanSet == null) {
			VizState state = ToolkitIO.openDefaultLayout();
			setVizState(state);
		}

		this.repaint();
	}

	void openAllComponents() {
		removeAllBeans();
		VizState state = ToolkitIO.openAllComponentsLayout();
		setVizState(state);
		this.repaint();
	}

	public VizState getVizState() {
		updateVizState();
		return vizState;
	}

	private void updateVizState() {
		vizState.setBeanSet(tBeanSet);
		HashSet<ToolkitBean> beans = tBeanSet.getBeanSet();
		for (ToolkitBean tBean : beans) {
			tBean.zOrder = desktop.getComponentZOrder(tBean.getInternalFrame());
		}
		ToolkitBean tBean = tBeanSet.getToolkitBean(desktop.getSelectedFrame());
		if (tBean == null) {
			vizState.setSelectedBean("");
		} else {
			vizState.setSelectedBean(tBean.getUniqueName());
		}

	}

	void saveProgramState() {
		updateVizState();
		ToolkitIO.saveVizStateToFile(vizState);
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
		if (tBeanSet == null) {
			return;
		}
		Iterator it = tBeanSet.getBeanSet().iterator();
		while (it.hasNext()) {
			ToolkitBean oldBean = (ToolkitBean) it.next();
			removeBeanFromGui(oldBean);
			oldBean = null;
		}
		tBeanSet.clear();

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

	public void setVizState(VizState newState) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		removeAllBeans();
		coord.removeBean(vizState);

		vizState = newState;
		// XXX may be a problem when connections can be cusomized by the user

		vizState.listenerList = new EventListenerList();
		coord.addBean(vizState);

		useProj = vizState.useProj;
		loadData(vizState.dataSource);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		addToolkitBeanSet(vizState.getBeanSet());
		HashSet<ToolkitBean> beans = tBeanSet.getBeanSet();
		for (ToolkitBean tBean : beans) {
			desktop.setComponentZOrder(tBean.getInternalFrame(), tBean.zOrder);
		}

		String uniqueName = vizState.getSelectedBean();
		ToolkitBean tBean = tBeanSet.getToolkitBean(uniqueName);
		if (tBean != null) {
			desktop.setSelectedFrame(tBean.getInternalFrame());

		}
		Object newBean = tBean.getOriginalBean();
		fireNewBeanMethods(newBean);
		setCursor(Cursor.getDefaultCursor());
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

	}

	private void fireNewBeanMethods(Object newBean) {
		if (newBean instanceof DataSetListener) {
			DataSetListener dataListener = (DataSetListener) newBean;
			dataListener.dataSetChanged(new DataSetEvent(dataSet, this));

		}
		if (newBean instanceof SubspaceListener) {
			SubspaceListener selListener = (SubspaceListener) newBean;
			SubspaceEvent e = vizState.getSubspaceEvent();
			if (e.getSubspace() != null) {
				selListener.subspaceChanged(e);
			}
		}

		if (newBean instanceof SelectionListener) {
			SelectionListener selListener = (SelectionListener) newBean;
			SelectionEvent e = vizState.getSelectionEvent();
			if (e.getSelection() != null) {
				selListener.selectionChanged(e);
			}
		}
		if (newBean instanceof IndicationListener) {
			IndicationListener indListener = (IndicationListener) newBean;
			IndicationEvent e = vizState.getIndicationEvent();
			indListener.indicationChanged(e);
		}

	}

	private void addToolkitBeanSet(ToolkitBeanSet beanSet) {
		logger.fine(" addtoolkitbeanset setting tbeanset, size = "
				+ beanSet.getBeanSet().size());
		tBeanSet = beanSet;
		Iterator iter = tBeanSet.iterator();
		while (iter.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) iter.next();
			Object obj = tBean.getOriginalBean();
			coord.addBean(obj);
			addBeanToGui(tBean);
			Object newBean = tBean.getOriginalBean();
			fireNewBeanMethods(newBean);
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
							"<html>"
									+ "GeoViz Toolkit, Version "
									+ VERSION_NUM
									+ "<br>This application developed by Frank Hardisty "
									+ "<br>with contributions by many others. "
									+ "<br> Contributor list here: "
									+ "<br> http://code.google.com/p/geoviz/wiki/GeoVizToolkitContributors </html>");
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

		} else if (e.getSource() == menuItemLoadGTD) {
			loadData("GTD");

		} else if (e.getSource() == menuItemLoadBackgroundShape) {
			openBackgroundShapeFilePicker();
		} else if (e.getSource() == menuItemLoadSCBackgroundShape) {
			loadBackgroundData("SC");
		}

		else if (e.getSource() == menuItemOpenLayout) {
			VizState state = ToolkitIO.getVizState(this);

			if (state == null) {
				return;
			}

			logger.info("about to set VizState");
			setVizState(state);
			logger.info("set VizState");

		} else if (e.getSource() == menuItemSaveLayout) {
			Marshaller marsh = Marshaller.INSTANCE;
			String xml = marsh.toXML(getVizState());
			if (logger.isLoggable(Level.FINEST)) {
				logger.finest(xml);
			}
			ToolkitIO.writeLayout(getFileName(), xml, this);
			// ToolkitIO.writeLayout(getFileName(), tBeanSet, this);

		} else if (toolClassHash.containsKey(e.getSource())) {
			createAndAddBean(e);
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

	private void createAndAddBean(ActionEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		String className = (String) toolClassHash.get(e.getSource());
		ToolkitBean tBean = null;
		tBean = instantiateBean(className);
		addBeanToGui(tBean);
		Object newBean = tBean.getOriginalBean();
		fireNewBeanMethods(newBean);
		tBeanSet.add(tBean);
		setCursor(Cursor.getDefaultCursor());
	}

	private void loadBackgroundData(String name) {
		Object[] newDataSet = createData(name);
		DataSetForApps auxData = new DataSetForApps(newDataSet);
		dataCaster.fireAuxiliaryDataSet(auxData);
	}

	public void loadData(String name) {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		this.repaint();
		vizState.setDataSource(name);
		filePath = name;
		logger
				.fine("geovizdemo, loadData, dataSetIsNull ="
						+ (dataSet == null));
		Object[] newDataSet = createData(name);
		logger
				.fine("geovizdemo, loadData, dataSetIsNull ="
						+ (dataSet == null));
		dataSet = new DataSetForApps(newDataSet);
		File newFile = new File(name);
		if (newFile.canRead()) {
			logger.info(name);
			dataSet.setDataSourceName(name);

		} else {
			dataSet.setDataSourceName("");
		}
		dataCaster.setAndFireDataSet(dataSet);
		logger
				.fine("geovizdemo, loadData, dataSetIsNull ="
						+ (dataSet == null));
		setCursor(Cursor.getDefaultCursor());

	}

	private Object[] createData(String name) {
		if (name == null) {
			return null;
		}
		Object[] newDataSet = null;
		if (name.equals("48States")) {
			GeoDataGeneralizedStates statesData = new GeoDataGeneralizedStates();
			newDataSet = statesData.getDataSet();

		} else if (name.equals("GTD")) {
			String className = "geovista.largedata.GTDReader";
			newDataSet = geoDataFromName(className);
		} else if (name.equals("Purdue")) {
			String className = "geovista.largedata.PurdueDataReader";
			newDataSet = geoDataFromName(className);
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
			// GeoDataNCTC cartogramData = new GeoDataNCTC();
			// newDataSet = cartogramData.getDataSet();

		}

		else if (name.equals("Niger")) {
			String className = "geovista.largedata.GeoDataNiger";

			newDataSet = geoDataFromName(className);
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

	private Object[] geoDataFromName(String className) {
		Object[] newDataSet;
		Class nigerClass;
		Object nigerInstance = null;
		try {
			nigerClass = Class.forName(className);
			nigerInstance = nigerClass.newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GeoDataSource dataSource = (GeoDataSource) nigerInstance;

		newDataSet = dataSource.getDataForApps().getDataObjectOriginal();
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
		menuItemLoadGTD.addActionListener(this);
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

		// Notepad!!!
		addToolToMenu(NotePad.class);
		menuAddTool.addSeparator();

		// data handling components
		addToolToMenu(VariablePicker.class);
		addToolToMenu(TableViewer.class);
		addToolToMenu(VariableTransformer.class);
		menuAddTool.addSeparator();

		// univariate data viz
		addToolToMenu(SingleHistogram.class);
		addToolToMenu(GeoMapUni.class);
		addToolToMenu(SpaceFill.class);
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
		addToolToMenu(GraduatedSymbolsMap.class);
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
		addToolToMenu(MultiplotMatrix.class);
		menuAddTool.addSeparator();

		// dynamic tools
		addToolToMenu(SelectionAnimator.class);
		addToolToMenu(IndicationAnimator.class);
		addToolToMenu(ConditionManager.class);
		menuAddTool.addSeparator();

		// Spatial analysis tools
		addToolToMenu(MoranMap.class);
		// addToolToMenu(MoranMatrix.class);
		addToolToMenu(SaTScan.class);
		addToolToMenu(Proclude.class);
		menuAddTool.addSeparator();

		// other...
		addToolToMenu(GeoJabber.class);

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
		menuItemLoadGTD.setText("Load GTD");
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
		menuCollaborate.setEnabled(true);// until it works
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
		// XXX until it works jMenuBar1.add(menuCollaborate);
		jMenuBar1.add(menuScreenShot);
		jMenuBar1.add(menuAbout);
		jMenuBar1.add(menuHelp);
		menuFile.add(menuItemLoadShp);
		menuFile.add(menuItemLoadStates);
		menuFile.add(menuItemLoadSC);
		menuFile.add(menuItemLoadSCCities);
		menuFile.add(menuItemLoadCartogram);
		menuFile.add(menuItemLoadGTD);
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

	public ToolkitBeanSet getTBeanSet() {
		return tBeanSet;
	}

	public void setTBeanSet(ToolkitBeanSet beanSet) {
		logger.fine("settbeanset setting tbeanset, size = "
				+ beanSet.getBeanSet().size());
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

	public void annotationChanged(AnnotationEvent e) {

	}

	public static void main(String[] args) {

		Logger logger = Logger.getLogger("geovista");
		// Logger mapLogger = Logger.getLogger("geovista.geoviz.map.MapCanvas");
		// logger.setLevel(Level.FINEST);
		// mapLogger.setLevel(Level.FINEST);
		// LogManager mng = LogManager.getLogManager();
		// mng.addLogger(logger);
		// mng.addLogger(mapLogger);

		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST);
		// logger.addHandler(handler);

		try {
			// Create a file handler that write log record to a file called
			// my.log
			FileHandler fHandler = new FileHandler("gv_toolkit.log");
			fHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fHandler);
		} catch (IOException e) {
			System.err.println("could not create log file");
			e.printStackTrace();
		}

		boolean useProj = false;
		boolean useAux = true;
		double tolerance = 0.001;
		ShapeFileDataReader.tolerance = tolerance;
		System.setProperty("swing.aatext", "true");

		try {
			// UIManager.setLookAndFeel(new SubstanceLookAndFeel());

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
			// fileName = "GTD";
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

		// GeoVizToolkit app2 = new GeoVizToolkit(fileName, useProj, useAux);
		// app2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	public static void main2(String[] args) {
		JFrame app = new JFrame();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		URL url = null;
		JEditorPane ep = null;
		try {
			url = new URL("http://en.wikipedia.org/wiki/Philippines");
			ep = new JEditorPane(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app.add(ep);
		app.setVisible(true);
		// String html =

	}
}
