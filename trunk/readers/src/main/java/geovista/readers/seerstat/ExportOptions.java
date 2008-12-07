/**
 * 
 */
package geovista.readers.seerstat;

/**
 * @author Frank Hardisty
 * 
 */
public class ExportOptions {

	boolean gZipped;
	String fieldDelimiter;
	boolean flagsIncluded;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ExportOptions reader = new ExportOptions();

		// TODO Auto-generated method stub

	}

	public boolean isGZipped() {
		return gZipped;
	}

	public void setGZipped(boolean zipped) {
		gZipped = zipped;
	}

	public String getFieldDelimiter() {
		return fieldDelimiter;
	}

	public void setFieldDelimiter(String fieldDelimiter) {
		this.fieldDelimiter = fieldDelimiter;
	}

	public boolean isFlagsIncluded() {
		return flagsIncluded;
	}

	public void setFlagsIncluded(boolean flagsIncluded) {
		this.flagsIncluded = flagsIncluded;
	}
}
