package model;

import entity.TimePeriod;
import entity.WorkTime;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.annotation.ManagedBean;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@ManagedBean
public class VoteAnalyzerImpl implements VoteAnalyzer {
  private static final Logger log = Logger.getLogger(VoteAnalyzerImpl.class);
  private Map<Integer, Map<String, String>> voteStationsSchedule;
  private Set<String> workDays;

  @Override
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

  @Override
  public Map<Integer, Map<String, String>> getVoteStationsSchedule() {
    return voteStationsSchedule;
  }

  @Override
  public Set<String> getWorkDays() {
    return workDays;
  }
}
