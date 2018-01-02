package org.biiig.dmgm.impl.graph_loader.tlf;

import org.biiig.dmgm.api.GraphCollectionFactory;
import org.biiig.dmgm.api.GraphFactory;
import org.biiig.dmgm.api.GraphCollectionLoader;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionFactory;
import org.biiig.dmgm.impl.graph.GraphBaseFactory;

public abstract class GraphCollectionLoaderBase implements GraphCollectionLoader {
  protected GraphCollectionFactory collectionFactory = new InMemoryGraphCollectionFactory();
  protected GraphFactory graphFactory = new GraphBaseFactory();

  @Override
  public GraphCollectionLoader withCollectionFactory(GraphCollectionFactory collectionFactory) {
    setCollectionFactory(collectionFactory);
    return this;
  }

  private void setCollectionFactory(GraphCollectionFactory collectionFactory) {
    this.collectionFactory = collectionFactory;
  }

  public void setGraphFactory(GraphFactory graphFactory) {
    this.graphFactory = graphFactory;
  }

  @Override
  public GraphCollectionLoader withGraphFactory(GraphFactory graphFactory) {
    setGraphFactory(graphFactory);
    return this;
  }
}
