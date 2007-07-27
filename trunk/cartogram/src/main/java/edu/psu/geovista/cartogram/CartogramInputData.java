/* -------------------------------------------------------------------
 Java source file for the class CartogramInputData
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramInputData.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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

import edu.psu.geovista.app.map.GeoMapUni;

//


/*
 * This class is the first step in the CartogramWizard
 */

public class CartogramInputData extends JPanel implements ActionListener {
    JLabel stepLabel;
    JTextField inputFileNameTextField;
    JButton inputFileNameButton;
    ActionListener wizard;

    /**
     * CartogramInputData
     */
    public CartogramInputData(GeoMapUni map) {
        initGui(map);
    }

    private void initGui(GeoMapUni map) {
        this.removeAll();
        stepLabel = new JLabel("Step One: Choose input file.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        //stepLabel.setForeground(Color.PINK);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
//this.add(stepLabel,BorderLayout.NORTH);


        JLabel inputFileNameLabel = new JLabel();
        inputFileNameButton = new JButton();
        inputFileNameButton.addActionListener(this);
        inputFileNameTextField = new JTextField();

//inputFileNameTextField.setFont();
        inputFileNameTextField.setPreferredSize(new Dimension(400, 25));
        JPanel content = GuiUtils.createInputPanel(map, inputFileNameLabel,
                inputFileNameButton, inputFileNameTextField);

        content.setBorder(BorderFactory.createLineBorder(Color.black));
        this.add(content, BorderLayout.CENTER);
        this.add(stepLabel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.inputFileNameButton) {
            String fileName = GuiUtils.chooseInputFilename(this);
            if (fileName != null) {
                this.inputFileNameTextField.setText(fileName);
                ActionEvent e2 = new ActionEvent(this, 0, fileName);
                wizard.actionPerformed(e2);
            }
        }
    }

    public void setFileName(String fileName) {
        this.inputFileNameTextField.setText(fileName);
    }

    public void setMap(GeoMapUni map) {
        Dimension stuffSize = new Dimension(600, 350);
        map.setPreferredSize(stuffSize);
        initGui(map);
    }

    public void addActionListener(ActionListener wizard) {
        this.wizard = wizard;
    }


}
