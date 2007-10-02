/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class SelectionStarPlot
 Copyright (c), 2003, Frank Hardisty
 All Rights Reserved.
 Original Author: Frank Hardisty
 $Author: hardisty $
 $Id: SelectionStarPlot.java,v 1.1 2005/02/13 03:26:27 hardisty Exp $
 $Date: 2005/02/13 03:26:27 $
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
package edu.psu.geovista.geoviz.descriptive;

import java.awt.BorderLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.text.NumberFormatter;

import edu.psu.geovista.common.data.DataSetForApps;
import edu.psu.geovista.common.event.DataSetEvent;
import edu.psu.geovista.common.event.DataSetListener;
import edu.psu.geovista.common.event.SelectionEvent;
import edu.psu.geovista.common.event.SelectionListener;
import edu.psu.geovista.data.DescriptiveStatistics;
import edu.psu.geovista.data.sample.GeoData48States;

/**
 * Displays descriptive statistics about a current variable, including
 * statistics for the currently selected set.
 * 
 * @author Frank Hardisty
 * 
 */
public class DescriptiveStats extends JPanel

implements SelectionListener, DataSetListener {
	JTable table;
	DataSetForApps dataSet;
	NumberFormatter decMatter;
	JFormattedTextField meanFTF;
	JFormattedTextField stdDevFTF;
	JFormattedTextField skewnessFTF;
	JFormattedTextField kurtosisFTF;
	JFormattedTextField meanSelFTF;
	JFormattedTextField stdDevSelFTF;
	JFormattedTextField skewnessSelFTF;
	JFormattedTextField kurtosisSelFTF;

	/**
	 * 
	 */

	public DescriptiveStats() {
		DecimalFormat decimalFormat = new DecimalFormat();
		decMatter = new NumberFormatter(decimalFormat);
		decMatter.setOverwriteMode(true);
		decMatter.setAllowsInvalid(false);

		JPanel currVarPanel = new JPanel();
		currVarPanel.setLayout(new BoxLayout(currVarPanel, BoxLayout.Y_AXIS));
		currVarPanel.setBorder(BorderFactory
				.createTitledBorder("Column Stats:"));
		currVarPanel.add(this.makeMeanPanel());
		currVarPanel.add(this.makeStdDevPanel());
		currVarPanel.add(this.makeSkewnessPanel());
		currVarPanel.add(this.makeKurtosisPanel());
		this.add(currVarPanel);

		JPanel selObsPanel = new JPanel();
		selObsPanel.setLayout(new BoxLayout(selObsPanel, BoxLayout.Y_AXIS));
		selObsPanel.setBorder(BorderFactory
				.createTitledBorder("Selection Stats:"));
		selObsPanel.add(this.makeSelMeanPanel());
		selObsPanel.add(this.makeSelStdDevPanel());
		selObsPanel.add(this.makeSelSkewnessPanel());
		selObsPanel.add(this.makeSelKurtosisPanel());
		this.add(selObsPanel);

		JPanel bottomPanel = new JPanel();
		bottomPanel.add(currVarPanel);
		bottomPanel.add(selObsPanel);

		this.setLayout(new BorderLayout());
		this.add(bottomPanel, BorderLayout.SOUTH);
		JTable table = new JTable();
		JScrollPane sPane = new JScrollPane();
		sPane.add(table);
		this.add(sPane);

	}

	private JPanel makeMeanPanel() {
		JPanel meanPan = new JPanel();
		JLabel label = new JLabel("Mean:");
		meanFTF = new JFormattedTextField(decMatter);
		meanFTF.setEditable(false);
		double num = 0;
		meanFTF.setValue(num);
		meanPan.add(label);
		meanPan.add(meanFTF);
		return meanPan;
	}

	private JPanel makeStdDevPanel() {
		JPanel stdDevPan = new JPanel();
		JLabel label = new JLabel("StdDev:");
		stdDevFTF = new JFormattedTextField(decMatter);
		stdDevFTF.setEditable(false);
		double num = 0;
		stdDevFTF.setValue(num);
		stdDevPan.add(label);
		stdDevPan.add(stdDevFTF);
		return stdDevPan;
	}

	private JPanel makeSkewnessPanel() {
		JPanel skewnessPan = new JPanel();
		JLabel label = new JLabel("Skewness:");
		skewnessFTF = new JFormattedTextField(decMatter);
		skewnessFTF.setEditable(false);
		double num = 0;
		skewnessFTF.setValue(num);
		skewnessPan.add(label);
		skewnessPan.add(skewnessFTF);
		return skewnessPan;
	}

	private JPanel makeKurtosisPanel() {
		JPanel kurtosisPan = new JPanel();
		JLabel label = new JLabel("Kurtosis:");
		kurtosisFTF = new JFormattedTextField(decMatter);
		kurtosisFTF.setEditable(false);
		double num = 0;
		kurtosisFTF.setValue(num);
		kurtosisPan.add(label);
		kurtosisPan.add(kurtosisFTF);
		return kurtosisPan;
	}

	private JPanel makeSelMeanPanel() {
		JPanel meanPan = new JPanel();
		JLabel label = new JLabel("Mean:");
		meanSelFTF = new JFormattedTextField(decMatter);
		meanSelFTF.setEditable(false);
		double num = 0;
		meanSelFTF.setValue(num);
		meanPan.add(label);
		meanPan.add(meanSelFTF);
		return meanPan;
	}

	private JPanel makeSelStdDevPanel() {
		JPanel stdDevPan = new JPanel();
		JLabel label = new JLabel("StdDev:");
		stdDevSelFTF = new JFormattedTextField(decMatter);
		stdDevSelFTF.setEditable(false);
		double num = 0;
		stdDevSelFTF.setValue(num);
		stdDevPan.add(label);
		stdDevPan.add(stdDevSelFTF);
		return stdDevPan;
	}

	private JPanel makeSelSkewnessPanel() {
		JPanel skewnessPan = new JPanel();
		JLabel label = new JLabel("Skewness:");
		skewnessSelFTF = new JFormattedTextField(decMatter);
		skewnessSelFTF.setEditable(false);
		double num = 0;
		skewnessSelFTF.setValue(num);
		skewnessPan.add(label);
		skewnessPan.add(skewnessSelFTF);
		return skewnessPan;
	}

	private JPanel makeSelKurtosisPanel() {
		JPanel kurtosisPan = new JPanel();
		JLabel label = new JLabel("Kurtosis:");
		kurtosisSelFTF = new JFormattedTextField(decMatter);
		kurtosisSelFTF.setEditable(false);
		double num = 0;
		kurtosisSelFTF.setValue(num);
		kurtosisPan.add(label);
		kurtosisPan.add(kurtosisSelFTF);
		return kurtosisPan;
	}

	public void selectionChanged(SelectionEvent e) {
		int[] selection = e.getSelection();
		this.calculateSelStats(selection);

	}

	public void dataSetChanged(DataSetEvent e) {
		this.dataSet = e.getDataSetForApps();
		calculateStats();

	}

	private void calculateStats() {
		double[] data = dataSet.getNumericDataAsDouble(0);
		double meanValue = DescriptiveStatistics.mean(data);
		this.meanFTF.setValue(meanValue);
		if (this.meanFTF.getText().equals("-0")){
			this.meanFTF.setText("0");
		}
		
		double stdDev = DescriptiveStatistics.stdDev(data, false);
		this.stdDevFTF.setValue(stdDev);
		double skewness = DescriptiveStatistics.skewness(data, false);
		this.skewnessFTF.setValue(skewness);
		double kurtosis = DescriptiveStatistics.kurtosis(data, false);
		this.kurtosisFTF.setValue(kurtosis);
		this.revalidate();
	}

	private void calculateSelStats(int[] selObs) {

		double[] originalData = dataSet.getNumericDataAsDouble(0);
		double[] data = null;
		if (selObs.length > 0) {
			data = new double[selObs.length];
			int counter = 0;
			for (int i : selObs) {
				
				data[counter++] = originalData[i];
			}
		} else {
			//strangely, if we don't do this next, the figures
			//don't agree. XXX investigate why....
			this.meanSelFTF.setText(this.meanFTF.getText());
			this.stdDevSelFTF.setText(this.stdDevFTF.getText());
			this.skewnessSelFTF.setText(this.skewnessFTF.getText());
			this.kurtosisSelFTF.setText(this.kurtosisFTF.getText());
			return;
		}
		double meanValue = DescriptiveStatistics.mean(data);
		this.meanSelFTF.setValue(meanValue);
		double stdDev = DescriptiveStatistics.stdDev(data, true);
		this.stdDevSelFTF.setValue(stdDev);
		double skewness = DescriptiveStatistics.skewness(data, true);
		this.skewnessSelFTF.setValue(skewness);
		double kurtosis = DescriptiveStatistics.kurtosis(data, true);
		this.kurtosisSelFTF.setValue(kurtosis);
		this.revalidate();
	}

	public static void main(String[] args) {
		JFrame app = new JFrame("stats");
		DescriptiveStats stats = new DescriptiveStats();
		app.add(stats);
		app.pack();
		app.setVisible(true);
		GeoData48States stateData = new GeoData48States();
		DataSetEvent e = new DataSetEvent(stateData.getDataForApps(), app);
		stats.dataSetChanged(e);
	}

}
