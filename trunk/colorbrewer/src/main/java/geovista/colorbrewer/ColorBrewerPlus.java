package geovista.colorbrewer;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.psu.geovista.common.event.BivariatePaletteEvent;
import edu.psu.geovista.common.event.BivariatePaletteListener;
import edu.psu.geovista.common.event.ClassNumberEvent;
import edu.psu.geovista.common.event.ClassNumberListener;
import edu.psu.geovista.common.event.PaletteEvent;
import edu.psu.geovista.common.event.PaletteListener;
import geovista.common.color.BivariatePalette;
import geovista.common.color.Palette;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ColorBrewerPlus
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */

public class ColorBrewerPlus extends JPanel implements ActionListener, ChangeListener, MouseListener, ListSelectionListener, ClassNumberListener {
	protected final static Logger logger = Logger.getLogger(ColorBrewerPlus.class.getName());
  //JFrame f = new JFrame();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel uniPanel = new JPanel();
  JPanel biPanel = new JPanel();

  Border raisedbevelborder = BorderFactory.createRaisedBevelBorder();
  Border loweredetchedborder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

  SpinnerNumberModel uniModel = new SpinnerNumberModel(5, 2, 15, 1);
  SpinnerNumberModel biHorizontalModel = new SpinnerNumberModel(5, 2, 15, 1);
  SpinnerNumberModel biVerticalModel = new SpinnerNumberModel(5, 2, 15, 1);

  OriginalColor cindy = new OriginalColor();
  ThumbnailBivariates thumbnailbivariates = new ThumbnailBivariates();
  Font pound12Font = new Font("Arial", Font.PLAIN, 12);

  int ranking = 0;

  public static final int maxRecentBivariateSchemes = 10;
  public static final int maxRecentUnivariateSchemes = 10;
  public static final int maxUnivariateClasses = 15;
  public static final int maxBivariateClasses = 15;

  private int currentBivariateFlag = 0;
  private int depthBivariateFlag = 0;
  private int currentUnivariateFlag = 0;
  private int depthUnivariateFlag = 0;
  private int undoredoFlag = 0;

  BivariateScheme[] recentBivariateSchemes = new BivariateScheme[maxRecentBivariateSchemes];
  BivariateScheme latestBivariateScheme = new BivariateScheme();
  UnivariateScheme[] recentUnivariateSchemes = new UnivariateScheme[maxRecentUnivariateSchemes];
  UnivariateScheme latestUnivariateScheme = new UnivariateScheme();

  String currentUnivariateScheme = new String();
  String currentBivariateScheme = new String();

  public static final int GOOD = 3;
  public static final int DOUBTFUL = 2;
  public static final int BAD = 1;

  private ImageIcon colorBlind_friendlyIcon = new ImageIcon(this.getClass().getResource("resources/ColorBlind_friendly.gif"));
  private ImageIcon colorBlind_doubtfulIcon = new ImageIcon(this.getClass().getResource("resources/ColorBlind_doubtful.gif"));
  private ImageIcon colorBlind_badIcon = new ImageIcon(this.getClass().getResource("resources/ColorBlind_bad.gif"));

  private ImageIcon colorPrinting_friendlyIcon = new ImageIcon(this.getClass().getResource("resources/ColorPrinting_friendly.gif"));
  private ImageIcon colorPrinting_doubtfulIcon = new ImageIcon(this.getClass().getResource("resources/ColorPrinting_doubtful.gif"));
  private ImageIcon colorPrinting_badIcon = new ImageIcon(this.getClass().getResource("resources/ColorPrinting_bad.gif"));

  private ImageIcon cRT_friendlyIcon = new ImageIcon(this.getClass().getResource("resources/CRT_friendly.gif"));
  private ImageIcon cRT_doubtfulIcon = new ImageIcon(this.getClass().getResource("resources/CRT_doubtful.gif"));
  private ImageIcon cRT_badIcon = new ImageIcon(this.getClass().getResource("resources/CRT_bad.gif"));

  private ImageIcon laptop_friendlyIcon = new ImageIcon(this.getClass().getResource("resources/Laptop_friendly.gif"));
  private ImageIcon laptop_doubtfulIcon = new ImageIcon(this.getClass().getResource("resources/Laptop_doubtful.gif"));
  private ImageIcon laptop_badIcon = new ImageIcon(this.getClass().getResource("resources/Laptop_bad.gif"));

  private ImageIcon lCDProjector_friendlyIcon = new ImageIcon(this.getClass().getResource("resources/LCDProjector_friendly.gif"));
  private ImageIcon lCDProjector_doubtfulIcon = new ImageIcon(this.getClass().getResource("resources/LCDProjector_doubtful.gif"));
  private ImageIcon lCDProjector_badIcon = new ImageIcon(this.getClass().getResource("resources/LCDProjector_bad.gif"));

  private ImageIcon photoCopy_friendlyIcon = new ImageIcon(this.getClass().getResource("resources/PhotoCopy_friendly.gif"));
  private ImageIcon photoCopy_doubtfulIcon = new ImageIcon(this.getClass().getResource("resources/PhotoCopy_doubtful.gif"));
  private ImageIcon photoCopy_badIcon = new ImageIcon(this.getClass().getResource("resources/PhotoCopy_bad.gif"));

  JButton[] suitabilityButtons = new JButton[6];

  ButtonGroup uniButtonGroup = new ButtonGroup();
  ButtonGroup biButtonGroup = new ButtonGroup();

  JPanel uniWestPanel = new JPanel();
  JPanel uniEastPanel = new JPanel();
  JScrollPane uniScrollPane = new JScrollPane();
  JPanel uniTypeAndClassNumberPanel = new JPanel();
  JPanel uniViewPanel = new JPanel();
  JPanel uniScrollPaneHolder = new JPanel();

  JPanel biWestPanel = new JPanel();
  JPanel biEastPanel = new JPanel();
  JScrollPane biScrollPane = new JScrollPane();
  JPanel biTypeAndClassNumberPanel = new JPanel();
  JPanel biViewPanel = new JPanel();
  JPanel biScrollPaneHolder = new JPanel();

  JPanel eastPanel = new JPanel();
  JPanel recentSchemePanel = new JPanel();
  JTabbedPane recentSchemePane = new JTabbedPane();
  JPanel suitabilityInformationPanel = new JPanel();
  JTabbedPane suitabilityInformationPane = new JTabbedPane();
  JPanel applyPanel = new JPanel();

  ImageIcon undoButtonIcon = createImageIcon("resources/back.gif");
  ImageIcon redoButtonIcon = createImageIcon("resources/forward.gif");
  JPanel recentUniSchemePanel = new JPanel();
  JPanel recentBiSchemePanel = new JPanel();
  JPanel recentUniButtonPanel = new JPanel();
  JPanel recentBiButtonPanel = new JPanel();
  JButton univariateUndoButton = new JButton(undoButtonIcon);
  JButton univariateRedoButton = new JButton(redoButtonIcon);
  JButton bivariateUndoButton = new JButton(undoButtonIcon);
  JButton bivariateRedoButton = new JButton(redoButtonIcon);
  JLabel univariateLabel = new JLabel("Univariates");
  JLabel bivariateLabel = new JLabel("Bivariates");


  //ImageIcon broadcastButtonIcon1 = createImageIcon("resources/broadcast1.gif");
  //ImageIcon broadcastButtonIcon2 = createImageIcon("resources/broadcast2.gif");
  JButton applyButton = new JButton("Apply");

  JPanel bivariateLegend = new JPanel();

  JRadioButton uniSeq = new JRadioButton("  Sequential  ");
  JRadioButton uniDiv = new JRadioButton("  Diverging  ");
  JRadioButton uniQua = new JRadioButton("  Qualitative ");
  Datacontrol uniClassNumber = new Datacontrol();

  JRadioButton biSeqSeq = new JRadioButton("  Seq-Seq ");
  JRadioButton biDivSeq = new JRadioButton("  Div-Seq ");
  JRadioButton biDivDiv = new JRadioButton("  Div-Div ");
  JRadioButton biQuaSeq = new JRadioButton("  Qua-Seq ");
  JRadioButton biSeqDiv = new JRadioButton("  Seq-Div ");
  JRadioButton biSeqQua = new JRadioButton("  Seq-Qua ");
  Datacontrol biHorizontalClassNumber = new Datacontrol();
  Datacontrol biVerticalClassNumber = new Datacontrol();

  JPanel seqthumbnails[] = new JPanel[18];
  JPanel divthumbnails[] = new JPanel[9];
  JPanel quathumbnails[] = new JPanel[8];

  JPanel seqschemes[] = new JPanel[6];
  JPanel divschemes[] = new JPanel[8];

  public static final int seqMaxlengthRecommended =  9;
  public static final int divMaxlengthRecommended =  11;
  public static final int quaMaxlengthRecommended =  8;

  public static final int seqMinlengthProvided =  5;
  public static final int divMinlengthProvided =  5;
  public static final int quaMinlengthProvided =  5;

  Quaseqbellcurve quaseqbellcurve1 = new Quaseqbellcurve(5, 5, 95, 26, 120, 7500, 0, 0, 25);
  Quaseqbellcurve seqquabellcurve1 = new Quaseqbellcurve(5, 5, 95, 26, 120, 7500, 0, 0, 25);
  Quaseqbellcurve quaseqbellcurve2 = new Quaseqbellcurve(5, 5, 87, 23, 164, 4913, 0, 0, 292);
  Quaseqbellcurve seqquabellcurve2 = new Quaseqbellcurve(5, 5, 87, 23, 164, 4913, 0, 0, 292);
  Quaseqcone quaseqcone1 = new Quaseqcone(5, 5, 97, 35, 130, 120, 0, 0, 10);
  Quaseqcone seqquacone1 = new Quaseqcone(5, 5, 97, 35, 130, 120, 0, 0, 10);
  Divdivbellcurve divdivbellcurve1 = new Divdivbellcurve(5, 5, 135, 100, 7500, 0, 2, 100);
  Divdivbellcurve divdivbellcurve2 = new Divdivbellcurve(5, 5, 157, 95, 8435, 0, 10, 130);
  Divseqellipsedn divseqellipsedown1 = new Divseqellipsedn(5, 5, 100, 46, 90, 80, 0, 4, 138);
  Divseqellipsedn seqdivellipsedown1 = new Divseqellipsedn(5, 5, 100, 46, 90, 80, 0, 4, 138);
  Divseqtrapezoid divseqtrapezoid1 = new Divseqtrapezoid(5, 5, 95, 50, 109, 75, 0, 0, 230);
  Divseqtrapezoid seqdivtrapezoid1 = new Divseqtrapezoid(5, 5, 95, 50, 109, 75, 0, 0, 230);
  Divseqgrids divseqgrid1 = new Divseqgrids(5, 5, 100, 35, 150, 120, 0, 0, 0);
  Divseqgrids seqdivgrid1 = new Divseqgrids(5, 5, 100, 35, 150, 120, 0, 0, 0);
  Seqseqgraydiamond seqseqgraydiamond1 = new Seqseqgraydiamond(5, 5, 100, 6, 137, 0, 0, 356);
  Seqseqgraydiamond seqseqgraydiamond2 = new Seqseqgraydiamond(5, 5, 98, 2, 129, 12, 14, 53);
  Seqseqnongraydiamond seqseqnongraydiamond1 = new Seqseqnongraydiamond(5, 5, 100, 18, 141, 5, 0, 0, 125);

  JPanel thumbnailquaseqbellcurve1 = new JPanel();
  JPanel thumbnailseqquabellcurve1 = new JPanel();
  JPanel thumbnailquaseqbellcurve2 = new JPanel();
  JPanel thumbnailseqquabellcurve2 = new JPanel();
  JPanel thumbnailquaseqcone1 = new JPanel();
  JPanel thumbnailseqquacone1 = new JPanel();
  JPanel thumbnaildivdivbellcurve1 = new JPanel();
  JPanel thumbnaildivdivbellcurve2 = new JPanel();
  JPanel thumbnaildivseqellipsedown1 = new JPanel();
  JPanel thumbnailseqdivellipsedown1 = new JPanel();
  JPanel thumbnaildivseqtrapezoid1 = new JPanel();
  JPanel thumbnailseqdivtrapezoid1 = new JPanel();
  JPanel thumbnaildivseqgrid1 = new JPanel();
  JPanel thumbnailseqdivgrid1 = new JPanel();
  JPanel thumbnailseqseqgraydiamond1 = new JPanel();
  JPanel thumbnailseqseqgraydiamond2 = new JPanel();
  JPanel thumbnailseqseqnongraydiamond1 = new JPanel();
  //transient private Vector paletteListeners;
  //transient private Vector bivariatePaletteListeners;
    public static final String COLOR_SCHEME_NAME_PUBUGN = "PuBuGn";
    public static final String COLOR_SCHEME_NAME_BUPU = "BuPu";

    public ColorBrewerPlus() {

    this.setPreferredSize(new Dimension(445, 315));
    this.setLayout(new BorderLayout());
    this.add(this.tabbedPane, BorderLayout.WEST);
    this.add(this.eastPanel, BorderLayout.EAST);

    this.tabbedPane.setPreferredSize(new Dimension(325, 290));
    tabbedPane.addTab("Univariate Schemes", uniPanel);
    this.tabbedPane.setFont(this.pound12Font);
    tabbedPane.addTab("Bivariate Schemes", biPanel);
    this.tabbedPane.addChangeListener(this);

    this.uniSeq.setFont(this.pound12Font);
    this.uniDiv.setFont(this.pound12Font);
    this.uniQua.setFont(this.pound12Font);
    this.biDivDiv.setFont(this.pound12Font);
    this.biDivSeq.setFont(this.pound12Font);
    this.biQuaSeq.setFont(this.pound12Font);
    this.biSeqDiv.setFont(this.pound12Font);
    this.biSeqQua.setFont(this.pound12Font);
    this.biSeqSeq.setFont(this.pound12Font);

    this.uniPanel.setLayout(new BorderLayout());
    this.uniPanel.add(this.uniWestPanel, BorderLayout.WEST);
    this.uniPanel.add(this.uniEastPanel, BorderLayout.EAST);
    this.uniWestPanel.setLayout(new GridLayout(1, 1));
    this.uniWestPanel.add(this.uniScrollPane);
    this.uniEastPanel.setLayout(new BorderLayout());
    this.uniEastPanel.add(this.uniTypeAndClassNumberPanel, BorderLayout.NORTH);
    this.uniEastPanel.add(this.uniViewPanel, BorderLayout.SOUTH);
    this.uniWestPanel.setBorder(this.loweredetchedborder);
    this.uniEastPanel.setBorder(this.loweredetchedborder);

    this.uniScrollPane.setPreferredSize(new Dimension(115, 290));
    this.uniScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.uniTypeAndClassNumberPanel.setPreferredSize(new Dimension(200, 85));
    this.uniViewPanel.setPreferredSize(new Dimension(200, 200));
    this.uniTypeAndClassNumberPanel.setBorder(this.loweredetchedborder);
    this.uniViewPanel.setBorder(this.loweredetchedborder);
    this.uniScrollPane.getViewport().add(this.uniScrollPaneHolder);

    this.uniTypeAndClassNumberPanel.setLayout(new FlowLayout());
    this.uniTypeAndClassNumberPanel.add(this.uniSeq);
    this.uniTypeAndClassNumberPanel.add(this.uniDiv);
    this.uniTypeAndClassNumberPanel.add(this.uniQua);
    this.uniTypeAndClassNumberPanel.add(this.uniClassNumber);
    this.uniSeq.setPreferredSize(new Dimension(100, 13));
    this.uniDiv.setPreferredSize(new Dimension(100, 13));
    this.uniQua.setPreferredSize(new Dimension(100, 13));
    this.uniSeq.addActionListener(this);
    this.uniDiv.addActionListener(this);
    this.uniQua.addActionListener(this);
    this.uniButtonGroup.add(this.uniSeq);
    this.uniButtonGroup.add(this.uniDiv);
    this.uniButtonGroup.add(this.uniQua);
    this.uniClassNumber.setPreferredSize(new Dimension(105, 20));
    this.uniClassNumber.slider1.setPreferredSize(new Dimension(65, 20));
    this.uniClassNumber.slider1.setToolTipText("Setting the Class Number");
    this.uniClassNumber.slider1.setMaximum(15);
    this.uniClassNumber.slider1.setMinimum(2);
    this.uniClassNumber.slider1.setValue(5);
    this.uniClassNumber.spinner1.setModel(this.uniModel);
    this.uniClassNumber.spinner1.setPreferredSize(new Dimension(40, 20));
    this.uniClassNumber.spinner1.addChangeListener(this);
    this.uniClassNumber.slider1.setEnabled(false);
    this.uniClassNumber.spinner1.setEnabled(false);

    this.biPanel.setLayout(new BorderLayout());
    this.biPanel.add(this.biWestPanel, BorderLayout.WEST);
    this.biPanel.add(this.biEastPanel, BorderLayout.EAST);
    this.biWestPanel.setLayout(new GridLayout(1, 1));
    this.biWestPanel.add(this.biScrollPane);
    this.biEastPanel.setLayout(new BorderLayout());
    this.biEastPanel.add(this.biTypeAndClassNumberPanel, BorderLayout.NORTH);
    this.biEastPanel.add(this.biViewPanel, BorderLayout.SOUTH );//
    this.biWestPanel.setBorder(this.loweredetchedborder);
    this.biEastPanel.setBorder(this.loweredetchedborder);

    this.biScrollPane.setPreferredSize(new Dimension(115, 290));
    this.biScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    this.biTypeAndClassNumberPanel.setPreferredSize(new Dimension(200, 85));
    this.biViewPanel.setPreferredSize(new Dimension(200, 200));
    this.biTypeAndClassNumberPanel.setBorder(this.loweredetchedborder);
    this.biViewPanel.setBorder(this.loweredetchedborder);
    this.biScrollPane.getViewport().add(this.biScrollPaneHolder);

    this.biTypeAndClassNumberPanel.setLayout(new GridLayout(4, 2));
    this.biTypeAndClassNumberPanel.add(this.biSeqSeq);
    this.biTypeAndClassNumberPanel.add(this.biDivDiv);
    this.biTypeAndClassNumberPanel.add(this.biDivSeq);
    this.biTypeAndClassNumberPanel.add(this.biQuaSeq);
    this.biTypeAndClassNumberPanel.add(this.biSeqDiv);
    this.biTypeAndClassNumberPanel.add(this.biSeqQua);
    this.biTypeAndClassNumberPanel.add(this.biHorizontalClassNumber);
    this.biTypeAndClassNumberPanel.add(this.biVerticalClassNumber);
    this.biSeqSeq.setPreferredSize(new Dimension(100, 13));
    this.biDivDiv.setPreferredSize(new Dimension(100, 13));
    this.biDivSeq.setPreferredSize(new Dimension(100, 13));
    this.biQuaSeq.setPreferredSize(new Dimension(100, 13));
    this.biSeqDiv.setPreferredSize(new Dimension(100, 13));
    this.biSeqQua.setPreferredSize(new Dimension(100, 13));
    this.biSeqSeq.addActionListener(this);
    this.biDivDiv.addActionListener(this);
    this.biDivSeq.addActionListener(this);
    this.biQuaSeq.addActionListener(this);
    this.biSeqDiv.addActionListener(this);
    this.biSeqQua.addActionListener(this);
    this.biButtonGroup.add(this.biSeqSeq);
    this.biButtonGroup.add(this.biDivDiv);
    this.biButtonGroup.add(this.biDivSeq);
    this.biButtonGroup.add(this.biQuaSeq);
    this.biButtonGroup.add(this.biSeqDiv);
    this.biButtonGroup.add(this.biSeqQua);
    this.biHorizontalClassNumber.setPreferredSize(new Dimension(100, 20));
    this.biHorizontalClassNumber.slider1.setToolTipText("Setting the Horizontal Class Number");
    this.biHorizontalClassNumber.slider1.setPreferredSize(new Dimension(60, 20));
    this.biHorizontalClassNumber.slider1.setMaximum(15);
    this.biHorizontalClassNumber.slider1.setMinimum(2);
    this.biHorizontalClassNumber.slider1.setValue(5);
    this.biHorizontalClassNumber.spinner1.setToolTipText("Setting the Horizontal Class Number");
    this.biHorizontalClassNumber.spinner1.setModel(this.biHorizontalModel);
    this.biHorizontalClassNumber.spinner1.setPreferredSize(new Dimension(40, 20));
    this.biHorizontalClassNumber.spinner1.addChangeListener(this);
    this.biVerticalClassNumber.setPreferredSize(new Dimension(100, 20));
    this.biVerticalClassNumber.slider1.setToolTipText("Setting the Vertical Class Number");
    this.biVerticalClassNumber.slider1.setPreferredSize(new Dimension(60, 20));
    this.biVerticalClassNumber.slider1.setMaximum(15);
    this.biVerticalClassNumber.slider1.setMinimum(2);
    this.biVerticalClassNumber.slider1.setValue(5);
    this.biVerticalClassNumber.spinner1.setToolTipText("Setting the Vertical Class Number");
    this.biVerticalClassNumber.spinner1.setModel(this.biVerticalModel);
    this.biVerticalClassNumber.spinner1.setPreferredSize(new Dimension(40, 20));
    this.biVerticalClassNumber.spinner1.addChangeListener(this);

    //this.biHorizontalClassNumber.slider1.setEnabled(false);
    //this.biHorizontalClassNumber.spinner1.setEnabled(false);
    //this.biVerticalClassNumber.slider1.setEnabled(false);
    //this.biVerticalClassNumber.spinner1.setEnabled(false);

    this.eastPanel.setPreferredSize(new Dimension(120, 290));
    this.eastPanel.setLayout(new BorderLayout());
    this.eastPanel.add(this.recentSchemePane, BorderLayout.CENTER);
    this.eastPanel.add(this.applyPanel, BorderLayout.SOUTH);
    this.eastPanel.add(this.suitabilityInformationPane, BorderLayout.NORTH);

    this.recentSchemePane.addTab("Recent Schemes", this.recentSchemePanel);
    this.recentSchemePane.setFont(this.pound12Font);
    this.recentSchemePanel.setPreferredSize(new Dimension(120, 130));

    this.recentSchemePanel.setLayout(new BorderLayout());
    this.recentSchemePanel.add(this.recentUniSchemePanel, BorderLayout.NORTH);
    this.recentUniSchemePanel.setBorder(this.loweredetchedborder);
    this.recentUniSchemePanel.setLayout(new BorderLayout());
    this.recentUniSchemePanel.add(this.univariateLabel, BorderLayout.NORTH);
    this.recentUniSchemePanel.add(this.recentUniButtonPanel, BorderLayout.SOUTH);
    this.univariateLabel.setFont(this.pound12Font);
    this.recentUniButtonPanel.add(this.univariateUndoButton);
    this.recentUniButtonPanel.add(this.univariateRedoButton);
    this.univariateUndoButton.addActionListener(this);
    this.univariateRedoButton.addActionListener(this);
    this.univariateUndoButton.setPreferredSize(new Dimension(30, 30));
    this.univariateUndoButton.setToolTipText("Undo");
    this.univariateRedoButton.setPreferredSize(new Dimension(30, 30));
    this.univariateRedoButton.setToolTipText("Redo");
    this.recentSchemePanel.add(this.recentBiSchemePanel, BorderLayout.SOUTH);
    this.recentBiSchemePanel.setBorder(this.loweredetchedborder);
    this.recentBiSchemePanel.setLayout(new BorderLayout());
    this.recentBiSchemePanel.add(this.bivariateLabel, BorderLayout.NORTH);
    this.recentBiSchemePanel.add(this.recentBiButtonPanel, BorderLayout.SOUTH);
    this.recentBiSchemePanel.add(this.bivariateLabel);
    this.bivariateLabel.setFont(this.pound12Font);
    this.bivariateUndoButton.setPreferredSize(new Dimension(30, 30));
    this.bivariateUndoButton.setToolTipText("Undo");
    this.bivariateRedoButton.setPreferredSize(new Dimension(30, 30));
    this.bivariateRedoButton.setToolTipText("Redo");
    this.recentBiButtonPanel.add(this.bivariateUndoButton);
    this.recentBiButtonPanel.add(this.bivariateRedoButton);
    this.bivariateUndoButton.addActionListener(this);
    this.bivariateUndoButton.addMouseListener(this);
    this.bivariateRedoButton.addActionListener(this);
    this.bivariateRedoButton.addMouseListener(this);

    for(int i = 0; i < 6; i ++){
      this.suitabilityButtons[i] = new JButton();
      this.suitabilityButtons[i].setEnabled(false);
    }

    this.suitabilityButtons[0].setIcon(this.colorBlind_friendlyIcon);
    this.suitabilityButtons[0].setBorder(BorderFactory.createEtchedBorder());
    this.suitabilityButtons[0].setToolTipText("Friendly to Red-Green Blindness");

    this.suitabilityButtons[1].setIcon(this.photoCopy_friendlyIcon);
    this.suitabilityButtons[1].setBorder(BorderFactory.createEtchedBorder());
    this.suitabilityButtons[1].setToolTipText("Friendly to Black-White Photocopying");

    this.suitabilityButtons[2].setIcon(this.lCDProjector_friendlyIcon);
    this.suitabilityButtons[2].setBorder(BorderFactory.createEtchedBorder());
    this.suitabilityButtons[2].setToolTipText("Friendly to typical LCD Projectors");

    this.suitabilityButtons[3].setIcon(this.laptop_friendlyIcon);
    this.suitabilityButtons[3].setBorder(BorderFactory.createEtchedBorder());
    this.suitabilityButtons[3].setToolTipText("Friendly to laptop LCD Display");

    this.suitabilityButtons[4].setIcon(this.cRT_friendlyIcon);
    this.suitabilityButtons[4].setBorder(BorderFactory.createEtchedBorder());
    this.suitabilityButtons[4].setToolTipText("Friendly to average CRT Display");

    this.suitabilityButtons[5].setIcon(this.colorPrinting_friendlyIcon);
    this.suitabilityButtons[5].setBorder(BorderFactory.createEtchedBorder());
    this.suitabilityButtons[5].setToolTipText("Friendly to Color Printing");


    this.suitabilityInformationPane.addTab("Suitability", this.suitabilityInformationPanel);
    this.suitabilityInformationPane.setFont(this.pound12Font);
    this.suitabilityInformationPanel.setPreferredSize(new Dimension(120, 80));
    this.suitabilityInformationPanel.setLayout(new GridLayout(2, 3));

    for(int i = 0; i < 6; i ++){
      suitabilityInformationPanel.add(this.suitabilityButtons[i], null);
    }


    //this.applyPanel.add(this.applyButton);
    this.applyPanel.setPreferredSize(new Dimension(120, 40));
    this.applyButton.setPreferredSize(new Dimension(110, 30));
    //this.broadcastButton.setText("Broadcast");
    this.applyButton.setHorizontalTextPosition(AbstractButton.TRAILING);
    this.applyButton.setToolTipText("Click to send the current univariate color scheme");
    this.applyButton.setFont(this.pound12Font);
    this.applyButton.setEnabled(false);
    this.applyButton.addActionListener(this);


    //this.f.setTitle("ColorBrewer Plus");
    //this.f.getContentPane().add(this);
    //this.f.pack();
    //this.f.setVisible(true);

    for(int i = 0; i < ColorBrewerPlus.maxRecentBivariateSchemes; i ++){
      this.recentBivariateSchemes[i] = new BivariateScheme();
    }

    for(int i = 0; i < ColorBrewerPlus.maxRecentUnivariateSchemes; i ++){
      this.recentUnivariateSchemes[i] = new UnivariateScheme();
    }

    this.bivariateUndoButton.setEnabled(false);
    this.bivariateRedoButton.setEnabled(false);
    this.univariateUndoButton.setEnabled(false);
    this.univariateRedoButton.setEnabled(false);
    init();

  }
  //jin: keep the empty method and leave it to subclass to implement
  protected  void init(){


  }

  public void actionPerformed(ActionEvent e) {

    //#####################################################################################################

    //if event fired by the applyButton

    //#####################################################################################################

    /*
    if(e.getSource() == this.applyButton){

      //under the univariate mode, so send the currentUnivariateScheme
      if(this.applyButton.getToolTipText() == "Click to send the current univariate color scheme"){

        this.firePaletteChanged(this.latestUnivariateScheme);

        logger.finest("Univariate Scheme sent");

      }

      //under the bivariate mode, so send the currentBivariateScheme
      if(this.applyButton.getToolTipText() == "Click to send the current bivariate color scheme"){

        this.fireBivariatePaletteChanged(this.latestBivariateScheme);

        logger.finest("Bivariate Scheme sent");

      }

    }
    */

    //#####################################################################################################

    //if event fired by the univariate type buttons

    //#####################################################################################################

    if(e.getSource() == this.uniSeq){

      if(this.uniSeq.isSelected() == true){

        this.uniViewPanel.removeAll();
        this.makeUnivariateThumbnail("seq");
        this.uniViewPanel.repaint();
        for(int i = 0; i < 6; i ++){
          this.suitabilityButtons[i].setToolTipText("");
        }
      }
    }

    if(e.getSource() == this.uniDiv){

      if(this.uniDiv.isSelected() == true){

        this.uniViewPanel.removeAll();
        this.makeUnivariateThumbnail("div");
        this.uniViewPanel.repaint();
        for(int i = 0; i < 6; i ++){
          this.suitabilityButtons[i].setToolTipText("");
        }
      }
    }

    if(e.getSource() == this.uniQua){

      if(this.uniQua.isSelected() == true){

        this.uniViewPanel.removeAll();
        this.makeUnivariateThumbnail("qua");
        this.uniViewPanel.repaint();
        for(int i = 0; i < 6; i ++){
          this.suitabilityButtons[i].setToolTipText("");
        }
      }
    }

    //#####################################################################################################

    //if event fired by the bivariate type buttons

    //#####################################################################################################

    if(e.getSource() == this.biSeqSeq){

      if(this.biSeqSeq.isSelected() == true){

        this.biViewPanel.removeAll();
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqseqgraydiamond1);
        this.makeBivariateThumbnail(this.thumbnailseqseqgraydiamond1, "seqseqgraydiamond1");
        this.biScrollPaneHolder.add(this.thumbnailseqseqgraydiamond2);
        this.makeBivariateThumbnail(this.thumbnailseqseqgraydiamond2, "seqseqgraydiamond2");
        this.biScrollPaneHolder.add(this.thumbnailseqseqnongraydiamond1);
        this.makeBivariateThumbnail(this.thumbnailseqseqnongraydiamond1, "seqseqnongraydiamond1");

      }
    }

    if(e.getSource() == this.biDivDiv){

      if(this.biDivDiv.isSelected() == true){

        this.biViewPanel.removeAll();
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnaildivdivbellcurve1);
        this.makeBivariateThumbnail(this.thumbnaildivdivbellcurve1, "divdivbellcurve1");
        this.biScrollPaneHolder.add(this.thumbnaildivdivbellcurve2);
        this.makeBivariateThumbnail(this.thumbnaildivdivbellcurve2, "divdivbellcurve2");

      }
    }

    if(e.getSource() == this.biDivSeq){

      if(this.biDivSeq.isSelected() == true){

        this.biViewPanel.removeAll();
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnaildivseqellipsedown1);
        this.makeBivariateThumbnail(this.thumbnaildivseqellipsedown1, "divseqellipsedown1");
        this.biScrollPaneHolder.add(this.thumbnaildivseqtrapezoid1);
        this.makeBivariateThumbnail(this.thumbnaildivseqtrapezoid1, "divseqtrapezoid1");
        this.biScrollPaneHolder.add(this.thumbnaildivseqgrid1);
        this.makeBivariateThumbnail(this.thumbnaildivseqgrid1, "divseqgrid1");

      }
    }

    if(e.getSource() == this.biQuaSeq){

      if(this.biQuaSeq.isSelected() == true){

        this.biViewPanel.removeAll();
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailquaseqbellcurve1);
        this.makeBivariateThumbnail(this.thumbnailquaseqbellcurve1, "quaseqbellcurve1");
        this.biScrollPaneHolder.add(this.thumbnailquaseqbellcurve2);
        this.makeBivariateThumbnail(this.thumbnailquaseqbellcurve2, "quaseqbellcurve2");
        this.biScrollPaneHolder.add(this.thumbnailquaseqcone1);
        this.makeBivariateThumbnail(this.thumbnailquaseqcone1, "quaseqcone1");

      }
    }

    if(e.getSource() == this.biSeqDiv){

      if(this.biSeqDiv.isSelected() == true){

        this.biViewPanel.removeAll();
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqdivellipsedown1);
        this.makeBivariateThumbnail(this.thumbnailseqdivellipsedown1, "seqdivellipsedown1");
        this.biScrollPaneHolder.add(this.thumbnailseqdivtrapezoid1);
        this.makeBivariateThumbnail(this.thumbnailseqdivtrapezoid1, "seqdivtrapezoid1");

      }
    }

    if(e.getSource() == this.biSeqQua){

      if(this.biSeqQua.isSelected() == true){

        this.biViewPanel.removeAll();
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqquabellcurve1);
        this.makeBivariateThumbnail(this.thumbnailseqquabellcurve1, "seqquabellcurve1");
        this.biScrollPaneHolder.add(this.thumbnailseqquabellcurve2);
        this.makeBivariateThumbnail(this.thumbnailseqquabellcurve2, "seqquabellcurve2");
        this.biScrollPaneHolder.add(this.thumbnailseqquacone1);
        this.makeBivariateThumbnail(this.thumbnailseqquacone1, "seqquacone1");

      }
    }

    //#####################################################################################################

    //if event fired by the back and forward buttons

    //#####################################################################################################

    if(e.getSource() == this.univariateUndoButton){

      //singal that undo or redo is happening
      this.undoredoFlag = 1;

      this.uniViewPanel.removeAll();

      int hclass = this.recentUnivariateSchemes[this.currentUnivariateFlag + 1].hclass;
      this.currentUnivariateScheme = this.recentUnivariateSchemes[this.currentUnivariateFlag + 1].name;
      this.ranking = this.recentUnivariateSchemes[this.currentUnivariateFlag + 1].ranking;

      //if there is change in hclass or vclass, reset the slider
      if(hclass != this.recentUnivariateSchemes[this.currentUnivariateFlag].hclass){
        this.uniClassNumber.slider1.setValue(hclass);
      }

      //if there is no change in hclass and vclass, just showUnivariateScheme
      if(hclass == this.recentUnivariateSchemes[this.currentUnivariateFlag].hclass){
        this.showUnivariateScheme(this.currentUnivariateScheme);
      }

      //reset the univariate radio buttons
      if(currentUnivariateScheme == "YlGn" || currentUnivariateScheme == "YlGnBu" || currentUnivariateScheme == "GnBu" || currentUnivariateScheme == "BuGn" || currentUnivariateScheme == "PuBuGn" || currentUnivariateScheme == "PuBu"|| currentUnivariateScheme == "BuPu"|| currentUnivariateScheme == "RdPu"|| currentUnivariateScheme == "PuRd"|| currentUnivariateScheme == "OrRd"|| currentUnivariateScheme == "YlOrRd"|| currentUnivariateScheme == "YlOrBr"|| currentUnivariateScheme == "Purples"|| currentUnivariateScheme == "Blues"|| currentUnivariateScheme == "Greens"|| currentUnivariateScheme == "Oranges"|| currentUnivariateScheme == "Reds"|| currentUnivariateScheme == "Grays"){
        this.uniSeq.setSelected(true);
        this.makeUnivariateThumbnail("seq");
      }

      if(currentUnivariateScheme == "PuOr" || currentUnivariateScheme == "BrBG"|| currentUnivariateScheme == "PRGn"|| currentUnivariateScheme == "PiYG"|| currentUnivariateScheme == "RdBu"|| currentUnivariateScheme == "RdGy"|| currentUnivariateScheme == "RdYlBu"|| currentUnivariateScheme == "Spectral"|| currentUnivariateScheme == "RdYlGn"){
        this.uniDiv.setSelected(true);
        this.makeUnivariateThumbnail("div");
      }

      if(currentUnivariateScheme == "Set1" || currentUnivariateScheme == "Pastel1"|| currentUnivariateScheme == "Set2"|| currentUnivariateScheme == "Pastel2"|| currentUnivariateScheme == "Dark2"|| currentUnivariateScheme == "Set3"|| currentUnivariateScheme == "Paired"|| currentUnivariateScheme == "Accents"){
        this.uniQua.setSelected(true);
        this.makeUnivariateThumbnail("qua");
      }

      //reset the Flags and check the state of the undo redo buttons
      this.currentUnivariateFlag = this.currentUnivariateFlag + 1;

      if(this.currentUnivariateFlag >= this.depthUnivariateFlag - 1){
        this.univariateUndoButton.setEnabled(false);
      }
      else{
        this.univariateUndoButton.setEnabled(true);
      }

      if(this.currentUnivariateFlag > 0){
        this.univariateRedoButton.setEnabled(true);
      }
      else{
        this.univariateRedoButton.setEnabled(false);
      }

      this.undoredoFlag = 0;

      this.repaint();

    }

    if(e.getSource() == this.univariateRedoButton){

      //singal that undo or redo is happening
      this.undoredoFlag = 1;

      this.uniViewPanel.removeAll();

      int hclass = this.recentUnivariateSchemes[this.currentUnivariateFlag - 1].hclass;
      this.currentUnivariateScheme = this.recentUnivariateSchemes[this.currentUnivariateFlag - 1].name;
      this.ranking = this.recentUnivariateSchemes[this.currentUnivariateFlag - 1].ranking;

      //if there is change in hclass or vclass, reset the slider
      if(hclass != this.recentUnivariateSchemes[this.currentUnivariateFlag].hclass){
        this.uniClassNumber.slider1.setValue(hclass);
      }

      //if there is no change in hclass and vclass, just showUnivariateScheme
      if(hclass == this.recentUnivariateSchemes[this.currentUnivariateFlag].hclass){
        this.showUnivariateScheme(this.currentUnivariateScheme);
      }

      //reset the univariate radio buttons and recreate the thumbnail color legends
      if(currentUnivariateScheme == "YlGn" || currentUnivariateScheme == "YlGnBu" || currentUnivariateScheme == "GnBu" || currentUnivariateScheme == "BuGn" || currentUnivariateScheme == "PuBuGn" || currentUnivariateScheme == "PuBu"|| currentUnivariateScheme == "BuPu"|| currentUnivariateScheme == "RdPu"|| currentUnivariateScheme == "PuRd"|| currentUnivariateScheme == "OrRd"|| currentUnivariateScheme == "YlOrRd"|| currentUnivariateScheme == "YlOrBr"|| currentUnivariateScheme == "Purples"|| currentUnivariateScheme == "Blues"|| currentUnivariateScheme == "Greens"|| currentUnivariateScheme == "Oranges"|| currentUnivariateScheme == "Reds"|| currentUnivariateScheme == "Grays"){
        this.uniSeq.setSelected(true);
        this.makeUnivariateThumbnail("seq");
      }

      if(currentUnivariateScheme == "PuOr" || currentUnivariateScheme == "BrBG"|| currentUnivariateScheme == "PRGn"|| currentUnivariateScheme == "PiYG"|| currentUnivariateScheme == "RdBu"|| currentUnivariateScheme == "RdGy"|| currentUnivariateScheme == "RdYlBu"|| currentUnivariateScheme == "Spectral"|| currentUnivariateScheme == "RdYlGn"){
        this.uniDiv.setSelected(true);
        this.makeUnivariateThumbnail("div");
      }

      if(currentUnivariateScheme == "Set1" || currentUnivariateScheme == "Pastel1"|| currentUnivariateScheme == "Set2"|| currentUnivariateScheme == "Pastel2"|| currentUnivariateScheme == "Dark2"|| currentUnivariateScheme == "Set3"|| currentUnivariateScheme == "Paired"|| currentUnivariateScheme == "Accents"){
        this.uniQua.setSelected(true);
        this.makeUnivariateThumbnail("qua");
      }


      //reset the Flags and check the state of the undo redo buttons
      this.currentUnivariateFlag = this.currentUnivariateFlag - 1;

      if(this.currentUnivariateFlag >= this.depthUnivariateFlag - 1){
        this.univariateUndoButton.setEnabled(false);
      }
      else{
        this.univariateUndoButton.setEnabled(true);
      }

      if(this.currentUnivariateFlag > 0){
        this.univariateRedoButton.setEnabled(true);
      }
      else{
        this.univariateRedoButton.setEnabled(false);
      }

      this.undoredoFlag = 0;

      this.repaint();

    }

    if(e.getSource() == this.bivariateUndoButton){

      //singal that undo or redo is happening
      this.undoredoFlag = 1;

      this.bivariateLegend.removeAll();

      int hclass = this.recentBivariateSchemes[this.currentBivariateFlag + 1].hclass;
      int vclass = this.recentBivariateSchemes[this.currentBivariateFlag + 1].vclass;
      this.currentBivariateScheme = this.recentBivariateSchemes[this.currentBivariateFlag + 1].name;

      //if there is change in hclass or vclass, reset the slider
      if(hclass != this.recentBivariateSchemes[this.currentBivariateFlag].hclass || vclass != this.recentBivariateSchemes[this.currentBivariateFlag].vclass){
        this.biHorizontalClassNumber.slider1.setValue(hclass);
        this.biVerticalClassNumber.slider1.setValue(vclass);
      }

      //if there is no change in hclass and vclass, just showBivariateScheme
      if(hclass == this.recentBivariateSchemes[this.currentBivariateFlag].hclass & vclass == this.recentBivariateSchemes[this.currentBivariateFlag].vclass){
        this.showBivariateScheme(this.currentBivariateScheme);
      }

      //reset the bivariate radio buttons
      if(this.currentBivariateScheme == "quaseqbellcurve1" || this.currentBivariateScheme == "quaseqbellcurve2" || this.currentBivariateScheme == "quaseqcone1"){
        this.biQuaSeq.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailquaseqbellcurve1);
        this.makeBivariateThumbnail(this.thumbnailquaseqbellcurve1, "quaseqbellcurve1");
        this.biScrollPaneHolder.add(this.thumbnailquaseqbellcurve2);
        this.makeBivariateThumbnail(this.thumbnailquaseqbellcurve2, "quaseqbellcurve2");
        this.biScrollPaneHolder.add(this.thumbnailquaseqcone1);
        this.makeBivariateThumbnail(this.thumbnailquaseqcone1, "quaseqcone1");
      }

      if(this.currentBivariateScheme == "seqquabellcurve1" || this.currentBivariateScheme == "seqquabellcurve2" || this.currentBivariateScheme == "seqquacone1"){
        this.biSeqQua.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqquabellcurve1);
        this.makeBivariateThumbnail(this.thumbnailseqquabellcurve1, "seqquabellcurve1");
        this.biScrollPaneHolder.add(this.thumbnailseqquabellcurve2);
        this.makeBivariateThumbnail(this.thumbnailseqquabellcurve2, "seqquabellcurve2");
        this.biScrollPaneHolder.add(this.thumbnailseqquacone1);
        this.makeBivariateThumbnail(this.thumbnailseqquacone1, "seqquacone1");
      }

      if(this.currentBivariateScheme == "divdivbellcurve1" || this.currentBivariateScheme == "divdivbellcurve2"){
        this.biDivDiv.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnaildivdivbellcurve1);
        this.makeBivariateThumbnail(this.thumbnaildivdivbellcurve1, "divdivbellcurve1");
        this.biScrollPaneHolder.add(this.thumbnaildivdivbellcurve2);
        this.makeBivariateThumbnail(this.thumbnaildivdivbellcurve2, "divdivbellcurve2");
      }

      if(this.currentBivariateScheme == "divseqellipsedown1" || this.currentBivariateScheme == "divseqtrapezoid1" || this.currentBivariateScheme == "divseqgrid1"){
        this.biDivSeq.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnaildivseqellipsedown1);
        this.makeBivariateThumbnail(this.thumbnaildivseqellipsedown1, "divseqellipsedown1");
        this.biScrollPaneHolder.add(this.thumbnaildivseqtrapezoid1);
        this.makeBivariateThumbnail(this.thumbnaildivseqtrapezoid1, "divseqtrapezoid1");
        this.biScrollPaneHolder.add(this.thumbnaildivseqgrid1);
        this.makeBivariateThumbnail(this.thumbnaildivseqgrid1, "divseqgrid1");
      }

      if(this.currentBivariateScheme == "seqdivellipsedown1" || this.currentBivariateScheme == "seqdivtrapezoid1" || this.currentBivariateScheme == "seqdivgrid1"){
        this.biSeqDiv.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqdivellipsedown1);
        this.makeBivariateThumbnail(this.thumbnailseqdivellipsedown1, "seqdivellipsedown1");
        this.biScrollPaneHolder.add(this.thumbnailseqdivtrapezoid1);
        this.makeBivariateThumbnail(this.thumbnailseqdivtrapezoid1, "seqdivtrapezoid1");
      }

      if(this.currentBivariateScheme == "seqseqgraydiamond1" || this.currentBivariateScheme == "seqseqgraydiamond2" || this.currentBivariateScheme == "seqseqnongraydiamond1"){
        this.biSeqSeq.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqseqgraydiamond1);
        this.makeBivariateThumbnail(this.thumbnailseqseqgraydiamond1, "seqseqgraydiamond1");
        this.biScrollPaneHolder.add(this.thumbnailseqseqgraydiamond2);
        this.makeBivariateThumbnail(this.thumbnailseqseqgraydiamond2, "seqseqgraydiamond2");
        this.biScrollPaneHolder.add(this.thumbnailseqseqnongraydiamond1);
        this.makeBivariateThumbnail(this.thumbnailseqseqnongraydiamond1, "seqseqnongraydiamond1");
      }

      //reset the Flags and check the state of the undo redo buttons
      this.currentBivariateFlag = this.currentBivariateFlag + 1;

      if(this.currentBivariateFlag >= this.depthBivariateFlag - 1){
        this.bivariateUndoButton.setEnabled(false);
      }

      else{
        this.bivariateUndoButton.setEnabled(true);
      }

      if(this.currentBivariateFlag > 0){
        this.bivariateRedoButton.setEnabled(true);
      }

      else{
        this.bivariateRedoButton.setEnabled(false);
      }

      this.undoredoFlag = 0;

      this.repaint();

    }

    if(e.getSource() == this.bivariateRedoButton){

      this.undoredoFlag = 1;

      this.bivariateLegend.removeAll();

      int hclass = this.recentBivariateSchemes[this.currentBivariateFlag - 1].hclass;
      int vclass = this.recentBivariateSchemes[this.currentBivariateFlag - 1].vclass;
      this.currentBivariateScheme = this.recentBivariateSchemes[this.currentBivariateFlag - 1].name;

      this.biHorizontalClassNumber.slider1.setValue(hclass);
      this.biVerticalClassNumber.slider1.setValue(vclass);

      if(hclass != this.recentBivariateSchemes[this.currentBivariateFlag].hclass || vclass != this.recentBivariateSchemes[this.currentBivariateFlag].vclass){
        this.biHorizontalClassNumber.slider1.setValue(hclass);
        this.biVerticalClassNumber.slider1.setValue(vclass);
      }

      if(hclass == this.recentBivariateSchemes[this.currentBivariateFlag].hclass & vclass == this.recentBivariateSchemes[this.currentBivariateFlag].vclass){
        this.showBivariateScheme(this.currentBivariateScheme);
      }

      //reset the bivariate radio buttons
      if(this.currentBivariateScheme == "quaseqbellcurve1" || this.currentBivariateScheme == "quaseqbellcurve2" || this.currentBivariateScheme == "quaseqcone1"){
        this.biQuaSeq.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailquaseqbellcurve1);
        this.makeBivariateThumbnail(this.thumbnailquaseqbellcurve1, "quaseqbellcurve1");
        this.biScrollPaneHolder.add(this.thumbnailquaseqbellcurve2);
        this.makeBivariateThumbnail(this.thumbnailquaseqbellcurve2, "quaseqbellcurve2");
        this.biScrollPaneHolder.add(this.thumbnailquaseqcone1);
        this.makeBivariateThumbnail(this.thumbnailquaseqcone1, "quaseqcone1");
      }

      if(this.currentBivariateScheme == "seqquabellcurve1" || this.currentBivariateScheme == "seqquabellcurve2" || this.currentBivariateScheme == "seqquacone1"){
        this.biSeqQua.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqquabellcurve1);
        this.makeBivariateThumbnail(this.thumbnailseqquabellcurve1, "seqquabellcurve1");
        this.biScrollPaneHolder.add(this.thumbnailseqquabellcurve2);
        this.makeBivariateThumbnail(this.thumbnailseqquabellcurve2, "seqquabellcurve2");
        this.biScrollPaneHolder.add(this.thumbnailseqquacone1);
        this.makeBivariateThumbnail(this.thumbnailseqquacone1, "seqquacone1");
      }

      if(this.currentBivariateScheme == "divdivbellcurve1" || this.currentBivariateScheme == "divdivbellcurve2"){
        this.biDivDiv.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnaildivdivbellcurve1);
        this.makeBivariateThumbnail(this.thumbnaildivdivbellcurve1, "divdivbellcurve1");
        this.biScrollPaneHolder.add(this.thumbnaildivdivbellcurve2);
        this.makeBivariateThumbnail(this.thumbnaildivdivbellcurve2, "divdivbellcurve2");
      }

      if(this.currentBivariateScheme == "divseqellipsedown1" || this.currentBivariateScheme == "divseqtrapezoid1" || this.currentBivariateScheme == "divseqgrid1"){
        this.biDivSeq.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnaildivseqellipsedown1);
        this.makeBivariateThumbnail(this.thumbnaildivseqellipsedown1, "divseqellipsedown1");
        this.biScrollPaneHolder.add(this.thumbnaildivseqtrapezoid1);
        this.makeBivariateThumbnail(this.thumbnaildivseqtrapezoid1, "divseqtrapezoid1");
        this.biScrollPaneHolder.add(this.thumbnaildivseqgrid1);
        this.makeBivariateThumbnail(this.thumbnaildivseqgrid1, "divseqgrid1");
      }

      if(this.currentBivariateScheme == "seqdivellipsedown1" || this.currentBivariateScheme == "seqdivtrapezoid1" || this.currentBivariateScheme == "seqdivgrid1"){
        this.biSeqDiv.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqdivellipsedown1);
        this.makeBivariateThumbnail(this.thumbnailseqdivellipsedown1, "seqdivellipsedown1");
        this.biScrollPaneHolder.add(this.thumbnailseqdivtrapezoid1);
        this.makeBivariateThumbnail(this.thumbnailseqdivtrapezoid1, "seqdivtrapezoid1");
      }

      if(this.currentBivariateScheme == "seqseqgraydiamond1" || this.currentBivariateScheme == "seqseqgraydiamond2" || this.currentBivariateScheme == "seqseqnongraydiamond1"){
        this.biSeqSeq.setSelected(true);
        this.biScrollPaneHolder.removeAll();
        this.biScrollPaneHolder.setPreferredSize(new Dimension(115, 280));
        this.biScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        this.biScrollPaneHolder.add(this.thumbnailseqseqgraydiamond1);
        this.makeBivariateThumbnail(this.thumbnailseqseqgraydiamond1, "seqseqgraydiamond1");
        this.biScrollPaneHolder.add(this.thumbnailseqseqgraydiamond2);
        this.makeBivariateThumbnail(this.thumbnailseqseqgraydiamond2, "seqseqgraydiamond2");
        this.biScrollPaneHolder.add(this.thumbnailseqseqnongraydiamond1);
        this.makeBivariateThumbnail(this.thumbnailseqseqnongraydiamond1, "seqseqnongraydiamond1");
      }

      this.currentBivariateFlag = this.currentBivariateFlag - 1;

      if(this.currentBivariateFlag == 0){
        this.bivariateRedoButton.setEnabled(false);
      }

      else{
        this.bivariateRedoButton.setEnabled(true);
      }

      if(this.currentBivariateFlag < this.depthBivariateFlag - 1){
        this.bivariateUndoButton.setEnabled(true);
      }

      else{
        this.bivariateUndoButton.setEnabled(false);
      }

      this.undoredoFlag = 0;

      this.repaint();
    }



  }



  public void stateChanged(ChangeEvent e){

    //if the change is caused by shifting between the univariate and bivariate modes
    if(e.getSource() == this.tabbedPane){

      //the statement here is kind like strange, but it works this way. Maybe it is the state before the change in the tabbedpane
      if(this.uniPanel.isVisible() == false){

        this.suitabilityCheck();

        //if there is no available currentUnivariateScheme, turn the applyButton off
        if(this.depthUnivariateFlag == 0){
          this.applyButton.setEnabled(false);
        }

        //disable bivariate redo and undo buttons
        this.bivariateRedoButton.setEnabled(false);
        this.bivariateUndoButton.setEnabled(false);

        //check whether to turn on or off univariate redo and undo buttons
        if(this.currentUnivariateFlag >= this.depthUnivariateFlag - 1){
          this.univariateUndoButton.setEnabled(false);
        }
        else{
          this.univariateUndoButton.setEnabled(true);
        }

        if(this.currentUnivariateFlag > 0){
          this.univariateRedoButton.setEnabled(true);
        }
        else{
          this.univariateRedoButton.setEnabled(false);
        }

        this.applyButton.setToolTipText("Click to send the current univariate color scheme");

      }

      if(this.biPanel.isVisible() == false){

        //if there is no available currentBivariateScheme, turn the applyButton off
        if(this.depthBivariateFlag == 0){
          this.applyButton.setEnabled(false);
        }

        for(int i = 0; i < 6; i ++){
          this.suitabilityButtons[i].setEnabled(false);
          this.suitabilityButtons[i].setToolTipText("Currently no Information on Bivariates");
        }

        //disable univariate redo and undo buttons
        this.univariateRedoButton.setEnabled(false);
        this.univariateUndoButton.setEnabled(false);

        //check whether to turn on or off bivariate redo and undo buttons
        if(this.currentBivariateFlag >= this.depthBivariateFlag - 1){
          this.bivariateUndoButton.setEnabled(false);
        }
        else{
          this.bivariateUndoButton.setEnabled(true);
        }

        if(this.currentBivariateFlag > 0){
          this.bivariateRedoButton.setEnabled(true);
        }
        else{
          this.bivariateRedoButton.setEnabled(false);
        }


        this.applyButton.setToolTipText("Click to send the current bivariate color scheme");

      }

    }


    //if the change is caused by univariate class number
    if(e.getSource() == this.uniClassNumber.spinner1){

      this.showUnivariateScheme(this.currentUnivariateScheme);

      this.suitabilityCheck();

      //When undoredoFlag == 1, the hclass is set by the undo or redo actions, so there is no need to update the recentUnivariateScheme
      //When undoredoFlag == 0, the hclass is set by user actions, and recentUnivariateScheme needs to be updated

      if(this.undoredoFlag == 0){
        this.updateRecentUnivariateScheme();
      }

      //check the state of the undo and redo buttons

      if(this.currentUnivariateFlag >= this.depthUnivariateFlag - 1){
        this.univariateUndoButton.setEnabled(false);
      }

      else{
        this.univariateUndoButton.setEnabled(true);
      }

      if(this.currentUnivariateFlag > 0){
        this.univariateRedoButton.setEnabled(true);
      }

      else{
        this.univariateRedoButton.setEnabled(false);
      }

    }


    //if the change is caused by bivariate classnumbers
    if(e.getSource() == this.biHorizontalClassNumber.spinner1 || e.getSource() == this.biVerticalClassNumber.spinner1){
      logger.finest("state changed in color brewer" + this.biHorizontalClassNumber.spinner1.getValue() + this.biVerticalClassNumber.spinner1.getValue());
      this.showBivariateScheme(this.currentBivariateScheme);
      this.updateRecentBivariateScheme();

      /*
      if(this.currentBivariateScheme == "seqseqgraydiamond1"){
        this.showBivariateScheme("seqseqgraydiamond1");
      }
      if(this.currentBivariateScheme == "seqseqgraydiamond2"){
        this.showBivariateScheme("seqseqgraydiamond2");
      }
      if(this.currentBivariateScheme == "seqseqnongraydiamond1"){
        this.showBivariateScheme("seqseqnongraydiamond1");
      }


      if(this.currentBivariateScheme == "divdivbellcurve1"){
        this.showBivariateScheme("divdivbellcurve1");
      }
      if(this.currentBivariateScheme == "divdivbellcurve2"){
        this.showBivariateScheme("divdivbellcurve2");
      }


      if(this.currentBivariateScheme == "divseqellipsedown1"){
        this.showBivariateScheme("divseqellipsedown1");
      }
      if(this.currentBivariateScheme == "divseqtrapezoid1"){
        this.showBivariateScheme("divseqtrapezoid1");
      }
      if(this.currentBivariateScheme == "divseqgrid1"){
        this.showBivariateScheme("divseqgrid1");
      }


      if(this.currentBivariateScheme == "quaseqbellcurve1"){
        this.showBivariateScheme("quaseqbellcurve1");
      }
      if(this.currentBivariateScheme == "quaseqbellcurve2"){
        this.showBivariateScheme("quaseqbellcurve2");
      }
      if(this.currentBivariateScheme == "quaseqcone1"){
        this.showBivariateScheme("quaseqcone1");
      }


      if(this.currentBivariateScheme == "seqdivellipsedown1"){
        this.showBivariateScheme("seqdivellipsedown1");
      }
      if(this.currentBivariateScheme == "seqdivtrapezoid1"){
        this.showBivariateScheme("seqdivtrapezoid1");
      }
      if(this.currentBivariateScheme == "seqdivgrid1"){
        this.showBivariateScheme("seqdivgrid1");
      }


      if(this.currentBivariateScheme == "seqquabellcurve1"){
        this.showBivariateScheme("seqquabellcurve1");
      }
      if(this.currentBivariateScheme == "seqquabellcurve2"){
        this.showBivariateScheme("seqquabellcurve2");
      }
      if(this.currentBivariateScheme == "seqquacone1"){
        this.showBivariateScheme("seqquacone1");
      }
      */


      //When undoredoFlag == 1, the hclass or vclass is set by the undo or redo actions, so there is no need to update the recentBivariateScheme
      //When undoredoFlag == 0, the hclass or vclass is set by user actions, and recentBivariateScheme needs to be updated

      if(this.undoredoFlag == 0){
        this.updateRecentBivariateScheme();
      }

      //check the state of the undo and redo buttons

      if(this.currentBivariateFlag >= this.depthBivariateFlag - 1){
        this.bivariateUndoButton.setEnabled(false);
      }
      else{
        this.bivariateUndoButton.setEnabled(true);
      }

      if(this.currentBivariateFlag > 0){
        this.bivariateRedoButton.setEnabled(true);
      }
      else{
        this.bivariateRedoButton.setEnabled(false);
      }

    }

  }

  private void makeUnivariateThumbnail(String schemename){

    this.uniScrollPaneHolder.removeAll();

    if(schemename == "seq"){
      this.uniScrollPaneHolder.setPreferredSize(new Dimension(95, 18*25 + 17*5));
      this.uniScrollPaneHolder.setLayout(new GridLayout(18, 1, 0, 5));
      JPanel[] patch = new JPanel[5];
      int[][][] currentschemeset = new int[18][5][3];
      currentschemeset = this.getCurrentschemeSet(18, 5);
      String[] tooltiptext = new String[18];
      tooltiptext = this.getToolTipText(18);

      for(int i = 0; i < 18; i ++){

        this.seqthumbnails[i] = new JPanel();
        this.seqthumbnails[i].setPreferredSize(new Dimension(95, 25));
        this.seqthumbnails[i].setToolTipText(tooltiptext[i]);
        this.seqthumbnails[i].addMouseListener(this);

        for(int j = 0; j < 5; j ++){
          patch[j] = new JPanel();
          this.seqthumbnails[i].setLayout(new GridLayout(1, 5));
          this.seqthumbnails[i].add(patch[j]);
          patch[j].setBackground(new Color(currentschemeset[i][j][0], currentschemeset[i][j][1], currentschemeset[i][j][2]));
        }
        this.uniScrollPaneHolder.add(this.seqthumbnails[i]);
      }
    }

    if(schemename == "div"){
      this.uniScrollPaneHolder.setPreferredSize(new Dimension(95, 9*25 + 8*6));
      //this.scrollPaneHolder.setLayout(new GridLayout(9, 1, 0, 5));
      this.uniScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 6));
      JPanel[] patch = new JPanel[5];
      int[][][] currentschemeset = new int[9][5][3];
      currentschemeset = this.getCurrentschemeSet(9, 5);
      String[] tooltiptext = new String[9];
      tooltiptext = this.getToolTipText(9);

      for(int i = 0; i < 9; i ++){

        this.divthumbnails[i] = new JPanel();
        this.divthumbnails[i].setPreferredSize(new Dimension(95, 25));
        this.divthumbnails[i].setToolTipText(tooltiptext[i]);
        this.divthumbnails[i].addMouseListener(this);

        for(int j = 0; j < 5; j ++){
          patch[j] = new JPanel();
          this.divthumbnails[i].setLayout(new GridLayout(1, 5));
          this.divthumbnails[i].add(patch[j]);
          patch[j].setBackground(new Color(currentschemeset[i][j][0], currentschemeset[i][j][1], currentschemeset[i][j][2]));
        }
        this.uniScrollPaneHolder.add(this.divthumbnails[i]);
      }
    }

    if(schemename == "qua"){
      this.uniScrollPaneHolder.setPreferredSize(new Dimension(95, 8*25 + 7*10));
      //this.scrollPaneHolder.setLayout(new GridLayout(8, 1, 0, 5));
      this.uniScrollPaneHolder.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 7));
      JPanel[] patch = new JPanel[5];
      int[][][] currentschemeset = new int[8][5][3];
      currentschemeset = this.getCurrentschemeSet(8, 5);
      String[] tooltiptext = new String[8];
      tooltiptext = this.getToolTipText(8);

      for(int i = 0; i < 8; i ++){
        this.quathumbnails[i] = new JPanel();
        this.quathumbnails[i].setPreferredSize(new Dimension(95, 25));
        this.quathumbnails[i].setToolTipText(tooltiptext[i]);
        this.quathumbnails[i].addMouseListener(this);

        for(int j = 0; j < 5; j ++){
          patch[j] = new JPanel();
          this.quathumbnails[i].setLayout(new GridLayout(1, 5));
          this.quathumbnails[i].add(patch[j]);
          patch[j].setBackground(new Color(currentschemeset[i][j][0], currentschemeset[i][j][1], currentschemeset[i][j][2]));
        }
        this.uniScrollPaneHolder.add(this.quathumbnails[i]);
      }
    }

    this.uniScrollPane.getViewport().revalidate();
    this.uniScrollPane.getViewport().repaint();
  }

  private void makeBivariateThumbnail(JPanel thumbnail, String schemename){

    thumbnail.removeAll();
    thumbnail.setLayout(new GridLayout(5, 5));
    thumbnail.setPreferredSize(new Dimension(80, 80));
    for(int i = 0; i < 5; i ++){
      for(int j = 0; j < 5; j ++){
        JPanel colorpatch = new JPanel();
        if(schemename == "quaseqbellcurve1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.quaseqbellcurve1[i][j][0], ThumbnailBivariates.quaseqbellcurve1[i][j][1], ThumbnailBivariates.quaseqbellcurve1[i][j][2]));
        }
        if(schemename == "seqquabellcurve1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqquabellcurve1[i][j][0], ThumbnailBivariates.seqquabellcurve1[i][j][1], ThumbnailBivariates.seqquabellcurve1[i][j][2]));
        }
        if(schemename == "quaseqbellcurve2"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.quaseqbellcurve2[i][j][0], ThumbnailBivariates.quaseqbellcurve2[i][j][1], ThumbnailBivariates.quaseqbellcurve2[i][j][2]));
        }
        if(schemename == "seqquabellcurve2"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqquabellcurve2[i][j][0], ThumbnailBivariates.seqquabellcurve2[i][j][1], ThumbnailBivariates.seqquabellcurve2[i][j][2]));
        }
        if(schemename == "quaseqcone1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.quaseqbellcurve1[i][j][0], ThumbnailBivariates.quaseqbellcurve1[i][j][1], ThumbnailBivariates.quaseqbellcurve1[i][j][2]));
        }
        if(schemename == "seqquacone1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqquabellcurve1[i][j][0], ThumbnailBivariates.seqquabellcurve1[i][j][1], ThumbnailBivariates.seqquabellcurve1[i][j][2]));
        }
        if(schemename == "divdivbellcurve1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.divdivbellcurve1[i][j][0], ThumbnailBivariates.divdivbellcurve1[i][j][1], ThumbnailBivariates.divdivbellcurve1[i][j][2]));
        }
        if(schemename == "divdivbellcurve2"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.divdivbellcurve2[i][j][0], ThumbnailBivariates.divdivbellcurve2[i][j][1], ThumbnailBivariates.divdivbellcurve2[i][j][2]));
        }
        if(schemename == "divseqellipsedown1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.divseqellipsedown1[i][j][0], ThumbnailBivariates.divseqellipsedown1[i][j][1], ThumbnailBivariates.divseqellipsedown1[i][j][2]));
        }
        if(schemename == "seqdivellipsedown1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqdivellipsedown1[i][j][0], ThumbnailBivariates.seqdivellipsedown1[i][j][1], ThumbnailBivariates.seqdivellipsedown1[i][j][2]));
        }
        if(schemename == "divseqtrapezoid1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.divseqtrapezoid1[i][j][0], ThumbnailBivariates.divseqtrapezoid1[i][j][1], ThumbnailBivariates.divseqtrapezoid1[i][j][2]));
        }
        if(schemename == "seqdivtrapezoid1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqdivtrapezoid1[i][j][0], ThumbnailBivariates.seqdivtrapezoid1[i][j][1], ThumbnailBivariates.seqdivtrapezoid1[i][j][2]));
        }
        if(schemename == "divseqgrid1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.divseqgrid1[i][j][0], ThumbnailBivariates.divseqgrid1[i][j][1], ThumbnailBivariates.divseqgrid1[i][j][2]));
        }
        if(schemename == "seqdivgrid1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqdivgrid1[i][j][0], ThumbnailBivariates.seqdivgrid1[i][j][1], ThumbnailBivariates.seqdivgrid1[i][j][2]));
        }
        if(schemename == "seqseqgraydiamond1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqseqgraydiamond1[i][j][0], ThumbnailBivariates.seqseqgraydiamond1[i][j][1], ThumbnailBivariates.seqseqgraydiamond1[i][j][2]));
        }
        if(schemename == "seqseqgraydiamond2"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqseqgraydiamond2[i][j][0], ThumbnailBivariates.seqseqgraydiamond2[i][j][1], ThumbnailBivariates.seqseqgraydiamond2[i][j][2]));
        }
        if(schemename == "seqseqnongraydiamond1"){
          colorpatch.setBackground(new Color(ThumbnailBivariates.seqseqnongraydiamond1[i][j][0], ThumbnailBivariates.seqseqnongraydiamond1[i][j][1], ThumbnailBivariates.seqseqnongraydiamond1[i][j][2]));
        }
        thumbnail.add(colorpatch);
      }
    }
    thumbnail.addMouseListener(this);
     //Jin:  fix bug-->
     // When integrate into ESTAT, we do not need the tab, but only uniPanel and biPanel. However, when switch radio buttons, the 2 panel not get updated ( repaint). This 2 line code is to force the panels repaint()
     thumbnail.revalidate();
     thumbnail.repaint();
     //Jin: fix bug<--
    this.revalidate();
    this.repaint();
  }

  private int[][][] getCurrentschemeSet(int numberofschemes, int numberofclasses){

    int[][][] currentschemeset = new int[numberofschemes][numberofclasses][3];

    if(numberofschemes == 18){

      currentschemeset[0] = this.cindy.makescheme("YlGn", numberofclasses);
      currentschemeset[1] = this.cindy.makescheme("YlGnBu", numberofclasses);
      currentschemeset[2] = this.cindy.makescheme("GnBu", numberofclasses);
      currentschemeset[3] = this.cindy.makescheme("BuGn", numberofclasses);
      currentschemeset[4] = this.cindy.makescheme("PuBuGn", numberofclasses);
      currentschemeset[5] = this.cindy.makescheme("PuBu", numberofclasses);
      currentschemeset[6] = this.cindy.makescheme("BuPu", numberofclasses);
      currentschemeset[7] = this.cindy.makescheme("RdPu", numberofclasses);
      currentschemeset[8] = this.cindy.makescheme("PuRd", numberofclasses);
      currentschemeset[9] = this.cindy.makescheme("OrRd", numberofclasses);
      currentschemeset[10] = this.cindy.makescheme("YlOrRd", numberofclasses);
      currentschemeset[11] = this.cindy.makescheme("YlOrBr", numberofclasses);
      currentschemeset[12] = this.cindy.makescheme("Purples", numberofclasses);
      currentschemeset[13] = this.cindy.makescheme("Blues", numberofclasses);
      currentschemeset[14] = this.cindy.makescheme("Greens", numberofclasses);
      currentschemeset[15] = this.cindy.makescheme("Oranges", numberofclasses);
      currentschemeset[16] = this.cindy.makescheme("Reds", numberofclasses);
      currentschemeset[17] = this.cindy.makescheme("Grays", numberofclasses);

    }

    if(numberofschemes == 9){

      currentschemeset[0] = this.cindy.makescheme("PuOr", numberofclasses);
      currentschemeset[1] = this.cindy.makescheme("BrBG", numberofclasses);
      currentschemeset[2] = this.cindy.makescheme("PRGn", numberofclasses);
      currentschemeset[3] = this.cindy.makescheme("PiYG", numberofclasses);
      currentschemeset[4] = this.cindy.makescheme("RdBu", numberofclasses);
      currentschemeset[5] = this.cindy.makescheme("RdGy", numberofclasses);
      currentschemeset[6] = this.cindy.makescheme("RdYlBu", numberofclasses);
      currentschemeset[7] = this.cindy.makescheme("Spectral", numberofclasses);
      currentschemeset[8] = this.cindy.makescheme("RdYlGn", numberofclasses);

    }

    if(numberofschemes == 8){

      currentschemeset[0] = this.cindy.makescheme("Set1", numberofclasses);
      currentschemeset[1] = this.cindy.makescheme("Pastel1", numberofclasses);
      currentschemeset[2] = this.cindy.makescheme("Set2", numberofclasses);
      currentschemeset[3] = this.cindy.makescheme("Pastel2", numberofclasses);
      currentschemeset[4] = this.cindy.makescheme("Dark2", numberofclasses);
      currentschemeset[5] = this.cindy.makescheme("Set3", numberofclasses);
      currentschemeset[6] = this.cindy.makescheme("Paired", numberofclasses);
      currentschemeset[7] = this.cindy.makescheme("Accents", numberofclasses);

    }

    return currentschemeset;
  }

  private String[] getToolTipText(int numberofschemes){
    String[] tooltiptext = new String[numberofschemes];

    if(numberofschemes == 18){
      tooltiptext[0] = "YlGn: light yellow to dark green";
      tooltiptext[1] = "YlGnBu: light yellow to green to dark blue";
      tooltiptext[2] = "GnBu: light green to dark blue";
      tooltiptext[3] = "BuGn: light blue to dark green";
      tooltiptext[4] = "PuBuGn: light purple to blue to dark green";
      tooltiptext[5] = "PuBu: light purple to dark blue";
      tooltiptext[6] = "BuPu: light blue to dark purple";
      tooltiptext[7] = "RdPu: light red to dark purple";
      tooltiptext[8] = "PuRd: light purple to dark red";
      tooltiptext[9] = "OrRd: light orange to dark red";
      tooltiptext[10] = "YlOrRd: light yellow to orange to dark red";
      tooltiptext[11] = "YlOrBr: light yellow to orange to dark brown";
      tooltiptext[12] = "Purples: light to dark purple";
      tooltiptext[13] = "Blues: light to dark blue";
      tooltiptext[14] = "Greens: light to dark green";
      tooltiptext[15] = "Oranges: light to dark orange";
      tooltiptext[16] = "Reds: light to dark red";
      tooltiptext[17] = "Grays: light yellow to dark gray";
    }

    if(numberofschemes == 9){
      tooltiptext[0] = "PuOr: dark orange to light to dark purple";
      tooltiptext[1] = "BrBG: dark brown to light to dark blue-green";
      tooltiptext[2] = "PRGn: dark reddish-purple to light to dark green";
      tooltiptext[3] = "PiYG: dark magenta to light to dark yellow-green";
      tooltiptext[4] = "RdBu: dark red to light to dark blue";
      tooltiptext[5] = "RdGy: dark red to light to dark gray";
      tooltiptext[6] = "RdYlBu: dark red to light yellow to dark blue";
      tooltiptext[7] = "Spectral: dark red, orange, light yellow, green, dark blue";
      tooltiptext[8] = "RdYlGn: dark red, orange, light yellow, yellow-green, dark green";
    }

    if(numberofschemes == 8){
      tooltiptext[0] = "Set1: bold, readily named, basic colors";
      tooltiptext[1] = "Pastel1: lighter version of Set1";
      tooltiptext[2] = "Set2: mostly mixture colors";
      tooltiptext[3] = "Pastel2: lighter version of Set2";
      tooltiptext[4] = "Dark2: darker version of Set2";
      tooltiptext[5] = "Set3: medium saturation set with more lightness variation";
      tooltiptext[6] = "Paired: light/dark pairs for namable hues";
      tooltiptext[7] = "Accents: lightness and saturation extremes to accent small or important areas";
    }

    return tooltiptext;
  }

  public void mouseClicked(MouseEvent e){

    //if the univariate bars are clicked
    for(int i = 0; i < 18; i ++){
      if(e.getSource() == this.seqthumbnails[i]){

        if(i == 0){
          this.currentUnivariateScheme = "YlGn";
        }
        if(i == 1){
          this.currentUnivariateScheme = "YlGnBu";
        }
        if(i == 2){
          this.currentUnivariateScheme = "GnBu";
        }
        if(i == 3){
          this.currentUnivariateScheme = "BuGn";
        }
        if(i == 4){
          this.currentUnivariateScheme = COLOR_SCHEME_NAME_PUBUGN;
        }
        if(i == 5){
          this.currentUnivariateScheme = "PuBu";
        }
        if(i == 6){
          this.currentUnivariateScheme = COLOR_SCHEME_NAME_BUPU;
        }
        if(i == 7){
          this.currentUnivariateScheme = "RdPu";
        }
        if(i == 8){
          this.currentUnivariateScheme = "PuRd";
        }
        if(i == 9){
          this.currentUnivariateScheme = "OrRd";
        }
        if(i == 10){
          this.currentUnivariateScheme = "YlOrRd";
        }
        if(i == 11){
          this.currentUnivariateScheme = "YlOrBr";
        }
        if(i == 12){
          this.currentUnivariateScheme = "Purples";
        }
        if(i == 13){
          this.currentUnivariateScheme = "Blues";
        }
        if(i == 14){
          this.currentUnivariateScheme = "Greens";
        }
        if(i == 15){
          this.currentUnivariateScheme = "Oranges";
        }
        if(i == 16){
          this.currentUnivariateScheme = "Reds";
        }
        if(i == 17){
          this.currentUnivariateScheme = "Grays";
        }
        this.ranking = i;
        this.showUnivariateScheme(this.currentUnivariateScheme);
        this.updateRecentUnivariateScheme();
        this.suitabilityCheck();
        this.uniSeq.setSelected(true);
      }
    }

    //if the diverging bars are clicked
    for(int i = 0; i < 9; i ++){
      if(e.getSource() == this.divthumbnails[i]){
        if(i == 0){
          this.currentUnivariateScheme = "PuOr";
        }
        if(i == 1){
          this.currentUnivariateScheme = "BrBG";
        }
        if(i == 2){
          this.currentUnivariateScheme = "PRGn";
        }
        if(i == 3){
          this.currentUnivariateScheme = "PiYG";
        }
        if(i == 4){
          this.currentUnivariateScheme = "RdBu";
        }
        if(i == 5){
          this.currentUnivariateScheme = "RdGy";
        }
        if(i == 6){
          this.currentUnivariateScheme = "RdYlBu";
        }
        if(i == 7){
          this.currentUnivariateScheme = "Spectral";
        }
        if(i == 8){
          this.currentUnivariateScheme = "RdYlGn";
        }
        this.ranking = i;
        this.showUnivariateScheme(this.currentUnivariateScheme);
        this.updateRecentUnivariateScheme();
        this.suitabilityCheck();
        this.uniDiv.setSelected(true);
      }
    }

    //if the qualitative bars are clicked
    for(int i = 0; i < 8; i ++){
      if(e.getSource() == this.quathumbnails[i]){
        if(i == 0){
          this.currentUnivariateScheme = "Set1";
        }
        if(i == 1){
          this.currentUnivariateScheme = "Pastel1";
        }
        if(i == 2){
          this.currentUnivariateScheme = "Set2";
        }
        if(i == 3){
          this.currentUnivariateScheme = "Pastel2";
        }
        if(i == 4){
          this.currentUnivariateScheme = "Dark2";
        }
        if(i == 5){
          this.currentUnivariateScheme = "Set3";
        }
        if(i == 6){
          this.currentUnivariateScheme = "Paired";
        }
        if(i == 7){
          this.currentUnivariateScheme = "Accents";
        }
        this.ranking = i;
        this.showUnivariateScheme(this.currentUnivariateScheme);
        this.updateRecentUnivariateScheme();
        this.suitabilityCheck();
        this.uniQua.setSelected(true);
      }
    }


    //if the bivariate thumbnails are clicked
    if(e.getSource() == this.thumbnailquaseqbellcurve1){
      this.showBivariateScheme("quaseqbellcurve1");
      this.currentBivariateScheme = "quaseqbellcurve1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqquabellcurve1){
      this.showBivariateScheme("seqquabellcurve1");
      this.currentBivariateScheme = "seqquabellcurve1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailquaseqbellcurve2){
      this.showBivariateScheme("quaseqbellcurve2");
      this.currentBivariateScheme = "quaseqbellcurve2";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqquabellcurve2){
      this.showBivariateScheme("seqquabellcurve2");
      this.currentBivariateScheme = "seqquabellcurve2";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailquaseqcone1){
      this.showBivariateScheme("quaseqcone1");
      this.currentBivariateScheme = "quaseqcone1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqquacone1){
      this.showBivariateScheme("seqquacone1");
      this.currentBivariateScheme = "seqquacone1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnaildivdivbellcurve1){
      this.showBivariateScheme("divdivbellcurve1");
      this.currentBivariateScheme = "divdivbellcurve1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnaildivdivbellcurve2){
      this.showBivariateScheme("divdivbellcurve2");
      this.currentBivariateScheme = "divdivbellcurve2";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnaildivseqellipsedown1){
      this.showBivariateScheme("divseqellipsedown1");
      this.currentBivariateScheme = "divseqellipsedown1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqdivellipsedown1){
      this.showBivariateScheme("seqdivellipsedown1");
      this.currentBivariateScheme = "seqdivellipsedown1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnaildivseqtrapezoid1){
      this.showBivariateScheme("divseqtrapezoid1");
      this.currentBivariateScheme = "divseqtrapezoid1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqdivtrapezoid1){
      this.showBivariateScheme("seqdivtrapezoid1");
      this.currentBivariateScheme = "seqdivtrapezoid1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnaildivseqgrid1){
      this.showBivariateScheme("divseqgrid1");
      this.currentBivariateScheme = "divseqgrid1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqdivgrid1){
      this.showBivariateScheme("seqdivgrid1");
      this.currentBivariateScheme = "seqdivgrid1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqseqgraydiamond1){
      this.showBivariateScheme("seqseqgraydiamond1");
      this.currentBivariateScheme = "seqseqgraydiamond1";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqseqgraydiamond2){
      this.showBivariateScheme("seqseqgraydiamond2");
      this.currentBivariateScheme = "seqseqgraydiamond2";
      this.updateRecentBivariateScheme();
    }
    if(e.getSource() == this.thumbnailseqseqnongraydiamond1){
      this.showBivariateScheme("seqseqnongraydiamond1");
      this.currentBivariateScheme = "seqseqnongraydiamond1";
      this.updateRecentBivariateScheme();
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

  protected  void updateRecentUnivariateScheme(){

    this.applyButton.setEnabled(true);

    this.uniClassNumber.slider1.setEnabled(true);
    this.uniClassNumber.spinner1.setEnabled(true);

    //pop in the stack
    for(int i = ColorBrewerPlus.maxRecentUnivariateSchemes - 2; i >= 0; i --){

      this.recentUnivariateSchemes[i].overWrite(this.recentUnivariateSchemes[i + 1]);

    }

    //read the latestUnivariateScheme into the stack as the first one
    this.latestUnivariateScheme.overWrite(this.recentUnivariateSchemes[0]);

    //depth ++
    if(this.depthUnivariateFlag < 10){
      this.depthUnivariateFlag = this.depthUnivariateFlag + 1;
    }

    //remember to reset the recentflag to be zero
    this.currentUnivariateFlag = 0;

    if(this.currentUnivariateFlag < this.depthUnivariateFlag - 1){
      this.univariateUndoButton.setEnabled(true);
      this.repaint();
    }

  }

  protected  void updateRecentBivariateScheme(){

    this.applyButton.setEnabled(true);

    this.biHorizontalClassNumber.slider1.setEnabled(true);
    this.biHorizontalClassNumber.spinner1.setEnabled(true);
    this.biVerticalClassNumber.slider1.setEnabled(true);
    this.biVerticalClassNumber.spinner1.setEnabled(true);

    //pop in the stack
    for(int i = ColorBrewerPlus.maxRecentBivariateSchemes - 2; i >= 0; i --){

      this.recentBivariateSchemes[i].overWrite(this.recentBivariateSchemes[i + 1]);

    }

    //read the latestBivariateScheme into the stack as the first one
    this.latestBivariateScheme.overWrite(this.recentBivariateSchemes[0]);

    //depth ++
    if(this.depthBivariateFlag < 10){
      this.depthBivariateFlag = this.depthBivariateFlag + 1;
    }

    //remember to reset the recentflag to be zero
    this.currentBivariateFlag = 0;

    if(this.currentBivariateFlag < this.depthBivariateFlag - 1){
      this.bivariateUndoButton.setEnabled(true);
      this.repaint();
    }

  }

  protected  void showUnivariateScheme(String currentUnivariateScheme){

    this.uniViewPanel.removeAll();
    this.uniViewPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 4));
    int hclass = Integer.valueOf(String.valueOf(this.uniClassNumber.spinner1.getValue())).intValue();

    this.latestUnivariateScheme.hclass = hclass;
    this.latestUnivariateScheme.name = currentUnivariateScheme;
    this.latestUnivariateScheme.ranking = this.ranking;

    if(currentUnivariateScheme == "YlGn" || currentUnivariateScheme == "YlGnBu" || currentUnivariateScheme == "GnBu" || currentUnivariateScheme == "BuGn" || currentUnivariateScheme == "PuBuGn" || currentUnivariateScheme == "PuBu"|| currentUnivariateScheme == "BuPu"|| currentUnivariateScheme == "RdPu"|| currentUnivariateScheme == "PuRd"|| currentUnivariateScheme == "OrRd"|| currentUnivariateScheme == "YlOrRd"|| currentUnivariateScheme == "YlOrBr"|| currentUnivariateScheme == "Purples"|| currentUnivariateScheme == "Blues"|| currentUnivariateScheme == "Greens"|| currentUnivariateScheme == "Oranges"|| currentUnivariateScheme == "Reds"|| currentUnivariateScheme == "Grays"){

      for(int i = 5; i < 10; i ++){
        this.seqschemes[(i - 5)] = new JPanel();
        //pay attention to these minute adjustments to the size of the scheme
        this.seqschemes[(i - 5)].setPreferredSize(new Dimension(20*i + 4, 24));
        this.seqschemes[(i - 5)].setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.seqschemes[(i - 5)].setToolTipText("Original scheme: " + i + " classes, available up to 9 classes");

        for(int j = 0; j < i; j ++){
          int[][][] currentschemeset = new int[18][i][3];
          currentschemeset = this.getCurrentschemeSet(18, i);
          JPanel colorpatch = new JPanel();
          colorpatch.setPreferredSize(new Dimension(20, 20));
          colorpatch.setBackground(new Color(currentschemeset[this.ranking][j][0], currentschemeset[this.ranking][j][1], currentschemeset[this.ranking][j][2]));
          this.seqschemes[(i - 5)].add(colorpatch);
        }

        if(i == hclass){
          this.seqschemes[(i - 5)].setBorder(this.raisedbevelborder);
        }

        this.uniViewPanel.add(this.seqschemes[(i - 5)]);

      }

      //recording the colors into the latestUnivariateScheme
      if(hclass <= ColorBrewerPlus.seqMaxlengthRecommended & hclass >= ColorBrewerPlus.seqMinlengthProvided){

        int[][][] currentschemeset = new int[18][hclass][3];
        currentschemeset = this.getCurrentschemeSet(18, hclass);
        for(int k = 0; k < hclass; k ++){
          this.latestUnivariateScheme.colorinRGB[k] = new Color(currentschemeset[this.ranking][k][0], currentschemeset[this.ranking][k][1], currentschemeset[this.ranking][k][2]);
        }

      }

      //interpolate when necessary, and record the colors into latestUnivariateScheme
      if(hclass > ColorBrewerPlus.seqMaxlengthRecommended || hclass < ColorBrewerPlus.seqMinlengthProvided){
        this.seqschemes[5] = new JPanel();
        this.seqschemes[5].setPreferredSize(new Dimension((180/hclass)*hclass + 4, 24));
        this.seqschemes[5].setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.seqschemes[5].setBorder(this.raisedbevelborder);
        this.seqschemes[5].setToolTipText("This scheme is interpolated from the above original ones");
        OriginalColor cindy = new OriginalColor();
        int[][] interpolatedScheme = new int[(hclass + 1)][3];
        if(hclass > ColorBrewerPlus.seqMaxlengthRecommended){
          interpolatedScheme = cindy.interpolate(this.currentUnivariateScheme, hclass);
        }
        if(hclass < ColorBrewerPlus.seqMinlengthProvided){
          interpolatedScheme = cindy.makescheme(this.currentUnivariateScheme, hclass);
        }
        for(int i = 0; i < hclass; i ++){
          JPanel colorpatch = new JPanel();
          colorpatch.setPreferredSize(new Dimension(180/hclass, 20));
          colorpatch.setBackground(new Color(interpolatedScheme[i][0], interpolatedScheme[i][1], interpolatedScheme[i][2]));
          this.seqschemes[5].add(colorpatch);
        }
        this.uniViewPanel.add(this.seqschemes[5]);

        //recording the color to the latestestUnivariateScheme
        for(int k = 0; k < hclass; k ++){
          this.latestUnivariateScheme.colorinRGB[k] = new Color(interpolatedScheme[k][0], interpolatedScheme[k][1], interpolatedScheme[k][2]);
        }

      }

    }


    if(currentUnivariateScheme == "PuOr" || currentUnivariateScheme == "BrBG"|| currentUnivariateScheme == "PRGn"|| currentUnivariateScheme == "PiYG"|| currentUnivariateScheme == "RdBu"|| currentUnivariateScheme == "RdGy"|| currentUnivariateScheme == "RdYlBu"|| currentUnivariateScheme == "Spectral"|| currentUnivariateScheme == "RdYlGn"){

      this.uniViewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));

      for(int i = 5; i < 12; i ++){
        this.divschemes[(i - 5)] = new JPanel();
        //pay attention to these minute adjustments to the size of the scheme
        this.divschemes[(i - 5)].setPreferredSize(new Dimension(16*i + 4, 22));
        this.divschemes[(i - 5)].setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        this.divschemes[(i - 5)].setToolTipText("Original scheme: " + i + " classes, available up to 11 classes");
        //scheme.setLayout(new GridLayout(1, i));
        if(i == hclass){
          this.divschemes[(i - 5)].setBorder(this.raisedbevelborder);
        }
        for(int j = 0; j < i; j ++){
          int[][][] currentschemeset = new int[9][i][3];
          currentschemeset = this.getCurrentschemeSet(9, i);
          JPanel colorpatch = new JPanel();
          colorpatch.setPreferredSize(new Dimension(16, 18));
          colorpatch.setBackground(new Color(currentschemeset[ranking][j][0], currentschemeset[ranking][j][1], currentschemeset[ranking][j][2]));
          this.divschemes[(i - 5)].add(colorpatch);
        }
        this.uniViewPanel.add(this.divschemes[(i - 5)]);
      }

      //recording the colors into the latestUnivariateScheme
      if(hclass <= ColorBrewerPlus.divMaxlengthRecommended & hclass >= ColorBrewerPlus.divMinlengthProvided){

        int[][][] currentschemeset = new int[9][hclass][3];
        currentschemeset = this.getCurrentschemeSet(9, hclass);
        for(int k = 0; k < hclass; k ++){
          this.latestUnivariateScheme.colorinRGB[k] = new Color(currentschemeset[this.ranking][k][0], currentschemeset[this.ranking][k][1], currentschemeset[this.ranking][k][2]);
        }

      }

      if(hclass > ColorBrewerPlus.divMaxlengthRecommended || hclass < ColorBrewerPlus.divMinlengthProvided){
        this.divschemes[7] = new JPanel();
        this.divschemes[7].setPreferredSize(new Dimension((180/hclass)*hclass + 4, 24));
        this.divschemes[7].setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.divschemes[7].setBorder(this.raisedbevelborder);
        this.divschemes[7].setToolTipText("This scheme is interpolated from the above original ones");
        OriginalColor cindy = new OriginalColor();

        int[][] interpolatedScheme = cindy.interpolate(this.currentUnivariateScheme, hclass);

        //adding a checkpoint here to rectify the center color to white or light yellow in case of odd number of classes
        if(hclass > ColorBrewerPlus.divMaxlengthRecommended && (hclass/2 != (hclass + 1)/2)){
          if(this.currentUnivariateScheme == "RdYlBu" || this.currentUnivariateScheme == "Spectral" || this.currentUnivariateScheme == "RdYlGn"){
            interpolatedScheme[(hclass - 1)/2][0] = 255;
            interpolatedScheme[(hclass - 1)/2][1] = 255;
            interpolatedScheme[(hclass - 1)/2][2] = 191;
          }
          else{
            interpolatedScheme[(hclass - 1)/2][0] = 255;
            interpolatedScheme[(hclass - 1)/2][1] = 255;
            interpolatedScheme[(hclass - 1)/2][2] = 255;
          }
        }

        for(int i = 0; i < hclass; i ++){
          JPanel colorpatch = new JPanel();
          colorpatch.setPreferredSize(new Dimension(180/hclass, 20));
          colorpatch.setBackground(new Color(interpolatedScheme[i][0], interpolatedScheme[i][1], interpolatedScheme[i][2]));
          this.divschemes[7].add(colorpatch);
        }
        this.uniViewPanel.add(this.divschemes[7]);

        //recording the color to the latestUnivariateScheme
        for(int k = 0; k < hclass; k ++){
          this.latestUnivariateScheme.colorinRGB[k] = new Color(interpolatedScheme[k][0], interpolatedScheme[k][1], interpolatedScheme[k][2]);
        }
      }

    }

    if(currentUnivariateScheme == "Set1" || currentUnivariateScheme == "Pastel1"|| currentUnivariateScheme == "Set2"|| currentUnivariateScheme == "Pastel2"|| currentUnivariateScheme == "Dark2"|| currentUnivariateScheme == "Set3"|| currentUnivariateScheme == "Paired"|| currentUnivariateScheme == "Accents"){

      this.uniViewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 2));

      if(this.currentUnivariateScheme == "Accents" || this.currentUnivariateScheme == "Dark2" || this.currentUnivariateScheme == "Pastel2" || this.currentUnivariateScheme == "Set2"){

        this.uniViewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        for(int i = 5; i < 9; i ++){
          JPanel scheme = new JPanel();
          //pay attention to these minute adjustments to the size of the scheme
          scheme.setPreferredSize(new Dimension(20*i + 4, 24));
          scheme.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
          scheme.setToolTipText("Original scheme: " + i + " classes, available up to 8 classes");
          //scheme.setLayout(new GridLayout(1, i));
          if(i == hclass){
            scheme.setBorder(this.raisedbevelborder);
          }
          for(int j = 0; j < i; j ++){
            int[][][] currentschemeset = new int[8][i][3];
            currentschemeset = this.getCurrentschemeSet(8, i);
            JPanel colorpatch = new JPanel();
            colorpatch.setPreferredSize(new Dimension(20, 20));
            colorpatch.setBackground(new Color(currentschemeset[ranking][j][0], currentschemeset[ranking][j][1], currentschemeset[ranking][j][2]));
            scheme.add(colorpatch);
          }
          this.uniViewPanel.add(scheme);

          //recording the colors into the latestUnivariateScheme
          if(hclass <= 8 & hclass >= 5){

            int[][][] currentschemeset = new int[8][hclass][3];
            currentschemeset = this.getCurrentschemeSet(8, hclass);
            for(int k = 0; k < hclass; k ++){
              this.latestUnivariateScheme.colorinRGB[k] = new Color(currentschemeset[this.ranking][k][0], currentschemeset[this.ranking][k][1], currentschemeset[this.ranking][k][2]);
            }
          }
        }
      }

      if(this.currentUnivariateScheme == "Pastel1" || this.currentUnivariateScheme == "Set1"){

        this.uniViewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        for(int i = 5; i < 10; i ++){
          JPanel scheme = new JPanel();
          //pay attention to these minute adjustments to the size of the scheme
          scheme.setPreferredSize(new Dimension(20*i + 4, 24));
          scheme.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
          scheme.setToolTipText("Original scheme: " + i + " classes, available up to 9 classes");
          //scheme.setLayout(new GridLayout(1, i));
          if(i == hclass){
            scheme.setBorder(this.raisedbevelborder);
          }
          for(int j = 0; j < i; j ++){
            int[][][] currentschemeset = new int[8][i][3];
            currentschemeset = this.getCurrentschemeSet(8, i);
            JPanel colorpatch = new JPanel();
            colorpatch.setPreferredSize(new Dimension(20, 20));
            colorpatch.setBackground(new Color(currentschemeset[ranking][j][0], currentschemeset[ranking][j][1], currentschemeset[ranking][j][2]));
            scheme.add(colorpatch);
          }
          this.uniViewPanel.add(scheme);

          //recording the colors into the latestUnivariateScheme
          if(hclass <= 9 & hclass >= 5){

            int[][][] currentschemeset = new int[8][hclass][3];
            currentschemeset = this.getCurrentschemeSet(8, hclass);
            for(int k = 0; k < hclass; k ++){
              this.latestUnivariateScheme.colorinRGB[k] = new Color(currentschemeset[this.ranking][k][0], currentschemeset[this.ranking][k][1], currentschemeset[this.ranking][k][2]);
            }
          }
        }
      }

      if(this.currentUnivariateScheme == "Paired" || this.currentUnivariateScheme == "Set3"){

        this.uniViewPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 4));

        for(int i = 5; i < 13; i ++){
          JPanel scheme = new JPanel();
          //pay attention to these minute adjustments to the size of the scheme
          scheme.setPreferredSize(new Dimension(14*i + 4, 22));
          scheme.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
          scheme.setToolTipText("Original scheme: " + i + " classes, available up to 12 classes");
          //scheme.setLayout(new GridLayout(1, i));
          if(i == hclass){
            scheme.setBorder(this.raisedbevelborder);
          }
          for(int j = 0; j < i; j ++){
            int[][][] currentschemeset = new int[8][i][3];
            currentschemeset = this.getCurrentschemeSet(8, i);
            JPanel colorpatch = new JPanel();
            colorpatch.setPreferredSize(new Dimension(14, 18));
            colorpatch.setBackground(new Color(currentschemeset[ranking][j][0], currentschemeset[ranking][j][1], currentschemeset[ranking][j][2]));
            scheme.add(colorpatch);
          }
          this.uniViewPanel.add(scheme);

          //recording the colors into the latestUnivariateScheme
          if(hclass <= 12 & hclass >= 5){

            int[][][] currentschemeset = new int[8][hclass][3];
            currentschemeset = this.getCurrentschemeSet(8, hclass);
            for(int k = 0; k < hclass; k ++){
              this.latestUnivariateScheme.colorinRGB[k] = new Color(currentschemeset[this.ranking][k][0], currentschemeset[this.ranking][k][1], currentschemeset[this.ranking][k][2]);
            }
          }
        }
      }

    }

    this.firePaletteChanged(this.latestUnivariateScheme);

    this.uniViewPanel.revalidate();
    this.uniViewPanel.repaint();

  }

  protected  void showBivariateScheme(String schemename){

    this.latestBivariateScheme.name = schemename;

    this.biViewPanel.removeAll();
    this.bivariateLegend.removeAll();
    this.biViewPanel.setLayout(new FlowLayout());
    this.biViewPanel.add(this.bivariateLegend);
    int hclass = Integer.valueOf(String.valueOf(this.biHorizontalClassNumber.spinner1.getValue())).intValue();
    int vclass = Integer.valueOf(String.valueOf(this.biVerticalClassNumber.spinner1.getValue())).intValue();
    this.bivariateLegend.setPreferredSize(new Dimension((180/hclass)*hclass, (180/vclass)*vclass));
    this.bivariateLegend.setLayout(new GridLayout(vclass, hclass));
    this.latestBivariateScheme.hclass = hclass;
    this.latestBivariateScheme.vclass = vclass;

    for(int i = 0; i < vclass; i ++){
      for(int j = 0; j < hclass; j ++){

        JPanel colorpatch = new JPanel();

        if(schemename == "quaseqbellcurve1"){
          this.quaseqbellcurve1 = new Quaseqbellcurve(vclass, hclass, 95, 26, 120, 7500, 0, 0, 25);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.quaseqbellcurve1.labcolor[i][j].L, this.quaseqbellcurve1.labcolor[i][j].a, this.quaseqbellcurve1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqquabellcurve1"){
          this.seqquabellcurve1 = new Quaseqbellcurve(hclass, vclass, 95, 26, 120, 7500, 0, 0, 25);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqquabellcurve1.labcolor[j][i].L, this.seqquabellcurve1.labcolor[j][i].a, this.seqquabellcurve1.labcolor[j][i].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "quaseqbellcurve2"){
          this.quaseqbellcurve2 = new Quaseqbellcurve(vclass, hclass, 87, 23, 164, 4913, 0, 0, 292);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.quaseqbellcurve2.labcolor[i][j].L, this.quaseqbellcurve2.labcolor[i][j].a, this.quaseqbellcurve2.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqquabellcurve2"){
          this.seqquabellcurve2 = new Quaseqbellcurve(hclass, vclass, 87, 23, 164, 4913, 0, 0, 292);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqquabellcurve2.labcolor[j][i].L, this.seqquabellcurve2.labcolor[j][i].a, this.seqquabellcurve2.labcolor[j][i].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "quaseqcone1"){
          this.quaseqcone1 = new Quaseqcone(vclass, hclass, 97, 35, 130, 120, 0, 0, 10);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.quaseqcone1.labcolor[i][j].L, this.quaseqcone1.labcolor[i][j].a, this.quaseqcone1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqquacone1"){
          this.seqquacone1 = new Quaseqcone(hclass, vclass, 97, 35, 130, 120, 0, 0, 10);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqquacone1.labcolor[j][i].L, this.seqquacone1.labcolor[j][i].a, this.seqquacone1.labcolor[j][i].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "divdivbellcurve1"){
          this.divdivbellcurve1 = new Divdivbellcurve(vclass, hclass, 135, 100, 7500, 0, 2, 100);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.divdivbellcurve1.labcolor[i][j].L, this.divdivbellcurve1.labcolor[i][j].a, this.divdivbellcurve1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "divdivbellcurve2"){
          this.divdivbellcurve2 = new Divdivbellcurve(vclass, hclass, 157, 95, 8435, 0, 10, 130);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.divdivbellcurve2.labcolor[i][j].L, this.divdivbellcurve2.labcolor[i][j].a, this.divdivbellcurve2.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "divseqellipsedown1"){
          this.divseqellipsedown1 = new Divseqellipsedn(vclass, hclass, 100, 46, 90, 80, 0, 4, 138);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.divseqellipsedown1.labcolor[i][j].L, this.divseqellipsedown1.labcolor[i][j].a, this.divseqellipsedown1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqdivellipsedown1"){
          this.seqdivellipsedown1 = new Divseqellipsedn(hclass, vclass, 100, 46, 90, 80, 0, 4, 138);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqdivellipsedown1.labcolor[j][i].L, this.seqdivellipsedown1.labcolor[j][i].a, this.seqdivellipsedown1.labcolor[j][i].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "divseqtrapezoid1"){
          this.divseqtrapezoid1 = new Divseqtrapezoid(vclass, hclass, 95, 50, 109, 75, 0, 0, 230);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.divseqtrapezoid1.labcolor[i][j].L, this.divseqtrapezoid1.labcolor[i][j].a, this.divseqtrapezoid1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqdivtrapezoid1"){
          this.seqdivtrapezoid1 = new Divseqtrapezoid(hclass, vclass, 95, 50, 109, 75, 0, 0, 230);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqdivtrapezoid1.labcolor[j][i].L, this.seqdivtrapezoid1.labcolor[j][i].a, this.seqdivtrapezoid1.labcolor[j][i].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "divseqgrid1"){
          this.divseqgrid1 = new Divseqgrids(vclass, hclass, 100, 35, 150, 120, 0, 0, 0);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.divseqgrid1.labcolor[i][j].L, this.divseqgrid1.labcolor[i][j].a, this.divseqgrid1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqdivgrid1"){
          this.seqdivgrid1 = new Divseqgrids(hclass, vclass, 100, 35, 150, 120, 0, 0, 0);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqdivgrid1.labcolor[j][i].L, this.seqdivgrid1.labcolor[j][i].a, this.seqdivgrid1.labcolor[j][i].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqseqgraydiamond1"){
          this.seqseqgraydiamond1 = new Seqseqgraydiamond(vclass, hclass, 100, 6, 137, 0, 0, 356);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqseqgraydiamond1.labcolor[i][j].L, this.seqseqgraydiamond1.labcolor[i][j].a, this.seqseqgraydiamond1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqseqgraydiamond2"){
          this.seqseqgraydiamond2 = new Seqseqgraydiamond(vclass, hclass, 98, 2, 129, 12, 14, 53);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqseqgraydiamond2.labcolor[i][j].L, this.seqseqgraydiamond2.labcolor[i][j].a, this.seqseqgraydiamond2.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        if(schemename == "seqseqnongraydiamond1"){
          this.seqseqnongraydiamond1 = new Seqseqnongraydiamond(vclass, hclass, 100, 18, 141, 5, 0, 0, 125);
          CIELabToSRGB CIELabToSRGB1 = new CIELabToSRGB(this.seqseqnongraydiamond1.labcolor[i][j].L, this.seqseqnongraydiamond1.labcolor[i][j].a, this.seqseqnongraydiamond1.labcolor[i][j].b);
          colorpatch.setBackground(new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255));
          this.latestBivariateScheme.colorinRGB[i][j] = new Color((int)CIELabToSRGB1.R255, (int)CIELabToSRGB1.G255, (int)CIELabToSRGB1.B255);
        }

        this.bivariateLegend.add(colorpatch);
      }
    }

    this.fireBivariatePaletteChanged(this.latestBivariateScheme);

    //Jin:  fix bug-->
     // bug: when integrated in ESTAT, sample legend not show up.
    this.biPanel.revalidate();
    biPanel.repaint();
    //Jin:  fix bug<--
    this.revalidate();
    this.repaint();

  }

  protected  void suitabilityCheck(){

    //turn the suitabilitybuttons on
    for(int  i = 0; i < 6; i ++){
      this.suitabilityButtons[i].setEnabled(true);
      this.suitabilityButtons[i].setToolTipText("");
    }

    int[][] currentSchemeSuitability = new int[14][6];

    if(this.currentUnivariateScheme == "YlGn"){
      currentSchemeSuitability = SchemeSuitability.YlGn;
    }
    if(this.currentUnivariateScheme == "YlGnBu"){
      currentSchemeSuitability = SchemeSuitability.YlGnBu;
    }
    if(this.currentUnivariateScheme == "GnBu"){
      currentSchemeSuitability = SchemeSuitability.GnBu;
    }
    if(this.currentUnivariateScheme == "BuGn"){
      currentSchemeSuitability = SchemeSuitability.BuGn;
    }
    if(this.currentUnivariateScheme == "PuBuGn"){
      currentSchemeSuitability = SchemeSuitability.PuBuGn;
    }
    if(this.currentUnivariateScheme == "PuBu"){
      currentSchemeSuitability = SchemeSuitability.PuBu;
    }
    if(this.currentUnivariateScheme == "BuPu"){
      currentSchemeSuitability = SchemeSuitability.BuPu;
    }
    if(this.currentUnivariateScheme == "RdPu"){
      currentSchemeSuitability = SchemeSuitability.RdPu;
    }
    if(this.currentUnivariateScheme == "PuRd"){
      currentSchemeSuitability = SchemeSuitability.PuRd;
    }
    if(this.currentUnivariateScheme == "OrRd"){
      currentSchemeSuitability = SchemeSuitability.OrRd;
    }
    if(this.currentUnivariateScheme == "YlOrRd"){
      currentSchemeSuitability = SchemeSuitability.YlOrRd;
    }
    if(this.currentUnivariateScheme == "YlOrBr"){
      currentSchemeSuitability = SchemeSuitability.YlOrBr;
    }
    if(this.currentUnivariateScheme == "Purples"){
      currentSchemeSuitability = SchemeSuitability.Purples;
    }
    if(this.currentUnivariateScheme == "Blues"){
      currentSchemeSuitability = SchemeSuitability.Blues;
    }
    if(this.currentUnivariateScheme == "Greens"){
      currentSchemeSuitability = SchemeSuitability.Greens;
    }
    if(this.currentUnivariateScheme == "Oranges"){
      currentSchemeSuitability = SchemeSuitability.Oranges;
    }
    if(this.currentUnivariateScheme == "Reds"){
      currentSchemeSuitability = SchemeSuitability.Reds;
    }
    if(this.currentUnivariateScheme == "Grays"){
      currentSchemeSuitability = SchemeSuitability.Grays;
    }


    if(this.currentUnivariateScheme == "PuOr"){
      currentSchemeSuitability = SchemeSuitability.PuOr;
    }
    if(this.currentUnivariateScheme == "BrBG"){
      currentSchemeSuitability = SchemeSuitability.BrBG;
    }
    if(this.currentUnivariateScheme == "PRGn"){
      currentSchemeSuitability = SchemeSuitability.PRGn;
    }
    if(this.currentUnivariateScheme == "PiYG"){
      currentSchemeSuitability = SchemeSuitability.PiYG;
    }
    if(this.currentUnivariateScheme == "RdBu"){
      currentSchemeSuitability = SchemeSuitability.RdBu;
    }
    if(this.currentUnivariateScheme == "RdGy"){
      currentSchemeSuitability = SchemeSuitability.RdGy;
    }
    if(this.currentUnivariateScheme == "RdYlBu"){
      currentSchemeSuitability = SchemeSuitability.RdYlBu;
    }
    if(this.currentUnivariateScheme == "Spectral"){
      currentSchemeSuitability = SchemeSuitability.Spectral;
    }
    if(this.currentUnivariateScheme == "RdYlGn"){
      currentSchemeSuitability = SchemeSuitability.RdYlGn;
    }


    if(this.currentUnivariateScheme == "Set1"){
      currentSchemeSuitability = SchemeSuitability.Set1;
    }
    if(this.currentUnivariateScheme == "Pastel1"){
      currentSchemeSuitability = SchemeSuitability.Pastel1;
    }
    if(this.currentUnivariateScheme == "Set2"){
      currentSchemeSuitability = SchemeSuitability.Set2;
    }
    if(this.currentUnivariateScheme == "Pastel2"){
      currentSchemeSuitability = SchemeSuitability.Pastel2;
    }
    if(this.currentUnivariateScheme == "Dark2"){
      currentSchemeSuitability = SchemeSuitability.Dark2;
    }
    if(this.currentUnivariateScheme == "Set3"){
      currentSchemeSuitability = SchemeSuitability.Set3;
    }
    if(this.currentUnivariateScheme == "Paired"){
      currentSchemeSuitability = SchemeSuitability.Paired;
    }
    if(this.currentUnivariateScheme == "Accents"){
      currentSchemeSuitability = SchemeSuitability.Accents;
    }

    //mount different icons according to the current number of class and the suitability dataset
    int hclass = Integer.valueOf(String.valueOf(this.uniClassNumber.spinner1.getValue())).intValue();

    if(currentSchemeSuitability[hclass - 2][0] == GOOD){
      this.suitabilityButtons[0].setIcon(this.colorBlind_friendlyIcon);
      this.suitabilityButtons[0].setToolTipText("Friendly to Red-Green Blindness");
    }
    if(currentSchemeSuitability[hclass - 2][0] == DOUBTFUL){
      this.suitabilityButtons[0].setIcon(this.colorBlind_doubtfulIcon);
      this.suitabilityButtons[0].setToolTipText("Doubtful whether Friendly to Red-Green Blindness");
    }
    if(currentSchemeSuitability[hclass - 2][0] == BAD){
      this.suitabilityButtons[0].setIcon(this.colorBlind_badIcon);
      this.suitabilityButtons[0].setToolTipText("Unfriendly to Red-Green Blindness");
    }

    if(currentSchemeSuitability[hclass - 2][1] == GOOD){
      this.suitabilityButtons[1].setIcon(this.photoCopy_friendlyIcon);
      this.suitabilityButtons[1].setToolTipText("Friendly to Black-White Photocopying");
    }
    if(currentSchemeSuitability[hclass - 2][1] == DOUBTFUL){
      this.suitabilityButtons[1].setIcon(this.photoCopy_doubtfulIcon);
      this.suitabilityButtons[1].setToolTipText("Doubtful whether Friendly to Black-White Photocopying");
    }
    if(currentSchemeSuitability[hclass - 2][1] == BAD){
      this.suitabilityButtons[1].setIcon(this.photoCopy_badIcon);
      this.suitabilityButtons[1].setToolTipText("Unfriendly to Black-White Photocopying");
    }

    if(currentSchemeSuitability[hclass - 2][2] == GOOD){
      this.suitabilityButtons[2].setIcon(this.lCDProjector_friendlyIcon);
      this.suitabilityButtons[2].setToolTipText("Friendly to typical LCD Projectors");
    }
    if(currentSchemeSuitability[hclass - 2][2] == DOUBTFUL){
      this.suitabilityButtons[2].setIcon(this.lCDProjector_doubtfulIcon);
      this.suitabilityButtons[2].setToolTipText("Doubtful whether Friendly to typical LCD Projectors");
    }
    if(currentSchemeSuitability[hclass - 2][2] == BAD){
      this.suitabilityButtons[2].setIcon(this.lCDProjector_badIcon);
      this.suitabilityButtons[2].setToolTipText("Unfriendly to typical LCD Projectors");
    }

    if(currentSchemeSuitability[hclass - 2][3] == GOOD){
      this.suitabilityButtons[3].setIcon(this.laptop_friendlyIcon);
      this.suitabilityButtons[3].setToolTipText("Friendly to laptop LCD Display");
    }
    if(currentSchemeSuitability[hclass - 2][3] == DOUBTFUL){
      this.suitabilityButtons[3].setIcon(this.laptop_doubtfulIcon);
      this.suitabilityButtons[3].setToolTipText("Doubtful whether Friendly to laptop LCD Display");
    }
    if(currentSchemeSuitability[hclass - 2][3] == BAD){
      this.suitabilityButtons[3].setIcon(this.laptop_badIcon);
      this.suitabilityButtons[3].setToolTipText("Unfriendly to laptop LCD Display");
    }

    if(currentSchemeSuitability[hclass - 2][4] == GOOD){
      this.suitabilityButtons[4].setIcon(this.cRT_friendlyIcon);
      this.suitabilityButtons[4].setToolTipText("Friendly to average CRT Display");
    }
    if(currentSchemeSuitability[hclass - 2][4] == DOUBTFUL){
      this.suitabilityButtons[4].setIcon(this.cRT_doubtfulIcon);
      this.suitabilityButtons[4].setToolTipText("Doubtful whether Friendly to average CRT Display");
    }
    if(currentSchemeSuitability[hclass - 2][4] == BAD){
      this.suitabilityButtons[4].setIcon(this.cRT_badIcon);
      this.suitabilityButtons[4].setToolTipText("Unfriendly to average CRT Display");
    }

    if(currentSchemeSuitability[hclass - 2][5] == GOOD){
      this.suitabilityButtons[5].setIcon(this.colorPrinting_friendlyIcon);
      this.suitabilityButtons[5].setToolTipText("Friendly to Color Printing");
    }
    if(currentSchemeSuitability[hclass - 2][5] == DOUBTFUL){
      this.suitabilityButtons[5].setIcon(this.colorPrinting_doubtfulIcon);
      this.suitabilityButtons[5].setToolTipText("Doubtful whether Friendly to Color Printing");
    }
    if(currentSchemeSuitability[hclass - 2][5] == BAD){
      this.suitabilityButtons[5].setIcon(this.colorPrinting_badIcon);
      this.suitabilityButtons[5].setToolTipText("Unfriendly to Color Printing");
    }

    this.uniViewPanel.revalidate();
    this.uniViewPanel.repaint();


  }



  public void valueChanged(ListSelectionEvent e) {

  }


  /** Returns an ImageIcon, or null if the path was invalid. */
  protected static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL = ColorBrewerPlus.class.getResource(path);
    if (imgURL != null) {
      return new ImageIcon(imgURL);
    } else {
      System.err.println("Couldn't find file: " + path);
      return null;
    }
  }


  public static void main(String[] args) {
    JFrame app = new JFrame();
    app.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    ColorBrewerPlus colorBrewerPlus = new ColorBrewerPlus();
    app.getContentPane().add(colorBrewerPlus);
    app.pack();
    app.setVisible(true);
  }

  /**
   * adds an PaletteListener
   */
  public void addPaletteListener(PaletteListener l) {
    listenerList.add(PaletteListener.class, l);
  }

  /**
   * removes an PaletteListener from the component
   */
  public void removePaletteListener(PaletteListener l) {
    listenerList.remove(PaletteListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  private void firePaletteChanged(Palette newPalette) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    PaletteEvent e = null;

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == PaletteListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new PaletteEvent(this, newPalette);
        }

        ((PaletteListener) listeners[i + 1]).paletteChanged(e);
      }
    } //next i
   }

   /**
    * adds an BivariatePaletteListener
    */
   public void addBivariatePaletteListener(BivariatePaletteListener l) {
     listenerList.add(BivariatePaletteListener.class, l);
   }

   /**
    * removes an BivariatePaletteListener from the component
    */
   public void removeBivariatePaletteListener(BivariatePaletteListener l) {
     listenerList.remove(BivariatePaletteListener.class, l);
   }

  public void fireBivariatePaletteChanged(BivariatePalette latestBivariateScheme) {
      // Guaranteed to return a non-null array
      Object[] listeners = listenerList.getListenerList();
      BivariatePaletteEvent e = null;
      // Process the listeners last to first, notifying
      // those that are interested in this event
      for (int i = listeners.length - 2; i >= 0; i -= 2) {
          if (listeners[i] == BivariatePaletteListener.class) {
            if (e == null) {
              e = new BivariatePaletteEvent(this, latestBivariateScheme);
            }

                        ( (BivariatePaletteListener) listeners[i + 1]).bivariatepaletteChanged(e);
          }
      } //next i
  }

  public Palette getLatestUniveriate(){
      return this.latestUnivariateScheme;
  }

  //changed by jfc173 March 8 2005
  public int getHClassCount(){
      return ((SpinnerNumberModel) biHorizontalClassNumber.spinner1.getModel()).getNumber().intValue();
  }

  //added by jfc173 March 7 2005
  public int getVClassCount(){
      return ((SpinnerNumberModel) biVerticalClassNumber.spinner1.getModel()).getNumber().intValue();
  }
  
  //added by jfc173 March 8 2005
  public int getUniClassCount(){
      return ((SpinnerNumberModel) uniClassNumber.spinner1.getModel()).getNumber().intValue();
  }
  
  //added by jfc173 June 17 2005
  public void setUniClassCount(int i){
      ((SpinnerNumberModel) uniClassNumber.spinner1.getModel()).setValue(new Integer(i));
  }
  
  //added by jfc173 June 17 2005
  public void setVClassCount(int i){
      ((SpinnerNumberModel) biVerticalClassNumber.spinner1.getModel()).setValue(new Integer(i));
  }
  
  //added by jfc173 June 17 2005
  public void setHClassCount(int i){      
      ((SpinnerNumberModel) biHorizontalClassNumber.spinner1.getModel()).setValue(new Integer(i));
  }
  
  public void classNumberChanged(ClassNumberEvent e){
    if (e.getBivariateClassNumber() == null){
      this.uniClassNumber.slider1.setValue(e.getClassNumber());
    }else{
      logger.finest("in Color Brewer: " + e.getBivariateClassNumber()[0] + "," + e.getBivariateClassNumber()[1]);
      this.biHorizontalClassNumber.slider1.setValue(e.getBivariateClassNumber()[0]);
      this.biVerticalClassNumber.slider1.setValue(e.getBivariateClassNumber()[1]);
    }
  }

    public JPanel getUniPanel() {
        return uniPanel;
    }

    public JPanel getBiPanel() {
        return biPanel;
    }
}

