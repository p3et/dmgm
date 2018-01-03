package org.biiig.dmgm.impl.algorithms.fsm.fsm;

import de.jesemann.paralleasy.queue_stream.QueueStreamSource;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.impl.algorithms.fsm.common.SubgraphMiningBase;
import org.biiig.dmgm.impl.graph.DFSCode;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Directed Multigraph gSpan
 */
public class FrequentSubgraphs extends SubgraphMiningBase {

  public FrequentSubgraphs(float minSupportRel, int maxEdgeCount) {
    super(minSupportRel, maxEdgeCount);
  }

  @Override
  public GraphCollection apply(GraphCollection rawInput) {
    int minSupportAbs = Math.round((float) rawInput.size() * this.minSupportRel);

    GraphCollectionBuilder collectionBuilder = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(rawInput.getLabelDictionary());

    GraphCollection input = pruneByLabels(rawInput, minSupportAbs, collectionBuilder);
    GraphCollection output = collectionBuilder.create();

    Map<DFSCode, DFSEmbedding[]> singleEdgeCandidates = initializeSingle(input);

    Predicate<DFSCodeEmbeddingsPair> predicate = new FilterFrequent(minSupportAbs);
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
