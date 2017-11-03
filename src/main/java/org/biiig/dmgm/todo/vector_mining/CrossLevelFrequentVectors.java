package org.biiig.dmgm.todo.vector_mining;

import org.biiig.dmgm.todo.model.Vector;
import org.biiig.dmgm.impl.model.countable.Countable;

import java.io.Serializable;
import java.util.List;

public interface CrossLevelFrequentVectors extends Serializable {

  List<Countable<Vector>> mine(List<Countable<Vector>> vectors, int minSupport);
}
