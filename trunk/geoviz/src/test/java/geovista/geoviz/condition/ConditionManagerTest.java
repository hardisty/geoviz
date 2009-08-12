/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.condition;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class ConditionManagerTest extends TestCase {
	public void testConditionManager() {

		ConditionManager comp = new ConditionManager();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);

	}
}
