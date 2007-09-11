/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SpaceFillCanvas
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: xpdai $
 $Id: SpaceFillCanvas.java,v 1.6 2003/09/05 13:02:45 xpdai Exp $
 $Date: 2003/09/05 13:02:45 $
 Reference:        Document no:
 ___                ___
 -------------------------------------------------------------------  *

 */
package edu.psu.geovista.app.spacefill;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.data.ArraySort2D;
import edu.psu.geovista.data.geog.DataSetForApps;
import edu.psu.geovista.symbolization.AffineTransformModifier;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import edu.psu.geovista.symbolization.ColorSymbolClassification;
import edu.psu.geovista.symbolization.ColorSymbolClassificationSimple;
import edu.psu.geovista.ui.event.IndicationEvent;
import edu.psu.geovista.ui.event.IndicationListener;
import edu.psu.geovista.ui.event.SelectionEvent;
import edu.psu.geovista.ui.event.SelectionListener;


public class SpaceFillCanvas extends JPanel implements MouseListener,
                                                       MouseMotionListener,
                                                       ComponentListener,
                                                       SelectionListener

{
	protected final static Logger logger = Logger.getLogger(SpaceFillCanvas.class.getName());
  private static final int COLUMN_ORIGINAL_DATA = 0; //for use with pixelOrder
  private static final int COLUMN_ORIGINAL_ORDER = 1; //for use with pixelOrder
  private static final int COLUMN_DATA_ORDER = 2; //for use with pixelOrder
  private static final int NUM_DATA_COLUMNS = 3; //for use with pixelOrder
  public static final String COMMAND_SELECTION = "cmdSel";
  public static final String COMMAND_COLOR_CLASSFICIATION = "colorClass";
   private AffineTransform xform;
  transient private int[] pixelColors; //reflected in pixelBuff
  transient private int[] pixelColorsOriginal; //reflected in pixelBuff
  transient private String[] observationNames;
  transient private double[][] pixelOrder; // row, column //reflected in pixelBuff
  transient private int[][] pixelIndex; //reflected in pixelBuff
  private int[] selectedObservations;
  transient private int[] conditioning;
   private int[] selectedObservationsOld;
  transient private int mouseX1;
  transient private int mouseX2;
  transient private int mouseY1;
  transient private int mouseY2;
  private int indication;
  private Color[] colors;
  private int fillOrder;
  private Object[] data;
  private DataSetForApps dataSet;
  private String[] variableNames;
  private BufferedImage pixelBuff;
  private Image drawingBuff;
  private int currOrderColumn;
  private int currColorColumn;

  //Colors
  private Color colorSelection;
  private boolean selOriginalColorMode;
  private Color colorIndication;
  private Color colorNull;
  private Color colorOutOfFocus;
  private Color colorNotInStudyArea;

  //transient private BivariateColorSymbolClassification bivarColorClasser;
  transient private ColorSymbolClassification colorClasser;
  private Shape[] originalSpatialData;
  private Shape[] drawingShapes;
  private boolean useDrawingShapes;

  private int borderThickness = 1;

  public SpaceFillCanvas() {
    this.indication = Integer.MIN_VALUE;
    this.setBorder(BorderFactory.createLineBorder(Color.black,this.borderThickness));
    ColorSymbolClassificationSimple biColorer = new ColorSymbolClassificationSimple();
    this.colorClasser = biColorer;

    this.useDrawingShapes = true;
    this.observationNames = null;
    this.fillOrder = FillOrder.FILL_ORDER_SCAN_LINE;

    this.selectedObservationsOld = new int[0];
    this.selectedObservations = new int[0];
    this.makeBuff(1, 1);
    this.xform = new AffineTransform();
    this.initColors();
    //this.setMinimumSize(new Dimension(50, 50));
    this.setPreferredSize(new Dimension(300, 300));
    this.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    currOrderColumn = -1;
    this.currColorColumn = -1;
    this.addMouseListener(this);
    addMouseMotionListener(this);

    this.addComponentListener(this);

    //orders to implement (in order): Morton
  }

  //
  private void initColors() {
    //let's set default colors
    //in some far-off happy day this will be set from user prefs
    colorSelection = Color.blue;
    colorIndication = Color.green;
    colorNull = Color.white;
    colorOutOfFocus = this.getBackground();
    colorNotInStudyArea = Color.black;
    this.colors = null; //new Color[1000];
  }

  private void makeBuff(int width, int height) {
    pixelBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  private void makeXform() {
    xform.setToIdentity();
    double sx = (double) (this.getWidth())/ (double) pixelBuff.getWidth();
    double sy = (double) (this.getHeight()) / (double) pixelBuff.getHeight();
    xform.setToScale(sx, sy);

  }

  private void refreshMembers() {
    if (data == null) {
      return;
    } else if (this.currOrderColumn == -1) { //no numeric data

      return;
    }

    int len = this.dataSet.getNumObservations();

    //init bufferedImage if need be
    int height = this.pixelIndex.length; //nRows
    int width = this.pixelIndex[0].length; //nColumns

    if ((this.pixelBuff.getWidth() != width) ||
          (this.pixelBuff.getHeight() != height)) {
      this.makeBuff(width, height);
    }

    //init pixel order if need be
    if (pixelOrder == null) {
      this.pixelOrder = new double[len][SpaceFillCanvas.NUM_DATA_COLUMNS];
    } else if ((this.pixelOrder.length != len) ||
                 (this.pixelOrder[0].length != SpaceFillCanvas.NUM_DATA_COLUMNS)) {
      this.pixelOrder = new double[len][SpaceFillCanvas.NUM_DATA_COLUMNS];
    }

    //init pixel colors if need be
    if (pixelColors == null) {
      this.pixelColors = new int[len];
      this.pixelColorsOriginal = new int[len];
    } else if (pixelColors.length != len) {
      this.pixelColors = new int[len];
      this.pixelColorsOriginal = new int[len];
    }
  }

  //start methods for manipulating dataSet of Object[]
  private String findValueOf(Object obj, int place) {
    if (obj instanceof double[]) {
      double[] dataArray = (double[]) obj;

      return String.valueOf(dataArray[place]);
    } else if (obj instanceof int[]) {
      int[] dataArray = (int[]) obj;

      return String.valueOf(dataArray[place]);
    } else if (obj instanceof String[]) {
      String[] dataArray = (String[]) obj;

      return String.valueOf(dataArray[place]);
    } else {
      throw new IllegalArgumentException(
          "obj passed in must be a String[], double[] or int[]");
    }
  }

  //end methods for manipulating dataSet of Object[]
  private void findBuffFillOrder() {
    //this.pixelIndex = FillOrderScan.getFillOrder(data[0].length, this.pixelIndex);
    if (this.currOrderColumn == -1) { //no numeric data

      return;
    }

    int len = this.dataSet.getNumObservations();

    this.pixelIndex = FillOrder.findFillOrder(len, this.pixelIndex,
                                              this.fillOrder);

    //init bufferedImage if need be
    int height = this.pixelIndex.length; //nRows
    int width = this.pixelIndex[0].length; //nColumns

    if ((this.pixelBuff.getWidth() != width) ||
          (this.pixelBuff.getHeight() != height)) {
      this.makeBuff(width, height);
    }
  }

  private static void findPixelOrder(Object[] data, double[][] pixOrder,
                                     int orderColumn) {
    if (orderColumn < 0) {
      return;
    }

    //we need to get our data in numeric form.
    double[] doubleData = null;

    if (data[orderColumn] instanceof double[]) {
      doubleData = (double[]) data[orderColumn];
    } else if (data[orderColumn] instanceof int[]) {
      int[] intData = (int[]) data[orderColumn];
      doubleData = new double[intData.length];

      for (int i = 0; i < intData.length; i++) {
        doubleData[i] = intData[i];
      } //next i
    } else {
      throw new IllegalArgumentException(
          "obj passed in must be a double[] or int[]");
    }

    //pixelOrder[][] is in row, column order (Frank)
    //first let's get the data and set the index
    for (int i = 0; i < pixOrder.length; i++) {
      pixOrder[i][SpaceFillCanvas.COLUMN_ORIGINAL_ORDER] = i;
      pixOrder[i][SpaceFillCanvas.COLUMN_ORIGINAL_DATA] = doubleData[i];
    } //next i


    //now sort on the data
    ArraySort2D sorter = new ArraySort2D();
    sorter.sortDouble(pixOrder, SpaceFillCanvas.COLUMN_ORIGINAL_DATA);

    //now add the index for pixeling by
    for (int i = 0; i < pixOrder.length; i++) {
      pixOrder[i][SpaceFillCanvas.COLUMN_DATA_ORDER] = i;
    } //next i
  }

  //public static void colorPixels(BufferedImage buffIm, double[][] pixOrder, int[] pixColors, int[][] pixIndex){
  private void colorPixels() {
    for (int i = 0; i < this.pixelIndex.length; i++) { //row

      for (int j = 0; j < pixelIndex[0].length; j++) { //column

        //step 1: find the index from the order array
        int rgb = 0;
        int buffIndex = pixelIndex[i][j];

        //if it is outside the space fill viz (can't make it a rectangle...)
        if (buffIndex == Integer.MIN_VALUE) {
          //rgb = this.colorNotInStudyArea.getRGB();
          rgb = this.colorNotInStudyArea.getRGB();
        } else  {
          //find out what element is the 'buffIndex'th one when sorted
          int arrayIndex = (int) Math.round(
                               pixelOrder[buffIndex][SpaceFillCanvas.COLUMN_ORIGINAL_ORDER]);

          //is it conditioned out?
          if (this.conditioning[arrayIndex] < 0){
            rgb = this.getBackground().getRGB();
          }
          else {
            //what color is that bad boy?
            rgb = pixelColors[arrayIndex];
          }

          //let's find the red val
          //Color aColor = new Color(rgb);
          //int red = aColor.getRed();
        }

        pixelBuff.setRGB(j, i, rgb);
      }
    }

    paintBuffer();
  }

  private void colorCurrSelection() {
    for (int i = 0; i < this.selectedObservations.length; i++) {
      int j = this.selectedObservations[i];
      this.pixelColors[j] = this.colorSelection.getRGB();
    }
  }

  private void colorNewSelection(int[] selectedObservationsOld) {
    for (int i = 0; i < selectedObservationsOld.length; i++) {
      this.pixelColors[selectedObservationsOld[i]] = this.pixelColorsOriginal[selectedObservationsOld[i]];
    }

    this.colorCurrSelection();
    this.colorCurrIndication();
  }

  private void colorCurrIndication() {
    if (this.indication >= 0) {
      this.pixelColors[indication] = this.colorIndication.getRGB();
    }
  }

  //note: there are two preconditions to this method working correctly:
  //the oldIndication must be set correcty, and this.indication must
  //reflect the new one.
  private void colorNewIndication(int oldIndication) {
    //first let's color the old indication correctly
    if (oldIndication >= 0) {
      this.pixelColors[oldIndication] = this.pixelColorsOriginal[oldIndication];
    }

    this.colorCurrSelection();
    this.colorCurrIndication();
  }

  private void findPixelColors(Object dataColors) {
    int len = -1;

    //doubleData = null;
    if (dataColors instanceof double[]) {
      double[] doubleData = (double[]) dataColors;
      len = doubleData.length;
    } else if (dataColors instanceof int[]) {
      int[] intData = (int[]) dataColors;
      len = intData.length;
    } else {
      throw new IllegalArgumentException(
          "dataColors must be a double[] or int[]");
    }

    if ((this.colors == null) || (this.colors.length != len)) {
      //we do a coloring here... note that this is a re-class and re-color each time
      //also, do we need to do the numeric type discovery thing twice???
      this.colors = this.findColoringByClass(dataColors);
    }

    for (int i = 0; i < this.pixelColors.length; i++) {
      int rgb = this.colors[i].getRGB();
      this.pixelColorsOriginal[i] = rgb;
      this.pixelColors[i] = rgb;
    }

    this.colorCurrSelection();
    this.colorCurrIndication();
  }

  private Color[] findColoringByClass(Object dataColors) {
    //we need to get our data in numeric form.
    double[] doubleData = null;

    if (dataColors instanceof double[]) {
      doubleData = (double[]) dataColors;
    } else if (dataColors instanceof int[]) {
      int[] intData = (int[]) dataColors;
      doubleData = new double[intData.length];

      for (int i = 0; i < intData.length; i++) {
        doubleData[i] = intData[i];
      } //next i
    } else {
      throw new IllegalArgumentException(
          "obj passed in must be a double[] or int[]");
    }

    Color[] returnColors = this.colorClasser.symbolize(doubleData);

    return returnColors;
  }

  //start shape handling
  public void makeGeographicShapes(Shape[] originalShapes) {
    if ((this.data == null) || (this.getWidth() < 1) ||
          (originalShapes == null)) {
      return;
    }

    int numShapes = originalShapes.length;

    if (numShapes > 100) {
      return;
    }

    if ((this.drawingShapes == null) ||
          (this.drawingShapes.length != numShapes)) {
      this.drawingShapes = new Shape[numShapes];
    }

    //xxx
    //first we figure out how big each shape is
    int width = this.getWidth();
    int height = this.getHeight();
    int heightIndex = this.pixelIndex.length; //nRows
    int widthIndex = this.pixelIndex[0].length; //nColumns
    float perCellWidth = (float) width / (float) widthIndex;
    float perCellHeight = (float) height / (float) heightIndex;

    AffineTransform xForm = null;

    for (int i = 0; i < this.pixelIndex.length; i++) { //row

      for (int j = 0; j < pixelIndex[0].length; j++) { //column

        //step 1: find the index from the order array
        int buffIndex = pixelIndex[i][j];

        //if it is not outside the space fill viz
        if (buffIndex != Integer.MIN_VALUE) {
          //get that original shape
          int arrayIndex = (int) Math.round(
                               pixelOrder[buffIndex][SpaceFillCanvas.COLUMN_ORIGINAL_ORDER]);
          Shape geogShape = originalShapes[arrayIndex];
          Rectangle2D source = geogShape.getBounds2D();
          Rectangle2D target = new Rectangle2D.Double(perCellWidth * j,
                                                      perCellHeight * i,
                                                      perCellWidth,
                                                      perCellHeight);
          xForm = AffineTransformModifier.makeGeogAffineTransform(source,
                                                                  target, true,
                                                                  true);
          this.drawingShapes[arrayIndex] = xForm.createTransformedShape(
                                               geogShape);
        } //end if
      } // next column
    } // next row

    return;
  }

  //end shape handling
  //start mouse event handling

  /**
   * Draws a bounding box for selection.
   * @param e
   */
  public void mouseDragged(MouseEvent e) {
    e.consume();
    mouseX2 = e.getX();
    mouseY2 = e.getY();
    repaint();
  }

  /**
   * Activates a tool tip.
   * @param e
   */
  public void mouseMoved(MouseEvent e) {
    //Tool tip, why not?
    //this is indication as well...
    if (data != null) {
      Point2D.Double mouseLocation = new Point2D.Double();
      mouseLocation.setLocation(e.getX(), e.getY());

      try {
        xform.inverseTransform(mouseLocation, mouseLocation);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      int x = (int) mouseLocation.getX();
      int y = (int) mouseLocation.getY();
      this.makeToolTip(x, y);

      int currObs = this.findArrayIndexAt(x, y);

      if (currObs != indication) {
        int oldInd = this.indication;
        indication = currObs;
        this.makeToolTip(indication);


        logger.finest("indication = " + indication);
        this.colorNewIndication(oldInd);
        this.colorPixels();
        fireIndicationChanged(indication);
      }
    }
  }

  /**
   * Inits selection bounding box.
   * @param e
   */
  public void mousePressed(MouseEvent e) {
    e.consume();
    mouseX1 = e.getX();
    mouseY1 = e.getY();
    mouseX2 = e.getX();
    mouseY2 = e.getY();
  }

  /**
   * Makes selection.
   * @param e
   */
  public void mouseReleased(MouseEvent e) {
    if (data != null) {
      mouseX2 = e.getX();
      mouseY2 = e.getY();
      e.consume();

      Point2D.Double selectionStart = new Point2D.Double();
      Point2D.Double selectionEnd = new Point2D.Double();

      selectionStart.setLocation(mouseX1, mouseY1);
      selectionEnd.setLocation(mouseX2, mouseY2);

      try {
        xform.inverseTransform(selectionStart, selectionStart);
        xform.inverseTransform(selectionEnd, selectionEnd);
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      int x1;
      int x2;
      int y1;
      int y2;
      x1 = (int) selectionStart.getX();
      x2 = 1 + (int) selectionEnd.getX();
      y1 = (int) selectionStart.getY();
      y2 = 1 + (int) selectionEnd.getY();

      if (e.isShiftDown()) {
        this.makeSelectionShift(x1, x2, y1, y2);
      } else {
        this.makeSelection(x1, x2, y1, y2);
      }

      mouseX1 = -2;
      repaint();
    } //if not null
  }

  /**
   * makes crosshair cursor
   * @param e
   */
  public void mouseEntered(MouseEvent e) {
    this.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
  }

  /**
   * resets cursor
   * @param e
   */
  public void mouseExited(MouseEvent e) {
  if (data == null){
   		return;
	}
    //ok, let's clobber the indiction
    this.indication = Integer.MIN_VALUE;
    this.findPixelColors(data[currColorColumn]);
    this.colorPixels();

    for (int i = 0; i < pixelColors.length; i++) {
      this.pixelColors[i] = this.pixelColorsOriginal[i];
    }
    this.fireIndicationChanged(-1);
  }

  /**
   * noop
   * @param e
   */
  public void mouseClicked(MouseEvent e) {
    if (e.getClickCount() > 1) { // This is a double-click or triple...

      //if (dataIndices[0] != dataIndices[1]) { //why this??? 
      //I guess we don't want to pop up one from the
      //diagonal if we are a scatterplot
      SpaceFill detailSpaceFill = new SpaceFill();
      detailSpaceFill.setDataSet(this.dataSet);


      //detailSpaceFill.setBivarColorClasser(this.bivarColorClasser);
      detailSpaceFill.setSelectedObservationsInt(this.selectedObservations);
      detailSpaceFill.setCurrColorColumn(this.currColorColumn);
      detailSpaceFill.setCurrOrderColumn(this.currOrderColumn);

      //(dataObject,
      //   dataIndices, true, background);
      JFrame dummyFrame = new JFrame();
      JDialog detailSpaceFillFrame = new JDialog(dummyFrame,
                                                 "Detail Space Filling Visualization",
                                                 true);
      detailSpaceFillFrame.setLocation(300, 300);
      detailSpaceFillFrame.setSize(300, 300);
      detailSpaceFillFrame.getContentPane().setLayout(new BorderLayout());
      detailSpaceFillFrame.getContentPane()
                    .add(detailSpaceFill, BorderLayout.CENTER);
      detailSpaceFill.addActionListener(new ActionListener() {
        /**
         * put your documentation comment here
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
          logger.finest("something came from detailed one.");
          SpaceFill detailSpaceFill = (SpaceFill) e.getSource();
          String command = e.getActionCommand();

          if (command.compareTo(SpaceFillCanvas.COMMAND_SELECTION) == 0) {
            Vector selRecords = detailSpaceFill.getSelectedObservations();


            // Don't recall the map which generated the original event
            SpaceFillCanvas.this.setSelectedObservations(selRecords);
            SpaceFillCanvas.this.fireActionPerformed(
                SpaceFillCanvas.COMMAND_SELECTION);

            //repaint();
          } else if (command.compareTo(
                         SpaceFillCanvas.COMMAND_COLOR_CLASSFICIATION) == 0) {
            SpaceFillCanvas.this.setBivarColorClasser(
                detailSpaceFill.getBivarColorClasser(), false);
            SpaceFillCanvas.this.fireActionPerformed(
                SpaceFillCanvas.COMMAND_COLOR_CLASSFICIATION);
          } else {
            System.err.println("Unknown command! = " + command);
          }
        }
      }); //end action performed
      detailSpaceFillFrame.setVisible(true);

      //}//end dataIndeces
    } //end if doubleclick
  }

  private int findArrayIndexAt(int x, int y) {
    int arrayIndex = Integer.MIN_VALUE;

    if (x > this.pixelIndex[0].length) {
      return arrayIndex;
    }

    if (y > this.pixelIndex.length) {
      return arrayIndex;
    }

    if (x < 0) {
      return arrayIndex;
    }

    if (y < 0) {
      return arrayIndex;
    }

    //first we find which pixel is selected
    //lets use the pixel index (he he)
    int buffIndex = pixelIndex[y][x];

    //if it is outside the space fill viz (can't make it a rectangle...)
    if (buffIndex != Integer.MIN_VALUE) {
      //find out what element is the 'buffIndex'th one when sorted
      arrayIndex = (int) Math.round(
                       pixelOrder[buffIndex][SpaceFillCanvas.COLUMN_ORIGINAL_ORDER]);
    }

    return arrayIndex;
  }

  private void makeToolTip(int arrayIndex) {
    if (arrayIndex != Integer.MIN_VALUE) {
      //setting multi-line tool tip
      //b.setToolTipText("<html>ToolTip : 1st Line<br>2nd Line<br> 3rd Line </html>");
      String colorVal = this.findValueOf(this.data[this.currColorColumn],
                                         arrayIndex);
      String orderVal = this.findValueOf(this.data[this.currOrderColumn],
                                         arrayIndex);
      String s = "<html> ";

      if (this.observationNames != null) {
        s = s + "Name = " + observationNames[arrayIndex] + "<br>";
      }

      s = s + variableNames[currColorColumn - 1] + " = " + colorVal + "<br>" +
          variableNames[currOrderColumn - 1] + " = " + orderVal + "</html>";

      this.setToolTipText(s);
    } //if
  }

  private void makeToolTip(int x, int y) {
    if (x > this.pixelIndex[0].length) {
      return;
    }

    if (y > this.pixelIndex.length) {
      return;
    }

    if (x < 0) {
      return;
    }

    if (y < 0) {
      return;
    }

    int arrayIndex = this.findArrayIndexAt(x, y);
    this.makeToolTip(arrayIndex);
  }

  private void makeSelection(int x1, int x2, int y1, int y2) {
    if (x1 > this.pixelIndex[0].length) {
      x1 = pixelIndex[0].length;
    }

    if (x2 > this.pixelIndex[0].length) {
      x2 = pixelIndex[0].length;
    }

    if (y1 > this.pixelIndex.length) {
      y1 = pixelIndex.length;
    }

    if (y2 > this.pixelIndex.length) {
      y2 = pixelIndex.length;
    }

    if (x1 < 0) {
      x1 = 0;
    }

    if (x2 < 0) {
      x2 = 0;
    }

    if (y1 < 0) {
      y1 = 0;
    }

    if (y2 < 0) {
      y2 = 0;
    }

    //X1 needs to be less than X2, same with Y1 and y2
    int tempx = 0;
    int tempy = 0;

    if (x1 > x2) {
      tempx = x1;
      x1 = x2;
      x2 = tempx;
    }

    if (y1 > y2) {
      tempy = y1;
      y1 = y2;
      y2 = tempy;
    }

    //first we assemble a list of what tuples have been selected.
    //lets use the pixel index (he he)
    Vector selObs = new Vector();

    for (int i = y1; i < y2; i++) { //row

      for (int j = x1; j < x2; j++) { //column

        int buffIndex = pixelIndex[i][j];

        //if it is outside the space fill viz (can't make it a rectangle...)
        if (buffIndex != Integer.MIN_VALUE) {
          //find out what element is the 'buffIndex'th one when sorted
          int arrayIndex = (int) Math.round(
                               pixelOrder[buffIndex][SpaceFillCanvas.COLUMN_ORIGINAL_ORDER]);
          selObs.add(new Integer(arrayIndex));
        } //if
      } //j
    } //i

    int counter = 0;
    int[] newSel = new int[selObs.size()];

    for (Enumeration e = selObs.elements(); e.hasMoreElements();) {
      Integer I = (Integer) e.nextElement();
      int i = I.intValue();
      newSel[counter] = i;


      //this.selectedObservationsOld[counter] = i;
      counter++;
    }

    this.setSelectedObservationsInt(newSel);
    fireActionPerformed(COMMAND_SELECTION);
  } //method

  private void makeSelectionShift(int x1, int x2, int y1, int y2) {
  }

  //end mouse event handling
  //start component event handling
  //note: this class only listens to itself
  public void componentHidden(ComponentEvent e) {
  }

  public void componentMoved(ComponentEvent e) {
  }

  public void componentResized(ComponentEvent e) {
    if (this.useDrawingShapes) {
      this.makeGeographicShapes(this.originalSpatialData);
    }

    remakeDrawingBuff();
  }

  public void componentShown(ComponentEvent e) {
    logger.finest("componentShown event from "               + e.getComponent().getClass().getName());
  }

  //end component handling
  //start coordinated events handling
  public void selectionChanged(SelectionEvent e) {
    int[] sel = e.getSelection();
    this.setSelectedObservationsInt(sel);
  }

  public void remakeDrawingBuff() {
    int width = this.getWidth();
    int height = this.getHeight();

    //this.drawingBuff = new Image(width,height,BufferedImage.TYPE_INT_ARGB);
    if ((width > 0) && (height > 0)) {
      drawingBuff = this.createImage(width, height);
      this.paintBuffer();
    }
  }

  //end component event handling

  public void setBackground(Color c){
    super.setBackground(c);
    this.colorOutOfFocus = c;
    if (this.dataSet != null){
      this.colorPixels();
      this.repaint();
    }
  }
  //start accessors
  public void setBivarColorClasser(BivariateColorSymbolClassification bivarColorClasser, boolean reverseColor) {
    logger.finest("got a color classer");

    ColorSymbolClassificationSimple colorClasser = new ColorSymbolClassificationSimple();
    colorClasser.setClasser(bivarColorClasser.getClasserY());
    colorClasser.setColorer(bivarColorClasser.getYColorSymbolizer());
    this.setColorSymbolizer(colorClasser);

    //this.colorClasser = bivarColorClasser.getYColorSymbolizer();
  }

  public BivariateColorSymbolClassification getBivarColorClasser() {
    return null;
  }

  public void setColorSymbolizer(ColorSymbolClassification colorSymbolizer) {
    this.colorClasser = colorSymbolizer;

    if (currColorColumn >= 0) {
      this.colors = this.findColoringByClass(data[currColorColumn]);
      this.findPixelColors(data[currColorColumn]);
      this.colorPixels();
    }
  }

  /**
   * @param data
   * 
   * This method is deprecated becuase it wants to create its very own pet
   * DataSetForApps. This is no longer allowed, to allow for a mutable, 
   * common data set. Use of this method may lead to unexpected
   * program behavoir. 
   * Please use setDataSet instead.
   */
  @Deprecated
  public void setData(Object[] data) {
	 this.setDataSet(new DataSetForApps(data));
    
  }
  public void setDataSet(DataSetForApps dataIn) {
    dataSet = new DataSetForApps();
    this.variableNames = dataSet.getAttributeNamesNumeric();
    this.data = dataSet.getDataSetNumericAndSpatial();

    if (dataSet.getNumberNumericAttributes() > 2) {
      this.currOrderColumn = 1;
      this.currColorColumn = 2;
    }

    this.observationNames = dataSet.getObservationNames();
    this.findBuffFillOrder();
    this.refreshMembers();
    SpaceFillCanvas.findPixelOrder(this.data, this.pixelOrder, this.currOrderColumn);

    if (currColorColumn >= 0) {
      this.findPixelColors(data[currColorColumn]);
    }
    this.conditioning = new int[dataSet.getNumObservations()];
    this.colorPixels();
    this.originalSpatialData = dataSet.getShapeData();

    if (this.useDrawingShapes) {
      this.makeGeographicShapes(this.originalSpatialData);
    }
  }

  public Object[] getData() {
    return this.data;
  }

  public void setPixelBuff(BufferedImage pixelBuff) {
    this.pixelBuff = pixelBuff;
  }

  public BufferedImage getPixelBuff() {
    return this.pixelBuff;
  }

  public void setCurrOrderColumn(int currOrderColumn) {
    if ((data != null) && (currOrderColumn < data.length)) {
      this.currOrderColumn = currOrderColumn;
      SpaceFillCanvas.findPixelOrder(this.data, this.pixelOrder, this.currOrderColumn);

      if (this.useDrawingShapes) {
        this.makeGeographicShapes(this.originalSpatialData);
      }

      this.colorPixels();
    }
  }

  public int getCurrOrderColumn() {
    return this.currOrderColumn;
  }

  public void setCurrColorColumn(int currColorColumn) {
    if ((data != null) && (currColorColumn < data.length)) {
      this.currColorColumn = currColorColumn;
      this.colors = this.findColoringByClass(data[currColorColumn]);
      this.findPixelColors(data[currColorColumn]);
      this.colorPixels();
    }
  }

  public int getCurrColorColumn() {
    return this.currColorColumn;
  }

  public void setColorSelection(Color colorSelection) {
    this.colorSelection = colorSelection;
  }

  public Color getColorSelection() {
    return this.colorSelection;
  }

  public boolean getSelOriginalColorMode() {
	  return selOriginalColorMode;
  }

  public void setSelOriginalColorMode(boolean selOriginalColorMode) {
	  this.selOriginalColorMode = selOriginalColorMode;
	}

  public void setColorIndication(Color colorIndication) {
    this.colorIndication = colorIndication;
  }

  public Color getColorIndication() {
    return this.colorIndication;
  }

  public void setIndication(int indication) {

    if (this.indication != indication) {
      int oldInd = this.indication;
      this.indication = indication;
      //this.makeToolTip(indication);


      logger.finest("indication = " + indication);
      this.colorNewIndication(oldInd);
      this.colorPixels();

    }


  }

  public int getIndication() {
    return this.indication;
  }

  public void setColorNull(Color colorNull) {
    this.colorNull = colorNull;
    this.findPixelColors(data[currColorColumn]);
    this.colorPixels();
  }

  public Color getColorNull() {
    return this.colorNull;
  }

  public void setColorOutOfFocus(Color colorOutOfFocus) {
    this.colorOutOfFocus = colorOutOfFocus;
    this.findPixelColors(data[currColorColumn]);
    this.colorPixels();
  }

  public Color getColorOutOfFocus() {
    return this.colorOutOfFocus;
  }

  public void setColorNotInStudyArea(Color colorNotInStudyArea) {
    this.colorNotInStudyArea = colorNotInStudyArea;
    this.findPixelColors(data[currColorColumn]);
    this.colorPixels();
  }

  public Color getColorNotInStudyArea() {
    return this.colorNotInStudyArea;
  }

  public void setVariableNames(String[] variableNames) {
    this.variableNames = variableNames;
  }

  public String[] getVariableNames() {
    return this.variableNames;
  }

  public void setConditionArray (int[] conditionArray){
    if (this.dataSet == null){
      return;
    }
    if (conditionArray.length != this.dataSet.getNumObservations()){
      throw new IllegalArgumentException("passed incompatible length conditioning array");
    }
    this.conditioning = conditionArray;


      this.colorPixels();
  }

  public void setSelectedObservations(Vector selectedObservations) {
    int counter = 0;
    this.selectedObservations = new int[selectedObservations.size()];

    for (Enumeration e = selectedObservations.elements(); e.hasMoreElements();) {
      Integer I = (Integer) e.nextElement();
      int i = I.intValue();
      this.selectedObservations[counter] = i;
      counter++;
    }

    this.setSelectedObservationsInt(this.selectedObservations);
  }

  public void setSelections(int[] selections) {
    Vector v = new Vector();

    for (int i = 0; i < selections.length; i++) {
      if (selections[i] == 1) {
        v.add(new Integer(i));
      }
    }

    this.selectedObservations = new int[v.size()];

    int i = 0;

    for (Enumeration e = v.elements(); e.hasMoreElements();) {
      Integer bigIint = (Integer) e.nextElement();
      this.selectedObservations[i] = bigIint.intValue();
      i++;
    }

    this.setSelectedObservationsInt(this.selectedObservations);
  }

  public int[] getSelections() {
    int[] selections = new int[this.dataSet.getNumObservations()];

    for (int i = 0; i < this.selectedObservations.length; i++) {
      selections[this.selectedObservations[i]] = 1;
    }

    return selections;
  }

  public Vector getSelectedObservations() {
    Vector v = new Vector();

    for (int i = 0; i < this.selectedObservations.length; i++) {
      Integer anInt = new Integer(this.selectedObservations[i]);
      v.add(anInt);
    }

    return v;
  }

  public void setSelectedObservationsInt(int[] selectedObservations) {
    //copy selected obs
    this.selectedObservationsOld = new int[this.selectedObservations.length];

    for (int i = 0; i < this.selectedObservations.length; i++) {
      this.selectedObservationsOld[i] = this.selectedObservations[i];
    }

    this.selectedObservations = selectedObservations;
    this.colorNewSelection(this.selectedObservationsOld); //this also colors


    //current selection
    this.findPixelColors(data[currColorColumn]);
    this.colorPixels();
    this.repaint();
  }

  public int[] getSelectedObservationsInt() {
    return this.selectedObservations;
  }

  public void setFillOrder(int fillOrder) {
    if (this.fillOrder != fillOrder) {
      if ((fillOrder > FillOrder.FILL_ORDER_MAX) || (fillOrder < 0)) {
        throw new IllegalArgumentException(
            "Fill order outside legal range defined in FillOrder");
      } else {
        this.fillOrder = fillOrder;
        this.findBuffFillOrder();
        this.colorPixels();
      }
    } //
  } //end method

  public int getFillOrder() {
    return this.fillOrder;
  }

  public void setColors(Color[] colors) {
    this.colors = colors;

    if (data != null) {
      this.findPixelColors(data[currColorColumn]);
      this.colorPixels();
    }
  }

  public Color[] getColors() {
    return this.colors;
  }

  public void setObservationNames(String[] observationNames) {
    this.observationNames = observationNames;
  }

  public String[] getObservationNames() {
    return this.observationNames;
  }

  public void setUseDrawingShapes(boolean useDrawingShapes) {
    this.useDrawingShapes = useDrawingShapes;

    if (this.useDrawingShapes) {
      this.makeGeographicShapes(this.originalSpatialData);
    } else {
      this.drawingShapes = null;
    }

    this.remakeDrawingBuff();
  }

  public boolean getUseDrawingShapes() {
    return this.useDrawingShapes;
  }

  //end accessors

  /**
   * This method reconstructs the contents of the drawingBuff.
   * @param g
   */
  public void paintBuffer() {
    if ((this.getWidth() <= 0) || (this.getHeight() <= 0)) {
      return;
    }

    if (drawingBuff == null) {
      this.remakeDrawingBuff();
    }

    Graphics g = this.drawingBuff.getGraphics();
    this.makeXform();

    Graphics2D g2 = (Graphics2D) g;


    //draw image
    g2.drawImage(pixelBuff, xform, this);

    if ((this.drawingShapes != null) && this.useDrawingShapes) {
      g2.setColor(Color.cyan);

      BasicStroke outline = new BasicStroke(2.0f);
      g2.setStroke(outline);

      for (int i = 0; i < this.drawingShapes.length; i++) {
        if (drawingShapes[i] != null) {
          g2.draw(this.drawingShapes[i]);
        } //end if null
      } // next i
    } // end if


    //draw shapes
    //xxx
    repaint();
  }

  /**
   * This method only paints the current contents of the drawingBuff.
   * @param g
   */
  public void paintComponent(Graphics g) {
    if ((this.getWidth() <= 0) || (this.getHeight() <= 0)) {
      return;
    }

    if (drawingBuff == null) {
      this.remakeDrawingBuff();
    }

    g.drawImage(this.drawingBuff, 0, 0, this);

    Graphics2D g2 = (Graphics2D) g;
    Stroke currStroke = g2.getStroke();
    Color currColor = g2.getColor();

    //draw selection box
    if (mouseX1 > -1) {
      float[] dash = new float[3];
      dash[0] = (float) 5.0;
      dash[1] = (float) 7.0;
      dash[2] = (float) 5.0;

      BasicStroke dashStroke = new BasicStroke((float) 2.0,
                                               BasicStroke.CAP_SQUARE,
                                               BasicStroke.JOIN_MITER,
                                               (float) 10.0, dash, 0);
      g2.setStroke(dashStroke);
      g2.setPaintMode();
      g2.setColor(Color.black);
      g2.setXORMode(Color.white);

      //let's take drawing the selection rectangle by cases
      //not elegant, but the alternative is introducing more class variables
      int selectX = 0;
      int selectY = 0;
      int selectWidth = 0;
      int selectHeight = 0;

      if ((mouseX1 <= mouseX2) && (mouseY1 <= mouseY2)) {
        selectX = mouseX1;
        selectY = mouseY1;
        selectWidth = mouseX2 - mouseX1;
        selectHeight = mouseY2 - mouseY1;
      }

      if ((mouseX2 < mouseX1) && (mouseY1 <= mouseY2)) {
        selectX = mouseX2;
        selectY = mouseY1;
        selectWidth = mouseX1 - mouseX2;
        selectHeight = mouseY2 - mouseY1;
      }

      if ((mouseX1 <= mouseX2) && (mouseY2 < mouseY1)) {
        selectX = mouseX1;
        selectY = mouseY2;
        selectWidth = mouseX2 - mouseX1;
        selectHeight = mouseY1 - mouseY2;
      }

      if ((mouseX2 < mouseX1) && (mouseY2 < mouseY1)) {
        selectX = mouseX2;
        selectY = mouseY2;
        selectWidth = mouseX1 - mouseX2;
        selectHeight = mouseY1 - mouseY2;
      }

      g2.drawRect(selectX, selectY, selectWidth, selectHeight);
    } //end if mouse
    g2.setStroke(currStroke);
    g2.setColor(currColor);
    g2.setPaintMode();
  }

  /**
   * adds an ActionListener to the component
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * removes an ActionListener from the component
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  public void fireActionPerformed(String command) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ActionEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == ActionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        }

        ((ActionListener) listeners[i + 1]).actionPerformed(e);
      }
    }
  }

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
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
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
}