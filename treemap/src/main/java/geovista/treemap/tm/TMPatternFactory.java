/*
 * TMPatternFactory.java
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

package geovista.treemap.tm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * The TMPatternFactory is a utility to get filling patterns.
 * It's a Singleton design pattern, and the only method of interest
 * is get(), which take a String in parameter and return the (if existing)
 * corresponding Paint pattern object.
 * <P>
 * The patterns are :
 * <UL>
 *   <IL> PATTERN_WHITE      
 *   <IL> PATTERN_DIAG1     
 *   <IL> PATTERN_DIAG2     
 *   <IL> PATTERN_DIAGDOTS     
 *   <IL> PATTERN_DIAGS     
 *   <IL> PATTERN_DOTS      
 *   <IL> PATTERN_PLUS      
 *   <IL> PATTERN_TUILES      
 *   <IL> PATTERN_SQUARES      
 *   <IL> PATTERN_LIGHT_GRAY
 *   <IL> PATTERN_DARK_GRAY 
 * </UL>
 * <P>
 * Launch "java geovista.matrix.treemap.tm.TMPatternFactory" to see the differents patterns.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TMPatternFactory {

    private static TMPatternFactory instance = null; // Singleton design pattern

    private Hashtable patterns = null; // set of patterns 


  /* --- Constructor --- */

    /**
     * Constructor.
     */
    public TMPatternFactory() {
        patterns = new Hashtable();
        buildPatternWhite();
        buildPatternDiag1();
        buildPatternDiag2();
        buildPatternDiagDots();
        buildPatternDiags();
        buildPatternDots();
        buildPatternPlus();
        buildPatternTuiles();
        buildPatternSquares();
        buildPatternLightGray();
        buildPatternDarkGray();
    }


  /* --- Singleton pattern --- */

    /**
     * Returns the running instance of TMPatternFactory.
     *
     * @return     the running instance of TMPatternFactory
     */
    public static TMPatternFactory getInstance() {
        if (instance == null) {
            instance = new TMPatternFactory();
        }
        return instance;
    }


  /* --- Patterns accessor --- */

    /**
     * Returns the pattern whose name is given in parameter.
     * If the name is not a recognized pattern name, 
     * prints an error on System.err, and returns <CODE>null<CODE>.
     *
     * @param patternName    the name of the pattern to find
     * @return               the desired pattern;
     *                       <CODE>null</CODE> if the pattern is not found
     */
    public Paint get(String patternName) {
        Paint pattern = (Paint) patterns.get(patternName);
        return pattern;
    }

    /**
     * Returns an Enumeration of patterns names.
     *
     * @return    an Enumeration of patterns' names
     */
    public Enumeration getPatternsNames() {
        return patterns.keys();
    }

  
  /* --- Patterns testing --- */

    /**
     * Display the differents patterns.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test Pattern Factory");
        JPanel panel = new JPanel();
        frame.setContentPane(panel);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        
        TMPatternFactory TM = TMPatternFactory.getInstance();

        for (Enumeration e = TM.getPatternsNames(); e.hasMoreElements(); ) {
            String name = (String) e.nextElement();
            panel.add(buildTestPatternPanel(name, TM.get(name)));
        }

        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Returns a JPanel containing the tested pattern with its name.
     *
     * @param name       the name of the tested pattern
     * @param pattern    the tested pattern
     * @return           the JPanel containing the tested pattern
     */
    private static JPanel buildTestPatternPanel(String name, Paint pattern) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel nameLabel = new JLabel(name);
        panel.add(nameLabel, BorderLayout.SOUTH);

        JPatternPanel patternPanel = new JPatternPanel(pattern);
        panel.add(patternPanel, BorderLayout.CENTER);

        return panel;
    }


  /* --- Patterns building --- */

    /**
     * Builds and adds the PATTERN_WHITE in patterns.
     */
    private void buildPatternWhite() {
        patterns.put("PATTERN_WHITE", Color.white);
    }
 
    /**
     * Builds and adds the PATTERN_DIAG1 in patterns.
     */
    private void buildPatternDiag1() {
        BufferedImage image = new BufferedImage(14, 14, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 14, 14);
        g.setColor(Color.black);
        g.drawLine(7, 0, 14, 7);
        g.drawLine(0, 0, 14, 14);
        g.drawLine(0, 7, 7, 14);
        Rectangle r = new Rectangle(0, 0, 14, 14);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_DIAG1", pattern);
    }

    /**
     * Builds and adds the PATTERN_DIAG2 in patterns.
     */
    private void buildPatternDiag2() {
        BufferedImage image = new BufferedImage(14, 14, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 14, 14);
        g.setColor(Color.black);
        g.drawLine(0, 0, 14, 14);
        g.drawLine(3, 0, 14, 11);
        g.drawLine(7, 0, 14, 7);
        g.drawLine(11, 0, 14, 3);
        g.drawLine(0, 3, 11, 14);
        g.drawLine(0, 7, 7, 14);
        g.drawLine(0, 11, 3, 14);
        Rectangle r = new Rectangle(0, 0, 14, 14);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_DIAG2", pattern);
    }

    /**
     * Builds and adds the PATTERN_DIAGS in patterns.
     */
    private void buildPatternDiags() {
        BufferedImage image = new BufferedImage(10, 10, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 10, 10);
        g.setColor(Color.black);
        g.drawLine(2, 2, 4, 4);
        g.drawLine(7, 9, 9, 7);
        Rectangle r = new Rectangle(0, 0, 10, 10);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_DIAGS", pattern);
    }

    /**
    /**
     * Builds and adds the PATTERN_DIAGDOTS in patterns.
     */
    private void buildPatternDiagDots() {
        BufferedImage image = new BufferedImage(13, 13, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 13, 13);
        g.setColor(Color.black);
        g.drawLine(0, 0, 13, 13);
        g.drawLine(3, 0, 13, 10);
        g.drawLine(7, 0, 13, 6);
        g.drawLine(11, 0, 13, 2);
        g.drawLine(0, 3, 10, 13);
        g.drawLine(0, 7, 6, 13);
        g.drawLine(0, 11, 2, 13);
        Rectangle r = new Rectangle(0, 0, 13, 13);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_DIAGDOTS", pattern);
    }

    /**
     * Builds and adds the PATTERN_DOTS in patterns.
     */
    private void buildPatternDots() {
        BufferedImage image = new BufferedImage(5, 5, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 5, 5);
        g.setColor(Color.black);
        g.fillOval(1, 1, 3, 3);
        Rectangle r = new Rectangle(0, 0, 5, 5);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_DOTS", pattern);
    }

    /**
     * Builds and adds the PATTERN_PLUS in patterns.
     */
    private void buildPatternPlus() {
        BufferedImage image = new BufferedImage(10, 10, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 10, 10);
        g.setColor(Color.black);
        g.drawLine(3, 5, 8, 5);
        g.drawLine(3, 6, 8, 6);
        g.drawLine(5, 3, 5, 8);
        g.drawLine(6, 3, 6, 8);
        Rectangle r = new Rectangle(0, 0, 10, 10);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_PLUS", pattern);
    }

    /**
     * Builds and adds the PATTERN_TUILES in patterns.
     */
    private void buildPatternTuiles() {
        BufferedImage image = new BufferedImage(10, 10,  
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 10, 10);
        g.setColor(Color.black);
        g.drawArc(-5, -5, 10, 10, 0, -90);
        g.drawArc(5, -5, 10, 10, 270, -90);
        g.drawArc(0, 0, 10, 10, 0, -180);
        Rectangle r = new Rectangle(0, 0, 10, 10);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_TUILES", pattern);
    }

    /**
     * Builds and adds the PATTERN_SQUARES in patterns.
     */
    private void buildPatternSquares() {
        BufferedImage image = new BufferedImage(8, 9, 
                                                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, 8, 9);
        g.setColor(Color.black);
        g.fillRect(2, 2, 3, 3);
        g.fillRect(5, 6, 3, 3);
        Rectangle r = new Rectangle(0, 0, 8, 9);
        Paint pattern = new TexturePaint(image, r);
        patterns.put("PATTERN_SQUARES", pattern);
    }

    /**
     * Builds and adds the PATTERN_LIGHT_GRAY in patterns.
     */
    private void buildPatternLightGray() {
        patterns.put("PATTERN_LIGHT_GRAY", Color.lightGray);
    }

    /**
     * Builds and adds the PATTERN_DARK_GRAY in patterns.
     */
    private void buildPatternDarkGray() {
        patterns.put("PATTERN_DARK_GRAY", Color.darkGray);
    }

}


/**
 * Inner class implementing a JPanel filled with a pattern.
 */
class JPatternPanel
    extends JPanel {

    private Paint pattern = null; // the pattern to paint

    /**
     * Constructor taking the filling pattern in parameter.
     *
     * @param pattern     the filling pattern
     */
    public JPatternPanel(Paint pattern) {
        this.pattern = pattern;
        setPreferredSize(new Dimension(50, 50));
    }

    /**
     * Paint method.
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(pattern);
        g2.fillRect(getX(), getY(), getWidth(), getHeight());
    }

}

