package org.biiig.dmgm.impl.algorithms.fsm.ccp;

import de.jesemann.paralleasy.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.algorithms.fsm.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.algorithms.fsm.fsm.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.fsm.fsm.DFSEmbedding;
import org.biiig.dmgm.impl.algorithms.fsm.fsm.Frequent;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CategoryCharacteristicSubgraphs extends SubgraphMiningBase {

  public static final String CATEGORY_KEY = "_category";

  private final Categorization categorization;
  private final Interestingness interestingness;

  /**
   * @param categorization
   * @param interestingness */

  public CategoryCharacteristicSubgraphs(
    float minSupportRel, int maxEdgeCount, Categorization categorization, Interestingness interestingness) {

    super(minSupportRel, maxEdgeCount);

    this.categorization = categorization;
    this.interestingness = interestingness;
  }

  @Override
  public GraphCollection apply(GraphCollection rawInput) {
    Map<Integer, String> categorizedGraphs = rawInput
      .parallelStream()
      .collect(Collectors.toConcurrentMap(Graph::getId, categorization::categorize));

    int minSupportAbs = Math.round((float) rawInput.size() * this.minSupportRel);

    GraphCollectionBuilder collectionBuilder = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(rawInput.getLabelDictionary());

    GraphCollection input = pruneByLabels(rawInput, minSupportAbs, collectionBuilder);
    GraphCollection output = collectionBuilder.create();

    Map<DFSCode, DFSEmbedding[]> singleEdgeCandidates = initializeSingle(input);

    Predicate<DFSCodeEmbeddingsPair> predicate = new CategoryCharacteristic(minSupportAbs, categorizedGraphs, interestingness);
    List<DFSCodeEmbeddingsPair> parents = aggregateSingle(singleEdgeCandidates, predicate, output);

    QueueStreamSource<DFSCodeEmbeddingsPair> queueStreamSource = QueueStreamSource.of(parents);

    queueStreamSource
      .parallelStream()
      .forEach(p -> {
        Map<DFSCode, DFSEmbedding[]> candidates = growChildren(p, input);
        aggregateChildren(candidates, predicate, queueStreamSource, output);
      });

    return output;
  }

}
