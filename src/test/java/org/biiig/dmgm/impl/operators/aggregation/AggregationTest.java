package org.biiig.dmgm.impl.operators.aggregation;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.SmallGraph;
import org.biiig.dmgm.api.HyperVertexOperator;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

public class AggregationTest extends DMGMTestBase {

  private static final String KEY = "vertexCount";

  @Test
  public void apply() throws IOException {
    GraphCollection collection = getPredictableDatabase();

    HyperVertexOperator aggregation = Aggregation.forInteger(KEY, SmallGraph::getVertexCount);
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