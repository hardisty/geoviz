/*
 * TMComputeSize.java
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
 * The TMComputeSize interface should be implemented 
 * by every class that implements algorithms for computing
 * size of TMNode.
 * <P>
 * As computing the size of a TMNode is dependant of the kind of 
 * TMNode, a TMComputeSize should test the kind of TMNode given,
 * and throw an TMExceptionBadTMNodeKind if there is incompatibility.
 * The isCompatibleWith method should test the kind of TMNode
 * passed in parameter and return <CODE>true</CODE> if this TMComputeSize
 * is compatible with it.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public interface TMComputeSize {

    /**
     * Test if this TMComputeSize could be used
     * with the kind of TMNode passed in parameter.
     *
     * @param node    the TMNode to test the compatibility with
     * @return        <CODE>true</CODE> if this kind of node is compatible;
     *                <CODE>false</CODE> otherwise
     */
    public boolean isCompatibleWith(TMNode node);

    /**
     * Returns the size of the TMNode.
     *
     * @param node                      we will compute the size of this TMNode
     * @return                          the computed size of the TMNode
     * @throws TMExceptionBadTMNodeKind If this kind of TMNode is incompatible 
     *                                   with this TMComputeSize.
     */
    public float getSize(TMNode node)
        throws TMExceptionBadTMNodeKind;
  
}

