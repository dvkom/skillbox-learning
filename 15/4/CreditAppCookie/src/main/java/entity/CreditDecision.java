package entity;

public class CreditDecision {

  private final CreditRequest creditRequest;
  private final boolean isApproved;

  public CreditDecision(CreditRequest creditRequest, boolean isApproved) {
    this.creditRequest = creditRequest;
    this.isApproved = isApproved;
  }

  public CreditRequest getCreditRequest() {
    return creditRequest;
  }

  public boolean isApproved() {
    return isApproved;
  }
}
