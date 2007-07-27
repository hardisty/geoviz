/*
 * TMThreadUpdateSize.java
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


/**
 * The TMThreadUpdateSize implements a thread that update the size of a 
 * TMNodeModel.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMThreadUpdateSize
    extends TMThreadModel {

    private TMNode node = null; // the node which has its size changed


    /**
     * Constructor.
     *
     * @param status         the status view for feedback
     * @param model          the TMNodeModelRoot 
     * @param view           the view to update
     * @param node           the updated node
     */
    TMThreadUpdateSize(TMStatusView status, TMNodeModelRoot model, TMView view,
                       TMNode node) {
        super(status, model, view);
        this.node = node;
    }

    /**
     * Update the size.
     */
    void task() {
        status.setStatus(new TMSDSimple("Updating size..."));
        TMNodeModel dirtyNode = model.nodeContaining(node);
        if (dirtyNode == null) {
            throw new TMExceptionUnknownTMNode(node);
        }
        dirtyNode.updateSize();

        model.computeSize();
        status.setStatus(new TMSDSimple("Size updated"));
    }

}

