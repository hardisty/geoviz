package geovista.cartogram;
/*
 * Created on Dec 10, 2004
 *
 */

/**
 * @author Nick
 *
 * we need this to simulate
 */
public class ArrayFloat {
        public ArrayFloat(){

        }
	public ArrayFloat ( int x ){
		array = new float[x];
	}

    public float[] getArray() {
        return array;
    }

    public void setArray(float[] array) {
        this.array = array;
    }

    public float array[] = null;
}
