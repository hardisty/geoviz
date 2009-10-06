/*
 * GrandTourPanel.java
 *
 * Created on 2005年1月10日, 下午9:59
 */
/**
 *
 * @author  Yunping Liu
 */
package geovista.geoviz.radviz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import javax.swing.text.NumberFormatter;

public class GrandTourPanel extends JPanel implements ActionListener, ChangeListener, PropertyChangeListener {
    private static final int FPS_MIN = 0;
    private static final int FPS_MAX = 30;
    private static final int FPS_INIT = 15;
    private static final int LEN_MIN = 0;
    private static final int LEN_MAX = 60;
    private static final int LEN_INIT = 30;
    private int animationLength = LEN_INIT;
    private EventListenerList ell = new EventListenerList();
    private JFormattedTextField animationLengthText;
    private JSpinner fpsSpinner;
    private JSlider animationLengthSlider;
    
    /** Creates a new instance of GrandTourPanel */
    public GrandTourPanel() {
        this.setLayout(new BorderLayout());
       
        //Create the animation settings panel
        JPanel animationPanel = new JPanel();
        animationPanel.setLayout(new BoxLayout(animationPanel, BoxLayout.PAGE_AXIS));
        animationPanel.setBorder(BorderFactory.createCompoundBorder(
                                 BorderFactory.createEtchedBorder(), 
                                 BorderFactory.createEmptyBorder(10,0,5,0)));
          //                       BorderFactory.createTitledBorder("Animation Settings"),
          //                       BorderFactory.createEmptyBorder(0,0,0,0)));
        JPanel animationSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        animationSubPanel.add(new JLabel("Frames Per Second:"));
        fpsSpinner = new JSpinner(new SpinnerNumberModel(FPS_INIT,FPS_MIN,FPS_MAX,1));
        animationSubPanel.add(fpsSpinner);
        animationSubPanel.add(new JLabel(" Length:"));
        //Create the formatted text field and its formatter.
        java.text.NumberFormat integerFormat = java.text.NumberFormat.getIntegerInstance();
        NumberFormatter formatterInt = new NumberFormatter(integerFormat);
        formatterInt.setOverwriteMode(true);
        formatterInt.setAllowsInvalid(true);
        formatterInt.setMinimum(new Integer(LEN_MIN));
        formatterInt.setMaximum(new Integer(LEN_MAX));
        animationLengthText = new JFormattedTextField(formatterInt);
        animationLengthText.setValue(new Integer(animationLength));
        animationLengthText.setColumns(5); //get some space
        animationLengthText.addPropertyChangeListener(this);
        //React when the user presses Enter.
        animationLengthText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
        animationLengthText.getActionMap().put("check", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!animationLengthText.isEditValid()) { //The text is invalid.
                    Toolkit.getDefaultToolkit().beep();
                    animationLengthText.selectAll();
                } else try {                    //The text is valid,
                    animationLengthText.commitEdit();     //so use it.
                } catch (java.text.ParseException exc) { }
            }
        });
        animationSubPanel.add(animationLengthText);
        animationSubPanel.add(new JLabel("seconds"));
        animationPanel.add(animationSubPanel);
        //Create the slider.
        animationLengthSlider = new JSlider(SwingConstants.HORIZONTAL, LEN_MIN, LEN_MAX, LEN_INIT);
        animationLengthSlider.addChangeListener(this);
        //Turn on labels at major tick marks.
        animationLengthSlider.setMajorTickSpacing(30);
        animationLengthSlider.setMinorTickSpacing(10);
        animationLengthSlider.setPaintTicks(true);
        Hashtable labelTableLEN = new Hashtable();
        labelTableLEN.put(new Integer(LEN_MIN), new JLabel("0"));
        labelTableLEN.put(new Integer(LEN_MAX/2), new JLabel("30"));
        labelTableLEN.put(new Integer(LEN_MAX), new JLabel("60"));
        animationLengthSlider.setLabelTable(labelTableLEN);
        animationLengthSlider.setPaintLabels(true);
        animationLengthSlider.setValue(animationLength);
        //animationLengthSlider.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
        animationPanel.add(animationLengthSlider);
        animationPanel.setMaximumSize(new Dimension(animationPanel.getMaximumSize().width, 
                                                    animationPanel.getPreferredSize().height));
        animationPanel.setMinimumSize(new Dimension(animationPanel.getPreferredSize().width, 
                                                    animationPanel.getPreferredSize().height));
        
        //Create the Ok and Cancel buttons
        JPanel btnPane = new JPanel();
        btnPane.setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        JButton button;
        button = new JButton("Ok");
        button.setActionCommand("Ok");
        button.addActionListener(this);
        btnPane.add(button);
        button = new JButton("Cancel");
        button.setActionCommand("Cancel");
        button.addActionListener(this);
        btnPane.add(button);
        
        add(animationPanel, BorderLayout.CENTER);
        add(btnPane, BorderLayout.PAGE_END);
        
    }
    
    public int getNumberOfSeconds() {
        return animationLength;
    }
    
    public int getFPS() {
        int fps = Integer.parseInt(fpsSpinner.getValue().toString());
        return fps;
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Ok")) {
            fireAction("GrandTourPanel.OK");
        }
        else if (cmd.equals("Cancel")) {
            fireAction("GrandTourPanel.CANCEL");
        }
    }
    
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (source == animationLengthSlider) {
            if (source.getValueIsAdjusting()) {
                animationLength = source.getValue();
                animationLengthText.setText(String.valueOf(animationLength));
            }
            else { //done adjusting
                int help = source.getValue();
                if (help != animationLength) {
                    animationLength = help;
                    animationLengthText.setValue(new Integer(animationLength)); //update weight value
                }
            }
        }
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        if (e.getSource() == animationLengthText) {
            if ("value".equals(e.getPropertyName())) {
                Number value = (Number)e.getNewValue();
                if (animationLengthSlider != null && value != null) {
                    int help = value.intValue();
                    if (help != animationLength) animationLength = help;
                    if (help != animationLengthSlider.getValue()) animationLengthSlider.setValue(help);
                }
            }
        }
    }
    
    public static void main(String args[]) {
      GrandTourPanel fd = new GrandTourPanel();
      JFrame f = new JFrame("Test");
      f.getContentPane().add(fd);
      f.setSize(360, 160);
      f.setVisible(true);
      f.repaint();
    }
    
    public void addActionListener(ActionListener sl){
        ell.add(ActionListener.class, sl);
    }

    public void removeActionListener(ActionListener sl){
        ell.remove(ActionListener.class, sl);
    }
    
    public void fireAction(String command){
        Object[] listeners = ell.getListenerList();
        int numListeners = listeners.length;
        ActionEvent se = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
        for (int i = 0; i < numListeners; i++){
          if (listeners[i]==ActionListener.class){
        // pass the event to the listeners event dispatch method
            ((ActionListener)listeners[i+1]).actionPerformed(se);
          }
        }
    }
    
}
