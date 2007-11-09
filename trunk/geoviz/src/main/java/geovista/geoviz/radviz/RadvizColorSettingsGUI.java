package geovista.geoviz.radviz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

public class RadvizColorSettingsGUI extends javax.swing.JPanel implements
       ActionListener, ChangeListener, MouseListener
{
    //Bean properties etc
    public final static String[] SCHEME_NAMES = {"Set1", "Pastel1", "Set2", "Pastel2", "Dark2", "Set3", "Paired", "Accents"};
    /*public final static String SET1 = "Set1";
    public final static String PASTEL1 = "Pastel1";
    public final static String SET2 = "Set2";
    public final static String PASTEL2 = "Pastel2";
    public final static String DARK2 = "Dark2";
    public final static String SET3 = "Set3";
    public final static String PAIRED = "Paired";
    public final static String ACCENTS = "Accents";*/
    
    private EventListenerList ell = new EventListenerList();
    private int alpha = 255;
    private int numClass = 1;
    private int pointSize = 1;
    private JButton okBtn, cancelBtn;
    private JRadioButton[] schemes = new JRadioButton[SCHEME_NAMES.length];
    private JLabel backgroundColorLabel, selectionColorLabel;
    private JLabel[] schemeLegendLabels;
    private JPanel schemeLegendPanel, symbolPanel;
    private JSpinner alphaSpinner;
    private JSpinner pointSizeSpinner;
    private String colorScheme = SCHEME_NAMES[0];
    private Color backgroundColor = Color.white;
    private Color selectionColor = Color.magenta;
    //fah 2007
    //private Color[] classificationColors = RadViz.getDistinctColors(colorScheme, numClass);
    private Color[] classificationColors = {Color.red, Color.blue};

    /**
     * Default constructor
     */
    public RadvizColorSettingsGUI() {
        initComponents();
    }
    
    public void setBackgroundColor(Color color) {
        if (backgroundColor != color) {
            backgroundColor = color;
            backgroundColorLabel.setBackground(backgroundColor);
        }
    }
    
    public Color getBackgroundColor() {
        return backgroundColor;
    }
    
    public void setSelectionColor(Color color) {
        if (selectionColor != color) {
            selectionColor = color;
            selectionColorLabel.setBackground(selectionColor);
        }
    }
    
    public Color getSelectionColor() {
        return selectionColor;
    }
    
    public void setClassificationColors(String scheme, int numClass) {
        if (this.numClass != numClass || !colorScheme.equalsIgnoreCase(scheme)) {
            if (this.numClass != numClass) {
                this.numClass = numClass;
                if (!colorScheme.equalsIgnoreCase(scheme)) {
                    colorScheme = scheme;
                    for (int i = 0; i < schemes.length; i++) {
                        if (colorScheme.equalsIgnoreCase(SCHEME_NAMES[i])) {
                            schemes[i].setSelected(true);
                        }
                    }
                }
                //fah 2007
                //classificationColors = RadViz.getDistinctColors(colorScheme, this.numClass);
                setSchemeLegendLabels(classificationColors.length);
            }
            else {
                colorScheme = scheme;
                for (int i = 0; i < schemes.length; i++) {
                    if (colorScheme.equalsIgnoreCase(SCHEME_NAMES[i])) {
                        schemes[i].setSelected(true);
                    }
                }
                //fah 2007
                //classificationColors = RadViz.getDistinctColors(colorScheme, this.numClass);
                
                for (int i = 0; i < classificationColors.length; i++)
                    schemeLegendLabels[i].setBackground(classificationColors[i]);
            }
        }
    }
    
    public Color[] getClassificationColors() {
        Color[] clrs = new Color[classificationColors.length];
        for (int i = 0; i < clrs.length; i++) {
            clrs[i] = new Color(classificationColors[i].getRed(), 
                                classificationColors[i].getGreen(),
                                classificationColors[i].getBlue(),
                                alpha);
        }
        return clrs;
        //return classificationColors;
    }
    
    public String getColorScheme() {
        return colorScheme;
    }
    
    public void setDataLabel(int pointSize, int alpha) {
        if (this.alpha != alpha || this.pointSize != pointSize) {
            this.alpha = alpha;
            this.pointSize = pointSize;
            symbolPanel.repaint();
        }
    }
    
    public int getAlpha() {
        return alpha;
    }
    
    public int getPointSize() {
        return pointSize;
    }
    
    private void setSchemeLegendLabels(int length) {
        if (schemeLegendPanel.getComponentCount() > 0) schemeLegendPanel.removeAll();
        schemeLegendPanel.setLayout(new GridLayout(1, length));
        schemeLegendLabels = new JLabel[length];
        for (int i = 0; i < length; i++) {
            schemeLegendLabels[i] = new JLabel(" ");
            schemeLegendLabels[i].setOpaque(true);
            schemeLegendLabels[i].setBackground(classificationColors[i]);
            schemeLegendPanel.add(schemeLegendLabels[i]);
        }
        schemeLegendPanel.validate();
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
    
    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5,5,5,5));//createCompoundBorder(BorderFactory.createTitledBorder("Color Settings"),
                                   //                     BorderFactory.createEmptyBorder(0,5,5,5)));
        JPanel generalPanel = new JPanel(new GridBagLayout());
        generalPanel.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder("General"),
                                BorderFactory.createEmptyBorder(0,5,5,5)));
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        generalPanel.add(new JLabel("Background Color:"), c);
        c.gridx = 1;
        c.gridy = 0;
        backgroundColorLabel = new JLabel(" ");
        backgroundColorLabel.setOpaque(true);
        backgroundColorLabel.setPreferredSize(new Dimension(50, 20));
        backgroundColorLabel.setBackground(backgroundColor);
        backgroundColorLabel.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
        backgroundColorLabel.addMouseListener(this);
        generalPanel.add(backgroundColorLabel, c);
        c.gridx = 2;
        c.gridy = 0;
        c.insets = new Insets(5,15,5,5);
        generalPanel.add(new JLabel("Point Size:"), c);
        c.gridx = 3;
        c.gridy = 0;
        c.insets = new Insets(5,5,5,5);
        pointSizeSpinner = new JSpinner(new SpinnerNumberModel(1,1,20,1));
        pointSizeSpinner.addChangeListener(this);
        generalPanel.add(pointSizeSpinner, c);
        c.gridx = 0;
        c.gridy = 1;
        generalPanel.add(new JLabel("Selection Color:"), c);
        c.gridx = 1;
        c.gridy = 1;
        selectionColorLabel = new JLabel(" ");
        selectionColorLabel.setOpaque(true);
        selectionColorLabel.setPreferredSize(new Dimension(50, 20));
        selectionColorLabel.setBackground(selectionColor);
        selectionColorLabel.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
        selectionColorLabel.addMouseListener(this);
        generalPanel.add(selectionColorLabel, c);
        c.gridx = 2;
        c.gridy = 1;
        c.insets = new Insets(5,15,5,5);
        generalPanel.add(new JLabel("Transparency:"), c);
        c.gridx = 3;
        c.gridy = 1;
        c.insets = new Insets(5,5,5,5);
        alphaSpinner = new JSpinner(new SpinnerNumberModel(255,0,255,1));
        alphaSpinner.addChangeListener(this);
        generalPanel.add(alphaSpinner, c);
        c.gridx = 4;
        c.gridy = 0;
        c.gridheight = 2;
        c.weightx = 0.1;
        c.insets = new Insets(5,15,5,5);
        symbolPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                g.setColor(Color.white);
                int w = this.getWidth();
                int h = this.getHeight();
                g.fillRect(0, 0, w, h);
                Color clr = new Color(classificationColors[0].getRed(),
                                      classificationColors[0].getGreen(),
                                      classificationColors[0].getBlue(), alpha);
                g.setColor(clr);
                int width = 2 * pointSize + 1;
                g.fillOval(w / 2 - pointSize, h / 2 - pointSize, width, width);
            }
        };
        symbolPanel.setBackground(Color.white);
        generalPanel.add(symbolPanel, c);
        //drawDataLabel();
        
        generalPanel.setMaximumSize(new Dimension(generalPanel.getMaximumSize().width, 
                                                  generalPanel.getPreferredSize().height));
        add(generalPanel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel schemePanel = new JPanel();
        schemePanel.setLayout(new BoxLayout(schemePanel, BoxLayout.Y_AXIS));
        schemePanel.setBorder(BorderFactory.createCompoundBorder(
                               BorderFactory.createTitledBorder("Classification Color Scheme"),
                               BorderFactory.createEmptyBorder(0,5,5,5)));
        JPanel schemeSelectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ButtonGroup schemeButtonGroup = new ButtonGroup();
        for (int i = 0; i < schemes.length; i++) {
            schemes[i] = new JRadioButton(SCHEME_NAMES[i]);
            schemes[i].setActionCommand(SCHEME_NAMES[i]);
            schemes[i].addActionListener(this);
            if (colorScheme.compareTo(SCHEME_NAMES[i]) == 0)
                schemes[i].setSelected(true);
            schemeButtonGroup.add(schemes[i]);
            schemeSelectorPanel.add(schemes[i]);
        }
        schemePanel.add(schemeSelectorPanel);
        
        schemeLegendPanel = new JPanel();
        setSchemeLegendLabels(getClassificationColors().length);
        schemePanel.add(schemeLegendPanel);
        
        schemePanel.setPreferredSize(new Dimension(schemePanel.getPreferredSize().width, 80));
        schemePanel.setMaximumSize(new Dimension(schemePanel.getMaximumSize().width, 
                                                 schemePanel.getPreferredSize().height));
        add(schemePanel);
        JPanel btnPanel = new JPanel();
        okBtn = new JButton("Ok");
        okBtn.addMouseListener(this);
        cancelBtn = new JButton("Cancel");
        cancelBtn.addMouseListener(this);
        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel);
       
        //int h = (int)generalPanel.getPreferredSize().getHeight() +
          //      (int)schemePanel.getPreferredSize().getHeight() +
            //    (int)btnPanel.getPreferredSize().getHeight() + 20;
        //setPreferredSize(new Dimension(420, h));
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (!command.equalsIgnoreCase(getColorScheme())) {
            setClassificationColors(command, numClass);
        }
    }
    
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == backgroundColorLabel) {
            Color newColor = JColorChooser.showDialog(this, "Choose Background Color", getBackgroundColor());
            if (newColor != null) setBackgroundColor(newColor);
        }
        else if (e.getSource() == selectionColorLabel) {
            Color newColor = JColorChooser.showDialog(this, "Choose Selection Color", getSelectionColor());
            if (newColor != null) setSelectionColor(newColor);
        }
        else if (e.getSource() == okBtn) {
            fireAction("RadvizColorSettingsGUI.OK");
        }
        else if (e.getSource() == cancelBtn) {
            fireAction("RadvizColorSettingsGUI.CANCEL");
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    }
    
    public void mouseExited(MouseEvent e) {
    }
    
    public void mousePressed(MouseEvent e) {
    }
    
    public void mouseReleased(MouseEvent e) {
    }
    
    public static void main(String args[]) {
      RadvizColorSettingsGUI sel = new RadvizColorSettingsGUI();
      JFrame f = new JFrame("Test");
      f.getContentPane().add(sel);
      f.setSize(570, 270);
      f.setVisible(true);
      f.repaint();
   }
    
    public void stateChanged(ChangeEvent e) {
        Object source = e.getSource();
        if (source == alphaSpinner) {
            int alpha = ((Integer)alphaSpinner.getValue()).intValue();
            setDataLabel(getPointSize(), alpha);
        }
        else if (source == pointSizeSpinner) {
            int pointSize = ((Integer)pointSizeSpinner.getValue()).intValue();
            setDataLabel(pointSize, getAlpha());
        }
    }
    
}
