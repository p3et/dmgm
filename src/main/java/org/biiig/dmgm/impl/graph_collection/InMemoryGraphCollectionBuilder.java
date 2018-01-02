package org.biiig.dmgm.impl.graph_collection;

import org.biiig.dmgm.api.ElementDataStore;
import org.biiig.dmgm.api.GraphCollection;
import org.biiig.dmgm.api.GraphCollectionBuilder;
import org.biiig.dmgm.api.LabelDictionary;
import org.biiig.dmgm.impl.data_store.InMemoryElementDataStore;
import org.biiig.dmgm.impl.label_dictionary.InMemoryLabelDictionary;

public class InMemoryGraphCollectionBuilder implements GraphCollectionBuilder {

  private LabelDictionary vertexDictionary;
  private LabelDictionary edgeDictionary;
  private ElementDataStore dataStore;

  @Override
  public GraphCollectionBuilder withVertexDictionary(LabelDictionary dictionary) {
    this.vertexDictionary = dictionary;
    return this;
  }

  @Override
  public GraphCollectionBuilder withEdgeDictionary(LabelDictionary dictionary) {
    this.edgeDictionary = dictionary;
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
      vertexDictionary == null ? new InMemoryLabelDictionary() : vertexDictionary,
      edgeDictionary == null ? new InMemoryLabelDictionary() : edgeDictionary,
      dataStore == null ? new InMemoryElementDataStore() : dataStore);
  }

}
