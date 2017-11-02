package org.biiig.dmgm.impl.to_string;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.to_string.DMGraphFormatter;

public class CAMGraphFormatter implements DMGraphFormatter {

  private static final char VERTEX_SEPARATOR = '\n';

  private final org.biiig.dmgm.impl.to_string.CAMVertexFormatter vertexLabeler;

  public CAMGraphFormatter(LabelDictionary vertexLabelDictionary, LabelDictionary edgeLabelDictionary) {
    this.vertexLabeler = new org.biiig.dmgm.impl.to_string.CAMVertexFormatter(vertexLabelDictionary, edgeLabelDictionary);
  }


  @Override
  public String format(DMGraph graph) {

    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++) {
      vertexStrings[vertexId] = vertexLabeler.format(graph, vertexId);
    }

    return "[\n" + StringUtils.join(vertexStrings, VERTEX_SEPARATOR) + "\n]";
  }
}
