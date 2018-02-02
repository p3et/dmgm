package org.biiig.dmgm.api;

import org.biiig.dmgm.impl.db.LongsPair;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Describes a database that supports:
 * - the Extended Property Graph Model
 * - edges between arbitrary elements (vertices, graphs, edges)
 * - dictionary coding for all symbols such as labels and property keys
 */
public interface GraphDB extends PropertyStore, LabelDictionary {

  // CREATE

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
   * Create a graph (Hypervertex).
   *
   * @param label label
   * @param vertexIds element ids in the role of vertices
   * @param edgeIds element ids in the role of edges
   * @return id
   */
  long createGraph(int label, long[] vertexIds, long[] edgeIds);

  /**
   * Create a graph collection (Hypervertex without edges).
   *
   * @param graphIds
   * @return
   */
  default long createCollection(int label, long[] graphIds) {
    return createGraph(label, graphIds, new long[0]);
  }

  // READ

  /**
   * Returns the label of an element.
   *
   * @param id graph, vertex or edge id
   * @return label
   */
  int getLabel(long id);

  /**
   * Query identifiers of elements matching a given label predicate.
   *
   * @param predicate label predicate
   * @return identifiers
   */
  long[] getElementsByLabel(IntPredicate predicate);

  /**
   * Query identifiers of elements matching a given property predicate.
   *
   * @param predicate over elementId and property store
   * @return
   */
  long[] getElementsByProperties(PropertyPredicate predicate);

  /**
   * Query vertices and edges of a graph.
   *
   * @param graphId
   * @return
   */
  LongsPair getGraphElementIds(long graphId);

  /**
   * Query all graph ids in which a vertex appears.
   *
   * @param vertexId id of this vertex.
   * @return all graph ids
   */
  long[] getGraphIdsOfVertex(long vertexId);

  /**
   * Query all graph ids in which an edge appears.
   *
   * @param edgeId id of this vertex.
   * @return all graph ids
   */
  long[] getGraphIdsOfEdge(long edgeId);

  /**
   * Materialize a single graph and return a respective Pojo.
   *
   * @param graphId graph id
   * @return cached immutable graph pojo
   */
  CachedGraph getCachedGraph(long graphId);

  /**
   * Materialize a graph collection and return a list of respective Pojos.
   *
   * @param collectionId hypervertex id
   * @return list of cached immutable graph pojos
   */
  List<CachedGraph> getCachedCollection(long collectionId);

  long[] getAllHyperVertexIds();
  long[] getAllGraphIds();
  long[] getAllCollectionIds();
  long[] getAllVertexIds();
  long[] getAllEdgeIds();
}
