/**
 * 
 */
package geovista.attic;

import geovista.common.event.SelectionEvent;
import geovista.geoviz.map.GeoMap;
import geovista.toolkitcore.GeoVizToolkit;
import geovista.toolkitcore.ToolkitBean;
import geovista.toolkitcore.ToolkitBeanSet;

import java.awt.Color;
import java.beans.XMLEncoder;
import java.util.BitSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * @author hardisty
 *
 */
public class SerializationExp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		XMLEncoder enc = new XMLEncoder(System.out);
		SelectionEvent sel = new SelectionEvent();
		int[] array = {1,2,3,4};
		sel.setSelection(array);
		
		BitSet set = new BitSet(4);
		set.flip(2);
		
		GeoVizToolkit tk = new GeoVizToolkit("");
		tk.setVisible(true);

		GeoMap geo = new GeoMap();
		geo.setBackground(Color.CYAN);
		
		ToolkitBeanSet beanSet = tk.getTBeanSet();
		beanSet.add(new ToolkitBean(geo,"a ggg geo"));
		tk.setVisible(false);
		
		enc.writeObject(beanSet);
		enc.close();
		
	       JAXBContext jc;
	       Marshaller m;
	       
	       
//		try {
//			jc = JAXBContext.newInstance( "geovista.toolkitcore" );
//		       m = jc.createMarshaller();
//		       m.marshal(beanSet, System.out);
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



	}

}
