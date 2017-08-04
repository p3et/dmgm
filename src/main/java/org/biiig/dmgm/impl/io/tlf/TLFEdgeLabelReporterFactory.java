package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.impl.db.LabelDictionary;

import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public class TLFEdgeLabelReporterFactory extends TLFLabelReporterFactory {

  private final LabelDictionary vertexDictionary;

  public TLFEdgeLabelReporterFactory(LabelDictionary vertexDictionary) {
    this.vertexDictionary = vertexDictionary;
  }

  @Override
  public Runnable create(Queue<String[]> splits, Boolean reachedEOF) {
    return new TLFEdgeLabelReporter(splits, reachedEOF, globalFrequencies, vertexDictionary);
  }
}
