package org.biiig.dmgm.impl.db;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.Property;
import org.biiig.dmgm.api.PropertyPredicate;
import org.biiig.dmgm.impl.graph.CachedGraphBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Pragmatic reference implementation of {@code GraphCollectionDatabase}.
 */
public class GraphDBBase implements GraphDB {

  private final AtomicLong nextId = new AtomicLong();

  private final AtomicInteger nextSymbol = new AtomicInteger();
  private final Map<String, Integer> stringIntegerDictionary = createMap();
  private final Map<Integer, String> integerStringDictionary = createMap();

  private final Map<Long, Integer> labels = createMap();
  private final Map<Long, LongPair> edges  = createMap();
  private final Map<Long, LongsPair> elements = createMap();

  private final Map<Integer, Set<Long>> booleanProperties = createMap();
  private final Map<Integer, Map<Long, Long>> longProperties = createMap();
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
  public long createGraph(int label, long[] vertexIds, long[] edgeIds) {
    long id = createElement(label);
    elements.put(id, new LongsPair(vertexIds, edgeIds));
    return id;
  }

  @Override
  public long[] getGraphIdsOfVertex(long vertexId) {
    return getGraphsOfElement(e -> ArrayUtils.contains(e.getValue().getLeft(), vertexId));
  }


  /**
   * Query all graph ids in which an element appears.
   *
   * @param predicate element predicate
   * @return all graph ids
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
  public long[] getGraphIdsOfEdge(long edgeId) {
    return getGraphsOfElement(e -> ArrayUtils.contains(e.getValue().getRight(), edgeId));
  }

  @Override
  public CachedGraph getCachedGraph(long graphId) {
    int graphLabel = labels.get(graphId);

    LongsPair globalVertexIdsEdgeIds = elements.get(graphId);

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

      long globalTargetId = globalSourceTargetPair.getRight();
      int targetId = ArrayUtils.indexOf(globalVertexIds, globalTargetId);
      targetIds[localEdgeId] = targetId;

      edgeLabels[localEdgeId] = labels.get(globalEdgeId);
    }

    return new CachedGraphBase(graphId, graphLabel, vertexLabels, edgeLabels, sourceIds, targetIds);
  }

  @Override
  public List<CachedGraph> getCachedCollection(long collectionId) {
    return LongStream
      .of(elements.get(collectionId).getLeft())
      .mapToObj(this::getCachedGraph)
      .collect(Collectors.toList());
  }

  @Override
  public long[] getAllHyperVertexIds() {
    return elements
      .keySet()
      .stream()
      .mapToLong(id -> id)
      .toArray();
  }

  @Override
  public long[] getAllGraphIds() {
    return elements
      .entrySet()
      .stream()
      .filter(e -> e.getValue().getRight().length != 0)
      .mapToLong(Map.Entry::getKey)
      .toArray();
  }

  @Override
  public long[] getAllCollectionIds() {
    return elements
      .entrySet()
      .stream()
      .filter(e -> e.getValue().getRight().length == 0)
      .mapToLong(Map.Entry::getKey)
      .toArray();
  }

  @Override
  public long[] getAllVertexIds() {
    return elements
      .values()
      .stream()
      .map(LongsPair::getLeft)
      .flatMapToLong(LongStream::of)
      .toArray();
  }

  @Override
  public long[] getAllEdgeIds() {
    return elements
      .values()
      .stream()
      .map(LongsPair::getRight)
      .flatMapToLong(LongStream::of)
      .toArray();
  }


  @Override
  public LongsPair getGraphElementIds(long graphId) {
    return elements.get(graphId);
  }

  @Override
  public int getLabel(long id) {
    return labels.get(id);
  }

  @Override
  public long[] getElementsByLabel(IntPredicate predicate) {
    return labels
      .entrySet()
      .stream()
      .filter(e -> predicate.test(e.getValue()))
      .mapToLong(Map.Entry::getKey)
      .toArray();
  }

  @Override
  public long[] getElementsByProperties(PropertyPredicate predicate) {
    return labels
      .keySet()
      .stream()
      .filter(id -> predicate.apply(this, id))
      .mapToLong(id -> id)
      .toArray();
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
  private Map<Long, Long> getLongValues(int key) {
    return getProperties(key, this.longProperties);
  }

  @Override
  public void set(long id, int key, long value) {
    getLongValues(key).put(id, value);
  }

  @Override
  public long getLong(long id, int key) {
    return getLongValues(key).get(id);
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

  @Override
  public Property[] getProperties(long id) {
    List<Property> properties = booleanProperties
      .entrySet()
      .parallelStream()
      .filter(e -> e.getValue().contains(id))
      .map(e -> new Property(e.getKey(), true))
      .collect(Collectors.toList());

    properties.addAll(getProperties(longProperties, id));
    properties.addAll(getProperties(doubleProperties, id));
    properties.addAll(getProperties(stringProperties, id));
    properties.addAll(getProperties(bigDecimalProperties, id));
    properties.addAll(getProperties(localDateProperties, id));
    properties.addAll(getProperties(intsProperties, id));
    properties.addAll(getProperties(stringsProperties, id));

    return properties
      .toArray(new Property[properties.size()]);
  }

  private <T> List<Property> getProperties(Map<Integer, Map<Long, T>> intProperties, long id) {
    return intProperties
      .entrySet()
      .parallelStream()
      .flatMap(e -> e.getValue()
        .entrySet()
        .stream()
        .filter(f -> f.getKey() == id)
        .map(f -> new Property(e.getKey(), f.getValue())))
      .collect(Collectors.toList());
  }

  @Override
  public Map<Long, Property[]> getAllProperties() {

    List<Pair<Long, Property>> properties = booleanProperties
      .entrySet()
      .parallelStream()
      .flatMap(e -> e.getValue()
        .stream()
        .map(id -> new Pair<>(id, new Property(e.getKey(), true))))
      .collect(Collectors.toList());

    properties.addAll(getProperties(longProperties));
    properties.addAll(getProperties(doubleProperties));
    properties.addAll(getProperties(stringProperties));
    properties.addAll(getProperties(bigDecimalProperties));
    properties.addAll(getProperties(localDateProperties));
    properties.addAll(getProperties(intsProperties));
    properties.addAll(getProperties(stringsProperties));

    return properties
      .parallelStream()
      .collect(new GroupByKeyArrayValues<>(Pair::getKey, Pair::getValue, Property.class));
  }

  private <T> List<Pair<Long, Property>> getProperties(Map<Integer, Map<Long, T>> intProperties) {
    return intProperties
      .entrySet()
      .parallelStream()
      .flatMap(e -> e.getValue()
        .entrySet()
        .stream()
        .map(f -> new Pair<>(f.getKey(), new Property(e.getKey(), f.getValue()))))
      .collect(Collectors.toList());
  }

  @Override
  public String toString() {
    return 
      "nid=" + nextId +
      "\nnsy=" + nextSymbol +
      "\nsid=" + stringIntegerDictionary +
      "\nisd=" + integerStringDictionary +
      "\nlbs=" + labels +
      "\negs=" + edges +
      "\nels=" + elements +
      "\nbol=" + booleanProperties +
      "\nint=" + longProperties +
      "\ndob=" + doubleProperties +
      "\nstr=" + stringProperties +
      "\nbdc=" + bigDecimalProperties +
      "\nlod=" + localDateProperties +
      "\nina=" + intsProperties +
      "\nsta=" + stringsProperties;
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
