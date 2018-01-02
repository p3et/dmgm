package org.biiig.dmgm.api.model.source;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.collection.GraphCollectionFactory;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;

/**
 * Describe a process that creates a graph collection from a source (e.g., file).
 */
public interface GraphCollectionLoader {

  /**
   * Load data and
   *
   * @return the graph collection.
   */
  GraphCollection getGraphCollection();

  /**
   * Specify a custom collection factory instead of using the implementations's default.
   *
   * @param collectionFactory collection factory
   * @return this
   */
  GraphCollectionLoader withCollectionFactory(GraphCollectionFactory collectionFactory);

  /**
   * Specify a graph collection factory instead of using the implementations's default.
   *
   * @param graphFactory graph factory
   * @return this
   */
  GraphCollectionLoader withGraphFactory(IntGraphFactory graphFactory);

}
