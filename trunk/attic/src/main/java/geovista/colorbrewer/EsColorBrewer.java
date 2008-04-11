/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Reserved.
 *
 * The purpose of the class is to make it easier to integerate ColorBrewer into ESTAT
 * @author: jin Chen 
 * @date: Jan 5, 2005$
 * 
 */
package geovista.colorbrewer;




import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import geovista.common.event.ClassNumberEvent;

public class EsColorBrewer extends ColorBrewerPlus {

    protected  void init(){
        //init uni colors
      uniSeq.doClick();
      this.currentUnivariateScheme = COLOR_SCHEME_NAME_BUPU;
      this.showUnivariateScheme(this.currentUnivariateScheme);
      this.updateRecentUnivariateScheme();
      this.suitabilityCheck();


      biSeqSeq.doClick();
      this.showBivariateScheme("seqseqnongraydiamond1");
      this.currentBivariateScheme = "seqseqnongraydiamond1";
      this.updateRecentBivariateScheme();


  }
   //jin: called whenever a classification(1D or 2D...)  is generated
   public void classNumberChanged(ClassNumberEvent e){

       //whenever a new classNumber change, reset color scheme and broadcast
        //this.showUnivariateScheme(this.currentBivariateScheme );    \
       //only can one of the showXXXScheme method?
       int[] bivariateClassNumber = e.getBivariateClassNumber();
       if(bivariateClassNumber==null||bivariateClassNumber.length <=0){//it is a Uni-variate classification
                 showUnivariateScheme(this.currentUnivariateScheme);
       }
       else{//it is a Bi-variate classification ( assuming at this point no multivariate classification available)

        this.showBivariateScheme(currentBivariateScheme);
       }
       super.classNumberChanged(e);
   }
   public String getCurrentUnivariateScheme() {
        return currentUnivariateScheme;
    }

    public void setCurrentUnivariateScheme(String currentUnivariateScheme) {
        this.currentUnivariateScheme = currentUnivariateScheme;
    }
    public void setUni(String name){
        if(name.equals("seq") ){
            this.uniSeq.setSelected(true);
        }
        else if(name.equals("div")) {
            this.uniDiv.setSelected(true);
        }
        else if(name.equals("qua")) {
            this.uniQua.setSelected(true);
        }

    }

    public void showMyColorScheme(){
        this.ranking = 1;
        this.showUnivariateScheme("YlGnBu");
        this.updateRecentUnivariateScheme();
        this.suitabilityCheck();
        this.uniSeq.setSelected(true);
    }


   public static void main(String[] args) {
    JFrame app = new JFrame();
    app.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });

    EsColorBrewer cb = new EsColorBrewer();
    cb.setUni("seq");
    cb.setCurrentUnivariateScheme("YlGnBu");
    ClassNumberEvent e=new ClassNumberEvent(new Object(),3);
    cb.classNumberChanged(e);
    cb.showMyColorScheme();
      // this.showUnivariateScheme(this.currentUnivariateScheme);
       // cb.updateRecentUnivariateScheme();
        //cb.suitabilityCheck();
       // cb.uniSeq.setSelected(true);
        //cb.repaint();

    app.getContentPane().add(cb);
    app.pack();
    app.setVisible(true);
  }
}