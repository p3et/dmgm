package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.recursion.RecursionStrategy;
import de.jesemann.paralleasy.recursion.RecursiveTask;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Specializer implements Function<DFSCodeEmbeddingsPair, Collection<Consumer<GraphDB>>> {

  private final PropertyStore dataStore;
  private final FilterOrOutput<PatternVectorsPair> filter;
  private final int taxonomyPathKey;

  public Specializer(PropertyStore dataStore, FilterOrOutput<PatternVectorsPair> filter, int taxonomyPathKey) {
    this.dataStore = dataStore;
    this.filter = filter;
    this.taxonomyPathKey = taxonomyPathKey;
  }

  @Override
  public Collection<Consumer<GraphDB>> apply(DFSCodeEmbeddingsPair pair) {
    List<MultiDimensionalVector> vectors = pair
      .getEmbeddings()
      .stream()
      .map(new ToMultiDimensionalVector(dataStore, taxonomyPathKey))
      .collect(Collectors.toList());

    DFSCode pattern = pair.getDFSCode();

    RecursiveTask<PatternVectorsPair, Consumer<GraphDB>> recursiveTask = RecursiveTask
      .createFor(new Specialize(pattern, filter))
      .on(Lists.newArrayList(new PatternVectorsPair(pattern, vectors)))
      .withStrategy(RecursionStrategy.SEQUENTIAL);

    recursiveTask.run();

    return recursiveTask.getOutput();
  }
}
