package model;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public interface VoteAnalyzer {
    void analyze(InputStream inputStreamToParse);

    Map<Integer, Map<String, String>> getVoteStationsSchedule();

    Set<String> getWorkDays();
}
