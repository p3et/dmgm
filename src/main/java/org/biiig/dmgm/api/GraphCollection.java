package org.biiig.dmgm.api;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public interface GraphCollection extends Iterable<Graph> {
  LabelDictionary getLabelDictionary();

  int size();

  int add(Graph graph);

  Graph getGraph(int graphId);

  ElementDataStore getElementDataStore();

  Stream<Graph> stream();

  Stream<Graph> parallelStream();

  default GraphCollection apply(Operator operator) {
    return operator.apply(this);
  }

  static String toString(GraphCollection collection) {
    List<String> graphStrings = collection
      .stream()
      .map(g -> toString(g, collection.getLabelDictionary()))
      .collect(Collectors.toList());

    return StringUtils.join(graphStrings, "\n");
  }

  static String toString(Graph graph, LabelDictionary dictionary) {
    return formatGraph(graph, dictionary) +
      "\n\tV={" + formatVertices(graph, dictionary) + "}" +
      "\n\tE={" + formatEdges(graph, dictionary) + "}";
  }

  static String formatEdges(Graph graph, LabelDictionary edgeDictionary) {
    String[] edgeStrings = new String[graph.getEdgeCount()];

    for (int edgeId = 0; edgeId < graph.getEdgeCount(); edgeId++)
      edgeStrings[edgeId] = formatEdge(graph, edgeDictionary, edgeId);

    return StringUtils.join(edgeStrings, ",");
  }

  static String formatEdge(Graph graph, LabelDictionary edgeDictionary, int edgeId) {
    return graph.getSourceId(edgeId) + "-" +
      edgeDictionary.translate(graph.getEdgeLabel(edgeId)) + "->" +
      graph.getTargetId(edgeId);
  }

  static String formatVertices(Graph graph, LabelDictionary vertexDictionary) {
    String[] vertexStrings = new String[graph.getVertexCount()];

    for (int vertexId = 0; vertexId < graph.getVertexCount(); vertexId++)
      vertexStrings[vertexId] = formatVertex(graph, vertexDictionary, vertexId);

    return StringUtils.join(vertexStrings, ",");
  }

  static String formatVertex(Graph graph, LabelDictionary dictionary, int vertexId) {
    return "(" + vertexId + ":" + dictionary.translate(graph.getVertexLabel(vertexId)) + ")";
  }

  static String formatGraph(Graph graph, LabelDictionary dictionary) {
    return dictionary.translate(graph.getLabel()) + "[id=" + graph.getId() + "]";
  }
}
