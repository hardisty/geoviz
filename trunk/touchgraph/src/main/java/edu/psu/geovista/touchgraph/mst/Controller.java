package edu.psu.geovista.touchgraph.mst;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

import edu.psu.geovista.touchgraph.mst.gui.GraphDisplay;
import edu.psu.geovista.touchgraph.mst.gui.MainGUI;


/**
 * Title: Controller Description: Controll class for the MST program Copyright:
 * Copyright (c) 2002 Company:
 * 
 * @author Markus Svensson
 * @version 1.5
 */

public class Controller extends Thread {

	private final int LOAD_FILE = 1;

	private final int EXIT = 2;

	private final int ABOUT = 3;

	private final int HELP = 4;

	private final int SAVE = 5;

	private final int CLEAR = 6;

	private final int LANGUAGE = 7;

	private final int DRAW = 8;

	private String fileName;

	private String[] args;

	private GraphDisplay drawFrame;

	private MainGUI gui;


	private File[] fileArray;



	private int command = 0;

	private boolean process = false;

	private MinimumSpanningTree mst = null;

	Locale locale;

	ResourceBundle res;

	/**
	 * Constructor
	 * 
	 * @param The
	 *            arguments of the program
	 */
	public Controller(String[] args) {
		this.gui = new MainGUI();
		// drawFrame = new GraphDisplay();
		// drawFrame.setVisible(true);
		this.locale = Locale.getDefault();
		
		//this.gui.changeStrings(this.res);
		this.gui.setVisible(true);
		this.args = args;
		this.run();
	}

	/**
	 * Show the about dialog
	 */
	private void about() {
		
		this.process = false;
	}

	/**
	 * Quit the program
	 */
	private void quit() {
		System.exit(0);
	}

	/**
	 * Display a help file
	 */
	private void help() {

		this.process = false;
	}

	/**
	 * Select a file to load
	 * 
	 * @return The file array
	 */
	private File[] fileLoader() {
		this.gui.setStatusText(this.res.getString("Load_Status"));
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new File("."));
		int returnValue = chooser.showOpenDialog(this.gui);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			this.process = true;
			return chooser.getSelectedFiles();
		} else {
			this.gui.setStatusText(this.res.getString("Status_Waiting"));
			this.process = false;
			return null;
		}
	}

	/**
	 * Save the window contents to a file.
	 */
	private void writeFile() throws IOException {
		this.gui.setStatusText(this.res.getString("Save_Status"));
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setCurrentDirectory(new File("."));
		int returnValue = chooser.showSaveDialog(this.gui);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			FileWriter writer = new FileWriter(file);
			writer.write(this.gui.getString(), 0, this.gui.getString().length());
			writer.flush();
			writer.close();
		} else {
			this.gui.setStatusText(this.res.getString("Status_Waiting"));
		}
		this.process = false;
	}

	/**
	 * Change the language used in the application.
	 */
	private void changeLanguage() {
		if (this.gui.getLanguage() == "EN") {
			this.locale = new Locale("en", "US");
		} else if (this.gui.getLanguage() == "SE") {
			this.locale = new Locale("sv", "SE");
		}
		this.res = ResourceBundle.getBundle(
				"edu.psu.geovista.touchgraph.mst.gui.language.MSTResource", this.locale);
		this.gui.changeStrings(this.res);
		this.process = false;
	}

	/**
	 * Draw the last calculated MST
	 */
	private void draw() {
		if (this.mst == null) {
			return;
		}
		this.drawFrame = new GraphDisplay(this.mst.getVector(), this.fileName);
		this.drawFrame.setVisible(true);
		this.process = false;
	}

	/**
	 * Run method for the controller thread
	 */
	public void run() {
		if (this.args.length == 0) {
			//this.gui.appendOutPutText(this.res.getString("Startup_Message"));
			//this.gui.appendOutPutText(this.res.getString("Startup_Message_2"));
			//this.gui.appendOutPutText(this.res.getString("Startup_Message_3"));
		} else {
			for (int i = 0; i < this.args.length; i++) {
				MinimumSpanningTree mst = new MinimumSpanningTree(this.args[i]);
				mst.kruskal(null, null, null, i);
				@SuppressWarnings("unused")
				String result = mst.printMST(this.res);
				//this.gui.appendOutPutText(result);
			}
		}
		while (true) {
			//this.gui.setStatusText(this.res.getString("Status_Waiting"));
			this.command = this.gui.waitForCommand();
			if (this.command == this.LOAD_FILE) {
				this.fileArray = this.fileLoader();
				this.gui.resetCommand();
			} else if (this.command == this.SAVE) {
				try {
					this.writeFile();
				} catch (IOException ioe) {
					this.gui.appendOutPutText(this.res.getString("Save_Failed"));
				} finally {
					this.gui.resetCommand();
					this.process = false;
				}
			} else if (this.command == this.CLEAR) {
				this.gui.clearOutput();
				this.gui.resetCommand();
			} else if (this.command == this.ABOUT) {
				this.about();
				this.gui.resetCommand();
			} else if (this.command == this.HELP) {
				this.help();
				this.gui.resetCommand();
			} else if (this.command == this.LANGUAGE) {
				this.changeLanguage();
				this.gui.resetCommand();
			} else if (this.command == this.EXIT) {
				this.quit();
			} else if (this.command == this.DRAW) {
				this.draw();
				this.gui.resetCommand();
			}

			if (this.process == true) {
				this.gui.setStatusText(this.res.getString("Processing_Status"));
				for (int i = 0; i < this.fileArray.length; i++) {
					this.mst = new MinimumSpanningTree(this.fileArray[i]);
					this.mst.kruskal(null, null, null, i);
					String result = this.mst.printMST(this.res);
					this.gui.appendOutPutText(result);
					this.fileName = this.fileArray[i].getAbsolutePath();
				}
				this.gui.setStatusText(this.res.getString("Done_Status"));
			}
		}
	}
}