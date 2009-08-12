/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.spacefill;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class SpaceFillTest extends TestCase {
	public void testSpacefill() {

		SpaceFill comp = new SpaceFill();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
