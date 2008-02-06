/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SonicRampTest
 Copyright (c), 2000, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: SonicRampTest.java,v 1.1 2003/07/29 15:47:31 jmacgill Exp $
 $Date: 2003/07/29 15:47:31 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------   */

package geovista.sound;

import geovista.coordination.CoordinationManager;
import geovista.geoviz.sample.GeoData48States;
import geovista.geoviz.sample.GeoDataGeneralizedStates;
import geovista.geoviz.shapefile.ShapeFileToShape;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SonicClassifierTest extends TestCase {
	protected SonicClassifier sClasser;

	public SonicClassifierTest(String name) {
		super(name);
		setUp();
	}

	@Override
	protected void setUp() {
		sClasser = new SonicClassifier();
		CoordinationManager coord = new CoordinationManager();
		GeoDataGeneralizedStates stateData = new GeoDataGeneralizedStates();
		ShapeFileToShape shpToShape = new ShapeFileToShape();
		coord.addBean(sClasser);
		coord.addBean(shpToShape);
		shpToShape.setInputDataSet(stateData.getDataSet());
	}

	public static Test suite() {
		return new TestSuite(SonicClassifierTest.class);
	}

	public void testGetRampedValueRGB() {

	}

}