/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ColorInterpolator
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ColorInterpolator.java,v 1.3 2003/07/18 14:07:50 hardisty Exp $
 $Date: 2003/07/18 14:07:50 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *

 */

package geovista.symbolization;

import java.awt.Color;

// import javax.swing.colorchooser.*;

public class ColorInterpolator {

	public ColorInterpolator() {
	}

	public static Color mixColorsHSB(Color leftColor, Color rightColor) {
		// float[] lowVals;
		// lowVals =
		// Color.RGBtoHSB(this.getLowColor().getRed(),this.getLowColor().getGreen(),this.getLowColor().getBlue(),null);
		float[] leftVals;
		leftVals = Color.RGBtoHSB(leftColor.getRed(), leftColor.getGreen(),
				leftColor.getBlue(), null);
		float[] rightVals;
		rightVals = Color.RGBtoHSB(rightColor.getRed(), rightColor.getGreen(),
				rightColor.getBlue(), null);
		float h = (leftVals[0] + rightVals[0]) / 2f;
		float s = (leftVals[1] + rightVals[1]) / 2f;
		float b = (leftVals[2] + rightVals[2]) / 2f;

		return Color.getHSBColor(h, s, b);

	}

	/*
	 * creates a new instance of a Color and returns that, does not modify input
	 * colors
	 */
	public static Color mixColorsRGB(Color leftColor, Color rightColor) {

		// hack for hsv

		// int i = 0;
		// if (i == 0){
		// return ColorInterpolator.mixColorsHSB(leftColor,rightColor);
		// }

		// float[] lowVals;
		// lowVals =
		// Color.RGBtoHSB(this.getLowColor().getRed(),this.getLowColor().getGreen(),this.getLowColor().getBlue(),null);

		int r = (int) (((float) leftColor.getRed() + (float) rightColor
				.getRed()) / 2f);
		int g = (int) (((float) leftColor.getGreen() + (float) rightColor
				.getGreen()) / 2f);
		int b = (int) (((float) leftColor.getBlue() + (float) rightColor
				.getBlue()) / 2f);
		int a = (int) (((float) leftColor.getAlpha() + (float) rightColor
				.getAlpha()) / 2f);
		return new Color(r, g, b, a);

	}

	public static Color mixColorsRGBHighSaturation(Color leftColor,
			Color rightColor) {

		Color mixedColor = mixColorsRGB(leftColor, rightColor);
		float[] hsb = new float[3];
		Color.RGBtoHSB(mixedColor.getRed(), mixedColor.getGreen(), mixedColor
				.getBlue(), hsb);
		float sat = hsb[1];
		// System.out.println("****");
		// System.out.println("s " + sat);
		float distFromOne = 1 - sat;
		float halfDist = distFromOne / 1.7f;
		float newSat = 1 - halfDist;
		// System.out.println("new s " + newSat);
		float[] leftVals;
		leftVals = Color.RGBtoHSB(leftColor.getRed(), leftColor.getGreen(),
				leftColor.getBlue(), null);
		float[] rightVals;
		rightVals = Color.RGBtoHSB(rightColor.getRed(), rightColor.getGreen(),
				rightColor.getBlue(), null);

		float s = (leftVals[1] + rightVals[1]) / 2f;

		if (leftVals[1] > rightVals[1]) {
			s = leftVals[1];
		} else {
			s = rightVals[1];
		}

		Color hsbColor = Color.getHSBColor(hsb[0], s, hsb[2]);
		// Color hsbColor = new Color(hsb[0], newSat, hsb[2]);

		return hsbColor;
	}

}
