package org.biiig.dmgm.todo.mining;

import org.apache.commons.lang3.StringUtils;

public class IntArrayUtils {


  public static final String INDENTION = "  ";

  public static String toString(int[][][] tripleNesting) {
    String[] nestingStrings = new String[tripleNesting.length + 2];
    nestingStrings[0] = "[";
    nestingStrings[tripleNesting.length + 1] = "]";

    for (int i = 0; i < tripleNesting.length; i++) {
      String nestingString = toString(tripleNesting[i], 1);

      nestingStrings[i+1] = nestingString;
    }

    return StringUtils.join(nestingStrings, "\n");
  }

  public static String toString(int[][] nesting, int level) {

    String[] arrayStrings = new String[nesting.length];

    for (int j = 0; j < nesting.length; j++) {
      int[] array = nesting[j];

      arrayStrings[j] = toString(array);
    }

    return "[" + StringUtils.join(arrayStrings, ",") + "]";
  }

  private static String prefix(int level) {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < level; i++) {
      builder.append(INDENTION);
    }

    return builder.toString();
  }

  public static String toString(int[] array) {
    String[] strings = new String[array.length];

    for (int i = 0; i < array.length; i++) {
      strings[i] = String.valueOf(array[i]);
    }

    return "[" + StringUtils.join(strings, ",") + "]";
  }

  public static String toString(int[][] nestedArray) {
    return toString(nestedArray, 0);
  }

  public static int[][] deepCopy(int[][] orig) {
    int[][] copy = new int[orig.length][];

    for (int i = 0; i < orig.length; i++) {
      copy[i] = orig[i].clone();
    }

    return copy;
  }
}
