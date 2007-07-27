/*
 *
 * GeoVISTA Center (Penn State, Dept. of Geography)
 * Copyright (c), 1999 - 2002, GeoVISTA Center
 * All Rights Researved.
 *
 * Description: Protect against circle reference
 * Date: Mar 27, 2003
 * Time: 8:21:57 PM
 * @author Jin Chen
 */

package edu.psu.geovista.app.spreadsheet.formula;

import java.util.HashSet;



public class ReferenceManager {
    private HashSet references;
    /* Reference Manager is owned by only one cell
     * It records all reference cells involved in calculating the owner cell
     */
    private Cell owner;

    public Cell getOwner() {
        return owner;
    }

    public boolean addReference(Cell cell) {
        return this.references.add(cell);
    }
    /**
     *  assume single thread(in swing)
     */
    public synchronized void setOwner(Cell owner) {
        if (hasOwner()) return;
        this.owner = owner;
    }

    public boolean hasOwner() {
        return (owner!=null);
    }
}
