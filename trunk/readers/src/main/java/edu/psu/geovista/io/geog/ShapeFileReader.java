/*------------------------------------------------------------------------------
* GeoVISTA Center, Penn State Geography Deptartment
*
* Java source file for the class ShapeFileReader            (1)
*
* Copyright (c), 2001, GeoVISTA Center                      (2)
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*
* Original Author: Benyah Shaparenko, benyah5@hotmail.com   (3)
* $Author: hardisty $
*
* $Date: 2005/09/15 14:54:06 $
*
* $Id: ShapeFileReader.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
*
* To Do:
* ___
*
*
* Date            Author     Changes
* Dec 19 2001     Benyah     Created file
*
------------------------------------------------------------------------------*/

package edu.psu.geovista.io.geog;

/**
* Can read one or more shape files
* An example of how to use these classes to read a shape file
*
* @see ShapeFile.java
* @version $Id: ShapeFileReader.java,v 1.3 2005/09/15 14:54:06 hardisty Exp $
* @author Benyah Shaparenko (benyah5@hotmail.com)
*/

public class ShapeFileReader {
	public ShapeFileReader() {
	}

	public static void main(String[] args) {
		try {
			// select the file to read
			String baseName = new String("C:\\my documents\\polyline");
			String fileName = new String(baseName + ".shp");

			// create a ShapeFile object
			ShapeFile shapeFile = new ShapeFile(fileName, baseName);
			System.out.println("shapeFile = " + fileName + ", file data = " + shapeFile.toString());
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
