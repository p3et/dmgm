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

package org.biiig.dmgm.api.config;

/**
 * Constants used with DMGM.
 */
public interface DMGMConstants {

  /**
   * Symbols for the representation of different elements.
   */
  interface Elements {

    /**
     * Symbols for the representation of vertices.
     */
    interface Vertex {

      /**
       * Start of a vertex.
       */
      String OPEN = "(";

      /**
       * End of a vertex.
       */
      String CLOSE = ")";
    }

    /**
     * Symbols for the representation of vertices.
     */
    interface Edge {

      /**
       * Start of an outgoing edge.
       */
      String OPEN_OUTGOING = "-";

      /**
       * End of an outgoing edge.
       */
      String CLOSE_OUTGOING = ">";
    }

    /**
     * Symbols for the representation of graphs.
     */
    interface Graph {

      /**
       * Start of a graph.
       */
      String OPEN = "{";

      /**
       * End of a graph.
       */
      String CLOSE = "}";
    }

    /**
     * Symbols for the representation of lists.
     */
    interface Collection {

      /**
       * Start of a list.
       */
      String OPEN = "[";

      /**
       * End of a list.
       */
      String CLOSE = "]";
    }
  }

  /**
   * Labels for vertices, edges, graph or collections.
   */
  interface Labels {
    /**
     * Frequent subgraph graph label.
     */
    String FREQUENT_SUBGRAPH = "Frequent Subgraph";
    /**
     * Frequent subgraphs collection label.
     */
    String FREQUENT_SUBGRAPHS = "Frequent Subgraphs";
  }

  /**
   * Property keys.
   */
  interface PropertyKeys {

    /**
     * DFS code. A specific canonical label for graph patterns.
     */
    String DFS_CODE = "_dfsCode";
    /**
     * The category of a graph.
     */
    String CATEGORY = "_category";
    /**
     * Support of a pattern.
     */
    String SUPPORT = "_support";
  }

  /**
   * Default values of property ke
   */
  interface PropertyDefaultValues {

    /**
     * String default value.
     */
    String STRING = "_default";
  }

  /**
   * Separators, e.g., for lists.
   */
  interface Separators {

    /**
     * Separator for lists.
     */
    String LIST = ",";

    /**
     * Separator of key-value pairs.
     */
    String KEY_VALUE = "=";
    /**
     * Separator for id-label substrings, e.g., vertex (1:A)
     */
    String ID_LABEL = ":";
    /**
     * Separator for taxonomy paths, e.g., {@code "2018_01_01"} or {@code "Animal_Mammal_Cat"}
     * For compatibility with GDL it should not be changed.
     *
     */
    String TAXONOMY_PATH_LEVEL = "_";
  }
}
