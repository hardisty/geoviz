/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ShapeFileToShape
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: ShapeFileToShape.java,v 1.1 2004/12/03 19:27:34 jmacgill Exp $
 $Date: 2004/12/03 19:27:34 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.readers.shapefile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.readers.shapefile.example.GeoData48States;


/**
 * This class does nothing now and is slated for removal.
 * All its functionality has been moved to ShapeFileDataReader
 *
 * This bean (used to) takes a shapefile and turns it into GeneralPath objects.
 * Note that there is a loss of accuracy from double to float upon doing so.
 */
public class ShapeFileToShape implements ActionListener {
	protected final static Logger logger = Logger.getLogger(ShapeFileToShape.class.getName());
  private transient DataSetForApps inputDataSetForApps;
  private transient DataSetForApps outputDataSetForApps;
  private transient EventListenerList listenerList;



  public ShapeFileToShape() {
    super();
    listenerList = new EventListenerList();
  }




    public void setInputDataSetForApps(DataSetForApps inputDataSetForApps) {
      if (inputDataSetForApps != null) {
        this.inputDataSetForApps = inputDataSetForApps;
        this.outputDataSetForApps = inputDataSetForApps;
        this.fireActionPerformed("made shapes");
        this.fireDataSetChanged(this.outputDataSetForApps);
      }

    }
    public DataSetForApps getInputDataSetForApps() {
      return this.inputDataSetForApps;
    }

    public void setOutputDataSetForApps(DataSetForApps outputDataSetForApps) {
      this.outputDataSetForApps = outputDataSetForApps;
    }
    public DataSetForApps getOutputDataSetForApps() {
      return this.outputDataSetForApps;
    }
    public void setInputDataSet(Object[] inputDataSet) {

      if (inputDataSet != null) {
        this.inputDataSetForApps = new DataSetForApps(inputDataSet);
        this.outputDataSetForApps = this.inputDataSetForApps;

        this.fireActionPerformed("made shapes");
        this.fireDataSetChanged(this.outputDataSetForApps);
      }

    }
    public Object[] getInputDataSet() {
      return this.inputDataSetForApps.getDataObjectOriginal();
    }

    public void setOutputDataSet(Object[] outputDataSet) {
      //this.outputDataSet = outputDataSet;
    }
    public Object[] getOutputDataSet() {
      return this.outputDataSetForApps.getDataObjectOriginal();
    }


    public void setListenerList(EventListenerList listenerList) {
      this.listenerList = listenerList;
    }
    public EventListenerList getListenerList() {
      return this.listenerList;
    }

    public void actionPerformed (ActionEvent e) {

      if (e.getSource() instanceof GeoData48States){
        GeoData48States data = (GeoData48States)e.getSource();
        this.setInputDataSet(data.getDataSet());
      }
    }

  /**
   * implements ActionListener
   */
  public void addActionListener(ActionListener l) {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * removes an ActionListener from the button
   */
  public void removeActionListener(ActionListener l) {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireActionPerformed(String command) {
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   ActionEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == ActionListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new ActionEvent(this,
                    ActionEvent.ACTION_PERFORMED,
                    command);
       }
       ((ActionListener)listeners[i + 1]).actionPerformed(e);
      }
    }
  }
  /**
   * implements DataSetListener
   */
  public void addDataSetListener(DataSetListener l) {
    listenerList.add(DataSetListener.class, l);
  }

  /**
   * removes an DataSetListener from the button
   */
  public void removeDataSetListener(DataSetListener l) {
    listenerList.remove(DataSetListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  protected void fireDataSetChanged(DataSetForApps dataSet) {
      logger.info("ShpToShp.fireDataSetChanged, not supposed to use me :(");
   // Guaranteed to return a non-null array
   Object[] listeners = listenerList.getListenerList();
   DataSetEvent e = null;
   // Process the listeners last to first, notifying
   // those that are interested in this event
   for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i] == DataSetListener.class) {
       // Lazily create the event:
       if (e == null) {
         e = new DataSetEvent(dataSet, this);

       }
       ((DataSetListener)listeners[i + 1]).dataSetChanged(e);
      }
    }
  }
}