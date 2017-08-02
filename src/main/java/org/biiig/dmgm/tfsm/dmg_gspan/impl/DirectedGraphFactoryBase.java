package org.biiig.dmgm.tfsm.dmg_gspan.impl;

import org.biiig.dmgm.tfsm.dmg_gspan.api.model.DirectedGraph;
import org.biiig.dmgm.tfsm.dmg_gspan.api.model.DirectedGraphFactory;

public abstract class DirectedGraphFactoryBase implements DirectedGraphFactory {
  @Override
  public abstract DirectedGraph create(int vertexCount, int edgeCount);

  @Override
  public DirectedGraph convert(DirectedGraph fromGraph) {
    int vertexCount = fromGraph.getVertexCount();
    int edgeCount = fromGraph.getEdgeCount();

    DirectedGraph toGraph = create(vertexCount, edgeCount);

    for (int vertexId = 0; vertexId < vertexCount; vertexId++) {
      toGraph.setVertex(vertexId, fromGraph.getVertexData(vertexId));
    }

    for (int edgeId = 0; edgeId < vertexCount; edgeId++) {
      toGraph.setEdge(
        edgeId,
        fromGraph.getSourceId(edgeId),
        fromGraph.getTargetId(edgeId) ,
        fromGraph.getEdgeData(edgeId)
      );
    }

    return toGraph;
  }
}
