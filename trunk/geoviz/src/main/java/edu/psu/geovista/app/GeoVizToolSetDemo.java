/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoVizToolSetDemo
 Copyright (c), 2000, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoVizToolSetDemo.java,v 1.11 2005/02/14 15:42:19 hardisty Exp $
 $Date: 2005/02/14 15:42:19 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------   */
package edu.psu.geovista.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;

import edu.psu.geovista.app.coordinator.CoordinationManagerGUI;
import edu.psu.geovista.app.coordinator.CoordinationUtils;
import edu.psu.geovista.app.map.GeoMap;
import edu.psu.geovista.app.map.GeoMapUni;
import edu.psu.geovista.app.map.MapMatrixElement;
import edu.psu.geovista.app.map.Projection;
import edu.psu.geovista.app.matrix.BiPlotMatrix;
import edu.psu.geovista.app.matrix.FixedRowMatrix;
import edu.psu.geovista.app.parvis.gui.ParallelPlot;
import edu.psu.geovista.app.scatterplot.ScatterPlot;
import edu.psu.geovista.app.scatterplot.SingleScatterPlot;
import edu.psu.geovista.app.spacefill.SpaceFill;
import edu.psu.geovista.app.spacefill.SpaceFillMatrixElement;
import edu.psu.geovista.app.table.TableBrowser;
import edu.psu.geovista.app.touchgraph.LinkGraph;
import edu.psu.geovista.app.touchgraph.SubspaceLinkGraph;
import edu.psu.geovista.classification.ClassifierPicker;
import edu.psu.geovista.data.condition.ConditionManager;
import edu.psu.geovista.data.sample.GeoData48States;
import edu.psu.geovista.data.shapefile.ShapeFileDataReader;
import edu.psu.geovista.data.shapefile.ShapeFileProjection;
import edu.psu.geovista.geoviz.star.StarPlot;
import edu.psu.geovista.sound.SonicClassifier;
import edu.psu.geovista.ui.animation.ConditioningAnimator;
import edu.psu.geovista.ui.animation.IndicationAnimator;
import edu.psu.geovista.ui.animation.SelectionAnimator;
import edu.psu.geovista.visclass.VisualClassifier;


// import junit.framework.*;
public class GeoVizToolSetDemo extends JFrame implements ActionListener {

	// Create JDesktopPane to hold the internal frame
	JDesktopPane desktop = new JDesktopPane();
	protected final static Logger logger = Logger.getLogger(GeoVizToolSetDemo.class.getName());
	boolean useBiMap = true;
	boolean useLinkgraph = true;
	boolean useBiPlot = true;
	boolean usePcp = true;
	boolean useTable = true;
	boolean useUniMap = true;
	boolean useSubspaceGraph = true;
	boolean useIndAdim = true;
	boolean useSelAnim = true;
	boolean useConditioningAnim = false;
	boolean useSpaceMap = false;
	boolean useSpaceFill = false;
	boolean useScatterSpace = false;
	boolean useSpaceMatrix = false;
	boolean useFixedRow = true;
	boolean useSingleScatter = false;
	boolean useCondManager = true;
	boolean useColorBrewer = false;
	boolean useVC = false;
	boolean useFileChooser = true;
	boolean useIndicationSonifier = false;
	boolean useIndicationSonifier2 = false;
	boolean useIndicationSonifier3 = false;
	boolean useIndicationSonifier4 = false;
	boolean useDimSpeaker = false; // needs external libraries
	boolean useStarPlot = true;
	JInternalFrame iBiMap;
	JInternalFrame iLinkgraph;
	JInternalFrame iBiPlot;
	JInternalFrame iPcp;
	JInternalFrame iCoord;
	JInternalFrame iTable;
	JInternalFrame iUniMap;
	JInternalFrame iVarGraph;
	JInternalFrame iIndAnim;
	JInternalFrame iSelAnim;
	JInternalFrame iConditioningAnim;
	JInternalFrame iSpaceMap;
	JInternalFrame iSpaceFill;
	JInternalFrame iScatterSpace;
	JInternalFrame iSpaceMatrix;
	JInternalFrame iFixedRow;
	JInternalFrame iSingleScatter;
	JInternalFrame iCondManager;
	JInternalFrame iColorBrewer;
	JInternalFrame iVC;
	JInternalFrame iFileChooser;
	JInternalFrame iIndicationSonifier;
	JInternalFrame iIndicationSonifier2;
	JInternalFrame iIndicationSonifier3;
	JInternalFrame iIndicationSonifier4;
	JInternalFrame iDimSpeaker;
	JInternalFrame iStarPlot;

	BiPlotMatrix scatterMap;
	ParallelPlot pcp;
	GeoMap biMap;
	SelectionAnimator selAnim;
	JFileChooser fileChooser;
	ShapeFileDataReader shpRead;
	ShapeFileProjection shpProj;

	public GeoVizToolSetDemo(String fileName, boolean useProj) {
		super("GeoViz Toolkit");
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
		init(fileName, useProj);

		// super();
	}

	public void init(String fileName, boolean useProj) {
		boolean useAux = true;

		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.getl
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		shpRead = new ShapeFileDataReader();

		this.getContentPane().add(desktop, BorderLayout.CENTER);

		// iBiMap.getContentPane().add(new ParallelPlot());
		this.setVisible(true);

		biMap = new GeoMap();

		GeoMapUni uniMap = new GeoMapUni();
		SubspaceLinkGraph subspaceGraph = new SubspaceLinkGraph();
		LinkGraph touchGraph = new LinkGraph();
		pcp = new ParallelPlot();

		TableBrowser table = new TableBrowser();
		IndicationAnimator indAnim = new IndicationAnimator();
		selAnim = new SelectionAnimator();

		CoordinationManagerGUI coord = new CoordinationManagerGUI();

		shpProj = new ShapeFileProjection();
		GeoData48States stateData = new GeoData48States();

		scatterMap = new BiPlotMatrix();
		scatterMap.setElementClass2(new MapMatrixElement());
		scatterMap.setElementClass1(new ScatterPlot());
		scatterMap.setBackground(Color.black);

		SpaceFillMatrixElement spaceElem = new SpaceFillMatrixElement();
		MapMatrixElement mapElem = new MapMatrixElement();
		ScatterPlot sp = new ScatterPlot();
		BiPlotMatrix scatterSpace = new BiPlotMatrix();
		scatterSpace.setElementClass2(spaceElem);
		scatterSpace.setElementClass1(sp);
		scatterSpace.setBackground(Color.black);

		BiPlotMatrix spaceMatrix = new BiPlotMatrix();
		spaceMatrix.setElementClass2(spaceElem);
		spaceMatrix.setElementClass1(spaceElem);
		spaceMatrix.setBackground(Color.black);

		FixedRowMatrix fixedRow = new FixedRowMatrix();
		fixedRow.setElementClass0(sp);
		fixedRow.setElementClass1(mapElem);
		fixedRow.setElementClass2(spaceElem);

		BiPlotMatrix spaceMap = new BiPlotMatrix();
		spaceMap.setElementClass1(mapElem);
		spaceMap.setElementClass2(spaceElem);
		spaceMap.setBackground(Color.black);

		SpaceFill spaceFill = new SpaceFill();
		SingleScatterPlot singleScatter = new SingleScatterPlot();

		ConditioningAnimator conditioningAnim = new ConditioningAnimator();

		ConditionManager condManager = new ConditionManager();

		// BigPanel colorBrewer = new BigPanel();
		VisualClassifier vc = null;
		vc = new VisualClassifier();
		vc
				.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
		SonicClassifier sc = null;

		SonicClassifier sc2 = null;
		if (this.useIndicationSonifier2) {
			sc2 = new SonicClassifier();
			sc2
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
		}
		SonicClassifier sc3 = null;

		SonicClassifier sc4 = null;

		// fileChooser = null;

		coord.addBean(shpProj);
		if (this.useBiMap) {
			coord.addBean(biMap);
		}
		//
		if (this.useUniMap) {
			coord.addBean(uniMap);
		}
		if (this.useSpaceMap) {
			coord.addBean(spaceMap);
		}
		if (this.useLinkgraph) {
			coord.addBean(touchGraph);
		}
		if (this.useBiPlot) {
			coord.addBean(scatterMap);
		}
		if (this.usePcp) {
			coord.addBean(pcp);
		}
		//
		if (this.useTable) {
			coord.addBean(table);
		}
		if (this.useSingleScatter) {
			coord.addBean(singleScatter);
		}

		if (this.useSubspaceGraph) {
			coord.addBean(subspaceGraph);
		}
		if (this.useIndAdim) {
			coord.addBean(indAnim);
		}
		if (this.useSelAnim) {
			coord.addBean(selAnim);
		}
		if (this.useSpaceFill) {
			coord.addBean(spaceFill);
		}
		if (this.useScatterSpace) {
			coord.addBean(scatterSpace);
		}
		if (this.useSpaceMatrix) {
			coord.addBean(spaceMatrix);
		}
		if (this.useConditioningAnim) {
			coord.addBean(conditioningAnim);
		}
		if (this.useFixedRow) {
			coord.addBean(fixedRow);
		}
		if (this.useCondManager) {
			coord.addBean(condManager);
		}
//		if (this.useColorBrewer) {
//			coord.addBean(colorBrewer);
//		}
		if (this.useVC) {
			coord.addBean(vc);
		}
		if (this.useIndicationSonifier) {
			sc = new SonicClassifier();
			sc
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
			coord.addBean(sc);
		}
		if (this.useIndicationSonifier2) {
			coord.addBean(sc2);
		}
		if (this.useIndicationSonifier3) {
			sc3 = new SonicClassifier();
			sc3
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
			coord.addBean(sc3);
		}
		if (this.useIndicationSonifier4) {
			sc4 = new SonicClassifier();
			sc4
					.setVariableChooserMode(ClassifierPicker.VARIABLE_CHOOSER_MODE_ACTIVE);
			coord.addBean(sc4);
		}
		StarPlot starPlot = null;
		if (useStarPlot) {
			starPlot = new StarPlot();
			coord.addBean(starPlot);
		}
		// DimensionSpeaker dimSpeaker = null;
//		if (this.useDimSpeaker) {
//			dimSpeaker = new DimensionSpeaker();
//			coord.addBean(dimSpeaker);
//		}

		if (fileName.length() == 0) {
			// stateData.addActionListener(shpProj);
			shpProj.setInputDataSet(shpRead.convertShpToShape(stateData
					.getDataSet()));
		} else {
			shpRead.setFileName(fileName, ShapeFileDataReader.FILE_TYPE_DBF);
			shpProj.setInputDataSet(shpRead.getDataSet());
		}

		this.getContentPane().setLayout(
				new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));

		Image im = null;
		Icon ic = null;

		// make an internal frame for our coord
		iCoord = new JInternalFrame("Event Coordinator ", true, false, true,
				true);
		iCoord.setLocation(5, 125);
		iCoord.setSize(180, 380);
		desktop.add(iCoord);
		iCoord.setVisible(true);
		im = CoordinationUtils.findSmallIcon(coord);
		ic = new ImageIcon(im);
		iCoord.setFrameIcon(ic);
		iCoord.getContentPane().add(coord);
		iCoord.revalidate();

		// make an internal frame for our indAnim
		if (this.useIndAdim) {
			iIndAnim = new JInternalFrame("Indication Animator", true, false,
					true, true);
			iIndAnim.setLocation(5, 5);
			iIndAnim.setSize(420, 120);
			desktop.add(iIndAnim);
			iIndAnim.setVisible(true);
			im = CoordinationUtils.findSmallIcon(indAnim);
			ic = new ImageIcon(im);
			iIndAnim.setFrameIcon(ic);
			iIndAnim.getContentPane().add(indAnim);
			iIndAnim.revalidate();
		}
		// make an internal frame for our selAnim
		if (this.useSelAnim) {
			iSelAnim = new JInternalFrame("Selection Animator", true, false,
					true, true);
			iSelAnim.setLocation(5, 540);
			iSelAnim.setSize(420, 120);
			desktop.add(iSelAnim);
			iSelAnim.setVisible(true);
			im = CoordinationUtils.findSmallIcon(selAnim);
			ic = new ImageIcon(im);
			iSelAnim.setFrameIcon(ic);
			iSelAnim.getContentPane().add(selAnim);
			iSelAnim.revalidate();
		}

		// make an internal frame for our conditioningAnim
		if (this.useConditioningAnim) {
			iConditioningAnim = new JInternalFrame("Conditioning Animator",
					true, false, true, true);
			iConditioningAnim.setLocation(5, 540);
			iConditioningAnim.setSize(420, 120);
			desktop.add(iConditioningAnim);
			iConditioningAnim.setVisible(true);
			im = CoordinationUtils.findSmallIcon(conditioningAnim);
			ic = new ImageIcon(im);
			iConditioningAnim.setFrameIcon(ic);
			iConditioningAnim.getContentPane().add(conditioningAnim);
			iConditioningAnim.revalidate();
		}
		if (this.useTable) {
			// make an internal frame for our table
			iTable = new JInternalFrame("Table Browser", true, false, true,
					true);
			iTable.setLocation(5, 5);
			iTable.setSize(475, 513);
			desktop.add(iTable);
			iTable.setVisible(true);
			im = CoordinationUtils.findSmallIcon(table);
			ic = new ImageIcon(im);
			iTable.setFrameIcon(ic);
			iTable.getContentPane().add(table);
			iTable.revalidate();
		}

		// make an internal frame for our biMap
		if (this.useBiMap) {
			iBiMap = new JInternalFrame("Bivariate Map", true, false, true,
					true);
			iBiMap.setLocation(485, 5);
			iBiMap.setSize(533, 513);
			desktop.add(iBiMap);
			iBiMap.setVisible(true);
			im = CoordinationUtils.findSmallIcon(biMap);
			ic = new ImageIcon(im);
			iBiMap.setFrameIcon(ic);
			iBiMap.getContentPane().add(biMap);
			iBiMap.revalidate();
		}
		// make an internal frame for our touchGraph
		if (this.useLinkgraph) {
			iLinkgraph = new JInternalFrame("Link Graph", true, false, true,
					true);
			iLinkgraph.setLocation(5, 5);
			iLinkgraph.setSize(475, 513);
			desktop.add(iLinkgraph);
			iLinkgraph.setVisible(true);
			im = CoordinationUtils.findSmallIcon(touchGraph);
			ic = new ImageIcon(im);
			iLinkgraph.setFrameIcon(ic);
			iLinkgraph.getContentPane().add(touchGraph);
			iLinkgraph.revalidate();
		}
		if (this.useSubspaceGraph) {
			// make an internal frame for our subspaceGraph
			iVarGraph = new JInternalFrame("Subspace Link Graph", true, false,
					true, true);
			iVarGraph.setLocation(5, 5);
			iVarGraph.setSize(475, 513);
			desktop.add(iVarGraph);
			iVarGraph.setVisible(true);
			im = CoordinationUtils.findSmallIcon(subspaceGraph);
			ic = new ImageIcon(im);
			iVarGraph.setFrameIcon(ic);
			iVarGraph.getContentPane().add(subspaceGraph);
			iVarGraph.revalidate();
		}
		// make an internal frame for our scatterMap
		if (this.useBiPlot) {
			iBiPlot = new JInternalFrame("Map and Scatterplot Matrix", true,
					false, true, true);
			iBiPlot.setLocation(435, 5);
			iBiPlot.setSize(583, 613);
			desktop.add(iBiPlot);
			iBiPlot.setVisible(true);
			im = CoordinationUtils.findSmallIcon(scatterMap);
			ic = new ImageIcon(im);
			iBiPlot.setFrameIcon(ic);
			iBiPlot.getContentPane().add(scatterMap);
			iBiPlot.revalidate();
		}
		// scatterMap.setSelOriginalColorMode(false);

		// make an internal frame for our spaceMap
		if (this.useSpaceMap) {
			iSpaceMap = new JInternalFrame("Spacefill and Map Matrix", true,
					false, true, true);
			iSpaceMap.setLocation(435, 5);
			iSpaceMap.setSize(583, 613);
			desktop.add(iSpaceMap);
			iSpaceMap.setVisible(true);
			im = CoordinationUtils.findSmallIcon(spaceMap);
			ic = new ImageIcon(im);
			iSpaceMap.setFrameIcon(ic);
			iSpaceMap.getContentPane().add(spaceMap);
			iSpaceMap.revalidate();
		}

		// make an internal frame for our pcp
		if (this.usePcp) {
			iPcp = new JInternalFrame("Parallel Coordinate Plot", true, false,
					true, true);
			iPcp.setLocation(5, 5);
			iPcp.setSize(425, 513);
			desktop.add(iPcp);
			iPcp.setVisible(true);
			im = CoordinationUtils.findSmallIcon(pcp);
			ic = new ImageIcon(im);
			iPcp.setFrameIcon(ic);
			iPcp.getContentPane().add(pcp);
			iPcp.revalidate();
		}

		// make an internal frame for our uniMap
		if (this.useUniMap) {
			iUniMap = new JInternalFrame("Univariate Map", true, false, true,
					true);
			iUniMap.setLocation(485, 5);
			iUniMap.setSize(533, 513);
			desktop.add(iUniMap);
			iUniMap.setVisible(true);
			im = CoordinationUtils.findSmallIcon(uniMap);
			ic = new ImageIcon(im);
			iUniMap.setFrameIcon(ic);
			iUniMap.getContentPane().add(uniMap);
			iUniMap.revalidate();
		}

		// make an internal frame for our spaceFill
		if (this.useSpaceFill) {
			iSpaceFill = new JInternalFrame("Space Filling", true, false, true,
					true);
			iSpaceFill.setLocation(485, 5);
			iSpaceFill.setSize(533, 513);
			desktop.add(iSpaceFill);
			iSpaceFill.setVisible(true);
			im = CoordinationUtils.findSmallIcon(spaceFill);
			ic = new ImageIcon(im);
			iSpaceFill.setFrameIcon(ic);
			iSpaceFill.getContentPane().add(spaceFill);
			iSpaceFill.revalidate();
		}

		// make an internal frame for our singleScatter
		if (this.useSingleScatter) {
			iSingleScatter = new JInternalFrame("Scatter Plot", true, false,
					true, true);
			iSingleScatter.setLocation(485, 5);
			iSingleScatter.setSize(533, 513);
			desktop.add(iSingleScatter);
			iSingleScatter.setVisible(true);
			im = CoordinationUtils.findSmallIcon(singleScatter);
			ic = new ImageIcon(im);
			iSingleScatter.setFrameIcon(ic);
			iSingleScatter.getContentPane().add(singleScatter);
			singleScatter.setBackground(Color.BLACK); // paint it black
			iSingleScatter.revalidate();
		}

		// make an internal frame for our scatterSpace
		if (this.useScatterSpace) {
			iScatterSpace = new JInternalFrame(
					"Scatterplot and Spacefill Matrix", true, false, true, true);
			iScatterSpace.setLocation(485, 5);
			iScatterSpace.setSize(533, 513);
			desktop.add(iScatterSpace);
			iScatterSpace.setVisible(true);
			im = CoordinationUtils.findSmallIcon(scatterSpace);
			ic = new ImageIcon(im);
			iScatterSpace.setFrameIcon(ic);
			iScatterSpace.getContentPane().add(scatterSpace);
			iScatterSpace.revalidate();
		}

		// make an internal frame for our spaceMatrix
		if (this.useSpaceMatrix) {
			iSpaceMatrix = new JInternalFrame("Space Filling", true, false,
					true, true);
			iSpaceMatrix.setLocation(485, 5);
			iSpaceMatrix.setSize(533, 513);
			desktop.add(iSpaceMatrix);
			iSpaceMatrix.setVisible(true);
			im = CoordinationUtils.findSmallIcon(spaceMatrix);
			ic = new ImageIcon(im);
			iSpaceMatrix.setFrameIcon(ic);
			iSpaceMatrix.getContentPane().add(spaceMatrix);
			iSpaceMatrix.revalidate();
		}

		// make an internal frame for our fixedRow
		if (this.useFixedRow) {
			iFixedRow = new JInternalFrame("Small Multiples", true, false,
					true, true);
			iFixedRow.setLocation(485, 5);
			iFixedRow.setSize(533, 513);
			desktop.add(iFixedRow);
			iFixedRow.setVisible(true);
			im = CoordinationUtils.findSmallIcon(fixedRow);
			ic = new ImageIcon(im);
			iFixedRow.setFrameIcon(ic);
			iFixedRow.getContentPane().add(fixedRow);
			iFixedRow.revalidate();
		}

		// make an internal frame for our condManager
		if (this.useCondManager) {
			iCondManager = new JInternalFrame("Conditioning Manager", true,
					false, true, true);
			iCondManager.setLocation(485, 5);
			iCondManager.setSize(233, 213);
			desktop.add(iCondManager);
			iCondManager.setVisible(true);
			im = CoordinationUtils.findSmallIcon(condManager);
			ic = new ImageIcon(im);
			iCondManager.setFrameIcon(ic);
			iCondManager.getContentPane().add(condManager);
			iCondManager.revalidate();
		}

		// //make an internal frame for our colorBrewer
		// if (this.useColorBrewer) {
		// iColorBrewer = new JInternalFrame("Color Brewer", true, false, true,
		// true);
		// iColorBrewer.setLocation(485, 5);
		// iColorBrewer.setSize(100, 413);
		// desktop.add(iColorBrewer);
		// iColorBrewer.setVisible(true);
		// im = CoordinationUtils.findSmallIcon(colorBrewer);
		// ic = new ImageIcon(im);
		// iColorBrewer.setFrameIcon(ic);
		// iColorBrewer.getContentPane().add(colorBrewer);
		// iColorBrewer.revalidate();
		// }

		// make an internal frame for our vc
		if (this.useVC) {
			iVC = new JInternalFrame("VisualClassifier", true, false, true,
					true);
			iVC.setLocation(485, 5);
			iVC.setSize(100, 413);
			desktop.add(iVC);
			iVC.setVisible(true);
			im = CoordinationUtils.findSmallIcon(vc);
			ic = new ImageIcon(im);
			iVC.setFrameIcon(ic);
			iVC.getContentPane().add(vc);
			iVC.revalidate();
		}
		// //make an internal frame for our inidicationSonifier(s)
		if (this.useIndicationSonifier) {
			iIndicationSonifier = new JInternalFrame("Indication Sonifier",
					true, false, true, true);
			iIndicationSonifier.setLocation(485, 5);
			iIndicationSonifier.setSize(413, 100);
			desktop.add(iIndicationSonifier);
			iIndicationSonifier.setVisible(true);
			im = CoordinationUtils.findSmallIcon(sc);
			ic = new ImageIcon(im);
			iIndicationSonifier.setFrameIcon(ic);
			iIndicationSonifier.getContentPane().add(sc);
			iIndicationSonifier.revalidate();
		}
		if (this.useIndicationSonifier2) {
			iIndicationSonifier2 = new JInternalFrame("Indication Sonifier2",
					true, false, true, true);
			iIndicationSonifier2.setLocation(485, 105);
			iIndicationSonifier2.setSize(413, 100);
			desktop.add(iIndicationSonifier2);
			iIndicationSonifier2.setVisible(true);
			im = CoordinationUtils.findSmallIcon(sc2);
			ic = new ImageIcon(im);
			iIndicationSonifier2.setFrameIcon(ic);
			iIndicationSonifier2.getContentPane().add(sc2);
			iIndicationSonifier2.revalidate();
		}
		if (this.useIndicationSonifier3) {
			iIndicationSonifier3 = new JInternalFrame("Indication Sonifier3",
					true, false, true, true);
			iIndicationSonifier3.setLocation(485, 205);
			iIndicationSonifier3.setSize(413, 100);
			desktop.add(iIndicationSonifier3);
			iIndicationSonifier3.setVisible(true);
			im = CoordinationUtils.findSmallIcon(sc3);
			ic = new ImageIcon(im);
			iIndicationSonifier3.setFrameIcon(ic);
			iIndicationSonifier3.getContentPane().add(sc3);
			iIndicationSonifier3.revalidate();
		}
		if (this.useIndicationSonifier4) {
			iIndicationSonifier4 = new JInternalFrame("Indication Sonifier4",
					true, false, true, true);
			iIndicationSonifier4.setLocation(485, 305);
			iIndicationSonifier4.setSize(413, 100);
			desktop.add(iIndicationSonifier4);
			iIndicationSonifier4.setVisible(true);
			im = CoordinationUtils.findSmallIcon(sc4);
			ic = new ImageIcon(im);
			iIndicationSonifier4.setFrameIcon(ic);
			iIndicationSonifier4.getContentPane().add(sc4);
			iIndicationSonifier4.revalidate();
		}
		if (this.useDimSpeaker) {
			iDimSpeaker = new JInternalFrame("Dimension Speaker", true, false,
					true, true);
			iDimSpeaker.setLocation(485, 305);
			iDimSpeaker.setSize(213, 100);
			desktop.add(iDimSpeaker);
			iDimSpeaker.setVisible(true);
			// im = CoordinationUtils.findSmallIcon(dimSpeaker);
			ic = new ImageIcon(im);
			iDimSpeaker.setFrameIcon(ic);
			// iDimSpeaker.getContentPane().add(dimSpeaker);
			iDimSpeaker.revalidate();
		}
		if (this.useStarPlot) {

			// make an internal frame for our fileChooser
			iStarPlot = new JInternalFrame("StarPlot", true, false, true, true);
			iStarPlot.setLocation(485, 405);
			iStarPlot.setSize(400, 430);
			desktop.add(iStarPlot);
			iStarPlot.setVisible(true);
			im = CoordinationUtils.findSmallIcon(starPlot);
			ic = new ImageIcon(im);
			iStarPlot.setFrameIcon(ic);
			iStarPlot.getContentPane().add(starPlot);
			iStarPlot.revalidate();
		}
		// init fileChooser
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			fileChooser = new JFileChooser("");
			// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (this.useFileChooser) {
			fileChooser.addActionListener(this);
			// make an internal frame for our fileChooser
			iFileChooser = new JInternalFrame("FileChooser", true, false, true,
					true);
			iFileChooser.setLocation(485, 5);
			iFileChooser.setSize(300, 350);
			desktop.add(iFileChooser);
			iFileChooser.setVisible(true);
			im = CoordinationUtils.findSmallIcon(fileChooser);
			ic = new ImageIcon(im);
			iFileChooser.setFrameIcon(ic);
			iFileChooser.getContentPane().add(fileChooser);
			iFileChooser.revalidate();
		}

		if (useAux) {
			ShapeFileProjection shpProj2 = new ShapeFileProjection();
			Projection proj = shpProj.getProj();
			shpProj2.setProj(proj);

			// ShapeFileToShape shpToShape2 = new ShapeFileToShape();
			// shpToShape2.setInputDataSet(stateData.getDataSet());
			ShapeFileDataReader reader = new ShapeFileDataReader();
			Object[] dataShapes = reader.convertShpToShape(stateData
					.getDataSet());
			shpProj2.setInputAuxiliaryData(dataShapes);

			biMap.setAuxiliarySpatialData(shpProj2.getOutputAuxiliarySpatialDataForApps());
			
			uniMap.setAuxiliarySpatialData(shpProj2.getOutputAuxiliarySpatialDataForApps());
					
		}

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.fileChooser
				&& e.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
			logger.finest("GeoVizToolSetDemo, command = " +e.getActionCommand());
			File f = fileChooser.getSelectedFile();
			this.shpRead.setFileName(f.getAbsolutePath());
			shpProj.setInputDataSet(shpRead.getDataSet());

		}
	}

	private void iconifyWindows() {
		// try {
		// if (this.useBiMap){
		// this.iBiMap.setIcon(true);
		// }
		//
		// //this.iUniMap.setIcon(true);
		// this.iBiPlot.setIcon(true);
		// this.iPcp.setIcon(true);
		// this.iLinkgraph.setIcon(true);
		// this.iVarGraph.setIcon(true);
		//
		// this.iSpaceFill.setIcon(true);
		// this.iScatterSpace.setIcon(true);
		// }
		// catch (PropertyVetoException ex) {
		// ex.printStackTrace();
		// }

		// special stuff for the scatterMap
		// BivariateColorSymbolClassificationSimple biColor =
		// (BivariateColorSymbolClassificationSimple)biMap.getBivariateColorSymbolClassification();
		// BivariateColorSymbolClassificationSimple biColor =
		// new BivariateColorSymbolClassificationSimple();
		// BivariateColorSymbolClassificationSimple biColor2 =
		// new BivariateColorSymbolClassificationSimple();
		// ColorSymbolizer xSym = biColor.getColorerX();
		// ColorSymbolizer ySym = biColor.getColorerY();
		// biColor2.setColorerX(ySym);
		// biColor2.setColorerY(xSym);
		// force a subspace event
		// SubspaceEvent evnt = new SubspaceEvent(this,);
		// this.scatterMap.fireSubspaceChanged(new int[] {1,2,3});
		// this.pcp.subspaceChanged(evnt);
		// this.selAnim.subspaceChanged(evnt);
		// this.scatterMap.setBivarColorClasser(biColor2);
	}

	public static void main(String[] args) {
		String fileName = null;

		if (args.length == 0) {
			// fileName = "c:\\dc_tracts\\48States.shp";
			fileName = "";
		} else {
			fileName = args[0];
		}

		logger.finest("File name = " + fileName);

		boolean useProj = true;

		if (args.length >= 2) {
			String arg2 = args[1];

			if (arg2.compareToIgnoreCase("false") == 0) {
				useProj = false;
			}
		}

		GeoVizToolSetDemo app = new GeoVizToolSetDemo(fileName, useProj);
		app.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// app.setSize(1024,768);
		app.setVisible(true);

		// JFrame app = new JFrame("Map with TouchGraph");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.iconifyWindows();
	}
}
