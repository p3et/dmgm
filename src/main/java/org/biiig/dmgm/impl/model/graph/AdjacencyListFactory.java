package org.biiig.dmgm.impl.model.graph;

import org.biiig.dmgm.api.model.graph.IntGraph;
import org.biiig.dmgm.api.model.graph.IntGraphFactory;

/**
 * Created by peet on 02.08.17.
 */
public class AdjacencyListFactory implements IntGraphFactory {


  @Override
  public IntGraph create() {
    return new AdjacencyList();
  }
}
