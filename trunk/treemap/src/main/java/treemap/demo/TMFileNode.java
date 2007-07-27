/*
 * TMFileNode.java
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

package treemap.demo;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import treemap.TMNode;
import treemap.TMUpdater;


/**
 * The TMFileNode implements an example of TMNode encapsulating a File.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
public class TMFileNode
    implements TMNode {

    protected final static int  delay    = 1000; // delay between update (milliseconds)

    protected File       file     = null; // the File encapsulated
    protected TMFileNode parent   = null; // the parent
    protected Hashtable  children = null; // the children of this node
    protected TMUpdater  updater  = null; // the updater for this node

    protected long       size     = -1L;  // the size (to detect changes)
    protected long       date     = -1L;  // the date (to detect changes)
    protected String     name     = null; // the name (to detect changes)


  /* --- Constructor --- */

    /**
     * Constructor.
     *
     * @param file    the File encapsulated in this node
     */
    public TMFileNode(File file) {
        this.file = file;
        children = new Hashtable();
        size = getSize();
        date = getDate();
        name = getName();
        
                 
        JFrame frame = new JFrame("Demo Init");
        JPanel pane = new JPanel(new BorderLayout()); 
        frame.setContentPane(pane);

        JLabel infoLabel = new JLabel("Initializing demo tree...");
        infoLabel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        pane.add(infoLabel, BorderLayout.NORTH);

        JPanel paneStatus = new JPanel(new FlowLayout());
        JLabel fixedLabel = new JLabel("Reading files : ");
        ProgressStatus progressStatus = new ProgressStatus();
        JLabel statusLabel = progressStatus.getLabel();
        paneStatus.add(fixedLabel);
        paneStatus.add(statusLabel);
        pane.add(paneStatus, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);


        progressStatus.increment();        
        buildTree(progressStatus);


        pane.remove(paneStatus);
        CheckingThread cheackingThread = new CheckingThread();
        cheackingThread.start();


        frame.dispose();
    }

    /**
     * Constructor.
     *
     * @param file      the File encapsulated in this node
     * @param parent    the parent of this node
     * @param status    the progress status to update
     */
    protected TMFileNode(File file, TMFileNode parent, ProgressStatus status) {
        this.file = file;
        this.parent = parent;
        children = new Hashtable();
        size = getSize();
        date = getDate();
        name = getName();
        
        if (status != null) {
            status.increment();
        }
        buildTree(status);
    }

    /**
     * Builds the tree hierarchie of a TMFileNode.
     * A status view shows the progression of the activity.
     *
     * @param node      the TMFileNode root of the tree
     * @param status    the progress status to update
     */
    protected void buildTree(ProgressStatus status) {
        if (! isLeaf()) {
            String[] tabFichiers = file.list();
            for (int i = 0; i < tabFichiers.length; i++) {
                File fichier = new File(file.getPath() + 
                                        File.separator + tabFichiers[i]);
                TMFileNode child = new TMFileNode(fichier, this, status);
                addChild(child);
            }
        }
    }


  /* --- Tree management --- */

    /**
     * Add child to the node.
     * 
     * @param child    the TMFileNode to add as a child
     */
    protected void addChild(TMFileNode child) {
        children.put(child.getName(), child);
    }

    /**
     * Removes a child from the node.
     *
     * @param child    the TMFileChild to remove.
     */
    protected void removeChild(TMFileNode child) {
        children.remove(child.getName());
    }


  /* --- Accessor --- */

    /**
     * Returns the full name of the file.
     *
     * @return    the full name of the file
     */
    public String getFullName() {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return getName();
        }
    }

    /**
     * Returns the name of the file.
     *
     * @return    the name of the file
     */
    public String getName() {
        return file.getName();
    }

    /**
     * Returns the size of the node.
     * If the node is a file, returns the size of the file.
     * If the node is a folder, returns 0.
     *
     * @return    the size of the node
     */
    public long getSize() {
        return file.length();
    }

    /**
     * Returns the last modification date.
     *
     * @return    the last modification date
     */
    public long getDate() {
        return file.lastModified();
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
     * Else, return an Enumeration full with TMFileNode.
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
        return (! file.isDirectory());
    }

    /**
     * Sets the updater for this node.
     *
     * @param updater    the updater for this node
     */
    public void setUpdater(TMUpdater updater) {
        this.updater = updater;
    }


  /* --- Updates --- */

    /**
     * Checks if something has changed.
     *
     * @return    <CODE>true</CODE> if something has changed;
     *            <CODE>false</CODE> otherwise
     */
    protected boolean hasChanged() {
        if (updater != null) {
            if (! file.exists()) {
                if (parent != null) {
                    parent.removeChild(this);
                }
                Runnable doRemoveChild = new Runnable() {
                         public void run() {
                              updater.removeChild(parent, TMFileNode.this);
                         }
                };
                SwingUtilities.invokeLater(doRemoveChild);
                return true;
            }

            if (size != getSize()) {
                size = getSize();
                Runnable doUpdateSize = new Runnable() {
                         public void run() {
                              updater.updateSize(TMFileNode.this);
                         }
                };
                SwingUtilities.invokeLater(doUpdateSize);
                return true;
            }
            if (date != getDate()) {
                date = getDate();
                Runnable doUpdateState = new Runnable() {
                         public void run() {
                             updater.updateState(TMFileNode.this);
                         }
                };
                SwingUtilities.invokeLater(doUpdateState);
                return true;
            }
            if ((name == null) || (! name.equals(getName()))) {
                name = getName();
                Runnable doUpdateState = new Runnable() {
                         public void run() {
                              updater.updateState(TMFileNode.this);
                         }
                };
                SwingUtilities.invokeLater(doUpdateState);
                return true;
            }

            if (! isLeaf()) {
                int childs = file.list().length;
                if (childs > children.size()) {
                    String[] childList = file.list();
                    for (int i = 0; i < childList.length; i++) {
                        if (! children.containsKey(childList[i])) {
                            File f = new File(file.getPath() +
                                              File.separator +
                                              childList[i]);
                            TMFileNode child = new TMFileNode(f, this, null);
                            addChild(child);

                            class DoAddChild implements Runnable {

                                TMFileNode child = null; // the new child

                                DoAddChild(TMFileNode child) {
                                    this.child = child;
                                }

                                public void run() {
                                    updater.addChild(TMFileNode.this, child);
                                } 
                            }

                            Runnable doAddChild = new DoAddChild(child);
                            SwingUtilities.invokeLater(doAddChild);
                            return true;
                        }
                    }
                }

                for (Enumeration e = children(); e.hasMoreElements(); ) {
                    TMFileNode child = (TMFileNode) e.nextElement();
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

