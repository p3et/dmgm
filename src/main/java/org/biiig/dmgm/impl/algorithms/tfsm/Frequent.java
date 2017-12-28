package org.biiig.dmgm.impl.algorithms.tfsm;

import javafx.util.Pair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;
import java.util.function.Predicate;

public class Frequent implements Predicate<Pair<DFSCode, List<DFSEmbedding>>> {
  private final int minSupportAbs;

  public Frequent(int minSupportAbs) {
    this.minSupportAbs = minSupportAbs;
  }


  @Override
  public boolean test(Pair<DFSCode, List<DFSEmbedding>> dfsCodeListPair) {

    List<DFSEmbedding> embeddings = dfsCodeListPair.getValue();

    int support = Math.toIntExact(
      embeddings
        .stream()
        .map(DFSEmbedding::getGraphId)
        .distinct()
        .count()
    );

    boolean frequent = support >= minSupportAbs;

    if (frequent) {
      DFSCode dfsCode = dfsCodeListPair.getKey();
      dfsCode.setSupport(support);
      dfsCode.setFrequency(embeddings.size());
    }

    return frequent;
  }
}
