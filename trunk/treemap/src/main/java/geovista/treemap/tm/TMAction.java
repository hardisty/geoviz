/*
 * TMAction.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2003 Christophe Bouthier
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * The TMAction class manages the action on the geovista.matrix.treemap.tm.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * .2
 */
public class TMAction
    extends MouseAdapter {

    private TMView view = null; // the view managed


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param view    the view managed
     */
    public TMAction(TMView view) {
        this.view = view;
    }
    

  /* --- MouseAdapter --- */

    /**
     * Called when a user clicked on the geovista.matrix.treemap.tm.
     * Used to zoom or unzoom.
     *
     * @param e    the MouseEvent generated when clicking
     */
    public void mouseClicked(MouseEvent e) {
        if (e.isShiftDown()) {
            view.unzoom();
        } else {
            view.zoom(e.getX(), e.getY());
        }
    }

}

