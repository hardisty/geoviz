/*
 * TMSDProgressSimple.java
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

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;


/**
 * The TMSDProgressSimple implements a view displaying 
 * the progress of an activity (building, computing size, ...) going
 * on an unknown numbers of elements (nodes, ...).
 * It display the number of elements already done.
 * To increment the number of elements already done, call increment().
 * The TMSDProgressSimple is configurable to indicate what the activity is
 * and what are the kind of elements used. 
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * 
 */
class TMSDProgressSimple
    extends TMStatusDisplay {

    private int    value    = 0;    // value of the progress
    private JLabel progress = null; // status progress
    private JLabel label    = null; // banner


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param activity    the name of the activity
     * @param elements    the name of the elements
     */
    TMSDProgressSimple(String activity, String elements) {
        super(new FlowLayout());

        label = new JLabel(activity + " " + elements + " : ");
        initUI();
    }

    /**
     * Constructor.
     *
     * @param banner    the banner
     * @param value     the current value
     */
    private TMSDProgressSimple(String banner, int value) {
        super(new FlowLayout());

        this.value = value;
        label = new JLabel(banner);
        initUI();
    }

    /**
     * Initialize the UI part.
     */
    private void initUI() {
        progress = new JLabel(Integer.toString(value));
        add(label);
        add(progress);

        Dimension dLabel = label.getPreferredSize();
        Dimension dProgress = progress.getPreferredSize();
        int width = dLabel.width + (5 * dProgress.width) + 10;
        int height = Math.max(dLabel.height, dProgress.height) + 10;
        setPreferredSize(new Dimension(width, height));
        setSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
    }


  /* --- Increment --- */

    /**
     * Increments the display. 
     */
    void increment() {
        value++;
        progress.setText(Integer.toString(value));
        repaint();
    }


  /* --- Clone --- */

    /**
     * Do a deep clone of the TMSDProgressSimple.
     *
     * @return    a deep clone
     */
    TMStatusDisplay deepClone() {
        TMSDProgressSimple clone = new TMSDProgressSimple(label.getText(), 
                                                          value);
        return clone;
    }


  /* --- ToSting --- */

    /**
     * Returns the TMSDProgressSimple in a String.
     *
     * @return    the status
     */
    public String toString() {
        return (label.getText() + progress.getText());
    }

}

