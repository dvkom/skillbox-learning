package model;


import entity.CreditDecision;

import javax.annotation.ManagedBean;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
public class CreditDecisionStorageImpl implements CreditDecisionStorage {
  private final ConcurrentHashMap<String, CreditDecision> decisionStorage =
      new ConcurrentHashMap<>();

  @Override
  public void put(String userId, CreditDecision creditDecision) {
    decisionStorage.put(userId, creditDecision);
  }

  @Override
  public CreditDecision get(String userId) {
    return decisionStorage.get(userId);
  }
}
