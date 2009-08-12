/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.matrix;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class TreemapAndScatterplotMatrixTest extends TestCase {
	public void testTreemapAndScatterplotMatrix() {

		TreemapAndScatterplotMatrix comp = new TreemapAndScatterplotMatrix();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
