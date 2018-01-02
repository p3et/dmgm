package org.biiig.dmgm.impl.graph_collection;

import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionFactory;
import org.biiig.dmgm.impl.label_dictionary.InMemoryLabelDictionary;

public class InMemoryGraphCollectionFactory implements GraphCollectionFactory {
  @Override
  public GraphCollection create() {
    return new InMemoryGraphCollection()
      .withVertexDictionary(new InMemoryLabelDictionary())
      .withEdgeDictionary(new InMemoryLabelDictionary());
  }
}
