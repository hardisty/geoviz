/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.parviz;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;
import geovista.geoviz.parvis.ParallelPlot;

public class ParallelPlotTest extends TestCase {
	public void testParallelPlot() {

		ParallelPlot comp = new ParallelPlot();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
