package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.AdjacencyList;

import java.util.Set;
import java.util.function.Function;

public class PruneEdges implements Function<SmallGraph, SmallGraph> {
  private final Set<Integer> frequentEdgeLabels;

  public PruneEdges(Set<Integer> frequentEdgeLabels) {
    this.frequentEdgeLabels = frequentEdgeLabels;
  }

  @Override
  public SmallGraph apply(SmallGraph inGraph) {

    SmallGraph outGraph = new AdjacencyList();
    outGraph.setLabel(inGraph.getLabel());

    for (int vertexId = 0; vertexId < inGraph.getVertexCount(); vertexId++)
      outGraph.addVertex(inGraph.getVertexLabel(vertexId));
    
    for (int edgeId = 0; edgeId < inGraph.getEdgeCount(); edgeId++) {
      int edgeLabel = inGraph.getEdgeLabel(edgeId);

      if (frequentEdgeLabels.contains(edgeLabel))
        outGraph.addEdge(inGraph.getSourceId(edgeId), inGraph.getTargetId(edgeId), edgeLabel);
    }
      
    return outGraph;
  }
}
