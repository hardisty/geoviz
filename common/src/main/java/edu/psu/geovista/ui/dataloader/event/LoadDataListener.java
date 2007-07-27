package edu.psu.geovista.ui.dataloader.event;

import java.util.EventListener;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @author: jin Chen 
 * @date: Oct 21, 2003$
 * @version: 1.0
 */

public interface LoadDataListener extends EventListener {
    public void loadData(LoadDataEvent evt);

}
