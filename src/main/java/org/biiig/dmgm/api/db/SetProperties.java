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

/**
 * Setter for properties.
 */
public interface SetProperties extends SymbolDictionary {

  /**
   * Set a boolean value.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void set(long id, int key, boolean value);

  /**
   * Set a long value.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void set(long id, int key, long value);

  /**
   * Set a double value.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void set(long id, int key, double value);

  /**
   * Set a string value.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void set(long id, int key, String value);

  /**
   * Set a decimal value.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void set(long id, int key, BigDecimal value);

  /**
   * Set a date value.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void set(long id, int key, LocalDate value);

  /**
   * Set an array of integer values.
   *
   * @param id element id
   * @param key encoded property key
   * @param values values
   */
  void set(long id, int key, int[] values);

  /**
   * Add an integer value to an array of integer values.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void add(long id, int key, int value);

  /**
   * Set an array of string values.
   *
   * @param id element id
   * @param key encoded property key
   * @param values values
   */
  void set(long id, int key, String[] values);

  /**
   * Add a string value to an array of string values.
   *
   * @param id element id
   * @param key encoded property key
   * @param value value
   */
  void add(long id, int key, String value);

  /**
   * Set a boolean value.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void set(long id, String key, boolean value) {
    set(id, encode(key), value);
  }

  /**
   * Set a long value.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void set(long id, String key, long value) {
    set(id, encode(key), value);
  }

  /**
   * Set a double value.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void set(long id, String key, double value) {
    set(id, encode(key), value);
  }
  /**
   * Set a string value.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void set(long id, String key, String value) {
    set(id, encode(key), value);
  }

  /**
   * Set a decimal value.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void set(long id, String key, BigDecimal value) {
    set(id, encode(key), value);
  }

  /**
   * Set a date value.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void set(long id, String key, LocalDate value) {
    set(id, encode(key), value);
  }

  /**
   * Set an array of integer values.
   *
   * @param id element id
   * @param key property key
   * @param values values
   */
  default void set(long id, String key, int[] values) {
    set(id, encode(key), values);
  }

  /**
   * Add an integer value to an array of integer values.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void add(long id, String key, int value) {
    add(id, encode(key), value);
  }

  /**
   * Set an array of string values.
   *
   * @param id element id
   * @param key property key
   * @param values values
   */
  default void set(long id, String key, String[] values) {
    set(id, encode(key), values);
  }

  /**
   * Add a string value to an array of string values.
   *
   * @param id element id
   * @param key property key
   * @param value value
   */
  default void add(long id, String key, String value) {
    add(id, encode(key), value);
  }
}
