package org.biiig.dmgm.api.model.source.tlf;

import org.biiig.dmgm.impl.model.source.tlf.TLFSplitReader;

import java.util.Queue;

public interface TLFSplitReaderFactory {
  TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF);
}
