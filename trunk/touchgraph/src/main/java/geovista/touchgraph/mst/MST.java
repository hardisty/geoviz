package geovista.touchgraph.mst;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Vector;

import javax.swing.JFrame;
import geovista.common.event.SelectionEvent;
import geovista.geoviz.map.GeoMap;

/**
 * Title: MST Description: Contains only the main method, just starts a
 * controller Copyright: Copyright (c) 2002 Company: MAH TS
 * 
 * @author Markus Svensson
 * @version 1.1
 */

public class MST {
	/**
	 * Main method for the MST Java Project
	 */
	
	public static void main(String[] args) {
		//new Controller(args);
		
		//time for some xml tests
		File fi = new File("C://xmlout.xml");

        XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(
			        new BufferedOutputStream(
			                new FileOutputStream(fi)));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		int[] nums = {1,3,6,45};
		
		SelectionEvent e = new SelectionEvent();
		e.setSelection(nums);
		encoder.writeObject(e);
		
		//pan.setSize(new Dimension(45,67));
		//pan.setBackground(Color.red);
		JFrame app = new JFrame();
		app.setTitle("llalalal");
		GeoMap map = new GeoMap();
		app.getContentPane().add(map);
		
		encoder.writeObject(app);
		

		
		encoder.writeObject(map);
		
		Integer i = new Integer(55);
		Vector v = new Vector();
		v.add(i);
		encoder.writeObject(v);
		
		System.out.println("all done!");
		
		//XStream streamer = new XStream(new DomDriver()); // does not require XPP3 library
		//String eString = streamer.toXML(nums);
		//System.out.println(eString);
		//JPanel pan = new JPanel();
		//String panString = streamer.toXML(pan);
		//System.out.println(panString);
		
		
		
	}
}