/* -------------------------------------------------------------------
 Java source file for the class SocketServer
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SocketServer.java,v 1.9 2006/02/27 19:28:41 hardisty Exp $
 $Date: 2006/02/27 19:28:41 $
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation under
 version 2.1 of the License.
 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.
 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package geovista.collaboration;

/** ******************************************************************* */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class SocketServer {

	public static void main(String args[]) throws Exception {

		ServerSocket welcomeSocket = null;

		try {
			welcomeSocket = new ServerSocket(80);
		} catch (IOException ioe) {
			System.out.println("Could not listen on port: 80");
			System.exit(1);
		}

		while (true) {
			Socket connectionSocket = null;

			try {
				connectionSocket = welcomeSocket.accept();
			} catch (IOException ioe) {
				System.err.println("Accept failed.");
				System.exit(1);
			}

			BufferedReader inFromClient = new BufferedReader(
					new InputStreamReader(connectionSocket.getInputStream()));
			BufferedOutputStream outToClient = new BufferedOutputStream(
					connectionSocket.getOutputStream());
			System.out.println(inFromClient.readLine());

			int totalSizeTransferred = 0;
			int totalSizeRead;
			int PACKET_SIZE = 20480;
			byte[] packet = new byte[PACKET_SIZE];

			System.out.println("reading file...");
			FileInputStream fis = new FileInputStream("6meg.pdf");

			while ((totalSizeRead = fis.read(packet, 0, packet.length)) >= 0) {
				outToClient.write(packet, 0, totalSizeRead);
				totalSizeTransferred = totalSizeTransferred + totalSizeRead;
				System.out.println(totalSizeTransferred);
			}

			System.out.println("done reading file...");
			outToClient.close();
			fis.close();
		}
	}

}
