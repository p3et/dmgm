package org.biiig.dmgm.impl.model.source.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.api.model.source.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.collection.InMemoryLabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TLFDataSource implements DMGraphDataSource {
  private final String inputPath;

  public TLFDataSource(String inputPath) {
    this.inputPath = inputPath;
  }

  @Override
  public void loadWithMinLabelSupport(DMGraphCollection database, DMGraphFactory graphFactory, float minSupportThreshold) throws IOException {

    TLFLabelReaderFactory labelReaderFactory = new TLFLabelReaderFactory();
    readSplits(labelReaderFactory);

    int count = labelReaderFactory.getGraphCount();
    int minSupport = (int) (count * minSupportThreshold);

    LabelDictionary vertexDictionary =
      createDictionary(labelReaderFactory.getVertexLabelFrequencies(), minSupport);

    LabelDictionary edgeDictionary =
      createDictionary(labelReaderFactory.getEdgeLabelFrequencies(), minSupport);

    database.setVertexDictionary(vertexDictionary);
    database.setEdgeDictionary(edgeDictionary);

    TLFGraphReaderFactory graphReaderFactory =
      new TLFGraphReaderFactory(graphFactory, database);

    readSplits(graphReaderFactory);
  }

  @Override
  public void load(DMGraphCollection database, DMGraphFactory graphFactory) throws IOException {
    loadWithMinLabelSupport(database, graphFactory, 1.0f);
  }

  private LabelDictionary createDictionary(
    List<Countable<String>> labelFrequencies, int minSupport) throws IOException {
    // prepare list for aggregation
    List<Countable<String>> globalFrequencies = Lists.newLinkedList(labelFrequencies);
    // aggregate frequencies
    Countable.sumSupportAndFrequency(globalFrequencies);
    // filter infrequent labels
    globalFrequencies.removeIf(c -> c.getSupport() < minSupport);
    // create dictionary
    return new InMemoryLabelDictionary(globalFrequencies);
  }

  /**
   * - splits a file in chunks for each graphs
   * - process splits in parallel
   *
   * @param readerFactory
   * @throws IOException
   */
  private void readSplits(TLFSplitReaderFactory readerFactory) throws IOException {
    Queue<String[]> splits = new ConcurrentLinkedDeque<>();
    Boolean reachedEOF = false;

    for (int i = 1; i < Runtime.getRuntime().availableProcessors(); i++) {
      new Thread(readerFactory.create(splits, reachedEOF)).start();
    }

    new TLFFileSplitter(Files.lines(Paths.get(inputPath)), splits).invoke();
    reachedEOF = true;

    readerFactory.create(splits, reachedEOF).run();
  }
}
