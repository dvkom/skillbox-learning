package model;

import entity.CreditRequest;

public interface DecisionEngine {
  boolean decide(CreditRequest creditRequest);
}
