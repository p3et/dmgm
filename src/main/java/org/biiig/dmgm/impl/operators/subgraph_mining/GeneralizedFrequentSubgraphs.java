package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SupportMethods;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.FrequentSupportMethods;

import java.util.List;

public class GeneralizedFrequentSubgraphs extends GeneralizedSubgraphsBase<Long> {


  public GeneralizedFrequentSubgraphs(GraphDB database, float minSupportRel, int maxEdgeCount) {
    super(maxEdgeCount, database, minSupportRel);
  }

  @Override
  public SupportMethods<Long> getAggregateAndFilter(List<CachedGraph> input) {
    long minSupportAbsolute = (long) Math.round(input.size() * minSupportRel);
    return new FrequentSupportMethods(database, minSupportAbsolute);
  }
}
