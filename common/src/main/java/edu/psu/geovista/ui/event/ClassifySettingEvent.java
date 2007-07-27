/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * The event fired to change the classification setting( e.g.: variable name, number of categories,....)
 * Not complete yet, it simply meet current requirement, may need redesign later
 * @author: jin Chen
 * @date: Jan 21, 2005$
 * @version: 1.0
 */
package edu.psu.geovista.ui.event;

import java.util.EventObject;

import edu.psu.geovista.classification.setting.ClassifySetting;

public class ClassifySettingEvent extends EventObject {
     ClassifySetting  setting;


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     */
    public ClassifySettingEvent(Object source, ClassifySetting setting) {
        super(source);
        this.setting =setting;
    }

    public ClassifySetting getSetting() {
        return setting;
    }
}
