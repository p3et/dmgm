package org.biiig.dmgm.api;

public class Property {
  private final int key;
  private final Object value;

  public Property(int key, Object value) {
    this.key = key;
    this.value = value;
  }

  public int getKey() {
    return key;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public String toString() {
    return key + ":" + value;
  }
}
