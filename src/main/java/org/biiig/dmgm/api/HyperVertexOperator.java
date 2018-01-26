package org.biiig.dmgm.api;

import java.util.function.BiFunction;

/**
 * Created by peet on 09.08.17.
 */
public interface HyperVertexOperator extends BiFunction<HyperVertexDB, Long, Long> {
  HyperVertexOperator parallel();
  HyperVertexOperator sequential();
}
