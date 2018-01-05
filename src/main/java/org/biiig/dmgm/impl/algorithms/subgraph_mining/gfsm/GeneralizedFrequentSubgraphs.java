package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;

public class GeneralizedFrequentSubgraphs extends SubgraphMiningBase {

  private static final String ARBITRARY = "*";

  public GeneralizedFrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  public FilterAndOutputFactory getFilterAndOutputFactory(GraphCollection input) {
    ElementDataStore dataStore = input.getElementDataStore();
    IntTaxonomies taxonomies = new IntTaxonomies();

    // cache taxonomy path for every linkable vertex
    input
      .parallelStream()
      .forEach(graph ->
        graph
          .vertexIdStream()
          .forEach(vertexId ->
            taxonomies
              .get(graph.getVertexLabel(vertexId))
              .ifPresent(taxonomy ->
                dataStore
                  .getVertexInteger(graph.getId(), vertexId, SubgraphMiningPropertyKeys.BOTTOM_LEVEL)
                  .ifPresent(value ->
                      taxonomy
                        .getRootPathTo(value)
                        .ifPresent(path ->
                          dataStore
                            .setVertex(graph.getId(), vertexId, SubgraphMiningPropertyKeys.TAXONOMY_PATH, path)
                        )))));

    return new FrequentGeneralizationFactory(Math.round(minSupport * input.size()));
  }

}
