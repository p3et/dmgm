package org.biiig.dmgm.impl.db;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph.SmallGraphBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Pragmatic reference implementation of {@code GraphCollectionDatabase}.
 */
public class HyperVertexDBBase implements HyperVertexDB {

  private final AtomicLong nextId = new AtomicLong();

  private final AtomicInteger nextSymbol = new AtomicInteger();
  private final Map<String, Integer> stringIntegerDictionary = createMap();
  private final Map<Integer, String> integerStringDictionary = createMap();

  private final Map<Long, Integer> labels = createMap();
  private final Map<Long, LongPair> edges  = createMap();
  private final Map<Long, LongsPair> elements = createMap();

  private final Map<Integer, Set<Long>> booleanProperties = createMap();
  private final Map<Integer, Map<Long, Integer>> intProperties = createMap();
  private final Map<Integer, Map<Long, Double>> doubleProperties = createMap();
  private final Map<Integer, Map<Long, String>> stringProperties = createMap();
  private final Map<Integer, Map<Long, BigDecimal>> bigDecimalProperties = createMap();
  private final Map<Integer, Map<Long, LocalDate>> localDateProperties = createMap();
  private final Map<Integer, Map<Long, int[]>> intsProperties = createMap();
  private final Map<Integer, Map<Long, String[]>> stringsProperties = createMap();

  @Override
  public int encode(String value) {
    return stringIntegerDictionary.computeIfAbsent(value, k -> {
      int integer = nextSymbol.getAndIncrement();
      integerStringDictionary.put(integer, value);
      return integer;
    });
  }

  @Override
  public String decode(int integer) {
    return integerStringDictionary.get(integer);
  }

  /**
   * Associates an id and stores the label of a new graph, vertex or edge
   *
   * @param label label
   * @return id
   */
  private long createElement(int label) {
    long id = nextId.getAndIncrement();
    labels.put(id, label);
    return id;
  }


  @Override
  public long createVertex(int label) {
    return createElement(label);
  }


  @Override
  public long createEdge(int label, long sourceId, long targetId) {
    long id = createElement(label);
    edges.put(id, new LongPair(sourceId, targetId));
    return id;
  }

  @Override
  public long createHyperVertex(int label, long[] vertexIds, long[] edgeIds) {
    long id = createElement(label);
    elements.put(id, new LongsPair(vertexIds, edgeIds));
    return id;
  }

  @Override
  public long[] getGraphsOfVertex(long id) {
    return getGraphsOfElement(e -> ArrayUtils.contains(e.getValue().getLeft(), id));
  }

  /**
   *
   *
   * @param predicate
   * @return
   */
  public long[] getGraphsOfElement(Predicate<Map.Entry<Long, LongsPair>> predicate) {
    return elements
      .entrySet()
      .stream()
      .filter(predicate)
      .mapToLong(Map.Entry::getKey)
      .toArray();
  }

  @Override
  public long[] getGraphsOfEdge(long id) {
    return getGraphsOfElement(e -> ArrayUtils.contains(e.getValue().getRight(), id));
  }

  @Override
  public SmallGraph getSmallGraph(long hyperVertexId) {
    int graphLabel = labels.get(hyperVertexId);

    LongsPair globalVertexIdsEdgeIds = elements.get(hyperVertexId);

    long[] globalVertexIds = globalVertexIdsEdgeIds.getLeft();

    int[] vertexLabels = LongStream
      .of(globalVertexIds)
      .mapToInt(labels::get)
      .toArray();

    long[] globalEdgeIds = globalVertexIdsEdgeIds.getRight();

    int edgeCount = globalEdgeIds.length;
    int[] edgeLabels = new int[edgeCount];
    int[] sourceIds = new int[edgeCount];
    int[] targetIds = new int[edgeCount];

    for (int localEdgeId = 0; localEdgeId < edgeCount; localEdgeId++) {
      Long globalEdgeId = globalEdgeIds[localEdgeId];
      LongPair globalSourceTargetPair = edges.get(globalEdgeId);

      long globalSourceId = globalSourceTargetPair.getLeft();
      int sourceId = ArrayUtils.indexOf(globalVertexIds, globalSourceId);
      sourceIds[localEdgeId] = sourceId;

      long globalTargetId = globalSourceTargetPair.getLeft();
      int targetId = ArrayUtils.indexOf(globalVertexIds, globalTargetId);
      targetIds[localEdgeId] = targetId;
    }

    return new SmallGraphBase(hyperVertexId, graphLabel, vertexLabels, edgeLabels, sourceIds, targetIds);
  }

  @Override
  public LongsPair getElementsOf(long hyperVertexId) {
    return elements.get(hyperVertexId);
  }

  @Override
  public int getLabel(long id) {
    return labels.get(id);
  }

  @Override
  public List<SmallGraph> getCollection(Long collectionId) {
    return LongStream
      .of(elements.get(collectionId).getLeft())
      .mapToObj(this::getSmallGraph)
      .collect(Collectors.toList());
  }

  @Override
  public long createHyperVertex(SmallGraph graph) {
    long[] vertexIds = graph
      .vertexIdStream()
      .map(graph::getVertexLabel)
      .mapToLong(this::createVertex)
      .toArray();

    long[] edgeIds = graph
      .edgeIdStream()
      .mapToLong(edgeId -> {
        int label = graph.getEdgeLabel(edgeId);
        long sourceId = vertexIds[graph.getSourceId(edgeId)];
        long targetId = vertexIds[graph.getTargetId(edgeId)];
        return createEdge(label, sourceId, targetId);
      })
      .toArray();

    return createHyperVertex(graph.getLabel(), vertexIds, edgeIds);
  }

  @Override
  public long createCollectionByLabel(int graphLabel, int collectionLabel) {
    long[] vertexIds = elements
      .keySet()
      .stream()
      .filter(g -> labels.get(g) == graphLabel)
      .mapToLong(g -> g)
      .toArray();

    return createHyperVertex(collectionLabel, vertexIds, new long[0]);
  }


  /**
   * Get boolean values by key.
   *
   * @param key property key
   * @return Set of ids whose property of this key is set to true
   */
  private Set<Long> getBooleanValues(int key) {
    return booleanProperties.computeIfAbsent(key, k -> Sets.newConcurrentHashSet());
  }

  @Override
  public void set(long id, int key, boolean value) {
    Set<Long> booleanValues = getBooleanValues(key);
    if(value)
      booleanValues.add(id);
    else
      booleanValues.remove(id);
  }

  @Override
  public boolean is(long id, int key) {
    return getBooleanValues(key).contains(id);
  }

  /**
   * Get values by key and compute if absent
   *
   * @param key property key
   * @param properties property map
   * @param <T> value type
   *
   * @return id-value map of property key
   */
  private <T> Map<Long, T> getProperties(int key, Map<Integer, Map<Long, T>> properties) {
    return properties.computeIfAbsent(key, k -> createMap());
  }

  /**
   * Get int values by key.
   *
   * @param key property key
   * @return map of set int values
   */
  private Map<Long, Integer> getIntValues(int key) {
    return getProperties(key, this.intProperties);
  }

  @Override
  public void set(long id, int key, int value) {
    getIntValues(key).put(id, value);
  }

  @Override
  public int getInt(long id, int key) {
    return getIntValues(key).get(id);
  }

  /**
   * Get double values by key.
   *
   * @param key property key
   * @return map of set double values
   */
  private Map<Long, Double> getDoubleValues(int key) {
    return getProperties(key, doubleProperties);
  }

  @Override
  public void set(long id, int key, double value) {
    getDoubleValues(key).put(id, value);
  }

  @Override
  public double getDouble(long id, int key) {
    return getDoubleValues(key).get(id);
  }

  /**
   * Get String values by key.
   *
   * @param key property key
   * @return map of set String values
   */
  private Map<Long, String> getStringValues(int key) {
    return getProperties(key, stringProperties);
  }

  @Override
  public void set(long id, int key, String value) {
    getStringValues(key).put(id, value);
  }

  @Override
  public String getString(long id, int key) {
    return getStringValues(key).get(id);
  }


  /**
   * Get BigDecimal values by key.
   *
   * @param key property key
   * @return map of set BigDecimal values
   */
  private Map<Long, BigDecimal> getBigDecimalValues(int key) {
    return getProperties(key, bigDecimalProperties);
  }

  @Override
  public void set(long id, int key, BigDecimal value) {
    getBigDecimalValues(key).put(id, value);
  }

  @Override
  public BigDecimal getBigDecimal(long id, int key) {
    return getBigDecimalValues(key).get(id);
  }

  /**
   * Get LocalDate values by key.
   *
   * @param key property key
   * @return map of set LocalDate values
   */
  private Map<Long, LocalDate> getLocalDateValues(int key) {
    return getProperties(key, localDateProperties);
  }

  @Override
  public void set(long id, int key, LocalDate value) {
    getLocalDateValues(key).put(id, value);
  }

  @Override
  public LocalDate getLocalDate(long id, int key) {
    return getLocalDateValues(key).get(id);
  }

  /**
   * Get int[] values by key.
   *
   * @param key property key
   * @return map of set int[] values
   */
  private Map<Long, int[]> getIntsValues(int key) {
    return getProperties(key, intsProperties);
  }

  @Override
  public void set(long id, int key, int[] value) {
    getIntsValues(key).put(id, value);
  }


  @Override
  public void add(long id, int key, int value) {
    getIntsValues(key)
      .compute(id, (k, values) -> values == null ? new int[] {value} : ArrayUtils.add(values, value));
  }


  @Override
  public int[] getInts(long id, int key) {
    return getIntsValues(key).get(id);
  }

  /**
   * Get String[] values by key.
   *
   * @param key property key
   * @return map of set String[] values
   */
  private Map<Long, String[]> getStringsValues(int key) {
    return getProperties(key, stringsProperties);
  }

  @Override
  public void set(long id, int key, String[] value) {
    getStringsValues(key).put(id, value);
  }


  @Override
  public void add(long id, int key, String value) {
    getStringsValues(key)
      .compute(id, (k, values) -> values == null ? new String[] {value} : ArrayUtils.add(values, value));
  }

  @Override
  public String[] getStrings(long id, int key) {
    return getStringsValues(key).get(id);
  }

  /**
   * Returns a new map.
   * 
   * @param <K> key type
   * @param <V> key type
   * @return map
   */
  private static <K, V>  Map<K, V> createMap() {
    return Maps.newConcurrentMap();
  }

}
