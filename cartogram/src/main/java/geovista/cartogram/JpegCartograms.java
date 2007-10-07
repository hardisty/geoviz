/* -------------------------------------------------------------------
 Java source file for the class MapGenFile
  Original Author: Frank Hardisty
  $Author: hardistf $
  $Id: ComparableShapes.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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

import java.awt.Shape;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

import edu.psu.geovista.geoviz.map.GeoMap;
import edu.psu.geovista.geoviz.sample.GeoData48States;
import geovista.common.data.DataSetForApps;

/*
 *
 */

public class JpegCartograms{
	public static boolean DEBUG = true;
	final static Logger logger = Logger.getLogger(JpegCartograms.class.getName());
	public static void main(String[] args){
GeoData48States data = new GeoData48States();
		DataSetForApps dataSet = data.getDataForApps();		
		
		int nSteps = 3;//we save a jpeg for each step. 
		int firstVar = 0;
		int secondVar = 1;

		double[] firstDat = dataSet.getNumericDataAsDouble(firstVar);
		double[] secondDat = dataSet.getNumericDataAsDouble(secondVar);
		double[] interpolatedData = new double[firstDat.length];
		
		double[] proportions = new double[firstDat.length];
		for (int i = 0; i < firstDat.length; i++){
			proportions[i] = (secondDat[i] - firstDat[i])/(nSteps-1);
		}
		if(JpegCartograms.logger.isLoggable(Level.FINEST)){
			logger.finest(" firstDat[0] = " + firstDat[0]);
			logger.finest(" secondDat[0] = " + secondDat[0]);
		}
		Shape[] originalShapes = dataSet.getShapeData();
		String[] strings = {"data"};
		Object[] newData = {strings, null, originalShapes};
		JProgressBar bar = new JProgressBar();
		TransformsMain trans = new TransformsMain(false);
		trans.setMaxNSquareLog(10);
		GeoMap map = new GeoMap();
		JFrame frame = new JFrame();
		for (int step = 0; step < nSteps; step++){
			
			for (int i = 0; i < firstDat.length; i++){
				
				interpolatedData[i] = firstDat[i] + (proportions[i] * step);
			}
			newData[1]= interpolatedData;
			//XXX trying not to use this cntr
			DataSetForApps newDataSet = new DataSetForApps(newData);
			
			@SuppressWarnings("unused")
			//I think a side effect of this is to do the work
			Shape[] transShapes = MapGenFile.createTempFilesAndCartogram(bar, newDataSet, 0, trans);
			
			
			logger.finest("step " + step + " interpolatedData[i] = " + interpolatedData[0]);
		}
		frame.getContentPane().add(map);
		frame.pack();
		frame.setVisible(true);
	}
	
}
