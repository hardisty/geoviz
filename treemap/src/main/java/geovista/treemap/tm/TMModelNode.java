/*
 * TMModelNode.java
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

import java.util.Enumeration;


/**
 * The TMModelNode interface should be implemented by 
 * object that are model of nodes of the tree that want to be 
 * displayed in the TreeMap.
 * It's the equivalent of theTreeModel, but for the TreeMap.
 * <P>
 * If you have already a tree structure, just implements
 * this interface in the model of the tree.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public interface TMModelNode {

    /**
     * Returns the root of the tree.
     * Should not return <CODE>null</CODE>.
     *
     * @return    the root of the tree
     */
    public Object getRoot();

    /**
     * Returns the children of the given node
     * in an Enumeration.
     * If the given node does not have children,
     * it should return an empty Enumeration,
     * not <CODE>null</CODE>.
     *
     * @return    an Enumeration containing childs of the given node
     */
    public Enumeration children(Object node);

    /**
     * Checks if the given node is a leaf or not.
     * A node could have no children and still not
     * be a leaf.
     *
     * @return    <CODE>true</CODE> if the given node is a leaf;
     *            <CODE>false</CODE> otherwise
     */
    public boolean isLeaf(Object node);

    /**
     * Called by the TMModelUpdater constructor. 
     * Gives to this model of nodes a reference to a TMModelUpdater object.
     * This node should use this reference
     * to notify geovista.matrix.treemap.tm that something has changed.
     * See the differents update methods of the TMModelUpdater interface.
     * <P>
     * As this method is called by the constructor
     * of TMModelUpdater, don't call methods of TMModelUpdater
     * in this method.
     *
     * @param updater    TMModelUpdater to be called when something has changed
     */
    public void setUpdater(TMModelUpdater updater);

}

