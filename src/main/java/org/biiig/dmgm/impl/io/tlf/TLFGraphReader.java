package org.biiig.dmgm.impl.io.tlf;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.graph.DirectedGraph;
import org.biiig.dmgm.api.model.graph.DirectedGraphFactory;
import org.biiig.dmgm.impl.db.LabelDictionary;

import java.util.Map;
import java.util.Queue;

public class TLFGraphReader extends TLFSplitReader {
  private final Database database;
  private final DirectedGraphFactory graphFactory;
  private final Map<String, Integer> vertexIdMap = Maps.newHashMap();

  public TLFGraphReader(Queue<String[]> splits, boolean reachedEOF,
    DirectedGraphFactory graphFactory, Database database) {
    super(splits, reachedEOF);
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  protected void process(String[] split) {
    vertexIdMap.clear();

    int firstEdgeIndex = getFirstEdgeIndex(split);
    int edgeCount = split.length - firstEdgeIndex;
    int vertexCount = split.length - edgeCount - 1;

    DirectedGraph graph = graphFactory.create(vertexCount, edgeCount);
    readVertices(split, firstEdgeIndex, graph);
    readEdges(split, firstEdgeIndex, graph);

    graph.trim();

    database.store(graph);
  }

  private int getFirstEdgeIndex(String[] split) {
    int firstEdgeIndex = -1;
    for (int l = 1; l < split.length; l++) {
      if (split[l].startsWith(TLFConstants.EDGE_SYMBOL)) {
        firstEdgeIndex = l;
        break;
      }
    }
    return firstEdgeIndex;
  }

  private void readVertices(String[] split, int firstEdgeIndex, DirectedGraph graph) {
    LabelDictionary dictionary = database.getVertexDictionary();
    int vertexId = 0;

    for (int l = 1; l < firstEdgeIndex; l++) {
      String[] fields = split[l].split(TLFConstants.FIELD_SEPARATOR);

      // if vertex has frequent label
      Integer firstLabel = dictionary.translate(fields[2]);
      if (firstLabel != null) {

        // multiple labels
        if (fields.length > 3) {
          int labelCount = fields.length - 2;
          int[] labels = new int[labelCount];

          labels[0] = firstLabel;

          for (int f = 3; f < fields.length; f++) {
            Integer label = dictionary.translate(fields[f]);
            if (label == null) {
              label = -1;
            }
            labels[f - 2] = label;
          }

          graph.setVertex(vertexId, labels);

          // single label
        } else {
          graph.setVertex(vertexId, firstLabel);
        }

        vertexIdMap.put(fields[1], vertexId);
        vertexId++;
      }
    }
  }

  private void readEdges(String[] split, int firstEdgeIndex, DirectedGraph graph) {
    LabelDictionary dictionary = database.getEdgeDictionary();
    int edgeId = 0;

    for (int l = firstEdgeIndex; l < split.length; l++) {
      String[] fields = split[l].split(TLFConstants.FIELD_SEPARATOR);

      // if source has frequent label
      Integer sourceId = vertexIdMap.get(fields[1]);
      if (sourceId != null) {

        // if target has frequent label
        Integer targetId = vertexIdMap.get(fields[2]);
        if (targetId != null) {

          // if edge has frequent label
          Integer firstLabel = dictionary.translate(fields[3]);
          if (firstLabel != null) {

            // single label
            if (fields.length == 4) {
              graph.setEdge(edgeId, sourceId, targetId, firstLabel);

              // multiple labels
            } else if (fields.length > 4) {
              int labelCount = fields.length - 3;
              int[] labels = new int[labelCount];

              labels[0] = firstLabel;

              for (int f = 4; f < fields.length; f++) {
                Integer label = dictionary.translate(fields[f]);
                if (label == null) {
                  label = -1;
                }
                labels[f - 3] = label;
              }

              graph.setEdge(edgeId, sourceId, targetId, labels);
            }

            edgeId++;
          }
        }
      }
    }
  }
}
