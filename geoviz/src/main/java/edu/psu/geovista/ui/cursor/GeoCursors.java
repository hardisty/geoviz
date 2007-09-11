/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class GeoCursors
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: GeoCursors.java,v 1.2 2003/04/25 17:47:59 hardisty Exp $
 $Date: 2003/04/25 17:47:59 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package edu.psu.geovista.ui.cursor;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.net.URL;

import javax.swing.ImageIcon;
/**
 * GeoCursors is used to manufacture custom cursors.
 *
 * GeoCursors lazily creates and then keeps instances of
 * cursors to be called when needed.
 *
 */
public class GeoCursors extends Component {

    private  Cursor[] cursors;
    private  URL[] urls;

    public static final int CURSOR_PAN = 0;
    public static final int CURSOR_ZOOM_OUT = 1;
    public static final int CURSOR_ZOOM_IN = 2;
    public static final int CURSOR_INFO = 3;
    public static final int CURSOR_ARROW_PAN = 4;
    public static final int CURSOR_GRAB = 5;
    public static final int CURSOR_ARROW_ZOOM_IN = 6;
    public static final int CURSOR_ARROW_ZOOM_OUT = 7;
    public static final int CURSOR_ARROW_SELECT = 8;
    private static final int numCursors = 9;

    private static final URL urlPan = GeoCursors.class.getResource("resources/pan.gif");
    //private static final URL urlZoomIn = GeoCursors.class.getResource("resources/zoomIn.gif");
    //private static final URL urlZoomOut = GeoCursors.class.getResource("resources/zoomOut.gif");
    private static final URL urlGrab = GeoCursors.class.getResource("resources/grab.gif");
    private static final URL urlArrowZoomIn = GeoCursors.class.getResource("resources/arrowZoomIn.gif");
    private static final URL urlArrowZoomOut = GeoCursors.class.getResource("resources/arrowZoomOut.gif");
    private static final URL urlArrowSelect = GeoCursors.class.getResource("resources/arrowSelect.gif");
    private static final URL urlArrowPan = GeoCursors.class.getResource("resources/arrowPan.gif");

    private static final Point ptHand = new Point(16,16);
    private static final Point ptArrow = new Point(10,8);
  /**
  * null ctr
  */
  public GeoCursors(){
    cursors = new Cursor[numCursors];
    urls = new URL[numCursors];
    urls[GeoCursors.CURSOR_PAN] = GeoCursors.urlPan;
    urls[GeoCursors.CURSOR_ZOOM_IN] = GeoCursors.urlArrowZoomIn;
    urls[GeoCursors.CURSOR_ZOOM_OUT] = GeoCursors.urlArrowZoomOut;
    urls[GeoCursors.CURSOR_GRAB] = GeoCursors.urlGrab;
    urls[GeoCursors.CURSOR_ARROW_ZOOM_IN] = GeoCursors.urlArrowZoomIn;
    urls[GeoCursors.CURSOR_ARROW_ZOOM_OUT] = GeoCursors.urlArrowZoomOut;
    urls[GeoCursors.CURSOR_ARROW_SELECT] = GeoCursors.urlArrowSelect;
    urls[GeoCursors.CURSOR_ARROW_PAN] = GeoCursors.urlArrowPan;

  }


    /**
     * Returns the cursor specified.
     */
  public Cursor getCursor(int cursor) {
    //lazily create cursor, if need be
    if (cursors[cursor] == null) {
      Point pt = null;
      if (cursor == GeoCursors.CURSOR_PAN || cursor == GeoCursors.CURSOR_GRAB) {
        pt = GeoCursors.ptHand;
      } else {
        pt = GeoCursors.ptArrow;
      }
      URL urlGif = urls[cursor];
      ImageIcon imIcon = new ImageIcon(urlGif);
      Image im = imIcon.getImage();
      cursors[cursor] = this.getToolkit().createCustomCursor(im,pt,"custom");
    }
    return cursors[cursor];
  }


}