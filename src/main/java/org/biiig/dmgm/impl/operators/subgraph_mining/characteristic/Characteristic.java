package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeSupportablePair;

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
