package org.biiig.dmgm.impl.graph_loader.tlf;

import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.api.SmallGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TLFSpliterator implements Spliterator<SmallGraph> {

  private final Iterator<String> iterator;
  private final LabelDictionary dictionary;
  private String line;
  private final GraphFactory graphFactory;


  public TLFSpliterator(String filePath, GraphFactory graphFactory, LabelDictionary dictionary) throws IOException {
    this.iterator = Files.lines(Paths.get(filePath)).iterator();
    this.graphFactory = graphFactory;
    this.dictionary = dictionary;
    if (iterator.hasNext())
      this.line = iterator.next();
  }

  @Override
  public void forEachRemaining(Consumer<? super SmallGraph> action) {
    while (iterator.hasNext())
      tryAdvance(action);
  }

  @Override
  public boolean tryAdvance(Consumer<? super SmallGraph> action) {
    boolean reachedEdges = false;
    boolean reachedNextGraph = false;

    SmallGraph graph = readGraph();

    while (iterator.hasNext() && !reachedNextGraph) {
      if (reachedEdges) {
        line = iterator.next();

        if (line.startsWith(TLFConstants.EDGE_SYMBOL)) {
          readEdge(graph);
        }
        else {
          reachedNextGraph = true;
        }

      } else {
        line = iterator.next();

        if (line.startsWith(TLFConstants.VERTEX_SYMBOL)) {
          readVertex(graph);
        }
        else if (line.startsWith(TLFConstants.EDGE_SYMBOL)) {
          readEdge(graph);
          reachedEdges = true;
        }
        else {
          reachedNextGraph = true;
        }
      }
    }

    action.accept(graph);

    return true;
  }

  private SmallGraph readGraph() {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split.length > TLFConstants.GRAPH_LABEL_INDEX ?
      split[TLFConstants.GRAPH_LABEL_INDEX] :
      TLFConstants.GRAPH_SYMBOL;

    SmallGraph graph = graphFactory.create();
    graph.setLabel(dictionary.translate(label));
    return graph;
  }

  private void readVertex(SmallGraph graph) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);
    String label = split[TLFConstants.VERTEX_LABEL_INDEX];
    graph.addVertex(dictionary.translate(label));
  }

  private void readEdge(SmallGraph graph) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);
    int source = Integer.valueOf(split[TLFConstants.EDGE_SOURCE_INDEX]);
    int target = Integer.valueOf(split[TLFConstants.EDGE_TARGET_INDEX]);
    String label = split[TLFConstants.EDGE_LABEL_INDEX];
    graph.addEdge(source, target, dictionary.translate(label));
  }

  @Override
  public Spliterator<SmallGraph> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return Long.MAX_VALUE;
  }

  @Override
  public int characteristics() {
    return ORDERED;
  }
}
