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


package org.biiig.dmgm.impl.operators;

import org.biiig.dmgm.api.db.PropertyGraphDB;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Superclass of DMGM operators.
 */
public abstract class DMGMOperatorBase {

  /**
   * Flag to enable parallel execution of the operator.
   * true <=> enabled
   */
  protected final boolean parallel;
  /**
   * Database of in- and output elements.
   */
  protected final PropertyGraphDB db;

  /**
   * Constructor.
   *
   * @param parallel parallel execution flag
   * @param db database
   */
  protected DMGMOperatorBase(boolean parallel, PropertyGraphDB db) {
    this.parallel = parallel;
    this.db = db;
  }

  /**
   * Stream a collection according to the parallel execution flag.
   *
   * @param collection collection to stream
   * @param <T> element type
   * @return sequential or parallel stream
   */
  protected <T> Stream<T> getParallelizableStream(Collection<T> collection) {
    return parallel ?
      collection.parallelStream() :
      collection.stream();
  }
}
