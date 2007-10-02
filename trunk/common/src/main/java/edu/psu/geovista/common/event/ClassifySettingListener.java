/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 *
 * @author: jin Chen 
 * @date: Jan 21, 2005$
 * @version: 1.0
 */
package edu.psu.geovista.common.event;

import java.util.EventListener;

public interface ClassifySettingListener extends EventListener {
    public void classifySettingChanged(ClassifySettingEvent setting);
}
