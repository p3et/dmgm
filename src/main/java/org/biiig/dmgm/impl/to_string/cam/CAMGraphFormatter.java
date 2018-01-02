package org.biiig.dmgm.impl.to_string.cam;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.DMGraphFormatter;

import java.util.Arrays;

public class CAMGraphFormatter implements DMGraphFormatter {

  private static final char VERTEX_SEPARATOR = ',';

  private final CAMVertexFormatter vertexFormatter;

  public CAMGraphFormatter(LabelDictionary vertexLabelDictionary, LabelDictionary edgeLabelDictionary) {
    this.vertexFormatter = new CAMVertexFormatter(vertexLabelDictionary, edgeLabelDictionary);
  }


  @Override
  public String format(Graph graph) {

    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++) {
      vertexStrings[vertexId] = vertexFormatter.format(graph, vertexId);
    }

    Arrays.sort(vertexStrings);

    return StringUtils.join(vertexStrings, VERTEX_SEPARATOR);
  }
}
