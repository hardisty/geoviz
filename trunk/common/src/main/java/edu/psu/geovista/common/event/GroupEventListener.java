package edu.psu.geovista.common.event;

import java.util.EventListener;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen 
 * @date: May 23, 2004$
 * @version: 1.0
 */

public interface GroupEventListener  extends EventListener {


  public void groupChanged(GroupEvent e);


}
