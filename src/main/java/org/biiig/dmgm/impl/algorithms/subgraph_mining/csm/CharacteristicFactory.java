package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;

import java.util.Map;

public class CharacteristicFactory implements FilterAndOutputFactory {
  private final Interestingness interestingness;
  private final Map<Integer, Integer> categoryMinSupportsAbsolute;
  private final int defaultCategory;
  private final Map<Integer, Integer> categorySizes;
  private final int inputSize;

  CharacteristicFactory(
    Interestingness interestingness,
    Map<Integer, Integer> categorySizes,
    Map<Integer, Integer> categoryMinSupports,
    int inputSize,
    int defaultCategory
  ) {
    this.interestingness = interestingness;
    this.categorySizes = categorySizes;
    this.categoryMinSupportsAbsolute = categoryMinSupports;
    this.inputSize = inputSize;
    this.defaultCategory = defaultCategory;
  }

  @Override
  public FilterAndOutput create(GraphCollection output) {
    return new Characteristic(
      output, interestingness, categorySizes, categoryMinSupportsAbsolute, inputSize, defaultCategory);
  }
}
