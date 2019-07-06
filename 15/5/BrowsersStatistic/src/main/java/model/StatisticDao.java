package model;

import java.util.Map;

public interface StatisticDao {
  void add(String browserInfo);
  Map<String, Integer> getAll();
}
