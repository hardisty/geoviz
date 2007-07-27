package edu.psu.geovista.common.color;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import java.awt.Color;
public interface Pallet {

  public static final int GOOD = 3;
  public static final int DOUBTFUL = 2;
  public static final int BAD = 1;
  public static final int NA = 0;

  public static final int SEQUENTIAL = 1;
  public static final int DIVERGING = 2;
  public static final int QUALITATIVE = 3;

  public Color[] getColors(int length);

  public String getName();

  public int getRecommendedMaxLength();

  public int isCRTSafe(int length);
  public int isColorblindSafe(int length);
  public int isPhotocopySafe(int length);
  public int isColorPrintingSafe(int length);
  public int isLCDprojectorSafe(int length);
  public int isLaptopSafe(int length);

  public int getType(); 

  public boolean isSequential();
  public boolean isDivergent();
  public boolean isQualatative();

}