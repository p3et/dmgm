package org.biiig.dmgm.api.model.source;

import org.biiig.dmgm.api.model.collection.DMGraphCollection;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;

import java.io.IOException;

public interface DMGraphDataSource {

  void loadWithMinLabelSupport(DMGraphCollection database, DMGraphFactory graphFactory, float minSupportThreshold) throws IOException;

  void load(DMGraphCollection database, DMGraphFactory graphFactory) throws IOException;
}
