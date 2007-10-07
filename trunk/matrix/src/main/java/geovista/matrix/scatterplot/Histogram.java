package geovista.matrix.scatterplot;

/**
 * Title: Histogram
 * Description:  Create histogram for a variable
 * Copyright:    Copyright (c) 2002
 * Company:  GeoVISTA Center
 * @author Xiping Dai
 * @version 1.0
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.BitSet;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.common.event.SelectionEvent;
import geovista.common.event.SelectionListener;

public class Histogram extends JPanel implements MouseListener, ComponentListener, DataSetListener, SelectionListener{
	private static double AXISSPACEPORTION = 1.0/6.0;
	private static int DEFAULT_HIST_NUM = 20;
	transient private double[] data;
	transient private String variableName;
	transient private DataArray dataArray;
	transient private DataArray histArray;
	transient private double[] histogramArray;
	transient private double[] accumulativeFrequency;
	transient private double[] dataX;
	 private int histNumber = DEFAULT_HIST_NUM;
	transient private double barWidth;
	private boolean axisOn = true;
	transient private boolean accFrequency = false;
	transient private int plotOriginX;
	transient private int plotOriginY;
	transient private int plotEndX;
	transient private int plotEndY;
	transient private double[] xAxisExtents;
	transient private double[] yAxisExtents;
	transient private int[] exsInt;
	transient private int[] whyInt;
	transient private int[] accumulativeInt;
	transient private int[] selectionInt;
    transient private double[] classBoundareis;
    transient private int[] classBoundariesInt;
	//private Vector selRecords = new Vector();
	transient private BitSet selectedRecords;
	transient private Vector[] histRecords;
	transient private Rectangle[] histRecs;
	transient private double[] selectionArray; //the count of seleced observation in each histogram bin.
	transient private Color background;
	transient private Color foreground;
         private Color histFillColor = Color.gray;
	transient private JPopupMenu popup;
	transient private JDialog dialog1;
	transient private JDialog dialog2;
	 private JTextField histNumberField = new JTextField(16);
	 private JTextField yAxisMinField = new JTextField(16);
	 private JTextField yAxisMaxField = new JTextField(16);
	 private JTextField xAxisMinField = new JTextField(16);
	 private JTextField xAxisMaxField = new JTextField(16);
	 private EventListenerList listenerListAction = new EventListenerList();
	protected final static Logger logger = Logger.getLogger(Histogram.class.getName());
    public Histogram() {
    	Dimension size = new Dimension(300,200);
      this.setPreferredSize(size);
      this.setSize(size);
    }

	public void setData (double[] data){
		this.data = data;
		this.dataArray = new DataArray(data);
		this.xAxisExtents = (double[])dataArray.getExtent().clone();
		this.selectedRecords = new BitSet(data.length);
		histogramCalculation ();
		this.addComponentListener(this);
		this.setupDataforDisplay();
		this.setAccumulativeFrequency();
        //Create the popup menu.
		popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Set Histogram Range");
		menuItem.addActionListener(new ActionListener() {

            /**
             * put your documentation comment here
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				showDialog1(400, 400);
			}
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Set Frequency Range");
		menuItem.addActionListener(new ActionListener() {

            /**
             * put your documentation comment here
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				showDialog2(400, 400);
			}
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Accumulative Frequency");
		menuItem.addActionListener(new ActionListener() {

            /**
             * put your documentation comment here
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				setAccFrequency (!accFrequency);
				repaint();
			}
		});
		popup.add(menuItem);
		addMouseListener(this);
	}

	public double[] getData (){
	    return this.data;
	}

	public void setVariableName (String name){
		this.variableName = name;
		this.repaint();
	}

	public String getVariableName (){
	    return this.variableName;
	}

	public void setHistNumber (int num){
		this.histNumber = num;
		this.setData(this.data);
		
	}

	public int getHistNumber (){
	    return this.histNumber;
	}

	public void setAxisOn (boolean axisOn){
		this.axisOn = axisOn;
	}

        public void setHistgramFillColor(Color color){
          this.histFillColor = color;
        }

	public boolean getAxisOn (){
	    return this.axisOn;
	}

        public void setClassBoundaries(double[] boundaries){
          this.classBoundareis = boundaries;
          this.setupDataforDisplay();
          this.repaint();
        }

/*	public void setSelection(Vector selectedObs){
		this.selRecords = selectedObs;
		logger.finest(selRecords.size());
		if (this.selectionArray == null || this.selectionArray.length != this.histNumber){
			this.selectionArray = new double[this.histNumber];
		} else {
			for (int i = 0; i < this.selectionArray.length; i ++){
				this.selectionArray[i] = 0;
			}
		}
		if (this.selectedRecords == null){
			this.selectedRecords = new int[this.data.length];
		}
		for(int i = 0; i < selRecords.size(); i ++){
			int j=(int)Math.floor((data[((Integer)selRecords.get(i)).intValue()]-xAxisExtents[0])/barWidth);
			j=((this.histNumber<=j) ? this.histNumber-1 :j);
			this.selectionArray[j] ++;
			this.selectedRecords[((Integer)selRecords.get(i)).intValue()] = 1;
		}
		this.setSelectionScreen();
	}*/

	public void setSelections(BitSet selectedObs){ 
		this.selectedRecords = selectedObs;
		if (this.selectionArray == null || this.selectionArray.length != this.histNumber){
			this.selectionArray = new double[this.histNumber];
		} else {
			for (int i = 0; i < this.selectionArray.length; i ++){
				this.selectionArray[i] = 0;
			}
		}
		for(int i = 0; i < this.selectedRecords.length(); i ++){
			int j;
			if (this.selectedRecords.get(i)){
				j=(int)Math.floor((data[i]-xAxisExtents[0])/barWidth);
				j=((this.histNumber<=j) ? this.histNumber-1 :j);
				this.selectionArray[j] ++;
			}
		}
		this.setSelectionScreen();
	}

	public BitSet getSelections (){
		return this.selectedRecords;
	}

/*	public Vector getSelection (){
		return this.selRecords;
	}*/

	/**
	 * Minimum and maximum values for xAxis. xAxisExtents[0] = min, xAxisExtents[1] = max.
	 * @param double[] xAxisExtents
	 */
	public void setXAxisExtents (double[] xAxisExtents) {
		logger.finest("set up axis ..." + xAxisExtents[0]);
		this.xAxisExtents = (double[])xAxisExtents.clone();
		logger.finest("set up axis ..." + xAxisExtents[0]);
		this.histogramCalculation();
		this.setupDataforDisplay();
		repaint();
	}

	/**
	 * put your documentation comment here
	 * @return
	 */
	public double[] getXAxisExtents () {
		return  this.xAxisExtents;
	}

	public void setAccFrequency(boolean accFrequency){
	    this.accFrequency = accFrequency;
	}

    /**
     * put your documentation comment here
     * @param c
     */
	public void setBackground (Color c) {
		if (c == null)
			return;
		this.background = c;
		int colorTotal = c.getRed() + c.getGreen() + c.getBlue();
		int greyColor = 128 * 3;
		if (colorTotal < greyColor)
			this.foreground = Color.white;
		else
			this.foreground = Color.black;
		this.repaint();
	}

	private void histogramCalculation (){
		if (data == null){
			return;
		}
		if (data.length < this.histNumber){
			this.histNumber = data.length;
		}

		this.histogramArray = new double[this.histNumber];
		this.accumulativeFrequency = new double[this.histNumber];
		this.dataX = new double[this.histNumber];
		this.histRecords = new Vector[this.histNumber];
		this.histRecs = new Rectangle[this.histNumber];

		for (int i = 0; i < this.histNumber; i ++){
			histRecords[i] = new Vector();
		}

		barWidth = (xAxisExtents[1] - xAxisExtents[0])/ (double)histNumber;

		for (int i = 0; i < data.length; i ++){
			if (data[i]>=xAxisExtents[0]&&data[i]<=xAxisExtents[1]) {
				int j=(int)Math.floor((data[i]-xAxisExtents[0])/barWidth);
				j=((this.histNumber<=j) ? this.histNumber-1 :j);
				this.histogramArray[j] ++;
				this.histRecords[j].add(new Integer(i));
			}
		}

		for (int i = 0; i < this.histNumber; i++){
		    dataX[i] = i * barWidth + xAxisExtents[0];
			if (i == 0){
				this.accumulativeFrequency[i] = this.histogramArray[i];
			}else{
			    this.accumulativeFrequency[i] = this.accumulativeFrequency[i-1]+
				this.histogramArray[i];
			}
		}

		this.histArray = new DataArray(this.histogramArray);
		this.yAxisExtents = histArray.getMaxMinCoorValue();
	}


	public void paintComponent (Graphics g) {
		g.setColor(background);
                if (this.histFillColor != null){
                  g.fillRect(0, 0, getSize().width, getSize().height);
                }
		g.setColor(foreground);
		if (this.axisOn == true){
			drawAxis(g);
		}
		drawPlot(g);
		if (this.selectionArray != null){
			drawSelection(g);
		}
		if (this.accFrequency == true){
		    drawAccumulativeFrequency(g);
		}
                if (this.classBoundariesInt != null){
                  logger.finest("class boundaries not null");
                  this.drawClassBoundaries(g);
                }
	}


	private void drawPlot (Graphics g) {
	    int len = this.histNumber;
		for (int i = 0; i < len-1; i ++){
			g.drawRect(this.exsInt[i], this.whyInt[i], this.exsInt[i+1]-this.exsInt[i], this.plotOriginY - this.whyInt[i]);
                        if (this.histFillColor != null){
                          g.setColor(this.histFillColor);
                          g.fillRect(this.exsInt[i] + 1, this.whyInt[i] + 1,
                                     this.exsInt[i + 1] - this.exsInt[i] - 1,
                                     this.plotOriginY - this.whyInt[i] - 1);
                          g.setColor(foreground);
                        }
		}
                g.drawRect(this.exsInt[len-1], this.whyInt[len-1], this.plotEndX-this.exsInt[len-1], this.plotOriginY - this.whyInt[len-1]);
                if (this.histFillColor != null){
                  g.setColor(this.histFillColor);
                  g.fillRect(this.exsInt[len - 1] + 1, this.whyInt[len - 1] + 1,
                             this.plotEndX - this.exsInt[len - 1] - 1,
                             this.plotOriginY - this.whyInt[len - 1] - 1);
                  g.setColor(this.foreground);
                }
	}

	private void setSelectionScreen(){
		this.selectionInt = new int[this.selectionArray.length];
		double scale;
		scale = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
		this.selectionInt = getValueScreen(this.selectionArray, scale, plotOriginY, 0);
	}

	private void drawSelection (Graphics g){
		for (int i = 0; i < this.histNumber-1; i ++){
			if (this.selectionArray[i] > 0){
				g.drawRect(this.exsInt[i], this.selectionInt[i], this.exsInt[i+1]-this.exsInt[i], this.plotOriginY - this.selectionInt[i]);
				g.setColor(Color.blue);
				g.fillRect(this.exsInt[i]+1, this.selectionInt[i]+1, this.exsInt[i+1]-this.exsInt[i]-1, this.plotOriginY - this.selectionInt[i]-1);
				g.setColor(foreground);
			}
		}
		if (selectionArray[this.histNumber-1] > 0){
		g.drawRect(this.exsInt[this.histNumber-1], this.selectionInt[this.histNumber-1], this.plotEndX-this.exsInt[this.histNumber-1], this.plotOriginY - this.selectionInt[this.histNumber-1]);
		g.setColor(Color.blue);
		g.fillRect(this.exsInt[this.histNumber-1]+1, this.selectionInt[this.histNumber-1]+1, this.plotEndX-this.exsInt[this.histNumber-1]-1, this.plotOriginY - this.selectionInt[this.histNumber-1]-1);
		g.setColor(this.foreground);
		}
	}

	private void drawAxis (Graphics g) {
		int plotWidth, plotHeight;
		plotWidth = (int)this.getSize().getWidth();
		plotHeight = (int)this.getSize().getHeight();
		g.setColor(foreground);
		g.drawLine(plotOriginX, plotEndY, plotOriginX, plotOriginY);
		g.drawLine(plotOriginX, plotOriginY, plotEndX, plotOriginY);
        // draw tick bars for scales on Y coordinate
		int fontSize;
		if (plotWidth < plotHeight){
			if (plotWidth < 300){
				fontSize = 9;
			} else {
				fontSize = (int)(plotWidth/32);
			}
		}else {
			if (plotHeight < 300){
				fontSize = 9;
			} else {
				fontSize = (int)(plotHeight/32);
			}
		}
		Font font = new Font("", Font.PLAIN, fontSize);
		g.setFont(font);
		//draw the labels on y axis (frequency).
		String scaleStringY;
		double barNumber = this.histArray.getTickNumber();
		double yBarDistance = ((plotOriginY - plotEndY)/barNumber);
			logger.finest("drawaxis: "+plotOriginY+" "+plotEndY+" "+yBarDistance+" "+barNumber);
		for (int i = 0; i <= barNumber; i++) {
			g.drawLine(plotOriginX - 3, plotEndY + (int)(i*yBarDistance), plotOriginX,
						plotEndY + (int)(i*yBarDistance));
			if (Math.abs(this.histArray.getMajorTick()) <= 1) {
					scaleStringY = Float.toString((float)(yAxisExtents[1] - i*this.histArray.getMajorTick()));
			}
			else {
					scaleStringY = Integer.toString((int)(yAxisExtents[1] - i*this.histArray.getMajorTick()));
			}
			g.drawString(scaleStringY, plotOriginX - (int)(plotWidth*AXISSPACEPORTION/2),
						plotEndY + (int)(i*yBarDistance + yBarDistance*1/6));
		}
		//draw the labels on x axis.
		//First tick.
		String scaleStringX;
		g.drawLine(plotOriginX, plotOriginY, plotOriginX, plotOriginY + 3);
		if (Math.abs(xAxisExtents[0]) <= 1) {
			scaleStringX = Float.toString((float)xAxisExtents[0]);
		} else {
			scaleStringX = Integer.toString((int)xAxisExtents[0]);
		}
		g.drawString(scaleStringX, plotOriginX - 3, plotOriginY + (int)(plotHeight*AXISSPACEPORTION/4));
		//Last tick.
		g.drawLine(plotEndX, plotOriginY, plotEndX, plotOriginY + 3);
		if (Math.abs(xAxisExtents[1]) <= 1) {
			scaleStringX = Float.toString((float)xAxisExtents[1]);
		} else {
			scaleStringX = Integer.toString((int)xAxisExtents[1]);
		}
		g.drawString(scaleStringX, plotEndX - 8, plotOriginY + (int)(plotHeight*AXISSPACEPORTION/4));
		font = new Font("", Font.PLAIN, fontSize + 3);
		g.setFont(font);
        //draw X axis attribute string
		g.drawString(this.variableName, plotOriginX + (plotEndX - plotOriginX)/2 - plotWidth/12,
					plotOriginY + plotHeight/6 - 5);
        //draw Y axis attribute string. Need rotation for drawing the string vertically.
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(-Math.PI/2, plotOriginX - plotWidth/9, plotOriginY - (plotOriginY
					- plotEndY)/3);
		g2d.drawString("Frequency", plotOriginX - plotWidth/9, plotOriginY - (plotOriginY
					- plotEndY)/3);
		g2d.rotate(+Math.PI/2, plotOriginX - plotWidth/9, plotOriginY - (plotOriginY
					- plotEndY)/3);
	}

	private void setAccumulativeFrequency(){
		this.accumulativeInt = new int[this.accumulativeFrequency.length];
		double scale;
		scale = getScale(plotOriginY, plotEndY, 0, this.data.length);
		accumulativeInt = getValueScreen(this.accumulativeFrequency, scale, plotOriginY, 0);
	}

	private void drawAccumulativeFrequency (Graphics g){
	    int len = this.histNumber;
		g.setColor(Color.blue);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		for (int i = 0; i < len-1; i ++){
			g.drawLine(this.exsInt[i], this.accumulativeInt[i], this.exsInt[i+1], this.accumulativeInt[i]);
		}
                g.drawLine(this.exsInt[len-1], this.accumulativeInt[len-1], this.plotEndX, this.accumulativeInt[len-1]);
		g.drawLine(this.plotEndX, this.plotOriginY, this.plotEndX, this.plotEndY);
		g2d.setStroke(new BasicStroke(1));
		//g.drawString((new Integer((int)(this.accumulativeFrequency[len-1]))).toString(), this.plotEndX + 1, this.plotEndY + 5);
		g.drawString((new Integer((int)(this.accumulativeFrequency[len-1]))).toString(), this.plotEndX + 1, this.accumulativeInt[len-1]+5);
		g.setColor(this.foreground);
	}

        private void drawClassBoundaries(Graphics g){
          g.setColor(Color.white);
          for (int i = 1; i < this.classBoundariesInt.length-1; i ++){
            g.drawLine(this.classBoundariesInt[i], 0, this.classBoundariesInt[i], this.getHeight());
          }
        }

    public void componentHidden(ComponentEvent e) {

    }

    public void componentMoved(ComponentEvent e) {

    }

    public void componentResized(ComponentEvent e) {
		logger.finest("in component resized");
		this.setupDataforDisplay();
		this.setAccumulativeFrequency();
		this.repaint();
    }

    public void componentShown(ComponentEvent e) {
    }

	    /**
     * Calculate scale between real data and integer data for showing up on screen.
     * @param min
     * @param max
     * @param dataMin
     * @param dataMax
     * @return scale
     */
	private double getScale (int min, int max, double dataMin, double dataMax) {
		double scale;
		scale = (max - min)/(dataMax - dataMin);
		return  scale;
	}
    /**
     * Convert the numeric values of observations to integer value worked on screen.
     * @param dataArray
     * @param scale
     * @param min
     * @param dataMin
     * @return valueScreen
     */
	private int[] getValueScreen (double[] dataArray, double scale, int min, double dataMin) {
		int[] valueScreen = new int[dataArray.length];
		for (int i = 0; i < dataArray.length; i++) {
			if (Double.isNaN(dataArray[i])) {
				valueScreen[i] = Integer.MIN_VALUE;
			}
			else {
				valueScreen[i] = (int)((dataArray[i] - dataMin)*scale + min);
			}
		}
		return  valueScreen;
	}

	private void setupDataforDisplay(){
		logger.finest("In setup data for display ..." + xAxisExtents[0]);
		if (axisOn){
		    plotOriginX = (int)(this.getWidth()*AXISSPACEPORTION);
		    plotOriginY = (int)(this.getHeight()*(1 - AXISSPACEPORTION));
		    plotEndX = (int)(this.getWidth()) - (int)(this.getWidth()*AXISSPACEPORTION/2);
		    plotEndY = (int)(this.getHeight()*AXISSPACEPORTION/2);
		}else {
			plotOriginX = 0;
			plotOriginY = (int)(this.getSize().getHeight() - 2);
			plotEndX = (int)(this.getSize().getWidth()) - 3;
			plotEndY = 3;
		}
		int len = this.histNumber;
		exsInt = new int[len];
		whyInt = new int[len];
            //get positions on screen
		double scale;
		scale = getScale(plotOriginX, plotEndX, xAxisExtents[0], xAxisExtents[1]);
		exsInt = getValueScreen(dataX, scale, plotOriginX, xAxisExtents[0]);
		scale = getScale(plotOriginY, plotEndY, yAxisExtents[0], yAxisExtents[1]);
		whyInt = getValueScreen(this.histogramArray, scale, plotOriginY, yAxisExtents[0]);
		logger.finest("setupdisplay: "+plotOriginY+" "+plotEndY+" "+scale);
		for (int i = 0; i < this.histNumber -1; i ++){
			this.histRecs[i] = new Rectangle (this.exsInt[i], this.whyInt[i],
			this.exsInt[i+1]-this.exsInt[i], this.plotOriginY - this.whyInt[i]);
		}
		this.histRecs[this.histNumber-1] = new Rectangle (this.exsInt[len-1],
				this.whyInt[len-1], this.plotEndX-this.exsInt[len-1], this.plotOriginY - this.whyInt[len-1]);
               //get class boundaries' positions on screen
               if(this.classBoundareis != null){
                     logger.finest("x and y boundaries are not null.");
                     this.classBoundariesInt = new int[this.classBoundareis.length];
                     this.classBoundariesInt = getValueScreen(this.classBoundareis, scale, plotOriginX, xAxisExtents[0]);
             }

	}
    /**
     * New data ranges setup dialog.
     * @param x
     * @param y
     */
	private void showDialog1 (int x, int y) {

		if (this.dialog1 == null){
		    JFrame dummyFrame = new JFrame();
		    dialog1 = new JDialog(dummyFrame, "Data Range Configuer", true);
		    JButton actionButton;
		    JButton resetButton;
		    dialog1.setLocation(x, y);
		    dialog1.setSize(300, 100);
		    dialog1.getContentPane().setLayout(new GridLayout(4, 2));
        //create buttons for action
		actionButton = new JButton("Apply");
		actionButton.addActionListener(new java.awt.event.ActionListener() {

            /**
             * Button to set up new data ranges shown up in scatter plot.
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				try {
					actionButton_actionPerformed(e);
				} catch (Exception exception) {exception.printStackTrace();}
			}
		});
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new java.awt.event.ActionListener() {

            /**
             * put your documentation comment here
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				resetButton_actionPerformed(e);
			}
		});
		histNumberField.setText(Integer.toString(this.histNumber));
		this.xAxisMinField.setText(Double.toString(this.xAxisExtents[0]));
		this.xAxisMaxField.setText(Double.toString(this.xAxisExtents[1]));
		dialog1.getContentPane().add(new JLabel(("Histogram Number")));
		dialog1.getContentPane().add(this.histNumberField);
		dialog1.getContentPane().add(new JLabel(("DataRange Min")));
		dialog1.getContentPane().add(this.xAxisMinField);
		dialog1.getContentPane().add(new JLabel(("DataRange Max")));
		dialog1.getContentPane().add(this.xAxisMaxField);
		dialog1.getContentPane().add(actionButton);
		dialog1.getContentPane().add(resetButton);
		}
		dialog1.setVisible(true);
	}

    /**
     * Set up new data ranges to show.
     * @param e
     */
	private void actionButton_actionPerformed (ActionEvent e) {
        //get the input data from text field
		this.histNumber = Integer.parseInt(histNumberField.getText());
		xAxisExtents[0] = Double.parseDouble(xAxisMinField.getText());
		xAxisExtents[1] = Double.parseDouble(xAxisMaxField.getText());
		this.histogramCalculation ();
		this.setupDataforDisplay();
		this.setAccumulativeFrequency();
		//fireActionPerformed(COMMAND_DATARANGE_SET);
		logger.finest("ok, fire event.");
		repaint();
		dialog1.setVisible(false);
	}

    /**
     * put your documentation comment here
     * @param e
     */
	private void resetButton_actionPerformed (ActionEvent e) {
		this.histNumber = Histogram.DEFAULT_HIST_NUM;
		this.xAxisExtents = (double[])dataArray.getExtent().clone();
		//yAxisExtents = (double[])this.histArray.getMaxMinCoorValue().clone();

		histNumberField.setText(Integer.toString(this.histNumber));
		xAxisMinField.setText(Double.toString(xAxisExtents[0]));
		xAxisMaxField.setText(Double.toString(xAxisExtents[1]));
		this.histogramCalculation ();
		this.setupDataforDisplay();
		this.setAccumulativeFrequency();
		//fireActionPerformed(COMMAND_DATARANGE_SET);
		repaint();
		dialog1.setVisible(false);
	}

    /**
     * New data ranges setup dialog.
     * @param x
     * @param y
     */
	private void showDialog2 (int x, int y) {
		JFrame dummyFrame = new JFrame();
		dialog2 = new JDialog(dummyFrame, "Frequency Range Configuer", true);
		JButton actionButton;
		JButton resetButton;
		dialog2.setLocation(x, y);
		dialog2.setSize(300, 100);
		dialog2.getContentPane().setLayout(new GridLayout(3, 2));
		yAxisMinField.setText(Double.toString(yAxisExtents[0]));
		yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
        //create buttons for action
		actionButton = new JButton("Apply");
		actionButton.addActionListener(new java.awt.event.ActionListener() {

            /**
             * Button to set up new data ranges shown up in scatter plot.
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				try {
					actionButton2_actionPerformed(e);
				} catch (Exception exception) {exception.printStackTrace();}
			}
		});
		resetButton = new JButton("Reset");
		resetButton.addActionListener(new java.awt.event.ActionListener() {

            /**
             * put your documentation comment here
             * @param e
             */
			public void actionPerformed (ActionEvent e) {
				resetButton2_actionPerformed(e);
			}
		});
		dialog2.getContentPane().add(new JLabel(("Frequency" + " Min")));
		dialog2.getContentPane().add(yAxisMinField);
		dialog2.getContentPane().add(new JLabel(("Frequency" + " Max")));
		dialog2.getContentPane().add(yAxisMaxField);
		dialog2.getContentPane().add(actionButton);
		dialog2.getContentPane().add(resetButton);
		dialog2.setVisible(true);
	}

    /**
     * Set up new data ranges to show.
     * @param e
     */
	private void actionButton2_actionPerformed (ActionEvent e) {
        //get the input data from text field
		yAxisExtents[0] = Double.parseDouble(yAxisMinField.getText());
		yAxisExtents[1] = Double.parseDouble(yAxisMaxField.getText());
		this.histArray.setExtent(this.yAxisExtents);
		this.setupDataforDisplay();
		//fireActionPerformed(COMMAND_DATARANGE_SET);
		logger.finest("ok, fire event.");
		repaint();
		dialog2.setVisible(false);
	}

    /**
     * put your documentation comment here
     * @param e
     */
	private void resetButton2_actionPerformed (ActionEvent e) {
		this.histArray.setDataExtent();
		yAxisExtents = (double[])this.histArray.getMaxMinCoorValue().clone();
		this.histogramCalculation ();
		yAxisMinField.setText(Double.toString(yAxisExtents[0]));
		yAxisMaxField.setText(Double.toString(yAxisExtents[1]));
		this.setupDataforDisplay();
		//fireActionPerformed(COMMAND_DATARANGE_SET);
		repaint();
		dialog2.setVisible(false);
	}
    /**
     * put your documentation comment here
     * @param e
     */
	private void maybeShowPopup (MouseEvent e) {
		if (e.isPopupTrigger()) {
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public void mouseClicked(MouseEvent e){
		int count = e.getClickCount();
		//With shift pressed, it will continue to select.
		if (!(e.isShiftDown())){
//			this.selRecords.clear();
			this.selectedRecords.clear();

			if (this.selectionArray == null || this.selectionArray.length != this.histNumber){
				this.selectionArray = new double[this.histNumber];
			} else {
				for (int i = 0; i < this.selectionArray.length; i ++){
					this.selectionArray[i] = 0;
				}
			}
		}
		int[] mousePos = new int[2];
		mousePos[0] = e.getX();
		mousePos[1] = e.getY();
		//single click, select performed.
		
		if (count == 2) {
			for (int i = 0; i < this.histNumber; i ++){
				if (this.histRecs[i].contains(mousePos[0],mousePos[1])){
					for (int j = 0; j < this.histRecords[i].size(); j ++){
						int index = ((Integer)this.histRecords[i].get(j)).intValue();
						this.selectedRecords.set(index, true);
						
					}
					
					//fireActionPerformed ();
					continue;
				}
			}
			
			int counter = 0;
			counter = this.selectedRecords.cardinality();
			int[] selInts = new int[counter];
			counter = 0;
			for (int i = 0; i < this.selectedRecords.length(); i++){
				boolean val = this.selectedRecords.get(i);
				if(val){
					selInts[counter] = i;
					counter++;
				}
			}
			this.fireSelectionChanged(selInts);
			this.setSelections(this.selectedRecords);
			//this.setSelectionScreen();
			repaint();
		}
	}

	public void mousePressed(MouseEvent e){
		if (e.isPopupTrigger())
			maybeShowPopup(e);
	}

	public void mouseReleased(MouseEvent e){
		if (e.isPopupTrigger()){
			maybeShowPopup(e);
		}
	}

	public void mouseEntered(MouseEvent e){
	}

	public void mouseExited(MouseEvent e){
	}

	/**
	 * adds an ActionListener to the button
	 */
	public void addActionListener (ActionListener l) {
		listenerListAction.add(ActionListener.class, l);
	}

	/**
	 * removes an ActionListener from the button
	 */
	public void removeActionListener (ActionListener l) {
		listenerListAction.remove(ActionListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type. The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	public void fireActionPerformed () {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerListAction.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
			ActionEvent e2 = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"OK");
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
				// Lazily create the event:
				((ActionListener)listeners[i + 1]).actionPerformed(e2);
			}
		}
	}
	/**
	 * adds an SelectionListener.
	 * 
	 * @see EventListenerList
	 */
	public void addSelectionListener(SelectionListener l) {
		listenerList.add(SelectionListener.class, l);
	}

	/**
	 * removes an SelectionListener from the component.
	 * 
	 * @see EventListenerList
	 */
	public void removeSelectionListener(SelectionListener l) {
		listenerList.remove(SelectionListener.class, l);

	}

	/**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
	protected void fireSelectionChanged(int[] newSelection) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		SelectionEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == SelectionListener.class) {
				// Lazily create the event:
				if (e == null) {
					e = new SelectionEvent(this, newSelection);
				}
				((SelectionListener) listeners[i + 1]).selectionChanged(e);
			}
		}// next i

	}
	public void dataSetChanged(DataSetEvent e) {
		this.setVariableName(e.getDataSetForApps().getNumericArrayName(0));
		this.setData(e.getDataSetForApps().getNumericDataAsDouble(0));
		
	}

	public void selectionChanged(SelectionEvent e) {
		int[] selected = e.getSelection();
		if (selected == null) {
			return;
		} else {
			this.selectedRecords.clear();
			for (int i = 0; i < selected.length; i++) {
				this.selectedRecords.set(i,true);
			}
		}
		this.setSelections(this.selectedRecords);
		this.repaint();
		
	}
}
