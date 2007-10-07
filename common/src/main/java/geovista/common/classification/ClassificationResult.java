package geovista.common.classification;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public interface ClassificationResult {
    
    public String[] getAttNames();
    
    public double[] getBoundaryX();
    
    public double[] getBoundaryY();
    
    public int[] getClassificationX();
    
    public int[] getClassificationY();
    
    public String getClassifierX();
    
    public String getClassifierY();
    
    public int getNumberOfClassX();
    
    public int getNumberOfClassY();
    
    //perhaps should not be here...
    public int getNumberOfAxis();
    
    //member count
    public int getRowCount();
    
    public String getAttX();
    
    public String getAttY();
    
    public double[] getDataX();
    
    public double[] getDataY();
}
