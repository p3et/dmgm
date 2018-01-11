package org.biiig.dmgm.impl.algorithms.order;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.algorithms.OperatorBase;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilder;

import java.util.Comparator;

public class Order extends OperatorBase {
  private final Comparator<Graph> comparator;

  private Order(Comparator<Graph> comparator) {
    this.comparator = comparator;
  }

  @Override
  public GraphCollection apply(GraphCollection input) {
    GraphCollection output = new InMemoryGraphCollectionBuilder()
      .withElementDataStore(input.getElementDataStore())
      .withLabelDictionary(input.getLabelDictionary())
      .create();

    input
      .stream()
      .sequential()
      .sorted(comparator)
      .forEach(output::add);

    return output;
  }

  public static Order by(Comparator<Graph> comparator) {
    return new Order(comparator);
  }
}
