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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

//
//step 4: create cartogram....create auxilliary shapefiles here

/*
 * This class is the fourth step in the CartogramWizard
 */

public class CartogramCreate extends JPanel implements ActionListener {
    JLabel stepLabel;

    ActionListener wizard;
    JProgressBar progressBar;
    WaitPanel waitPanel;

    /**
     * CartogramCreate
     */
    public CartogramCreate(ActionListener wizard) {
        this.wizard = wizard;
        initGui();
    }

    private void initGui() {
        this.removeAll();

        stepLabel = new JLabel(
                "Step Four: Create cartogram.");
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepLabel.setBackground(Color.PINK);
        stepLabel.setOpaque(true);
        //stepLabel.setForeground(Color.PINK);
        BorderLayout border = new BorderLayout();
        this.setLayout(border);



        progressBar = new JProgressBar();
       JPanel content = new JPanel();
        JPanel content2 = new JPanel();
        content2.setLayout(new BorderLayout());
        content2.add(content, BorderLayout.NORTH);
        waitPanel = new WaitPanel();

        waitPanel.setPreferredSize(new Dimension (500,400));
        content2.add(waitPanel,BorderLayout.CENTER);
        content2.add(progressBar, BorderLayout.SOUTH);

        this.add(content2, BorderLayout.CENTER);
        this.add(stepLabel, BorderLayout.SOUTH);

    }

    public void actionPerformed(ActionEvent e) {

    }


    public void addActionListener(ActionListener wizard) {
        this.wizard = wizard;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
    public void startWaiting(){
        this.waitPanel.startWaiting();
    }
    public void stopWaiting(){
        this.waitPanel.stopWaiting();
    }


}
