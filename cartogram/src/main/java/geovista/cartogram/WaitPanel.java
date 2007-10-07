/* -------------------------------------------------------------------
 Java source file for the class WaitPanel
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: WaitPanel.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
 $Date: 2005/12/05 20:17:05 $
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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;


public class WaitPanel extends JPanel {
	protected final static Logger logger = Logger.getLogger(WaitPanel.class.getName());
    boolean goingUp;
    int whichCircle;

    JLabel message;
    private volatile WaiterThread blinker;
    long interval;


    public WaitPanel() {

        init();
    }

    private void init() {
        this.setBackground(Color.lightGray);
        this.whichCircle = 0;
        goingUp = true;
        interval = 2000;
    }


    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (logger.isLoggable(Level.FINEST)){
logger.finest("Painting");
logger.finest("" + (this.blinker == null));
logger.finest("which circle = " + whichCircle);
        }
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(this.getBackground());
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        int y, width, height;
        y = this.getHeight() / 2;
        int sizeFactor = 8;
        if (this.getWidth() > this.getHeight()) {
            height = this.getHeight() / sizeFactor;
            width = height;
        } else {
            width = this.getWidth() / sizeFactor;
            height = width;
        }

        drawCircles(g2, y, width, height);
        this.fillCircle(g2, y, width, height);

    }

    private void drawCircles(Graphics2D g, int y, int width, int height) {

        int x;

        x = this.getWidth() / 7;

        g.setColor(Color.darkGray);
        g.drawOval(x, y, width, height);

        x = x + this.getWidth() / 6;
        g.setColor(Color.darkGray);
        g.drawOval(x, y, width, height);

        x = x + this.getWidth() / 6;
        g.setColor(Color.darkGray);
        g.drawOval(x, y, width, height);

        x = x + this.getWidth() / 6;
        g.setColor(Color.darkGray);
        g.drawOval(x, y, width, height);

        x = x + this.getWidth() / 6;
        g.setColor(Color.darkGray);
        g.drawOval(x, y, width, height);

    }


    private void fillCircle(Graphics g, int y, int width, int height) {
        int x;
        if (blinker != null) {

            x = this.getWidth() / 7;
            x = x + (this.getWidth() / 6 * whichCircle);

            g.setColor(Color.darkGray);
            g.fillOval(x, y, width, height);
            if (this.whichCircle == 4) {
                this.goingUp = false;
            } else if (this.whichCircle == 0){
                this.goingUp = true;
            }

            if (goingUp){
                whichCircle++;
            } else {
                whichCircle--;
            }
        }
    }

    void startWaiting() {
        blinker = new WaiterThread(this);
        blinker.start();
    }


    void stopWaiting() {
        blinker = null;
        this.repaint();
        this.whichCircle = 1;
    }

    private class WaiterThread extends Thread {
        WaitPanel waiter;

        WaiterThread(WaitPanel waiter) {
            this.waiter = waiter;
        }

        public void run() {

            while (this == waiter.blinker) {

                try {
                    Thread.sleep(waiter.getInterval());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                waiter.repaint();
            }
        }


    }


    public static void main(String args[]) {
        JFrame app = new JFrame("test wait frame");
        final WaitPanel waiter = new WaitPanel();
        app.getContentPane().add(waiter);
        final JToggleButton start = new JToggleButton("start");
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (start.isSelected()) {

                    waiter.startWaiting();
                } else {
                    waiter.stopWaiting();
                }
            }
        });
        app.getContentPane().add(start, BorderLayout.SOUTH);
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        app.pack();
        app.setVisible(true);

    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
