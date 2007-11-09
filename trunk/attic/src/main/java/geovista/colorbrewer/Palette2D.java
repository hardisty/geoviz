//******************************************************************************
// interface oblivion.awt.colorbrewer.Palette2D
// Copyright (c) Christopher E. Weaver. All rights reserved.
//******************************************************************************
// File: Palette2D.java
// Last modified: Wed Sep  7 11:47:49 2005 by Chris Weaver
//******************************************************************************
// Modification History:
//
// 20050907 [weaver]: Original file.
//
//******************************************************************************
//
//******************************************************************************

package geovista.colorbrewer;

//import java.lang.*;
import java.awt.Color;

//******************************************************************************
// interface Palette2D
//******************************************************************************

/**
 * The <CODE>Palette2D</CODE> interface.
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public interface Palette2D extends Palette
{
	//**********************************************************************
	// Members
	//**********************************************************************

	int		SEQUENTIAL_SEQUENTIAL	= 1;
	int		SEQUENTIAL_DIVERGING	= 2;
	int		SEQUENTIAL_QUALITATIVE	= 3;
	int		QUALITATIVE_SEQUENTIAL	= 4;
	int		DIVERGING_DIVERGING		= 5;
	int		DIVERGING_SEQUENTIAL	= 6;

	//**********************************************************************
	// Methods
	//**********************************************************************

    public int			getType();

    public Color[][]	getColors(int cols, int rows);
}

//******************************************************************************
