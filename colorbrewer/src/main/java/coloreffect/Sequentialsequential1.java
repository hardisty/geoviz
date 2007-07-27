package coloreffect;

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

public class Sequentialsequential1 {
	protected final static Logger logger = Logger.getLogger(Sequentialsequential1.class.getName());
	// the angle from the Red-Green axis
	private static final double theta = 90;

	// the angle that control the shape of the diamond
	private static final double alhpa = 60;

	// the max & min lightness of the color schemes
	private static final double maxLightness = 100;

	private static final double minLightness = 0;

	private int numberoflightnessSteps;

	private double lightnessStep;

	public LABcolor[][] labcolor;

	public Sequentialsequential1(int row, int column) {

		// creating the labcolor double array to store the values in LAB format
		this.labcolor = new LABcolor[row][column];

		// calculating the number of lightnessSteps
		this.numberoflightnessSteps = row * 2 - 1;

		// creating a lightness array to store the lightness of each level
		double[] lightness = new double[this.numberoflightnessSteps];

		// the first element take on the maxlightness
		lightness[0] = Sequentialsequential1.maxLightness;

		// the last element take on the minlightness
		lightness[this.numberoflightnessSteps - 1] = Sequentialsequential1.minLightness;

		// calculating the span of the lightnessstep
		this.lightnessStep = (Sequentialsequential1.maxLightness - Sequentialsequential1.minLightness)
				/ (double) (this.numberoflightnessSteps - 1);

		// assigning lightness to each level
		for (int i = 0; i < this.numberoflightnessSteps; i++) {
			lightness[i] = Sequentialsequential1.maxLightness - i
					* this.lightnessStep;
			logger.finest("" + i);
			logger.finest("" + lightness[i] + '\n');
		}

		// declaring the variable to use in calculating the colors
		double L;
		double a = 0;
		double b = 0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				// calculating the a & b according to the matrix

				a = Math.cos(Math.toRadians(Sequentialsequential1.theta))
						* (i - j)
						* (Sequentialsequential1.maxLightness - Sequentialsequential1.minLightness)
						* Math.tan(Math.toRadians(Sequentialsequential1.alhpa))
						/ (2 * row);

				b = Math.sin(Math.toRadians(Sequentialsequential1.theta))
						* (i - j)
						* (Sequentialsequential1.maxLightness - Sequentialsequential1.minLightness)
						* Math.tan(Math.toRadians(Sequentialsequential1.alhpa))
						/ (2 * column);

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

	public Sequentialsequential1(int vclass, int hclass, int maxlightness,
			int minlightness, int alpha, int startingcolor) {

		// creating the labcolor double array to store the values in LAB format
		this.labcolor = new LABcolor[vclass][hclass];

		// calculating the number of lightnessSteps
		this.numberoflightnessSteps = vclass * 2 - 1;

		// creating a lightness array to store the lightness of each level
		double[] lightness = new double[this.numberoflightnessSteps];

		// the first element take on the maxlightness
		lightness[0] = maxlightness;

		// the last element take on the minlightness
		lightness[this.numberoflightnessSteps - 1] = minlightness;

		// calculating the span of the lightnessstep
		this.lightnessStep = (maxlightness - minlightness)
				/ (double) (this.numberoflightnessSteps - 1);

		// assigning lightness to each level
		for (int i = 0; i < this.numberoflightnessSteps; i++) {
			lightness[i] = maxlightness - i * minlightness;
		}

		// declaring the variable to use in calculating the colors
		double L;
		double a = 0;
		double b = 0;
		for (int i = 0; i < vclass; i++) {
			for (int j = 0; j < hclass; j++) {
				// calculating the a & b according to the matrix

				a = Math.cos(Math.toRadians(startingcolor)) * (i - j)
						* (maxlightness - minlightness)
						* Math.tan(Math.toRadians(alhpa)) / (2 * vclass);

				b = Math.sin(Math.toRadians(startingcolor)) * (i - j)
						* (maxlightness - minlightness)
						* Math.tan(Math.toRadians(alhpa)) / (2 * hclass);

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