package org.biiig.dmgm.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ElementDataStore {
  boolean isGraph(int graphId, String key);
  boolean isVertex(int graphId, int vertexId, String key);
  boolean isEdge(int graphId, int edgeId, String key);

  void setGraph(int graphId, String key, boolean value);
  void setVertex(int graphId, int vertexId, String key, boolean value);
  void setEdge(int graphId, int edgeId, String key, boolean value);

  Optional<Integer> getGraphInteger(int graphId, String key);
  Optional<Integer> getVertexInteger(int graphId, int vertexId, String key);
  Optional<Integer> getEdgeInteger(int graphId, int edgeId, String key);

  Optional<int[]> getGraphIntegers(int graphId, String key);
  Optional<int[]> getVertexIntegers(int graphId, int vertexId, String key);
  Optional<int[]> getEdgeIntegers(int graphId, int edgeId, String key);

  void setGraph(int graphId, String key, int value);
  void setVertex(int graphId, int vertexId, String key, int value);
  void setEdge(int graphId, int edgeId, String key, int value);

  void setGraph(int graphId, String key, int[] values);
  void setVertex(int graphId, int vertexId, String key, int[] values);
  void setEdge(int graphId, int edgeId, String key, int[] values);

  void addGraph(int graphId, String key, int value);
  void addVertex(int graphId, int vertexId, String key, int value);
  void addEdge(int graphId, int edgeId, String key, int value);

  Optional<String> getGraphString(int graphId, String key);
  Optional<String> getVertexString(int graphId, int vertexId, String key);
  Optional<String> getEdgeString(int graphId, int edgeId, String key);

  Optional<String[]> getGraphStrings(int graphId, String key);
  Optional<String[]> getVertexStrings(int graphId, int vertexId, String key);
  Optional<String[]> getEdgeStrings(int graphId, int edgeId, String key);

  void setGraph(int graphId, String key, String[] values);
  void setVertex(int graphId, int vertexId, String key, String[] values);
  void setEdge(int graphId, int edgeId, String key, String[] values);

  void addGraph(int graphId, String key, String value);
  void addVertex(int graphId, int vertexId, String key, String value);
  void addEdge(int graphId, int edgeId, String key, String value);

  void setGraph(int graphId, String key, String value);
  void setVertexString(int graphId, int vertexId, String key, String value);
  void setEdgeString(int graphId, int edgeId, String key, String value);

  Optional<BigDecimal> getGraphBigDecimal(int graphId, String key);
  Optional<BigDecimal> getVertexBigDecimal(int graphId, int vertexId, String key);
  Optional<BigDecimal> getEdgeBigDecimal(int graphId, int edgeId, String key);

  void setGraph(int graphId, String key, BigDecimal value);
  void setVertex(int graphId, int vertexId, String key, BigDecimal value);
  void setEdge(int graphId, int edgeId, String key, BigDecimal value);

  Optional<LocalDate> getGraphLocalDate(int graphId, String key);
  Optional<LocalDate> getVertexLocalDate(int graphId, int vertexId, String key);
  Optional<LocalDate> getEdgeLocalDate(int graphId, int edgeId, String key);

  void setGraph(int graphId, String key, LocalDate value);
  void setVertex(int graphId, int vertexId, String key, LocalDate value);
  void setEdge(int graphId, int edgeId, String key, LocalDate value);
}
