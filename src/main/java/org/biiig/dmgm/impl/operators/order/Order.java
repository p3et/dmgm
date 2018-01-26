package org.biiig.dmgm.impl.operators.order;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.operators.OperatorBase;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilder;

import java.util.Comparator;

public class Order extends OperatorBase {
  private final Comparator<SmallGraph> comparator;

  private Order(Comparator<SmallGraph> comparator) {
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

  public static Order by(Comparator<SmallGraph> comparator) {
    return new Order(comparator);
  }
}
