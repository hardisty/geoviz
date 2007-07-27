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
package edu.psu.geovista.ui.event;

import java.util.EventObject;

import edu.psu.geovista.group.MapGroup;



public class GroupEvent extends EventObject {
    MapGroup group;
    public GroupEvent(Object source) {
        super(source);
    }

    public MapGroup getGroup() {
        return group;
    }

    public void setGroup(MapGroup group) {
        this.group = group;
    }

}
