package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class OutputSpecialization implements Consumer<Map.Entry<MultiDimensionalVector, List<MultiDimensionalVector>>> {
  public OutputSpecialization(DFSCode topLevel) {

  }

  @Override
  public void accept(Map.Entry<MultiDimensionalVector, List<MultiDimensionalVector>> entry) {
    System.out.println(entry.getKey());
  }
}
