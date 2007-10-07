package geovista.matrix.visclass;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.EventListenerList;

import edu.psu.geovista.common.data.DataSetForApps;
import geovista.common.classification.ClassifierMLClassify;
import geovista.common.classification.MultiGaussian;
import geovista.common.classification.MultiGaussianModel;
import geovista.matrix.DistributionMatrix;

public class ClassifierMaximumLikelihood
{
 
  private String[] attributesDisplay;
  private String[] trainingAtt;
  private Vector[] trainingDataVector;
  private int classnumber = 5; // number of classes
  private MultiGaussian[] multiGaussian;
  private MultiGaussianModel multiGaussianModel;
  private ClassifierMLClassify classify;
  private boolean visualDisplay = true;
  private EventListenerList listenerListAction = new EventListenerList();
  public ClassifierMaximumLikelihood(){
  }

//before setting training data, set class number.
	public void setTrainingData(Vector[] data){

          this.trainingDataVector = data;
          this.classnumber = this.trainingDataVector.length;
          this.multiGaussianModel = new MultiGaussianModel();

		if(this.visualDisplay == true){
			this.drawVisualDisplay1D();
			ActionEvent e = new ActionEvent(this,1,"training data set");
			this.fireActionPerformed(e);
		}

	  //this.multiGaussian = new MultiGaussian[this.classnumber];
    this.multiGaussianModel.setClassNumber(this.classnumber);
    this.multiGaussianModel.setTrainingData(this.trainingDataVector);
    this.multiGaussian = this.multiGaussianModel.getMultiGaussianModel();
//	  for (int i = 0; i< this.classnumber; i ++){
//		  multiGaussian[i] = new MultiGaussian();
//		  multiGaussian[i].setTrainingData(this.trainingDataVector[i]);
//	  }
	}

	  /**
	   * @param data
	   * 
	   * This method is deprecated becuase it wants to create its very own pet
	   * DataSetForApps. This is no longer allowed, to allow for a mutable, 
	   * common data set. Use of this method may lead to unexpected
	   * program behavoir. 
	   * Please use setDataSet instead.
	   */
	  @Deprecated
	  public void setDataObject(Object[] data) {
		 this.setDataSet(new DataSetForApps(data));
	    
	  }	
	
  public void setDataSet(DataSetForApps data){

    if(this.visualDisplay == true){
      //this.drawVisualDisplay();
      ActionEvent e = new ActionEvent(this,1,"variable name set");
      this.fireActionPerformed(e);
    }

    this.classify = new ClassifierMLClassify();
    this.classify.setClassNumber(this.classnumber);
    this.classify.setClassificationModel(this.multiGaussian);
    this.classify.setDataSet(data);

//          edu.psu.geovista.common.data.DataSetForApps dataObjTransfer = new edu.psu.geovista.common.data.DataSetForApps();
//          dataObjTransfer.setDataObject(data);
//          this.dataObject = dataObjTransfer.getDataSetNumericAndSpatial();
//          this.attributesDisplay = dataObjTransfer.getAttributeNamesNumeric();
//
//		  dataArray = new double[dataObjTransfer.getNumObservations()][attributesDisplay.length];
//		  // transfer data array to double array
//		  for (int j=0;j<attributesDisplay.length;j++)
//		  {
//				  int t = 0;
//				  if (dataObject[j+1] instanceof double[]) t=0;
//				  else if (dataObject[j+1] instanceof int[]) t=1;
//			  else if (dataObject[j+1] instanceof boolean[]) t=2;
//				  for (int i=0;i<dataArray.length;i++)
//				  {
//						  switch (t)
//						  {
//								  case 0 :
//										  dataArray[i][j]=((double[])dataObject[j+1])[i];
//										  break;
//							  case 1 :
//										  dataArray[i][j]=(double)((int[])dataObject[j+1])[i];
//										  break;
//							  case 2 :
//										  dataArray[i][j]=((boolean[])dataObject[j+1])[i]?1.0:0.0;
//										  break;
//						  }
//				  }
//		  }
//		  maximumClassifier();
	}

	public void setSingleTuple(double[] tuple){
          this.classify = new ClassifierMLClassify();
          this.classify.setClassNumber(this.classnumber);
          this.classify.setClassificationModel(this.multiGaussian);
          this.classify.setSingleTuple(tuple);

		//classifyTuple(tuple);
	}

	public int getClassTuple(){
		return this.classify.getClassTuple();
                //return this.possibleClass;
	}

	public void setClassNumber(int classNumber){
		this.classnumber = classNumber;
	}

	public void setClassColors (Color[] colors){
	}

	public int[] getClassificaiton(){
          return this.classify.getClassificaiton();
          //return this.classes;
	}

	public void setVisualDisplay(boolean visualDisplay){
		this.visualDisplay = visualDisplay;
	}

	public String[] getVariableNames(){
		return this.attributesDisplay;
	}

	public Vector[] getTrainingData(){
		return this.trainingDataVector;
	}

	public void setTrainingAttributesLabels(String[] att){
		this.trainingAtt = att;
	}

//  private void maximumClassifier(){
//	  //find estimated mean and standard deviation for the underlying distribution of
//	  //each class
//	  this.classes = new int[this.dataArray.length];
//
//	  int tmpClass = 0;
//	  double[] pdfs = new double[this.classnumber];
//	  for (int i = 0; i < this.dataArray.length; i ++){
//		  for (int j = 0; j < this.classnumber; j ++){
//			  pdfs[j] = multiGaussian[j].getPDF(dataArray[i]);
//		  }
//		  //find the biggest pdf using density function of each class
//		  tmpClass = 0;
//		  for (int j = 1; j < this.classnumber; j ++){
//
//			  if (pdfs[j] > pdfs[tmpClass]){
//				  tmpClass = j;
//			  }
//		  }
//		  //assign the class information to each observation.
//		  //this.classes[i] = tmpClass+1;//class 1-5, especially for Kioloa data.
//		  this.classes[i] = tmpClass;
//	  }
//  }
//
//  private void classifyTuple(double[] tuple){
//
//	  int tmpClass = 0;
//	  double[] pdfs = new double[this.classnumber];
//
//		  for (int j = 0; j < this.classnumber; j ++){
//			  pdfs[j] = multiGaussian[j].getPDF(tuple);
//		  }
//		  //find the biggest pdf using density function of each class
//		  tmpClass = 0;
//		  for (int j = 1; j < this.classnumber; j ++){
//
//			  if (pdfs[j] > pdfs[tmpClass]){
//				  tmpClass = j;
//			  }
//		  }
//		  //assign the class information to each observation.
//		  //this.classes[i] = tmpClass+1;//class 1-5, especially for Kioloa data.
//		 possibleClass = tmpClass;
//
//  }

  //pop up 2D distribution graphs
  JFrame dummyFrame = new JFrame("Data Distribution");
  JDialog graph;
//  private void drawVisualDisplay(){
//	  //training data in classes, class 0
//	  double[][] data;
//	  int len = this.trainingDataVector[0].size();
//	  data = new double[len][1];
//	  for(int i = 0; i < len; i ++){
//		  data[i][0] = ((double[])this.trainingDataVector[0].get(i))[0];
//	  }
//	  //graph = new JDialog(dummyFrame, "Data Distribution", true);
//	  //dummyFrame.getContentPane().add(distributions);
//	  Distribution2DMatrix distributions = new Distribution2DMatrix();
//	  distributions.setBackground(Color.white);
//	  distributions.setClassColors(this.classColors);
//	  distributions.setVariableNames(this.trainingAtt);
//	  distributions.setDataVector(this.trainingDataVector);
//
//		  //graph = new JDialog(dummyFrame, "Data Distribution", true);
//	  dummyFrame.getContentPane().add(distributions);
//
//	  dummyFrame.setSize(400, 400);
//	  dummyFrame.setVisible(true);
//	  dummyFrame.setVisible(true);
//  }

  private void drawVisualDisplay1D(){
            //training data in classes, class 0
            double[][] data;
            int len = this.trainingDataVector[0].size();
            data = new double[len][1];
            for(int i = 0; i < len; i ++){
                    data[i][0] = ((double[])this.trainingDataVector[0].get(i))[0];
            }
            DistributionMatrix distributions = new DistributionMatrix();
            distributions.setBackground(Color.white);
            //distributions.setClassColors(this.classColors);
            distributions.setVariableNames(this.trainingAtt);
            distributions.setDataVector(this.trainingDataVector);

            dummyFrame.getContentPane().add(distributions);

            dummyFrame.setSize(100, 400);
            dummyFrame.setVisible(true);
            dummyFrame.setVisible(true);
    }


  /**
   * adds an ActionListener to the button
   */
  public void addActionListener (ActionListener l) {
	  listenerListAction.add(ActionListener.class, l);
  }

  /**
   * removes an ActionListener from the button
   */
  public void removeActionListener (ActionListener l) {
	  listenerListAction.remove(ActionListener.class, l);
  }

  /**
   * Notify all listeners that have registered interest for
   * notification on this event type. The event instance
   * is lazily created using the parameters passed into
   * the fire method.
   * @see EventListenerList
   */
  public void fireActionPerformed (ActionEvent e) {
	  // Guaranteed to return a non-null array
	  Object[] listeners = listenerListAction.getListenerList();
	  // Process the listeners last to first, notifying
	  // those that are interested in this event
		  ActionEvent e2 = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"OK");
	  for (int i = listeners.length - 2; i >= 0; i -= 2) {
		  if (listeners[i] == ActionListener.class) {
			  // Lazily create the event:
			  ((ActionListener)listeners[i + 1]).actionPerformed(e2);
		  }
	  }
	}
}
