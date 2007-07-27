/*
 * TMModelUpdaterConcrete.java
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

import java.util.Hashtable;


/**
 * The TMModelUpdaterConcrete is responsible for propagating changes in the 
 * TMModelNode (user's tree model) to all views.
 * For this, it uses a TMUpdaterConcrete.
 * <P>
 * For the user, the TMModelUpdaterConcrete is seen as a TMModelUpdater 
 * interface.
 * It's the Bridge design pattern.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMModelUpdaterConcrete
    implements TMModelUpdater {

	private TMUpdaterConcrete  updater = null; // the real updater	
	private TMModelNode        model   = null; // the tree model
	private TMNodeEncapsulator root    = null; // root of the new tree
    private Hashtable          nodes   = null; // nodes


  /* --- Constructor --- */

    /**
     * Constructor.
     * Goes throught the tree and build
     * TMNodeEncapsulators for each user's node.
     *
     * @param model    the model of the user's tree
     */
    TMModelUpdaterConcrete(TMModelNode model) {
    	nodes = new Hashtable();
        root = new TMNodeEncapsulator(model, model.getRoot(), this);
        updater = new TMUpdaterConcrete(root);
        model.setUpdater(this);
    }


  /* --- Accessor --- */
  
    /**
     * Returns the updater.
     *
     * @return    the updater
     */
    TMUpdaterConcrete getUpdater() {
        return updater;
    }

    /**
     * Sets a new node.
     */
    void addNode(Object node, TMNodeEncapsulator eNode) {
        nodes.put(node, eNode);
    }
    

 /* --- Updates --- */
 
    /**
     * To be called when the user node has its size changed.
     *
     * @param node    the node that has its size changed
     */
    public void updateSize(Object node) {
        TMNodeEncapsulator eNode = (TMNodeEncapsulator) nodes.get(node);
        updater.updateSize(eNode);
    }
    
    /**
     * To be called when the user node has its state changed.
     *
     * @param node    the node that has its state changed
     */    
    public void updateState(Object node) {
        TMNodeEncapsulator eNode = (TMNodeEncapsulator) nodes.get(node);
        updater.updateState(eNode);        
    }

    /**
     * To be called when the user node has a new child.
     *
     * @param node     the node that has a new child
     * @param child    the node's new child
     */
    public void addChild(Object node, Object child) {
         TMNodeEncapsulator eNode = (TMNodeEncapsulator) nodes.get(node);
         TMNodeEncapsulator eChild = new TMNodeEncapsulator(model, child, this);
         eNode.addChild(eChild);
         updater.addChild(eNode, eChild);   
    }

    /**
     * To be called when the user node removes a child.
     *
     * @param node     the node that removes a child
     * @param child    the node's lost child
     */
    public void removeChild(Object node, Object child) {
         TMNodeEncapsulator eNode = (TMNodeEncapsulator) nodes.get(node);
         TMNodeEncapsulator eChild = (TMNodeEncapsulator) nodes.get(child);
         eNode.removeChild(eChild);
         updater.removeChild(eNode, eChild);   
    }

}

