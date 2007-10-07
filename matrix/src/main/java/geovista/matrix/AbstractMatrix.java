package geovista.matrix;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GeoVISTA Center</p>
 * @author Xiping Dai
 * @version 1.0
 */
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionListener;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.ColorArrayEvent;
import edu.psu.geovista.common.event.ColorArrayListener;
import edu.psu.geovista.common.event.ConditioningEvent;
import edu.psu.geovista.common.event.ConditioningListener;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.IndicationEvent;
import edu.psu.geovista.common.event.IndicationListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;
import edu.psu.geovista.common.event.SubspaceEvent;
import edu.psu.geovista.common.event.SubspaceListener;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassification;
import edu.psu.geovista.symbolization.BivariateColorSymbolClassificationSimple;
import edu.psu.geovista.symbolization.ColorSymbolClassificationSimple;
import edu.psu.geovista.symbolization.ColorSymbolizer;
import edu.psu.geovista.symbolization.event.ColorClassifierEvent;
import edu.psu.geovista.symbolization.event.ColorClassifierListener;
import geovista.common.classification.Classifier;

public abstract class AbstractMatrix
    extends JPanel
    implements MouseListener, MouseMotionListener, ChangeListener,
    ListSelectionListener,
    SelectionListener, DataSetListener, ConditioningListener,
    ColorArrayListener, IndicationListener, SubspaceListener, Serializable,
    ColorClassifierListener {
	protected final static Logger logger = Logger.getLogger(AbstractMatrix.class.getName());
    protected static final int DEFAULT_MAX_NUM_ARRAYS = 3;
    protected static final int DEFAULT_PANEL_HEIGHT_PIXELS = 300;
    protected static final int DEFAULT_PANEL_WIDTH_PIXELS = 300;
	protected static final int DEFAULT_BUTTON_CONSTRAINTS = 4;
    protected transient Class elementClass;
    protected transient Object[] dataObject;
    protected transient DataSetForApps dataSet;    
    protected transient int[] conditionArray = null;
    protected transient String[] attributesDisplay;
	protected transient String[] attributeDescriptions;
    protected transient int plottedBegin = 0;
    protected transient int plotNumber;
    protected transient int maxNumArrays = DEFAULT_MAX_NUM_ARRAYS;
    protected transient int[] plottedAttributes;
    //protected transient Vector selectedObvs = new Vector();
    protected transient Color[] multipleSelectionColors;
    protected transient Color[] colorArrayForObs;
    protected transient MatrixElement[] element;
    protected transient int[] selectedObvsInt;
	protected transient int[] selections;//short array, sharing with other components
    protected transient BivariateColorSymbolClassification bivarColorClasser;
    protected transient Color selectionColor = Color.blue;
    protected transient Color background = Color.white;
	protected transient boolean selOriginalColorMode = true;
    protected transient GridBagLayout matrixLayout;
    protected transient Point posLast = new Point();
    protected transient Point posNew = new Point();
    protected transient Point posDrag = new Point();
    protected transient boolean recreate = true;
    protected transient int panelWidthPixels = DEFAULT_PANEL_WIDTH_PIXELS;
    protected transient int panelHeightPixels = DEFAULT_PANEL_HEIGHT_PIXELS;
    protected transient JList attList;
	protected transient JList descriptionList;
    protected transient SPGridBagConstraints c;
    protected static ImageIcon leftRightArrow;
    protected static ImageIcon topDownArrow;
    protected static final Insets nullInsets = new Insets(0, 0, 0, 0);;
    private transient Vector indicListeners;
	protected String[] varTags;
	protected SPTagButton[] columnButton;
	protected SPTagButton[] rowButton;
	protected JButton configButton;
    protected abstract void createMatrix();  

    public AbstractMatrix() {
        this.setPreferredSize(new Dimension(300, 300));
    }

    /**
     * Set up the default size of matrix panel.
     * @param widthPixels
     * @param heightPixels
     */
    public void setPanelSize(int widthPixels, int heightPixels) {
        setSize(widthPixels, heightPixels);
        //setPreferredSize(new Dimension(widthPixels, heightPixels));
        this.setMinimumSize(new Dimension(widthPixels, heightPixels));
        super.setLayout(null);
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Class getElementClass() {
        return this.elementClass;
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
    public void setDataObject(Object[] data) {
        if (data == null) {
            return;
        }
        this.dataSet = new DataSetForApps(data);
        this.setDataSet(dataSet);
    }
    public void setDataSet(DataSetForApps dataSet){
           
        this.dataSet = dataSet;
        //XXX this should be changed to just use the
        //data object passed in, not create a new one
        this.dataObject = dataSet.getDataSetNumericAndSpatial();
        DataSetForApps dataObjTransfer = new DataSetForApps(this.dataObject);

        	
        this.conditionArray = dataObjTransfer.getConditionArray();
        this.attributesDisplay = dataSet.getAttributeNamesNumeric();
		if (dataSet.getAttributeDescriptions() != null){
			this.attributeDescriptions = dataSet.getAttributeDescriptions();
		} else{
			this.attributeDescriptions = null;
		}
        int numLen;
        numLen = dataObjTransfer.getNumberNumericAttributes();
        //check if there are enough attributes to display in matrix from specified beginning.
        if (plottedBegin >= numLen) {
            System.err.println("There aren't enough attributes to display! Please reset the begin display attribute or reload a more attribute data file.");
            return;
        }
        plotNumber = (numLen <= maxNumArrays) ? numLen : maxNumArrays;
        if ( (plottedBegin + plotNumber) > numLen) {
            plotNumber = numLen - plottedBegin;
        }
        this.plottedAttributes = new int[plotNumber];
        for (int i = plottedBegin; i < plottedBegin + plotNumber; i++) {
            plottedAttributes[i - plottedBegin] = i;
        }
        //if (!selectedObvs.isEmpty()) {
        //    selectedObvs.clear();
        //}
		int numObvs;
		numObvs = dataObjTransfer.getNumObservations();
		this.selectedObvsInt = new int[numObvs];
        init();
        //registerIndicationListeners();
    }

    protected void registerIndicationListeners() {
        //register "this" with each element
        for (int k = 0; k < element.length; k++) {
            MatrixElement otherElement = element[k];
            otherElement.addIndicationListener(this);
        }
        //register all added indication listeners
        if (this.indicListeners == null) {
            return;
        }
        Vector listeners = this.indicListeners;
        for (Enumeration e = listeners.elements(); e.hasMoreElements(); ) {
            IndicationListener listener = (IndicationListener) e.nextElement();
            for (int k = 0; k < element.length; k++) {
                MatrixElement otherElement = element[k];
                otherElement.addIndicationListener(listener);
            }
        }

    }

	public void setSelectedObvs(int[] selected) {
		logger.finest("Set Selected Obs: ");
		if(selected == null){
			return;
		}else{
			for(int i = 0; i < this.selectedObvsInt.length; i++){
				this.selectedObvsInt[i] = 0;
			}
			for(int i = 0; i < selected.length; i++){
				this.selectedObvsInt[selected[i]] = 1;
			}
		}
		this.multipleSelectionColors = null;
		//Once selection from other components has been set, pass it to each element inside of matrix.
		for (int k = 0; k < this.element.length; k++) {
			MatrixElement otherElement = element[k];
			otherElement.setSelections(this.selectedObvsInt);
			otherElement.setMultipleSelectionColors(this.
				multipleSelectionColors);
		}
		repaint();
	}

	/**
	 * Return index array for selected observations.
	 * @return
	 */
	public int[] getSelectedObvs() {
		Vector selectedObvs = new Vector();
		for(int i = 0; i < this.selectedObvsInt.length; i ++){
			if (this.selectedObvsInt[i] == 1){
				selectedObvs.add(new Integer(i));
			}
		}
		selections = new int[selectedObvs.size()];
		for (int i = 0; i < selectedObvs.size(); i++) {
			selections[i] = ( (Integer) selectedObvs.get(i)).intValue();
		}
		return selections;
    }

	private void setIndicatedObv(int indicated) {
		if (this.dataObject == null) {
			return;
		}
		//Once selection from other components has been set, pass it to each element inside of matrix.
		for (int k = 0; k < this.element.length; k++) {
//			if (this.plotNumber * this.plotNumber != this.element.length){
//				return;
//			}
			MatrixElement otherElement = element[k];
			if (otherElement == null){
				return;
			}
			otherElement.setIndication(indicated);
		}
	}

    /**
     * Set the maximum number of columns and rows can be shown in matrix.
     * @param maxNum
     */
    public void setMaxNumArrays(int maxNum) {
        this.maxNumArrays = maxNum;
    }

    /**
     * Get the maximum number of columns and rows can be shown in matrix.
     * @return
     */
    public int getMaxNumArrays() {
        return maxNumArrays;
    }

    /**
     * Set the beginning varaible.
     * @param plottedBegin
     */
    public void setPlottedBegin(int plottedBegin) {
        this.plottedBegin = plottedBegin;
    }

    /**
     * Get the beginning varaible.
     * @return
     */
    public int getPlottedBegin() {
        return plottedBegin;
    }

    /**
     * Set the attribute arrays which need to display in matrix by indices.
     * @param plottedAtt
     */
    public void setPlottedAttributes(int[] plottedAtt) {
        this.plottedAttributes = plottedAtt;
    }

    /**
     * put your documentation comment here
     * @param condition
     */
    public void setConditionArray(int[] condition) {

        if (condition == null) {
            return;
        }
        else {
            this.conditionArray = condition;
            for (int k = 0; k < plotNumber * plotNumber; k++) {
                MatrixElement otherElement = element[k];
                if (otherElement != null) {
                    otherElement.setConditionArray(conditionArray);
                }
            }
            repaint();
        }
    }

    /**
     * Set up classification color for each matrix element.
     * @param c
     */
    public void setBivarColorClasser(BivariateColorSymbolClassification
                                     bivarColorClasser) {

        this.bivarColorClasser = bivarColorClasser;
        if (this.bivarColorClasser == null) {
            return;
        }
		int row, column;
		boolean reverseColor = false;
        for (int k = 0; k < this.element.length; k++) {
            MatrixElement otherElement = element[k];
            String className = otherElement.getClass().getName();

            if (className.equals("geovista.geoviz.scatterplot.ScatterPlot")) {
				row =k/this.plotNumber;
				column = k%this.plotNumber;
				if (row > column){
					reverseColor = true;
				}else{
					reverseColor = false;
				}
                otherElement.setBivarColorClasser(this.bivarColorClasser, reverseColor);
            }
        }
    }

    public BivariateColorSymbolClassification getBivarColorClasser() {
        return this.bivarColorClasser;
    }

    /**
     * Set up selection color for each matrix element. See in bean proporty.
     * @param c
     */
    public void setSelectionColor(Color c) {
        this.selectionColor = c;
        for (int k = 0; k < this.element.length; k++) {
            MatrixElement otherElement = element[k];
            otherElement.setSelectionColor(this.selectionColor);
        }
    }

    public void setColorArrayForObs(Color[] colorArray) {
        if (this.element == null){
          return;
        }
        this.colorArrayForObs = colorArray;
        for (int k = 0; k < this.element.length; k++) {
            MatrixElement otherElement = element[k];
            otherElement.setColorArrayForObs(this.colorArrayForObs);
        }
        repaint();
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Color getSelectionColor() {
        return this.selectionColor;
    }

	public boolean getSelOriginalColorMode() {
		return selOriginalColorMode;
	}

	public void setSelOriginalColorMode(boolean selOriginalColorMode) {
		this.selOriginalColorMode = selOriginalColorMode;
		for (int k = 0; k < this.element.length; k++) {
			MatrixElement otherElement = element[k];
			otherElement.setSelOriginalColorMode(this.selOriginalColorMode);
        }
    }
    /**
     * Set up selection. Input is integer array, but internally use vector.
     * @param
     */
    public void setMultipleSelectionColors(Color[] multipleSelectionColors) {
        logger.finest("Set Selected colors: ");
        this.multipleSelectionColors = multipleSelectionColors;
		for(int i = 0; i < this.selectedObvsInt.length; i ++){
			this.selectedObvsInt[i] = 0;
		}
        for (int k = 0; k < plotNumber * plotNumber; k++) {
            MatrixElement otherElement = element[k];
            otherElement.setSelections(this.selectedObvsInt);
            otherElement.setMultipleSelectionColors(this.
                multipleSelectionColors);
        }
        repaint();
    }

    /**
     * Set up background color. See it in bean proporty.
     * @param c
     */
    public void setBackground(Color c) {
        if (c == null) {
            return;
        }
        background = c;
        for (int k = 0; k < plotNumber * plotNumber; k++) {
            MatrixElement otherElement = element[k];
            otherElement.setBackground(this.background);
        }
    }

    /**
     * put your documentation comment here
     * @return
     */
    public Color getBackground() {
        return background;
    }

    //Costomization of GridBagConstraints. We want to trace the position of rows and column for
    //the function of row and column reposition.
    protected class SPGridBagConstraints
        extends GridBagConstraints {
        protected int column;
        protected int row;
    }

    public class SPTagButton
        extends JButton
        implements MouseListener, MouseMotionListener {

        //String buttonLabel;
        /**
         * put your documentation comment here
         * @param 		String label
         */
        SPTagButton(String label) {
            super(label);
            
            //buttonLabel = label;
        }

        /**
         * put your documentation comment here
         * @param 		ImageIcon icon
         */
        SPTagButton(ImageIcon icon) {
            super(icon);
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            SPGridBagConstraints gbconst = (SPGridBagConstraints) matrixLayout.
                getConstraints(button);
            posLast = new Point(gbconst.column, gbconst.row);
            posDrag = (Point) posLast.clone();
        }

        /**
             * Mouse release event. Detect the destination of row or column reposition.
         * @param e
         */
        public void mouseReleased(MouseEvent e) {
            posNew = SwingUtilities.convertPoint( (SPTagButton) e.getSource(),
                                                 e.getX(),
                                                 e.getY(), AbstractMatrix.this);
            posNew = matrixLayout.location(posNew.x, posNew.y);
            if (posNew.x > 4 * (plotNumber - 1)) {
                posNew.setLocation(posNew.x / 4 + 1, posNew.y / 4);
            }
            else {
                posNew.setLocation(posNew.x / 4, posNew.y / 4);
                logger.finest("PosNewX: " + posNew.x + "posNewY: " + posNew.y);
            }
            int lastPos = 0;
            int newPos = 0;
            if (!validCellPos(posDrag) || !validCellPos(posNew)) {
                return;
            }
            if (posDrag.x != posNew.x) {
                lastPos = posLast.x;
                newPos = posNew.x;
            }
            else if (posDrag.y != posNew.y) {
                lastPos = posLast.y;
                newPos = posNew.y;
            }
            if (lastPos != newPos) {
                moveRowAndColumn(lastPos, newPos);
                posDrag = posNew;
                repaint();
            }
        }

        /**
         * Mouse drag event. Detect the rows or columns on which row or column reposition passed.
         * @param e
         */
        public void mouseDragged(MouseEvent e) {
            posNew = SwingUtilities.convertPoint( (SPTagButton) e.getSource(),
                                                 e.getX(),
                                                 e.getY(), AbstractMatrix.this);
            posNew = matrixLayout.location(posNew.x, posNew.y);
            logger.finest("PosNew0X: " + posNew.x + "posNew0Y: " + posNew.y);
            if (posNew.x > 4 * (plotNumber - 1)) {
                posNew.setLocation(posNew.x / 4 + 1, posNew.y / 4);
            }
            else {
                posNew.setLocation(posNew.x / 4, posNew.y / 4);
                logger.finest("PosNewX: " + posNew.x + "posNewY: " + posNew.y);
            }
            int lastPos = 0;
            int newPos = 0;
            if (!validCellPos(posDrag) || !validCellPos(posNew)) {
                return;
            }
            if (posDrag.x != posNew.x) {
                lastPos = posDrag.x;
                newPos = posNew.x;
            }
            else if (posDrag.y != posNew.y) {
                lastPos = posDrag.y;
                newPos = posNew.y;
            }
            if (lastPos != newPos) {
                moveRowAndColumn(lastPos, newPos);
                posDrag = posNew;
                repaint();
            }
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseExited(MouseEvent e) {
            ;
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseMoved(MouseEvent e) {
            ;
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseEntered(MouseEvent e) {
            ;
        }

        /**
         * put your documentation comment here
         * @param e
         */
        public void mouseClicked(MouseEvent e) {
            ;
        }
    }

    /**
     * put your documentation comment here
     */
    protected void init() {
        if (!this.recreate) {
            return; // maybe display error message.
        }
        this.removeAll();
        setPanelSize(panelWidthPixels, panelHeightPixels);

        if (this.dataObject != null) {
			attList = new JList(this.attributesDisplay);
			if (this.attributeDescriptions != null){
				descriptionList = new JList(this.attributeDescriptions);
			}
            this.element = new MatrixElement[plotNumber * plotNumber];
            matrixLayout = new GridBagLayout();
            //c = new SPGridBagConstraints();
            
            //c.fill = GridBagConstraints.BOTH;
			varTags = new String[plotNumber];
            createMatrix();
            Container parent = getParent();
            if (parent != null) {
                parent.validate();
            }
            else {
                validate();
            }
        }
        registerIndicationListeners();
    }

    /**
     * put your documentation comment here
     * @param pos
     * @return
     */
    protected boolean validCellPos(Point pos) {
        return (pos.x == 0 && pos.y != 0) || (pos.x != 0 && pos.y == 0);
    }

    /**
     * Shift columns or rows in the matrix.
     * @param lastPos
     * @param newPos
     */
    protected void moveRowAndColumn(int lastPos, int newPos) {
        logger.finest("move row or column...");
        int indicesRow;
        int indicesCol;
        int[] indicesLast1;
        int[] indicesNew1;
        int[] indicesLast2;
        int[] indicesNew2;
		String varTagMoved = new String(varTags[lastPos - 1]);
		varTags[lastPos - 1] = varTags[newPos - 1];
		this.columnButton[lastPos - 1].setText(varTags[lastPos - 1]);
		this.rowButton[lastPos - 1].setText(varTags[lastPos - 1]);
		varTags[newPos - 1] = varTagMoved;
		this.columnButton[newPos - 1].setText(varTags[newPos - 1]);
		this.rowButton[newPos - 1].setText(varTags[newPos - 1]);
        for (int i = 0; i < plotNumber; i++) {
            indicesLast1 = (element[i * plotNumber + lastPos -
                            1].getElementPosition());
            indicesLast2 = (element[ (lastPos - 1) * plotNumber +
                            i].getElementPosition());
            logger.finest("Indices before move" + indicesLast1[0] + indicesLast1[1]);
            indicesRow = indicesLast1[0];
            indicesCol = indicesLast2[1];
            indicesNew1 = element[i * plotNumber + newPos -
                1].getElementPosition();
            indicesNew2 = element[ (newPos - 1) * plotNumber +
                i].getElementPosition();
            logger.finest("Indices after move" + indicesNew1[0] + indicesNew1[1]);
            indicesLast1[0] = indicesNew1[0];
            indicesLast2[1] = indicesNew2[1];
            element[i * plotNumber + lastPos -
                1].setElementPosition(indicesLast1);
            element[ (lastPos - 1) * plotNumber +
                i].setElementPosition(indicesLast2);
            indicesNew1[0] = indicesRow;
            indicesNew2[1] = indicesCol;
            element[i * plotNumber + newPos - 1].setElementPosition(indicesNew1);
            element[ (newPos - 1) * plotNumber +
                i].setElementPosition(indicesNew2);

            /*this.remove((Component)this.element[i*plotNumber + lastPos - 1]);
                this.add((Component)this.element[i*plotNumber + newPos - 1], i*plotNumber + lastPos - 1);
                 this.remove((Component)this.element[i*plotNumber + newPos - 1]);
                this.add((Component)this.element[i*plotNumber + lastPos - 1], i*plotNumber + newPos - 1);
                 this.remove((Component)this.element[(lastPos - 1)*plotNumber + i]);
                add((Component)this.element[(newPos - 1)*plotNumber + i], (lastPos - 1)*plotNumber + i);
                 this.remove((Component)this.element[(newPos - 1)*plotNumber + i]);
                add((Component)this.element[(lastPos - 1)*plotNumber + i], (newPos - 1)*plotNumber + i);
                this.revalidate();
                repaint();*/
        }

    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void mousePressed(MouseEvent e) {}

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseReleased(MouseEvent e) {}

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseExited(MouseEvent e) {
        ;
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseDragged(MouseEvent e) {}

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseMoved(MouseEvent e) {
        ;
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
        ;
    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        ;
    }

    public void selectionChanged(SelectionEvent e) {
        if (e.getMultipleSlectionColors() != null) {
            this.setMultipleSelectionColors(e.getMultipleSlectionColors());
        }
        else {
            this.setSelectedObvs(e.getSelection());
        }
    }

    public void indicationChanged(IndicationEvent e) {
        this.setIndicatedObv(e.getIndication());
    }

    public void dataSetChanged(DataSetEvent e) {
        this.setDataObject(e.getDataSet());
    }

    public void conditioningChanged(ConditioningEvent e) {
        this.setConditionArray(e.getConditioning());
    }

    public void colorArrayChanged(ColorArrayEvent e) {
        logger.finest("colorArrayChanged...");
        this.setColorArrayForObs(e.getColors());
    }

    public void colorClassifierChanged(ColorClassifierEvent e) {
        if (e.getColorSymbolClassification() == null || this.dataObject == null) {
            return;
        }
        boolean isX = false;
        if (e.getOrientation() == ColorClassifierEvent.SOURCE_ORIENTATION_X) {
            isX = true;
        }
        if (this.bivarColorClasser == null){
            this.bivarColorClasser = new BivariateColorSymbolClassificationSimple();
        }
        BivariateColorSymbolClassificationSimple biColor = (
            BivariateColorSymbolClassificationSimple)this.bivarColorClasser;
        ColorSymbolClassificationSimple colorClasser = (
            ColorSymbolClassificationSimple) e.getColorSymbolClassification();
        ColorSymbolizer colorer = colorClasser.getColorer();
        Classifier classer = colorClasser.getClasser();

        if (isX) {
            logger.finest("setting x");
            biColor.setClasserX(classer);
            biColor.setColorerX(colorer);
        }
        else {
            logger.finest("Setting Y");
            biColor.setClasserY(classer);
            biColor.setColorerY(colorer);
        }
//        BivariateColorSymbolClassificationSimple biColor2 = new
//            BivariateColorSymbolClassificationSimple();
//        ColorSymbolizer xSym = biColor.getColorerX();
//        ColorSymbolizer ySym = biColor.getColorerY();
//        biColor2.setColorerX(ySym);
//        biColor2.setColorerY(xSym);
        //this.bivarColorClasser = biColor;
        if (this.bivarColorClasser == null) {
            return;
        }

		int row, column;
		boolean reverseColor = false;
        for (int k = 0; k < this.element.length; k++) {
            MatrixElement otherElement = element[k];
            String className = otherElement.getClass().getName();
            if (className.equals("geovista.geoviz.scatterplot.ScatterPlot")) {
                //otherElement.setBivarColorClasser(biColor2);
				row =k/this.plotNumber;
				column = k%this.plotNumber;
				if (row > column){
					reverseColor = true;
				}else{
					reverseColor = false;
				}
				otherElement.setBivarColorClasser(this.bivarColorClasser, reverseColor);
            }
            else {
                otherElement.setBivarColorClasser(this.bivarColorClasser, false);
            }
        }

    }

    public void subspaceChanged(SubspaceEvent e) {
        int[] list = e.getSubspace();
        int maxVars = 8;
        if (list.length > maxVars) {
            list = new int[maxVars];
            for (int i = 0; i < maxVars; i++) {
                list[i] = e.getSubspace()[i];
            }
        }
        plottedAttributes = list;
        plotNumber = plottedAttributes.length;
        this.init();
    }

    /**
     * adds an IndicationListener to the elements
     */
    public void addIndicationListener(IndicationListener l) {
        if (this.indicListeners == null) {
            this.indicListeners = new Vector();
        }

        this.indicListeners.add(l);
        if (element != null) {
            for (int k = 0; k < element.length; k++) {

                MatrixElement otherElement = element[k];
                otherElement.addIndicationListener(l);
            }

        }
    }

    /**
     * removes an IndicationListener from the elements
     */
    public void removeIndicationListener(IndicationListener l) {

		if (this.element == null){
			return;
		}
        for (int k = 0; k < this.element.length; k++) {
            MatrixElement otherElement = element[k];
            otherElement.removeIndicationListener(l);
        }
        this.indicListeners.remove(l);

    }

    /**
     * adds an SelectionListener.
     * @see EventListenerList
     */
    public void addSelectionListener(SelectionListener l) {
        listenerList.add(SelectionListener.class, l);
    }

    /**
     * removes an SelectionListener from the component.
     * @see EventListenerList
     */
    public void removeSelectionListener(SelectionListener l) {
        listenerList.remove(SelectionListener.class, l);

    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
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
                ( (SelectionListener) listeners[i + 1]).selectionChanged(e);
            }
        } //next i

    }

    /**
     * put your documentation comment here
     * @param e
     */
    public void stateChanged(ChangeEvent e) {
    }

    /**
     * put your documentation comment here
     * @param l
     */
    public void addChangeListener(ChangeListener l) {
        this.listenerList.add(ChangeListener.class, l);
    }

    /**
     * put your documentation comment here
     * @param l
     */
    public void removeChangeListener(ChangeListener l) {
        this.listenerList.remove(ChangeListener.class, l);
    }

    /**
     * put your documentation comment here
     */
    protected void fireChangeEvent() {
        Object[] listeners = this.listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ( (ChangeListener) listeners[i +
                    1]).stateChanged(new ChangeEvent(this));
            }
        } // end for
    }

    /**
     * put your documentation comment here
     * @param oos
     * @exception IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * put your documentation comment here
     * @param ois
     * @exception ClassNotFoundException, IOException
     */
    private void readObject(ObjectInputStream ois) throws
        ClassNotFoundException,
        IOException {
        ois.defaultReadObject();
    }

    //Set up arrow figure for the first row and column buttons.
    static  {
        try {
            Class cl = UniPlotMatrix.class;
            URL urlGifH = cl.getResource("arrow_h32.gif");
            URL urlGifV = cl.getResource("arrow_v32.gif");
            leftRightArrow = new ImageIcon(urlGifH);
            topDownArrow = new ImageIcon(urlGifV);
            // Just do this create once
            
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * adds a SubspaceListener
     */
    public void addSubspaceListener(SubspaceListener l) {
        listenerList.add(SubspaceListener.class, l);

    }

    /**
     * removes an SubspaceListener from the component
     */
    public void removeSubspaceListener(SubspaceListener l) {
        listenerList.remove(SubspaceListener.class, l);

    }

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
    public void fireSubspaceChanged(int[] selection) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        SubspaceEvent e = null;

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SubspaceListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new SubspaceEvent(this, selection);
                }

                ( (SubspaceListener) listeners[i + 1]).subspaceChanged(e);
            }
        } //next i

    }

}