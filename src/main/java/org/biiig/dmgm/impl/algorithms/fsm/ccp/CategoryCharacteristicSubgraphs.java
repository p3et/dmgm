package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CategoryCharacteristicSubgraphs implements Operator {

  public static final String CATEGORY_KEY = "_category";

  private final Categorization categorization;
  private final Interestingness interestingness;

  /**
   * @param dataStore
   * @param categorization
   * @param interestingness
   * */

  public CategoryCharacteristicSubgraphs(
    ElementDataStore dataStore, Categorization categorization, Interestingness interestingness) {
    this.categorization = categorization;
    this.interestingness = interestingness;
  }

  @Override
  public GraphCollection apply(GraphCollection input) {
    Map<String, List<Graph>> categorizedGraphs = input
      .parallelStream()
      .collect(new GroupByKeyListValues<>(categorization::categorize, Function.identity()));

    return null;
  }
}
