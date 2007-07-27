/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 *
 * @author: jin Chen 
 * @date: Jan 6, 2005$
 * @version: 1.0
 */
package edu.psu.geovista.classification;

import java.awt.Color;

public class BasicVisualInfo implements VisualInfo {
    Color color;
    CategoryItf category;

    public BasicVisualInfo(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    /*public CategoryItf getCategory() {
        return category;
    }

    public void setCategory(CategoryItf category) {
        this.category = category;
    }*/
}
