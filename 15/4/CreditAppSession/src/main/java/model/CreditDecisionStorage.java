package model;

import entity.CreditDecision;

public interface CreditDecisionStorage {
  void put(String sessionId, CreditDecision creditDecision);
  CreditDecision get(String sessionId);
}
