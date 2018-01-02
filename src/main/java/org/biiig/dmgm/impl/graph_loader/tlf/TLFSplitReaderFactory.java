package org.biiig.dmgm.impl.graph_loader.tlf;

import java.util.Queue;

public interface TLFSplitReaderFactory {
  TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF);
}
