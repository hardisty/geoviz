/*
 * DimensionListener.java
 *
 * Created on June 7, 2004, 12:55 PM
 */

package geovista.common.event;

import java.util.EventListener;

/**
 *
 * @author  jamesm
 */
public interface DimensionListener extends EventListener{
   
    public void dimensionChanged(DimensionEvent e);
   
}
