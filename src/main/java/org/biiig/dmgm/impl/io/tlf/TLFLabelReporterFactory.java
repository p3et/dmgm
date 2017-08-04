package org.biiig.dmgm.impl.io.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;

/**
 * Created by peet on 04.08.17.
 */
public abstract class TLFLabelReporterFactory implements TLFSplitReaderFactory {
  protected final List<Countable<String>> globalFrequencies = Lists.newCopyOnWriteArrayList();

  public List<Countable<String>> getGlobalFrequencies() {
    return globalFrequencies;
  }
}
