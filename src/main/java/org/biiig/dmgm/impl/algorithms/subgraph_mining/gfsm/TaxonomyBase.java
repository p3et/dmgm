package org.biiig.dmgm.impl.algorithms.subgraph_mining.gfsm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.javaws.exceptions.InvalidArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


abstract class TaxonomyBase<T, P> implements Taxonomy<T,P> {

  private static final String SEPARATOR = ".";

  private static final String MSG_DUPLICATE = "value already part of taxonomy";
  private static final String MSG_NO_ROOT_PATH = "no path to root found";
  private static final String MSG_NO_PARENT = "parent not found";
  private static final String MSG_ROOT_PARENT = "adding a parent to root is not allowed";
  private static final String MSG_CONTAINS_SEPARATOR = "string representaion must not contain \'" + SEPARATOR + "\'";


  final T root;

  Map<T, T> childParent = Maps.newHashMap();
  private Map<T, Collection<T>> parentChildren = Maps.newHashMap();

  TaxonomyBase(T root) {
    this.root = root;
    parentChildren.put(root, Lists.newArrayList());
  }

  @Override
  public void add(T parent, T child) throws InvalidArgumentException {
    // child contains separator
    if (child.toString().contains(SEPARATOR))
      throwInvalidArgumentException(parent, MSG_CONTAINS_SEPARATOR);

    // Try to add a parent to root?
    if (child.equals(root))
      throwInvalidArgumentException(parent, MSG_ROOT_PARENT);

    // Does parent exist?
    if (!parentChildren.containsKey(parent))
      throwInvalidArgumentException(parent, MSG_NO_PARENT);

    // Does child already exist?
    if (childParent.containsKey(child))
      throwInvalidArgumentException(child, MSG_DUPLICATE);

    childParent.put(child, parent);
    parentChildren.get(parent).add(child);
    parentChildren.put(child, Lists.newArrayList());
  }

  private void throwInvalidArgumentException(T argument, String msg) throws InvalidArgumentException {
    throw new InvalidArgumentException(new String[]{argument.toString(), msg});
  }

  @Override
  public String toString() {
    return StringUtils.join(
      parentChildren
        .keySet()
        .stream()
        .map(this::getRootPathTo)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(p -> (T[]) p)
        .map(a -> StringUtils.join(a, SEPARATOR))
        .sorted()
        .collect(Collectors.toList()),
      "\n"
    );
  }

  @Override
  public T getParent(T child) {
    return childParent.get(child);
  }

  @Override
  public Collection<T> getChildren(T parent) {
    return parentChildren.get(parent);
  }
}
