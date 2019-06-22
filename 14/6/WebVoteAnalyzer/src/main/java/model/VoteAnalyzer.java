package model;

import entity.VoteStationSchedule;

import java.io.InputStream;

public interface VoteAnalyzer {
    VoteStationSchedule analyze(InputStream inputStreamToParse);
}
