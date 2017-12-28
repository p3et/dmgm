package org.biiig.dmgm.api.model.source;

import org.biiig.dmgm.api.model.collection.IntGraphCollection;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;
import org.biiig.dmgm.cli.IntGraphCollectionFactory;

import java.io.IOException;

public interface DMGraphDataSource {

  void loadWithMinLabelSupport(IntGraphCollection database, IntGraphFactory graphFactory, float minSupportThreshold) throws IOException;

  void load(IntGraphCollection database, IntGraphFactory graphFactory) throws IOException;

  IntGraphCollection getGraphCollection();

  DMGraphDataSource withCollectionFactory(IntGraphCollectionFactory collectionFactory);

}
