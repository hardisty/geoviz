package geovista.touchgraph.mst;

import geovista.touchgraph.mst.gui.GraphDisplay;
import geovista.touchgraph.mst.gui.MainGUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;

/**
 * Title: Controller Description: Controll class for the MST program Copyright:
 * Copyright (c) 2002 Company:
 * 
 * @author Markus Svensson
 * 
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

	private final String[] args;

	private GraphDisplay drawFrame;

	private final MainGUI gui;

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
		gui = new MainGUI();
		// drawFrame = new GraphDisplay();
		// drawFrame.setVisible(true);
		locale = Locale.getDefault();

		// this.gui.changeStrings(this.res);
		gui.setVisible(true);
		this.args = args;
		run();
	}

	/**
	 * Show the about dialog
	 */
	private void about() {

		process = false;
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

		process = false;
	}

	/**
	 * Select a file to load
	 * 
	 * @return The file array
	 */
	private File[] fileLoader() {
		gui.setStatusText(res.getString("Load_Status"));
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.OPEN_DIALOG);
		chooser.setMultiSelectionEnabled(true);
		chooser.setCurrentDirectory(new File("."));
		int returnValue = chooser.showOpenDialog(gui);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			process = true;
			return chooser.getSelectedFiles();
		} else {
			gui.setStatusText(res.getString("Status_Waiting"));
			process = false;
			return null;
		}
	}

	/**
	 * Save the window contents to a file.
	 */
	private void writeFile() throws IOException {
		gui.setStatusText(res.getString("Save_Status"));
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setCurrentDirectory(new File("."));
		int returnValue = chooser.showSaveDialog(gui);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			FileWriter writer = new FileWriter(file);
			writer.write(gui.getString(), 0, gui.getString().length());
			writer.flush();
			writer.close();
		} else {
			gui.setStatusText(res.getString("Status_Waiting"));
		}
		process = false;
	}

	/**
	 * Change the language used in the application.
	 */
	private void changeLanguage() {
		if (gui.getLanguage() == "EN") {
			locale = new Locale("en", "US");
		} else if (gui.getLanguage() == "SE") {
			locale = new Locale("sv", "SE");
		}
		res = ResourceBundle.getBundle(
				"geovista.touchgraph.mst.gui.language.MSTResource", locale);
		gui.changeStrings(res);
		process = false;
	}

	/**
	 * Draw the last calculated MST
	 */
	private void draw() {
		if (mst == null) {
			return;
		}
		drawFrame = new GraphDisplay(mst.getVector(), fileName);
		drawFrame.setVisible(true);
		process = false;
	}

	/**
	 * Run method for the controller thread
	 */
	@Override
	public void run() {
		if (args.length == 0) {
			// this.gui.appendOutPutText(this.res.getString("Startup_Message"));
			// this.gui.appendOutPutText(this.res.getString("Startup_Message_2"));
			// this.gui.appendOutPutText(this.res.getString("Startup_Message_3"));
		} else {
			for (int i = 0; i < args.length; i++) {
				MinimumSpanningTree mst = new MinimumSpanningTree(args[i]);
				MinimumSpanningTree.kruskal(null, null, null, i);
				@SuppressWarnings("unused")
				String result = mst.printMST(res);
				// this.gui.appendOutPutText(result);
			}
		}
		while (true) {
			// this.gui.setStatusText(this.res.getString("Status_Waiting"));
			command = gui.waitForCommand();
			if (command == LOAD_FILE) {
				fileArray = fileLoader();
				gui.resetCommand();
			} else if (command == SAVE) {
				try {
					writeFile();
				} catch (IOException ioe) {
					gui.appendOutPutText(res.getString("Save_Failed"));
				} finally {
					gui.resetCommand();
					process = false;
				}
			} else if (command == CLEAR) {
				gui.clearOutput();
				gui.resetCommand();
			} else if (command == ABOUT) {
				about();
				gui.resetCommand();
			} else if (command == HELP) {
				help();
				gui.resetCommand();
			} else if (command == LANGUAGE) {
				changeLanguage();
				gui.resetCommand();
			} else if (command == EXIT) {
				quit();
			} else if (command == DRAW) {
				draw();
				gui.resetCommand();
			}

			if (process == true) {
				gui.setStatusText(res.getString("Processing_Status"));
				for (int i = 0; i < fileArray.length; i++) {
					mst = new MinimumSpanningTree(fileArray[i]);
					MinimumSpanningTree.kruskal(null, null, null, i);
					String result = mst.printMST(res);
					gui.appendOutPutText(result);
					fileName = fileArray[i].getAbsolutePath();
				}
				gui.setStatusText(res.getString("Done_Status"));
			}
		}
	}
}