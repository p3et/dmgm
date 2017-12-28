package org.biiig.dmgm.cli;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.impl.model.source.tlf.TLFConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TLFSpliterator implements Spliterator<StringGraph> {

  private final Iterator<String> iterator;
  private String line;


  public TLFSpliterator(
    String filePath
  ) throws IOException {
    this.iterator = Files.lines(Paths.get(filePath)).iterator();
    if (iterator.hasNext())
      this.line = iterator.next();
  }

  @Override
  public void forEachRemaining(Consumer<? super StringGraph> action) {
    while (iterator.hasNext())
      tryAdvance(action);
  }

  @Override
  public boolean tryAdvance(Consumer<? super StringGraph> action) {
    boolean reachedEdges = false;
    boolean reachedNextGraph = false;

    StringGraph graph = readGraph();

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

  private StringGraph readGraph() {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split.length > TLFConstants.GRAPH_LABEL_INDEX ?
      split[TLFConstants.GRAPH_LABEL_INDEX] :
      TLFConstants.GRAPH_SYMBOL;

    return new StringGraph(label);
  }

  private void readVertex(StringGraph graph) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);
    String label = split[TLFConstants.VERTEX_LABEL_INDEX];
    graph.addVertex(label);
  }

  private void readEdge(StringGraph graph) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);
    int source = Integer.valueOf(split[TLFConstants.EDGE_SOURCE_INDEX]);
    int target = Integer.valueOf(split[TLFConstants.EDGE_TARGET_INDEX]);
    String label = split[TLFConstants.EDGE_LABEL_INDEX];
    graph.addEdge(source, target, label);
  }

  @Override
  public Spliterator<StringGraph> trySplit() {
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
