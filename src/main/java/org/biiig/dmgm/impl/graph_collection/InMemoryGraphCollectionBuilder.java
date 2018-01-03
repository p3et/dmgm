package org.biiig.dmgm.impl.graph_collection;

import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.data_store.InMemoryElementDataStore;
import org.biiig.dmgm.impl.label_dictionary.InMemoryLabelDictionary;

public class InMemoryGraphCollectionBuilder implements GraphCollectionBuilder {

  private LabelDictionary dictionary;
  private ElementDataStore dataStore;

  @Override
  public GraphCollectionBuilder withLabelDictionary(LabelDictionary dictionary) {
    this.dictionary = dictionary;
    return this;
  }

  @Override
  public GraphCollectionBuilder withElementDataStore(ElementDataStore dataStore) {
    this.dataStore = dataStore;
    return this;
  }

  @Override
  public GraphCollection create() {
    return new InMemoryGraphCollection(
      dictionary == null ? new InMemoryLabelDictionary() : dictionary,
      dataStore == null ? new InMemoryElementDataStore() : dataStore);
  }

}
