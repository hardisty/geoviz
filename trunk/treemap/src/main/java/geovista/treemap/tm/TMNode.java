/*
 * TMNode.java
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
 * The TMNode interface should be implemented by 
 * object that are node of the tree that want to be 
 * displayed in the TreeMap.
 * <P>
 * If you have already a tree structure, just implements
 * this interface in node of the tree.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * 
 */
public interface TMNode {

    /**
     * Returns the children of this node
     * in an Enumeration.
     * If this object does not have children,
     * it should return an empty Enumeration,
     * not <CODE>null</CODE>.
     * All objects contained in the Enumeration
     * should implements TMNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children();

    /**
     * Checks if this node is a leaf or not.
     * A node could have no children and still not
     * be a leaf.
     *
     * @return    <CODE>true</CODE> if this node is a leaf;
     *            <CODE>false</CODE> otherwise
     */
    public boolean isLeaf();

    /**
     * Called by the TMUpdater constructor. 
     * Gives to this node a reference to a TMUpdater object.
     * This node should use this reference
     * to notify geovista.matrix.treemap.tm that something has changed.
     * See the differents update methods of the TMUpdater interface.
     * <P>
     * As this method is called by the constructor
     * of TMUpdater, don't call methods of TMUpdater
     * in this method.
     *
     * @param updater    the TMUpdater to be called when something has changed
     */
    public void setUpdater(TMUpdater updater);

}

