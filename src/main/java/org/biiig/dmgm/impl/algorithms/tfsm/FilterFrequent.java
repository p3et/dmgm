package org.biiig.dmgm.impl.algorithms.tfsm;

import javafx.util.Pair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.List;
import java.util.function.Predicate;

public class FilterFrequent implements Predicate<Pair<DFSCode,List<DFSEmbedding>>> {
  private final int minSupportAbs;

  public FilterFrequent(int minSupportAbs) {
    this.minSupportAbs = minSupportAbs;
  }

  @Override
  public boolean test(Pair<DFSCode,List<DFSEmbedding>> pairs) {
    List<DFSEmbedding> embeddings = pairs.getValue();
    int frequency = embeddings.size();

    boolean frequent = frequency >= minSupportAbs;

    if (frequent) {
      int support = Math.toIntExact(
        embeddings
          .stream()
          .map(DFSEmbedding::getGraphId)
          .distinct()
          .count()
      );

      frequent = support >= minSupportAbs;
    }

    return frequent;
  }


}
