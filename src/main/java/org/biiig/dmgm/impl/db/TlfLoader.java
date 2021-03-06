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

package org.biiig.dmgm.impl.db;

import com.google.common.collect.Maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.impl.util.arrays.LongArrayBuilder;

/**
 * Read a database from a TLF file (graph Transaction List Format).
 */
public class TlfLoader extends PropertyGraphDbLoaderBase {

  /**
   * Increment for vertex and edge id array builders.
   */
  private static final int ARRAY_BUILDER_INCREMENT = 100;

  /**
   * Input file path.
   */
  private final String filePath;

  /**
   * Constructor.
   *
   * @param dbSupplier database factory
   * @param filePath input file path
   */
  public TlfLoader(Supplier<PropertyGraphDb> dbSupplier, String filePath) {
    super(dbSupplier);
    this.filePath = filePath;
  }

  @Override
  public PropertyGraphDb get() {
    PropertyGraphDb db = new InMemoryGraphDb(true);

    try {
      Iterator<String> iterator = Files.lines(Paths.get(filePath)).iterator();

      if (iterator.hasNext()) {
        // create cache for first graph
        String graphLine = iterator.next();
        Map<String, Long> vertexIdMap = Maps.newHashMap();
        LongArrayBuilder vertexIdsBuilder = new LongArrayBuilder(ARRAY_BUILDER_INCREMENT);
        LongArrayBuilder edgeIdsBuilder = new LongArrayBuilder(ARRAY_BUILDER_INCREMENT);

        // read vertices and edges
        while (iterator.hasNext()) {
          String line = iterator.next();

          if (line.startsWith(TlfConstants.VERTEX_SYMBOL)) {
            vertexIdsBuilder.add(readVertex(db, line, vertexIdMap));

          } else if (line.startsWith(TlfConstants.EDGE_SYMBOL)) {
            edgeIdsBuilder.add(readEdge(db, line, vertexIdMap));

          } else {
            // save graph
            readGraph(db, graphLine, vertexIdsBuilder.get(), edgeIdsBuilder.get());
            // reset cache
            vertexIdMap.clear();
            vertexIdsBuilder.reset();
            edgeIdsBuilder.reset();
          }
        }

        // save last graph
        readGraph(db, graphLine, vertexIdsBuilder.get(), edgeIdsBuilder.get());
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
  private void readGraph(PropertyGraphDb db, String line, long[] vertexIds, long[] edgeIds) {
    String[] split = line.split(TlfConstants.FIELD_SEPARATOR);

    String label = split.length > TlfConstants.GRAPH_LABEL_INDEX
        ? split[TlfConstants.GRAPH_LABEL_INDEX]
        : TlfConstants.GRAPH_SYMBOL;

    db.createGraph(db.encode(label), vertexIds, edgeIds);
  }

  /**
   * Read a vertex line, i.e., add a vertex to the database.
   * Additionally, update vertex id mapping.2
   *
   * @param db target database
   * @param line current line
   * @param vertexIdMap tlf vertex id -> db vertex id
   * @return db vertex id
   */
  private long readVertex(PropertyGraphDb db, String line, Map<String, Long> vertexIdMap) {
    String[] split = line.split(TlfConstants.FIELD_SEPARATOR);

    String label = split[TlfConstants.VERTEX_LABEL_INDEX];
    String tlfId = split[TlfConstants.VERTEX_ID_INDEX];

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
  private long readEdge(PropertyGraphDb db, String line, Map<String, Long> vertexIdMap) {
    String[] split = line.split(TlfConstants.FIELD_SEPARATOR);

    String source = split[TlfConstants.EDGE_SOURCE_INDEX];
    String target = split[TlfConstants.EDGE_TARGET_INDEX];
    String label = split[TlfConstants.EDGE_LABEL_INDEX];

    return db.createEdge(db.encode(label), vertexIdMap.get(source), vertexIdMap.get(target));
  }

}
