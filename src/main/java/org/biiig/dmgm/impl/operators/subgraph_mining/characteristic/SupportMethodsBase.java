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
    int vertexCount = graph.getVertexCount();
    long[] vertexIds = new long[vertexCount];
    for (int v = 0; v < vertexCount; v++)
      vertexIds[v] = db.createVertex(graph.getVertexLabel(v));


    int edgeCount = graph.getEdgeCount();
    long[] edgeIds = new long[edgeCount];
    for (int e = 0; e < edgeCount; e++) {
      int label = graph.getEdgeLabel(e);
      long sourceId = vertexIds[graph.getSourceId(e)];
      long targetId = vertexIds[graph.getTargetId(e)];
      edgeIds[e] = db.createEdge(label, sourceId, targetId);
    }

    return db.createGraph(graph.getLabel(), vertexIds, edgeIds);
  }
}
