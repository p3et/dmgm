package org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining;
import com.google.common.collect.Lists;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.Vector;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.countable.Countable;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CrossLevelFrequentVectorsTest {

  private final CrossLevelVectorComparator patternComparator  = new CrossLevelVectorComparator();

  @Test
  public void mineBottomUp() throws Exception {
    mine(new CrossLevelFrequentVectorsBottomUp());
  }

  @Test
  public void mineTopDown() throws Exception {
    mine(new CrossLevelFrequentVectorsTopDown());
  }

  private void mine(CrossLevelFrequentVectors miner) {
    List<Countable<Vector>> database = getDatabase();

    List<Countable<Vector>>expectedResult = getExpectedResult();
//    Arrays.sort(expectedResult, patternComparator);


    List<Countable<Vector>>result = miner.mine(database, 3);

    assertEquals(expectedResult.size(), result.size());

    for (Countable<Vector> expected : expectedResult) {
      boolean found = false;

      for (Countable<Vector> actual : result) {
        found = expected.getObject().equals(actual.getObject());

        if (found) {
          break;
        }
      }

      assertTrue(found);
    }
  }

  public List<Countable<Vector>>getDatabase() {
    int[][] vector1 = new int[2][];
    vector1[0] = new int[] {1, 1};
    vector1[1] = new int[] {1, 1, 1};

    int[][] vector2 = new int[2][];
    vector2[0] = new int[] {1, 1};
    vector2[1] = new int[] {1, 1, 2};

    int[][] vector3 = new int[2][];
    vector3[0] = new int[] {1, 2};
    vector3[1] = new int[] {1, 1, 2};

    List<Countable<Vector>>database = Lists.newArrayList();

    database.add(new Countable<Vector>(new Vector(vector1)));
    database.add(new Countable<Vector>(new Vector(vector2)));
    database.add(new Countable<Vector>(new Vector(vector3)));

    return database;
  }

  public List<Countable<Vector>>getExpectedResult() {
    int[][] root = new int[2][];
    root[0] = new int[] {0, 0};
    root[1] = new int[] {0, 0, 0};

    int[][] pattern1 = new int[2][];
    pattern1[0] = new int[] {0, 0};
    pattern1[1] = new int[] {1, 0, 0};

    int[][] pattern2 = new int[2][];
    pattern2[0] = new int[] {0, 0};
    pattern2[1] = new int[] {1, 1, 0};

    int[][] pattern3 = new int[2][];
    pattern3[0] = new int[] {1, 0};
    pattern3[1] = new int[] {0, 0, 0};

    int[][] pattern4 = new int[2][];
    pattern4[0] = new int[] {1, 0};
    pattern4[1] = new int[] {1, 0, 0};

    int[][] pattern5 = new int[2][];
    pattern5[0] = new int[] {1, 0};
    pattern5[1] = new int[] {1, 1, 0};

    List<Countable<Vector>>database = Lists.newArrayList();

    database.add(new Countable<Vector>(new Vector(root)));
    database.add(new Countable<Vector>(new Vector(pattern1)));
    database.add(new Countable<Vector>(new Vector(pattern2)));
    database.add(new Countable<Vector>(new Vector(pattern3)));
    database.add(new Countable<Vector>(new Vector(pattern4)));
    database.add(new Countable<Vector>(new Vector(pattern5)));

    return database;
  }
}