package org.biiig.dmgm.impl.operators.subgraph_mining.common;

public class SubgraphMiningTestData {

  public static final String SINGLE_EDGE_INPUT =
    "[(v1:A)-[e1:a]->(v2:A)]" +
    "[(v1)-[e1]->(v2)]" +
    "[(:A)-[:a]->(:A),(:B)-[:b]->(:B),(:B)-[:b]->(:B)]" +
    "[(:A)-[:b]->(:A),(:A)-[:b]->(:A),(:A)-[:b]->(:A)]";

  public static final String SINGLE_EDGE_EXPECTED =
    "[(:A)-[:a]->(:A)]";

  public static final String SIMPLE_GRAPH_INPUT =
    "[(:A)-[:a]->(v1:B)-[:b]->(:C),(v1)-[:c]->(:D)]" +
    "[(:A)-[:a]->(v2:B)-[:b]->(:C),(v2)-[:c]->(:E)]" +
    "[(:A)-[:a]->(v3:B)-[:d]->(:C),(v3)-[:c]->(:E)]";

  public static final String SIMPLE_GRAPH_EXPECTED =
    "[(:A)-[:a]->(:B)]" +
    "[(:B)-[:b]->(:C)]" +
    "[(:B)-[:c]->(:E)]" +
    "[(:A)-[:a]->(:B)-[:b]->(:C)]" +
    "[(:A)-[:a]->(:B)-[:c]->(:E)]";

  public static final String PARALLEL_EDGES_INPUT =
    "[(v1:A)-[:a]->(:A)-[:a]->(v1:A)]" +
    "[(v2:A)-[:a]->(:A)-[:a]->(v2:A)]" +
    "[(:A)-[:a]->(:A)-[:a]->(:A)]";

  public static final String PARALLEL_EDGES_EXPECTED =
    "[(:A)-[:a]->(:A)]" +
    "[(v3:A)-[:a]->(:A)-[:a]->(v3:A)]";

  public static final String LOOP_INPUT =
    "g1[(v1:A)-[:a]->(v1)-[:a]->(:A)]" +
    "g2[(v2:A)-[:a]->(v2)-[:a]->(:A)]" +
    "g3[(v3:A)-[:a]->(v3)-[:a]->(:A)]" +
    "g4[(:A)-[:a]->(:A)-[:a]->(:A)]";

  public static final String LOOP_EXPECTED =
    "s1[(:A)-[:a]->(:A)]" +
    "s2[(v3:A)-[:a]->(v3)]" +
    "s3[(v4:A)-[:a]->(v4)-[:a]->(:A)]";

  public static final String DIAMOND_INPUT =
    "g1[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]" +
    "g2[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]" +
    "g3[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]";

  public static final String DIAMOND_EXPECTED =
    "s1[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)-[:a]->(v4:A)]" +
    "s2[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),(v1:A)-[:a]->(v3:A)             ]" +
    "s3[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A),             (v3:A)-[:a]->(v4:A)]" +
    "s4[(v1:A)-[:a]->(v2:A)-[:a]->(v4:A)                                 ]" +
    "s5[(v1:A)-[:a]->(v2:A)             ,(v1:A)-[:a]->(v3:A)             ]" +
    "s6[             (v2:A)-[:a]->(v4:A),             (v3:A)-[:a]->(v4:A)]" +
    "s7[(v1:A)-[:a]->(v2:A)                                              ]";

  public static final String CIRCLE_WITH_BRANCH_INPUT =
    "g1[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]" +
    "g2[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]" +
    "g3[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]";

  public static final String CIRCLE_WITH_BRANCH_EXPECTED =
    "s1[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)-[:b]->(:B)]" +
    "s2[(v1:A)-[:a]->(:A)-[:a]->(:A)-[:a]->(v1)           ]" +
    "s3[(v1:A)-[:a]->(:A)-[:a]->(:A)       (v1)-[:b]->(:B)]" +
    "s4[(v1:A)-[:a]->(:A)       (:A)-[:a]->(v1)-[:b]->(:B)]" +
    "s5[             (:A)-[:a]->(:A)-[:a]->(v1:A)-[:b]->(:B)]" +
    "s6[(:A)-[:a]->(:A)-[:a]->(:A)]" +
    "s7[(:A)-[:a]->(:A)-[:b]->(:B)]" +
    "s8[(:A)<-[:a]-(:A)-[:b]->(:B)]" +
    "s9[(:A)-[:a]->(:A)]" +
    "s10[(:A)-[:b]->(:B)]";

  public static final String MULTI_LABELED_CIRCLE_INPUT =
    "g1[(v:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(v)]" +
    "g2[(v:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(v)]";

  public static final String MULTI_LABELED_CIRCLE_EXPECTED =
    "s1[(v:A)-[:a]->(:B)-[:a]->(:C)-[:a]->(v)]" +
    "s2[(:A)-[:a]->(:B)-[:a]->(:C)]" +
    "s3[(:B)-[:a]->(:C)-[:a]->(:A)]" +
    "s4[(:C)-[:a]->(:A)-[:a]->(:B)]" +
    "s5[(:A)-[:a]->(:B)]" +
    "s6[(:B)-[:a]->(:C)]" +
    "s7[(:C)-[:a]->(:A)]";
}
