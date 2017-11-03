package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleEdgeNodeCreator
  extends DequeUpdateTask<Integer> implements TaskWithOutput<List<DFSTreeNode>> {

  private final DMGraphCollection input;
  private final List<DFSTreeNode> output = Lists.newLinkedList();
  private final DFSCodeOperations gSpan = new DFSCodeOperations();

  private final List<DFSTreeNode> childNodes = Lists.newLinkedList();
  private final Collection<Integer> emptyCollection = Lists.newArrayListWithCapacity(0);

  public SingleEdgeNodeCreator(
    AtomicInteger activeCount, Deque<Integer> deque, DMGraphCollection input) {
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

    DMGraph graph = input.getGraph(graphId);

    gSpan.initSingleEdgeDFSCodes(graph);
    output.addAll(childNodes);

    return emptyCollection;
  }


}
