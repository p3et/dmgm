package org.biiig.dmgm.impl.io.tlf;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Iterator;
import java.util.Queue;
import java.util.stream.Stream;

class TLFFileSplitter  {
  private final Queue<String[]> splits;
  private final Stream<String> stream;

  public TLFFileSplitter(Stream<String> stream, Queue<String[]> splits) {
    this.splits = splits;
    this.stream = stream;
  }

  public void invoke() {
    String[] graphLines = new String[0];

    Iterator<String> iterator = stream.iterator();
    while (iterator.hasNext()) {
      String line = iterator.next();

      if (line.startsWith(TLFConstants.GRAPH_SYMBOL)) {
        if (graphLines.length > 0) {
          splits.add(graphLines);
        }
        graphLines = new String[] {line};
      } else {
        graphLines = ArrayUtils.add(graphLines, line);
      }
    }
    if (graphLines.length > 0) {
      splits.add(graphLines);
    }
  }
}
