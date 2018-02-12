/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.loader;

public class TLFConstants {
  public static final java.lang.String FIELD_SEPARATOR = " ";

  public static final java.lang.String GRAPH_SYMBOL = "t";
  public static final int GRAPH_ID_INDEX = 2;
  public static final int GRAPH_LABEL_INDEX = 3;
  public static final int GRAPH_DATA_START = 4;

  public static final java.lang.String VERTEX_SYMBOL = "v";
  public static final int VERTEX_ID_INDEX = 1;
  public static final int VERTEX_LABEL_INDEX = 2;
  public static final int VERTEX_DATA_START = 3;

  public static final java.lang.String EDGE_SYMBOL = "e";
  public static final int EDGE_SOURCE_INDEX = 1;
  public static final int EDGE_TARGET_INDEX = 2;
  public static final int EDGE_LABEL_INDEX = 3;
  public static final int EDGE_DATA_START = 4;

}
