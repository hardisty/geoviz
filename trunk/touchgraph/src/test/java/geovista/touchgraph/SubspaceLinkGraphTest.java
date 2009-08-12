/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.touchgraph;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class SubspaceLinkGraphTest extends TestCase {
	public void testSubspaceLinkGraph() {

		SubspaceLinkGraph comp = new SubspaceLinkGraph();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
