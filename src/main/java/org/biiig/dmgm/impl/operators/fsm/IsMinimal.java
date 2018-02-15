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

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.biiig.dmgm.impl.util.collectors.GroupByKeyListValues;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Test, if a DFS code is minimal, i.e., canonical.
 */
public class IsMinimal implements Predicate<DfsCode> {

  private static final InitializeSingleEdgePatterns INITIALIZE_PARENTS =
      new InitializeSingleEdgePatterns(0);

  @Override
  public boolean test(DfsCode dfsCode) {

    Pair<DfsCode, List<WithEmbedding>> minPair = INITIALIZE_PARENTS
        .apply(dfsCode)
        .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue))
        .entrySet()
        .stream()
        .map(e -> new Pair<>(e.getKey(), e.getValue()))
        .min(Comparator.comparing(Pair::getKey))
        .get();

    DfsCode minCode = minPair.getKey();
    boolean minimal = minCode.parentOf(dfsCode);

    List<WithEmbedding> minEmbeddings = minPair.getValue();

    while (minCode != null && minimal) {
      GrowChildrenByAllEdges growChildrenByAllEdges = new GrowChildrenByAllEdges(minCode);

      List<Pair<DfsCode, WithEmbedding>> children = Lists.newArrayList();
      minEmbeddings
        .forEach(e -> growChildrenByAllEdges.addChildren(dfsCode, e.getEmbedding(), children));

      if (!children.isEmpty()) {
        children.sort(Comparator.comparing(Pair::getKey));

        minCode = children.get(0).getKey();
        minimal = minCode.parentOf(dfsCode);

        if (minimal) {
          DfsCode finalMinCode = minCode;
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
