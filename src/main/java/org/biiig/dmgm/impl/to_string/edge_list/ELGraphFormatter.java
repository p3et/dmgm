package org.biiig.dmgm.impl.to_string.edge_list;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.DMGraphFormatter;

public class ELGraphFormatter implements DMGraphFormatter {
  private static final char EDGE_SEPARATOR = ',';

  private final ELTripleFormatter edgeFormatter;

  public ELGraphFormatter(LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    this.edgeFormatter = new ELTripleFormatter(vertexDictionary, edgeDictionary);
  }

  @Override
  public String format(Graph graph) {
    String[] edgeStrings = new String[graph.getEdgeCount()];

    for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++) {
      edgeStrings[edgeId] = edgeFormatter.format(graph, edgeId);
    }

    return StringUtils.join(edgeStrings, EDGE_SEPARATOR);
  }
}
