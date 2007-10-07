/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * To support freeze action in PCP
 * @author: jin Chen 
 * @date: Aug 27, 2005$
 * @version: 1.0
 */
package geovista.common.event;

import java.util.EventListener;

public interface FreezeListener extends EventListener {
    public void freezeChanged(FreezeEvent e);
}
