package org.biiig.dmgm.impl.operators;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.GraphDB;

import java.util.stream.Stream;

public abstract class CollectionOperatorBase implements CollectionOperator {
  protected boolean parallel = false;

  @Override
  public CollectionOperator parallel() {
    this.parallel = true;
    return this;
  }

  @Override
  public CollectionOperator sequential() {
    this.parallel = false;
    return this;
  }

  public long createGraph(GraphDB db, CachedGraph graph) {
    long[] vertexIds = graph
      .vertexIdStream()
      .map(graph::getVertexLabel)
      .mapToLong(db::createVertex)
      .toArray();

    long[] edgeIds = graph
      .edgeIdStream()
      .mapToLong(edgeId -> {
        int label = graph.getEdgeLabel(edgeId);
        long sourceId = vertexIds[graph.getSourceId(edgeId)];
        long targetId = vertexIds[graph.getTargetId(edgeId)];
        return db.createEdge(label, sourceId, targetId);
      })
      .toArray();

    return db.createGraph(graph.getLabel(), vertexIds, edgeIds);
  }
}
