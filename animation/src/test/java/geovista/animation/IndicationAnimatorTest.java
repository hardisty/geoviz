/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.animation;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class IndicationAnimatorTest extends TestCase {
	public void testIndicationAnimator() {

		IndicationAnimator comp = new IndicationAnimator();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
