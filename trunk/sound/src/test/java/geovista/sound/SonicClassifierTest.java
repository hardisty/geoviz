/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.sound;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class SonicClassifierTest extends TestCase {
	public void testSonicClassifier() {

		SonicClassifier comp = new SonicClassifier();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}

}