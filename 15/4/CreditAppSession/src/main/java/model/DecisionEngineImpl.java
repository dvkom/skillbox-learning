package model;

import entity.CreditRequest;

import javax.annotation.ManagedBean;
import java.time.LocalDate;

@ManagedBean
public class DecisionEngineImpl implements DecisionEngine {
  private static final int MINIMUM_AGE = 18;
  private static final int MAXIMUM_AGE = 80;
  private static final int CREDIT_REDUCTION_RATE = 200_000;
  private static final int TERM_REDUCTION_RATE = 36;

  @Override
  public boolean decide(CreditRequest creditRequest) {
    if (LocalDate.now().minusYears(MINIMUM_AGE).isBefore(creditRequest.getBirthday())) {
      return false;
    }
    if (LocalDate.now().minusYears(MAXIMUM_AGE).isAfter(creditRequest.getBirthday())) {
      return false;
    }
    if (!creditRequest.getIsPrivacyAccepted()) {
      return false;
    }
    if (LocalDate.now().isBefore(creditRequest.getPassportDateOfIssue())) {
      return false;
    }
    if (BlackListOfClients.contains(creditRequest.getPassportSeries() +
        creditRequest.getPassportNumber())) {
      return false;
    }

    int score = 25;
    if (creditRequest.getIsTrustedClient()) {
      score += 15;
    }
    if (creditRequest.getIsSalaryProject()) {
      score += 15;
    }
    if (creditRequest.getIsRetiree()) {
      score += 5;
    }
    score -= creditRequest.getCreditSum() / CREDIT_REDUCTION_RATE;
    score -= creditRequest.getCreditTerm() / TERM_REDUCTION_RATE;

    return score >= 25;
  }
}
