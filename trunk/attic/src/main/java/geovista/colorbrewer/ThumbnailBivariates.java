package geovista.colorbrewer;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ThumbnailBivariates
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * @version 1.0
 */


public class ThumbnailBivariates {

  //Quaseqbellcurve1 is a qua-seq color scheme created using bell curve, starting with red, and ending with purple
  //Parameters: vertex=141, divider=7500, lightness range=26~95, startinghue=25, deviation a-b=0, deviationy=0
  public static int[][][]  quaseqbellcurve1 = {
    {{255, 213, 209}, {255, 242, 160}, {148, 255, 224}, {129, 255, 255}, {255, 224, 255}},
    {{255, 148, 148}, {210, 192,  80}, {  0, 216, 168}, {  0, 209, 255}, {225, 167, 255}},
    {{255,  83,  96}, {161, 147,   0}, {  0, 172, 119}, {  0, 167, 255}, {182, 115, 239}},
    {{218,   0,  47}, {110, 105,   0}, {  0, 128,  72}, {  0, 127, 231}, {138,  60, 210}},
    {{180,   0,   8}, { 61,  66,   0}, {  0,  84,  27}, {  0,  89, 211}, { 89,   0, 186}},
  };

  //Seqquabellcurve1 is the transposition of Quaseqbellcurve1
  //Parameters: all parameters follow those of Quaseqbellcurve1
  public static int[][][]  seqquabellcurve1 = {
    {{255, 213, 209}, {255, 148, 148}, {255,  83,  96}, {218,   0,  47}, {180,   0,   8}},
    {{255, 242, 160}, {210, 192,  80}, {161, 147,   0}, {110, 105,   0}, { 61,  66,   0}},
    {{148, 255, 224}, {  0, 216, 168}, {  0, 172, 119}, {  0, 128,  72}, {  0,  84,  27}},
    {{129, 255, 255}, {  0, 209, 255}, {  0, 167, 255}, {  0, 127, 231}, {  0,  89, 211}},
    {{255, 224, 255}, {225, 167, 255}, {182, 115, 239}, {138,  60, 210}, { 89,   0, 186}},
  };

  //Quaseqbellcurve2 is a qua-seq color scheme created using bell curve, starting with red, and ending with purple
  //Parameters: vertex=164, divider=4913, lightness range=23~87, startinghue=292, deviation a-b=0, deviationy=0
  public static int[][][]  quaseqbellcurve2 = {
    {{201, 210, 255}, {255, 175, 213}, {255, 207, 115}, {135, 241, 160}, {  0, 243, 255}},
    {{148, 165, 255}, {255, 120, 169}, {228, 161,  54}, { 68, 197, 109}, {  0, 200, 248}},
    {{ 87, 123, 251}, {242,  55, 126}, {184, 118,   0}, {  0, 155,  59}, {  0, 159, 213}},
    {{  0,  83, 222}, {204,   0,  87}, {139,  78,   0}, {  0, 114,   0}, {  0, 118, 180}},
    {{  0,  49, 199}, {168,   0,  51}, { 97,  38,   0}, {  0,  74,   0}, {  0,  79, 152}},
  };

  //Seqquabellcurve2 is the transposition of Quaseqbellcurve2
  //Parameters: all parameters follow those of Quaseqbellcurve2
  public static int[][][]  seqquabellcurve2 = {
    {{201, 210, 255}, {148, 165, 255}, { 87, 123, 251}, {  0,  83, 222}, {  0,  49, 199}},
    {{255, 175, 213}, {255, 120, 169}, {242,  55, 126}, {204,   0,  87}, {168,   0,  51}},
    {{255, 207, 115}, {228, 161,  54}, {184, 118,   0}, {139,  78,   0}, { 97,  38,   0}},
    {{135, 241, 160}, { 68, 197, 109}, {  0, 155,  59}, {  0, 114,   0}, {  0,  74,   0}},
    {{  0, 243, 255}, {  0, 200, 248}, {  0, 159, 213}, {  0, 118, 180}, {  0,  79, 152}},
  };

  //Quaseqcone1 is a qua-seq color scheme starting created using cone, starting with red, and ending with blue
  //Parameters: cone height=130, cone radius=120, lightness range=35~97, startinghue=10, deviation a-b=0, deviationy=0
  public static int[][][]  quaseqcone1 = {
    {{255, 226, 238}, {255, 242, 189}, {199, 255, 220}, {166, 255, 255}, {249, 240, 255}},
    {{255, 168, 189}, {239, 195, 117}, {127, 221, 163}, {  0, 221, 255}, {202, 192, 255}},
    {{255, 112, 145}, {201, 153,  49}, { 39, 183, 112}, {  0, 183, 236}, {156, 148, 255}},
    {{228,  34, 101}, {159, 111,   0}, {  0, 143,  60}, {  0, 144, 209}, {101, 104, 232}},
    {{196,   0,  64}, {117,  74,   0}, {  0, 105,   5}, {  0, 108, 185}, {  3,  66, 210}},
  };

  //Seqquacone1 is the transposition of Quaseqcone1
  //Parameters: all parameters follow those of Quaseqcone1
  public static int[][][]  seqquacone1 = {
    {{255, 226, 238}, {255, 168, 189}, {255, 112, 145}, {228,  34, 101}, {196,   0,  64}},
    {{255, 242, 189}, {239, 195, 117}, {201, 153,  49}, {159, 111,   0}, {117,  74,   0}},
    {{199, 255, 220}, {127, 221, 163}, { 39, 183, 112}, {  0, 143,  60}, {  0, 105,   5}},
    {{166, 255, 255}, {  0, 221, 255}, {  0, 183, 236}, {  0, 144, 209}, {  0, 108, 185}},
    {{249, 240, 255}, {202, 192, 255}, {156, 148, 255}, {101, 104, 232}, {  3,  66, 210}},
  };

  //Divdivbellcurve1 is a div-div color scheme created using bell curve
  //Parameters: meshspan=135, vertex=100, divider=7500, startinghue=100, deviation a-b=0, deviationy=2
  public static int[][][]  divdivbellcurve1 = {
    {{  0, 138, 220}, {  0, 179, 217}, {  0, 196, 186}, {  0, 178, 122}, {  0, 136,  29}},
    {{  0, 169, 255}, {102, 225, 255}, {167, 250, 243}, {163, 226, 168}, {115, 169,  61}},
    {{115, 171, 255}, {216, 234, 255}, {255, 255, 255}, {245, 236, 183}, {184, 174,  70}},
    {{172, 135, 241}, {251, 191, 253}, {255, 216, 227}, {255, 194, 153}, {207, 141,  45}},
    {{179,  64, 189}, {237, 109, 186}, {255, 129, 156}, {245, 115,  93}, {194,  78,   0}},
  };

  //Divdivbellcurve2 is a div-div color scheme created using bell curve
  //Parameters: meshspan=157, vertex=95, divider=8435, startinghue=130, deviation a-b=0, deviationy=10
  public static int[][][]  divdivbellcurve2 = {
    {{  0, 127, 255}, {  0, 172, 255}, {  0, 196, 242}, {  0, 182, 180}, {  0, 141,  97}},
    {{ 66, 151, 255}, {140, 220, 255}, {153, 255, 255}, {100, 235, 205}, {  0, 178, 100}},
    {{198, 147, 255}, {255, 229, 255}, {255, 255, 255}, {218, 250, 193}, {121, 187,  78}},
    {{238, 101, 204}, {255, 181, 222}, {255, 222, 202}, {247, 208, 129}, {156, 159,   0}},
    {{232,   0, 129}, {255,  89, 127}, {255, 131, 100}, {221, 131,  26}, {148, 106,   0}},
  };

  //Divseqellipsedown1 is a div-seq color scheme created using ellipse curling down
  //Parameters: top angle=90, shape index=80, lightness range=46~100, startinghue=138, deviation a-b=0, deviationy=4
  public static int[][][]  divseqellipsedown1 = {
    {{255, 182, 255}, {255, 239, 255}, {255, 255, 255}, {219, 255, 206}, {121, 254, 109}},
    {{251, 158, 255}, {252, 206, 255}, {230, 230, 230}, {189, 235, 178}, {107, 219,  96}},
    {{208, 133, 238}, {209, 172, 222}, {191, 191, 191}, {158, 195, 149}, { 92, 182,  83}},
    {{170, 110, 194}, {170, 141, 180}, {156, 156, 156}, {130, 159, 123}, { 79, 149,  71}},
    {{130,  86, 147}, {130, 109, 137}, {119, 119, 119}, {101, 122,  95}, { 64, 115,  57}},
  };

  //Seqdivellipsedown1 is the transposition of Divseqellipsedown1
  //Parameters: all parameters follow those of Divseqellipsedown1
  public static int[][][]  seqdivellipsedown1 = {
    {{255, 182, 255}, {251, 158, 255}, {208, 133, 238}, {170, 110, 194}, {130,  86, 147}},
    {{255, 239, 255}, {252, 206, 255}, {209, 172, 222}, {170, 141, 180}, {130, 109, 137}},
    {{255, 255, 255}, {230, 230, 230}, {191, 191, 191}, {156, 156, 156}, {119, 119, 119}},
    {{219, 255, 206}, {189, 235, 178}, {158, 195, 149}, {130, 159, 123}, {101, 122,  95}},
    {{121, 254, 109}, {107, 219,  96}, { 92, 182,  83}, { 79, 149,  71}, { 64, 115,  57}},
  };

  //Divseqtrapezoid1 is a div-seq color scheme created using trapezoid
  //Parameters: top angle=109, radius=75, lightness range=50~95, startinghue=230, deviation a-b=0, deviationy=0
  public static int[][][]  divseqtrapezoid1 = {
    {{255, 178,  79}, {255, 207, 147}, {241, 241, 241}, {190, 250, 255}, {  0, 255, 255}},
    {{255, 146,  52}, {255, 175, 119}, {207, 207, 207}, {159, 216, 236}, {  0, 231, 255}},
    {{255, 118,  25}, {255, 147,  95}, {177, 177, 177}, {131, 185, 204}, {  0, 199, 255}},
    {{255,  90,   0}, {224, 119,  71}, {148, 148, 148}, {104, 155, 173}, {  0, 168, 225}},
    {{220,  61,   0}, {188,  92,  48}, {119, 119, 119}, { 77, 126, 143}, {  0, 138, 191}},
  };

  //Seqdivtrapezoid1 is the transposition of Divseqtrapezoid1
  //Parameters: all parameters follow those of Divseqtrapezoid1
  public static int[][][]  seqdivtrapezoid1 = {
    {{255, 178,  79}, {255, 146,  52}, {255, 118,  25}, {255,  90,   0}, {220,  61,   0}},
    {{255, 207, 147}, {255, 175, 119}, {255, 147,  95}, {224, 119,  71}, {188,  92,  48}},
    {{241, 241, 241}, {207, 207, 207}, {177, 177, 177}, {148, 148, 148}, {119, 119, 119}},
    {{190, 250, 255}, {159, 216, 236}, {131, 185, 204}, {104, 155, 173}, { 77, 126, 143}},
    {{  0, 255, 255}, {  0, 231, 255}, {  0, 199, 255}, {  0, 168, 225}, {  0, 138, 191}},
  };

  //divseqgrid1 is a div-seq color scheme created using grids
  //Parameters: top angle=150, saturation range=120, lightness range=35~100, startinghue=0, deviation a-b=0, deviationy=0
  public static int[][][]  divseqgrid1 = {
    {{  0, 238, 208}, {161, 249, 231}, {255, 255, 255}, {255, 212, 234}, {255, 162, 212}},
    {{  0, 189, 162}, {114, 200, 184}, {207, 207, 207}, {238, 165, 186}, {255, 114, 166}},
    {{  0, 145, 120}, { 69, 157, 141}, {164, 164, 164}, {192, 122, 143}, {210,  68, 124}},
    {{  0, 102,  81}, { 14, 115, 101}, {122, 122, 122}, {148,  82, 103}, {163,   6,  85}},
    {{  0,  62,  45}, {  0,  75,  63}, { 83,  83,  83}, {105,  44,  65}, {116,   0,  49}},
  };

  //seqdivgrid1 is the transposition of divseqgrid1
  //Parameters: all parameters follow those of divseqgrid1
  public static int[][][]  seqdivgrid1 = {
    {{  0, 238, 208}, {  0, 189, 162}, {  0, 145, 120}, {  0, 102,  81}, {  0,  62,  45}},
    {{161, 249, 231}, {114, 200, 184}, { 69, 157, 141}, { 14, 115, 101}, {  0,  75,  63}},
    {{255, 255, 255}, {207, 207, 207}, {164, 164, 164}, {122, 122, 122}, { 83,  83,  83}},
    {{255, 212, 234}, {238, 165, 186}, {192, 122, 143}, {148,  82, 103}, {105,  44,  65}},
    {{255, 162, 212}, {255, 114, 166}, {210,  68, 124}, {163,   6,  85}, {116,   0,  49}},
  };

  //Seqseqgray1 is a seq-seq color scheme with gray axis, red on one wing, and green on the other wing
  //Parameters: Top angle=137, lightness range=6~100, startinghue=356
  public static int[][][]  seqseqgraydiamond1 = {
    {{230, 230, 230}, {244, 188, 207}, {252, 142, 184}, {255,  90, 165}, {255,   0, 143}},
    {{153, 216, 199}, {177, 177, 177}, {190, 137, 155}, {199,  94, 136}, {200,  20, 115}},
    {{ 35, 199, 170}, {103, 163, 148}, {127, 127, 127}, {141,  91, 109}, {145,  43,  89}},
    {{  0, 180, 142}, {  0, 147, 121}, { 54, 114, 100}, { 83,  83,  83}, { 92,  46,  64}},
    {{  0, 159, 115}, {  0, 128,  94}, {  0,  97,  75}, {  0,  70,  58}, { 40,  40,  40}},
  };

  //Seqseqgray2 is a seq-seq color scheme with gray axis, yellow-orange on one wing, and blue on the other wing
  //Parameters: Top angle=129, lightness range=2~98, startinghue=53, deviation a-b=12, deviationy=14
  public static int[][][]  seqseqgraydiamond2 = {
    {{255, 255, 244}, {219, 238, 246}, {141, 219, 248}, {  0, 199, 249}, {  0, 179, 250}},
    {{255, 220, 187}, {229, 203, 190}, {166, 184, 192}, { 86, 166, 194}, {  0, 147, 195}},
    {{255, 184, 132}, {230, 167, 136}, {175, 151, 139}, {116, 133, 141}, { 18, 116, 142}},
    {{255, 147,  77}, {224, 132,  83}, {174, 117,  88}, {124, 102,  91}, { 69,  86,  93}},
    {{255, 108,   2}, {213,  96,  27}, {166,  83,  38}, {121,  70,  44}, { 77,  57,  47}},
  };

  //Seqseqnongray1 is a seq-seq color scheme with near-gray axis, purple on one wing, and green-cyan on the other wing
  //Parameters: Top angle=141, tilt angle=5, lightness range=18~100, startinghue=125, deviation a-b=0, deviationy=0
  public static int[][][]  seqseqnongraydiamond1 = {
    {{249, 227, 223}, {233, 198, 236}, {214, 168, 248}, {193, 139, 255}, {167, 109, 255}},
    {{213, 211, 166}, {199, 183, 179}, {184, 154, 192}, {166, 126, 203}, {143,  97, 213}},
    {{174, 195, 109}, {165, 167, 124}, {152, 140, 137}, {137, 113, 149}, {119,  86, 159}},
    {{133, 178,  45}, {127, 151,  69}, {119, 125,  85}, {109,  99,  98}, { 93,  74, 108}},
    {{ 85, 161,   0}, { 86, 135,   0}, { 83, 110,  30}, { 76,  85,  49}, { 66,  61,  61}},
  };

  public ThumbnailBivariates() {
  }
}