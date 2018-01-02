package org.biiig.dmgm.api.model.collection;

import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.api.model.graph.IntGraph;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface GraphCollection extends Iterable<IntGraph> {
  GraphCollection withVertexDictionary(LabelDictionary dictionary);

  GraphCollection withEdgeDictionary(LabelDictionary dictionary);

  LabelDictionary getVertexDictionary();

  LabelDictionary getEdgeDictionary();

  int size();

  void add(IntGraph graph);

  IntGraph getGraph(int graphId);

  Stream<IntGraph> stream();

  Stream<IntGraph> parallelStream();

  default GraphCollection apply(Operator operator) {
    return operator.apply(this);
  }

  static String toString(GraphCollection collection) {
    List<String> graphStrings = collection
      .stream()
      .map(g -> toString(g, collection.getVertexDictionary(), collection.getEdgeDictionary()))
      .collect(Collectors.toList());

    return StringUtils.join(graphStrings, "\n");
  }

  static String toString(IntGraph graph, LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) {
    return formatGraph(graph) +
      "\n\tV={" + formatVertices(graph, vertexDictionary) + "}" +
      "\n\tE={" + formatEdges(graph, edgeDictionary) + "}";
  }

  static String formatEdges(IntGraph graph, LabelDictionary edgeDictionary) {
    String[] edgeStrings = new String[graph.getEdgeCount()];

    for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++)
      edgeStrings[edgeId] = formatEdge(graph, edgeDictionary, edgeId);

    return StringUtils.join(edgeStrings, ",");
  }

  static String formatEdge(IntGraph graph, LabelDictionary edgeDictionary, int edgeId) {
    return graph.getSourceId(edgeId) + "-" +
      edgeDictionary.translate(graph.getEdgeLabel(edgeId)) + "->" +
      graph.getTargetId(edgeId);
  }

  static String formatVertices(IntGraph graph, LabelDictionary vertexDictionary) {
    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++)
      vertexStrings[vertexId] = formatVertex(graph, vertexDictionary, vertexId);

    return StringUtils.join(vertexStrings, ",");
  }

  static String formatVertex(IntGraph graph, LabelDictionary vertexDictionary, int vertexId) {
    return "(" + vertexId + ":" + vertexDictionary.translate(graph.getVertexLabel(vertexId)) + ")";
  }

  static String formatGraph(IntGraph graph) {
    return "G[id=" + graph.getId() + "]";
  }
}
