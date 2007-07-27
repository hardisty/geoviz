package edu.psu.geovista.cartogram;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShapeUtil {

	protected final static Logger logger = Logger.getLogger(ShapeUtil.class.getName());
       public static double[] computeAreaArray(Shape[] shapes){
           double[] areas = new double[shapes.length];
           for (int i = 0; i < shapes.length; i++){
               areas[i] = computeArea(shapes[i]);
               if (logger.isLoggable(Level.FINEST)){
               logger.finest(i + " " + areas[i] * 100);
               }
           }
           return areas;
       }
       public static double computeArea(Shape shape){
           Shape[] shapeArray = {shape};
           return computeArea(shapeArray);
       }

       public static double computeArea(Shape[] shape) {
               return computeArea(shape, shape.length, 1.0, 10);
       }

       public static double computeArea(Shape[] shape, int n, double flatness, int limit) {
               if (n == 0)
                       return 0;

               Area area = new Area(shape[0]);

               for (int i = 1; i < n; i++) {
                       area.add(new Area(shape[i]));
               }

               return computeArea(new FlatteningPathIterator(area.getPathIterator(null), flatness, limit));
       }

       // path iterator must be flat
       public static double computeArea(PathIterator pi) {
               float[] pts = new float[2];

               Vector gpVec = new Vector();

               GeneralPath gp = null;
               int i = 0;

               while (!pi.isDone()) {
                       int type = pi.currentSegment(pts);

                       if (type == PathIterator.SEG_MOVETO) {
                               gp = new GeneralPath();
                               gpVec.add(gp);
                               gp.moveTo(pts[0], pts[1]);
                       }
                       else { // LINETO
                               gp.lineTo(pts[0], pts[1]);
                       }

                       pi.next();
               }

               GeneralPath[] arr = new GeneralPath[gpVec.size()];
               for (i = 0; i < arr.length; i++)
                       arr[i] = (GeneralPath) gpVec.get(i);



               boolean[] hole = determineHoles(arr);


               return computeArea(arr, hole);
       }

       private static double computeArea(GeneralPath[] arr, boolean[] hole) {
               double area = 0;

               for (int i = 0; i < arr.length; i++) {
                       double d = getArea(arr[i]);

                       if (hole[i])
                               area = area - d;
                       else
                               area = area + d;
               }

               return area;
       }

       private static boolean[] determineHoles(GeneralPath[] arr) {
               int[] count = new int[arr.length]; // counts the number of polygons each polygon is inside

               for (int i = 0; i < arr.length; i++) {
                       GeneralPath gp = arr[i];

                       for (int j = 0; j < arr.length; j++) {
                               if (i != j) {
                                       PathIterator pi = arr[j].getPathIterator(null);
                                       double[] pt = new double[2];
                                       pi.currentSegment(pt);

                                       if (gp.contains(pt[0], pt[1]))
                                               count[j]++;
                               }
                       }
               }

               boolean[] hole = new boolean[arr.length];

               for (int i = 0; i < hole.length; i++)
                       hole[i] = ((count[i] % 2) != 0);

               return hole;
       }

       private static double getArea(GeneralPath gp) {
               double[] x = new double[2];
               double[] y = new double[2];

               PathIterator pi = gp.getPathIterator(null);
               int i = 0;
               double[] pt = new double[2];

               while (!pi.isDone()) {
                       if (i == x.length) {
                               double[] tx = new double[x.length * 2];
                               double[] ty = new double[tx.length];

                               for (int j = 0; j < i; j++) {
                                       tx[j] = x[j];
                                       ty[j] = y[j];
                               }

                               x = tx;
                               y = ty;
                       }

                       pi.currentSegment(pt);

                       x[i] = pt[0];
                       y[i] = pt[1];

                       i++;
                       pi.next();
               }

               int n = i;

               double area = 0;

               for (i = 0; i < n; i++) {
                       int j = (i + 1) % n;

                       area += (x[i] * y[j] - x[j] * y[i]);
               }

               if (area < 0) area = -area;

               return area / 2;
       }
}

