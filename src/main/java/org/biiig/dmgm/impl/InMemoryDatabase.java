package org.biiig.dmgm.impl;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.Database;
import org.biiig.dmgm.api.model.DirectedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryDatabase implements Database {
  private final List<DirectedGraph> graphs = new ArrayList<>();
  private final Map<String, Integer> stringIntegerDictionary = Maps.newConcurrentMap();
  private final Map<Integer, String> integerStringDictionary = Maps.newConcurrentMap();
}
