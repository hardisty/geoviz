/*
 * Created on Jan 5, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package geovista.cartogram;

/**
 * @author Nick
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Unsigned {
	long value;
	long getLong(){
		return value;
	}
	int getInt(){
		return (int) value;
	}

	void setValue( long value ){
		this.value = value;
	}

	long shiftLeft(){
		value <<= 1;
		return value;
	}

	long shiftRight(){
		value >>= 1;
		return value;
	}
}
