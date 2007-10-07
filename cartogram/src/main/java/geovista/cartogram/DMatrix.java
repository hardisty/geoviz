package geovista.cartogram;
/*
 * Created on Dec 30, 2004
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
public class DMatrix {
	private int nrl;

	private int ncl;


	private float matrix[][];
	// Function to allocate a float matrix with subscript range
	// m[nrl..nrh][ncl..nch]. From "Numerical Recipes in C".

	//all parameters changed from long to int
	//the method used to be called: dmatrix
	public DMatrix (int nrl,int nrh,int ncl,int nch)
	{
		matrix = new float[nrh-nrl+1][nch-ncl+1];
		this.nrl = nrl;

		this.ncl = ncl;
	}

	public float getElement( int x, int y )
	{
		return matrix[x-nrl][y-ncl];
	}

	public void setElement( int x, int y, float f)
	{
		matrix[x-nrl][y-ncl]=f;
	}
}
