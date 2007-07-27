/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 *
 * @Original Author: jin Chen
 * @date: Aug 20, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.data.model;

import java.util.List;

public class Column {
    //function type
  public static final int FTYPE_NONE = -1; //funtion type
  public static final int FTYPE_NAME = 0;
  public static final int FTYPE_DATA = 1; //DATA
  public static final int FTYPE_NUMDATA = 2; //NUMERIC
  public static final int FTYPE_SPC = 3; //SPACIAL(SHAPE,POINT...)
  public static final String VALUE_NA="N/A";


    private String Id;//=VALUE_NA; //for now, name serve as ID
    private int fytpe=-1;//funtional type
    private Class dtype=null;//data type. null means unknown type. It indicates we don't care the type
    //private Class stype=null;
    private float value;
    private List data;
    //private int index; //index of column in data map which is mapped by the Index object


    public Column() {
        //name=VALUE_NA;
    }

    public Column(String Id) {
        this.Id = Id;
    }

    public Column(String Id, Class dtype, int fytpe) {
        this.Id = Id;
        this.dtype = dtype;
        this.fytpe = fytpe;
    }



    public String getID() {
        return Id;
    }

    public void setID(String ID) {
        this.Id = ID;
    }

    public Class getDtype() {
        return dtype;
    }

    public void setDtype(Class dtype) {
        this.dtype = dtype;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public int getFytpe() {
        return fytpe;
    }

    public void setFytpe(int fytpe) {
        this.fytpe = fytpe;
    }



    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }


}
