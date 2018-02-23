package org.biiig.dmgm.impl.util.arrays;


/**
 * Create primitive arrays of previously unknown size.
 * Internally, an array and the current maximum index are stored.
 * To avoid object instantiations the internal array has an initial size
 * and will be extended only if this size is exceeded.
 * At any time, a copy of the currently filled subarray can be provided.
 * To further avoid instantiations, the builder can be reset.
 * In this case, the internal array is kept and just the maximum index is set to zero.
 */
public class ArrayBuilderBase {
  /**
   * Length increment in the case of internal array extensions.
   * Additionally, this value is the initial size of the internal array.
   */
  protected final int increment;
  /**
   * Current length of the internal array.
   */
  protected int length;
  /**
   * Current maximum index of the internal array.
   */
  protected int index;

  protected ArrayBuilderBase(int increment) {
    this.increment = increment;
    this.length = increment;
    this.index = 0;
  }

  /**
   * Logically remove all elements.
   */
  public void reset() {
    index = 0;
  }
}
