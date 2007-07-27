/* -------------------------------------------------------------------
 Java source file for the class ToolkitLayoutIO
 Copyright (c), 2005 Ke Liao, Frank Hardisty
 $Author: hardisty $
 $Id: ToolkitLayoutIO.java,v 1.6 2005/04/11 17:52:14 hardisty Exp $
 $Date: 2005/04/11 17:52:14 $
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class  SaveCommentedImageDialog extends JDialog{

	public SaveCommentedImageDialog(){
		this.add(new JFileChooser());
		JPanel textPan = new JPanel();
		String s = "<html>Enter text <br>to be embedded<br>in image:</html>";
		JLabel textLab = new JLabel(s, JLabel.RIGHT);
		textPan.add(textLab);
		
		JScrollPane sPane = new JScrollPane();
		sPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JTextArea tArea = new JTextArea();
		Font fon = new Font(tArea.getFont().getName(),Font.PLAIN,14);
		tArea.setFont(fon);
		sPane.getViewport().add(tArea);
		sPane.setPreferredSize(new Dimension(200,50));
		textPan.add(sPane);
		textPan.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(textPan,BorderLayout.SOUTH);
		
	}
	public static void main(String[] args) {
		
		SaveCommentedImageDialog imgDlg = new SaveCommentedImageDialog();
		imgDlg.pack();
		imgDlg.setVisible(true);
		
	}
}
