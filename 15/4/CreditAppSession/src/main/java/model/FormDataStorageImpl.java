package model;

import javax.annotation.ManagedBean;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
public class FormDataStorageImpl implements FormDataStorage {
  private volatile ConcurrentHashMap<String, String> formDataStorage = new ConcurrentHashMap<>();

  @Override
  public void put(String key, String value) {
    formDataStorage.put(key, value);
  }

  @Override
  public boolean containsKey(String key) {
    return formDataStorage.containsKey(key);
  }

  @Override
  public String get(String key) {
    return formDataStorage.get(key);
  }
}
