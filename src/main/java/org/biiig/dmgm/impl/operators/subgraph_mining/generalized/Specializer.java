package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.recursion.RecursionStrategy;
import de.jesemann.paralleasy.recursion.RecursiveTask;
import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSCodeEmbeddingsPair;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.FilterOrOutput;
import org.biiig.dmgm.impl.graph.DFSCode;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Specializer implements Function<DFSCodeEmbeddingsPair, Collection<Consumer<GraphCollection>>> {

  private final ElementDataStore dataStore;
  private final FilterOrOutput<PatternVectorsPair> filter;

  public Specializer(ElementDataStore dataStore, FilterOrOutput<PatternVectorsPair> filter) {
    this.dataStore = dataStore;
    this.filter = filter;
  }

  @Override
  public Collection<Consumer<GraphCollection>> apply(DFSCodeEmbeddingsPair pair) {
    List<MultiDimensionalVector> vectors = pair
      .getEmbeddings()
      .stream()
      .map(new ToMultiDimensionalVector(dataStore))
      .collect(Collectors.toList());

    DFSCode pattern = pair.getDFSCode();

    RecursiveTask<PatternVectorsPair, Consumer<GraphCollection>> recursiveTask = RecursiveTask
      .createFor(new Specialize(pattern, filter))
      .on(Lists.newArrayList(new PatternVectorsPair(pattern, vectors)))
      .withStrategy(RecursionStrategy.SEQUENTIAL);

    recursiveTask.run();

    return recursiveTask.getOutput();
  }
}
