package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public class TLFEdgeLabelReporterFactory extends TLFLabelReporterFactory {

  public TLFEdgeLabelReporterFactory(List<Countable<String>> globalLabelFrequencies) {
    super(globalLabelFrequencies);
  }

  @Override
  public Runnable create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFEdgeLabelReporter(splits, reachedEOF, globalLabelFrequencies);
  }
}
