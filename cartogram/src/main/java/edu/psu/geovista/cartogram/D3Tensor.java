package edu.psu.geovista.cartogram;
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
public class D3Tensor {
		private int nrl;
		private int ncl;
		private int ndl;
		int xSize, ySize, zSize;
		int kOffset = 0;

		private float matrix[][][];
// Function to allocate a float 3tensor with range
// t[nrl..nrh][ncl..nch][ndl..ndh]. From "Numerical Recipes in C".

		//all parameters changed from long to int
		//the method used to be called: d3tensor
		public D3Tensor (int nrl,int nrh,int ncl,int nch, int ndl, int ndh)
		{
			this.nrl = nrl;
			this.ncl = ncl;
			this.ndl = ndl;
			xSize = nrh-nrl+1;
			ySize = nch-ncl+1;
			zSize = ndh-ndl+1;
			matrix = new float[xSize][ySize][zSize];
		}

		public float getElement( int x, int y , int z)
		{
			return matrix[x-nrl][y-ncl][z-ndl];
		}
		public float getElement( int k ){
			k+=kOffset;
			int zPos = k % zSize;
			int yPos = (k / zSize) % ySize;
			int xPos = (k / zSize)/ySize;

			return matrix[ xPos ][ yPos  ][ zPos ];
		}

		/**
		 * @param b
		 * @param x
		 * @return
		 */
		private float getAndSetElement(int k, float x) {
			k+=kOffset;
			int zPos = k % zSize;
			int yPos = (k / zSize) % ySize;
			int xPos = (k / zSize)/ySize;

			float f = matrix[ xPos ][ yPos  ][ zPos ];
			matrix[ xPos ][ yPos  ][ zPos ] = x;
			return f;
		}
		/**
		 * @param i
		 * @param tempr
		 */
		public void addToElement(int k, float x) {
			k+=kOffset;
			int zPos = k % zSize;
			int yPos = (k / zSize) % ySize;
			int xPos = (k / zSize)/ySize;

			matrix[ xPos ][ yPos  ][ zPos ] += x;
		}


		public void setElement( int k, float f){
			k+=kOffset;
			int zPos = k % zSize;
			int yPos = (k / zSize) % ySize;
			int xPos = (k / zSize)/ySize;

			matrix[ xPos ][ yPos  ][ zPos ] = f;
		}

		public void setElement( int x, int y, int z, float f)
		{
			matrix[x-nrl][y-ncl][z-ndl]=f;
		}

		public int getElementsCount(){
			return xSize*ySize*zSize;
		}

		/**
		 * @param a
		 * @param b
		 */
		public void swapElements(int a, int b) {
			float x = getElement(a);
			float y = getAndSetElement(b,x);
			setElement(a,y);
		}

		/**
		 * @param i
		 * @param j
		 * @param k
		 */
		public void setOffset(int i, int j, int k) {
			// TODO Auto-generated method stub
			kOffset+= k +(j+i*ySize)*zSize;
		}
}
