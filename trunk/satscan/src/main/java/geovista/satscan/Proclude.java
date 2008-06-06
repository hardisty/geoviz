/*
 * Proclude.java
 *
 * Created on May 22, 2008, 3:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package geovista.satscan;

import geovista.common.data.DataSetForApps;
import geovista.common.event.DataSetEvent;
import geovista.common.event.DataSetListener;
import geovista.geoviz.sample.GeoData48States;
import geovista.proclude.AbstractGam;
import geovista.proclude.BesagNewellGAM;
import geovista.proclude.CrossMidLine;
import geovista.proclude.FitnessRelativePct;
import geovista.proclude.Gene;
import geovista.proclude.GeneticGAM;
import geovista.proclude.InitGAMFile;
import geovista.proclude.MutateLinearAmount;
import geovista.proclude.RandomGam;
import geovista.proclude.RelocateDifference;
import geovista.proclude.SelectRandomElite;
import geovista.proclude.StopAtNGens;
import geovista.proclude.SurviveEliteN;
import geovista.proclude.SystematicGam;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author jfc173
 */
public class Proclude extends JPanel implements ActionListener, DataSetListener{
    
    InitGAMFile initializer;    
    int type = 0;
    Gene[] out;
    private static final String[] typeNames = {"Genetic","Random","Systematic","Besag"};
    JComboBox types = new JComboBox(typeNames);
    JButton runButton = new JButton("RUN");
    JPanel output = new JPanel();
    
    public final static int GENETIC_TYPE = 0;
    public final static int RANDOM_TYPE = 1;
    public final static int SYSTEMATIC_TYPE = 2;
    public final static int BESAG_TYPE = 3;     
    
    protected final static Logger logger = Logger.getLogger(Proclude.class.getName());
    
    /** Creates a new instance of Proclude */
    public Proclude() {
        initializer = new InitGAMFile();
        types.setActionCommand("NEW_TYPE");
        types.addActionListener(this);
        runButton.setActionCommand("RUN");
        runButton.addActionListener(this);
        
        this.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel();
//        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(types);
        topPanel.add(runButton);
        this.add(topPanel, BorderLayout.NORTH);
        this.add(output, BorderLayout.CENTER);
    }

    public void dataSetChanged(DataSetEvent e) {
        initializer.processDataSetForApps(e.getDataSetForApps());
        run();
    }
        
    public void setDataSetForApps(DataSetForApps dsfa){
        initializer.processDataSetForApps(dsfa);
    }
    
    public void setType(int i){
        type = i;
    }    
    
    public void run(){
        double largeDimension = Math.max(initializer.getMaxX() - initializer.getMinX(),
                                         initializer.getMaxY() - initializer.getMinY());
        System.out.println("largeDimension is " + largeDimension);
        AbstractGam gam;
        switch (type){
            case GENETIC_TYPE:
                gam = initGenetic(largeDimension);
                break;
            case RANDOM_TYPE:
                gam = initRandom();
                break;
            case SYSTEMATIC_TYPE:
                gam = initSystematic();
                break;
            case BESAG_TYPE:
                gam = initBesag();
                break;
            default:
                gam = initGenetic(largeDimension);              
        }
        gam.setMaxRadius(0.05 * largeDimension);
        gam.setMinRadius(0.005 * largeDimension);
        gam.setMinPoints(3);
        gam.setMinAccepted(1.5);
        gam.setFitnessFunction(new FitnessRelativePct(initializer.getDataSet()));  
        gam.setInitializer(initializer);
        
        //Whew.  Now that the method is created, run it!  This is a lot easier.
        Vector solutions = gam.run();
        
        out = new Gene[solutions.size()];
        for (int i = 0; i < solutions.size(); i++){
            out[i] = (Gene) solutions.get(i);
        }
        updateOutputDisplay();
    }
    
    public Gene[] getOutput(){
        return out;
    }
    
    private AbstractGam initSystematic(){
        return new SystematicGam();
    }
    
    private AbstractGam initRandom(){
        RandomGam gam = new RandomGam();
        gam.setNumTests(500);
        return gam;
    }
    
    private AbstractGam initBesag(){
        return new BesagNewellGAM();       
    }
    
    private AbstractGam initGenetic(double largeDimension){
        //Whee!  A lot of parameters.  These values work reasonably well, in my experience.                
        GeneticGAM gam = new GeneticGAM();
        StopAtNGens halt = new StopAtNGens(75);
        ((GeneticGAM) gam).setHaltCondition(halt);
        ((GeneticGAM) gam).setPopSize(100);
        ((GeneticGAM) gam).setProbMut(0.05);
        ((GeneticGAM) gam).setRandomGenes(0);
        ((GeneticGAM) gam).setFirstAdd(4);
        ((GeneticGAM) gam).setUpdateOften(2);
        ((GeneticGAM) gam).setSelectPairs(false);
        ((GeneticGAM) gam).setBannedList(true);
        ((GeneticGAM) gam).setSolutionList(false);
        ((GeneticGAM) gam).setAntiConvergence(true);
        SelectRandomElite selection = new SelectRandomElite(0.2);
        ((GeneticGAM) gam).setSelectMethod(selection);
        SurviveEliteN survive = new SurviveEliteN(0.2);
        ((GeneticGAM) gam).setSurviveMethod(survive);
        CrossMidLine crossover = new CrossMidLine();
        ((GeneticGAM) gam).setCrossoverMethod(crossover);
        MutateLinearAmount mutation = new MutateLinearAmount(0.05 * largeDimension);
        ((GeneticGAM) gam).setMutationMethod(mutation);
        RelocateDifference relocation = new RelocateDifference(3, 1.5);
        ((GeneticGAM) gam).setRelocationMethod(relocation);
        return gam;
    }
       
    public static void main(String[] args) {
        GeoData48States states = new GeoData48States();
        if(logger.isLoggable(Level.FINEST)){
                logger.finest("n num atts" + states.getDataForApps().getNumberNumericAttributes());
        }
        Proclude scanner = new Proclude();
        scanner.setDataSetForApps(states.getDataForApps());
        
        JFrame app = new JFrame("testing Proclude");
        app.add(scanner);
        app.pack();
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }    

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equalsIgnoreCase("RUN")){
            run();
        } else if (command.equalsIgnoreCase("NEW_TYPE")){
            int selected = types.getSelectedIndex();
            setType(selected);
        } else {
            System.out.println("snuh?");
        }
    }
    
    private void updateOutputDisplay(){
        System.out.println("there are " + out.length + " clusters");
        output.removeAll();
        for (int i = 0; i < out.length; i++){  
            Gene next = out[i];
            JLabel[] labels = geneToLabels(next);
            JPanel thisGene = new JPanel();
            thisGene.setLayout(new BoxLayout(thisGene, BoxLayout.Y_AXIS));
            for (int x = 0; x < 9; x++){
                thisGene.add(labels[x]);
            }
            output.add(thisGene);
        }   
        revalidate();
        repaint();
    }
    
    private JLabel[] geneToLabels(Gene g){
        JLabel[] labels = new JLabel[9];
        labels[0] = new JLabel("Center X: " + roundToHundredths(g.getX()));
        labels[1] = new JLabel("Center Y: " + roundToHundredths(g.getY()));
        labels[2] = new JLabel("Major Radius: " + roundToHundredths(g.getMajorAxisRadius()));
        labels[3] = new JLabel("Minor Radius: " + roundToHundredths(g.getMinorAxisRadius()));
        labels[4] = new JLabel("Orientation: " + roundToHundredths(g.getOrientation()));
        labels[5] = new JLabel("Area: " + roundToHundredths(Math.PI * g.getMajorAxisRadius() * g.getMinorAxisRadius()));
        labels[6] = new JLabel("Population: " + g.getPopulation());
        labels[7] = new JLabel("Count: " + g.getCount());
        labels[8] = new JLabel("Fitness: " + roundToHundredths(g.getFitness()));
        labels[8].setBorder(BorderFactory.createEmptyBorder(0,0,5,0));
        labels[0].setBorder(BorderFactory.createEmptyBorder(5,0,0,0));
        return labels;
    }    
    
    private double roundToHundredths(double d){
        return ((double) Math.round(d * 100))/100;
    }    
    
}
