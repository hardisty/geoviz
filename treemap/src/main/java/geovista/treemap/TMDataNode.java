/*
 * TMDataNode.java
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

package geovista.treemap;

import java.awt.Color;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import geovista.treemap.tm.TMNode;
import geovista.treemap.tm.TMUpdater;



/**
 * The TMDataNode implements an example of TMNode encapsulating a File.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TMDataNode
    implements TMNode {

    protected static final int  delay    = 1000; // delay between update (milliseconds)

    protected double       value     = Double.NaN; // the File encapsulated
    protected TMDataNode parent   = null; // the parent
    protected Hashtable  children = null; // the children of this node
    protected TMUpdater  updater  = null; // the updater for this node

    protected long       size     = -1L;  // the size (to detect changes)
    protected long       date     = -1L;  // the date (to detect changes)
    protected String     name     = null; // the name (to detect changes)
    private boolean isLeaf = true ; //default
    private boolean isIndicated = false;
    protected Color color= Color.white;//default background color to paint this node
     CheckingThread checkingThread;
     protected boolean updateFlag = false;//fah using this instead of threads for leaves
     protected Color indicationColor = Color.red;



  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param file    the File encapsulated in this node
     */
    public TMDataNode(double value, String name) {
        this.value = value;
        children = new Hashtable();


        this.name = name;

         //fah moving this to set child method
         //so only non-leaf nodes have threads
         //this attempts to get around too many threads problem
        //checkingThread = new CheckingThread();
        //checkingThread.start();

    }



    /**
     * Constructor.
     *
     * @param file      the File encapsulated in this node
     * @param parent    the parent of this node
     * @param status    the progress status to update
     */
    protected TMDataNode(double value, TMDataNode parent, ProgressStatus status) {
        this.value = value;
        this.parent = parent;
        children = new Hashtable();


        name = getName();

        if (status != null) {
            status.increment();
        }

    }




  /* --- Tree management --- */

    /**
     * Add child to the node.
     *
     * @param child    the TMDataNode to add as a child
     */
    public void addChild(TMDataNode child) {
        children.put(child.getName(), child);
        this.isLeaf = false;//fah xxx is this safe?
        if(this.checkingThread == null){
            checkingThread = new CheckingThread();
        checkingThread.start();
        }
    }

    /**
     * Removes a child from the node.
     *
     * @param child    the TMFileChild to remove.
     */
    protected void removeChild(TMDataNode child) {
        children.remove(child.getName());
    }


  /* --- Accessor --- */



    /**
     * Returns the name of the file.
     *
     * @return    the name of the file
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the size of the node.
     * If the node is a file, returns the size of the file.
     * If the node is a folder, returns 0.
     *
     * @return    the size of the node
     */
    public double getValue() {
        return this.value;
    }

  public Color getColor() {
    if (this.isIndicated){
      return this.indicationColor;
    }
    return color;
  }

  /**
     * Returns the node in a String form : return the name.
     *
     * @return    the name of the file
     */
    public String toString() {
        return getName();
    }


  /* --- TMNode --- */

    /**
     * Returns the children of this node in an Enumeration.
     * If this node is a file, return a empty Enumeration.
     * Else, return an Enumeration full with TMDataNode.
     *
     * @return    an Enumeration containing childs of this node
     */
    public Enumeration children() {
        return children.elements();
    }

    /**
     * Returns true if this node is not a directory.
     *
     * @return    <CODE>false</CODE> if this node is a directory;
     *            <CODE>true</CODE> otherwise
     */
    public boolean isLeaf() {
      //return children.elements().hasMoreElements();
        return (this.isLeaf);
    }

    /**
     * Sets the updater for this node.
     *
     * @param updater    the updater for this node
     */
    public void setUpdater(TMUpdater updater) {
        this.updater = updater;
    }

  public void setIsLeaf(boolean isLeaf) {
    this.isLeaf = isLeaf;
  }

  public void setColor(Color color) {
    this.color = color;
    this.updateFlag = true;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setValue(double value) {
    this.value = value;
    this.updateFlag = true;
  }
  public void setIsIndicated(boolean isIndicated){
    this.isIndicated = isIndicated;
    this.updateFlag = true;

  }

  /* --- Updates --- */

    /**
     * Checks if something has changed.
     *
     * @return    <CODE>true</CODE> if something has changed;
     *            <CODE>false</CODE> otherwise
     */
    protected boolean hasChanged() {
      if (this.isLeaf && this.updateFlag){
        this.updateFlag = false;
        return true;
      }
      if (updater != null) {
          if (Double.isNaN(this.value)) {
              if (parent != null) {
                  parent.removeChild(this);
              }
              Runnable doRemoveChild = new Runnable() {
                       public void run() {
                            updater.removeChild(parent, TMDataNode.this);
                       }
              };
              SwingUtilities.invokeLater(doRemoveChild);
              return true;
          }

          if (value != getValue()) {
              value = getValue();
              Runnable doUpdateSize = new Runnable() {
                       public void run() {
                            updater.updateSize(TMDataNode.this);
                       }
              };
              SwingUtilities.invokeLater(doUpdateSize);
              return true;
          }
          if (color != getColor()) {
              color = getColor();
              Runnable doUpdateState = new Runnable() {
                       public void run() {
                           updater.updateState(TMDataNode.this);
                       }
              };
              SwingUtilities.invokeLater(doUpdateState);
              return true;
          }
          if ((name == null) || (! name.equals(getName()))) {
              name = getName();
              Runnable doUpdateState = new Runnable() {
                       public void run() {
                            updater.updateState(TMDataNode.this);
                       }
              };
              SwingUtilities.invokeLater(doUpdateState);
              return true;
          }

          if (! isLeaf()) {


              for (Enumeration e = children(); e.hasMoreElements(); ) {
                  TMDataNode child = (TMDataNode) e.nextElement();
                  if (child.hasChanged()) {
                      return true;
                  }
              }
          }
        }

        return false;
    }


  /* --- Inners --- */

   /**
    * The inner class ProgressStatus implements
    * a simple way to update a JLabel to reflect
    * the progress of an activity.
    */
   class ProgressStatus {

       private JLabel label    = null; // the view : a JLabel
       private int    progress = 0; // the model : the progress


       /**
        * Constructor.
        */
       ProgressStatus() {
           label = new JLabel(Integer.toString(progress));
       }

       /**
        * Returns the label.
        *
        * @return    the label
        */
       JLabel getLabel() {
           return label;
       }

       /**
        * Increments the progress.
        */
       void increment() {
           progress++;
           label.setText(Integer.toString(progress));
           label.repaint();
       }
   }


    /**
     * The inner class CheackingThread implements
     * a Thread that checks if files have changed.
     */
    class CheckingThread
        extends Thread {

        /**
         * Checks if files have changed.
         */
        public void run() {
            try {
                while(true) {
                    hasChanged();
                    sleep(delay);
                }
            } catch (InterruptedException e) {
            }
        }
    }

}

