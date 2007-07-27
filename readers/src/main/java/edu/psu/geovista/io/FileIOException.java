/* -------------------------------------------------------------------
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * 
 * Java source file for the class FileIOException
 * 
 * Copyright (c), 2002, Masahiro Takatsuka & GeoVISTA Center
 * All Rights Reserved.
 *
 * This is a copy of jh9gpz.io.MtFileIOException originally written by
 * Masahiro Takatsuka
 *
 * Original Author: Masahiro Takatsuka
 * $Author: hardisty $
 * 
 * $Date: 2003/04/25 18:24:59 $
 * 
 * Reference:		Document no:
 * ___				___
 * 
 * To Do:
 * ___
 * 
------------------------------------------------------------------- */

/* --------------------------- Package ---------------------------- */
package edu.psu.geovista.io;

/* ------------------ Import classes (packages) ------------------- */
import java.io.IOException;

/*====================================================================
               Implementation of class FileIOException                
====================================================================*/
/**
 * FileIOException is used for all edu.psu.geovista.io package
 * related exceptions.
 * 
 * @version $Revision: 1.2 $
 * @author Masahiro Takatsuka (masa@psu.edu)
 * @see IOException
 */

public class FileIOException extends IOException {
    public FileIOException(){
		super();
	}

    public FileIOException(String string) {
        super(string);
    }
}
