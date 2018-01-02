package org.biiig.dmgm.todo.model.labeled_graph;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Arrays;

public class LabeledGraph {
  private final int id;
  private LabeledVertex[] vertices;
  private LabeledEdge[] edges;
  private LabeledAdjacencyListEntry[][] adjacencyList;

  public LabeledGraph(int id) {
    this.id = id;
  }

  public LabeledGraph(DFSCode dfsCode) {
    this.id = 0;

    for (int edgeId = 0; edgeId < dfsCode.getEdgeCount(); edgeId++) {

      int fromTime = dfsCode.getFromTime(edgeId);
      int toTime = dfsCode.getToTime(edgeId);
      int fromLabel = dfsCode.getVertexLabel(fromTime);
      boolean outgoing = dfsCode.isOutgoing(edgeId);
      int edgeLabel = dfsCode.getEdgeLabel(edgeId);
      int toLabel = dfsCode.getVertexLabel(toTime);

      if (vertices == null) {
        vertices = new LabeledVertex[] {new LabeledVertex(fromTime, fromLabel)};
      }

      if (toTime > fromTime) {
        vertices = ArrayUtils.add(vertices, new LabeledVertex(toTime, toLabel));
      }

      int sourceId;
      int targetId;
      if (outgoing) {
        sourceId = fromTime;
        targetId = toTime;
      } else {
        sourceId = toTime;
        targetId = fromTime;
      }
      LabeledEdge edge = new LabeledEdge(edgeId, sourceId, edgeLabel, targetId);

      if (edges == null) {
        edges = new LabeledEdge[] {edge};
      } else {
        edges = ArrayUtils.add(edges, edge);
      }
    }
  }

  public void add(LabeledEdge edge) {
    this.edges = ArrayUtils.add(edges, edge);
  }

  public void add(LabeledVertex vertex) {
    int id = vertex.getId();

    if (vertices == null) {
      vertices = new LabeledVertex[] {vertex};
    } else if (vertices.length <= id) {
      LabeledVertex[] newArray = new LabeledVertex[id + 1];
      System.arraycopy(vertices, 0, newArray, 0, vertices.length);
      this.vertices = newArray;
    }

    this.vertices[id] = vertex;
  }

  @Override
  public String toString() {
    return id + "=<" +
      StringUtils.join(vertices, ",") +
      "\t" +
//      StringUtils.join(toString(adjacencyList), ",") +
      StringUtils.join(edges, ",") +

      ">";
  }

  private String[] toString(LabeledAdjacencyListEntry[][] adjacencyList) {
    String[] entryStrings = new String[adjacencyList.length];

    int i = 0;
    for (LabeledAdjacencyListEntry[] row : adjacencyList) {
      entryStrings[i] = StringUtils.join(row, ",");
    }

    return entryStrings;
  }

  public LabeledEdge[] getEdges() {
    return edges;
  }

  public LabeledVertex[] getVertices() {
    return vertices;
  }

  public int getId() {
    return id;
  }

  public void createAdjacencyList() {
    this.adjacencyList = new LabeledAdjacencyListEntry[vertices.length][];

    int[] vertexIdMap = new int[vertices.length];

    Arrays.sort(vertices, LabeledVertex::compareTo);

    int newId = 0;
    for (LabeledVertex vertex : vertices) {
      this.adjacencyList[newId] = new LabeledAdjacencyListEntry[0];
      vertexIdMap[vertex.getId()] = newId;
      vertex.setId(newId);

      newId++;
    }


    for (int id = 0; id < edges.length; id++) {
      LabeledEdge edge = edges[id];

      int source = vertexIdMap[edge.getSource()];
      int target = vertexIdMap[edge.getTarget()];

      edge.setSource(source);
      edge.setTarget(target);

      boolean loop = edge.isLoop();

      LabeledAdjacencyListEntry sourceEntry = new LabeledAdjacencyListEntry(
        loop, true, id, edge.getLabel(), target, vertices[target].getLabel());

      adjacencyList[source] = ArrayUtils.add(adjacencyList[source], sourceEntry);

      if (!loop) {

        LabeledAdjacencyListEntry targetEntry = new LabeledAdjacencyListEntry(
          loop, false, id, edge.getLabel(), source, vertices[source].getLabel());

        adjacencyList[target] = ArrayUtils.add(adjacencyList[target], targetEntry);
      }
    }

    for (LabeledAdjacencyListEntry[] row : adjacencyList) {
      Arrays.sort(row, LabeledAdjacencyListEntry::compareTo);
    }
  }

  public LabeledAdjacencyListEntry[][] getAdjacencyList() {
    return adjacencyList;
  }

  public int getEdgeCount() {
    return edges == null ? 0 : edges.length;
  }

  public int getVertexCount() {
    return vertices == null ? 0 : vertices.length;
  }
}
