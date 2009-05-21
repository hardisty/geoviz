/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Masahiro Takatsuka*/

package geovista.readers;

/* ------------------ Import classes (packages) ------------------- */
import java.io.IOException;

/*====================================================================
 Implementation of class FileIOException                
 ====================================================================*/
/**
 * FileIOException is used for all geovista.readers package related exceptions.
 * 
 * 
 * @author Masahiro Takatsuka (masa@psu.edu)
 * @see IOException
 */

public class FileIOException extends IOException {
	public FileIOException() {
		super();
	}

	public FileIOException(String string) {
		super(string);
	}
}
