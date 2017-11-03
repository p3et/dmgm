package org.biiig.dmgm.todo.mining;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.algorithms.tfsm.TFSMConfig;
import org.biiig.dmgm.impl.model.graph.DFSCode;
import org.biiig.dmgm.impl.algorithms.tfsm.model.DFSEmbedding;
import org.biiig.dmgm.todo.gspan.GenSpanTreeNode;
import org.biiig.dmgm.impl.algorithms.tfsm.model.GraphIdEmbeddingPair;
import org.biiig.dmgm.todo.model.Vector;
import org.biiig.dmgm.impl.model.countable.Countable;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledAdjacencyListEntry;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledEdge;
import org.biiig.dmgm.todo.model.multilevel_graph.MultiLevelGraph;
import org.biiig.dmgm.todo.model.multilevel_graph.MultiLevelVertex;
import org.biiig.dmgm.todo.vector_mining.CrossLevelFrequentVectors;
import org.biiig.dmgm.todo.vector_mining.CrossLevelFrequentVectorsBottomUp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Directed Multigraph gSpan
 */
public class GenSpan extends GSpanBase {

  private final List<MultiLevelGraph> graphs = Lists.newArrayList();
  private final Deque<GenSpanTreeNode> parents = Lists.newLinkedList();
  private final List<GenSpanTreeNode> reports = Lists.newArrayList();
  private final List<GenSpanTreeNode> children = Lists.newLinkedList();
  private final List<Countable<DFSCode>> result = Lists.newArrayList();
  private final CrossLevelFrequentVectors gfvm = new CrossLevelFrequentVectorsBottomUp();

  public GenSpan(TFSMConfig config) {
    super(config);
  }


  public void mine() throws IOException {
    this.minSupport = Math.round((float) graphCount * minSupportThreshold);

    initSingleEdgePatterns();

    while (! parents.isEmpty()) {
      growForNextParent();
    }
  }

  private void growForNextParent() {
    children.clear();
    GenSpanTreeNode parent = parents.pollLast();

    DFSCode dfsCode = parent.getDfsCode();

    int[] rightmostPathTimes = getRightmostPathTimes(dfsCode);

    for (GraphIdEmbeddingPair graphEmbeddings : parent.getEmbeddings()) {

      growForGraph(
        graphs.get(graphEmbeddings.getGraphId()), graphEmbeddings, dfsCode, rightmostPathTimes);
      GenSpanTreeNode.aggregateForGraph(reports);
      children.addAll(reports);
    }

    countAndPrune();
  }

  private void countAndPrune() {

    GenSpanTreeNode.aggregate(children);

    for (GenSpanTreeNode node : children) {
      int support = node.getSupport();
      if (support >= minSupport) {
        int size = node.getDfsCode().getEdgeCount();
        if (size == 1 || size <= kMax && isMinimal(node.getDfsCode())) {
          if (node.getVectors().size() > 1) {
            for (Countable<Vector> countable : gfvm.mine(node.getVectors(), minSupport)) {
              DFSCode oCode = node.getDfsCode();
              DFSCode genCode = oCode.deepCopy();

              int[] fieldMapping = node.getFieldMapping();
              Vector vector = countable.getObject();

              for (int field = 0; field < fieldMapping.length; field++) {
                int vertexTime = fieldMapping[field];
                int[] levels = vector.getField(field);

                int level = levels.length - 1;
                int label = levels[level];

                while (label == 0 && level > 0) {
                  level --;
                  label = levels[level];
                }


                if (label > 0) {
                  genCode.setVertexLabel(vertexTime, label);
                }
              }

              result.add(
                new Countable<>(genCode, countable.getSupport(), countable.getFrequency()));
            }
          } else {
            Countable<Vector> countable = node.getVectors().get(0);

            result.add(
              new Countable<>(node.getDfsCode(), countable.getSupport(), countable.getFrequency()));
          }

          if (size < kMax) {
            parents.add(node);
          }
        }
      }
    }
  }

  private boolean isMinimal(DFSCode dfsCode) {

    MultiLevelGraph graph = new MultiLevelGraph(dfsCode);
    graph.createAdjacencyList();
    reportSingleEdges(graph);

    GenSpanTreeNode minParentNode = reports.get(0);
    boolean minimal = minParentNode.getDfsCode().parentOf(dfsCode);

    while (minimal) {
      DFSCode minDFSCode = minParentNode.getDfsCode();


      GraphIdEmbeddingPair[] embeddings = minParentNode.getEmbeddings();
      int[] rightmostPath = getRightmostPathTimes(minDFSCode);

      growForGraph(graph, embeddings[0], minDFSCode, rightmostPath);

      GenSpanTreeNode.aggregateForGraph(reports);

      if (reports.isEmpty()) {
        break;
      } else {
        minParentNode = reports.get(0);
        minimal = minParentNode.getDfsCode().parentOf(dfsCode);
      }
    }

    return minimal;
  }

  private void initSingleEdgePatterns() {
    children.clear();


    for (MultiLevelGraph graph : graphs) {
      reportSingleEdges(graph);
      children.addAll(reports);
    }

    countAndPrune();
  }

  private void reportSingleEdges(MultiLevelGraph graph) {
    reports.clear();
    int fromTime = 0;


    graph.createAdjacencyList();

    int fromId = 0;
    for (LabeledAdjacencyListEntry[] row : graph.getAdjacencyList()) {
      int fromLabel = graph.getVertices()[fromId].getTopLevelLabel();

      for (LabeledAdjacencyListEntry entry : row) {
        int edgeId = entry.getEdgeId();
        int toLabel = entry.getToLabel();
        boolean outgoing = entry.isOutgoing();

        if (fromLabel < toLabel || fromLabel == toLabel && outgoing) {

          int toTime = entry.isLoop() ? 0 : 1;
          int edgeLabel = entry.getEdgeLabel();
          int toId = entry.getToId();

          DFSCode dfsCode = new DFSCode(
            fromTime, toTime, fromLabel, outgoing, edgeLabel, toLabel);

          DFSEmbedding embedding = new DFSEmbedding(fromId, edgeId, toId);

          GraphIdEmbeddingPair embeddings = new GraphIdEmbeddingPair(graph.getId(), null);

          Vector vector = new Vector();
          int[] fieldMapping = new int[0];
          
          int vertexTime = 0;
          for (int vertexId : embedding.getVertexIds()) {
            int[] lowerLevelLabels = graph.getVertices()[vertexId].getLowerLevelLabels();
            
            if (lowerLevelLabels != null) {
              vector.addField(lowerLevelLabels);
              fieldMapping = ArrayUtils.add(fieldMapping, vertexTime);
              vertexTime++;
            }
          }

          reports.add(new GenSpanTreeNode(dfsCode, fieldMapping, vector, embeddings));
        }
      }
      fromId++;
    }
    GenSpanTreeNode.aggregateForGraph(reports);
  }

  private void createDictionaries(String input) throws IOException {
    Stream<String> stream = Files.lines(Paths.get(input));
    Iterator<String> iterator = stream.iterator();

    final List<Countable<String>> vertexLabels = Lists.newArrayList();
    final List<Countable<String>> edgeLabels = Lists.newArrayList();

    final List<Countable<String>> graphVertexLabels = Lists.newArrayList();
    final List<Countable<String>> graphEdgeLabels = Lists.newArrayList();

    while (iterator.hasNext()) {
      String line = iterator.next();
      String[] fields = line.split(" ");

      if (fields[0].equals("t")) {
        graphCount++;

        Countable.sumFrequency(graphVertexLabels);
        vertexLabels.addAll(graphVertexLabels);
        graphVertexLabels.clear();

        Countable.sumFrequency(graphEdgeLabels);
        edgeLabels.addAll(graphEdgeLabels);
        graphEdgeLabels.clear();

      } else if (fields[0].equals("e")) {
        graphEdgeLabels.add(new Countable<>(fields[3]));
      } else {
        for (int i = 2; i < fields.length; i++) {
          graphVertexLabels.add(new Countable<>(fields[i]));
        }
      }
    }

    Countable.sumFrequency(graphVertexLabels);
    vertexLabels.addAll(graphVertexLabels);

    Countable.sumFrequency(graphEdgeLabels);
    edgeLabels.addAll(graphEdgeLabels);

    createDictionaries(vertexLabels, edgeLabels);
  }

  private void readGraphs(String input) throws IOException {
    Stream<String> stream = Files.lines(Paths.get(input));
    Iterator<String> iterator = stream.iterator();

    MultiLevelGraph graph = new MultiLevelGraph(-1);
    Map<Integer, Integer> vertexIdMap = Maps.newHashMap();

    while (iterator.hasNext()) {
      String line = iterator.next();
      String[] fields = line.split(" ");

      if (fields[0].equals("t")) {
        graph = new MultiLevelGraph(graphs.size());
        graphs.add(graph);
        vertexIdMap.clear();
      } else if (fields[0].equals("e")) {
        Integer label = edgeDictionary.get(fields[3]);

        if (label != null) {
          Integer source = vertexIdMap.get(Integer.valueOf(fields[1]));

          if (source != null) {
            Integer target = vertexIdMap.get(Integer.valueOf(fields[2]));

            if (target != null) {
              int id = graph.getEdgeCount();
              LabeledEdge edge = new LabeledEdge(id, source, label, target);
              graph.add(edge);
            }
          }
        }
      } else {
        int sourceId = Integer.valueOf(fields[1]);
        Integer topLevelLabel = vertexDictionary.get(fields[2]);

        if (topLevelLabel != null) {
          int targetId = vertexIdMap.size();
          vertexIdMap.put(sourceId, targetId);

          MultiLevelVertex vertex;

          if (fields.length > 3) {
            int[] lowerLevelLabels = new int[fields.length - 3];

            for (int i = 3; i < fields.length; i++) {
              lowerLevelLabels[i - 3] = vertexDictionary.get(fields[i]);
            }

            vertex = new MultiLevelVertex(targetId, topLevelLabel, lowerLevelLabels);
          } else {
            vertex = new MultiLevelVertex(targetId, topLevelLabel);
          }

          graph.add(vertex);
        }
      }
    }
  }

  public int[] getRightmostPathTimes(DFSCode dfsCode) {

    int[] rightmostPathTimes;

    // 1-edge pattern
    if (dfsCode.getEdgeCount() == 1) {
      // loop
      if (dfsCode.getFromTime(0) == dfsCode.getToTime( 0)) {
        rightmostPathTimes = new int[] {0};
      } else {
        rightmostPathTimes = new int[] {1, 0};
      }
    } else {
      rightmostPathTimes = new int[0];

      for (int edgeTime = dfsCode.getEdgeCount() - 1; edgeTime >= 0; edgeTime--) {
        int fromTime = dfsCode.getFromTime(edgeTime);
        int toTime = dfsCode.getToTime(edgeTime);
        boolean firstStep = rightmostPathTimes.length == 0;

        // forwards
        if (toTime > fromTime) {

          // first step, add both times
          if (firstStep) {
            rightmostPathTimes = ArrayUtils.add(rightmostPathTimes, toTime);
            rightmostPathTimes = ArrayUtils.add(rightmostPathTimes, fromTime);

            // add from time
          } else if (ArrayUtils.indexOf(rightmostPathTimes, toTime) >= 0) {
            rightmostPathTimes = ArrayUtils.add(rightmostPathTimes, fromTime);
          }

          // first step and loop
        } else if (firstStep && fromTime == toTime) {
          rightmostPathTimes = ArrayUtils.add(rightmostPathTimes, 0);
        }
      }
    }

    return rightmostPathTimes;
  }

  private void growForGraph(MultiLevelGraph graph, GraphIdEmbeddingPair graphEmbeddings, DFSCode parentCode,
    int[] rightmostPath) {
    reports.clear();

    Map<DFSEmbedding, DFSCode> map = Maps.newHashMap();

    DFSEmbedding[] embeddings = graphEmbeddings.getEmbeddings();

    for (DFSEmbedding parentEmbedding : embeddings) {
      boolean rightmost = true;
      for (int fromTime : rightmostPath) {
        int fromId = parentEmbedding.getVertexId(fromTime);

        for (LabeledAdjacencyListEntry entry : graph.getAdjacencyList()[fromId]) {
          // if not contained in parent embedding
          int edgeId = entry.getEdgeId();
          if (! parentEmbedding.containsEdgeId(edgeId)) {

            // determine times of incident vertices in parent embedding
            int toId = entry.getToId();
            int toTime = parentEmbedding.getVertexTime(toId);

            // CHECK FOR BACKWARDS GROWTH OPTIONS

            // grow backwards
            if (rightmost && toTime >= 0) {
              DFSCode childCode = parentCode.growChild(
                fromTime,
                toTime,
                entry.isOutgoing(),
                entry.getEdgeLabel(),
                graph.getVertices()[fromId].getTopLevelLabel()
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

              GraphIdEmbeddingPair
                childEmbeddings = new GraphIdEmbeddingPair(graph.getId(), null);

              Vector vector = new Vector();
              int[] fieldMapping = new int[0];

              int vertexTime = 0;
              for (int vertexId : childEmbedding.getVertexIds()) {
                int[] lowerLevelLabels = graph.getVertices()[vertexId].getLowerLevelLabels();

                if (lowerLevelLabels != null) {
                  vector.addField(lowerLevelLabels);
                  fieldMapping = ArrayUtils.add(fieldMapping, vertexTime);
                  vertexTime++;
                }
              }

              reports.add(new GenSpanTreeNode(childCode, fieldMapping, vector, childEmbeddings));

              // grow backwards from to
            } else if (toTime < 0) {
              DFSCode childCode = parentCode.growChild(
                fromTime,
                toTime,
                entry.isOutgoing(),
                entry.getEdgeLabel(),
                graph.getVertices()[toId].getTopLevelLabel()
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

              GraphIdEmbeddingPair
                childEmbeddings = new GraphIdEmbeddingPair(graph.getId(), null);

              Vector vector = new Vector();
              int[] fieldMapping = new int[0];

              int vertexTime = 0;
              for (int vertexId : childEmbedding.getVertexIds()) {
                int[] lowerLevelLabels = graph.getVertices()[vertexId].getLowerLevelLabels();

                if (lowerLevelLabels != null) {
                  vector.addField(lowerLevelLabels);
                  fieldMapping = ArrayUtils.add(fieldMapping, vertexTime);
                  vertexTime++;
                }
              }

              reports.add(new GenSpanTreeNode(childCode, fieldMapping, vector, childEmbeddings));
            }
          }
        }

        rightmost = false;
      }
    }
  }

  public List<Countable<DFSCode>> getResult() {
    return result;
  }

  private DFSCode getMinDFSCode(MultiLevelGraph graph) {
    graph.createAdjacencyList();
    reportSingleEdges(graph);

    GenSpanTreeNode minParentNode = reports.get(0);

    for (int i = 1; i < graph.getEdgeCount(); i++) {
      DFSCode minDFSCode = minParentNode.getDfsCode();

      GraphIdEmbeddingPair[] embeddings = minParentNode.getEmbeddings();
      int[] rightmostPath = getRightmostPathTimes(minDFSCode);

      growForGraph(graph, embeddings[0], minDFSCode, rightmostPath);

      minParentNode = reports.get(0);
    }

    return minParentNode.getDfsCode();
  }

  public List<MultiLevelGraph> getGraphs() {
    return graphs;
  }

  public void printResult() {
//    result.forEach(r -> System.out.println(r.getObject()));
//    result.forEach(r -> System.out.println(format(r.getObject())));
  }
}
