package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public class TLFVertexLabelReporterFactory extends TLFLabelReporterFactory {
  public TLFVertexLabelReporterFactory(List<Countable<String>> globalLabelFrequencies) {
    super(globalLabelFrequencies);
  }

  @Override
  public Runnable create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFVertexLabelReporter(splits, reachedEOF, globalLabelFrequencies);
  }
}
