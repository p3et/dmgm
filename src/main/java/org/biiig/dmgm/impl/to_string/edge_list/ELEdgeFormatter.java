package org.biiig.dmgm.impl.to_string.edge_list;

import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.SmallGraph;

public class ELEdgeFormatter {

  private final LabelDictionary edgeDictionary;

  public ELEdgeFormatter(LabelDictionary edgeDictionary) {
    this.edgeDictionary = edgeDictionary;
  }

  public String format(SmallGraph graph, int edgeId) {
    return "-" + edgeDictionary.translate(graph.getEdgeLabel(edgeId)) + ">";
  }
}
