package org.biiig.dmgm.cli;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.collection.IntGraphCollectionFactory;
import org.biiig.dmgm.impl.model.collection.InMemoryGraphCollection;
import org.biiig.dmgm.impl.model.collection.InMemoryLabelDictionary;

public class InMemoryIntGraphCollectionFactory implements IntGraphCollectionFactory {
  @Override
  public GraphCollection create() {
    return new InMemoryGraphCollection()
      .withVertexDictionary(new InMemoryLabelDictionary())
      .withEdgeDictionary(new InMemoryLabelDictionary());
  }
}
