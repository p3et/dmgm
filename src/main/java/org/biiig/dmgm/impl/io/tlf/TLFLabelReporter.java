package org.biiig.dmgm.impl.io.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public abstract class TLFLabelReporter extends TLFSplitReader {
  protected final List<Countable<String>> globalFrequencies;
  protected final List<Countable<String>> partitionFrequencies = Lists.newLinkedList();
  protected final List<Countable<String>> graphFrequencies = Lists.newLinkedList();

  public TLFLabelReporter(Queue<String[]> splits, boolean reachedEOF,
    List<Countable<String>> globalFrequencies) {
    super(splits, reachedEOF);
    this.globalFrequencies = globalFrequencies;
  }

  @Override
  protected void finish() {
    globalFrequencies.addAll(partitionFrequencies);
  }
}
