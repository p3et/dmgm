package org.biiig.dmgm.impl.model.source.gdl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.model.collection.InMemoryLabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;
import org.biiig.dmgm.impl.model.source.tlf.GraphCollectionLoaderBase;
import org.s1ck.gdl.GDLHandler;
import org.s1ck.gdl.model.Edge;
import org.s1ck.gdl.model.Graph;
import org.s1ck.gdl.model.Vertex;

import java.util.List;
import java.util.Map;

public class GDLLoader extends GraphCollectionLoaderBase {

  private final String gdlString;

  private GDLLoader(String gdlString) {
    this.gdlString = gdlString;
  }

  @Override
  public GraphCollection getGraphCollection() {
    GraphCollection graphCollection = collectionFactory.create();

    GDLHandler gdlHandler = new GDLHandler.Builder().buildFromString(gdlString);

    // read graphs
    Map<Long, List<Vertex>> graphVertices = Maps.newHashMap();
    Map<Long, List<Edge>> graphEdges = Maps.newHashMap();
    for (Graph graph : gdlHandler.getGraphs()) {
      long graphId = graph.getId();
      graphVertices.put(graphId, Lists.newArrayList());
      graphEdges.put(graphId, Lists.newArrayList());
    }

    // read vertices

    List<Countable<String>> labelFrequencies = Lists.newLinkedList();
    for (Vertex vertex : gdlHandler.getVertices()) {
      for (long graphId : vertex.getGraphs()) {
        graphVertices.get(graphId).add(vertex);
        labelFrequencies.add(new Countable<>(vertex.getLabel()));
      }
    }
    LabelDictionary dictionary = new InMemoryLabelDictionary(labelFrequencies);
    graphCollection.withVertexDictionary(dictionary);

    // read edges

    labelFrequencies = Lists.newLinkedList();
    for (Edge edge : gdlHandler.getEdges()) {
      for (long graphId : edge.getGraphs()) {
        graphEdges.get(graphId).add(edge);
        labelFrequencies.add(new Countable<>(edge.getLabel()));
      }
    }
    dictionary = new InMemoryLabelDictionary(labelFrequencies);
    graphCollection.withEdgeDictionary(dictionary);

    // write graphs

    for (Graph graph : gdlHandler.getGraphs()) {
      long graphId = graph.getId();
      List<Vertex> vertices = graphVertices.get(graphId);
      Map<Long, Integer> vertexIdMap = Maps.newHashMapWithExpectedSize(vertices.size());
      List<Edge> edges = graphEdges.get(graphId);

      IntGraph dmGraph = graphFactory.create();

      // write vertices

      int vertexId = 0;
      for (Vertex vertex : vertices) {
        vertexIdMap.put(vertex.getId(), vertexId);
        dmGraph.addVertex(
          graphCollection.getVertexDictionary().translate(vertex.getLabel())
        );
        vertexId++;
      }

      // write edges

      for (Edge edge : edges) {
        dmGraph.addEdge(
          vertexIdMap.get(edge.getSourceVertexId()),
          vertexIdMap.get(edge.getTargetVertexId()),
          graphCollection.getEdgeDictionary().translate(edge.getLabel())
        );
      }

      graphCollection.add(dmGraph);
    }

    return graphCollection;
  }

  public static GDLLoader fromString(String gdlString) {
    return new GDLLoader(gdlString);
  }
}
