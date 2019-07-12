package model;

public interface FormDataStorage {
  void put(String key, String value);
  String get(String key);

}
