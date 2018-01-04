package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputFactory;

import java.util.Map;

public class CharacteristicFactory implements FilterAndOutputFactory {
  private final Map<Integer, String> categorizedGraphs;
  private final Interestingness interestingness;

  public CharacteristicFactory(Map<Integer, String> categorizedGraphs, Interestingness interestingness) {
    this.categorizedGraphs = categorizedGraphs;
    this.interestingness = interestingness;
  }

  @Override
  public FilterAndOutput create(int minSupportAbs, GraphCollection output) {
    return new Characteristic(minSupportAbs, output, categorizedGraphs, interestingness);
  }
}
