package edu.psu.geovista.cartogram;
/*
 * Created on Dec 10, 2004
 * Point structure with float coordinates
 */

/**
 * @author Nick
 *
 */
public class Point{
	public Point(){
		x = y = 0;
	}
	public Point(float x, float y){
		this.x = x;
		this.y = y;
	}
	public float x,y;

	public String toString(){
		return "("+x+","+y+")";
	}
}
