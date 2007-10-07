package geovista.cartogram;
/*
 * Created on Dec 10, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author Nick
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ArrayPoint {
	/**
	 * @param i
	 */
	public ArrayPoint(int i) {
		array = new Point[i];
		for(int x = 0; x<i; x++)
			array[x] = new Point();
	}

	public Point array[] = null;
}
