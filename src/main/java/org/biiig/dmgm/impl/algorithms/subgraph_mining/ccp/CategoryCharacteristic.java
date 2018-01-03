package org.biiig.dmgm.impl.algorithms.subgraph_mining.ccp;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CategoryCharacteristic implements FilterAndOutput {
  private final int minSupportAbs;
  private final GraphCollection output;

  private final Map<Integer, String> graphCategory;
  private final Interestingness interestingness;

  public CategoryCharacteristic(
    int minSupportAbs,
    GraphCollection output, Map<Integer, String> graphCategory,
    Interestingness interestingness) {
    this.minSupportAbs = minSupportAbs;
    this.graphCategory = graphCategory;
    this.interestingness = interestingness;
    this.output = output;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pairs) {
    DFSEmbedding[] embeddings = pairs.getEmbeddings();
    int frequency = embeddings.length;

    boolean characteristic = frequency >= minSupportAbs;

    if (characteristic) {
      List<String> graphIds = Stream.of(embeddings)
        .map(DFSEmbedding::getGraphId)
        .distinct()
        .map(graphCategory::get)
        .collect(Collectors.toList());

      int totalSupport = graphIds.size();
      characteristic = totalSupport >= minSupportAbs;

      if (characteristic) {
        Collection<String> characteristicCategories = Lists.newArrayList();

        graphIds
          .stream()
          .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
          .forEach((category, support) -> {
            if (interestingness.isInteresting(Math.toIntExact(support), totalSupport))
              characteristicCategories.add(category);
          });

        characteristic = !characteristicCategories.isEmpty();

        if (characteristic) {
          DFSCode dfsCode = pairs.getDfsCode();
          int graphId = output.add(dfsCode);
          String[] categories = characteristicCategories.toArray(new String[characteristicCategories.size()]);
          output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.CATEGORIES, categories);
          output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, totalSupport);
          output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.FREQUENCY, frequency);
        }
      }
    }

    return characteristic;
  }


}
