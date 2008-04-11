/*
 * TMNodeAdapter.java
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
 * The TMNodeAdapter abstract class encapsulate a TMNode for
 * a TMComputeDraw.
 * To compute filling and tooltip, a TMComputeDraw
 * could need more information that what is stocked in
 * the TMNode.
 * For example, a filling could be size-dependant,
 * and a tooltip could be filling-dependant and size-dependant.
 * The TMNodeAdapter can give the size, and can transmit information 
 * between filling and tooltip with getUserData() and setUserData().
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * 
 */
public abstract class TMNodeAdapter {

    private   Object        userData    = null; // user data
    protected TMCushionData cushionData = null;

    /**
     * Returns the TMNode encapsulated.
     *
     * @return    the TMNode encapsulated
     */
    public abstract TMNode getNode();
 
    /**
     * Returns the size of the TMNode encapsulated.
     *
     * @return    the size of the TMNode encapsulated
     */
    public abstract float getSize();

    /**
     * Returns the user data.
     *
     * @return    the user data 
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * Sets the user data.
     *
     * @param o    the user data
     */
    public void setUserData(Object o) {
        userData = o;
    }


  /* --- Cushion Data --- */

    public TMCushionData getCushionData() {
        return cushionData;
    }

}

