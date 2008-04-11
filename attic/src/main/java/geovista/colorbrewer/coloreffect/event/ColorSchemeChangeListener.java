package geovista.colorbrewer.coloreffect.event;

/**
 * <p>Title: Interactive Spatial Data Mining for Large and High-Dimensional Data</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: GeoVISTA Center and Department of Geography, Pennsylvania State University</p>
 * @author Diansheng Guo
 * 
 */
import java.util.EventListener;

public interface ColorSchemeChangeListener extends EventListener {
  public void colorSchemeChanged(ColorSchemeEvent e);
}
