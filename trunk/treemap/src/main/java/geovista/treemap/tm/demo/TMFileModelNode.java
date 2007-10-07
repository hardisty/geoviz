/*
 * TMFileModelNode.java
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

package geovista.treemap.tm.demo;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import geovista.treemap.tm.TMModelNode;
import geovista.treemap.tm.TMModelUpdater;



/**
 * The TMFileModelNode implements an example of TMModelNode encapsulating Files.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TMFileModelNode
    implements TMModelNode {

    private File           root    = null; // root of the tree
    
    
  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root    the File root of this tree
     */
    public TMFileModelNode(File root) {
        this.root = root;
    }


  /* --- TMModelNode --- */
  
    /**
     * Returns the root of the tree.
     * Should not return <CODE>null</CODE>.
     *
     * @return    the root of the tree
     */
    public Object getRoot() {
        return root;
    }

    /**
     * Returns the children of this node in an Enumeration.
     * If this node is a file, return a empty Enumeration.
     * Else, return an Enumeration full with TMFileNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children(Object node) {
        Vector children = new Vector();
        if (node instanceof File) {
            File file = (File) node;
            if (file.isDirectory()) {
                String[] tabFichiers = file.list();
                for (int i = 0; i < tabFichiers.length; i++) {
                    File fichier = new File(file.getPath() + 
                                       File.separator + tabFichiers[i]);
                    children.add(fichier);
                }
            }
        }
        return children.elements();
    }

    /**
     * Returns true if this node is not a directory.
     *
     * @return    <CODE>false</CODE> if this node is a directory;
     *            <CODE>true</CODE> otherwise
     */
    public boolean isLeaf(Object node) {
        if (node instanceof File) {
            File file = (File) node;
            return (! file.isDirectory());
        }
        return false;
    }

    /**
     * Sets the updater for this node.
     *
     * @param updater    the updater for this node
     */
    public void setUpdater(TMModelUpdater updater) {
    }

}

