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

package org.biiig.dmgm.impl.operators.fsm;

import javafx.util.Pair;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Collection;

public class GrowChildrenByAllEdges implements GrowChildren {

  private final GrowChildren outgoing;
  private final GrowChildren incoming;


  public GrowChildrenByAllEdges(DfsCode parent) {
    outgoing = new GrowChildrenByOutgoingEdges(parent);
    incoming = new GrowChildrenByIncomingEdges(parent);
  }

  @Override
  public void addChildren(CachedGraph graph, DfsEmbedding embedding, Collection<Pair<DfsCode, WithEmbedding>> output) {
    outgoing.addChildren(graph, embedding, output);
    incoming.addChildren(graph, embedding, output);
  }
}
