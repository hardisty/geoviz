/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.map;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class GeoMapTest extends TestCase {
	public void testGeoMap() {

		GeoMap comp = new GeoMap();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);
	}
}
