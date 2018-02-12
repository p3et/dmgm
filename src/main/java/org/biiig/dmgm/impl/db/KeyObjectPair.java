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

package org.biiig.dmgm.impl.db;

import org.biiig.dmgm.api.config.DMGMConstants;
import org.biiig.dmgm.api.db.Property;

/**
 * Reference implementation of a property.
 */
public class KeyObjectPair implements Property {
  /**
   * Encoded key
   */
  private final int key;
  /**
   * Value
   */
  private final Object value;

  /**
   * Constructor.
   *
   * @param key encoded key
   * @param value value
   */
  KeyObjectPair(int key, Object value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public int getKey() {
    return key;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return key + DMGMConstants.Separators.KEY_VALUE + value;
  }
}
