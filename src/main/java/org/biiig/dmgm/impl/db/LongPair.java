package org.biiig.dmgm.impl.db;

public class LongPair {
  private final long left;
  private final long right;

  public LongPair(long left, long right) {
    this.left = left;
    this.right = right;
  }

  public long getLeft() {
    return left;
  }

  public long getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    return (int)(left ^ (left >>> 32)) + 31 * (int)(right ^ (right >>> 32));
  }

  @Override
  public boolean equals(Object obj) {
    boolean equal;
    if (obj != null && obj.getClass().equals(this.getClass())) {
      LongPair that = (LongPair) obj;
      equal = this.left == that.left && this.right == that.right;
    } else {
      equal = false;
    }

    return equal;
  }

  @Override
  public String toString() {
    return "(" + left + "," + right + ")";
  }
}
