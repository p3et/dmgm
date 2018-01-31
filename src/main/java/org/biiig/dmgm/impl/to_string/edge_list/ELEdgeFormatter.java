package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;

public class ELEdgeFormatter {

  private final GraphDB db;

  public ELEdgeFormatter(GraphDB db) {
    this.db = db;
  }

  public String format(CachedGraph graph, int edgeId) {
    return "-" + db.decode(graph.getEdgeLabel(edgeId)) + ">";
  }
}
