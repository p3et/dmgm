package org.biiig.dmgm.api.io.tlf;

import org.biiig.dmgm.impl.io.tlf.TLFSplitReader;

import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public interface TLFSplitReaderFactory {
  TLFSplitReader create(Queue<String[]> splits, Boolean reachedEOF);
}
