package utils;

import dao.VoterDao;
import entity.WorkTime;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Handler extends DefaultHandler {
  private static final Logger log = Logger.getLogger(Handler.class);
  private static SimpleDateFormat visitDateFormat =
      new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

  private HashMap<Integer, WorkTime> voteStationWorkTimes = new HashMap<>();
  private VoterDao voterDao = new VoterDao();

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    if (qName.equals("voter")) {
      String name = attributes.getValue("name");
      String birthDay = attributes.getValue("birthDay");
      try {
        voterDao.countVoter(name, birthDay);
      } catch (SQLException e) {
        log.error(e);
      }

    }/* else if (qName.equals("visit")) {
      Integer station = Integer.parseInt(attributes.getValue("station"));
      Date time = null;
      try {
        time = visitDateFormat.parse(attributes.getValue("time"));
      } catch (ParseException e) {
        log.error(e);
      }
      WorkTime workTime = voteStationWorkTimes.get(station);
      if (workTime == null) {
        workTime = new WorkTime();
        voteStationWorkTimes.put(station, workTime);
      }
      if (time != null) {
        workTime.addVisitTime(time.getTime());
      }
    }*/
  }

  @Override
  public void endDocument() {
    try {
      voterDao.flushCounter();
    } catch (SQLException e) {
      log.error(e);
    }
  }

  public void printResults() {
//    System.out.println("Voting station work times: ");
//    for (Integer votingStation : voteStationWorkTimes.keySet()) {
//      WorkTime workTime = voteStationWorkTimes.get(votingStation);
//      System.out.println("\t" + votingStation + " - " + workTime);
//    }

    System.out.println("Duplicated voters: ");
    try {
      voterDao.printVoterCounts();
    } catch (SQLException e) {
      log.error(e);
    }
  }
}
