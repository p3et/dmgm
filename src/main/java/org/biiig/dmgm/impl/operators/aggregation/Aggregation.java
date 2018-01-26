package org.biiig.dmgm.impl.operators.aggregation;


import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.operators.HyperVertexOperatorBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Aggregation extends HyperVertexOperatorBase {

  private final BiConsumer<SmallGraph, PropertyStore> aggregateFunction;

  public Aggregation(BiConsumer<SmallGraph, PropertyStore> aggregateFunction) {
    this.aggregateFunction = aggregateFunction;
  }

  public static Aggregation forBoolean(String propertyKey, Function<SmallGraph, Boolean> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forInteger(String propertyKey, Function<SmallGraph, Integer> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forIntegers(String propertyKey, Function<SmallGraph, int[]> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forString(String propertyKey, Function<SmallGraph, String> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forStrings(String propertyKey, Function<SmallGraph, String[]> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forDecimal(String propertyKey, Function<SmallGraph, BigDecimal> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forDate(String propertyKey, Function<SmallGraph, LocalDate> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }


  @Override
  public GraphCollection apply(GraphCollection graphs) {
    Stream<SmallGraph> stream = graphs
      .stream();

    stream = setParallelism(stream);

    stream
      .parallel()
      .forEach(g -> aggregateFunction.accept(g, graphs.getElementDataStore()));
    
    return graphs;
  }

}
