/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.model;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.SymbolDictionary;
import org.biiig.dmgm.api.model.CachedGraph;

import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * Superclass of input graph representations.
 */
public class CachedGraphBase implements CachedGraph {

  /**
   * Global graph id.
   */
  private final long id;
  /**
   * Graph label.
   */
  protected final int label;
  /**
   * Encoded vertex labels.
   * Indexes correspond to local vertex ids.
   */
  protected final int[] vertexLabels;
  /**
   * Encoded edge labels.
   * Indexes correspond to local edge ids.
   */
  protected final int[] edgeLabels;
  /**
   * Local source vertex ids.
   * Indexes correspond to local edge ids.
   */
  protected final int[] sourceIds;
  /**
   * Local edge vertex ids.
   * Indexes correspond to local edge ids.
   */
  protected final int[] targetIds;

  /**
   * Constructor.
   *
   * @param id global graph id
   * @param label graph label
   * @param vertexLabels vertex labels
   * @param edgeLabels edge labels
   * @param sourceIds source ids of edges
   * @param targetIds target ids of edges
   */
  public CachedGraphBase(long id, int label, int[] vertexLabels, int[] edgeLabels, int[] sourceIds, int[] targetIds) {
    this.id = id;
    this.label = label;
    this.vertexLabels = vertexLabels;
    this.edgeLabels = edgeLabels;
    this.sourceIds = sourceIds;
    this.targetIds = targetIds;
  }


  @Override
  public int getLabel() {
    return label;
  }

  @Override
  public int getVertexCount() {
    return vertexLabels.length;
  }

  @Override
  public int getEdgeCount() {
    return edgeLabels.length;
  }

  @Override
  public int getVertexLabel(int vertexId) {
    return vertexLabels[vertexId];
  }

  @Override
  public int getEdgeLabel(int edgeId) {
    return edgeLabels[edgeId];
  }

  @Override
  public int getSourceId(int edgeId) {
    return sourceIds[edgeId];
  }

  @Override
  public int getTargetId(int edgeId) {
    return targetIds[edgeId];
  }

  @Override
  public int[] getOutgoingEdgeIds(int vertexId) {
    return IntStream.range(0, getEdgeCount())
      .filter(edgeId -> sourceIds[edgeId] == vertexId)
      .toArray();
  }

  @Override
  public int[] getIncomingEdgeIds(int vertexId) {
    return IntStream.range(0, getEdgeCount())
      .filter(edgeId -> targetIds[edgeId] == vertexId)
      .toArray();
  }

  @Override
  public long getId() {
    return id;
  }

  @Override
  public int[] getVertexLabels() {
    return vertexLabels;
  }

  @Override
  public int[] getEdgeLabels() {
    return edgeLabels;
  }

  @Override
  public int[] getSourceIds() {
    return sourceIds;
  }

  @Override
  public int[] getTargetIds() {
    return targetIds;
  }

  @Override
  public String toString(SymbolDictionary dictionary) {
    return formatGraph(dictionary::decode);
  }

  @Override
  public String toString() {
    return formatGraph(Object::toString);
  }

  /**
   * Generalization of graph formatting methods.
   * @param labelFormatter formatter for vertex labels
   * @return string representation of this graph
   */
  private String formatGraph(Function<Integer, String> labelFormatter) {

    String[] edgeStrings = new String[getEdgeCount()];
    for (int edgeId = 0; edgeId < getEdgeCount(); edgeId++)
      edgeStrings[edgeId] = formatEdge(edgeId, labelFormatter);

    return id +
      DMGMConstants.Separators.ID_LABEL +
      labelFormatter.apply(label) +
      DMGMConstants.Separators.KEY_VALUE +
      DMGMConstants.Elements.Collection.OPEN +
      StringUtils.join(edgeStrings, DMGMConstants.Separators.LIST) +
      DMGMConstants.Elements.Collection.CLOSE;
  }

  /**
   * Represent an edge by a string.
   *
   * @param edgeId edge id to format
   * @param labelFormatter function to format the edge label
   * @return string representation
   */
  private String formatEdge(int edgeId, Function<Integer, String> labelFormatter) {
    return formatVertex(getSourceId(edgeId), labelFormatter)
      + DMGMConstants.Elements.Edge.OPEN_OUTGOING
      + edgeId
      + DMGMConstants.Separators.ID_LABEL
      + labelFormatter.apply(getEdgeLabel(edgeId))
      + DMGMConstants.Elements.Edge.CLOSE_OUTGOING
      + formatVertex(getTargetId(edgeId), labelFormatter);
  }

  /**
   * Represent a vertex by a string.
   *
   * @param vertexId vertex id to format
   * @param labelFormatter function to format the vertex label
   * @return string representation
   */
  private String formatVertex(int vertexId, Function<Integer, String> labelFormatter) {
    return DMGMConstants.Elements.Vertex.OPEN +
      vertexId +
      DMGMConstants.Separators.ID_LABEL +
      labelFormatter.apply(getVertexLabel(vertexId)) +
      DMGMConstants.Elements.Vertex.CLOSE;
  }
}
