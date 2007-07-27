/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Save the configure file of properties format in <user> directory
 *
 * @author: jin Chen
 * @date: Oct 20, 2003$
 * @version: 1.0
 */
package edu.psu.geovista.ui.dataloader.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurePropSavor {

	public static final boolean DEBUG = true;
	protected String userHomePath;// C:\Documents and Settings\<user>
	protected String cfgHome = "/.myApp/data/";
	protected File rootDir;// location where the file is saved
	protected String cfgFileName;
	protected Properties props = new Properties();
	final static Logger logger = Logger.getLogger(ConfigurePropSavor.class.getName());
	public ConfigurePropSavor(String cfgHome, String fileName) {
		cfgFileName = fileName;
		this.cfgHome = cfgHome;
		String userHome = System.getProperty("user.home");
		userHomePath = userHome.replace('\\', '/');
		String myHome = userHomePath + cfgHome;
		this.rootDir = new File(myHome);

		boolean created = rootDir.mkdirs();
		if (created) {
			logger.finest("create dir:" + rootDir);

		} else {
			logger.finest("Fail to create dir:" + rootDir);
		}

	}

	public void load() {
		File cfgFile = new File(rootDir, cfgFileName);
		FileInputStream in = null;
		if (cfgFile.exists() && cfgFile.isFile()) {
			try {
				in = new FileInputStream(cfgFile);
				props.load(in);
				// dp{
				if (logger.isLoggable(Level.FINEST)) {
					this.showProperty(" load properties -->");
				}// dp}
				in.close();

			} catch (FileNotFoundException e) {
				if (in != null)
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				e.printStackTrace(); // To change body of catch statement use
										// Options | File Templates.
			} catch (IOException e) {
				if (in != null)
					try {
						in.close();
						e.printStackTrace(); // To change body of catch
												// statement use Options | File
												// Templates.
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		}

	}

	private void showProperty(String msg) {
		logger.finest(this.getClass().getName() + "@"
				+ Integer.toHexString(this.hashCode()) + msg);
		Enumeration names = props.propertyNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = props.getProperty(name);
			logger.finest("  " + name + ":" + value);
		}
		logger.finest("<--");
	}

	public void save() {
		File cfgFile = new File(rootDir, cfgFileName);
		if (!cfgFile.exists() || !cfgFile.isFile()) {
			try {
				cfgFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(); // To change body of catch statement use
										// Options | File Templates.
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(cfgFile);
			// dp{
			if (logger.isLoggable(Level.FINEST)) {
				this.showProperty("Save properties --->");
				logger.finest("");

			}// dp}
			props.store(out, "-- Configure info --");
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
		} catch (IOException e) {
			if (out != null)
				try {
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace(); // To change body of catch statement
											// use Options | File Templates.
				}
			e.printStackTrace(); // To change body of catch statement use
									// Options | File Templates.
		}

	}

	public void setProperty(String key, String value) {
		this.props.setProperty(key, value);
	}

	public String getProperty(String key) {
		return this.props.getProperty(key);
	}

	public void clear() {
		this.props.clear();
	}

	public void showProperties() {
		showProps(this.props);
	}

	public static void showSystemProps() {
		Properties props = System.getProperties();
		showProps(props);
	}

	public static void showProps(Properties props) {

		Enumeration ekeys = props.keys();
		ArrayList keys = Collections.list(ekeys);
		Collections.sort(keys);
		logger.finest("Properties ->");

		for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = props.getProperty(key);
			logger.finest(key + ":" + value);

		}
		/*
		 * String key = (String) keys.nextElement(); String
		 * value=props.getProperty(key); logger.finest(key+":"+value); }
		 */
		logger.finest("<--");
	}

	public static void main(String[] args) {
		ConfigurePropSavor.showSystemProps();

		ConfigurePropSavor cps = new ConfigurePropSavor("/.pcp/data",
				"configure");
		cps.setProperty("Ob file", "c:/pcpHome/data/nci/cancer.csv");
		cps.setProperty("ObMeta file", "c:/pcpHome/data/nci/cancer_obMeta.csv");
		cps.setProperty("ObShape file", "c:/pcpHome/data/nci/cancer.shp");
		cps.save();
		cps.clear();
		cps.showProperties();
		cps.load();
		cps.showProperties();

	}
}
