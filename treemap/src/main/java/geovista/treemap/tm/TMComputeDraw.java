/*
 * TMComputeDraw.java
 * www.bouthier.net
 *
 * The MIT License :
 * -----------------
 * Copyright (c) 2001 Christophe Bouthier, Vesselin Markovsky
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

import java.awt.Paint;


/**
 * The TMDrawManager interface should be implemented
 * by every class that implements algorithms for drawing
 * TMNode.
 * <P>
 * The methods to implements are :
 * <UL>
 *   <LI> getFilling(), to compute and return the filling of a node
 *   <LI> getTooltip(), to compute and return the tooltip of a node
 *   <LI> getTitle(), to compute and return the title (name) of a node
 *   <LI> getTitleColor(), to compute and return the color of the title
 * </UL>
 * <P>
 * As the filling and tooltip of a node could depends of its size,
 * methods get a TMNodeAdapter reference and not directly a TMNode.
 * To get the TMNode, call nodeAdapter.getNode().
 * To get the size of the TMNode, call nodeAdapter.getSize().
 * To pass information from filling to tooltip, use setUserData() and
 * getUserData() methods.
 * <P>
 * As computing the drawing of a node is dependant of the kind of
 * TMNode, a TMComputeDraw should test the kind of TMNode returned by
 * nodeAdapter.getNode(), and throw an TMExceptionBadTMNodeKind
 * if there is incompatibility.
 * The isCompatibleWith method should test the kind of TMNode
 * passed in parameter and return <CODE>true</CODE> if this TMComputeDraw
 * is compatible with it.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @author Vesselin Markovsky [markovsky@semantec.bg]
 * 
 */
public interface TMComputeDraw {

    /**
     * Test if this TMComputeDraw could be used
     * with the kind of TMNode passed in parameter.
     *
     * @param node    the TMNode to test the compatibility with
     * @return        <CODE>true</CODE> if this kind of node is compatible;
     *                <CODE>false</CODE> otherwise
     */
    public boolean isCompatibleWith(TMNode node);

    /**
     * Returns the filling of the node.
     *
     * @param nodeAdapter               the node which we will draw
     * @return                          the filling of the node
     * @throws TMExceptionBadTMNodeKind If the kind of TMNode returned is 
     *                                  incompatible with this TMComputeDraw.
     */
    public Paint getFilling(TMNodeAdapter nodeAdapter)
        throws TMExceptionBadTMNodeKind;

    /**
     * Returns the tooltip of the node.
     *
     * @param nodeAdapter               the node for which we want the tooltip
     * @return                          the tooltip of the node
     * @throws TMExceptionBadTMNodeKind If the kind of TMNode returned is 
     *                                  incompatible with this TMComputeDraw.
     */
    public String getTooltip(TMNodeAdapter nodeAdapter)
        throws TMExceptionBadTMNodeKind;

    /**
     * Returns the title of the node.
     *
     * @param nodeAdapter               the node for which we want the title
     * @return                          the title of the node
     * @throws TMExceptionBadTMNodeKind if the kind of TMNode returned is 
     *                                  incompatible with this TMComputeDraw.
     */
    public String getTitle(TMNodeAdapter nodeAdapter)
        throws TMExceptionBadTMNodeKind;


    /**
     * Returns the color of the title of the node.
     *
     * @param nodeAdapter               the node for which we want the title
     * @return                          the title of the node
     * @throws TMExceptionBadTMNodeKind if the kind of TMNode returned is 
     *                                  incompatible with this TMComputeDraw.
     */
    public Paint getTitleColor(TMNodeAdapter nodeAdapter)
        throws TMExceptionBadTMNodeKind;

}

