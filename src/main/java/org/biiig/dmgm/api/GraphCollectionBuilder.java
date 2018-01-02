package org.biiig.dmgm.api;

public interface GraphCollectionBuilder {
  GraphCollectionBuilder withVertexDictionary(LabelDictionary dictionary);
  GraphCollectionBuilder withEdgeDictionary(LabelDictionary dictionary);
  GraphCollectionBuilder withElementDataStore(ElementDataStore dataStore);

  GraphCollection create();
}
