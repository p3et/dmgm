package org.biiig.dmgm.impl.loader;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.HyperVertexDB;
import org.biiig.dmgm.impl.db.HyperVertexDBBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public class TLFLoader implements Supplier<HyperVertexDB> {
  private final String filePath;

  private TLFLoader(String filePath) {
    this.filePath = filePath;
  }

  public static TLFLoader fromFile(String filePath) {
    return new TLFLoader(filePath);
  }

  @Override
  public HyperVertexDB get() {
    HyperVertexDB db = new HyperVertexDBBase();

    try {
      Iterator<String> iterator = Files.lines(Paths.get(filePath)).iterator();

      if (iterator.hasNext()) {
        // cache
        String graphLine = iterator.next();
        Map<String, Long> vertexIdMap = Maps.newHashMap();
        long[] vertexIds = new long[0];
        long[] edgeIds = new long[0];

        while (iterator.hasNext()) {
          String line = iterator.next();

          if (line.startsWith(TLFConstants.VERTEX_SYMBOL)) {
            vertexIds = ArrayUtils.add(vertexIds, readVertex(db, line, vertexIdMap));

          } else if (line.startsWith(TLFConstants.EDGE_SYMBOL)) {
            edgeIds = ArrayUtils.add(edgeIds, readEdge(db, line, vertexIdMap));

          } else {
            // save graph
            readGraph(db, graphLine, vertexIds, edgeIds);
            // reset cache
            vertexIds = new long[0];
            edgeIds = new long[0];
            vertexIdMap.clear();
          }
        }
        readGraph(db, graphLine, vertexIds, edgeIds);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }


    return db;
  }

  private void readGraph(HyperVertexDB db, String line, long[] vertexIds, long[] edgeIds) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split.length > TLFConstants.GRAPH_LABEL_INDEX ?
      split[TLFConstants.GRAPH_LABEL_INDEX] :
      TLFConstants.GRAPH_SYMBOL;

    db.createHyperVertex(db.encode(label), vertexIds, edgeIds);
  }

  private long readVertex(HyperVertexDB db, String line, Map<String, Long> vertexIdMap) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split[TLFConstants.VERTEX_LABEL_INDEX];
    String tlfId = split[TLFConstants.VERTEX_ID_INDEX];

    long dbId = db.createVertex(db.encode(label));
    vertexIdMap.put(tlfId, dbId);

    return dbId;
  }

  private long readEdge(HyperVertexDB db, String line, Map<String, Long> vertexIdMap) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String source = split[TLFConstants.EDGE_SOURCE_INDEX];
    String target = split[TLFConstants.EDGE_TARGET_INDEX];
    String label = split[TLFConstants.EDGE_LABEL_INDEX];

    return db.createEdge(db.encode(label), vertexIdMap.get(source), vertexIdMap.get(target));
  }

}
