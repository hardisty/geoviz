/*
 * ParallelDisplayUI.java
 *
 * Created on 19. November 2001, 16:06
 *
 * Copyright 2001 Flo Ledermann flo@subnet.at
 *
 * Licensed under GNU General Public License (GPL).
 * See http://www.gnu.org/copyleft/gpl.html
 */

package geovista.geoviz.parvis;

import javax.swing.plaf.ComponentUI;

/**
 * Abstract UI Delegate for the rendering of the ParallelDisplay component. This
 * is a swing guideline to provide an empty abstract class as a UI delegat base
 * class. See BasicParallelDisplayUI for the actual implementation.
 *
 * @author Flo Ledermann flo@subnet.at
 * @version 0.1
 */
public abstract class ParallelDisplayUI extends ComponentUI {

    public ParallelDisplayUI(){ }

    //edited by FAH 30 July 02
    public abstract void createBrushImage(ParallelDisplay comp);
    public abstract void renderBrush();
}
