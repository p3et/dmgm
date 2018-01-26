package org.biiig.dmgm.impl.to_string.edge_list;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.api.DMGraphFormatter;

public class ELGraphFormatter implements DMGraphFormatter {
  private static final char EDGE_SEPARATOR = ',';

  private final ELTripleFormatter edgeFormatter;

  public ELGraphFormatter(HyperVertexDB db) {
    this.edgeFormatter = new ELTripleFormatter(db);
  }

  @Override
  public String format(SmallGraph graph) {
    String[] edgeStrings = new String[graph.getEdgeCount()];

    for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++) {
      edgeStrings[edgeId] = edgeFormatter.format(graph, edgeId);
    }

    return StringUtils.join(edgeStrings, EDGE_SEPARATOR);
  }
}
