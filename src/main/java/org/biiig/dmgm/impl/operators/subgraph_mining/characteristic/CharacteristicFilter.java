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

package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.QueryElements;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CharacteristicFilter<T extends DFSCodeSupportablePair> implements FilterOrOutput<T> {
  private final Map<Integer, Integer> graphLabel;
  private final Map<Integer, Integer> graphLabelCounts;


  private final Interestingness interestingness;
  private final int graphCount;
  private final float minSupport;

  CharacteristicFilter(
    Interestingness interestingness,
    Map<Integer, Integer> graphLabel,
    Map<Integer, Integer> graphLabelCounts,
    int graphCount,
    float minSupport) {
    this.graphLabel = graphLabel;
    this.graphLabelCounts = graphLabelCounts;
    this.interestingness = interestingness;
    this.graphCount = graphCount;

    this.minSupport = minSupport;
  }

  @Override
  public Pair<Optional<T>, Optional<Consumer<QueryElements>>> apply(T supportable) {
    Map<Integer, List<DFSEmbedding>> categoryEmbeddings = supportable
      .getEmbeddings()
      .stream()
      .collect(new GroupByKeyListValues<>(
        e -> graphLabel.get(e.getGraphId()),
        Function.identity())
      );

    Map<Integer, Float> labelSupports = categoryEmbeddings
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        Map.Entry::getKey,
        e -> (float) e.getValue()
          .stream()
          .map(DFSEmbedding::getGraphId)
          .distinct()
          .count()
          / graphLabelCounts.get(e.getKey())));

    float totalSupport = 0l;


    Optional<T> child;
    Optional<Consumer<QueryElements>> store;

    boolean atLeastOnceFrequent = false;

    for (float support : labelSupports.values())
      if (atLeastOnceFrequent = support >= minSupport)
        break;

    if (atLeastOnceFrequent) {
      child = Optional.of(supportable);

      int[] labels = interestingness.getInterestingCategories(labelSupports, totalSupport);

      if (labels != null && labels.length > 0) {
        Collection<Consumer<QueryElements>> outputs = Lists.newArrayListWithCapacity(labels.length);

        for (int label : labels) {
          outputs.add(output -> {
//            DFSCode dfsCode = supportable.getDFSCode().addEdge();
//            dfsCode.setLabel(label);
//            int graphId = output.add(dfsCode);
            BigDecimal support = BigDecimal.valueOf(labelSupports.get(label));
//            output.getElementDataStore()
//              .setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
          });
        }

        store = Optional.of(s -> outputs.forEach(o -> o.accept(s)));

      } else {
        store = Optional.empty();
      }
    } else {
      child = Optional.empty();
      store = Optional.empty();
    }

    return new Pair<>(child, store);
  }
}
