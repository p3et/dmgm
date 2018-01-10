package org.biiig.dmgm.api;

import java.util.function.Function;

/**
 * Created by peet on 09.08.17.
 */
public interface Operator extends Function<GraphCollection, GraphCollection> {
  Operator parallel();
  Operator sequential();
}
