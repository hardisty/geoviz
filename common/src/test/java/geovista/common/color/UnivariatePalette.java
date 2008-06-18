/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.*/

package geovista.common.color;

import java.awt.Color;
import java.util.HashMap;

/**
 * The <CODE>UnivariatePalette</CODE> class.
 * 
 * @author Chris Weaver
 */
public final class UnivariatePalette implements Palette1D {

	public static final HashMap MAP = new HashMap();

	private final int type;
	private final String family;
	private final String name;
	private final int maxlength;
	private final int[] colors;
	private final int[][] suitability;

	public UnivariatePalette(int type, String family, String name,
			int maxlength, int[] colors, int[][] suitability) {
		this.type = type;
		this.family = family;
		this.name = name;

		this.maxlength = maxlength;
		this.colors = colors;
		this.suitability = suitability;

		String path = family + '.' + name;

		MAP.put(path, this);
	}

	public static void main(String[] args) {

		// UnivariatePalette pal = new
		// UnivariatePalette(UnivariatePalette.TYPE_DIVERGING,UnivariatePalette.)

	}

	public String getFamily() {
		return family;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	// implementing the UnivariatePalette interface
	public Color[] getColors(int length) {
		if ((length < 2) || (length >= maxlength)) {
			throw new IllegalArgumentException();
		}

		Color[] scheme = new Color[length];
		int[][] map = null;

		switch (type) {
		case TYPE_SEQUENTIAL:
			map = MAP_SEQUENTIAL;
			break;
		case TYPE_DIVERGING:
			map = MAP_DIVERGING;
			break;
		case TYPE_QUALITATIVE:
			map = MAP_QUALITATIVE;
			break;
		}

		if (map == null) {
			return null;
		}

		for (int i = 0; i < length; i++) {
			scheme[i] = new Color(colors[map[length][i]]);
		}

		return scheme;
	}

	// **********************************************************************
	// Public Methods
	// **********************************************************************

	public Color getColor(int length, int index) {
		if ((length < 2) || (length >= maxlength)) {
			return null;
		}

		if ((index < 0) || (index >= length)) {
			return null;
		}

		int[][] map = null;

		switch (type) {
		case TYPE_SEQUENTIAL:
			map = MAP_SEQUENTIAL;
			break;
		case TYPE_DIVERGING:
			map = MAP_DIVERGING;
			break;
		case TYPE_QUALITATIVE:
			map = MAP_QUALITATIVE;
			break;
		}

		if (map == null) {
			return null;
		}

		return new Color(colors[map[length][index]]);
	}

	public int getMaxLength() {
		return maxlength;
	}

	public int getSuitability(int count, int purpose) {
		try {
			return suitability[count][purpose];
		} catch (ArrayIndexOutOfBoundsException e) {
			return NONE;
		}
	}

	// **********************************************************************
	// Private Class Members
	// **********************************************************************

	private static final int[][] MAP_SEQUENTIAL = { { 5, 8 }, { 2, 5, 8 },
			{ 1, 4, 6, 9 }, { 1, 4, 6, 8, 10 }, { 0, 2, 5, 7, 10, 12 },
			{ 0, 2, 5, 6, 8, 10, 12 }, { 0, 2, 3, 5, 6, 9, 10, 12 },
			{ 0, 2, 3, 5, 6, 7, 9, 10, 12 }, };

	private static final int[][] MAP_DIVERGING = { { 4, 10 }, { 4, 7, 10 },
			{ 2, 5, 9, 12 }, { 2, 5, 7, 9, 12 }, { 1, 4, 6, 8, 10, 13 },
			{ 0, 2, 5, 7, 9, 12, 14 }, { 0, 2, 5, 7, 8, 10, 12, 14 },
			{ 0, 1, 3, 5, 7, 8, 10, 12, 14 },
			{ 0, 1, 3, 5, 7, 8, 9, 11, 13, 14 },
			{ 0, 1, 3, 5, 6, 7, 8, 9, 11, 13, 14 }, };

	private static final int[][] MAP_QUALITATIVE = { { 0, 1 }, { 0, 1, 2 },
			{ 0, 1, 2, 3 }, { 0, 1, 2, 3, 4 }, { 0, 1, 2, 3, 4, 5 },
			{ 0, 1, 2, 3, 4, 5, 6 }, { 0, 1, 2, 3, 4, 5, 6, 7 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }, };

	// **********************************************************************
	// Public Class Members (Palettes, Sequential)
	// **********************************************************************

	public static UnivariatePalette create(int type, String family,
			String name, int lmax, int[] colors, int[][] suitability) {
		return new UnivariatePalette(type, family, name, lmax, colors,
				suitability);
	}

	public static final UnivariatePalette YL_GN = create(TYPE_SEQUENTIAL,
			"Sequential", "YlGn", 9, new int[] { 0xffffe5, 0xffffcc, 0xf7fcb9,
					0xd9f0a3, 0xc2e699, 0xaddd8e, 0x78c679, 0x41ab5d, 0x31a354,
					0x238443, 0x006837, 0x005a32, 0x004529 }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, IFFY, IFFY, GOOD },
					{ GOOD, IFFY, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette YL_GN_BU = create(TYPE_SEQUENTIAL,
			"Sequential", "YlGnBu", 9, new int[] { 0xffffd9, 0xffffcc,
					0xedf8b1, 0xc7e9b4, 0xa1dab4, 0x7fcdbb, 0x41b6a9, 0x1d91c0,
					0x2c7fb8, 0x2260a8, 0x253494, 0x0c2c84, 0x081d58 },
			new int[][] { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, IFFY, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette GN_BU = create(TYPE_SEQUENTIAL,
			"Sequential", "GnBu", 9, new int[] { 0xf7fcf0, 0xf0f9e8, 0xe0f3db,
					0xccebc5, 0xbae4bc, 0xa8ddb5, 0x7bccc4, 0x4eb3d3, 0x43a2ca,
					0x2b8cbe, 0x0868ac, 0x08589e, 0x084081 }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, POOR, IFFY },
					{ GOOD, POOR, POOR, IFFY, POOR, POOR },
					{ GOOD, POOR, POOR, IFFY, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette BU_GN = create(TYPE_SEQUENTIAL,
			"Sequential", "BuGn", 9, new int[] { 0xf7fcfd, 0xedf8fb, 0xe5f5f9,
					0xccece6, 0xb2e2e2, 0x99d8c9, 0x66c2a4, 0x41ae76, 0x2ca25f,
					0x238b45, 0x006d2c, 0x005824, 0x00441b }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, IFFY, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, POOR, IFFY },
					{ GOOD, POOR, POOR, IFFY, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PU_BU_GN = create(TYPE_SEQUENTIAL,
			"Sequential", "PuBuGn", 9, new int[] { 0xfff7fb, 0xf6eff7,
					0xece2f0, 0xd0d1e6, 0xbdc9e1, 0xa6bddb, 0x74a9cf, 0x3690c0,
					0x1c9099, 0x02818a, 0x016c59, 0x016450, 0x014636 },
			new int[][] { { GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, IFFY, GOOD },
					{ GOOD, IFFY, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, IFFY, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PU_BU = create(TYPE_SEQUENTIAL,
			"Sequential", "PuBu", 9, new int[] { 0xfff7fb, 0xf1eef6, 0xece7f2,
					0xd0d1e6, 0xbdc9e1, 0xa6bddb, 0x67a9cf, 0x3690c0, 0x2b8cbe,
					0x0570b0, 0x045a8d, 0x034e7b, 0x023858 }, new int[][] {
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ IFFY, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, GOOD, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ POOR, GOOD, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette BU_PU = create(TYPE_SEQUENTIAL,
			"Sequential", "BuPu", 9, new int[] { 0xf7fcfd, 0xedf8fb, 0xe0ecf4,
					0xbfd3e6, 0xb3cde3, 0x9ebcda, 0x8c96c6, 0x8c6bb1, 0x8856a7,
					0x88419d, 0x810f7c, 0x6e016b, 0x4d004b }, new int[][] {
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, IFFY, GOOD },
					{ GOOD, IFFY, GOOD, IFFY, IFFY, IFFY },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ POOR, GOOD, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette RD_PU = create(TYPE_SEQUENTIAL,
			"Sequential", "RdPu", 9, new int[] { 0xfff7f3, 0xfeebe2, 0xfde0dd,
					0xfcc5c0, 0xfbb4b9, 0xfa9fb5, 0xf768a1, 0xdd3497, 0xc51b8a,
					0xae017e, 0x7a0177, 0x7a0177, 0x49006a }, new int[][] {
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, IFFY, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PU_RD = create(TYPE_SEQUENTIAL,
			"Sequential", "PuRd", 9, new int[] { 0xf7f4f9, 0xf1eef6, 0xe7e1ef,
					0xd4b9da, 0xd7b5d8, 0xc994c7, 0xdf65b0, 0xe7298a, 0xdd1c77,
					0xce1256, 0x980043, 0x91003f, 0x67001f }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette OR_RD = create(TYPE_SEQUENTIAL,
			"Sequential", "OrRd", 9, new int[] { 0xfff7ec, 0xfef0d9, 0xfee8c8,
					0xfdd49e, 0xfdcc8a, 0xfdbb84, 0xfc8d59, 0xef6548, 0xe34a33,
					0xd7301f, 0xb30000, 0x990000, 0x7f0000 }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette YL_OR_RD = create(TYPE_SEQUENTIAL,
			"Sequential", "YlOrRd", 9, new int[] { 0xffffcc, 0xffffb2,
					0xffeda0, 0xfed976, 0xfecc5c, 0xfeb24c, 0xfd8d3c, 0xfc4e2a,
					0xf03b20, 0xe31a1c, 0xbd0026, 0xb10026, 0x800026 },
			new int[][] { { GOOD, GOOD, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, POOR },
					{ GOOD, POOR, POOR, POOR, IFFY, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette YL_OR_BR = create(TYPE_SEQUENTIAL,
			"Sequential", "YlOrBr", 9, new int[] { 0xffffe5, 0xffffd4,
					0xfff7bc, 0xfee391, 0xfed98e, 0xfec44f, 0xfe9929, 0xec7014,
					0xd95f0e, 0xcc4c02, 0x993404, 0x8c2d04, 0x662506 },
			new int[][] { { GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PURPLES = create(TYPE_SEQUENTIAL,
			"Basic", "Purples", 9, new int[] { 0xfffbfd, 0xf2f0f7, 0xefedf5,
					0xdadaeb, 0xcbc9e2, 0xbcbddc, 0x9e9ac8, 0x807dba, 0x756bb1,
					0x6a51a3, 0x54278f, 0x4a1486, 0x3f007d }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, IFFY, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette BLUES = create(TYPE_SEQUENTIAL,
			"Basic", "Blues", 9, new int[] { 0xf7fbff, 0xeff3ff, 0xdeebf7,
					0xc6dbef, 0xbdd7e7, 0x9ecae1, 0x6baed6, 0x4292c6, 0x3182bd,
					0x2171b5, 0x08519c, 0x084594, 0x08306b }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, IFFY, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette GREENS = create(TYPE_SEQUENTIAL,
			"Basic", "Greens", 9, new int[] { 0xf7fcf5, 0xedf8e9, 0xe5f5e0,
					0xc7e9c0, 0xbae4b3, 0xa1d99b, 0x74c476, 0x41ab5d, 0x31a354,
					0x238b45, 0x006d2c, 0x005a32, 0x00441b }, new int[][] {
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, IFFY, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette ORANGES = create(TYPE_SEQUENTIAL,
			"Basic", "Oranges", 9, new int[] { 0xfff5eb, 0xfeedde, 0xfee6ce,
					0xfdd0a2, 0xfdbe85, 0xfdae6b, 0xfd8d3c, 0xf16913, 0xe6550d,
					0xd94801, 0xa63603, 0x8c2d04, 0x7f2704 }, new int[][] {
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, GOOD, IFFY },
					{ GOOD, IFFY, IFFY, GOOD, GOOD, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette REDS = create(TYPE_SEQUENTIAL,
			"Basic", "Reds", 9, new int[] { 0xfff5f0, 0xfee5d9, 0xfee0d2,
					0xfcbba1, 0xfcae91, 0xfc9272, 0xfb6a4a, 0xef3b2c, 0xde2d26,
					0xcb181d, 0xa50f15, 0x99000d, 0x67000d }, new int[][] {
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, IFFY, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, IFFY, POOR, IFFY, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette GRAYS = create(TYPE_SEQUENTIAL,
			"Basic", "Grays", 9, new int[] { 0xffffff, 0xf7f7f7, 0xf0f0f0,
					0xd9d9d9, 0xcccccc, 0xbdbdbd, 0x969696, 0x737373, 0x636363,
					0x525252, 0x252525, 0x252525, 0x000000 }, new int[][] {
					{ GOOD, GOOD, GOOD, POOR, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, POOR, GOOD, GOOD },
					{ GOOD, GOOD, IFFY, GOOD, IFFY, GOOD },
					{ GOOD, IFFY, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });

	// **********************************************************************
	// Public Class Members (Palettes, Diverging)
	// **********************************************************************

	public static final UnivariatePalette PU_OR = create(TYPE_DIVERGING,
			"Diverging", "PuOr", 11,
			new int[] { 0x7f3b08, 0xb35806, 0xe66101, 0xe08214, 0xf1a340,
					0xfdb863, 0xfee0b6, 0xf7f7f7, 0xd8daeb, 0xb2abd2, 0x998ec3,
					0x8073ac, 0x5e3c99, 0x542788, 0x2d004b }, new int[][] {
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, GOOD, POOR },
					{ GOOD, POOR, IFFY, POOR, IFFY, IFFY },
					{ GOOD, POOR, IFFY, POOR, IFFY, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette BR_BG = create(TYPE_DIVERGING,
			"Diverging", "BrBG", 11,
			new int[] { 0x543005, 0x8c510a, 0xa6611a, 0xbf812d, 0xd8b365,
					0xdfc27d, 0xf6e8c3, 0xf5f5f5, 0xc7eae5, 0x80cdc1, 0x5ab4ac,
					0x35978f, 0x018571, 0x01665e, 0x003c30 }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PR_GN = create(TYPE_DIVERGING,
			"Diverging", "PRGn", 11,
			new int[] { 0x40004b, 0x762a83, 0x7b3294, 0x9970ab, 0xaf8dc3,
					0xc2a5cf, 0xe7d4e8, 0xf7f7f7, 0xd9f0d3, 0xa6dba0, 0x7fbf7b,
					0x5aae61, 0x008837, 0x1b7837, 0x00441b }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PI_YG = create(TYPE_DIVERGING,
			"Diverging", "PiYG", 11,
			new int[] { 0x8e0152, 0xc51b7d, 0xd01c8b, 0xde77ae, 0xe9a3c9,
					0xf1b6da, 0xfde0ef, 0xf7f7f7, 0xe6f5d0, 0xb8e186, 0xa1d76a,
					0x7fbc41, 0x4dac26, 0x4d9221, 0x276419 }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, IFFY, POOR, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette RD_BU = create(TYPE_DIVERGING,
			"Diverging", "RdBu", 11,
			new int[] { 0x67001f, 0xb2182b, 0xca0020, 0xd6604d, 0xef8a62,
					0xf4a582, 0xfddbc7, 0xf7f7f7, 0xd1e5f0, 0x92c5de, 0x67a9cf,
					0x4393c3, 0x0571b0, 0x2166ac, 0x053061 }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, GOOD, IFFY },
					{ GOOD, POOR, POOR, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette RD_GY = create(TYPE_DIVERGING,
			"Diverging", "RdGy", 11,
			new int[] { 0x67001f, 0xb2182b, 0xca0020, 0xd6604d, 0xef8a62,
					0xf4a582, 0xfddbc7, 0xffffff, 0xe0e0e0, 0xbababa, 0x999999,
					0x878787, 0x404040, 0x4d4d4d, 0x1a1a1a }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, IFFY, IFFY, GOOD, IFFY },
					{ GOOD, POOR, IFFY, GOOD, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, POOR },
					{ GOOD, POOR, POOR, POOR, IFFY, POOR }, });
	public static final UnivariatePalette RD_YL_BU = create(TYPE_DIVERGING,
			"Diverging", "RdYlBu", 11, new int[] { 0xa50026, 0xd73027,
					0xd7191c, 0xf46d43, 0xfc8d59, 0xfdae61, 0xfee090, 0xffffbf,
					0xe0f3f8, 0xabd9e9, 0x91bfdb, 0x74add1, 0x2c7bb6, 0x4575b4,
					0x313695 }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, IFFY },
					{ GOOD, POOR, POOR, POOR, IFFY, POOR }, });
	public static final UnivariatePalette SPECTRAL = create(TYPE_DIVERGING,
			"Diverging", "Spectral", 11, new int[] { 0x9e0142, 0xd53e4f,
					0xd7191c, 0xf46d43, 0xfc8d59, 0xfdae61, 0xfee08b, 0xffffbf,
					0xe6f598, 0xabdda4, 0x99d594, 0x66c2a5, 0x2b83ba, 0x3288bd,
					0x5e4fa2 }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, IFFY, POOR, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, POOR, POOR, IFFY, POOR },
					{ POOR, POOR, POOR, POOR, IFFY, POOR },
					{ POOR, POOR, POOR, POOR, IFFY, POOR }, });
	public static final UnivariatePalette RD_YL_GN = create(TYPE_DIVERGING,
			"Diverging", "RdYlGn", 11, new int[] { 0xa50026, 0xd73027,
					0xd7191c, 0xf46d43, 0xfc8d59, 0xfdae61, 0xfee08b, 0xffffbf,
					0xd9ef8b, 0xa6d96a, 0x91cf60, 0x66bd63, 0x1a9641, 0x1a9850,
					0x006837 }, new int[][] {
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, POOR, POOR, IFFY, IFFY },
					{ POOR, POOR, POOR, POOR, POOR, POOR },
					{ POOR, POOR, POOR, POOR, POOR, POOR },
					{ POOR, POOR, POOR, POOR, POOR, POOR }, });

	// **********************************************************************
	// Public Class Members (Palettes, Qualitative)
	// **********************************************************************

	public static final UnivariatePalette SET1 = create(TYPE_QUALITATIVE,
			"Qualitative", "Set1", 8, new int[] { 0xe41a1c, 0x377eb8, 0x4daf4a,
					0x984ea3, 0xff7f00, 0xffff33, 0xa65628, 0xf781bf, 0x999999,
					0xffffff, 0xffffff, 0xffffff }, new int[][] {
					{ GOOD, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD }, });
	public static final UnivariatePalette PASTEL1 = create(TYPE_QUALITATIVE,
			"Qualitative", "Pastel1", 8, new int[] { 0xfbb4ae, 0xb3cde3,
					0xccebc5, 0xdecbe4, 0xfed9a6, 0xffffcc, 0xe5d8bd, 0xfddaec,
					0xf2f2f2, 0xffffff, 0xffffff, 0xffffff }, new int[][] {
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, IFFY, IFFY, GOOD, IFFY },
					{ POOR, POOR, POOR, POOR, IFFY, IFFY },
					{ POOR, POOR, POOR, POOR, IFFY, IFFY },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD }, });
	public static final UnivariatePalette SET2 = create(TYPE_QUALITATIVE,
			"Qualitative", "Set2", 8, new int[] { 0x66c2a5, 0xfc8d62, 0x8da0cb,
					0xe78ac3, 0xa6d854, 0xffd92f, 0xe5c494, 0xb3b3b3, 0xffffff,
					0xffffff, 0xffffff, 0xffffff }, new int[][] {
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD }, });
	public static final UnivariatePalette PASTEL2 = create(TYPE_QUALITATIVE,
			"Qualitative", "Pastel2", 8, new int[] { 0xb3e2cd, 0xfdcdac,
					0xcbd5e8, 0xf4cae4, 0xe6f5c9, 0xfff2ae, 0xf1e2cc, 0xcccccc,
					0xffffff, 0xffffff, 0xffffff, 0xffffff }, new int[][] {
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ GOOD, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, POOR, POOR, IFFY, GOOD },
					{ POOR, POOR, POOR, POOR, POOR, POOR },
					{ POOR, POOR, POOR, POOR, POOR, POOR },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD }, });
	public static final UnivariatePalette DARK2 = create(TYPE_QUALITATIVE,
			"Qualitative", "Dark2", 8, new int[] { 0x1b9e77, 0xd95f02,
					0x7570b3, 0xe7298a, 0x66a61e, 0xe6ab02, 0xa6761d, 0x666666,
					0xffffff, 0xffffff, 0xffffff, 0xffffff }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, POOR, POOR, POOR, POOR, IFFY },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD }, });
	public static final UnivariatePalette SET3 = create(TYPE_QUALITATIVE,
			"Qualitative", "Set3", 8, new int[] { 0x8dd377, 0xffffb3, 0xbebada,
					0xfb8072, 0x80b1d3, 0xfdb462, 0xb3de69, 0xfccde5, 0xd9d9d9,
					0xbc80bd, 0xccebc5, 0xffed6f }, new int[][] {
					{ GOOD, IFFY, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, IFFY, GOOD, IFFY, GOOD, IFFY },
					{ GOOD, IFFY, GOOD, IFFY, GOOD, IFFY },
					{ POOR, IFFY, GOOD, IFFY, GOOD, IFFY },
					{ POOR, IFFY, GOOD, IFFY, GOOD, IFFY },
					{ POOR, IFFY, GOOD, IFFY, IFFY, IFFY },
					{ POOR, IFFY, GOOD, IFFY, IFFY, GOOD },
					{ POOR, IFFY, IFFY, IFFY, IFFY, IFFY },
					{ POOR, POOR, IFFY, POOR, IFFY, IFFY },
					{ POOR, POOR, POOR, POOR, GOOD, IFFY },
					{ POOR, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette PAIRED = create(TYPE_QUALITATIVE,
			"Qualitative", "Paired", 8, new int[] { 0xa6cee3, 0x1f78b4,
					0xb2df8a, 0x33a02c, 0xfb9a99, 0xe31a1c, 0xfdbf6f, 0xff7f00,
					0xcab2d6, 0x6a3d9a, 0xffff99, 0xb15928 }, new int[][] {
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, POOR },
					{ GOOD, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, GOOD, GOOD, IFFY },
					{ POOR, POOR, GOOD, IFFY, GOOD, POOR },
					{ POOR, POOR, POOR, POOR, POOR, POOR }, });
	public static final UnivariatePalette ACCENTS = create(TYPE_QUALITATIVE,
			"Qualitative", "Accents", 8, new int[] { 0x7fc97f, 0xbeaed4,
					0xfdc086, 0xffff99, 0x386cb0, 0xf0027f, 0xbf5b17, 0x666666,
					0xffffff, 0xffffff, 0xffffff, 0xffffff }, new int[][] {
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ POOR, IFFY, GOOD, GOOD, GOOD, IFFY },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD },
					{ GOOD, GOOD, GOOD, GOOD, GOOD, GOOD }, });

	// **********************************************************************
	// Public Class Members (Palette Families)
	// **********************************************************************

	public static final String[] FAMILIES = { "Basic", "Diverging",
			"Qualitative", "Sequential", };

	public static final UnivariatePalette[][] PALETTES = {
			{ GRAYS, PURPLES, BLUES, GREENS, ORANGES, REDS, },
			{ PU_OR, BR_BG, PR_GN, PI_YG, RD_BU, RD_GY, RD_YL_BU, RD_YL_GN,
					SPECTRAL, },
			{ SET1, SET2, SET3, PASTEL1, PASTEL2, DARK2, PAIRED, ACCENTS, },
			{ YL_GN, YL_GN_BU, GN_BU, BU_GN, PU_BU_GN, PU_BU, BU_PU, RD_PU,
					PU_RD, OR_RD, YL_OR_RD, YL_OR_BR, }, };
}

// ******************************************************************************
