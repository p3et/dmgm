package org.biiig.dmgm.impl.model.source.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.model.source.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class TLFLabelReaderFactory implements TLFSplitReaderFactory {

  private AtomicInteger graphCount = new AtomicInteger(0);

  private final List<Countable<String>> vertexLabelFrequencies = Lists.newCopyOnWriteArrayList();

  private final List<Countable<String>> edgeLabelFrequencies = Lists.newCopyOnWriteArrayList();

  @Override
  public TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFLabelReader(
      splits, reachedEOF, graphCount, vertexLabelFrequencies, edgeLabelFrequencies) {
    };
  }

  public int getGraphCount() {
    return graphCount.get();
  }

  public List<Countable<String>> getVertexLabelFrequencies() {
    return vertexLabelFrequencies;
  }

  public List<Countable<String>> getEdgeLabelFrequencies() {
    return edgeLabelFrequencies;
  }
}
