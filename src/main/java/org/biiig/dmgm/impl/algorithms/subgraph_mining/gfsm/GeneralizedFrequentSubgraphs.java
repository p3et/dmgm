package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Maps;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.fsm.Frequent;
import org.biiig.dmgm.impl.data_store.InMemoryElementDataStore;

import java.util.Map;

public class GeneralizedFrequentSubgraphs extends SubgraphMiningBase {

  public static final String LEVEL_SEPARATOR = "x";

  public GeneralizedFrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  protected FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput) {
    int minSupportAbsolute = Math.round(minSupport * rawInput.size());
    ElementDataStore dataStore = new InMemoryElementDataStore();
    FilterOrOutput<PatternVectorsPair> filter = new Frequent<>(minSupportAbsolute);
    Specializer spezializer = new Specializer(dataStore, filter);

    LabelDictionary dictionary = rawInput.getLabelDictionary();
    Map<Integer, Pair<Integer, int[]>> pathCache = Maps.newConcurrentMap();

    // cache taxonomy path for every linkable vertex
    rawInput
      .parallelStream()
      .forEach(graph -> graph
        .vertexIdStream()
        .forEach(vertexId -> {
          int bottomLevel = graph.getVertexLabel(vertexId);

          Pair<Integer, int[]> pathPair = pathCache.get(bottomLevel);

          // if label has no assigned taxonomy path
          if (pathPair == null) {
            String label = dictionary.translate(bottomLevel);

            // if at least two separators (because taxonomy.topLevel.spec1,..)
            if (StringUtils.countMatches(label, LEVEL_SEPARATOR) > 1) {
              int[] specPath = new int[] {bottomLevel};

              // generalize while at least two separators (taxonomy.topLevel.spec1)
              while (StringUtils.countMatches(label, LEVEL_SEPARATOR) > 2) {
                label = StringUtils.substringBeforeLast(label, LEVEL_SEPARATOR);
                specPath = ArrayUtils.add(specPath, dictionary.translate(label));
              }

              ArrayUtils.reverse(specPath);

              // generalize top level taxonomy.topLevel
              int topLevel = dictionary
                .translate(StringUtils.substringBeforeLast(label, LEVEL_SEPARATOR));
              pathPair = new Pair<>(topLevel, specPath);
              pathCache.put(bottomLevel, pathPair);
            }
          }

          if (pathPair != null) {
            // replace label with top level
            graph.setVertexLabel(vertexId, pathPair.getKey());

            // store path of specializations

            dataStore
              .setVertex(
                graph.getId(),
                vertexId,
                SubgraphMiningPropertyKeys.TAXONOMY_PATH,
                pathPair.getValue()
              );
          }
        }));

    return new Generalized(minSupportAbsolute, spezializer);
  }

}
