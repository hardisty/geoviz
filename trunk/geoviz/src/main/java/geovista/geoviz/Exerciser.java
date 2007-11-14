/*
 *  This class is designed to provide a thorough work-out for any bean or set of beans that is to be so tested.
 */
package geovista.geoviz;

import geovista.common.event.SelectionListener;
import geovista.coordination.CoordinationManager;

import java.util.ArrayList;


public class Exerciser {
	
	CoordinationManager coord;
	ArrayList beans;
	Object beanIn;
	
	public enum Event{
		Selection,
		Indication,
		DataSet,
		VariableSelection,
		SpatialExtent,
		ColorArray,
		Conditioning,
		Subspace

	}
	/* Add a bean to this harness */	
	public void addBean(Object beanIn){

		coord.addBean(beanIn);	
		this.beans.add(beanIn);
	}
	public void testEvent(Event eventType, Object bean){
		switch (eventType){
			case Selection: 
				if(beanIn instanceof SelectionListener){
					
				}
				
				return;
			
		}
	}
	public void testEvent(Event e){
		
	}
	public void testAllEvents(){
		for (Event e : Event.values()){
			testEvent(e);
		}
	}
}