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

import java.util.Collection;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;


/**
 * Gow pattern by all edge.
 */
public class GrowChildrenByAllEdges implements GrowChildren {

  /**
   * Gow pattern by outgoing edges.
   */
  private final GrowChildren outgoing;

  /**
   * Gow pattern by incoming edges.
   */
  private final GrowChildren incoming;

  /**
   * Constructor.
   *
   * @param parent parent DFS code
   */
  GrowChildrenByAllEdges(DfsCode parent) {
    outgoing = new GrowChildrenByOutgoingEdges(parent);
    incoming = new GrowChildrenByIncomingEdges(parent);
  }

  @Override
  public void addChildren(
      CachedGraph graph, DfsEmbedding embedding, Collection<Pair<DfsCode, WithEmbedding>> output) {

    outgoing.addChildren(graph, embedding, output);
    incoming.addChildren(graph, embedding, output);
  }
}
