/* -------------------------------------------------------------------
 Java source file for the class CartogramCreate
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramCreate.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

//
//step 4: create cartogram....create auxilliary shapefiles here

/*
 * This class is the fourth step in the CartogramWizard
 */

public class CartogramCreateAux extends JPanel implements ActionListener {
    JLabel stepLabel;
    JTextField inputFileNameTextField;
    JButton inputFileNameButton;
    JButton runAux;
    ActionListener wizard;
    JProgressBar progressBar;
    WaitPanel waitPanel;

    /**
     * CartogramCreate
     */
    public CartogramCreateAux(ActionListener wizard) {
        this.wizard = wizard;
        initGui();
        this.haveCartogram(false);
    }

    private void initGui() {
        this.removeAll();

        stepLabel = new JLabel(
                "Step Six (Optional): Create other cartograms based on the same distortion field.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        //stepLabel.setForeground(Color.PINK);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);

        JLabel inputFileNameLabel = new JLabel();
        inputFileNameButton = new JButton();
        inputFileNameButton.addActionListener(this);
        inputFileNameTextField = new JTextField();
        inputFileNameLabel.setText("Additional Input File Name:");
        inputFileNameTextField.setPreferredSize(new Dimension(350, 25));
        JPanel content = GuiUtils.createOutputPanel(inputFileNameLabel,
                inputFileNameButton, inputFileNameTextField,
                "Transform Shapefiles Based on Cartogram Field");
        inputFileNameButton.setText("Choose Aux");
        progressBar = new JProgressBar();
        runAux = new JButton("Create Aux!");
        content.add(runAux);
        JPanel content2 = new JPanel();
        content2.setLayout(new BorderLayout());
        content2.add(content, BorderLayout.NORTH);
        waitPanel = new WaitPanel();
        waitPanel.setBackground(Color.gray);
        waitPanel.setPreferredSize(new Dimension (300,350));
        content2.add(waitPanel,BorderLayout.CENTER);
        content2.add(progressBar, BorderLayout.SOUTH);

        this.add(content2, BorderLayout.CENTER);
        this.add(stepLabel, BorderLayout.SOUTH);

    }
    void haveCartogram(boolean have){

            this.inputFileNameButton.setEnabled(have);
            this.inputFileNameTextField.setEnabled(have);
            this.runAux.setEnabled(have);
            this.repaint();

    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.inputFileNameButton) {
            String fileName = GuiUtils.chooseInputFilename(this);
            if (fileName != null) {
                this.inputFileNameTextField.setText(fileName);
                ActionEvent e2 = new ActionEvent(this, 0, fileName);
                wizard.actionPerformed(e2);
                //readShapeFile(fileName);
            }
        }
    }

    public void setFileName(String fileName) {
        this.inputFileNameTextField.setText(fileName);
    }

    public void addActionListener(ActionListener wizard) {
        this.wizard = wizard;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }



}
