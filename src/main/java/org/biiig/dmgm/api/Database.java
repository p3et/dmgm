package org.biiig.dmgm.api;

import org.biiig.dmgm.impl.db.LabelDictionary;


public interface Database {
  void setVertexDictionary(LabelDictionary labelFrequencies);
}
