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

import org.biiig.dmgm.api.CollectionOperator;
import org.biiig.dmgm.api.QueryElements;
import org.biiig.dmgm.impl.operators.subgraph_mining.GeneralizedFrequentSubgraphs;

import java.util.function.Function;

public class GFSMGeneralizationTest extends GeneralizationTestBase {

  @Override
  public Function<QueryElements, CollectionOperator> getOperator() {
    return db -> new GeneralizedFrequentSubgraphs(db,1.0f, 10);
  }
}
