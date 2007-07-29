/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SonicRampSwatch
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SonicRampSwatch.java,v 1.3 2005/05/31 17:42:53 hardisty Exp $
 $Date: 2005/05/31 17:42:53 $
 Reference:                Document no:
 ___                                ___
 -------------------------------------------------------------------  *

 */
package edu.psu.geovista.sound;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.logging.Logger;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SonicRampSwatch extends JPanel implements MouseListener,
    ChangeListener {
    public transient static boolean OFF = false;
    public transient static boolean ON = true;
    private boolean anchored;
    private Color swatchColor;
    private transient SonicRampPicker parent;
    private transient boolean isEnd;
    private transient ImageIcon iconBlack;
    private transient ImageIcon iconWhite;
    private transient TexturePaint texPaint;
    private ChannelData cc; // current channel

    private Synthesizer synthesizer;
    private Instrument instruments[];
    //private ChannelData[] channels;
    private boolean noteState;
    JTable table;
    private transient int keyNum;
    protected final static Logger logger = Logger.getLogger(SonicRampSwatch.class.getName());
    public SonicRampSwatch(SonicRampPicker parent, boolean anchored, boolean end) {
        this.makeImage();
        this.parent = parent;
        this.swatchColor = Color.black;
        this.setBackground(swatchColor);
        this.addMouseListener(this);
        this.setAnchored(anchored);
        this.isEnd = end;
        this.initSound();
    }

    private void initSound() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    logger.finest("getSynthesizer() failed!");

                    return;
                }
            }

            synthesizer.open();

        } catch (Exception ex) {
            ex.printStackTrace();

            return;
        }

        //Soundbank sb = synthesizer.getDefaultSoundbank();
        Soundbank sb = synthesizer.getDefaultSoundbank();

        if (sb != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }

        MidiChannel[] midiChannels = synthesizer.getChannels();


        cc = new ChannelData(midiChannels[0],0);

        this.setInstrument(27);
    }

    public void makeImage() {
        Class cl = this.getClass();
        URL urlGif = cl.getResource("resources/SonicAnchor16.gif");
        ImageIcon icon = new ImageIcon(urlGif, "Anchors the color in a ramp");
        this.iconBlack = icon;

        URL urlGif2 = cl.getResource("resources/SonicAnchor16.gif");
        ImageIcon icon2 = new ImageIcon(urlGif2, "Anchors the color in a ramp");
        this.iconWhite = icon2;
    }

    public void setTexPaint(TexturePaint texPaint) {
        this.texPaint = texPaint;
    }

    public void setSwatchColor(Color newColor) {
        this.swatchColor = newColor;
        this.setBackground(newColor);
    }

    public Color getSwatchColor() {
        return this.swatchColor;
    }

    public void setAnchored(boolean anchor) {
        this.anchored = anchor;

        if (anchor || this.isEnd) {
            this.setBorder(BorderFactory.createLoweredBevelBorder());
        } else {
            this.setBorder(BorderFactory.createRaisedBevelBorder());
        }
    }

    public boolean getAnchored() {
        return this.anchored;
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        this.setNoteState(SonicRampSwatch.ON);
        this.on();

    }

    public void mouseExited(MouseEvent e) {
      this.setNoteState(SonicRampSwatch.OFF);
      this.off();

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 1) { //double or more clicks
        } else if (e.getClickCount() == 1) { //toggle anchor state on single click

            if (this.isEnd) {
                return; // if we are an end, we should always remain anchored!
            }

            if (this.anchored) {
                this.setAnchored(false);
                this.parent.swatchChanged(); //if we are now "unanchored", need to update
            } else {
                this.setAnchored(true); // This won't affect other swatches
            }
        }
         //end if doubleclick
    }

    public void stateChanged(ChangeEvent e) {
        //if (e.getSource().getClass() == javax.swing.JPanel.class){
        JSlider slid = (JSlider) e.getSource();
        int val = slid.getValue();


        this.setKeyNum(val);
        //}
    }

    public void setKeyNum(int keyNum) {
        logger.finest("setting key num, num = " + keyNum);
        this.keyNum = keyNum;

    }

    public void setInstrument(int insturment) {
        logger.finest("setting insturment,= " + insturment);
        //synthesizer.unloadAllInstruments(synthesizer.getDefaultSoundbank());
        //synthesizer.loadInstrument(instruments[insturment]);
        boolean currState = this.noteState;
        if (this.noteState == SonicRampSwatch.ON){
          this.off();
        }
        cc.channel.programChange(insturment);
        if (currState == SonicRampSwatch.ON){
          this.on();
        }

        //}
    }

    public void paintComponent(Graphics g) {
        g.setColor(this.getBackground());

        //adding support for textures
        if (this.texPaint != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(texPaint);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        } else {
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        if (this.getAnchored()) {
            int midX = this.getWidth() / 2;
            int midY = this.getHeight() / 2;

            Color c = this.getBackground();
            int colorValue = c.getRed() + c.getBlue() + c.getGreen();
            Image ico = null;

            if (colorValue > 200) { //pulled this out of my hat
                ico = this.iconBlack.getImage();
            } else {
                ico = this.iconWhite.getImage();
            }

            midX = midX - (ico.getWidth(this) / 2);
            midY = midY - (ico.getHeight(this) / 2);
            g.drawImage(ico, midX, midY, this);
        }
    }

    public boolean isNoteOn() {
        return noteState == ON;
    }

    public void on() {
        setNoteState(ON);
        cc.channel.noteOn(keyNum, cc.velocity);
        this.makeTexPaint();
        this.repaint();
    }

    public void off() {
        setNoteState(OFF);
        cc.channel.noteOff(keyNum, cc.velocity);
        this.texPaint = null;
        this.repaint();
    }

    public void open() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    logger.finest("getSynthesizer() failed!");

                    return;
                }
            }

            synthesizer.open();

        } catch (Exception ex) {
            ex.printStackTrace();

            return;
        }

        Soundbank sb = synthesizer.getDefaultSoundbank();

        if (sb != null) {
            instruments = synthesizer.getDefaultSoundbank().getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }

        ListSelectionModel lsm = table.getSelectionModel();
        lsm.setSelectionInterval(0, 0);
        lsm = table.getColumnModel().getSelectionModel();
        lsm.setSelectionInterval(0, 0);
    }

    public void setNoteState(boolean state) {
        noteState = state;
    }

    /**
     * Main method for testing.
     */
    public static void main(String[] args) {
        JFrame app = new JFrame();

        //app.getContentPane().setLayout(new BorderLayout());
        app.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        SonicRampPicker pick = new SonicRampPicker();
        SonicRampSwatch swat = new SonicRampSwatch(pick, true, false);
        app.getContentPane().setLayout(new BoxLayout(app.getContentPane(),
                BoxLayout.X_AXIS));
        app.getContentPane().add(swat);

        JSlider slider = new JSlider();
        slider.setValue(0);
        slider.addChangeListener(swat);
        app.getContentPane().add(slider);

        //app.getContentPane().add(setColorsPan);
        app.pack();
        app.setVisible(true);
    }
    private void makeTexPaint(){

       int texSize = 4;
       Rectangle2D.Float rect = new Rectangle2D.Float(0,0,texSize,texSize);
       BufferedImage buff = new BufferedImage(texSize,texSize,BufferedImage.TYPE_INT_ARGB);
       Graphics2D g2 = buff.createGraphics();
       Color trans = new Color(255,255,255); //white
       g2.setColor(trans);
       g2.fillRect(0,0,texSize,texSize);
       g2.setColor(Color.blue);
       g2.drawLine(0,0,32,32);

       texPaint = new TexturePaint(buff,rect);

    }
    /**
     * Stores MidiChannel information.
     */
    class ChannelData {
        MidiChannel channel;
        boolean solo;
        boolean mono;
        boolean mute;
        boolean sustain;
        int velocity;
        int pressure;
        int bend;
        int reverb;
        int row;
        int col;
        int num;

        public ChannelData(MidiChannel channel, int num) {
            this.channel = channel;
            this.num = num;
            velocity = pressure = bend = reverb = 64;
        }
    }
     // End class ChannelData
}
