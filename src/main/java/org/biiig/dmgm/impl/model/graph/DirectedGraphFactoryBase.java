package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;

public abstract class DirectedGraphFactoryBase implements IntGraphFactory {
  @Override
  public abstract IntGraph create(int vertexCount, int edgeCount);

  @Override
  public IntGraph convert(IntGraph fromGraph) {
    int vertexCount = fromGraph.getVertexCount();
    int edgeCount = fromGraph.getEdgeCount();

    IntGraph toGraph = create(vertexCount, edgeCount);

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
