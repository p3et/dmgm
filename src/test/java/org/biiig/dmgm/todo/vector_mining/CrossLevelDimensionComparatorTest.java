//package org.biiig.dmgm.todo.vector_mining;
//
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//
//import static org.junit.Assert.assertTrue;
//
//public class CrossLevelDimensionComparatorTest {
//
//  private int[] field1 = new int[] {0, 1, 0};
//  private int[] field2 = new int[] {0, 1, 1};
//  private int[] field3 = new int[] {0, 1};
//
//  private CrossLevelDimensionComparator comparator = new CrossLevelDimensionComparator();
//
//  @Rule
//  public ExpectedException exception = ExpectedException.none();
//
//  @Test
//  public void testGreater() {
//    assertTrue(comparator.compare(field2, field1) > 0);
//  }
//
//  @Test
//  public void testEqual() {
//    assertTrue(comparator.compare(field1, field1) == 0);
//  }
//
//  @Test
//  public void testLess() {
//    assertTrue(comparator.compare(field1, field2) < 0);
//  }
//
//  @Test
//  public void testException() {
//    exception.expect(IllegalArgumentException.class);
//    comparator.compare(field1, field3);
//  }
//}