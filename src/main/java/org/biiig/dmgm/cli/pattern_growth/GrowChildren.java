package org.biiig.dmgm.cli.pattern_growth;

import com.google.common.collect.Lists;
import javafx.util.Pair;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Collection;

public class GrowChildren {

  public Collection<Pair<DFSCode, DFSEmbedding>> apply(
    IntGraph graph, DFSCode parent, int[] rightmostPath, DFSEmbedding parentEmbedding) {

    Collection<Pair<DFSCode, DFSEmbedding>> children = Lists.newArrayList();


    return children;
  }


}
