package org.biiig.dmgspan.gspan;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;

public class GraphEmbeddings {
  private final int graphId;
  private int[] vertices;
  private int[] edges;

  public GraphEmbeddings(int graphId, int fromId, int toId, int edgeId) {
    this.graphId = graphId;
    this.vertices = fromId == toId ? new int[] {fromId} : new int[] {fromId, toId};
    this.edges = new int[] {edgeId};
  }


  @Override
  public String toString() {
    Collection<Integer> vertexList = Lists.newArrayList();
    for (int i : vertices) {
      vertexList.add(i);
    }
    Collection<Integer> edgeList = Lists.newArrayList();
    for (int i : edges) {
      edgeList.add(i);
    }

    return String.valueOf(graphId) +
      ":[" +
      vertexList +
      "][" +
      edgeList +
      "]";
  }

  public void merge(GraphEmbeddings that) {
    this.vertices = ArrayUtils.addAll(this.vertices, that.vertices);
    this.edges = ArrayUtils.addAll(this.edges, that.edges);
  }
}
