package geovista.toolkitcore;

import junit.framework.TestCase;

public class GeoVizToolkitTest extends TestCase {

	public GeoVizToolkitTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMain() {
		GeoVizToolkit kit = new GeoVizToolkit("");
		kit.setVisible(true);
	 assert(kit != null);
	}

}
