package model;

import javax.annotation.ManagedBean;
import javax.enterprise.inject.Alternative;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@Alternative
public class StatisticDaoMapImpl implements StatisticDao{
  private ConcurrentHashMap<String, Integer> storage = new ConcurrentHashMap<>();

  @Override
  public void add(String browserInfo) {
    if (storage.containsKey(browserInfo)) {
      int currentCount = storage.get(browserInfo);
      storage.put(browserInfo, currentCount + 1);
    } else {
      storage.put(browserInfo, 1);
    }
  }

  @Override
  public Map<String, Integer> getAll() {
    return storage;
  }
}
