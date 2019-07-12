package model;


import entity.CreditDecision;

public interface CreditDecisionStorage {
  void put(String userId, CreditDecision creditDecision);
  CreditDecision get(String userId);
}
