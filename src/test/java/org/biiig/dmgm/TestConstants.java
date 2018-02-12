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

package org.biiig.dmgm;

public interface TestConstants {
  /**
   * Convention: Graphs showing this label are considered to be input graphs.
   */
  String INPUT_GRAPH_LABEL = "IN";
  /**
   * Convention: Graphs showing this label are considered to be expected output graphs.
   */
  String EXPECTATION_GRAPH_LABEL = "EX";
  /**
   * Default value for parallel execution flag.
   */
  boolean PARALLEL = true;
}
