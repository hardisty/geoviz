package geovista.category;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * 
 */

public class ErrorMatrixPane extends JPanel {
    private BorderLayout borderLayout1 = new BorderLayout();
	private JLabel evaluatedImageLabel;
	private JLabel referenceImageLabel;

	private String evaluatedImage;
	private String referenceImage;

	private JPanel overallAccuracyPane;
	private ErrorMatrix errorMatrix;
	private int[] evaluatedClassResult;
	private int[] referenceClass;

        private int[] categoryIDNumber;
        private Vector errorMatrixVector;


    public ErrorMatrixPane() {
        try {
            jbInit();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    void jbInit() throws Exception {
//      matrixLayout = new GridBagLayout();
//      c = new GridBagConstraints();
//      this.setLayout(matrixLayout);
//      c.fill = GridBagConstraints.BOTH;

        this.setLayout(borderLayout1);


        this.evaluatedImage = new String("Image to be evaluated");
        this.evaluatedImageLabel = new JLabel(this.evaluatedImage);

        this.referenceImage = new String("Reference image");
        this.referenceImageLabel = new JLabel(this.referenceImage);
        AffineTransform trans = new AffineTransform();
        trans.rotate(-Math.PI/2, this.referenceImageLabel.getWidth(), this.referenceImageLabel.getHeight()/2);
        Font font = new Font("", Font.BOLD, 11);
        font = font.deriveFont(trans);
        this.referenceImageLabel.setFont(font);

        this.overallAccuracyPane = new JPanel();

//        c.weightx = 0.0;
//        c.weighty = 0.0;
//        c.gridwidth = 1;
//        c.gridheight = 1;
//        matrixLayout.setConstraints(this.emptyLabel, c);
//        add(this.emptyLabel);
//
//        c.weightx = 1.0;
//        c.weighty = 0.0;
//        c.gridwidth = 4;
//        c.gridheight = 1;
//        matrixLayout.setConstraints(this.evaluatedImageLabel, c);
//        add(this.evaluatedImageLabel);
//
//        c.weightx = 1.0;
//        c.weighty = 0.0;
//        c.gridwidth = 1;
//        c.gridheight = 4;
//        matrixLayout.setConstraints(this.referenceImageLabel, c);
//        add(this.referenceImageLabel);
//
//        c.weightx = 1.0;
//        c.weighty = 0.0;
//        c.gridwidth = 4;
//        c.gridheight = 4;
//        matrixLayout.setConstraints(this.overallAccuracyPane, c);
//        add(this.overallAccuracyPane);


//        this.matrixPane = new JPanel();

//        this.matrixPane.add(this.referenceImageLabel, BorderLayout.CENTER);
//        this.matrixPane.add(this.overallAccuracyPane, BorderLayout.EAST);
//
        this.add(this.evaluatedImageLabel, BorderLayout.NORTH);
//        this.add(this.matrixPane, BorderLayout.CENTER);
        this.add(this.overallAccuracyPane, BorderLayout.SOUTH);

    }

    private void init(){
      this.overallAccuracyPane.removeAll();
      int row;
      int col;
      this.errorMatrix = new ErrorMatrix();
      this.errorMatrixVector = this.errorMatrix.updatematrix(this.referenceClass, this.evaluatedClassResult, this.categoryIDNumber);
      row = this.errorMatrixVector.size();
      col = ((Vector)this.errorMatrixVector.elementAt(0)).size();
      this.overallAccuracyPane.setLayout(new GridLayout(row, col));

      for (int i = 0; i < row; i ++){
        for (int j = 0; j < col; j ++){
          String value = (String) ((Vector)this.errorMatrixVector.get(i)).get(j);
          JLabel valueLabel = new JLabel(value);
          this.overallAccuracyPane.add(valueLabel);
        }

      }
    }

    public void setUpErrorMatrix(int[] referenceClass, int[] evaluatedClassResult, int[] categoryIDNumber){
      this.referenceClass = referenceClass;
      this.evaluatedClassResult = evaluatedClassResult;
      this.categoryIDNumber = categoryIDNumber;
      this.init();
    }
}
