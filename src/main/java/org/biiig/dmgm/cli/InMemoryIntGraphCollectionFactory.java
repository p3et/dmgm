package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.collection.InMemoryLabelDictionary;

public class InMemoryIntGraphCollectionFactory implements IntGraphCollectionFactory {
  @Override
  public IntGraphCollection create() {
    return new InMemoryGraphCollection()
      .withVertexDictionary(new InMemoryLabelDictionary())
      .withEdgeDictionary(new InMemoryLabelDictionary());
  }
}
