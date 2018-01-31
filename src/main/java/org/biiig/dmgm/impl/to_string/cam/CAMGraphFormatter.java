package org.biiig.dmgm.impl.to_string.cam;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.DMGraphFormatter;

import java.util.Arrays;

public class CAMGraphFormatter implements DMGraphFormatter {

  private static final char VERTEX_SEPARATOR = ',';

  private final CAMVertexFormatter vertexFormatter;

  public CAMGraphFormatter(GraphDB db) {
    this.vertexFormatter = new CAMVertexFormatter(db);
  }


  @Override
  public String format(CachedGraph graph) {

    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++) {
      vertexStrings[vertexId] = vertexFormatter.format(graph, vertexId);
    }

    Arrays.sort(vertexStrings);

    return StringUtils.join(vertexStrings, VERTEX_SEPARATOR);
  }
}
