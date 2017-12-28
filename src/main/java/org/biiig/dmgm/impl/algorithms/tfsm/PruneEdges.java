package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.model.graph.AdjacencyList;
import org.biiig.dmgm.impl.model.graph.IntGraphBase;

import java.util.Set;
import java.util.function.Function;

public class PruneEdges implements Function<IntGraph, IntGraph> {
  private final Set<Integer> frequentEdgeLabels;

  public PruneEdges(Set<Integer> frequentEdgeLabels) {
    this.frequentEdgeLabels = frequentEdgeLabels;
  }

  @Override
  public IntGraph apply(IntGraph inGraph) {

    IntGraph outGraph = new AdjacencyList();

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
