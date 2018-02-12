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

package org.biiig.dmgm.impl.operators.subgraph_mining.generalized;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.biiig.dmgm.api.db.QueryElements;
import org.junit.Test;

import java.util.function.Function;

public abstract class GeneralizationTestBase extends DMGMTestBase {
  @Test
  public void testAlgorithm() {
    Function<QueryElements, CollectionToCollectionOperator> operator = getOperator();

    String inputGDL =
      ":IN[(:A_A)-[:a]->(:B_B)-[:a]->(:C)]" +
      ":IN[(:A_B)-[:a]->(:B_B_B)-[:a]->(:C)]" +
      ":EX[(:A)-[:a]->(:B)]" +
      ":EX[(:A)-[:a]->(:B_B)]" +
      ":EX[(:B)-[:a]->(:C)]" +
      ":EX[(:B_B)-[:a]->(:C)]" +
      ":EX[(:A)-[:a]->(:B_B)-[:a]->(:C)]" +
      ":EX[(:A)-[:a]->(:B)-[:a]->(:C)]";

    runAndTestExpectation(operator, inputGDL, false);
  }

  public abstract Function<QueryElements, CollectionToCollectionOperator> getOperator();
}
