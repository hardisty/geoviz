/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class FoldupPanel
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: jmacgill $
 $Id: FoldupPanel.java,v 1.1.1.1 2003/02/28 14:53:56 jmacgill Exp $
 $Date: 2003/02/28 14:53:56 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */

package edu.psu.geovista.ui.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * FoldupPanel is like a normal JPanel except that it has two states: "normal"
 * where the only difference is that there is a small "thumb" area in the left
 * side of the component for changing the state. "folded" where the panel
 * becomes very thin. The only interaction possible with the component in the
 * folded state is to click on the thumb area to change it back to normal.
 * 
 * To use the FoldupPanel, access the "contentPanel" using the method
 * getContentPanel();
 * 
 * Trying to add components to or change the layout of the main panel will throw
 * an exception.
 * 
 * Currently, the component only "folds up" vertically.
 */
public class FoldupPanel extends JPanel implements ActionListener {
	protected final static Logger logger = Logger.getLogger(FoldupPanel.class.getName());
	transient boolean folded = false;
	transient ThumbPanel thumb;
	 JPanel contentPanel;
	 JPanel spaceHolder;
	transient Dimension tempPreferredSize;
	private static Dimension foldedPreferredSize = new Dimension(600, 8);
	 boolean initFinished;

	/**
	 * null ctr
	 */
	public FoldupPanel() {
		this.initFinished = false;
		JPanel thumbHolder = this.makeThumbPanel();
		this.spaceHolder = new JPanel();
		thumb.setPreferredSize(new Dimension(8, 30));
		spaceHolder.setPreferredSize(new Dimension(2000, 5));
		contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(2000, 10));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.add(thumbHolder);
		this.add(contentPanel);
		this.add(spaceHolder);
		spaceHolder.setVisible(false);
		// this.setBorder(BorderFactory.createLineBorder(Color.black,2));
		// this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		this.setPreferredSize(new Dimension(4000, 5));
		this.thumb.addActionListener(this);
		this.initFinished = true;
	}

	// toggle state. based on listening to our ThumbPanel
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("fold")) {
			logger.finest("folding...");
			tempPreferredSize = new Dimension(this.getPreferredSize());
			this.setPreferredSize(FoldupPanel.foldedPreferredSize);
			this.contentPanel.setVisible(false);
			this.spaceHolder.setVisible(true);
			// this.setSize(new Dimension(600,8));
		} else if (command.equals("unfold")) {
			logger.finest("unfolding...");
			this.setPreferredSize(tempPreferredSize);
			this.spaceHolder.setVisible(false);
			this.contentPanel.setVisible(true);
		}
		this.revalidate();
		this.thumb.repaint();

	}

	public JPanel makeThumbPanel() {
		thumb = new ThumbPanel();
		// thumb.setMinimumSize(ne);
		JPanel thumbHolder;
		thumbHolder = new JPanel();
		thumbHolder.setBorder(BorderFactory.createLineBorder(Color.black));
		thumbHolder.setLayout(new BorderLayout());
		// JPanel ThumbSpaceHolder = new JPanel();
		// ThumbSpaceHolder.setPreferredSize(new Dimension(5,4000));

		// thumbHolder.add(ThumbSpaceHolder);
		thumbHolder.add(thumb, BorderLayout.SOUTH);
		return thumbHolder;
	}

	public JPanel getContentPanel() {
		return this.contentPanel;
	}

	public void setContentPanel(JPanel contentPanel) {
		this.contentPanel = contentPanel;
	}

	// overridden methods
	public Component add(Component c) {
		if (initFinished) {
			throw new IllegalArgumentException(
					"Can't add components to FoldupPanel. Please use foldup.getContentPanel() instead");
		} else {
			super.add(c);
		}
		return c;
	}

	public void add(Component c, Object constraints) {
		if (initFinished) {
			throw new IllegalArgumentException(
					"Can't add components to FoldupPanel. Please use foldup.getContentPanel() instead");
		} else {
			super.add(c, constraints);
		}
	}

	public void setLayout(LayoutManager lm) {
		if (initFinished) {
			throw new IllegalArgumentException(
					"Can't set layout in FoldupPanel. Please use foldup.getContentPanel() instead");
		} else {
			super.setLayout(lm);
		}
	}

}