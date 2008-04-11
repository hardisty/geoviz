package geovista.colorbrewer;

/**
 * GeoVISTA Center (Penn State, Dept. of Geography) Java source file for the
 * class SchemeSuitability Copyright (c), 2004, GeoVISTA Center All Rights
 * Reserved. Original Author: Biliang Zhou
 * 
 * 
 */

public class SchemeSuitability {

	public SchemeSuitability() {
	}

	public static final int GOOD = 3;
	public static final int DOUBTFUL = 2;
	public static final int BAD = 1;

	/*
	 * ////////////////////////////////////////////////////////////////////////////
	 * This is the suitability information of each of the schemes: 3 stands for
	 * the fact that this color scheme is good for this purpose; 2 stands for
	 * the fact that it is doubtful whether this scheme is good; 1 stands for
	 * the fact that this scheme is not suitable for this purpose.
	 * 
	 * The order of the purposes:
	 * 
	 * ColorBlind, PhotoCopy, LCD Projector, LCD, CRT, Color Printing
	 * 
	 * 
	 * //////////////////////Sequential
	 * Schemes////////////////////////////////////
	 */
	public static int[][] YlGn = { { GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] YlGnBu = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, DOUBTFUL, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] GnBu = { { GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, BAD, BAD },
			{ GOOD, BAD, BAD, DOUBTFUL, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] BuGn = { { GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] PuBuGn = {
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] PuBu = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ DOUBTFUL, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ BAD, GOOD, BAD, BAD, BAD, BAD } };

	public static int[][] BuPu = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ BAD, GOOD, BAD, BAD, BAD, BAD } };

	public static int[][] RdPu = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] PuRd = { { GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] OrRd = { { GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] YlOrRd = {
			{ GOOD, GOOD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, BAD },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] YlOrBr = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Purples = {
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Blues = { { GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Greens = {
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Oranges = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, GOOD, DOUBTFUL },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Reds = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, DOUBTFUL, BAD, DOUBTFUL, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Grays = { { GOOD, GOOD, GOOD, BAD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, BAD, GOOD, GOOD },
			{ GOOD, GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD },
			{ GOOD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	// //////////////////////////////////Diverging
	// Schemes/////////////////////////

	public static int[][] PuOr = { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, GOOD, BAD },
			{ GOOD, BAD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, BAD, DOUBTFUL, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] BrBG = { { GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] PRGn = { { GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] PiYG = { { GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] RdBu = { { GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] RdGy = { { GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, BAD },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, BAD } };

	public static int[][] RdYlBu = { { GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, DOUBTFUL, BAD } };

	public static int[][] Spectral = {
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, BAD },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, BAD },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, BAD } };

	public static int[][] RdYlGn = { { BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, BAD, BAD }, { BAD, BAD, BAD, BAD, BAD, BAD },
			{ BAD, BAD, BAD, BAD, BAD, BAD } };

	// //////////////////Qualitative
	// Schemes///////////////////////////////////////

	public static int[][] Set1 = {
			{ GOOD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD } };

	public static int[][] Pastel1 = {
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, DOUBTFUL },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD } };

	public static int[][] Set2 = {
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD } };

	public static int[][] Pastel2 = {
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ GOOD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, DOUBTFUL, GOOD },
			{ BAD, BAD, BAD, BAD, BAD, BAD }, { BAD, BAD, BAD, BAD, BAD, BAD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD } };

	public static int[][] Dark2 = { { GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, BAD, BAD, BAD, BAD, DOUBTFUL },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD } };

	public static int[][] Set3 = {
			{ GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ GOOD, DOUBTFUL, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, DOUBTFUL, DOUBTFUL, GOOD },
			{ BAD, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, DOUBTFUL, BAD, DOUBTFUL, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, GOOD, DOUBTFUL },
			{ BAD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Paired = { { GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, BAD },
			{ GOOD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, BAD, GOOD, DOUBTFUL, GOOD, BAD },
			{ BAD, BAD, BAD, BAD, BAD, BAD } };

	public static int[][] Accents = {
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ BAD, DOUBTFUL, GOOD, GOOD, GOOD, DOUBTFUL },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
			{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD } };

	public static int[] Outofbound = { BAD, BAD, BAD, BAD, BAD, BAD };

	public int[] getSuitability(String schemename, int numberofClasses) {
		// create an array to store the suitability information, length is 6
		// because there are six suitability concerns
		int[] suitability = new int[6];
		// if numberofClasses is within 9, get the correct scheme according to
		// the schemename. numberofClasses - 1 because the array starts from 2
		if (schemename.equals("YlGn") || schemename.equals("YlGnBu")
				|| schemename.equals("GnBu") || schemename == "BuGn"
				|| schemename == "PuBuGn" || schemename == "PuBu"
				|| schemename == "BuPu" || schemename == "RdPu"
				|| schemename == "PuRd" || schemename == "OrRd"
				|| schemename == "YlOrRd" || schemename == "YlOrBr"
				|| schemename == "Purples" || schemename == "Blues"
				|| schemename == "Greens" || schemename == "Oranges"
				|| schemename == "Reds" || schemename == "Grays") {
			if (numberofClasses <= 9) {
				if ("YlGn" == schemename) {
					suitability = SchemeSuitability.YlGn[numberofClasses - 2];
				}
				if ("YlGnBu" == schemename) {
					suitability = SchemeSuitability.YlGnBu[numberofClasses - 2];
				}
				if ("GnBu" == schemename) {
					suitability = SchemeSuitability.GnBu[numberofClasses - 2];
				}
				if ("BuGn" == schemename) {
					suitability = SchemeSuitability.BuGn[numberofClasses - 2];
				}
				if ("PuBuGn" == schemename) {
					suitability = SchemeSuitability.PuBuGn[numberofClasses - 2];
				}
				if ("PuBu" == schemename) {
					suitability = SchemeSuitability.PuBu[numberofClasses - 2];
				}
				if ("BuPu" == schemename) {
					suitability = SchemeSuitability.BuPu[numberofClasses - 2];
				}
				if ("RdPu" == schemename) {
					suitability = SchemeSuitability.RdPu[numberofClasses - 2];
				}
				if ("PuRd" == schemename) {
					suitability = SchemeSuitability.PuRd[numberofClasses - 2];
				}
				if ("OrRd" == schemename) {
					suitability = SchemeSuitability.OrRd[numberofClasses - 2];
				}
				if ("YlOrRd" == schemename) {
					suitability = SchemeSuitability.YlOrRd[numberofClasses - 2];
				}
				if ("YlOrBr" == schemename) {
					suitability = SchemeSuitability.YlOrBr[numberofClasses - 2];
				}
				if ("Purples" == schemename) {
					suitability = SchemeSuitability.Purples[numberofClasses - 2];
				}
				if ("Blues" == schemename) {
					suitability = SchemeSuitability.Blues[numberofClasses - 2];
				}
				if ("Greens" == schemename) {
					suitability = SchemeSuitability.Greens[numberofClasses - 2];
				}
				if ("Oranges" == schemename) {
					suitability = SchemeSuitability.Oranges[numberofClasses - 2];
				}
				if ("Reds" == schemename) {
					suitability = SchemeSuitability.Reds[numberofClasses - 2];
				}
				if ("Grays" == schemename) {
					suitability = SchemeSuitability.Grays[numberofClasses - 2];
				}
			} else {
				suitability = SchemeSuitability.Outofbound;
			}
		}

		if (schemename == "PuOr" || schemename == "BrBG"
				|| schemename == "PRGn" || schemename == "PiYG"
				|| schemename == "RdBu" || schemename == "RdGy"
				|| schemename == "RdYlBu" || schemename == "Spectral"
				|| schemename == "RdYlGn") {
			if (numberofClasses <= 11) {
				if ("PuOr" == schemename) {
					suitability = SchemeSuitability.PuOr[numberofClasses - 2];
				}
				if ("BrBG" == schemename) {
					suitability = SchemeSuitability.BrBG[numberofClasses - 2];
				}
				if ("PRGn" == schemename) {
					suitability = SchemeSuitability.PRGn[numberofClasses - 2];
				}
				if ("PiYG" == schemename) {
					suitability = SchemeSuitability.PiYG[numberofClasses - 2];
				}
				if ("RdBu" == schemename) {
					suitability = SchemeSuitability.RdBu[numberofClasses - 2];
				}
				if ("RdGy" == schemename) {
					suitability = SchemeSuitability.RdGy[numberofClasses - 2];
				}
				if ("RdYlBu" == schemename) {
					suitability = SchemeSuitability.RdYlBu[numberofClasses - 2];
				}
				if ("Spectral" == schemename) {
					suitability = SchemeSuitability.Spectral[numberofClasses - 2];
				}
				if ("RdYlGn" == schemename) {
					suitability = SchemeSuitability.RdYlGn[numberofClasses - 2];
				}
			} else {
				suitability = SchemeSuitability.Outofbound;
			}
		}

		return suitability;
	}

}