package model;

import entity.VoteStationSchedule;
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

@ManagedBean
public class VoteAnalyzerImpl implements VoteAnalyzer {
  private static final Logger log = Logger.getLogger(VoteAnalyzerImpl.class);

  @Override
  public VoteStationSchedule analyze(InputStream inputStreamToParse) {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      SAXParser parser = factory.newSAXParser();
      XmlTagHandler handler = new XmlTagHandler();
      parser.parse(inputStreamToParse, handler);
      HashMap<Integer, WorkTime> voteStationWorkTimes = handler.getVoteStationWorkTimes();

      return VoteStationSchedule.buildSchedule(voteStationWorkTimes);
    } catch (ParserConfigurationException | IOException | SAXException e) {
      log.error(e);
      return VoteStationSchedule.emptySchedule();
    }
  }
}
