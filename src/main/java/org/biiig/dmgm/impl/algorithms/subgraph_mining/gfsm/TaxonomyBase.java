package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * @param <T> value type
 */
abstract class TaxonomyBase<T> {
  Set<T> values = Sets.newHashSet();
  Map<T, T> childParent = Maps.newHashMap();
  Map<T, Set<T>> parentChildren = Maps.newHashMap();
}
