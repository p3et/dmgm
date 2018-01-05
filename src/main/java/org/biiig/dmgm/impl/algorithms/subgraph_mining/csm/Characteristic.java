package org.biiig.dmgm.impl.algorithms.subgraph_mining.csm;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutput;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.FilterAndOutputBase;
import org.biiig.dmgm.impl.algorithms.subgraph_mining.common.SubgraphMiningPropertyKeys;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Characteristic extends FilterAndOutputBase implements FilterAndOutput {
  private final Map<Integer, Integer> categorySizes;
  private final Map<Integer, Integer> categoryMinSupports;

  private final Interestingness interestingness;
  private final int defaultCategory;
  private final int graphCount;

  Characteristic(
    GraphCollection output,
    Interestingness interestingness, Map<Integer, Integer> categorySizes,
    Map<Integer, Integer> categoryMinSupports, int inputSize,
    int defaultCategory
  ) {
    super(output);
    this.categorySizes = categorySizes;
    this.interestingness = interestingness;
    this.categoryMinSupports = categoryMinSupports;
    graphCount = inputSize;
    this.defaultCategory = defaultCategory;
  }

  @Override
  public boolean test(DFSCodeEmbeddingsPair pair) {
    DFSEmbedding[] embeddings = pair.getEmbeddings();

    int totalSupport = getSupport(embeddings);

    Map<Integer, List<DFSEmbedding>> categorizedEmbeddings = Stream.of(embeddings)
      .collect(
        Collectors.groupingBy(
          e -> output
            .getElementDataStore()
            .getGraphInteger(e.getGraphId(), SubgraphMiningPropertyKeys.CATEGORY).orElse(defaultCategory),
          Collectors.toList()));

    // all categories in which the pattern is characteristic will be added
    int[] categories = new int[0];

    // will be set to true, if the pattern is frequent in at least one category
    boolean atLeastOnceFrequent = false;

    for (Map.Entry<Integer, List<DFSEmbedding>> entry : categorizedEmbeddings.entrySet()) {
      Integer category = entry.getKey();

      int categorySupport = getSupport(entry.getValue());
      if (categorySupport >= categoryMinSupports.get(category)) {
        atLeastOnceFrequent = true;

        if (isInteresting(category, categorySupport, totalSupport))
          categories = ArrayUtils.add(categories, category);
      }
    }

    // output only of characteristic for at least one category
    if (categories.length > 0)
      store(pair.getDfsCode(), embeddings.length, totalSupport, categories);

    // grow children as long as frequent in at least one category
    return atLeastOnceFrequent;
  }

  public boolean isInteresting(Integer category, int categorySupportAbsolute, int totalSupportAbsolute) {
    float categorySupport = (float) categorySupportAbsolute / categorySizes.get(category);
    float totalSupport = (float) totalSupportAbsolute / graphCount;
    return interestingness.isInteresting(categorySupport, totalSupport);
  }

  private void store(DFSCode dfsCode, int embeddingCount, int support, int[] categories) {
    int graphId = output.add(dfsCode);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.SUPPORT, support);
    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.EMBEDDING_COUNT, embeddingCount);

    String[] translatedCategories = new String[categories.length];

    for (int i = 0; i < categories.length; i++)
      translatedCategories[i] = output.getLabelDictionary().translate(categories[i]);

    output.getElementDataStore().setGraph(graphId, SubgraphMiningPropertyKeys.CATEGORIES, translatedCategories);
  }
}
