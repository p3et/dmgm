package org.biiig.dmgm.api;

public interface GraphCollectionBuilder {
  GraphCollectionBuilder withLabelDictionary(LabelDictionary dictionary);
  GraphCollectionBuilder withElementDataStore(ElementDataStore dataStore);
  GraphCollection create();
}
