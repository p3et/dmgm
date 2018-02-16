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

package org.biiig.dmgm.impl.operators.patternmining;

public class SubgraphMiningTestData {

  public static final String SINGLE_EDGE =
      ":IN[(v1:A)-[e1:a]->(v2:A)]"
          + ":IN[(v1)-[e1]->(v2)]"
          + ":IN[(:A)-[:a]->(:A),(:B)-[:b]->(:B),(:B)-[:b]->(:B)]"
          + ":IN[(:A)-[:b]->(:A),(:A)-[:b]->(:A),(:A)-[:b]->(:A)]"
          + ":EX[(:A)-[:a]->(:A)]";

  public static final String Y =
      ":IN[(a:A)-[:a]->(:B),(a)-[:a]->(:C)]"
          + ":IN[(b:A)-[:a]->(:B),(b)-[:a]->(:C)]"
          + ":IN[(c:A)-[:a]->(:C),(c)-[:a]->(:B)]"
          + ":EX[(a:A)-[:a]->(:B),(a)-[:a]->(:C)]"
          + ":EX[(b:A)-[:a]->(:B)]"
          + ":EX[(c:A)-[:a]->(:C)]";

  public static final String SIMPLE_GRAPH_INPUT =
      ":IN[(:A)-[:a]->(v1:B)-[:b]->(:C),(v1)-[:c]->(:D)]"
          + ":IN[(:A)-[:a]->(v2:B)-[:b]->(:C),(v2)-[:c]->(:E)]"
          + ":IN[(:A)-[:a]->(v3:B)-[:d]->(:C),(v3)-[:c]->(:E)]"
          + ":EX[(:A)-[:a]->(:B)]"
          + ":EX[(:B)-[:b]->(:C)]"
          + ":EX[(:B)-[:c]->(:E)]"
          + ":EX[(:A)-[:a]->(:B)-[:b]->(:C)]"
          + ":EX[(:A)-[:a]->(:B)-[:c]->(:E)]";

  public static final String PARALLEL_EDGES_INPUT =
      ":IN[(v1:A)-[:a]->(:A)-[:a]->(v1:A)]"
          + ":IN[(v2:A)-[:a]->(:A)-[:a]->(v2:A)]"
          + ":IN[(:A)-[:a]->(:A)-[:a]->(:A)]"
          + ":EX[(:A)-[:a]->(:A)]"
          + ":EX[(v3:A)-[:a]->(:A)-[:a]->(v3:A)]";

  public static final String LOOP_INPUT =
      "g1:IN[(v1:A)-[:a]->(v1)-[:a]->(:A)]"
          + "g2:IN[(v2:A)-[:a]->(v2)-[:a]->(:A)]"
          + "g3:IN[(v3:A)-[:a]->(v3)-[:a]->(:A)]"
          + "g4:IN[(:A)-[:a]->(:A)-[:a]->(:A)]"
          + "s1:EX[(:A)-[:a]->(:A)]"
          + "s2:EX[(v3:A)-[:a]->(v3)]"
          + "s3:EX[(v4:A)-[:a]->(v4)-[:a]->(:A)]";

  public static final String DIAMOND_INPUT =
      "g1:IN[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]"
          + "g2:IN[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]"
          + "g3:IN[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]"
          + "s1:EX[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]"
          + "s2:EX[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)             ]"
          + "s3:EX[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),             (v3:A)-[:a]->(v4:A)]"
          + "s4:EX[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A)                                 ]"
          + "s5:EX[(v1:A)-[:a]->(v2:A)             ,(v1:A)-[:a]->(v3:A)             ]"
          + "s6:EX[             (v2:A)-[:a]->(v4:A),             (v3:A)-[:a]->(v4:A)]"
          + "s7:EX[(v1:A)-[:a]->(v2:A)                                              ]";

  public static final String CIRCLE_WITH_BRANCH_INPUT =
      "g1:IN[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]"
          + "g2:IN[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]"
          + "g3:IN[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]"
          + "s1:EX[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]"
          + "s2:EX[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)           ]"
          + "s3:EX[(v1:A)-[:a]->(:A)-[:a]->(:A)       (v1)-[:b]->(:B)]"
          + "s4:EX[(v1:A)-[:a]->(:A)       (:A)-[:a]->(v1)-[:b]->(:B)]"
          + "s5:EX[             (:A)-[:a]->(:A)-[:a]->(v1:A)-[:b]->(:B)]"
          + "s6:EX[(:A)-[:a]->(:A)-[:a]->(:A)]"
          + "s7:EX[(:A)-[:a]->(:A)-[:b]->(:B)]"
          + "s8:EX[(:A)<-[:a]-(:A)-[:b]->(:B)]"
          + "s9:EX[(:A)-[:a]->(:A)]"
          + "s10:EX[(:A)-[:b]->(:B)]";

  public static final String MULTI_LABELED_CIRCLE_INPUT =
      "g1:IN[(v:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(v)]"
          + "g2:IN[(v:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(v)]"
          + "s1:EX[(v:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(v)]"
          + "s2:EX[(:A)-[:a]->(:B)-[:a]->(:C)]"
          + "s3:EX[(:B)-[:a]->(:C)-[:a]->(:A)]"
          + "s4:EX[(:C)-[:a]->(:A)-[:a]->(:B)]"
          + "s5:EX[(:A)-[:a]->(:B)]"
          + "s6:EX[(:B)-[:a]->(:C)]"
          + "s7:EX[(:C)-[:a]->(:A)]";
}
