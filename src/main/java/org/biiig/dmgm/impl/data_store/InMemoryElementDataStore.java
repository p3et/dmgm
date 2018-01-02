package org.biiig.dmgm.impl.data_store;

import de.jesemann.paralleasy.property_store.PropertyStore;
import org.biiig.dmgm.api.ElementDataStore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class InMemoryElementDataStore implements ElementDataStore {

  private final PropertyStore<String, Integer> graphStore = new PropertyStore<>();
  private final PropertyStore<String, Long> vertexStore = new PropertyStore<>();
  private final PropertyStore<String, Long> edgeStore = new PropertyStore<>();

  private long getGlobalId(long graphId, int elementId) {
    return (graphId << 32) | (elementId & 0xffffffffL);
  }

  @Override
  public boolean isGraph(int graphId, String key) {
    return graphStore.is(graphId, key);
  }

  @Override
  public boolean isVertex(int graphId, int vertexId, String key) {
    return vertexStore.is(getGlobalId(graphId, vertexId), key);
  }

  @Override
  public boolean isEdge(int graphId, int edgeId, String key) {
    return edgeStore.is(getGlobalId(graphId, edgeId), key);
  }

  @Override
  public void setGraph(int graphId, String key, boolean value) {
    graphStore.set(graphId, key, value);
  }

  @Override
  public void setVertex(int graphId, int vertexId, String key, boolean value) {
    vertexStore.set(getGlobalId(graphId, vertexId), key, value);
  }

  @Override
  public void setEdge(int graphId, int edgeId, String key, boolean value) {
    edgeStore.set(getGlobalId(graphId, edgeId), key, value);
  }

  @Override
  public Optional<String> getGraphString(int graphId, String key) {
    return graphStore.getString(graphId, key);
  }

  @Override
  public Optional<String> getVertexString(int graphId, int vertexId, String key) {
    return vertexStore.getString(getGlobalId(graphId, vertexId), key);
  }

  @Override
  public Optional<String> getEdgeString(int graphId, int edgeId, String key) {
    return edgeStore.getString(getGlobalId(graphId, edgeId), key);
  }

  @Override
  public void setGraph(int graphId, String key, String value) {
    graphStore.set(graphId, key, value);
  }

  @Override
  public void setVertexString(int graphId, int vertexId, String key, String value) {
    vertexStore.set(getGlobalId(graphId, vertexId), key, value);
  }

  @Override
  public void setEdgeString(int graphId, int edgeId, String key, String value) {
    edgeStore.set(getGlobalId(graphId, edgeId), key, value);
  }

  @Override
  public Optional<Integer> getGraphInteger(int graphId, String key) {
    return graphStore.getInteger(graphId, key);
  }

  @Override
  public Optional<Integer> getVertexInteger(int graphId, int vertexId, String key) {
    return vertexStore.getInteger(getGlobalId(graphId, vertexId), key);
  }

  @Override
  public Optional<Integer> getEdgeInteger(int graphId, int edgeId, String key) {
    return edgeStore.getInteger(getGlobalId(graphId, edgeId), key);
  }

  @Override
  public void setGraph(int graphId, String key, int value) {
    graphStore.set(graphId, key, value);
  }

  @Override
  public void setVertex(int graphId, int vertexId, String key, int value) {
    vertexStore.set(getGlobalId(graphId, vertexId), key, value);
  }

  @Override
  public void setEdge(int graphId, int edgeId, String key, int value) {
    edgeStore.set(getGlobalId(graphId, edgeId), key, value);
  }

  @Override
  public Optional<BigDecimal> getGraphBigDecimal(int graphId, String key) {
    return graphStore.getDecimal(graphId, key);
  }

  @Override
  public Optional<BigDecimal> getVertexBigDecimal(int graphId, int vertexId, String key) {
    return vertexStore.getDecimal(getGlobalId(graphId, vertexId), key);
  }

  @Override
  public Optional<BigDecimal> getEdgeBigDecimal(int graphId, int edgeId, String key) {
    return edgeStore.getDecimal(getGlobalId(graphId, edgeId), key);
  }

  @Override
  public void setGraph(int graphId, String key, BigDecimal value) {
    graphStore.set(graphId, key, value);
  }

  @Override
  public void setVertex(int graphId, int vertexId, String key, BigDecimal value) {
    vertexStore.set(getGlobalId(graphId, vertexId), key, value);
  }

  @Override
  public void setEdge(int graphId, int edgeId, String key, BigDecimal value) {
    edgeStore.set(getGlobalId(graphId, edgeId), key, value);
  }

  @Override
  public Optional<LocalDate> getGraphLocalDate(int graphId, String key) {
    return graphStore.getDate(graphId, key);
  }

  @Override
  public Optional<LocalDate> getVertexLocalDate(int graphId, int vertexId, String key) {
    return vertexStore.getDate(getGlobalId(graphId, vertexId), key);
  }

  @Override
  public Optional<LocalDate> getEdgeLocalDate(int graphId, int edgeId, String key) {
    return edgeStore.getDate(getGlobalId(graphId, edgeId), key);
  }

  @Override
  public void setGraph(int graphId, String key, LocalDate value) {
    graphStore.set(graphId, key, value);
  }

  @Override
  public void setVertex(int graphId, int vertexId, String key, LocalDate value) {
    vertexStore.set(getGlobalId(graphId, vertexId), key, value);
  }

  @Override
  public void setEdge(int graphId, int edgeId, String key, LocalDate value) {
    edgeStore.set(getGlobalId(graphId, edgeId), key, value);
  }

}
