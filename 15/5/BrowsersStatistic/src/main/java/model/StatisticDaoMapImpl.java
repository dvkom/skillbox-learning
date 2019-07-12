package model;

import javax.annotation.ManagedBean;
import javax.enterprise.inject.Default;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
@Default
public class StatisticDaoMapImpl implements StatisticDao{
  private final ConcurrentHashMap<String, Integer> storage = new ConcurrentHashMap<>();

  @Override
  public void add(String browserInfo) {
    storage.merge(browserInfo, 1, Integer::sum);
  }

  @Override
  public Map<String, Integer> getAll() {
    return storage;
  }
}
