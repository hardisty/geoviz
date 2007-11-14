package geovista.toolkitcore;

import junit.framework.TestCase;

public class GeoVizToolkitTest extends TestCase {
	GeoVizToolkit kit;
	GeoVizToolkit newKit = new GeoVizToolkit("");
	public GeoVizToolkitTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		//kit = new GeoVizToolkit("");
		//kit.setVisible(true);
		super.setUp();
	}

	public void testMain() {
		
		
		System.out.println("I did done test main!");

	}
	public void testRemoveAllBeans(){
		
		int numBeans = newKit.coord.getFiringBeans().length;
		System.out.println(numBeans);
		newKit.removeAllBeans();
		numBeans = newKit.coord.getFiringBeans().length;
		System.out.println(numBeans);		
		assert(newKit.coord.getFiringBeans().length == 0);
	}

}
