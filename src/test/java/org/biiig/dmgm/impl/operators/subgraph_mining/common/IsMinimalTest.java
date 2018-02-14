package org.biiig.dmgm.impl.operators.subgraph_mining.common;

import org.biiig.dmgm.impl.operators.fsm.common.DFSCode;
import org.biiig.dmgm.impl.operators.fsm.common.IsMinimal;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class IsMinimalTest {

  @Test
  public void test1() {

    // 0:0-3->1:1 0:0<-3-2:2

    DFSCode dfsCode = new DFSCode(
      0, new int[] {0 ,1 ,2},
      new int[] {3, 3},
      new int[] {0, 2},
      new int[] { 1, 0},
      new boolean[] {true, false});

    assertTrue(new IsMinimal().test(dfsCode));
  }
}