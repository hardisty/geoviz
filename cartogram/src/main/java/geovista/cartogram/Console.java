/* -------------------------------------------------------------------
 Java source file for the class Console
 $Author: hardistf $
 $Id: Console.java,v 1.2 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
 Original Source: The Java Almanac 1.4. Reuse authorized by the following
 statement at http://javaalmanac.com/, accessed Aug 24, 2005.
 All the code examples from the book are made available here
 for you to copy and paste into your programs.

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


package geovista.cartogram;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Console extends JFrame {
    PipedInputStream piOut;
    PipedInputStream piErr;
    PipedOutputStream poOut;
    PipedOutputStream poErr;
    JTextArea textArea = new JTextArea();

    public Console() throws IOException {

        this.setTitle("Console Messages");


        // Set up System.out
        String outFileName = System.getProperty("user.home") + "/out.log";
        File fOut = new File(outFileName);
        FileOutputStream fio = new FileOutputStream(fOut);
        PrintStream psOut = new PrintStream(fio);


        piOut = new PipedInputStream();
        poOut = new PipedOutputStream(piOut);
        PrintStream prOut = new PrintStream(poOut, true);
        PrintStream tee = new TeeStream(System.out, prOut, psOut);
        System.setOut(tee);

        // Set up System.err
        String errFileName = System.getProperty("user.home") + "/err.log";

        FileOutputStream fio2 = new FileOutputStream(errFileName);
        PrintStream errOut = new PrintStream(fio2);
        piErr = new PipedInputStream();
        poErr = new PipedOutputStream(piErr);
        PrintStream prErr = new PrintStream(poErr,true);
        tee = new TeeStream(System.err, prErr, errOut);
        System.setErr(tee);

        // Add a scrolling text area

        textArea.setEditable(false);
        textArea.setRows(20);
        textArea.setColumns(50);
        this.getContentPane().add(new JScrollPane(textArea),
                                  BorderLayout.CENTER);
        pack();
        setVisible(true);

        // Create reader threads
        new ReaderThread(piOut).start();
        new ReaderThread(piErr).start();
    }

    class ReaderThread extends Thread {
        PipedInputStream pi;

        ReaderThread(PipedInputStream pi) {
            this.pi = pi;
        }

        public void run() {
            final byte[] buf = new byte[1024];
            try {
                while (true) {
                    final int len = pi.read(buf);
                    if (len == -1) {
                        break;
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            textArea.append(new String(buf, 0, len));

                            // Make sure the last line is always visible
                            textArea.setCaretPosition(textArea.getDocument().getLength());

                            // Keep the text area down to a certain character size
                                int idealSize = 1000;
                                int maxExcess = 500;
                                int excess = textArea.getDocument().getLength() - idealSize;
                                if (excess >= maxExcess) {
                                    textArea.replaceRange("", 0, excess);
                                }
                        }
                    });
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }


    class TeeStream extends PrintStream {
        PrintStream out;
        PrintStream out2;
        public TeeStream(PrintStream out1, PrintStream out2, PrintStream out3) {

            super(out1);
            this.out = out2;
            this.out2 = out3;
        }

        public void write(byte buf[], int off, int len) {
            try {
                super.write(buf, off, len);
                out.write(buf, off, len);
                out2.write(buf, off, len);
            } catch (Exception e) {
            }
        }

        public void flush() {
            super.flush();
            out.flush();
        }
    }

}
