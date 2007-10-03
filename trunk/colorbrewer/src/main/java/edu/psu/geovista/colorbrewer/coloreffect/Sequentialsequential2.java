package edu.psu.geovista.colorbrewer.coloreffect;

import java.util.logging.Logger;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class Sequentialsequential2 {
	protected final static Logger logger = Logger.getLogger(Sequentialsequential2.class.getName());
	// the angle from the Red-Green axis
	private static final double THETA = 90;

	// the angle that control the shape of the diamond
	private static final double ALPHA = 60;

	// the angle from the a-b surface
	private static final double BETA = 75;

	// the max & min lightness of the color schemes
	private static final double MAX_LIGHTNESS = 100;

	private static final double MIN_LIGHTNESS = 0;

	private int numberoflightnessSteps;

	private double lightnessStep;

	public LABcolor[][] labcolor;

	public Sequentialsequential2(int row, int column) {

		// creating the labcolor double array to store the values in LAB format
		this.labcolor = new LABcolor[row][column];

		// calculating the number of lightnessSteps
		this.numberoflightnessSteps = row * 2 - 1;

		// creating a lightness array to store the lightness of each level
		double[] lightness = new double[this.numberoflightnessSteps];

		// the first element take on the maxlightness
		lightness[0] = Sequentialsequential2.MAX_LIGHTNESS;

		// the last element take on the minlightness
		lightness[this.numberoflightnessSteps - 1] = Sequentialsequential2.MIN_LIGHTNESS;

		// calculating the span of the lightnessstep
		this.lightnessStep = (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
				/ (double) (this.numberoflightnessSteps - 1);

		// assigning lightness to each level
		for (int i = 0; i < this.numberoflightnessSteps; i++) {
			lightness[i] = Sequentialsequential2.MAX_LIGHTNESS - i * this.lightnessStep;
			logger.finest(""+i);
			logger.finest(""+(lightness[i] + '\n'));
		}

		// declaring the variable to use in calculating the colors
		double L;
		double a = 0;
		double b = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {

				if (Sequentialsequential2.THETA <= 90 && Sequentialsequential2.THETA >= 0) {
					// calculating the a & b according to the matrix

					a = Math.cos(Math.toRadians(90 - Sequentialsequential2.THETA)) * (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * row)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.cos(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));

					// a = Math.cos(Math.toRadians(Sequentialsequential2.theta))*(i -
					// j)*(this.maxLightness -
					// this.minLightness)/(2*this.numberofClasses*Math.tan(Math.toRadians(this.alhpa)));

					b = Math.sin(Math.toRadians(90 - Sequentialsequential2.THETA))
							* (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * column)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.sin(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));
				}

				if (Sequentialsequential2.THETA <= 180 && Sequentialsequential2.THETA > 90) {
					// calculating the a & b according to the matrix

					a = Math.cos(Math.toRadians(180 - Sequentialsequential2.THETA)) * (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * row)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.cos(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));

					// a = Math.cos(Math.toRadians(this.theta))*(i -
					// j)*(this.maxLightness -
					// this.minLightness)/(2*this.numberofClasses*Math.tan(Math.toRadians(this.alhpa)));

					b = Math.sin(Math.toRadians(180 - Sequentialsequential2.THETA))
							* (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * column)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.sin(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));
				}

				if (Sequentialsequential2.THETA <= 270 && Sequentialsequential2.THETA > 180) {
					// calculating the a & b according to the matrix

					a = Math.cos(Math.toRadians(270 - Sequentialsequential2.THETA)) * (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * row)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.cos(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));

					// a = Math.cos(Math.toRadians(this.theta))*(i -
					// j)*(this.maxLightness -
					// this.minLightness)/(2*this.numberofClasses*Math.tan(Math.toRadians(this.alhpa)));

					b = Math.sin(Math.toRadians(270 - Sequentialsequential2.THETA))
							* (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * column)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.sin(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));
				}

				if (Sequentialsequential2.THETA <= 360 && Sequentialsequential2.THETA > 270) {
					// calculating the a & b according to the matrix

					a = Math.cos(Math.toRadians(360 - Sequentialsequential2.THETA)) * (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * row)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.cos(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));

					// a = Math.cos(Math.toRadians(this.theta))*(i -
					// j)*(this.maxLightness -
					// this.minLightness)/(2*this.numberofClasses*Math.tan(Math.toRadians(this.alhpa)));

					b = Math.sin(Math.toRadians(360 - Sequentialsequential2.THETA))
							* (i - j)
							* (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.tan(Math.toRadians(Sequentialsequential2.ALPHA))
							/ (2 * Math.sin(Math.toRadians(Sequentialsequential2.BETA)) * column)
							+ (Sequentialsequential2.MAX_LIGHTNESS - Sequentialsequential2.MIN_LIGHTNESS)
							* Math.sin(Math.toRadians(Sequentialsequential2.THETA))
							/ Math.tan(Math.toRadians(Sequentialsequential2.BETA));
				}

				// a = 0;
				// b = 0;
				// assigning the lightness according to the matrix index
				L = lightness[(i + j)];

				// creating colors
				LABcolor c = new LABcolor(L, a, b);
				labcolor[i][j] = c;
			}
		}
	}
}