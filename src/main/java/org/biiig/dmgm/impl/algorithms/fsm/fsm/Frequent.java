package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class Frequent implements Predicate<DFSCodeEmbeddingsPair> {
  private final int minSupportAbs;

  public Frequent(int minSupportAbs) {
    this.minSupportAbs = minSupportAbs;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    DFSEmbedding[] embeddings = pairs.getEmbeddings();
    int frequency = embeddings.length;

    boolean frequent = frequency >= minSupportAbs;

    if (frequent) {
      int support = Math.toIntExact(
        Stream.of(embeddings)
          .map(DFSEmbedding::getGraphId)
          .distinct()
          .count()
      );

      frequent = support >= minSupportAbs;
    }

    return frequent;
  }


}
