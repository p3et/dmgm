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

package org.biiig.dmgm.impl.db;

/**
 * Constants of TLF (graph Transaction List Format).
 * This format is often used by prototypes for Frequent Subgraph Mining.
 */
public class TlfConstants {
  /**
   * Separator of fields in a line.
   */
  public static final String FIELD_SEPARATOR = " ";

  /**
   * Lines starting with this string represent graphs.
   */
  public static final String GRAPH_SYMBOL = "t";
  /**
   * Index of the id field in a split graph line.
   */
  public static final int GRAPH_ID_INDEX = 2;
  /**
   * Index of the label field in a split graph line.
   */
  public static final int GRAPH_LABEL_INDEX = 3;
  /**
   * Lines starting with this string represent vertices.
   */
  public static final String VERTEX_SYMBOL = "v";
  /**
   * Index of the id field in a split vertex line.
   */
  public static final int VERTEX_ID_INDEX = 1;
  /**
   * Index of the label field in a split vertex line.
   */
  public static final int VERTEX_LABEL_INDEX = 2;
  /**
   * Lines starting with this string represent edges.
   */
  public static final String EDGE_SYMBOL = "e";
  /**
   * Index of the source vertex id field in a split edge line.
   */
  public static final int EDGE_SOURCE_INDEX = 1;
  /**
   * Index of the target vertex id field in a split edge line.
   */
  public static final int EDGE_TARGET_INDEX = 2;
  /**
   * Index of the label field in a split edge line.
   */
  public static final int EDGE_LABEL_INDEX = 3;
}
