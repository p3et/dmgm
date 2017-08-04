package org.biiig.dmgm.impl.io.tlf;

import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public class TLFVertexLabelReporterFactory extends TLFLabelReporterFactory {

  @Override
  public Runnable create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFVertexLabelReporter(splits, reachedEOF, globalFrequencies);
  }
}
