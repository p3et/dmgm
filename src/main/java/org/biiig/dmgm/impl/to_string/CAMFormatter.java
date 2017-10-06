package org.biiig.dmgm.impl.to_string;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.to_string.DMGraphFormatter;

import java.util.Map;

public class CAMFormatter implements DMGraphFormatter {

  private static final char VERTEX_SEPARATOR = '\n';

  @Override
  public String format(DMGraph graph) {

    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++) {

      // determine outgoing edge labels

      Map<Integer, int[]> outgoingEdgeLabels = Maps.newHashMap();

      for (int edgeId : graph.getOutgoingEdgeIds(vertexId)) {
        int targetId = graph.getTargetId(edgeId);
        int edgeLabel = graph.getEdgeLabel(edgeId);

        int[] edgeLabels = outgoingEdgeLabels.get(targetId);

        edgeLabels = edgeLabels == null ?
          new int[] {edgeLabel} :
          ArrayUtils.add(edgeLabels, edgeLabel);

        outgoingEdgeLabels.put(targetId, edgeLabels);
      }

      // create outgoing edge strings




    }

    return StringUtils.join(vertexStrings, VERTEX_SEPARATOR);
  }

}
