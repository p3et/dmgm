package org.biiig.dmgm.impl.operators.subgraph;

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.model.GraphView;
import org.biiig.dmgm.impl.db.GdlLoader;
import org.biiig.dmgm.impl.db.InMemoryGraphDbSupplier;
import org.junit.Test;

import java.util.function.IntPredicate;

import static org.junit.Assert.assertEquals;

public class FilterVerticesAndEdgesByLabelTest {


  private static final String GDL = "[(:A)-[:a]->(:B)<-[:b]-(:C)]";

  @Test
  public void dropVertexA() {
    PropertyGraphDb db = getDb();

    IntPredicate vertexPredicate = l -> l != db.encode("A");
    IntPredicate edgePredicate = e -> true;
    Boolean dropIsolatedVertices = false;
    int expectedVertexCount = 2;
    int expectedEdgeCount = 1;

    test(db, vertexPredicate, edgePredicate, dropIsolatedVertices, expectedVertexCount, expectedEdgeCount);
  }

  @Test
  public void dropVertexBkeepIsolated() {
    dropVertexB(false, 2, 0);
  }

  @Test
  public void dropVertexBdropIsolated() {    
    dropVertexB(true, 0, 0);
  }


  private void dropVertexB(Boolean dropIsolatedVertices, int expectedVertexCount, int expectedEdgeCount) {
    PropertyGraphDb db = getDb();
    IntPredicate vertexPredicate = l -> l != db.encode("B");
    IntPredicate edgePredicate = e -> true;
    test(db, vertexPredicate, edgePredicate, dropIsolatedVertices, expectedVertexCount, expectedEdgeCount);
  }

  @Test
  public void dropEdgeBkeepIsolated() {
    dropEdgeB(false, 3, 1);
  }

  @Test
  public void dropEdgeBdropIsolated() {
    dropEdgeB(true, 2, 1);
  }


  private void dropEdgeB(Boolean dropIsolatedVertices, int expectedVertexCount, int expectedEdgeCount) {
    PropertyGraphDb db = getDb();
    IntPredicate vertexPredicate = v -> true;
    IntPredicate edgePredicate = e -> db.encode("b") != e;
    test(db, vertexPredicate, edgePredicate, dropIsolatedVertices, expectedVertexCount, expectedEdgeCount);
  }


  private void test(PropertyGraphDb db, IntPredicate vertexPredicate, IntPredicate edgePredicate, Boolean dropIsolatedVertices, int expectedVertexCount, int expectedEdgeCount) {
    long graphId = db.getGraphIds()[0];
    GraphView inGraph = db.getGraphView(graphId);


    GraphView outGraph = new FilterVerticesAndEdgesByLabel(
        vertexPredicate, edgePredicate, dropIsolatedVertices).apply(inGraph);


    assertEquals("vertex count", expectedVertexCount, outGraph.getVertexCount());
    assertEquals("edge count", expectedEdgeCount, outGraph.getEdgeCount());
  }

  private PropertyGraphDb getDb() {
    return new GdlLoader(new InMemoryGraphDbSupplier(true), GDL).get();
  }
}