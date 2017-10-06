package org.biiig.dmgm.impl.io.gdl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.io.DMGraphDataSource;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.biiig.dmgm.impl.db.LabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;
import org.s1ck.gdl.GDLHandler;
import org.s1ck.gdl.model.Edge;
import org.s1ck.gdl.model.Graph;
import org.s1ck.gdl.model.Vertex;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GDLDataSource implements DMGraphDataSource {

  private final String gdlString;

  public GDLDataSource(String gdlString) {
    this.gdlString = gdlString;
  }

  @Override
  public void loadWithMinLabelSupport(DMGraphDatabase database, DMGraphFactory graphFactory,
    float minSupportThreshold) throws IOException {
    load(database, graphFactory);
  }

  @Override
  public void load(DMGraphDatabase database, DMGraphFactory graphFactory) {
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
    LabelDictionary dictionary = new LabelDictionary(labelFrequencies);
    database.setVertexDictionary(dictionary);

    // read edges

    labelFrequencies = Lists.newLinkedList();
    for (Edge edge : gdlHandler.getEdges()) {
      for (long graphId : edge.getGraphs()) {
        graphEdges.get(graphId).add(edge);
        labelFrequencies.add(new Countable<>(edge.getLabel()));
      }
    }
    dictionary = new LabelDictionary(labelFrequencies);
    database.setEdgeDictionary(dictionary);

    // write graphs

    for (Graph graph : gdlHandler.getGraphs()) {
      long graphId = graph.getId();
      List<Vertex> vertices = graphVertices.get(graphId);
      Map<Long, Integer> vertexIdMap = Maps.newHashMapWithExpectedSize(vertices.size());
      List<Edge> edges = graphEdges.get(graphId);

      DMGraph dmGraph = graphFactory.create(vertices.size(), edges.size());

      // write vertices

      int vertexId = 0;
      for (Vertex vertex : vertices) {
        vertexIdMap.put(vertex.getId(), vertexId);
        dmGraph.setVertex(
          vertexId,
          database.getVertexDictionary().translate(vertex.getLabel())
        );
        vertexId++;
      }

      // write edges

      int edgeId = 0;
      for (Edge edge : edges) {
        dmGraph.setEdge(
          edgeId,
          vertexIdMap.get(edge.getSourceVertexId()),
          vertexIdMap.get(edge.getTargetVertexId()),
          database.getEdgeDictionary().translate(edge.getLabel())
        );
        edgeId++;
      }
      database.store(dmGraph);
    }
  }
}
