package org.biiig.dmgm.todo.mining;

import com.google.common.collect.Maps;
import org.biiig.dmgm.todo.gspan.DFSCode;
import org.biiig.dmgm.todo.model.countable.Countable;
import org.biiig.dmgm.todo.model.countable.CountableSupportComparator;

import java.util.List;
import java.util.Map;

/**
 * Created by peet on 13.07.17.
 */
public class GSpanBase {
  protected static final int IS_A_EDGE_LABEL = Integer.MAX_VALUE;

  protected final Float minSupportThreshold;
  protected final Map<String, Integer> vertexDictionary = Maps.newHashMap();
  protected final Map<String, Integer> edgeDictionary = Maps.newHashMap();
  protected final Map<Integer, String> reverseVertexDictionary = Maps.newHashMap();
  protected final Map<Integer, String> reverseEdgeDictionary = Maps.newHashMap();
  protected int graphCount = 0;
  protected int minSupport;
  protected final int kMax;

  public GSpanBase(Float minSupportThreshold, int kMax) {
    this.minSupportThreshold = minSupportThreshold;
    this.kMax = kMax;
  }

  protected String format(DFSCode dfsCode) {
    StringBuilder builder = new StringBuilder();

    reverseEdgeDictionary.put(IS_A_EDGE_LABEL, "isA");

    for (int i = 0; i < dfsCode.size(); i++) {
      builder
        .append(dfsCode.getFromTime(i)).append(":")
        .append(reverseVertexDictionary.get(dfsCode.getFromLabel(i)))
        .append(dfsCode.isOutgoing(i) ? "-" : "<-")
        .append(reverseEdgeDictionary.get(dfsCode.getEdgeLabel(i)))
        .append(dfsCode.isOutgoing(i) ? "->" : "-")
        .append(dfsCode.getToTime(i)).append(":")
        .append(reverseVertexDictionary.get(dfsCode.getToLabel(i))).append(" ");
    }
    return builder.toString();
  }

  protected void createDictionaries(List<Countable<String>> vertexLabels,
    List<Countable<String>> edgeLabels) {
    Countable.aggregateSupport(vertexLabels);
    vertexLabels.sort(new CountableSupportComparator<>());
    int translation = 1;
    for (Countable<String> countable : vertexLabels) {
      if (countable.getSupport() >= minSupport) {
        vertexDictionary.put(countable.getObject(), translation);
        reverseVertexDictionary.put(translation, countable.getObject());
        translation++;
      }
    }

    Countable.aggregateSupport(edgeLabels);
    edgeLabels.sort(new CountableSupportComparator<>());
    translation = 1;
    for (Countable<String> countable : edgeLabels) {
      if (countable.getSupport() >= minSupport) {
        edgeDictionary.put(countable.getObject(), translation);
        reverseEdgeDictionary.put(translation, countable.getObject());
        translation++;
      }
    }
  }


  public Map<Integer, String> getReverseVertexDictionary() {
    return reverseVertexDictionary;
  }

  public Map<Integer, String> getReverseEdgeDictionary() {
    return reverseEdgeDictionary;
  }
}
