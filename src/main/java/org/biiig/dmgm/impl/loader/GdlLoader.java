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

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import org.biiig.dmgm.api.db.PropertyGraphDb;
import org.biiig.dmgm.api.db.SetProperties;
import org.biiig.dmgm.api.loader.PropertyGraphDbFactory;
import org.biiig.dmgm.impl.db.LongPair;
import org.s1ck.gdl.GDLHandler;
import org.s1ck.gdl.model.Element;

/**
 * Turn a GDL String into a {@code PropertyGraphDB}.
 * GDL is a graph description language.
 * @see <a href="https://github.com/s1ck/gdl">Graph Definition Language</a>
 */
public class GdlLoader extends PropertyGraphDbLoaderBase {

  /**
   * String containing the GDL.
   */
  private final String gdlString;

  /**
   * Constructor.
   *
   * @param dbFactory database factory.
   * @param gdlString GDL
   */
  public GdlLoader(PropertyGraphDbFactory dbFactory, String gdlString) {
    super(dbFactory);
    this.gdlString = gdlString;
  }

  @Override
  public PropertyGraphDb get() {
    PropertyGraphDb db = dbFactory.get();

    GDLHandler gdlHandler = new GDLHandler.Builder()
        .buildFromString(gdlString);

    // VERTICES
    Map<Long, Long> vertexIdMap = gdlHandler
        .getVertices()
        .stream()
        .map(vertex -> {
          int label = db.encode(vertex.getLabel());
          long gdlId = vertex.getId();
          long dbId = db.createVertex(label);
          addProperties(db, vertex, dbId);
          return new LongPair(gdlId, dbId);
        })
        .collect(Collectors.toMap(
          LongPair::getSourceId,
          LongPair::getTargetId
        ));

    // EDGES
    Map<Long, Long> edgeIdMap = gdlHandler
        .getEdges()
        .stream()
        .map(edge -> {
          int label = db.encode(edge.getLabel());
          long gdlId = edge.getId();
          long sourceId = vertexIdMap.get(edge.getSourceVertexId());
          long targetId = vertexIdMap.get(edge.getTargetVertexId());
          long dbId = db.createEdge(label, sourceId, targetId);
          addProperties(db, edge, dbId);
          return new LongPair(gdlId, dbId);
        })
        .collect(Collectors.toMap(
          LongPair::getSourceId,
          LongPair::getTargetId
        ));

    // GRAPHS
    gdlHandler
        .getGraphs()
        .forEach(graph -> {
          int label = db.encode(graph.getLabel());

          long[] vertices = gdlHandler
            .getVertices()
            .stream()
            .filter(v -> v.getGraphs().contains(graph.getId()))
            .mapToLong(Element::getId)
            .map(vertexIdMap::get)
            .toArray();

          long[] edges = gdlHandler
            .getEdges()
            .stream()
            .filter(e -> e.getGraphs().contains(graph.getId()))
            .mapToLong(Element::getId)
            .map(edgeIdMap::get)
            .toArray();

          long dbId = db.createGraph(label, vertices, edges);
          addProperties(db, graph, dbId);
        });

    return db;
  }

  /**
   * Add GDL properties to the database.
   *
   * @param db database to store properties
   * @param element vertex, edge or graph
   * @param dbId the element's database id
   */
  private void addProperties(SetProperties db, Element element, long dbId) {
    element
        .getProperties()
        .forEach((key, value) -> {
          if (value != null) {
            int encodedKey = db.encode(key);

            if (value instanceof Integer) {
              db.set(dbId, encodedKey, (int) value);
            } else if (value instanceof Long) {
              db.set(dbId, encodedKey, (long) value);
            } else if (value instanceof Float) {
              db.set(dbId, encodedKey, (float) value);
            } else if (value instanceof Double) {
              db.set(dbId, encodedKey, (double) value);
            } else if (value instanceof String) {
              db.set(dbId, encodedKey, (String) value);
            } else if (value instanceof BigDecimal) {
              db.set(dbId, encodedKey, (BigDecimal) value);
            } else if (value instanceof Boolean && (boolean) value) {
              db.set(dbId, encodedKey, true);
            }
          }
        });
  }
}
