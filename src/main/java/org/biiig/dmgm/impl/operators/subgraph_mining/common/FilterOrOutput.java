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

package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import javafx.util.Pair;
import org.biiig.dmgm.api.QueryElements;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A function that outputs a pair with optionally
 * - the input as key, if the input should be fed back to the next iteration
 * - a task for the data store, if something should be stored
 */
public interface FilterOrOutput<T extends DFSCodeSupportablePair> {
  Pair<Optional<T>, Optional<Consumer<QueryElements>>> apply(T supportable);
}
