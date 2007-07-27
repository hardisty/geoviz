/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * Store basic information of a one-dimension category, which is a result of classification.
 * @author: yunping liu
 * @date: Dec 7, 2004$
 * @version: 2.0
 */
package edu.psu.geovista.classification;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Basic2DCategory extends BasicCategory implements Category2D{
    private Range rangeY;
    private int yIndex = -1;
    private String yVariableName; //the name of the Y dimension variable based on which the classification is made and the category is generated
    public Basic2DCategory(Object ID, Set memberIds, String name, Range rangeX, Range rangeY) {
        super(ID, memberIds, name, rangeX);
        this.rangeY = rangeY;
    }
    
    public Basic2DCategory(Object ID, Set memberIds, Range rangeX, Range rangeY) {
        super(ID, memberIds, rangeX);
        this.rangeY = rangeY;
    }

     public int getYIndex() {
        return yIndex;
    }

    public void setYIndex(int index) {
        this.yIndex = index;
    }
    
    public int getXIndex() {
        return this.getIndex();
    }

    public void setXIndex(int index) {
        this.setIndex(index);
    }

    public Range[] getRanges() {
        Range[] ranges = new Range[2];
        ranges[0] = this.getXRange();
        ranges[1] = this.getYRange();
        return ranges;
    }

    public void setRanges(Range[] ranges) {
        this.setXRange(ranges[0]);
        this.setYRange(ranges[1]);
    }
    
    public edu.psu.geovista.classification.Range getXRange() {
        return this.getRange();
    }
    
    public edu.psu.geovista.classification.Range getYRange() {
        return this.rangeY;
    }
    
    public void setXRange(Range range) {
        this.setRange(range);
    }
    
    public void setYRange(Range range) {
        this.rangeY = range;
    }

    public String getYVariableName() {
        return yVariableName;
    }

    public void setYVariableName(String yVariableName) {
        this.yVariableName = yVariableName;
    }

    public String getXVariableName() {
        return this.getVariableName() ;
    }

    public void setXVariableName(String xVariableName) {
        this.setVariableName(xVariableName);
    }
    
    public static CategoryItf[] combine2Categories(CategoryItf[] x, CategoryItf[] y) {
        if (x != null && y != null) {
            int sizeX = x.length;
            int sizeY = y.length;
            if (sizeX > 0 && sizeY > 0) {
                Category2D[] combinedCategories = new Category2D[sizeX * sizeY];
                for (int i = 0; i < sizeX; i++) {
                    for (int j = 0; j < sizeY; j++) {
                        HashSet source, target;
                        if (x[i].getMemberIdSize() < y[j].getMemberIdSize()) {
                            source = x[i].getMemberIds();
                            target = y[j].getMemberIds();
                        }
                        else {
                            source = x[i].getMemberIds();
                            target = y[j].getMemberIds();
                        }
                        HashSet members2D = new HashSet();
                        if (source != null && !source.isEmpty() && target != null && !target.isEmpty()) {
                            Iterator itr = source.iterator();
                            while(itr.hasNext()) {
                                Object obj = itr.next();
                                if (target.contains(obj)) members2D.add(obj);
                            }
                        }
                        String ID = x[i].getID().toString() + "_" + y[j].getID().toString();
                        int index = i * sizeY + j;
                        combinedCategories[index] = new Basic2DCategory(ID, members2D, x[i].getRange(), y[j].getRange());
                        combinedCategories[index].setXVariableName(x[i].getVariableName());
                        combinedCategories[index].setYVariableName(y[j].getVariableName());
                        combinedCategories[index].setName(new String(x[i].getName() + ";" + y[j].getName()));
                    }
                }
                return combinedCategories;
            }
            else if (sizeX > 0) return x;
            else if (sizeY > 0) return y;
            else return null;
        }
        else if (x != null) return x;
        else if (y != null) return y;
        else return null;
    }
}
