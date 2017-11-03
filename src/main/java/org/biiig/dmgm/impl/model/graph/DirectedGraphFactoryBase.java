package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;

public abstract class DirectedGraphFactoryBase implements DMGraphFactory {
  @Override
  public abstract DMGraph create(int vertexCount, int edgeCount);

  @Override
  public DMGraph convert(DMGraph fromGraph) {
    int vertexCount = fromGraph.getVertexCount();
    int edgeCount = fromGraph.getEdgeCount();

    DMGraph toGraph = create(vertexCount, edgeCount);

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
