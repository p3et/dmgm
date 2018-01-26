package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.SmallGraphBase;

import java.util.Set;
import java.util.function.Function;

public class PruneVertices implements Function<SmallGraph, SmallGraph> {
  private final Set<Integer> frequentVertexLabels;

  public PruneVertices(Set<Integer> frequentVertexLabels) {
    this.frequentVertexLabels = frequentVertexLabels;
  }

  @Override
  public SmallGraph apply(SmallGraph inGraph) {
    int[] vertexMap = new int[inGraph.getVertexCount()];
    for (int vertexId = 0; vertexId < inGraph.getVertexCount(); vertexId++)
      vertexMap[vertexId] = -1;

    SmallGraph outGraph = new SmallGraphBase(id, label, vertexLabels, edgeLabels, sourceIds, targetIds);
    outGraph.setLabel(inGraph.getLabel());

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
