/*
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description:
 * Date: Apr 2, 2003
 * Time: 10:42:12 PM
 * @author Jin Chen
 */

package geovista.geoviz.spreadsheet.event;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;



public class SSTableModelEvent extends TableModelEvent{
    public static final int RESET_DATA =  -202;
    public static final int INSERT_COLUMN =  202;

    public SSTableModelEvent(TableModel source, int type) {
        super(source);
        this.type =type;
    }
}
