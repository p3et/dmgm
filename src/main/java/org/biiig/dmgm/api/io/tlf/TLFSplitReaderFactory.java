package org.biiig.dmgm.api.io.tlf;

import org.biiig.dmgm.impl.io.tlf.TLFSplitReader;

import java.util.Queue;

public interface TLFSplitReaderFactory {
  TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF);
}
