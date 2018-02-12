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
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.impl.db.InMemoryGraphDB;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.DFSEmbedding;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.PropertyKeys;
import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ToMultiDimensionalVectorTest extends DMGMTestBase {

  @Test
  public void apply() {
    PropertyGraphDB db = new InMemoryGraphDB(true);
    int taxonomyPathKey = db.encode(PropertyKeys.TAXONOMY_PATH);

    db.set(0l, taxonomyPathKey, new int[] {1, 2});

    DFSEmbedding embeddingA = new DFSEmbedding(0, new int[]{1, 2}, new int[0]);
    DFSEmbedding embeddingB = new DFSEmbedding(0, new int[]{1, 3}, new int[0]);


    Function<DFSEmbedding, MultiDimensionalVector> function = new ToMultiDimensionalVector(db, taxonomyPathKey);

    assertEquals("with data", getVectorA(), function.apply(embeddingA));
    assertEquals("without data", getVectorB(), function.apply(embeddingB));
  }

  private MultiDimensionalVector getVectorA() {
    int[][] dimensionPaths = new int[2][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[] {1, 2};

    return MultiDimensionalVector.create(null, dimensionPaths);
  }

  private MultiDimensionalVector getVectorB() {
    int[][] dimensionPaths = new int[2][];
    dimensionPaths[0] = new int[0];
    dimensionPaths[1] = new int[0];

    return MultiDimensionalVector.create(null, dimensionPaths);
  }
}