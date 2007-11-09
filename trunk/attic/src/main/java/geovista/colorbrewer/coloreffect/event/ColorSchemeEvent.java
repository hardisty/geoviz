package geovista.colorbrewer.coloreffect.event;

/**
 * <p>Title: Interactive Spatial Data Mining for Large and High-Dimensional Data</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: GeoVISTA Center and Department of Geography, Pennsylvania State University</p>
 * @author Diansheng Guo
 * @version 1.0
 */
import java.awt.Color;
import java.util.EventObject;

public class ColorSchemeEvent extends EventObject
{
  private Color[][] colors;

  public ColorSchemeEvent(Object source, Color[][] colors)
  {
    super(source);
    this.colors = colors;
  }

  /**
   *
   * @return
   */
  public Color[][] get2DColors()
  {
   return this.colors;
  }
}
