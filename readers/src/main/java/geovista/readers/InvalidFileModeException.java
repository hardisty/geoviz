/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Masahiro Takatsuka*/
package geovista.readers;

/**
 * InvalidFileModeException is thown when invalid file mode is specified.
 * 
 * 
 * @author Masahiro Takatsuka (masa@psu.edu)
 * @see FileIOException
 */

public final class InvalidFileModeException extends FileIOException {
	public InvalidFileModeException() {
		super();
	}

	public InvalidFileModeException(String string) {
		super(string);
	}
}
