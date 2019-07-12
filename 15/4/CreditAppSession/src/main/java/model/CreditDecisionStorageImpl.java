package model;

import entity.CreditDecision;

import javax.annotation.ManagedBean;
import java.util.concurrent.ConcurrentHashMap;

@ManagedBean
public class CreditDecisionStorageImpl implements CreditDecisionStorage {
  private final ConcurrentHashMap<String, CreditDecision> decisionStorage =
      new ConcurrentHashMap<>();

  @Override
  public void put(String sessionId, CreditDecision creditDecision) {
    decisionStorage.put(sessionId, creditDecision);
  }

  @Override
  public CreditDecision get(String sessionId) {
    return decisionStorage.get(sessionId);
  }
}
