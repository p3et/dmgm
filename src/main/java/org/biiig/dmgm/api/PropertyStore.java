package org.biiig.dmgm.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * A storage for labels and typed properties.
 */
public interface PropertyStore {



  void set(long id, int key, boolean value);
  boolean is(long id, int key);

  void set(long id, int key, int value);
  int getInt(long id, int key);

  void set(long id, int key, double value);
  double getDouble(long id, int key);

  void set(long id, int key, String value);
  String getString(long id, int key);

  void set(long id, int key, BigDecimal value);
  BigDecimal getBigDecimal(long id, int key);

  void set(long id, int key, LocalDate value);
  LocalDate getLocalDate(long id, int key);

  void set(long id, int key, int[] values);
  void add(long id, int key, int value);
  int[] getInts(long id, int key);

  void set(long id, int key, String[] values);
  void add(long id, int key, String value);
  String[] getStrings(long id, int key);


  Property[] getProperties(long id);
  Map<Long, Property[]> getAllProperties();

}
