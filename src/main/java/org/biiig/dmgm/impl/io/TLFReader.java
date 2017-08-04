package org.biiig.dmgm.impl.io;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataReader;
import org.biiig.dmgm.api.model.DirectedGraphFactory;
import org.biiig.dmgm.todo.model.labeled_graph.LabeledGraph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Stream;

public class TLFReader implements DataReader {
  private final String inputPath;
  private final Database database;
  private final DirectedGraphFactory graphFactory;

  public TLFReader(String inputPath, DirectedGraphFactory graphFactory, Database database) {
    this.inputPath = inputPath;
    this.graphFactory = graphFactory;
    this.database = database;
  }

  @Override
  public void read() throws IOException {
    int parallelism = Runtime.getRuntime().availableProcessors();

    Queue<String[]> splits = new ConcurrentLinkedDeque<>();

    LabeledGraph graph = new LabeledGraph(-1);
    Map<Integer, Integer> vertexIdMap = Maps.newHashMap();

    Stream<String> stream = Files.lines(Paths.get(inputPath));

    Boolean readingComplete = false;

    for (int i = 1; i < parallelism; i++) {
      TLFGraphReader reader = new TLFGraphReader(splits, graphFactory, database, readingComplete);
      new Thread(reader).start();
    }

    new TLFFileSplitter(stream, splits).invoke();

    readingComplete = true;

    new TLFGraphReader(splits, graphFactory, database, readingComplete).run();

    System.out.println(splits.size());

  }
}
