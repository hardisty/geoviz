/*
 * TMThreadChangeDrawing.java
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


/**
 * The TMThreadChangeDrawing implements a thread that change the TMComputeDraw
 * object used to compute the drawing of TMNodeModel.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMThreadChangeDrawing
    extends TMThreadModel {

    private TMNodeModelRoot.CDrawSetter cDrawSetter = null; // changing object
    
    /**
     * Constructor.
     *
     * @param status          the status view for feedback
     * @param model           the TMNodeModelRoot 
     * @param view            the view to update
     * @param cDrawSetter     the changing object
     */
    TMThreadChangeDrawing(TMStatusView status, TMNodeModelRoot model, 
                          TMView view,
                          TMNodeModelRoot.CDrawSetter cDrawSetter) {
        super(status, model, view);
        this.cDrawSetter = cDrawSetter;
    }

    /**
     * Change the TMComputeDraw.
     */
    void task() {
        status.setStatus(new TMSDSimple("Changing TMComputeDraw object ..."));
        cDrawSetter.setCDraw();
        model.flushDraw();
        model.computeDrawing();
        status.setStatus(new TMSDSimple("TMComputeDraw changed"));
    }
    
}
