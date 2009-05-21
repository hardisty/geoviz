/* Licensed under LGPL v. 2.1 or any later version;
 see GNU LGPL for details.
 Original Author: Xiping Dai and Frank Hardisty */

package geovista.readers.geog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import geovista.readers.FileIO;

/**

 *
 */
public class AttributeDescriptionFile {

	transient private String[] attributeDescriptions;

	public AttributeDescriptionFile() {

	}

	public AttributeDescriptionFile(String fileName) throws IOException {

		File tryIt = new File(fileName);
		boolean exists = tryIt.exists();
		if (!exists) {
			attributeDescriptions = null;
			return;
		}

		try {
			FileIO fio = new FileIO(fileName, "r");
			Vector desc = new Vector();

			while (!fio.hasReachedEOF()) {
				String line = fio.readLine();
				if (line != null) {// the line after the last line is always
									// null, but we don't want to read it.
					desc.add(line);
				}
			}
			desc.trimToSize();
			int len = desc.size();
			attributeDescriptions = new String[len];
			for (int i = 0; i < len; i++) {
				attributeDescriptions[i] = (String) desc.get(i);
			}
		}
		// catch (IOException ex) {
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public String[] getAttributeDescriptions() {
		return attributeDescriptions;
	}

	public void setAttributeDescriptions(String[] attributeDescriptions) {
		this.attributeDescriptions = attributeDescriptions;
	}

}
