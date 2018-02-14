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

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.impl.operators.fsm.fsm.FSMEmbedding;
import org.biiig.dmgm.impl.operators.fsm.fsm.FSMGraph;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class IsMinimal implements java.util.function.Predicate<DFSCode> {

  private static final BiFunction<FSMGraph, DFSEmbedding, FSMEmbedding> EMBEDDING_FACTORY =
    (g, e) -> new FSMEmbedding(e);

  private static final InitializeSingleEdgePatterns<FSMGraph, FSMEmbedding> INITIALIZE_PARENTS =
    new InitializeSingleEdgePatterns<>(0, EMBEDDING_FACTORY);

  @Override
  public boolean test(DFSCode dfsCode) {
    FSMGraph withGraph = new FSMGraph(dfsCode);

    Pair<DFSCode, List<FSMEmbedding>> minPair = INITIALIZE_PARENTS
      .apply(withGraph)
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
      .entrySet()
      .stream()
      .map(e -> new Pair<>(e.getKey(), e.getValue()))
      .min(Comparator.comparing(Pair::getKey))
      .get();

    DFSCode minCode = minPair.getKey();
    boolean minimal = minCode.parentOf(dfsCode);

    List<FSMEmbedding> minEmbeddings = minPair.getValue();

    while (minCode != null && minimal) {
      DFSCode parentCode = minPair.getKey();

      GrowAllChildren<FSMGraph, FSMEmbedding> growAllChildren = new GrowAllChildren<>(parentCode, EMBEDDING_FACTORY);

      List<Pair<DFSCode, FSMEmbedding>> children = Lists.newArrayList();
      minEmbeddings
        .forEach(e -> growAllChildren.addChildren(withGraph, e.getEmbedding(), children));

      if (!children.isEmpty()) {
        children.sort(Comparator.comparing(Pair::getKey));
        minCode = children.get(0).getKey();
        minimal = minCode.parentOf(dfsCode);

        if(minimal) {
          DFSCode finalMinCode = minCode;
          minEmbeddings = children
            .stream()
            .filter(p -> p.getKey().equals(finalMinCode))
            .map(Pair::getValue)
            .collect(Collectors.toList());
        }
      } else {
        minCode = null;
      }
    }

    return minimal;
  }
}
