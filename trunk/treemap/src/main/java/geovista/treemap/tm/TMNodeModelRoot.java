/*
 * TMNodeModelRoot.java
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
 * The TMNodeModelRoot class implements the root of the tree of TMNodeModels.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * 
 */
class TMNodeModelRoot {

    private int            dirtySNodes = 0;    // number of dirty size nodes
    private int            dirtyDNodes = 0;    // number of dirty draw nodes

    private TMComputeSize  cSize       = null; // object computing the size
    private TMComputeDraw  cDraw       = null; // object computing the drawing

    private TMNodeModel    root        = null; // the root of TMNodeModel tree
    private TMStatusView   status      = null; // the status view
    private TMView         view        = null; // the view
    private TMThreadQueue  threadQueue = null; // the thread queue
    private TMThreadLock   lock        = null; // threads lock


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root      the root of the TMNode tree
     * @param cSize     the object computing the size;
     * @param cDraw     the object computing the drawing;
     * @param status    the status view
     * @param view      the view
     */
    TMNodeModelRoot(TMNode root, TMComputeSize cSize, TMComputeDraw cDraw, 
                    TMStatusView status, TMView view) {
        this.cSize = cSize;
        this.cDraw = cDraw;
        this.status = status;
        this.view = view;
        this.threadQueue = new TMThreadQueue();
        this.lock = new TMThreadLock();

        launchTMNodeModelTreeBuilding(root);
    }


  /* --- Accessor --- */

    /**
     * Returns the root of the TMNodeModel tree.
     *
     * @return    the root of the TMNodeModel tree
     */
    TMNodeModel getRoot() {
        return root;
    }


    /**
     * Returns the TMThreadLock used to separate updating and drawing threads.
     * 
     * @return    the lock
     */
    TMThreadLock getLock() {
        return lock;
    }


  /* --- Zooming --- */

    /**
     * Zooms the geovista.matrix.treemap.tm of one level in the direction of the node
     * at the given coordinates.
     *
     * @param x    the x coord
     * @param y    the y coord
     */
    public void zoom(int x,
                     int y) {
        TMNodeModel endNode = nodeContaining(x, y);
        if (endNode != null) {
            TMNodeModel ancestor = endNode.getParent();
            while((ancestor != null) &&
                  (ancestor != root)) {
                endNode = ancestor;
                ancestor = endNode.getParent();
            }
            root = endNode;
            view.repaint();
        }
    }

    /**
     * Unzooms.
     * Does nothing if already at the root.
     */
    public void unzoom() {
        TMNodeModel ancestor = root.getParent();
        if (ancestor != null) {
            root = ancestor;
            view.repaint();
        }
    }
    

  /* --- cDraw and cSize management --- */

    /**
     * Returns the object in charge of computing size of TMNodes.
     *
     * @return    the object in charge of computing size of TMNodes
     */
    TMComputeSize getCSize() {
        return cSize;
    }
    
    /**
     * Returns the object in charge of computing the drawing of TMNodes.
     *
     * @return    the object in charge of computing the drawing of TMNodes
     */
    TMComputeDraw getCDraw() {
        return cDraw;
    }


  /* --- Status changes --- */

    /**
     * Increments the number of created nodes.
     */
    void incrementNumberOfNodes() {
        status.increment();
    }

    /**
     * Increments the number of dirty size nodes.
     */
    void incrementNumberOfDirtySNodes() {
        dirtySNodes++;
    }

    /**
     * Decrements the number of dirty size nodes.
     */
    void decrementNumberOfDirtySNodes() {
        if (dirtySNodes > 0) {
            dirtySNodes--;
        }
        status.increment();
    }

    /**
     * Increments the number of dirty draw nodes.
     */
    void incrementNumberOfDirtyDNodes() {
        dirtyDNodes++;
    }

    /**
     * Decrements the number of dirty draw nodes.
     */
    void decrementNumberOfDirtyDNodes() {
        if (dirtyDNodes > 0) {
            dirtyDNodes--;
        }
        status.increment();
    }


  /* --- Finding node --- */

    /**
     * Returns the most inner TMNodeModel
     * which contains in its drawing area
     * the given coordonates.
     *
     * @param x    the X coordonate
     * @param y    the Y coordonate
     * @return     the TMNodeModel containing thoses coordonates;
     *             <CODE>null</CODE> if there is no such TMNodeModel
     */
    TMNodeModel nodeContaining(int x, int y) {
        return root.nodeContaining(x, y);
    }

    /**
     * Returns the most inner TMNodeModel
     * which contains the given TMNode.
     *
     * @param node    the TMNode
     * @return        the TMNodeModel containing this TMNode;
     *                <CODE>null</CODE> if there is no such TMNodeModel
     */
    TMNodeModel nodeContaining(TMNode node) {
        return root.nodeContaining(node);
    }


  /* --- Computing --- */

    /**
     * Compute the size of the dirty nodes, and as the drawing
     * could depends on the size, call computeDrawing.
     *
     * @return         the size of the node
     */
    void computeSize() {
        if (dirtySNodes > 0) {
            status.setStatus(new TMSDProgressBar(dirtySNodes, 
                                                "Computing size of", "nodes"));
            root.computeSize(); 
            status.unsetStatus();

            getLock().lock();
            getRoot().clearBuffers();
            getLock().unlock();

            computeDrawing();
        }
    }


    /**
     * Compute the filling and the tooltip 
     * of the dirty drawing nodes.
     */
    void computeDrawing() {
        if (dirtyDNodes > 0) {
            status.setStatus(new TMSDProgressBar(dirtyDNodes, 
                                             "Computing drawing of", "nodes"));
            root.computeDrawing(); 
            status.unsetStatus();
        }
    }


  /* --- Updates --- */

    /**
     * Updates the size of the TMNodeModel containing
     * the given TMNode.
     *
     * @param node    the dirty TMNode
     */
    void updateSize(TMNode node) {
        TMThreadModel task = new TMThreadUpdateSize(status, this, view, node);
        threadQueue.add(task);
    }

    /**
     * Updates the drawing of the TMNodeModel
     * containing the given TMNode.
     *
     * @param node    the dirty TMNode
     */
    void updateDrawing(TMNode node) {
        TMThreadModel task = new TMThreadUpdateDraw(status, this, view, node);
        threadQueue.add(task);
    }


    /**
     * Builds the hierarchie of TMNodeModel corresponding
     * to the TMNode child given, and adds it as a child to 
     * the TMNodeModel containing the TMNode parent.
     * Updates the size and the drawing of the parents.
     *
     * @param parent    the parent TMNode
     * @param child     the child TMNode
     */
    void newChild(TMNode parent, TMNode child) {
        TMThreadModel task = new TMThreadNewChild(status, this, view, 
                                                  parent, child);
        threadQueue.add(task);
    }

    /**
     * Removes the TMNodeModel corresponding
     * to the TMNode child given from the children
     * of the TMNodeModel containing the TMNodeParent.
     * Updates the size and the drawing of the parents.
     *
     * @param parent    the parent TMNode
     * @param child     the child TMNode
     */
    void lostChild(TMNode parent, TMNode child) {
        TMThreadModel task = new TMThreadLostChild(status, this, view, 
                                                   parent, child);
        threadQueue.add(task);
    }


  /* --- Changing TMComputeSize or TMComputeDraw --- */

    /**
     * Launch the changing of the TMComputeSize object
     * in a separated thread, executed by the TMThreadQueue.
     *
     * @param cSize    the new TMComputeSize object
     */
    void launchChangeSizing(TMComputeSize cSize) {
        TMThreadModel task = new TMThreadChangeSizing(status, this, view, 
                                                      new CSizeSetter(cSize));
        threadQueue.add(task);
    }
  
    /**
     * Launch the changing of the TMComputeDraw object
     * in a separated thread, executed by the TMThreadQueue.
     *
     * @param cDraw    the new TMComputeDraw object
     */
    void launchChangeDrawing(TMComputeDraw cDraw) {
        TMThreadModel task = new TMThreadChangeDrawing(status, this, view, 
                                                       new CDrawSetter(cDraw));
        threadQueue.add(task);
    }

    /**
     * Flush the dirtyD flag for the whole tree. 
     */
    void flushDraw() {
        root.flushDraw();
    }

    /**
     * Flush the dirtyS and dirtyD flags for the whole tree.
     */
    void flushAll() {
        root.flushAll();
    }


  /* --- Threads --- */

    /**
     * Launch the building of the TMNodeModel's tree
     * in a separated thread, executed by the TMThreadQueue.
     *
     * @param root    the root of the TMNode tree
     * @param view    the view to update 
     */
    private void launchTMNodeModelTreeBuilding(TMNode root) {
        TMThreadModel task = new TMThreadTreeBuilder(status, this, view,
                                                     root, new RootSetter());
        threadQueue.add(task);
    }


  /* --- Inners access class --- */

    /**
     * The goal of the RootSetter inner class is to permit
     * the access to the root private variable of the TMNodeModelRoot.
     * With it, we can permit the setting of the root only to
     * thoses that we wants.
     * It's a kind of execution-dependant access right to private variable.
     * I don't know if it's really pure object oriented, but
     * I find it really fun :-)
     */
    class RootSetter {
       
        /**
         * Sets the root of the model.
         *
         * @param root    the new root of TMNodeModel tree
         */
        void setRoot(TMNodeModel root) {
            TMNodeModelRoot.this.root = root;
        }
    }  

    /**
     * The goal of the CSizeSetter inner class is to permit
     * the access to the cSize private variable of the TMNodeModelRoot.
     * With it, we can permit the setting of the cSize only to
     * thoses that we wants.
     * It's a kind of execution-dependant access right to private variable.
     * I don't know if it's really pure object oriented, but
     * I find it really fun :-)
     */
    class CSizeSetter {
    
        private TMComputeSize cSize = null; // the new TMComputeSize
   
         /**
         * Constructor.
         *
         * @param cSize    the new TMComputeSize
         */
        CSizeSetter(TMComputeSize cSize) {
            this.cSize = cSize;
        }
       
        /**
         * Sets the cSize of the model.
         */
        void setCSize() {
            TMNodeModelRoot.this.cSize = cSize;
        }
    }  

    /**
     * The goal of the CDrawSetter inner class is to permit
     * the access to the cDraw private variable of the TMNodeModelRoot.
     * With it, we can permit the setting of the cDraw only to
     * thoses that we wants.
     * It's a kind of execution-dependant access right to private variable.
     * I don't know if it's really pure object oriented, but
     * I find it really fun :-)
     */
    class CDrawSetter {
    
        private TMComputeDraw cDraw = null; // the new TMComputeDraw
   
         /**
         * Constructor.
         *
         * @param cDraw    the new TMComputeDraw
         */
        CDrawSetter(TMComputeDraw cDraw) {
            this.cDraw = cDraw;
        }
       
        /**
         * Sets the cDraw of the model.
         */
        void setCDraw() {
            TMNodeModelRoot.this.cDraw = cDraw;
        }
    }  

}

