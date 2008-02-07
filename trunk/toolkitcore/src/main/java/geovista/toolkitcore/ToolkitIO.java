/* -------------------------------------------------------------------
 Java source file for the class ToolkitIO
 Copyright (c), 2005 Ke Liao, Frank Hardisty
 $Author: hardisty $
 $Id: ToolkitIO.java,v 1.6 2005/04/11 17:52:14 hardisty Exp $
 $Date: 2005/04/11 17:52:14 $
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

package geovista.toolkitcore;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.NodeList;

import com.thoughtworks.xstream.XStream;

import geovista.coordination.CoordinationManager;
import geovista.coordination.FiringBean;
import geovista.readers.util.MyFileFilter;
import geovista.toolkitcore.marshal.GeoMapConverter;
import geovista.toolkitcore.marshal.GeoVizToolkitConverter;
import geovista.toolkitcore.marshal.IndicationAnimatorConverter;
import geovista.toolkitcore.marshal.JInternalFrameConverter;
import geovista.toolkitcore.marshal.JPanelConverter;
import geovista.toolkitcore.marshal.SingleHistogramConverter;
import geovista.toolkitcore.marshal.StarPlotConverter;
import geovista.toolkitcore.marshal.StarPlotMapConverter;
import geovista.toolkitcore.marshal.ToolkitBeanConverter;
import geovista.toolkitcore.marshal.VariablePickerConverter;

public class ToolkitIO {
	protected final static Logger logger = Logger
			.getLogger(ToolkitIO.class.getName());
	public static final int ACTION_OPEN = 0;
	public static final int ACTION_SAVE = 1;

	public static final int FILE_TYPE_LAYOUT = 0;
	public static final int FILE_TYPE_SHAPEFILE = 1;

	private static String LAYOUT_DIR = "LastGoodLayoutDirectory";
	private static String SHAPEFILE_DIR = "LastGoodFileDirectory";
	private static String IMAGE_DIR = "LastGoodImageDirectory";
	public static String dataSetPathFromXML = " ";

	void initXStream() {
		XStream xstream = new XStream();
		xstream.registerConverter(new GeoMapConverter());
		xstream.registerConverter(new JPanelConverter(xstream.getMapper(),
				xstream.getReflectionProvider()));
		xstream.registerConverter(new JInternalFrameConverter());
		xstream.registerConverter(new StarPlotConverter());
		xstream.registerConverter(new StarPlotMapConverter());
		xstream.registerConverter(new IndicationAnimatorConverter());
		xstream.registerConverter(new SingleHistogramConverter());
		xstream.registerConverter(new VariablePickerConverter());
		xstream.registerConverter(new GeoVizToolkitConverter());
		xstream.registerConverter(new ToolkitBeanConverter());

	}

	public static void saveImageToFile(Component c) {
		BufferedImage buff = new BufferedImage(c.getWidth(), c.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = buff.getGraphics();
		c.paint(g);
		Preferences gvPrefs = Preferences
				.userNodeForPackage(ToolkitBeanSet.class);
		String defaultDir = gvPrefs.get(ToolkitIO.IMAGE_DIR, "");
		JFileChooser chooser = new JFileChooser(defaultDir);
		MyFileFilter myff = new MyFileFilter("png");
		chooser.setFileFilter(myff);

		int returnVal = chooser.showSaveDialog(c);
		File fi = null;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			chooser.getSelectedFile();
			String fileName = chooser.getSelectedFile().getPath();

			if (!fileName.contains(".png")) {
				fileName = fileName + ".png";
			}
			fi = new File(fileName);
			try {
				ImageIO.write(buff, "png", fi);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			gvPrefs.put(ToolkitIO.IMAGE_DIR, fi.getPath());

		}

	}

	public static String getFileName(Component parent, int action, int fileType) {
		Preferences gvPrefs = Preferences
				.userNodeForPackage(ToolkitBeanSet.class);
		String fullFileName = null;

		try {
			// LookAndFeel laf = UIManager.getLookAndFeel();
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			String defaultDir = "";
			MyFileFilter fileFilter = null;
			// we want to get the last good directory of the type we are looking
			// for
			if (fileType == ToolkitIO.FILE_TYPE_LAYOUT) {
				defaultDir = gvPrefs.get(ToolkitIO.LAYOUT_DIR, "");
				fileFilter = new MyFileFilter("xml");
			} else if (fileType == ToolkitIO.FILE_TYPE_SHAPEFILE) {
				defaultDir = gvPrefs.get(ToolkitIO.SHAPEFILE_DIR, "");
				fileFilter = new MyFileFilter(new String[] { "shp", "dbf",
						"csv" });
			}
			JFileChooser fileChooser = new JFileChooser(defaultDir);
			fileChooser.setFileFilter(fileFilter);
			int returnVal = JFileChooser.CANCEL_OPTION;
			if (action == ToolkitIO.ACTION_OPEN) {
				returnVal = fileChooser.showOpenDialog(parent);

			} else if (action == ToolkitIO.ACTION_SAVE) {
				returnVal = fileChooser.showSaveDialog(parent);
				File tempFile = fileChooser.getSelectedFile();
				if (tempFile.exists()) {
					logger.finest("this files exists!");

					int erase;
					String[] choices = { "Erase existing one",
							"Choose another name", "Return without saving" };
					erase = JOptionPane.showOptionDialog(parent,
							"This fils exists, you want to:",
							"Warning: name conflicts",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE, null, choices,
							"Choose another name");

					if (erase == JOptionPane.NO_OPTION) {
						GeoVizToolkit gvt = (GeoVizToolkit) parent;
						ToolkitIO.writeLayout(gvt.getFileName(),
								gvt.tBeanSet, parent);
					} else if (erase == JOptionPane.CANCEL_OPTION) {
						return null;
					}
				}
			}
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = null;
				file = fileChooser.getSelectedFile();
				fullFileName = file.getAbsolutePath();
				String path = file.getPath();
				logger.finest("path = " + path);
				logger.finest("absolutePath = " + file.getAbsolutePath());

				if (fileType == ToolkitIO.FILE_TYPE_LAYOUT) {
					gvPrefs.put(ToolkitIO.LAYOUT_DIR, path);
				} else if (fileType == ToolkitIO.FILE_TYPE_SHAPEFILE) {
					gvPrefs.put(ToolkitIO.SHAPEFILE_DIR, path);
				}
			}

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}
		return fullFileName;

	}

	public static void writeLayout(String dataSetFullName,
			ToolkitBeanSet tBeanSet, Component parent) {
		String xmlFullName = ToolkitIO.getFileName(parent,
				ToolkitIO.ACTION_SAVE, ToolkitIO.FILE_TYPE_LAYOUT);
		if (xmlFullName == null) {
			return;
		}
		int periodPlace = xmlFullName.lastIndexOf(".");
		String extension = null;
		if (periodPlace == -1) {
			extension = "";
		} else {
			extension = xmlFullName
					.substring(periodPlace, xmlFullName.length());
		}
		logger.finest("extension = " + extension);
		if (extension.compareToIgnoreCase(".xml") != 0) {
			xmlFullName = xmlFullName + ".xml";
		}
		// ### write xml and label it with the name given by the user ###//
		Document doc = new Document();
		Element rootEle = new Element("GeoVizTool_BeansLayout");
		rootEle.setAttribute("dataSetFullName", dataSetFullName);

		Vector beanElements = new Vector();
		Element beanElement = new Element("bean");
		int x, y, wd, ht, beanCount = 0;
		ToolkitBean temp;
		Iterator itBeans = tBeanSet.iterator();
		while (itBeans.hasNext()) {
			temp = (ToolkitBean) itBeans.next();
			x = temp.getInternalFrame().getX();
			y = temp.getInternalFrame().getY();
			wd = temp.getInternalFrame().getWidth();
			ht = temp.getInternalFrame().getHeight();
			beanElement.setText(temp.getOriginalBean().getClass().getName());
			beanElement.setAttribute("index", Integer.toString(beanCount));
			beanElement.setAttribute("xLocation", Integer.toString(x));
			beanElement.setAttribute("yLocation", Integer.toString(y));
			beanElement.setAttribute("width", Integer.toString(wd));
			beanElement.setAttribute("height", Integer.toString(ht));

			beanElement.setAttribute("uniqueName", temp.getUniqueName());
			beanElement.addContent("\n  ");
			// rootEle.addContent(beanCount, beanElement);
			beanElements.add(beanCount, beanElement);
			beanElement = new Element("bean");

			beanCount++;
		}
		rootEle.setContent(beanElements);
		doc.setRootElement(rootEle);
		XMLOutputter outp = new XMLOutputter();

		// XMLOutputter outp = new XMLOutputter(" ", true);
		// ## set indent besides new lines, not implemented in 1.O though
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(xmlFullName);
			outp.output(doc, fos);
			Preferences gvPrefs = Preferences
					.userNodeForPackage(ToolkitBeanSet.class);
			if (xmlFullName != null) {
				File fi = new File(xmlFullName);
				String path = fi.getAbsolutePath();
				gvPrefs.put("LastGoodLayoutDirectory", path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static ToolkitBeanSet openLayout(InputStream is) {
		Document docIn = readDocument(is);

		ToolkitBeanSet beanSet = new ToolkitBeanSet();
		Element rootE = docIn.getRootElement();
		dataSetPathFromXML = (rootE.getAttribute("dataSetFullName").getValue());
		logger.finest("xml read dataset as:");
		Vector allChildren = new Vector(rootE.getChildren("bean"));
		for (int i = 0; i < allChildren.size(); i++) {
			Element tempBean = (Element) (allChildren.get(i));
			String className = tempBean.getText();
			ToolkitBean newToolkitBean = null;
			JInternalFrame bInterFrame = null;
			CoordinationManager coord = new CoordinationManager();
			className = className.trim();

			try {

				Object newBean = GeoVizToolkit.makeObject(className);
				FiringBean newFBean = coord.addBean(newBean);
				String uniqueName = newFBean.getBeanName();

				newToolkitBean = new ToolkitBean(newBean, uniqueName);

				bInterFrame = newToolkitBean.getInternalFrame();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

			int x = Integer.parseInt(tempBean.getAttributeValue("xLocation"));
			int y = Integer.parseInt(tempBean.getAttributeValue("yLocation"));
			int width = Integer.parseInt(tempBean.getAttributeValue("width"));
			int height = Integer.parseInt(tempBean.getAttributeValue("height"));

			bInterFrame.setSize(width, height);
			bInterFrame.setLocation(x, y);
			bInterFrame.setPreferredSize(new Dimension(width, height));

			beanSet.add(newToolkitBean);

		}

		// this.repaint();
		return beanSet;

	}

	private static Document readDocument(InputStream is) {
		Document docIn = null;
		// #### READ XML THROUGH SAXBUILDER ####//
		try {

			SAXBuilder sb = new SAXBuilder();
			docIn = sb.build(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return docIn;
	}

	private static ToolkitBeanSet openLayout(String xmlFullName) {
		FileInputStream fis = null;

		if (xmlFullName == null) {
			return null;
		}
		try {
			fis = new FileInputStream(xmlFullName);

			return ToolkitIO.openLayout(fis);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static ToolkitBeanSet openDefaultLayout() {
		InputStream inStream = null;
		try {
			Class cl = ToolkitIO.class;

			inStream = cl.getResourceAsStream("resources/default.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ToolkitIO.openLayout(inStream);
	}

	public static ToolkitBeanSet openStarPlotMapLayout() {
		InputStream inStream = null;
		try {
			Class cl = ToolkitIO.class;

			inStream = cl.getResourceAsStream("resources/starmap.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ToolkitIO.openLayout(inStream);
	}

	public static ToolkitBeanSet openAllComponentsLayout() {
		InputStream inStream = null;
		try {
			Class cl = ToolkitIO.class;

			inStream = cl.getResourceAsStream("resources/new_all.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ToolkitIO.openLayout(inStream);
	}

	public static ToolkitBeanSet openLayout(Component parent) {

		String xmlFullName = ToolkitIO.getFileName(parent,
				ToolkitIO.ACTION_OPEN, ToolkitIO.FILE_TYPE_LAYOUT);
		return ToolkitIO.openLayout(xmlFullName);
	}

	public static void copyComponentImageToClipboard(Component c) {
		BufferedImage buff = new BufferedImage(c.getWidth(), c.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = buff.getGraphics();
		c.paint(g);
		ToolkitIO.sendImageToClipboard(buff);
	}

	/*
	 * public static void saveCommentedImage(Component c) { float
	 * COMPRESSION_QUALITY = 0.95F; BufferedImage buff = new
	 * BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
	 * Graphics g = buff.getGraphics(); c.paint(g);
	 * 
	 * String outputFileName = "C:\\test.jpg"; InputStream inStream = null; try {
	 * Class cl = ToolkitIO.class;
	 * 
	 * inStream = cl.getResourceAsStream("resources/default.xml"); } catch
	 * (Exception ex) { ex.printStackTrace(); } Document doc =
	 * ToolkitIO.readDocument(inStream); // Get core JPEG writer. Iterator
	 * writers = ImageIO.getImageWritersByFormatName("jpeg"); ImageWriter writer =
	 * ImageIO.getImageWritersByFormatName("jpeg").next(); while
	 * (writers.hasNext()) { writer = (ImageWriter) writers.next(); if
	 * (writer.getClass().getName().startsWith( "javax_imageio_jpeg_image_1.0")) { //
	 * Break on finding the core provider. break; } } if (writer == null) {
	 * System.err.println("Cannot find core JPEG writer!"); } // Set the
	 * compression level. ImageWriteParam writeParam =
	 * writer.getDefaultWriteParam();
	 * writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
	 * writeParam.setCompressionQuality(COMPRESSION_QUALITY); RenderedImage
	 * image = buff; ImageTypeSpecifier spec = ImageTypeSpecifier
	 * .createFromBufferedImageType(BufferedImage.TYPE_INT_ARGB); // construct
	 * the image metadata. // IIOMetadata imageMetadata =
	 * writer.getDefaultImageMetadata(imageType, // param) IIOMetadata
	 * imageMetadata = writer.getDefaultImageMetadata(spec, null); //
	 * IIOMetadata imageMetadata = new JPEGMetadata(); // add something...
	 * IIOMetadataNode commentNode = new IIOMetadataNode("com");
	 * commentNode.setAttribute("comment", doc.toString());
	 * logger.finest(""+imageMetadata.isStandardMetadataFormatSupported()); try {
	 * IIOMetadataNode root = (IIOMetadataNode) imageMetadata
	 * .getAsTree("javax_imageio_jpeg_image_1.0"); //
	 * root.setAttribute("comment", "Anthony is in trouble"); //
	 * root.setTextContent("this is text content");
	 * 
	 * Vector vec = new Vector(); Element el = new Element("AString");
	 * vec.add(el); // Element el2 = new Element("anotherstring"); //
	 * vec.add(el2); // Document doc = new Document(vec); //
	 * root.setUserObject("Anthony.... this had better be good"); //
	 * root.setUserObject(doc);
	 *  // root.setUserObject(someBytes); NodeList rootnl =
	 * root.getChildNodes(); // root.getLastChild().appendChild(commentNode);
	 * for (int i = 0; i < rootnl.getLength(); i++) { Node nod = rootnl.item(i);
	 * logger.finest("***"); logger.finest("nod " + i); logger.finest("nod name " +
	 * nod.getNodeName()); logger.finest("nod value " + nod.getNodeValue());
	 * IIOMetadataNode iioNod = (IIOMetadataNode) nod; logger.finest("iiomnod
	 * comment " + iioNod.getAttribute("comment")); } logger.finest("***");
	 * logger.finest("root comment " + root.getAttribute("Comment"));
	 * logger.finest("***"); logger.finest("root text content " +
	 * root.getTextContent()); logger.finest("***"); logger.finest("root
	 * userObject " + root.getUserObject()); //
	 * imageMetadata.setFromTree("javax_imageio_jpeg_image_1.0", root); //
	 * imageMetadata.mergeTree("javax_imageio_jpeg_image_1.0", // commentNode);
	 * 
	 * IIOMetadataNode newRoot = (IIOMetadataNode) imageMetadata
	 * .getAsTree("javax_imageio_jpeg_image_1.0"); NodeList nl =
	 * newRoot.getChildNodes();
	 * 
	 * for (int i = 0; i < nl.getLength(); i++) { logger.finest("***");
	 * logger.finest("new nod " + i); Node nod = nl.item(i); logger.finest("new
	 * nod name " + nod.getNodeName()); logger.finest("new nod value " +
	 * nod.getNodeValue()); IIOMetadataNode iioNod = (IIOMetadataNode) nod;
	 * logger.finest("new iiomnod comment " + iioNod.getAttribute("Comment")); }
	 * logger.finest("***"); logger.finest("new root comment " +
	 * newRoot.getAttribute("Comment")); logger.finest("***"); System.out
	 * .println("new root text content " + root.getTextContent());
	 * logger.finest("***"); logger.finest("new root userObject " +
	 * root.getUserObject()); } catch (Exception e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } // Set the output stream, write the
	 * image try {
	 * 
	 * writer .setOutput(new FileImageOutputStream(new File( outputFileName))); //
	 * writer.replaceImageMetadata(0,imageMetadata); writer.write(null, new
	 * IIOImage(image, null, imageMetadata), writeParam); writer.dispose(); }
	 * catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); } }
	 */
	public static String openCommentedImage(String fileName) {

		fileName = "C:\\test.jpg";

		// Get core JPEG reader.
		Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpeg").next();
		while (readers.hasNext()) {
			reader = (ImageReader) readers.next();
			if (reader.getClass().getName().startsWith("com.sun.imageio")) {
				// Break on finding the core provider.
				break;
			}
		}
		if (reader == null) {
			System.err.println("Cannot find core JPEG reader!");
		}

		String comment = null;
		// Use Java Image I/O directly
		// Set the input stream.
		try {
			reader.setInput(new FileImageInputStream(new File(fileName)));
			// Get the image metadata.
			IIOMetadata imageMetadata = reader.getImageMetadata(0);
			String[] jpegFormats = imageMetadata.getMetadataFormatNames();
			for (String s : jpegFormats) {
				logger.finest(s);
			}
			// get that **** comment

			IIOMetadataNode nod = (IIOMetadataNode) imageMetadata
					.getAsTree("javax_imageio_jpeg_image_1.0");
			IIOMetadataNode chNod = (IIOMetadataNode) nod.getLastChild();
			NodeList nodList = chNod.getChildNodes();
			for (int i = 0; i < nodList.getLength(); i++) {
				IIOMetadataNode child = (IIOMetadataNode) nodList.item(i);
				logger.finest("child " + i + " value = " + child.getNodeName());
				if (child.getNodeName().equals("com")) {
					logger.finest("this is the one");
					logger.finest(child.getAttribute("comment"));
				}
			}

			logger.finest("user object from file");
			logger.finest("object " + nod.getUserObject());
			logger.finest("text content from file");
			logger.finest(nod.getTextContent());
			// comment = (String) nod.getUserObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return comment;
	}

	// I got this method and the next class from an online article in devx.
	// the code was available for download with no copyright
	// notice, so I judge the risk of problems to be small - Frank
	public static void sendImageToClipboard(Image image) {
		ImageSelection imageSelection = new ImageSelection(image);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
				imageSelection, null);
	}

	// Inner class is used to hold an image while on the clipboard.
	public static class ImageSelection implements Transferable {
		// the Image object which will be housed by the ImageSelection
		private final Image image;

		public ImageSelection(Image image) {
			this.image = image;
		}

		// Returns the supported flavors of our implementation
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}

		// Returns true if flavor is supported
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return DataFlavor.imageFlavor.equals(flavor);
		}

		// Returns Image object housed by Transferable object
		public Object getTransferData(DataFlavor flavor)
				throws UnsupportedFlavorException, IOException {
			if (!DataFlavor.imageFlavor.equals(flavor)) {
				throw new UnsupportedFlavorException(flavor);
			}
			// else return the payload
			return image;
		}

	}

	public static void main(String[] args) {
		JFrame app = new JFrame("image test");
		JPanel pan = new JPanel();
		pan.setBackground(Color.pink);
		// gvt.setVisible(true);
		pan.setMinimumSize(new Dimension(300, 300));
		pan.setPreferredSize(new Dimension(300, 300));
		app.add(pan);
		app.pack();
		app.setVisible(true);
		// ToolkitIO.saveCommentedImage(app);
		logger.finest(ToolkitIO.openCommentedImage(""));
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
