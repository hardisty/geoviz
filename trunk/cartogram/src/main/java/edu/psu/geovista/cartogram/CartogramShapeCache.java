/* -------------------------------------------------------------------
 Java source file for the class CartogramShapeCache
  Original Author: Frank Hardisty
  $Author: hardistf $
  $Id: ComparableShapes.java,v 1.1 2005/12/05 20:17:05 hardistf Exp $
  $Date: 2005/12/05 20:17:05 $
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.
  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.
  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 -------------------------------------------------------------------   */

package edu.psu.geovista.cartogram;

import java.util.ArrayList;


/*
 * Singleton class for holding completed cartogram calculations.
 * Not thread-safe.
 */

public final class CartogramShapeCache {
   private static CartogramShapeCache instance = null;
   private ArrayList list;
   protected CartogramShapeCache() {
      // Exists only to defeat instantiation.
   }
   public static CartogramShapeCache getInstance() {
      if(instance == null) {
         instance = new CartogramShapeCache();
         instance.list = new ArrayList();
      }
      return instance;
   }

   public ComparableCartogram findCartogram(ComparableCartogram input){
     Object[] objs = list.toArray();
     for (int i = 0; i < objs.length; i++){
       ComparableCartogram test = (ComparableCartogram)objs[i];
       if (test.isEqualTo(input)){
         return test;
       }
     }

     return null;
   }
   public void addComparableCartogram(ComparableCartogram add){
     this.list.add(add);
   }

}
