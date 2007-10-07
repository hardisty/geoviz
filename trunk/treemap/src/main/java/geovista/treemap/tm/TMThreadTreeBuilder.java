/*
 * TMThreadTreeBuilder.java
 * www.bouthier.net
 *
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
 * The TMThreadTreeBuilder implements a thread that builds the tree
 * of TMNodeModels from the tree of TMNodes.
 * It's the first thread launched by TMNodeModelRoot.TMThreadQueue.
 * After building the tree, it switch the TMView to the initialized state.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMThreadTreeBuilder
    extends TMThreadModel {

    private TMNode                     root        = null; // TMNode tree root
    private TMNodeModelRoot.RootSetter modelAccess = null; // to set the root 
                                                           // of TMNodeModelRoot


    /**
     * Constructor.
     *
     * @param status         the status view for feedback
     * @param model          the TMNodeModelRoot 
     * @param view           the view to update
     * @param root           the root of the TMNode tree
     * @param modelAccess    to set the root of model
     */
    TMThreadTreeBuilder(TMStatusView status, TMNodeModelRoot model, TMView view,
                        TMNode root, TMNodeModelRoot.RootSetter modelAccess) {
        super(status, model, view);

        this.root = root;
        this.modelAccess = modelAccess;
    }

    /**
     * Build the tree.
     */
    void task() {
        TMNodeModel resultingRoot = null;

        status.setStatus(new TMSDProgressSimple("Reading", "TMNodes"));
        if (root.isLeaf()) {
            resultingRoot = new TMNodeModel(root, model);
        } else {
            resultingRoot = new TMNodeModelComposite(root, model);
        }
        status.unsetStatus();

        modelAccess.setRoot(resultingRoot);

        model.computeSize();
    }

    /**
     * Update the view.
     */
    void guiTask() {
        view.Initialized();
        super.guiTask();
    }

}

