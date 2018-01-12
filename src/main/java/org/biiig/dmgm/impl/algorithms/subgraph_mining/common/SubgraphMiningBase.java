package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import de.jesemann.paralleasy.recursion.RecursiveTask;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.api.Operator;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class SubgraphMiningBase extends org.biiig.dmgm.impl.algorithms.OperatorBase {
  protected final float minSupport;
  private final int maxEdgeCount;

  public SubgraphMiningBase(float minSupport, int maxEdgeCount) {
    this.minSupport = minSupport;
    this.maxEdgeCount = maxEdgeCount;
  }

  // ORCHESTRATION

  @Override
  public GraphCollection apply(GraphCollection rawInput) {
    GraphCollectionBuilder collectionBuilder = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(rawInput.getLabelDictionary())
      .withElementDataStore(rawInput.getElementDataStore());

    FilterOrOutput<DFSCodeEmbeddingsPair> filterOrOutput = getFilterAndOutput(rawInput);

    GraphCollection input = getPreprocessor().apply(rawInput, collectionBuilder);

    SingleEdgeDFSNodes singleEdgeDFSNodes = new SingleEdgeDFSNodes(input, filterOrOutput);
    singleEdgeDFSNodes.run();

    RecursiveTask<DFSCodeEmbeddingsPair, Consumer<GraphCollection>> patternGrowth = RecursiveTask
      .createFor(new ProcessDFSNode(input, filterOrOutput, maxEdgeCount))
      .on(singleEdgeDFSNodes.getSingleEdgeDFSNodes());
    patternGrowth.run();

    GraphCollection output = collectionBuilder.create();

    singleEdgeDFSNodes
      .getOutput()
      .forEach(c -> c.accept(output));

    patternGrowth
      .getOutput()
      .forEach(c -> c.accept(output));

    return output;
  }

  protected abstract Preprocessor getPreprocessor();

  protected abstract FilterOrOutput<DFSCodeEmbeddingsPair> getFilterAndOutput(GraphCollection rawInput);

}
