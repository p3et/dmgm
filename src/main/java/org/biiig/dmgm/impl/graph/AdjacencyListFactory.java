package org.biiig.dmgm.impl.graph;

import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphFactory;

/**
 * Created by peet on 02.08.17.
 */
public class AdjacencyListFactory implements GraphFactory {


  @Override
  public Graph create() {
    return new AdjacencyList();
  }
}
