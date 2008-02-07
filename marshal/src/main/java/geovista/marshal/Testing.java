/**
 * 
 */
package geovista.marshal;

import java.awt.Color;
import java.util.Iterator;

import geovista.geoviz.map.GeoMap;
import geovista.toolkitcore.GeoVizToolkit;
import geovista.toolkitcore.ToolkitBean;
import geovista.toolkitcore.ToolkitBeanSet;
import geovista.toolkitcore.ToolkitIO;

/**
 * @author localadmin
 * 
 */
public class Testing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		GeoVizToolkit gvz = new GeoVizToolkit();

		// ToolkitBeanSet tbs = gvz.getTBeanSet();

		ToolkitBeanSet tbs = ToolkitIO.openStarPlotMapLayout();
		Iterator<ToolkitBean> it = tbs.iterator();

		while (it.hasNext()) {
			ToolkitBean bean = it.next();
			if (bean.getObjectClass().equals("geovista.geoviz.map.GeoMap")) {
				GeoMap map = (GeoMap) bean.getOriginalBean();
				map.setBackgroundColor(Color.white);
			}
		}

		gvz.setTBeanSet(tbs);

		GeoMap map = new GeoMap();
		int[] sel = { 3, 4, 5 };
		map.setSelectedObservations(sel);

		Marshaller marsh = new Marshaller();

		String xml = marsh.toXML(gvz);

		System.out.println(xml);

		// gvz.init("48States", true);

		GeoVizToolkit gvz2 = marsh.fromXML(xml);

		// JAXBContext jc;
		// try {
		// jc = JAXBContext.newInstance("geovista.geoviz.map");
		// Marshaller m = jc.createMarshaller();
		// m.marshal(map, System.out);
		// } catch (JAXBException e2) {
		// // TODO Auto-generated catch block
		// e2.printStackTrace();
		// }

		// SelectionEvent map = new SelectionEvent(streamer,new int[]{1,2,3});

		/*
		 * some prefs testing Preferences prefs =
		 * Preferences.userNodeForPackage(Testing.class);
		 * 
		 * prefs.put("favorite color", "blue"); prefs.putInt("n classes", 78);
		 * try { prefs.flush(); } catch (BackingStoreException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } try {
		 * ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		 * prefs.exportNode(outStream); InputStream inStream = new
		 * ByteArrayInputStream(outStream.toByteArray()); SAXBuilder builder =
		 * new SAXBuilder();
		 * 
		 * Document doc = builder.build(inStream);
		 * System.out.println(doc.toString()); prefs.exportNode(System.out); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (BackingStoreException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }catch (JDOMException
		 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
		 */

		/*
		 * 
		 * XMLEncoder coder = null; try { coder = new XMLEncoder( new
		 * BufferedOutputStream( new FileOutputStream("Test.xml"))); } catch
		 * (FileNotFoundException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 * 
		 * //XMLEncoder coder = new XMLEncoder(System.out);
		 * 
		 * //coder.writeObject(new Integer(43)); coder.writeObject(map);
		 * coder.flush(); coder.close();
		 * 
		 * 
		 * 
		 * SAXBuilder parser = new SAXBuilder();
		 * 
		 * Document doc = null; try { doc = parser.build("Test.xml"); } catch
		 * (JDOMException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); } Iterator it =
		 * doc.getDescendants(); while (it.hasNext()){
		 * 
		 * //System.out.println(it.next()); }
		 */

		// GeoVizToolkit gvz2 = (GeoVizToolkit)streamer.fromXML(xml);
		// gvz2.setVisible(true);
	}

}
