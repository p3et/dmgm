package org.biiig.dmgm.api;

public interface LabelDictionary {
  /**
   * Encode a string symbol (e.g., label or property key)
   *
   * @param value original value
   * @return symbol
   */
  int encode(String value);

  /**
   * Decode a symbol (e.g., label or property key
   *
   * @param symbol encoded value
   * @return original value
   */
  String decode(int symbol);
}
