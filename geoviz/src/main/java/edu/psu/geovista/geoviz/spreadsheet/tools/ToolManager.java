package edu.psu.geovista.geoviz.spreadsheet.tools;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import edu.psu.geovista.geoviz.spreadsheet.table.SSTable;

/*
 * Description:
 * Date: Mar 29, 2003
 * Time: 4:33:18 PM
 * @author Jin Chen
 */

public class ToolManager {
    private SSTable table;
    private JPanel toolBar;
    final public static String RESOURCES="resources/";
    final public static String MODEL_ROOT="edu/psu/geovista/geoviz/spreadsheet/";
    final public static String IMAGES=MODEL_ROOT+RESOURCES+"images/";

     final private ImageIcon saveIcon = getImageIcon("save.gif");
    final private ImageIcon unlockedIcon = getImageIcon("unlocked.gif");
    final private ImageIcon printIcon = getImageIcon("print.gif");
    final private ImageIcon undoIcon = getImageIcon("undo.gif");
    final private ImageIcon redoIcon = getImageIcon("redo.gif");
    final private ImageIcon cutIcon = getImageIcon("cut.gif");
    final private ImageIcon copyIcon = getImageIcon("copy.gif");
    final private ImageIcon pasteIcon = getImageIcon("paste.gif");
    final private ImageIcon findIcon = getImageIcon("find.gif");
    final private ImageIcon insertRowIcon = getImageIcon("insertrow.gif");
    final private ImageIcon insertColumnIcon = getImageIcon("insertcolumn.gif");
    final private ImageIcon deleteRowIcon = getImageIcon("deleterow.gif");
    final private ImageIcon deleteColumnIcon = getImageIcon("deletecolumn.gif");
    final private ImageIcon sortDIcon = getImageIcon("sort_desend.gif");
    final private ImageIcon sortAIcon = getImageIcon("sort_ascend.gif");
    final private ImageIcon sortNoIcon = getImageIcon("sort_no.gif");
    public ToolManager(SSTable tb) {
        table=tb;
        this.toolBar =this.setupToolBar(tb);

    }

    private JPanel setupToolBar(final SSTable table){
        //Container c=this.getRootPane().getContentPane();

        JButton addCol=new JButton(insertColumnIcon);
        addCol.setToolTipText("Insert a column");
        addCol.addActionListener(new AddColumn(table));


        JButton removeCol=new JButton(deleteColumnIcon);
        removeCol.setToolTipText("Remove a column");
        removeCol.addActionListener(new RemoveColumn(table));

        JButton addRow=new JButton(insertRowIcon);
        addRow.setToolTipText("Insert a row");
        addRow.addActionListener(new AddRow(table));

        JButton removeRow=new JButton(deleteRowIcon);
        removeRow.setToolTipText("Remove a row");
        removeRow.addActionListener(new RemoveRow(table));

        JButton sorta=new JButton(sortAIcon);
        sorta.setToolTipText("Ascending Sort ") ;
        sorta.addActionListener(new Sort(table,Sort.ASCEND ));

        JButton sortd=new JButton(sortDIcon);
        sortd.setToolTipText("Descending Sort ") ;
        sortd.addActionListener(new Sort(table,Sort.DESCEND ));

        JButton sortn=new JButton(sortNoIcon);
        sortn.setToolTipText("Clear Sort ") ;
        sortn.addActionListener(new Sort(table,Sort.CLEAR ));
        //JButton sort=new JButton("edu.psu.geovista.geoviz.spreadsheet.tools.Sort");
        //sort.addActionListener(new Sort(table,-1));
        /***************** NOT implement yet ***************************/
        // we save this button as private member for further reference
        JButton saveButton = new JButton(saveIcon);
        saveButton.setToolTipText("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //fileOp.saveFile();
                table.requestFocus();
            }
        });

        // we save this button as private member for further reference
        JButton passwordButton = new JButton(unlockedIcon);
        passwordButton.setToolTipText("Set Password");
        passwordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //fileOp.setPassword();
                table.requestFocus();
            }
        });

        JButton printButton = new JButton(printIcon);
        printButton.setToolTipText("Print");
        printButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                Thread runner = new Thread() {
                    public void run() {
                        //fileOp.printData();
                    }
                };
                runner.start();
                table.requestFocus();
            }
        });

        JButton undoButton = new JButton(undoIcon);
        undoButton.setToolTipText("Undo");
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //history.undo(tableModel);
                table.requestFocus();
            }
        });

        JButton redoButton = new JButton(redoIcon);
        redoButton.setToolTipText("Redo");
        redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //history.redo(tableModel);
                table.requestFocus();
            }
        });

        JButton cutButton = new JButton(cutIcon);
        cutButton.setToolTipText("Cut");
        cutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //editOp.cut();
                table.requestFocus();
            }
        });
        JButton copyButton = new JButton(copyIcon);
        copyButton.setToolTipText("Copy");
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //editOp.copy();
                table.requestFocus();
            }
        });

        JButton pasteButton = new JButton(pasteIcon);
        pasteButton.setToolTipText("Paste");
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //editOp.paste();
                table.requestFocus();
            }
        });

        JButton findButton = new JButton(findIcon);
        findButton.setToolTipText("Find");
        findButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //editOp.find(true);
                table.requestFocus();
            }
        });
        /***************** NOT implement yet ***************************/
        //Set fname=Formula.getFunctionNames();
        //fname.add("function");
        //JComboBox functions=new JComboBox(Formula.getFunctionNames() );
        JComboBox functions=new JComboBox(this.table.getFunManager().getFunctionNames() );
        JPanel tp=new JPanel(); //edu.psu.geovista.geoviz.spreadsheet.tools panel
        tp.setLayout(new FlowLayout());
        //tp.add(saveButton);
        //tp.add(printButton);
        tp.add(cutButton);
        tp.add(copyButton);
        tp.add(pasteButton);

        tp.add(undoButton);
        tp.add(redoButton);

        tp.add(findButton);
        tp.add(addCol);
        tp.add(removeCol);
        tp.add(addRow);
        tp.add(removeRow);
        tp.add(sorta);
        tp.add(sortd);
        tp.add(sortn);
        tp.add(functions);
        return tp;
    }

    public JPanel getToolBar() {
        return toolBar;
    }

    // loading images
    public static ImageIcon getImageIcon(String name) {
        String fullName=IMAGES+name;
        ClassLoader cl=ToolManager.class.getClassLoader() ;
        URL url=cl.getResource(fullName);
        if (url == null) {
            System.out.println("image "+name+" not found");
            return null;
        }
        return new ImageIcon(url);
    }
}
