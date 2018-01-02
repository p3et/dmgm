package org.biiig.dmgm.api;

public interface LabelDictionary {
  int size();

  int translate(String value);

  String translate(int value);
}
