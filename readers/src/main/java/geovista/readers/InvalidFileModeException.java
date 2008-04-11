/* -------------------------------------------------------------------
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * 
 * Java source file for the class InvalidFileModeException
 * 
 * Copyright (c), 2002, Masahiro Takatsuka & GeoVISTA Center
 * All Rights Reserved.
 *
 * This is a copy of jh9gpz.io.InvalidFileModeException
 * originally written by Masahiro Takatsuka
 *
 * Original Author: Masahiro Takatsuka
 * $Author: hardisty $
 * 
 * $Date: 2003/04/25 18:25:00 $
 * 
 * Reference:		Document no:
 * ___				___
 * 
 * To Do:
 * ___
 * 
------------------------------------------------------------------- */

/* --------------------------- Package ---------------------------- */
package geovista.readers;

/* ------------------ Import classes (packages) ------------------- */
// import foo.*;

/*====================================================================
           Implementation of class InvalidFileModeException           
====================================================================*/
/**
 * InvalidFileModeException is thown when invalid file mode is specified.
 * 
 * 
 * @author Masahiro Takatsuka (masa@psu.edu)
 * @see FileIOException
 */

public final class InvalidFileModeException extends FileIOException {
    public InvalidFileModeException(){
		super();
	}

    public InvalidFileModeException(String string) {
        super(string);
    }
}
