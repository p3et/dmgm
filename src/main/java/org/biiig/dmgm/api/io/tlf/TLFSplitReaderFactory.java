package org.biiig.dmgm.api.io.tlf;

import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public interface TLFSplitReaderFactory {
  Runnable create(Queue<String[]> splits, Boolean reachedEOF);
}
