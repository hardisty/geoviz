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

import java.awt.Color;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class SonicRampTest extends TestCase {
	protected SonicRamp ramp;

	public SonicRampTest(String name) {
		super(name);
                setUp();
	}

	protected void setUp() {
		ramp = new SonicRamp();
                Color[] colors = new Color[3];
                colors[0] = Color.white;
                colors[2] = Color.black;
                ramp.rampColors(colors);
	}
	public static Test suite() {
		return new TestSuite(SonicRampTest.class);
	}


	public void testGetRampedValueRGB() {
            double prop;

            int resultColor;

            prop = 0;

            resultColor = ramp.getRampedValueRGB(prop);
            assertTrue(resultColor == -1);

	}
	public void testRampColors() {
            boolean[] anch = new boolean[7];
            //all anchors start out false
            anch[0] = true;
            anch[2] = true;
            anch[6] = true;

            Color[] col = new Color[7];
            for (int i = 0; i < col.length; i++) {
              col[i] = Color.white;
            }
            col[6] = Color.black;

            ramp.rampColors(col,anch);
            int redVal = col[1].getRed();
            assertTrue(col[1].getRed() == 255);
            redVal = col[5].getRed();
            //255 * .25 = 63.75
            assertTrue(redVal == 64);


	}


}