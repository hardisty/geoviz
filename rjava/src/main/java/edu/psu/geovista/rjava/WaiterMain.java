/* -------------------------------------------------------------------
 Java source file for the class WaiterMain
 Copyright (c), 2004, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: WaiterMain.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
 $Date: 2005/02/13 03:26:27 $
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
 -------------------------------------------------------------------   */

package edu.psu.geovista.rjava;

/**
 * This class listens for events, especially coordinated events, and makes the
 * results available for query by entities that cannot be themselves coordinated.
 * An example is an instance of R polling an instance of this class for the
 * current selection etc.
 */
public class WaiterMain
  {
    /**
     * Main method for testing.
     */
    public static void main(String[] args) {
      Waiter waiter = new Waiter();
     
      waiter.waiting(500d);
   

    }

}
