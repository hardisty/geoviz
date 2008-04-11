package geovista.colorbrewer.coloreffect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class CIELUVDesignBoard extends JPanel implements ActionListener, ChangeListener{
	protected final static Logger logger = Logger.getLogger(CIELUVDesignBoard.class.getName());
  //create a 3D array to store the current color array (the maximum size is 15X15)
  int[][][] currentColorarray = new int[15][15][3];

  //create three 3D arrays to store the color array for each test field(the maximum size is 15X15)
  int[][][][] colorarray = new int[15][15][3][3];
  int[][][][] luvcolorarray = new int[15][15][3][3];

  //create a tabbedPane to host the categories of bivariate schemes
  JTabbedPane tabbedPane = new JTabbedPane();

  //create a panel for saving and loading functions, and two buttons
  JPanel saveLoad = new JPanel();
  JButton loadButton = new JButton("Open a File...", createImageIcon("resources/openFile.gif"));
  JButton saveButton = new JButton("Save a File...", createImageIcon("resources/saveFile.gif"));

  //create a filechooser
  final JFileChooser fc = new JFileChooser();

  //create a list of exemplary bivariate color schemes
  SeqseqDiamond seqseqDiamond1 = new SeqseqDiamond(5, 5, 95, 20, 120, 0, 0, 0);
  SeqseqTiltedDiamond seqseqTiltedDiamond1 = new SeqseqTiltedDiamond(5, 5, 95, 20, 120, 15, 0, 0, 0);
  SeqseqFoldedDiamond seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(5, 5, 95, 20, 45, 0, 270, 0, 0, 0);

  DivseqWedge divseqWedge1 = new DivseqWedge(5, 5, 100, 40, 150, 85, 160, 0, 0, 0);
  DivseqEllipse divseqEllipse1 = new DivseqEllipse(5, 5, 95, 50, 60, 0, 0, 0, 0);
  DivseqTrapezoid divseqTrapezoid1 = new DivseqTrapezoid(5, 5, 95, 50, 60, 100, 0, 0, 0);

  QuaseqCone quaseqCone1 = new QuaseqCone(5, 5, 95, 35, 150, 120, 0, 0, 0);
  QuaseqEllipsoid quaseqEllipsoid1 = new QuaseqEllipsoid(5, 5, 95, 35, 120, 120, 120, 0, 0, 0);
  QuaseqEllipsecurve quaseqEllipsecurve1 = new QuaseqEllipsecurve(5, 5, 95, 35, 300, 120, 0, 0, 0);
  QuaseqBellshape quaseqBellshape1 = new QuaseqBellshape(5, 5, 95, 35, 150, 5000, 0, 0, 0);

  DivdivCone divdivCone1 = new DivdivCone(5, 5, 160, 100, 130, 0, 0, 0);
  DivdivEllipsoid divdivEllipsoid1 = new DivdivEllipsoid(5, 5, 160, 100, 100, 0, 0, 0);
  DivdivEllipsecurve divdivEllipsecurve1 = new DivdivEllipsecurve(5, 5, 160, 1800, 100, 0, 0, 0);
  DivdivBellshape divdivBellshape1 = new DivdivBellshape(5, 5, 160, 100, 7500, 0, 0, 0);

  //f holds the popup figures
  JFrame f = new JFrame();

  //Create the list of figures
  JButton seqseqDiamondFigureButton = new JButton(createImageIcon("resources/SeqseqDiamond.gif"));

  JPanel northPanel = new JPanel();
  JPanel southPanel = new JPanel();

  JPanel seqseqPanel = new JPanel();
  JPanel divseqPanel = new JPanel();
  JPanel quaseqPanel = new JPanel();
  JPanel divdivPanel = new JPanel();

  JPanel seqseqGeometricObjectPanel = new JPanel();
  JPanel divseqGeometricObjectPanel = new JPanel();
  JPanel quaseqGeometricObjectPanel = new JPanel();
  JPanel divdivGeometricObjectPanel = new JPanel();

  JPanel seqseqParameterPanel = new JPanel();
  JPanel divseqParameterPanel = new JPanel();
  JPanel quaseqParameterPanel = new JPanel();
  JPanel divdivParameterPanel = new JPanel();

  JPanel seqseqShiftPanel = new JPanel();
  JPanel divseqShiftPanel = new JPanel();
  JPanel quaseqShiftPanel = new JPanel();
  JPanel divdivShiftPanel = new JPanel();

  JPanel[] samplePanel = new JPanel[3];
  JPanel[] sample = new JPanel[3];
  Datacontrol[] horizontalClass = new Datacontrol[3];
  Datacontrol[] verticalClass = new Datacontrol[3];
  JCheckBox[] checkbox = new JCheckBox[3];
  ButtonGroup checkboxgroup = new ButtonGroup();

  JRadioButton seqseqDiamondButton = new JRadioButton("Diamond");
  ButtonGroup seqseqButtongroup = new ButtonGroup();
  RangeControl seqseqDiamondLightnessRange = new RangeControl();
  Datacontrol seqseqDiamond_TopAngle = new Datacontrol();
  Datacontrol seqseqDiamond_Shift_r = new Datacontrol();
  Datacontrol seqseqDiamond_Shift_p = new Datacontrol();
  Datacontrol seqseqDiamond_Shift_h = new Datacontrol();
  JButton seqseqDiamondRecommended = new JButton();
  JButton seqseqDiamondFigure = new JButton();

  JRadioButton seqseqTiltedDiamondButton = new JRadioButton("Tilted Diamond");
  RangeControl seqseqTiltedDiamondLightnessRange = new RangeControl();
  Datacontrol seqseqTiltedDiamond_TopAngle = new Datacontrol();
  Datacontrol seqseqTiltedDiamond_TiltAngle = new Datacontrol();
  Datacontrol seqseqTiltedDiamond_Shift_r = new Datacontrol();
  Datacontrol seqseqTiltedDiamond_Shift_p = new Datacontrol();
  Datacontrol seqseqTiltedDiamond_Shift_h = new Datacontrol();
  JButton seqseqTiltedDiamondRecommended = new JButton();
  JButton seqseqTiltedDiamondFigure = new JButton();

  JRadioButton seqseqFoldedDiamondButton = new JRadioButton("Folded Diamond");
  RangeControl seqseqFoldedDiamondLightnessRange = new RangeControl();
  Datacontrol seqseqFoldedDiamond_Angle = new Datacontrol();
  Datacontrol seqseqFoldedDiamond_Leftwing = new Datacontrol();
  Datacontrol seqseqFoldedDiamond_Rightwing = new Datacontrol();
  Datacontrol seqseqFoldedDiamond_Shift_r = new Datacontrol();
  Datacontrol seqseqFoldedDiamond_Shift_p = new Datacontrol();
  Datacontrol seqseqFoldedDiamond_Shift_h = new Datacontrol();
  JButton seqseqFoldedDiamondRecommended = new JButton();
  JButton seqseqFoldedDiamondFigure = new JButton();

  JRadioButton divseqWedgeButton = new JRadioButton("Wedge");
  ButtonGroup divseqButtongroup = new ButtonGroup();
  RangeControl divseqWedgeLightnessRange = new RangeControl();
  Datacontrol divseqWedge_Alpha = new Datacontrol();
  Datacontrol divseqWedge_Beta = new Datacontrol();
  Datacontrol divseqWedge_Range = new Datacontrol();
  Datacontrol divseqWedge_Shift_r = new Datacontrol();
  Datacontrol divseqWedge_Shift_p = new Datacontrol();
  Datacontrol divseqWedge_Shift_h = new Datacontrol();
  JButton divseqWedgeRecommended = new JButton();
  JButton divseqWedgeFigure = new JButton();

  JRadioButton divseqEllipseButton = new JRadioButton("Ellipse");
  RangeControl divseqEllipseLightnessRange = new RangeControl();
  Datacontrol divseqEllipse_Alpha = new Datacontrol();
  Datacontrol divseqEllipse_E = new Datacontrol();
  Datacontrol divseqEllipse_Shift_r = new Datacontrol();
  Datacontrol divseqEllipse_Shift_p = new Datacontrol();
  Datacontrol divseqEllipse_Shift_h = new Datacontrol();
  JButton divseqEllipseRecommended = new JButton();
  JButton divseqEllipseFigure = new JButton();

  JRadioButton divseqTrapezoidButton = new JRadioButton("Trapezoid");
  RangeControl divseqTrapezoidLightnessRange = new RangeControl();
  Datacontrol divseqTrapezoid_Alpha = new Datacontrol();
  Datacontrol divseqTrapezoid_Radius = new Datacontrol();
  Datacontrol divseqTrapezoid_Shift_r = new Datacontrol();
  Datacontrol divseqTrapezoid_Shift_p = new Datacontrol();
  Datacontrol divseqTrapezoid_Shift_h = new Datacontrol();
  JButton divseqTrapezoidRecommended = new JButton();
  JButton divseqTrapezoidFigure = new JButton();

  JRadioButton quaseqConeButton = new JRadioButton("Cone");
  ButtonGroup quaseqButtongroup = new ButtonGroup();
  RangeControl quaseqConeLightnessRange = new RangeControl();
  Datacontrol quaseqCone_Height = new Datacontrol();
  Datacontrol quaseqCone_Radius = new Datacontrol();
  Datacontrol quaseqCone_Shift_h = new Datacontrol();
  Datacontrol quaseqCone_Shift_r = new Datacontrol();
  Datacontrol quaseqCone_Shift_p = new Datacontrol();
  JButton quaseqConeRecommended = new JButton();
  JButton quaseqConeFigure = new JButton();

  JRadioButton quaseqEllipsoidButton = new JRadioButton("Ellipsoid");
  RangeControl quaseqEllipsoidLightnessRange = new RangeControl();
  Datacontrol quaseqEllipsoid_Semiaxis_ab = new Datacontrol();
  Datacontrol quaseqEllipsoid_Semiaxis_c = new Datacontrol();
  Datacontrol quaseqEllipsoid_Shift_h = new Datacontrol();
  Datacontrol quaseqEllipsoid_Shift_r = new Datacontrol();
  Datacontrol quaseqEllipsoid_Shift_p = new Datacontrol();
  JButton quaseqEllipsoidRecommended = new JButton();
  JButton quaseqEllipsoidFigure = new JButton();

  JRadioButton quaseqEllipseCurveButton = new JRadioButton("Elipse Curve");
  RangeControl quaseqEllipseCurveLightnessRange = new RangeControl();
  Datacontrol quaseqEllipseCurve_Semiaxis_a = new Datacontrol();
  Datacontrol quaseqEllipseCurve_Semiaxis_b = new Datacontrol();
  Datacontrol quaseqEllipseCurve_Shift_h = new Datacontrol();
  Datacontrol quaseqEllipseCurve_Shift_r = new Datacontrol();
  Datacontrol quaseqEllipseCurve_Shift_p = new Datacontrol();
  JButton quaseqEllipseCurveRecommended = new JButton();
  JButton quaseqEllipseCurveFigure = new JButton();

  JRadioButton quaseqBellshapeButton = new JRadioButton("Bell Shape");
  RangeControl quaseqBellshapeLightnessRange = new RangeControl();
  Datacontrol quaseqBellshape_Vertex = new Datacontrol();
  Datacontrol quaseqBellshape_Divisor = new Datacontrol();
  Datacontrol quaseqBellshape_Shift_h = new Datacontrol();
  Datacontrol quaseqBellshape_Shift_r = new Datacontrol();
  Datacontrol quaseqBellshape_Shift_p = new Datacontrol();
  JButton quaseqBellshapeRecommended = new JButton();
  JButton quaseqBellshapeCurveFigure = new JButton();

  JRadioButton divdivConeButton = new JRadioButton("Cone");
  ButtonGroup divdivButtongroup = new ButtonGroup();
  Datacontrol divdivCone_Range = new Datacontrol();
  Datacontrol divdivCone_Height = new Datacontrol();
  Datacontrol divdivCone_Radius = new Datacontrol();
  Datacontrol divdivCone_Shift_h = new Datacontrol();
  Datacontrol divdivCone_Shift_r = new Datacontrol();
  Datacontrol divdivCone_Shift_p = new Datacontrol();
  JButton divdivConeRecommended = new JButton();
  JButton divdivConeFigure = new JButton();

  JRadioButton divdivEllipsoidButton = new JRadioButton("Ellipsoid");
  Datacontrol divdivEllipsoid_Range = new Datacontrol();
  Datacontrol divdivEllipsoid_Semiaxis_ab = new Datacontrol();
  Datacontrol divdivEllipsoid_Semiaxis_c = new Datacontrol();
  Datacontrol divdivEllipsoid_Shift_h = new Datacontrol();
  Datacontrol divdivEllipsoid_Shift_r = new Datacontrol();
  Datacontrol divdivEllipsoid_Shift_p = new Datacontrol();
  JButton divdivEllipsoidRecommended = new JButton();
  JButton divdivEllipsoidFigure = new JButton();

  JRadioButton divdivEllipseCurveButton = new JRadioButton("Elipse Curve");
  Datacontrol divdivEllipseCurve_Range = new Datacontrol();
  Datacontrol divdivEllipseCurve_Semiaxis_a = new Datacontrol();
  Datacontrol divdivEllipseCurve_Semiaxis_b = new Datacontrol();
  Datacontrol divdivEllipseCurve_Shift_h = new Datacontrol();
  Datacontrol divdivEllipseCurve_Shift_r = new Datacontrol();
  Datacontrol divdivEllipseCurve_Shift_p = new Datacontrol();
  JButton divdivEllipseCurveRecommended = new JButton();
  JButton divdivEllipseCurveFigure = new JButton();

  JRadioButton divdivBellshapeButton = new JRadioButton("Bell Shape");
  Datacontrol divdivBellshape_Range = new Datacontrol();
  Datacontrol divdivBellshape_Vertex = new Datacontrol();
  Datacontrol divdivBellshape_Divisor = new Datacontrol();
  Datacontrol divdivBellshape_Shift_h = new Datacontrol();
  Datacontrol divdivBellshape_Shift_r = new Datacontrol();
  Datacontrol divdivBellshape_Shift_p = new Datacontrol();
  JButton divdivBellshapeRecommended = new JButton();
  JButton divdivBellshapeFigure = new JButton();


  public CIELUVDesignBoard() {



    this.tabbedPane.setPreferredSize(new Dimension(700, 220));
    this.tabbedPane.addTab("Sequential-Sequential", seqseqPanel);
    this.tabbedPane.addTab("Diverging-Sequential", divseqPanel);
    this.tabbedPane.addTab("Qualitative-Sequential", quaseqPanel);
    this.tabbedPane.addTab("Diverging-Diverging", divdivPanel);
    this.tabbedPane.addChangeListener(this);

    this.setPreferredSize(new Dimension(780, 480));
    this.setLayout(new BorderLayout());
    this.add(this.northPanel, BorderLayout.NORTH);
    this.add(this.southPanel, BorderLayout.SOUTH);

    this.northPanel.setLayout(new BorderLayout());
    this.northPanel.add(this.tabbedPane, BorderLayout.WEST);
    this.northPanel.add(this.saveLoad, BorderLayout.EAST);

    this.saveLoad.setPreferredSize(new Dimension(70, 220));
    this.saveLoad.setLayout(new GridLayout(2, 1));
    this.saveLoad.add(this.saveButton);
    this.saveLoad.add(this.loadButton);
    this.saveButton.addActionListener(this);
    this.loadButton.addActionListener(this);

    this.seqseqPanel.add(this.seqseqGeometricObjectPanel);
    this.seqseqGeometricObjectPanel.setBorder(BorderFactory.createTitledBorder("Geometric Objects"));
    this.seqseqGeometricObjectPanel.setPreferredSize(new Dimension(120, 180));

    this.seqseqPanel.add(this.seqseqParameterPanel);
    this.seqseqParameterPanel.setBorder(BorderFactory.createTitledBorder("Object Parameters"));
    this.seqseqParameterPanel.setPreferredSize(new Dimension(370, 180));

    this.seqseqPanel.add(this.seqseqShiftPanel);
    this.seqseqShiftPanel.setBorder(BorderFactory.createTitledBorder("Coordinate Shifts"));
    this.seqseqShiftPanel.setPreferredSize(new Dimension(190, 180));

    this.seqseqGeometricObjectPanel.setLayout(new GridLayout(3, 1));
    this.seqseqGeometricObjectPanel.add(this.seqseqDiamondButton);
    this.seqseqGeometricObjectPanel.add(this.seqseqTiltedDiamondButton);
    this.seqseqGeometricObjectPanel.add(this.seqseqFoldedDiamondButton);
    this.seqseqButtongroup.add(this.seqseqDiamondButton);
    this.seqseqButtongroup.add(this.seqseqTiltedDiamondButton);
    this.seqseqButtongroup.add(this.seqseqFoldedDiamondButton);
    this.seqseqDiamondButton.addActionListener(this);
    this.seqseqTiltedDiamondButton.addActionListener(this);
    this.seqseqFoldedDiamondButton.addActionListener(this);


    this.divseqPanel.add(this.divseqGeometricObjectPanel);
    this.divseqGeometricObjectPanel.setBorder(BorderFactory.createTitledBorder("Geometric Objects"));
    this.divseqGeometricObjectPanel.setPreferredSize(new Dimension(120, 180));

    this.divseqPanel.add(this.divseqParameterPanel);
    this.divseqParameterPanel.setBorder(BorderFactory.createTitledBorder("Object Parameters"));
    this.divseqParameterPanel.setPreferredSize(new Dimension(370, 180));

    this.divseqPanel.add(this.divseqShiftPanel);
    this.divseqShiftPanel.setBorder(BorderFactory.createTitledBorder("Coordinate Shifts"));
    this.divseqShiftPanel.setPreferredSize(new Dimension(180, 180));

    this.divseqGeometricObjectPanel.setLayout(new GridLayout(3, 1));
    this.divseqGeometricObjectPanel.add(this.divseqWedgeButton);
    this.divseqGeometricObjectPanel.add(this.divseqEllipseButton);
    this.divseqGeometricObjectPanel.add(this.divseqTrapezoidButton);
    this.divseqButtongroup.add(this.divseqWedgeButton);
    this.divseqButtongroup.add(this.divseqEllipseButton);
    this.divseqButtongroup.add(this.divseqTrapezoidButton);
    this.divseqWedgeButton.addActionListener(this);
    this.divseqEllipseButton.addActionListener(this);
    this.divseqTrapezoidButton.addActionListener(this);


    this.quaseqPanel.add(this.quaseqGeometricObjectPanel);
    this.quaseqGeometricObjectPanel.setBorder(BorderFactory.createTitledBorder("Geometric Objects"));
    this.quaseqGeometricObjectPanel.setPreferredSize(new Dimension(120, 180));

    this.quaseqPanel.add(this.quaseqParameterPanel);
    this.quaseqParameterPanel.setBorder(BorderFactory.createTitledBorder("Object Parameters"));
    this.quaseqParameterPanel.setPreferredSize(new Dimension(370, 180));

    this.quaseqPanel.add(this.quaseqShiftPanel);
    this.quaseqShiftPanel.setBorder(BorderFactory.createTitledBorder("Coordinate Shifts"));
    this.quaseqShiftPanel.setPreferredSize(new Dimension(180, 180));

    this.quaseqGeometricObjectPanel.setLayout(new GridLayout(4, 1));
    this.quaseqGeometricObjectPanel.add(this.quaseqConeButton);
    this.quaseqGeometricObjectPanel.add(this.quaseqEllipsoidButton);
    this.quaseqGeometricObjectPanel.add(this.quaseqEllipseCurveButton);
    this.quaseqGeometricObjectPanel.add(this.quaseqBellshapeButton);
    this.quaseqButtongroup.add(this.quaseqConeButton);
    this.quaseqButtongroup.add(this.quaseqEllipsoidButton);
    this.quaseqButtongroup.add(this.quaseqEllipseCurveButton);
    this.quaseqButtongroup.add(this.quaseqBellshapeButton);
    this.quaseqConeButton.addActionListener(this);
    this.quaseqEllipsoidButton.addActionListener(this);
    this.quaseqEllipseCurveButton.addActionListener(this);
    this.quaseqBellshapeButton.addActionListener(this);


    this.divdivPanel.add(this.divdivGeometricObjectPanel);
    this.divdivGeometricObjectPanel.setBorder(BorderFactory.createTitledBorder("Geometric Objects"));
    this.divdivGeometricObjectPanel.setPreferredSize(new Dimension(120, 180));

    this.divdivPanel.add(this.divdivParameterPanel);
    this.divdivParameterPanel.setBorder(BorderFactory.createTitledBorder("Object Parameters"));
    this.divdivParameterPanel.setPreferredSize(new Dimension(370, 180));

    this.divdivPanel.add(this.divdivShiftPanel);
    this.divdivShiftPanel.setBorder(BorderFactory.createTitledBorder("Coordinate Shifts"));
    this.divdivShiftPanel.setPreferredSize(new Dimension(180, 180));

    this.divdivGeometricObjectPanel.setLayout(new GridLayout(4, 1));
    this.divdivGeometricObjectPanel.add(this.divdivConeButton);
    this.divdivGeometricObjectPanel.add(this.divdivEllipsoidButton);
    this.divdivGeometricObjectPanel.add(this.divdivEllipseCurveButton);
    this.divdivGeometricObjectPanel.add(this.divdivBellshapeButton);
    this.divdivButtongroup.add(this.divdivConeButton);
    this.divdivButtongroup.add(this.divdivEllipsoidButton);
    this.divdivButtongroup.add(this.divdivEllipseCurveButton);
    this.divdivButtongroup.add(this.divdivBellshapeButton);
    this.divdivConeButton.addActionListener(this);
    this.divdivEllipsoidButton.addActionListener(this);
    this.divdivEllipseCurveButton.addActionListener(this);
    this.divdivBellshapeButton.addActionListener(this);


    this.southPanel.setPreferredSize(new Dimension(700, 230));
    this.southPanel.setLayout(new GridLayout(1, 3));
    for(int i = 0; i < 3; i ++){
      this.samplePanel[i] = new JPanel();
      this.sample[i] = new JPanel();
      this.horizontalClass[i] = new Datacontrol();
      this.verticalClass[i] = new Datacontrol();
      this.checkbox[i] = new JCheckBox();

      this.sample[i].setPreferredSize(new Dimension(200, 200));

      this.horizontalClass[i].setBorder(BorderFactory.createEtchedBorder());
      this.verticalClass[i].setBorder(BorderFactory.createEtchedBorder());
      this.verticalClass[i].slider1.setOrientation(1);
      this.verticalClass[i].add(this.verticalClass[i].slider1, BorderLayout.SOUTH);
      this.verticalClass[i].add(this.verticalClass[i].spinner1, BorderLayout.NORTH);
      this.verticalClass[i].slider1.setPreferredSize(new Dimension(20, 120));
      this.verticalClass[i].spinner1.setPreferredSize(new Dimension(40, 20));
      this.horizontalClass[i].slider1.setPreferredSize(new Dimension(120, 20));
      this.horizontalClass[i].slider1.setMaximum(15);
      this.horizontalClass[i].slider1.setMinimum(2);
      this.horizontalClass[i].slider1.setValue(5);
      SpinnerNumberModel horizontal = new SpinnerNumberModel(5, 2, 15, 1);
      this.horizontalClass[i].spinner1.setModel(horizontal);
      this.horizontalClass[i].spinner1.addChangeListener(this);
      this.verticalClass[i].slider1.setMaximum(15);
      this.verticalClass[i].slider1.setMinimum(2);
      this.verticalClass[i].slider1.setValue(5);
      SpinnerNumberModel vertical = new SpinnerNumberModel(5, 2, 15, 1);
      this.verticalClass[i].spinner1.setModel(vertical);
      this.verticalClass[i].spinner1.addChangeListener(this);


      this.checkboxgroup.add(this.checkbox[i]);

      this.southPanel.add(this.samplePanel[i]);
      this.samplePanel[i].setLayout(new FlowLayout());
      this.samplePanel[i].add(this.horizontalClass[i]);
      this.samplePanel[i].add(this.checkbox[i]);
      this.samplePanel[i].add(this.sample[i]);
      this.samplePanel[i].add(this.verticalClass[i]);

    }

    this.f.setBounds(this.getX() + 620, this.getY(), 600, 600);


  }

  public void actionPerformed(ActionEvent e) {

    //SeqseqDiamond model
    if(e.getSource() == this.seqseqDiamondButton){
      if(this.seqseqDiamondButton.isSelected() == true){
        this.seqseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.seqseqDiamond1 = new SeqseqDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.maxlightness, this.seqseqDiamond1.minlightness, this.seqseqDiamond1.top_angle, this.seqseqDiamond1.shift_h, this.seqseqDiamond1.shift_r, this.seqseqDiamond1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.luvcolor);
        this.seqseqDiamondLightnessRange.multiSlider1.setValueAt(0, this.seqseqDiamond1.minlightness);
        this.seqseqDiamondLightnessRange.multiSlider1.setValueAt(1, this.seqseqDiamond1.maxlightness);
        this.seqseqDiamond_TopAngle.slider1.setValue(this.seqseqDiamond1.top_angle);
        this.seqseqDiamond_Shift_h.slider1.setValue(this.seqseqDiamond1.shift_h);
        this.seqseqDiamond_Shift_r.slider1.setValue(this.seqseqDiamond1.shift_r);
        this.seqseqDiamond_Shift_p.slider1.setValue(this.seqseqDiamond1.shift_p);

        this.seqseqParameterPanel.add(this.seqseqDiamondLightnessRange);
        this.seqseqDiamondLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel seqseqDiamondLightnessRange_left = new SpinnerNumberModel(this.seqseqDiamond1.minlightness, 0, 100, 1);
        this.seqseqDiamondLightnessRange.leftSpinner.setModel(seqseqDiamondLightnessRange_left);
        this.seqseqDiamondLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel seqseqDiamondLightnessRange_right = new SpinnerNumberModel(this.seqseqDiamond1.maxlightness, 0, 100, 1);
        this.seqseqDiamondLightnessRange.rightSpinner.setModel(seqseqDiamondLightnessRange_right);
        this.seqseqDiamondLightnessRange.rightSpinner.addChangeListener(this);
        this.seqseqDiamondLightnessRange.multiSlider1.setValueAt(0, this.seqseqDiamond1.minlightness);
        this.seqseqDiamondLightnessRange.multiSlider1.setValueAt(1, this.seqseqDiamond1.maxlightness);
        this.seqseqDiamondLightnessRange.multiSlider1.setMaximum(100);
        this.seqseqDiamondLightnessRange.multiSlider1.setMinimum(0);

        this.seqseqParameterPanel.add(this.seqseqDiamond_TopAngle);
        this.seqseqDiamond_TopAngle.setTitle("Top Angle: 90~160 degrees");
        this.seqseqDiamond_TopAngle.slider1.setPreferredSize(new Dimension(110, 20));
        this.seqseqDiamond_TopAngle.slider1.setMaximum(160);
        this.seqseqDiamond_TopAngle.slider1.setMinimum(90);
        this.seqseqDiamond_TopAngle.slider1.setValue(this.seqseqDiamond1.top_angle);
        SpinnerNumberModel seqseqDiamond_TopAngle = new SpinnerNumberModel(this.seqseqDiamond1.top_angle, 90, 160, 1);
        this.seqseqDiamond_TopAngle.spinner1.setModel(seqseqDiamond_TopAngle);
        this.seqseqDiamond_TopAngle.spinner1.addChangeListener(this);

        this.seqseqParameterPanel.add(this.seqseqDiamondRecommended);
        this.seqseqDiamondRecommended.setPreferredSize(new Dimension(120, 30));
        this.seqseqDiamondRecommended.setText("Recommended");
        this.seqseqDiamondRecommended.addActionListener(this);

        this.seqseqParameterPanel.add(this.seqseqDiamondFigure);
        this.seqseqDiamondFigure.setPreferredSize(new Dimension(50, 30));
        this.seqseqDiamondFigure.setText("Fig");
        this.seqseqDiamondFigure.addActionListener(this);
        this.seqseqDiamondFigureButton.addActionListener(this);

        this.seqseqShiftPanel.removeAll();
        this.seqseqShiftPanel.add(this.seqseqDiamond_Shift_h);
        this.seqseqDiamond_Shift_h.setTitle("Vertical Shift: -20~20");
        this.seqseqDiamond_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqDiamond_Shift_h.slider1.setMaximum(20);
        this.seqseqDiamond_Shift_h.slider1.setMinimum(-20);
        this.seqseqDiamond_Shift_h.slider1.setValue(this.seqseqDiamond1.shift_h);
        SpinnerNumberModel seqseqDiamond_Shift_h = new SpinnerNumberModel(this.seqseqDiamond1.shift_h, -20, 20, 1);
        this.seqseqDiamond_Shift_h.spinner1.setModel(seqseqDiamond_Shift_h);
        this.seqseqDiamond_Shift_h.spinner1.addChangeListener(this);

        this.seqseqShiftPanel.add(this.seqseqDiamond_Shift_r);
        this.seqseqDiamond_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.seqseqDiamond_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqDiamond_Shift_r.slider1.setMaximum(50);
        this.seqseqDiamond_Shift_r.slider1.setMinimum(0);
        this.seqseqDiamond_Shift_r.slider1.setValue(this.seqseqDiamond1.shift_r);
        SpinnerNumberModel seqseqDiamond_Shift_r = new SpinnerNumberModel(this.seqseqDiamond1.shift_r, 0, 50, 1);
        this.seqseqDiamond_Shift_r.spinner1.setModel(seqseqDiamond_Shift_r);
        this.seqseqDiamond_Shift_r.spinner1.addChangeListener(this);

        this.seqseqShiftPanel.add(this.seqseqDiamond_Shift_p);
        this.seqseqDiamond_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.seqseqDiamond_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqDiamond_Shift_p.slider1.setMaximum(360);
        this.seqseqDiamond_Shift_p.slider1.setMinimum(0);
        this.seqseqDiamond_Shift_p.slider1.setValue(this.seqseqDiamond1.shift_p);
        SpinnerNumberModel seqseqDiamond_Shift_p = new SpinnerNumberModel(this.seqseqDiamond1.shift_p, 0, 360, 1);
        this.seqseqDiamond_Shift_p.spinner1.setModel(seqseqDiamond_Shift_p);
        this.seqseqDiamond_Shift_p.spinner1.addChangeListener(this);
      }
    }

    if(e.getSource() == this.seqseqDiamondRecommended){
      this.seqseqDiamondLightnessRange.multiSlider1.setValueAt(0, 20);
      this.seqseqDiamondLightnessRange.multiSlider1.setValueAt(1, 95);
      this.seqseqDiamond_TopAngle.slider1.setValue(120);
      this.seqseqDiamond_Shift_h.slider1.setValue(0);
      this.seqseqDiamond_Shift_r.slider1.setValue(0);
      this.seqseqDiamond_Shift_p.slider1.setValue(0);

      this.seqseqDiamond1.minlightness = 20;
      this.seqseqDiamond1.maxlightness = 95;
      this.seqseqDiamond1.top_angle = 120;
      this.seqseqDiamond1.shift_h = 0;
      this.seqseqDiamond1.shift_r = 0;
      this.seqseqDiamond1.shift_p = 0;

      this.seqseqDiamond1 = new SeqseqDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 20, 120, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.luvcolor);

      logger.finest("seqseqDiamondRecommended");
      /*
      this.seqseqDiamond1.minlightness = Integer.valueOf(String.valueOf(this.seqseqDiamondLightnessRange.leftSpinner.getValue())).intValue();
      this.seqseqDiamond1.maxlightness = Integer.valueOf(String.valueOf(this.seqseqDiamondLightnessRange.rightSpinner.getValue())).intValue();
      this.seqseqDiamond1.top_angle = Integer.valueOf(String.valueOf(this.seqseqDiamond_TopAngle.spinner1.getValue())).intValue();
      this.seqseqDiamond1.shift_h = Integer.valueOf(String.valueOf(this.seqseqDiamond_Shift_h.spinner1.getValue())).intValue();
      this.seqseqDiamond1.shift_r = Integer.valueOf(String.valueOf(this.seqseqDiamond_Shift_r.spinner1.getValue())).intValue();
      this.seqseqDiamond1.shift_p = Integer.valueOf(String.valueOf(this.seqseqDiamond_Shift_p.spinner1.getValue())).intValue();
      this.seqseqDiamond1 = new SeqseqDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.maxlightness, this.seqseqDiamond1.minlightness, this.seqseqDiamond1.top_angle, this.seqseqDiamond1.shift_h, this.seqseqDiamond1.shift_r, this.seqseqDiamond1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.labcolor);
    */
    }

    if(e.getSource() == this.seqseqDiamondFigure){
      this.f.setVisible(true);
      this.seqseqDiamondFigureButton.setPreferredSize(new Dimension(500, 500));
      this.f.getContentPane().setLayout(new FlowLayout());
      this.f.getContentPane().add(this.seqseqDiamondFigureButton);

      this.f.setTitle("Sequential-sequential Diamond");
      this.f.repaint();

    }


    //SeqseqTiltedDiamond model
    if(e.getSource() == this.seqseqTiltedDiamondButton){
      if(this.seqseqTiltedDiamondButton.isSelected() == true){
        this.seqseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.seqseqTiltedDiamond1 = new SeqseqTiltedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.maxlightness, this.seqseqTiltedDiamond1.minlightness, this.seqseqTiltedDiamond1.top_angle, this.seqseqTiltedDiamond1.tilt_angle, this.seqseqTiltedDiamond1.shift_h, this.seqseqTiltedDiamond1.shift_r, this.seqseqTiltedDiamond1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.luvcolor);
        this.seqseqTiltedDiamondLightnessRange.multiSlider1.setValueAt(0, this.seqseqTiltedDiamond1.minlightness);
        this.seqseqTiltedDiamondLightnessRange.multiSlider1.setValueAt(1, this.seqseqTiltedDiamond1.maxlightness);
        this.seqseqTiltedDiamond_TopAngle.slider1.setValue(this.seqseqTiltedDiamond1.top_angle);
        this.seqseqTiltedDiamond_TiltAngle.slider1.setValue(this.seqseqTiltedDiamond1.tilt_angle);
        this.seqseqTiltedDiamond_Shift_h.slider1.setValue(this.seqseqTiltedDiamond1.shift_h);
        this.seqseqTiltedDiamond_Shift_r.slider1.setValue(this.seqseqTiltedDiamond1.shift_r);
        this.seqseqTiltedDiamond_Shift_p.slider1.setValue(this.seqseqTiltedDiamond1.shift_p);

        this.seqseqParameterPanel.add(this.seqseqTiltedDiamondLightnessRange);
        this.seqseqTiltedDiamondLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel seqseqTiltedDiamondLightnessRange_left = new SpinnerNumberModel(this.seqseqTiltedDiamond1.minlightness, 0, 100, 1);
        this.seqseqTiltedDiamondLightnessRange.leftSpinner.setModel(seqseqTiltedDiamondLightnessRange_left);
        this.seqseqTiltedDiamondLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel seqseqTiltedDiamondLightnessRange_right = new SpinnerNumberModel(this.seqseqTiltedDiamond1.maxlightness, 0, 100, 1);
        this.seqseqTiltedDiamondLightnessRange.rightSpinner.setModel(seqseqTiltedDiamondLightnessRange_right);
        this.seqseqTiltedDiamondLightnessRange.rightSpinner.addChangeListener(this);
        this.seqseqTiltedDiamondLightnessRange.multiSlider1.setValueAt(0, this.seqseqTiltedDiamond1.minlightness);
        this.seqseqTiltedDiamondLightnessRange.multiSlider1.setValueAt(1, this.seqseqTiltedDiamond1.maxlightness);
        this.seqseqTiltedDiamondLightnessRange.multiSlider1.setMaximum(100);
        this.seqseqTiltedDiamondLightnessRange.multiSlider1.setMinimum(0);

        this.seqseqParameterPanel.add(this.seqseqTiltedDiamond_TopAngle);
        this.seqseqTiltedDiamond_TopAngle.setTitle("Top Angle: 90~160 degrees");
        this.seqseqTiltedDiamond_TopAngle.slider1.setPreferredSize(new Dimension(110, 20));
        this.seqseqTiltedDiamond_TopAngle.slider1.setMaximum(160);
        this.seqseqTiltedDiamond_TopAngle.slider1.setMinimum(90);
        this.seqseqTiltedDiamond_TopAngle.slider1.setValue(this.seqseqTiltedDiamond1.top_angle);
        SpinnerNumberModel seqseqTiltedDiamond_TopAngle = new SpinnerNumberModel(this.seqseqTiltedDiamond1.top_angle, 0, 160, 1);
        this.seqseqTiltedDiamond_TopAngle.spinner1.setModel(seqseqTiltedDiamond_TopAngle);
        this.seqseqTiltedDiamond_TopAngle.spinner1.addChangeListener(this);

        this.seqseqParameterPanel.add(this.seqseqTiltedDiamond_TiltAngle);
        this.seqseqTiltedDiamond_TiltAngle.setTitle("Tilt Angle: 0~45 degrees");
        this.seqseqTiltedDiamond_TiltAngle.slider1.setPreferredSize(new Dimension(110, 20));
        this.seqseqTiltedDiamond_TiltAngle.slider1.setMaximum(45);
        this.seqseqTiltedDiamond_TiltAngle.slider1.setMinimum(0);
        this.seqseqTiltedDiamond_TiltAngle.slider1.setValue(this.seqseqTiltedDiamond1.tilt_angle);
        SpinnerNumberModel seqseqTiltedDiamond_TiltAngle = new SpinnerNumberModel(this.seqseqTiltedDiamond1.tilt_angle, 0, 45, 1);
        this.seqseqTiltedDiamond_TiltAngle.spinner1.setModel(seqseqTiltedDiamond_TiltAngle);
        this.seqseqTiltedDiamond_TiltAngle.spinner1.addChangeListener(this);

        this.seqseqParameterPanel.add(this.seqseqTiltedDiamondRecommended);
        this.seqseqTiltedDiamondRecommended.setPreferredSize(new Dimension(120, 30));
        this.seqseqTiltedDiamondRecommended.setText("Recommended");
        this.seqseqTiltedDiamondRecommended.addActionListener(this);

        this.seqseqParameterPanel.add(this.seqseqTiltedDiamondFigure);
        this.seqseqTiltedDiamondFigure.setPreferredSize(new Dimension(50, 30));
        this.seqseqTiltedDiamondFigure.setText("Fig");
        this.seqseqTiltedDiamondFigure.addActionListener(this);


        this.seqseqShiftPanel.removeAll();
        this.seqseqShiftPanel.add(this.seqseqTiltedDiamond_Shift_h);
        this.seqseqTiltedDiamond_Shift_h.setTitle("Vertical Shift: -20~20");
        this.seqseqTiltedDiamond_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqTiltedDiamond_Shift_h.slider1.setMaximum(20);
        this.seqseqTiltedDiamond_Shift_h.slider1.setMinimum(-20);
        this.seqseqTiltedDiamond_Shift_h.slider1.setValue(this.seqseqTiltedDiamond1.shift_h);
        SpinnerNumberModel seqseqTiltedDiamond_Shift_h = new SpinnerNumberModel(this.seqseqTiltedDiamond1.shift_h, -20, 20, 1);
        this.seqseqTiltedDiamond_Shift_h.spinner1.setModel(seqseqTiltedDiamond_Shift_h);
        this.seqseqTiltedDiamond_Shift_h.spinner1.addChangeListener(this);

        this.seqseqShiftPanel.add(this.seqseqTiltedDiamond_Shift_r);
        this.seqseqTiltedDiamond_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.seqseqTiltedDiamond_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqTiltedDiamond_Shift_r.slider1.setMaximum(50);
        this.seqseqTiltedDiamond_Shift_r.slider1.setMinimum(0);
        this.seqseqTiltedDiamond_Shift_r.slider1.setValue(this.seqseqTiltedDiamond1.shift_r);
        SpinnerNumberModel seqseqTiltedDiamond_Shift_r = new SpinnerNumberModel(this.seqseqTiltedDiamond1.shift_r, 0, 50, 1);
        this.seqseqTiltedDiamond_Shift_r.spinner1.setModel(seqseqTiltedDiamond_Shift_r);
        this.seqseqTiltedDiamond_Shift_r.spinner1.addChangeListener(this);

        this.seqseqShiftPanel.add(this.seqseqTiltedDiamond_Shift_p);
        this.seqseqTiltedDiamond_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.seqseqTiltedDiamond_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqTiltedDiamond_Shift_p.slider1.setMaximum(360);
        this.seqseqTiltedDiamond_Shift_p.slider1.setMinimum(0);
        this.seqseqTiltedDiamond_Shift_p.slider1.setValue(this.seqseqTiltedDiamond1.shift_p);
        SpinnerNumberModel seqseqTiltedDiamond_Shift_p = new SpinnerNumberModel(this.seqseqTiltedDiamond1.shift_p, 0, 360, 1);
        this.seqseqTiltedDiamond_Shift_p.spinner1.setModel(seqseqTiltedDiamond_Shift_p);
        this.seqseqTiltedDiamond_Shift_p.spinner1.addChangeListener(this);
      }
    }

    if(e.getSource() == this.seqseqTiltedDiamondRecommended){
      this.seqseqTiltedDiamond1 = new SeqseqTiltedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 20, 120, 15, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.luvcolor);
      this.seqseqTiltedDiamondLightnessRange.multiSlider1.setValueAt(0, 20);
      this.seqseqTiltedDiamondLightnessRange.multiSlider1.setValueAt(1, 95);
      this.seqseqTiltedDiamond_TopAngle.slider1.setValue(120);
      this.seqseqTiltedDiamond_TiltAngle.slider1.setValue(15);
      this.seqseqTiltedDiamond_Shift_h.slider1.setValue(0);
      this.seqseqTiltedDiamond_Shift_r.slider1.setValue(0);
      this.seqseqTiltedDiamond_Shift_p.slider1.setValue(0);
    }

    //SeqseqFoldedDiamond model
    if(e.getSource() == this.seqseqFoldedDiamondButton){
      if(this.seqseqFoldedDiamondButton.isSelected() == true){
        this.seqseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.maxlightness, this.seqseqFoldedDiamond1.minlightness, this.seqseqFoldedDiamond1.angle, this.seqseqFoldedDiamond1.leftwing_position, this.seqseqFoldedDiamond1.rightwing_position, this.seqseqFoldedDiamond1.shift_h, this.seqseqFoldedDiamond1.shift_r, this.seqseqFoldedDiamond1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.luvcolor);
        this.seqseqFoldedDiamondLightnessRange.multiSlider1.setValueAt(0, this.seqseqFoldedDiamond1.minlightness);
        this.seqseqFoldedDiamondLightnessRange.multiSlider1.setValueAt(1, this.seqseqFoldedDiamond1.maxlightness);
        this.seqseqFoldedDiamond_Angle.slider1.setValue(this.seqseqFoldedDiamond1.angle);
        this.seqseqFoldedDiamond_Leftwing.slider1.setValue(this.seqseqFoldedDiamond1.leftwing_position);
        this.seqseqFoldedDiamond_Rightwing.slider1.setValue(this.seqseqFoldedDiamond1.rightwing_position);
        this.seqseqFoldedDiamond_Shift_h.slider1.setValue(this.seqseqFoldedDiamond1.shift_h);
        this.seqseqFoldedDiamond_Shift_r.slider1.setValue(this.seqseqFoldedDiamond1.shift_r);
        this.seqseqFoldedDiamond_Shift_p.slider1.setValue(this.seqseqFoldedDiamond1.shift_p);

        this.seqseqParameterPanel.add(this.seqseqFoldedDiamondLightnessRange);
        this.seqseqFoldedDiamondLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel seqseqFoldedDiamondLightnessRange_left = new SpinnerNumberModel(this.seqseqFoldedDiamond1.minlightness, 0, 100, 1);
        this.seqseqFoldedDiamondLightnessRange.leftSpinner.setModel(seqseqFoldedDiamondLightnessRange_left);
        this.seqseqFoldedDiamondLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel seqseqFoldedDiamondLightnessRange_right = new SpinnerNumberModel(this.seqseqFoldedDiamond1.maxlightness, 0, 100, 1);
        this.seqseqFoldedDiamondLightnessRange.rightSpinner.setModel(seqseqFoldedDiamondLightnessRange_right);
        this.seqseqFoldedDiamondLightnessRange.rightSpinner.addChangeListener(this);
        this.seqseqFoldedDiamondLightnessRange.multiSlider1.setValueAt(0, this.seqseqFoldedDiamond1.minlightness);
        this.seqseqFoldedDiamondLightnessRange.multiSlider1.setValueAt(1, this.seqseqFoldedDiamond1.maxlightness);
        this.seqseqFoldedDiamondLightnessRange.multiSlider1.setMaximum(100);
        this.seqseqFoldedDiamondLightnessRange.multiSlider1.setMinimum(0);

        this.seqseqParameterPanel.add(this.seqseqFoldedDiamond_Angle);
        this.seqseqFoldedDiamond_Angle.setTitle("Side Angle: 90~160 degrees");
        this.seqseqFoldedDiamond_Angle.slider1.setPreferredSize(new Dimension(110, 20));
        this.seqseqFoldedDiamond_Angle.slider1.setMaximum(100);
        this.seqseqFoldedDiamond_Angle.slider1.setMinimum(20);
        this.seqseqFoldedDiamond_Angle.slider1.setValue(this.seqseqFoldedDiamond1.angle);
        SpinnerNumberModel seqseqFoldedDiamond_Angle = new SpinnerNumberModel(this.seqseqFoldedDiamond1.angle, 20, 100, 1);
        this.seqseqFoldedDiamond_Angle.spinner1.setModel(seqseqFoldedDiamond_Angle);
        this.seqseqFoldedDiamond_Angle.spinner1.addChangeListener(this);

        this.seqseqParameterPanel.add(this.seqseqFoldedDiamond_Rightwing);
        this.seqseqFoldedDiamond_Rightwing.setTitle("Right wing: 0~360 degrees");
        this.seqseqFoldedDiamond_Rightwing.slider1.setPreferredSize(new Dimension(110, 20));
        this.seqseqFoldedDiamond_Rightwing.slider1.setMaximum(360);
        this.seqseqFoldedDiamond_Rightwing.slider1.setMinimum(0);
        this.seqseqFoldedDiamond_Rightwing.slider1.setValue(this.seqseqFoldedDiamond1.rightwing_position);
        SpinnerNumberModel seqseqFoldedDiamond_Rightwing = new SpinnerNumberModel(this.seqseqFoldedDiamond1.rightwing_position, 0, 360, 1);
        this.seqseqFoldedDiamond_Rightwing.spinner1.setModel(seqseqFoldedDiamond_Rightwing);
        this.seqseqFoldedDiamond_Rightwing.spinner1.addChangeListener(this);

        this.seqseqParameterPanel.add(this.seqseqFoldedDiamond_Leftwing);
        this.seqseqFoldedDiamond_Leftwing.setTitle("Left wing: 0~360 degrees");
        this.seqseqFoldedDiamond_Leftwing.slider1.setPreferredSize(new Dimension(110, 20));
        this.seqseqFoldedDiamond_Leftwing.slider1.setMaximum(360);
        this.seqseqFoldedDiamond_Leftwing.slider1.setMinimum(0);
        this.seqseqFoldedDiamond_Leftwing.slider1.setValue(this.seqseqFoldedDiamond1.leftwing_position);
        SpinnerNumberModel seqseqFoldedDiamond_Leftwing = new SpinnerNumberModel(this.seqseqFoldedDiamond1.leftwing_position, 0, 360, 1);
        this.seqseqFoldedDiamond_Leftwing.spinner1.setModel(seqseqFoldedDiamond_Leftwing);
        this.seqseqFoldedDiamond_Leftwing.spinner1.addChangeListener(this);

        this.seqseqParameterPanel.add(this.seqseqFoldedDiamondRecommended);
        this.seqseqFoldedDiamondRecommended.setPreferredSize(new Dimension(120, 30));
        this.seqseqFoldedDiamondRecommended.setText("Recommended");
        this.seqseqFoldedDiamondRecommended.addActionListener(this);

        this.seqseqParameterPanel.add(this.seqseqFoldedDiamondFigure);
        this.seqseqFoldedDiamondFigure.setPreferredSize(new Dimension(50, 30));
        this.seqseqFoldedDiamondFigure.setText("Fig");
        this.seqseqFoldedDiamondFigure.addActionListener(this);

        this.seqseqShiftPanel.removeAll();
        this.seqseqShiftPanel.add(this.seqseqFoldedDiamond_Shift_h);
        this.seqseqFoldedDiamond_Shift_h.setTitle("Vertical Shift: -20~20");
        this.seqseqFoldedDiamond_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqFoldedDiamond_Shift_h.slider1.setMaximum(20);
        this.seqseqFoldedDiamond_Shift_h.slider1.setMinimum(-20);
        this.seqseqFoldedDiamond_Shift_h.slider1.setValue(this.seqseqFoldedDiamond1.shift_h);
        SpinnerNumberModel seqseqFoldedDiamond_Shift_h = new SpinnerNumberModel(this.seqseqFoldedDiamond1.shift_h, -20, 20, 1);
        this.seqseqFoldedDiamond_Shift_h.spinner1.setModel(seqseqFoldedDiamond_Shift_h);
        this.seqseqFoldedDiamond_Shift_h.spinner1.addChangeListener(this);

        this.seqseqShiftPanel.add(this.seqseqFoldedDiamond_Shift_r);
        this.seqseqFoldedDiamond_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.seqseqFoldedDiamond_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqFoldedDiamond_Shift_r.slider1.setMaximum(50);
        this.seqseqFoldedDiamond_Shift_r.slider1.setMinimum(0);
        this.seqseqFoldedDiamond_Shift_r.slider1.setValue(this.seqseqFoldedDiamond1.shift_r);
        SpinnerNumberModel seqseqFoldedDiamond_Shift_r = new SpinnerNumberModel(this.seqseqFoldedDiamond1.shift_r, 0, 50, 1);
        this.seqseqFoldedDiamond_Shift_r.spinner1.setModel(seqseqFoldedDiamond_Shift_r);
        this.seqseqFoldedDiamond_Shift_r.spinner1.addChangeListener(this);

        this.seqseqShiftPanel.add(this.seqseqFoldedDiamond_Shift_p);
        this.seqseqFoldedDiamond_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.seqseqFoldedDiamond_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.seqseqFoldedDiamond_Shift_p.slider1.setMaximum(360);
        this.seqseqFoldedDiamond_Shift_p.slider1.setMinimum(0);
        this.seqseqFoldedDiamond_Shift_p.slider1.setValue(this.seqseqFoldedDiamond1.shift_p);
        SpinnerNumberModel seqseqFoldedDiamond_Shift_p = new SpinnerNumberModel(this.seqseqFoldedDiamond1.shift_p, 0, 360, 1);
        this.seqseqFoldedDiamond_Shift_p.spinner1.setModel(seqseqFoldedDiamond_Shift_p);
        this.seqseqFoldedDiamond_Shift_p.spinner1.addChangeListener(this);
      }
    }

    if(e.getSource() == this.seqseqFoldedDiamondRecommended){
      this.seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 20, 45, 0, 270, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.luvcolor);
      this.seqseqFoldedDiamondLightnessRange.multiSlider1.setValueAt(0, 20);
      this.seqseqFoldedDiamondLightnessRange.multiSlider1.setValueAt(1, 95);
      this.seqseqFoldedDiamond_Angle.slider1.setValue(45);
      this.seqseqFoldedDiamond_Leftwing.slider1.setValue(0);
      this.seqseqFoldedDiamond_Rightwing.slider1.setValue(270);
      this.seqseqFoldedDiamond_Shift_h.slider1.setValue(0);
      this.seqseqFoldedDiamond_Shift_r.slider1.setValue(0);
      this.seqseqFoldedDiamond_Shift_p.slider1.setValue(0);
    }


    //DivseqWedge model
    if(e.getSource() == this.divseqWedgeButton){
      if(this.divseqWedgeButton.isSelected() == true){
        this.divseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divseqWedge1 = new DivseqWedge(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.maxlightness, this.divseqWedge1.minlightness, this.divseqWedge1.alpha, this.divseqWedge1.beta, this.divseqWedge1.range, this.divseqWedge1.shift_h, this.divseqWedge1.shift_r, this.divseqWedge1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.luvcolor);
        this.divseqWedgeLightnessRange.multiSlider1.setValueAt(0, this.divseqWedge1.minlightness);
        this.divseqWedgeLightnessRange.multiSlider1.setValueAt(1, this.divseqWedge1.maxlightness);
        this.divseqWedge_Alpha.slider1.setValue(this.divseqWedge1.alpha);
        this.divseqWedge_Beta.slider1.setValue(this.divseqWedge1.beta);
        this.divseqWedge_Range.slider1.setValue(this.divseqWedge1.range);
        this.divseqWedge_Shift_h.slider1.setValue(this.divseqWedge1.shift_h);
        this.divseqWedge_Shift_r.slider1.setValue(this.divseqWedge1.shift_r);
        this.divseqWedge_Shift_p.slider1.setValue(this.divseqWedge1.shift_p);

        this.divseqParameterPanel.add(this.divseqWedgeLightnessRange);
        this.divseqWedgeLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel divseqWedgeLightnessRange_left = new SpinnerNumberModel(this.divseqWedge1.minlightness, 0, 100, 1);
        this.divseqWedgeLightnessRange.leftSpinner.setModel(divseqWedgeLightnessRange_left);
        this.divseqWedgeLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel divseqWedgeLightnessRange_right = new SpinnerNumberModel(this.divseqWedge1.maxlightness, 0, 100, 1);
        this.divseqWedgeLightnessRange.rightSpinner.setModel(divseqWedgeLightnessRange_right);
        this.divseqWedgeLightnessRange.rightSpinner.addChangeListener(this);
        this.divseqWedgeLightnessRange.multiSlider1.setValueAt(0, this.divseqWedge1.minlightness);
        this.divseqWedgeLightnessRange.multiSlider1.setValueAt(1, this.divseqWedge1.maxlightness);
        this.divseqWedgeLightnessRange.multiSlider1.setMaximum(100);
        this.divseqWedgeLightnessRange.multiSlider1.setMinimum(0);

        this.divseqParameterPanel.add(this.divseqWedge_Alpha);
        this.divseqWedge_Alpha.setTitle("Top Angle: 90~180 degrees");
        this.divseqWedge_Alpha.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqWedge_Alpha.slider1.setMaximum(180);
        this.divseqWedge_Alpha.slider1.setMinimum(90);
        this.divseqWedge_Alpha.slider1.setValue(this.divseqWedge1.alpha);
        SpinnerNumberModel divseqWedge_Alpha = new SpinnerNumberModel(this.divseqWedge1.alpha, 90, 180, 1);
        this.divseqWedge_Alpha.spinner1.setModel(divseqWedge_Alpha);
        this.divseqWedge_Alpha.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqWedge_Beta);
        this.divseqWedge_Beta.setTitle("Oblique Angle: 80~90 degrees");
        this.divseqWedge_Beta.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqWedge_Beta.slider1.setMaximum(90);
        this.divseqWedge_Beta.slider1.setMinimum(80);
        this.divseqWedge_Beta.slider1.setValue(this.divseqWedge1.beta);
        SpinnerNumberModel divseqWedge_Beta = new SpinnerNumberModel(this.divseqWedge1.beta, 80, 90, 1);
        this.divseqWedge_Beta.spinner1.setModel(divseqWedge_Beta);
        this.divseqWedge_Beta.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqWedge_Range);
        this.divseqWedge_Range.setTitle("Bottom Radius: 120~180");
        this.divseqWedge_Range.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqWedge_Range.slider1.setMaximum(180);
        this.divseqWedge_Range.slider1.setMinimum(120);
        this.divseqWedge_Range.slider1.setValue(this.divseqWedge1.range);
        SpinnerNumberModel divseqWedge_Range = new SpinnerNumberModel(this.divseqWedge1.range, 120, 180, 1);
        this.divseqWedge_Range.spinner1.setModel(divseqWedge_Range);
        this.divseqWedge_Range.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqWedgeRecommended);
        this.divseqWedgeRecommended.setPreferredSize(new Dimension(145, 30));
        this.divseqWedgeRecommended.setText("Get Recommended");
        this.divseqWedgeRecommended.addActionListener(this);

        this.divseqShiftPanel.removeAll();
        this.divseqShiftPanel.add(this.divseqWedge_Shift_h);
        this.divseqWedge_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divseqWedge_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqWedge_Shift_h.slider1.setMaximum(20);
        this.divseqWedge_Shift_h.slider1.setMinimum(-20);
        this.divseqWedge_Shift_h.slider1.setValue(this.divseqWedge1.shift_h);
        SpinnerNumberModel divseqWedge_Shift_h = new SpinnerNumberModel(this.divseqWedge1.shift_h, -20, 20, 1);
        this.divseqWedge_Shift_h.spinner1.setModel(divseqWedge_Shift_h);
        this.divseqWedge_Shift_h.spinner1.addChangeListener(this);

        this.divseqShiftPanel.add(this.divseqWedge_Shift_r);
        this.divseqWedge_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divseqWedge_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqWedge_Shift_r.slider1.setMaximum(50);
        this.divseqWedge_Shift_r.slider1.setMinimum(0);
        this.divseqWedge_Shift_r.slider1.setValue(this.divseqWedge1.shift_r);
        SpinnerNumberModel divseqWedge_Shift_r = new SpinnerNumberModel(this.divseqWedge1.shift_r, 0, 50, 1);
        this.divseqWedge_Shift_r.spinner1.setModel(divseqWedge_Shift_r);
        this.divseqWedge_Shift_r.spinner1.addChangeListener(this);

        this.divseqShiftPanel.add(this.divseqWedge_Shift_p);
        this.divseqWedge_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divseqWedge_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqWedge_Shift_p.slider1.setMaximum(360);
        this.divseqWedge_Shift_p.slider1.setMinimum(0);
        this.divseqWedge_Shift_p.slider1.setValue(this.divseqWedge1.shift_p);
        SpinnerNumberModel divseqWedge_Shift_p = new SpinnerNumberModel(this.divseqWedge1.shift_p, 0, 360, 1);
        this.divseqWedge_Shift_p.spinner1.setModel(divseqWedge_Shift_p);
        this.divseqWedge_Shift_p.spinner1.addChangeListener(this);
      }
    }

    if(e.getSource() == this.divseqWedgeRecommended){
      this.divseqWedge1 = new DivseqWedge(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 100, 40, 150, 80, 160, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.luvcolor);
      this.divseqWedgeLightnessRange.multiSlider1.setValueAt(0, 40);
      this.divseqWedgeLightnessRange.multiSlider1.setValueAt(1, 100);
      this.divseqWedge_Alpha.slider1.setValue(150);
      this.divseqWedge_Beta.slider1.setValue(80);
      this.divseqWedge_Range.slider1.setValue(160);
      this.divseqWedge_Shift_h.slider1.setValue(0);
      this.divseqWedge_Shift_r.slider1.setValue(0);
      this.divseqWedge_Shift_p.slider1.setValue(0);
    }

    //DivseqEllipse model
    if(e.getSource() == this.divseqEllipseButton){
      if(this.divseqEllipseButton.isSelected() == true){
        this.divseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divseqEllipse1 = new DivseqEllipse(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.maxlightness, this.divseqEllipse1.minlightness, this.divseqEllipse1.alpha, this.divseqEllipse1.e, this.divseqEllipse1.shift_h, this.divseqEllipse1.shift_r, this.divseqEllipse1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.luvcolor);
        this.divseqEllipseLightnessRange.multiSlider1.setValueAt(0, this.divseqEllipse1.minlightness);
        this.divseqEllipseLightnessRange.multiSlider1.setValueAt(1, this.divseqEllipse1.maxlightness);
        this.divseqEllipse_Alpha.slider1.setValue(this.divseqEllipse1.alpha);
        this.divseqEllipse_E.slider1.setValue(this.divseqEllipse1.e);
        this.divseqEllipse_Shift_h.slider1.setValue(this.divseqEllipse1.shift_h);
        this.divseqEllipse_Shift_r.slider1.setValue(this.divseqEllipse1.shift_r);
        this.divseqEllipse_Shift_p.slider1.setValue(this.divseqEllipse1.shift_p);

        this.divseqParameterPanel.add(this.divseqEllipseLightnessRange);
        this.divseqEllipseLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel divseqEllipseLightnessRange_left = new SpinnerNumberModel(this.divseqEllipse1.minlightness, 0, 100, 1);
        this.divseqEllipseLightnessRange.leftSpinner.setModel(divseqEllipseLightnessRange_left);
        this.divseqEllipseLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel divseqEllipseLightnessRange_right = new SpinnerNumberModel(this.divseqEllipse1.maxlightness, 0, 100, 1);
        this.divseqEllipseLightnessRange.rightSpinner.setModel(divseqEllipseLightnessRange_right);
        this.divseqEllipseLightnessRange.rightSpinner.addChangeListener(this);
        this.divseqEllipseLightnessRange.multiSlider1.setValueAt(0, this.divseqEllipse1.minlightness);
        this.divseqEllipseLightnessRange.multiSlider1.setValueAt(1, this.divseqEllipse1.maxlightness);
        this.divseqEllipseLightnessRange.multiSlider1.setMaximum(100);
        this.divseqEllipseLightnessRange.multiSlider1.setMinimum(0);

        this.divseqParameterPanel.add(this.divseqEllipse_Alpha);
        this.divseqEllipse_Alpha.setTitle("Top Angle: 45~150 degrees");
        this.divseqEllipse_Alpha.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqEllipse_Alpha.slider1.setMaximum(150);
        this.divseqEllipse_Alpha.slider1.setMinimum(45);
        this.divseqEllipse_Alpha.slider1.setValue(this.divseqEllipse1.alpha);
        SpinnerNumberModel divseqEllipse_Alpha = new SpinnerNumberModel(this.divseqEllipse1.alpha, 45, 150, 1);
        this.divseqEllipse_Alpha.spinner1.setModel(divseqEllipse_Alpha);
        this.divseqEllipse_Alpha.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqEllipse_E);
        this.divseqEllipse_E.setTitle("Eccentricity: 0~0.99");
        this.divseqEllipse_E.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqEllipse_E.slider1.setMaximum(99);
        this.divseqEllipse_E.slider1.setMinimum(0);
        this.divseqEllipse_E.slider1.setValue(this.divseqEllipse1.e);
        SpinnerNumberModel divseqEllipse_E = new SpinnerNumberModel(this.divseqEllipse1.e, 0, 99, 1);
        this.divseqEllipse_E.spinner1.setModel(divseqEllipse_E);
        this.divseqEllipse_E.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqEllipseRecommended);
        this.divseqEllipseRecommended.setPreferredSize(new Dimension(145, 30));
        this.divseqEllipseRecommended.setText("Get Recommended");
        this.divseqEllipseRecommended.addActionListener(this);

        this.divseqShiftPanel.removeAll();
        this.divseqShiftPanel.add(this.divseqEllipse_Shift_h);
        this.divseqEllipse_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divseqEllipse_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqEllipse_Shift_h.slider1.setMaximum(20);
        this.divseqEllipse_Shift_h.slider1.setMinimum(-20);
        this.divseqEllipse_Shift_h.slider1.setValue(this.divseqEllipse1.shift_h);
        SpinnerNumberModel divseqEllipse_Shift_h = new SpinnerNumberModel(this.divseqEllipse1.shift_h, -20, 20, 1);
        this.divseqEllipse_Shift_h.spinner1.setModel(divseqEllipse_Shift_h);
        this.divseqEllipse_Shift_h.spinner1.addChangeListener(this);

        this.divseqShiftPanel.add(this.divseqEllipse_Shift_r);
        this.divseqEllipse_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divseqEllipse_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqEllipse_Shift_r.slider1.setMaximum(50);
        this.divseqEllipse_Shift_r.slider1.setMinimum(0);
        this.divseqEllipse_Shift_r.slider1.setValue(this.divseqEllipse1.shift_r);
        SpinnerNumberModel divseqEllipse_Shift_r = new SpinnerNumberModel(this.divseqEllipse1.shift_r, 0, 50, 1);
        this.divseqEllipse_Shift_r.spinner1.setModel(divseqEllipse_Shift_r);
        this.divseqEllipse_Shift_r.spinner1.addChangeListener(this);

        this.divseqShiftPanel.add(this.divseqEllipse_Shift_p);
        this.divseqEllipse_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divseqEllipse_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqEllipse_Shift_p.slider1.setMaximum(360);
        this.divseqEllipse_Shift_p.slider1.setMinimum(0);
        this.divseqEllipse_Shift_p.slider1.setValue(this.divseqEllipse1.shift_p);
        SpinnerNumberModel divseqEllipse_Shift_p = new SpinnerNumberModel(this.divseqEllipse1.shift_p, 0, 360, 1);
        this.divseqEllipse_Shift_p.spinner1.setModel(divseqEllipse_Shift_p);
        this.divseqEllipse_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.divseqEllipseRecommended){
      this.divseqEllipse1 = new DivseqEllipse(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 50, 60, 0, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.luvcolor);
      this.divseqEllipseLightnessRange.multiSlider1.setValueAt(0, 50);
      this.divseqEllipseLightnessRange.multiSlider1.setValueAt(1, 95);
      this.divseqEllipse_Alpha.slider1.setValue(60);
      this.divseqEllipse_E.slider1.setValue(0);
      this.divseqEllipse_Shift_h.slider1.setValue(0);
      this.divseqEllipse_Shift_r.slider1.setValue(0);
      this.divseqEllipse_Shift_p.slider1.setValue(0);
    }

    //DivseqTrapezoid model
    if(e.getSource() == this.divseqTrapezoidButton){
      if(this.divseqTrapezoidButton.isSelected() == true){
        this.divseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divseqTrapezoid1 = new DivseqTrapezoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.maxlightness, this.divseqTrapezoid1.minlightness, this.divseqTrapezoid1.alpha, this.divseqTrapezoid1.radius, this.divseqTrapezoid1.shift_h, this.divseqTrapezoid1.shift_r, this.divseqTrapezoid1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.luvcolor);
        this.divseqTrapezoidLightnessRange.multiSlider1.setValueAt(0, this.divseqTrapezoid1.minlightness);
        this.divseqTrapezoidLightnessRange.multiSlider1.setValueAt(1, this.divseqTrapezoid1.maxlightness);
        this.divseqTrapezoid_Alpha.slider1.setValue(this.divseqTrapezoid1.alpha);
        this.divseqTrapezoid_Radius.slider1.setValue(this.divseqTrapezoid1.radius);
        this.divseqTrapezoid_Shift_h.slider1.setValue(this.divseqTrapezoid1.shift_h);
        this.divseqTrapezoid_Shift_r.slider1.setValue(this.divseqTrapezoid1.shift_r);
        this.divseqTrapezoid_Shift_p.slider1.setValue(this.divseqTrapezoid1.shift_p);

        this.divseqParameterPanel.add(this.divseqTrapezoidLightnessRange);
        this.divseqTrapezoidLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel divseqTrapezoidLightnessRange_left = new SpinnerNumberModel(this.divseqTrapezoid1.minlightness, 0, 100, 1);
        this.divseqTrapezoidLightnessRange.leftSpinner.setModel(divseqTrapezoidLightnessRange_left);
        this.divseqTrapezoidLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel divseqTrapezoidLightnessRange_right = new SpinnerNumberModel(this.divseqTrapezoid1.maxlightness, 0, 100, 1);
        this.divseqTrapezoidLightnessRange.rightSpinner.setModel(divseqTrapezoidLightnessRange_right);
        this.divseqTrapezoidLightnessRange.rightSpinner.addChangeListener(this);
        this.divseqTrapezoidLightnessRange.multiSlider1.setValueAt(0, this.divseqTrapezoid1.minlightness);
        this.divseqTrapezoidLightnessRange.multiSlider1.setValueAt(1, this.divseqTrapezoid1.maxlightness);
        this.divseqTrapezoidLightnessRange.multiSlider1.setMaximum(100);
        this.divseqTrapezoidLightnessRange.multiSlider1.setMinimum(0);

        this.divseqParameterPanel.add(this.divseqTrapezoid_Alpha);
        this.divseqTrapezoid_Alpha.setTitle("Top Angle: 45~120 degrees");
        this.divseqTrapezoid_Alpha.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqTrapezoid_Alpha.slider1.setMaximum(120);
        this.divseqTrapezoid_Alpha.slider1.setMinimum(45);
        this.divseqTrapezoid_Alpha.slider1.setValue(this.divseqTrapezoid1.alpha);
        SpinnerNumberModel divseqTrapezoid_Alpha = new SpinnerNumberModel(this.divseqTrapezoid1.alpha, 45, 120, 1);
        this.divseqTrapezoid_Alpha.spinner1.setModel(divseqTrapezoid_Alpha);
        this.divseqTrapezoid_Alpha.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqTrapezoid_Radius);
        this.divseqTrapezoid_Radius.setTitle("Trapezoid Radius: 50~150");
        this.divseqTrapezoid_Radius.slider1.setPreferredSize(new Dimension(110, 20));
        this.divseqTrapezoid_Radius.slider1.setMaximum(150);
        this.divseqTrapezoid_Radius.slider1.setMinimum(50);
        this.divseqTrapezoid_Radius.slider1.setValue(this.divseqTrapezoid1.radius);
        SpinnerNumberModel divseqTrapezoid_Radius = new SpinnerNumberModel(this.divseqTrapezoid1.radius, 50, 150, 1);
        this.divseqTrapezoid_Radius.spinner1.setModel(divseqTrapezoid_Radius);
        this.divseqTrapezoid_Radius.spinner1.addChangeListener(this);

        this.divseqParameterPanel.add(this.divseqTrapezoidRecommended);
        this.divseqTrapezoidRecommended.setPreferredSize(new Dimension(145, 30));
        this.divseqTrapezoidRecommended.setText("Get Recommended");
        this.divseqTrapezoidRecommended.addActionListener(this);

        this.divseqShiftPanel.removeAll();
        this.divseqShiftPanel.add(this.divseqTrapezoid_Shift_h);
        this.divseqTrapezoid_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divseqTrapezoid_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqTrapezoid_Shift_h.slider1.setMaximum(20);
        this.divseqTrapezoid_Shift_h.slider1.setMinimum(-20);
        this.divseqTrapezoid_Shift_h.slider1.setValue(this.divseqTrapezoid1.shift_h);
        SpinnerNumberModel divseqTrapezoid_Shift_h = new SpinnerNumberModel(this.divseqTrapezoid1.shift_h, -20, 20, 1);
        this.divseqTrapezoid_Shift_h.spinner1.setModel(divseqTrapezoid_Shift_h);
        this.divseqTrapezoid_Shift_h.spinner1.addChangeListener(this);

        this.divseqShiftPanel.add(this.divseqTrapezoid_Shift_r);
        this.divseqTrapezoid_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divseqTrapezoid_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqTrapezoid_Shift_r.slider1.setMaximum(50);
        this.divseqTrapezoid_Shift_r.slider1.setMinimum(0);
        this.divseqTrapezoid_Shift_r.slider1.setValue(this.divseqTrapezoid1.shift_r);
        SpinnerNumberModel divseqTrapezoid_Shift_r = new SpinnerNumberModel(this.divseqTrapezoid1.shift_r, 0, 50, 1);
        this.divseqTrapezoid_Shift_r.spinner1.setModel(divseqTrapezoid_Shift_r);
        this.divseqTrapezoid_Shift_r.spinner1.addChangeListener(this);

        this.divseqShiftPanel.add(this.divseqTrapezoid_Shift_p);
        this.divseqTrapezoid_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divseqTrapezoid_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divseqTrapezoid_Shift_p.slider1.setMaximum(360);
        this.divseqTrapezoid_Shift_p.slider1.setMinimum(0);
        this.divseqTrapezoid_Shift_p.slider1.setValue(this.divseqTrapezoid1.shift_p);
        SpinnerNumberModel divseqTrapezoid_Shift_p = new SpinnerNumberModel(this.divseqTrapezoid1.shift_p, 0, 360, 1);
        this.divseqTrapezoid_Shift_p.spinner1.setModel(divseqTrapezoid_Shift_p);
        this.divseqTrapezoid_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.divseqTrapezoidRecommended){
      this.divseqTrapezoid1 = new DivseqTrapezoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 50, 60, 100, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.luvcolor);
      this.divseqTrapezoidLightnessRange.multiSlider1.setValueAt(0, 50);
      this.divseqTrapezoidLightnessRange.multiSlider1.setValueAt(1, 95);
      this.divseqTrapezoid_Alpha.slider1.setValue(60);
      this.divseqTrapezoid_Radius.slider1.setValue(100);
      this.divseqTrapezoid_Shift_h.slider1.setValue(0);
      this.divseqTrapezoid_Shift_r.slider1.setValue(0);
      this.divseqTrapezoid_Shift_p.slider1.setValue(0);
    }

    //QuaseqCone model
    if(e.getSource() == this.quaseqConeButton){
      if(this.quaseqConeButton.isSelected() == true){
        this.quaseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.quaseqCone1 = new QuaseqCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.maxlightness, this.quaseqCone1.minlightness, this.quaseqCone1.height, this.quaseqCone1.radius, this.quaseqCone1.shift_h, this.quaseqCone1.shift_r, this.quaseqCone1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.luvcolor);
        this.quaseqConeLightnessRange.multiSlider1.setValueAt(0, this.quaseqCone1.minlightness);
        this.quaseqConeLightnessRange.multiSlider1.setValueAt(1, this.quaseqCone1.maxlightness);
        this.quaseqCone_Height.slider1.setValue(this.quaseqCone1.height);
        this.quaseqCone_Radius.slider1.setValue(this.quaseqCone1.radius);
        this.quaseqCone_Shift_h.slider1.setValue(this.quaseqCone1.shift_h);
        this.quaseqCone_Shift_r.slider1.setValue(this.quaseqCone1.shift_r);
        this.quaseqCone_Shift_p.slider1.setValue(this.quaseqCone1.shift_p);

        this.quaseqParameterPanel.add(this.quaseqConeLightnessRange);
        this.quaseqConeLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel quaseqConeLightnessRange_left = new SpinnerNumberModel(this.quaseqCone1.minlightness, 0, 100, 1);
        this.quaseqConeLightnessRange.leftSpinner.setModel(quaseqConeLightnessRange_left);
        this.quaseqConeLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel quaseqConeLightnessRange_right = new SpinnerNumberModel(this.quaseqCone1.maxlightness, 0, 100, 1);
        this.quaseqConeLightnessRange.rightSpinner.setModel(quaseqConeLightnessRange_right);
        this.quaseqConeLightnessRange.rightSpinner.addChangeListener(this);
        this.quaseqConeLightnessRange.multiSlider1.setValueAt(0, this.quaseqCone1.minlightness);
        this.quaseqConeLightnessRange.multiSlider1.setValueAt(1, this.quaseqCone1.maxlightness);
        this.quaseqConeLightnessRange.multiSlider1.setMaximum(100);
        this.quaseqConeLightnessRange.multiSlider1.setMinimum(0);

        this.quaseqParameterPanel.add(this.quaseqCone_Height);
        this.quaseqCone_Height.setTitle("Height: 100~300");
        this.quaseqCone_Height.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqCone_Height.slider1.setMaximum(300);
        this.quaseqCone_Height.slider1.setMinimum(100);
        this.quaseqCone_Height.slider1.setValue(this.quaseqCone1.height);
        SpinnerNumberModel quaseqCone_Height = new SpinnerNumberModel(this.quaseqCone1.height, 100, 300, 1);
        this.quaseqCone_Height.spinner1.setModel(quaseqCone_Height);
        this.quaseqCone_Height.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqCone_Radius);
        this.quaseqCone_Radius.setTitle("Bottom Radius: 40~200");
        this.quaseqCone_Radius.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqCone_Radius.slider1.setMaximum(200);
        this.quaseqCone_Radius.slider1.setMinimum(40);
        this.quaseqCone_Radius.slider1.setValue(this.quaseqCone1.radius);
        SpinnerNumberModel quaseqCone_Radius = new SpinnerNumberModel(this.quaseqCone1.radius, 40, 200, 1);
        this.quaseqCone_Radius.spinner1.setModel(quaseqCone_Radius);
        this.quaseqCone_Radius.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqConeRecommended);
        this.quaseqConeRecommended.setPreferredSize(new Dimension(145, 30));
        this.quaseqConeRecommended.setText("Get Recommended");
        this.quaseqConeRecommended.addActionListener(this);

        this.quaseqShiftPanel.removeAll();
        this.quaseqShiftPanel.add(this.quaseqCone_Shift_h);
        this.quaseqCone_Shift_h.setTitle("Vertical Shift: -20~20");
        this.quaseqCone_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqCone_Shift_h.slider1.setMaximum(20);
        this.quaseqCone_Shift_h.slider1.setMinimum(-20);
        this.quaseqCone_Shift_h.slider1.setValue(this.quaseqCone1.shift_h);
        SpinnerNumberModel quaseqCone_Shift_h = new SpinnerNumberModel(this.quaseqCone1.shift_h, -20, 20, 1);
        this.quaseqCone_Shift_h.spinner1.setModel(quaseqCone_Shift_h);
        this.quaseqCone_Shift_h.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqCone_Shift_r);
        this.quaseqCone_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.quaseqCone_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqCone_Shift_r.slider1.setMaximum(50);
        this.quaseqCone_Shift_r.slider1.setMinimum(0);
        this.quaseqCone_Shift_r.slider1.setValue(this.quaseqCone1.shift_r);
        SpinnerNumberModel quaseqCone_Shift_r = new SpinnerNumberModel(this.quaseqCone1.shift_r, 0, 50, 1);
        this.quaseqCone_Shift_r.spinner1.setModel(quaseqCone_Shift_r);
        this.quaseqCone_Shift_r.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqCone_Shift_p);
        this.quaseqCone_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.quaseqCone_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqCone_Shift_p.slider1.setMaximum(360);
        this.quaseqCone_Shift_p.slider1.setMinimum(0);
        this.quaseqCone_Shift_p.slider1.setValue(this.quaseqCone1.shift_p);
        SpinnerNumberModel quaseqCone_Shift_p = new SpinnerNumberModel(this.quaseqCone1.shift_p, 0, 360, 1);
        this.quaseqCone_Shift_p.spinner1.setModel(quaseqCone_Shift_p);
        this.quaseqCone_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.quaseqConeRecommended){
      this.quaseqCone1 = new QuaseqCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 35, 150, 120, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.luvcolor);
      this.quaseqConeLightnessRange.multiSlider1.setValueAt(0, 35);
      this.quaseqConeLightnessRange.multiSlider1.setValueAt(1, 95);
      this.quaseqCone_Height.slider1.setValue(150);
      this.quaseqCone_Radius.slider1.setValue(120);
      this.quaseqCone_Shift_h.slider1.setValue(0);
      this.quaseqCone_Shift_r.slider1.setValue(0);
      this.quaseqCone_Shift_p.slider1.setValue(0);
    }

    //QuaseqEllipsoid model
    if(e.getSource() == this.quaseqEllipsoidButton){

      if(this.quaseqEllipsoidButton.isSelected() == true){
        this.quaseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.quaseqEllipsoid1 = new QuaseqEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.maxlightness, this.quaseqEllipsoid1.minlightness, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.c, this.quaseqEllipsoid1.shift_h, this.quaseqEllipsoid1.shift_r, this.quaseqEllipsoid1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.luvcolor);
        this.quaseqEllipsoidLightnessRange.multiSlider1.setValueAt(0, this.quaseqEllipsoid1.minlightness);
        this.quaseqEllipsoidLightnessRange.multiSlider1.setValueAt(1, this.quaseqEllipsoid1.maxlightness);
        this.quaseqEllipsoid_Semiaxis_ab.slider1.setValue(this.quaseqEllipsoid1.a);
        this.quaseqEllipsoid_Semiaxis_c.slider1.setValue(this.quaseqEllipsoid1.c);
        this.quaseqEllipsoid_Shift_h.slider1.setValue(this.quaseqEllipsoid1.shift_h);
        this.quaseqEllipsoid_Shift_r.slider1.setValue(this.quaseqEllipsoid1.shift_r);
        this.quaseqEllipsoid_Shift_p.slider1.setValue(this.quaseqEllipsoid1.shift_p);

        this.quaseqParameterPanel.add(this.quaseqEllipsoidLightnessRange);
        this.quaseqEllipsoidLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel quaseqEllipsoidLightnessRange_left = new SpinnerNumberModel(this.quaseqEllipsoid1.minlightness, 0, 100, 1);
        this.quaseqEllipsoidLightnessRange.leftSpinner.setModel(quaseqEllipsoidLightnessRange_left);
        this.quaseqEllipsoidLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel quaseqEllipsoidLightnessRange_right = new SpinnerNumberModel(this.quaseqEllipsoid1.maxlightness, 0, 100, 1);
        this.quaseqEllipsoidLightnessRange.rightSpinner.setModel(quaseqEllipsoidLightnessRange_right);
        this.quaseqEllipsoidLightnessRange.rightSpinner.addChangeListener(this);
        this.quaseqEllipsoidLightnessRange.multiSlider1.setValueAt(0, this.quaseqEllipsoid1.minlightness);
        this.quaseqEllipsoidLightnessRange.multiSlider1.setValueAt(1, this.quaseqEllipsoid1.maxlightness);
        this.quaseqEllipsoidLightnessRange.multiSlider1.setMaximum(100);
        this.quaseqEllipsoidLightnessRange.multiSlider1.setMinimum(0);

        this.quaseqParameterPanel.add(this.quaseqEllipsoid_Semiaxis_ab);
        this.quaseqEllipsoid_Semiaxis_ab.setTitle("Semiaxis a: 100~200");
        this.quaseqEllipsoid_Semiaxis_ab.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqEllipsoid_Semiaxis_ab.slider1.setMaximum(200);
        this.quaseqEllipsoid_Semiaxis_ab.slider1.setMinimum(100);
        this.quaseqEllipsoid_Semiaxis_ab.slider1.setValue(this.quaseqEllipsoid1.a);
        SpinnerNumberModel quaseqEllipsoid_Semiaxis_ab = new SpinnerNumberModel(this.quaseqEllipsoid1.a, 100, 200, 1);
        this.quaseqEllipsoid_Semiaxis_ab.spinner1.setModel(quaseqEllipsoid_Semiaxis_ab);
        this.quaseqEllipsoid_Semiaxis_ab.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqEllipsoid_Semiaxis_c);
        this.quaseqEllipsoid_Semiaxis_c.setTitle("Semiaxis b: 100~200");
        this.quaseqEllipsoid_Semiaxis_c.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqEllipsoid_Semiaxis_c.slider1.setMaximum(200);
        this.quaseqEllipsoid_Semiaxis_c.slider1.setMinimum(100);
        this.quaseqEllipsoid_Semiaxis_c.slider1.setValue(this.quaseqEllipsoid1.c);
        SpinnerNumberModel quaseqEllipsoid_Semiaxis_c = new SpinnerNumberModel(this.quaseqEllipsoid1.c, 100, 200, 1);
        this.quaseqEllipsoid_Semiaxis_c.spinner1.setModel(quaseqEllipsoid_Semiaxis_c);
        this.quaseqEllipsoid_Semiaxis_c.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqEllipsoidRecommended);
        this.quaseqEllipsoidRecommended.setPreferredSize(new Dimension(145, 30));
        this.quaseqEllipsoidRecommended.setText("Get Recommended");
        this.quaseqEllipsoidRecommended.addActionListener(this);

        this.quaseqShiftPanel.removeAll();
        this.quaseqShiftPanel.add(this.quaseqEllipsoid_Shift_h);
        this.quaseqEllipsoid_Shift_h.setTitle("Vertical Shift: -20~20");
        this.quaseqEllipsoid_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqEllipsoid_Shift_h.slider1.setMaximum(20);
        this.quaseqEllipsoid_Shift_h.slider1.setMinimum(-20);
        this.quaseqEllipsoid_Shift_h.slider1.setValue(this.quaseqEllipsoid1.shift_h);
        SpinnerNumberModel quaseqEllipsoid_Shift_h = new SpinnerNumberModel(this.quaseqEllipsoid1.shift_h, -20, 20, 1);
        this.quaseqEllipsoid_Shift_h.spinner1.setModel(quaseqEllipsoid_Shift_h);
        this.quaseqEllipsoid_Shift_h.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqEllipsoid_Shift_r);
        this.quaseqEllipsoid_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.quaseqEllipsoid_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqEllipsoid_Shift_r.slider1.setMaximum(50);
        this.quaseqEllipsoid_Shift_r.slider1.setMinimum(0);
        this.quaseqEllipsoid_Shift_r.slider1.setValue(this.quaseqEllipsoid1.shift_r);
        SpinnerNumberModel quaseqEllipsoid_Shift_r = new SpinnerNumberModel(this.quaseqEllipsoid1.shift_r, 0, 50, 1);
        this.quaseqEllipsoid_Shift_h.spinner1.setModel(quaseqEllipsoid_Shift_r);
        this.quaseqEllipsoid_Shift_h.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqEllipsoid_Shift_p);
        this.quaseqEllipsoid_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.quaseqEllipsoid_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqEllipsoid_Shift_p.slider1.setMaximum(360);
        this.quaseqEllipsoid_Shift_p.slider1.setMinimum(0);
        this.quaseqEllipsoid_Shift_p.slider1.setValue(this.quaseqCone1.shift_p);
        SpinnerNumberModel quaseqEllipsoid_Shift_p = new SpinnerNumberModel(this.quaseqEllipsoid1.shift_p, 0, 360, 1);
        this.quaseqEllipsoid_Shift_p.spinner1.setModel(quaseqEllipsoid_Shift_p);
        this.quaseqEllipsoid_Shift_p.spinner1.addChangeListener(this);
      }

    }


    if(e.getSource() == this.quaseqEllipsoidRecommended){
      this.quaseqEllipsoid1 = new QuaseqEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 35, 120, 120, 120, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.luvcolor);
      this.quaseqEllipsoidLightnessRange.multiSlider1.setValueAt(0, 35);
      this.quaseqEllipsoidLightnessRange.multiSlider1.setValueAt(1, 95);
      this.quaseqEllipsoid_Semiaxis_ab.slider1.setValue(120);
      this.quaseqEllipsoid_Semiaxis_c.slider1.setValue(120);
      this.quaseqEllipsoid_Shift_h.slider1.setValue(0);
      this.quaseqEllipsoid_Shift_r.slider1.setValue(0);
      this.quaseqEllipsoid_Shift_p.slider1.setValue(0);
    }

    //QuaseqEllipsecurve model
    if(e.getSource() == this.quaseqEllipseCurveButton){

      if(this.quaseqEllipseCurveButton.isSelected() == true){
        this.quaseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.quaseqEllipsecurve1 = new QuaseqEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.maxlightness, this.quaseqEllipsecurve1.minlightness, this.quaseqEllipsecurve1.a, this.quaseqEllipsecurve1.b, this.quaseqEllipsecurve1.shift_h, this.quaseqEllipsecurve1.shift_r, this.quaseqEllipsecurve1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.luvcolor);
        this.quaseqEllipseCurveLightnessRange.multiSlider1.setValueAt(0, this.quaseqEllipsecurve1.minlightness);
        this.quaseqEllipseCurveLightnessRange.multiSlider1.setValueAt(1, this.quaseqEllipsecurve1.maxlightness);
        this.quaseqEllipseCurve_Semiaxis_a.slider1.setValue(this.quaseqEllipsecurve1.a);
        this.quaseqEllipseCurve_Semiaxis_b.slider1.setValue(this.quaseqEllipsecurve1.b);
        this.quaseqEllipseCurve_Shift_h.slider1.setValue(this.quaseqEllipsecurve1.shift_h);
        this.quaseqEllipseCurve_Shift_r.slider1.setValue(this.quaseqEllipsecurve1.shift_r);
        this.quaseqEllipseCurve_Shift_p.slider1.setValue(this.quaseqEllipsecurve1.shift_p);

        this.quaseqParameterPanel.add(this.quaseqEllipseCurveLightnessRange);
        this.quaseqEllipseCurveLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel quaseqEllipseCurveLightnessRange_left = new SpinnerNumberModel(this.quaseqEllipsecurve1.minlightness, 0, 100, 1);
        this.quaseqEllipseCurveLightnessRange.leftSpinner.setModel(quaseqEllipseCurveLightnessRange_left);
        this.quaseqEllipsoidLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel quaseqEllipseCurveLightnessRange_right = new SpinnerNumberModel(this.quaseqEllipsecurve1.maxlightness, 0, 100, 1);
        this.quaseqEllipseCurveLightnessRange.rightSpinner.setModel(quaseqEllipseCurveLightnessRange_right);
        this.quaseqEllipseCurveLightnessRange.rightSpinner.addChangeListener(this);
        this.quaseqEllipseCurveLightnessRange.multiSlider1.setValueAt(0, this.quaseqEllipsecurve1.minlightness);
        this.quaseqEllipseCurveLightnessRange.multiSlider1.setValueAt(1, this.quaseqEllipsecurve1.maxlightness);
        this.quaseqEllipseCurveLightnessRange.multiSlider1.setMaximum(100);
        this.quaseqEllipseCurveLightnessRange.multiSlider1.setMinimum(0);

        this.quaseqParameterPanel.add(this.quaseqEllipseCurve_Semiaxis_a);
        this.quaseqEllipseCurve_Semiaxis_a.setTitle("Semiaxis a: 200~500");
        this.quaseqEllipseCurve_Semiaxis_a.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqEllipseCurve_Semiaxis_a.slider1.setMaximum(500);
        this.quaseqEllipseCurve_Semiaxis_a.slider1.setMinimum(200);
        this.quaseqEllipseCurve_Semiaxis_a.slider1.setValue(this.quaseqEllipsecurve1.a);
        SpinnerNumberModel quaseqEllipseCurve_Semiaxis_a = new SpinnerNumberModel(this.quaseqEllipsecurve1.a, 200, 500, 1);
        this.quaseqEllipseCurve_Semiaxis_a.spinner1.setModel(quaseqEllipseCurve_Semiaxis_a);
        this.quaseqEllipseCurve_Semiaxis_a.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqEllipseCurve_Semiaxis_b);
        this.quaseqEllipseCurve_Semiaxis_b.setTitle("Semiaxis b: 100~200");
        this.quaseqEllipseCurve_Semiaxis_b.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqEllipseCurve_Semiaxis_b.slider1.setMaximum(200);
        this.quaseqEllipseCurve_Semiaxis_b.slider1.setMinimum(100);
        this.quaseqEllipseCurve_Semiaxis_b.slider1.setValue(this.quaseqEllipsecurve1.b);
        SpinnerNumberModel quaseqEllipseCurve_Semiaxis_b = new SpinnerNumberModel(this.quaseqEllipsecurve1.b, 100, 200, 1);
        this.quaseqEllipseCurve_Semiaxis_b.spinner1.setModel(quaseqEllipseCurve_Semiaxis_b);
        this.quaseqEllipseCurve_Semiaxis_b.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqEllipseCurveRecommended);
        this.quaseqEllipseCurveRecommended.setPreferredSize(new Dimension(145, 30));
        this.quaseqEllipseCurveRecommended.setText("Get Recommended");
        this.quaseqEllipseCurveRecommended.addActionListener(this);

        this.quaseqShiftPanel.removeAll();
        this.quaseqShiftPanel.add(this.quaseqEllipseCurve_Shift_h);
        this.quaseqEllipseCurve_Shift_h.setTitle("Vertical Shift: -20~20");
        this.quaseqEllipseCurve_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqEllipseCurve_Shift_h.slider1.setMaximum(20);
        this.quaseqEllipseCurve_Shift_h.slider1.setMinimum(-20);
        this.quaseqEllipseCurve_Shift_h.slider1.setValue(this.quaseqEllipsecurve1.shift_h);
        SpinnerNumberModel quaseqEllipseCurve_Shift_h = new SpinnerNumberModel(this.quaseqEllipsecurve1.shift_h, -20, 20, 1);
        this.quaseqEllipseCurve_Shift_h.spinner1.setModel(quaseqEllipseCurve_Shift_h);
        this.quaseqEllipseCurve_Shift_h.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqEllipseCurve_Shift_r);
        this.quaseqEllipseCurve_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.quaseqEllipseCurve_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqEllipseCurve_Shift_r.slider1.setMaximum(50);
        this.quaseqEllipseCurve_Shift_r.slider1.setMinimum(0);
        this.quaseqEllipseCurve_Shift_r.slider1.setValue(this.quaseqEllipsecurve1.shift_r);
        SpinnerNumberModel quaseqEllipseCurve_Shift_r = new SpinnerNumberModel(this.quaseqEllipsecurve1.shift_r, 0, 50, 1);
        this.quaseqEllipseCurve_Shift_r.spinner1.setModel(quaseqEllipseCurve_Shift_r);
        this.quaseqEllipseCurve_Shift_r.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqEllipseCurve_Shift_p);
        this.quaseqEllipseCurve_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.quaseqEllipseCurve_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqEllipseCurve_Shift_p.slider1.setMaximum(360);
        this.quaseqEllipseCurve_Shift_p.slider1.setMinimum(0);
        this.quaseqEllipseCurve_Shift_p.slider1.setValue(this.quaseqEllipsecurve1.shift_p);
        SpinnerNumberModel quaseqEllipseCurve_Shift_p = new SpinnerNumberModel(this.quaseqEllipsecurve1.shift_p, 0, 360, 1);
        this.quaseqEllipseCurve_Shift_p.spinner1.setModel(quaseqEllipseCurve_Shift_p);
        this.quaseqEllipseCurve_Shift_p.spinner1.addChangeListener(this);
      }

    }



    if(e.getSource() == this.quaseqEllipseCurveRecommended){
      this.quaseqEllipsecurve1 = new QuaseqEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 35, 300, 120, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.luvcolor);
      this.quaseqEllipseCurveLightnessRange.multiSlider1.setValueAt(0, 35);
      this.quaseqEllipseCurveLightnessRange.multiSlider1.setValueAt(1, 95);
      this.quaseqEllipseCurve_Semiaxis_a.slider1.setValue(300);
      this.quaseqEllipseCurve_Semiaxis_b.slider1.setValue(120);
      this.quaseqEllipseCurve_Shift_h.slider1.setValue(0);
      this.quaseqEllipseCurve_Shift_r.slider1.setValue(0);
      this.quaseqEllipseCurve_Shift_p.slider1.setValue(0);
    }

    //QuaseqBellshape model
    if(e.getSource() == this.quaseqBellshapeButton){

      if(this.quaseqBellshapeButton.isSelected() == true){
        this.quaseqParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.quaseqBellshape1 = new QuaseqBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.maxlightness, this.quaseqBellshape1.minlightness, this.quaseqBellshape1.curvevertex, this.quaseqBellshape1.divisor, this.quaseqBellshape1.shift_h, this.quaseqBellshape1.shift_r, this.quaseqBellshape1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.luvcolor);
        this.quaseqBellshapeLightnessRange.multiSlider1.setValueAt(0, this.quaseqBellshape1.minlightness);
        this.quaseqBellshapeLightnessRange.multiSlider1.setValueAt(1, this.quaseqBellshape1.maxlightness);
        this.quaseqBellshape_Vertex.slider1.setValue(this.quaseqBellshape1.curvevertex);
        this.quaseqBellshape_Divisor.slider1.setValue(this.quaseqBellshape1.divisor);
        this.quaseqBellshape_Shift_h.slider1.setValue(this.quaseqBellshape1.shift_h);
        this.quaseqBellshape_Shift_r.slider1.setValue(this.quaseqBellshape1.shift_r);
        this.quaseqBellshape_Shift_p.slider1.setValue(this.quaseqBellshape1.shift_p);

        this.quaseqParameterPanel.add(this.quaseqBellshapeLightnessRange);
        this.quaseqBellshapeLightnessRange.setTitle("Lightness Range: 0~100");
        SpinnerNumberModel quaseqBellshapeLightnessRange_left = new SpinnerNumberModel(this.quaseqBellshape1.minlightness, 0, 100, 1);
        this.quaseqBellshapeLightnessRange.leftSpinner.setModel(quaseqBellshapeLightnessRange_left);
        this.quaseqEllipsoidLightnessRange.leftSpinner.addChangeListener(this);
        SpinnerNumberModel quaseqBellshapeLightnessRange_right = new SpinnerNumberModel(this.quaseqBellshape1.maxlightness, 0, 100, 1);
        this.quaseqBellshapeLightnessRange.rightSpinner.setModel(quaseqBellshapeLightnessRange_right);
        this.quaseqBellshapeLightnessRange.rightSpinner.addChangeListener(this);
        this.quaseqBellshapeLightnessRange.multiSlider1.setValueAt(0, this.quaseqBellshape1.minlightness);
        this.quaseqBellshapeLightnessRange.multiSlider1.setValueAt(1, this.quaseqBellshape1.maxlightness);
        this.quaseqBellshapeLightnessRange.multiSlider1.setMaximum(100);
        this.quaseqBellshapeLightnessRange.multiSlider1.setMinimum(0);

        this.quaseqParameterPanel.add(this.quaseqBellshape_Vertex);
        this.quaseqBellshape_Vertex.setTitle("Vertex: 100~500");
        this.quaseqBellshape_Vertex.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqBellshape_Vertex.slider1.setMaximum(500);
        this.quaseqBellshape_Vertex.slider1.setMinimum(100);
        this.quaseqBellshape_Vertex.slider1.setValue(this.quaseqBellshape1.curvevertex);
        SpinnerNumberModel quaseqBellshape_Vertex = new SpinnerNumberModel(this.quaseqBellshape1.curvevertex, 100, 500, 1);
        this.quaseqBellshape_Vertex.spinner1.setModel(quaseqBellshape_Vertex);
        this.quaseqBellshape_Vertex.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqBellshape_Divisor);
        this.quaseqBellshape_Divisor.setTitle("Divisor: 1000~9000");
        this.quaseqBellshape_Divisor.slider1.setPreferredSize(new Dimension(110, 20));
        this.quaseqBellshape_Divisor.slider1.setMaximum(9000);
        this.quaseqBellshape_Divisor.slider1.setMinimum(1000);
        this.quaseqBellshape_Divisor.slider1.setValue(this.quaseqBellshape1.divisor);
        SpinnerNumberModel quaseqBellshape_Divisor = new SpinnerNumberModel(this.quaseqBellshape1.divisor, 1000, 9000, 1);
        this.quaseqBellshape_Divisor.spinner1.setModel(quaseqBellshape_Divisor);
        this.quaseqBellshape_Divisor.spinner1.setPreferredSize(new Dimension(60, 20));
        this.quaseqBellshape_Divisor.spinner1.addChangeListener(this);

        this.quaseqParameterPanel.add(this.quaseqBellshapeRecommended);
        this.quaseqBellshapeRecommended.setPreferredSize(new Dimension(145, 30));
        this.quaseqBellshapeRecommended.setText("Get Recommended");
        this.quaseqBellshapeRecommended.addActionListener(this);

        this.quaseqShiftPanel.removeAll();
        this.quaseqShiftPanel.add(this.quaseqBellshape_Shift_h);
        this.quaseqBellshape_Shift_h.setTitle("Vertical Shift: -20~20");
        this.quaseqBellshape_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqBellshape_Shift_h.slider1.setMaximum(20);
        this.quaseqBellshape_Shift_h.slider1.setMinimum(-20);
        this.quaseqBellshape_Shift_h.slider1.setValue(this.quaseqBellshape1.shift_h);
        SpinnerNumberModel quaseqBellshape_Shift_h = new SpinnerNumberModel(this.quaseqBellshape1.shift_h, -20, 20, 1);
        this.quaseqBellshape_Shift_h.spinner1.setModel(quaseqBellshape_Shift_h);
        this.quaseqBellshape_Shift_h.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqBellshape_Shift_r);
        this.quaseqBellshape_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.quaseqBellshape_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqBellshape_Shift_r.slider1.setMaximum(50);
        this.quaseqBellshape_Shift_r.slider1.setMinimum(0);
        this.quaseqBellshape_Shift_r.slider1.setValue(this.quaseqBellshape1.shift_r);
        SpinnerNumberModel quaseqBellshape_Shift_r = new SpinnerNumberModel(this.quaseqBellshape1.shift_r, 0, 50, 1);
        this.quaseqBellshape_Shift_r.spinner1.setModel(quaseqBellshape_Shift_r);
        this.quaseqBellshape_Shift_r.spinner1.addChangeListener(this);

        this.quaseqShiftPanel.add(this.quaseqBellshape_Shift_p);
        this.quaseqBellshape_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.quaseqBellshape_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.quaseqBellshape_Shift_p.slider1.setMaximum(360);
        this.quaseqBellshape_Shift_p.slider1.setMinimum(0);
        this.quaseqBellshape_Shift_p.slider1.setValue(this.quaseqBellshape1.shift_p);
        SpinnerNumberModel quaseqBellshape_Shift_p = new SpinnerNumberModel(this.quaseqBellshape1.shift_p, 0, 360, 1);
        this.quaseqBellshape_Shift_p.spinner1.setModel(quaseqBellshape_Shift_p);
        this.quaseqBellshape_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.quaseqBellshapeRecommended){
      this.quaseqBellshape1 = new QuaseqBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 95, 35, 150, 5000, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.luvcolor);
      this.quaseqBellshapeLightnessRange.multiSlider1.setValueAt(0, 35);
      this.quaseqBellshapeLightnessRange.multiSlider1.setValueAt(1, 95);
      this.quaseqBellshape_Vertex.slider1.setValue(150);
      this.quaseqBellshape_Divisor.slider1.setValue(5000);
      this.quaseqBellshape_Shift_h.slider1.setValue(0);
      this.quaseqBellshape_Shift_r.slider1.setValue(0);
      this.quaseqBellshape_Shift_p.slider1.setValue(0);
    }

    //DivdivCone model
    if(e.getSource() == this.divdivConeButton){
      if(this.divdivConeButton.isSelected() == true){
        this.divdivParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divdivCone1 = new DivdivCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.range, this.divdivCone1.height, this.divdivCone1.radius, this.divdivCone1.shift_h, this.divdivCone1.shift_r, this.divdivCone1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.luvcolor);
        this.divdivCone_Range.slider1.setValue(this.divdivCone1.range);
        this.divdivCone_Height.slider1.setValue(this.divdivCone1.height);
        this.divdivCone_Radius.slider1.setValue(this.divdivCone1.radius);
        this.divdivCone_Shift_h.slider1.setValue(this.divdivCone1.shift_h);
        this.divdivCone_Shift_r.slider1.setValue(this.divdivCone1.shift_r);
        this.divdivCone_Shift_p.slider1.setValue(this.divdivCone1.shift_p);

        this.divdivParameterPanel.add(this.divdivCone_Range);
        this.divdivCone_Range.setTitle("Range: 100~200");
        this.divdivCone_Range.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivCone_Range.slider1.setMaximum(200);
        this.divdivCone_Range.slider1.setMinimum(100);
        this.divdivCone_Range.slider1.setValue(this.divdivCone1.range);
        SpinnerNumberModel divdivCone_Range = new SpinnerNumberModel(this.divdivCone1.range, 100, 200, 1);
        this.divdivCone_Range.spinner1.setModel(divdivCone_Range);
        this.divdivCone_Range.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivCone_Height);
        this.divdivCone_Height.setTitle("Height: 100~300");
        this.divdivCone_Height.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivCone_Height.slider1.setMaximum(300);
        this.divdivCone_Height.slider1.setMinimum(100);
        this.divdivCone_Height.slider1.setValue(this.divdivCone1.height);
        SpinnerNumberModel divdivCone_Height = new SpinnerNumberModel(this.divdivCone1.height, 100, 300, 1);
        this.divdivCone_Height.spinner1.setModel(divdivCone_Height);
        this.divdivCone_Height.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivCone_Radius);
        this.divdivCone_Radius.setTitle("Bottom Radius: 40~200");
        this.divdivCone_Radius.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivCone_Radius.slider1.setMaximum(200);
        this.divdivCone_Radius.slider1.setMinimum(40);
        this.divdivCone_Radius.slider1.setValue(this.divdivCone1.radius);
        SpinnerNumberModel divdivCone_Radius = new SpinnerNumberModel(this.divdivCone1.radius, 40, 200, 1);
        this.divdivCone_Radius.spinner1.setModel(divdivCone_Radius);
        this.divdivCone_Radius.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivConeRecommended);
        this.divdivConeRecommended.setPreferredSize(new Dimension(145, 30));
        this.divdivConeRecommended.setText("Get Recommended");
        this.divdivConeRecommended.addActionListener(this);

        this.divdivShiftPanel.removeAll();
        this.divdivShiftPanel.add(this.divdivCone_Shift_h);
        this.divdivCone_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divdivCone_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivCone_Shift_h.slider1.setMaximum(20);
        this.divdivCone_Shift_h.slider1.setMinimum(-20);
        this.divdivCone_Shift_h.slider1.setValue(this.divdivCone1.shift_h);
        SpinnerNumberModel divdivCone_Shift_h = new SpinnerNumberModel(this.divdivCone1.shift_h, -20, 20, 1);
        this.divdivCone_Shift_h.spinner1.setModel(divdivCone_Shift_h);
        this.divdivCone_Shift_h.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivCone_Shift_r);
        this.divdivCone_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divdivCone_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivCone_Shift_r.slider1.setMaximum(50);
        this.divdivCone_Shift_r.slider1.setMinimum(0);
        this.divdivCone_Shift_r.slider1.setValue(this.divdivCone1.shift_r);
        SpinnerNumberModel divdivCone_Shift_r = new SpinnerNumberModel(this.divdivCone1.shift_r, 0, 50, 1);
        this.divdivCone_Shift_r.spinner1.setModel(divdivCone_Shift_r);
        this.divdivCone_Shift_r.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivCone_Shift_p);
        this.divdivCone_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divdivCone_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivCone_Shift_p.slider1.setMaximum(360);
        this.divdivCone_Shift_p.slider1.setMinimum(0);
        this.divdivCone_Shift_p.slider1.setValue(this.divdivCone1.shift_p);
        SpinnerNumberModel divdivCone_Shift_p = new SpinnerNumberModel(this.divdivCone1.shift_p, 0, 360, 1);
        this.divdivCone_Shift_p.spinner1.setModel(divdivCone_Shift_p);
        this.divdivCone_Shift_p.spinner1.addChangeListener(this);
      }
    }

    if(e.getSource() == this.divdivConeRecommended){
      this.divdivCone1 = new DivdivCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 160, 100, 130, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.luvcolor);
      this.divdivCone_Range.slider1.setValue(160);
      this.divdivCone_Height.slider1.setValue(100);
      this.divdivCone_Radius.slider1.setValue(130);
      this.divdivCone_Shift_h.slider1.setValue(0);
      this.divdivCone_Shift_r.slider1.setValue(0);
      this.divdivCone_Shift_p.slider1.setValue(0);
    }

    //DivdivEllipsoid model
    if(e.getSource() == this.divdivEllipsoidButton){

      if(this.divdivEllipsoidButton.isSelected() == true){
        this.divdivParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divdivEllipsoid1 = new DivdivEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.range, this.divdivEllipsoid1.a, this.divdivEllipsoid1.c, this.divdivEllipsoid1.shift_h, this.divdivEllipsoid1.shift_r, this.divdivEllipsoid1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.luvcolor);
        this.divdivEllipsoid_Range.slider1.setValue(this.divdivEllipsoid1.range);
        this.divdivEllipsoid_Semiaxis_ab.slider1.setValue(this.divdivEllipsoid1.a);
        this.divdivEllipsoid_Semiaxis_c.slider1.setValue(this.divdivEllipsoid1.c);
        this.divdivEllipsoid_Shift_h.slider1.setValue(this.divdivEllipsoid1.shift_h);
        this.divdivEllipsoid_Shift_r.slider1.setValue(this.divdivEllipsoid1.shift_r);
        this.divdivEllipsoid_Shift_p.slider1.setValue(this.divdivEllipsoid1.shift_p);

        this.divdivParameterPanel.add(this.divdivEllipsoid_Range);
        this.divdivEllipsoid_Range.setTitle("Range: 100~200");
        this.divdivEllipsoid_Range.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivEllipsoid_Range.slider1.setMaximum(200);
        this.divdivEllipsoid_Range.slider1.setMinimum(100);
        this.divdivEllipsoid_Range.slider1.setValue(this.divdivEllipsoid1.range);
        SpinnerNumberModel divdivEllipsoid_Range = new SpinnerNumberModel(this.divdivEllipsoid1.range, 100, 200, 1);
        this.divdivEllipsoid_Range.spinner1.setModel(divdivEllipsoid_Range);
        this.divdivEllipsoid_Range.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivEllipsoid_Semiaxis_ab);
        this.divdivEllipsoid_Semiaxis_ab.setTitle("Semiaxis a: 100~200");
        this.divdivEllipsoid_Semiaxis_ab.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivEllipsoid_Semiaxis_ab.slider1.setMaximum(200);
        this.divdivEllipsoid_Semiaxis_ab.slider1.setMinimum(100);
        this.divdivEllipsoid_Semiaxis_ab.slider1.setValue(this.divdivEllipsoid1.a);
        SpinnerNumberModel divdivEllipsoid_Semiaxis_ab = new SpinnerNumberModel(this.divdivEllipsoid1.a, 100, 200, 1);
        this.divdivEllipsoid_Semiaxis_ab.spinner1.setModel(divdivEllipsoid_Semiaxis_ab);
        this.divdivEllipsoid_Semiaxis_ab.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivEllipsoid_Semiaxis_c);
        this.divdivEllipsoid_Semiaxis_c.setTitle("Semiaxis b: 100~200");
        this.divdivEllipsoid_Semiaxis_c.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivEllipsoid_Semiaxis_c.slider1.setMaximum(200);
        this.divdivEllipsoid_Semiaxis_c.slider1.setMinimum(100);
        this.divdivEllipsoid_Semiaxis_c.slider1.setValue(this.divdivEllipsoid1.c);
        SpinnerNumberModel divdivEllipsoid_Semiaxis_c = new SpinnerNumberModel(this.divdivEllipsoid1.c, 100, 200, 1);
        this.divdivEllipsoid_Semiaxis_c.spinner1.setModel(divdivEllipsoid_Semiaxis_c);
        this.divdivEllipsoid_Semiaxis_c.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivEllipsoidRecommended);
        this.divdivEllipsoidRecommended.setPreferredSize(new Dimension(145, 30));
        this.divdivEllipsoidRecommended.setText("Get Recommended");
        this.divdivEllipsoidRecommended.addActionListener(this);

        this.divdivShiftPanel.removeAll();
        this.divdivShiftPanel.add(this.divdivEllipsoid_Shift_h);
        this.divdivEllipsoid_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divdivEllipsoid_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivEllipsoid_Shift_h.slider1.setMaximum(20);
        this.divdivEllipsoid_Shift_h.slider1.setMinimum(-20);
        this.divdivEllipsoid_Shift_h.slider1.setValue(this.divdivEllipsoid1.shift_h);
        SpinnerNumberModel divdivEllipsoid_Shift_h = new SpinnerNumberModel(this.divdivEllipsoid1.shift_h, -20, 20, 1);
        this.divdivEllipsoid_Shift_h.spinner1.setModel(divdivEllipsoid_Shift_h);
        this.divdivEllipsoid_Shift_h.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivEllipsoid_Shift_r);
        this.divdivEllipsoid_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divdivEllipsoid_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivEllipsoid_Shift_r.slider1.setMaximum(50);
        this.divdivEllipsoid_Shift_r.slider1.setMinimum(0);
        this.divdivEllipsoid_Shift_r.slider1.setValue(this.divdivEllipsoid1.shift_r);
        SpinnerNumberModel divdivEllipsoid_Shift_r = new SpinnerNumberModel(this.divdivEllipsoid1.shift_r, 0, 50, 1);
        this.divdivEllipsoid_Shift_h.spinner1.setModel(divdivEllipsoid_Shift_r);
        this.divdivEllipsoid_Shift_h.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivEllipsoid_Shift_p);
        this.divdivEllipsoid_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divdivEllipsoid_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivEllipsoid_Shift_p.slider1.setMaximum(360);
        this.divdivEllipsoid_Shift_p.slider1.setMinimum(0);
        this.divdivEllipsoid_Shift_p.slider1.setValue(this.divdivCone1.shift_p);
        SpinnerNumberModel divdivEllipsoid_Shift_p = new SpinnerNumberModel(this.divdivEllipsoid1.shift_p, 0, 360, 1);
        this.divdivEllipsoid_Shift_p.spinner1.setModel(divdivEllipsoid_Shift_p);
        this.divdivEllipsoid_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.divdivEllipsoidRecommended){
      this.divdivEllipsoid1 = new DivdivEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 160, 100, 100, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.luvcolor);
      this.divdivEllipsoid_Range.slider1.setValue(160);
      this.divdivEllipsoid_Semiaxis_ab.slider1.setValue(100);
      this.divdivEllipsoid_Semiaxis_c.slider1.setValue(100);
      this.divdivEllipsoid_Shift_h.slider1.setValue(0);
      this.divdivEllipsoid_Shift_r.slider1.setValue(0);
      this.divdivEllipsoid_Shift_p.slider1.setValue(0);
    }

    //DivdivEllipsecurve model
    if(e.getSource() == this.divdivEllipseCurveButton){

      if(this.divdivEllipseCurveButton.isSelected() == true){
        this.divdivParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divdivEllipsecurve1 = new DivdivEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.range, this.divdivEllipsecurve1.a, this.divdivEllipsecurve1.b, this.divdivEllipsecurve1.shift_h, this.divdivEllipsecurve1.shift_r, this.divdivEllipsecurve1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.luvcolor);
        this.divdivEllipseCurve_Range.slider1.setValue(this.divdivEllipsecurve1.range);
        this.divdivEllipseCurve_Semiaxis_a.slider1.setValue(this.divdivEllipsecurve1.a);
        this.divdivEllipseCurve_Semiaxis_b.slider1.setValue(this.divdivEllipsecurve1.b);
        this.divdivEllipseCurve_Shift_h.slider1.setValue(this.divdivEllipsecurve1.shift_h);
        this.divdivEllipseCurve_Shift_r.slider1.setValue(this.divdivEllipsecurve1.shift_r);
        this.divdivEllipseCurve_Shift_p.slider1.setValue(this.divdivEllipsecurve1.shift_p);

        this.divdivParameterPanel.add(this.divdivEllipseCurve_Range);
        this.divdivEllipseCurve_Range.setTitle("Range: 100~200");
        this.divdivEllipseCurve_Range.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivEllipseCurve_Range.slider1.setMaximum(200);
        this.divdivEllipseCurve_Range.slider1.setMinimum(100);
        this.divdivEllipseCurve_Range.slider1.setValue(this.divdivEllipsecurve1.range);
        SpinnerNumberModel divdivEllipsecurve_Range = new SpinnerNumberModel(this.divdivEllipsecurve1.range, 100, 200, 1);
        this.divdivEllipseCurve_Range.spinner1.setModel(divdivEllipsecurve_Range);
        this.divdivEllipseCurve_Range.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivEllipseCurve_Semiaxis_a);
        this.divdivEllipseCurve_Semiaxis_a.setTitle("Semiaxis a: 500~3000");
        this.divdivEllipseCurve_Semiaxis_a.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivEllipseCurve_Semiaxis_a.slider1.setMaximum(3000);
        this.divdivEllipseCurve_Semiaxis_a.slider1.setMinimum(500);
        this.divdivEllipseCurve_Semiaxis_a.slider1.setValue(this.divdivEllipsecurve1.a);
        SpinnerNumberModel divdivEllipseCurve_Semiaxis_a = new SpinnerNumberModel(this.divdivEllipsecurve1.a, 500, 3000, 1);
        this.divdivEllipseCurve_Semiaxis_a.spinner1.setModel(divdivEllipseCurve_Semiaxis_a);
        this.divdivEllipseCurve_Semiaxis_a.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivEllipseCurve_Semiaxis_b);
        this.divdivEllipseCurve_Semiaxis_b.setTitle("Semiaxis b: 80~200");
        this.divdivEllipseCurve_Semiaxis_b.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivEllipseCurve_Semiaxis_b.slider1.setMaximum(200);
        this.divdivEllipseCurve_Semiaxis_b.slider1.setMinimum(80);
        this.divdivEllipseCurve_Semiaxis_b.slider1.setValue(this.divdivEllipsecurve1.b);
        SpinnerNumberModel divdivEllipseCurve_Semiaxis_b = new SpinnerNumberModel(this.divdivEllipsecurve1.b, 80, 200, 1);
        this.divdivEllipseCurve_Semiaxis_b.spinner1.setModel(divdivEllipseCurve_Semiaxis_b);
        this.divdivEllipseCurve_Semiaxis_b.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivEllipseCurveRecommended);
        this.divdivEllipseCurveRecommended.setPreferredSize(new Dimension(145, 30));
        this.divdivEllipseCurveRecommended.setText("Get Recommended");
        this.divdivEllipseCurveRecommended.addActionListener(this);

        this.divdivShiftPanel.removeAll();
        this.divdivShiftPanel.add(this.divdivEllipseCurve_Shift_h);
        this.divdivEllipseCurve_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divdivEllipseCurve_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivEllipseCurve_Shift_h.slider1.setMaximum(20);
        this.divdivEllipseCurve_Shift_h.slider1.setMinimum(-20);
        this.divdivEllipseCurve_Shift_h.slider1.setValue(this.divdivEllipsecurve1.shift_h);
        SpinnerNumberModel divdivEllipseCurve_Shift_h = new SpinnerNumberModel(this.divdivEllipsecurve1.shift_h, -20, 20, 1);
        this.divdivEllipseCurve_Shift_h.spinner1.setModel(divdivEllipseCurve_Shift_h);
        this.divdivEllipseCurve_Shift_h.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivEllipseCurve_Shift_r);
        this.divdivEllipseCurve_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divdivEllipseCurve_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivEllipseCurve_Shift_r.slider1.setMaximum(50);
        this.divdivEllipseCurve_Shift_r.slider1.setMinimum(0);
        this.divdivEllipseCurve_Shift_r.slider1.setValue(this.divdivEllipsecurve1.shift_r);
        SpinnerNumberModel divdivEllipseCurve_Shift_r = new SpinnerNumberModel(this.divdivEllipsecurve1.shift_r, 0, 50, 1);
        this.divdivEllipseCurve_Shift_r.spinner1.setModel(divdivEllipseCurve_Shift_r);
        this.divdivEllipseCurve_Shift_r.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivEllipseCurve_Shift_p);
        this.divdivEllipseCurve_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divdivEllipseCurve_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivEllipseCurve_Shift_p.slider1.setMaximum(360);
        this.divdivEllipseCurve_Shift_p.slider1.setMinimum(0);
        this.divdivEllipseCurve_Shift_p.slider1.setValue(this.divdivEllipsecurve1.shift_p);
        SpinnerNumberModel divdivEllipseCurve_Shift_p = new SpinnerNumberModel(this.divdivEllipsecurve1.shift_p, 0, 360, 1);
        this.divdivEllipseCurve_Shift_p.spinner1.setModel(divdivEllipseCurve_Shift_p);
        this.divdivEllipseCurve_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.divdivEllipseCurveRecommended){
      this.divdivEllipsecurve1 = new DivdivEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 160, 1800, 100, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.luvcolor);
      this.divdivEllipseCurve_Range.slider1.setValue(160);
      this.divdivEllipseCurve_Semiaxis_a.slider1.setValue(1800);
      this.divdivEllipseCurve_Semiaxis_b.slider1.setValue(100);
      this.divdivEllipseCurve_Shift_h.slider1.setValue(0);
      this.divdivEllipseCurve_Shift_r.slider1.setValue(0);
      this.divdivEllipseCurve_Shift_p.slider1.setValue(0);
    }

    //DivdivBellshape model
    if(e.getSource() == this.divdivBellshapeButton){

      if(this.divdivBellshapeButton.isSelected() == true){
        this.divdivParameterPanel.removeAll();
        this.repaint();
        this.revalidate();
        this.divdivBellshape1 = new DivdivBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.range, this.divdivBellshape1.curvevertex, this.divdivBellshape1.divisor, this.divdivBellshape1.shift_h, this.divdivBellshape1.shift_r, this.divdivBellshape1.shift_p);
        this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.luvcolor);
        this.divdivBellshape_Range.slider1.setValue(this.divdivBellshape1.range);
        this.divdivBellshape_Vertex.slider1.setValue(this.divdivBellshape1.curvevertex);
        this.divdivBellshape_Divisor.slider1.setValue(this.divdivBellshape1.divisor);
        this.divdivBellshape_Shift_h.slider1.setValue(this.divdivBellshape1.shift_h);
        this.divdivBellshape_Shift_r.slider1.setValue(this.divdivBellshape1.shift_r);
        this.divdivBellshape_Shift_p.slider1.setValue(this.divdivBellshape1.shift_p);

        this.divdivParameterPanel.add(this.divdivBellshape_Range);
        this.divdivBellshape_Range.setTitle("Range: 100~200");
        this.divdivBellshape_Range.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivBellshape_Range.slider1.setMaximum(200);
        this.divdivBellshape_Range.slider1.setMinimum(100);
        this.divdivBellshape_Range.slider1.setValue(this.divdivBellshape1.range);
        SpinnerNumberModel divdivBellshape_Range = new SpinnerNumberModel(this.divdivBellshape1.range, 100, 200, 1);
        this.divdivBellshape_Range.spinner1.setModel(divdivBellshape_Range);
        this.divdivBellshape_Range.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivBellshape_Vertex);
        this.divdivBellshape_Vertex.setTitle("Vertex: 100~500");
        this.divdivBellshape_Vertex.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivBellshape_Vertex.slider1.setMaximum(500);
        this.divdivBellshape_Vertex.slider1.setMinimum(100);
        this.divdivBellshape_Vertex.slider1.setValue(this.divdivBellshape1.curvevertex);
        SpinnerNumberModel divdivBellshape_Vertex = new SpinnerNumberModel(this.divdivBellshape1.curvevertex, 100, 500, 1);
        this.divdivBellshape_Vertex.spinner1.setModel(divdivBellshape_Vertex);
        this.divdivBellshape_Vertex.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivBellshape_Divisor);
        this.divdivBellshape_Divisor.setTitle("Divisor: 1000~9000");
        this.divdivBellshape_Divisor.slider1.setPreferredSize(new Dimension(110, 20));
        this.divdivBellshape_Divisor.slider1.setMaximum(9000);
        this.divdivBellshape_Divisor.slider1.setMinimum(1000);
        this.divdivBellshape_Divisor.slider1.setValue(this.divdivBellshape1.divisor);
        SpinnerNumberModel divdivBellshape_Divisor = new SpinnerNumberModel(this.divdivBellshape1.divisor, 1000, 9000, 1);
        this.divdivBellshape_Divisor.spinner1.setModel(divdivBellshape_Divisor);
        this.divdivBellshape_Divisor.spinner1.setPreferredSize(new Dimension(60, 20));
        this.divdivBellshape_Divisor.spinner1.addChangeListener(this);

        this.divdivParameterPanel.add(this.divdivBellshapeRecommended);
        this.divdivBellshapeRecommended.setPreferredSize(new Dimension(145, 30));
        this.divdivBellshapeRecommended.setText("Get Recommended");
        this.divdivBellshapeRecommended.addActionListener(this);

        this.divdivShiftPanel.removeAll();
        this.divdivShiftPanel.add(this.divdivBellshape_Shift_h);
        this.divdivBellshape_Shift_h.setTitle("Vertical Shift: -20~20");
        this.divdivBellshape_Shift_h.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivBellshape_Shift_h.slider1.setMaximum(20);
        this.divdivBellshape_Shift_h.slider1.setMinimum(-20);
        this.divdivBellshape_Shift_h.slider1.setValue(this.divdivBellshape1.shift_h);
        SpinnerNumberModel divdivBellshape_Shift_h = new SpinnerNumberModel(this.divdivBellshape1.shift_h, -20, 20, 1);
        this.divdivBellshape_Shift_h.spinner1.setModel(divdivBellshape_Shift_h);
        this.divdivBellshape_Shift_h.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivBellshape_Shift_r);
        this.divdivBellshape_Shift_r.setTitle("Horizontal Shift Distance: 0~50");
        this.divdivBellshape_Shift_r.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivBellshape_Shift_r.slider1.setMaximum(50);
        this.divdivBellshape_Shift_r.slider1.setMinimum(0);
        this.divdivBellshape_Shift_r.slider1.setValue(this.divdivBellshape1.shift_r);
        SpinnerNumberModel divdivBellshape_Shift_r = new SpinnerNumberModel(this.divdivBellshape1.shift_r, 0, 50, 1);
        this.divdivBellshape_Shift_r.spinner1.setModel(divdivBellshape_Shift_r);
        this.divdivBellshape_Shift_r.spinner1.addChangeListener(this);

        this.divdivShiftPanel.add(this.divdivBellshape_Shift_p);
        this.divdivBellshape_Shift_p.setTitle("Horizontal Shift Angle: 0~360");
        this.divdivBellshape_Shift_p.slider1.setPreferredSize(new Dimension(120, 20));
        this.divdivBellshape_Shift_p.slider1.setMaximum(360);
        this.divdivBellshape_Shift_p.slider1.setMinimum(0);
        this.divdivBellshape_Shift_p.slider1.setValue(this.divdivBellshape1.shift_p);
        SpinnerNumberModel divdivBellshape_Shift_p = new SpinnerNumberModel(this.divdivBellshape1.shift_p, 0, 360, 1);
        this.divdivBellshape_Shift_p.spinner1.setModel(divdivBellshape_Shift_p);
        this.divdivBellshape_Shift_p.spinner1.addChangeListener(this);
      }

    }

    if(e.getSource() == this.divdivBellshapeRecommended){
      this.divdivBellshape1 = new DivdivBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), 160, 100, 7500, 0, 0, 0);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.luvcolor);
      this.divdivBellshape_Range.slider1.setValue(160);
      this.divdivBellshape_Vertex.slider1.setValue(100);
      this.divdivBellshape_Divisor.slider1.setValue(7500);
      this.divdivBellshape_Shift_h.slider1.setValue(0);
      this.divdivBellshape_Shift_r.slider1.setValue(0);
      this.divdivBellshape_Shift_p.slider1.setValue(0);
    }

    if (e.getSource() == saveButton) {
      int returnVal = this.fc.showSaveDialog(this.saveButton);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        try{
          DataOutputStream out = new DataOutputStream(new FileOutputStream(file));

          if(this.tabbedPane.getSelectedIndex() == 2){
            out.writeChars("A qualitative/sequential bivariate color scheme.");
            out.writeChar('\n');
            if(this.quaseqConeButton.isSelected() == true){
              out.writeChars("Created by using cone as geometric object.");
              out.writeChar('\n');
              out.writeChars("The height of the cone is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqCone1.height);
              //out.writeChars('');
              out.writeChar('\n');
              out.writeChars("The radius of the cone is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqCone1.radius);
              out.writeChar('\n');
            }
            if(this.quaseqBellshapeButton.isSelected() == true){
              out.writeChars("Created by using bell shape as geometric object.");
              out.writeChar('\n');
              out.writeChars("The vertex of the bell curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqBellshape1.curvevertex);
              out.writeChar('\n');
              out.writeChars("The divisor of the bell curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqBellshape1.divisor);
              out.writeChar('\n');
            }
            if(this.quaseqEllipsoidButton.isSelected() == true){
              out.writeChars("Created by using ellisoid as geometric object.");
              out.writeChar('\n');
              out.writeChars("The semi-axis a & b of the ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqEllipsoid1.a);
              out.writeChar('\n');
              out.writeChars("The semi-axis c of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqEllipsoid1.c);
              out.writeChar('\n');
            }
            if(this.quaseqEllipseCurveButton.isSelected() == true){
              out.writeChars("Created by using ellipse curve as geometric object.");
              out.writeChar('\n');
              out.writeChars("The semi-axis a of the ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqEllipsecurve1.a);
              out.writeChar('\n');
              out.writeChars("The semi-axis b of the ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.quaseqEllipsecurve1.b);
              out.writeChar('\n');
            }
          }

          if(this.tabbedPane.getSelectedIndex() == 3){
            out.writeChars("A diverging/diverging bivariate color scheme.");
            out.writeChar('\n');
            if(this.divdivBellshapeButton.isSelected() == true){
              out.writeChars("Created by using bell shape as geometric object.");
              out.writeChar('\n');
              out.writeChars("The vertex of the bell shape is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivBellshape1.curvevertex);
              out.writeChar('\n');
              out.writeChars("The divisor of the bell shape is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivBellshape1.divisor);
              out.writeChar('\n');
            }
            if(this.divdivEllipsoidButton.isSelected() == true){
              out.writeChars("Created by using ellisoid as geometric object.");
              out.writeChar('\n');
              out.writeChars("The semi-axis a of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivEllipsoid1.a);
              out.writeChar('\n');
              out.writeChars("The semi-axis c of the half ellipsoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivEllipsoid1.c);
              out.writeChar('\n');
            }
            if(this.divdivConeButton.isSelected() == true){
              out.writeChars("Created by using cone as geometric object.");
              out.writeChar('\n');
              out.writeChars("The height of the cone is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivCone1.height);
              out.writeChar('\n');
              out.writeChars("The radius of the cone is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivCone1.radius);
              out.writeChar('\n');
            }
            if(this.divdivEllipseCurveButton.isSelected() == true){
              out.writeChars("Created by using ellipse curve as geometric object.");
              out.writeChar('\n');
              out.writeChars("The semi-axis a of the half ellipse curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivEllipsecurve1.a);
              out.writeChar('\n');
              out.writeChars("The semi-axis b of the half ellipse curve is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divdivEllipsecurve1.b);
              out.writeChar('\n');
            }
          }

          if(this.tabbedPane.getSelectedIndex() == 1){
            out.writeChars("A diverging/sequential bivariate color scheme.");
            out.writeChar('\n');
            if(this.divseqEllipseButton.isSelected() == true){
              out.writeChars("Created by using ellipse as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the ellipse is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqEllipse1.alpha);
              out.writeChar('\n');
              out.writeChars("The shape index of the ellipse is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqEllipse1.e);
              out.writeChar('\n');
            }
            if(this.divseqTrapezoidButton.isSelected() == true){
              out.writeChars("Created by using a trapezoid as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the trapezoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqTrapezoid1.alpha);
              out.writeChar('\n');
              out.writeChars("The bottom radius of the trapezoid is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqTrapezoid1.radius);
              out.writeChar('\n');
            }
            if(this.divseqWedgeButton.isSelected() == true){
              out.writeChars("Created by using a wedge of grids as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the wedge is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqWedge1.alpha);
              out.writeChar('\n');
              out.writeChars("The side angle of the wedge is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqWedge1.beta);
              out.writeChar('\n');
              out.writeChars("The bottom radius of the wedge is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.divseqWedge1.range);
              out.writeChar('\n');
            }
          }

          if(this.tabbedPane.getSelectedIndex() == 0){
            out.writeChars("A sequential/sequential (with gray axis) bivariate color scheme.");
            out.writeChar('\n');
            if(this.seqseqDiamondButton.isSelected() == true){
              out.writeChars("Created by using diamond as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqDiamond1.top_angle);
              out.writeChar('\n');
            }
          }

          if(this.seqseqTiltedDiamondButton.isSelected() == true){
            out.writeChars("A sequential/sequential bivariate color scheme.");
            out.writeChar('\n');
            if(this.seqseqTiltedDiamondButton.isSelected() == true){
              out.writeChars("Created by using tilted diamond as geometric object.");
              out.writeChar('\n');
              out.writeChars("The top angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqTiltedDiamond1.top_angle);
              out.writeChar('\n');
              out.writeChars("The tilt angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqTiltedDiamond1.tilt_angle);
              out.writeChar('\n');
            }
          }

          if(this.seqseqFoldedDiamondButton.isSelected() == true){
            out.writeChars("A sequential/sequential bivariate color scheme.");
            out.writeChar('\n');
            if(this.seqseqFoldedDiamondButton.isSelected() == true){
              out.writeChars("Created by using folded diamond as geometric object.");
              out.writeChar('\n');
              out.writeChars("The side angle of the diamond is: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqFoldedDiamond1.angle);
              out.writeChar('\n');
              out.writeChars("The left wing of the diamond is at: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqFoldedDiamond1.leftwing_position);
              out.writeChar('\n');
              out.writeChars("The right wing of the diamond is at: " + "\t" + "\t" + "\t" + "\t" + "\t" + "\t" + this.seqseqFoldedDiamond1.rightwing_position);
              out.writeChar('\n');
            }
          }











          out.close();
        }

        catch(IOException ioe){
          System.err.println("Caught IOException: " + ioe.getMessage());
        }
      }
    }


    this.repaint();
    this.revalidate();

  }

  public void stateChanged(ChangeEvent e){

    //SeqseqDiamond model
    if(e.getSource() == this.seqseqDiamondLightnessRange.leftSpinner || e.getSource() == this.seqseqDiamondLightnessRange.rightSpinner || e.getSource() == this.seqseqDiamond_TopAngle.spinner1 || e.getSource() == this.seqseqDiamond_Shift_h.spinner1 || e.getSource() == this.seqseqDiamond_Shift_r.spinner1 || e.getSource() == this.seqseqDiamond_Shift_p.spinner1){
      this.seqseqDiamond1.minlightness = Integer.valueOf(String.valueOf(this.seqseqDiamondLightnessRange.leftSpinner.getValue())).intValue();
      this.seqseqDiamond1.maxlightness = Integer.valueOf(String.valueOf(this.seqseqDiamondLightnessRange.rightSpinner.getValue())).intValue();
      this.seqseqDiamond1.top_angle = Integer.valueOf(String.valueOf(this.seqseqDiamond_TopAngle.spinner1.getValue())).intValue();
      this.seqseqDiamond1.shift_h = Integer.valueOf(String.valueOf(this.seqseqDiamond_Shift_h.spinner1.getValue())).intValue();
      this.seqseqDiamond1.shift_r = Integer.valueOf(String.valueOf(this.seqseqDiamond_Shift_r.spinner1.getValue())).intValue();
      this.seqseqDiamond1.shift_p = Integer.valueOf(String.valueOf(this.seqseqDiamond_Shift_p.spinner1.getValue())).intValue();
      this.seqseqDiamond1 = new SeqseqDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.maxlightness, this.seqseqDiamond1.minlightness, this.seqseqDiamond1.top_angle, this.seqseqDiamond1.shift_h, this.seqseqDiamond1.shift_r, this.seqseqDiamond1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.luvcolor);
    }

    //Seqseq_TiltedDiamond model
    if(e.getSource() == this.seqseqTiltedDiamondLightnessRange.leftSpinner || e.getSource() == this.seqseqTiltedDiamondLightnessRange.rightSpinner || e.getSource() == this.seqseqTiltedDiamond_TopAngle.spinner1 || e.getSource() == this.seqseqTiltedDiamond_TiltAngle.spinner1 || e.getSource() == this.seqseqTiltedDiamond_Shift_h.spinner1 || e.getSource() == this.seqseqTiltedDiamond_Shift_r.spinner1 || e.getSource() == this.seqseqTiltedDiamond_Shift_p.spinner1){
      this.seqseqTiltedDiamond1.minlightness = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamondLightnessRange.leftSpinner.getValue())).intValue();
      this.seqseqTiltedDiamond1.maxlightness = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamondLightnessRange.rightSpinner.getValue())).intValue();
      this.seqseqTiltedDiamond1.top_angle = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamond_TopAngle.spinner1.getValue())).intValue();
      this.seqseqTiltedDiamond1.tilt_angle = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamond_TiltAngle.spinner1.getValue())).intValue();
      this.seqseqTiltedDiamond1.shift_h = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamond_Shift_h.spinner1.getValue())).intValue();
      this.seqseqTiltedDiamond1.shift_r = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamond_Shift_r.spinner1.getValue())).intValue();
      this.seqseqTiltedDiamond1.shift_p = Integer.valueOf(String.valueOf(this.seqseqTiltedDiamond_Shift_p.spinner1.getValue())).intValue();
      this.seqseqTiltedDiamond1 = new SeqseqTiltedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.maxlightness, this.seqseqTiltedDiamond1.minlightness, this.seqseqTiltedDiamond1.top_angle, this.seqseqTiltedDiamond1.tilt_angle, this.seqseqTiltedDiamond1.shift_h, this.seqseqTiltedDiamond1.shift_r, this.seqseqTiltedDiamond1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.luvcolor);
    }

    //Seqseq_FoldedDiamond model
    if(e.getSource() == this.seqseqFoldedDiamondLightnessRange.leftSpinner || e.getSource() == this.seqseqFoldedDiamondLightnessRange.rightSpinner || e.getSource() == this.seqseqFoldedDiamond_Angle.spinner1 || e.getSource() == this.seqseqFoldedDiamond_Leftwing.spinner1 || e.getSource() == this.seqseqFoldedDiamond_Rightwing.spinner1 || e.getSource() == this.seqseqFoldedDiamond_Shift_h.spinner1 || e.getSource() == this.seqseqFoldedDiamond_Shift_r.spinner1 || e.getSource() == this.seqseqFoldedDiamond_Shift_p.spinner1){
      this.seqseqFoldedDiamond1.minlightness = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamondLightnessRange.leftSpinner.getValue())).intValue();
      this.seqseqFoldedDiamond1.maxlightness = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamondLightnessRange.rightSpinner.getValue())).intValue();
      this.seqseqFoldedDiamond1.angle = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamond_Angle.spinner1.getValue())).intValue();
      this.seqseqFoldedDiamond1.leftwing_position = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamond_Leftwing.spinner1.getValue())).intValue();
      this.seqseqFoldedDiamond1.rightwing_position = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamond_Rightwing.spinner1.getValue())).intValue();
      this.seqseqFoldedDiamond1.shift_h = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamond_Shift_h.spinner1.getValue())).intValue();
      this.seqseqFoldedDiamond1.shift_r = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamond_Shift_r.spinner1.getValue())).intValue();
      this.seqseqFoldedDiamond1.shift_p = Integer.valueOf(String.valueOf(this.seqseqFoldedDiamond_Shift_p.spinner1.getValue())).intValue();
      this.seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.maxlightness, this.seqseqFoldedDiamond1.minlightness, this.seqseqFoldedDiamond1.angle, this.seqseqFoldedDiamond1.leftwing_position, this.seqseqFoldedDiamond1.rightwing_position, this.seqseqFoldedDiamond1.shift_h, this.seqseqFoldedDiamond1.shift_r, this.seqseqFoldedDiamond1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.luvcolor);
    }

    //DivseqWedge model
    if(e.getSource() == this.divseqWedgeLightnessRange.leftSpinner || e.getSource() == this.divseqWedgeLightnessRange.rightSpinner || e.getSource() == this.divseqWedge_Alpha.spinner1 || e.getSource() == this.divseqWedge_Beta.spinner1 || e.getSource() == this.divseqWedge_Range.spinner1 || e.getSource() == this.divseqWedge_Shift_h.spinner1 || e.getSource() == this.divseqWedge_Shift_r.spinner1 || e.getSource() == this.divseqWedge_Shift_p.spinner1){
      this.divseqWedge1.minlightness = Integer.valueOf(String.valueOf(this.divseqWedgeLightnessRange.leftSpinner.getValue())).intValue();
      this.divseqWedge1.maxlightness = Integer.valueOf(String.valueOf(this.divseqWedgeLightnessRange.rightSpinner.getValue())).intValue();
      this.divseqWedge1.alpha = Integer.valueOf(String.valueOf(this.divseqWedge_Alpha.spinner1.getValue())).intValue();
      this.divseqWedge1.beta = Integer.valueOf(String.valueOf(this.divseqWedge_Beta.spinner1.getValue())).intValue();
      this.divseqWedge1.range = Integer.valueOf(String.valueOf(this.divseqWedge_Range.spinner1.getValue())).intValue();
      this.divseqWedge1.shift_h = Integer.valueOf(String.valueOf(this.divseqWedge_Shift_h.spinner1.getValue())).intValue();
      this.divseqWedge1.shift_r = Integer.valueOf(String.valueOf(this.divseqWedge_Shift_r.spinner1.getValue())).intValue();
      this.divseqWedge1.shift_p = Integer.valueOf(String.valueOf(this.divseqWedge_Shift_p.spinner1.getValue())).intValue();
      this.divseqWedge1 = new DivseqWedge(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.maxlightness, this.divseqWedge1.minlightness, this.divseqWedge1.alpha, this.divseqWedge1.beta, this.divseqWedge1.range, this.divseqWedge1.shift_h, this.divseqWedge1.shift_r, this.divseqWedge1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.luvcolor);
    }

    //DivseqEllipse model
    if(e.getSource() == this.divseqEllipseLightnessRange.leftSpinner || e.getSource() == this.divseqEllipseLightnessRange.rightSpinner || e.getSource() == this.divseqEllipse_Alpha.spinner1 || e.getSource() == this.divseqEllipse_E.spinner1 || e.getSource() == this.divseqEllipse_Shift_h.spinner1 || e.getSource() == this.divseqEllipse_Shift_r.spinner1 || e.getSource() == this.divseqEllipse_Shift_p.spinner1){
      this.divseqEllipse1.minlightness = Integer.valueOf(String.valueOf(this.divseqEllipseLightnessRange.leftSpinner.getValue())).intValue();
      this.divseqEllipse1.maxlightness = Integer.valueOf(String.valueOf(this.divseqEllipseLightnessRange.rightSpinner.getValue())).intValue();
      this.divseqEllipse1.alpha = Integer.valueOf(String.valueOf(this.divseqEllipse_Alpha.spinner1.getValue())).intValue();
      this.divseqEllipse1.e = Integer.valueOf(String.valueOf(this.divseqEllipse_E.spinner1.getValue())).intValue();
      this.divseqEllipse1.shift_h = Integer.valueOf(String.valueOf(this.divseqEllipse_Shift_h.spinner1.getValue())).intValue();
      this.divseqEllipse1.shift_r = Integer.valueOf(String.valueOf(this.divseqEllipse_Shift_r.spinner1.getValue())).intValue();
      this.divseqEllipse1.shift_p = Integer.valueOf(String.valueOf(this.divseqEllipse_Shift_p.spinner1.getValue())).intValue();
      this.divseqEllipse1 = new DivseqEllipse(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.maxlightness, this.divseqEllipse1.minlightness, this.divseqEllipse1.alpha, this.divseqEllipse1.e, this.divseqEllipse1.shift_h, this.divseqEllipse1.shift_r, this.divseqEllipse1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.luvcolor);
    }

    //DivseqTrapezoid model
    if(e.getSource() == this.divseqTrapezoidLightnessRange.leftSpinner || e.getSource() == this.divseqTrapezoidLightnessRange.rightSpinner || e.getSource() == this.divseqTrapezoid_Alpha.spinner1 || e.getSource() == this.divseqTrapezoid_Radius.spinner1 || e.getSource() == this.divseqTrapezoid_Shift_h.spinner1 || e.getSource() == this.divseqTrapezoid_Shift_r.spinner1 || e.getSource() == this.divseqTrapezoid_Shift_p.spinner1){
      this.divseqTrapezoid1.minlightness = Integer.valueOf(String.valueOf(this.divseqTrapezoidLightnessRange.leftSpinner.getValue())).intValue();
      this.divseqTrapezoid1.maxlightness = Integer.valueOf(String.valueOf(this.divseqTrapezoidLightnessRange.rightSpinner.getValue())).intValue();
      this.divseqTrapezoid1.alpha = Integer.valueOf(String.valueOf(this.divseqTrapezoid_Alpha.spinner1.getValue())).intValue();
      this.divseqTrapezoid1.radius = Integer.valueOf(String.valueOf(this.divseqTrapezoid_Radius.spinner1.getValue())).intValue();
      this.divseqTrapezoid1.shift_h = Integer.valueOf(String.valueOf(this.divseqTrapezoid_Shift_h.spinner1.getValue())).intValue();
      this.divseqTrapezoid1.shift_r = Integer.valueOf(String.valueOf(this.divseqTrapezoid_Shift_r.spinner1.getValue())).intValue();
      this.divseqTrapezoid1.shift_p = Integer.valueOf(String.valueOf(this.divseqTrapezoid_Shift_p.spinner1.getValue())).intValue();
      this.divseqTrapezoid1 = new DivseqTrapezoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.maxlightness, this.divseqTrapezoid1.minlightness, this.divseqTrapezoid1.alpha, this.divseqTrapezoid1.radius, this.divseqTrapezoid1.shift_h, this.divseqTrapezoid1.shift_r, this.divseqTrapezoid1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.luvcolor);
    }

    //QuaseqCone model
    if(e.getSource() == this.quaseqConeLightnessRange.leftSpinner || e.getSource() == this.quaseqConeLightnessRange.rightSpinner || e.getSource() == this.quaseqCone_Height.spinner1 || e.getSource() == this.quaseqCone_Radius.spinner1 || e.getSource() == this.quaseqCone_Shift_h.spinner1 || e.getSource() == this.quaseqCone_Shift_r.spinner1 || e.getSource() == this.quaseqCone_Shift_p.spinner1){
      this.quaseqCone1.minlightness = Integer.valueOf(String.valueOf(this.quaseqConeLightnessRange.leftSpinner.getValue())).intValue();
      this.quaseqCone1.maxlightness = Integer.valueOf(String.valueOf(this.quaseqConeLightnessRange.rightSpinner.getValue())).intValue();
      this.quaseqCone1.height = Integer.valueOf(String.valueOf(this.quaseqCone_Height.spinner1.getValue())).intValue();
      this.quaseqCone1.radius = Integer.valueOf(String.valueOf(this.quaseqCone_Radius.spinner1.getValue())).intValue();
      this.quaseqCone1.shift_h = Integer.valueOf(String.valueOf(this.quaseqCone_Shift_h.spinner1.getValue())).intValue();
      this.quaseqCone1.shift_r = Integer.valueOf(String.valueOf(this.quaseqCone_Shift_r.spinner1.getValue())).intValue();
      this.quaseqCone1.shift_p = Integer.valueOf(String.valueOf(this.quaseqCone_Shift_p.spinner1.getValue())).intValue();
      this.quaseqCone1 = new QuaseqCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.maxlightness, this.quaseqCone1.minlightness, this.quaseqCone1.height, this.quaseqCone1.radius, this.quaseqCone1.shift_h, this.quaseqCone1.shift_r, this.quaseqCone1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.luvcolor);
    }

    //QuaseqEllipsoid model
    if(e.getSource() == this.quaseqEllipsoidLightnessRange.leftSpinner || e.getSource() == this.quaseqEllipsoidLightnessRange.rightSpinner || e.getSource() == this.quaseqEllipsoid_Semiaxis_ab.spinner1 || e.getSource() == this.quaseqEllipsoid_Semiaxis_c.spinner1 || e.getSource() == this.quaseqEllipsoid_Shift_h.spinner1 || e.getSource() == this.quaseqEllipsoid_Shift_r.spinner1 || e.getSource() == this.quaseqEllipsoid_Shift_p.spinner1){
      this.quaseqEllipsoid1.minlightness = Integer.valueOf(String.valueOf(this.quaseqEllipsoidLightnessRange.leftSpinner.getValue())).intValue();
      this.quaseqEllipsoid1.maxlightness = Integer.valueOf(String.valueOf(this.quaseqEllipsoidLightnessRange.rightSpinner.getValue())).intValue();
      this.quaseqEllipsoid1.a = Integer.valueOf(String.valueOf(this.quaseqEllipsoid_Semiaxis_ab.spinner1.getValue())).intValue();
      this.quaseqEllipsoid1.c = Integer.valueOf(String.valueOf(this.quaseqEllipsoid_Semiaxis_c.spinner1.getValue())).intValue();
      this.quaseqEllipsoid1.shift_h = Integer.valueOf(String.valueOf(this.quaseqEllipsoid_Shift_h.spinner1.getValue())).intValue();
      this.quaseqEllipsoid1.shift_r = Integer.valueOf(String.valueOf(this.quaseqEllipsoid_Shift_r.spinner1.getValue())).intValue();
      this.quaseqEllipsoid1.shift_p = Integer.valueOf(String.valueOf(this.quaseqEllipsoid_Shift_p.spinner1.getValue())).intValue();
      this.quaseqEllipsoid1 = new QuaseqEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.maxlightness, this.quaseqEllipsoid1.minlightness, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.c, this.quaseqEllipsoid1.shift_h, this.quaseqEllipsoid1.shift_r, this.quaseqEllipsoid1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.luvcolor);
    }

    //QuaseqEllipsecurve model
    if(e.getSource() == this.quaseqEllipseCurveLightnessRange.leftSpinner || e.getSource() == this.quaseqEllipseCurveLightnessRange.rightSpinner || e.getSource() == this.quaseqEllipseCurve_Semiaxis_a.spinner1 || e.getSource() == this.quaseqEllipseCurve_Semiaxis_b.spinner1 || e.getSource() == this.quaseqEllipseCurve_Shift_h.spinner1 || e.getSource() == this.quaseqEllipseCurve_Shift_r.spinner1 || e.getSource() == this.quaseqEllipseCurve_Shift_p.spinner1){
      this.quaseqEllipsecurve1.minlightness = Integer.valueOf(String.valueOf(this.quaseqEllipseCurveLightnessRange.leftSpinner.getValue())).intValue();
      this.quaseqEllipsecurve1.maxlightness = Integer.valueOf(String.valueOf(this.quaseqEllipseCurveLightnessRange.rightSpinner.getValue())).intValue();
      this.quaseqEllipsecurve1.a = Integer.valueOf(String.valueOf(this.quaseqEllipseCurve_Semiaxis_a.spinner1.getValue())).intValue();
      this.quaseqEllipsecurve1.b = Integer.valueOf(String.valueOf(this.quaseqEllipseCurve_Semiaxis_b.spinner1.getValue())).intValue();
      this.quaseqEllipsecurve1.shift_h = Integer.valueOf(String.valueOf(this.quaseqEllipseCurve_Shift_h.spinner1.getValue())).intValue();
      this.quaseqEllipsecurve1.shift_r = Integer.valueOf(String.valueOf(this.quaseqEllipseCurve_Shift_r.spinner1.getValue())).intValue();
      this.quaseqEllipsecurve1.shift_p = Integer.valueOf(String.valueOf(this.quaseqEllipseCurve_Shift_p.spinner1.getValue())).intValue();
      this.quaseqEllipsecurve1 = new QuaseqEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.maxlightness, this.quaseqEllipsecurve1.minlightness, this.quaseqEllipsecurve1.a, this.quaseqEllipsecurve1.b, this.quaseqEllipsecurve1.shift_h, this.quaseqEllipsecurve1.shift_r, this.quaseqEllipsecurve1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.luvcolor);
    }

    //QuaseqBellshape model
    if(e.getSource() == this.quaseqBellshapeLightnessRange.leftSpinner || e.getSource() == this.quaseqBellshapeLightnessRange.rightSpinner || e.getSource() == this.quaseqBellshape_Vertex.spinner1 || e.getSource() == this.quaseqBellshape_Divisor.spinner1 || e.getSource() == this.quaseqBellshape_Shift_h.spinner1 || e.getSource() == this.quaseqBellshape_Shift_r.spinner1 || e.getSource() == this.quaseqBellshape_Shift_p.spinner1){
      this.quaseqBellshape1.minlightness = Integer.valueOf(String.valueOf(this.quaseqBellshapeLightnessRange.leftSpinner.getValue())).intValue();
      this.quaseqBellshape1.maxlightness = Integer.valueOf(String.valueOf(this.quaseqBellshapeLightnessRange.rightSpinner.getValue())).intValue();
      this.quaseqBellshape1.curvevertex = Integer.valueOf(String.valueOf(this.quaseqBellshape_Vertex.spinner1.getValue())).intValue();
      this.quaseqBellshape1.divisor = Integer.valueOf(String.valueOf(this.quaseqBellshape_Divisor.spinner1.getValue())).intValue();
      this.quaseqBellshape1.shift_h = Integer.valueOf(String.valueOf(this.quaseqBellshape_Shift_h.spinner1.getValue())).intValue();
      this.quaseqBellshape1.shift_r = Integer.valueOf(String.valueOf(this.quaseqBellshape_Shift_r.spinner1.getValue())).intValue();
      this.quaseqBellshape1.shift_p = Integer.valueOf(String.valueOf(this.quaseqBellshape_Shift_p.spinner1.getValue())).intValue();
      this.quaseqBellshape1 = new QuaseqBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.maxlightness, this.quaseqBellshape1.minlightness, this.quaseqBellshape1.curvevertex, this.quaseqBellshape1.divisor, this.quaseqBellshape1.shift_h, this.quaseqBellshape1.shift_r, this.quaseqBellshape1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.luvcolor);
    }

    //DivdivCone model
    if(e.getSource() == this.divdivCone_Range.spinner1|| e.getSource() == this.divdivCone_Height.spinner1 || e.getSource() == this.divdivCone_Radius.spinner1 || e.getSource() == this.divdivCone_Shift_h.spinner1 || e.getSource() == this.divdivCone_Shift_r.spinner1 || e.getSource() == this.divdivCone_Shift_p.spinner1){
      this.divdivCone1.range = Integer.valueOf(String.valueOf(this.divdivCone_Range.spinner1.getValue())).intValue();
      this.divdivCone1.height = Integer.valueOf(String.valueOf(this.divdivCone_Height.spinner1.getValue())).intValue();
      this.divdivCone1.radius = Integer.valueOf(String.valueOf(this.divdivCone_Radius.spinner1.getValue())).intValue();
      this.divdivCone1.shift_h = Integer.valueOf(String.valueOf(this.divdivCone_Shift_h.spinner1.getValue())).intValue();
      this.divdivCone1.shift_r = Integer.valueOf(String.valueOf(this.divdivCone_Shift_r.spinner1.getValue())).intValue();
      this.divdivCone1.shift_p = Integer.valueOf(String.valueOf(this.divdivCone_Shift_p.spinner1.getValue())).intValue();
      this.divdivCone1 = new DivdivCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.range, this.divdivCone1.height, this.divdivCone1.radius, this.divdivCone1.shift_h, this.divdivCone1.shift_r, this.divdivCone1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.luvcolor);
    }

    //DivdivEllipsoid model
    if(e.getSource() == this.divdivEllipsoid_Range.spinner1 || e.getSource() == this.divdivEllipsoid_Semiaxis_ab.spinner1 || e.getSource() == this.divdivEllipsoid_Semiaxis_c.spinner1 || e.getSource() == this.divdivEllipsoid_Shift_h.spinner1 || e.getSource() == this.divdivEllipsoid_Shift_r.spinner1 || e.getSource() == this.divdivEllipsoid_Shift_p.spinner1){
      this.divdivEllipsoid1.range = Integer.valueOf(String.valueOf(this.divdivEllipsoid_Range.spinner1.getValue())).intValue();
      this.divdivEllipsoid1.a = Integer.valueOf(String.valueOf(this.divdivEllipsoid_Semiaxis_ab.spinner1.getValue())).intValue();
      this.divdivEllipsoid1.c = Integer.valueOf(String.valueOf(this.divdivEllipsoid_Semiaxis_c.spinner1.getValue())).intValue();
      this.divdivEllipsoid1.shift_h = Integer.valueOf(String.valueOf(this.divdivEllipsoid_Shift_h.spinner1.getValue())).intValue();
      this.divdivEllipsoid1.shift_r = Integer.valueOf(String.valueOf(this.divdivEllipsoid_Shift_r.spinner1.getValue())).intValue();
      this.divdivEllipsoid1.shift_p = Integer.valueOf(String.valueOf(this.divdivEllipsoid_Shift_p.spinner1.getValue())).intValue();
      this.divdivEllipsoid1 = new DivdivEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.range, this.divdivEllipsoid1.a, this.divdivEllipsoid1.c, this.divdivEllipsoid1.shift_h, this.divdivEllipsoid1.shift_r, this.divdivEllipsoid1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.luvcolor);
    }

    //DivdivEllipsecurve model
    if(e.getSource() == this.divdivEllipseCurve_Range.spinner1 || e.getSource() == this.divdivEllipseCurve_Semiaxis_a.spinner1 || e.getSource() == this.divdivEllipseCurve_Semiaxis_b.spinner1 || e.getSource() == this.divdivEllipseCurve_Shift_h.spinner1 || e.getSource() == this.divdivEllipseCurve_Shift_r.spinner1 || e.getSource() == this.divdivEllipseCurve_Shift_p.spinner1){
      this.divdivEllipsecurve1.range = Integer.valueOf(String.valueOf(this.divdivEllipseCurve_Range.spinner1.getValue())).intValue();
      this.divdivEllipsecurve1.a = Integer.valueOf(String.valueOf(this.divdivEllipseCurve_Semiaxis_a.spinner1.getValue())).intValue();
      this.divdivEllipsecurve1.b = Integer.valueOf(String.valueOf(this.divdivEllipseCurve_Semiaxis_b.spinner1.getValue())).intValue();
      this.divdivEllipsecurve1.shift_h = Integer.valueOf(String.valueOf(this.divdivEllipseCurve_Shift_h.spinner1.getValue())).intValue();
      this.divdivEllipsecurve1.shift_r = Integer.valueOf(String.valueOf(this.divdivEllipseCurve_Shift_r.spinner1.getValue())).intValue();
      this.divdivEllipsecurve1.shift_p = Integer.valueOf(String.valueOf(this.divdivEllipseCurve_Shift_p.spinner1.getValue())).intValue();
      this.divdivEllipsecurve1 = new DivdivEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.range, this.divdivEllipsecurve1.a, this.divdivEllipsecurve1.b, this.divdivEllipsecurve1.shift_h, this.divdivEllipsecurve1.shift_r, this.divdivEllipsecurve1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.luvcolor);
    }

    //DivdivBellshape model
    if(e.getSource() == this.divdivBellshape_Range.spinner1 || e.getSource() == this.divdivBellshape_Vertex.spinner1 || e.getSource() == this.divdivBellshape_Divisor.spinner1 || e.getSource() == this.divdivBellshape_Shift_h.spinner1 || e.getSource() == this.divdivBellshape_Shift_r.spinner1 || e.getSource() == this.divdivBellshape_Shift_p.spinner1){
      this.divdivBellshape1.range = Integer.valueOf(String.valueOf(this.divdivBellshape_Range.spinner1.getValue())).intValue();
      this.divdivBellshape1.curvevertex = Integer.valueOf(String.valueOf(this.divdivBellshape_Vertex.spinner1.getValue())).intValue();
      this.divdivBellshape1.divisor = Integer.valueOf(String.valueOf(this.divdivBellshape_Divisor.spinner1.getValue())).intValue();
      this.divdivBellshape1.shift_h = Integer.valueOf(String.valueOf(this.divdivBellshape_Shift_h.spinner1.getValue())).intValue();
      this.divdivBellshape1.shift_r = Integer.valueOf(String.valueOf(this.divdivBellshape_Shift_r.spinner1.getValue())).intValue();
      this.divdivBellshape1.shift_p = Integer.valueOf(String.valueOf(this.divdivBellshape_Shift_p.spinner1.getValue())).intValue();
      this.divdivBellshape1 = new DivdivBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.range, this.divdivBellshape1.curvevertex, this.divdivBellshape1.divisor, this.divdivBellshape1.shift_h, this.divdivBellshape1.shift_r, this.divdivBellshape1.shift_p);
      this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.luvcolor);
    }


    //horizontal and vertical number of classes
    for(int i = 0; i < 3; i ++){
      if(e.getSource() == this.horizontalClass[i].spinner1 || e.getSource() == this.verticalClass[i].spinner1){
        this.checkbox[i].setSelected(true);
        int m = Integer.valueOf(String.valueOf(this.verticalClass[i].spinner1.getValue())).intValue();
        int n = Integer.valueOf(String.valueOf(this.horizontalClass[i].spinner1.getValue())).intValue();

        if(this.getCurrentGeometricModel() == "SeqseqDiamond"){
          this.seqseqDiamond1 = new SeqseqDiamond(m, n, this.seqseqDiamond1.maxlightness, this.seqseqDiamond1.minlightness, this.seqseqDiamond1.top_angle, this.seqseqDiamond1.shift_h, this.seqseqDiamond1.shift_r, this.seqseqDiamond1.shift_p);
          this.createPatches(m, n, this.seqseqDiamond1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "SeqseqTiltedDiamond"){
          this.seqseqTiltedDiamond1 = new SeqseqTiltedDiamond(m, n, this.seqseqTiltedDiamond1.maxlightness, this.seqseqTiltedDiamond1.minlightness, this.seqseqTiltedDiamond1.top_angle, this.seqseqTiltedDiamond1.tilt_angle, this.seqseqTiltedDiamond1.shift_h, this.seqseqTiltedDiamond1.shift_r, this.seqseqTiltedDiamond1.shift_p);
          this.createPatches(m, n, this.seqseqTiltedDiamond1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "SeqseqFoldedDiamond"){
          this.seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(m, n, this.seqseqFoldedDiamond1.maxlightness, this.seqseqFoldedDiamond1.minlightness, this.seqseqFoldedDiamond1.angle, this.seqseqFoldedDiamond1.leftwing_position, this.seqseqFoldedDiamond1.rightwing_position, this.seqseqFoldedDiamond1.shift_h, this.seqseqFoldedDiamond1.shift_r, this.seqseqFoldedDiamond1.shift_p);
          this.createPatches(m, n, this.seqseqFoldedDiamond1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivseqWedge"){
          this.divseqWedge1 = new DivseqWedge(m, n, this.divseqWedge1.maxlightness, this.divseqWedge1.minlightness, this.divseqWedge1.alpha, this.divseqWedge1.beta, this.divseqWedge1.range, this.divseqWedge1.shift_h, this.divseqWedge1.shift_r, this.divseqWedge1.shift_p);
          this.createPatches(m, n, this.divseqWedge1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivseqEllipse"){
          this.divseqEllipse1 = new DivseqEllipse(m, n, this.divseqEllipse1.maxlightness, this.divseqEllipse1.minlightness, this.divseqEllipse1.alpha, this.divseqEllipse1.e, this.divseqEllipse1.shift_h, this.divseqEllipse1.shift_r, this.divseqEllipse1.shift_p);
          this.createPatches(m, n, this.divseqEllipse1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivseqTrapezoid"){
          this.divseqTrapezoid1 = new DivseqTrapezoid(m, n, this.divseqTrapezoid1.maxlightness, this.divseqTrapezoid1.minlightness, this.divseqTrapezoid1.alpha, this.divseqTrapezoid1.radius, this.divseqTrapezoid1.shift_h, this.divseqTrapezoid1.shift_r, this.divseqTrapezoid1.shift_p);
          this.createPatches(m, n, this.divseqTrapezoid1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "QuaseqCone"){
          this.quaseqCone1 = new QuaseqCone(m, n, this.quaseqCone1.maxlightness, this.quaseqCone1.minlightness, this.quaseqCone1.height, this.quaseqCone1.radius, this.quaseqCone1.shift_h, this.quaseqCone1.shift_r, this.quaseqCone1.shift_p);
          this.createPatches(m, n, this.quaseqCone1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "QuaseqEllipsoid"){
          this.quaseqEllipsoid1 = new QuaseqEllipsoid(m, n, this.quaseqEllipsoid1.maxlightness, this.quaseqEllipsoid1.minlightness, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.c, this.quaseqEllipsoid1.shift_h, this.quaseqEllipsoid1.shift_r, this.quaseqEllipsoid1.shift_p);
          this.createPatches(m, n, this.quaseqEllipsoid1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "QuaseqEllipsecurve"){
          this.quaseqEllipsecurve1 = new QuaseqEllipsecurve(m, n, this.quaseqEllipsecurve1.maxlightness, this.quaseqEllipsecurve1.minlightness, this.quaseqEllipsecurve1.a, this.quaseqEllipsecurve1.b, this.quaseqEllipsecurve1.shift_h, this.quaseqEllipsecurve1.shift_r, this.quaseqEllipsecurve1.shift_p);
          this.createPatches(m, n, this.quaseqEllipsecurve1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "QuaseqBellshape"){
          this.quaseqBellshape1 = new QuaseqBellshape(m, n, this.quaseqBellshape1.maxlightness, this.quaseqBellshape1.minlightness, this.quaseqBellshape1.curvevertex, this.quaseqBellshape1.divisor, this.quaseqBellshape1.shift_h, this.quaseqBellshape1.shift_r, this.quaseqBellshape1.shift_p);
          this.createPatches(m, n, this.quaseqBellshape1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivdivCone"){
          this.divdivCone1 = new DivdivCone(m, n, this.divdivCone1.range, this.divdivCone1.height, this.divdivCone1.radius, this.divdivCone1.shift_h, this.divdivCone1.shift_r, this.divdivCone1.shift_p);
          this.createPatches(m, n, this.divdivCone1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivdivEllipsoid"){
          this.divdivEllipsoid1 = new DivdivEllipsoid(m, n, this.divdivEllipsoid1.range, this.divdivEllipsoid1.a, this.divdivEllipsoid1.c, this.divdivEllipsoid1.shift_h, this.divdivEllipsoid1.shift_r, this.divdivEllipsoid1.shift_p);
          this.createPatches(m, n, this.divdivEllipsoid1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivdivEllipsecurve"){
          this.divdivEllipsecurve1 = new DivdivEllipsecurve(m, n, this.divdivEllipsecurve1.range, this.divdivEllipsecurve1.a, this.divdivEllipsecurve1.b, this.divdivEllipsecurve1.shift_h, this.divdivEllipsecurve1.shift_r, this.divdivEllipsecurve1.shift_p);
          this.createPatches(m, n, this.divdivEllipsecurve1.luvcolor);
        }
        if(this.getCurrentGeometricModel() == "DivdivBellshape"){
          this.divdivBellshape1 = new DivdivBellshape(m, n, this.divdivBellshape1.range, this.divdivBellshape1.curvevertex, this.divdivBellshape1.divisor, this.divdivBellshape1.shift_h, this.divdivBellshape1.shift_r, this.divdivBellshape1.shift_p);
          this.createPatches(m, n, this.divdivBellshape1.luvcolor);
        }
      }
    }


    if(e.getSource() == this.tabbedPane){

      if(this.tabbedPane.getSelectedIndex() == 0){
        if(this.seqseqDiamondButton.isSelected()){
          this.seqseqDiamond1 = new SeqseqDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.maxlightness, this.seqseqDiamond1.minlightness, this.seqseqDiamond1.top_angle, this.seqseqDiamond1.shift_h, this.seqseqDiamond1.shift_r, this.seqseqDiamond1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqDiamond1.luvcolor);
        }
        if(this.seqseqTiltedDiamondButton.isSelected()){
          this.seqseqTiltedDiamond1 = new SeqseqTiltedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.maxlightness, this.seqseqTiltedDiamond1.minlightness, this.seqseqTiltedDiamond1.top_angle, this.seqseqTiltedDiamond1.tilt_angle, this.seqseqTiltedDiamond1.shift_h, this.seqseqTiltedDiamond1.shift_r, this.seqseqTiltedDiamond1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqTiltedDiamond1.luvcolor);
        }
        if(this.seqseqFoldedDiamondButton.isSelected()){
          this.seqseqFoldedDiamond1 = new SeqseqFoldedDiamond(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.maxlightness, this.seqseqFoldedDiamond1.minlightness, this.seqseqFoldedDiamond1.angle, this.seqseqFoldedDiamond1.leftwing_position, this.seqseqFoldedDiamond1.rightwing_position, this.seqseqFoldedDiamond1.shift_h, this.seqseqFoldedDiamond1.shift_r, this.seqseqFoldedDiamond1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.seqseqFoldedDiamond1.luvcolor);
        }
      }

      if(this.tabbedPane.getSelectedIndex() == 1){
        if(this.divseqWedgeButton.isSelected()){
          this.divseqWedge1 = new DivseqWedge(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.maxlightness, this.divseqWedge1.minlightness, this.divseqWedge1.alpha, this.divseqWedge1.beta, this.divseqWedge1.range, this.divseqWedge1.shift_h, this.divseqWedge1.shift_r, this.divseqWedge1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqWedge1.luvcolor);
        }
        if(this.divseqEllipseButton.isSelected()){
          this.divseqEllipse1 = new DivseqEllipse(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.maxlightness, this.divseqEllipse1.minlightness, this.divseqEllipse1.alpha, this.divseqEllipse1.e, this.divseqEllipse1.shift_h, this.divseqEllipse1.shift_r, this.divseqEllipse1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqEllipse1.luvcolor);
        }
        if(this.divseqTrapezoidButton.isSelected()){
          this.divseqTrapezoid1 = new DivseqTrapezoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.maxlightness, this.divseqTrapezoid1.minlightness, this.divseqTrapezoid1.alpha, this.divseqTrapezoid1.radius, this.divseqTrapezoid1.shift_h, this.divseqTrapezoid1.shift_r, this.divseqTrapezoid1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divseqTrapezoid1.luvcolor);
        }
      }

      if(this.tabbedPane.getSelectedIndex() == 2){
        if(this.quaseqConeButton.isSelected()){
          this.quaseqCone1 = new QuaseqCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.maxlightness, this.quaseqCone1.minlightness, this.quaseqCone1.height, this.quaseqCone1.radius, this.quaseqCone1.shift_h, this.quaseqCone1.shift_r, this.quaseqCone1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqCone1.luvcolor);
        }
        if(this.quaseqEllipsoidButton.isSelected()){
          this.quaseqEllipsoid1 = new QuaseqEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.maxlightness, this.quaseqEllipsoid1.minlightness, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.a, this.quaseqEllipsoid1.c, this.quaseqEllipsoid1.shift_h, this.quaseqEllipsoid1.shift_r, this.quaseqEllipsoid1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsoid1.luvcolor);
        }
        if(this.quaseqEllipseCurveButton.isSelected()){
          this.quaseqEllipsecurve1 = new QuaseqEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.maxlightness, this.quaseqEllipsecurve1.minlightness, this.quaseqEllipsecurve1.a, this.quaseqEllipsecurve1.b, this.quaseqEllipsecurve1.shift_h, this.quaseqEllipsecurve1.shift_r, this.quaseqEllipsecurve1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqEllipsecurve1.luvcolor);
        }
        if(this.quaseqBellshapeButton.isSelected()){
          this.quaseqBellshape1 = new QuaseqBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.maxlightness, this.quaseqBellshape1.minlightness, this.quaseqBellshape1.curvevertex, this.quaseqBellshape1.divisor, this.quaseqBellshape1.shift_h, this.quaseqBellshape1.shift_r, this.quaseqBellshape1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.quaseqBellshape1.luvcolor);
        }
      }

      if(this.tabbedPane.getSelectedIndex() == 3){
        if(this.divdivConeButton.isSelected()){
          this.divdivCone1 = new DivdivCone(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.range, this.divdivCone1.height, this.divdivCone1.radius, this.divdivCone1.shift_h, this.divdivCone1.shift_r, this.divdivCone1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivCone1.luvcolor);
        }
        if(this.divdivEllipsoidButton.isSelected()){
          this.divdivEllipsoid1 = new DivdivEllipsoid(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.range, this.divdivEllipsoid1.a, this.divdivEllipsoid1.c, this.divdivEllipsoid1.shift_h, this.divdivEllipsoid1.shift_r, this.divdivEllipsoid1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsoid1.luvcolor);
        }
        if(this.divdivEllipseCurveButton.isSelected()){
          this.divdivEllipsecurve1 = new DivdivEllipsecurve(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.range, this.divdivEllipsecurve1.a, this.divdivEllipsecurve1.b, this.divdivEllipsecurve1.shift_h, this.divdivEllipsecurve1.shift_r, this.divdivEllipsecurve1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivEllipsecurve1.luvcolor);
        }
        if(this.divdivBellshapeButton.isSelected()){
          this.divdivBellshape1 = new DivdivBellshape(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.range, this.divdivBellshape1.curvevertex, this.divdivBellshape1.divisor, this.divdivBellshape1.shift_h, this.divdivBellshape1.shift_r, this.divdivBellshape1.shift_p);
          this.createPatches(this.verticalClass[this.getCurrentSample()].slider1.getValue(), this.horizontalClass[this.getCurrentSample()].slider1.getValue(), this.divdivBellshape1.luvcolor);
        }
      }

    }


    this.repaint();
    this.revalidate();
  }

  private int getCurrentSample(){
    int currentSample = 0;
    for(int i = 0; i < 3; i ++){
      if(this.checkbox[i].isSelected() == true){
        currentSample = i;
      }
    }
    return currentSample;
  }

  private void createPatches(int vclass, int hclass, LUVcolor luvcolor[][]){

    //clear the current color patches
    this.sample[this.getCurrentSample()].removeAll();
    GridLayout g1 = new GridLayout(vclass, hclass);
    this.sample[this.getCurrentSample()].setLayout(g1);
    this.sample[this.getCurrentSample()].setToolTipText("Click to enlarge");

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        CIELUVtoSRGB CIELUVtoSRGB1 = new CIELUVtoSRGB(luvcolor[i][j].L, luvcolor[i][j].U, luvcolor[i][j].V);
        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension((int)Math.floor(200/hclass), (int)Math.floor(200/vclass)));
        p.setBackground(new Color((int)CIELUVtoSRGB1.R255, (int)CIELUVtoSRGB1.G255, (int)CIELUVtoSRGB1.B255));
        this.sample[this.getCurrentSample()].add(p);

        this.colorarray[i][j][0][this.getCurrentSample()] = (int)CIELUVtoSRGB1.R255;
        this.colorarray[i][j][1][this.getCurrentSample()] = (int)CIELUVtoSRGB1.G255;
        this.colorarray[i][j][2][this.getCurrentSample()] = (int)CIELUVtoSRGB1.B255;

        this.luvcolorarray[i][j][0][this.getCurrentSample()] = (int)luvcolor[i][j].L;
        this.luvcolorarray[i][j][1][this.getCurrentSample()] = (int)luvcolor[i][j].U;
        this.luvcolorarray[i][j][2][this.getCurrentSample()] = (int)luvcolor[i][j].V;

      }
    }

    this.sample[this.getCurrentSample()].repaint();
    this.sample[this.getCurrentSample()].revalidate();
  }

  private String getCurrentGeometricModel(){

    String currentGeometricModel = "SeqseqDiamond";

    if(this.tabbedPane.getSelectedIndex() == 0){
      if(this.seqseqDiamondButton.isSelected()){
        currentGeometricModel = "SeqseqDiamond";
      }
      if(this.seqseqTiltedDiamondButton.isSelected()){
        currentGeometricModel = "SeqseqTiltedDiamond";
      }
      if(this.seqseqFoldedDiamondButton.isSelected()){
        currentGeometricModel = "SeqseqFoldedDiamond";
      }
    }

    if(this.tabbedPane.getSelectedIndex() == 1){
      if(this.divseqWedgeButton.isSelected()){
        currentGeometricModel = "DivseqWedge";
      }
      if(this.divseqEllipseButton.isSelected()){
        currentGeometricModel = "DivseqEllipse";
      }
      if(this.divseqTrapezoidButton.isSelected()){
        currentGeometricModel = "DivseqTrapezoid";
      }
    }

    if(this.tabbedPane.getSelectedIndex() == 2){
      if(this.quaseqConeButton.isSelected()){
        currentGeometricModel = "QuaseqCone";
      }
      if(this.quaseqEllipsoidButton.isSelected()){
        currentGeometricModel = "QuaseqEllipsoid";
      }
      if(this.quaseqEllipseCurveButton.isSelected()){
        currentGeometricModel = "QuaseqEllipsecurve";
      }
      if(this.quaseqBellshapeButton.isSelected()){
        currentGeometricModel = "QuaseqBellshape";
      }
    }

    if(this.tabbedPane.getSelectedIndex() == 3){
      if(this.divdivConeButton.isSelected()){
        currentGeometricModel = "DivdivCone";
      }
      if(this.divdivEllipsoidButton.isSelected()){
        currentGeometricModel = "DivdivEllipsoid";
      }
      if(this.divdivEllipseCurveButton.isSelected()){
        currentGeometricModel = "DivdivEllipsecurve";
      }
      if(this.divdivBellshapeButton.isSelected()){
        currentGeometricModel = "DivdivBellshape";
      }
    }

    return currentGeometricModel;

  }

  /** Returns an ImageIcon, or null if the path was invalid. */
  private static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL = SRGBDesignBoard.class.getResource(path);
    if (imgURL != null) {
      return new ImageIcon(imgURL);
    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }

}