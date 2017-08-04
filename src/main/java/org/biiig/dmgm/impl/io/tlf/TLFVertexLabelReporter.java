package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

public class TLFVertexLabelReporter extends TLFLabelReporter {

  TLFVertexLabelReporter(
    Queue<String[]> splits, boolean reachedEOF, List<Countable<String>> globalFrequencies) {
    super(splits, reachedEOF, globalFrequencies);
  }

  @Override
  protected void process(String[] split) {
    for (int i = 1; i < split.length; i++) {
      String line = split[i];

      if (line.startsWith(TLFConstants.EDGE_SYMBOL)) {
        break;
      } else {
        String[] fields = line.split(TLFConstants.FIELD_SEPARATOR);

        for (int j = 2; j < fields.length; j++) {
          graphFrequencies.add(new Countable<>(fields[j]));
        }
      }
    }

    Countable.sumFrequency(graphFrequencies);
    partitionFrequencies.addAll(graphFrequencies);
    graphFrequencies.clear();
  }

}
