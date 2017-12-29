package org.biiig.dmgm.impl.algorithms.tfsm;

import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class FilterFrequent implements Predicate<DFSCodeEmbeddingsPair> {
  private final int minSupportAbs;

  public FilterFrequent(int minSupportAbs) {
    this.minSupportAbs = minSupportAbs;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    DFSEmbedding[] embeddings = pairs.getEmbeddings();
    int frequency = embeddings.length;

    boolean frequent = frequency >= minSupportAbs;

    if (frequent) {
      int support = Math.toIntExact(
        Stream
          .of(embeddings)
          .map(DFSEmbedding::getGraphId)
          .distinct()
          .count()
      );

      frequent = support >= minSupportAbs;
    }

    return frequent;
  }


}
