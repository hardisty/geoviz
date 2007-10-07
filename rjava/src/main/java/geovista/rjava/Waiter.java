/* -------------------------------------------------------------------
 Java source file for the class Waiter
 Copyright (c), 2004, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: Waiter.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
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

package geovista.rjava;

import java.util.logging.Logger;

/**
 * This class listens for events, especially coordinated events, and makes the
 * results available for query by entities that cannot be themselves coordinated.
 * An example is an instance of R polling an instance of this class for the
 * current selection etc.
 */
public class Waiter
    implements Runnable {
	protected final static Logger logger = Logger.getLogger(Waiter.class.getName());
  public void run(){

  }
  public Waiter() {
    super();

  }

public synchronized boolean waiting(double milis){
  int anInt = 0;
  anInt = (int)milis;
  //Integer bigInt = new Integer(milis);


  return this.waiting(anInt);
}
  public synchronized boolean waiting(int milis){
//    Timer tim = new Timer();

    //Thread t = new Thread();
     logger.finest("going to wait " + milis);

      //let's see if we can thrrow an exception first
      //int[] myStuff = {1,2};
      //myStuff[78] = 45;//throw it!
    boolean ok = false;
    try {
          this.wait(milis);
      //t.wait(milis);

          //tim.wait(milis);
      ok = true;
      logger.finest("just waited " + milis);
    }
    catch (InterruptedException ex) {
      ex.printStackTrace();
    }
    return ok;
  }
}
