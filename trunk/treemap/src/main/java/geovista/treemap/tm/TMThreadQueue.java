/*
 * TMThreadQueue.java
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

import java.util.Vector;


/**
 * The TMThreadQueue implements a queue of TMThreadModel to be executed
 * in a separates thread. With it, we can assure that only one 
 * update or buffer drawing is ocuring at a time.
 * This class is inspirated of the QueueExecutor of Doug Lea's 
 * util.concurrent package.
 *
 * @author Christophe Bouthier [bouthier@loria.fr]
 * @version 2.5
 */
class TMThreadQueue {

    private Queue        queue         = null; // the storage queue
    private Thread       loopingThread = null; // the running thread
    private Loop         loop          = null; // the loop itself
    private TMThreadLock lock          = null; // a simple lock

    /**
     * Constructor.
     */
    TMThreadQueue() {
        lock = new TMThreadLock();
        queue = new Queue();
        loop = new Loop();
    }

    /**
     * Adds a TMThreadModel to the thread queue.
     *
     * @param thread    the thread to add
     */
    void add(TMThreadModel thread) {
        queue.put(thread);
        restart();
    }

    /**
     * Starts or restarts the looping thread
     */
    void restart() {
        lock.lock();
        if (loopingThread == null) {
            lock.unlock();
            loopingThread = new Thread(loop);
            loopingThread.start();
        } else {
            lock.unlock();
        }
    }


  /* --- Inner running class --- */

    /**
     * The Loop class implements a loop on the queue.
     */
    class Loop
        implements Runnable {

        private boolean doIt = true; // stop the thread

        /**
         * Loops on every thread in the event queue.
         */
        public void run() {
            doIt = true;
            while(doIt) {
                lock.lock();
                TMThreadModel task = (TMThreadModel) queue.get();
                if (task != null) {
                    lock.unlock();
                    task.run();
                    task = null;
                } else {
                    doIt = false;
                }
            }
            loopingThread = null;     
            lock.unlock();
        }
    }

}

/**
 * The Queue class implements a simple synchronized FIFO
 * based on the Vector class.
 */
class Queue {

    private Vector storage = new Vector(); // the storage

    /**
     * Puts the object at the end of the queue.
     *
     * @param o    the object to put
     */
    synchronized void put(Object o) {
        storage.add(o);
    }

    /**
     * Gets the first object of the queue.
     * Returns <CODE>null</CODE> if the queue is empty.
     *
     * @return    the first object of the queue;
     *            <CODE>null</CODE> if the queue is empty
     */
    synchronized Object get() {
        Object result = null;

        try {
            result = storage.remove(0);
        } catch (IndexOutOfBoundsException e) {
            result = null;
        }
        return result;
    } 

}

