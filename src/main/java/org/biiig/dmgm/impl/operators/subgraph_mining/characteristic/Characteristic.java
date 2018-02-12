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

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;

import java.util.List;
import java.util.Map;

public interface Characteristic {
  default Preprocessor getCharacteristicLabels(float minSupport) {
    return new CharacteristicLabels(minSupport);
  }

  default <T extends DFSCodeSupportablePair> FilterOrOutput<T> getCharacteristicFilter(List<CachedGraph> rawInput, float minSupport, Interestingness interestingness) {
    Map<Integer, Integer> labelCounts = Maps.newHashMap();
    Map<Integer, Integer> graphLabel = Maps.newHashMap();

    rawInput
      .forEach(g -> {
        int label = g.getLabel();
//        graphLabel.put(g.getId(), label);
        Integer count = labelCounts.get(label);
        labelCounts.put(label, count == null ? 1 : count + 1);
      });

    return new CharacteristicFilter<>(interestingness, graphLabel, labelCounts, rawInput.size(), minSupport);
  }


}
