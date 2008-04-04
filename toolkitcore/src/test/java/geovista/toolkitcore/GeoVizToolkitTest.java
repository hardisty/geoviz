/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.toolkitcore;

import junit.framework.TestCase;

public class GeoVizToolkitTest extends TestCase {

	// ;// = new GeoVizToolkit("");
	public GeoVizToolkitTest(String name) {
		super(name);
	}

	// protected void setUp() throws Exception {
	// newKit = new GeoVizToolkit("");
	// newKit.setVisible(true);
	// super.setUp();
	// }

	public void testMain() {

		System.out.println("I did done test main!");

	}

	public void testRemoveAllBeans() {
		// GeoVizToolkit newKit = new GeoVizToolkit("");
		// int numBeans = newKit.coord.getFiringBeans().length;
		// System.out.println(numBeans);
		// newKit.removeAllBeans();
		// numBeans = newKit.coord.getFiringBeans().length;
		// System.out.println(numBeans);
		// assert(newKit.coord.getFiringBeans().length == 0);

	}

	public void testOpenAllComponents() {
		GeoVizToolkit newKit = new GeoVizToolkit("");
		// newKit.openAllComponents();
		newKit = null;
	}

}
