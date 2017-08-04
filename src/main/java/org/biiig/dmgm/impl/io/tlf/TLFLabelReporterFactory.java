package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;

/**
 * Created by peet on 04.08.17.
 */
public abstract class TLFLabelReporterFactory implements TLFSplitReaderFactory {
  protected final List<Countable<String>> globalLabelFrequencies;

  public TLFLabelReporterFactory(List<Countable<String>> globalLabelFrequencies) {
    this.globalLabelFrequencies = globalLabelFrequencies;
  }
}
