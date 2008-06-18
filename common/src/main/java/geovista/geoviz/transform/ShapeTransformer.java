/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class ShapeTransformer
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: ShapeTransformer.java,v 1.3 2005/04/11 17:37:47 hardisty Exp $
 $Date: 2005/04/11 17:37:47 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.geoviz.transform;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Defines what ShapeTransformers must do.
 */
public interface ShapeTransformer {

    public Shape[] makeTransformedShapes (Shape[] shapes, AffineTransform xForm);
    public Point2D[] makeTransformedPoints (Point2D[] points, AffineTransform xForm);
    public void setXForm(AffineTransform xForm);

public AffineTransform getXForm();


}
