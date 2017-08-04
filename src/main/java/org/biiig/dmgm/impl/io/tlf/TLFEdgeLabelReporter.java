package org.biiig.dmgm.impl.io.tlf;

import org.biiig.dmgm.impl.db.LabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

public class TLFEdgeLabelReporter extends TLFLabelReporter {

  TLFEdgeLabelReporter(Queue<String[]> splits, boolean reachedEOF,
    List<Countable<String>> globalFrequencies, LabelDictionary vertexDictionary) {
    super(splits, reachedEOF, globalFrequencies);
  }

  @Override
  protected void process(String[] split) {
    boolean reachedEdges = false;

    for (String line : split) {
      if (reachedEdges) {
        String[] fields = line.split(TLFConstants.FIELD_SEPARATOR);

        for (int i = 3; i < fields.length; i++) {
          graphFrequencies.add(new Countable<>(fields[i]));
        }
      } else {
        reachedEdges = line.startsWith(TLFConstants.EDGE_SYMBOL);
      }
    }

    Countable.sumFrequency(graphFrequencies);
    partitionFrequencies.addAll(graphFrequencies);
    graphFrequencies.clear();
  }

}
