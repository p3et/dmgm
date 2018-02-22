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

package org.biiig.dmgm.api.operators;

import java.util.Collection;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.biiig.dmgm.api.model.GraphView;

/**
 * An operator for directed multigraphs.
 */
public interface DmgmOperator extends WithDatabaseAccess {

  /**
   * Add a graph to the database and get its id.
   *
   * @param graph cached graph
   *
   * @return graph id
   */
  long createGraph(GraphView graph);

  /**
   * Stream a collection according to the operators parallelization options.
   *
   * @param collection collection to stream
   * @param <T> element type
   * @return sequential or parallel stream
   */
  <T> Stream<T> getParallelizableStream(Collection<T> collection);

  /**
   * Stream an array of longs according to the operators parallelization options.
   *
   * @param longs array to stream
   * @return sequential or parallel stream
   */
  LongStream getParallelizableLongStream(long[] longs);
}
