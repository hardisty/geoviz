package edu.psu.geovista.common.event;

import java.util.EventListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public interface BivariatePaletteListener extends EventListener {

  public void bivariatepaletteChanged(BivariatePaletteEvent e);

}