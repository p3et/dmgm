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

import javax.activation.UnsupportedDataTypeException;
import java.math.BigDecimal;
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

    for (org.s1ck.gdl.model.Graph gdlGraph : gdlHandler.getGraphs()) {
      Graph graph = graphFactory.create();
      int graphId = graphCollection.add(graph);

      gdlGraph
        .getProperties()
        .forEach((k, v) -> {
          if (v instanceof String)
            graphCollection.getElementDataStore().setGraph(graphId, k, (String) v);
          else if (v instanceof Integer)
            graphCollection.getElementDataStore().setGraph(graphId, k, (Integer) v);
          else if (v instanceof Long)
            graphCollection.getElementDataStore().setGraph(graphId, k, Math.toIntExact((Long) v));
          else if (v instanceof BigDecimal)
            graphCollection.getElementDataStore().setGraph(graphId, k, (BigDecimal) v);
        });

      long gdlGraphId = gdlGraph.getId();
      int graphLabel = graphCollection.getLabelDictionary().translate(gdlGraph.getLabel());

      List<Vertex> vertices = graphVertices.get(gdlGraphId);
      Map<Long, Integer> vertexIdMap = vertices != null ?
        Maps.newHashMapWithExpectedSize(vertices.size()) :
        Maps.newHashMap();
      List<Edge> edges = graphEdges.get(gdlGraphId);

      graph.setLabel(graphLabel);

      // write vertices

      int vertexId = 0;

      if (vertices != null)
        for (Vertex vertex : vertices) {
          vertexIdMap.put(vertex.getId(), vertexId);
          graph.addVertex(
            graphCollection.getLabelDictionary().translate(vertex.getLabel())
          );

          int finalVertexId = vertexId;
          vertex
            .getProperties()
            .forEach((k, v) -> {
              if (v instanceof String)
                graphCollection.getElementDataStore().setVertex(graphId, finalVertexId, k, (String) v);
              else if (v instanceof Integer)
                graphCollection.getElementDataStore().setVertex(graphId, finalVertexId, k, (Integer) v);
              else if (v instanceof Long)
                graphCollection.getElementDataStore().setVertex(graphId, finalVertexId, k, Math.toIntExact((Long) v));
              else if (v instanceof BigDecimal)
                graphCollection.getElementDataStore().setVertex(graphId, finalVertexId, k, (BigDecimal) v);
            });
          vertexId++;
        }

      // write edges

      if (edges != null)
        for (Edge edge : edges) {
          graph.addEdge(
            vertexIdMap.get(edge.getSourceVertexId()),
            vertexIdMap.get(edge.getTargetVertexId()),
            graphCollection.getLabelDictionary().translate(edge.getLabel())
          );
        }
    }

    return graphCollection;
  }

  public static GDLLoader fromString(String gdlString) {
    return new GDLLoader(gdlString);
  }
}
