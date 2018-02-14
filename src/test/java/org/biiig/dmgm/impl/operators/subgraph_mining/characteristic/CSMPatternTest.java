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

import org.biiig.dmgm.TestConstants;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.operators.CollectionToCollectionOperator;
import org.biiig.dmgm.impl.operators.fsm.CharacteristicSimpleSubgraphs;
import org.biiig.dmgm.impl.operators.subgraph_mining.common.SubgraphMiningPatternTest;

import java.util.function.Function;

public class CSMPatternTest extends SubgraphMiningPatternTest {


  @Override
  protected Function<PropertyGraphDB, CollectionToCollectionOperator> getOperator() {
    return db -> new CharacteristicSimpleSubgraphs(db, TestConstants.PARALLEL, 0.6f, 10);
  }

}
