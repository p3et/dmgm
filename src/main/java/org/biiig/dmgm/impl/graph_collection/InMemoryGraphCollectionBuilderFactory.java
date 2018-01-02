package org.biiig.dmgm.impl.graph_collection;

import org.biiig.dmgm.api.*;

public class InMemoryGraphCollectionBuilderFactory implements GraphCollectionBuilderFactory {

  @Override
  public GraphCollectionBuilder create() {
    return new InMemoryGraphCollectionBuilder();
  }
}
