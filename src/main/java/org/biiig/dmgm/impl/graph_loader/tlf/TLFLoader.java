package org.biiig.dmgm.impl.graph_loader.tlf;

import org.biiig.dmgm.api.GraphCollection;

import java.io.IOException;
import java.util.stream.StreamSupport;

public class TLFLoader extends GraphCollectionLoaderBase {
  private final String filePath;


  private TLFLoader(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public GraphCollection getGraphCollection() {

    GraphCollection collection = collectionFactory.create();

    try {
      StreamSupport
        .stream(new TLFSpliterator(
          filePath,
          graphFactory,
          collection.getVertexDictionary(),
          collection.getEdgeDictionary()),
          false
        )
        .forEach(collection::add);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return collection;
  }

  public static TLFLoader fromFile(String filePath) {
    return new TLFLoader(filePath);
  }
}
