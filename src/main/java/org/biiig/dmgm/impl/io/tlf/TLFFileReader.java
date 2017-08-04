package org.biiig.dmgm.impl.io.tlf;

import com.google.common.collect.Lists;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.io.DataSource;
import org.biiig.dmgm.api.io.tlf.TLFSplitReaderFactory;
import org.biiig.dmgm.api.model.graph.DirectedGraphFactory;
import org.biiig.dmgm.impl.db.LabelDictionary;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TLFFileReader implements DataSource {
  private final String inputPath;

  public TLFFileReader(String inputPath) {
    this.inputPath = inputPath;
  }

  @Override
  public void load(Database database, DirectedGraphFactory graphFactory) throws IOException {

    TLFVertexLabelReporterFactory vertexReporterFactory = new TLFVertexLabelReporterFactory();
    LabelDictionary vertexDictionary = createDictionary(vertexReporterFactory);
    database.setVertexDictionary(vertexDictionary);

    TLFEdgeLabelReporterFactory edgeReporterFactory =
      new TLFEdgeLabelReporterFactory(vertexDictionary);
    LabelDictionary edgeDictionary = createDictionary(edgeReporterFactory);
    database.setEdgeDictionary(edgeDictionary);

    System.out.println(vertexDictionary);
    System.out.println(edgeDictionary);
  }

  private LabelDictionary createDictionary(TLFLabelReporterFactory labelReporterFactory) throws IOException {
    // read file and determine partition frequencies
    readSplits(labelReporterFactory);
    // prepare list for aggregation
    List<Countable<String>> globalFrequencies =
      Lists.newLinkedList(labelReporterFactory.getGlobalFrequencies());
    // aggregate frequencies
    Countable.sumSupportAndFrequency(globalFrequencies);
    return new LabelDictionary(globalFrequencies);
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
