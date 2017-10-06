package org.biiig.dmgm.api.io;

import org.biiig.dmgm.api.DMGraphDatabase;
import org.biiig.dmgm.api.model.graph.DMGraphFactory;

import java.io.IOException;

public interface DMGraphDataSource {

  void loadWithMinLabelSupport(DMGraphDatabase database, DMGraphFactory graphFactory, float minSupportThreshold) throws IOException;

  void load(DMGraphDatabase database, DMGraphFactory graphFactory) throws IOException;
}
