/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.radviz;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class RadVizTest extends TestCase {
	public void testRadViz() {

		RadViz comp = new RadViz();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
