package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Maps;
import org.biiig.dmgm.api.LabelDictionary;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Map;

public class Taxonomies {

  Map<String, IntTaxonomy> stringMap = Maps.newHashMap();
  Map<Integer, IntTaxonomy> integerMap;

  private static final String MSG_NO_DICT = "dictionary must be set before accessing taxonomies via integer labels";

  private LabelDictionary dictionary = null;
  private boolean dictionarySet = false;

  public void setDictionary(LabelDictionary dictionary) {
    this.dictionary = dictionary;
    dictionarySet = true;
  }

  public void add(String vertexLabel, IntTaxonomy taxonomy) {

  }

  public IntTaxonomy getTaxonomy(int vertexLabel) {
    if (!dictionarySet) throw new InvalidStateException(MSG_NO_DICT);
    return null;
  }
}
