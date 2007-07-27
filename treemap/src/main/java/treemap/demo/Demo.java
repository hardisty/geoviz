/*
 * Demo.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package treemap.demo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import treemap.TMView;
import treemap.TreeMap;


/**
 * The Demo class implements a demo for Treemap. It display a treemap
 * view of a demo tree, and a configuration frame.
 * The demo tree is build from a file tree, passed in parameter.
 * Demo use TMFileNode as TMNode, draw and size algorithm in the treemap.demo
 * package. Demo could take an argument, the path from which
 * start the representing of files.
 * If no arguments is given, the treemap start from the root.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class Demo {

    private static int        count   = 1;    // to have unique view name

    private static TMFileNode root    = null; // the root of the demo tree
    private static TreeMap    treeMap = null; // the treemap builded


    /**
     * Display a demo TreeMap.
     */
    public static void main(String[] args) {
        String pathRoot = null;

        if (args.length > 0) {
            pathRoot = args[0];
        } else {
            pathRoot = ".";
        }
        pathRoot = "C:\\geovista\\treemap\\src";
        pathRoot = "C:\\geovista\\geovistastudio\\Applications\\GeoVizToolkit\\src\\edu\\sc";
        File rootFile = new File(pathRoot);
        try {
            System.out.println("Starting the treemap from " +
                                rootFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (! rootFile.exists()) {
            System.out.println("Can't start treemap : " + rootFile.getName() +
                               " does not exist.");
            return;
        }


        root = new TMFileNode(rootFile);
        if (root == null) {
            System.err.println("Error : can't start treemap from " +
                                rootFile.getAbsolutePath());
            return;
        }

        treeMap = new TreeMap(root);
        TMView view = buildNewView();

        Demo demo = new Demo();
        ConfFrame confFrame = demo. new ConfFrame();
        ConfView  confView = demo. new ConfView(view);
        confFrame.addConfView(confView);
        confFrame.pack();
        confFrame.setVisible(true);
    }


    /**
     * Build a new TMView, shows it in a frame, and return the TMView.
     */
    static TMView buildNewView() {
        TMFileSize fSize = new TMFileSize();
        TMFileDraw fDraw = new TMFileDraw();
        TMView view = treeMap.getView(fSize, fDraw);
        JFrame viewFrame = new JFrame(root.getFullName() + " : " + count);
        count++;
        viewFrame.setContentPane(view);
        viewFrame.pack();
        viewFrame.setVisible(true);
        return view;
    }



  /* --- Inner conf class --- */

    /**
     * The ConfFrame class implements a configuration frame
     * containing ConfView for treemap's views.
     */
    class ConfFrame
        extends JFrame {

        private JTabbedPane tabbedPane = null; // one tab per view


        /**
         * Constructor.
         */
        ConfFrame() {
            super("Configuration");

            JPanel panel = new JPanel(new BorderLayout());
            setContentPane(panel);
            tabbedPane = new JTabbedPane();
            panel.add(tabbedPane, BorderLayout.CENTER);
            JButton button = new JButton("Create new view");
            JPanel buttonPane = new JPanel(new FlowLayout());
            buttonPane.add(button);
            panel.add(buttonPane, BorderLayout.NORTH);

            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    TMView view = buildNewView();
                    ConfView confV = Demo.this. new ConfView(view);
                    addConfView(confV);
                }
            });

        }


        /**
         * Adds a tab pane.
         */
        void addConfView(ConfView view) {
            tabbedPane.addTab(view.getName(), view);
        }
    }


    /**
     * The ConfView class implements a configuration view
     * for a treemap view.
     */
    class ConfView
        extends JPanel {

        private TMView            view    = null; // the TMView to configure
        private TMFileDraw        fDraw   = null; // compute draw with color
        private TMFileDrawPattern fDrawP  = null; // compute draw with pattern
        private TMFileSize        fSize   = null; // compute size with file size
        private TMFileSizeDate    fSizeD  = null; // compute size with file date

        private JRadioButton AlgoClassic  = null; // algo classic choice
        private JRadioButton AlgoSquar    = null; // algo squartified choice
        private JRadioButton DrawColor    = null; // compute draw color choice
        private JRadioButton DrawPattern  = null; // compute draw pattern choice
        private JRadioButton SizeSize     = null; // compute size size choice
        private JRadioButton SizeDate     = null; // compute size date choice

        private JCheckBox    TitleBox     = null; // draw or not nodes titles

        private JPanel       AlgoPanel    = null; // the algorithm choice panel
        private JPanel       AlgoConfView = null; // the algorithm conf view

        /**
         * Constructor.
         *
         * @param view    the TMView to configure
         */
        ConfView(TMView view) {
            super(new BorderLayout());
            this.view = view;
            setName("View " + (count - 1));
            fDraw = new TMFileDraw();
            fDrawP = new TMFileDrawPattern();
            fSize = new TMFileSize();
            fSizeD = new TMFileSizeDate();


            Border etchedBorder = BorderFactory.createEtchedBorder();

         // NORTH
            AlgoPanel = new JPanel(new BorderLayout());
            AlgoPanel.setBorder(BorderFactory.createTitledBorder(etchedBorder,
                                                                 "Algorithm"));
            add(AlgoPanel, BorderLayout.NORTH);

         // CENTER
            JPanel CenterPanel = new JPanel(new GridLayout(3, 1));
            add(CenterPanel, BorderLayout.CENTER);

            JPanel DrawPanel = new JPanel(new GridLayout(1, 2));
            DrawPanel.setBorder(BorderFactory.createTitledBorder(etchedBorder,
                                                                 "Draw"));
            CenterPanel.add(DrawPanel);

            JPanel SizePanel = new JPanel(new GridLayout(1, 2));
            SizePanel.setBorder(BorderFactory.createTitledBorder(etchedBorder,
                                                                 "Size"));
            CenterPanel.add(SizePanel);

            JPanel TitlePanel = new JPanel(new FlowLayout());
            TitlePanel.setBorder(BorderFactory.createTitledBorder(etchedBorder,
                                                                  "Titles"));
            CenterPanel.add(TitlePanel);

         // SOUTH
            JPanel SouthPanel = new JPanel(new FlowLayout());
            JLabel statusLabel = new JLabel("Status : ");
            SouthPanel.add(statusLabel);
            SouthPanel.add(view.getStatusView());
            add(SouthPanel, BorderLayout.SOUTH);


         // AlgoPanel
            JPanel choicePanel = new JPanel(new GridLayout(1, 2));
            AlgoPanel.add(choicePanel, BorderLayout.NORTH);
            AlgoClassic = new JRadioButton("Classic", true);
            AlgoSquar = new JRadioButton("Squarified");
            ButtonGroup bg = new ButtonGroup();
            bg.add(AlgoClassic);
            bg.add(AlgoSquar);
            choicePanel.add(AlgoClassic);
            choicePanel.add(AlgoSquar);
            AlgoConfView = view.getAlgorithm().getConfiguringView();
            AlgoPanel.add(AlgoConfView, BorderLayout.SOUTH);
            AlgoClassic.addActionListener(new AlgoClassicListener());
            AlgoSquar.addActionListener(new AlgoSquarListener());

         // DrawPanel
            DrawColor = new JRadioButton("Color", true);
            DrawPattern = new JRadioButton("Pattern");
            ButtonGroup bgD = new ButtonGroup();
            bgD.add(DrawColor);
            bgD.add(DrawPattern);
            DrawPanel.add(DrawColor);
            DrawPanel.add(DrawPattern);
            DrawColor.addActionListener(new DrawColorListener());
            DrawPattern.addActionListener(new DrawPatternListener());

         // SizePanel
            SizeSize = new JRadioButton("File Size", true);
            SizeDate = new JRadioButton("File Date");
            ButtonGroup bgS = new ButtonGroup();
            bgS.add(SizeSize);
            bgS.add(SizeDate);
            SizePanel.add(SizeSize);
            SizePanel.add(SizeDate);
            SizeSize.addActionListener(new SizeSizeListener());
            SizeDate.addActionListener(new SizeDateListener());

         // TitlePanel
            TitleBox = new JCheckBox("Draw Titles", view.isDrawingTitle());
            TitlePanel.add(TitleBox);
            TitleBox.addActionListener(new TitleBoxListener());
        }


      /* --- Inner's Inner action listener --- */

        /**
         * Algo classic radio button listener.
         */
        class AlgoClassicListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                view.setAlgorithm(TMView.CLASSIC);
                AlgoPanel.remove(AlgoConfView);
                AlgoConfView = view.getAlgorithm().getConfiguringView();
                TitleBox.setSelected(view.isDrawingTitle());
                AlgoPanel.add(AlgoConfView, BorderLayout.SOUTH);
                AlgoPanel.revalidate();
            }
        }

        /**
         * Algo squar radio button listener.
         */
        class AlgoSquarListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                view.setAlgorithm(TMView.SQUARIFIED);
                AlgoPanel.remove(AlgoConfView);
                AlgoConfView = view.getAlgorithm().getConfiguringView();
                TitleBox.setSelected(view.isDrawingTitle());
                AlgoPanel.add(AlgoConfView, BorderLayout.SOUTH);
                AlgoPanel.revalidate();
            }
        }

        /**
         * Draw color radio button listener.
         */
        class DrawColorListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                view.changeTMComputeDraw(fDraw);
            }
        }

        /**
         * Draw pattern radio button listener.
         */
        class DrawPatternListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                view.changeTMComputeDraw(fDrawP);
            }
        }

        /**
         * Size file size radio button listener.
         */
        class SizeSizeListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                view.changeTMComputeSize(fSize);
            }
        }

        /**
         * Size file date radio button listener.
         */
        class SizeDateListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                view.changeTMComputeSize(fSizeD);
            }
        }

        /**
         * Title checkbox listener.
         */
        class TitleBoxListener
            implements ActionListener {

            public void actionPerformed(ActionEvent e) {
                if (TitleBox.isSelected()) {
                    view.DrawTitles(true);
                } else {
                    view.DrawTitles(false);
                }
            }
        }
    }


}

