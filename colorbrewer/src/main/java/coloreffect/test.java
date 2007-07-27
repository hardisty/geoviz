package coloreffect;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class test {

  public test() {

    CIECAM02toSRGB[] cIECAM02toSRGB = new CIECAM02toSRGB[16];


    CIECAM02toSRGB[] cIECAM02toSRGB_hues = new CIECAM02toSRGB[15];

/*
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(50.0, 50.0, 0, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(50.0, 50.0, 90, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(50.0, 50.0, 180, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(50.0, 50.0, 270, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(50.0, 100.0, 20.14, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(70.0, 50.0, 90, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(70.0, 70.0, 164.25, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(70.0, 70.0, 237.53, 95.02, 100.00, 108.81, 20.0, 63.6619, 1.0, 0.69, 1.0);
*/
    //4X4 grid test for yellow - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(100.0, 40.0, 90);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(100.0, 60.0, 90);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(100.0, 80.0, 90);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(100.0, 100.0, 90);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 40.0, 90);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(80.0, 60.0, 90);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(80.0, 80.0, 90);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(80.0, 100.0, 90);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(60.0, 40.0, 90);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 60.0, 90);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(60.0, 80.0, 90);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(60.0, 100.0, 90);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(40.0, 40.0, 90);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(40.0, 60.0, 90);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 80.0, 90);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(40.0, 100.0, 90);

    //4X4 grid test for yellow - II
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(120.0, 20.0, 90);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(120.0, 40.0, 90);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(120.0, 60.0, 90);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(120.0, 80.0, 90);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(100.0, 20.0, 90);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(100.0, 40.0, 90);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(100.0, 60.0, 90);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(100.0, 80.0, 90);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(80.0, 20.0, 90);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(80.0, 40.0, 90);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(80.0, 60.0, 90);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(80.0, 80.0, 90);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(60.0, 20.0, 90);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(60.0, 40.0, 90);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(60.0, 60.0, 90);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(60.0, 80.0, 90);



    //4X4 grid test for blue - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(100.0, 40.0, 237.53);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(100.0, 60.0, 237.53);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(100.0, 80.0, 237.53);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(100.0, 100.0, 237.53);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 40.0, 237.53);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(80.0, 60.0, 237.53);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(80.0, 80.0, 237.53);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(80.0, 100.0, 237.53);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(60.0, 40.0, 237.53);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 60.0, 237.53);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(60.0, 80.0, 237.53);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(60.0, 100.0, 237.53);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(40.0, 40.0, 237.53);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(40.0, 60.0, 237.53);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 80.0, 237.53);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(40.0, 100.0, 237.53);

    //4X4 grid test for blue - II
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(80.0, 35.0, 237.53);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(80.0, 50.0, 237.53);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(80.0, 65.0, 237.53);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(80.0, 80.0, 237.53);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(65.0, 35.0, 237.53);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(65.0, 50.0, 237.53);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(65.0, 65.0, 237.53);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(65.0, 80.0, 237.53);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(50.0, 35.0, 237.53);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(50.0, 50.0, 237.53);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(50.0, 60.0, 237.53);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(50.0, 80.0, 237.53);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(35.0, 35.0, 237.53);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(35.0, 50.0, 237.53);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(35.0, 60.0, 237.53);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(35.0, 80.0, 237.53);

    //4X4 grid test for red - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(100.0, 40.0, 20.14);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(100.0, 60.0, 20.14);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(100.0, 80.0, 20.14);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(100.0, 100.0, 20.14);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 40.0, 20.14);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(80.0, 60.0, 20.14);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(80.0, 80.0, 20.14);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(80.0, 100.0, 20.14);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(60.0, 40.0, 20.14);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 60.0, 20.14);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(60.0, 80.0, 20.14);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(60.0, 100.0, 20.14);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(40.0, 40.0, 20.14);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(40.0, 60.0, 20.14);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 80.0, 20.14);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(40.0, 100.0, 20.14);

    //4X4 grid test for red - II
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(100.0, 70.0, 20.14);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(100.0, 80.0, 20.14);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(100.0, 90.0, 20.14);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(100.0, 100.0, 20.14);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 70.0, 20.14);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(80.0, 80.0, 20.14);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(80.0, 90.0, 20.14);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(80.0, 100.0, 20.14);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(60.0, 70.0, 20.14);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 80.0, 20.14);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(60.0, 90.0, 20.14);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(60.0, 100.0, 20.14);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(40.0, 70.0, 20.14);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(40.0, 80.0, 20.14);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 90.0, 20.14);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(40.0, 100.0, 20.14);

    //4X4 grid test for green - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(100.0, 40.0, 164.25);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(100.0, 60.0, 164.25);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(100.0, 80.0, 164.25);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(100.0, 100.0, 164.25);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 40.0, 164.25);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(80.0, 60.0, 164.25);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(80.0, 80.0, 164.25);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(80.0, 100.0, 164.25);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(60.0, 40.0, 164.25);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 60.0, 164.25);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(60.0, 80.0, 164.25);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(60.0, 100.0, 164.25);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(40.0, 40.0, 164.25);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(40.0, 60.0, 164.25);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 80.0, 164.25);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(40.0, 100.0, 164.25);

    //4X4 grid test for green - II
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(80.0, 20.0, 164.25);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(80.0, 40.0, 164.25);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(80.0, 60.0, 164.25);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(80.0, 80.0, 164.25);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(60.0, 20.0, 164.25);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(60.0, 40.0, 164.25);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(60.0, 60.0, 164.25);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(60.0, 80.0, 164.25);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(40.0, 20.0, 164.25);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(40.0, 40.0, 164.25);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(40.0, 60.0, 164.25);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(40.0, 80.0, 164.25);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(20.0, 20.0, 164.25);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(20.0, 40.0, 164.25);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(20.0, 60.0, 164.25);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(20.0, 80.0, 164.25);

    //4X4 grid test for qua/seq - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(80.0, 20.0, 20.14);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(60.0, 40.0, 20.14);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(40.0, 60.0, 20.14);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(20.0, 80.0, 20.14);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 20.0, 90);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(60.0, 40.0, 90);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(40.0, 60.0, 90);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(20.0, 80.0, 90);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(80.0, 20.0, 164.25);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 40.0, 164.25);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(40.0, 60.0, 164.25);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(20.0, 80.0, 164.25);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(80.0, 20.0, 237.53);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(60.0, 40.0, 237.53);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 60.0, 237.53);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(20.0, 80.0, 237.53);

    //4X4 grid test for qua/seq - II
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(80.0, 20.0, 0);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(60.0, 40.0, 0);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(40.0, 60.0, 0);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(20.0, 80.0, 0);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 20.0, 90);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(60.0, 40.0, 90);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(40.0, 60.0, 90);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(20.0, 80.0, 90);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(80.0, 20.0, 180);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 40.0, 180);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(40.0, 60.0, 180);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(20.0, 80.0, 180);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(80.0, 20.0, 270);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(60.0, 40.0, 270);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 60.0, 270);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(20.0, 80.0, 270);

    //4X4 grid test for Dim surroundings - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(80.0, 20.0, 20.14);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(60.0, 40.0, 20.14);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(40.0, 60.0, 20.14);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(20.0, 80.0, 20.14);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 20.0, 90);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(60.0, 40.0, 90);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(40.0, 60.0, 90);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(20.0, 80.0, 90);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(80.0, 20.0, 164.25);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 40.0, 164.25);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(40.0, 60.0, 164.25);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(20.0, 80.0, 164.25);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(80.0, 20.0, 237.53);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(60.0, 40.0, 237.53);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 60.0, 237.53);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(20.0, 80.0, 237.53);

    //4X4 grid test for Dark surroundings - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(80.0, 20.0, 20.14);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(60.0, 40.0, 20.14);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(40.0, 60.0, 20.14);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(20.0, 80.0, 20.14);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 20.0, 90);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(60.0, 40.0, 90);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(40.0, 60.0, 90);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(20.0, 80.0, 90);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(80.0, 20.0, 164.25);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 40.0, 164.25);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(40.0, 60.0, 164.25);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(20.0, 80.0, 164.25);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(80.0, 20.0, 237.53);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(60.0, 40.0, 237.53);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 60.0, 237.53);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(20.0, 80.0, 237.53);

    //4X4 grid test for constant hues - I
    cIECAM02toSRGB[0] = new CIECAM02toSRGB(100.0, 40.0, 360);
    cIECAM02toSRGB[1] = new CIECAM02toSRGB(100.0, 60.0, 360);
    cIECAM02toSRGB[2] = new CIECAM02toSRGB(100.0, 80.0, 360);
    cIECAM02toSRGB[3] = new CIECAM02toSRGB(100.0, 100.0, 360);
    cIECAM02toSRGB[4] = new CIECAM02toSRGB(80.0, 40.0, 360);
    cIECAM02toSRGB[5] = new CIECAM02toSRGB(80.0, 60.0, 360);
    cIECAM02toSRGB[6] = new CIECAM02toSRGB(80.0, 80.0, 360);
    cIECAM02toSRGB[7] = new CIECAM02toSRGB(80.0, 100.0, 360);
    cIECAM02toSRGB[8] = new CIECAM02toSRGB(60.0, 40.0, 360);
    cIECAM02toSRGB[9] = new CIECAM02toSRGB(60.0, 60.0, 360);
    cIECAM02toSRGB[10] = new CIECAM02toSRGB(60.0, 80.0, 360);
    cIECAM02toSRGB[11] = new CIECAM02toSRGB(60.0, 100.0, 360);
    cIECAM02toSRGB[12] = new CIECAM02toSRGB(40.0, 40.0, 360);
    cIECAM02toSRGB[13] = new CIECAM02toSRGB(40.0, 60.0, 360);
    cIECAM02toSRGB[14] = new CIECAM02toSRGB(40.0, 80.0, 360);
    cIECAM02toSRGB[15] = new CIECAM02toSRGB(40.0, 100.0, 360);

    //creating a spectrum of every 5 degrees all around
    for(int i = 0; i < 72; i ++){

    }

    JFrame f = new JFrame();
    f.setVisible(true);
    //f.getContentPane().setLayout(new GridLayout(4, 4));
    //f.setSize(400, 400);
    //f.getContentPane().setLayout(new GridLayout(20, 72));
    //f.setSize(7200, 400);

    f.getContentPane().setLayout(new GridLayout(15, 15));
    f.setSize(1500, 300);

/*
    for(int i = 0; i < 16; i ++){
      JPanel p = new JPanel();
      p.setBackground(new Color((int)cIECAM02toSRGB[i].R255, (int)cIECAM02toSRGB[i].G255, (int)cIECAM02toSRGB[i].B255));
      f.getContentPane().add(p);

    }
*/

/*
    for(int i = 0; i < 20; i ++){
      for(int j = 0; j < 72; j ++){
        JPanel p = new JPanel();
        cIECAM02toSRGB_allaround[i] = new CIECAM02toSRGB(i*5 + 20, 60.0, j*5);
        p.setBackground(new Color((int)cIECAM02toSRGB_allaround[i].R255, (int)cIECAM02toSRGB_allaround[i].G255, (int)cIECAM02toSRGB_allaround[i].B255));
        f.getContentPane().add(p);

      }
    }
*/



/*
    for(int i = 0; i < 20; i ++){
      for(int j = 0; j < 72; j ++){
        JPanel p = new JPanel();
        cIELabToSRGB_allaround[i] = new CIELabToSRGB(i*5 + 20, 80*Math.cos(Math.toRadians(j*5)), 80*Math.sin(Math.toRadians(j*5)));
        p.setBackground(new Color((int)cIELabToSRGB_allaround[i].R255, (int)cIELabToSRGB_allaround[i].G255, (int)cIELabToSRGB_allaround[i].B255));
        f.getContentPane().add(p);
      }
    }
*/

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 5);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }


    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 20);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 40);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 70);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 90);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 115);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 130);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 164);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 185);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 210);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 238);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 265);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 285);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 315);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }

    for(int i = 0; i < 15; i ++){
      JPanel p = new JPanel();
      cIECAM02toSRGB_hues[i] = new CIECAM02toSRGB(85 - i*3, 70, 345);
      p.setBackground(new Color((int)cIECAM02toSRGB_hues[i].R255, (int)cIECAM02toSRGB_hues[i].G255, (int)cIECAM02toSRGB_hues[i].B255));
      f.getContentPane().add(p);
    }



/*
    for(int i = 0; i < 20; i ++){
      for(int j = 0; j < 72; j ++){
        JPanel p = new JPanel();
        cIELUVtoSRGB_allaround[i] = new CIELUVtoSRGB(i*5 + 20, 80*Math.cos(Math.toRadians(j*5)), 80*Math.sin(Math.toRadians(j*5)));
        p.setBackground(new Color((int)cIELUVtoSRGB_allaround[i].R255, (int)cIELUVtoSRGB_allaround[i].G255, (int)cIELUVtoSRGB_allaround[i].B255));
        f.getContentPane().add(p);
      }
    }
*/

    f.repaint();
    f.validate();

  }



}