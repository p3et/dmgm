/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */

package org.biiig.dmgm.impl.operators.patternmining;

import org.biiig.dmgm.DmgmTestBase;
import org.biiig.dmgm.TestConstants;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.junit.Test;

import java.util.function.Function;

public abstract class SubgraphMiningPatternTest extends DmgmTestBase {
  @Test
  public void testSingleEdge() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.SINGLE_EDGE, false);
  }

  @Test
  public void testSimpleGraph() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.SIMPLE_GRAPH_INPUT, false);
  }

  @Test
  public void testY() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.Y, false);
  }

  @Test
  public void testParallelEdges() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.PARALLEL_EDGES_INPUT, false);
  }

  @Test
  public void testLoop() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.LOOP_INPUT, false);
  }

  @Test
  public void testDiamond() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.DIAMOND_INPUT, false);
  }

  @Test
  public void testCircleWithBranch() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.CIRCLE_WITH_BRANCH_INPUT, false);
  }

  @Test
  public void testMultiLabeledCircle() {
    runAndTestExpectation(getOperator(),
        SubgraphMiningTestData.MULTI_LABELED_CIRCLE_INPUT, false);
  }

  protected abstract Function<PropertyGraphDb, CollectionToCollectionOperator> getOperator();

  protected CollectionFrequentSubgraphsMinerBuilder getBuilder(PropertyGraphDb db) {
    return new PatternMinerBuilder(db, TestConstants.PARALLEL)
        .fromCollection().extractFrequentSubgraphs(0.6f, 10);
  }
}
