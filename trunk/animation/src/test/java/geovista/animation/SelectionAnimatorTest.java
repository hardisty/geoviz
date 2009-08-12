/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.animation;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class SelectionAnimatorTest extends TestCase {
	public void testSelectionAnimator() {

		SelectionAnimator comp = new SelectionAnimator();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
