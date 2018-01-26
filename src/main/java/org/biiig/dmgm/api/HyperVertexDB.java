package org.biiig.dmgm.api;

import org.biiig.dmgm.impl.db.LongsPair;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.List;

/**
 * Describes a database that supports:
 * - the Extended Property Graph Model
 * - edges between arbitrary elements (vertices, graphs, edges)
 * - dictionary coding for all symbols such as labels and property keys
 */
public interface HyperVertexDB extends PropertyStore {

  /**
   * Encode a string symbol (e.g., label or property key)
   *
   * @param value original value
   * @return symbol
   */
  int encode(String value);

  /**
   * Decode a symbol (e.g., label or property key
   *
   * @param symbol encoded value
   * @return original value
   */
  String decode(int symbol);

  /**
   * Create a vertex.
   *
   * @param label label
   * @return id
   */
  long createVertex(int label);

  /**
   * Create an edge.
   *
   * @param label label
   * @param sourceId source element id
   * @param targetId target element id
   * @return id
   */
  long createEdge(int label, long sourceId, long targetId);

  /**
   * Create a graph.
   *
   * @param label label
   * @param vertices element ids in the role of vertices
   * @param edges element ids in the role of edges
   * @return id
   */
  long createHyperVertex(int label, long[] vertices, long[] edges);
  long[] getGraphsOfVertex(long id);
  long[] getGraphsOfEdge(long id);
  SmallGraph getSmallGraph(long hyperVertexId);

  LongsPair getElementsOf(long hyperVertexId);

  int getLabel(long id);

  List<SmallGraph> getCollection(Long collectionId);

  long createHyperVertex(SmallGraph graph);

  long createCollectionByLabel(int graphLabel, int collectionLabel);
}
