package model;

import java.util.Set;

class BlackListOfClients {
  private static final Set<String> blackList = Set.of(
      "1234123456",
      "1111222222",
      "5656909090"
      );

  static boolean contains(String passportData) {
    return blackList.contains(passportData);
  }
}
