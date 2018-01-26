package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;

public class ELEdgeFormatter {

  private final HyperVertexDB db;

  public ELEdgeFormatter(HyperVertexDB db) {
    this.db = db;
  }

  public String format(SmallGraph graph, int edgeId) {
    return "-" + db.decode(graph.getEdgeLabel(edgeId)) + ">";
  }
}
