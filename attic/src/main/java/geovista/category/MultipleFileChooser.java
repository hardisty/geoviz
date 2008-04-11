package geovista.category;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * 
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import geovista.readers.util.MyFileFilter;

public class MultipleFileChooser extends JPanel implements ActionListener {


  transient private static final String[] modes = {"Open", "Save"};
  protected static final int            OPEN_MODE = 0;
  protected static final int            SAVE_MODE = 1;

  public static final String            COMMAND_GET_FILE = "getFile";
  public static final String		COMMAND_FILE_SELECTED		 = "flSlctd";

  private String FileName;
  protected JTextField                  textfield;
  protected JComboBox                   combo;
  protected JButton                     selectButton;
  transient protected JFileChooser      chooser;
  transient protected File[]              selectedFiles;
  transient private String filesPath;
  // Event list to handle beans listening to us
  private EventListenerList listenerList;


  public MultipleFileChooser() {
    super();
    setLayout(new BorderLayout());
    // filename field
    JPanel filenamePanel = new JPanel();
    filenamePanel.setLayout(new BorderLayout());
    filenamePanel.add(new JLabel("File Name:", JLabel.RIGHT), BorderLayout.WEST);
    this.textfield = new JTextField();
    filenamePanel.add(this.textfield, BorderLayout.CENTER);
    add(filenamePanel, BorderLayout.NORTH);

    // open/save selector
    this.combo = new JComboBox(modes);
    this.combo.setSelectedIndex(0);
    this.combo.setEditable(false);
    add(this.combo, BorderLayout.WEST);

    // file chooser button
    this.selectButton = new JButton("Select");
    this.selectButton.addActionListener(this);
    add(this.selectButton, BorderLayout.EAST);

    initialize();
  }

  private void initialize() {

            FileName ="D:\\";
            MyFileFilter filter = new MyFileFilter("IMG");
            File file = new File(this.textfield.getText());
            this.chooser = file.exists() ? new JFileChooser(file) : new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setDialogTitle("Load Data");
            chooser.setCurrentDirectory(new File(FileName));
            chooser.setApproveButtonText("Load");
            chooser.setFileFilter(filter);

//            File[] files;
//            int returnVal = chooser.showOpenDialog(null);
//            if(returnVal == JFileChooser.APPROVE_OPTION) {
//                     files =  chooser.getSelectedFiles();
//            }
//            else return;
            this.listenerList = new EventListenerList();
  }

  public void actionPerformed(ActionEvent e) {
         Object obj = e.getSource();
         String modeStr = (String) this.combo.getSelectedItem();
         filesPath = new String();
         int mode = (modeStr.equals(modes[0])) ? OPEN_MODE : SAVE_MODE;

         if (obj == this.selectButton) {
                 int state = JFileChooser.CANCEL_OPTION;
                 if (mode == OPEN_MODE) { // open
                         state = this.chooser.showOpenDialog(null);
                 } else {			// save
                         state = this.chooser.showSaveDialog(null);
                 }
                 File[] f;
                 if (state == JFileChooser.APPROVE_OPTION) {
                         f = this.chooser.getSelectedFiles();
                         // display filename
                         if (f != null){
                           for (int i = 0; i < f.length; i ++){
                             filesPath = filesPath + f[i].getPath() + " ";
                           }
                         }else filesPath = "";
                         this.textfield.setText(filesPath);
                         if ((mode == OPEN_MODE && f[0].exists()) ||
                                 (mode == SAVE_MODE)) {
                                 this.selectedFiles = f;
                                 // notify listeners.
                                 fireActionPerformed(COMMAND_GET_FILE);
                         } else {
                                 this.selectedFiles = null;
                         }
                 } else {
                         this.selectedFiles = null;
                 }
         }
 }


 public void setFilename(String s) {
         textfield.setText(s);
 }

 public String getFileNames() {
     return textfield.getText();
 }


 public File[] getFiles() {
         return this.selectedFiles;
 }

 public String getAbsolutePath() {
         return this.filesPath;
 }

/**
* adds an ActionListener to the button
*/
public void addActionListener(ActionListener l) {
 listenerList.add(ActionListener.class, l);
}

/**
* removes an ActionListener from the button
*/
public void removeActionListener(ActionListener l) {
 listenerList.remove(ActionListener.class, l);
}

/**
* Notify all listeners that have registered interest for
* notification on this event type. The event instance
* is lazily created using the parameters passed into
* the fire method.
* @see EventListenerList
*/
private void fireActionPerformed(String command) {
 // Guaranteed to return a non-null array
 Object[] listeners = listenerList.getListenerList();
 ActionEvent e = null;
 // Process the listeners last to first, notifying
 // those that are interested in this event
 for (int i = listeners.length - 2; i >= 0; i -= 2) {
     if (listeners[i]==ActionListener.class) {
         // Lazily create the event:
         if (e == null) {
             e = new ActionEvent(this,
                                 ActionEvent.ACTION_PERFORMED,
                                 command);
         }
         ((ActionListener)listeners[i+1]).actionPerformed(e);
     }
 }
}

  /**
   * Serialization methods
   */
  private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
              initialize();
  }


  /* ------------------------- test method -------------------------- */
  public static void main(String[] args) {
          JFrame frame = new JFrame();
          frame.setSize(200, 100);

          MultipleFileChooser sfio = new MultipleFileChooser();
          frame.getContentPane().add(sfio);
          frame.setVisible(true);
  }



}
