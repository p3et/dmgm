package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import com.google.common.collect.Maps;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPropertyKeys;

import java.util.Map;

public abstract class GeneralizedSubgraphsBase extends SubgraphMiningBase {
  public static final String LEVEL_SEPARATOR = "_";

  public GeneralizedSubgraphsBase(float minSupport, int maxEdgeCount) {
    super(minSupport, maxEdgeCount);
  }

  protected Specializer getSpecializer(GraphCollection rawInput, FilterOrOutput<PatternVectorsPair> vectorFilter) {
    PropertyStore dataStore = new InMemoryPropertyStore();
    Specializer spezializer = new Specializer(dataStore, vectorFilter);
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
    return spezializer;
  }
}
