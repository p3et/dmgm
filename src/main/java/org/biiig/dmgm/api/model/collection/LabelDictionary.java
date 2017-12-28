package org.biiig.dmgm.api.model.collection;

import javafx.util.Pair;

import java.util.Collection;
import java.util.List;

public interface LabelDictionary {
  int size();

  int translate(String value);

  String translate(int value);
}
