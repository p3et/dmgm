package org.biiig.dmgm.api.model.source;

import org.biiig.dmgm.api.model.collection.GraphCollection;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.api.model.collection.IntGraphCollectionFactory;

import java.io.IOException;

public interface DMGraphDataSource {

  void loadWithMinLabelSupport(GraphCollection database, IntGraphFactory graphFactory, float minSupportThreshold) throws IOException;

  void load(GraphCollection database, IntGraphFactory graphFactory) throws IOException;

  GraphCollection getGraphCollection();

  DMGraphDataSource withCollectionFactory(IntGraphCollectionFactory collectionFactory);

}
