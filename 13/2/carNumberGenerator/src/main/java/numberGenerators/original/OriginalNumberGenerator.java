package numberGenerators.original;
/**
 * Original not optimized version from skillbox
 */

import numberGenerators.Generatable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OriginalNumberGenerator implements Generatable {
  private static final char[] LETTERS =
      {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
  private static final int MAX_NUMBER_VALUE = 1000;
  private static final int MAX_REGION_CODE = 50;
  private static final int BUFFER_SIZE = 10_000_000;
  private static final String PATH_TO_SAVE = "src/main/res/numbers_original.txt";

  @Override
  public void generate() {
    StringBuilder carNumbers = new StringBuilder();
    try (PrintWriter writer = new PrintWriter(PATH_TO_SAVE)) {
      for (int number = 1; number < MAX_NUMBER_VALUE; number++) {
        for (int regionCode = 1; regionCode < MAX_REGION_CODE; regionCode++) {
          for (char firstLetter : LETTERS) {
            for (char secondLetter : LETTERS) {
              for (char thirdLetter : LETTERS) {
                if (carNumbers.length() > BUFFER_SIZE) {
                  writer.write(carNumbers.toString());
                  carNumbers = new StringBuilder();
                }
                carNumbers.append(firstLetter);
                if (number < 10) {
                  carNumbers.append("00");
                } else if (number < 100) {
                  carNumbers.append("0");
                }
                carNumbers.append(number)
                    .append(secondLetter)
                    .append(thirdLetter);
                if (regionCode < 10) {
                  carNumbers.append("0");
                }
                carNumbers.append(regionCode)
                    .append("\n");
              }
            }
          }
        }
      }
      writer.write(carNumbers.toString());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}
