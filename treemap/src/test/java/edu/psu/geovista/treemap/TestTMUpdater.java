/*
 * TestTMUpdater.java
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

package edu.psu.geovista.treemap;

import java.util.Enumeration;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import edu.psu.geovista.treemap.tm.TMNode;
import edu.psu.geovista.treemap.tm.TMUpdater;

/**
 * The TestTMUpdater is a JUnit test class
 * for testing the setting of TestTMUpdater in TMNode classes.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TestTMUpdater
    extends TestCase {

    private TestNode          root   = null;
    private TestNode          fold   = null;
    private TestNode          file   = null;
    private TMUpdater update = null;

        
  /* --- Constructor --- */

    /**
     * Constructor.
     * Take the name of the test in parameter.
     *
     * @param name    the name of the test
     */
    public TestTMUpdater(String name) {
        super(name);
    }


  /* --- Suite --- */

    /**
     * TestSuite.
     */
    public static Test suite() {
        return new TestSuite(TestTMUpdater.class);
    }


  /* --- Setup --- */

    /**
     * Setup the fixture.
     */
    protected void setUp() {
        root = new TestNode();
        fold = new TestNode();
        file = new TestNode(true);
        root.add(fold);
        fold.add(file);
    }

  
  /* --- Tests --- */

    /**
     * Tests the setting of the TMUpdater in every TMNodes.
     */
    public void testSettingOfUpdater() {
        update = new TMUpdater(root);
        assertSame(update, root.getUpdater());
        assertSame(update, fold.getUpdater());
        assertSame(update, file.getUpdater());
    }

  
  /* --- Inners nodes --- */

    /**
     * Implements TMNode
     */
    class TestNode
        implements TMNode {

        private Vector    children  = null;  // children
        private boolean   leaf      = false; // is a leaf
        private TMUpdater updater   = null;  // updater


      /* --- Constructor --- */

        /**
         * Constructor.
         */
        TestNode() {
            children = new Vector();
        }

        /**
         * Constructor.
         */
        TestNode(boolean leaf) {
            this();
            this.leaf = leaf;
        }


      /* --- Standard --- */

        /**
         * Add child.
         */
        void add(TestNode child) {
            children.add(child);
        }


      /* --- TMNode --- */

        /**
         * Is leaf ?
         */
        public boolean isLeaf() {
            return leaf;
        }

        /**
         * Returns children.
         */
        public Enumeration children() {
            return children.elements();
        }

        /**
         * Set updater.
         */
        public void setUpdater(TMUpdater updater) {
            this.updater = updater;
        }

        /**
         * Get updater.
         */
        public TMUpdater getUpdater() {
            return updater;
        }
    }

}

