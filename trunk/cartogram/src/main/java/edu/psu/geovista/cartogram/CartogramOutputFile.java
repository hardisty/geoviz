/* -------------------------------------------------------------------
 Java source file for the class CartogramOutputFile
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramOutputFile.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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

package edu.psu.geovista.cartogram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



//step 3: choose output file location


/*
 * This class is the third step in the CartogramWizard
 */

public class CartogramOutputFile extends JPanel implements ActionListener{
    JLabel stepLabel;
    JTextField outputFileNameTextField;
    JButton outputFileNameButton;
    ActionListener wizard;
    String outputFileName;

   public CartogramOutputFile(ActionListener wizard, String outputFileName){

       this.wizard = wizard;
       initGui();
       this.outputFileNameTextField.setText(outputFileName);
   }

    private void initGui() {
        this.removeAll();
        JPanel spacePanel = new JPanel();
        spacePanel.setPreferredSize(new Dimension(300,350));
        JPanel stuffPanel = new JPanel();
        BorderLayout stuffBorder = new BorderLayout();
        stuffPanel.setLayout(stuffBorder);
        stepLabel = new JLabel("Step three: Choose output file location.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        //stepLabel.setForeground(Color.PINK);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
//this.add(stepLabel,BorderLayout.NORTH);


        JLabel outputFileNameLabel = new JLabel();
        outputFileNameButton = new JButton();
        outputFileNameButton.addActionListener(this);
        outputFileNameTextField = new JTextField(this.outputFileName);
        outputFileNameTextField.setPreferredSize(new Dimension(400, 25));
        outputFileNameLabel.setText("Output File Name:");
        JPanel content = GuiUtils.createOutputPanel(outputFileNameLabel,
                outputFileNameButton, outputFileNameTextField,"Pick Output Shapefile Name");
        //JPanel content = new JPanel();
        //content.add(outputFileNameButton);
        //content.add(outputFileNameTextField);
        //content.setBorder(BorderFactory.createLineBorder(Color.black));
        stuffPanel.add(content, BorderLayout.CENTER);
        stuffPanel.add(stepLabel, BorderLayout.SOUTH);
        this.add(spacePanel, BorderLayout.CENTER);
        this.add(stuffPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.outputFileNameButton) {
            String fileName = GuiUtils.chooseOutputFilename(this);
            if (fileName != null) {
                this.outputFileNameTextField.setText(fileName);
                ActionEvent e2 = new ActionEvent(this, 0, fileName);
                wizard.actionPerformed(e2);
                //readShapeFile(fileName);
            }
        }
    }

    public void setFileName(String fileName) {
        this.outputFileNameTextField.setText(fileName);
    }



    public void addActionListener(ActionListener wizard) {
        this.wizard = wizard;
    }

}
