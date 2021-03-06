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

package org.biiig.dmgm.impl.operators.patternmining;

import org.biiig.dmgm.api.model.GraphView;

/**
 * A graph associated to n categories.
 */
class GraphViewWithCategories implements WithGraphView, WithCategories {

  /**
   * Graph.
   */
  private GraphView graph;

  /**
   * Categories.
   */
  private final int[] categories;

  /**
   * Constructor.
   *
   * @param graph graph
   * @param categories categories
   */
  GraphViewWithCategories(GraphView graph, int[] categories) {
    this.graph = graph;
    this.categories = categories;
  }

  @Override
  public int[] getCategories() {
    return categories;
  }

  @Override
  public GraphView getGraph() {
    return graph;
  }
}
