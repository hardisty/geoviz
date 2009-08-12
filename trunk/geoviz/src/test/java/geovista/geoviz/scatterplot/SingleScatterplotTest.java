/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.scatterplot;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class SingleScatterplotTest extends TestCase {
	public void testSingleScatterplot() {

		SingleScatterPlot comp = new SingleScatterPlot();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
