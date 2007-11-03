/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SonicRampPicker
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SonicRampPicker.java,v 1.2 2003/07/21 01:35:28 hardisty Exp $
 $Date: 2003/07/21 01:35:28 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */


package geovista.sound;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.logging.Logger;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;


public class SonicRampPicker extends JPanel implements ComponentListener, ActionListener {

    public SonicRampSwatch[] panSet;
    private boolean[] anchored;
    private Color[] colors;
    private SonicRamp ramp;
    private int previousPlay = -1;
    private int nSwatches;

    public static final String COMMAND_SWATCH_COLOR_CHANGED = "color_changed";
    public static final String COMMAND_SWATCH_TEXTURE_CHANGED = "texture_changed";

    public static final int DEFAULT_NUM_SWATCHES = 8;

    public static final Color DEFAULT_LOW_COLOR = new Color (255,255,255);//light grey
    public static final Color DEFAULT_HIGH_COLOR_DARK = new Color(0,0,0);
    public static final int DEFAULT_LOW_KEY = 30;//light grey
    public static final int DEFAULT_HIGH_KEY = 90;
    //public static final Color DEFAULT_HIGH_COLOR_GREEN = new Color(0,150,0);

    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    private transient  int currOrientation = 0;
    private JComboBox insturmentCombo;
    private Synthesizer synthesizer;
    protected final static Logger logger = Logger.getLogger(SonicRampPicker.class.getName());
private Instrument instruments[];


    public SonicRampPicker() {
      init();
    }

    private void init() {
        this.insturmentCombo = new JComboBox();
        this.insturmentCombo.addActionListener(this);
        this.insturmentCombo.setPreferredSize(new Dimension(80,20));
        //this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        ramp = new SonicRamp();

        nSwatches = SonicRampPicker.DEFAULT_NUM_SWATCHES; //default
        this.colors = new Color[nSwatches];
        this.colors[0] = SonicRampPicker.DEFAULT_LOW_COLOR;
        this.colors[nSwatches-1] = SonicRampPicker.DEFAULT_HIGH_COLOR_DARK;
        anchored = new boolean[nSwatches];
        this.makeRamp(nSwatches);
        this.rampSwatches();
        this.ramp.rampColors(colors,anchored);

        this.setPreferredSize(new Dimension(365,20));//these match 0.5 of the ClassifierPicker
        //this.setMinimumSize(new Dimension(200,20));
        //this.setMaximumSize(new Dimension(1000,60));
        this.addComponentListener(this);
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
        Class clazz = this.getClass();
        Soundbank sb = null;
    	try {
    		sb = MidiSystem.getSoundbank(clazz.getResourceAsStream("resources/soundbank.gm"));
    	} catch (InvalidMidiDataException e) {
    	    logger.throwing(clazz.getName(), "initSound", e);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	} 
        
        if (sb != null) {
            instruments = sb.getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
        for (int i = 0; i < instruments.length; i++){
          this.insturmentCombo.addItem(instruments[i].getName());
        }



        //this.setInstrument(27);
    }

    public void actionPerformed(ActionEvent e){
      if(e.getSource() == this.insturmentCombo){
        for (int i = 0; i < this.panSet.length; i++){
          this.panSet[i].setInstrument(this.insturmentCombo.getSelectedIndex());
        }
      }
    }
    public void makeRamp(int nSwatches) {
        //first find out if we already have some colors etc.
        //if so, track the num.
        int len = 0;
        if (colors != null) {
          len = colors.length;
          this.removeAll();
        }

        panSet = new SonicRampSwatch[nSwatches];
        for (int i = 0; i < panSet.length; i++){
           if (i == 0) {//first one
            panSet[i] = new SonicRampSwatch(this, true, true);
            panSet[i].setSwatchColor(this.getLowColor());
          } else if (i == nSwatches - 1) {//last one
            panSet[i] = new SonicRampSwatch(this, true, true);
            panSet[i].setSwatchColor(this.getHighColor());
          } else {
            if (i < len-1) {
              boolean anch = anchored[i];
              Color c = colors[i];
              panSet[i] = new SonicRampSwatch(this, anch, false);
              panSet[i].setSwatchColor(c);
            } else {
            panSet[i] = new SonicRampSwatch(this, false, false);
             panSet[i].setSwatchColor(Color.white);
             }
          }
          //panSet[i].setPreferredSize(new Dimension(40,40));
          this.add(panSet[i]);
          this.add(this.insturmentCombo);

        }
        anchored = new boolean[nSwatches];
        colors = new Color[nSwatches];

                    //swatSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
    }




    public void rampSwatches() {

        if (panSet.length <= 0){
          return;
        }
        for (int j = 0; j < panSet.length; j++){
          colors[j] = panSet[j].getSwatchColor();
          if(panSet[j].getAnchored()){
            anchored[j] = true;
          } else {
             anchored[j] = false;
          }
        }
        this.ramp.rampColors(this.colors,this.anchored);
        
        for (int j = 0; j < panSet.length; j++){
            panSet[j].setSwatchColor(colors[j]);
            int numPans = panSet.length;
            int panStep = (SonicRampPicker.DEFAULT_HIGH_KEY - SonicRampPicker.DEFAULT_LOW_KEY)/(numPans-1);
            int panKey = j * panStep + SonicRampPicker.DEFAULT_LOW_KEY;
            panSet[j].setKeyNum(panKey);//setting sound
            logger.finest("penKey = " + panKey);
        }


    }

    public void swatchChanged() {
      this.rampSwatches();
      this.fireActionPerformed(SonicRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
    }

    private void changeOrientation(int orientation){
      if (orientation == this.currOrientation) {
        return;
      } else if (orientation == SonicRampPicker.X_AXIS) {
        Component[] comps = new Component[this.getComponentCount()];
        int counter = 0;
        for (int i = this.getComponentCount()-1; i > -1; i--) {
          comps[counter] = this.getComponent(i);
          counter++;
        }
        //this.removeAll();
        this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
        for (int i = 0; i < comps.length; i++) {
          this.add(comps[i]);
        }

        this.currOrientation = SonicRampPicker.X_AXIS;
        this.revalidate();
      } else if (orientation == SonicRampPicker.Y_AXIS) {
        Component[] comps = new Component[this.getComponentCount()];
        for (int i = 0; i < this.getComponentCount(); i++) {
          comps[i] = this.getComponent(i);
        }
        //this.removeAll();
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        for (int i = this.getComponentCount()-1; i > -1; i--) {
          this.add(comps[i]);
        }
        this.currOrientation = SonicRampPicker.Y_AXIS;
        this.revalidate();
      }

    }
     //start component event handling
     //note: this class only listens to itself
    public void componentHidden(ComponentEvent e) {}

    public void componentMoved(ComponentEvent e) {}

    public void componentShown(ComponentEvent e) {}

    public void componentResized(ComponentEvent e) {
     float ratio = ((float)this.getWidth() / (float)this.getHeight());

     if (ratio >= 1 && this.currOrientation == SonicRampPicker.Y_AXIS) {
      this.changeOrientation(SonicRampPicker.X_AXIS);
     }
     if (ratio < 1 && this.currOrientation == SonicRampPicker.X_AXIS) {
      this.changeOrientation(SonicRampPicker.Y_AXIS);
     }
    }
    //end component event handling
    //start accessors

    public void setPanSet(SonicRampSwatch[] panSet) {
      this.panSet = panSet;
    }
    public SonicRampSwatch[] getPanSet() {
      return this.panSet;
    }

    public void setAnchored(boolean[] anchored) {
      this.anchored = anchored;
    }
    public boolean[] getAnchored() {
      return this.anchored;
    }

    public void setColors(Color[] colors) {
      this.nSwatches = colors.length;
      this.colors = colors;
      this.anchored = new boolean[colors.length];
      for (int i = 0; i < colors.length; i++){
        this.anchored[i] = true;
      }
      this.setLowColor(colors[0]);
      this.setHighColor(colors[colors.length-1]);
      this.colors = colors;
      this.rampSwatches();
      this.repaint();
    }
    public Color[] getColors() {
      return this.colors;
    }
    public void playKey(int swatch){
      if (this.previousPlay >= 0 && this.previousPlay < this.panSet.length){
      SonicRampSwatch swat = this.panSet[previousPlay];
      swat.setNoteState(SonicRampSwatch.OFF);
      swat.off();
      }

      SonicRampSwatch swat = this.panSet[swatch];
      swat.setNoteState(SonicRampSwatch.ON);
      swat.on();
      this.previousPlay = swatch;
    }

    public void setRamp(SonicRamp ramp) {
      this.ramp = ramp;
    }
    public SonicRamp getRamp() {
      return this.ramp;
    }

    public void setLowColor(Color lowColor) {
      this.colors[0] = lowColor;
      this.panSet[0].setSwatchColor(lowColor);
      this.makeRamp(this.nSwatches);
      this.rampSwatches();
      this.repaint();
    }
    public Color getLowColor() {
      return colors[0];
    }

    public void setHighColor(Color highColor) {
      this.colors[colors.length -1] = highColor;
      this.makeRamp(this.nSwatches);
      this.rampSwatches();
      this.repaint();
      this.fireActionPerformed(SonicRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
    }
    public Color getHighColor() {
      return this.colors[colors.length -1];
    }

    public void setNSwatches(int nSwatches) {
      this.nSwatches = nSwatches;
      this.makeRamp(nSwatches);
      this.rampSwatches();
      this.validate();
      this.repaint();
    }
    public int getNSwatches() {
      return this.nSwatches;
    }


    /**
     * implements ActionListener
     */
	public void addActionListener (ActionListener l) {
		listenerList.add(ActionListener.class, l);
	}

    /**
     * removes an ActionListener from the component
     */
	public void removeActionListener (ActionListener l) {
		listenerList.remove(ActionListener.class, l);
	}

    /**
     * Notify all listeners that have registered interest for
     * notification on this event type. The event instance
     * is lazily created using the parameters passed into
     * the fire method.
     * @see EventListenerList
     */
	private void fireActionPerformed (String command) {
        // Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ActionListener.class) {
                // Lazily create the event:
				if (e == null) {
					e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
				}
				((ActionListener)listeners[i + 1]).actionPerformed(e);
			}
		}
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
