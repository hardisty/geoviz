/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 *
 * @author: jin Chen 
 * @date: Dec 8, 2004$
 * @version: 1.0
 */
package edu.psu.geovista.classification;

public class Range {
    //public static final float PRCESION=1f;//it is not practical to get absolute equality. This is for approximately equal
    double max;
    double min;
    //private boolean valid;

    //private float range;
    public Range() {
        this(0.0, 0.0);
    }

    public Range(double max, double min) {
        this.max = max;
        this.min = min;
    }
    

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public boolean isValid() {
        return (getRange()>0);
    }
    public void setExtreme(Range extreme) {
        this.max = extreme.max ;
        this.min = extreme.min;
    }
    public double getRange() {
        return (max-min);
    }
    public boolean equal(Range range) {
        if (this.getMax() == range.getMax() &&
            this.getMin() == range.getMin() ){
            return true;
        }
        return false;
    }
    public String toString(){
        return "["+min+","+max+"]";
    }
}
