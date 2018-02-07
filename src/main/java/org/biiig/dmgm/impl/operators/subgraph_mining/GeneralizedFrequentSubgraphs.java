package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.SpecializableCachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.FrequentSupportMethods;

import java.util.Map;

public class GeneralizedFrequentSubgraphs extends GeneralizedSubgraphsBase<Long> {


  public GeneralizedFrequentSubgraphs(GraphDB database, float minSupportRel, int maxEdgeCount) {
    super(maxEdgeCount, database, minSupportRel);
  }

  @Override
  public SupportMethods<Long> getAggregateAndFilter(Map<Long, SpecializableCachedGraph> input) {
    long minSupportAbsolute = (long) Math.round(input.size() * minSupportRel);
    return new FrequentSupportMethods(database, parallel, minSupportAbsolute);
  }
}
