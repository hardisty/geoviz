/*
 * TMCushionData.java
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
 * The TMCushionData implements the data computed at each node
 * for the cushion visualization
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMCushionData {

    double x2 = 0.0;
    double x  = 0.0;
    double y2 = 0.0;
    double y  = 0.0;
    double h  = 0.0;

    TMCushionData() {};

    TMCushionData(TMCushionData clone) {
        if (clone != null) {
            x2 = clone.x2;
            x = clone.x;
            y2 = clone.y2;
            y = clone.y;
            h = clone.h;
        }
    }

    public String toString() {
        String s = "x2 = " + x2 + ", x = " + x + 
                 "\n    y2 = " + y2 + ", y = " + y + "    h = " + h;
        return s;
    }
}

