package org.biiig.dmgm.impl.algorithms.tfsm;

/**
 * Created by peet on 11.08.17.
 */
public class TFSMConfig {

  private final float minSupport;
  private final int maxEdgeCount;

  public TFSMConfig(float minSupport, int maxEdgeCount) {
    this.minSupport = minSupport;
    this.maxEdgeCount = maxEdgeCount;
  }

  public float getMinSupport() {
    return minSupport;
  }

  public int getMaxEdgeCount() {
    return maxEdgeCount;
  }
}
