package model;

import javax.annotation.ManagedBean;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
public class FormDataStorageImpl implements FormDataStorage {
  private final ConcurrentHashMap<String, String> formDataStorage = new ConcurrentHashMap<>();

  @Override
  public void put(String key, String value) {
    formDataStorage.put(key, value);
  }

  @Override
  public String get(String key) {
    return formDataStorage.get(key);
  }
}
