/*
 * RenderThread.java
 *
 * Created on 11. Februar 2002, 19:38
 *
 * Licensed under GNU General Public License (GPL).
 * See http://www.gnu.org/copyleft/gpl.html
 */

package edu.psu.geovista.app.parvis.gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 *
 * @author  flo
 * @version
 */
class RenderThread extends Thread {
	protected final static Logger logger = Logger.getLogger(RenderThread.class.getName());
    /** flags to control rendering */
    boolean quality = false;
    boolean progressive = false;
    boolean brushMode = false;

    /** flags to indicate thread state */
    boolean isWorking = false;
    boolean doWork = false;
    boolean wasInterrupted = false;
    boolean progressiveInterrupted = false;
    boolean secondPass = false;
    boolean qualitychanged = false;

    int startAxis, stopAxis;
    int progressiveStartAxis, progressiveStopAxis;
    int lastStart = 0;
    int lastStop = 0;

    int ids[] = null;
    BufferedImage renderedImage = null;

    Stroke stroke = new BasicStroke();
    Color color = Color.black;

    BasicParallelDisplayUI ui = null;
    ParallelDisplay comp = null;


    RenderThread(BasicParallelDisplayUI ui){
        this.ui = ui;
        this.setPriority(Thread.MIN_PRIORITY);
    }

    void setCurrentComponent(ParallelDisplay comp){
        this.comp = comp;
    }

    void setBrushMode(boolean brushMode){
        this.brushMode = brushMode;
    }

    void setQuality(boolean quality, boolean progressive){
        this.progressive = progressive;
        if (progressive)
            this.quality = false;
        else {
            if (this.quality != quality){
                qualitychanged = true;
                this.quality = quality;
            }
        }
    }

    synchronized void setRegion(int startAxis, int stopAxis){
        if (wasInterrupted  || ((isWorking || doWork) && !secondPass) || (isWorking && secondPass && doWork)){
            // old render area still invalid -> expand
            if (startAxis < this.startAxis)
                this.startAxis = startAxis;
            if (stopAxis > this.stopAxis)
                this.stopAxis = stopAxis;
            if (startAxis < this.progressiveStartAxis)
                this.progressiveStartAxis = startAxis;
            if (stopAxis > this.progressiveStopAxis)
                this.progressiveStopAxis = stopAxis;
        }
        else if (progressiveInterrupted || (isWorking || doWork)){
            this.startAxis = startAxis;
            this.stopAxis = stopAxis;
            if (startAxis < this.progressiveStartAxis)
                this.progressiveStartAxis = startAxis;
            if (stopAxis > this.progressiveStopAxis)
                this.progressiveStopAxis = stopAxis;
        }
        else {
            this.startAxis = startAxis;
            this.stopAxis = stopAxis;
            this.progressiveStartAxis = startAxis;
            this.progressiveStopAxis = stopAxis;
        }
        //this.ids = ids.clone();
        logger.finest("RenderThread: setting repaint axes: " + this.startAxis + ", " + this.stopAxis);
    }

    void setStyle(Stroke stroke, Color color){
        this.stroke = stroke;
        this.color = color;
    }

    synchronized boolean isWorking(){
        return isWorking;
    }

    public BufferedImage getRenderedImage(){
        return renderedImage;
    }

    public int getRenderedRegionStart(){
        return lastStart;
    }

    public int getRenderedRegionStop(){
        return lastStop;
    }

    public void run(){
        while(true){
            synchronized(this){
                isWorking = false;
                // wait for next rendering to be queued
                do {
                    try {
                        if (! doWork){
                            logger.finest("RenderThread: waiting...");
                            this.wait();
                        }
                        logger.finest("RenderThread: exit waiting.");
                    }
                    catch (InterruptedException iex){
                        logger.finest("RenderThread: interruptedExcpetion.");
                        //rendering has been cancelled
                    }
                }
                while (Thread.interrupted());

                isWorking = true;
                doWork = false;
                renderedImage = null;
                qualitychanged = false;
            }

            logger.finest("RenderThread: run loop start...");

            boolean progress = true;

            while (comp != null && progress){
                String modestr;

                if (progressive && quality){
                    secondPass = true;

                    logger.finest("RenderThread: starting progressive paint...");
                    modestr = "[quality]";

                    //2nd pass: lower priority, keep response time low
                    Thread.yield();
                }
                else {
                    secondPass = false;

                    logger.finest("RenderThread: starting paint...");
                    modestr = "[preview]";
                }

                // this is the main rendering routine
                comp.fireProgressEvent(new ProgressEvent(comp, ProgressEvent.PROGRESS_START, 0.0f, "rendering " + modestr));

                BufferedImage img = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D g2 = (Graphics2D)img.getGraphics();
                setupRendering(g2, quality, stroke, color);

                // render all records
                int i = 0;
                float brushVal = 0.0f;
                if (brushMode){
                    color = comp.getBrushedColor();//changed fah july 30 02
                }

                for (; i<comp.getNumRecords(); i++){
                    if (i % 300 == 0){
                        comp.fireProgressEvent(new ProgressEvent(comp, ProgressEvent.PROGRESS_UPDATE, ((float)i)/comp.getNumRecords(), "rendering " + modestr));
                    }
                    if (!brushMode || (brushVal = comp.getBrushValue(i)) > 0.0f){
                        // select records in brushmode, render all in normal mode
                        //skip soft edges
                        if (!quality && brushMode && brushVal < 0.8) continue;
                        if (brushMode && quality){
                        	
                            Color col = new Color(color.getRed(), color.getBlue(), color.getGreen(), (int)(255 * brushVal));
                            logger.finest("Brush value: " + brushVal + " alpha: " + col.getAlpha());
                            
                            //g2.setColor(col);
                          ui.drawRecord(g2, comp, i, progressiveStartAxis, progressiveStopAxis);

                        }
                        if (secondPass) {
                            ui.drawRecord(g2, comp, i, progressiveStartAxis, progressiveStopAxis);

                        }
                        else {
                            ui.drawRecord(g2, comp, i, startAxis, stopAxis);

                        }

                        if (qualitychanged || secondPass){
                            //2nd pass: lower priority, keep response time low
                            Thread.yield();
                            if (Thread.interrupted()){
                                progressiveInterrupted = true;
                                logger.finest("### breaking!");
                                break;
                            }
                        }
                    }

                }

                if (i==comp.getNumRecords()){
                    //finished all records
                    wasInterrupted = false;

                    renderedImage = img;
                    if (secondPass){
                        lastStart = progressiveStartAxis;
                        lastStop = progressiveStopAxis;
                        progressiveInterrupted = false;
                    }
                    else {
                        lastStart = startAxis;
                        lastStop = stopAxis;
                    }

                    comp.fireProgressEvent(new ProgressEvent(comp, ProgressEvent.PROGRESS_FINISH, 1.0f, "rendering " + modestr));
                    comp.repaint();

                    logger.finest("RenderThread: paint finished...");

                    if (progressive){
                        if (quality) {
                            quality = false;
                            progress = false;
                        }
                        else {
                            quality = true;
                        }
                    }
                    else {
                        progress = false;
                    }
                }
                else {
                    // we have been interrupted
                    logger.finest("RenderThread: paint interrupted...");
                    progress = false;
                    if ( secondPass ){
                        //2nd pass progressive -> throw away
                        wasInterrupted = false;
                        quality = false;
                    }
                }
                secondPass = false;

            }
        }
    }

    synchronized void render(){
        logger.finest(this.getName() + ".render() called");
        if (this.isWorking){
            this.interrupt();
        }
        this.doWork = true;
        this.notify();
    }

    public void reset(){
        //throw away image
        renderedImage = null;
    }

    public void setupRendering(Graphics2D g2, boolean quality, Stroke stroke, Color color){
        RenderingHints qualityHints = new RenderingHints(null);

        if (quality) {
            qualityHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f);
            g2.setComposite(ac);

            g2.setStroke(stroke);
            g2.setColor(color);
        }
        else {
            qualityHints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            g2.setStroke(new BasicStroke());
            //strip out alpha
            g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue()));
        }

        g2.setRenderingHints(qualityHints);


    }

}
