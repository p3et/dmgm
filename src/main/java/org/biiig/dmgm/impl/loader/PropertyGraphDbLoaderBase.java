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

import org.biiig.dmgm.api.loader.PropertyGraphDbFactory;
import org.biiig.dmgm.api.loader.PropertyGraphDbLoader;

/**
 * Superclass of database loaders.
 */
public abstract class PropertyGraphDbLoaderBase implements PropertyGraphDbLoader {
  /**
   * Factory to create database objects.
   */
  final PropertyGraphDbFactory dbFactory;

  /**
   * Constructor.
   *
   * @param dbFactory a factory for databases
   */
  protected PropertyGraphDbLoaderBase(PropertyGraphDbFactory dbFactory) {
    this.dbFactory = dbFactory;
  }
}
