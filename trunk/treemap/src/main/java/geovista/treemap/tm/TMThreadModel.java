/*
 * TMThreadModel.java
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

import javax.swing.SwingUtilities;


/**
 * The TMThreadModel abstract class implements a thread of the TMNodeModelRoot.
 * Such a thread is launched to update the TMNodeModel (size, drawing, childs)
 * or to redraw the drawing buffer of the TMView.
 * IT should be executed by a TMThreadQueue.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
abstract class TMThreadModel {

    protected TMStatusView    status    = null; // the status view
    protected TMNodeModelRoot model     = null; // the TMNodeModelRoot
    protected TMView          view      = null; // the TMView to update


    /**
     * Constructor.
     *
     * @param status    the status view for feedback
     * @param model     the model for lock and clearing caches
     * @param view      the view to update
     */
    TMThreadModel(TMStatusView status, TMNodeModelRoot model, TMView view) {
        this.status = status;
        this.model = model;
        this.view = view;
    }
  
    /**
     * Runs the task and the guiTask.
     */
    void run() {
        task();

        model.getLock().lock();
        model.getRoot().clearBuffers();
        model.getLock().unlock();
        
        Runnable guiThread = new Runnable() {
            public void run() {
                guiTask();
            }
        }; 

        SwingUtilities.invokeLater(guiThread);
    }

    /**
     * Task to be executed in the gui thread, once task has finished.
     */
    void guiTask() {
        view.repaint();
    }


  /* --- TO BE IMPLEMENTED IN SUBCLASSES --- */

    /**
     * Task to be executed in a separated thread.
     */
    abstract void task();
}

