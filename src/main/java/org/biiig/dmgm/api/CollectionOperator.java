package org.biiig.dmgm.api;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by peet on 09.08.17.
 */
public interface CollectionOperator extends Function<Long, Long> {
  CollectionOperator parallel();
  CollectionOperator sequential();
}
