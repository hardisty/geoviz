package edu.psu.geovista.geoviz.condition;


public class OneVarCondition {

	double[] dataArray;
	double[] conditionRanges;
	int[] conditionResults;

    /**Construct the application*/
    public OneVarCondition() {
    }

	public void setDataArray (double[] dataArray){
	    this.dataArray = dataArray;
	}

	public void setConditionRanges (double[] ranges){
	    this.conditionRanges = ranges;
		setConditionResults();
	}

	public int[] getConditionResults(){

		return conditionResults;
	}

	private void setConditionResults(){
		    this.conditionResults = new int[dataArray.length];
		    for (int i=0; i<dataArray.length; i++){
			    if ((dataArray[i] < conditionRanges[0]) || (dataArray[i] > conditionRanges[1])){
					conditionResults[i] = -1;
	    		} else {
					conditionResults[i] = 0;
		    	}
			}
	}

    /**Main method*/
    public static void main(String[] args) {
        new OneVarCondition();
    }
}