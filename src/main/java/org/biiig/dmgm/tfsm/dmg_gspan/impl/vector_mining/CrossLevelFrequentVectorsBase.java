package org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ArrayUtils;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.Vector;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.countable.Countable;

import java.util.Iterator;
import java.util.List;

public abstract class CrossLevelFrequentVectorsBase implements CrossLevelFrequentVectors {

  protected int dimCount;

  protected int[] schema;
  List<GFSMLatticeNode> bottom = Lists.newLinkedList();
  List<GFSMLatticeNode> parents = Lists.newLinkedList();
  List<GFSMLatticeNode> children = Lists.newLinkedList();
  List<GFSMLatticeNode> result = Lists.newLinkedList();

  protected void switchParentsChildren() {
    List<GFSMLatticeNode> temp = children;
    children = parents;
    parents = temp;
  }

  protected void clearLists() {
    this.bottom.clear();
    this.parents.clear();
    this.children.clear();
    this.result.clear();
  }

  protected List<GFSMLatticeNode> aggregate(List<GFSMLatticeNode> nodes, boolean updateIndex) {
    if(nodes.size() > 1) {

      nodes.sort(GFSMLatticeNode::compareTo);

      Iterator<GFSMLatticeNode> iterator = nodes.iterator();

      GFSMLatticeNode last = iterator.next();


      while (iterator.hasNext()) {
        GFSMLatticeNode next = iterator.next();

        if (last.getVector().equals(next.getVector())) {
          last.setFrequency(last.getFrequency() + next.getFrequency());
          last.setSupport(last.getSupport() + next.getSupport());

          if (updateIndex) {
            last.setSupporters(ArrayUtils.addAll(last.getSupporters(), next.getSupporters()));
          }

          iterator.remove();
        } else {
          last = next;
        }
      }
    }

    return nodes;
  }

  protected void setBottomNodes(List<Countable<Vector>> vectors, boolean createIndex) {
    for (int i = 0; i < vectors.size(); i++) {
      Countable<Vector> vector  = vectors.get(i);
      bottom.add(new GFSMLatticeNode(
        vector.getObject(), 0, vector.getSupport(), vector.getFrequency(), new int[0]));
    }

    aggregate(bottom, false);

    if (createIndex) {
      for(int i = 0; i < bottom.size(); i++) {
        bottom.get(i).setSupporters(new int[] {i});
      }
    }
  }

  protected List<Countable<Vector>> frequentVectors() {
    List<Countable<Vector>> vectors = Lists.newArrayList();

    for (int i = 0; i < result.size(); i++) {
      GFSMLatticeNode node = result.get(i);
      vectors.add(new Countable<Vector>(node.getVector(), node.getFrequency(), node.getSupport()));
    }

    return vectors;
  }
}
