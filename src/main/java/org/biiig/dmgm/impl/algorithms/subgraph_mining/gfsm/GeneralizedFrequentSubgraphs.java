package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;

import java.util.Map;
import java.util.Optional;

public class GeneralizedFrequentSubgraphs extends SubgraphMiningBase {

  private final Map<String, StringTaxonomy> taxonomies;

  public GeneralizedFrequentSubgraphs(float minSupportRel, int maxEdgeCount, Map<String, StringTaxonomy> taxonomies) {
    super(minSupportRel, maxEdgeCount);
    this.taxonomies = taxonomies;
  }

  @Override
  public FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input) {
    ElementDataStore dataStore = input.getElementDataStore();
    LabelDictionary dictionary = input.getLabelDictionary();

    // cache taxonomy path for every linkable vertex
    input
      .parallelStream()
      .forEach(graph -> graph
        .vertexIdStream()
        .forEach(vertexId -> dataStore
          .getVertexString(graph.getId(), vertexId, SubgraphMiningPropertyKeys.TAXONOMY_VALUE)
          .ifPresent(value -> Optional
            .of(taxonomies.get(dictionary.translate(graph.getVertexLabel(vertexId))))
            .ifPresent(taxonomy -> taxonomy.getRootPathTo(value)
              .ifPresent(stringPath -> {
                int[] intPath = new int[stringPath.length];
                for (int i = 0; i < stringPath.length; i++)
                  intPath[i] = dictionary.translate(stringPath[i]);
                dataStore.setVertex(graph.getId(), vertexId, SubgraphMiningPropertyKeys.TAXONOMY_PATH, intPath);
              })))));

    return new FrequentGeneralizationFactory(Math.round(minSupport * input.size()));
  }

}
