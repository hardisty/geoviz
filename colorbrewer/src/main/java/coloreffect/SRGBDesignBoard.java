package coloreffect;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SRGBDesignBoard extends JPanel implements ActionListener, ChangeListener, KeyListener, MouseListener {
	protected final static Logger logger = Logger.getLogger(SRGBDesignBoard.class.getName());
  //create a 3D array to store the current color array (the maximum size is 15X15)
  int[][][] currentColorarray = new int[15][15][3];

  //create three 3D arrays to store the color array for each test field(the maximum size is 15X15)
  int[][][][] colorarray = new int[15][15][3][3];
  int[][][][] labcolorarray = new int[15][15][3][3];
  GridLayout[] gridLayout = new GridLayout[3];

  //create a filechooser
  final JFileChooser fc = new JFileChooser();

  //create a set of objects to store user inputs
  Quaseqbellcurve quaseqbellcurve1;
  QuaseqCone quaseqcone1;
  Quaseqhalfellipsoid quaseqhalfellipsoid1;
  Quaseqparabola quaseqparabola1;

  DivdivBellshape divdivbellcurve1;
  Divdivhalfellipsoid divdivhalfellipsoid1;

  Divseqellipseup divseqellipseup1;
  Divseqellipsedn divseqellipsedn1;
  Divseqgrids divseqgrid1;
  DivseqTrapezoid DivseqTrapezoid1;

  Seqseqgraydiamond seqseqgraydiamond1;

  Seqseqnongraydiamond seqseqnongraydiamond1;

  //create a series of objects to store user inputs
  boolean isQuaseq;
  boolean isDivdiv;
  boolean isDivseq;
  boolean isSeqseqgray;
  boolean isSeqseqnongray;

  boolean isQuaseqbellcurve;
  boolean isQuaseqcone;
  boolean isQuaseqhalfellipsoid;
  boolean isQuaseqparabola;
  boolean isDivdivBellshape;
  boolean isDivdivhalfellipsoid;
  boolean isDivseqellipsedn;
  boolean isDivseqellipseup;
  boolean isDivseqgrid;
  boolean isDivseqTrapezoid;
  boolean isSeqseqgraydiamond;
  boolean isSeqseqnongraydiamond;

  int hclass;
  int vclass;
  int maxl;
  int minl;
  int dx;
  int dy;
  int sh;

  //f holds the popup test field
  JFrame f = new JFrame();

  //create northpanel to place the controls
  JPanel northpanel = new JPanel();

  JPanel northeastpanel = new JPanel();

  //create typeselectionpanel for the selection of the scheme types
  SchemeType schemetypepanel = new SchemeType();

  //create a buttongroup to hold the typePanel buttons
  ButtonGroup schemetypegroup = new ButtonGroup();

  //add four radio buttons for the selection of the scheme types, group them, and add them to the typePanel
  JRadioButton quaseqButton = new JRadioButton("Qua-Seq");
  JRadioButton divdivButton = new JRadioButton("Div-Div");
  JRadioButton divseqButton = new JRadioButton("Div-Seq");
  JRadioButton seqseqgrayButton = new JRadioButton("Seq-Seq-Gray");
  JRadioButton seqseqnongrayButton = new JRadioButton("Seq-Seq-nonGray");

  //create basicsPanel for the selection of the number of classes on each dimension, and lightness range
  Object basicsPanel = new Object();
  Datacontrol horizontal = new Datacontrol();
  Datacontrol vertical = new Datacontrol();
  Datacontrol maxlightness = new Datacontrol();
  Datacontrol minlightness = new Datacontrol();
  Datacontrol lightnessrange = new Datacontrol();
  Datacontrol range = new Datacontrol();

  //create methodPanel to hold the function, the sampling method and parameters
  JPanel methodPanel = new JPanel();

  //Functionpanel functionPanel = new Functionpanel();
  //CardLayout f1 = new CardLayout();

  Functionpanel deviationPanel = new Functionpanel();
  GridLayout g1 = new GridLayout(1, 3);

  Object objectPanel = new Object();
  CardLayout o1 = new CardLayout();

  Object parameterPanel = new Object();
  CardLayout p1 = new CardLayout();

  Object functionPanel = new Object();



  //images used in functionPanels
  //ImageIcon quaseqconefunctionIcon = new ImageIcon(this.getClass().getResource("resources/cone.jpg"));
  //ImageIcon quaseqparabolafunctionIcon = new ImageIcon(this.getClass().getResource("resources/parabola.jpg"));
  //ImageIcon quaseqhalfspherefunctionIcon = new ImageIcon(this.getClass().getResource("resources/halfsphere.jpg"));

  //a list of cards to use in functionPanels
  //JLabel emptyfunction = new JLabel();

  //JLabel quaseqconefunction = new JLabel(quaseqconefunctionIcon);
  //JLabel quaseqparabolafunction = new JLabel(quaseqparabolafunctionIcon);
  //JLabel quaseqhalfspherefunction = new JLabel(quaseqhalfspherefunctionIcon);

  //a list of controls to use in the deviationpanel
  Datacontrol xdeviation = new Datacontrol();
  Datacontrol ydeviation = new Datacontrol();
  Datacontrol startinghue = new Datacontrol();

  //a list of cards to use in objectPanel, and their respective attachments (with an empty card at the top level)
  JPanel emptyobject = new JPanel();

  JPanel quaseqobject = new JPanel();
  JRadioButton quaseqbellcurveButton = new JRadioButton("Bell Curve");
  JRadioButton quaseqconeButton = new JRadioButton("Cone");
  JRadioButton quaseqhalfellipsoidButton = new JRadioButton("Half Ellipsoid");
  JRadioButton quaseqparabolaButton = new JRadioButton("Parabola");

  JPanel divdivobject = new JPanel();
  JRadioButton divdivbellcurveButton = new JRadioButton("Bell Curve");
  JRadioButton divdivconeButton = new JRadioButton("Cone");
  JRadioButton divdivhalfellipsoidButton = new JRadioButton("Half Ellipsoid");

  JPanel divseqobject = new JPanel();
  JRadioButton divseqellipseupButton = new JRadioButton("Fan Upward");
  JRadioButton divseqellipsednButton = new JRadioButton("Fan Downward");
  JRadioButton divseqgridButton = new JRadioButton("Grids");
  JRadioButton DivseqTrapezoidButton = new JRadioButton("Trapezoid");

  JPanel seqseqgrayobject = new JPanel();
  JRadioButton seqseqgraydiamondButton = new JRadioButton("Diamond");

  JPanel seqseqnongrayobject = new JPanel();
  JRadioButton seqseqnongraydiamondButton = new JRadioButton("Tilted Diamond");

  //a list of cards to use in parameterPanel, and their respective attachments
  JPanel emptyparameter = new JPanel();

  JPanel quaseqbellcurveparameter = new JPanel();
  Datacontrol quaseqbellcurvevertex = new Datacontrol();
  Datacontrol quaseqbellcurvedivisor = new Datacontrol();

  JPanel quaseqconeparameter = new JPanel();
  Datacontrol quaseqconeheight = new Datacontrol();
  Datacontrol quaseqconeradius = new Datacontrol();

  JPanel quaseqhalfellipsoidparameter = new JPanel();
  Datacontrol quaseqhalfellipsoidradiusab = new Datacontrol();
  Datacontrol quaseqhalfellipsoidradiusc = new Datacontrol();

  JPanel quaseqparabolaparameter = new JPanel();
  Datacontrol quaseqparabolaconstant = new Datacontrol();

  JPanel divdivbellcurveparameter = new JPanel();
  Datacontrol divdivbellcurvevertex = new Datacontrol();
  Datacontrol divdivbellcurvedivisor = new Datacontrol();

  JPanel divdivconeparameter = new JPanel();
  Datacontrol divdivconeradius = new Datacontrol();
  Datacontrol divdivconeheight = new Datacontrol();

  JPanel divdivhalfellipsoidparameter = new JPanel();
  Datacontrol divdivhalfellipsoidradiusab = new Datacontrol();
  Datacontrol divdivhalfellipsoidradiusc = new Datacontrol();

  JPanel divseqellipseupparameter = new JPanel();
  Datacontrol divseqellipseupe = new Datacontrol();
  Datacontrol divseqellipseupalpha = new Datacontrol();

  JPanel divseqellipsedownparameter = new JPanel();
  Datacontrol divseqellipsedowne = new Datacontrol();
  Datacontrol divseqellipsedownalpha = new Datacontrol();

  JPanel divseqgridparameter = new JPanel();
  Datacontrol divseqgridrange = new Datacontrol();
  Datacontrol divseqgridalpha = new Datacontrol();

  JPanel DivseqTrapezoidparameter = new JPanel();
  Datacontrol DivseqTrapezoidradius = new Datacontrol();
  Datacontrol DivseqTrapezoidalpha = new Datacontrol();

  JPanel seqseqgraydiamondparameter = new JPanel();
  Datacontrol seqseqgraydiamondalpha = new Datacontrol();

  JPanel seqseqnongraydiamondparameter = new JPanel();
  Datacontrol seqseqnongraydiamondalpha = new Datacontrol();
  Datacontrol seqseqnongraydiamondbeta = new Datacontrol();

  JButton openButton = new JButton("Open a File...", createImageIcon("resources/openFile.gif"));
  JButton saveButton = new JButton("Save a File...", createImageIcon("resources/saveFile.gif"));
  JButton exampleButton = new JButton("Examples");


  //create southpanel to place the samples
  JPanel southpanel = new JPanel();

  //create checkboxpanel, checkboxes to notify which sample area is the current one, and current checkbox
  JPanel checkboxpanel = new JPanel();
  JCheckBox[] checkbox = new JCheckBox[3];
  int currentcheckbox;

  //create samplepanel
  JPanel samplepanel = new JPanel();

  //create three panels to place the samples
  JPanel[] samples = new JPanel[3];

  public SRGBDesignBoard() {

    //initializing the set of objects
    this.quaseqbellcurve1 = new Quaseqbellcurve(5, 5, 95, 50, 150, 5000, 0, 0, 0);
    this.quaseqcone1 = new QuaseqCone(5, 5, 95, 50, 150, 120, 0, 0, 0);
    this.quaseqhalfellipsoid1 = new Quaseqhalfellipsoid(5, 5, 95, 50, 120, 120, 120, 0, 0, 0);
    this.quaseqparabola1 = new Quaseqparabola(5, 5, 95, 50, 5000, 0, 0, 0);

    this.divdivbellcurve1 = new DivdivBellshape(5, 5, 120, 100, 6500, 0, 0, 0);
    this.divdivhalfellipsoid1 = new Divdivhalfellipsoid(5, 5, 160, 120, 100, 0, 0, 0);

    this.divseqellipseup1 = new Divseqellipseup(5, 5, -50, -80, 60, 0, 0, 100, 0);
    this.divseqellipsedn1 = new Divseqellipsedn(5, 5, 95, 50, 60, 0, 0, 0, 0);
    this.divseqgrid1 = new Divseqgrids(5, 5, 95, 40, 150, 120, 0, 0, 0);
    this.DivseqTrapezoid1 = new DivseqTrapezoid(5, 5, 95, 50, 60, 100, 0, 0, 0);

    this.seqseqgraydiamond1 = new Seqseqgraydiamond(5, 5, 95, 20, 120, 0, 0, 0);

    this.seqseqnongraydiamond1 = new Seqseqnongraydiamond(5, 5, 95, 20, 120, 15, 0, 0, 0);

    //intialize the test fields to be white and the gridlayout to be 5X5
    for(int i = 0; i < 15; i ++){
      for(int j = 0; j < 15; j ++){
        for(int m = 0; m < 3; m ++){
          for(int n = 0; n < 3; n ++){
            this.colorarray[i][j][m][n] = 255;
          }
        }
      }
    }

    for(int i = 0; i < 3; i ++){
      this.gridLayout[i] = new GridLayout(5, 5);
    }

    this.f.setBounds(this.getX() + 620, this.getY(), 300, 300);

    this.f.setVisible(false);

    this.setPreferredSize(new Dimension(600, 450));

    //customizing the northpanel
    this.add(northpanel, BorderLayout.NORTH);
    northpanel.setPreferredSize(new Dimension(600, 200));
    northpanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    northpanel.setBorder(BorderFactory.createEtchedBorder());

    //add schemetypepanel to the northpanel
    northpanel.add(schemetypepanel);
    schemetypepanel.setPreferredSize(new Dimension(120, 195));
    schemetypepanel.setTitle("Scheme Type");

    //adding schemetypepanel buttons
    schemetypepanel.add(quaseqButton);
    schemetypepanel.add(divdivButton);
    schemetypepanel.add(divseqButton);
    schemetypepanel.add(seqseqgrayButton);
    schemetypepanel.add(seqseqnongrayButton);

    quaseqButton.addActionListener(this);
    divdivButton.addActionListener(this);
    divseqButton.addActionListener(this);
    seqseqgrayButton.addActionListener(this);
    seqseqnongrayButton.addActionListener(this);

    //adding schemetypepanel buttons to the buttongroup
    schemetypegroup.add(quaseqButton);
    schemetypegroup.add(divdivButton);
    schemetypegroup.add(divseqButton);
    schemetypegroup.add(seqseqgrayButton);
    schemetypegroup.add(seqseqnongrayButton);

    northpanel.add(basicsPanel);

    //customizing the basicsPanel
    basicsPanel.setSize(new Dimension(125, 195));
    basicsPanel.setLayout(new GridLayout(4, 1));
    basicsPanel.setBorder(BorderFactory.createEtchedBorder());
    basicsPanel.setTitle("Global Variables");

    //customizing the basics: horizontal and vertical data inputs, maxlightness and minimum lightness
    horizontal.setPreferredSize(new Dimension(120, 43));
    horizontal.slider1.setValue(5);
    horizontal.slider1.setMaximum(15);
    horizontal.slider1.setMinimum(3);
    horizontal.slider1.setPreferredSize(new Dimension(70, 20));
    SpinnerNumberModel model1 = new SpinnerNumberModel(5, 3, 15, 1);
    horizontal.spinner1.setModel(model1);
    horizontal.spinner1.setPreferredSize(new Dimension(40, 20));
    horizontal.setTitle("Horizontal classes");
    horizontal.spinner1.addChangeListener(this);
    horizontal.slider1.addChangeListener(this);

    vertical.setPreferredSize(new Dimension(120, 43));
    vertical.slider1.setValue(5);
    vertical.slider1.setMaximum(15);
    vertical.slider1.setMinimum(3);
    vertical.slider1.setPreferredSize(new Dimension(70, 20));
    SpinnerNumberModel model2 = new SpinnerNumberModel(5, 3, 15, 1);
    vertical.spinner1.setModel(model2);
    vertical.spinner1.setPreferredSize(new Dimension(40, 20));
    vertical.setTitle("Vertical classes");
    vertical.spinner1.addChangeListener(this);
    vertical.slider1.addChangeListener(this);

    maxlightness.setPreferredSize(new Dimension(120, 43));
    maxlightness.slider1.setValue(95);
    maxlightness.slider1.setMaximum(100);
    maxlightness.slider1.setMinimum(50);
    maxlightness.slider1.setPreferredSize(new Dimension(60, 20));
    SpinnerNumberModel model3 = new SpinnerNumberModel(95, 50, 100, 1);
    maxlightness.spinner1.setModel(model3);
    maxlightness.spinner1.setPreferredSize(new Dimension(50, 20));
    maxlightness.setTitle("Max Lightness");
    maxlightness.spinner1.addChangeListener(this);
    maxlightness.slider1.addChangeListener(this);

    minlightness.setPreferredSize(new Dimension(120, 43));
    minlightness.slider1.setValue(50);
    minlightness.slider1.setMaximum(50);
    minlightness.slider1.setMinimum(0);
    minlightness.slider1.setPreferredSize(new Dimension(60, 20));
    SpinnerNumberModel model4 = new SpinnerNumberModel(50, 0, 50, 1);
    minlightness.spinner1.setModel(model4);
    minlightness.spinner1.setPreferredSize(new Dimension(50, 20));
    minlightness.setTitle("Min Lightness");
    minlightness.spinner1.addChangeListener(this);
    minlightness.slider1.addChangeListener(this);

    lightnessrange.setPreferredSize(new Dimension(120, 43));
    lightnessrange.slider1.setPreferredSize(new Dimension(70, 20));
    lightnessrange.spinner1.setPreferredSize(new Dimension(40, 20));
    lightnessrange.setTitle("Lightness Range");
    lightnessrange.slider1.setEnabled(false);
    lightnessrange.spinner1.setEnabled(false);
    lightnessrange.setToolTipText("Cannot set lightness range here");

    range.setPreferredSize(new Dimension(120, 43));
    range.slider1.setValue(100);
    range.slider1.setMaximum(200);
    range.slider1.setMinimum(100);
    range.slider1.setPreferredSize(new Dimension(65, 20));
    SpinnerNumberModel model8 = new SpinnerNumberModel(100, 100, 200, 1);
    range.spinner1.setModel(model8);
    range.spinner1.setPreferredSize(new Dimension(45, 20));
    range.setTitle("Span of the mesh");
    range.spinner1.addChangeListener(this);
    range.slider1.addChangeListener(this);

    basicsPanel.add(horizontal);
    basicsPanel.add(vertical);
    basicsPanel.add(maxlightness);
    basicsPanel.add(minlightness);

    //customizing the methodpanel, functionpanel, objectpanel and parameterpanel
    this.northpanel.add(this.methodPanel);
    this.methodPanel.setPreferredSize(new Dimension(340, 200));
    this.methodPanel.setLayout(new BorderLayout());
    this.methodPanel.setBorder(BorderFactory.createEtchedBorder());

    this.methodPanel.add(this.northeastpanel, BorderLayout.NORTH);
    this.northeastpanel.setPreferredSize(new Dimension(340, 125));
    this.northeastpanel.setLayout(new FlowLayout());

    this.northeastpanel.add(this.objectPanel);
    this.objectPanel.setPreferredSize(new Dimension(115, 120));
    this.objectPanel.setLayout(o1);
    this.objectPanel.setTitle("Geometric Object");

    this.northeastpanel.add(this.parameterPanel);
    this.parameterPanel.setPreferredSize(new Dimension(140, 120));
    this.parameterPanel.setLayout(p1);
    this.parameterPanel.setTitle("Parameters");

    this.northeastpanel.add(this.functionPanel);
    this.functionPanel.setPreferredSize(new Dimension(65, 120));
    this.functionPanel.setLayout(new GridLayout(3, 1));
    this.functionPanel.setTitle("Functions");

    this.methodPanel.add(this.deviationPanel, BorderLayout.SOUTH);
    this.deviationPanel.setPreferredSize(new Dimension(340, 70));
    this.deviationPanel.setLayout(g1);
    this.deviationPanel.setTitle("Deviations");



    //customizing the list of cards used in functionPanel, and their respective attachments
    //this.functionPanel.add(this.emptyfunction, "empty");

    //this.functionPanel.add(this.quaseqconefunction, "quaseqcone");
    //this.functionPanel.add(this.quaseqparabolafunction, "quaseqparabola");
    //this.functionPanel.add(this.quaseqhalfspherefunction, "quaseqhalfsphere");

    //adding three controls to the deviationpanel: deviation x, deviation y, and startinghue
    this.deviationPanel.add(this.xdeviation);
    this.deviationPanel.add(this.startinghue);
    this.deviationPanel.add(this.ydeviation);

    this.xdeviation.slider1.setPreferredSize(new Dimension(55, 20));
    this.xdeviation.spinner1.setPreferredSize(new Dimension(45, 20));
    this.xdeviation.slider1.setMaximum(50);
    this.xdeviation.slider1.setMinimum(-50);
    this.xdeviation.slider1.setValue(0);
    SpinnerNumberModel model5 = new SpinnerNumberModel(0, -50, 50, 1);
    this.xdeviation.spinner1.setModel(model5);
    this.xdeviation.setTitle("In a-b plane");
    this.xdeviation.spinner1.addChangeListener(this);
    this.xdeviation.slider1.addChangeListener(this);

    this.startinghue.slider1.setPreferredSize(new Dimension(55, 20));
    this.startinghue.spinner1.setPreferredSize(new Dimension(45, 20));
    this.startinghue.slider1.setMaximum(360);
    this.startinghue.slider1.setMinimum(0);
    this.startinghue.slider1.setValue(0);
    SpinnerNumberModel model7 = new SpinnerNumberModel(0, 0, 360, 1);
    this.startinghue.spinner1.setModel(model7);
    this.startinghue.setTitle("Starting Hue");
    this.startinghue.spinner1.addChangeListener(this);
    this.startinghue.slider1.addChangeListener(this);

    this.ydeviation.slider1.setPreferredSize(new Dimension(55, 20));
    this.ydeviation.spinner1.setPreferredSize(new Dimension(45, 20));
    this.ydeviation.slider1.setMaximum(50);
    this.ydeviation.slider1.setMinimum(-50);
    this.ydeviation.slider1.setValue(0);
    SpinnerNumberModel model6 = new SpinnerNumberModel(0, -50, 50, 1);
    this.ydeviation.spinner1.setModel(model6);
    this.ydeviation.setTitle("Along L axis");
    this.ydeviation.spinner1.addChangeListener(this);
    this.ydeviation.slider1.addChangeListener(this);

    //customizing the list of cards used in objectPanel, and their respective attachments (by default, show the empty state)
    this.objectPanel.add(this.emptyobject, "empty");

    o1.show(this.objectPanel, "empty");

    this.objectPanel.add(this.quaseqobject, "quaseq");
    this.quaseqobject.setPreferredSize(new Dimension(100, 160));
    this.quaseqobject.setLayout(new GridLayout(4, 1));
    this.quaseqobject.add(quaseqbellcurveButton);
    this.quaseqobject.add(quaseqconeButton);
    this.quaseqobject.add(quaseqhalfellipsoidButton);
    this.quaseqobject.add(quaseqparabolaButton);
    ButtonGroup quaseqbuttongroup = new ButtonGroup();
    quaseqbuttongroup.add(quaseqbellcurveButton);
    quaseqbuttongroup.add(quaseqconeButton);
    quaseqbuttongroup.add(quaseqhalfellipsoidButton);
    quaseqbuttongroup.add(quaseqparabolaButton);
    quaseqbellcurveButton.addActionListener(this);
    quaseqconeButton.addActionListener(this);
    quaseqhalfellipsoidButton.addActionListener(this);
    quaseqparabolaButton.addActionListener(this);

    this.objectPanel.add(this.divdivobject, "divdiv");
    this.divdivobject.setPreferredSize(new Dimension(100, 160));
    this.divdivobject.setLayout(new GridLayout(3, 1));
    this.divdivobject.add(this.divdivbellcurveButton);
    this.divdivobject.add(divdivhalfellipsoidButton);
    ButtonGroup divdivbuttongroup = new ButtonGroup();
    divdivbuttongroup.add(this.divdivbellcurveButton);
    divdivbuttongroup.add(this.divdivhalfellipsoidButton);
    this.divdivbellcurveButton.addActionListener(this);
    this.divdivhalfellipsoidButton.addActionListener(this);

    this.objectPanel.add(this.divseqobject, "divseq");
    this.divseqobject.setPreferredSize(new Dimension(100, 160));
    this.divseqobject.setLayout(new GridLayout(3, 1));
    //this.divseqobject.add(this.divseqellipseupButton);
    this.divseqobject.add(this.divseqellipsednButton);
    this.divseqobject.add(this.divseqgridButton);
    this.divseqobject.add(this.DivseqTrapezoidButton);
    ButtonGroup divseqbuttongroup = new ButtonGroup();
    divseqbuttongroup.add(divseqellipseupButton);
    divseqbuttongroup.add(divseqellipsednButton);
    divseqbuttongroup.add(divseqgridButton);
    divseqbuttongroup.add(DivseqTrapezoidButton);
    this.divseqellipsednButton.addActionListener(this);
    this.divseqgridButton.addActionListener(this);
    this.DivseqTrapezoidButton.addActionListener(this);

    this.objectPanel.add(this.seqseqgrayobject, "seqseqgray");
    this.seqseqgrayobject.setPreferredSize(new Dimension(100, 160));
    this.seqseqgrayobject.setLayout(new GridLayout(1, 1));
    this.seqseqgrayobject.add(this.seqseqgraydiamondButton);
    this.seqseqgraydiamondButton.addActionListener(this);

    this.objectPanel.add(this.seqseqnongrayobject, "seqseqnongray");
    this.seqseqnongrayobject.setPreferredSize(new Dimension(100, 160));
    this.seqseqnongrayobject.setLayout(new GridLayout(1, 1));
    this.seqseqnongrayobject.add(this.seqseqnongraydiamondButton);
    this.seqseqnongraydiamondButton.addActionListener(this);

    //customizing the list of cards used in parameterPanel, and their respective attachments
    this.parameterPanel.add(this.emptyparameter, "empty");

    p1.show(this.parameterPanel, "empty");

    this.parameterPanel.add(this.quaseqbellcurveparameter, "quaseqbellcurve");
    this.parameterPanel.add(this.quaseqconeparameter, "quaseqcone");
    this.parameterPanel.add(this.quaseqhalfellipsoidparameter, "quaseqhalfellipsoid");
    this.parameterPanel.add(this.quaseqparabolaparameter, "quaseqparabola");

    this.quaseqbellcurveparameter.setPreferredSize(new Dimension(180, 160));
    this.quaseqbellcurveparameter.setLayout(new GridLayout(2, 1));
    this.quaseqbellcurveparameter.setBorder(BorderFactory.createEtchedBorder());

    this.quaseqbellcurveparameter.add(this.quaseqbellcurvevertex);
    quaseqbellcurvevertex.setPreferredSize(new Dimension(120, 45));
    quaseqbellcurvevertex.slider1.setMaximum(500);
    quaseqbellcurvevertex.slider1.setMinimum(100);
    quaseqbellcurvevertex.slider1.setValue(150);
    this.quaseqbellcurvevertex.slider1.setPreferredSize(new Dimension(70, 20));
    this.quaseqbellcurvevertex.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelquaseqbellcurvevertex = new SpinnerNumberModel(150, 100, 500, 1);
    this.quaseqconeheight.spinner1.setModel(modelquaseqbellcurvevertex);
    quaseqbellcurvevertex.setTitle("Vertex of the curve");
    quaseqbellcurvevertex.spinner1.addChangeListener(this);
    quaseqbellcurvevertex.slider1.addChangeListener(this);

    this.quaseqbellcurveparameter.add(this.quaseqbellcurvedivisor);
    this.quaseqbellcurvedivisor.setPreferredSize(new Dimension(120, 45));
    this.quaseqbellcurvedivisor.slider1.setMaximum(10000);
    this.quaseqbellcurvedivisor.slider1.setMinimum(1000);
    this.quaseqbellcurvedivisor.slider1.setValue(5000);
    this.quaseqbellcurvedivisor.slider1.setPreferredSize(new Dimension(60, 20));
    this.quaseqbellcurvedivisor.spinner1.setPreferredSize(new Dimension(55, 20));
    SpinnerNumberModel modelquaseqbellcurvedivisor = new SpinnerNumberModel(5000, 1000, 10000, 1);
    this.quaseqbellcurvedivisor.spinner1.setModel(modelquaseqbellcurvedivisor);
    this.quaseqbellcurvedivisor.setTitle("divisor");
    this.quaseqbellcurvedivisor.spinner1.addChangeListener(this);
    this.quaseqbellcurvedivisor.slider1.addChangeListener(this);

    this.quaseqconeparameter.setPreferredSize(new Dimension(180, 160));
    this.quaseqconeparameter.setLayout(new GridLayout(2, 1));
    this.quaseqconeparameter.setBorder(BorderFactory.createEtchedBorder());

    this.quaseqconeparameter.add(this.quaseqconeheight);
    quaseqconeheight.setPreferredSize(new Dimension(120, 45));
    quaseqconeheight.slider1.setMaximum(500);
    quaseqconeheight.slider1.setMinimum(100);
    quaseqconeheight.slider1.setValue(150);
    this.quaseqconeheight.slider1.setPreferredSize(new Dimension(70, 20));
    this.quaseqconeheight.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelquaseqconeheight = new SpinnerNumberModel(150, 100, 500, 1);
    this.quaseqconeheight.spinner1.setModel(modelquaseqconeheight);
    quaseqconeheight.setTitle("Height of the cone");
    quaseqconeheight.spinner1.addChangeListener(this);
    quaseqconeheight.slider1.addChangeListener(this);

    this.quaseqconeparameter.add(this.quaseqconeradius);
    quaseqconeradius.setPreferredSize(new Dimension(120, 45));
    quaseqconeradius.slider1.setMaximum(300);
    quaseqconeradius.slider1.setMinimum(20);
    quaseqconeradius.slider1.setValue(120);
    this.quaseqconeradius.slider1.setPreferredSize(new Dimension(70, 20));
    this.quaseqconeradius.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelquaseqconeradius = new SpinnerNumberModel(120, 20, 300, 1);
    this.quaseqconeradius.spinner1.setModel(modelquaseqconeradius);
    quaseqconeradius.setTitle("Radius of the cone");
    quaseqconeradius.spinner1.addChangeListener(this);
    quaseqconeradius.slider1.addChangeListener(this);

    this.quaseqhalfellipsoidparameter.setPreferredSize(new Dimension(180, 160));
    this.quaseqhalfellipsoidparameter.setLayout(new GridLayout(2, 1));
    this.quaseqhalfellipsoidparameter.setBorder(BorderFactory.createEtchedBorder());

    this.quaseqhalfellipsoidparameter.add(this.quaseqhalfellipsoidradiusab);
    quaseqhalfellipsoidradiusab.setPreferredSize(new Dimension(120, 45));
    quaseqhalfellipsoidradiusab.slider1.setMaximum(200);
    quaseqhalfellipsoidradiusab.slider1.setMinimum(100);
    quaseqhalfellipsoidradiusab.slider1.setValue(120);
    this.quaseqhalfellipsoidradiusab.slider1.setPreferredSize(new Dimension(70, 20));
    this.quaseqhalfellipsoidradiusab.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelquaseqhalfellipsoidradiusab = new SpinnerNumberModel(120, 100, 200, 1);
    this.quaseqhalfellipsoidradiusab.spinner1.setModel(modelquaseqhalfellipsoidradiusab);
    quaseqhalfellipsoidradiusab.setTitle("Ellipsoid radius a, b");
    quaseqhalfellipsoidradiusab.spinner1.addChangeListener(this);
    quaseqhalfellipsoidradiusab.slider1.addChangeListener(this);

    this.quaseqhalfellipsoidparameter.add(this.quaseqhalfellipsoidradiusc);
    quaseqhalfellipsoidradiusc.setPreferredSize(new Dimension(120, 45));
    quaseqhalfellipsoidradiusc.slider1.setMaximum(200);
    quaseqhalfellipsoidradiusc.slider1.setMinimum(100);
    quaseqhalfellipsoidradiusc.slider1.setValue(120);
    this.quaseqhalfellipsoidradiusc.slider1.setPreferredSize(new Dimension(70, 20));
    this.quaseqhalfellipsoidradiusc.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelquaseqhalfellipsoidradiusc = new SpinnerNumberModel(120, 100, 200, 1);
    this.quaseqhalfellipsoidradiusc.spinner1.setModel(modelquaseqhalfellipsoidradiusc);
    quaseqhalfellipsoidradiusc.setTitle("Ellipsoid radius c");
    quaseqhalfellipsoidradiusc.spinner1.addChangeListener(this);
    quaseqhalfellipsoidradiusc.slider1.addChangeListener(this);

    this.quaseqparabolaparameter.setPreferredSize(new Dimension(180, 160));
    this.quaseqparabolaparameter.setLayout(new GridLayout(2, 1));
    this.quaseqparabolaparameter.setBorder(BorderFactory.createEtchedBorder());

    this.quaseqparabolaparameter.add(this.quaseqparabolaconstant);
    quaseqparabolaconstant.setPreferredSize(new Dimension(120, 45));
    quaseqparabolaconstant.slider1.setMaximum(9000);
    quaseqparabolaconstant.slider1.setMinimum(3000);
    quaseqparabolaconstant.slider1.setValue(5000);
    this.quaseqparabolaconstant.slider1.setPreferredSize(new Dimension(60, 20));
    this.quaseqparabolaconstant.spinner1.setPreferredSize(new Dimension(55, 20));
    SpinnerNumberModel modelquaseqparabolaconstant = new SpinnerNumberModel(5000, 3000, 9000, 1);
    this.quaseqparabolaconstant.spinner1.setModel(modelquaseqparabolaconstant);
    quaseqparabolaconstant.setTitle("Constant");
    quaseqparabolaconstant.spinner1.addChangeListener(this);
    quaseqparabolaconstant.slider1.addChangeListener(this);

    this.parameterPanel.add(this.divdivbellcurveparameter, "divdivbellcurve");
    this.divdivbellcurveparameter.setPreferredSize(new Dimension(180, 160));
    this.divdivbellcurveparameter.setLayout(new GridLayout(2, 1));
    this.divdivbellcurveparameter.setBorder(BorderFactory.createEtchedBorder());

    this.divdivbellcurveparameter.add(this.divdivbellcurvevertex);
    this.divdivbellcurvevertex.setPreferredSize(new Dimension(120, 45));
    this.divdivbellcurvevertex.slider1.setMaximum(120);
    this.divdivbellcurvevertex.slider1.setMinimum(80);
    this.divdivbellcurvevertex.slider1.setValue(100);
    this.divdivbellcurvevertex.slider1.setPreferredSize(new Dimension(60, 20));
    this.divdivbellcurvevertex.spinner1.setPreferredSize(new Dimension(55, 20));
    SpinnerNumberModel modeldivdivbellcurvevertex = new SpinnerNumberModel(100, 80, 120, 1);
    this.divdivbellcurvevertex.spinner1.setModel(modeldivdivbellcurvevertex);
    this.divdivbellcurvevertex.setTitle("Vertex of the curve");
    this.divdivbellcurvevertex.spinner1.addChangeListener(this);
    this.divdivbellcurvevertex.slider1.addChangeListener(this);

    this.divdivbellcurveparameter.add(this.divdivbellcurvedivisor);
    this.divdivbellcurvedivisor.setPreferredSize(new Dimension(120, 45));
    this.divdivbellcurvedivisor.slider1.setMaximum(10000);
    this.divdivbellcurvedivisor.slider1.setMinimum(1000);
    this.divdivbellcurvedivisor.slider1.setValue(6500);
    this.divdivbellcurvedivisor.slider1.setPreferredSize(new Dimension(60, 20));
    this.divdivbellcurvedivisor.spinner1.setPreferredSize(new Dimension(55, 20));
    SpinnerNumberModel modeldivdivbellcurvedivisor = new SpinnerNumberModel(6500, 1000, 10000, 1);
    this.divdivbellcurvedivisor.spinner1.setModel(modeldivdivbellcurvedivisor);
    this.divdivbellcurvedivisor.setTitle("divisor");
    this.divdivbellcurvedivisor.spinner1.addChangeListener(this);
    this.divdivbellcurvedivisor.slider1.addChangeListener(this);

    this.parameterPanel.add(this.divdivconeparameter, "divdivcone");
    this.divdivconeparameter.setPreferredSize(new Dimension(180, 160));
    this.divdivconeparameter.setLayout(new GridLayout(2, 1));
    this.divdivconeparameter.setBorder(BorderFactory.createEtchedBorder());

    this.divdivconeparameter.add(this.divdivconeheight);
    this.divdivconeheight.setPreferredSize(new Dimension(120, 45));
    this.divdivconeheight.slider1.setMaximum(300);
    this.divdivconeheight.slider1.setMinimum(0);
    this.divdivconeheight.slider1.setValue(150);
    this.divdivconeheight.setTitle("Height of the cone");
    this.divdivconeheight.spinner1.addChangeListener(this);
    this.divdivconeheight.slider1.addChangeListener(this);

    this.divdivconeparameter.add(this.divdivconeradius);
    this.divdivconeradius.setPreferredSize(new Dimension(120, 45));
    this.divdivconeradius.slider1.setMaximum(300);
    this.divdivconeradius.slider1.setMinimum(0);
    this.divdivconeradius.slider1.setValue(120);
    this.divdivconeradius.setTitle("Radius of the cone");
    this.divdivconeradius.spinner1.addChangeListener(this);
    this.divdivconeradius.slider1.addChangeListener(this);

    this.parameterPanel.add(this.divdivhalfellipsoidparameter, "divdivhalfellipsoid");
    this.divdivhalfellipsoidparameter.setPreferredSize(new Dimension(180, 160));
    this.divdivhalfellipsoidparameter.setLayout(new GridLayout(2, 1));
    this.divdivhalfellipsoidparameter.setBorder(BorderFactory.createEtchedBorder());

    this.divdivhalfellipsoidparameter.add(this.divdivhalfellipsoidradiusab);
    this.divdivhalfellipsoidradiusab.setPreferredSize(new Dimension(120, 45));
    this.divdivhalfellipsoidradiusab.slider1.setMaximum(200);
    this.divdivhalfellipsoidradiusab.slider1.setMinimum(100);
    this.divdivhalfellipsoidradiusab.slider1.setValue(120);
    this.divdivhalfellipsoidradiusab.slider1.setPreferredSize(new Dimension(70, 20));
    this.divdivhalfellipsoidradiusab.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modeldivdivhalfellipsoidab = new SpinnerNumberModel(120, 100, 200, 1);
    this.divdivhalfellipsoidradiusab.spinner1.setModel(modeldivdivhalfellipsoidab);
    this.divdivhalfellipsoidradiusab.setTitle("Ellipsoid radius a&b");
    this.divdivhalfellipsoidradiusab.spinner1.addChangeListener(this);
    this.divdivhalfellipsoidradiusab.slider1.addChangeListener(this);

    this.divdivhalfellipsoidparameter.add(this.divdivhalfellipsoidradiusc);
    this.divdivhalfellipsoidradiusc.setPreferredSize(new Dimension(120, 45));
    this.divdivhalfellipsoidradiusc.slider1.setMaximum(120);
    this.divdivhalfellipsoidradiusc.slider1.setMinimum(60);
    this.divdivhalfellipsoidradiusc.slider1.setValue(100);
    this.divdivhalfellipsoidradiusc.slider1.setPreferredSize(new Dimension(70, 20));
    this.divdivhalfellipsoidradiusc.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modeldivdivhalfellipsoidradiusc = new SpinnerNumberModel(100, 60, 120, 1);
    this.divdivhalfellipsoidradiusc.spinner1.setModel(modeldivdivhalfellipsoidradiusc);
    this.divdivhalfellipsoidradiusc.setTitle("Ellipsoid radius c");
    this.divdivhalfellipsoidradiusc.spinner1.addChangeListener(this);
    this.divdivhalfellipsoidradiusc.slider1.addChangeListener(this);

    this.parameterPanel.add(this.divseqellipseupparameter, "divseqellipseup");
    this.divseqellipseupparameter.setPreferredSize(new Dimension(180, 160));
    this.divseqellipseupparameter.setLayout(new GridLayout(2, 1));
    this.divseqellipseupparameter.setBorder(BorderFactory.createEtchedBorder());

    this.divseqellipseupparameter.add(this.divseqellipseupalpha);
    this.divseqellipseupalpha.setPreferredSize(new Dimension(120, 45));
    this.divseqellipseupalpha.slider1.setMaximum(120);
    this.divseqellipseupalpha.slider1.setMinimum(45);
    this.divseqellipseupalpha.slider1.setValue(60);
    this.divseqellipseupalpha.slider1.setPreferredSize(new Dimension(70, 20));
    this.divseqellipseupalpha.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modeldivseqellipseupalpha = new SpinnerNumberModel(60, 45, 120, 1);
    this.divseqellipseupalpha.spinner1.setModel(modeldivseqellipseupalpha);
    this.divseqellipseupalpha.setTitle("Fanup top angle");
    this.divseqellipseupalpha.spinner1.addChangeListener(this);
    this.divseqellipseupalpha.slider1.addChangeListener(this);

    this.divseqellipseupparameter.add(this.divseqellipseupe);
    this.divseqellipseupe.setPreferredSize(new Dimension(120, 45));
    this.divseqellipseupe.slider1.setMaximum(99);
    this.divseqellipseupe.slider1.setMinimum(0);
    this.divseqellipseupe.slider1.setValue(0);
    this.divseqellipseupe.slider1.setPreferredSize(new Dimension(75, 20));
    this.divseqellipseupe.spinner1.setPreferredSize(new Dimension(40, 20));
    SpinnerNumberModel modeldivseqellipseupe = new SpinnerNumberModel(0, 0, 99, 1);
    this.divseqellipseupe.spinner1.setModel(modeldivseqellipseupe);
    this.divseqellipseupe.setTitle("Ellipse shape index");
    this.divseqellipseupe.spinner1.addChangeListener(this);
    this.divseqellipseupe.slider1.addChangeListener(this);

    this.parameterPanel.add(this.divseqellipsedownparameter, "divseqellipsedown");
    this.divseqellipsedownparameter.setPreferredSize(new Dimension(180, 160));
    this.divseqellipsedownparameter.setLayout(new GridLayout(2, 1));
    this.divseqellipsedownparameter.setBorder(BorderFactory.createEtchedBorder());

    this.divseqellipsedownparameter.add(this.divseqellipsedownalpha);
    this.divseqellipsedownalpha.setPreferredSize(new Dimension(120, 45));
    this.divseqellipsedownalpha.slider1.setMaximum(120);
    this.divseqellipsedownalpha.slider1.setMinimum(45);
    this.divseqellipsedownalpha.slider1.setValue(60);
    this.divseqellipsedownalpha.slider1.setPreferredSize(new Dimension(70, 20));
    this.divseqellipsedownalpha.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modeldivseqellipsedownalpha = new SpinnerNumberModel(60, 45, 120, 1);
    this.divseqellipsedownalpha.spinner1.setModel(modeldivseqellipsedownalpha);
    this.divseqellipsedownalpha.setTitle("Fandown top angle");
    this.divseqellipsedownalpha.spinner1.addChangeListener(this);
    this.divseqellipsedownalpha.slider1.addChangeListener(this);

    this.divseqellipsedownparameter.add(this.divseqellipsedowne);
    this.divseqellipsedowne.setPreferredSize(new Dimension(120, 45));
    this.divseqellipsedowne.slider1.setMaximum(99);
    this.divseqellipsedowne.slider1.setMinimum(0);
    this.divseqellipsedowne.slider1.setValue(0);
    this.divseqellipsedowne.slider1.setPreferredSize(new Dimension(75, 20));
    this.divseqellipsedowne.spinner1.setPreferredSize(new Dimension(40, 20));
    SpinnerNumberModel modeldivseqellipsedowne = new SpinnerNumberModel(0, 0, 99, 1);
    this.divseqellipsedowne.spinner1.setModel(modeldivseqellipsedowne);
    this.divseqellipsedowne.setTitle("Ellipse shape index");
    this.divseqellipsedowne.spinner1.addChangeListener(this);
    this.divseqellipsedowne.slider1.addChangeListener(this);

    this.parameterPanel.add(this.divseqgridparameter, "divseqgrid");
    this.divseqgridparameter.setPreferredSize(new Dimension(180, 160));
    this.divseqgridparameter.setLayout(new GridLayout(2, 1));
    this.divseqgridparameter.setBorder(BorderFactory.createEtchedBorder());

    this.divseqgridparameter.add(this.divseqgridalpha);
    this.divseqgridalpha.setPreferredSize(new Dimension(120, 45));
    this.divseqgridalpha.slider1.setMaximum(180);
    this.divseqgridalpha.slider1.setMinimum(90);
    this.divseqgridalpha.slider1.setValue(150);
    this.divseqgridalpha.slider1.setPreferredSize(new Dimension(70, 20));
    this.divseqgridalpha.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modeldivseqgridalpha = new SpinnerNumberModel(150, 90, 180, 1);
    this.divseqgridalpha.spinner1.setModel(modeldivseqgridalpha);
    this.divseqgridalpha.setTitle("Top angle");
    this.divseqgridalpha.spinner1.addChangeListener(this);
    this.divseqgridalpha.slider1.addChangeListener(this);

    this.divseqgridparameter.add(this.divseqgridrange);
    this.divseqgridrange.setPreferredSize(new Dimension(120, 45));
    this.divseqgridrange.slider1.setMaximum(160);
    this.divseqgridrange.slider1.setMinimum(90);
    this.divseqgridrange.slider1.setValue(120);
    this.divseqgridrange.slider1.setPreferredSize(new Dimension(70, 20));
    this.divseqgridrange.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modeldivseqgridradius = new SpinnerNumberModel(120, 90, 160, 1);
    this.divseqgridrange.spinner1.setModel(modeldivseqgridradius);
    this.divseqgridrange.setTitle("Saturation range");
    this.divseqgridrange.spinner1.addChangeListener(this);
    this.divseqgridrange.slider1.addChangeListener(this);

    this.parameterPanel.add(this.DivseqTrapezoidparameter, "DivseqTrapezoid");
    this.DivseqTrapezoidparameter.setPreferredSize(new Dimension(180, 160));
    this.DivseqTrapezoidparameter.setLayout(new GridLayout(2, 1));
    this.DivseqTrapezoidparameter.setBorder(BorderFactory.createEtchedBorder());

    this.DivseqTrapezoidparameter.add(this.DivseqTrapezoidalpha);
    this.DivseqTrapezoidalpha.setPreferredSize(new Dimension(120, 45));
    this.DivseqTrapezoidalpha.slider1.setMaximum(120);
    this.DivseqTrapezoidalpha.slider1.setMinimum(45);
    this.DivseqTrapezoidalpha.slider1.setValue(60);
    this.DivseqTrapezoidalpha.slider1.setPreferredSize(new Dimension(70, 20));
    this.DivseqTrapezoidalpha.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelDivseqTrapezoidalpha = new SpinnerNumberModel(60, 45, 120, 1);
    this.DivseqTrapezoidalpha.spinner1.setModel(modelDivseqTrapezoidalpha);
    this.DivseqTrapezoidalpha.setTitle("Trapezoid top angle");
    this.DivseqTrapezoidalpha.spinner1.addChangeListener(this);
    this.DivseqTrapezoidalpha.slider1.addChangeListener(this);

    this.DivseqTrapezoidparameter.add(this.DivseqTrapezoidradius);
    this.DivseqTrapezoidradius.setPreferredSize(new Dimension(120, 45));
    this.DivseqTrapezoidradius.slider1.setMaximum(150);
    this.DivseqTrapezoidradius.slider1.setMinimum(50);
    this.DivseqTrapezoidradius.slider1.setValue(100);
    this.DivseqTrapezoidradius.slider1.setPreferredSize(new Dimension(70, 20));
    this.DivseqTrapezoidradius.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelDivseqTrapezoidradius = new SpinnerNumberModel(100, 50, 150, 1);
    this.DivseqTrapezoidradius.spinner1.setModel(modelDivseqTrapezoidradius);
    this.DivseqTrapezoidradius.setTitle("Trapezoid radius");
    this.DivseqTrapezoidradius.spinner1.addChangeListener(this);
    this.DivseqTrapezoidradius.slider1.addChangeListener(this);

    this.parameterPanel.add(this.seqseqgraydiamondparameter, "seqseqgraydiamond");
    this.seqseqgraydiamondparameter.setPreferredSize(new Dimension(180, 160));
    this.seqseqgraydiamondparameter.setLayout(new GridLayout(2, 1));
    this.seqseqgraydiamondparameter.setBorder(BorderFactory.createEtchedBorder());

    this.seqseqgraydiamondparameter.add(this.seqseqgraydiamondalpha);
    seqseqgraydiamondalpha.setPreferredSize(new Dimension(120, 45));
    seqseqgraydiamondalpha.slider1.setMaximum(150);
    seqseqgraydiamondalpha.slider1.setMinimum(60);
    seqseqgraydiamondalpha.slider1.setValue(120);
    this.seqseqgraydiamondalpha.slider1.setPreferredSize(new Dimension(70, 20));
    this.seqseqgraydiamondalpha.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelseqseqgraydiamondalpha = new SpinnerNumberModel(120, 60, 150, 1);
    this.seqseqgraydiamondalpha.spinner1.setModel(modelseqseqgraydiamondalpha);
    seqseqgraydiamondalpha.setTitle("Diamond top angle");
    seqseqgraydiamondalpha.spinner1.addChangeListener(this);
    seqseqgraydiamondalpha.slider1.addChangeListener(this);

    this.parameterPanel.add(this.seqseqnongraydiamondparameter, "seqseqnongraydiamond");
    this.seqseqnongraydiamondparameter.setPreferredSize(new Dimension(180, 160));
    this.seqseqnongraydiamondparameter.setLayout(new GridLayout(2, 1));
    this.seqseqnongraydiamondparameter.setBorder(BorderFactory.createEtchedBorder());

    this.seqseqnongraydiamondparameter.add(this.seqseqnongraydiamondalpha);
    this.seqseqnongraydiamondalpha.setPreferredSize(new Dimension(120, 45));
    this.seqseqnongraydiamondalpha.slider1.setMaximum(150);
    this.seqseqnongraydiamondalpha.slider1.setMinimum(60);
    this.seqseqnongraydiamondalpha.slider1.setValue(120);
    this.seqseqnongraydiamondalpha.slider1.setPreferredSize(new Dimension(70, 20));
    this.seqseqnongraydiamondalpha.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelseqseqnongraydiamondalpha = new SpinnerNumberModel(120, 60, 150, 1);
    this.seqseqnongraydiamondalpha.spinner1.setModel(modelseqseqnongraydiamondalpha);
    this.seqseqnongraydiamondalpha.setTitle("Diamond top angle");
    this.seqseqnongraydiamondalpha.spinner1.addChangeListener(this);
    this.seqseqnongraydiamondalpha.slider1.addChangeListener(this);

    this.seqseqnongraydiamondparameter.add(this.seqseqnongraydiamondbeta);
    this.seqseqnongraydiamondbeta.setPreferredSize(new Dimension(120, 45));
    this.seqseqnongraydiamondbeta.slider1.setMaximum(45);
    this.seqseqnongraydiamondbeta.slider1.setMinimum(0);
    this.seqseqnongraydiamondbeta.slider1.setValue(15);
    this.seqseqnongraydiamondbeta.slider1.setPreferredSize(new Dimension(70, 20));
    this.seqseqnongraydiamondbeta.spinner1.setPreferredSize(new Dimension(45, 20));
    SpinnerNumberModel modelseqseqnongraydiamondbeta = new SpinnerNumberModel(15, 0, 45, 1);
    this.seqseqnongraydiamondbeta.spinner1.setModel(modelseqseqnongraydiamondbeta);
    this.seqseqnongraydiamondbeta.setTitle("Diamond tilt angle");
    this.seqseqnongraydiamondbeta.spinner1.addChangeListener(this);
    this.seqseqnongraydiamondbeta.slider1.addChangeListener(this);

    //customizing the functionpanel
    this.functionPanel.setLayout(new GridLayout(3, 1));
    this.functionPanel.add(this.openButton);
    this.openButton.setToolTipText("Load a color scheme");
    this.openButton.addActionListener(this);
    this.functionPanel.add(this.saveButton);
    this.saveButton.setToolTipText("Save current color scheme");
    this.saveButton.addActionListener(this);
    this.functionPanel.add(this.exampleButton);
    this.exampleButton.setToolTipText("Load recommended color schemes");
    this.exampleButton.addActionListener(this);

    //customizing the southpanel
    southpanel.setPreferredSize(new Dimension(600, 230));
    southpanel.setLayout(new BorderLayout());
    southpanel.setBorder(BorderFactory.createEtchedBorder());

    //customizing the checkboxpanel, adding checkboxes
    checkboxpanel.setPreferredSize(new Dimension(600, 30));
    checkboxpanel.setLayout(new GridLayout(1, 3));
    ButtonGroup checkboxgroup = new ButtonGroup();

    for(int i = 0; i < 3; i ++){
      //initialize the checkboxes
      checkbox[i] = new JCheckBox();
    }

    checkboxpanel.add(checkbox[0]);
    checkboxpanel.add(checkbox[1]);
    checkboxpanel.add(checkbox[2]);
    checkboxgroup.add(checkbox[0]);
    checkboxgroup.add(checkbox[1]);
    checkboxgroup.add(checkbox[2]);
    //set the first checkbox the default one
    checkbox[0].setSelected(true);

    //customizing the samplepanel
    samplepanel.setPreferredSize(new Dimension(600, 200));
    samplepanel.setLayout(new GridLayout(1, 3));
    samplepanel.setBorder(BorderFactory.createEtchedBorder());

    for(int i = 0; i < 3; i ++){
      //initialize the samples
      samples[i] = new JPanel();
      //customizing the samples
      samples[i].setSize(new Dimension(200, 200));
      //customizing the borders of the samples
      samples[i].setBorder(BorderFactory.createEtchedBorder());
      samples[i].setBorder(BorderFactory.createLineBorder(Color.white));

      samples[i].setBackground(new Color(255, 255, 255));

      //add samples[i] to the southpanel
      samplepanel.add(samples[i]);

      //
      samples[i].addMouseListener(this);
    }



    //add checkboxpanel and samplepanel to southpanel
    southpanel.add(checkboxpanel, BorderLayout.NORTH);
    southpanel.add(samplepanel, BorderLayout.SOUTH);

    //add southpanel
    this.add(southpanel, BorderLayout.SOUTH);

  }

  public void actionPerformed(ActionEvent e) {

    //#####################################################################################################

    //if event fired by the type buttons

    //#####################################################################################################

    if(e.getSource() == this.quaseqButton){

      if(this.quaseqButton.isSelected() == true){

        //reload the Global variable panel
        this.basicsPanel.removeAll();
        this.basicsPanel.add(this.horizontal);
        this.basicsPanel.add(this.vertical);
        this.basicsPanel.add(this.maxlightness);
        this.basicsPanel.add(this.minlightness);

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(95);
        SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(50);
        SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(0);
        SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.getcurrentcheckbox();
        this.samples[this.currentcheckbox].removeAll();

        //show the geometry objects & a clear parameter panel
        o1.show(this.objectPanel, "quaseq");
        p1.show(this.parameterPanel, "empty");

        if(this.quaseqbellcurveButton.isSelected() == true){
          p1.show(this.parameterPanel, "quaseqbellcurve");
          this.displayquaseqbellcurve();
          this.setGlobalValues(this.quaseqbellcurve1);
        }

        if(this.quaseqconeButton.isSelected() == true){
          p1.show(this.parameterPanel, "quaseqcone");
          this.displayquaseqcone();
          this.setGlobalValues(this.quaseqcone1);
        }

        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          p1.show(this.parameterPanel, "quaseqhalfellipsoid");
          this.displayquaseqhalfellipsoid();
          this.setGlobalValues(this.quaseqhalfellipsoid1);
        }

        if(this.quaseqparabolaButton.isSelected() == true){
          p1.show(this.parameterPanel, "quaseqparabola");
          this.displayquaseqparabola();
          this.setGlobalValues(this.quaseqparabola1);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divdivButton){

      if(this.divdivButton.isSelected() == true){

        //reload the Global variable panel
        this.basicsPanel.removeAll();
        this.basicsPanel.add(this.horizontal);
        this.basicsPanel.add(this.vertical);
        this.basicsPanel.add(this.range);
        this.basicsPanel.add(this.lightnessrange);

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(95);
        SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(50);
        SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(0);
        SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.getcurrentcheckbox();
        this.samples[this.currentcheckbox].removeAll();

        //show the geometry objects & a clear parameter panel
        o1.show(this.objectPanel, "divdiv");
        p1.show(this.parameterPanel, "empty");

        if(this.divdivbellcurveButton.isSelected() == true){
          p1.show(this.parameterPanel, "divdivbellcurve");
          this.displaydivdivbellcurve();
          this.setGlobalValues(this.divdivbellcurve1);
        }

        if(this.divdivhalfellipsoidButton.isSelected() == true){
          p1.show(this.parameterPanel, "divdivhalfellipsoid");
          this.displaydivdivhalfellipsoid();
          this.setGlobalValues(this.divdivhalfellipsoid1);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divseqButton){
      if(this.divseqButton.isSelected() == true){

        //reload the Global variable panel
        this.basicsPanel.removeAll();
        this.basicsPanel.add(this.horizontal);
        this.basicsPanel.add(this.vertical);
        this.basicsPanel.add(this.maxlightness);
        this.basicsPanel.add(this.minlightness);

        if(this.divseqellipseupButton.isSelected() == true){
          this.maxlightness.slider1.setMinimum(-80);
          this.maxlightness.slider1.setMaximum(0);
          this.maxlightness.slider1.setValue(this.divseqellipseup1.maxlightness);
          SpinnerNumberModel model4 = new SpinnerNumberModel(this.divseqellipseup1.maxlightness, -80, 0, 1);
          this.maxlightness.spinner1.setModel(model4);

          this.minlightness.slider1.setMinimum(-150);
          this.minlightness.slider1.setMaximum(-80);
          this.minlightness.slider1.setValue(this.divseqellipseup1.minlightness);
          SpinnerNumberModel model5 = new SpinnerNumberModel(this.divseqellipseup1.minlightness, -150, -80, 1);
          this.minlightness.spinner1.setModel(model5);

          this.ydeviation.slider1.setMinimum(70);
          this.ydeviation.slider1.setMaximum(150);
          this.ydeviation.slider1.setValue(this.divseqellipseup1.dy);
          SpinnerNumberModel model6 = new SpinnerNumberModel(this.divseqellipseup1.dy, 70, 150, 1);
          this.ydeviation.spinner1.setModel(model6);
        }

        if(this.divseqellipsednButton.isSelected() == true || this.DivseqTrapezoidButton.isSelected() == true || this.divseqgridButton.isSelected() == true){
          this.maxlightness.slider1.setMinimum(50);
          this.maxlightness.slider1.setMaximum(100);
          this.maxlightness.slider1.setValue(95);
          SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
          this.maxlightness.spinner1.setModel(model1);

          this.minlightness.slider1.setMinimum(0);
          this.minlightness.slider1.setMaximum(50);
          this.minlightness.slider1.setValue(50);
          SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
          this.minlightness.spinner1.setModel(model2);

          this.ydeviation.slider1.setMinimum(-50);
          this.ydeviation.slider1.setMaximum(50);
          this.ydeviation.slider1.setValue(0);
          SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
          this.ydeviation.spinner1.setModel(model3);
        }

        this.getcurrentcheckbox();
        this.samples[this.currentcheckbox].removeAll();

        o1.show(this.objectPanel, "divseq");
        p1.show(this.parameterPanel, "empty");

        if(this.divseqellipseupButton.isSelected() == true){
          p1.show(this.parameterPanel, "divseqellipseup");
          this.divseqellipseup1 = new Divseqellipseup(5, 5, -50, -80, 60, 0, 0, 100, 0);
          this.displaydivseqellipseup();
          this.setGlobalValues(this.divseqellipseup1);
        }

        if(this.divseqellipsednButton.isSelected() == true){
          p1.show(this.parameterPanel, "divseqellipsedown");
          this.displaydivseqellipsedn();
          this.setGlobalValues(this.divseqellipsedn1);
        }

        if(this.divseqgridButton.isSelected() == true){
          p1.show(this.parameterPanel, "divseqgrid");
          this.displaydivseqgrid();
          this.setGlobalValues(this.divseqgrid1);
        }

        if(this.DivseqTrapezoidButton.isSelected() == true){
          p1.show(this.parameterPanel, "DivseqTrapezoid");
          this.displayDivseqTrapezoid();
          this.setGlobalValues(this.DivseqTrapezoid1);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.seqseqgrayButton){
      if(this.seqseqgrayButton.isSelected() == true){

        //reload the Global variable panel
        this.basicsPanel.removeAll();
        this.basicsPanel.add(this.horizontal);
        this.basicsPanel.add(this.vertical);
        this.basicsPanel.add(this.maxlightness);
        this.basicsPanel.add(this.minlightness);

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(95);
        SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(50);
        SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(0);
        SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.getcurrentcheckbox();
        this.samples[this.currentcheckbox].removeAll();

        o1.show(this.objectPanel, "seqseqgray");
        p1.show(this.parameterPanel, "empty");

        if(this.seqseqgraydiamondButton.isSelected() == true){
          p1.show(this.parameterPanel, "seqseqgraydiamond");
          this.displayseqseqgray();
          this.setGlobalValues(this.seqseqgraydiamond1);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.seqseqnongrayButton){
      if(this.seqseqnongrayButton.isSelected() == true){

        //reload the Global variable panel
        this.basicsPanel.removeAll();
        this.basicsPanel.add(this.horizontal);
        this.basicsPanel.add(this.vertical);
        this.basicsPanel.add(this.maxlightness);
        this.basicsPanel.add(this.minlightness);

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(95);
        SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(50);
        SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(0);
        SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.getcurrentcheckbox();
        this.samples[this.currentcheckbox].removeAll();

        o1.show(this.objectPanel, "seqseqnongray");
        p1.show(this.parameterPanel, "empty");

        if(this.seqseqnongraydiamondButton.isSelected() == true){
          o1.show(this.objectPanel, "seqseqnongraydiamond");
          this.displayseqseqnongray();
          this.setGlobalValues(this.seqseqnongraydiamond1);
        }
        this.repaint();
      }
    }


    //#####################################################################################################

    //if event fired by the object buttons

    //#####################################################################################################

    if(e.getSource() == this.quaseqbellcurveButton){
      if(this.quaseqbellcurveButton.isSelected() == true){

        p1.show(this.parameterPanel, "quaseqbellcurve");

        this.displayquaseqbellcurve();
        this.setGlobalValues(this.quaseqbellcurve1);

        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.quaseqconeButton){
      if(this.quaseqconeButton.isSelected() == true){

        p1.show(this.parameterPanel, "quaseqcone");

        this.displayquaseqcone();
        this.setGlobalValues(this.quaseqcone1);
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.quaseqparabolaButton){
      if(this.quaseqparabolaButton.isSelected() == true){

        p1.show(this.parameterPanel, "quaseqparabola");

        this.setGlobalValues(this.quaseqparabola1);
        this.displayquaseqparabola();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.quaseqhalfellipsoidButton){
      if(this.quaseqhalfellipsoidButton.isSelected() == true){

        p1.show(this.parameterPanel, "quaseqhalfellipsoid");

        this.setGlobalValues(this.quaseqhalfellipsoid1);
        this.displayquaseqhalfellipsoid();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divdivbellcurveButton){
      if(this.divdivbellcurveButton.isSelected() == true){

        p1.show(this.parameterPanel, "divdivbellcurve");

        this.setGlobalValues(this.divdivbellcurve1);
        this.displaydivdivbellcurve();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divdivhalfellipsoidButton){
      if(this.divdivhalfellipsoidButton.isSelected() == true){

        p1.show(this.parameterPanel, "divdivhalfellipsoid");

        this.setGlobalValues(this.divdivhalfellipsoid1);
        this.displaydivdivhalfellipsoid();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divseqellipseupButton){
      if(this.divseqellipseupButton.isSelected() == true){

        p1.show(this.parameterPanel, "divseqellipseup");
        logger.finest("this.divseqellipseup1.maxlightness = " + this.divseqellipseup1.maxlightness);
        this.maxlightness.slider1.setMinimum(-80);
        logger.finest("this.divseqellipseup1.maxlightness = " + this.divseqellipseup1.maxlightness);
        this.maxlightness.slider1.setMaximum(0);
        logger.finest("this.divseqellipseup1.maxlightness = " + this.divseqellipseup1.maxlightness);
        this.maxlightness.slider1.setValue(this.divseqellipseup1.maxlightness);
        SpinnerNumberModel model1 = new SpinnerNumberModel(this.divseqellipseup1.maxlightness, -80, 0, 1);
        this.maxlightness.spinner1.setModel(model1);

        logger.finest("this.divseqellipseup1.minlightness = " + this.divseqellipseup1.minlightness);
        this.minlightness.slider1.setMinimum(-150);
        this.minlightness.slider1.setMaximum(-80);
        logger.finest("this.divseqellipseup1.minlightness = " + this.divseqellipseup1.minlightness);
        this.minlightness.slider1.setValue(this.divseqellipseup1.minlightness);
        SpinnerNumberModel model2 = new SpinnerNumberModel(this.divseqellipseup1.minlightness, -150, 0, 1);
        this.minlightness.spinner1.setModel(model2);

        logger.finest("this.divseqellipseup1.dy = " + this.divseqellipseup1.dy);
        this.ydeviation.slider1.setMinimum(70);
        logger.finest("this.divseqellipseup1.dy = " + this.divseqellipseup1.dy);
        this.ydeviation.slider1.setMaximum(150);
        logger.finest("this.divseqellipseup1.dy = " + this.divseqellipseup1.dy);
        this.ydeviation.slider1.setValue(this.divseqellipseup1.dy);
        SpinnerNumberModel model3 = new SpinnerNumberModel(this.divseqellipseup1.dy, 70, 150, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.setGlobalValues(this.divseqellipseup1);
        this.displaydivseqellipseup();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divseqellipsednButton){
      if(this.divseqellipsednButton.isSelected() == true){

        p1.show(this.parameterPanel, "divseqellipsedown");

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(this.divseqellipsedn1.maxlightness);
        SpinnerNumberModel model1 = new SpinnerNumberModel(this.divseqellipsedn1.maxlightness, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(this.divseqellipsedn1.minlightness);
        SpinnerNumberModel model2 = new SpinnerNumberModel(this.divseqellipsedn1.minlightness, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(this.divseqellipsedn1.dy);
        SpinnerNumberModel model3 = new SpinnerNumberModel(this.divseqellipsedn1.dy, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.setGlobalValues(this.divseqellipsedn1);
        this.displaydivseqellipsedn();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.divseqgridButton){
      if(this.divseqgridButton.isSelected() == true){

        p1.show(this.parameterPanel, "divseqgrid");

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(this.divseqgrid1.maxlightness);
        SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(this.divseqgrid1.minlightness);
        SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(this.divseqgrid1.dy);
        SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.setGlobalValues(this.divseqgrid1);
        this.displaydivseqgrid();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.DivseqTrapezoidButton){
      if(this.DivseqTrapezoidButton.isSelected() == true){

        p1.show(this.parameterPanel, "DivseqTrapezoid");

        this.maxlightness.slider1.setMinimum(50);
        this.maxlightness.slider1.setMaximum(100);
        this.maxlightness.slider1.setValue(this.DivseqTrapezoid1.maxlightness);
        SpinnerNumberModel model1 = new SpinnerNumberModel(95, 50, 100, 1);
        this.maxlightness.spinner1.setModel(model1);

        this.minlightness.slider1.setMinimum(0);
        this.minlightness.slider1.setMaximum(50);
        this.minlightness.slider1.setValue(this.DivseqTrapezoid1.minlightness);
        SpinnerNumberModel model2 = new SpinnerNumberModel(50, 0, 50, 1);
        this.minlightness.spinner1.setModel(model2);

        this.ydeviation.slider1.setMinimum(-50);
        this.ydeviation.slider1.setMaximum(50);
        this.ydeviation.slider1.setValue(this.DivseqTrapezoid1.shift_h);
        SpinnerNumberModel model3 = new SpinnerNumberModel(0, -50, 50, 1);
        this.ydeviation.spinner1.setModel(model3);

        this.setGlobalValues(this.DivseqTrapezoid1);
        this.displayDivseqTrapezoid();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.seqseqgraydiamondButton){
      if(this.seqseqgraydiamondButton.isSelected() == true){

        p1.show(this.parameterPanel, "seqseqgraydiamond");

        this.setGlobalValues(this.seqseqgraydiamond1);
        this.displayseqseqgray();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    if(e.getSource() == this.seqseqnongraydiamondButton){
      if(this.seqseqnongraydiamondButton.isSelected() == true){

        p1.show(this.parameterPanel, "seqseqnongraydiamond");

        this.setGlobalValues(this.seqseqnongraydiamond1);
        this.displayseqseqnongray();
        if(this.f.isShowing() == true){
          this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
        }
      }
      this.repaint();
    }

    //#####################################################################################################

    //if fired by the open, save or default buttons

    //#####################################################################################################



    if (e.getSource() == saveButton) {
      int returnVal = this.fc.showSaveDialog(this.saveButton);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        try{
          DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

          if(this.quaseqButton.isSelected() == true){
            out.writeChars("A qualitative/sequential bivariate color scheme.");
            out.writeChar('\n');
            if(this.quaseqconeButton.isSelected() == true){
              out.writeChars("Created by using a cone as geometric object.");
              out.writeChar('\n');
              out.writeChars("The height of the cone is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqcone1.height);
              //out.writeChars('');
              out.writeChar('\n');
              out.writeChars("The radius of the cone is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqcone1.radius);
              out.writeChar('\n');
            }
            if(this.quaseqbellcurveButton.isSelected() == true){
              out.writeChars("Created by using a bell curve as geometric object.");
              out.writeChar('\n');
              out.writeChars("The vertex of the bell curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqbellcurve1.curvevertex);
              out.writeChar('\n');
              out.writeChars("The divisor of the bell curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqbellcurve1.divisor);
              out.writeChar('\n');
            }
            if(this.quaseqhalfellipsoidButton.isSelected() == true){
              out.writeChars("Created by using a half ellisoid as geometric object.");
              out.writeChar('\n');
              out.writeChars("The a & b radius of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqhalfellipsoid1.a);
              out.writeChar('\n');
              out.writeChars("The c radius of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqhalfellipsoid1.c);
              out.writeChar('\n');
            }
            if(this.quaseqparabolaButton.isSelected() == true){
              out.writeChars("Created by using a parabola as geometric object.");
              out.writeChar('\n');
              out.writeChars("The constant of the parabola is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqparabola1.constant);
              out.writeChar('\n');
            }
          }

          if(this.divdivButton.isSelected() == true){
            out.writeChars("A diverging/diverging bivariate color scheme.");
            out.writeChar('\n');
            if(this.divdivbellcurveButton.isSelected() == true){
              out.writeChars("Created by using bell curve as geometric object.");
              out.writeChar('\n');
              out.writeChars("The vertex of the bell curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivbellcurve1.curvevertex);
              out.writeChar('\n');
              out.writeChars("The divisor of the bell curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivbellcurve1.divisor);
              out.writeChar('\n');
            }
            if(this.divdivhalfellipsoidButton.isSelected() == true){
              out.writeChars("Created by using a half ellisoid as geometric object.");
              out.writeChar('\n');
              out.writeChars("The a & b radius of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivhalfellipsoid1.a);
              out.writeChar('\n');
              out.writeChars("The c radius of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivhalfellipsoid1.c);
              out.writeChar('\n');
            }
          }

          if(this.divseqButton.isSelected() == true){
            out.writeChars("A diverging/sequential bivariate color scheme.");
            out.writeChar('\n');
            if(this.divseqellipseupButton.isSelected() == true){
              out.writeChars("Created by using an ellipse curling upward as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the ellipse is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqellipseup1.alpha);
              out.writeChar('\n');
              out.writeChars("The shape index of the ellipse is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqellipseup1.e);
              out.writeChar('\n');
            }
            if(this.divseqellipsednButton.isSelected() == true){
              out.writeChars("Created by using an ellipse curling downward as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the ellipse is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqellipsedn1.alpha);
              out.writeChar('\n');
              out.writeChars("The shape index of the ellipse is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqellipsedn1.e);
              out.writeChar('\n');
            }
            if(this.divseqgridButton.isSelected() == true){
              out.writeChars("Created by using a set of grids as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the grid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqgrid1.alpha);
              out.writeChar('\n');
              out.writeChars("The saturation range of the grid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqgrid1.range);
              out.writeChar('\n');
            }
            if(this.DivseqTrapezoidButton.isSelected() == true){
              out.writeChars("Created by using a trapezoid as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the trapezoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.DivseqTrapezoid1.alpha);
              out.writeChar('\n');
              out.writeChars("The radius of the trapezoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.DivseqTrapezoid1.radius);
              out.writeChar('\n');
            }
          }

          if(this.seqseqgrayButton.isSelected() == true){
            out.writeChars("A sequential/sequential (with gray axis) bivariate color scheme.");
            out.writeChar('\n');
            if(this.seqseqgraydiamondButton.isSelected() == true){
              out.writeChars("Created by using a diamond as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqgraydiamond1.alpha);
              out.writeChar('\n');
            }
          }

          if(this.seqseqnongrayButton.isSelected() == true){
            out.writeChars("A sequential/sequential (without gray axis) bivariate color scheme.");
            out.writeChar('\n');
            if(this.seqseqnongraydiamondButton.isSelected() == true){
              out.writeChars("Created by using a tilted diamond as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqnongraydiamond1.alpha);
              out.writeChar('\n');
              out.writeChars("The tilt angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqnongraydiamond1.beta);
              out.writeChar('\n');
            }
          }

          out.writeChars("The vertical classes of this color scheme is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.vertical.slider1.getValue());
          out.writeChar('\n');
          out.writeChars("The horizontal classes of this color scheme is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.horizontal.slider1.getValue());
          out.writeChar('\n');
          if(this.maxlightness.isShowing() == true){
            out.writeChars("The lightness range of this color scheme is: from " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.minlightness.slider1.getValue() + " to " + this.maxlightness.slider1.getValue());
          }
          if(this.range.isShowing() == true){
            out.writeChars("The range of this color scheme is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.range.slider1.getValue());
          }
          out.writeChar('\n');
          out.writeChars("The deviation in a-b surface of this color scheme is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.xdeviation.slider1.getValue());
          out.writeChar('\n');
          out.writeChars("The starting hue of this color scheme is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.startinghue.slider1.getValue());
          out.writeChar('\n');
          out.writeChars("The deviation along the lightness axis of this color scheme is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.ydeviation.slider1.getValue());
          out.writeChar('\n');


          this.getcurrentcheckbox();

          for(int i = 0; i < this.vertical.slider1.getValue(); i ++){
            for(int j = 0; j < this.horizontal.slider1.getValue(); j ++){
              Integer int1 = new Integer(this.colorarray[i][j][0][this.currentcheckbox]);
              out.writeChars(int1.toString());
              out.writeChar('\t');
              Integer int2 = new Integer(this.colorarray[i][j][1][this.currentcheckbox]);
              out.writeChars(int2.toString());
              out.writeChar('\t');
              Integer int3 = new Integer(this.colorarray[i][j][2][this.currentcheckbox]);
              out.writeChars(int3.toString());
              out.writeChar('\t');
              out.writeChar('\t');
            }
            out.writeChar('\n');

          }

          out.writeChar('\n');

          for(int i = 0; i < this.vertical.slider1.getValue(); i ++){
            for(int j = 0; j < this.horizontal.slider1.getValue(); j ++){
              Integer int1 = new Integer(this.labcolorarray[i][j][0][this.currentcheckbox]);
              out.writeChars(int1.toString());
              out.writeChar('\t');
              Integer int2 = new Integer(this.labcolorarray[i][j][1][this.currentcheckbox]);
              out.writeChars(int2.toString());
              out.writeChar('\t');
              Integer int3 = new Integer(this.labcolorarray[i][j][2][this.currentcheckbox]);
              out.writeChars(int3.toString());
              out.writeChar('\t');
              out.writeChar('\t');
            }
            out.writeChar('\n');
          }

          out.close();
        }
        catch(IOException ioe){
          System.err.println("Caught IOException: " + ioe.getMessage());
        }
      }
    }

    //don't forget this!!!
    this.revalidate();
  }




  public void stateChanged(ChangeEvent e){

    //#####################################################################################################

    //if event fired by basics spinners

    //#####################################################################################################


    if(e.getSource() == this.vertical.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divdivButton.isSelected() == true){
        if(this.divdivbellcurveButton.isSelected() == true){
          this.divdivbellcurve1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displaydivdivbellcurve();
        }
        if(this.divdivhalfellipsoidButton.isSelected() == true){
          this.divdivhalfellipsoid1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displaydivdivhalfellipsoid();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.vclass = Integer.valueOf(String.valueOf(this.vertical.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

      this.repaint();

    }

    if(e.getSource() == this.horizontal.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          //this.quaseqcone1 = new Quaseqcone(this.quaseqcone1.hclass, this.quaseqcone1.vclass, this.quaseqcone1.maxlightness, this.quaseqcone1.minlightness, this.quaseqcone1.height, this.quaseqcone1.radius, this.quaseqcone1.dx, this.quaseqcone1.dy, this.quaseqcone1.startinghue);
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divdivButton.isSelected() == true){
        if(this.divdivbellcurveButton.isSelected() == true){
          this.divdivbellcurve1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displaydivdivbellcurve();
        }
        if(this.divdivhalfellipsoidButton.isSelected() == true){
          this.divdivhalfellipsoid1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displaydivdivhalfellipsoid();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.hclass = Integer.valueOf(String.valueOf(this.horizontal.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

      this.repaint();
    }


    if(e.getSource() == this.maxlightness.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.maxlightness = Integer.valueOf(String.valueOf(this.maxlightness.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

    }

    if(e.getSource() == this.minlightness.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.minlightness = Integer.valueOf(String.valueOf(this.minlightness.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

    }

    if(e.getSource() == this.range.spinner1){

      if(this.divdivButton.isSelected() == true){
        if(this.divdivbellcurveButton.isSelected() == true){
          this.divdivbellcurve1.range = Integer.valueOf(String.valueOf(this.range.spinner1.getValue())).intValue();
          this.displaydivdivbellcurve();
        }
        if(this.divdivhalfellipsoidButton.isSelected() == true){
          this.divdivhalfellipsoid1.range = Integer.valueOf(String.valueOf(this.range.spinner1.getValue())).intValue();
          this.displaydivdivhalfellipsoid();
        }
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

    }

    //#####################################################################################################

    //If event fired by the deviation spinners

    //#####################################################################################################

    if(e.getSource() == this.startinghue.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.shift_p = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divdivButton.isSelected() == true){
        if(this.divdivbellcurveButton.isSelected() == true){
          this.divdivbellcurve1.shift_p = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displaydivdivbellcurve();
        }
        if(this.divdivhalfellipsoidButton.isSelected() == true){
          this.divdivhalfellipsoid1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displaydivdivhalfellipsoid();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.shift_p = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.startinghue = Integer.valueOf(String.valueOf(this.startinghue.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

    }

    if(e.getSource() == this.xdeviation.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.shift_r = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divdivButton.isSelected() == true){
        if(this.divdivbellcurveButton.isSelected() == true){
          this.divdivbellcurve1.shift_r = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displaydivdivbellcurve();
        }
        if(this.divdivhalfellipsoidButton.isSelected() == true){
          this.divdivhalfellipsoid1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displaydivdivhalfellipsoid();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.shift_r = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.dx = Integer.valueOf(String.valueOf(this.xdeviation.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

    }

    if(e.getSource() == this.ydeviation.spinner1){

      //check the current object, and respond respectively
      if(this.quaseqButton.isSelected() == true){
        if(this.quaseqbellcurveButton.isSelected() == true){
          this.quaseqbellcurve1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displayquaseqbellcurve();
        }
        if(this.quaseqconeButton.isSelected() == true){
          this.quaseqcone1.shift_h = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displayquaseqcone();
        }
        if(this.quaseqhalfellipsoidButton.isSelected() == true){
          this.quaseqhalfellipsoid1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displayquaseqhalfellipsoid();
        }
        if(this.quaseqparabolaButton.isSelected() == true){
          this.quaseqparabola1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displayquaseqparabola();
        }
      }

      if(this.divdivButton.isSelected() == true){
        if(this.divdivbellcurveButton.isSelected() == true){
          this.divdivbellcurve1.shift_h = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displaydivdivbellcurve();
        }
        if(this.divdivhalfellipsoidButton.isSelected() == true){
          this.divdivhalfellipsoid1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displaydivdivhalfellipsoid();
        }
      }

      if(this.divseqButton.isSelected() == true){
        if(this.divseqellipseupButton.isSelected() == true){
          this.divseqellipseup1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displaydivseqellipseup();
        }
        if(this.divseqellipsednButton.isSelected() == true){
          this.divseqellipsedn1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displaydivseqellipsedn();
        }
        if(this.divseqgridButton.isSelected() == true){
          this.divseqgrid1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displaydivseqgrid();
        }
        if(this.DivseqTrapezoidButton.isSelected() == true){
          this.DivseqTrapezoid1.shift_h = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
          this.displayDivseqTrapezoid();
        }
      }

      if(this.seqseqgrayButton.isSelected() == true){
        this.seqseqgraydiamond1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
        this.displayseqseqgray();
      }

      if(this.seqseqnongrayButton.isSelected() == true){
        this.seqseqnongraydiamond1.dy = Integer.valueOf(String.valueOf(this.ydeviation.spinner1.getValue())).intValue();
        this.displayseqseqnongray();
      }

      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }

    }

    //#####################################################################################################

    //if the source of state change is quaseq spinners

    //#####################################################################################################

    if(e.getSource() == this.quaseqbellcurvevertex.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqbellcurve1.curvevertex = Integer.valueOf(String.valueOf(this.quaseqbellcurvevertex.spinner1.getValue())).intValue();
      this.displayquaseqbellcurve();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
          this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.quaseqbellcurvedivisor.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqbellcurve1.divisor = Integer.valueOf(String.valueOf(this.quaseqbellcurvedivisor.spinner1.getValue())).intValue();
      this.displayquaseqbellcurve();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.quaseqconeheight.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqcone1.height = Integer.valueOf(String.valueOf(this.quaseqconeheight.spinner1.getValue())).intValue();
      this.displayquaseqcone();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.quaseqconeradius.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqcone1.radius = Integer.valueOf(String.valueOf(this.quaseqconeradius.spinner1.getValue())).intValue();
      this.displayquaseqcone();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.quaseqhalfellipsoidradiusab.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqhalfellipsoid1.a = Integer.valueOf(String.valueOf(this.quaseqhalfellipsoidradiusab.spinner1.getValue())).intValue();
      this.displayquaseqhalfellipsoid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.quaseqhalfellipsoidradiusc.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqhalfellipsoid1.c = Integer.valueOf(String.valueOf(this.quaseqhalfellipsoidradiusc.spinner1.getValue())).intValue();
      this.displayquaseqhalfellipsoid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.quaseqparabolaconstant.spinner1){
      //call customized method to get the current, and this.currentcheckbox is initialized
      this.getcurrentcheckbox();
      //remove the current color patches
      this.samples[currentcheckbox].removeAll();
      //call the customized method to reload schemes
      this.quaseqparabola1.constant = Integer.valueOf(String.valueOf(this.quaseqparabolaconstant.spinner1.getValue())).intValue();
      this.displayquaseqparabola();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }


    //#####################################################################################################

    //if the source of state change is divdiv spinners

    //#####################################################################################################

    if(e.getSource() == this.divdivbellcurvevertex.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divdivbellcurve1.curvevertex = Integer.valueOf(String.valueOf(this.divdivbellcurvevertex.spinner1.getValue())).intValue();
      this.displaydivdivbellcurve();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divdivbellcurvedivisor.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divdivbellcurve1.divisor = Integer.valueOf(String.valueOf(this.divdivbellcurvedivisor.spinner1.getValue())).intValue();
      this.displaydivdivbellcurve();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divdivhalfellipsoidradiusab.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divdivhalfellipsoid1.a = Integer.valueOf(String.valueOf(this.divdivhalfellipsoidradiusab.spinner1.getValue())).intValue();
      this.displaydivdivhalfellipsoid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divdivhalfellipsoidradiusc.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divdivhalfellipsoid1.c = Integer.valueOf(String.valueOf(this.divdivhalfellipsoidradiusc.spinner1.getValue())).intValue();
      this.displaydivdivhalfellipsoid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    //#####################################################################################################

    //if fired by divseq spinners

    //#####################################################################################################

    if(e.getSource() == this.divseqellipseupalpha.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divseqellipseup1.alpha = Integer.valueOf(String.valueOf(this.divseqellipseupalpha.spinner1.getValue())).intValue();
      this.displaydivseqellipseup();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divseqellipseupe.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divseqellipseup1.e = Integer.valueOf(String.valueOf(this.divseqellipseupe.spinner1.getValue())).intValue();
      this.displaydivseqellipseup();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divseqellipsedownalpha.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divseqellipsedn1.alpha = Integer.valueOf(String.valueOf(this.divseqellipsedownalpha.spinner1.getValue())).intValue();
      this.displaydivseqellipsedn();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divseqellipsedowne.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divseqellipsedn1.e = Integer.valueOf(String.valueOf(this.divseqellipsedowne.spinner1.getValue())).intValue();
      this.displaydivseqellipsedn();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divseqgridalpha.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divseqgrid1.alpha = Integer.valueOf(String.valueOf(this.divseqgridalpha.spinner1.getValue())).intValue();
      this.displaydivseqgrid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.divseqgridrange.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.divseqgrid1.range = Integer.valueOf(String.valueOf(this.divseqgridrange.spinner1.getValue())).intValue();
      this.displaydivseqgrid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.DivseqTrapezoidalpha.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.DivseqTrapezoid1.alpha = Integer.valueOf(String.valueOf(this.DivseqTrapezoidalpha.spinner1.getValue())).intValue();
      this.displayDivseqTrapezoid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.DivseqTrapezoidradius.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.DivseqTrapezoid1.radius = Integer.valueOf(String.valueOf(this.DivseqTrapezoidradius.spinner1.getValue())).intValue();
      this.displayDivseqTrapezoid();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    //#####################################################################################################

    //if the source of state change is seqseqgray spinners

    //#####################################################################################################

    if(e.getSource() == this.seqseqgraydiamondalpha.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.seqseqgraydiamond1.alpha = Integer.valueOf(String.valueOf(this.seqseqgraydiamondalpha.spinner1.getValue())).intValue();
      this.displayseqseqgray();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    //#####################################################################################################

    //if the source of state change is seqseqnongray spinners

    //#####################################################################################################

    if(e.getSource() == this.seqseqnongraydiamondalpha.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.seqseqnongraydiamond1.alpha = Integer.valueOf(String.valueOf(this.seqseqnongraydiamondalpha.spinner1.getValue())).intValue();
      this.displayseqseqnongray();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    if(e.getSource() == this.seqseqnongraydiamondbeta.spinner1){
      //clear the current color patches
      this.getcurrentcheckbox();
      this.samples[currentcheckbox].removeAll();
      this.seqseqnongraydiamond1.beta = Integer.valueOf(String.valueOf(this.seqseqnongraydiamondbeta.spinner1.getValue())).intValue();
      this.displayseqseqnongray();
      if(this.f.isShowing() == true){
        this.getcurrentcheckbox();
        this.popupRepaint(this.currentcheckbox);
      }
    }

    //don't forget this!!!
    this.revalidate();

  }

  //the quaseq with the bell curve structure
  public void displayquaseqbellcurve() {
    this.quaseqbellcurve1 = new Quaseqbellcurve(this.quaseqbellcurve1.vclass, this.quaseqbellcurve1.hclass, this.quaseqbellcurve1.maxlightness, this.quaseqbellcurve1.minlightness, this.quaseqbellcurve1.curvevertex, this.quaseqbellcurve1.divisor, this.quaseqbellcurve1.dx, this.quaseqbellcurve1.dy, this.quaseqbellcurve1.startinghue);
    this.createPatches(this.quaseqbellcurve1.vclass, this.quaseqbellcurve1.hclass, this.quaseqbellcurve1.labcolor);
  }

  //the quaseq with the cone structure
  public void displayquaseqcone() {
    this.quaseqcone1 = new QuaseqCone(this.quaseqcone1.vclass, this.quaseqcone1.hclass, this.quaseqcone1.maxlightness, this.quaseqcone1.minlightness, this.quaseqcone1.height, this.quaseqcone1.radius, this.quaseqcone1.shift_r, this.quaseqcone1.shift_h, this.quaseqcone1.shift_p);
    this.createPatches(this.quaseqcone1.vclass, this.quaseqcone1.hclass, this.quaseqcone1.labcolor);
  }

  //the quaseq with the halfsphere structure
  public void displayquaseqhalfellipsoid() {
    this.quaseqhalfellipsoid1 = new Quaseqhalfellipsoid(this.quaseqhalfellipsoid1.vclass, this.quaseqhalfellipsoid1.hclass, this.quaseqhalfellipsoid1.maxlightness, this.quaseqhalfellipsoid1.minlightness, this.quaseqhalfellipsoid1.a, this.quaseqhalfellipsoid1.a, this.quaseqhalfellipsoid1.c, this.quaseqhalfellipsoid1.dx, this.quaseqhalfellipsoid1.dy, this.quaseqhalfellipsoid1.startinghue);
    this.createPatches(this.quaseqhalfellipsoid1.vclass, this.quaseqhalfellipsoid1.hclass, this.quaseqhalfellipsoid1.labcolor);
  }

  //the quaseq with the parabola structure
  public void displayquaseqparabola() {
    this.quaseqparabola1 = new Quaseqparabola(this.quaseqparabola1.vclass, this.quaseqparabola1.hclass, this.quaseqparabola1.maxlightness, this.quaseqparabola1.minlightness, this.quaseqparabola1.constant, this.quaseqparabola1.dx, this.quaseqparabola1.dy, this.quaseqparabola1.startinghue);
    this.createPatches(this.quaseqparabola1.vclass, this.quaseqparabola1.hclass, this.quaseqparabola1.labcolor);
  }

  //the divdiv with bell curve model
  public void displaydivdivbellcurve() {
    this.divdivbellcurve1 = new DivdivBellshape(this.divdivbellcurve1.vclass, this.divdivbellcurve1.hclass, this.divdivbellcurve1.range, this.divdivbellcurve1.curvevertex, this.divdivbellcurve1.divisor, this.divdivbellcurve1.shift_r, this.divdivbellcurve1.shift_h, this.divdivbellcurve1.shift_p);
    this.createPatches(this.divdivbellcurve1.vclass, this.divdivbellcurve1.hclass, this.divdivbellcurve1.labcolor);
  }

  //the divdiv with half ellipsoid model
  public void displaydivdivhalfellipsoid() {
    this.divdivhalfellipsoid1 = new Divdivhalfellipsoid(this.divdivhalfellipsoid1.vclass, this.divdivhalfellipsoid1.hclass, this.divdivhalfellipsoid1.range, this.divdivhalfellipsoid1.a, this.divdivhalfellipsoid1.c, this.divdivhalfellipsoid1.dx, this.divdivhalfellipsoid1.dy, this.divdivhalfellipsoid1.startinghue);
    this.createPatches(this.divdivhalfellipsoid1.vclass, this.divdivhalfellipsoid1.hclass, this.divdivhalfellipsoid1.labcolor);
  }

  //the divseq with the ellipse ellipse (upward) structure
  public void displaydivseqellipseup() {
    this.divseqellipseup1 = new Divseqellipseup(this.divseqellipseup1.vclass, this.divseqellipseup1.hclass, this.divseqellipseup1.maxlightness, this.divseqellipseup1.minlightness, this.divseqellipseup1.alpha, this.divseqellipseup1.e, this.divseqellipseup1.dx, this.divseqellipseup1.dy, this.divseqellipseup1.startinghue);
    this.createPatches(this.divseqellipseup1.vclass, this.divseqellipseup1.hclass, this.divseqellipseup1.labcolor);
  }

  //the divseq with the ellipse ellipse (downward) structure
  public void displaydivseqellipsedn() {
    this.divseqellipsedn1 = new Divseqellipsedn(this.divseqellipsedn1.vclass, this.divseqellipsedn1.hclass, this.divseqellipsedn1.maxlightness, this.divseqellipsedn1.minlightness, this.divseqellipsedn1.alpha, this.divseqellipsedn1.e, this.divseqellipsedn1.dx, this.divseqellipsedn1.dy, this.divseqellipsedn1.startinghue);
    this.createPatches(this.divseqellipsedn1.vclass, this.divseqellipsedn1.hclass, this.divseqellipsedn1.labcolor);
  }

  //the divseq with the grids structure
  public void displaydivseqgrid() {
    this.divseqgrid1 = new Divseqgrids(this.divseqgrid1.vclass, this.divseqgrid1.hclass, this.divseqgrid1.maxlightness, this.divseqgrid1.minlightness, this.divseqgrid1.alpha, this.divseqgrid1.range, this.divseqgrid1.dx, this.divseqgrid1.dy, this.divseqgrid1.startinghue);
    this.createPatches(this.divseqgrid1.vclass, this.divseqgrid1.hclass, this.divseqgrid1.labcolor);
  }

  //the divseq with the trapezoid structure
  public void displayDivseqTrapezoid() {
    this.DivseqTrapezoid1 = new DivseqTrapezoid(this.DivseqTrapezoid1.vclass, this.DivseqTrapezoid1.hclass, this.DivseqTrapezoid1.maxlightness, this.DivseqTrapezoid1.minlightness, this.DivseqTrapezoid1.alpha, this.DivseqTrapezoid1.radius, this.DivseqTrapezoid1.shift_r, this.DivseqTrapezoid1.shift_h, this.DivseqTrapezoid1.shift_p);
    this.createPatches(this.DivseqTrapezoid1.vclass, this.DivseqTrapezoid1.hclass, this.DivseqTrapezoid1.labcolor);
  }

  //the seqseqgray with the diamond structure
  public void displayseqseqgray() {
    this.seqseqgraydiamond1 = new Seqseqgraydiamond(this.seqseqgraydiamond1.vclass, this.seqseqgraydiamond1.hclass, this.seqseqgraydiamond1.maxlightness, this.seqseqgraydiamond1.minlightness, this.seqseqgraydiamond1.alpha, this.seqseqgraydiamond1.dx, this.seqseqgraydiamond1.dy, this.seqseqgraydiamond1.startinghue);
    this.createPatches(this.seqseqgraydiamond1.vclass, this.seqseqgraydiamond1.hclass, this.seqseqgraydiamond1.labcolor);
  }

  //the seqseqnongray with the diamond structure
  public void displayseqseqnongray() {
    this.seqseqnongraydiamond1 = new Seqseqnongraydiamond(this.seqseqnongraydiamond1.vclass, this.seqseqnongraydiamond1.hclass, this.seqseqnongraydiamond1.maxlightness, this.seqseqnongraydiamond1.minlightness, this.seqseqnongraydiamond1.alpha, this.seqseqnongraydiamond1.beta, this.seqseqnongraydiamond1.dx, this.seqseqnongraydiamond1.dy, this.seqseqnongraydiamond1.startinghue);
    this.createPatches(this.seqseqnongraydiamond1.vclass, this.seqseqnongraydiamond1.hclass, this.seqseqnongraydiamond1.labcolor);
  }

  public void getcurrentcheckbox(){

    //which of the checkbox is currently selected
    for(int i = 0; i < 3; i ++){
      if(this.checkbox[i].isSelected() == true){
        this.currentcheckbox = i;
      }
    }
    //by default, make the currentcheckbox the first one
    //if(this.checkbox[0].isSelected() == false && this.checkbox[1].isSelected() == false && this.checkbox[2].isSelected() == false){
      //this.currentcheckbox = 0;
    //}
  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL = SRGBDesignBoard.class.getResource(path);
    if (imgURL != null) {
      return new ImageIcon(imgURL);
    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }

  private void popupRepaint(int clickedSample){

      this.f.getContentPane().removeAll();
      JPanel colorpopup = new JPanel();
      colorpopup.setPreferredSize(new Dimension(300, 300));
      colorpopup.setLayout(this.gridLayout[clickedSample]);
      int rows = this.gridLayout[clickedSample].getRows();
      int columns = this.gridLayout[clickedSample].getColumns();

      this.f.getContentPane().add(colorpopup);

      for(int i = 0; i < rows; i ++){
        for(int j = 0; j < columns; j ++){
          JPanel p = new JPanel();
          p.setPreferredSize(new Dimension((int)Math.floor(300/columns), (int)Math.floor(300/rows)));
          p.setBackground(new Color(this.colorarray[i][j][0][clickedSample], this.colorarray[i][j][1][clickedSample], this.colorarray[i][j][2][clickedSample]));
          colorpopup.add(p);
        }
      }

      this.f.setTitle("Pop up test field");
      this.f.setVisible(true);
      this.f.repaint();
  }

  private void createPatches(int vclass, int hclass, LABcolor labcolor[][]){

    //clear the current color patches
    this.getcurrentcheckbox();
    this.samples[this.currentcheckbox].removeAll();
    GridLayout g1 = new GridLayout(vclass, hclass);
    this.samples[this.currentcheckbox].setLayout(g1);
    this.samples[this.currentcheckbox].setToolTipText("Click to enlarge");
    //record the gridlayout for the testfield
    this.gridLayout[this.currentcheckbox] = g1;

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(labcolor[i][j].L, labcolor[i][j].a, labcolor[i][j].b);
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension((int)Math.floor(200/hclass), (int)Math.floor(200/vclass)));
        p.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
        this.samples[currentcheckbox].add(p);

        this.colorarray[i][j][0][this.currentcheckbox] = (int)CIELabToSRGB1.R255;
        this.colorarray[i][j][1][this.currentcheckbox] = (int)CIELabToSRGB1.G255;
        this.colorarray[i][j][2][this.currentcheckbox] = (int)CIELabToSRGB1.B255;

        this.labcolorarray[i][j][0][this.currentcheckbox] = (int)labcolor[i][j].L;
        this.labcolorarray[i][j][1][this.currentcheckbox] = (int)labcolor[i][j].a;
        this.labcolorarray[i][j][2][this.currentcheckbox] = (int)labcolor[i][j].b;

      }
    }
  }

  private void setGlobalValues(SchemeObject schemeobject){
    if (schemeobject == this.quaseqbellcurve1){
      this.vertical.slider1.setValue(this.quaseqbellcurve1.vclass);
      this.horizontal.slider1.setValue(this.quaseqbellcurve1.hclass);
      this.maxlightness.slider1.setValue(this.quaseqbellcurve1.maxlightness);
      this.minlightness.slider1.setValue(this.quaseqbellcurve1.minlightness);
      this.xdeviation.slider1.setValue(this.quaseqbellcurve1.dx);
      this.ydeviation.slider1.setValue(this.quaseqbellcurve1.dy);
      this.startinghue.slider1.setValue(this.quaseqbellcurve1.startinghue);
    }

    if (schemeobject == this.quaseqcone1){
      this.vertical.slider1.setValue(this.quaseqcone1.vclass);
      this.horizontal.slider1.setValue(this.quaseqcone1.hclass);
      this.maxlightness.slider1.setValue(this.quaseqcone1.maxlightness);
      this.minlightness.slider1.setValue(this.quaseqcone1.minlightness);
      this.xdeviation.slider1.setValue(this.quaseqcone1.shift_r);
      this.ydeviation.slider1.setValue(this.quaseqcone1.shift_h);
      this.startinghue.slider1.setValue(this.quaseqcone1.shift_p);
    }

    if (schemeobject == this.quaseqhalfellipsoid1){
      this.vertical.slider1.setValue(this.quaseqhalfellipsoid1.vclass);
      this.horizontal.slider1.setValue(this.quaseqhalfellipsoid1.hclass);
      this.maxlightness.slider1.setValue(this.quaseqhalfellipsoid1.maxlightness);
      this.minlightness.slider1.setValue(this.quaseqhalfellipsoid1.minlightness);
      this.xdeviation.slider1.setValue(this.quaseqhalfellipsoid1.dx);
      this.ydeviation.slider1.setValue(this.quaseqhalfellipsoid1.dy);
      this.startinghue.slider1.setValue(this.quaseqhalfellipsoid1.startinghue);
    }

    if (schemeobject == this.quaseqparabola1){
      this.vertical.slider1.setValue(this.quaseqparabola1.vclass);
      this.horizontal.slider1.setValue(this.quaseqparabola1.hclass);
      this.maxlightness.slider1.setValue(this.quaseqparabola1.maxlightness);
      this.minlightness.slider1.setValue(this.quaseqparabola1.minlightness);
      this.xdeviation.slider1.setValue(this.quaseqparabola1.dx);
      this.ydeviation.slider1.setValue(this.quaseqparabola1.dy);
      this.startinghue.slider1.setValue(this.quaseqparabola1.startinghue);
    }

    if (schemeobject == this.divdivbellcurve1){
      this.vertical.slider1.setValue(this.divdivbellcurve1.vclass);
      this.horizontal.slider1.setValue(this.divdivbellcurve1.hclass);
      this.range.slider1.setValue(this.divdivbellcurve1.range);
      this.xdeviation.slider1.setValue(this.divdivbellcurve1.shift_r);
      this.ydeviation.slider1.setValue(this.divdivbellcurve1.shift_h);
      this.startinghue.slider1.setValue(this.divdivbellcurve1.shift_p);
    }

    if (schemeobject == this.divdivhalfellipsoid1){
      this.vertical.slider1.setValue(this.divdivhalfellipsoid1.vclass);
      this.horizontal.slider1.setValue(this.divdivhalfellipsoid1.hclass);
      this.range.slider1.setValue(this.divdivhalfellipsoid1.range);
      this.xdeviation.slider1.setValue(this.divdivhalfellipsoid1.dx);
      this.ydeviation.slider1.setValue(this.divdivhalfellipsoid1.dy);
      this.startinghue.slider1.setValue(this.divdivhalfellipsoid1.startinghue);
    }

    if (schemeobject == this.divseqellipseup1){
      this.vertical.slider1.setValue(this.divseqellipseup1.vclass);
      this.horizontal.slider1.setValue(this.divseqellipseup1.hclass);
      this.maxlightness.slider1.setValue(this.divseqellipseup1.maxlightness);
      this.minlightness.slider1.setValue(this.divseqellipseup1.minlightness);
      this.xdeviation.slider1.setValue(this.divseqellipseup1.dx);
      this.ydeviation.slider1.setValue(this.divseqellipseup1.dy);
      this.startinghue.slider1.setValue(this.divseqellipseup1.startinghue);
    }

    if (schemeobject == this.divseqellipsedn1){
      this.vertical.slider1.setValue(this.divseqellipsedn1.vclass);
      this.horizontal.slider1.setValue(this.divseqellipsedn1.hclass);
      this.maxlightness.slider1.setValue(this.divseqellipsedn1.maxlightness);
      this.minlightness.slider1.setValue(this.divseqellipsedn1.minlightness);
      this.xdeviation.slider1.setValue(this.divseqellipsedn1.dx);
      this.ydeviation.slider1.setValue(this.divseqellipsedn1.dy);
      this.startinghue.slider1.setValue(this.divseqellipsedn1.startinghue);
    }

    if (schemeobject == this.divseqgrid1){
      this.vertical.slider1.setValue(this.divseqgrid1.vclass);
      this.horizontal.slider1.setValue(this.divseqgrid1.hclass);
      this.maxlightness.slider1.setValue(this.divseqgrid1.maxlightness);
      this.minlightness.slider1.setValue(this.divseqgrid1.minlightness);
      this.xdeviation.slider1.setValue(this.divseqgrid1.dx);
      this.ydeviation.slider1.setValue(this.divseqgrid1.dy);
      this.startinghue.slider1.setValue(this.divseqgrid1.startinghue);
    }

    if (schemeobject == this.DivseqTrapezoid1){
      this.vertical.slider1.setValue(this.DivseqTrapezoid1.vclass);
      this.horizontal.slider1.setValue(this.DivseqTrapezoid1.hclass);
      this.maxlightness.slider1.setValue(this.DivseqTrapezoid1.maxlightness);
      this.minlightness.slider1.setValue(this.DivseqTrapezoid1.minlightness);
      this.xdeviation.slider1.setValue(this.DivseqTrapezoid1.shift_r);
      this.ydeviation.slider1.setValue(this.DivseqTrapezoid1.shift_h);
      this.startinghue.slider1.setValue(this.DivseqTrapezoid1.shift_p);
    }

    if (schemeobject == this.seqseqgraydiamond1){
      this.vertical.slider1.setValue(this.seqseqgraydiamond1.vclass);
      this.horizontal.slider1.setValue(this.seqseqgraydiamond1.hclass);
      this.maxlightness.slider1.setValue(this.seqseqgraydiamond1.maxlightness);
      this.minlightness.slider1.setValue(this.seqseqgraydiamond1.minlightness);
      this.xdeviation.slider1.setValue(this.seqseqgraydiamond1.dx);
      this.ydeviation.slider1.setValue(this.seqseqgraydiamond1.dy);
      this.startinghue.slider1.setValue(this.seqseqgraydiamond1.startinghue);
    }

    if (schemeobject == this.seqseqnongraydiamond1){
      this.vertical.slider1.setValue(this.seqseqnongraydiamond1.vclass);
      this.horizontal.slider1.setValue(this.seqseqnongraydiamond1.hclass);
      this.maxlightness.slider1.setValue(this.seqseqnongraydiamond1.maxlightness);
      this.minlightness.slider1.setValue(this.seqseqnongraydiamond1.minlightness);
      this.xdeviation.slider1.setValue(this.seqseqnongraydiamond1.dx);
      this.ydeviation.slider1.setValue(this.seqseqnongraydiamond1.dy);
      this.startinghue.slider1.setValue(this.seqseqnongraydiamond1.startinghue);
    }
  }

  public void mouseClicked(MouseEvent e){

    for(int i = 0; i < 3; i++){
      if(e.getSource() == this.samples[i]){
        this.popupRepaint(i);
      }
    }

  }

  public void mousePressed(MouseEvent e){

  }

  public void mouseReleased(MouseEvent e){

  }

  public void mouseEntered(MouseEvent e){

  }

  public void mouseExited(MouseEvent e){

  }


  public void keyTyped(KeyEvent e) {
  }
  public void keyPressed(KeyEvent e) {
  }
  public void keyReleased(KeyEvent e) {
  }

}