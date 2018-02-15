/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.db;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.jesemann.paralleasy.collectors.GroupByKeyArrayValues;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import javafx.util.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.db.Property;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.CachedGraph;
import org.biiig.dmgm.impl.model.CachedGraphBase;


/**
 * Pragmatic reference implementation of {@code PropertyGraphDB}.
 */
public class InMemoryGraphDb implements PropertyGraphDb {

  /**
   * Parallel read mode.
   * true <=> enabled.
   */
  private final boolean parallelRead;

  // SymbolDictionary

  /**
   * Symbol that will be mapped to the next new string value that shall be encoded.
   */
  private final AtomicInteger nextSymbol = new AtomicInteger();
  /**
   * dictionary coding: string -> integer.
   */
  private final Map<String, Integer> stringIntegerDictionary = createMap();
  /**
   * dictionary coding: integer -> symbol.
   */
  private final Map<Integer, String> integerStringDictionary = createMap();


  // CreateElements & GetElements

  /**
   * Identifier that will be mapped to the next created element.
   */
  private final AtomicLong nextId = new AtomicLong();
  /**
   * Stores labels of elements.
   * element id -> encoded label
   */
  private final Map<Long, Integer> labels = createMap();
  /**
   * Stores edge elements.
   * element id -> (sourceId, targetId)
   */
  private final Map<Long, LongPair> edges  = createMap();
  /**
   * Stores model elements.
   * element id -> (vertexId..., edgeId...)
   */
  private final Map<Long, LongsPair> graphs = createMap();
  /**
   * Stores model collections.
   * element id -> (graphId...)
   */
  private final Map<Long, long[]> collections = createMap();


  // SetProperties & GetProperties

  /**
   * Stores boolean properties.
   * property key -> element id...
   * element.property == true <=> map.get(property).contains(element)
   */
  private final Map<Integer, Set<Long>> booleanProperties = createMap();
  /**
   * Stores long properties.
   * property key -> element id -> property value
   */
  private final Map<Integer, Map<Long, Long>> longProperties = createMap();
  /**
   * Stores double properties.
   * property key -> element id -> property value
   */
  private final Map<Integer, Map<Long, Double>> doubleProperties = createMap();
  /**
   * Stores long properties.
   * property string -> element id -> property value
   */
  private final Map<Integer, Map<Long, String>> stringProperties = createMap();
  /**
   * Stores decimal properties.
   * property key -> element id -> property value
   */
  private final Map<Integer, Map<Long, BigDecimal>> bigDecimalProperties = createMap();
  /**
   * Stores date properties.
   * property key -> element id -> property value
   */
  private final Map<Integer, Map<Long, LocalDate>> localDateProperties = createMap();
  /**
   * Stores multi-value integer properties.
   * property key -> element id -> property value...
   */
  private final Map<Integer, Map<Long, int[]>> intsProperties = createMap();
  /**
   * Stores multi-value string properties.
   * property key -> element id -> property value...
   */
  private final Map<Integer, Map<Long, String[]>> stringsProperties = createMap();

  /**
   * Constructor.
   *
   * @param parallelRead true <=> enabled
   */
  public InMemoryGraphDb(boolean parallelRead) {
    this.parallelRead = parallelRead;
  }

  // SymbolDictionary

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

  // CreateElements

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
    graphs.put(id, new LongsPair(vertexIds, edgeIds));
    return id;
  }

  @Override
  public long createCollection(int label, long[] graphIds) {
    long id = createElement(label);
    collections.put(id, graphIds);
    return id;
  }

  /**
   * Associates an id and stores the label of a new model, vertex or edge.
   *
   * @param label label
   * @return id
   */
  private long createElement(int label) {
    long id = nextId.getAndIncrement();
    labels.put(id, label);
    return id;
  }

  // GetElements


  @Override
  public int getLabel(long id) {
    return labels.get(id);
  }

  @Override
  public LongPair getSourceIdTargetId(long edgeId) {
    return edges.get(edgeId);
  }

  @Override
  public LongsPair getVertexIdsEdgeIds(long graphId) {
    return graphs.get(graphId);
  }

  @Override
  public long[] getGraphIds(Long collectionId) {
    return collections.get(collectionId);
  }

  @Override
  public long[] getGraphIds() {
    return getParallelizableEntryStream(graphs)
        .mapToLong(Map.Entry::getKey)
        .toArray();
  }

  @Override
  public long[] getGraphIdsOfVertex(long vertexId) {
    return getGraphsOfElement(vertexId, LongsPair::getVertexIds);
  }


  @Override
  public long[] getGraphIdsOfEdge(long edgeId) {
    return getGraphsOfElement(edgeId, LongsPair::getEdgeIds);
  }

  /**
   * Query all model ids in which an element appears.
   *
   * @param id vertex or edge id
   * @param getter getter for vertex or edge ids
   * @return model ids
   */
  private long[] getGraphsOfElement(long id, Function<LongsPair, long[]> getter) {
    return getParallelizableEntryStream(graphs)
      .filter(e -> ArrayUtils.contains(getter.apply(e.getValue()), id))
      .mapToLong(Map.Entry::getKey)
      .toArray();
  }

  @Override
  public long[] getVertexIds() {
    return getParallelizableValueStream(graphs)
      .map(LongsPair::getVertexIds)
      .flatMapToLong(LongStream::of)
      .toArray();
  }

  @Override
  public long[] getEdgeIds() {
    return getParallelizableValueStream(graphs)
      .map(LongsPair::getEdgeIds)
      .flatMapToLong(LongStream::of)
      .toArray();
  }


  @Override
  public long[] getCollectionIds() {
    return getParallelizableEntryStream(collections)
      .mapToLong(Map.Entry::getKey)
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
    if (value) {
      booleanValues.add(id);
    } else {
      booleanValues.remove(id);
    }
  }

  @Override
  public void set(long id, int key, long value) {
    getLongValues(key).put(id, value);
  }

  @Override
  public void set(long id, int key, double value) {
    getDoubleValues(key).put(id, value);
  }

  @Override
  public void set(long id, int key, String value) {
    getStringValues(key).put(id, value);
  }

  @Override
  public void set(long id, int key, BigDecimal value) {
    getBigDecimalValues(key).put(id, value);
  }

  @Override
  public void set(long id, int key, LocalDate value) {
    getLocalDateValues(key).put(id, value);
  }

  @Override
  public void set(long id, int key, int[] value) {
    getIntsValues(key).put(id, value);
  }

  @Override
  public void set(long id, int key, String[] value) {
    getStringsValues(key).put(id, value);
  }

  @Override
  public boolean is(long id, int key) {
    return getBooleanValues(key).contains(id);
  }

  @Override
  public void add(long id, int key, int value) {
    getIntsValues(key).compute(id, (k, values) ->
        values == null ? new int[] {value} : ArrayUtils.add(values, value));
  }

  @Override
  public void add(long id, int key, String value) {
    getStringsValues(key).compute(id, (k, values) ->
        values == null ? new String[] {value} : ArrayUtils.add(values, value));
  }

  @Override
  public KeyObjectPair[] getProperties(long id) {
    List<KeyObjectPair> properties = booleanProperties
        .entrySet()
        .parallelStream()
        .filter(e -> e.getValue().contains(id))
        .map(e -> new KeyObjectPair(e.getKey(), true))
        .collect(Collectors.toList());

    properties.addAll(getProperties(longProperties, id));
    properties.addAll(getProperties(doubleProperties, id));
    properties.addAll(getProperties(stringProperties, id));
    properties.addAll(getProperties(bigDecimalProperties, id));
    properties.addAll(getProperties(localDateProperties, id));
    properties.addAll(getProperties(intsProperties, id));
    properties.addAll(getProperties(stringsProperties, id));

    return properties
        .toArray(new KeyObjectPair[properties.size()]);
  }

  /**
   * Get all properties of a given element id and type.
   *
   * @param propertyMap key -> id -> value
   * @param id element id
   * @param <T> value type
   * @return all properties with of the given id and type
   */
  private <T> List<KeyObjectPair> getProperties(Map<Integer, Map<Long, T>> propertyMap, long id) {
    return getParallelizableEntryStream(propertyMap)
        .flatMap(e -> e.getValue()
            .entrySet()
            .stream()
            .filter(f -> f.getKey() == id)
            .map(f -> new KeyObjectPair(e.getKey(), f.getValue())))
        .collect(Collectors.toList());
  }

  /**
   * Get values by key and compute if absent.
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
   * Get all properties of a given type.
   *
   * @param propertyMap key -> id -> value
   * @param <T> value type
   * @return all properties of a given type
   */
  private <T> List<Pair<Long, KeyObjectPair>> getProperties(
      Map<Integer, Map<Long, T>> propertyMap) {
    return getParallelizableEntryStream(propertyMap)
        .flatMap(e -> e.getValue()
            .entrySet()
            .stream()
            .map(f -> new Pair<>(f.getKey(), new KeyObjectPair(e.getKey(), f.getValue()))))
        .collect(Collectors.toList());
  }

  @Override
  public Map<Long, Property[]> getAllProperties() {

    List<Pair<Long, KeyObjectPair>> properties = getParallelizableEntryStream(booleanProperties)
        .flatMap(e -> e.getValue()
            .stream()
            .map(id -> new Pair<>(id, new KeyObjectPair(e.getKey(), true))))
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
  public String[] getStrings(long id, int key) {
    return getStringsValues(key).get(id);
  }

  // QueryElements

  @Override
  public long[] queryElements(IntPredicate labelPredicate) {
    return labels
      .entrySet()
      .stream()
      .filter(e -> labelPredicate.test(e.getValue()))
      .mapToLong(Map.Entry::getKey)
      .toArray();
  }

  @Override
  public long[] queryElements(PropertyPredicate propertyPredicate) {
    return labels
      .keySet()
      .stream()
      .filter(id -> propertyPredicate.apply(this, id))
      .mapToLong(id -> id)
      .toArray();
  }

  @Override
  public long[] queryElements(IntPredicate labelPredicate, PropertyPredicate propertyPredicate) {
    return labels
      .entrySet()
      .stream()
      .filter(e -> labelPredicate.test(e.getValue()))
      .mapToLong(Map.Entry::getKey)
      .filter(id -> propertyPredicate.apply(this, id))
      .toArray();
  }

  /**
   * Return a map's entry stream according to parallel read mode.
   *
   * @param map map
   * @param <K> key type
   * @param <V> value type
   * @return value stream
   */
  private <K, V> Stream<Map.Entry<K,V>> getParallelizableEntryStream(Map<K, V> map) {
    Set<Map.Entry<K, V>> entries = map.entrySet();
    return parallelRead ? entries.parallelStream() : entries.stream();
  }

  /**
   * Return a map's key stream according to parallel read mode.
   *
   * @param map map
   * @param <K> key type
   * @param <V> value type
   * @return value stream
   */
  public <K, V> Stream<K> getParallelizableKeyStream(Map<K, V> map) {
    Set<K> values = map.keySet();
    return parallelRead ? values.parallelStream() : values.stream();
  }

  /**
   * Return a map's value stream according to parallel read mode.
   *
   * @param map map
   * @param <K> key type
   * @param <V> value type
   * @return value stream
   */
  private <K, V> Stream<V> getParallelizableValueStream(Map<K, V> map) {
    Collection<V> values = map.values();
    return parallelRead ? values.parallelStream() : values.stream();
  }


  // PropertyGraphDB

  @Override
  public CachedGraph getCachedGraph(long graphId) {
    int graphLabel = labels.get(graphId);

    LongsPair globalVertexIdsEdgeIds = graphs.get(graphId);

    long[] globalVertexIds = globalVertexIdsEdgeIds.getVertexIds();

    int[] vertexLabels = LongStream
      .of(globalVertexIds)
      .mapToInt(labels::get)
      .toArray();

    long[] globalEdgeIds = globalVertexIdsEdgeIds.getEdgeIds();

    int edgeCount = globalEdgeIds.length;
    int[] edgeLabels = new int[edgeCount];
    int[] sourceIds = new int[edgeCount];
    int[] targetIds = new int[edgeCount];

    for (int localEdgeId = 0; localEdgeId < edgeCount; localEdgeId++) {
      Long globalEdgeId = globalEdgeIds[localEdgeId];
      LongPair globalSourceTargetPair = edges.get(globalEdgeId);

      long globalSourceId = globalSourceTargetPair.getSourceId();
      int sourceId = ArrayUtils.indexOf(globalVertexIds, globalSourceId);
      sourceIds[localEdgeId] = sourceId;

      long globalTargetId = globalSourceTargetPair.getTargetId();
      int targetId = ArrayUtils.indexOf(globalVertexIds, globalTargetId);
      targetIds[localEdgeId] = targetId;

      edgeLabels[localEdgeId] = labels.get(globalEdgeId);
    }

    return new CachedGraphBase(graphId, graphLabel, vertexLabels, edgeLabels, sourceIds, targetIds);
  }

  @Override
  public List<CachedGraph> getCachedCollection(long collectionId) {
    LongStream graphIdStream = LongStream
        .of(collections.get(collectionId));

    if (parallelRead) {
      graphIdStream = graphIdStream.parallel();
    }

    return graphIdStream
      .mapToObj(this::getCachedGraph)
      .collect(Collectors.toList());
  }


  @Override
  public String toString() {
    return "nid=" + nextId
        + "\nnsy=" + nextSymbol
        + "\nsid=" + stringIntegerDictionary
        + "\nisd=" + integerStringDictionary
        + "\nlbs=" + labels
        + "\negs=" + edges
        + "\nels=" + graphs
        + "\nbol=" + booleanProperties
        + "\nint=" + longProperties
        + "\ndob=" + doubleProperties
        + "\nstr=" + stringProperties
        + "\nbdc=" + bigDecimalProperties
        + "\nlod=" + localDateProperties
        + "\nina=" + intsProperties
        + "\nsta=" + stringsProperties;
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
