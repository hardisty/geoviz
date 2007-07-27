/* -------------------------------------------------------------------
 Java source file for the class USCHelp
 Copyright (c), 2005 Aaron Myers
 $Author: myersat $
 $Id: USCHelp.java,v 1.6 2005/03/28 15:57:15 myersat Exp $
 $Date: 2005/03/28 15:57:15 $
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

package edu.psu.geovista.toolkitcore;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class USCHelp extends JInternalFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JEditorPane help;
    JEditorPane helpTOC;
    MyHyperlinkListener linkListener;
    protected final static Logger logger = Logger.getLogger(USCHelp.class.getName());
//create USC Help
    public USCHelp() {
        //Sets Internal Frame Values and Icon
        super("Tutorial", true, true, true, true);

        URL helpGif = null;
        ImageIcon helpIcon = null;
        try {
            Class helpcl = this.getClass();
            helpGif = helpcl.getResource("resources/help32.gif");
            helpIcon = new ImageIcon(helpGif);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.setFrameIcon(helpIcon);

        //Creates Help EditorPane
        help = new JEditorPane();
        help.setEditable(false);
        help.setPreferredSize(new Dimension(650, 700));
        help.setLayout(new BorderLayout());
        linkListener = new MyHyperlinkListener();
        help.addHyperlinkListener(linkListener);

        java.net.URL helpURL = USCHelp.class.getResource("resources/help.html");
        if (helpURL != null) {
            try {
                help.setPage(helpURL);
                logger.finest("help text = " + help.getText());
            } catch (IOException e) {
                System.err.println("Attempted to read bad URL" + helpURL);
            }
        } else {
            logger.finest("helpURL is null, ack!");
        }

// Creates Table of Contents EditorPane
        helpTOC = new JEditorPane();
        helpTOC.setEditable(false);
        helpTOC.setPreferredSize(new Dimension(200, 700));
        helpTOC.setLayout(new BorderLayout());
        helpTOC.addHyperlinkListener(this.linkListener);

        java.net.URL helpTOCURL = USCHelp.class.getResource(
                "resources/toc.html");
        if (helpTOCURL != null) {
            try {
                helpTOC.setPage(helpTOCURL);
                logger.finest("help textTOC = " + help.getText());
            } catch (IOException e) {
                System.err.println("Attempted to read bad URL" + helpTOCURL);
            }
        } else {
            System.err.println("Could not find file: Table of Contents");
        }

//Creates Scrollpanes and adds Help and Table of Contents
        JScrollPane helpScroll = new JScrollPane();
        helpScroll.setHorizontalScrollBarPolicy(JScrollPane.
                                                HORIZONTAL_SCROLLBAR_AS_NEEDED);
        helpScroll.setVerticalScrollBarPolicy(JScrollPane.
                                              VERTICAL_SCROLLBAR_ALWAYS);
        helpScroll.setViewportView(help);
        helpScroll.getViewport();
        helpScroll.setVisible(true);
        helpScroll.setWheelScrollingEnabled(true);

        JScrollPane tocScroll = new JScrollPane();
        tocScroll.setHorizontalScrollBarPolicy(JScrollPane.
                                               HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tocScroll.setVerticalScrollBarPolicy(JScrollPane.
                                             VERTICAL_SCROLLBAR_AS_NEEDED);
        tocScroll.setViewportView(helpTOC);
        tocScroll.getViewport();
        tocScroll.setVisible(true);
        tocScroll.setWheelScrollingEnabled(true);

//Creates SplitPane and adds two Scrollpanes
        JSplitPane helpPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        helpPane.setDividerLocation(209);
        helpPane.getDividerLocation();
        helpPane.setContinuousLayout(true);
        helpPane.add(helpScroll);
        helpPane.add(tocScroll);
        helpPane.setLeftComponent(tocScroll);
        helpPane.setRightComponent(helpScroll);
        helpPane.getLeftComponent();
        helpPane.getRightComponent();
        Dimension minimumsize = new Dimension(100, 50);
        Dimension maximumsize = new Dimension(550,209);
        helpScroll.setMinimumSize(minimumsize);
        tocScroll.setMinimumSize(minimumsize);
        tocScroll.setMaximumSize(maximumsize);
        helpPane.setPreferredSize(new Dimension(650, 660));
        helpPane.setOneTouchExpandable(true);
        helpPane.setResizeWeight(0.1);
        helpPane.setOpaque(true);
        this.getContentPane().add(helpPane);
        this.setContentPane(helpPane);
        this.setVisible(true);
        this.setLocation(361, 1);
        this.setEnabled(true);
    }

//Internal HyperLinkListerner
    class MyHyperlinkListener implements HyperlinkListener {
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    help.setPage(e.getURL());
                    logger.finest("MyHyper... setting URL");
                } catch (IOException exc) {
                }
            }
        }
    }


//add USC Help Pane to JFrame
    public static void main(String[] args) {
        JFrame app = new JFrame();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout flow = new FlowLayout();
        app.getContentPane().setLayout(flow);
        USCHelp hp = new USCHelp();
        app.getContentPane().add(hp);
        app.setContentPane(hp);
        app.setEnabled(true);
        app.pack();
        app.setVisible(true); 
    }
}
