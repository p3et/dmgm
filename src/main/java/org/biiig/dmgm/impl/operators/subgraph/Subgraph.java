package org.biiig.dmgm.impl.operators.subgraph;

import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.impl.graph_collection.InMemoryGraphCollectionBuilderFactory;
import org.biiig.dmgm.impl.operators.HyperVertexOperatorBase;

import java.util.function.Function;
import java.util.function.IntPredicate;

public class Subgraph extends HyperVertexOperatorBase {

  private final Function<SmallGraph, SmallGraph> subgraphMapper;

  public Subgraph(IntPredicate vertexPredicate, IntPredicate edgePredicate, Boolean dropIsolatedVertices) {
    this.subgraphMapper = new FilterVerticesAndEdgesByLabel(new GraphBaseFactory(), vertexPredicate, edgePredicate, dropIsolatedVertices);
  }

  @Override
  public GraphCollection apply(GraphCollection in) {
    GraphCollection out = new InMemoryGraphCollectionBuilderFactory()
      .create()
      .withLabelDictionary(in.getLabelDictionary())
      .create();

    in
      .stream()
      .map(subgraphMapper)
      .forEach(out::add);

    return out;
  }

}
