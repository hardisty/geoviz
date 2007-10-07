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
package geovista.common.classification.setting;

public class AbstractClassifySetting implements ClassifySetting {
    /* a value of   VARIABLE,BOUNDARY,NUM_OF_CATEGORY,COMBINED ....
     * With a type other than COMBINED(e.g.:VARIABLE) , u should only set one attribute (e.g. : VARIABLE)
     */
    int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
