import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ViewerModel implements Model {
  private static final Logger log = Logger.getLogger(ViewerModel.class);
  private volatile String pathToFile;
  private static final int DEFAULT_BUFFER_OF_LINES_SIZE = 33;
  private static final int INDEX_BUFFER = 30_000;
  private int bufferOfLinesSize;
  private long fileLength;
  private long currentPointer;
  private int linesCount;
  private Map<Integer, Long> indexedPointers;

  @Override
  public void setPathToFile(Path pathToFile) {
    this.pathToFile = pathToFile.toString();
  }

  @Override
  public String openFile() {
    currentPointer = 0;
    indexedPointers = new HashMap<>();

    log.info("First open file " + pathToFile);
    try (RandomAccessFile file = new RandomAccessFile(pathToFile, "r")) {
      fileLength = file.length();
      log.info("Length of file = " + fileLength);
      log.info(pathToFile + " has been opened");
    } catch (IOException e) {
      log.error("Error opening file " + pathToFile, e);
    }
    indexedPointers.put(0, 0L);

    linesCount = countAndIndexLines();
    log.info("Number of lines = " + linesCount);
    if (linesCount == 0) {
      return null;
    }

    bufferOfLinesSize = linesCount < DEFAULT_BUFFER_OF_LINES_SIZE ?
        linesCount :
        DEFAULT_BUFFER_OF_LINES_SIZE;

    return readSnippet(0);
  }


  @Override
  public String readSnippet(int lineNumber) {
    currentPointer = findPointerByLineNumber(lineNumber);
    StringBuilder result = new StringBuilder();

    if (currentPointer < fileLength - 1) {
      try (RandomAccessFile file = new RandomAccessFile(pathToFile, "r")) {
        file.seek(currentPointer);
        int counter = 0;

        while (counter < bufferOfLinesSize) {
          String line = file.readLine();
          if (line != null) {
            result.append(new String(line.getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8)).append('\n');
            counter++;
          } else {
            break;
          }
        }
        result.deleteCharAt(result.length() - 1);

        log.info("Current pointer in file = " + file.getFilePointer());
        return result.toString();
      } catch (IOException e) {
        log.error("Error reading file " + pathToFile, e);
        return null;
      }
    }

    log.info("End of file " + pathToFile);
    return null;
  }

  @Override
  public int getLinesCount() {
    return linesCount;
  }

  private long findPointerByLineNumber(int lineNumber) {
    try (InputStream is = new BufferedInputStream(new FileInputStream(pathToFile))) {
      int lineIndex = lineNumber / INDEX_BUFFER;
      long pointer = indexedPointers.get(lineIndex);

      if (lineNumber % INDEX_BUFFER == 0) {
        return pointer;
      }

      final int BUFFER_SIZE = 1024;
      byte[] c = new byte[BUFFER_SIZE];

      is.skip(pointer);

      int readChars = is.read(c);
      if (readChars == -1) {
        return pointer;
      }

      int count = lineIndex * INDEX_BUFFER;
      while (readChars == BUFFER_SIZE) {
        for (int i = 0; i < BUFFER_SIZE; i++) {
          if (c[i] == '\n') {
            count++;
            if (count == lineNumber) {
              return pointer + i + 1;
            }
          }
        }
        pointer += BUFFER_SIZE;
        readChars = is.read(c);
      }

      while (readChars != -1) {
        for (int i = 0; i < readChars; i++) {
          if (c[i] == '\n') {
            count++;
            if (count == lineNumber) {
              return pointer + i;
            }
          }
        }
        pointer += readChars;
        readChars = is.read(c);
      }
      return pointer;
    } catch (IOException e) {
      log.error(e);
      return 0;
    }
  }

  private int countAndIndexLines() {
    try (InputStream is = new BufferedInputStream(new FileInputStream(pathToFile))) {
      long pointer = 0;
      final int BUFFER_SIZE = 1024;
      byte[] c = new byte[BUFFER_SIZE];

      int readChars = is.read(c);
      if (readChars == -1) {
        return 0;
      }

      int count = 0;
      while (readChars == BUFFER_SIZE) {
        for (int i = 0; i < BUFFER_SIZE; i++) {
          if (c[i] == '\n') {
            count++;
            if (count % INDEX_BUFFER == 0) {
              indexedPointers.put(count / INDEX_BUFFER, pointer + i + 1);
            }
          }
        }
        pointer += BUFFER_SIZE;
        readChars = is.read(c);
      }

      while (readChars != -1) {
        for (int i = 0; i < readChars; i++) {
          pointer++;
          if (c[i] == '\n') {
            count++;
            if (count % INDEX_BUFFER == 0) {
              indexedPointers.put(count / INDEX_BUFFER, pointer + i);
            }
          }
        }
        pointer += readChars;
        readChars = is.read(c);
      }

      return count == 0 ? 1 : count;
    } catch (IOException e) {
      log.error(e);
      return 0;
    }
  }
}
