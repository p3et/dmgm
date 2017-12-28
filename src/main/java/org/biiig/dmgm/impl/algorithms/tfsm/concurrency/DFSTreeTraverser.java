package org.biiig.dmgm.impl.algorithms.tfsm.concurrency;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.logic.DFSCodeOperations;
import org.biiig.dmgm.impl.algorithms.tfsm.logic.DFSTreeNodeAggregator;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;
import org.biiig.dmgm.impl.algorithms.tfsm.model.GraphIdEmbeddingPair;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DFSTreeTraverser
  extends DequeUpdateTask<DFSTreeNode> implements TaskWithOutput<List<IntGraph>> {

  private final IntGraphCollection input;
  private List<IntGraph> output = Lists.newLinkedList();
  private final DFSCodeOperations gSpan = new DFSCodeOperations();
  private final DFSTreeNodeAggregator aggregator = new DFSTreeNodeAggregator();
  private final int minSupport;


  public DFSTreeTraverser(Deque<DFSTreeNode> deque, AtomicInteger activeCount,
                          IntGraphCollection input, int minSupport) {
    super(deque, activeCount);
    this.input = input;
    this.minSupport = minSupport;
  }

  @Override
  public List<IntGraph> getOutput() {
    return output;
  }

  @Override
  protected Collection<DFSTreeNode> process(DFSTreeNode next) {
    DFSCode parentCode = next.getDfsCode();
    output.add(parentCode);

    List<DFSTreeNode> children = Lists.newLinkedList();

    // for each graph supporting the parent code
    for (GraphIdEmbeddingPair graphEmbeddings : next.getEmbeddings()) {
      int graphId = graphEmbeddings.getGraphId();
      IntGraph graph = input.getGraph(graphId);

      DFSCodeEmbeddingsPair[] childDFSCodes = gSpan
        .growChildDFSCodes(graph, parentCode, graphEmbeddings.getEmbeddings());

      for (DFSCodeEmbeddingsPair pair : childDFSCodes) {
        GraphIdEmbeddingPair childGraphEmbeddings = new GraphIdEmbeddingPair(graphId, pair.getEmbeddings());
        children.add(new DFSTreeNode(pair.getDfsCode(), childGraphEmbeddings));
      }
    }

    children = aggregator.aggregate(children);
    children.removeIf(c -> c.getSupport() < minSupport || !gSpan.isMinimal(c.getDfsCode()));

    return children;
  }

}
