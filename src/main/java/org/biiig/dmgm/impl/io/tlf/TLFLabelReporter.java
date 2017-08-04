package org.biiig.dmgm.impl.io.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.util.List;
import java.util.Queue;

/**
 * Created by peet on 04.08.17.
 */
public class TLFLabelReporter extends TLFSplitReader {
  protected final List<Countable<String>> globalVertexLabelFrequencies;
  protected final List<Countable<String>> partitionVertexLabelFrequencies = Lists.newLinkedList();
  protected final List<Countable<String>> graphVertexLabelFrequencies = Lists.newLinkedList();
  protected final List<Countable<String>> globalEdgeLabelFrequencies;
  protected final List<Countable<String>> partitionEdgeLabelFrequencies = Lists.newLinkedList();
  protected final List<Countable<String>> graphEdgeLabelFrequencies = Lists.newLinkedList();

  public TLFLabelReporter(Queue<String[]> splits, boolean reachedEOF,
    List<Countable<String>> globalVertexLabelFrequencies,
    List<Countable<String>> globalEdgeLabelFrequencies) {
    super(splits, reachedEOF);
    this.globalVertexLabelFrequencies = globalVertexLabelFrequencies;
    this.globalEdgeLabelFrequencies = globalEdgeLabelFrequencies;
  }

  @Override
  protected void process(String[] split) {
    readVertices(split);


    Countable.sumFrequency(graphVertexLabelFrequencies);
    partitionVertexLabelFrequencies.addAll(graphVertexLabelFrequencies);
    graphVertexLabelFrequencies.clear();

    Countable.sumFrequency(graphEdgeLabelFrequencies);
    partitionEdgeLabelFrequencies.addAll(graphEdgeLabelFrequencies);
    graphEdgeLabelFrequencies.clear();
  }

  private void readVertices(String[] split) {
    for (int l = 1; l < split.length; l++) {
      String line = split[l];

      if (line.startsWith(TLFConstants.EDGE_SYMBOL)) {
        readEdges(split, l);
        break;

      } else {
        String[] fields = line.split(TLFConstants.FIELD_SEPARATOR);

        for (int j = 2; j < fields.length; j++) {
          graphVertexLabelFrequencies.add(new Countable<>(fields[j]));
        }
      }
    }
  }

  private void readEdges(String[] split, int from) {
    for (int l = from; l < split.length; l++ ) {
      String line = split[l];
      String[] fields = line.split(TLFConstants.FIELD_SEPARATOR);

      for (int f = 3; f < fields.length; f++) {
        graphEdgeLabelFrequencies.add(new Countable<>(fields[f]));
      }
    }
  }

  @Override
  protected void finish() {
    globalVertexLabelFrequencies.addAll(partitionVertexLabelFrequencies);
    globalEdgeLabelFrequencies.addAll(partitionEdgeLabelFrequencies);
  }
}
