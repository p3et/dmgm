package org.biiig.dmgm.impl.algorithms.tfsm.concurrency;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.impl.algorithms.tfsm.logic.DFSCodeOperations;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSTreeNode;
import org.biiig.dmgm.impl.algorithms.tfsm.model.GraphIdEmbeddingPair;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DFSTreeInitializer
  extends DequeUpdateTask<Integer> implements TaskWithOutput<List<DFSTreeNode>> {

  private final GraphCollection input;
  private final List<DFSTreeNode> output = Lists.newLinkedList();
  private final DFSCodeOperations gSpan = new DFSCodeOperations();

  private final List<DFSTreeNode> childNodes = Lists.newLinkedList();
  private final Collection<Integer> emptyCollection = Lists.newArrayListWithCapacity(0);

  public DFSTreeInitializer(
    AtomicInteger activeCount, Deque<Integer> deque, GraphCollection input) {
    super(deque, activeCount);
    this.input = input;
  }

  @Override
  public List<DFSTreeNode> getOutput() {
    return output;
  }

  @Override
  protected Collection<Integer> process(Integer graphId) {
    childNodes.clear();

    IntGraph graph = input.getGraph(graphId);

    DFSCodeEmbeddingsPair[] sinleEdgeDFSCodes = gSpan.initSingleEdgeDFSCodes(graph);

    for (DFSCodeEmbeddingsPair pair : sinleEdgeDFSCodes) {
      GraphIdEmbeddingPair graphEmbeddings = new GraphIdEmbeddingPair(graphId, pair.getEmbeddings());
      output.add(new DFSTreeNode(pair.getDfsCode(), graphEmbeddings));
    }

    output.addAll(childNodes);

    return emptyCollection;
  }


}
