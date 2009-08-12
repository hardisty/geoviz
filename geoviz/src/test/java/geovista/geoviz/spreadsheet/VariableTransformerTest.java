/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.geoviz.spreadsheet;

import junit.framework.TestCase;

import geovista.geoviz.Exerciser;

public class VariableTransformerTest extends TestCase {
	public void testVariableTransformer() {

		VariableTransformer comp = new VariableTransformer();
		Exerciser exer = new Exerciser();
		exer.testGUIAndEvents(comp);
	}
}
