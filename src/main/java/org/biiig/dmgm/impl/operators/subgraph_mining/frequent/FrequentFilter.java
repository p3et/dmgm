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

package org.biiig.dmgm.impl.operators.subgraph_mining.frequent;

import javafx.util.Pair;
import org.biiig.dmgm.api.db.QueryElements;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;

import java.util.Optional;
import java.util.function.Consumer;

public class FrequentFilter<T extends DFSCodeSupportablePair> implements FilterOrOutput<T> {

  private final int minSupportAbsolute;

  public FrequentFilter(int minSupportAbsolute) {
    this.minSupportAbsolute = minSupportAbsolute;
  }

  @Override
  public Pair<Optional<T>, Optional<Consumer<QueryElements>>> apply(T supportable) {    int embeddingCount = 0;
    long support =0l;

    boolean frequent = support >= minSupportAbsolute;

    Optional<T> child;
    Optional<Consumer<QueryElements>> store;

    if (frequent) {
      child = Optional.of(supportable);
      store = Optional.of(s -> {
//        int graphId = s.add(supportable.getDFSCode());
//        s.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
//        s.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, embeddingCount);
      });

    } else {
      child = Optional.empty();
      store = Optional.empty();
    }


    return new Pair<>(child, store);
  }
}
