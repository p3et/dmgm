package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;

public class ELEdgeFormatter {

  private final LabelDictionary edgeDictionary;

  public ELEdgeFormatter(LabelDictionary edgeDictionary) {
    this.edgeDictionary = edgeDictionary;
  }

  public String format(DMGraph graph, int edgeId) {
    return "-" + edgeDictionary.translate(graph.getEdgeLabel(edgeId)) + ">";
  }
}
