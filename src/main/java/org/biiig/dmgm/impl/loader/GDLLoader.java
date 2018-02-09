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

import org.biiig.dmgm.api.PropertyGraphDB;
import org.biiig.dmgm.impl.db.InMemoryGraphDB;
import org.biiig.dmgm.impl.db.SourceIdTargetId;
import org.s1ck.gdl.GDLHandler;
import org.s1ck.gdl.model.Element;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GDLLoader implements Supplier<PropertyGraphDB> {

  private final String gdlString;

  private GDLLoader(String gdlString) {
    this.gdlString = gdlString;
  }

  @Override
  public PropertyGraphDB get() {
    PropertyGraphDB db = new InMemoryGraphDB(true);

    GDLHandler gdlHandler = new GDLHandler.Builder().buildFromString(gdlString);

    Map<Long, Long> vertexIdMap = gdlHandler
      .getVertices()
      .stream()
      .map(vertex -> {
        int label = db.encode(vertex.getLabel());
        long gdlId = vertex.getId();
        long dbId = db.createVertex(label);
        addProperties(db, vertex, dbId);
        return new SourceIdTargetId(gdlId, dbId);
      })
      .collect(Collectors.toMap(SourceIdTargetId::getSourceId, SourceIdTargetId::getRight));

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
        return new SourceIdTargetId(gdlId, dbId);
      })
      .collect(Collectors.toMap(SourceIdTargetId::getSourceId, SourceIdTargetId::getRight));

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

  private void addProperties(PropertyGraphDB db, Element vertex, long dbId) {
    vertex.getProperties()
      .forEach((key, value) -> {
        int symbol = db.encode(key);
        if (value instanceof String)
          db.set(dbId, symbol, (String) value);
        else if (value instanceof Integer)
          db.set(dbId, symbol, (Integer) value);
        else if (value instanceof Long)
          db.set(dbId, symbol, Math.toIntExact((Long) value));
        else if (value instanceof BigDecimal)
          db.set(dbId, symbol, (BigDecimal) value);
      });
  }

  public static GDLLoader fromString(String gdlString) {
    return new GDLLoader(gdlString);
  }



}
