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

package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class GrowChildrenOf<G extends CachedGraph>
  implements Function<DFSEmbedding, Stream<Pair<DFSCode,DFSEmbedding>>> {

  private final GrowChildrenByOutgoingEdges growChildrenByOutgoingEdges = new GrowChildrenByOutgoingEdges();
  private final GrowChildrenByIncomingEdges growChildrenByIncomingEdges = new GrowChildrenByIncomingEdges();
  private final DFSCode parent;
  private final int[] rightmostPaath;
  private final Map<Long, G> input;

  public GrowChildrenOf(DFSCode parent, Map<Long, G> input) {
    this.input = input;
    this.parent = parent;
    this.rightmostPaath = parent.getRightmostPath();
  }


  public Pair<DFSCode,DFSEmbedding>[] apply(G graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    Pair<DFSCode,DFSEmbedding>[] children =
      growChildrenByOutgoingEdges.apply(graph, parent, rightmostPath, parentEmbedding);

    children = ArrayUtils
      .addAll(children, growChildrenByIncomingEdges.apply(graph, parent, rightmostPath, parentEmbedding));

    return children;
  }

  @Override
  public Stream<Pair<DFSCode,DFSEmbedding>> apply(DFSEmbedding dfsEmbedding) {
    CachedGraph graph = input.get(dfsEmbedding.getGraphId());

    Pair<DFSCode,DFSEmbedding>[] outChildren = growChildrenByOutgoingEdges
      .apply(graph, parent, rightmostPaath, dfsEmbedding);

    Pair<DFSCode,DFSEmbedding>[] inChildren = growChildrenByIncomingEdges
      .apply(graph, parent, rightmostPaath, dfsEmbedding);

    return Stream.of(ArrayUtils.addAll(outChildren, inChildren));
  }
}
