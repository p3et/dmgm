/*
 * This file is part of Directed Multigraph Miner (DMGM).
 *
 * DMGM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DMGM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DMGM. If not, see <http://www.gnu.org/licenses/>.
 */


package org.biiig.dmgm.impl.loader;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.db.PropertyGraphDB;
import org.biiig.dmgm.api.loader.PropertyGraphDBFactory;
import org.biiig.dmgm.impl.db.InMemoryGraphDB;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;

/**
 * Read a database from a TLF file (graph Transaction List Format).
 */
public class TLFLoader extends PropertyGraphDBLoaderBase {
  /**
   * Input file path.
   */
  private final String filePath;

  /**
   * Constructor.
   *
   * @param dbFactory database factory
   * @param filePath input file path
   */
  public TLFLoader(PropertyGraphDBFactory dbFactory, String filePath) {
    super(dbFactory);
    this.filePath = filePath;
  }

  @Override
  public PropertyGraphDB get() {
    PropertyGraphDB db = new InMemoryGraphDB(true);

    try {
      Iterator<String> iterator = Files.lines(Paths.get(filePath)).iterator();

      if (iterator.hasNext()) {
        // create cache for first graph
        String graphLine = iterator.next();
        Map<String, Long> vertexIdMap = Maps.newHashMap();
        long[] vertexIds = new long[0];
        long[] edgeIds = new long[0];

        // read vertices and edges
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
            vertexIdMap.clear();
            vertexIds = new long[0];
            edgeIds = new long[0];
          }
        }

        // save last graph
        readGraph(db, graphLine, vertexIds, edgeIds);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

    return db;
  }

  /**
   * Read a graph line, i.e., add a graph to the database.
   *
   * @param db target database
   * @param line current line
   * @param vertexIds the graph's vertex ids
   * @param edgeIds the graph's edge ids
   */
  private void readGraph(PropertyGraphDB db, String line, long[] vertexIds, long[] edgeIds) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split.length > TLFConstants.GRAPH_LABEL_INDEX ?
      split[TLFConstants.GRAPH_LABEL_INDEX] :
      TLFConstants.GRAPH_SYMBOL;

    db.createGraph(db.encode(label), vertexIds, edgeIds);
  }

  /**
   * Read a vertex line, i.e., add a vertex to the database.
   * Additionally, update vertex id mapping.
   *
   * @param db target database
   * @param line current line
   * @param vertexIdMap tlf vertex id -> db vertex id
   * @return db vertex id
   */
  private long readVertex(PropertyGraphDB db, String line, Map<String, Long> vertexIdMap) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String label = split[TLFConstants.VERTEX_LABEL_INDEX];
    String tlfId = split[TLFConstants.VERTEX_ID_INDEX];

    long dbId = db.createVertex(db.encode(label));
    vertexIdMap.put(tlfId, dbId);

    return dbId;
  }

  /**
   * Read an edge line, i.e., add an edge to the database.
   *
   * @param db target database
   * @param line current line
   * @param vertexIdMap tlf vertex id -> db vertex id
   * @return db edge id
   */
  private long readEdge(PropertyGraphDB db, String line, Map<String, Long> vertexIdMap) {
    String[] split = line.split(TLFConstants.FIELD_SEPARATOR);

    String source = split[TLFConstants.EDGE_SOURCE_INDEX];
    String target = split[TLFConstants.EDGE_TARGET_INDEX];
    String label = split[TLFConstants.EDGE_LABEL_INDEX];

    return db.createEdge(db.encode(label), vertexIdMap.get(source), vertexIdMap.get(target));
  }

}
