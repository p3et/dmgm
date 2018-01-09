package org.biiig.dmgm.impl.algorithms.aggregation;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.Graph;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.Operator;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.Assert.*;

public class AggregationTest extends DMGMTestBase {

  private static final String KEY = "vertexCount";

  @Test
  public void apply() throws IOException {
    GraphCollection collection = getPredictableDatabase();

    Operator aggregation = Aggregation.forInteger(KEY, Graph::getVertexCount);
    collection.apply(aggregation);

    collection.stream().forEach(graph ->
      assertEquals(
        "vertexCount",
        Optional.of(graph.getVertexCount()),
        collection.getElementDataStore().getGraphInteger(graph.getId(), KEY)
      )
    );
  }
}