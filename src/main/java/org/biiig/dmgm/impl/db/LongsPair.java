package org.biiig.dmgm.impl.db;

import java.util.Arrays;

public class LongsPair {
  private final long[] left;
  private final long[] right;

  public LongsPair(long[] left, long[] right) {
    this.left = left;
    this.right = right;
  }

  public long[] getLeft() {
    return left;
  }

  public long[] getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(left) + 31 * Arrays.hashCode(right);
  }

  @Override
  public boolean equals(Object obj) {
    boolean equal;
    if (obj != null && obj.getClass().equals(this.getClass())) {
      LongsPair that = (LongsPair) obj;
      equal = Arrays.equals(this.left, that.left) && Arrays.equals(this.right, that.right);
    } else {
      equal = false;
    }

    return equal;
  }
}
