package org.biiig.dmgm.impl.to_string.cam;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.to_string.DMGraphFormatter;

public class CAMGraphFormatter implements DMGraphFormatter {

  private static final char VERTEX_SEPARATOR = '\n';

  private final CAMVertexFormatter vertexFormatter;

  public CAMGraphFormatter(LabelDictionary vertexLabelDictionary, LabelDictionary edgeLabelDictionary) {
    this.vertexFormatter = new CAMVertexFormatter(vertexLabelDictionary, edgeLabelDictionary);
  }


  @Override
  public String format(DMGraph graph) {

    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++) {
      vertexStrings[vertexId] = vertexFormatter.format(graph, vertexId);
    }

    return StringUtils.join(vertexStrings, VERTEX_SEPARATOR);
  }
}
