package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.impl.graph.GraphBase;

import java.util.Set;
import java.util.function.Function;

public class PruneVertices implements Function<Graph, Graph> {
  private final Set<Integer> frequentVertexLabels;

  public PruneVertices(Set<Integer> frequentVertexLabels) {
    this.frequentVertexLabels = frequentVertexLabels;
  }

  @Override
  public Graph apply(Graph inGraph) {
    int[] vertexMap = new int[inGraph.getVertexCount()];
    for (int vertexId = 0; vertexId < inGraph.getVertexCount(); vertexId++)
      vertexMap[vertexId] = -1;

    Graph outGraph = new GraphBase();

    for (int vertexId = 0; vertexId < inGraph.getVertexCount(); vertexId++) {
      int vertexLabel = inGraph.getVertexLabel(vertexId);
      if (frequentVertexLabels.contains(vertexLabel)) {
        outGraph.addVertex(vertexLabel);
        vertexMap[vertexId] = outGraph.getVertexCount() - 1;
      }
    }

    for (int edgeId = 0; edgeId < inGraph.getEdgeCount(); edgeId++) {

      int sourceMapping = vertexMap[inGraph.getSourceId(edgeId)];
      if (sourceMapping > -1) {

        int targetMapping = vertexMap[inGraph.getTargetId(edgeId)];
        if (targetMapping > -1) {

         outGraph.addEdge(sourceMapping, targetMapping, inGraph.getEdgeLabel(edgeId));
        }
      }
    }

    return outGraph;
  }
}
