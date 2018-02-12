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

package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import org.biiig.dmgm.DMGMTestBase;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.junit.Test;

import java.util.function.Function;

public abstract class CharacteristicTestBase extends DMGMTestBase {
  @Test
  public void testAlgorithm() {
    String gdl =
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"X\"}[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:D)]" +
      ":IN{_category:\"Y\"}[(:A)-[:a]->(:B)-[:b]->(:C)-[:b]->(:E)]" +
      ":EX[(:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX[(:A)-[:a]->(:B)-[:a]->(:C)]" +
      ":EX[(:B)-[:a]->(:C)-[:a]->(:D)]" +
      ":EX[(:B)-[:a]->(:C)]" +
      ":EX[(:C)-[:a]->(:D)]" +
      ":EX{_category:\"X\"}[(:A)-[:a]->(:B)]" +
      ":EX{_category:\"Y\"}[(:A)-[:a]->(:B)]" +
      ":EX[(:A)-[:a]->(:B)-[:b]->(:C)]" +
      ":EX[(:B)-[:b]->(:C)]" ;

    runAndTestExpectation(getOperator(), gdl, false);
  }

  public abstract Function<PropertyGraphDB, CollectionToCollectionOperator> getOperator();
}
