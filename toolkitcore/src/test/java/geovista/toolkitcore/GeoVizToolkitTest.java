package geovista.toolkitcore;

import junit.framework.TestCase;

public class GeoVizToolkitTest extends TestCase {
	GeoVizToolkit kit;
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
	    //assert(kit != null);
	}
	public void testRemoveAllBeans(){
		GeoVizToolkit newKit = new GeoVizToolkit("");
		int numBeans = newKit.coord.getFiringBeans().length;
		newKit.removeAllBeans();
		assert(kit.coord.getFiringBeans().length == 0);
	}

}
