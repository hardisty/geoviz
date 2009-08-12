/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.touchgraph;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class PCAVizTest extends TestCase {
	public void testPCAViz() {

		PCAViz comp = new PCAViz();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
