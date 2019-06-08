import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import utils.Handler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class VoteAnalyzer {
  private static final Logger log = Logger.getLogger(VoteAnalyzer.class);
  private String fileName;

  public VoteAnalyzer(String fileName) {
    this.fileName = fileName;
  }

  public void startAnalyze() {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    try {
      SAXParser parser = factory.newSAXParser();
      Handler handler = new Handler();
      parser.parse(new File(fileName), handler);
      handler.printResults();
    } catch (ParserConfigurationException | IOException | SAXException e) {
      log.error(e);
    }
  }
}
