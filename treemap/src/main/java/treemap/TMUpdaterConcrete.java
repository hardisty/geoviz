/*
 * TMUpdaterConcrete.java
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

import java.util.Enumeration;
import java.util.Observable;


/**
 * The TMUpdaterConcrete is responsible for propagating changes in the TMNodes 
 * (user's node objets) to all views.
 * For this, it extends java.util.Observable (Observer design pattern).
 * <P>
 * For the user, the TMUpdaterConcrete is seen as a TMUpdater interface.
 * It's the Bridge design pattern.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMUpdaterConcrete
    extends    Observable 
    implements TMUpdater {

    private TMNode root = null; // the root of the TMNode tree


  /* --- Constructor --- */

    /**
     * Constructor.
     * Goes throught the TMNode tree and call
     * each TMNode's setUpdater() method with itself
     * as parameter.
     *
     * @param node    the root of the TMNode tree
     */
    TMUpdaterConcrete(TMNode root) {
        this.root = root;
        setUpdater(root);
    }

    /**
     * Sets the updater of the given node and of 
     * all its child to be this TMUpdaterConcrete object.
     *
     * @param node    the root of the TMNode tree to set updater on
     */ 
    private void setUpdater(TMNode node) {
        node.setUpdater(this);
        if (! node.isLeaf()) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                // should test here that e is not null
                // should test here that we really have a TMNode object
                TMNode childNode = (TMNode) e.nextElement();
                setUpdater(childNode);
            }
        }
    }
    
    
  /* --- Accessor --- */
  
    /**
     * Returns the root of the TMNode tree encapsulated.
     *
     * @return    the root of the TMNode tree
     */
    TMNode getRoot() {
        return root;
    }


 /* --- Updates --- */
 
     /**
     * To be called when the user node has its size changed.
     *
     * @param node    the node that has its size changed
     */
    public void updateSize(TMNode node) {
        setChanged();
        notifyObservers(new TMEventUpdateSize(node));
    }
    
    /**
     * To be called when the user node has its state changed.
     *
     * @param node    the node that has its state changed
     */    
    public void updateState(TMNode node) {
        setChanged();
        notifyObservers(new TMEventUpdateDraw(node));
    }

    /**
     * To be called when the user node has a new child.
     *
     * @param node     the node that has a new child
     * @param child    the node's new child
     */
    public void addChild(TMNode node, TMNode child) {
        setUpdater(child);
        setChanged();
        notifyObservers(new TMEventUpdateNewChild(node, child));
    }

    /**
     * To be called when the user node removes a child.
     *
     * @param node     the node that removes a child
     * @param child    the node's lost child
     */
    public void removeChild(TMNode node, TMNode child) {
        setChanged();
        notifyObservers(new TMEventUpdateLostChild(node, child));
    }

}

