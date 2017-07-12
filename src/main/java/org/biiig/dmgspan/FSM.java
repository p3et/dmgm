package org.biiig.dmgspan;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.biiig.dmgspan.gspan.DFSCode;
import org.biiig.dmgspan.gspan.SearchTreeNode;
import org.biiig.dmgspan.gspan.GraphEmbeddings;
import org.biiig.dmgspan.model.AdjacencyListEntry;
import org.biiig.dmgspan.model.Countable;
import org.biiig.dmgspan.model.CountableSupportComparator;
import org.biiig.dmgspan.model.Edge;
import org.biiig.dmgspan.model.Graph;
import org.biiig.dmgspan.model.Vertex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

public class FSM {
  private int graphCount = 0;
  private int minSupport;

  private final List<Graph> graphs = Lists.newArrayList();
  private final Map<String, Integer> vertexDictionary = Maps.newHashMap();
  private final Map<String, Integer> edgeDictionary = Maps.newHashMap();
  private final Queue<SearchTreeNode> parents = Lists.newLinkedList();
  private final List<SearchTreeNode> children = Lists.newLinkedList();

  public FSM() {
  }

  public void mine(String input) throws IOException {
    createDictionaries(input);
    readGraphs(input);
    initSingleEdgePatterns();

    System.out.println(parents);
  }

  private void initSingleEdgePatterns() {
    List<SearchTreeNode> reports = Lists.newArrayList();
    children.clear();

    Set<Integer> processedEdges = Sets.newHashSet();
    int fromTime = 0;

    for (Graph graph : graphs) {
      reports.clear();
      processedEdges.clear();

      graph.createAdjacencyList();

      int fromId = 0;
      for (AdjacencyListEntry[] row : graph.getAjacencyList()) {
        int fromLabel = graph.getVertices()[fromId].getLabel();

        for (AdjacencyListEntry entry : row) {
          int edgeId = entry.getEdgeId();

          if (!processedEdges.contains(edgeId)) {
            processedEdges.add(edgeId);

            int toTime = entry.isLoop() ? 0 : 1;
            boolean outgoing = entry.isOutgoing();
            int edgeLabel = entry.getEdgeLabel();
            int toId = entry.getToId();
            int toLabel = entry.getToLabel();

            DFSCode dfsCode = new DFSCode(
              fromTime, toTime, fromLabel, outgoing, edgeLabel, toLabel);

            GraphEmbeddings embeddings = new GraphEmbeddings(graph.getId(), fromId, toId, edgeId);

            reports.add(new SearchTreeNode(dfsCode, embeddings));
          }
        }
        fromId++;
      }
      SearchTreeNode.aggregateForGraph(reports);
      children.addAll(reports);
    }

    SearchTreeNode.aggregate(children);
    children.removeIf(c -> c.getEmbeddings().length < minSupport);
    parents.addAll(children);
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
    this.minSupport = 9;

    Countable.aggregateFrequency(graphVertexLabels);
    vertexLabels.addAll(graphVertexLabels);

    Countable.aggregateFrequency(graphEdgeLabels);
    edgeLabels.addAll(graphEdgeLabels);

    Countable.aggregateSupport(vertexLabels);
    vertexLabels.sort(new CountableSupportComparator<>());
    int translation = 0;
    for (Countable<String> countable : vertexLabels) {
      if (countable.getSupport() >= minSupport) {
        vertexDictionary.put(countable.getObject(), translation);
        translation++;
      }
    }

    Countable.aggregateSupport(edgeLabels);
    edgeLabels.sort(new CountableSupportComparator<>());
    translation = 0;
    for (Countable<String> countable : edgeLabels) {
      if (countable.getSupport() >= minSupport) {
        edgeDictionary.put(countable.getObject(), translation);
        translation++;
      }
    }
  }

  private void readGraphs(String input) throws IOException {
    Stream<String> stream = Files.lines(Paths.get(input));
    Iterator<String> iterator = stream.iterator();

    Graph graph = new Graph(-1);
    Map<Integer, Integer> vertexIdMap = Maps.newHashMap();

    while (iterator.hasNext()) {
      String line = iterator.next();
      String[] fields = line.split(" ");

      if (fields[0].equals("t")) {
        graph = new Graph(graphs.size());
        graphs.add(graph);
        vertexIdMap.clear();
      } else if (fields[0].equals("e")) {
        Integer label = edgeDictionary.get(fields[3]);

        if (label != null) {
          Integer source = vertexIdMap.get(Integer.valueOf(fields[1]));

          if (source != null) {
            Integer target = vertexIdMap.get(Integer.valueOf(fields[2]));

            if (target != null) {
              int id = graph.getEdges().length;
              Edge edge = new Edge(id, source, label, target);
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

          Vertex vertex = new Vertex(targetId, label);
          graph.add(vertex);
        }
      }
    }
  }

}
