/*
 * TMSDProgressBar.java
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

package treemap;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;


/**
 * The TMSDProgressBar implements a view displaying 
 * the progress of an activity (building, computing size, ...) going
 * on a fixed known numbers of elements (nodes, ...).
 * It display a progress bar, the number of elements already done, 
 * and the total number of elements.
 * To increment the progress bar and the number of elements already done,
 * call increment().
 * The TMSDProgressBar is configurable to indicate what the activity is
 * and what are the kind of elements used. 
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMSDProgressBar
    extends TMStatusDisplay {

    private int          value       = 0;    // value of the progress
    private JLabel       progress    = null; // status progress
    private JProgressBar progressBar = null; // progress bar
    private JLabel       l1          = null; // banner 1
    private JLabel       l2          = null; // banner 2


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param limit       the total number of elements
     * @param activity    the name of the activity
     * @param elements    the name of the elements
     */
    TMSDProgressBar(int limit, String activity, String elements) {
        super(new FlowLayout());
        
        l1 = new JLabel(activity + " ");
        l2 = new JLabel(" " + elements + " in " + limit);
        initUI(limit);
    }

    /**
     * Constructor.
     *
     * @param limit         the total number of elements
     * @param bannerAct     the activity banner
     * @param bannerElmt    the elements banner
     * @param value         the current value
     */
    private TMSDProgressBar(int limit, String bannerAct, String bannerElmt, 
                            int value) {
        super(new FlowLayout());

        this.value = value;
        l1 = new JLabel(bannerAct);
        l2 = new JLabel(bannerElmt);
        initUI(limit);
    }

    /**
     * Initialize the UI part.
     *
     * @param limit    the limit of the progress bar
     */
    private void initUI(int limit) {
        JPanel containPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel paneBar = new JPanel(new FlowLayout());
        progressBar = new JProgressBar(0, limit);
        progressBar.setValue(value);
        paneBar.add(progressBar);
        containPanel.add(paneBar);

        JPanel paneStatus = new JPanel(new FlowLayout()); 
        progress = new JLabel(Integer.toString(value));

        paneStatus.add(l1);
        paneStatus.add(progress);
        paneStatus.add(l2);
        containPanel.add(paneStatus);

        Dimension dPB = progressBar.getPreferredSize();
        Dimension dL1 = l1.getPreferredSize();
        Dimension dL2 = l2.getPreferredSize();
        Dimension dP  = progress.getPreferredSize();
        int width = dL1.width + (5 * dP.width) + dL2.width;
        int height = Math.max(Math.max(dL1.height, dL2.height), dP.height);

        dPB.width = width;
        progressBar.setPreferredSize(dPB);
        progressBar.setSize(dPB);
        progressBar.setMaximumSize(dPB);

        Dimension dPS = new Dimension(width, height);
        paneStatus.setPreferredSize(dPS);
        paneStatus.setSize(dPS);
        paneStatus.setMaximumSize(dPS);

        height += dPB.height + 15;
        width += 15;
        Dimension d = new Dimension(width, height);
        containPanel.setPreferredSize(d);
        containPanel.setSize(d);
        containPanel.setMaximumSize(d);

        add(containPanel);
    }


  /* --- Increment --- */

    /**
     * Increments the display. 
     */
    void increment() {
        value++;
        progressBar.setValue(value);
        progress.setText(Integer.toString(value));
        repaint();
    }


  /* --- Clone --- */

    /**
     * Do a deep clone of the TMSDProgressBar.
     *
     * @return    a deep clone
     */
    TMStatusDisplay deepClone() {
        TMSDProgressBar clone = new TMSDProgressBar(progressBar.getMaximum(), 
                                                    l1.getText(), l2.getText(),
                                                    value);
        return clone;
    }


  /* --- ToSting --- */

    /**
     * Returns the TMSDProgressBar in a String.
     *
     * @return    the status
     */
    public String toString() {
        return (l1.getText() + progress.getText() + l2.getText());
    }

}

