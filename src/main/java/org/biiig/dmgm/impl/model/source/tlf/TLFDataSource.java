package org.biiig.dmgm.impl.model.source.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.api.model.collection.LabelDictionary;
import org.biiig.dmgm.api.model.source.DMGraphDataSource;
import org.biiig.dmgm.api.model.source.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.cli.*;
import org.biiig.dmgm.impl.model.collection.InMemoryLabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TLFDataSource implements DMGraphDataSource {
  private final String filePath;
  private GraphCollectionFactory collectionFactory;

  private TLFDataSource(String filePath) {
    this.filePath = filePath;
    this.collectionFactory = new InMemoryGraphCollectionFactory();
  }

  @Override
  public GraphCollection getGraphCollection() {

    Collection<StringGraph> graphs = null;
    try {
      graphs = StreamSupport
        .stream(new TLFSpliterator(filePath), false)
        .collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return GraphCollection
      .fromCollection(graphs);
  }

  @Override
  public DMGraphDataSource withCollectionFactory(GraphCollectionFactory collectionFactory) {
    setCollectionFactory(collectionFactory);
    return this;
  }

  @Override
  public void loadWithMinLabelSupport(IntGraphCollection database, IntGraphFactory graphFactory, float minSupportThreshold) throws IOException {

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
  public void load(IntGraphCollection database, IntGraphFactory graphFactory) throws IOException {
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
    // aggregateReports dictionary
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

    new TLFFileSplitter(Files.lines(Paths.get(filePath)), splits).invoke();
    reachedEOF = true;

    readerFactory.create(splits, reachedEOF).run();
  }

  public static TLFDataSource fromFile(String filePath) {
    return new TLFDataSource(filePath);
  }

  public void setCollectionFactory(GraphCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }
}
