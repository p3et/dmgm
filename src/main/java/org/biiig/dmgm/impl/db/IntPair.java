package org.biiig.dmgm.impl.db;

public class IntPair {
  private final int left;
  private final int right;

  public IntPair(int left, int right) {
    this.left = left;
    this.right = right;
  }

  public int getLeft() {
    return left;
  }

  public int getRight() {
    return right;
  }

  @Override
  public int hashCode() {
    return left + 31 * right;
  }

  @Override
  public boolean equals(Object obj) {
    boolean equal;
    if (obj != null && obj.getClass().equals(this.getClass())) {
      IntPair that = (IntPair) obj;
      equal = this.left == that.left && this.right == that.right;
    } else {
      equal = false;
    }
        
    return equal;
  }
}
