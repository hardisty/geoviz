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

package edu.psu.geovista.toolkitcore;

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
import java.util.logging.ConsoleHandler;
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

import edu.psu.geovista.animation.IndicationAnimator;
import edu.psu.geovista.animation.SelectionAnimator;
import edu.psu.geovista.app.map.MoranMap;
import edu.psu.geovista.app.matrix.MapAndScatterplotMatrix;
import edu.psu.geovista.app.matrix.MapMatrix;
import edu.psu.geovista.app.matrix.MapScatterplotTreemapMatrix;
import edu.psu.geovista.app.matrix.TreemapAndScatterplotMatrix;
import edu.psu.geovista.app.scatterplot.SingleHistogram;
import edu.psu.geovista.app.scatterplot.SingleScatterPlot;
import edu.psu.geovista.app.touchgraph.LinkGraph;
import edu.psu.geovista.app.touchgraph.PCAViz;
import edu.psu.geovista.app.touchgraph.SubspaceLinkGraph;
import edu.psu.geovista.cartogram.GeoMapCartogram;
import edu.psu.geovista.common.data.DataSetBroadcaster;
import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.data.DataSetModifiedBroadcaster;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.ui.VariablePicker;
import edu.psu.geovista.coordination.CoordinationManager;
import edu.psu.geovista.coordination.CoordinationUtils;
import edu.psu.geovista.coordination.FiringBean;
import edu.psu.geovista.data.condition.ConditionManager;
import edu.psu.geovista.data.sample.GeoDataGeneralizedStates;
import edu.psu.geovista.data.shapefile.ShapeFileDataReader;
import edu.psu.geovista.data.shapefile.ShapeFileProjection;
import edu.psu.geovista.geoviz.map.GeoMap;
import edu.psu.geovista.geoviz.map.GeoMapUni;
import edu.psu.geovista.geoviz.parvis.gui.ParallelPlot;
import edu.psu.geovista.geoviz.radviz.RadViz;
import edu.psu.geovista.geoviz.spreadsheet.SpreadSheetBean;
import edu.psu.geovista.geoviz.spreadsheet.VariableTransformer;
import edu.psu.geovista.geoviz.star.StarPlot;
import edu.psu.geovista.geoviz.star.StarPlotMap;
import edu.psu.geovista.satscan.SaTScan;
import edu.psu.geovista.sound.SonicClassifier;
import edu.psu.geovista.toolkitcore.data.GeoDataCartogram;
import edu.psu.geovista.toolkitcore.data.GeoDataPennaPCA;
import edu.psu.geovista.toolkitcore.data.GeoDataSCarolina;
import edu.psu.geovista.toolkitcore.data.GeoDataSCarolinaCities;

/*
 * Assumptions: 1. One dataset at a time. 2. Maximum coordination as a default.
 * 
 * 
 */

public class GeoVizToolkit extends JFrame implements ActionListener,
		ComponentListener, DataSetListener {

	/**
	 * 
	 */
	final static Logger logger = Logger
			.getLogger(GeoVizToolkit.class.getName());
	// collection of classes to add
	ArrayList toolMenuList = new ArrayList();
	HashMap toolClassHash = new HashMap();

	// collection of active beans
	ToolkitBeanSet tBeanSet = new ToolkitBeanSet();

	// Create JDesktopPane to hold the internal frames
	JDesktopPane desktop = new JDesktopPane();

	// managing our layouts

	String filePath = "";
	JFileChooser fileChooser;
	ShapeFileDataReader shpRead = new ShapeFileDataReader();
	ShapeFileProjection shpProj = new ShapeFileProjection();
	DataSetBroadcaster dataCaster = new DataSetBroadcaster();
	CoordinationManager coord = new CoordinationManager();
	DataSetForApps dataSet;
	Vector backgroundDataSets; // every item in this should be a DataSetForApps
	JMenuBar jMenuBar1 = new JMenuBar();
	JMenu menuFile = new JMenu();
	JMenuItem menuItemLoadShp = new JMenuItem();

	JMenu menuAddTool = new JMenu();
	JMenuItem menuItemLoadStates = new JMenuItem();
	JMenuItem menuItemLoadSC = new JMenuItem();
	JMenuItem menuItemLoadSCCities = new JMenuItem();
	JMenuItem menuItemLoadCartogram = new JMenuItem();
	JMenuItem menuItemLoadBackgroundShape = new JMenuItem();
	JMenuItem menuItemLoadSCBackgroundShape = new JMenuItem();
	JMenu menuRemoveTool = new JMenu();
	JMenuItem menuItemRemoveAllTools = new JMenuItem();
	JMenu menuAbout = new JMenu();
	JMenu menuHelp = new JMenu();
	JMenuItem menuItemAboutGeoviz = new JMenuItem();
	JMenuItem menuItemAboutGeoVista = new JMenuItem();
	JMenuItem menuItemHelp = new JMenuItem();
	JMenuItem menuItemOpenLayout = new JMenuItem();
	JMenuItem menuItemSaveLayout = new JMenuItem();
	JMenuItem menuItemExit = new JMenuItem();
	JMenu menuCollaborate = new JMenu();
	JMenuItem menuItemEnableCollaboration = new JMenuItem();
	JMenuItem menuItemDisableCollaboration = new JMenuItem();
	JMenuItem menuItemConnect = new JMenuItem();
	JMenuItem menuItemDisconnect = new JMenuItem();
	JMenu menuScreenShot = new JMenu();
	JMenuItem menuItemCopyApplicationToClipboard = new JMenuItem();
	JMenuItem menuItemCopySelectedWindowToClipboard = new JMenuItem();
	JMenuItem menuItemSaveWholeImageToFile = new JMenuItem();
	JMenuItem menuItemSaveSelectedWindowToFile = new JMenuItem();
	// how about svg and postscript?

	USCHelp help;

	boolean useProj;

	static boolean DEBUG = false;

	public GeoVizToolkit(String fileNameIn) {
		new GeoVizToolkit(fileNameIn, false, true);
	}

	public GeoVizToolkit(String fileNameIn, boolean useProj, boolean useAux) {

		super("GeoViz Toolkit");

		this.desktop.setBackground(new Color(20, 20, 80));
		this.useProj = useProj;
		this.getContentPane().add(desktop, BorderLayout.CENTER);
		coord.addBean(this.dataCaster);
		try {
			this.init();
			this.initMenuListeners();
		} catch (Exception ex1) {
			ex1.printStackTrace();
		}

		if (fileNameIn == null || fileNameIn.equals("")) {
			fileNameIn = "PennaPCA";
		}

		this.loadData(fileNameIn);

		URL urlGif = null;

		ImageIcon im = null;
		try {
			Class cl = this.getClass();
			urlGif = cl.getResource("resources/geoviz_toolkit32.gif");
			im = new ImageIcon(urlGif);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		this.setIconImage(im.getImage());
		this.pack();
		this.setVisible(true);

		this.setExtendedState(Frame.MAXIMIZED_BOTH);

		this.tBeanSet = ToolkitLayoutIO.openStarPlotMapLayout();
		this.addToolkitBeanSet(this.tBeanSet);

		this.repaint();
	}

	public void addExternalBean(Object bean) {
		this.coord.addBean(bean);
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
		Iterator it = this.tBeanSet.iterator();
		while (it.hasNext()) {
			ToolkitBean oldBean = (ToolkitBean) it.next();
			this.removeBeanFromGui(oldBean);
			oldBean = null;
		}
		this.tBeanSet.clear();

	}

	/*
	 * deleting named bean
	 * 
	 */

	public void deleteBean(ToolkitBean oldBean) {
		this.removeBeanFromGui(oldBean);
		this.tBeanSet.remove(oldBean);
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
		this.menuRemoveTool.remove(item);
		this.coord.removeBean(oldBean.getOriginalBean());

		// find area in layout occupied by internal frame
		JInternalFrame iFrame = oldBean.getInternalFrame();
		int iX = iFrame.getX();
		int iY = iFrame.getY();
		int iWidth = iFrame.getWidth();
		int iHeight = iFrame.getHeight();
		// remove it, then repaint area
		this.desktop.remove(oldBean.getInternalFrame());
		this.desktop.repaint(iX, iY, iWidth, iHeight);

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
	 * because we have loaded a layout, then the method in ToolkitLayoutIO is
	 * used.
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
			// edu.psu.geovista.sound.resources

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
		JMenuItem item = newToolkitBean.getRemoveMenuItem();
		item.addActionListener(this);

		this.menuRemoveTool.add(item, 0); // add at the top

		Object newBean = newToolkitBean.getOriginalBean();
		if (newBean instanceof DataSetListener) {
			DataSetListener dataListener = (DataSetListener) newBean;
			dataListener.dataSetChanged(new DataSetEvent(dataSet, this));

		}

	}

	private void addToolkitBeanSet(ToolkitBeanSet beanSet) {
		this.tBeanSet = beanSet;
		Iterator iter = tBeanSet.iterator();
		while (iter.hasNext()) {
			ToolkitBean tBean = (ToolkitBean) iter.next();
			Object obj = tBean.getOriginalBean();
			this.coord.addBean(obj);
			this.addBeanToGui(tBean);

		}

	}

	public void dataSetChanged(DataSetEvent e) {
		this.dataSet = e.getDataSetForApps();
	}

	private void showHelp() {
		// lazy initialize
		if (this.help == null) {
			this.help = new USCHelp();
			this.help.pack();
		}
		boolean haveHelp = false;
		Component[] desktopComponents = desktop.getComponents();
		for (int i = 0; i < desktopComponents.length; i++) {
			if (desktopComponents[i] == this.help) {
				desktop.remove(desktopComponents[i]);
				desktop.add(desktopComponents[i], 0);
				haveHelp = true;
			}
		}
		if (!haveHelp) {
			logger.finest("GVToolkit, showhelp, adding to desktop");
			desktop.add(this.help, 0); // add on top
		}
		this.repaint();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.menuItemAboutGeoVista) {

			JOptionPane.showMessageDialog(this,
					"Most components in this toolkit "
							+ "developed at the GeoVISTA Center.");

		} else if (e.getSource() == this.menuItemAboutGeoviz) {

			JOptionPane
					.showMessageDialog(
							this,
							"This application developed by "
									+ "Frank Hardisty with contributions by Diansheng Guo, Ke Liao, and Aaron Myers "
									+ "at the Univerisity of South Carolina, and many others at the GeoVISTA Center.");
		} else if (e.getSource() == this.menuItemHelp) {
			this.showHelp();
		} else if (e.getSource() == this.menuItemExit) {
			// is this too weak? do we need a system exit?
			this.setVisible(false);
		} else if (e.getSource() == this.menuItemLoadSC) {
			this.loadData("SC");
		} else if (e.getSource() == this.menuItemLoadSCCities) {
			this.loadData("SCCities");

		} else if (e.getSource() == this.menuItemLoadShp) {
			this.openShapefilePicker();
		} else if (e.getSource() == this.menuItemLoadStates) {
			this.loadData("48States");
		} else if (e.getSource() == this.menuItemLoadCartogram) {
			this.loadData("Cartogram");
		} else if (e.getSource() == this.menuItemLoadBackgroundShape) {
			this.openBackgroundShapeFilePicker();
		} else if (e.getSource() == this.menuItemLoadSCBackgroundShape) {
			this.loadBackgroundData("SC");
		}

		else if (e.getSource() == this.menuItemOpenLayout) {

			ToolkitBeanSet tempBeanSet = ToolkitLayoutIO.openLayout(this);
			if (tempBeanSet == null) {
				return;
			}
			this.removeAllBeans();
			this.addToolkitBeanSet(tempBeanSet);

		} else if (e.getSource() == this.menuItemSaveLayout) {

			ToolkitLayoutIO
					.writeLayout(this.getFileName(), this.tBeanSet, this);

		} else if (this.toolClassHash.containsKey(e.getSource())) { // one of
			// our added
			// classes
			String className = (String) toolClassHash.get(e.getSource());
			ToolkitBean tBean = null;
			tBean = this.instantiateBean(className);
			this.addBeanToGui(tBean);
			this.tBeanSet.add(tBean);

		}

		else if (e.getSource() == this.menuItemRemoveAllTools) {
			this.removeAllBeans();
		} else if (e.getSource() instanceof JMenuItem
				&& tBeanSet.contains((JMenuItem) e.getSource())) {
			JMenuItem item = (JMenuItem) e.getSource();

			ToolkitBean oldTool = this.tBeanSet.getToolkitBean(item);
			// assert (item != null);
			this.deleteBean(oldTool); // deleteBean calls removeBeanFromGUI
			oldTool = null; // or should we just let it go out of scope? or do
			// we need to do more?

		} else if (e.getSource() == this.menuItemCopyApplicationToClipboard) {

			ToolkitLayoutIO.copyComponentImageToClipboard(this);
		} else if (e.getSource() == this.menuItemCopySelectedWindowToClipboard) {
			JInternalFrame frame = this.desktop.getSelectedFrame();
			if (frame != null) {
				ToolkitLayoutIO.copyComponentImageToClipboard(frame);
			}

		} else if (e.getSource() == this.menuItemSaveWholeImageToFile) {

			ToolkitLayoutIO.saveImageToFile(this);
		} else if (e.getSource() == this.menuItemSaveSelectedWindowToFile) {
			JInternalFrame frame = this.desktop.getSelectedFrame();
			if (frame != null) {
				ToolkitLayoutIO.saveImageToFile(frame);
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
		Object[] newDataSet = this.createData(name);
		DataSetForApps auxData = new DataSetForApps(newDataSet);
		this.dataCaster.fireAuxiliaryDataSet(auxData);
	}

	public void loadData(String name) {
		Object[] newDataSet = this.createData(name);

		this.dataSet = new DataSetForApps(newDataSet);
		this.dataCaster.setAndFireDataSet(newDataSet);

	}

	private Object[] createData(String name) {
		if (name == null) {
			return null;
		}
		Object[] newDataSet = null;
		if (name.equals("48States")) {
			GeoDataGeneralizedStates statesData = new GeoDataGeneralizedStates();
			Object[] tempDataSet = statesData.getDataSet();

			newDataSet = shpRead.convertShpToShape(tempDataSet);

		} else if (name.equals("SC")) {
			GeoDataSCarolina carolinaData = new GeoDataSCarolina();
			Object[] tempDataSet = carolinaData.getDataSet();
			newDataSet = shpRead.convertShpToShape(tempDataSet);

		} else if (name.equals("SCCities")) {
			GeoDataSCarolinaCities carolinaData = new GeoDataSCarolinaCities();
			Object[] tempDataSet = carolinaData.getDataSet();
			newDataSet = shpRead.convertShpToShape(tempDataSet);

		} else if (name.equals("Cartogram")) {
			GeoDataCartogram cartogramData = new GeoDataCartogram();
			Object[] tempDataSet = cartogramData.getDataSet();
			newDataSet = shpRead.convertShpToShape(tempDataSet);

		} else if (name.equals("PennaPCA")) {
			// XXX hack for testing
			GeoDataPennaPCA pennaPCAData = new GeoDataPennaPCA();
			// GeoDataGeneralizedStates pennaPCAData = new
			// GeoDataGeneralizedStates();
			Object[] tempDataSet = pennaPCAData.getDataSet();
			newDataSet = shpRead.convertShpToShape(tempDataSet);

		} else {
			shpRead.setFileName(name);
			if (this.useProj) {
				this.shpProj.setInputDataSet(shpRead.getDataSet());
				newDataSet = shpProj.getOutputDataSet();
			} else {
				newDataSet = shpRead.getDataSet();
			}
		}

		return newDataSet;

	}

	private void openShapefilePicker() {

		String fileName = ToolkitLayoutIO.getFileName(this,
				ToolkitLayoutIO.ACTION_OPEN,
				ToolkitLayoutIO.FILE_TYPE_SHAPEFILE);

		this.loadData(fileName);
	}

	private void openBackgroundShapeFilePicker() {
		String fileName = ToolkitLayoutIO.getFileName(this,
				ToolkitLayoutIO.ACTION_OPEN,
				ToolkitLayoutIO.FILE_TYPE_SHAPEFILE);

		this.loadBackgroundData(fileName);
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
		this.menuAddTool.add(item); // the menu
		item.addActionListener(this);
		this.toolClassHash.put(item, className); // the menuItem is the key,
		// the classname the value
		this.toolMenuList.add(item);

	}

	public String getFileName() {
		return this.filePath;
	}

	// start component event handling
	public void componentHidden(ComponentEvent e) {
		if (e.getSource() instanceof JInternalFrame
				&& this.tBeanSet.contains((JInternalFrame) e.getSource())) {
			this.deleteBean(this.tBeanSet.getToolkitBean((JInternalFrame) e
					.getSource()));

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
					&& this.tBeanSet.contains((JInternalFrame) e.getSource())) {
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

		this.menuItemAboutGeoVista.addActionListener(this);
		this.menuItemAboutGeoviz.addActionListener(this);
		this.menuItemExit.addActionListener(this);
		this.menuItemLoadSC.addActionListener(this);
		this.menuItemLoadSCCities.addActionListener(this);
		this.menuItemLoadShp.addActionListener(this);
		this.menuItemLoadCartogram.addActionListener(this);
		this.menuItemLoadBackgroundShape.addActionListener(this);
		this.menuItemLoadSCBackgroundShape.addActionListener(this);
		this.menuItemLoadStates.addActionListener(this);
		this.menuItemOpenLayout.addActionListener(this);
		this.menuItemSaveLayout.addActionListener(this);
		this.menuItemRemoveAllTools.addActionListener(this);
		this.menuItemHelp.addActionListener(this);
		this.menuItemEnableCollaboration.addActionListener(this);
		this.menuItemDisableCollaboration.addActionListener(this);
		this.menuItemConnect.addActionListener(this);
		this.menuItemDisconnect.addActionListener(this);
		this.menuItemDisableCollaboration.setEnabled(false);
		this.menuItemConnect.setEnabled(false);
		this.menuItemDisconnect.setEnabled(false);
		this.menuItemSaveWholeImageToFile.addActionListener(this);
		this.menuItemSaveSelectedWindowToFile.addActionListener(this);
		this.menuItemCopyApplicationToClipboard.addActionListener(this);
		this.menuItemCopySelectedWindowToClipboard.addActionListener(this);

		// components are organized by dimension of analysis
		// then by commonality of usage

		// data handling components
		addToolToMenu(VariablePicker.class);
		addToolToMenu(SpreadSheetBean.class);
		addToolToMenu(VariableTransformer.class);
		this.menuAddTool.addSeparator();

		// univariate data viz
		addToolToMenu(SingleHistogram.class);
		addToolToMenu(GeoMapUni.class);
		// addToolToMenu(SpaceFill.class);
		addToolToMenu(SonicClassifier.class);
		addToolToMenu(GeoMapCartogram.class);
		this.menuAddTool.addSeparator();

		// bivariate data viz
		addToolToMenu(SingleScatterPlot.class);
		addToolToMenu(GeoMap.class);
		this.menuAddTool.addSeparator();

		// multivaraite data viz
		addToolToMenu(LinkGraph.class);
		addToolToMenu(ParallelPlot.class);
		addToolToMenu(StarPlot.class);
		addToolToMenu(StarPlotMap.class);
		addToolToMenu(RadViz.class);
		// addToolToMenu(CartogramAndScatterplotMatrix.class);
		// addToolToMenu(CartogramMatrix.class);
		this.menuAddTool.addSeparator();

		// tools that operate on variables
		addToolToMenu(PCAViz.class);
		addToolToMenu(SubspaceLinkGraph.class);
		this.menuAddTool.addSeparator();

		// matrix tools
		// addToolToMenu(TreeMap.class);
		addToolToMenu(MapAndScatterplotMatrix.class);
		addToolToMenu(TreemapAndScatterplotMatrix.class);
		addToolToMenu(MapScatterplotTreemapMatrix.class);
		addToolToMenu(MapMatrix.class);
		this.menuAddTool.addSeparator();

		// dynamic tools
		addToolToMenu(SelectionAnimator.class);
		addToolToMenu(IndicationAnimator.class);
		addToolToMenu(ConditionManager.class);
		this.menuAddTool.addSeparator();

		// Spatial analysis tools
		addToolToMenu(MoranMap.class);
		addToolToMenu(SaTScan.class);
		this.menuAddTool.addSeparator();

		// other...
		// addToolToMenu(GeoJabber.class);

	}

	private void init() throws Exception {
		this.setJMenuBar(jMenuBar1);
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

		Logger logger = Logger.getLogger("edu.psu.geovista");
		Logger mapLogger = Logger
				.getLogger("edu.psu.geovista.geoviz.map.MapCanvas");
		// logger.setLevel(Level.FINEST);
		// mapLogger.setLevel(Level.FINEST);
		// LogManager mng = LogManager.getLogManager();
		// mng.addLogger(logger);
		// mng.addLogger(mapLogger);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.FINEST);
		logger.addHandler(handler);
		mapLogger.addHandler(handler);
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

		try {
			Class beanClass = Class
					.forName("edu.psu.geovista.collaboration.GeoJabber");
			Object beanInstance = beanClass.newInstance();
			logger.finest("yahhh");
			logger.finest("found a " + beanClass.getClass().getCanonicalName());
			logger.finest("found a "
					+ beanInstance.getClass().getCanonicalName());
		} catch (Exception e) {
			logger.finest("waaaa");
			e.printStackTrace();
		}

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
		
/*     until we get jts in maven
		String fileName2 = "C:\\data\\grants\\nevac\\crimes\\cri.shp";
		fileName2 = "C:\\data\\grants\\esda 07\\oe_data\\race1_00.shp";
		ShapefileReader reader = new ShapefileReader();
		DriverProperties dp = new DriverProperties(fileName2);
		FeatureCollection featColl = null;

		try {
			featColl = reader.read(dp);
		} catch (IllegalParametersException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Feature> featList = featColl.getFeatures();

		for (Feature feat : featList) {

			Geometry geom = (Geometry) feat.getAttribute(0);
			//System.out.println(geom.getClass().getName());

			Java2DConverter converter = new Java2DConverter(new Viewport(app
					.getGlassPane()));
			try {
				Shape shp = converter.toShape(geom);
				Graphics g = app.desktop.getGraphics();
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.yellow);
				g2.fill(shp);
			} catch (NoninvertibleTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
*/
	}

}
