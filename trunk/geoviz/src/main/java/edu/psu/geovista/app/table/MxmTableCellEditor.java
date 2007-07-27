package edu.psu.geovista.app.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;

public class MxmTableCellEditor extends AbstractCellEditor {
	private DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

	private String myValue = null;

	private TableBrowser myTB = null;

	public Object getCellEditorValue() {
		return new Object();
	}

	public MxmTableCellEditor(TableBrowser tb) {

		myTB = tb;

		final JPopupMenu popup = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("Study information");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {

				JEditorPane myEP = new JEditorPane();
				myEP.setEditable(false);
				myEP.setContentType("text/html");
				myEP
						.setText("Data source of this report is EIA-878, one of the two weekly Computer Assisted Telephone Interview surveys which EIA conducts to collect prices at the outlet level. EIA-878 collects prices of regular, midgrade, and premium motor gasoline by formulation from service stations across the country each Monday morning. Average prices of gasoline and diesel fuel through outlets at the five Petroleum Allocation for Defense District (PADD) levels, regions of the country, sub-PADD levels, and the state of California are released by the end of the day through Listserv, the Web, Fax, and telephone hotline");
				// myTB.addMetaFrame("Meta data",myEP);
			}

		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Authority and Contact Information");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// JEditorPane myEP=myTB.getMetaPane();
				JEditorPane myEP = new JEditorPane();
				myEP.setEditable(false);
				myEP.setContentType("text/html");
				String metaCont = "<html>" + "<head> "
						+ "<title>Test Page</title>" + "</head>"
						+ "<body bgcolor= #FFFFFF > "
						+ " Your hypertext is here " + "</body> " + "</html> ";

				myEP.setText(metaCont);
				// myEP.setText("In determining residence, the Census Bureau
				// counts each person as an inhabitant of a usual place of
				// residence (i.e., the place where one usually lives and
				// sleeps).");
				// myTB.addMetaFrame("Meta data",myEP);
			}

		});
		popup.add(menuItem);

		renderer.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JEditorPane myEP = new JEditorPane();
					myEP.setEditable(false);
					myEP.setContentType("text/html");
					String abc = myTB.getCurrentTable().getName();
					if (abc.endsWith("table12.xml")) {

						int currentColumn = myTB.getCurrentTable()
								.getSelectedColumn();
						int currentRow = myTB.getCurrentTable()
								.getSelectedRow();
						if (currentColumn == 0) {

							if (currentRow <= 12) {

							} else if (currentRow == 15) {

							}

						}

					} else {
						myEP
								.setText("The table belongs to a section presenting statistics on the growth, distribution, and characteristics of the U.S. population. The principal source of these data, the U.S. Census Bureau, conducts a decennial census of population, a monthly population survey, a program of population estimates and projections, and a number of other periodic surveys relating to population characteristics. For a list of relevant publications, see the Guide to Sources of Statistics in Appendix 1<br>");
					}
					// myTB.addMetaFrame("Meta data",myEP);
				}

				if (SwingUtilities.isRightMouseButton(e)) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}

				if (SwingUtilities.isMiddleMouseButton(e)) {
					JEditorPane myEP = new JEditorPane();
					myEP.setEditable(false);
					myEP.setContentType("text/html");
					myEP.setText(" You pressed the middle button");
					// myTB.addMetaFrame("Meta data",myEP);
				}

				if (SwingUtilities.isLeftMouseButton(e)
						&& (e.getClickCount() == 1)) {
					fireEditingCanceled();
				}

			}
		});
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		myValue = (String) value;
		renderer.setText(myValue);
		return renderer;
	}

	public boolean isCellEditable(EventObject anEvent) {

		return true;
	}

	public boolean stopCellEditing() {
		// setCellEditorValue(myValue);

		return super.stopCellEditing();
	}

}
