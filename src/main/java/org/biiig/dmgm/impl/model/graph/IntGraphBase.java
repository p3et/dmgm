package org.biiig.dmgm.impl.model.graph;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.Arrays;
import java.util.List;

/**
 * Created by peet on 02.08.17.
 */
public abstract class IntGraphBase implements IntGraph {
  protected int[][] vertexData;
  protected int[][] edgeData;

  public IntGraphBase(int edgeCount, int vertexCount) {
    edgeData = new int[edgeCount][];
    vertexData = new int[vertexCount][];
  }

  @Override
  public int[] getVertexData(int vertexId) {
    return vertexData[vertexId];
  }

  @Override
  public int[] getEdgeData(int edgeId) {
    return edgeData[edgeId];
  }

  @Override
  public int getVertexCount() {
    return vertexData.length;
  }

  @Override
  public int getEdgeCount() {
    return edgeData.length;
  }

  @Override
  public String toString() {
    return "g=\n" +
      "Lv=" + toString(vertexData) + "\n" +
      "Le=" + toString(edgeData);
  }

  private String toString(int[][] data) {
    List<String> elementStrings = Lists.newArrayListWithExpectedSize(data.length);

    for (int i = 0; i < data.length; i++) {
      elementStrings.add(String.valueOf(i) + ":" + Arrays.toString(data[i]));
    }

    return StringUtils.join(elementStrings, ",");
  }
}
