package numberGenerators.optimized;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriterTask implements Runnable {
  private static final char[] LETTERS =
      {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
  private static final int MAX_REGION_CODE = 50;
  private static final int BUFFER_SIZE = 10_000;
  private static final String PATH_TO_SAVE = "src/main/res/numbers_optimized";
  private volatile int partNumber;

  public WriterTask(int partNumber) {
    this.partNumber = partNumber;
  }

  @Override
  public void run() {
    StringBuilder carNumbers = new StringBuilder();
    try (PrintWriter writer = new PrintWriter(PATH_TO_SAVE + "_part_" +
        partNumber + ".txt")) {
      for (int number = partNumber; number < partNumber + OptimizedNumberGenerator.STEP; number++) {
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
      System.out.println(Thread.currentThread().getName() + " done");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
