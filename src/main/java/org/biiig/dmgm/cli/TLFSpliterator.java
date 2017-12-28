package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.impl.model.source.tlf.TLFConstants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class TLFSpliterator implements Spliterator<IntGraph> {

  private final Iterator<String> iterator;
  private final LabelDictionary vertexDictionary;
  private final LabelDictionary edgeDictionary;
  private String line;
  private final IntGraphFactory graphFactory;


  public TLFSpliterator(
    String filePath,
    IntGraphFactory graphFactory, LabelDictionary vertexDictionary, LabelDictionary edgeDictionary) throws IOException {
    this.iterator = Files.lines(Paths.get(filePath)).iterator();
    this.graphFactory = graphFactory;
    this.vertexDictionary = vertexDictionary;
    this.edgeDictionary = edgeDictionary;
    if (iterator.hasNext())
      this.line = iterator.next();
  }

  @Override
  public void forEachRemaining(Consumer<? super IntGraph> action) {
    while (iterator.hasNext())
      tryAdvance(action);
  }

  @Override
  public boolean tryAdvance(Consumer<? super IntGraph> action) {
    boolean reachedEdges = false;
    boolean reachedNextGraph = false;

    IntGraph graph = readGraph();

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

  private IntGraph readGraph() {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split.length > TLFConstants.GRAPH_LABEL_INDEX ?
      split[TLFConstants.GRAPH_LABEL_INDEX] :
      TLFConstants.GRAPH_SYMBOL;

    return graphFactory.create();
  }

  private void readVertex(IntGraph graph) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);
    String label = split[TLFConstants.VERTEX_LABEL_INDEX];
    graph.addVertex(vertexDictionary.translate(label));
  }

  private void readEdge(IntGraph graph) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);
    int source = Integer.valueOf(split[TLFConstants.EDGE_SOURCE_INDEX]);
    int target = Integer.valueOf(split[TLFConstants.EDGE_TARGET_INDEX]);
    String label = split[TLFConstants.EDGE_LABEL_INDEX];
    graph.addEdge(source, target, edgeDictionary.translate(label));
  }

  @Override
  public Spliterator<IntGraph> trySplit() {
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
