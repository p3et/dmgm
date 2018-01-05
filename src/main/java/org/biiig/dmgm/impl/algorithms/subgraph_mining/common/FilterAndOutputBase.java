package org.biiig.dmgm.impl.algorithms.subgraph_mining.common;

import org.biiig.dmgm.api.GraphCollection;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public abstract class FilterAndOutputBase implements FilterAndOutput {
  protected final GraphCollection output;

  public FilterAndOutputBase(GraphCollection output) {
    this.output = output;
  }

  public int getSupport(GraphInformation[] infos) {
    return Math.toIntExact(
      Stream.of(infos)
        .map(GraphInformation::getGraphId)
        .distinct()
        .count()
    );
  }

  public int getSupport(Collection<? extends GraphInformation> collection) {
    return getSupport(collection.toArray(new GraphInformation[collection.size()]));
  }
}
