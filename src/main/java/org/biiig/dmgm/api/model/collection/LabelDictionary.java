package org.biiig.dmgm.api.model.collection;

public interface LabelDictionary {
  int size();

  int translate(String value);

  String translate(int value);
}
