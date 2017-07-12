package org.biiig.dmgspan.model;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class Graph {
  private final int id;
  private Vertex[] vertices = new Vertex[0];
  private Edge[] edges = new Edge[0];
  private AdjacencyListEntry[][] adjacencyList;

  public Graph(int id) {
    this.id = id;
  }

  public void add(Edge edge) {
    this.edges = ArrayUtils.add(edges, edge);
  }

  public void add(Vertex vertex) {
    int id = vertex.getId();

    if (vertices.length <= id) {
      Vertex[] newArray = new Vertex[id + 1];
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
      StringUtils.join(toString(adjacencyList), ",") +
      ">";
  }

  private String[] toString(AdjacencyListEntry[][] adjacencyList) {
    String[] entryStrings = new String[adjacencyList.length];

    int i = 0;
    for (AdjacencyListEntry[] row : adjacencyList) {
      entryStrings[i] = StringUtils.join(row, ",");
    }

    return entryStrings;
  }

  public Edge[] getEdges() {
    return edges;
  }

  public Vertex[] getVertices() {
    return vertices;
  }

  public int getId() {
    return id;
  }

  public void createAdjacencyList() {
    this.adjacencyList = new AdjacencyListEntry[vertices.length][];

    int[] vertexIdMap = new int[vertices.length];

    Arrays.sort(vertices, Vertex::compareTo);

    int newId = 0;
    for (Vertex vertex : vertices) {
      this.adjacencyList[newId] = new AdjacencyListEntry[0];
      vertexIdMap[vertex.getId()] = newId;
      vertex.setId(newId);

      newId++;
    }


    for (int id = 0; id < edges.length; id++) {
      Edge edge = edges[id];

      int source = vertexIdMap[edge.getSource()];
      int target = vertexIdMap[edge.getTarget()];

      edge.setSource(source);
      edge.setTarget(target);

      boolean loop = edge.isLoop();

      AdjacencyListEntry sourceEntry = new AdjacencyListEntry(
        loop, true, id, edge.getLabel(), target, vertices[target].getLabel());

      adjacencyList[source] = ArrayUtils.add(adjacencyList[source], sourceEntry);

      AdjacencyListEntry targetEntry = new AdjacencyListEntry(
        loop, false, id, edge.getLabel(), source, vertices[source].getLabel());

      adjacencyList[target] = ArrayUtils.add(adjacencyList[target], targetEntry);
    }

    for (AdjacencyListEntry[] row : adjacencyList) {
      Arrays.sort(row, AdjacencyListEntry::compareTo);
    }
  }

  public AdjacencyListEntry[][] getAjacencyList() {
    return adjacencyList;
  }
}
