package edu.psu.geovista.data.condition;


public class MultiVarCondition {

	Object[] dataArray;
	Object[] conditionRanges;
	int[] conditionResults;
	int[] tempConditioning;
	double[] dataConditioned;
	double[] conditionRange;

    /**Construct the application*/
    public MultiVarCondition() {
    }

	public void setDataArray (Object[] dataArray){
	    this.dataArray = dataArray;
	}

	public void setConditionRanges (Object[] ranges){
	    this.conditionRanges = ranges;
		setConditionResults();
	}

	public int[] getConditionResults(){

		return conditionResults;
	}

	private void setConditionResults(){
		int len = ((double[])dataArray[0]).length;
		this.conditionResults = new int[len];
		this.tempConditioning = new int[len];
		for (int i=0; i<dataArray.length; i++){
			this.dataConditioned = (double[])dataArray[i];
			this.conditionRange = (double[])conditionRanges[i];
			for(int j = 0; j < len; j ++){
			    if ((this.dataConditioned[j] < this.conditionRange[0]) || (this.dataConditioned[j] > conditionRange[1])){
					tempConditioning[i] = -1;
	    		} else {
					tempConditioning[i] = 0;
		    	}
				this.conditionResults[j] = (this.conditionResults[j] + 1) * (tempConditioning[i] + 1) - 1;
			}
		}
	}
}