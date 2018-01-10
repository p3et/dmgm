package org.biiig.dmgm.impl.algorithms.aggregation;


import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.OperatorBase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class Aggregation extends OperatorBase {

  private final BiConsumer<Graph, ElementDataStore> aggregateFunction;

  public Aggregation(BiConsumer<Graph, ElementDataStore> aggregateFunction) {
    this.aggregateFunction = aggregateFunction;
  }

  public static Aggregation forBoolean(String propertyKey, Function<Graph, Boolean> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forInteger(String propertyKey, Function<Graph, Integer> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forIntegers(String propertyKey, Function<Graph, int[]> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forString(String propertyKey, Function<Graph, String> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forStrings(String propertyKey, Function<Graph, String[]> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forDecimal(String propertyKey, Function<Graph, BigDecimal> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }

  public static Aggregation forDate(String propertyKey, Function<Graph, LocalDate> function) {
    return new Aggregation((g, s) -> s.setGraph(g.getId(), propertyKey, function.apply(g)));
  }


  @Override
  public GraphCollection apply(GraphCollection graphs) {
    Stream<Graph> stream = graphs
      .stream();

    stream = setParallelism(stream);

    stream
      .parallel()
      .forEach(g -> aggregateFunction.accept(g, graphs.getElementDataStore()));
    
    return graphs;
  }

}
