/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Frank Hardisty */

package geovista.sound;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import geovista.coordination.CoordinationManager;
import geovista.readers.example.GeoDataGeneralizedStates;
import geovista.readers.shapefile.ShapeFileToShape;

public class SonicClassifierTest extends TestCase {
	protected SonicClassifier sClasser;

	public SonicClassifierTest(String name) {
		super(name);
		setUp();
	}

	@Override
	protected void setUp() {
		// sClasser = new SonicClassifier();
		CoordinationManager coord = new CoordinationManager();
		GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		// coord.addBean(sClasser);
		coord.addBean(shpToShape);
		shpToShape.setInputDataSet(stateData.getDataSet());
	}

	public static Test suite() {
		return new TestSuite(SonicClassifierTest.class);
	}

	public void testGetRampedValueRGB() {

	}

}