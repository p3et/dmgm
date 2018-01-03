package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.algorithms.fsm.fsm.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.fsm.fsm.DFSEmbedding;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryCharacteristic implements Predicate<DFSCodeEmbeddingsPair> {
  private final int minSupportAbs;
  private final Map<Integer, String> graphCategory;
  private final Interestingness interestingness;

  public CategoryCharacteristic(int minSupportAbs, Map<Integer, String> graphCategory, Interestingness interestingness) {
    this.minSupportAbs = minSupportAbs;
    this.graphCategory = graphCategory;
    this.interestingness = interestingness;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    DFSEmbedding[] embeddings = pairs.getEmbeddings();
    int frequency = embeddings.length;

    boolean characteristic = frequency >= minSupportAbs;

    if (characteristic) {
      List<Integer> graphIds = Stream.of(embeddings)
        .map(DFSEmbedding::getGraphId)
        .distinct()
        .collect(Collectors.toList());

      int totalSupport = graphIds.size();
      characteristic = totalSupport >= minSupportAbs;

      if (characteristic) {
        Collection<Integer> characteristicCategories = Lists.newArrayList();

        graphIds
          .stream()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .forEach((category, support) -> {
            if (interestingness.isInteresting(Math.toIntExact(support), totalSupport))
              characteristicCategories.add(category);
          });

        characteristic = !characteristicCategories.isEmpty();

        // TODO store categories
      }
    }

    return characteristic;
  }


}
