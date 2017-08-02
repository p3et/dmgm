package org.biiig.dmgm.todo.mining;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.todo.gspan.DFSCode;
import org.biiig.dmgm.todo.gspan.GSpanTreeNode;
import org.biiig.dmgm.todo.model.countable.Countable;
import org.biiig.dmgm.todo.gspan.DFSEmbedding;
import org.biiig.dmgm.todo.gspan.GraphDFSEmbeddings;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledAdjacencyListEntry;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledEdge;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledGraph;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledVertex;

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
public class DircetedMulitgraphGSpan extends GSpanBase {
  private final String inputPath;

  private final List<LabeledGraph> graphs = Lists.newArrayList();
  private final Deque<GSpanTreeNode> parents = Lists.newLinkedList();
  private final List<GSpanTreeNode> reports = Lists.newArrayList();
  private final List<GSpanTreeNode> children = Lists.newLinkedList();
  private final List<Countable<DFSCode>> result = Lists.newArrayList();


  public DircetedMulitgraphGSpan(String inputPath, Float minSupportThreshold, int kMax) {
    super(minSupportThreshold, kMax);
    this.inputPath = inputPath;
  }

  public void mine() throws IOException {
    createDictionaries(inputPath);
    readGraphs(inputPath);

    this.minSupport = Math.round((float) graphCount * minSupportThreshold);

    initSingleEdgePatterns();

    while (! parents.isEmpty()) {
      growForNextParent();
    }
  }

  private void growForNextParent() {
    children.clear();
    GSpanTreeNode parent = parents.pollLast();

    DFSCode dfsCode = parent.getDfsCode();
    int support = 0;
    int frequency = 0;

    int[] rightmostPathTimes = getRightmostPathTimes(dfsCode);

    for (GraphDFSEmbeddings graphEmbeddings : parent.getEmbeddings()) {
      support++;
      frequency += graphEmbeddings.getEmbeddings().length;

      growForGraph(
        graphs.get(graphEmbeddings.getGraphId()), graphEmbeddings, dfsCode, rightmostPathTimes);
      GSpanTreeNode.aggregateForGraph(reports);
      children.addAll(reports);
    }
    countAndPrune();
  }

  private void countAndPrune() {
    GSpanTreeNode.aggregate(children);

    for (GSpanTreeNode node : children) {
      int support = node.getSupport();
      if (support >= minSupport) {
        int size = node.getDfsCode().size();
        if (size == 1 || isMinimal(node.getDfsCode())) {

          if (size < kMax ) {
            parents.add(node);
          }

          result.add(new Countable<>(node.getDfsCode(), node.getSupport(), node.getFrequency()));
        }
      }
    }
  }

  private boolean isMinimal(DFSCode dfsCode) {

    LabeledGraph graph = new LabeledGraph(dfsCode);
    graph.createAdjacencyList();
    reportSingleEdges(graph);

    GSpanTreeNode minParentNode = reports.get(0);
    boolean minimal = minParentNode.getDfsCode().parentOf(dfsCode);

    while (minimal) {
      DFSCode minDFSCode = minParentNode.getDfsCode();


      GraphDFSEmbeddings[] embeddings = minParentNode.getEmbeddings();
      int[] rightmostPath = getRightmostPathTimes(minDFSCode);

      growForGraph(graph, embeddings[0], minDFSCode, rightmostPath);

      GSpanTreeNode.aggregateForGraph(reports);

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


    for (LabeledGraph graph : graphs) {
      reportSingleEdges(graph);
      children.addAll(reports);
    }

    countAndPrune();
  }

  private void reportSingleEdges(LabeledGraph graph) {
    reports.clear();
    int fromTime = 0;


    graph.createAdjacencyList();

    int fromId = 0;
    for (LabeledAdjacencyListEntry[] row : graph.getAdjacencyList()) {
      int fromLabel = graph.getVertices()[fromId].getLabel();

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

          GraphDFSEmbeddings embeddings = new GraphDFSEmbeddings(graph.getId(), embedding);

          reports.add(new GSpanTreeNode(dfsCode, embeddings));
        }
      }
      fromId++;
    }
    GSpanTreeNode.aggregateForGraph(reports);
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

        Countable.aggregateFrequency(graphVertexLabels);
        vertexLabels.addAll(graphVertexLabels);
        graphVertexLabels.clear();

        Countable.aggregateFrequency(graphEdgeLabels);
        edgeLabels.addAll(graphEdgeLabels);
        graphEdgeLabels.clear();

      } else if (fields[0].equals("e")) {
        graphEdgeLabels.add(new Countable<>(fields[3]));
      } else {
        graphVertexLabels.add(new Countable<>(fields[2]));
      }
    }

    Countable.aggregateFrequency(graphVertexLabels);
    vertexLabels.addAll(graphVertexLabels);

    Countable.aggregateFrequency(graphEdgeLabels);
    edgeLabels.addAll(graphEdgeLabels);

    createDictionaries(vertexLabels, edgeLabels);
  }

  private void readGraphs(String input) throws IOException {
    Stream<String> stream = Files.lines(Paths.get(input));
    Iterator<String> iterator = stream.iterator();

    LabeledGraph graph = new LabeledGraph(-1);
    Map<Integer, Integer> vertexIdMap = Maps.newHashMap();

    while (iterator.hasNext()) {
      String line = iterator.next();
      String[] fields = line.split(" ");

      if (fields[0].equals("t")) {
        graph = new LabeledGraph(graphs.size());
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
        Integer label = vertexDictionary.get(fields[2]);

        if (label != null) {
          int targetId = vertexIdMap.size();
          vertexIdMap.put(sourceId, targetId);

          LabeledVertex vertex = new LabeledVertex(targetId, label);
          graph.add(vertex);
        }
      }
    }
  }

  public int[] getRightmostPathTimes(DFSCode dfsCode) {

    int[] rightmostPathTimes;

    // 1-edge pattern
    if (dfsCode.size() == 1) {
      // loop
      if (dfsCode.getFromTime(0) == dfsCode.getToTime( 0)) {
        rightmostPathTimes = new int[] {0};
      } else {
        rightmostPathTimes = new int[] {1, 0};
      }
    } else {
      rightmostPathTimes = new int[0];

      for (int edgeTime = dfsCode.size() - 1; edgeTime >= 0; edgeTime--) {
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

  private void growForGraph(LabeledGraph graph, GraphDFSEmbeddings graphEmbeddings, DFSCode parentCode,
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
              DFSCode childCode = parentCode.deepCopy();

              childCode.grow(
                fromTime,
                toTime,
                graph.getVertices()[fromId].getLabel(),
                entry.isOutgoing(),
                entry.getEdgeLabel(),
                graph.getVertices()[toId].getLabel()
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeId(edgeId);

              GraphDFSEmbeddings
                childEmbeddings = new GraphDFSEmbeddings(graph.getId(), childEmbedding);

              GSpanTreeNode childNode = new GSpanTreeNode(childCode, childEmbeddings);
              reports.add(childNode);

              // grow backwards from to
            } else if (toTime < 0) {
              DFSCode childCode = parentCode.deepCopy();
              toTime = parentEmbedding.getVertexCount();

              childCode.grow(
                fromTime,
                toTime,
                graph.getVertices()[fromId].getLabel(),
                entry.isOutgoing(),
                entry.getEdgeLabel(),
                graph.getVertices()[toId].getLabel()
              );

              DFSEmbedding childEmbedding = parentEmbedding.expandByEdgeIdAndVertexId(edgeId, toId);

              GraphDFSEmbeddings
                childEmbeddings = new GraphDFSEmbeddings(graph.getId(), childEmbedding);

              GSpanTreeNode childNode = new GSpanTreeNode(childCode, childEmbeddings);
              reports.add(childNode);
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

  private DFSCode getMinDFSCode(LabeledGraph graph) {
    graph.createAdjacencyList();
    reportSingleEdges(graph);

    GSpanTreeNode minParentNode = reports.get(0);

    for (int i = 1; i < graph.getEdgeCount(); i++) {
      DFSCode minDFSCode = minParentNode.getDfsCode();

      GraphDFSEmbeddings[] embeddings = minParentNode.getEmbeddings();
      int[] rightmostPath = getRightmostPathTimes(minDFSCode);

      growForGraph(graph, embeddings[0], minDFSCode, rightmostPath);

      minParentNode = reports.get(0);
    }

    return minParentNode.getDfsCode();
  }
}
