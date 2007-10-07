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

import java.util.EventObject;

public class FreezeEvent extends EventObject {
    public static final int ACTION_FREEZE =1;
    //public static final int ACTION_UNFREEZE =2; not use this. Must provide ids of those observation that still need to be frozen.
    public static final int ACTION_UNFREEZE_ALL =3;
    public static final int LEVEL_NORMAL=1;
    public static final int LEVEL_GROUP=2;//subclass can define more, like Summary, Classified...

    private int freezeAction;
    private int[] ids;//frozen or unfrozen ids
    private int mode;//can be: 1) normal mode=0; 2) group/summary mode=1.  Listener act on it only when their level match

    public FreezeEvent(Object source, int freezeType, int[] ids) {
        super(source);
        this.freezeAction = freezeType;
        this.ids = ids;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getFreezeAction() {
        return freezeAction;
    }

    public void setFreezeAction(int freezeAction) {
        this.freezeAction = freezeAction;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }
}
