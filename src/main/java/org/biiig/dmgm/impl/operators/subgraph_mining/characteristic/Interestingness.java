package org.biiig.dmgm.impl.operators.subgraph_mining.characteristic;

import java.util.Map;

public interface Interestingness {
  int[] getInterestingCategories(Map<Integer, Float> categorySupports, float totalSupport);
}
