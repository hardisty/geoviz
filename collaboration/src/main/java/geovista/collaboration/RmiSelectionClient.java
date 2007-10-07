/* -------------------------------------------------------------------
 Java source file for the class RmiSelectionClient
 Original Authors: Linna Li and Frank hardisty
 $Author: hardisty $
 $Id: RmiSelectionClient.java,v 1.3 2006/02/27 16:25:06 hardisty Exp $
 $Date: 2006/02/27 16:25:06 $
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

package geovista.collaboration;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

public class RmiSelectionClient {
	protected final static Logger logger = Logger.getLogger(RmiSelectionClient.class.getName());

  static public void main(String args[]) {
    ReceiveMessage rmiListener;
    Registry registry;
    String listenerAddress = args[0];
    String listenerPort = args[1];
    String text = args[2];
    logger.finest("sending " + text + " to " + listenerAddress + ":" + listenerPort);
    try {
      // get the “registry"
      registry = LocateRegistry.getRegistry(
          listenerAddress,
          (new Integer(listenerPort)).intValue()
          );
      // look up the remote object
      rmiListener =
          (ReceiveMessage) (registry.lookup("rmiListener"));

      // call the remote method
      //rmiListener.receiveMessage(text);
      RemoteSelectionListener selectionListener = (RemoteSelectionListener) rmiListener;
      int[] sel = {1,2,3};
      selectionListener.selectionChanged("Frank", sel);
    }
    catch (RemoteException e) {
      e.printStackTrace();
    }
    catch (NotBoundException e) {
      e.printStackTrace();
    }
  }
}
