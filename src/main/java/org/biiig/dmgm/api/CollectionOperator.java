package org.biiig.dmgm.api;

import java.util.function.UnaryOperator;

/**
 * An operator that creates a new graph collection from an existing one.
 */
public interface CollectionOperator extends UnaryOperator<Long> {
  /**
   * Ensure the operator is executed in parallel.
   *
   * @return parallel operator
   */
  CollectionOperator parallel();

  /**
   * Ensure the operator is executed sequentially (default).
   *
   * @return sequential operator
   */
  CollectionOperator sequential();

  /**
   * Execute the operation.
   *
   * @param inputCollectionId id of the input graph collection
   * @return id of the output graph collection
   */
  @Override
  Long apply(Long inputCollectionId);
}
