/*
 * DemoModel.java
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
import java.io.IOException;

import javax.swing.JFrame;

import geovista.treemap.tm.TMView;
import geovista.treemap.tm.TreeMap;



/**
 * The DemoModel class implements a demo for Treemap.
 * It's the same than the Demo class, but it use TMModelNode
 * instead of TMNode to describe the user's tree.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class DemoModel {

    private static int             count   = 1;    // to have unique view name

    private static TMFileModelNode model   = null; // the model of the demo tree
    private static TreeMap         treeMap = null; // the geovista.matrix.treemap.tm builded
    private static String          name    = null; // name for this demo

    /**
     * Display a demo TreeMap.
     */
    public static void main(String[] args) {
        String pathRoot = null;

        if (args.length > 0) {
            pathRoot = args[0];
        } else {
            pathRoot = File.separator;
        }

        File rootFile = new File(pathRoot);
        try {
            System.out.println("Starting the geovista.matrix.treemap.tm from " + 
                                rootFile.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } 

        if (! rootFile.exists()) {
            System.out.println("Can't start geovista.matrix.treemap.tm : " + rootFile.getName() + 
                               " does not exist.");
            return;
        }


        model = new TMFileModelNode(rootFile);
        if (model == null) {
            System.err.println("Error : can't start geovista.matrix.treemap.tm from " + 
                                rootFile.getAbsolutePath());
            return;
        }

        treeMap = new TreeMap(model);
        name = rootFile.getAbsolutePath();

        TMFileModelSize fSize = new TMFileModelSize();
        TMFileModelDraw fDraw = new TMFileModelDraw();
        TMView view = treeMap.getView(fSize, fDraw);
        
        JFrame viewFrame = new JFrame(name + " : " + count);
        viewFrame.setContentPane(view);
        viewFrame.pack();
        viewFrame.setVisible(true);
    }
}

