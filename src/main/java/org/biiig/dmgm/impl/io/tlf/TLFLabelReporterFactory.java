package org.biiig.dmgm.impl.io.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

public class TLFLabelReporterFactory implements TLFSplitReaderFactory {
  protected final List<Countable<String>> globalVertexLabelFrequencies = Lists.newCopyOnWriteArrayList();
  protected final List<Countable<String>> globalEdgeLabelFrequencies = Lists.newCopyOnWriteArrayList();

  @Override
  public Runnable create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFLabelReporter(
      splits, reachedEOF, globalVertexLabelFrequencies, globalEdgeLabelFrequencies) {
    };
  }

  public List<Countable<String>> getVertexLabelFrequencies() {
    return globalVertexLabelFrequencies;
  }

  public List<Countable<String>> getEdgeLabelFrequencies() {
    return globalEdgeLabelFrequencies;
  }
}
