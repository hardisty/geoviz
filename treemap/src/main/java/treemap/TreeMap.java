/*
 * TreeMap.java
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
 * The TreeMap class implements a tree map representation for data.
 * For information about treemap, see 
 * <A HREF="http://www.cs.umd.edu/hcil/treemaps/">Schneiderman 1992</A>
 * <P>
 * A TreeMap is build from hierarchical data, given as a tree
 * of TMNode. So, the first parameter to give to build a TreeMap
 * is the TMNode which is the root of the to-be-represented tree.
 * The tree to be displayed by the TreeMap should be build before the call
 * to TreeMap, that is the root node should return children
 * when the children() method is called.
 * <P>
 * You can get a TMView (herited from JView) containing the TreeMap by calling
 * getView().  It takes a TMSizeRenderer and a TMDrawRenderer as parameters.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TreeMap {

    private TMUpdaterConcrete updater = null; // the updater of TMNode's tree


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param root    the root of the tree to be represented;
     *                could not be <CODE>null</CODE>
     */
    public TreeMap(TMNode root) {
        if (root == null) {
            throw new TMExceptionNullParameter("Impossible to build a tree" + 
                                               " from a null root.");
        }
        updater = new TMUpdaterConcrete(root);
    }
    
    /**
     * Constructor.
     *
     * @param model    the model of the tree to be represented;
     *                 could not be <CODE>null</CODE>
     */
    public TreeMap(TMModelNode model) {
        if (model == null) {
            throw new TMExceptionNullParameter("Impossible to build a tree" + 
                                               " from a null model.");
        }
        updater = (new TMModelUpdaterConcrete(model)).getUpdater();
    }


  /* --- View --- */

    /**
     * Returns a view of the treemap with the
     * given renderers.
     *
     * @param cSize         the object computing the size;
     *                      could not be <CODE>null</CODE>
     * @param cDraw         the object computing the drawing;
     *                      could not be <CODE>null</CODE>
     * @return              the desired view of the treemap
     */
    public TMView getView(TMComputeSize cSize, TMComputeDraw cDraw) {
        if (cSize == null) {
            throw new TMExceptionNullParameter("Impossible to build a treemap"
                                          + "view with a null TMComputeSize.");
        } else if (cDraw == null) {
            throw new TMExceptionNullParameter("Impossible to build a treemap"
                                          + "view with a null TMComputeDraw.");
        }
        return new TMView(updater, cSize, cDraw);
    }

}

