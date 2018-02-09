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

package org.biiig.dmgm.api;

import org.biiig.dmgm.impl.Property;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * A storage for labels and typed properties.
 */
public interface GetProperties {


  boolean is(long id, int key);

  long getLong(long id, int key);

  double getDouble(long id, int key);

  String getString(long id, int key);

  BigDecimal getBigDecimal(long id, int key);

  LocalDate getLocalDate(long id, int key);

  int[] getInts(long id, int key);

  String[] getStrings(long id, int key);


  Property[] getProperties(long id);
  Map<Long, Property[]> getAllProperties();

}
