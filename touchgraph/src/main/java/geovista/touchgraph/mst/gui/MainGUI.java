package geovista.touchgraph.mst.gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ResourceBundle;

/**
 * Title:        MainGUI
 * Description:  This is the main gui class for the MST Java Project.
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Markus Svensson
 * 
 */

public class MainGUI extends JFrame {

  private final int LOAD_FILE = 1;
  private final int EXIT = 2;
  private final int ABOUT = 3;
  private final int HELP = 4;
  private final int SAVE = 5;
  private final int CLEAR = 6;
  private final int LANGUAGE = 7;
  private final int DRAW = 8;

  private BorderLayout mainLayout = new BorderLayout();
  private BorderLayout mainPanelLayout = new BorderLayout();
  private JLabel statusBar = new JLabel();
  private JPanel mainPanel = new JPanel();
  private JTextArea output = new JTextArea();
  private JScrollPane scroller = new JScrollPane(output);
  private JPanel topPanel = new JPanel();
  private FlowLayout topLayout = new FlowLayout();
  private JButton drawButton = new JButton();
  private JButton quitButton = new JButton();
  private JButton clearButton = new JButton();
  private JButton saveButton = new JButton();
  private JButton loadButton = new JButton();
  private JMenuBar mainMenu = new JMenuBar();
  private JMenu fileMenu = new JMenu();
  private JMenu helpMenu = new JMenu();
  private JMenuItem loadMenuItem = new JMenuItem();
  private JMenuItem saveMenuItem = new JMenuItem();
  private JMenuItem clearMenuItem = new JMenuItem();
  private JMenuItem quitMenuItem = new JMenuItem();
  private JMenuItem helpMenuItem = new JMenuItem();
  private JMenuItem aboutMenuItem = new JMenuItem();
  private JPopupMenu popupMenu = new JPopupMenu();
  private JMenuItem loadPopUp = new JMenuItem();
  private JMenuItem savePopUp = new JMenuItem();
  private JMenuItem clearPopUp = new JMenuItem();
  private JMenuItem quitPopUp = new JMenuItem();
  private JMenu languagesMenu = new JMenu();
  private JMenuItem enMenuItem = new JMenuItem();
  private JMenuItem seMenuItem = new JMenuItem();
  private String language;
  private int command = 0;

  /**
   * Constructor
   */
  public MainGUI() {
      jbInit();
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      this.setSize(450, 400);
      this.setLocation((screen.width - 450) / 2, (screen.height - 400) / 2);
  }

  /**
   * Init the GUI
   */
  private void jbInit() {
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setJMenuBar(mainMenu);
    this.setTitle("MST Java V1.5");
    this.getContentPane().setLayout(mainLayout);
    mainLayout.setHgap(5);
    mainLayout.setVgap(5);
    mainPanelLayout.setHgap(5);
    mainPanelLayout.setVgap(5);
    mainPanel.setLayout(mainPanelLayout);
    mainPanel.setBackground(Color.lightGray);
    statusBar.setBackground(Color.lightGray);
    statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
    scroller.setAutoscrolls(true);
    scroller.setBorder(BorderFactory.createEtchedBorder());
    output.setBorder(null);
    output.setDoubleBuffered(true);
    output.setEditable(false);
    output.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        output_mouseReleased(e);
      }
    });
    topPanel.setBackground(Color.lightGray);
    topPanel.setBorder(BorderFactory.createEtchedBorder());
    topPanel.setLayout(topLayout);
    quitButton.setBackground(Color.lightGray);
    quitButton.setActionCommand("quitButton");
    quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        quitButton_mouseReleased(e);
      }
    });
    clearButton.setBackground(Color.lightGray);
    clearButton.setActionCommand("clearButton");
    clearButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        clearButton_mouseReleased(e);
      }
    });
    saveButton.setBackground(Color.lightGray);
    saveButton.setActionCommand("saveButton");
    saveButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        saveButton_mouseReleased(e);
      }
    });
    loadButton.setBackground(Color.lightGray);
    loadButton.setActionCommand("loadButton");
    loadButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        loadButton_mouseReleased(e);
      }
    });
    mainMenu.setBackground(Color.lightGray);
    fileMenu.setBackground(Color.lightGray);
    fileMenu.setBorder(null);
    helpMenu.setBackground(Color.lightGray);
    helpMenu.setBorder(null);
    loadMenuItem.setBackground(Color.lightGray);
    loadMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        loadMenuItem_mouseReleased(e);
      }
    });
    saveMenuItem.setBackground(Color.lightGray);
    saveMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        saveMenuItem_mouseReleased(e);
      }
    });
    clearMenuItem.setBackground(Color.lightGray);
    clearMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        clearMenuItem_mouseReleased(e);
      }
    });
    quitMenuItem.setBackground(Color.lightGray);
    quitMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        quitMenuItem_mouseReleased(e);
      }
    });
    helpMenuItem.setBackground(Color.lightGray);
    helpMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        helpMenuItem_mouseReleased(e);
      }
    });
    aboutMenuItem.setBackground(Color.lightGray);
    aboutMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        aboutMenuItem_mouseReleased(e);
      }
    });
    popupMenu.setBackground(Color.lightGray);
    loadPopUp.setBackground(Color.lightGray);
    loadPopUp.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        loadPopUp_mouseReleased(e);
      }
    });
    savePopUp.setBackground(Color.lightGray);
    savePopUp.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        savePopUp_mouseReleased(e);
      }
    });
    clearPopUp.setBackground(Color.lightGray);
    clearPopUp.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        clearPopUp_mouseReleased(e);
      }
    });
    quitPopUp.setBackground(Color.lightGray);
    quitPopUp.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        quitPopUp_mouseReleased(e);
      }
    });
    languagesMenu.setBackground(Color.lightGray);
    enMenuItem.setBackground(Color.lightGray);
    enMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        enMenuItem_mouseReleased(e);
      }
    });
    seMenuItem.setBackground(Color.lightGray);
    seMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        seMenuItem_mouseReleased(e);
      }
    });
    drawButton.setBackground(Color.lightGray);
    drawButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        drawButton_mouseReleased(e);
      }
    });
    mainPanel.add(scroller, BorderLayout.CENTER);
    mainPanel.add(topPanel, BorderLayout.NORTH);
    topPanel.add(loadButton, null);
    topPanel.add(saveButton, null);
    topPanel.add(drawButton, null);
    topPanel.add(clearButton, null);
    topPanel.add(quitButton, null);
    this.getContentPane().add(statusBar,  BorderLayout.SOUTH);
    this.getContentPane().add(mainPanel,  BorderLayout.CENTER);
    mainMenu.add(fileMenu);
    mainMenu.add(languagesMenu);
    mainMenu.add(helpMenu);
    fileMenu.add(loadMenuItem);
    fileMenu.add(saveMenuItem);
    fileMenu.add(clearMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(quitMenuItem);
    helpMenu.add(helpMenuItem);
    helpMenu.add(aboutMenuItem);
    popupMenu.add(loadPopUp);
    popupMenu.add(savePopUp);
    popupMenu.add(clearPopUp);
    popupMenu.add(quitPopUp);
    languagesMenu.add(enMenuItem);
    languagesMenu.add(seMenuItem);
  }

  /**
   * Handle to right mouse button event for the output pane
   * to display the popup menu.
   * @param The event
   */
  void output_mouseReleased(MouseEvent e) {
    if(e.isMetaDown())
      popupMenu.show(e.getComponent(), e.getX(), e.getY());
  }

  /**
   * Event handler for the draw button.
   * @param The event
   */
  synchronized void drawButton_mouseReleased(MouseEvent e) {
    this.command = DRAW;
    notifyAll();
  }

  /**
   * Event handler for the quit button
   * @param The event
   */
  synchronized void quitButton_mouseReleased(MouseEvent e) {
    this.command = EXIT;
    notifyAll();
  }

  /**
   * Event handler for the clear button
   * @param The event
   */
  synchronized void clearButton_mouseReleased(MouseEvent e) {
    this.command = CLEAR;
    notifyAll();
  }

  /**
   * Event handler for the save button
   * @param The event
   */
  synchronized void saveButton_mouseReleased(MouseEvent e) {
    this.command = SAVE;
    notifyAll();
  }

  /**
   * Event handler for the load button
   * @param The event
   */
  synchronized void loadButton_mouseReleased(MouseEvent e) {
    this.command = LOAD_FILE;
    notifyAll();
  }

  /**
   * Event handler for the load menu item
   * @param The event
   */
  synchronized void loadMenuItem_mouseReleased(MouseEvent e) {
    this.command = LOAD_FILE;
    notifyAll();
  }

  /**
   * Event handler for the save menu item
   * @param The event
   */
  synchronized void saveMenuItem_mouseReleased(MouseEvent e) {
    this.command = SAVE;
    notifyAll();
  }

  /**
   * Event handler for the clear menu item
   * @param The event
   */
  synchronized void clearMenuItem_mouseReleased(MouseEvent e) {
    this.command = CLEAR;
    notifyAll();
  }

  /**
   * Event handler for the quit menu item
   * @param The event
   */
  synchronized void quitMenuItem_mouseReleased(MouseEvent e) {
    this.command = EXIT;
    notifyAll();
  }

  /**
   * Event handler for the about menu item
   * @param The event
   */
  synchronized void aboutMenuItem_mouseReleased(MouseEvent e) {
    this.command = ABOUT;
    notifyAll();
  }

  /**
   * Event handler for the help menu item
   * @param The event
   */
  synchronized void helpMenuItem_mouseReleased(MouseEvent e) {
    this.command = HELP;
    notifyAll();
  }

  /**
   * Set language to English
   */
  synchronized void enMenuItem_mouseReleased(MouseEvent e) {
    this.language = "EN";
    this.command = LANGUAGE;
    notifyAll();
  }

  /**
   * Set langauage to Swedish
   */
  synchronized void seMenuItem_mouseReleased(MouseEvent e) {
    this.language = "SE";
    this.command = LANGUAGE;
    notifyAll();
  }

  /**
   * Handle event for the load item on the popup menu
   * @param The event
   */
  synchronized void loadPopUp_mouseReleased(MouseEvent e) {
    this.command = LOAD_FILE;
    notifyAll();
  }

  /**
   * Handle event for the save item on the popup menu
   * @param The event
   */
  synchronized void savePopUp_mouseReleased(MouseEvent e) {
    this.command = SAVE;
    notifyAll();
  }

  /**
   * Handle event for the clear item on the popup menu
   * @param The event
   */
  synchronized void clearPopUp_mouseReleased(MouseEvent e) {
    this.command = CLEAR;
    notifyAll();
  }

  /**
   * Handle event for the quit item on the popup menu
   * @param The event
   */
  synchronized void quitPopUp_mouseReleased(MouseEvent e) {
    this.command = EXIT;
    notifyAll();
  }

  /**
   * Set the text in the statusbar
   * @param The text
   */
  public void setStatusText(String text){
    statusBar.setText(text);
  }


  /**
   * Wait for a command to become available
   * @return The command
   */
  public synchronized int waitForCommand(){
    while(this.command == 0)
      try{
        wait();
      }catch(InterruptedException ie) {};
    return this.command;
  }

  /**
   * Reset the command to zero
   */
  public synchronized void resetCommand(){
    this.command = 0;
  }

  /**
   * Return the contents of the window as a string
   * @return The string
   */
  public String getString(){
    return this.output.getText();
  }

  /**
   * Clear the output area
   */
  public void clearOutput(){
    this.output.setText("");
  }

  /**
   * Set the text in the output area
   * @param The text
   */
  public void setNewText(String text){
    this.output.setText(text);
  }

  /**
   * Append text to the output area
   * @param The output
   */
  public void appendOutPutText(String output){
    this.output.append(output);
  }

  /**
   * Get the language
   * @return The language
   */
  public String getLanguage(){
    return language;
  }

  /**
   * Set the language of the main interface
   * @param The resource bundle
   * @param The locale
   */
  public synchronized void changeStrings(ResourceBundle res){
    fileMenu.setText(res.getString("File_Title"));
    drawButton.setText(res.getString("Draw_Title"));
    quitButton.setText(res.getString("Quit_Title"));
    clearButton.setText(res.getString("Clear_Title"));
    saveButton.setText(res.getString("Save_Title"));
    loadButton.setText(res.getString("Load_Title"));
    helpMenu.setText(res.getString("Help_Title"));
    loadMenuItem.setText(res.getString("Load_Title"));
    saveMenuItem.setText(res.getString("Save_Title"));
    clearMenuItem.setText(res.getString("Clear_Title"));
    quitMenuItem.setText(res.getString("Quit_Title"));
    helpMenuItem.setText(res.getString("Help_Title"));
    aboutMenuItem.setText(res.getString("About_Title"));
    loadPopUp.setText(res.getString("Load_Title"));
    savePopUp.setText(res.getString("Save_Title"));
    clearPopUp.setText(res.getString("Clear_Title"));
    quitPopUp.setText(res.getString("Quit_Title"));
    languagesMenu.setText(res.getString("Language_Title"));
    enMenuItem.setText(res.getString("English_Title"));
    seMenuItem.setText(res.getString("Swedish_Title"));
    drawButton.setToolTipText(res.getString("Draw_Tooltip"));
    statusBar.setToolTipText(res.getString("Status_Tooltip"));
    quitButton.setToolTipText(res.getString("Quit_Tooltip"));
    clearButton.setToolTipText(res.getString("Clear_Tooltip"));
    saveButton.setToolTipText(res.getString("Save_Tooltip"));
    loadButton.setToolTipText(res.getString("Load_Tooltip"));
  }
}
