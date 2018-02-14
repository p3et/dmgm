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

package org.biiig.dmgm.impl.operators.fsm.common;

import javafx.util.Pair;

import java.util.Collection;
import java.util.function.BiFunction;

public class GrowAllChildren<G extends WithCachedGraph, E extends WithEmbedding> implements GrowChildren<G, E> {

  private final GrowChildren<G, E> outgoing;
  private final GrowChildren<G, E> incoming;


  public GrowAllChildren(DFSCode parent, BiFunction<G, DFSEmbedding, E> embeddingFactory) {
    outgoing = new GrowChildrenByOutgoingEdges<>(parent, embeddingFactory);
    incoming = new GrowChildrenByIncomingEdges<>(parent, embeddingFactory);
  }

  @Override
  public void addChildren(G withGraph, DFSEmbedding embedding, Collection<Pair<DFSCode, E>> output) {
    outgoing.addChildren(withGraph, embedding, output);
    incoming.addChildren(withGraph, embedding, output);
  }
}
