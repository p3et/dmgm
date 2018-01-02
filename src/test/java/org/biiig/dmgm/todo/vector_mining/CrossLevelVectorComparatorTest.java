//package org.biiig.dmgm.todo.vector_mining;
//
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import static org.junit.Assert.assertTrue;
//
//public class CrossLevelVectorComparatorTest {
//
//  private int[] field1 = new int[] {0, 1};
//  private int[] field2 = new int[] {0, 1, 1};
//  private int[] field3 = new int[] {0, 1, 0};
//
//  private int[][] vector1 = new int[][] {field1, field2, field3};
//  private int[][] vector2 = new int[][] {field1, field3, field2};
//  private int[][] vector3 = new int[][] {field1, field2};
//  private int[][] vector4 = new int[][] {field2, field1};
//
//  private CrossLevelVectorComparator comparator = new CrossLevelVectorComparator();
//
//  @Rule
//  public ExpectedException exception = ExpectedException.none();
//
//  @Test
//  public void testGreater() {
//    assertTrue(comparator.compare(vector1, vector2) > 0);
//  }
//
//  @Test
//  public void testEqual() {
//    assertTrue(comparator.compare(vector1, vector1) == 0);
//  }
//
//  @Test
//  public void testLess() {
//    assertTrue(comparator.compare(vector2, vector1) < 0);
//  }
//
//  @Test
//  public void testVectorSizeException() {
//    exception.expect(IllegalArgumentException.class);
//    comparator.compare(vector1, vector3);
//  }
//
//  @Test
//  public void testDimensionDepthException() {
//    exception.expect(IllegalArgumentException.class);
//    comparator.compare(vector3, vector4);
//  }
//}