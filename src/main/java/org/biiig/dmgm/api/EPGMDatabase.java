package org.biiig.dmgm.api;

public interface EPGMDatabase extends PropertyStore {
  int encode(String value);
  String decode(int value);
  long createVertex(int label);
  long createEdge(long sourceId, long targetId, int label);
  long createGraph(int label, long[] vertices, long[] edges);
  long[] getGraphsOfVertex(long id);
  long[] getGraphsOfEdge(long id);
  SmallGraph getSmallGraph(long graphId);
  GraphCollection getCollection(long[] graphIds);
}
