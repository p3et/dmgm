package org.biiig.dmgm.impl.algorithms.tfsm;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.biiig.dmgm.api.concurrency.TaskWithOutput;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraph;
import org.biiig.dmgm.impl.concurrency.DequeUpdateTask;
import org.biiig.dmgm.impl.model.graph.DFSCode;

import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DFSTreeTraverser
  extends DequeUpdateTask<DFSTreeNode> implements TaskWithOutput<List<DMGraph>> {

  private final DMGraphCollection input;
  private List<DMGraph> output = Lists.newArrayList();
  private final GSpanLogic gSpan = new GSpanLogic();
  private final Aggregator aggregator = new Aggregator();


  public DFSTreeTraverser(Deque<DFSTreeNode> deque, AtomicInteger activeCount,
    DMGraphCollection input) {
    super(deque, activeCount);
    this.input = input;
  }

  @Override
  public List<DMGraph> getOutput() {
    return output;
  }

  @Override
  protected Collection<DFSTreeNode> process(DFSTreeNode next) {

    DFSCode parentCode = next.getDfsCode();

    output.add(parentCode);

    List<DFSTreeNode> childNodes = Lists.newLinkedList();


    // for each graph supporting the parent code
    for (GraphDFSEmbeddings graphEmbeddings : next.getEmbeddings()) {
      int graphId = graphEmbeddings.getGraphId();
      DMGraph graph = input.getGraph(graphId);

      List<DFSCodeEmbeddingPair> reports = gSpan
        .growChildren(graph, parentCode, graphEmbeddings.getEmbeddings());

      childNodes.addAll(aggregator.aggregateReports(reports));
    }

    childNodes = aggregator.aggregate(childNodes);

    return childNodes;
  }




}
