package org.biiig.dmgm.impl.graph_collection;

import org.biiig.dmgm.api.PropertyStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.db.InMemoryGraphCollection;
import org.biiig.dmgm.impl.label_dictionary.InMemoryLabelDictionary;

public class InMemoryGraphCollectionBuilder {

  private LabelDictionary dictionary;
  private PropertyStore dataStore;

  @Override
  public GraphCollectionBuilder withLabelDictionary(LabelDictionary dictionary) {
    this.dictionary = dictionary;
    return this;
  }

  @Override
  public GraphCollectionBuilder withElementDataStore(PropertyStore dataStore) {
    this.dataStore = dataStore;
    return this;
  }

  @Override
  public GraphCollection create() {
    return new InMemoryGraphCollection(
      dictionary == null ? new InMemoryLabelDictionary() : dictionary,
      dataStore == null ? new InMemoryPropertyStore() : dataStore);
  }

}
