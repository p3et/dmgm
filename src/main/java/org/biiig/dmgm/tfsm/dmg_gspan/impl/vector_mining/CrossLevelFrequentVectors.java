package org.biiig.dmgm.tfsm.dmg_gspan.impl.vector_mining;

import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.Vector;
import org.biiig.dmgm.tfsm.dmg_gspan.impl.model.countable.Countable;

import java.io.Serializable;
import java.util.List;

public interface CrossLevelFrequentVectors extends Serializable {

  List<Countable<Vector>> mine(List<Countable<Vector>> vectors, int minSupport);
}
