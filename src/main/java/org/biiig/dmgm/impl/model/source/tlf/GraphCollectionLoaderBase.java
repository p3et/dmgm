package org.biiig.dmgm.impl.model.source.tlf;

import org.biiig.dmgm.api.model.collection.GraphCollectionFactory;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.api.model.source.GraphCollectionLoader;
import org.biiig.dmgm.cli.InMemoryGraphCollectionFactory;
import org.biiig.dmgm.impl.model.graph.IntGraphBaseFactory;

public abstract class GraphCollectionLoaderBase implements GraphCollectionLoader {
  protected GraphCollectionFactory collectionFactory = new InMemoryGraphCollectionFactory();
  protected IntGraphFactory graphFactory = new IntGraphBaseFactory();

  @Override
  public GraphCollectionLoader withCollectionFactory(GraphCollectionFactory collectionFactory) {
    setCollectionFactory(collectionFactory);
    return this;
  }

  private void setCollectionFactory(GraphCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  public void setGraphFactory(IntGraphFactory graphFactory) {
    this.graphFactory = graphFactory;
  }

  @Override
  public GraphCollectionLoader withGraphFactory(IntGraphFactory graphFactory) {
    setGraphFactory(graphFactory);
    return this;
  }
}
