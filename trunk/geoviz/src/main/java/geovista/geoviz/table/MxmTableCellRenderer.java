package geovista.geoviz.table;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class MxmTableCellRenderer extends DefaultTableCellRenderer
{
	int align = JLabel.CENTER;

	public MxmTableCellRenderer( int alignment )
	{
		align = alignment;
	}

	public Component getTableCellRendererComponent
            ( JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int col )
   	{
		int numOfCol = table.getColumnCount();
		int numOfRow = table.getRowCount();
		if (col!=0){

			if (col >= numOfCol-3)
			{
				String header = null;
	  			 header = value.toString();
				//JButton columnLabel = new JButton( header );
				JLabel columnLabel = new JLabel( header );
				Font defaultFont = new Font("Verdana", Font.PLAIN, 15);
				columnLabel.setFont(defaultFont);
				columnLabel.setForeground(new Color(0,0,254));//RGB
				columnLabel.setHorizontalAlignment( align );

				return columnLabel;
			}

			if (row == numOfRow-1)
			{
				String header = null;
	  			 header = value.toString();
				//JButton columnLabel = new JButton( header );
				JLabel columnLabel = new JLabel( header );
				Font defaultFont = new Font("Verdana", Font.PLAIN, 15);
				columnLabel.setFont(defaultFont);
				columnLabel.setForeground(new Color(0,0,254));//RGB
				columnLabel.setHorizontalAlignment( align );

				return columnLabel;
			}
if (col == numOfCol-4)
			{
				String header = null;
	  			 header = value.toString();
				//JButton columnLabel = new JButton( header );
				JLabel columnLabel = new JLabel( header );
				Font defaultFont = new Font("Verdana", Font.PLAIN, 15);
				columnLabel.setFont(defaultFont);
				columnLabel.setForeground(new Color(254,0,0));//RGB
				columnLabel.setHorizontalAlignment( align );

				return columnLabel;
			}
			if (row == numOfRow-2)
			{
				String header = null;
	
				header = value.toString();
				//JButton columnLabel = new JButton( header );
				JLabel columnLabel = new JLabel( header );
				Font defaultFont = new Font("Verdana", Font.PLAIN, 15);
				columnLabel.setFont(defaultFont);
				columnLabel.setForeground(new Color(254,0,0));//RGB
				columnLabel.setHorizontalAlignment( align );

				return columnLabel;
			}
			if (row+1 ==col & row < numOfRow-1)
			{	String header = null;

				header = value.toString();
				//JButton columnLabel = new JButton( header );
				JLabel columnLabel = new JLabel( header );
				Font defaultFont = new Font("Verdana", Font.PLAIN, 15);
				columnLabel.setFont(defaultFont);
				columnLabel.setForeground(new Color(0,254,0));//RGB
				columnLabel.setHorizontalAlignment( align );
				return columnLabel;

			}


		   	hasFocus = false ;
      			setHorizontalAlignment( align ) ;
			return super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col ) ;
		}
		else
		{
			String header = null;

	   		if( align == JLabel.RIGHT )
	   		{
	   			header = value.toString() ;

			} else
				header = value.toString();
				JButton columnLabel = new JButton( header );
				//JLabel columnLabel = new JLabel( header );
				columnLabel.setHorizontalAlignment( JLabel.CENTER );
				Color color = new Color( 100, 100, 100 ) ;

	   			//if(  col == _clickedColumn )
		  		/* {
					javax.swing.border.BevelBorder loweredBorder =
			        	new BevelBorder( BevelBorder.LOWERED,
			                	color, Color.white, color,
				            	Color.gray );
				columnLabel.setBorder( loweredBorder );
	   			} else
	   			{*/
				javax.swing.border.BevelBorder raisedBorder =
				        new BevelBorder( BevelBorder.RAISED,
				                color, Color.white, color,
					            Color.gray );
				columnLabel.setBorder( raisedBorder );
	   			//}

			return columnLabel;
		}
   	}
}
