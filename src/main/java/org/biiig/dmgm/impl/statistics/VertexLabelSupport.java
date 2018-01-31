package org.biiig.dmgm.impl.statistics;

import com.google.common.collect.Lists;
import de.jesemann.paralleasy.collectors.GroupByKeyListValues;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.biiig.dmgm.api.GraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VertexLabelSupport implements Support<Integer> {

  @Override
  public Map<Integer, Integer> getAbsolute(GraphDB db, long collectionId) {
    Map<Integer, Long> intSupport = db
      .getCachedCollection(collectionId)
      .stream()
      .flatMap(g -> g
        .vertexIdStream()
        .map(g::getVertexLabel)
        .boxed()
      )
      .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    Map<String, List<Long>> stringSupport = intSupport
      .entrySet()
      .stream()
      .map(e -> new Pair<>(db.decode(e.getKey()), e.getValue()))
      .flatMap(p -> {
        Stream<Pair<String, Long>> stream;

        String label = p.getKey();

        if (label.contains(GeneralizedFrequentSubgraphs.LEVEL_SEPARATOR)) {
          Collection<Pair<String, Long>> pairs = Lists.newArrayList(p);

          Long support = p.getValue();

          while (StringUtils.countMatches(label, GeneralizedFrequentSubgraphs.LEVEL_SEPARATOR) > 1) {
            label = StringUtils.substringBeforeLast(label, GeneralizedFrequentSubgraphs.LEVEL_SEPARATOR);
            pairs.add(new Pair<>(label, support));
          }

          stream = pairs.stream();
        } else {
          stream = Stream.of(p);
        }

        return stream;
      })
      .collect(new GroupByKeyListValues<>(Pair::getKey, Pair::getValue));


    return stringSupport
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
        e -> db.encode(e.getKey()),
        e -> e.getValue().stream().mapToInt(Math::toIntExact).sum()
      ));
  }
}
