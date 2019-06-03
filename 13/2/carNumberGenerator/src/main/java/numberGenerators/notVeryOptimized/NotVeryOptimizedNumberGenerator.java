package numberGenerators.notVeryOptimized;

import numberGenerators.Generatable;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.*;

public class NotVeryOptimizedNumberGenerator implements Generatable {
  private static final char[] LETTERS =
      {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};
  private static final int MAX_NUMBER_VALUE = 1000;
  private static final int MAX_REGION_CODE = 50;
  private static final int BUFFER_SIZE = 10_000;
  private static final String PATH_TO_SAVE = "src/main/res/numbers_notVeryOptimized.txt";
  private BlockingQueue<String> numbersQueue;

  @Override
  public void generate() {
    numbersQueue = new ArrayBlockingQueue<>(200);
    Thread producerThread = new Thread(new Producer(1, MAX_NUMBER_VALUE));
    producerThread.start();

    Thread consumerThread = new Thread(new Consumer());
    consumerThread.start();
    try {
      consumerThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  class Producer implements Runnable {
    private int start;
    private int offset;

    public Producer(int start, int offset) {
      this.start = start;
      this.offset = offset;
    }

    @Override
    public void run() {
      System.out.println("Producer started");
      StringBuilder carNumbers = new StringBuilder();
      for (int number = start; number < start + offset; number++) {
        for (int regionCode = 1; regionCode < MAX_REGION_CODE; regionCode++) {
          for (char firstLetter : LETTERS) {
            for (char secondLetter : LETTERS) {
              for (char thirdLetter : LETTERS) {
                if (carNumbers.length() > BUFFER_SIZE) {
                  try {
                    numbersQueue.put(carNumbers.toString());
                  } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                  }
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
      try {
        numbersQueue.put(carNumbers.toString());
        numbersQueue.put("end");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Producer done");
    }
  }

  class Consumer implements Runnable {

    @Override
    public void run() {
      System.out.println("Consumer started");
      try (PrintWriter writer = new PrintWriter(PATH_TO_SAVE)) {
        while (true) {
          String number = numbersQueue.take();
          if (number.equals("end")) {
            break;
          }
          writer.write(number);
        }
        System.out.println("Consumer done");
      } catch (FileNotFoundException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}
