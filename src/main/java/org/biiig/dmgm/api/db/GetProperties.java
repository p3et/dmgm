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

package org.biiig.dmgm.api.db;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Getter for properties.
 */
public interface GetProperties extends SymbolDictionary {

  /**
   * Get a boolean value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  boolean is(long id, int key);

  /**
   * Get a long value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  long getLong(long id, int key);

  /**
   * Get a double value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  double getDouble(long id, int key);

  /**
   * Get a string value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  String getString(long id, int key);

  /**
   * Get a decimal value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  BigDecimal getBigDecimal(long id, int key);

  /**
   * Get a date value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  LocalDate getLocalDate(long id, int key);

  /**
   * Get get an array of integer values.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  int[] getInts(long id, int key);

  /**
   * Get get an array of integer values.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  String[] getStrings(long id, int key);

  /**
   * Get a boolean value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default boolean is(long id, String key) {
    return is(id, encode(key));
  }

  /**
   * Get a long value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default long getLong(long id, String key){
    return getLong(id, encode(key));
  }

  /**
   * Get a double value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default double getDouble(long id, String key) {
    return getDouble(id, encode(key));
  }

  /**
   * Get a string value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default String getString(long id, String key) {
    return getString(id, encode(key));
  }

  /**
   * Get a decimal value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default BigDecimal getBigDecimal(long id, String key){
    return getBigDecimal(id, encode(key));
  }

  /**
   * Get a date value.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default LocalDate getLocalDate(long id, String key) {
    return getLocalDate(id, encode(key));
  }

  /**
   * Get get an array of integer values.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default int[] getInts(long id, String key) {
    return getInts(id, encode(key));
  }

  /**
   * Get get an array of integer values.
   *
   * @param id element id
   * @param key property key
   * @return value
   */
  default String[] getStrings(long id, String key) {
    return getStrings(id, encode(key));
  }

  /**
   * Get all properties of an element.
   *
   * @param id element id
   * @return an array of properties
   */
  Property[] getProperties(long id);

  /**
   * Get all properties of all elements.
   *
   * @return map: element id -> properties
   */
  Map<Long, Property[]> getAllProperties();
}
