/*
 * TMSDSimple.java
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
 * The TMSDSimple implements a view displaying 
 * the activity taking place.
 * The TMSDSimple is configurable to indicate what the activity is.
 * <P>
 * Here, increment() does nothing.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMSDSimple
    extends TMStatusDisplay {

    private JLabel label = null; // the label


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param status    the status text
     */
    TMSDSimple(String status) {
        super(new FlowLayout());
        label = new JLabel(status);
        add(label);
       
        Dimension d = label.getPreferredSize();
        setPreferredSize(d);
        setSize(d);
        setMaximumSize(d);
    }


  /* --- Increment --- */

    /**
     * Increments the display. 
     * Does nothing here.
     */
    void increment() {
    }


  /* --- Clone --- */

    /**
     * Do a deep clone of the TMSDSimple.
     *
     * @return    a deep clone
     */
    TMStatusDisplay deepClone() {
        TMSDSimple clone = new TMSDSimple(label.getText());
        return clone;
    }

  
  /* --- ToString --- */

    /**
     * Returns the TMSDSimple in a String.
     *
     * @return    the status
     */
    public String toString() {
        return label.getText();
    }

}

