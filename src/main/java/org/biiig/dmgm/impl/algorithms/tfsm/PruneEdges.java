package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.AdjacencyList;

import java.util.Set;
import java.util.function.Function;

public class PruneEdges implements Function<Graph, Graph> {
  private final Set<Integer> frequentEdgeLabels;

  public PruneEdges(Set<Integer> frequentEdgeLabels) {
    this.frequentEdgeLabels = frequentEdgeLabels;
  }

  @Override
  public Graph apply(Graph inGraph) {

    Graph outGraph = new AdjacencyList();

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
