package edu.psu.geovista.data.model;

/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description
 *
 * Date: May 29, 2003
 * Time: 11:22:40 AM
 * @Original Author Jin Chen
 */

public class Range {
    public static final float PRCESION=1f;//it is not practical to get absolute equality. This is for approximately equal
    float max;
    float min;
    //private float range;
    public Range() {
        this(0,0);
    }

    public Range(float max, float min) {
        this.max = max;
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public boolean isValid() {
        return (getRange()>0);
    }
    public boolean setExtreme(Range range) {

        return this.setExtreme(range.min,range.max);
    }
    public float getRange() {
        return (max-min);
    }
    public boolean equal(Range range) {
        if (this.getMax() ==range.getMax() &&
            this.getMin() ==range.getMin() ){
            return true;
        }
        return false;
        //return (Math.abs(this.getRange() -range.getRange() )<=PRCESION);

    }
   /**
    *
    * @param min
    * @param max
    * @return     true, if update. False if given values are same as current ones
    */
    public boolean setExtreme(float min, float max) {
        //boolean updated=false;
        if(min==this.getMin() &&max==this.getMax()){
            return false;
        }
        else{
            this.setMax(max);
            this.setMin(min);
            return true;
        }
    }
}
