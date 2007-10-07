/* -------------------------------------------------------------------
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class VariableSelectionListener
 Copyright (c), 2002, GeoVISTA Center
 All Rights Reserved.
 Original Author: Xiping Dai
 $Author: xpdai $
 $Id: VariableSelectionListener.java,v 1.1 2004/07/28 15:34:44 xpdai Exp $
 $Date: 2004/07/28 15:34:44 $
 Reference:		Document no:
 ___				___
 -------------------------------------------------------------------  *
 */


package geovista.common.event;

import java.util.EventListener;


/**
 * This interface enables listening to senders of VariableSelectionEvents.
 *
 * This interface also enables "fireVariableSelectionChanged" methods in classes
 * that generate and broadcast VariableSelectionEvents.
 *
 */
public interface VariableSelectionListener extends EventListener {


  public void variableSelectionChanged(VariableSelectionEvent e);


}
