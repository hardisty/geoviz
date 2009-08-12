/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class MapScatterplotTreemapMatrixTest extends TestCase {
	public void testMapScatterplotTreemapMatrix() {

		MapScatterplotTreemapMatrix comp = new MapScatterplotTreemapMatrix();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
