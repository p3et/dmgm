package org.biiig.dmgm.todo.mining;

import org.biiig.dmgm.todo.pvalues.model.VertexDegreeKey;

/**
 * Created by peet on 14.07.17.
 */
public class VertexDegreeDistributionKey implements Comparable<VertexDegreeDistributionKey> {
  private final VertexDegreeKey vertexDegreeKey;
  private final int frequency;

  public VertexDegreeDistributionKey(VertexDegreeKey vertexDegreeKey, int frequency) {
    this.vertexDegreeKey = vertexDegreeKey;
    this.frequency = frequency;
  }

  @Override
  public int compareTo(VertexDegreeDistributionKey o) {
    int comparison = vertexDegreeKey.compareTo(o.vertexDegreeKey);

    if (comparison == 0) {
      comparison = this.frequency - o.frequency;
    }

    return comparison;
  }
}
