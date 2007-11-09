package geovista.colorbrewer;
import java.awt.Color;
import java.util.logging.Logger;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class OriginalColor
 Copyright (c), 2004, 2005 GeoVISTA Center under the LPGL
 Original Author: Biliang Zhou
 * @version 1.0
 */

public class OriginalColor {
    /* Added by Gary L. */
    public final static String SET1 = "Set1";
    public final static String PASTEL1 = "Pastel1";
    public final static String SET2 = "Set2";
    public final static String PASTEL2 = "Pastel2";
    public final static String DARK2 = "Dark2";
    public final static String SET3 = "Set3";
    public final static String PAIRED = "Paired";
    public final static String ACCENTS = "Accents";

  private int numberofRound = 0;

  private int currentRound = 0;
  protected final static Logger logger = Logger.getLogger(OriginalColor.class.getName());
  //Sequential schemes: 18 ones, all support 9 colors

  public static int[][]  YlGn = {
    {255, 255, 229},
    {255, 255, 204},
    {247, 252, 185},
    {217, 240, 163},
    {194, 230, 153},
    {173, 221, 142},
    {120, 198, 121},
    { 65, 171,  93},
    { 49, 163,  84},
    { 35, 132,  67},
    {  0, 104,  55},
    {  0,  90,  50},
    {  0,  69,  41},
    };

  public static int[][]  YlGnBu = {
    {255, 255, 217},
    {255, 255, 204},
    {237, 248, 177},
    {199, 233, 180},
    {161, 218, 180},
    {127, 205, 187},
    { 65, 182, 169},
    { 29, 145, 192},
    { 44, 127, 184},
    { 34,  96, 168},
    { 37,  52, 148},
    { 12,  44, 132},
    {  8,  29,  88},
    };

  public static int[][]  GnBu = {
    {247, 252, 240},
    {240, 249, 232},
    {224, 243, 219},
    {204, 235, 197},
    {186, 228, 188},
    {168, 221, 181},
    {123, 204, 196},
    { 78, 179, 211},
    { 67, 162, 202},
    { 43, 140, 190},
    {  8, 104, 172},
    {  8,  88, 158},
    {  8,  64, 129},
    };

  public static int[][]  BuGn = {
    {247, 252, 253},
    {237, 248, 251},
    {229, 245, 249},
    {204, 236, 230},
    {178, 226, 226},
    {153, 216, 201},
    {102, 194, 164},
    { 65, 174, 118},
    { 44, 162,  95},
    { 35, 139,  69},
    {  0, 109,  44},
    {  0,  88,  36},
    {  0,  68,  27},
    };

  public static int[][]  PuBuGn = {
    {255, 247, 251},
    {246, 239, 247},
    {236, 226, 240},
    {208, 209, 230},
    {189, 201, 225},
    {166, 189, 219},
    {116, 169, 207},
    { 54, 144, 192},
    { 28, 144, 153},
    {  2, 129, 138},
    {  1, 108,  89},
    {  1, 100,  80},
    {  1,  70,  54},
    };

  public static int[][]  PuBu = {
    {255, 247, 251},
    {241, 238, 246},
    {236, 231, 242},
    {208, 209, 230},
    {189, 201, 225},
    {166, 189, 219},
    {103, 169, 207},
    { 54, 144, 192},
    { 43, 140, 190},
    {  5, 112, 176},
    {  4,  90, 141},
    {  3,  78, 123},
    {  2,  56,  88},
    };

  public static int[][]  BuPu = {
    {247, 252, 253},
    {237, 248, 251},
    {224, 236, 244},
    {191, 211, 230},
    {179, 205, 227},
    {158, 188, 218},
    {140, 150, 198},
    {140, 107, 177},
    {136,  86, 167},
    {136,  65, 157},
    {129,  15, 124},
    {110,   1, 107},
    { 77,   0,  75},
    };

  public static int[][]  RdPu = {
    {255, 247, 243},
    {254, 235, 226},
    {253, 224, 221},
    {252, 197, 192},
    {251, 180, 185},
    {250, 159, 181},
    {247, 104, 161},
    {221,  52, 151},
    {197,  27, 138},
    {174,   1, 126},
    {122,   1, 119},
    {122,   1, 119},
    { 73,   0, 106},
    };

  public static int[][]  PuRd = {
    {247, 244, 249},
    {241, 238, 246},
    {231, 225, 239},
    {212, 185, 218},
    {215, 181, 216},
    {201, 148, 199},
    {223, 101, 176},
    {231,  41, 138},
    {221,  28, 119},
    {206,  18,  86},
    {152,   0,  67},
    {145,   0,  63},
    {103,   0,  31},
    };

  public static int[][]  OrRd = {
    {255, 247, 236},
    {254, 240, 217},
    {254, 232, 200},
    {253, 212, 158},
    {253, 204, 138},
    {253, 187, 132},
    {252, 141,  89},
    {239, 101,  72},
    {227,  74,  51},
    {215,  48,  31},
    {179,   0,   0},
    {153,   0,   0},
    {127,   0,   0},
    };

  public static int[][]  YlOrRd = {
    {255, 255, 204},
    {255, 255, 178},
    {255, 237, 160},
    {254, 217, 118},
    {254, 204,  92},
    {254, 178,  76},
    {253, 141,  60},
    {252,  78,  42},
    {240,  59,  32},
    {227,  26,  28},
    {189,   0,  38},
    {177,   0,  38},
    {128,   0,  38},
    };

  public static int[][]  YlOrBr = {
    {255, 255, 229},
    {255, 255, 212},
    {255, 247, 188},
    {254, 227, 145},
    {254, 217, 142},
    {254, 196,  79},
    {254, 153,  41},
    {236, 112,  20},
    {217,  95,  14},
    {204,  76,   2},
    {153,  52,   4},
    {140,  45,   4},
    {102,  37,   6},
    };

  public static int[][]  Purples = {
    {255, 251, 253},
    {242, 240, 247},
    {239, 237, 245},
    {218, 218, 235},
    {203, 201, 226},
    {188, 189, 220},
    {158, 154, 200},
    {128, 125, 186},
    {117, 107, 177},
    {106,  81, 163},
    { 84,  39, 143},
    { 74,  20, 134},
    { 63,   0, 125},
    };

  public static int[][]  Blues = {
    {247, 251, 255},
    {239, 243, 255},
    {222, 235, 247},
    {198, 219, 239},
    {189, 215, 231},
    {158, 202, 225},
    {107, 174, 214},
    { 66, 146, 198},
    { 49, 130, 189},
    { 33, 113, 181},
    {  8,  81, 156},
    {  8,  69, 148},
    {  8,  48, 107},
    };

  public static int[][]  Greens = {
    {247, 252, 245},
    {237, 248, 233},
    {229, 245, 224},
    {199, 233, 192},
    {186, 228, 179},
    {161, 217, 155},
    {116, 196, 118},
    { 65, 171,  93},
    { 49, 163,  84},
    { 35, 139,  69},
    {  0, 109,  44},
    {  0,  90,  50},
    {  0,  68,  27},
    };

  public static int[][]  Oranges = {
    {255, 245, 235},
    {254, 237, 222},
    {254, 230, 206},
    {253, 208, 162},
    {253, 190, 133},
    {253, 174, 107},
    {253, 141,  60},
    {241, 105,  19},
    {230,  85,  13},
    {217,  72,   1},
    {166,  54,   3},
    {140,  45,   4},
    {127,  39,   4},
    };

  public static int[][]  Reds = {
    {255, 245, 240},
    {254, 229, 217},
    {254, 224, 210},
    {252, 187, 161},
    {252, 174, 145},
    {252, 146, 114},
    {251, 106,  74},
    {239,  59,  44},
    {222,  45,  38},
    {203,  24,  29},
    {165,  15,  21},
    {153,   0,  13},
    {103,   0,  13},
    };

  public static int[][]  Grays = {
    {255, 255, 255},
    {247, 247, 247},
    {240, 240, 240},
    {217, 217, 217},
    {204, 204, 204},
    {189, 189, 189},
    {150, 150, 150},
    {115, 115, 115},
    { 99,  99,  99},
    { 82,  82,  82},
    { 37,  37,  37},
    { 37,  37,  37},
    {  0,   0,   0},
    };

  //Diverging schemes: 9 ones, all support 11 colors

  public static int[][]  PuOr = {
    {127,  59,   8},
    {179,  88,   6},
    {230,  97,   1},
    {224, 130,  20},
    {241, 163,  64},
    {253, 184,  99},
    {254, 224, 182},
    {247, 247, 247},
    {216, 218, 235},
    {178, 171, 210},
    {153, 142, 195},
    {128, 115, 172},
    { 94,  60, 153},
    { 84,  39, 136},
    { 45,   0,  75},
    };

  public static int[][]  BrBG = {
    { 84,  48,   5},
    {140,  81,  10},
    {166,  97,  26},
    {191, 129,  45},
    {216, 179, 101},
    {223, 194, 125},
    {246, 232, 195},
    {245, 245, 245},
    {199, 234, 229},
    {128, 205, 193},
    { 90, 180, 172},
    { 53, 151, 143},
    {  1, 133, 113},
    {  1, 102,  94},
    {  0,  60,  48},
    };

  public static int[][]  PRGn = {
    { 64,   0,  75},
    {118,  42, 131},
    {123,  50, 148},
    {153, 112, 171},
    {175, 141, 195},
    {194, 165, 207},
    {231, 212, 232},
    {247, 247, 247},
    {217, 240, 211},
    {166, 219, 160},
    {127, 191, 123},
    { 90, 174,  97},
    {  0, 136,  55},
    { 27, 120,  55},
    {  0,  68,  27},
    };

  public static int[][]  PiYG = {
    {142,   1,  82},
    {197,  27, 125},
    {208,  28, 139},
    {222, 119, 174},
    {233, 163, 201},
    {241, 182, 218},
    {253, 224, 239},
    {247, 247, 247},
    {230, 245, 208},
    {184, 225, 134},
    {161, 215, 106},
    {127, 188,  65},
    { 77, 172,  38},
    { 77, 146,  33},
    { 39, 100,  25},
    };

  public static int[][]  RdBu = {
    {103,   0,  31},
    {178,  24,  43},
    {202,   0,  32},
    {214,  96,  77},
    {239, 138,  98},
    {244, 165, 130},
    {253, 219, 199},
    {247, 247, 247},
    {209, 229, 240},
    {146, 197, 222},
    {103, 169, 207},
    { 67, 147, 195},
    {  5, 113, 176},
    { 33, 102, 172},
    {  5,  48,  97},
    };

  public static int[][]  RdGy = {
    {103,   0,  31},
    {178,  24,  43},
    {202,   0,  32},
    {214,  96,  77},
    {239, 138,  98},
    {244, 165, 130},
    {253, 219, 199},
    {255, 255, 255},
    {224, 224, 224},
    {186, 186, 186},
    {153, 153, 153},
    {135, 135, 135},
    { 64,  64,  64},
    { 77,  77,  77},
    { 26,  26,  26},
    };

  public static int[][]  RdYlBu = {
    {165,   0,  38},
    {215,  48,  39},
    {215,  25,  28},
    {244, 109,  67},
    {252, 141,  89},
    {253, 174,  97},
    {254, 224, 144},
    {255, 255, 191},
    {224, 243, 248},
    {171, 217, 233},
    {145, 191, 219},
    {116, 173, 209},
    { 44, 123, 182},
    { 69, 117, 180},
    { 49,  54, 149},
    };

  public static int[][]  Spectral = {
    {158,   1,  66},
    {213,  62,  79},
    {215,  25,  28},
    {244, 109,  67},
    {252, 141,  89},
    {253, 174,  97},
    {254, 224, 139},
    {255, 255, 191},
    {230, 245, 152},
    {171, 221, 164},
    {153, 213, 148},
    {102, 194, 165},
    { 43, 131, 186},
    { 50, 136, 189},
    { 94,  79, 162},
    };

  public static int[][]  RdYlGn = {
    {165,   0,  38},
    {215,  48,  39},
    {215,  25,  28},
    {244, 109,  67},
    {252, 141,  89},
    {253, 174,  97},
    {254, 224, 139},
    {255, 255, 191},
    {217, 239, 139},
    {166, 217, 106},
    {145, 207,  96},
    {102, 189,  99},
    { 26, 150,  65},
    { 26, 152,  80},
    {  0, 104,  55},
    };

  //Qualitative schemes: 8 ones, 4 support 8 colors, 2 support 9 colors, and 2 support 12 colors (and all are manually interpolated to 12 colors)

  public static int[][]  Set1 = {
    {228,  26,  28},
    { 55, 126, 184},
    { 77, 175,	74},
    {152,  78, 163},
    {255, 127,   0},
    {255, 255,  51},
    {166,  86,	40},
    {247, 129, 191},
    {153, 153, 153},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    };

  public static int[][]  Pastel1 = {
    {251, 180, 174},
    {179, 205, 227},
    {204, 235, 197},
    {222, 203, 228},
    {254, 217, 166},
    {255, 255, 204},
    {229, 216, 189},
    {253, 218, 236},
    {242, 242, 242},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    };

  public static int[][]  Set2 = {
    {102, 194, 165},
    {252, 141,  98},
    {141, 160, 203},
    {231, 138, 195},
    {166, 216,  84},
    {255, 217,  47},
    {229, 196, 148},
    {179, 179, 179},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    };

  public static int[][]  Pastel2 = {
    {179, 226, 205},
    {253, 205, 172},
    {203, 213, 232},
    {244, 202, 228},
    {230, 245, 201},
    {255, 242, 174},
    {241, 226, 204},
    {204, 204, 204},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    };

  public static int[][]  Dark2 = {
    { 27, 158, 119},
    {217,  95,   2},
    {117, 112, 179},
    {231,  41, 138},
    {102, 166,  30},
    {230, 171,   2},
    {166, 118,  29},
    {102, 102, 102},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    };

  public static int[][]  Set3 = {
    {141, 211, 119},
    {255, 255, 179},
    {190, 186, 218},
    {251, 128, 114},
    {128, 177, 211},
    {253, 180,  98},
    {179, 222, 105},
    {252, 205, 229},
    {217, 217, 217},
    {188, 128, 189},
    {204, 235, 197},
    {255, 237, 111},
    };

  public static int[][]  Paired = {
    {166, 206, 227},
    { 31, 120, 180},
    {178, 223, 138},
    { 51, 160,  44},
    {251, 154, 153},
    {227,  26,  28},
    {253, 191, 111},
    {255, 127,   0},
    {202, 178, 214},
    {106,  61, 154},
    {255, 255, 153},
    {177,  89,  40},
    };

  public static int[][]  Accents = {
    {127, 201, 127},
    {190, 174, 212},
    {253, 192, 134},
    {255, 255, 153},
    { 56, 108, 176},
    {240,   2, 127},
    {191,  91,  23},
    {102, 102, 102},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    {255, 255, 255},
    };

  public OriginalColor() {
  }

  public int[][] makescheme(String schemename, int n){
    //create an array to store the scheme
    int[][] outputscheme = new int[n][3];

    //for sequential schemes, make for 2, 3, 4, 5, 6, 7, 8 and 9 classes
    if(schemename.equals("YlGn") || schemename.equals("YlGnBu")|| schemename.equals("GnBu")|| schemename.equals("BuGn")|| schemename.equals("PuBuGn")|| schemename.equals("PuBu")|| schemename.equals("BuPu")|| schemename.equals("RdPu")|| schemename.equals("PuRd")|| schemename.equals("OrRd")|| schemename.equals("YlOrRd")|| schemename.equals("YlOrBr")|| schemename.equals("Purples")|| schemename.equals("Blues")|| schemename.equals("Greens")|| schemename.equals("Oranges")|| schemename.equals("Reds")|| schemename.equals("Grays")){
      //create an array that corresponds to the schemename
      int[][] scheme = new int[13][3];
      //look for the correct scheme
      if("YlGn".equals(schemename)){
        scheme = OriginalColor.YlGn;
      }
      if("YlGnBu".equals(schemename)){
        scheme = OriginalColor.YlGnBu;
      }
      if("GnBu".equals(schemename)){
        scheme = OriginalColor.GnBu;
      }
      if("BuGn".equals(schemename)){
        scheme = OriginalColor.BuGn;
      }
      if("PuBuGn".equals(schemename)){
        scheme = OriginalColor.PuBuGn;
      }
      if("PuBu".equals(schemename)){
        scheme = OriginalColor.PuBu;
      }
      if("BuPu".equals(schemename)){
        scheme = OriginalColor.BuPu;
      }
      if("RdPu".equals(schemename)){
        scheme = OriginalColor.RdPu;
      }
      if("PuRd".equals(schemename)){
        scheme = OriginalColor.PuRd;
      }
      if("OrRd".equals(schemename)){
        scheme = OriginalColor.OrRd;
      }
      if("YlOrRd".equals(schemename)){
        scheme = OriginalColor.YlOrRd;
      }
      if("YlOrBr".equals(schemename)){
        scheme = OriginalColor.YlOrBr;
      }
      if("Purples".equals(schemename)){
        scheme = OriginalColor.Purples;
      }
      if("Blues".equals(schemename)){
        scheme = OriginalColor.Blues;
      }
      if("Greens".equals(schemename)){
        scheme = OriginalColor.Greens;
      }
      if("Oranges".equals(schemename)){
        scheme = OriginalColor.Oranges;
      }
      if("Reds".equals(schemename)){
        scheme = OriginalColor.Reds;
      }
      if("Grays".equals(schemename)){
        scheme = OriginalColor.Grays;
      }
      if(n == 2){
        outputscheme[0] = scheme[5];
        outputscheme[1] = scheme[8];
      }
      if(n == 3){
        outputscheme[0] = scheme[2];
        outputscheme[1] = scheme[5];
        outputscheme[2] = scheme[8];
      }
      if(n == 4){
        outputscheme[0] = scheme[1];
        outputscheme[1] = scheme[4];
        outputscheme[2] = scheme[6];
        outputscheme[3] = scheme[9];
      }
      if(n == 5){
        outputscheme[0] = scheme[1];
        outputscheme[1] = scheme[4];
        outputscheme[2] = scheme[6];
        outputscheme[3] = scheme[8];
        outputscheme[4] = scheme[10];
      }
      if(n == 6){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[2];
        outputscheme[2] = scheme[5];
        outputscheme[3] = scheme[7];
        outputscheme[4] = scheme[10];
        outputscheme[5] = scheme[12];
      }
      if(n == 7){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[2];
        outputscheme[2] = scheme[5];
        outputscheme[3] = scheme[6];
        outputscheme[4] = scheme[8];
        outputscheme[5] = scheme[10];
        outputscheme[6] = scheme[12];
      }
      if(n == 8){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[2];
        outputscheme[2] = scheme[3];
        outputscheme[3] = scheme[5];
        outputscheme[4] = scheme[6];
        outputscheme[5] = scheme[9];
        outputscheme[6] = scheme[10];
        outputscheme[7] = scheme[12];
      }
      if(n == 9){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[2];
        outputscheme[2] = scheme[3];
        outputscheme[3] = scheme[5];
        outputscheme[4] = scheme[6];
        outputscheme[5] = scheme[7];
        outputscheme[6] = scheme[9];
        outputscheme[7] = scheme[10];
        outputscheme[8] = scheme[12];
      }
    }

    //for diverging schemes, make for 2, 3, 4, 5, 6, 7, 8, 9, 10 and 11 classes
    if(schemename.equals("PuOr")|| schemename.equals("BrBG")|| schemename.equals("PRGn")|| schemename.equals("PiYG")|| schemename.equals("RdBu")|| schemename.equals("RdGy")|| schemename.equals("RdYlBu")|| schemename.equals("Spectral")|| schemename.equals("RdYlGn")){
      //create an array that corresponds to the schemename
      int[][] scheme = new int[15][3];
      //looking for the correct scheme
      if("PuOr".equals(schemename)){
        scheme = OriginalColor.PuOr;
      }
      if("BrBG".equals(schemename)){
        scheme = OriginalColor.BrBG;
      }
      if("PRGn".equals(schemename)){
        scheme = OriginalColor.PRGn;
      }
      if("PiYG".equals(schemename)){
        scheme = OriginalColor.PiYG;
      }
      if("RdBu".equals(schemename)){
        scheme = OriginalColor.RdBu;
      }
      if("RdGy".equals(schemename)){
        scheme = OriginalColor.RdGy;
      }
      if("RdYlBu".equals(schemename)){
        scheme = OriginalColor.RdYlBu;
      }
      if("Spectral".equals(schemename)){
        scheme = OriginalColor.Spectral;
      }
      if("RdYlGn".equals(schemename)){
        scheme = OriginalColor.RdYlGn;
      }
      if(n == 2){
        outputscheme[0] = scheme[4];
        outputscheme[1] = scheme[10];
      }
      if(n == 3){
        outputscheme[0] = scheme[4];
        outputscheme[1] = scheme[7];
        outputscheme[2] = scheme[10];
      }
      if(n == 4){
        outputscheme[0] = scheme[2];
        outputscheme[1] = scheme[5];
        outputscheme[2] = scheme[9];
        outputscheme[3] = scheme[12];
      }
      if(n == 5){
        outputscheme[0] = scheme[2];
        outputscheme[1] = scheme[5];
        outputscheme[2] = scheme[7];
        outputscheme[3] = scheme[9];
        outputscheme[4] = scheme[12];
      }
      if(n == 6){
        outputscheme[0] = scheme[1];
        outputscheme[1] = scheme[4];
        outputscheme[2] = scheme[6];
        outputscheme[3] = scheme[8];
        outputscheme[4] = scheme[10];
        outputscheme[5] = scheme[13];
      }
      if(n == 7){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[2];
        outputscheme[2] = scheme[5];
        outputscheme[3] = scheme[7];
        outputscheme[4] = scheme[9];
        outputscheme[5] = scheme[12];
        outputscheme[6] = scheme[14];
      }
      if(n == 8){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[2];
        outputscheme[2] = scheme[5];
        outputscheme[3] = scheme[7];
        outputscheme[4] = scheme[8];
        outputscheme[5] = scheme[10];
        outputscheme[6] = scheme[12];
        outputscheme[7] = scheme[14];
      }
      if(n == 9){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[3];
        outputscheme[3] = scheme[5];
        outputscheme[4] = scheme[7];
        outputscheme[5] = scheme[8];
        outputscheme[6] = scheme[10];
        outputscheme[7] = scheme[12];
        outputscheme[8] = scheme[14];
      }
      if(n == 10){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[3];
        outputscheme[3] = scheme[5];
        outputscheme[4] = scheme[7];
        outputscheme[5] = scheme[8];
        outputscheme[6] = scheme[9];
        outputscheme[7] = scheme[11];
        outputscheme[8] = scheme[13];
        outputscheme[8] = scheme[14];
      }
      if(n == 11){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[3];
        outputscheme[3] = scheme[5];
        outputscheme[4] = scheme[6];
        outputscheme[5] = scheme[7];
        outputscheme[6] = scheme[8];
        outputscheme[7] = scheme[9];
        outputscheme[8] = scheme[11];
        outputscheme[9] = scheme[13];
        outputscheme[10] = scheme[14];
      }
    }

    //for qualitative schemes, make for 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 classes
    if(schemename.equals("Set1")|| schemename.equals("Pastel1")|| schemename.equals("Set2")|| schemename.equals("Pastel2")|| schemename.equals("Dark2")|| schemename.equals("Set3")|| schemename.equals("Paired")|| schemename.equals("Accents")){
      //create an array that corresponds to the schemename
      int[][] scheme = new int[12][3];
      //looking for the correct scheme
      if("Set1".equals(schemename)){
        scheme = OriginalColor.Set1;
      }
      if("Pastel1".equals(schemename)){
        scheme = OriginalColor.Pastel1;
      }
      if("Set2".equals(schemename)){
        scheme = OriginalColor.Set2;
      }
      if("Pastel2".equals(schemename)){
        scheme = OriginalColor.Pastel2;
      }
      if("Dark2".equals(schemename)){
        scheme = OriginalColor.Dark2;
      }
      if("Set3".equals(schemename)){
        scheme = OriginalColor.Set3;
      }
      if("Paired".equals(schemename)){
        scheme = OriginalColor.Paired;
      }
      if("Accents".equals(schemename)){
        scheme = OriginalColor.Accents;
      }
      if(n == 2){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
      }
      if(n == 3){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
      }
      if(n == 4){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
      }
      if(n == 5){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
      }
      if(n == 6){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
      }
      if(n == 7){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
        outputscheme[6] = scheme[6];
      }
      if(n == 8){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
        outputscheme[6] = scheme[6];
        outputscheme[7] = scheme[7];
      }
      if(n == 9){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
        outputscheme[6] = scheme[6];
        outputscheme[7] = scheme[7];
        outputscheme[8] = scheme[8];
      }
      if(n == 10){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
        outputscheme[6] = scheme[6];
        outputscheme[7] = scheme[7];
        outputscheme[8] = scheme[8];
        outputscheme[9] = scheme[9];
      }
      if(n == 11){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
        outputscheme[6] = scheme[6];
        outputscheme[7] = scheme[7];
        outputscheme[8] = scheme[8];
        outputscheme[9] = scheme[9];
        outputscheme[10] = scheme[10];
      }
      if(n == 12){
        outputscheme[0] = scheme[0];
        outputscheme[1] = scheme[1];
        outputscheme[2] = scheme[2];
        outputscheme[3] = scheme[3];
        outputscheme[4] = scheme[4];
        outputscheme[5] = scheme[5];
        outputscheme[6] = scheme[6];
        outputscheme[7] = scheme[7];
        outputscheme[8] = scheme[8];
        outputscheme[9] = scheme[9];
        outputscheme[10] = scheme[10];
        outputscheme[11] = scheme[11];
      }
    }

    return outputscheme;
  }

  public int[][] linearInterpolate(String schemename, int numberofClasses, int color[][]){

    int[][] schemeforInterpolation = new int[2*numberofClasses + 1][3];
    int[][] interim = new int[2*numberofClasses + 1][3];

    int maxlengthRecommended = 0;

    if(schemename.equals("YlGn")|| schemename.equals("YlGnBu")|| schemename.equals("GnBu")|| schemename.equals("BuGn")|| schemename.equals("PuBuGn")|| schemename.equals("PuBu")|| schemename.equals("BuPu")|| schemename.equals("RdPu")|| schemename.equals("PuRd")|| schemename.equals("OrRd")|| schemename.equals("YlOrRd")|| schemename.equals("YlOrBr")|| schemename.equals("Purples")|| schemename.equals("Blues")|| schemename.equals("Greens")|| schemename.equals("Oranges")|| schemename.equals("Reds")|| schemename.equals("Grays")){
      maxlengthRecommended = 9;
    }

    if(schemename.equals("PuOr")|| schemename.equals("BrBG")|| schemename.equals("PRGn")|| schemename.equals("PiYG")|| schemename.equals("RdBu")|| schemename.equals("RdGy")|| schemename.equals("RdYlBu")|| schemename.equals("Spectral")|| schemename.equals("RdYlGn")){
      maxlengthRecommended = 11;
    }

    //if the numberofClasses is smaller than or equals to the maxlengthRecommended, make it the simple way
    if(numberofClasses <= maxlengthRecommended){

      //get the schemeforInterpolation
      schemeforInterpolation = this.makescheme(schemename, numberofClasses);
      //do linear interpolation between existing colorbrewer univariate color schemes
      for(int i = 0; i < (2*numberofClasses - 1); i ++){
        for(int j = 0; j < 3; j ++){
          //for even number of slots
        	if((i % 2) == 1){
            //get the correct colors into correct slots (no calculation needed, just moving them)
            color[i][j] = schemeforInterpolation[i/2][j];
          }
          else{
            //linearly calculate the colors between original ones
            color[i][j] = (schemeforInterpolation[(i - 1)/2][j] + schemeforInterpolation[(i + 1)/2][j])/2;

            //another method of interpolation
            //this.color[i][j] = (int)Math.sqrt((Math.pow(this.Cindy.YlGnBu1[numberofClasses][(i + 1)/2][j], 2) + Math.pow(this.Cindy.YlGnBu1[numberofClasses][(i - 1)/2][j], 2))/2);
          }
        }
      }
    }

    //if the numberofClasses is greater than the maxlengthRecommended, use the current color to interpolate
    else{
      //read the current this.color into the interim
      for(int i = 0; i < numberofClasses + 1; i ++){
        for(int j = 0; j < 3; j ++){
          interim[i][j] = color[i][j];
        }
      }
      //use the interim file to interpolate
      for(int i = 0; i < (2*numberofClasses - 1); i ++){
        for(int j = 0; j < 3; j ++){
          if((i % 2) == 0){
            color[i][j] = interim[i/2][j];
          }
          else{
            color[i][j] = (interim[(i + 1)/2][j] + interim[(i - 1)/2][j])/2;
          }
        }
      }
    }
    return color;
  }

  public int[][] interpolate(String schemename, int numberofClasses){

    int[][] color = new int[numberofClasses + 1][3];

    int maxlengthRecommended = 0;

    if(schemename.equals("YlGn") || schemename.equals("YlGnBu") || schemename.equals("GnBu") || schemename.equals("BuGn") || schemename.equals("PuBuGn") || schemename.equals("PuBu")|| schemename.equals("BuPu")|| schemename.equals("RdPu")|| schemename.equals("PuRd")|| schemename.equals("OrRd")|| schemename.equals("YlOrRd")|| schemename.equals("YlOrBr")|| schemename.equals("Purples")|| schemename.equals("Blues")|| schemename.equals("Greens")|| schemename.equals("Oranges")|| schemename.equals("Reds")|| schemename.equals("Grays")){
      maxlengthRecommended = 9;
    }

    if(schemename.equals("PuOr") || schemename.equals("BrBG")|| schemename.equals("PRGn")|| schemename.equals("PiYG")|| schemename.equals("RdBu")|| schemename.equals("RdGy")|| schemename.equals("RdYlBu")|| schemename.equals("Spectral")|| schemename.equals("RdYlGn")){
      maxlengthRecommended = 11;
    }

    //find the basic information: the number of rounds, basebumber, and the steps[]

    this.getNumberofround(numberofClasses, maxlengthRecommended);
    logger.finest("Rounds of manipulation is:" + numberofRound);

    this.getBasicnumber(numberofClasses, maxlengthRecommended);


    int[] steps = new int[(this.numberofRound + 1)];
    this.getSteps(numberofClasses, steps, maxlengthRecommended);
    steps[0] = numberofClasses;
    for(int i = 0; i <= this.numberofRound; i ++){
      logger.finest("steps " + steps[i]);
    }

    //if the numberofClasses is within the maxlengthRecommended, then just get the recommended colors
    if(numberofClasses <= maxlengthRecommended){
      color = this.makescheme(schemename, numberofClasses);
    }

    //if the numberofClasses is greater than the maxlengthRecommended
    else{
      for(int i = this.numberofRound; i > 0; i --){
        //if the manipulation from steps[i - 1] to steps[i] is odd, do not perform any cutting
        if(steps[i - 1] == 2*steps[i] - 1){
          color = this.linearInterpolate(schemename, steps[i], color);
        }
        //if the manipulation from steps[i - 1] to steps[i] is even, perform head-cutting (ignoring the lighter colors)
        else{
          color = this.linearInterpolate(schemename, steps[i], color);
          //head-cutting (moving all elements in this.color to one space up)
          for(int j = 0; j < 2*steps[i] - 2; j ++){
            for(int k = 0; k < 3; k ++){
              color[j][k] = color[j + 1][k];
            }
          }
          //the last element is set to be zero
          //for(int k = 0; k < 3; k ++){
            //color[2*steps[i] - 2][k] = 0;
          //}
        }
      }
    }

    return color;
  }

  public void getSteps(int numberofClasses, int[] steps, int maxlengthRecommended){

    //if the numberofClasses is smaller than the maxlengthRecommended
    if(numberofClasses <= maxlengthRecommended){

    }

    //if the numberofClasses is greater than the maxlengthRecommended
    else{
      //if the numberofClasses is odd
    	if((numberofClasses % 2) == 1){
        //do an odd manipulation
        numberofClasses = (numberofClasses + 1)/2;

        //let the round of manipulation plus one
        this.currentRound = this.currentRound + 1;

        //put the numberofClasses into the steps
        steps[this.currentRound] = numberofClasses;

        //make a recursive search
        this.getSteps(numberofClasses, steps, maxlengthRecommended);
      }

      //if the numberofClasses is even
      else{
        //do an even manipulation
        numberofClasses = (numberofClasses + 2)/2;
        //let the round of manipulation plus one
        this.currentRound = this.currentRound + 1;

        //put the numberofClasses into the steps
        steps[this.currentRound] = numberofClasses;

        //make a recursive search
        this.getSteps(numberofClasses, steps, maxlengthRecommended);
      }
    }
  }

  public void getBasicnumber(int numberofClasses, int maxlengthRecommended){

    //if the numberofClasses is within the maxlengthRecommended, the basicnumber is the current numberofClasses
    if(numberofClasses <= maxlengthRecommended){
    }

    //if the numberofClasses is greater than the maxlengthRecommended
    else{
      //if the numberofClasses is odd
      if((numberofClasses % 2) == 1){
        //do an odd manipulation
        numberofClasses = (numberofClasses + 1)/2;
        //make a recursive search
        this.getBasicnumber(numberofClasses, maxlengthRecommended);
      }

      //if the numberofClasses is even
      else{
        //do an even manipulation
        numberofClasses = (numberofClasses + 2)/2;
        //make a recursive search
        this.getBasicnumber(numberofClasses, maxlengthRecommended);
      }
    }
  }

  public void getNumberofround(int numberofClasses, int maxlengthRecommended){

    //if the numberofClasses is within the maxlengthRecommended, the numberofround is zero
    if(numberofClasses <= maxlengthRecommended){
      this.numberofRound = this.numberofRound + 0;
    }

    //if the numberofClasses is greater than the maxlengthRecommended,
    else{
      //if the numberofClasses is odd
    	if((numberofClasses % 2) == 1){
        //do an odd manipulation
        numberofClasses = (numberofClasses + 1)/2;

        //let the round of manipulation plus one
        this.numberofRound = this.numberofRound + 1;

        //make a recursive search
        this.getNumberofround(numberofClasses, maxlengthRecommended);
      }

      //if the numberofClasses is even
      else{
        //do an even manipulation
        numberofClasses = (numberofClasses + 2)/2;
        //let the round of manipulation plus one
        this.numberofRound = this.numberofRound + 1;
        //make a recursive search
        this.getNumberofround(numberofClasses, maxlengthRecommended);
      }
    }
  }

  public Color[] transform(int[][] scheme, int length){

    Color[] color = new Color[length];

    for(int i = 0; i < length; i ++){
      for(int j = 0; j < 3; j ++){
        color[i] = new Color(scheme[i][0], scheme[i][1], scheme[i][2]);
      }
    }

    return color;
  }



}