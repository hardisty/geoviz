/*
 * TMEventUpdateLostChild.java
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
 * The TMEventUpdateLostChild class implements a update event
 * for removing a child of a TMNode.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMEventUpdateLostChild
    extends TMEventUpdate {

    private TMNode child = null; 

 
  /* --- Constructor --- */
 
    /**
     * Constructor.
     *
     * @param node     the node who lost a child
     * @param child    the lost child
     */
    TMEventUpdateLostChild(TMNode node, TMNode child) {
        super(node);
        this.child = child;
    }


  /* --- Command design pattern --- */

    /**
     * Executed on the TMView when received.
     */
    void execute(TMNodeModelRoot model) {
        model.lostChild(getNode(), child);
    }
 
}
 
