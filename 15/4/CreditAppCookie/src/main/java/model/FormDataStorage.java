package model;

public interface FormDataStorage {
  void put(String key, String value);
  boolean containsKey(String key);
  String get(String key);

}
