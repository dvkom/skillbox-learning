package model;

import entity.TimePeriod;
import entity.WorkTime;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class VoteAnalyzer {
  private static final Logger log = Logger.getLogger(VoteAnalyzer.class);
  private Map<Integer, Map<String, String>> voteStationsSchedule;
  private Set<String> workDays;

  public void analyze(InputStream inputStreamToParse) {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      SAXParser parser = factory.newSAXParser();
      XmlTagHandler handler = new XmlTagHandler();
      parser.parse(inputStreamToParse, handler);
      HashMap<Integer, WorkTime> voteStationWorkTimes = handler.getVoteStationWorkTimes();

      voteStationsSchedule = voteStationWorkTimes.entrySet().stream()
          .collect(Collectors.toMap(Map.Entry::getKey,
              e -> e.getValue().getPeriods().stream()
                  .collect(Collectors.toMap(TimePeriod::getDate, TimePeriod::getTimePeriod)))
          );
      workDays = new TreeSet<>();
      voteStationsSchedule.values().forEach(entry -> workDays.addAll(entry.keySet()));
    } catch (ParserConfigurationException | IOException | SAXException e) {
      log.error(e);
    }
  }

  public Map<Integer, Map<String, String>> getVoteStationsSchedule() {
    return voteStationsSchedule;
  }

  public Set<String> getWorkDays() {
    return workDays;
  }
}
