package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph.GraphBase;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class FrequentSpecialization extends FilterAndOutputBase 
  implements Predicate<Map.Entry<MultiDimensionalVector, List<MultiDimensionalVector>>> {

  private final int minSupportAbsolute;
  private final DFSCode dfsCode;

  FrequentSpecialization(GraphCollection output, int minSupportAbsolute, DFSCode dfsCode) {
    super(output);
    this.minSupportAbsolute = minSupportAbsolute;
    this.dfsCode = dfsCode;
  }

  @Override
  public boolean test(Map.Entry<MultiDimensionalVector, List<MultiDimensionalVector>> entry) {
    List<MultiDimensionalVector> embeddings = entry.getValue();

    int embeddingCount = embeddings.size();
    int support = embeddingCount >= minSupportAbsolute ? getSupport(embeddings) : 0;

    boolean frequent = support >= minSupportAbsolute;
    if (frequent)
      store(entry.getKey(), embeddingCount, support);

    return frequent;
  }
  
  private void store(MultiDimensionalVector vector, int embeddingCount, int support) {
    Graph graph = new GraphBase();

    dfsCode
      .vertexIdStream()
      .forEach(dim -> graph
        .addVertex(
          vector.getSpecializedValue(dim)
            .orElse(dfsCode.getVertexLabel(dim))
        ));

    dfsCode
      .edgeIdStream()
      .forEach(e ->
        graph.addEdge(
          dfsCode.getSourceId(e),
          dfsCode.getTargetId(e),
          dfsCode.getEdgeLabel(e)
        ));

    int graphId = output.add(graph);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.EMBEDDING_COUNT, embeddingCount);
  }
}
