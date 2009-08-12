/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.map;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class GeoMapUniTest extends TestCase {
	public void testGeoMapUni() {
		GeoMapUni comp = new GeoMapUni();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);
	}
}
