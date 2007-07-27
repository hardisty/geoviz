/* -------------------------------------------------------------------
 Java source file for the class CartogramViewResults
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: CartogramViewResults.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.psu.geovista.app.map.GeoMapUni;

//
//step 4: create cartogram....
//step 5: view results

/*
 * This class is the last step in the CartogramWizard
 */

public class CartogramViewResults extends JPanel {
    JLabel stepLabel;
    GeoMapUni mapInput;
    GeoMapUni mapOutput;
    Dimension mapSize = new Dimension(300, 400);
    /**
     * CartogramViewResults
     */
    public CartogramViewResults(GeoMapUni mapInput, GeoMapUni mapOutput) {
        this.mapInput = mapInput;
        this.mapOutput = mapOutput;
        mapOutput.setBorder(BorderFactory.createTitledBorder(
                "Map of Cartogram Output"));
        mapOutput.setPreferredSize(mapSize);
        stepLabel = new JLabel("Step Five: View Results.");
        stepLabel.setBackground(Color.pink);
        stepLabel.setOpaque(true);
        stepLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    }

    public void setInputMap(GeoMapUni map) {
        this.mapInput = map;
        this.mapInput.setPreferredSize(mapSize);
        this.updateLayout();
    }
    void zoomFullExtentOnDelay(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        	ex.printStackTrace();
        }

        mapInput.zoomFullExtent();
        mapOutput.zoomFullExtent();
    }
    private void updateLayout() {
        this.removeAll();
        BorderLayout border = new BorderLayout();
        this.setLayout(border);
        this.add(stepLabel, BorderLayout.SOUTH);
        JPanel mapHolder = new JPanel();
        mapHolder.add(mapInput);
        mapHolder.add(mapOutput);
        this.add(mapHolder);
    }

}
