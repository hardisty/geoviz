package edu.psu.geovista.geoviz.condition;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.psu.geovista.common.ui.slider.RangeSlider;

public class Sliders extends JPanel implements ChangeListener, ComponentListener{

	Vector titleRangesVector = new Vector();
	Vector sliderVector = new Vector();
	Object[] sliderRanges;
	int numSlider;
	Object titleRanges;
	protected final static Logger logger = Logger.getLogger(Sliders.class.getName());
    public Sliders() {
		this.setMinimumSize(new Dimension(50, 30));
		this.setPreferredSize(new Dimension(100, 50));
		this.addComponentListener(this);
    }

    public int getNumSlider() {
        return numSlider;
    }

    public void setNumSlider(int numSlider) {
        this.numSlider = numSlider;
    }

    public Object getTitleRanges() {
        return titleRanges;
    }

    public void setTitleRanges(Object titleRanges) {
        this.titleRanges = titleRanges;
    }

    public Vector getTitleRangesVector() {
        return titleRangesVector;
    }

    public void setTitleRangesVector(Vector titleRangesVector) {

        this.titleRangesVector = titleRangesVector;
		init();
    }

	public Object[] getSliderRanges() {
		logger.finest("get slider ranges..");
		return sliderRanges;
	}

	public void setSliderRanges(Object[] sliderRanges) {
		this.sliderRanges = sliderRanges;
    }

	private void init(){
		this.removeAll();
		int size = this.titleRangesVector.size();
		this.setLayout(new GridLayout(size, 1));
		this.sliderVector.clear();
		this.sliderRanges = new Object[size];
		for (int i = 0; i < size; i ++){
			RangeSlider slider = new RangeSlider();
			slider.setRangeTitle((Object[])this.titleRangesVector.get(i));
			this.sliderVector.add(i, slider);
			this.add((RangeSlider) sliderVector.get(i));
			this.sliderRanges[i] = slider.getValueDoubles();

			((RangeSlider) sliderVector.get(i)).addChangeListener(new ChangeListener() {

				/**
				* Get the event from a unit plot and send it to all units.
				* @param e
				*/
				public void stateChanged (ChangeEvent e) {
					// This gets the source or originator of the event
					try {
						slider_actionPerformed(e);
						} catch (Exception ex) {
							logger.throwing(Sliders.class.getName(), "init", ex);
						}
					}
				});
			this.add((RangeSlider) sliderVector.get(i));
		}
		this.validate();
	}

	private void slider_actionPerformed(ChangeEvent e){
		RangeSlider slider = (RangeSlider)e.getSource();
		for (int i = 0; i < this.titleRangesVector.size(); i ++){
			if (slider == (RangeSlider) sliderVector.get(i)){
				this.sliderRanges[i] = slider.getValueDoubles();
				break;
			}
		}
		this.fireChangeEvent();
	}

	// Event handling
	public void stateChanged (ChangeEvent ce) {
		try {} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
	}

	public void addChangeListener (ChangeListener l) {
		this.listenerList.add(ChangeListener.class, l);
	}

	/**
	 * put your documentation comment here
	 * @param l
	 */
	public void removeChangeListener (ChangeListener l) {
		this.listenerList.remove(ChangeListener.class, l);
	}

	/**
	 * put your documentation comment here
	 */
	private void fireChangeEvent () {
		Object[] listeners = this.listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener)listeners[i + 1]).stateChanged(new ChangeEvent(this));
			}
		}             // end for
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {
		logger.finest("in component resized");
		RangeSlider slider;
		int size = this.titleRangesVector.size();
		int width = this.getWidth();
		logger.finest("in component resized" + width);
		for (int i = 0; i < size; i ++){
			slider = (RangeSlider) this.sliderVector.elementAt(i);
			if ((width < 290) || (slider.getHeight() < 70)){
				//((RangeSlider)this.sliderVector.elementAt(i)).setPaintLabel(false);
				slider.setPaintLabel(false);
			}else {
				slider.setPaintLabel(true);
			}
		}
	}

	public void componentShown(ComponentEvent e) {
	}


}
