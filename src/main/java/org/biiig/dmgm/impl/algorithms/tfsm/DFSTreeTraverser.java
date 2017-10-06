package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;
import org.biiig.dmgm.todo.gspan.DFSTreeNode;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DFSTreeTraverser
  extends DequeUpdateTask<DFSTreeNode> implements TaskWithOutput<List<DMGraph>> {

  private List<DMGraph> output = Lists.newArrayList();
  private Collection<DFSTreeNode> children = Lists.newLinkedList();


  public DFSTreeTraverser(Deque<DFSTreeNode> deque, AtomicInteger activeCount) {
    super(deque, activeCount);
  }

  @Override
  public List<DMGraph> getOutput() {
    return output;
  }

  @Override
  protected Collection<DFSTreeNode> process(DFSTreeNode next) {
    children.clear();

    output.add(next.getDfsCode());

    // TODO pattern growth



    return children;
  }
}
