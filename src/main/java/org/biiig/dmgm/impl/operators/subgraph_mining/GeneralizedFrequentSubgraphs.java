package org.biiig.dmgm.impl.operators.subgraph_mining;

import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.CachedGraph;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.Preprocessor;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.biiig.dmgm.impl.operators.subgraph_mining.frequent.Frequent;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.Generalized;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.GeneralizedSubgraphsBase;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.PatternVectorsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.generalized.Specializer;

import java.util.List;

public class GeneralizedFrequentSubgraphs extends GeneralizedSubgraphsBase implements Frequent {

  public GeneralizedFrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

}
