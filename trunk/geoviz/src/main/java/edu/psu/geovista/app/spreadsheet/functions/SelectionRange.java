package edu.psu.geovista.app.spreadsheet.functions;

import java.awt.Point;

/*
 * Description: represent selected area for function evaluation
 * Date: Mar 25, 2003
 * Time: 11:14:16 PM
 * @author Jin Chen
 */

public  class SelectionRange{
        public static final int SINGLE_ROW=1;
        public static final int SINGLE_COLUMN=2;
        //public static final int
        private Point[] range;
        private int type;
/*public SelectionRange() {
        }     */

        public SelectionRange(Point[] range, int type) {
            this.range = range;
            this.type = type;
        }

        public Point[] getRange() {
            return range;
        }



        public int getType() {
            return type;
        }
        public int getRowCount() {
            int num=(int)(range[1].getX() -range[0].getX() );
            return Math.abs(num)+1;
        }
        public int getColumnCount() {
            int num=(int)(range[1].getY() -range[0].getY() );
            return Math.abs(num)+1;
        }
    }
