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

package org.biiig.dmgm.impl.loader;

import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.loader.PropertyGraphDBFactory;
import org.biiig.dmgm.impl.db.InMemoryGraphDB;

/**
 * Reference implementation of a database factory.
 */
public class InMemoryGraphDBFactory implements PropertyGraphDBFactory {
  /**
   * Flag to enable parallel read for created databases.
   */
  private final boolean parallelRead;

  /**
   * Constructor.
   *
   * @param parallelRead parallel read flag
   */
  public InMemoryGraphDBFactory(boolean parallelRead) {
    this.parallelRead = parallelRead;
  }

  @Override
  public PropertyGraphDB get() {
    return new InMemoryGraphDB(parallelRead);
  }
}
