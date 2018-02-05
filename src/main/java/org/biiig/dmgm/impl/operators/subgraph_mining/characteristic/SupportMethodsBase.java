package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;

public class SupportMethodsBase {
  protected static final String DFS_CODE = "_dfsCode";
  protected final GraphDB database;
  protected final int dfsCodeKey;
  protected final int supportKey;
  protected final boolean parallel;

  public SupportMethodsBase(GraphDB database, boolean parallel) {
    this.database = database;
    supportKey = database.encode(PropertyKeys.SUPPORT);
    dfsCodeKey = database.encode(DFS_CODE);
    this.parallel = parallel;
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
