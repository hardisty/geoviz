/* -------------------------------------------------------------------
 Java source file for the class TransformSerialization
 Copyright (c), 2005 Frank Hardisty
 $Author: hardistf $
 $Id: TransformSerialization.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Logger;

public class TransformSerialization {

	protected final static Logger logger = Logger.getLogger(TransformSerialization.class.getName());
    public static void writeTransform(TransformsMain trans, String dir) {
        logger.finest("start to write trans");

        String fileName = dir + "trans.xml";
        writeObject(trans, fileName);

        ArrayFloat[] xArrays = trans.retreiveX();
        for (int i = 0; i < xArrays.length; i++) {
            Object obj = xArrays[i];
            String xfileName = dir + "x" + i + ".xml";
            writeObject(obj, xfileName);
        }

        ArrayFloat[] yArrays = trans.retreiveY();
        for (int i = 0; i < yArrays.length; i++) {
            Object obj = yArrays[i];
            String yfileName = dir + "y" + i + ".xml";
            writeObject(obj, yfileName);
        }

        logger.finest("wrote trans");

    }

    static void writeObject(Object obj, String fileName) {
        try {
            XMLEncoder e = new XMLEncoder(
                    new BufferedOutputStream(
                            new FileOutputStream(fileName)));
            e.writeObject(obj);
            e.close();

        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        }

    }

    static Object readObject(String fileName) {
        try {
            XMLDecoder d = new XMLDecoder(
                    new BufferedInputStream(
                            new FileInputStream(fileName)));
            Object result = d.readObject();
            d.close();
            return result;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static TransformsMain readTransform(String dir) {
        logger.finest("start to read trans");
        Object result = readObject(dir + "trans.xml");
        TransformsMain trans = (TransformsMain) result;
        int nArrays = trans.getArrayLength();

        ArrayFloat[] xArrays = new ArrayFloat[nArrays];
        for (int i = 0; i < xArrays.length; i++) {
            String xfileName = dir + "x" + i + ".xml";
            Object obj = readObject(xfileName);
            xArrays[i] = (ArrayFloat) obj;
        }
        trans.putX(xArrays);

        ArrayFloat[] yArrays = new ArrayFloat[nArrays];
        for (int i = 0; i < yArrays.length; i++) {
            String yfileName = dir + "y" + i + ".xml";
            Object obj = readObject(yfileName);
            yArrays[i] = (ArrayFloat) obj;
        }
        trans.putY(yArrays);

        logger.finest("read trans");

        return trans;
    }
}
