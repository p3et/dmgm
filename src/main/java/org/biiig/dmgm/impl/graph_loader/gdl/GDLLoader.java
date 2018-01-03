package org.biiig.dmgm.impl.graph_loader.gdl;

import com.google.common.collect.Maps;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;
import org.biiig.dmgm.impl.graph_loader.tlf.GraphCollectionLoaderBase;
import org.s1ck.gdl.GDLHandler;
import org.s1ck.gdl.model.Edge;
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
    GraphCollection graphCollection = new InMemoryGraphCollectionBuilderFactory().create().create();

    GDLHandler gdlHandler = new GDLHandler.Builder().buildFromString(gdlString);

    // read graphs
    Map<Long, List<Vertex>> graphVertices = gdlHandler
      .getVertices()
      .parallelStream()
      .flatMap(v -> v.getGraphs()
        .stream()
        .map(g -> new Pair<>(g, v))
      )
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));

    Map<Long, List<Edge>> graphEdges = gdlHandler
      .getEdges()
      .parallelStream()
      .flatMap(e -> e.getGraphs()
        .stream()
        .map(g -> new Pair<>(g, e))
      )
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));

    for (org.s1ck.gdl.model.Graph graph : gdlHandler.getGraphs()) {
      long graphId = graph.getId();
      int graphLabel = graphCollection.getLabelDictionary().translate(graph.getLabel());

      List<Vertex> vertices = graphVertices.get(graphId);
      Map<Long, Integer> vertexIdMap = Maps.newHashMapWithExpectedSize(vertices.size());
      List<Edge> edges = graphEdges.get(graphId);

      Graph dmGraph = graphFactory.create();
      dmGraph.setLabel(graphLabel);

      // write vertices

      int vertexId = 0;
      for (Vertex vertex : vertices) {
        vertexIdMap.put(vertex.getId(), vertexId);
        dmGraph.addVertex(
          graphCollection.getLabelDictionary().translate(vertex.getLabel())
        );
        vertexId++;
      }

      // write edges

      for (Edge edge : edges) {
        dmGraph.addEdge(
          vertexIdMap.get(edge.getSourceVertexId()),
          vertexIdMap.get(edge.getTargetVertexId()),
          graphCollection.getLabelDictionary().translate(edge.getLabel())
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
