package model;

import entity.CreditRequest;

import javax.annotation.ManagedBean;

@ManagedBean
public class CreditRequestStorage implements SingleObjectStorage<CreditRequest> {
  private volatile CreditRequest creditRequest;

  @Override
  public synchronized void save(CreditRequest object) {
    creditRequest = object;
  }

  @Override
  public synchronized CreditRequest get() {
    return creditRequest;
  }
}
