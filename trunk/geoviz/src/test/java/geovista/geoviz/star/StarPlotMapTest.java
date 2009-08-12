/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.star;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class StarPlotMapTest extends TestCase {
	public void testStarPlotMap() {

		StarPlotMap comp = new StarPlotMap();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
